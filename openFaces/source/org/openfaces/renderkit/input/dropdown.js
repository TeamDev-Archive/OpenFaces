/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
O$._initDropDownInput = function(dropDownId,
                                 initialText,
                                 containerClass,
                                 rolloverContainerClass,
                                 fieldClass,
                                 rolloverFieldClass,
                                 focusedClass,
                                 promptText,
                                 promptTextClass) {
  var dropDown = O$(dropDownId);

  dropDown._field = O$(dropDownId + "::field");
  dropDown._statePrompt = O$(dropDownId + "::field" + "::statePrompt");

  dropDown._initialText = initialText;

  if (!dropDown._field) {
    // the case for SuggestionField
    O$.assert(dropDown.nodeName.toUpperCase() == "INPUT", "O$._initDropDownField. Unexpected dropDown.nodeName: " + dropDown.nodeName);
    dropDown._field = dropDown;
  }

  var field = dropDown._field;
  dropDown._containerClass = O$._dropdown_getClassName(containerClass);
  dropDown._focusedClass = O$._dropdown_getClassName(focusedClass);
  dropDown.className = dropDown._containerClass;
  dropDown._rolloverContainerClass = dropDown._containerClass + O$._dropdown_getClassName(rolloverContainerClass);

  dropDown._promptText = promptText;
  dropDown._promptTextClass = O$._dropdown_getClassName(promptTextClass);

  if (dropDown._promptText) {
    if (dropDown._field.value.length == 0 ||
        ((dropDown._field.value == dropDown._promptText) && dropDown._statePrompt.value == "true")) {   //needed for FireFox, when press F5 key
      dropDown._statePrompt.value = true;
    }
  } else
    dropDown._statePrompt.value = false;

  dropDown._fieldClass = O$._dropdown_getClassName(fieldClass);
  if (dropDown != field)
    field.className = dropDown._fieldClass;

  if (document.compatMode == "CSS1Compat") { // fixes the strict mode issue of assigning 100% width on text-field inside a table
    if (O$.isOpera9AndLate() || O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/) {
      field.parentNode.style.paddingBottom = "2px";
    }
    if (!O$.isOpera()) {
      field.parentNode.style.paddingRight = O$.isExplorer() ? "8px" : "4px";
    }
  }
  if (O$.isOpera() && !O$.isOpera9AndLate()) { // padding not correct work in Opera8
    field.style.padding = "0px";
  }

  dropDown._rolloverFieldClass = dropDown._fieldClass + O$._dropdown_getClassName(rolloverFieldClass);

  dropDown._initValue = function (value) {
    field.value = value;
  }

  if (dropDown != field)
    dropDown.focus = function() {
      try {
        field.focus();
      }
      catch(e) {
      }
    }
  dropDown._onfocus = dropDown.onfocus;

  var waitingForFocusReacquiring = false;
  O$.addEventHandler(field, "focus", function() {
    if (waitingForFocusReacquiring) {
      waitingForFocusReacquiring = false;
      return;
    }
    O$.appendClassNames(dropDown, [dropDown._focusedClass]);
    dropDown._containerClass = O$._addInClassName(dropDown._containerClass, dropDown._focusedClass);
    dropDown._rolloverContainerClass = O$._addInClassName(dropDown._rolloverContainerClass, dropDown._focusedClass);
    if (dropDown._onfocus) {
      dropDown._onfocus();
    }

    if (dropDown._promptText) {
      if ((dropDown._field.value == dropDown._promptText) && (dropDown._statePrompt.value == "true")) {
        if (promptTextClass)
          O$.excludeClassNames(dropDown._field, [promptTextClass]);
        dropDown._field.value = "";
        dropDown._statePrompt.value = false;
      }
    }
  });

  dropDown._onblur = dropDown.onblur;
  O$.addEventHandler(field, "blur", function() {
    waitingForFocusReacquiring = true;
    setTimeout(function() {
      if (!waitingForFocusReacquiring)
        return;
      waitingForFocusReacquiring = false;
      O$.excludeClassNames(dropDown, [dropDown._focusedClass]);
      dropDown._containerClass = O$._removeOfClassName(dropDown._containerClass, dropDown._focusedClass);
      dropDown._rolloverContainerClass = O$._removeOfClassName(dropDown._rolloverContainerClass, dropDown._focusedClass);

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
            dropDown._field.value = dropDown._promptText;
          }, 1);

          dropDown._statePrompt.value = true;
        } else
          dropDown._statePrompt.value = false;
      }
    }, 1);
  });

  if (O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/)
    O$.addEventHandler(dropDown, "keyup", function(evt) {
      if (evt.keyCode == 27) {//ESC key
        if (dropDown._field.value == dropDown._promptText) {
          dropDown._field.value = "";
        }
      }
    });

  setTimeout(function() {
    if (initialText.length > 0) {
      dropDown._initValue(initialText);
    } else if (initialText.length == 0 && dropDown._promptText) {
      dropDown._initValue(initialText);

      setTimeout(function() {
        dropDown._field.value = dropDown._promptText;
      }, 1);

      if (promptTextClass) {
        O$.appendClassNames(dropDown._field, [dropDown._promptTextClass]);
      }
      dropDown._statePrompt.value = true;
    } else {
      dropDown._initValue(initialText);
    }

    dropDown._skipValidation = false;
  }, 100);
}

O$._initDropDown = function(dropDownId,
                            initialText,
                            containerClass,
                            rolloverContainerClass,
                            fieldClass,
                            rolloverFieldClass,
                            focusedClass,
                            buttonClass,
                            rolloverButtonClass,
                            pressedButtonClass,
                            popupClass,
                            rolloverPopupClass,
                            disabled,
                            promptText,
                            promptTextClass) {

  O$._initDropDownInput(dropDownId,
          initialText,
          containerClass,
          rolloverContainerClass,
          fieldClass,
          rolloverFieldClass,
          focusedClass,
          promptText,
          promptTextClass);

  var dropDown = O$(dropDownId);

  dropDown._button = O$(dropDownId + "::button");
  dropDown._popup = O$(dropDownId + "--popup");

  var field = dropDown._field;
  var button = dropDown._button;
  var popup = dropDown._popup;

  dropDown._skipValidation = true;

  field._dropDown = dropDown;
  if (button)
    button._dropDown = dropDown;
  popup._dropDown = dropDown;

  dropDown._buttonClass = O$._dropdown_getClassName(buttonClass);
  if (button)
    button.className = dropDown._buttonClass;
  dropDown._rolloverButtonClass = dropDown._buttonClass + O$._dropdown_getClassName(rolloverButtonClass);
  dropDown._pressedButtonClass = dropDown._rolloverButtonClass + O$._dropdown_getClassName(pressedButtonClass);

  var mouseOverBehaviorNeeded = rolloverContainerClass || rolloverFieldClass || rolloverButtonClass || rolloverPopupClass;

  if (button) {
    var buttonImage = button.getElementsByTagName("img")[0];
    if (disabled) {
      button._focusable = false;
      buttonImage._focusable = false;
    } else {
      button._focusable = true;
      buttonImage._focusable = true;

      button.onmousedown = function (e) {
        if (O$.isEventFromInsideOfElement(e, popup))
          return;
        button.className = dropDown._pressedButtonClass;
        dropDown._showHidePopup();
        O$._hideAllPopupsExceptOne(popup);
        O$.breakEvent(e);
      }
      button.ondblclick = function (e) {
        if (O$.isEventFromInsideOfElement(e, popup))
          return;
        if (!O$.isExplorer())
          return;
        dropDown._showHidePopup();
        O$.breakEvent(e);
      }
      button.onmousemove = function (e) {
        if (O$.isEventFromInsideOfElement(e, popup))
          return;
        O$.breakEvent(e);
      }
      button.onmouseup = function (e) {
        if (O$.isEventFromInsideOfElement(e, popup))
          return;
        button.className = dropDown._rolloverButtonClass;
      }
      button.ondragstart = function(e) {
        O$.breakEvent(e);
      }
      button.onselectstart = function (e) {
        O$.breakEvent(e);
      }
    }
  }

  popup.onmousemove = function (e) {
    O$.breakEvent(e);
  }

  popupClass = O$.combineClassNames([popupClass, popup.className]);
  dropDown._popupClass = O$._dropdown_getClassName(popupClass);
  popup.className = dropDown._popupClass;
  dropDown._rolloverPopupClass = dropDown._popupClass + " " + O$._dropdown_getClassName(rolloverPopupClass);

  if (mouseOverBehaviorNeeded) {
    dropDown._dropDownMouseOver = function() {
      if (dropDown && dropDown._containerClass != dropDown._rolloverContainerClass)
        dropDown.className = dropDown._rolloverContainerClass;
      if (dropDown != field && dropDown._fieldClass != dropDown._rolloverFieldClass)
        field.className = dropDown._rolloverFieldClass;
      if (button && dropDown._buttonClass != dropDown._rolloverButtonClass)
        button.className = dropDown._rolloverButtonClass;
      if (popup && dropDown._popupClass != dropDown._rolloverPopupClass)
        popup.className = dropDown._rolloverPopupClass;
      O$.repaintAreaForOpera(dropDown, true);
      O$.repaintAreaForOpera(popup, true);
    }

    dropDown._dropDownMouseOut = function() {
      if (dropDown && dropDown._containerClass != dropDown._rolloverContainerClass)
        dropDown.className = dropDown._containerClass;
      if (dropDown != field && dropDown._fieldClass != dropDown._rolloverFieldClass)
        field.className = dropDown._fieldClass;
      if (button && dropDown._buttonClass != dropDown._rolloverButtonClass)
        button.className = dropDown._buttonClass;
      if (popup && dropDown._popupClass != dropDown._rolloverPopupClass)
        popup.className = dropDown._popupClass;
      O$.repaintAreaForOpera(dropDown, true);
      O$.repaintAreaForOpera(popup, true);
    }

    O$.addMouseOverListener(dropDown, dropDown._dropDownMouseOver);
    O$.addMouseOverListener(popup, dropDown._dropDownMouseOver);

    O$.addMouseOutListener(dropDown, dropDown._dropDownMouseOut);
    O$.addMouseOutListener(popup, dropDown._dropDownMouseOut);
  }

  //add function to change popup position if window is resized and layout is changed
  O$.addEventHandler(window, "resize", function () {
    var dropDownPopup = dropDown._popup;
    // drop-down can be removed from the page using Ajax, so we need to check its presense
    if (dropDownPopup && dropDownPopup.isVisible()) {
      O$._dropDown_alignPopup(dropDown);
    }
  });

}

O$._addInClassName = function(classDest, className) { // todo: remove this method and replace usages with O$.appendClassNames or O$.setElementStyleMappings
  if (classDest.length > 0) {
    classDest = classDest + " " + className;
  } else {
    classDest = className;
  }
  return classDest;
}

O$._removeOfClassName = function(classDest, className) { // todo: remove this method and replace usages with O$.excludeClassNames or O$.setElementStyleMappings
  if (classDest.length > 0) {
    classDest = classDest.replace(className, "");
  }
  return classDest;
}

O$._dropdown_getClassName = function(param) { // todo: remove the need for this function. merging styles should be done in a more localized way
  return (param == null) ? "" : " " + param;
}


O$._dropDown_initPopup = function(dropDown, calendar) {
  var repaintDropDown = false;
  var popup = dropDown._popup;

  var sizeChanged = false;
  if (calendar) {
    popup.style.width = calendar.offsetWidth + "px";
  } else {
    var innerTable = O$(popup.id + "::innerTable");
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
      if (popup.offsetWidth > O$.getNumericStyleProperty(popup, "width", true) && !popup._widthCorrection) {
        popup._widthCorrection = popup.offsetWidth - O$.getNumericStyleProperty(popup, "width", true);
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
    O$._dropDown_alignPopup(dropDown);
  O$._dropDown_alignPopup(dropDown, calendar);
  if (popup._ieTransparencyControl)
    popup._ieTransparencyControl._updatePositionAndSize();
}

O$._dropDown_alignPopup = function(dropDown, calendar) {
  var popup = dropDown._popup;

  var dropDownRect = O$.getElementBorderRectangle(dropDown, true);

  var left;
  if (dropDown._listAlignment == "right" || calendar) { // to the right border
    left = dropDownRect.x - ((calendar ? calendar.offsetWidth : popup.offsetWidth) - dropDownRect.width);
  } else { // bind drop list to the left border
    left = dropDownRect.x;
  }
  var top = dropDownRect.getMaxY();

  popup.setTop(top);
  popup.setLeft(left);
}
