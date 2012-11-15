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
  
  _init: function (inputSecretId) {
    var inputSecret = O$.initComponent(inputSecretId,
     {
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

    var defaults = {
      interval:      0,
      duration:      0,
      replacement:   '#',
      prefix:        'password_'
    }

    var opts    = defaults;
    var checker = new Array();
    var timer   = new Array();

    // set timers
    checker.push(inputSecretId);
    timer.push(inputSecretId);

    var attributesForCurrentElement = inputSecret.attributes;
    var attributesForSubstitutionalElement = new Object();

    for(var i=0;i<attributesForCurrentElement.length;i++){
      var currentAttribute = attributesForCurrentElement[i];
      var currentAttributeValue = currentAttribute.value;
      if(currentAttribute.name == "name" || currentAttribute.name == "id"){
        currentAttributeValue = opts.prefix + currentAttributeValue;
      }

      attributesForSubstitutionalElement[currentAttribute.name] = currentAttributeValue;
    }

    attributesForSubstitutionalElement.type = "text";
    attributesForSubstitutionalElement.autocomplete = "off";

    inputSecret.style.display = 'none';

    var substitutionalElement = document.createElement('input');

    for(var key in attributesForSubstitutionalElement){
      var value = attributesForSubstitutionalElement[key];
      try{
        substitutionalElement[key]=value;
      }catch(e){
        console.log("Error:"+ e.message+"; key="+key+" value="+value);
      }
    }

    inputSecret.parentNode.insertBefore(substitutionalElement,inputSecret.nextSibling);

    // disable tabindex
    inputSecret.setAttribute('tabindex', '');
    // disable accesskey
    inputSecret.setAttribute('accesskey', '');

    // bind event
    O$.addEventHandler(substitutionalElement, "focus", function() {
      var id = getId(this.getAttribute('id'));
      clearTimeout(checker[id]);
      checker[id] = setTimeout("check('" + id + "', '','');", opts.interval);
    });

    O$.addEventHandler(substitutionalElement, "blur", function() {
      var id = getId(this.getAttribute('id'));
      clearTimeout(checker[id]);
    });

    getId = function(id) {
      var pattern = opts.prefix+'(.*)';
      var regex = new RegExp(pattern);
      regex.exec(id);
      id = RegExp.$1;

      return id;
    }

    setPassword = function(id, str) {

      var tmp = '';
      for (i=0; i < str.length; i++) {
        if (str.charAt(i) == opts.replacement) {
          var currentSymbol = inputSecret.value;
          tmp = tmp + currentSymbol.charAt(i);
        }
        else {
          tmp = tmp + str.charAt(i);
        }
      }
      inputSecret.value = tmp;
    }

    check = function(id, oldValue, initialCall) {

      var bullets = substitutionalElement.value;

      if (oldValue != bullets) {
        setPassword(id, bullets);
        if (bullets.length > 1) {
          var tmp = '';
          for (i=0; i < bullets.length-1; i++) {
            tmp = tmp + opts.replacement;
          }
          tmp = tmp + bullets.charAt(bullets.length-1);

          substitutionalElement.value = tmp;
        }

        clearTimeout(timer[id]);
        timer[id] = setTimeout("convertLastChar('" + id + "')", opts.duration);
      }

      if (!initialCall) {
        checker[id] = setTimeout("check('" + id + "', '" + substitutionalElement.value + "', false)", opts.interval);
      }
    }

    convertLastChar = function(id) {
      if (substitutionalElement.value != '') {
        var tmp = '';
        for (i=0; i < substitutionalElement.value.length; i++) {
          tmp = tmp + opts.replacement;
        }

        substitutionalElement.value=tmp;
      }
    }
  }
};