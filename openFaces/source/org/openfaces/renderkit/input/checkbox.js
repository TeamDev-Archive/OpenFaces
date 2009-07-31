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

  _init: function(checkboxId, images, styles, tristate, disabled, onchange) {

    var checkbox = O$(checkboxId);

    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    checkbox.className = getClassName("styleClass");

    checkbox._rolloverClass = getClassName("rolloverClass");
    checkbox._focusedClass = getClassName("focusedClass");
    checkbox._selectedClass = getClassName("selectedClass");
    checkbox._unselectedClass = getClassName("unselectedClass");
    checkbox._undefinedClass = getClassName("undefinedClass");

    checkbox._onchange = onchange;

    var isOpera = O$.isOpera();

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
          updateStyles(this);
        }
      }

      checkbox.isSelected = function() {
        return this._state.value === "on";
      }

      checkbox.setSelected = function(flag) {
        if (this.isSelected() !== flag) {
          this._state.value = flag ? "on" : "off"
          updateImage(this);
          updateStyles(this);
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
          updateImage(this);
          updateStyles(this);
        }
      }

      O$.addEventHandler(checkbox, "mousedown", function() {
        if (!checkbox._disabled) {
          checkbox._pressed = true;
          updateImage(checkbox);
        }
      });

      O$.addEventHandler(checkbox, "keydown", function(e) {
        if (!checkbox._disabled) {
          if (isSpacebar(e)) {
            checkbox._pressed = true;
            updateImage(checkbox);
          }
        }
      });

      // spacebar also fires "click" at non-Opera browsers
      if (isOpera) {
        O$.addEventHandler(checkbox, "keypress", function(e) {
          if (!checkbox._disabled) {
            if (e.which === 32) {
              checkbox._pressed = false;
              nextState(checkbox);
              fireOnChange(checkbox);
            }
          }
        });
      }

    } else {

      // html checkbox

      checkbox.setDisabled = function(flag) { this.disabled = flag; updateStyles(this); }
      checkbox.getDisabled = function() { return this.disabled; }
      checkbox.setSelected = function(flag) { this.checked = flag; updateStyles(this); }
      checkbox.isSelected = function() { return this.checked; }
      checkbox.setDefined = function() { } // do nothing
      checkbox.isDefined = function() { return true; }

    }

    if (needFixPosition(checkbox)) {
      checkbox._indents = {};
      checkbox._defaultIndent = O$.getNumericStyleProperty(checkbox, "margin-left");
    }

    updateStyles(checkbox);

    O$.addEventHandler(checkbox, "focus", function() {
      if (shouldProcessEvents(checkbox)) {
        checkbox._focused = true;
        updateStyles(checkbox);
      }
    });

    O$.addEventHandler(checkbox, "blur", function() {
      if (shouldProcessEvents(checkbox)) {
        checkbox._focused = false;
        updateStyles(checkbox);
      }
    });

    O$.addEventHandler(checkbox, "click", function(e) {
      if (shouldProcessEvents(checkbox)) {
        checkbox._pressed = false;
        if (checkbox._images) {
          nextState(checkbox);
          fireOnChange(checkbox);
        } else {
          updateStyles(checkbox);
        }
      }
      if (checkbox._images) {
        O$.preventDefaultEvent(e); // no form submission
      }
    });

    O$.addEventHandler(checkbox, "mouseover", function(e) {
      if (shouldProcessEvents(checkbox)) {
        checkbox._rollover = true;
        if (checkbox._images) {
          updateImage(checkbox);
        }
        updateStyles(checkbox);
      }
      if (checkbox._images) {
        // no form URL in status bar
        if (e && e.preventDefault) {
          e.preventDefault();
        } else {
          return true; // IE
        }
      }
    });

    O$.addEventHandler(checkbox, "mouseout", function() {
      if (shouldProcessEvents(checkbox)) {
        checkbox._rollover = false;
        if (checkbox._images) {
          checkbox._pressed = false;
          updateImage(checkbox);
        }
        updateStyles(checkbox);
      }
    });

    var stateTable = {
      bistate: {
        "off": "on",
        "on": "off"
      },
      tristate: {
        "off": "nil",
        "nil": "on",
        "on": "off"
      }
    }

    function nextState(checkbox) {
      checkbox._state.value = stateTable[checkbox._tristate ? "tristate" : "bistate"][checkbox._state.value];
      updateImage(checkbox);
      updateStyles(checkbox);
    }

    function fireOnChange(checkbox) {
      if (checkbox._onchange) {
        var event = O$.createEvent("change");
        var returnValue = checkbox._onchange(event);
        if (returnValue == undefined) { returnValue = event.returnValue; }
        return returnValue;
      }
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

    function shouldProcessEvents(checkbox) {
      return !checkbox._images || !checkbox._disabled;
    }

    function updateStyles(checkbox) {
      if (needFixPosition(checkbox)) {
        checkbox.style.marginLeft = checkbox._defaultIndent;
      }
      O$.setElementStyleMappings(checkbox, {
        selected: checkbox.isSelected() ? checkbox._selectedClass : null,
        unselected: (checkbox.isDefined() && !checkbox.isSelected()) ? checkbox._unselectedClass : null,
        _undefined: checkbox.isDefined() ? null  : checkbox._undefinedClass,
        rollover: checkbox._rollover ? checkbox._rolloverClass : null,
        focused: checkbox._focused ? checkbox._focusedClass : null
      });
      fixPosition(checkbox);
    }

    function fixPosition(checkbox) {
      if (needFixPosition(checkbox)) {
        var styleKey = getStyleKey(checkbox);
        var indent = checkbox._indents[styleKey];
        if (!indent) {
          indent = 4 + O$.getNumericStyleProperty(checkbox, "margin-left");
          checkbox._indents[styleKey] = indent;
        }
        checkbox.style.marginLeft = indent;
      }
    }

    function needFixPosition(checkbox) {
      return !isOpera && checkbox._images;
    }

    function getStyleKey(checkbox) {
      var result = checkbox.isDefined() ? (checkbox.isSelected() ? "selected" : "unselected") : "undefined";
      if (checkbox._rollover) { result += "+rollover"; }
      if (checkbox._focused) { result += "+focused"; }
      return result;
    }

  }

};
