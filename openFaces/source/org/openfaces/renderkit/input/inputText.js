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

O$.InputText = {
  _init: function(componentId,
                  promptText,
                  promptTextClass,
                  rolloverClass,
                  focusedClass,
                  isDisable) {
    var inputText = O$(componentId);
    inputText._statePrompt = O$(componentId + "::statePrompt");
    inputText._promptText = promptText;
    
    function getClassName(param) {
      return (param == null) ? "" : param;
    }

    inputText._focusedClass = getClassName(focusedClass);
    inputText._rolloverClass = getClassName(rolloverClass);
    inputText._promptTextClass = getClassName(promptTextClass);

    inputText._addInClassName = function (className) {
      if (inputText.className.length > 0) {
        inputText.className = inputText.className + " " + className;
      } else {
        inputText.className = className;
      }
    }; // todo: replace usages of these methods with O$.setStyleMappings
    inputText._removeOfClassName = function (className) {
      if (inputText.className.length > 0) {
        inputText.className = inputText.className.replace(className, "");
      }
    };

    if (inputText._promptText) {
      if (inputText.value.length == 0 ||
          ((inputText.value == inputText._promptText) && inputText._statePrompt.value == "true")) {   // needed for FireFox, when press F5 key
        inputText.value = inputText._promptText;
        inputText._addInClassName(inputText._promptTextClass);
        inputText._statePrompt.value = true;
      }
    } else
      inputText._statePrompt.value = false;

    inputText.getValue = function() {
      if (inputText._focused)
        return inputText.value;

      if (inputText._statePrompt.value == "true")
        return "";
      return inputText.value;
    };

    inputText.setValue = function(value) {
      if (!value)
        value = "";
      inputText.value = value;
      if (inputText._focused)
        return;
      inputText._statePrompt.value = !value;
      if (value)
        inputText._removeOfClassName(inputText._promptTextClass);
      else {
        inputText._addInClassName(inputText._promptTextClass);
        if (inputText._promptText)
          inputText.value = inputText._promptText;
      }
    };

    O$.addEventHandler(inputText, "focus", function() {
      inputText._focused = true;
      if (focusedClass)
        inputText._addInClassName(inputText._focusedClass);

      if (inputText._promptText) {
        if ((inputText.value == inputText._promptText) && (inputText._statePrompt.value == "true")) {
          inputText.value = "";
          if (promptTextClass)
            inputText._removeOfClassName(inputText._promptTextClass);
          inputText._statePrompt.value = false;
        }
      }
    });

    O$.addEventHandler(inputText, "blur", function() {
      inputText._focused = false;
      if (focusedClass)
        inputText._removeOfClassName(inputText._focusedClass);

      if (inputText._promptText) {
        if ((inputText.value.length == 0)) {
          inputText.value = inputText._promptText;
          if (promptTextClass)
            inputText._addInClassName(inputText._promptTextClass);
          inputText._statePrompt.value = true;
        } else
          inputText._statePrompt.value = false;
      }
    });

    if (rolloverClass && (!isDisable)) {
      O$.addEventHandler(inputText, "mouseover", function() {
        inputText._addInClassName(inputText._rolloverClass);
      });
      O$.addEventHandler(inputText, "mouseout", function() {
        inputText._removeOfClassName(inputText._rolloverClass);
      });
    }

    if (O$.isMozillaFF())
      O$.addEventHandler(inputText, "keyup", function(evt) {
        if (evt.keyCode == 27) { // ESC key
          if (inputText.value == inputText._promptText) {
            inputText.value = "";
          }
        }
      });

  }

};