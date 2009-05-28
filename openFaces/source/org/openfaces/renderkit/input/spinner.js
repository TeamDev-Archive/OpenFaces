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
O$._initSpinner = function(spinnerId,
                           minValue,
                           maxValue,
                           step,
                           cycled,
                           buttonClass,
                           rolloverButtonClass,
                           pressedButtonClass,
                           disabled,
                           onchange) {
  var spinner = O$(spinnerId);
  spinner._increaseButton = O$(spinnerId + "::increase_button");
  spinner._decreaseButton = O$(spinnerId + "::decrease_button");

  var increaseButton = spinner._increaseButton;
  var decreaseButton = spinner._decreaseButton;
  var field = spinner._field;

  spinner._buttonClass = O$._dropdown_getClassName(buttonClass);

  if (increaseButton && decreaseButton) {
    increaseButton.className = spinner._buttonClass;
    decreaseButton.className = spinner._buttonClass;
  }

  if (onchange) {
    spinner.onchange = function() {
      eval(onchange);
    }
  }

  spinner._rolloverButtonClass = rolloverButtonClass;
  spinner._pressedButtonClass = pressedButtonClass;

  if (!disabled) {
    field.onkeypress = function(e) {
      var evt = O$.getEvent(e);

      var valueBefore = field.value;
      setTimeout(function() {
        var valueAfter = field.value;
        if (valueBefore == valueAfter)
          return;
        notifyOfInputChanges(spinner);
      }, 1);
      evt.cancelBubble = true;
    }

    field.onblur = function(e) {
      checkValueForBounds(spinner);
    }

    increaseButton.onmouseup = function (e) {
      O$.setElementStyleMappings(increaseButton, {
        pressed: null
      });
    }

    decreaseButton.onmouseup = function (e) {
      O$.setElementStyleMappings(decreaseButton, {
        pressed: null
      });
    }

    increaseButton.ondragstart = function(e) {
      O$.breakEvent(e);
    }
    decreaseButton.ondragstart = function(e) {
      O$.breakEvent(e);
    }

    increaseButton.onselectstart = function (e) {
      O$.breakEvent(e);
    }
    decreaseButton.onselectstart = function (e) {
      O$.breakEvent(e);
    }
  }

  decreaseButton.ondblclick = O$.repeatClickOnDblclick;
  increaseButton.ondblclick = O$.repeatClickOnDblclick;

  spinner.getValue = function() {
    if (!spinner._field)
      return null;
    var value = parseInt(spinner._field.value, 10);
    return !isNaN(value) ? value : null;
  }

  spinner.setValue = function(value) {
    var newValue = value == null || isNaN(value)
            ? ""
            : value;
    var prevValue = spinner._field.value;
    spinner._field.value = newValue;
    if (newValue != parseInt(prevValue, 10)) {
      notifyOfInputChanges(spinner);
    }

  }

  if (!disabled) {
    spinner._increaseButton.onmousedown = function(e) {
      checkValueForBounds(spinner);

      O$.setElementStyleMappings(increaseButton, {
        pressed: spinner._pressedButtonClass
      });

      var value = parseInt(spinner._field.value, 10);
      if (!value && value != 0) {
        spinner._field.value = minValue;
      } else if (value + step <= maxValue) {
        spinner._field.value = value + step;
      } else {
        if (cycled) {
          spinner._field.value = minValue;
        } else {
          spinner._field.value = maxValue;
        }
      }
      if (value != parseInt(spinner._field.value, 10)) {
        notifyOfInputChanges(spinner);
      }
      O$.breakEvent(e);
    };
    spinner._decreaseButton.onmousedown = function(e) {

      checkValueForBounds(spinner);

      O$.setElementStyleMappings(decreaseButton, {
        pressed: spinner._pressedButtonClass
      });

      var value = parseInt(spinner._field.value, 10);
      if (!value && value != 0) {
        spinner._field.value = minValue;
      } else if (value - step >= minValue) {
        spinner._field.value = value - step;
      }
      else {
        if (cycled) {
          spinner._field.value = maxValue;
        } else {
          spinner._field.value = minValue;
        }
      }
      if (value != parseInt(spinner._field.value, 10)) {
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
        checkValueForBounds(spinner);
        spinner._increaseButton.onmousedown(e);
      } else if (keyCode == 40) { // Down key
        checkValueForBounds(spinner);
        spinner._decreaseButton.onmousedown(e);
      }
      if (field._oldInkeydown)
        field._oldInkeydown(e);
    }

    field._oldOnkeyup = field.onkeyup;
    field.onkeyup = function(e) {
      var evt = O$.getEvent(e);
      if (!evt) return;
      var keyCode = evt.keyCode;
      if (keyCode == 38) { // Up key
        spinner._increaseButton.onmouseup(e);
      } else if (keyCode == 40) { // Down key        
        spinner._decreaseButton.onmouseup(e);
      }
      if (field._oldOnkeyup)
        field._oldOnkeyup(e);
    }
  }

  if (rolloverButtonClass) {
    spinner._dropDownMouseOver = function() {
      if (spinner && spinner._containerClass != spinner._rolloverContainerClass)
        spinner.className = spinner._rolloverContainerClass;
      if (spinner != field && spinner._fieldClass != spinner._rolloverFieldClass)
        field.className = spinner._rolloverFieldClass;
      if (increaseButton && decreaseButton && spinner._buttonClass != spinner._rolloverButtonClass) {
        O$.setElementStyleMappings(increaseButton, {
          mouseover: spinner._rolloverButtonClass
        });

        O$.setElementStyleMappings(decreaseButton, {
          mouseover: spinner._rolloverButtonClass
        });
      }
      O$.repaintAreaForOpera(spinner, true);
    }

    spinner._dropDownMouseOut = function() {
      if (spinner && spinner._containerClass != spinner._rolloverContainerClass)
        spinner.className = spinner._containerClass;
      if (spinner._fieldClass != spinner._rolloverFieldClass)
        field.className = spinner._fieldClass;
      if (increaseButton && decreaseButton && spinner._buttonClass != spinner._rolloverButtonClass) {

        O$.setElementStyleMappings(increaseButton, {
          mouseover: null
        });

        O$.setElementStyleMappings(decreaseButton, {
          mouseover: null
        });
      }

      O$.repaintAreaForOpera(spinner, true);
    }

    O$.addMouseOverListener(spinner, spinner._dropDownMouseOver);

    O$.addMouseOutListener(spinner, spinner._dropDownMouseOut);
  }

  function notifyOfInputChanges(spinner) {
    if (spinner.onchange)
      spinner.onchange();
  }

  function checkValueForBounds(spinner) {
    var value = parseInt(spinner._field.value, 10);
    if (value) {
      if (value < minValue) {
        spinner._field.value = minValue;
        notifyOfInputChanges(spinner);
      }
      if (value > maxValue) {
        spinner._field.value = maxValue;
        notifyOfInputChanges(spinner);
      }
    }
  }
}
