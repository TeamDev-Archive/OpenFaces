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

O$.InputSecret = {

  _init:function (inputSecretId, interval, duration, replacement, promptVisible, promptText, promptTextClass, rolloverClass, focusedClass) {
    var promptVisibleFieldName = inputSecretId + "::promptVisible";
    var substitutionalElement = O$(inputSecretId + "::inputSecretValue");
    var checker = new Array();
    var timer = new Array();
    var passwordVisible = false;
    var inputSecret = O$.initComponent(inputSecretId, {
      rollover:rolloverClass,
      focused:focusedClass
    }, {

      getValue:function () {
        if (substitutionalElement._focused)
          return inputSecret.value;
        if (promptVisible.value == "true")
          return "";
        return inputSecret.value;
      },

      setValue:function (value) {
        if (!value) {
          value = "";
        }

        substitutionalElement.value = value;
        inputSecret.value = value;

        if (!passwordVisible) {
          runValueConverter(inputSecretId);
        }

        if (substitutionalElement._focused) {
          return;
        }

        inputSecret.setPromptVisible(!value);
        O$.setStyleMappings(substitutionalElement, {prompt:(value ? null : promptTextClass)});
        if (!value && promptText)
          substitutionalElement.value = promptText;
      },

      setPromptVisible:function (value) {
        O$.setHiddenField(O$(inputSecretId), promptVisibleFieldName, value);
        promptVisible = value;
      },

      showPassword:function () {
        if (substitutionalElement.value == promptText && promptVisible) {
          O$.setStyleMappings(substitutionalElement, {prompt:null});
          inputSecret.setPromptVisible(false);
        }

        stopValueConverter(inputSecretId);
        passwordVisible = true;
        substitutionalElement.value = inputSecret.value;
      },

      hidePassword:function () {
        passwordVisible = false;

        if (substitutionalElement.value.length != 0){
          runValueConverter(inputSecretId);
        }

        if (substitutionalElement.value.length == 0 && promptText ){
          O$.setStyleMappings(substitutionalElement, {prompt:promptTextClass});
          substitutionalElement.value = promptText;
          inputSecret.setPromptVisible(true);
        }
      },

      generatePassword:function (lengthOfSyllable, useNumbers) {
        lengthOfSyllable = typeof(syllableNum) != 'undefined' ? syllableNum : 3;
        useNumbers = typeof(useNums) != 'undefined' ? useNums : true;

        var consonantLetters = "bcdfghklmnprstvzjqwx";
        var vowelLetters = "aeiouy";
        var allLetters = consonantLetters + vowelLetters;

        var password = "";
        var numberProbability = 0;
        var numberProbabilityStep = 0.25;	// Number probability between syllable

        for (var i = 0; i < lengthOfSyllable; ++i) {
          if (Math.round(Math.random())) {
            password += getRandomChar(consonantLetters).toUpperCase() + getRandomChar(vowelLetters) + getRandomChar(allLetters);
          } else {
            password += getRandomChar(vowelLetters).toUpperCase() + getRandomChar(consonantLetters);
          }
          if (useNumbers && Math.round(Math.random() + numberProbability)) {
            password += getRandomNumber(0, 9);
            numberProbability += numberProbabilityStep;
          }
        }

        inputSecret.setValue(password);
      }

    });

    // set timers
    checker.push(inputSecretId);
    timer.push(inputSecretId);

    if (!promptText) {
      O$.addLoadEvent(function () {
        inputSecret.setPromptVisible(false)
      });
    } else {
      if (substitutionalElement.value.length == 0 ||
              ((substitutionalElement.value == promptText) && promptVisible == true)) {   // needed for FireFox, when press F5 key
        substitutionalElement.value = promptText;
        O$.setStyleMappings(substitutionalElement, {prompt:promptTextClass});
        O$.addLoadEvent(function () {
          inputSecret.setPromptVisible(true);
        });
      }
    }

    // bind event
    O$.addEventHandler(substitutionalElement, "focus", function () {
      //hide prompt text
      substitutionalElement._focused = true;
      if (promptText) {
        if ((substitutionalElement.value == promptText) && (promptVisible == true)) {
          substitutionalElement.value = "";
          O$.setStyleMappings(substitutionalElement, {prompt:null});
          inputSecret.setPromptVisible(false);
        }
      }

      //check inputSecret value
      runValueConverter(inputSecretId);
    });

    O$.addEventHandler(substitutionalElement, "blur", function () {
      //show/hide prompt text
      substitutionalElement._focused = false;
      if (promptText) {
        if (substitutionalElement.value.length == 0) {
          substitutionalElement.value = promptText;
          O$.setStyleMappings(substitutionalElement, {prompt:promptTextClass});
          inputSecret.setPromptVisible(true);
        } else
          inputSecret.setPromptVisible(false);
      }

      //stop checking
      clearTimeout(checker[inputSecretId]);
    });

    if (O$.isMozillaFF()) {
      O$.addEventHandler(substitutionalElement, "keyup", function (event) {
        if (event.keyCode == 27) { // ESC key
          if (substitutionalElement.value == promptText) {
            substitutionalElement.value = "";
            inputSecret.value = "";
          }
        }
      });
    }

    O$.initUnloadableComponent(substitutionalElement);

    var setPassword = function (id, str) {
      var tmp = "";
      for (var i = 0; i < str.length; i++) {
        if (str.charAt(i) == unescape(replacement)){
          var currentSymbol = inputSecret.value;
          tmp = tmp + currentSymbol.charAt(i);
        }
        else {
          tmp = tmp + str.charAt(i);
        }
      }
      inputSecret.value = tmp;
    }

    var check = function (id, oldValue, initialCall) {
      var bullets = substitutionalElement.value;

      if (oldValue != bullets) {
        setPassword(id, bullets);
      }

      if (oldValue != bullets && !passwordVisible) {
        if (bullets.length > 1) {
          var tmp = '';
          for (i = 0; i < bullets.length - 1; i++) {
            tmp = tmp + unescape(replacement);
          }
          tmp = tmp + bullets.charAt(bullets.length - 1);

          substitutionalElement.value = tmp;
        }

        clearTimeout(timer[id]);
        timer[id] = setTimeout(function () {
          convertLastChar(id);
        }, duration);
      }

      if (!initialCall) {
        var substitutionalElementValue = substitutionalElement.value;
        runValueConverter(id, substitutionalElementValue, false);
      }
    }

    var convertLastChar = function (id) {
      if (substitutionalElement.value != "" && ((substitutionalElement.value != promptText) && !promptVisible)) {
        var tmp = "";
        for (var i = 0; i < substitutionalElement.value.length; i++) {
          tmp = tmp + unescape(replacement);
        }
        substitutionalElement.value = tmp;
      }
    }

    var runValueConverter = function (id, oldValue, initialCall) {
      clearTimeout(checker[id]);
      checker[id] = setTimeout(function () {
        check(id, oldValue, initialCall);
      }, interval);
    }

    var stopValueConverter = function (id) {
      clearTimeout(checker[id]);
      clearTimeout(timer[id]);
    }

    var getRandomChar = function (a) {
      return a.charAt(getRandomNumber(0, a.length - 1));
    }

    var getRandomNumber = function (from, to) {
      from = typeof(from) != 'undefined' ? from : 0;
      to = typeof(to) != 'undefined' ? to : from + 1;
      return Math.round(from + Math.random() * (to - from));
    };
  }
};