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
O$.DropDown = {
  _initInput: function(dropDownId,
                       initialText,
                       containerClass,
                       rolloverContainerClass,
                       disabledClass,
                       fieldClass,
                       rolloverFieldClass,
                       disabledFieldClass,
                       focusedClass,
                       promptText,
                       promptTextClass) {
    var dropDown = O$.initComponent(dropDownId, null, {
      _field: O$(dropDownId + "::field"),
      _promptVisible: O$(dropDownId + "::field" + "::promptVisible"),
      _initialText: initialText,
      _focusable : true,
      _fieldDisabled: false,

      _setFieldDisabled: function(disabled) {
        if (this._fieldDisabled == disabled) return;

        this._fieldDisabled = disabled;

        O$.setStyleMappings(this, {disabled: disabled ? disabledClass : ""});
        O$.setStyleMappings(this._field, {fieldDisabled: disabled ? disabledFieldClass : ""});

        this._field.disabled = disabled ? "disabled" : "";
      }
    });

    dropDown._o_inputField = dropDown._field; // for O$._selectTextRange to be able to access the text field

    if (!dropDown._field) {
      // the case for SuggestionField
      O$.assert(dropDown.nodeName.toUpperCase() == "INPUT", "O$._initDropDownField. Unexpected dropDown.nodeName: " + dropDown.nodeName);
      dropDown._field = dropDown;
    }

    var field = dropDown._field;
    dropDown._containerClass = O$.DropDown._getClassName(containerClass);
    dropDown._focusedClass = O$.DropDown._getClassName(focusedClass);
    dropDown.className = dropDown._containerClass;
    dropDown._rolloverContainerClass = dropDown._containerClass + O$.DropDown._getClassName(rolloverContainerClass);

    dropDown._promptText = promptText;
    dropDown._promptTextClass = O$.DropDown._getClassName(promptTextClass);

    if (dropDown._promptText) {
      if ((initialText.length = 0 && dropDown._field.value.length == 0) ||
          (initialText.length = 0 && dropDown._field.value == dropDown._promptText)) {
        //needed for FireFox, when press F5 key
        dropDown._promptVisible.value = true;
      }
    } else
      dropDown._promptVisible.value = false;

    dropDown._fieldClass = O$.DropDown._getClassName(fieldClass);
    if (dropDown != field)
      field.className = dropDown._fieldClass;

    if (O$.isOpera() && !O$.isOpera9AndLate()) { // padding not correct work in Opera8
      field.style.padding = "0px";
    }

    dropDown._rolloverFieldClass = dropDown._fieldClass + O$.DropDown._getClassName(rolloverFieldClass);

    dropDown._initValue = function (value) {
      field.value = value;
    };

    if (dropDown != field)
      dropDown.focus = function() {
        try {
          field.focus();
        } catch (e) {
        }
      };
    dropDown._onfocus = dropDown.onfocus;

    var waitingForFocusReacquiring = false;
    dropDown._focusHandler = function() {
      if (waitingForFocusReacquiring) {
        waitingForFocusReacquiring = false;
        return;
      }
      O$.appendClassNames(dropDown, [dropDown._focusedClass]);
      dropDown._containerClass = O$.DropDown._addInClassName(dropDown._containerClass, dropDown._focusedClass);
      dropDown._rolloverContainerClass = O$.DropDown._addInClassName(dropDown._rolloverContainerClass, dropDown._focusedClass);
      if (dropDown._onfocus) {
        dropDown._onfocus();
      }

      if (dropDown._promptText) {
        if ((dropDown._field.value == dropDown._promptText) && (dropDown._promptVisible.value == "true")) {
          if (promptTextClass)
            O$.excludeClassNames(dropDown._field, [promptTextClass]);
          dropDown._field.value = "";
          dropDown._promptVisible.value = false;
        }
      }
    };
    O$.addEventHandler(field, "focus", dropDown._focusHandler);
    O$.initUnloadableComponent(field);

    dropDown._onblur = dropDown.onblur;
    O$.addEventHandler(field, "blur", function() {
      waitingForFocusReacquiring = true;
      setTimeout(function() {
        if (!waitingForFocusReacquiring)
          return;
        waitingForFocusReacquiring = false;
        O$.excludeClassNames(dropDown, [dropDown._focusedClass]);
        dropDown._containerClass = O$.DropDown._removeOfClassName(dropDown._containerClass, dropDown._focusedClass);
        dropDown._rolloverContainerClass = O$.DropDown._removeOfClassName(dropDown._rolloverContainerClass, dropDown._focusedClass);

        if (dropDown._onblur) {
          dropDown._onblur();
        }

        if (dropDown._promptText) {
          if ((dropDown._field.value.length == 0)) {
            if (promptTextClass) {
              O$.appendClassNames(dropDown._field, [promptTextClass]);
            }

            // This timeout is required for the prompt text under IE
            setTimeout(function() {
              if (dropDown._itemPresentation){
                dropDown._showPresentationPromptText(dropDown._promptText);
              } else {
                dropDown._field.value = dropDown._promptText;
              }
            }, 1);

            dropDown._promptVisible.value = true;
          } else
            dropDown._promptVisible.value = false;
        }
      }, 1);
    });

    if (O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/) {
      O$.addEventHandler(dropDown, "keyup", function(evt) {
        if (evt.keyCode == 27) {//ESC key
          if (dropDown._field.value == dropDown._promptText) {
            dropDown._field.value = "";
          }
        }
      });
      O$.initUnloadableComponent(dropDown);
    }

    setTimeout(function() {
      if (initialText.length > 0) {
        dropDown._initValue(initialText);
      } else if (initialText.length == 0 && dropDown._promptText) {
        dropDown._initValue(initialText);

        setTimeout(function() {
          if (dropDown._itemPresentation){
            dropDown._showPresentationPromptText(dropDown._promptText);
          } else {
            dropDown._field.value = dropDown._promptText;
          }
        }, 1);

        if (promptTextClass) {
          O$.appendClassNames(dropDown._field, [dropDown._promptTextClass]);
        }
        dropDown._promptVisible.value = true;
      } else {
        dropDown._initValue(initialText);
      }

      dropDown._skipValidation = false;
    }, 100);

    field._o_zeroBorders = true;
    field._o_fullWidth = true;
    field._o_fullHeight = false;
    O$.fixInputsWidthStrict(dropDown);
  },

  _init: function(dropDownId,
                  initialText,
                  containerClass,
                  rolloverContainerClass,
                  disabledClass,
                  fieldClass,
                  rolloverFieldClass,
                  disabledFieldClass,
                  focusedClass,
                  buttonClass,
                  rolloverButtonClass,
                  pressedButtonClass,
                  disabledButtonClass,
                  disabledButtonImageUrl,
                  popupClass,
                  rolloverPopupClass,
                  disabled,
                  readOnly,
                  promptText,
                  promptTextClass,
                  pullPopupFromContainer) {

    O$.DropDown._initInput(dropDownId,
            initialText,
            containerClass,
            rolloverContainerClass,
            disabledClass,
            fieldClass,
            rolloverFieldClass,
            disabledFieldClass,
            focusedClass,
            promptText,
            promptTextClass);

    var popupId = dropDownId + "--popup";
    O$.removeRelocatedPopupIfExists(popupId);

    var originalEnabledButtonImageUrl = null;

    var dropDown = O$.initComponent(dropDownId, null, {
      _button: O$(dropDownId + "::button"),
      _popup: O$(popupId),
      _disabled: false,

      setDisabled: function(disabled) {
        O$.setHiddenField(this, dropDownId + "::disabled", disabled);
        if (this._disabled == disabled) return;


        if (this._button) {
          // if there's a button (if it's not a plain SuggestionField component)
          if (!this._button._img) {
            this._button._img = O$(dropDownId + "::button::img");
          }
          if (!originalEnabledButtonImageUrl) {
            O$.assert(!this._disabled, "The drop-down field is expected to be enabled by default during initialization");
            originalEnabledButtonImageUrl = this._button._img.src;
          }
        }

        this._disabled = disabled;
        this._setFieldDisabled(disabled);

        if (this._button) {
          O$.setStyleMappings(this._button, {disabled: disabled ? disabledButtonClass : ""});

          this._button._img.src = disabled ? disabledButtonImageUrl : originalEnabledButtonImageUrl;

          this._button._focusable = !disabled;
          this._button._img._focusable = !disabled;
        }
      },

      getDisabled: function() {
        return this._disabled;
      }

    });

    if (pullPopupFromContainer) {
      dropDown._popup._pullFromContainer();
    }

    var field = dropDown._field;
    var button = dropDown._button;
    var popup = dropDown._popup;

    popup._dependedFieldId = field.id;

    dropDown._skipValidation = true;

    field._dropDown = dropDown;
    if (button)
      button._dropDown = dropDown;
    popup._dropDown = dropDown;

    O$.addEventHandler(popup, "mousedown", function(e) {
      O$.stopEvent(e);
    });
    O$.initUnloadableComponent(popup);

    dropDown._buttonClass = O$.DropDown._getClassName(buttonClass);
    if (button)
      button.className = dropDown._buttonClass;

    O$.addLoadEvent(function (){
      dropDown.setDisabled(disabled);
    });

    dropDown._rolloverButtonClass = dropDown._buttonClass + O$.DropDown._getClassName(rolloverButtonClass);
    dropDown._pressedButtonClass = dropDown._rolloverButtonClass + O$.DropDown._getClassName(pressedButtonClass);

    var mouseOverBehaviorNeeded = rolloverContainerClass || rolloverFieldClass || rolloverButtonClass || rolloverPopupClass;

    if (button) {
      if (!readOnly) {
        O$.extend(button, {
          onmousedown: function(e) {
            if (dropDown._disabled) return;
            if (O$.isEventFromInsideOfElement(e, popup))
              return;
            button.className = dropDown._pressedButtonClass;
            dropDown._showHidePopup();
            O$.Popup._hideAllPopupsExceptOne(popup);
            O$.cancelEvent(e);
          },
          ondblclick: function(e) {
            if (dropDown._disabled) return;
            if (O$.isEventFromInsideOfElement(e, popup))
              return;
            if (!O$.isExplorer())
              return;
            dropDown._showHidePopup();
            O$.cancelEvent(e);
          },
          onmousemove: function(e) {
            if (dropDown._disabled) return;
            if (O$.isEventFromInsideOfElement(e, popup))
              return;
            O$.cancelEvent(e);
          },
          onmouseup: function(e) {
            if (dropDown._disabled) return;
            if (O$.isEventFromInsideOfElement(e, popup))
              return;
            button.className = dropDown._rolloverButtonClass;
          },
          onmouseover: function(e) {
            if (dropDown._disabled) return;
            if (O$.isEventFromInsideOfElement(e, popup))
              return;
            if (dropDown._buttonClass != dropDown._rolloverButtonClass)
              button.className = dropDown._rolloverButtonClass;
          },
          onmouseout: function(e) {
            if (dropDown._disabled) return;
            if (O$.isEventFromInsideOfElement(e, popup))
              return;

            if (dropDown._buttonClass != dropDown._rolloverButtonClass)
              button.className = dropDown._buttonClass;
          },
          ondragstart: function(e) {
            O$.cancelEvent(e);
          },
          onselectstart: function(e) {
            O$.cancelEvent(e);
          }
        });

      }
    }

    popup.onmousemove = function(e) {
      O$.cancelEvent(e);
    };
    O$.addUnloadHandler(popup, function () {
      popup.onmousemove = null;
    });

    popupClass = O$.combineClassNames([popupClass, popup.className]);
    dropDown._popupClass = O$.DropDown._getClassName(popupClass);
    popup.className = dropDown._popupClass;
    dropDown._rolloverPopupClass = dropDown._popupClass + " " + O$.DropDown._getClassName(rolloverPopupClass);

    if (mouseOverBehaviorNeeded) {
      dropDown._dropDownMouseOver = function() {
        if (dropDown._disabled) return;
        if (dropDown && dropDown._containerClass != dropDown._rolloverContainerClass)
          dropDown.className = dropDown._rolloverContainerClass;
        if (dropDown != field && dropDown._fieldClass != dropDown._rolloverFieldClass)
          field.className = dropDown._rolloverFieldClass;
        if (popup && dropDown._popupClass != dropDown._rolloverPopupClass)
          popup.className = dropDown._rolloverPopupClass;
        O$.repaintAreaForOpera(dropDown, true);
        O$.repaintAreaForOpera(popup, true);
      };

      dropDown._dropDownMouseOut = function() {
        if (dropDown._disabled) return;
        if (dropDown && dropDown._containerClass != dropDown._rolloverContainerClass)
          dropDown.className = dropDown._containerClass;
        if (dropDown != field && dropDown._fieldClass != dropDown._rolloverFieldClass)
          field.className = dropDown._fieldClass;
        if (popup && dropDown._popupClass != dropDown._rolloverPopupClass)
          popup.className = dropDown._popupClass;
        O$.repaintAreaForOpera(dropDown, true);
        O$.repaintAreaForOpera(popup, true);
      };

      O$.addMouseOverListener(dropDown, dropDown._dropDownMouseOver);
      O$.addMouseOverListener(popup, dropDown._dropDownMouseOver);

      O$.addMouseOutListener(dropDown, dropDown._dropDownMouseOut);
      O$.addMouseOutListener(popup, dropDown._dropDownMouseOut);
    }
    function resizeHandlerOnWindow() {
      var dropDownPopup = dropDown._popup;
      // drop-down can be removed from the page using Ajax, so we need to check its presence
      if (dropDownPopup && dropDownPopup.isVisible()) {
        O$.DropDown._alignPopup(dropDown);
      }
    }

    //add function to change popup position if window is resized and layout is changed
    O$.addEventHandler(window, "resize", resizeHandlerOnWindow);
    O$.addUnloadHandler(popup, function () {
      O$.removeEventHandler(window, "resize", resizeHandlerOnWindow);
    });
  },

  /*
   @deprecated: don't use or copy -- use O$.setStyleMappings instead
   */
  _addInClassName: function(classDest, className) { // todo: remove this method and replace usages with O$.appendClassNames or O$.setStyleMappings
    if (classDest.length > 0) {
      classDest = classDest + " " + className;
    } else {
      classDest = className;
    }
    return classDest;
  },

  /*
   @deprecated: don't use or copy -- use O$.setStyleMappings instead
   */
  _removeOfClassName: function(classDest, className) { // todo: remove this method and replace usages with O$.excludeClassNames or O$.setStyleMappings
    if (classDest.length > 0) {
      classDest = classDest.replace(className, "");
    }
    return classDest;
  },

  _getClassName: function(param) { // todo: remove the need for this function. merging styles should be done in a more localized way
    return (param == null) ? "" : " " + param;
  },


  _initPopup: function(dropDown, calendar) {
    var repaintDropDown = false;
    var popup = dropDown._popup;
    var container = O$.getDefaultAbsolutePositionParent();
    if ((O$.isExplorer() && O$.isQuirksMode() && dropDown._popup.parentNode != container) ||
        (O$.isMozillaFF2() && O$.isStrictMode())) {
      // prevent clipping the drop-down with parent nodes with hidden overflow (possible only in IE+quirks)
      // (e.g. scrollable table header might prevent drop-down filter popup from displaying fully) 
      container.appendChild(dropDown._popup);
    }

    var sizeChanged = false;
    if (calendar) {
      popup.style.width = calendar.offsetWidth + "px";
    } else {
      var innerTable = O$(dropDown.id + "--popup::innerTable");
      var tableWidth = innerTable.offsetWidth;
      var tableHeight = innerTable.offsetHeight;
      if (popup._initializedTableWidth != tableWidth || popup._initializedTableHeight != tableHeight) {
        popup._initializedTableWidth = tableWidth;
        popup._initializedTableHeight = tableHeight;
        sizeChanged = true;

        var minWidth;
        if (popup._initialClientWidth == undefined) {
          popup._initialClientWidth = popup.clientWidth;
        }
        var dropDownRect = O$.getElementBorderRectangle(dropDown);
        if (popup.clientWidth <= 0) { // the case when width is not specified explicitly
          minWidth = dropDownRect.width;
          if (tableWidth > dropDownRect.width) {
            minWidth = tableWidth;
          }
        } else
          minWidth = popup.offsetWidth;

        if (popup.offsetWidth < minWidth) {
          popup.style.width = minWidth + "px";
          popup._widthCorrection = popup.offsetWidth - minWidth;
          if (popup._widthCorrection) { // actual for Mozilla & Opera
            popup.style.width = (minWidth - popup._widthCorrection) + "px";
            repaintDropDown = true;
          }
        }
        if (popup.offsetWidth > O$.getNumericElementStyle(popup, "width", true) && !popup._widthCorrection) {
          popup._widthCorrection = popup.offsetWidth - O$.getNumericElementStyle(popup, "width", true);
        }

        if (popup.offsetWidth < minWidth) {
          popup.style.width = dropDownRect.width + "px";
          repaintDropDown = true;
        }

        if (popup._initialClientHeight == undefined) {
          popup._initialClientHeight = popup.clientHeight;
        } else {
          popup.style.height = popup._initialClientHeight + "px";
        }
        function adjustHeight() {
          var contentHeight = innerTable.offsetHeight;
          if (popup.clientHeight > contentHeight) {
            var borderAccomodation = (O$.isExplorer() && document.compatMode == "BackCompat") ? 10 : 0;
            var preferredHeight = borderAccomodation + (contentHeight ? contentHeight : 20);
            popup.style.height = preferredHeight + "px";
            var heightCorrection = popup.clientHeight - preferredHeight;
            if (heightCorrection)
              popup.style.height = (preferredHeight - heightCorrection - borderAccomodation) + "px";
          }
        }

        adjustHeight();

        var recalculateWidth = O$.isExplorer() && document.compatMode == "CSS1Compat";
        if (popup._initialClientWidth <= 0) { // if width is not specified explicitly
          if (recalculateWidth)
            innerTable.width = "";
          var contentWidth = innerTable.offsetWidth;
          var missingWidth = contentWidth - popup.clientWidth;
          if (missingWidth > 0)
            popup.style.width = popup.offsetWidth + missingWidth + "px";
          if (recalculateWidth)
            innerTable.width = popup.clientWidth;
        } else {
          if (recalculateWidth) {
            innerTable.width = popup.clientWidth;
          }
        }

        if (document.compatMode == "CSS1Compat") {
          adjustHeight();
        }
      }
    }

    if (repaintDropDown && O$.isMozillaFF())
      O$.DropDown._alignPopup(dropDown);
    O$.DropDown._alignPopup(dropDown, calendar);
    if (popup._ieTransparencyControl)
      popup._ieTransparencyControl._updatePositionAndSize();
  },

  _alignPopup: function(dropDown, calendar) {
    var popup = dropDown._popup;
    if (dropDown._listAlignment == "right" || calendar) { // align by the right edge
      O$.alignPopupByElement(popup, dropDown, O$.RIGHT, O$.BELOW);
    } else { // bind drop list to the left border
      O$.alignPopupByElement(popup, dropDown, O$.LEFT, O$.BELOW);
    }
  }
};
