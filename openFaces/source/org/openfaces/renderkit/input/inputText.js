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

O$._initInputText = function(componentId,
                             promptText,
                             styleClass,
                             promptTextClass,
                             rolloverClass,
                             focusedClass,
                             isDisable) {
  var inputText = O$(componentId);
  inputText._statePrompt = O$(componentId + "::statePrompt");
  inputText._promptText = promptText;
  inputText._styleClass = O$._inputText_getClassName(styleClass);
  inputText._focusedClass = O$._inputText_getClassName(focusedClass);
  inputText._rolloverClass = O$._inputText_getClassName(rolloverClass);
  inputText._promptTextClass = O$._inputText_getClassName(promptTextClass);

  inputText._addInClassName = function (className) {
    if (inputText.className.length > 0) {
      inputText.className = inputText.className + " " + className;
    } else {
      inputText.className = className;
    }
  }
  inputText._removeOfClassName = function (className) {
    if (inputText.className.length > 0) {
      inputText.className = inputText.className.replace(className, "");
    }
  }

  if (inputText._promptText) {
    if (inputText.value.length == 0 ||
        ((inputText.value == inputText._promptText) && inputText._statePrompt.value == 'true')) {   // needed for FireFox, when press F5 key
      inputText.value = inputText._promptText;
      inputText._addInClassName(inputText._promptTextClass);
      inputText._statePrompt.value = true;
    }
  } else
    inputText._statePrompt.value = false;

  O$.addEventHandler(inputText, 'focus', function() {
    if (focusedClass)
      inputText._addInClassName(inputText._focusedClass);

    if (inputText._promptText) {
      if ((inputText.value == inputText._promptText) && (inputText._statePrompt.value == 'true')) {
        inputText.value = "";
        if (promptTextClass)
          inputText._removeOfClassName(inputText._promptTextClass);
        inputText._statePrompt.value = false;
      }
    }
  });

  O$.addEventHandler(inputText, 'blur', function() {
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
    O$.addEventHandler(inputText, 'mouseover', function() {
      inputText._addInClassName(inputText._rolloverClass);
    });
    O$.addEventHandler(inputText, 'mouseout', function() {
      inputText._removeOfClassName(inputText._rolloverClass);
    });
  }

  if (O$.isMozillaFF())
    O$.addEventHandler(inputText, 'keyup', function(evt) {
      if (evt.keyCode == 27) { // ESC key
        if (inputText.value == inputText._promptText) {
          inputText.value = "";
        }
      }
    });

}

O$._inputText_getClassName = function(param) {
  return (param == null) ? '' : param;
}
