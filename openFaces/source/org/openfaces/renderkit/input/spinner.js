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
O$.Spinner = {
  _init: function(spinnerId,
                  minValue,
                  maxValue,
                  step,
                  cycled,
                  buttonClass,
                  rolloverButtonClass,
                  pressedButtonClass,
                  disabled,
                  onchange,
                  formatOptions) {
    var spinner = O$.initComponent(spinnerId, null, {
      getValue: function() {
        if (!spinner._field)
          return null;
        var value = O$.Dojo.Number.parse(spinner._field.value, formatOptions);
        if (isNaN(value)) {
          value = parseFloat(spinner._field.value, 10);
        }
        return !isNaN(value) ? value : null;
      },

      setValue: function(value, silent) {
        var newValue = value == null || isNaN(value)
                ? ""
                : value;
        var prevValue;
        if (!silent) {
          prevValue = spinner.getValue();
        }
        spinner._field.value = O$.Dojo.Number.format(newValue, formatOptions);
        if (!silent && newValue != prevValue) {
          notifyOfInputChanges(spinner);
        }
      }

    });

    var increaseButton = O$(spinnerId + "::increase_button");
    var decreaseButton = O$(spinnerId + "::decrease_button");
    var field = spinner._field;

    if (increaseButton && decreaseButton) {
      increaseButton.className = buttonClass;
      decreaseButton.className = buttonClass;
    }

    if (onchange) {
      spinner._onchange = function() {
        eval(onchange);
      };
    }

    formatOptions = formatOptions || {};
    if (formatOptions.type && formatOptions.type == "number") {
      formatOptions.type = "decimal";
    }

    if (!disabled) {
      field.onkeypress = function(e) {
        var evt = O$.getEvent(e);

        var valueBefore = spinner.getValue();
        setTimeout(function() {
          var valueAfter = spinner.getValue();
          if (valueBefore == valueAfter)
            return;
          notifyOfInputChanges(spinner);
        }, 1);
        evt.cancelBubble = true;
      };

      field.onblur = function() {
        checkValueForBounds(spinner);
      };

      increaseButton.onmouseup = function () {
        O$.setStyleMappings(increaseButton, {
          pressed: null
        });
      };

      decreaseButton.onmouseup = function () {
        O$.setStyleMappings(decreaseButton, {
          pressed: null
        });
      };

      increaseButton.ondragstart = function(e) {
        O$.breakEvent(e);
      };
      decreaseButton.ondragstart = function(e) {
        O$.breakEvent(e);
      };

      increaseButton.onselectstart = function (e) {
        O$.breakEvent(e);
      };
      decreaseButton.onselectstart = function (e) {
        O$.breakEvent(e);
      };
    }

    decreaseButton.ondblclick = O$.repeatClickOnDblclick;
    increaseButton.ondblclick = O$.repeatClickOnDblclick;

    if (!disabled) {
      increaseButton.onmousedown = function(e) {
        spinner._focusHandler();

        O$.setStyleMappings(increaseButton, {
          pressed: pressedButtonClass
        });

        var value = spinner.getValue();
        if (!value && value != 0) {
          spinner.setValue(minValue, true);
        } else if (value + step <= maxValue) {
          spinner.setValue(value + step, true);
        } else {
          if (cycled) {
            spinner.setValue(minValue, true);
          } else {
            spinner.setValue(maxValue, true);
          }
        }
        if (value != spinner.getValue()) {
          notifyOfInputChanges(spinner);
        }
        O$.breakEvent(e);
      };
      decreaseButton.onmousedown = function(e) {
        spinner._focusHandler();

        O$.setStyleMappings(decreaseButton, {
          pressed: pressedButtonClass
        });

        var value = spinner.getValue();
        if (!value && value != 0) {
          spinner.setValue(minValue, true);
        } else if (value - step >= minValue) {
          spinner.setValue(value - step, true);
        }
        else {
          if (cycled) {
            spinner.setValue(maxValue, true);
          } else {
            spinner.setValue(minValue, true);
          }
        }
        if (value != spinner.getValue()) {
          notifyOfInputChanges(spinner);
        }
        O$.breakEvent(e);
      };
      field._oldInkeydown = field.onkeydown;

      field.onkeydown = function(e) {
        var evt = O$.getEvent(e);
        if (!evt) return;
        var keyCode = evt.keyCode;
        if (keyCode == 38) { // Up key
          increaseButton.onmousedown(e);
        } else if (keyCode == 40) { // Down key
          decreaseButton.onmousedown(e);
        }
        if (field._oldInkeydown)
          field._oldInkeydown(e);
      };

      field._oldOnkeyup = field.onkeyup;
      field.onkeyup = function(e) {
        var evt = O$.getEvent(e);
        if (!evt) return;
        var keyCode = evt.keyCode;
        if (keyCode == 38) { // Up key
          increaseButton.onmouseup(e);
        } else if (keyCode == 40) { // Down key
          decreaseButton.onmouseup(e);
        }
        if (field._oldOnkeyup)
          field._oldOnkeyup(e);
      };
    }

    if (rolloverButtonClass) {
      O$.addMouseOverListener(spinner, function() {
        if (spinner && spinner._containerClass != spinner._rolloverContainerClass)
          spinner.className = spinner._rolloverContainerClass;
        if (spinner != field && spinner._fieldClass != spinner._rolloverFieldClass)
          field.className = spinner._rolloverFieldClass;
        if (increaseButton && decreaseButton) {
          O$.setStyleMappings(increaseButton, {
            mouseover: rolloverButtonClass
          });

          O$.setStyleMappings(decreaseButton, {
            mouseover: rolloverButtonClass
          });
        }
        O$.repaintAreaForOpera(spinner, true);
      });
      O$.addMouseOutListener(spinner, function() {
        if (spinner && spinner._containerClass != spinner._rolloverContainerClass)
          spinner.className = spinner._containerClass;
        if (spinner._fieldClass != spinner._rolloverFieldClass)
          field.className = spinner._fieldClass;
        if (increaseButton && decreaseButton) {
          O$.setStyleMappings(increaseButton, {
            mouseover: null
          });

          O$.setStyleMappings(decreaseButton, {
            mouseover: null
          });
        }

        O$.repaintAreaForOpera(spinner, true);
      });
    }

    function notifyOfInputChanges(spinner) {
      if (spinner._onchange)
        spinner._onchange();
    }

    function checkValueForBounds(spinner) {
      var value = spinner.getValue();
      if (value != null) {
        if (value < minValue) {
          spinner.setValue(minValue);
        }
        if (value > maxValue) {
          spinner.setValue(maxValue);
        }
        spinner.setValue(value, true);
      } else {
        if (spinner._field.value != "") {
          spinner._field.value = "";
          notifyOfInputChanges(spinner);
        }
      }
    }
  }
};