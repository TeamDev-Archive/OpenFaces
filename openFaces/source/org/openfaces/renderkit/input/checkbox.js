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

  _init: function(checkboxId, images, styles, tristate, disabled) {

    var checkbox = O$(checkboxId);

    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    checkbox._styleClass = getClassName("styleClass");
    checkbox._rolloverClass = getClassName("rolloverClass");
    checkbox._focusedClass = getClassName("focusedClass");
    checkbox._selectedClass = getClassName("selectedClass");
    checkbox._unselectedClass = getClassName("unselectedClass");

    if (images) {

      // image-based checkbox

      checkbox._state = O$(checkboxId + "::state");
      checkbox._images = images;
      checkbox._tristate = tristate;
      checkbox._disabled = disabled;

      if (disabled) {
        checkbox._tabIndex = checkbox.tabIndex;
        checkbox.tabIndex = -1;
      }

      for (stateKey in images) {
        var effects = images[stateKey];
        for (effectKey in effects) {
          O$.preloadImage(effects[effectKey]);
        }
      }

      updateImage(checkbox); // Firefox page reload keeps form values

      // using "getDisabled" instead of "isDisabled "
      // because of standard "isDisabled" property
      checkbox.getDisabled = function() {
        return this._disabled;
      }

      checkbox.setDisabled = function(flag) {
        if (this._disabled !== flag) {
          this._disabled = flag;
          if (flag) {
            checkbox._tabIndex = checkbox.tabIndex;
            checkbox.tabIndex = -1;
          } else {
            checkbox.tabIndex = checkbox._tabIndex;
          }
          updateImage(this);
        }
      }

      checkbox.isSelected = function() {
        return this._state.value === "on";
      }

      checkbox.setSelected = function(flag) {
        if (this.isSelected() !== flag) {
          this._state.value = flag ? "on" : "off"
          updateImage(checkbox);
        }
      }

      checkbox.isDefined = function() {
        return this._state.value !== "nil";
      }

      checkbox.setDefined = function(flag) {
        if (this.isDefined() !== flag) {
          if (flag) {
            if (this._state.value === "nil") {
              this._state.value = "off";
            }
          } else {
            this._state.value = "nil";
          }
          updateImage(checkbox);
        }
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

    } else {

      // html checkbox

      checkbox.setDisabled = function(flag) { this.disabled = flag; }
      checkbox.getDisabled = function() { return this.disabled; }
      checkbox.setSelected = function(flag) { this.checked = flag; }
      checkbox.isSelected = function() { return this.checked; }
      checkbox.setDefined = function() { } // do nothing
      checkbox.isDefined = function() { return true; }

      if (checkbox._selectedClass || checkbox._unselectedClass) {
        O$.addEventHandler(checkbox, "click", function() {
          updateStateStyle(checkbox);
        });
      }

    }

    updateStateStyle(checkbox);

    if (checkbox._focusedClass) {
      O$.addEventHandler(checkbox, "focus", function() {
        if (!checkbox.getDisabled()) {
          O$.setElementStyleMappings(checkbox, { focused: checkbox._focusedClass });
        }
      });
      O$.addEventHandler(checkbox, "blur", function() {
        if (!checkbox.getDisabled()) {
          O$.setElementStyleMappings(checkbox, { focused: null });
        }
      });
    }

    if (checkbox._rolloverClass) {
      O$.addEventHandler(checkbox, "mouseover", function() {
        if (!checkbox.getDisabled()) {
          O$.setElementStyleMappings(checkbox, { rollover: checkbox._rolloverClass });
        }
      });
      O$.addEventHandler(checkbox, "mouseout", function() {
        if (!checkbox.getDisabled()) {
          O$.setElementStyleMappings(checkbox, { rollover: null });
        }
      });
    }

    if (checkbox._selectedClass || checkbox._unselectedClass) {
      O$.addEventHandler(checkbox, "click", function() {
        if (!checkbox.getDisabled()) { // disabled image-based checkbox receives clicks
          updateStateStyle(checkbox);
        }
      });
    }

    var stateTable = {
      "bistate": {
        "off": "on",
        "on": "off"
      },
      "tristate": {
        "off": "nil",
        "nil": "on",
        "on": "off"
      }
    }

    function nextState(checkbox) {
      checkbox._state.value = stateTable[checkbox._tristate ? "tristate" : "bistate"][checkbox._state.value];
      updateImage(checkbox);
      updateStateStyle(checkbox);
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

    function updateStateStyle(checkbox) {
      if (checkbox._selectedClass || checkbox._unselectedClass) {
        O$.setElementStyleMappings(checkbox, {
          selected: checkbox.isSelected() ? checkbox._selectedClass : null,
          unselected: (checkbox.isDefined() && !checkbox.isSelected()) ? checkbox._unselectedClass : null
        });
      }
    }

    function isSpacebar(e) {
      var evt = O$.getEvent(e);
      return (evt.which || evt.keyCode) === 32;
    }

  }

};
