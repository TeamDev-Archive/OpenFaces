/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
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
                  disabled,
                  promptVisible) {
    var promptVisibleFieldName = componentId + "::promptVisible";
    var inputText = O$.initComponent(componentId, {
      rollover: !disabled ? rolloverClass : null,
      focused: focusedClass
    }, {
      getValue: function() {
        if (inputText._focused)
          return inputText.value;
        if (promptVisible.value == "true")
          return "";
        return inputText.value;
      },

      setValue: function(value) {
        if (!value)
          value = "";
        inputText.value = value;
        if (inputText._focused)
          return;
        inputText.setPromptVisible(!value);
        O$.setStyleMappings(inputText, {prompt:(value ? null : promptTextClass)});
        if (!value && promptText)
          inputText.value = promptText;
      },

      setPromptVisible: function(value) {
        O$.setHiddenField(O$(componentId), promptVisibleFieldName, value);
        promptVisible = value;
      }
    });

    if (promptText) {
      if (inputText.value.length == 0 ||
              ((inputText.value == promptText) && promptVisible == true)) {   // needed for FireFox, when press F5 key
        inputText.value = promptText;
        O$.setStyleMappings(inputText, {prompt:promptTextClass});
        O$.addLoadEvent(function () {
          inputText.setPromptVisible(true)
        });
      }
    } else {
      O$.addLoadEvent(function () {
        inputText.setPromptVisible(false)
      });
    }

    if (!inputText._eventsInitialized) {
      // events might have already been initialized when _init is called in case when <o:inputText> was reloaded with
      // Ajax, where jsf.js has a special case for reloading <input> tags which are not actually reloaded but their
      // attributes copied instead
      inputText._eventsInitialized = true;
      O$.addEventHandler(inputText, "focus", function() {
        inputText._focused = true;

        if (promptText) {
          if ((inputText.value == promptText) && (promptVisible == true)) {
            inputText.value = "";
            O$.setStyleMappings(inputText, {prompt: null});
            inputText.setPromptVisible(false);
          }
        }
      });

      O$.addEventHandler(inputText, "blur", function() {
        inputText._focused = false;

        if (promptText) {
          if (inputText.value.length == 0) {
            inputText.value = promptText;
            O$.setStyleMappings(inputText, {prompt: promptTextClass});
            inputText.setPromptVisible(true);
          } else
            inputText.setPromptVisible(false);
        }
      });
    }

    if (O$.isMozillaFF())
      O$.addEventHandler(inputText, "keyup", function(evt) {
        if (evt.keyCode == 27) { // ESC key
          if (inputText.value == promptText) {
            inputText.value = "";
          }
        }
      });

    O$.initUnloadableComponent(inputText);
  }

};