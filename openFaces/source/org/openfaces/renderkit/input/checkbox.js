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
O$.Checkbox = {

  _init: function(checkboxId, stateCount, images, disabled) {

    var checkbox = O$(checkboxId);

    if (images) {

      // image-based checkbox

      checkbox._state = O$(checkboxId + "::state");
      checkbox._stateCount = stateCount;
      checkbox._images = images;
      checkbox._disabled = disabled;

      for (stateKey in images) {
        var effects = images[stateKey];
        for (effectKey in effects) {
          O$.preloadImage(effects[effectKey]);
        }
      }

      updateImage(checkbox); // Firefox page reload keeps form values

      // using "disabledState" instead of "disabled"
      // because of standard "isDisabled" property
      checkbox.setDisabledState = function(flag) {
        this._disabled = flag;
        updateImage(this);
      }

      checkbox.isDisabledState = function() {
        return this._disabled;
      }

      checkbox.setSelected = function(flag) {
        this._state.value = flag ? "on" : "off";
        updateImage(checkbox);
      }

      checkbox.isSelected = function() {
        return this._state.value === "on";
      }

      checkbox.setChecked = checkbox.setSelected; // alias
      checkbox.isChecked = checkbox.isSelected; // alias

      checkbox.setDefined = function(flag) {
        if (flag) {
          if (this._state.value === "nil") {
            this._state.value = "off";
          }
        } else {
          this._state.value = "nil";
        }
        updateImage(checkbox);
      }

      checkbox.isDefined = function() {
        return this._state.value !== "nil";
      }

      O$.addEventHandler(checkbox, "click",
        function(e) {
          if (!checkbox._disabled) {
            checkbox._pressed = false;
            nextState(checkbox);
          }
          O$.preventDefaultEvent(e); // no form submission
        }
      );

      O$.addEventHandler(checkbox, "mouseover",
        function(e) {
          if (!checkbox._disabled) {
            checkbox._rollover = true;
            updateImage(checkbox);
          }
          // no form URL in status bar
          if (e && e.preventDefault) {
            e.preventDefault();
          } else {
            return true; // IE
          }
        }
      );

      O$.addEventHandler(checkbox, "mouseout",
        function() {
          if (!checkbox._disabled) {
            checkbox._rollover = false;
            checkbox._pressed = false;
            updateImage(checkbox);
          }
        }
      );

      O$.addEventHandler(checkbox, "mousedown",
        function() {
          if (!checkbox._disabled) {
            checkbox._pressed = true;
            updateImage(checkbox);
          }
        }
      );

      O$.addEventHandler(checkbox, "keydown",
        function(e) {
          if (!checkbox._disabled) {
            if (isSpacebar(e)) {
              checkbox._pressed = true;
              updateImage(checkbox);
            }
          }
        }
      );

      // spacebar also fires "click" at non-Opera browsers
      if (O$.isOpera()) {
        O$.addEventHandler(checkbox, "keypress",
          function(e) {
            if (!checkbox._disabled) {
              if (e.which === 32) {
                checkbox._pressed = false;
                nextState(checkbox);
              }
            }
          }
        );
      }

      //...

    } else {

      // html checkbox

      checkbox.setDisabledState = function(flag) { this.disabled = flag; }
      checkbox.isDisabledState = function() { return this.disabled; }
      checkbox.setSelected = function(flag) { this.checked = flag; }
      checkbox.isSelected = function() { return this.checked; }
      checkbox.setChecked = checkbox.setSelected; // alias
      checkbox.isChecked = checkbox.isSelected; // alias
      checkbox.setDefined = function() { } // do nothing
      checkbox.isDefined = function() { return true; }

    }

    var stateTable = {
      2: {
        "off": "on",
        "on": "off"
      },
      3: {
        "off": "nil",
        "nil": "on",
        "on": "off"
      }
    }

    function nextState(checkbox) {
      checkbox._state.value = stateTable[checkbox._stateCount][checkbox._state.value];
      updateImage(checkbox);
    }

    function updateImage(checkbox) {
      var effect;
      if (checkbox._disabled) {
        effect = "disabled";
      } else if (checkbox._pressed) {
        effect = "pressed";
      } else if (checkbox._rollover) {
        effect = "rollover";
      } else {
        effect = "plain";
      }
      checkbox.src = checkbox._images[checkbox._state.value][effect];
    }

    function isSpacebar(e) {
      var evt = O$.getEvent(e);
      return (evt.which || evt.keyCode) === 32;
    }

  }

};
