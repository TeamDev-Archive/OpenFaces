/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
O$.Slider = {
  _init: function(sl_sliderId,
                  sl_value,
                  sl_minValue,
                  sl_maxValue,
                  sl_minorTickSpacing,
                  sl_majorTickSpacing,
                  sl_orientation,
                  sl_fillDirection,
                  //   sl_ticksAlignment,
                  //  sl_isDefaultStyling,
                  sl_isDisabled,
                  sl_tooltipEnabled,
                  sl_barCanChange,
                  sl_barIsVisible,
                  sl_textFieldState,
                  sl_snapToTicks,
                  sl_onchange,
                  sl_onchanging,
                  sl_textFieldStyle,
                  sl_toolTipStyle,
                  sl_toolTipIsInteger,
                  sl_styleClass,
                  focusedClass,

                  rolloverAEClass,
                  sl_autoRepeatDelay,

                  formatOptions,
                  sl_transitionPeriod,

                  handleImageUrl,
                  handleRolloverImageUrl,
                  LTButtonImageUrl,
                  LTButtonRolloverImageUrl,

                  RBButtonImageUrl,
                  RBButtonRolloverImageUrl) {
    var slider = O$.initComponent(sl_sliderId, {focused: focusedClass}, {

      s_rightBottomButton : O$.byIdOrName(sl_sliderId + "::rightBottomButton"),
      s_leftTopButton : O$.byIdOrName(sl_sliderId + "::leftTopButton"),
      s_handle : O$.byIdOrName(sl_sliderId + "::handle"),
      s_bar : O$.byIdOrName(sl_sliderId + "::bar"),
      s_ticksLT : O$.byIdOrName(sl_sliderId + "::ticksLT"),
      s_ticksRB : O$.byIdOrName(sl_sliderId + "::ticksRB"),
      s_textField : O$.byIdOrName(sl_sliderId + "::textField"),
      s_workspace : O$.byIdOrName(sl_sliderId + "::workspace"),

      s_pixelStep : 0,
      s_widthCorrection :0,
      ondragstart: O$.cancelEvent,
      onselectstart: O$.cancelEvent,

      getValue: function() {
        if (!slider.s_textField)
          return null;
        var value = O$.Dojo.Number.parse(slider.s_textField.value, formatOptions);

        return !isNaN(value) ? value : null;
      },

      setValue: function(value) {
        var prevValue = slider.getValue();
        var newValue = value == null || isNaN(value)
                ? prevValue
                : value;
        if ((sl_minValue != null && newValue > sl_minValue) &&
                (sl_maxValue != null && newValue < sl_maxValue)) {
          newValue = (sl_snapToTicks) ? Math.round(newValue / sl_minorTickSpacing) * sl_minorTickSpacing :
                  newValue;
        } else if (sl_maxValue != null && newValue >= sl_maxValue) {
          newValue = sl_maxValue;
        } else if (sl_minValue != null && newValue <= sl_minValue) {
          newValue = sl_minValue;
        }
        if (newValue != prevValue) {
          slider.s_textField.value = O$.Dojo.Number.format(newValue, formatOptions);
        }
      },

      getValueToDisplay: function() {
        if (sl_orientation == "horizontal")
          return (sl_fillDirection == "fromStart") ? slider.getValue() : (sl_maxValue - slider.getValue() + sl_minValue);
        else
          return (sl_fillDirection != "fromStart") ? slider.getValue() : (sl_maxValue - slider.getValue() + sl_minValue);
      },

      increaseValue: function() {
        var value = slider.getValue();
        if (sl_maxValue == null || value + sl_minorTickSpacing <= sl_maxValue) {
          slider.setValue(value + sl_minorTickSpacing);
        } else {
          slider.setValue(sl_maxValue);
        }
        if (value != slider.getValue()) {
          notifyOfInputChanges(slider);
          notifyOfInputChanging(slider);
        }
      },

      decreaseValue: function() {
        var value = slider.getValue();
        if (sl_minValue == null || value - sl_minorTickSpacing >= sl_minValue) {
          slider.setValue(value - sl_minorTickSpacing, true);
        } else {
          slider.setValue(sl_minValue);
        }
        if (value != slider.getValue()) {
          notifyOfInputChanges(slider);
          notifyOfInputChanging(slider);
        }
      },

      _rightBottomButtonClick: function(e) {
        if (sl_orientation == "horizontal") {
          if (sl_fillDirection == "fromStart") {
            slider.increaseValue();
          } else {
            slider.decreaseValue();
          }
        } else {
          if (sl_fillDirection == "fromStart") {
            slider.decreaseValue();
          } else {
            slider.increaseValue();
          }
        }
        slider.redisplayHandle();
        O$.cancelEvent(e);
      },

      _leftTopButtonClick:function(e) {
        if (sl_orientation == "horizontal") {
          if (sl_fillDirection == "fromStart") {
            slider.decreaseValue();
          } else {
            slider.increaseValue();
          }
        } else {
          if (sl_fillDirection == "fromStart") {
            slider.increaseValue();
          } else {
            slider.decreaseValue();
          }
        }
        slider.redisplayHandle();
        O$.cancelEvent(e);
      },

      getElementDragDirectionSize: function(element) {
        return element == null ? 0 : (
                sl_orientation == "horizontal" ?
                        O$.getElementSize(element).width :
                        O$.getElementSize(element).height);
      },

      getElementNonDragDirectionSize: function(element) {
        return element == null ? 0 : (
                sl_orientation == "horizontal" ?
                        O$.getElementSize(element).height :
                        O$.getElementSize(element).width);
      },

      getElementPaddingValues : function(element) {
        return {top : O$.getNumericElementStyle(element, "padding-top"),
          bottom : O$.getNumericElementStyle(element, "padding-bottom"),
          right : O$.getNumericElementStyle(element, "padding-right"),
          left : O$.getNumericElementStyle(element, "padding-left")
        }
      },

      getWorkSpaceDragDirectionSize: function() {
        var isHor = sl_orientation == "horizontal";
        var result;
        if (isHor) {
          result = O$.calculateNumericCSSValue(O$.getStyleClassProperty(sl_styleClass, "width"));
        } else {
          result = O$.calculateNumericCSSValue(O$.getStyleClassProperty(sl_styleClass, "height"));
        }

        var padding = slider.getElementPaddingValues(O$(sl_sliderId));
        if (O$.isExplorer() || O$.isOpera()) {
          result = result - (isHor ? (padding.left + padding.right) : (padding.top + padding.bottom));
        }
        var cur = slider.getElementDragDirectionSize(O$(sl_sliderId + "::leftTopButton"));
        if (cur != 0) {
          padding = slider.getElementPaddingValues(O$.getParentNode(O$(sl_sliderId + "::leftTopButton"), "td"));
          result = result - cur - (isHor ? (padding.left + padding.right) : (padding.top + padding.bottom));
          result = result + (O$.isExplorer6() || (O$.isExplorer() && O$.isQuirksMode()) ? 2 : 0);
        }
        cur = slider.getElementDragDirectionSize(O$(sl_sliderId + "::rightBottomButton"));
        if (cur != 0) {
          padding = slider.getElementPaddingValues(O$.getParentNode(O$(sl_sliderId + "::rightBottomButton"), "td"));
          result = result - cur - (isHor ? (padding.left + padding.right) : (padding.top + padding.bottom));
          result = result + (O$.isExplorer6() || (O$.isExplorer() && O$.isQuirksMode()) ? 2 : 0);
        }
        cur = slider.getElementDragDirectionSize(O$(sl_sliderId + "::textField"));
        if (cur != 0) {
          padding = slider.getElementPaddingValues(O$.getParentNode(O$(sl_sliderId + "::textField"), "td"));
          result = result - cur - (isHor ? (padding.left + padding.right) : (padding.top + padding.bottom));
          result = result + (O$.isExplorer6() || (O$.isExplorer() && O$.isQuirksMode()) ? 2 : 0);
        }
        return result;
      },

      getTicksAmount: function() {
        return Math.round((sl_maxValue - sl_minValue) / sl_minorTickSpacing) + 1;
      },

      getPixelStep : function() {
        return slider.s_pixelStep;
      },

      setPixelStep : function() {
        this.s_pixelStep = Math.floor(slider.getWorkSpaceDragDirectionSize() / slider.getTicksAmount());
      },

      redisplayHandle: function () {

        var s_handleValueOffset = Math.floor(Math.abs(slider.getValueToDisplay() - sl_minValue) / sl_minorTickSpacing * slider.s_pixelStep);
        if (sl_orientation == "horizontal") {
          O$.runTransitionEffect(slider.s_handle, "marginLeft", s_handleValueOffset + "px", sl_transitionPeriod, 10);
        } else {
          O$.runTransitionEffect(slider.s_handle, "marginTop", s_handleValueOffset + "px", sl_transitionPeriod, 10);
        }
      },

      getMaxTickDragDirectionSize: function() {
        var max = 0;
        var s_aTick;
        var tickSize;
        for (var counterTicks = 0; counterTicks < slider.getTicksAmount(); counterTicks++) {
          if (O$.byIdOrName(sl_sliderId + "::ticksLT") != null) {
            s_aTick = O$.byIdOrName(sl_sliderId + "::ticksLT::tick" + counterTicks);
          } else {
            s_aTick = O$.byIdOrName(sl_sliderId + "::ticksRB::tick" + counterTicks);
          }
          tickSize = O$.getElementSize(s_aTick);
          if (sl_orientation == "horizontal") {
            if (tickSize.width > max) {
              max = tickSize.width;
            }
          } else {
            if (tickSize.height > max) {
              max = tickSize.height;
            }
          }
        }
        return max;
      },

      getMaxTickNonDragDirectionSize: function() {
        var maxTickSize = -1;
        var curMax, text,img;
        for (var c = 0; c <= slider.getTicksAmount(); c++) {
          if (O$(sl_sliderId + "::ticksLT") != null) {
            img = O$.byIdOrName(sl_sliderId + "::ticksLT::tick" + c + "::image");
            text = O$.byIdOrName(sl_sliderId + "::ticksLT::tick" + c + "::text");
          } else {
            img = O$.byIdOrName(sl_sliderId + "::ticksRB::tick" + c + "::image");
            text = O$.byIdOrName(sl_sliderId + "::ticksRB::tick" + c + "::text");
          }
          if (sl_orientation == "horizontal") {
            curMax = (img == null ? 0 : O$.getElementSize(img).height) +
                    (text == null ? 0 : O$.getElementSize(text).height);
          } else {
            curMax = (img == null ? 0 : O$.getElementSize(img).width) +
                    (text == null ? 0 : O$.getElementSize(text).width);
          }
          if (curMax > maxTickSize) {
            maxTickSize = curMax;
          }
        }
        return maxTickSize;
      },

      getElementNonDragOffset: function(id) {
        var element = O$.byIdOrName(id);
        if (element == null)
          return 0;
        var barPos = O$.getElementPos(O$(sl_sliderId + "::bar"));
        var elPos = O$.getElementPos(element);
        var barSize = slider.getElementNonDragDirectionSize(O$(sl_sliderId + "::bar"));
        var elSize = slider.getElementNonDragDirectionSize(element);

        var offset = sl_orientation == "horizontal" ? barPos.y - elPos.y : barPos.x - elPos.x;
        offset = offset + (barSize - elSize) / 2;

        return Math.round(offset);
      },

      redraw : function(orientation) {

        if (slider.s_leftTopButton != null) {
          O$.setElementSize(slider.s_leftTopButton, O$.getElementSize(O$(sl_sliderId + "::leftTopButton::image")));
          slider.s_widthCorrection = slider.s_widthCorrection +
                  (O$.isExplorer6() || (O$.isExplorer() && O$.isQuirksMode()) ? 2 : 0);
        }
        if (slider.s_rightBottomButton != null) {
          O$.setElementSize(slider.s_rightBottomButton, O$.getElementSize(O$(sl_sliderId + "::rightBottomButton::image")));
          slider.s_widthCorrection = slider.s_widthCorrection +
                  (O$.isExplorer6() || (O$.isExplorer() && O$.isQuirksMode()) ? 2 : 0);
        }
        if (sl_textFieldState != "off") {
          slider.s_widthCorrection = slider.s_widthCorrection +
                  (O$.isExplorer6() || (O$.isExplorer() && O$.isQuirksMode()) ? 2 : 0);
        }
        slider.setPixelStep();

        var maxTickSize = slider.getMaxTickNonDragDirectionSize();
        var s_handleValueOffset = Math.floor(Math.abs(slider.getValueToDisplay() - sl_minValue) / sl_minorTickSpacing * slider.s_pixelStep);
        var s_aTickLT, s_aTickRB;
        var text;
        var image;
        var sizeText;
        var size;
        var handleImg = O$(sl_sliderId + "::handle::image");
        var handleImgSizeW = O$.getElementSize(handleImg).width;
        var handleImgSizeH = O$.getElementSize(handleImg).height;
        var counterTicks;

        if (orientation == "horizontal") {

          O$.setElementWidth(slider.s_handle, slider.s_pixelStep);
          O$.setElementHeight(slider.s_handle, handleImgSizeH);
          if (slider.s_ticksLT != null) {
            O$.setElementSize(slider.s_ticksLT, {
              width: slider.s_pixelStep * slider.getTicksAmount() + slider.s_widthCorrection,
              height: maxTickSize});
          }

          if (slider.s_ticksRB != null) {
            O$.setElementSize(slider.s_ticksRB, {
              width: slider.s_pixelStep * slider.getTicksAmount() + slider.s_widthCorrection,
              height: maxTickSize});
          }
          O$.setElementWidth(O$.getParentNode(slider.s_workspace, "td"), slider.s_pixelStep * slider.getTicksAmount() + slider.s_widthCorrection);
          O$.setElementWidth(slider.s_workspace, slider.s_pixelStep * slider.getTicksAmount() + slider.s_widthCorrection);
          O$.setElementWidth(slider.s_bar, slider.s_pixelStep * slider.getTicksAmount());
          O$.setElementHeight(slider.s_bar, O$.getElementSize(O$(sl_sliderId + "::bar::image")).height);
          var tempMarginLeft;
          if (O$(sl_sliderId + "::ticksLT::tick0") != null || O$(sl_sliderId + "::ticksRB::tick0") != null) {
            for (counterTicks = 0; counterTicks < slider.getTicksAmount(); counterTicks++) {
              s_aTickLT = O$(sl_sliderId + "::ticksLT::tick" + counterTicks);
              image = O$(sl_sliderId + "::ticksLT::tick" + counterTicks + "::image");
              text = O$(sl_sliderId + "::ticksLT::tick" + counterTicks + "::text");
              if (slider.s_ticksLT != null && s_aTickLT != null) {
                O$.setElementWidth(s_aTickLT, slider.s_pixelStep);
                O$.setElementHeight(s_aTickLT, maxTickSize);
                if (image != null) {
                  image.style.marginTop = maxTickSize - O$.getElementSize(image).height + "px";
                  image.style.marginLeft = Math.floor((slider.s_pixelStep - O$.getElementSize(image).width) / 2) -
                          (slider.isCorrectionNeeded(slider.s_pixelStep, O$.getElementSize(image).width) ? 1 : 0) + "px";
                }

                if (text != null) {
                  sizeText = O$.getElementSize(text).width;
                  size = O$.getElementSize(s_aTickLT).width;
                  tempMarginLeft = Math.floor((size - sizeText) / 2);
                  if (image != null) {
                    tempMarginLeft -= slider.isCorrectionNeeded(slider.s_pixelStep, O$.getElementSize(image).width) ? 1 : 0;
                    text.style.marginTop = Math.floor(O$.getElementSize(text).height * 3 / 2) - maxTickSize + "px";
                  } else {
                    text.style.marginTop = Math.floor(O$.getElementSize(text).height * 3 / 2) - maxTickSize * 2 + "px";
                  }
                  text.style.marginLeft = tempMarginLeft + "px";
                }
              }

              s_aTickRB = O$(sl_sliderId + "::ticksRB::tick" + counterTicks);
              image = O$(sl_sliderId + "::ticksRB::tick" + counterTicks + "::image");
              text = O$(sl_sliderId + "::ticksRB::tick" + counterTicks + "::text");

              if (slider.s_ticksRB != null && s_aTickRB != null) {
                O$.setElementWidth(s_aTickRB, slider.s_pixelStep);
                O$.setElementHeight(s_aTickRB, maxTickSize);
                if (image != null) {
                  image.style.marginBottom = maxTickSize - O$.getElementSize(image).height + "px";
                  image.style.marginLeft = Math.floor((slider.s_pixelStep - O$.getElementSize(image).width) / 2) -
                          (slider.isCorrectionNeeded(slider.s_pixelStep, O$.getElementSize(image).width) ? 1 : 0) + "px";
                }
                if (text != null) {
                  sizeText = O$.getElementSize(text).width;
                  size = O$.getElementSize(s_aTickRB).width;
                  tempMarginLeft = Math.floor((size - sizeText) / 2);
                  if (image != null) {
                    tempMarginLeft -= slider.isCorrectionNeeded(slider.s_pixelStep, O$.getElementSize(image).width) ? 1 : 0;
                    text.style.marginTop = maxTickSize - Math.floor(O$.getElementSize(text).height / 2) + "px";
                  } else {
                    text.style.marginTop = maxTickSize + "px";
                  }
                  text.style.marginLeft = tempMarginLeft + "px";
                }
              }
            }
          }
          if (O$.isMozillaFF() || O$.isChrome() || O$.isSafari()) {
            slider.s_handle.style.marginTop = -1 + slider.getElementNonDragOffset(sl_sliderId + "::handle") + "px";
          }
          if (O$.isOpera()) {
            slider.s_handle.style.marginTop = -2 + slider.getElementNonDragOffset(sl_sliderId + "::handle") + "px";
          }
          if (O$.isExplorer6() || O$.isExplorer7()) {
            slider.s_handle.style.marginTop = (O$.isQuirksMode() ? 4 : -1) + slider.getElementNonDragOffset(sl_sliderId + "::handle") + "px";
          }
          if (O$.isExplorer8()) {
            slider.s_handle.style.marginTop = (!O$.isQuirksMode() ? -5 : 4) + slider.getElementNonDragOffset(sl_sliderId + "::handle") + "px";
          }

          slider.s_handle.style.marginLeft = s_handleValueOffset + "px";

          if (slider.s_leftTopButton != null) {
            slider.s_leftTopButton.style.marginTop = slider.getElementNonDragOffset(sl_sliderId + "::leftTopButton") + "px";
          }

          if (slider.s_rightBottomButton != null) {
            slider.s_rightBottomButton.style.marginTop = slider.getElementNonDragOffset(sl_sliderId + "::rightBottomButton") + "px";
          }

          handleImg.style.marginLeft = Math.floor((slider.s_pixelStep - handleImgSizeW) / 2) -
                  (slider.isCorrectionNeeded(slider.s_pixelStep, handleImgSizeW) ? 1 : 0) + "px";

          if (O$.isMozillaFF() || O$.isSafari() || O$.isChrome()) {
            handleImg.style.marginTop = "1px";
          }
          if (O$.isOpera()) {
            handleImg.style.marginTop = "2px";
          }

          if ((O$.isExplorer6() || O$.isExplorer7())) {
            if (O$.isQuirksMode())
              handleImg.style.marginTop = "-4px";
            else
              handleImg.style.marginTop = "1px";
          }
          if (O$.isExplorer8()) {
            if (O$.isQuirksMode()) {
              handleImg.style.marginTop = "-4px";
            }
            else
              handleImg.style.marginTop = "5px";
          }
        }
        else { //vertical case
          O$.setElementHeight(slider.s_handle, slider.s_pixelStep);
          O$.setElementWidth(slider.s_handle, handleImgSizeW);
          if (slider.s_ticksLT != null) {
            O$.setElementSize(slider.s_ticksLT, { height: slider.s_pixelStep * slider.getTicksAmount(),width: maxTickSize});
          }
          if (slider.s_ticksRB != null) {
            O$.setElementSize(slider.s_ticksRB, { height: slider.s_pixelStep * slider.getTicksAmount(),width: maxTickSize});
          }

          O$.setElementHeight(slider.s_workspace, slider.s_pixelStep * slider.getTicksAmount() - (O$.isExplorer() && O$.isStrictMode() ? Math.round(slider.s_pixelStep / 2) + 1 : 0));

          var workspaceOffset = slider.getElementNonDragDirectionSize(slider.s_workspace);
          workspaceOffset = slider.getElementNonDragDirectionSize(O$.getParentNode(slider.s_workspace, "td")) - workspaceOffset;
          workspaceOffset = Math.floor(workspaceOffset / 2);
          O$.getParentNode(slider.s_workspace, "td").style.paddingLeft = workspaceOffset + "px";

          O$.setElementHeight(slider.s_bar, slider.s_pixelStep * slider.getTicksAmount() - (O$.isExplorer() && O$.isStrictMode() ? Math.round(slider.s_pixelStep / 2) + 1 : 0));
          O$.setElementWidth(slider.s_bar, O$.getElementSize(O$(sl_sliderId + "::bar::image")).width);
          O$.setElementWidth(O$.getParentNode(slider.s_bar, "td"), O$.getElementSize(O$(sl_sliderId + "::bar::image")).width);
          if (O$(sl_sliderId + "::ticksLT::tick0") != null || O$(sl_sliderId + "::ticksRB::tick0") != null) {
            for (counterTicks = 0; counterTicks < slider.getTicksAmount(); counterTicks++) {
              s_aTickLT = O$(sl_sliderId + "::ticksLT::tick" + counterTicks);
              image = O$(sl_sliderId + "::ticksLT::tick" + counterTicks + "::image");
              text = O$(sl_sliderId + "::ticksLT::tick" + counterTicks + "::text");

              if (slider.s_ticksLT != null) {
                if (image != null) {
                  O$.setElementHeight(s_aTickLT, slider.s_pixelStep - (Math.floor((slider.s_pixelStep - O$.getElementSize(image).height) / 2) -
                          (slider.isCorrectionNeeded(slider.s_pixelStep, O$.getElementSize(image).height) ? 1 : 0)));
                  O$.getParentNode(image, "div").style.paddingTop = Math.floor((slider.s_pixelStep - O$.getElementSize(image).height) / 2) -
                          (slider.isCorrectionNeeded(slider.s_pixelStep, O$.getElementSize(image).height) ? 1 : 0) + "px";
                  image.style.marginLeft = maxTickSize - O$.getElementSize(image).width + "px";
                } else if (text != null) {
                  O$.setElementHeight(s_aTickLT, slider.s_pixelStep);
                  O$.getParentNode(text, "div").style.paddingTop = Math.floor(slider.s_pixelStep) + "px";
                }
                if (text != null) {
                  sizeText = O$.getElementSize(text).width;
                  if (image != null) {
                    size = O$.getElementSize(image).width;
                  }
                  if (O$.isExplorer6() || O$.isExplorer7() || (O$.isExplorer8() && O$.isQuirksMode()))
                    text.style.marginLeft = -maxTickSize + "px";
                  else
                    text.style.marginLeft = -Math.floor(sizeText / 2) + "px";
                  sizeText = O$.getElementSize(text).height;

                  text.style.marginTop = Math.floor(( - sizeText) / 2) + "px";
                }
              }

              s_aTickRB = O$(sl_sliderId + "::ticksRB::tick" + counterTicks);
              image = O$(sl_sliderId + "::ticksRB::tick" + counterTicks + "::image");
              text = O$(sl_sliderId + "::ticksRB::tick" + counterTicks + "::text");

              if (slider.s_ticksRB != null) {
                if (image != null) {
                  O$.setElementHeight(s_aTickRB, slider.s_pixelStep - (Math.floor((slider.s_pixelStep - O$.getElementSize(image).height) / 2) -
                          (slider.isCorrectionNeeded(slider.s_pixelStep, O$.getElementSize(image).height) ? 1 : 0)));
                  O$.getParentNode(image, "div").style.paddingTop = Math.floor((slider.s_pixelStep - O$.getElementSize(image).height) / 2) -
                          (slider.isCorrectionNeeded(slider.s_pixelStep, O$.getElementSize(image).height) ? 1 : 0) + "px";
                } else if (text != null) {
                  O$.setElementHeight(s_aTickRB, slider.s_pixelStep);
                  O$.getParentNode(text, "div").style.paddingTop = Math.floor(slider.s_pixelStep) + "px";
                }
                if (text != null) {
                  sizeText = O$.getElementSize(text).width;
                  if (image != null) {
                    size = O$.getElementSize(image).width;
                  }
                  text.style.marginLeft = Math.floor(maxTickSize - sizeText / 2) + "px";
                  sizeText = O$.getElementSize(text).height;
                  text.style.marginTop = Math.floor(( - sizeText) / 2) + "px";
                }
              }
            }
          }
          slider.s_handle.style.marginTop = s_handleValueOffset + "px";
          slider.s_handle.style.marginLeft = slider.getElementNonDragOffset(sl_sliderId + "::handle") + "px";
          if (slider.s_leftTopButton != null)
            slider.s_leftTopButton.style.marginLeft = slider.getElementNonDragOffset(sl_sliderId + "::leftTopButton") + "px";
          if (slider.s_rightBottomButton != null)
            slider.s_rightBottomButton.style.marginLeft = slider.getElementNonDragOffset(sl_sliderId + "::rightBottomButton") + "px";

          handleImg.style.marginTop = Math.floor((slider.s_pixelStep - handleImgSizeH) / 2) -
                  (slider.isCorrectionNeeded(slider.s_pixelStep, handleImgSizeH) ? 1 : 0) + "px";
        }

        slider.style.width = O$.calculateNumericCSSValue(O$.getStyleClassProperty(sl_styleClass, "width")) + "px";
        slider.style.height = O$.calculateNumericCSSValue(O$.getStyleClassProperty(sl_styleClass, "height")) + "px";

      },

      isCorrectionNeeded : function(a, b) {
        var rez = true;
        if ((a % 2 == 0 && b % 2 == 0) ||
                (a % 2 != 0 && b % 2 != 0)) {
          rez = false
        }
        return rez;
      },

      createToolTip : function() {
        var tooltip = O$.createAbsolutePositionedElement(slider.s_handle);
        tooltip.style.border = O$.getStyleClassProperty(sl_toolTipStyle, "border");
        tooltip.style.background = O$.getStyleClassProperty(sl_toolTipStyle, "background");
        tooltip.style.color = O$.getStyleClassProperty(sl_toolTipStyle, "color");
        tooltip.style.fontSize = O$.getStyleClassProperty(sl_toolTipStyle, "font-size");
        tooltip.style.textAlign = O$.getStyleClassProperty(sl_toolTipStyle, "text-align");
        tooltip.style.visibility = O$.getStyleClassProperty(sl_toolTipStyle, "visibility");
        tooltip.style.width = O$.getStyleClassProperty(sl_toolTipStyle, "width");
        tooltip.style.height = O$.getStyleClassProperty(sl_toolTipStyle, "height");
        tooltip.style.whiteSpace = "nowrap";
        if (sl_orientation == "horizontal") {
          tooltip.style.top = - slider.getElementNonDragDirectionSize(tooltip) + O$.getNumericElementStyle(O$(sl_sliderId + "::handle::image"), "margin-top") + "px";
          tooltip.style.left = - Math.abs(slider.getElementDragDirectionSize(slider.s_handle) - slider.getElementDragDirectionSize(tooltip)) / 2 + "px";
        } else {
          tooltip.style.left = - slider.getElementNonDragDirectionSize(tooltip) + "px";
          tooltip.style.top = - Math.abs(slider.getPixelStep() - slider.getElementDragDirectionSize(tooltip)) / 2 + "px";
        }
        if (sl_toolTipIsInteger) {
          tooltip.innerHTML = Math.round(slider.getValue());
        } else {
          tooltip.innerHTML = slider.getValue();
        }
        var height = O$.calculateNumericCSSValue(O$.getStyleClassProperty(sl_toolTipStyle, "height"));
        O$.addEventHandler(slider.s_handle, "mousedown", function(e) {
          O$.setElementEffectProperty(tooltip, "height", 1);
          tooltip.style.visibility = "visible";
          O$.runTransitionEffect(tooltip, "height", height, 50);
          O$.cancelEvent(e);
        });
        O$.addEventHandler(document, "mouseup", function(e) {
          tooltip.style.visibility = "hidden";
          O$.cancelEvent(e);
        });
        return tooltip;
      }
    });
    setCorrectPaddingForOnFocusSlider();

    formatOptions = formatOptions || {};
    if (formatOptions.type && formatOptions.type == "number") {
      formatOptions.type = "decimal";
    }

    slider.setValue(sl_value);

    var s_handleSize_w = 0;
    var s_workspaceSize_w = 0;

    var tooltip = null;

    O$.addLoadEvent(function() {
      slider.setValue(sl_value);
      slider.redraw(sl_orientation);

      s_handleSize_w = slider.getElementDragDirectionSize(O$(sl_sliderId + "::handle"));
      s_workspaceSize_w = slider.getElementDragDirectionSize(O$(sl_sliderId + "::workspace"));
      if (!sl_isDisabled)
        sli_makeDraggable(slider.s_handle);
      if (sl_tooltipEnabled) {
        tooltip = slider.createToolTip();
      }
      slider.style.visibility = "visible";

      O$.repaintWindowForSafari(true);
      O$.repaintAreaForOpera(slider, true);
      if (sl_onchanging) {
        slider._onchanging = function() {
          var event = O$.createEvent("changing");
          eval(sl_onchanging);
        };
      }
    });


    O$.setupArtificialFocus(slider, focusedClass);
    var eventName = "onkeydown";
    slider._prevKeyHandler = slider[eventName];
    slider[eventName] = function (evt) {
      var e = evt ? evt : window.event;
      switch (e.keyCode) {
        case 107:
        { //+
          slider.increaseValue();
          slider.redisplayHandle();
          O$.cancelEvent(e);
          break;
        }
        case 109:
        { //-
          slider.decreaseValue();
          slider.redisplayHandle();
          O$.cancelEvent(e);
          break;
        }
        case 38: // up
          if (sl_orientation == "horizontal") {
            slider._rightBottomButtonClick(e);
            break;
          } else {
            slider._leftTopButtonClick(e);
            break;
          }
        case 40: // down
          if (sl_orientation == "horizontal") {
            slider._leftTopButtonClick(e);
            break;
          }
          else {
            slider._rightBottomButtonClick(e);
            break;
          }
        case 39: // left
          if (sl_orientation == "horizontal") {
            slider._rightBottomButtonClick(e);
            break;
          }
          else {
            slider._leftTopButtonClick(e);
            break;
          }
        case 37: // right
          if (sl_orientation == "horizontal") {
            slider._leftTopButtonClick(e);
            break;
          } else {
            slider._rightBottomButtonClick(e);
            break;
          }
        case 35: // end
        {
          slider.setValue(sl_maxValue);
          slider.redisplayHandle();
          notifyOfInputChanges(slider);
          notifyOfInputChanging(slider);
          break;
        }
        case 36: // home
        {
          slider.setValue(sl_minValue);
          slider.redisplayHandle();
          notifyOfInputChanges(slider);
          notifyOfInputChanging(slider);
          break;
        }
      }
      if (slider._prevKeyHandler) {
        slider._prevKeyHandler(e);
      }
      if ((e.keyCode == 33 || e.keyCode == 34 || e.keyCode == 35 || e.keyCode == 36
              || e.keyCode == 37 || e.keyCode == 38 || e.keyCode == 39 || e.keyCode == 40)
              && (O$.isSafari() || O$.isChrome())) {
        return false;
      }
    };


    if (sl_onchange) {
      slider._onchange = function() {
        var event = O$.createEvent("change");
        eval(sl_onchange);
      };
    }

    if (!sl_barIsVisible) {
      slider.s_bar.style.visibility = "hidden";
    }

    if (!sl_isDisabled) {
      if (slider.s_leftTopButton != null) {
        O$.setupHoverAndPressStateFunction(slider.s_leftTopButton, function(mouseInside, pressed) {
          O$.setStyleMappings(slider.s_leftTopButton, {
            rollover: mouseInside ? rolloverAEClass : null
          });
          O$(sl_sliderId + "::leftTopButton::image").src = mouseInside ? LTButtonRolloverImageUrl : LTButtonImageUrl;
          O$(sl_sliderId + "::leftTopButton::image").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + (mouseInside ? LTButtonRolloverImageUrl : LTButtonImageUrl) + "' width:expression(1); height:expression(1)";
        });
        slider.s_leftTopButton.onmousedown = slider._leftTopButtonClick;
        slider.s_leftTopButton.ondblclick = O$.repeatClickOnDblclick;
      }
      if (slider.s_rightBottomButton != null) {
        O$.setupHoverAndPressStateFunction(slider.s_rightBottomButton, function(mouseInside, pressed) {
          O$.setStyleMappings(slider.s_rightBottomButton, {
            rollover: mouseInside ? rolloverAEClass : null
          });
          O$(sl_sliderId + "::rightBottomButton::image").src = mouseInside ? RBButtonRolloverImageUrl : RBButtonImageUrl;
          O$(sl_sliderId + "::rightBottomButton::image").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + (mouseInside ? RBButtonRolloverImageUrl : RBButtonImageUrl) + "' width:expression(1); height:expression(1)";
        });
        slider.s_rightBottomButton.onmousedown = slider._rightBottomButtonClick;
        slider.s_rightBottomButton.ondblclick = O$.repeatClickOnDblclick;
      }
      O$.setupHoverAndPressStateFunction(slider.s_handle, function(mouseInside, pressed) {
        O$.setStyleMappings(slider.s_handle, {
          rollover: mouseInside ? rolloverAEClass : null
        });
        O$(sl_sliderId + "::handle::image").src = mouseInside ? handleRolloverImageUrl : handleImageUrl;
        O$(sl_sliderId + "::handle::image").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + (mouseInside ? handleRolloverImageUrl : handleImageUrl) + "' width:expression(1); height:expression(1)";
      });

      if (sl_barCanChange) {
        O$.setupHoverAndPressStateFunction(slider.s_bar, function(mouseInside, pressed) {
          O$.setStyleMappings(slider.s_bar, {
            rollover: mouseInside ? rolloverAEClass : null
          });
        });

        function mouseDownOnBar(curClickPos) {
          var handlePos = O$.getElementPos(slider.s_handle, slider.s_workspace);
          if (sl_fillDirection == "fromStart") {
            if (((sl_orientation == "horizontal") && (curClickPos.x - handlePos.x >= 0) ) ||
                    ((sl_orientation == "vertical") && (curClickPos.y - handlePos.y <= 0) )) {
              slider.increaseValue();
            } else {
              slider.decreaseValue();
            }
          } else {
            if (((sl_orientation == "horizontal") && (curClickPos.x - handlePos.x >= 0) ) ||
                    ((sl_orientation == "vertical") && (curClickPos.y - handlePos.y <= 0) )) {
              slider.decreaseValue();
            } else {
              slider.increaseValue();
            }
          }
          slider.redisplayHandle();
        }

        var mouseDown;

        function mouseDownAction(ev) {
          var curClickPos = O$.getEventPoint(ev, slider.s_handle);

          mouseDownOnBar(curClickPos);
          mouseDown = setInterval(function() {
            mouseDownOnBar(curClickPos);
          }, sl_autoRepeatDelay);

        }

        function mouseUpAction() {
          if (typeof(mouseDown) != "undefined") clearTimeout(mouseDown);
        }

        O$.addEventHandler(slider.s_bar, "mousedown", mouseDownAction);
        O$.addEventHandler(document, "mouseup", mouseUpAction);
      }

      if (sl_textFieldState == "writeEnabled") {
        eventName = "onkeydown";
        slider.s_textField[eventName] = function(e) {
          var evt = O$.getEvent(e);
          if (evt.keyCode != 8 && //not backspace
                  evt.keyCode != 190 && //not dot
                  evt.keyCode != 110 && // not dot (numlock)
                  evt.keyCode != 46 && //not  delete
                  (evt.keyCode < 48 || evt.keyCode > 57)) { //not numbers
            return false;
          }
        };
        eventName = "onkeyup";
        slider.s_textField[eventName] = function(e) {
          var evt = O$.getEvent(e);

          if (evt.keyCode != 8 && //not backspace
                  evt.keyCode != 190 && //not dot
                  evt.keyCode != 110 && // not dot (numlock)
                  evt.keyCode != 46 && //not  delete
                  (evt.keyCode < 48 || evt.keyCode > 57)) { //not numbers
            return false;
          }
          var parsed = O$.Dojo.Number.parse(slider.s_textField.value, formatOptions);
          setTimeout(function() {
            if (!isNaN(parsed)) {
              parsed = parseFloat(slider.s_textField.value);
              slider.setValue(parsed);
              slider.redisplayHandle();
              notifyOfInputChanges(slider);
              notifyOfInputChanging(slider);
            } else {
              parsed = parseFloat(slider.s_textField.value);
              if (!isNaN(parsed)) {
                slider.setValue(parsed);
                slider.redisplayHandle();
                notifyOfInputChanges(slider);
                notifyOfInputChanging(slider);
              }
            }
            e.cancelBubble = true;
          }, 1);
        };
      }
    }

    function sli_makeDraggable(element) {
      makeDraggable(element);
      O$.addEventHandler(document, "mousemove", mouseMove, true);
      O$.addEventHandler(document, "mouseup", mouseUp, true);

      var dragObject;

      function mouseMove(ev) {
        ev = ev || O$.getEvent();
        if (dragObject && dragObject != null) {
          var mousePos = O$.getEventPoint(ev, dragObject);
          var topAlignment;
          var leftAlignment;
          var toSetValue;
          if (sl_orientation == "horizontal") {
            if ((mousePos.x - s_handleSize_w / 2) < 0) {
              dragObject.style.marginLeft = 0 + "px";
              toSetValue = (sl_fillDirection == "fromStart") ? sl_minValue : sl_maxValue;
            }
            else if (mousePos.x > s_workspaceSize_w - slider.s_widthCorrection - s_handleSize_w / 2) {
              dragObject.style.marginLeft = s_workspaceSize_w - slider.s_widthCorrection - s_handleSize_w + "px";
              toSetValue = (sl_fillDirection == "fromStart") ? sl_maxValue : sl_minValue;
            }
            else {
              leftAlignment = Math.round(mousePos.x - s_handleSize_w / 2);

              if (sl_snapToTicks) {
                toSetValue = !(sl_fillDirection == "fromStart") ?
                        sl_maxValue - Math.round(leftAlignment / slider.s_pixelStep) * sl_minorTickSpacing :
                        Math.round(leftAlignment / slider.s_pixelStep) * sl_minorTickSpacing + sl_minValue;

                dragObject.style.marginLeft = Math.round(leftAlignment / slider.s_pixelStep) * slider.s_pixelStep + "px";
              } else {
                toSetValue = !(sl_fillDirection == "fromStart") ?
                        sl_maxValue - (leftAlignment / slider.s_pixelStep) * sl_minorTickSpacing :
                        leftAlignment / slider.s_pixelStep * sl_minorTickSpacing + sl_minValue;
                dragObject.style.marginLeft = leftAlignment + "px";
              }
            }
          }
          else if (sl_orientation == "vertical") {
            if ((mousePos.y - s_handleSize_w / 2 ) < 0) {
              dragObject.style.marginTop = 0 + "px";
              toSetValue = (sl_fillDirection == "fromStart") ? sl_maxValue : sl_minValue;
            }
            else if (mousePos.y > s_workspaceSize_w - s_handleSize_w / 2) {
              dragObject.style.marginTop = (s_workspaceSize_w - s_handleSize_w) + "px";
              toSetValue = (sl_fillDirection == "fromStart") ? sl_minValue : sl_maxValue;
            }
            else {
              topAlignment = Math.round(mousePos.y - s_handleSize_w / 2);
              if (sl_snapToTicks) {
                toSetValue = (sl_fillDirection == "fromStart") ?
                        sl_maxValue - Math.round(topAlignment / slider.s_pixelStep) * sl_minorTickSpacing :
                        Math.round(topAlignment / slider.s_pixelStep) * sl_minorTickSpacing + sl_minValue;

                dragObject.style.marginTop = Math.round(topAlignment / slider.s_pixelStep) * slider.s_pixelStep + "px";
              } else {
                toSetValue = (sl_fillDirection == "fromStart") ?
                        sl_maxValue - (topAlignment / slider.s_pixelStep) * sl_minorTickSpacing - sl_minValue :
                        topAlignment / slider.s_pixelStep * sl_minorTickSpacing + sl_minValue;
                dragObject.style.marginTop = topAlignment + "px";
              }
            }
          }
          slider.setValue(toSetValue);
          if (tooltip && sl_tooltipEnabled) {
            if (sl_toolTipIsInteger) {
              tooltip.innerHTML = Math.round(slider.getValue());
            } else {
              tooltip.innerHTML = slider.getValue();
            }
          }
          notifyOfInputChanging(slider);
        }
      }

      function mouseUp() {
        if (dragObject != null) {
          notifyOfInputChanging(slider)
          notifyOfInputChanges(slider);
        }
        dragObject = null;
      }

      function makeDraggable(item) {
        if (!item) return;
        item.onmousedown = function() {
          dragObject = this;
          return false;
        };
      }
    }

    function notifyOfInputChanges(slider) {
      if (tooltip && sl_tooltipEnabled) {
        if (sl_toolTipIsInteger) {
          tooltip.innerHTML = Math.round(slider.getValue());
        } else {
          tooltip.innerHTML = slider.getValue();
        }
      }
      if (slider._onchange)
        slider._onchange(O$.createEvent("change"));
    }

    function notifyOfInputChanging(slider) {
      if (tooltip && sl_tooltipEnabled) {
        if (sl_toolTipIsInteger) {
          tooltip.innerHTML = Math.round(slider.getValue());
        } else {
          tooltip.innerHTML = slider.getValue();
        }
      }
      if (slider._onchanging)
        slider._onchanging(O$.createEvent("changing"));
    }

    function setCorrectPaddingForOnFocusSlider() {
      var sliderOnFocusPaddings = O$.getStyleClassProperties(
              focusedClass, ["padding-left", "padding-right", "padding-top", "padding-bottom"]);

      var sliderOnFocusBorder = O$.getStyleClassProperties(
              focusedClass, ["border-left-width", "border-right-width", "border-top-width", "border-bottom-width"]);

      var sliderStyle = O$.getElementStyle(slider, ["padding-left", "padding-right", "padding-top", "padding-bottom",
        "border-left-width", "border-top-width", "border-right-width", "border-bottom-width"]);

      var adjustedStyles = "";

      function adjustPaddingIfNotSpecified(paddingPropertyName, padding, border, onFocusedBorder, borderOnFocusedName) {
        if (sliderOnFocusPaddings[paddingPropertyName])
          return;
        var onFocusedPadding = O$.calculateNumericCSSValue(padding) + O$.calculateNumericCSSValue(border) - O$.calculateNumericCSSValue(onFocusedBorder);
        adjustedStyles += paddingPropertyName + ": " + onFocusedPadding + "px; ";

        if (onFocusedPadding > O$.calculateNumericCSSValue(padding)) {
          var borderOnFocus = O$.calculateNumericCSSValue(padding) + O$.calculateNumericCSSValue(border) - onFocusedPadding;
          adjustedStyles += borderOnFocusedName + ": " + borderOnFocus + "px; ";
        }
      }

      adjustPaddingIfNotSpecified("padding-left", sliderStyle.paddingLeft, sliderStyle.borderLeftWidth,
              sliderOnFocusBorder["border-left-width"], "border-left-width");
      adjustPaddingIfNotSpecified("padding-right", sliderStyle.paddingRight, sliderStyle.borderRightWidth,
              sliderOnFocusBorder["border-right-width"], "border-right-width");
      adjustPaddingIfNotSpecified("padding-top", sliderStyle.paddingTop, sliderStyle.borderTopWidth,
              sliderOnFocusBorder["border-top-width"], "border-top-width");
      adjustPaddingIfNotSpecified("padding-bottom", sliderStyle.paddingBottom, sliderStyle.borderBottomWidth,
              sliderOnFocusBorder["border-bottom-width"], "border-bottom-width");

      if (adjustedStyles) {
        var newClassName = O$.createCssClass(adjustedStyles);
        focusedClass = O$.combineClassNames([focusedClass, newClassName]);
      }
    }
  }
};

