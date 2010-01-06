/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
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
                  disabled) {
    var inputText = O$.initComponent(componentId, {
      rollover: !disabled ? rolloverClass : null,
      focused: focusedClass
    }, {
      getValue: function() {
        if (inputText._focused)
          return inputText.value;

        if (statePrompt.value == "true")
          return "";
        return inputText.value;
      },

      setValue: function(value) {
        if (!value)
          value = "";
        inputText.value = value;
        if (inputText._focused)
          return;
        statePrompt.value = !value;
        O$.setStyleMappings(inputText, {prompt: (value ? null : promptTextClass)});
        if (!value && promptText)
            inputText.value = promptText;
      }

    });
    var statePrompt = O$(componentId + "::statePrompt");

    if (promptText) {
      if (inputText.value.length == 0 ||
          ((inputText.value == promptText) && statePrompt.value == "true")) {   // needed for FireFox, when press F5 key
        inputText.value = promptText;
        O$.setStyleMappings(inputText, {prompt: promptTextClass});
        statePrompt.value = true;
      }
    } else
      statePrompt.value = false;


    O$.addEventHandler(inputText, "focus", function() {
      inputText._focused = true;

      if (promptText) {
        if ((inputText.value == promptText) && (statePrompt.value == "true")) {
          inputText.value = "";
          O$.setStyleMappings(inputText, {prompt: null});
          statePrompt.value = false;
        }
      }
    });

    O$.addEventHandler(inputText, "blur", function() {
      inputText._focused = false;

      if (promptText) {
        if ((inputText.value.length == 0)) {
          inputText.value = promptText;
          O$.setStyleMappings(inputText, {prompt: promptTextClass});
          statePrompt.value = true;
        } else
          statePrompt.value = false;
      }
    });

    if (O$.isMozillaFF())
      O$.addEventHandler(inputText, "keyup", function(evt) {
        if (evt.keyCode == 27) { // ESC key
          if (inputText.value == promptText) {
            inputText.value = "";
          }
        }
      });

  }

};