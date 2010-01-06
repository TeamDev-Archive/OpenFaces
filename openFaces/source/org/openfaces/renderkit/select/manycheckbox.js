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

O$.ManyCheckbox = {

  _init: function(checkboxId, images, styles, checkboxItemCount, disabled, readonly, onchange) {
    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    var checkboxContainer = O$.initComponent(checkboxId, null, {
      className: getClassName("styleClass"),
      _rolloverClass: getClassName("rolloverClass"),
      _focusedClass: getClassName("focusedClass"),

      _checkboxItems: [],
      _checkboxElems: [],
      _checkboxItemCount: checkboxItemCount
    });
    checkboxContainer._styleClass = checkboxContainer.className;
    for (var i = 0; i < checkboxItemCount; i++) {
      var checkboxItemId = checkboxId + ":" + i;
      checkboxContainer._checkboxElems[i] = O$(checkboxItemId);

      O$.CheckboxItem._init(checkboxContainer, i, checkboxItemId, images, styles, disabled, readonly, onchange);
    }

    O$.addEventHandler(checkboxContainer, "mouseover", function(e) {
      if (!disabled) {
        checkboxContainer._rollover = true;
        O$.ManyCheckbox.updateContainerStyles(checkboxContainer);
        if (images) {
          // no form URL in status bar
          if (e && e.preventDefault) {
            e.preventDefault();
          } else {
            return true; // IE
          }
        }
      }
    });

    O$.addEventHandler(checkboxContainer, "mouseout", function() {
      if (!disabled) {
        checkboxContainer._rollover = false;
        O$.ManyCheckbox.updateContainerStyles(checkboxContainer);
      }
    });

  },

  updateContainerStyles : function(checkboxContainer) {
    var containerFocused = checkboxContainer._focused;
    var containerRollover = checkboxContainer._rollover;
    for (var i = 0; i < checkboxContainer._checkboxItems.length; i++) {
      containerFocused |= checkboxContainer._checkboxItems[i]._focused || checkboxContainer._checkboxItems[i]._pressed;
      containerRollover |= checkboxContainer._checkboxItems[i]._rollover;
    }

    O$.setStyleMappings(checkboxContainer, {
      rollover: containerRollover ? checkboxContainer._rolloverClass : null,
      focused: containerFocused ? checkboxContainer._focusedClass : null
    });
  }
};

O$.CheckboxItem = {

  _init: function(checkboxContainer, index, checkboxItemId, images, styles, disabled, readonly, onchange) {

    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    var isOpera = O$.isOpera();

    var checkboxItem;
    if (images) {
      checkboxItem = O$(checkboxItemId + "::image");
      checkboxItem._state = checkboxContainer._checkboxElems[index].checked ? "on" : "off";
      checkboxItem._images = images;
    } else {
      checkboxItem = O$(checkboxItemId);
      checkboxItem._state = checkboxItem.checked ? "on" : "off";
    }
    checkboxItem._container = checkboxContainer;
    checkboxItem._label = O$(checkboxItemId + "::label");
    checkboxItem._firstItem = index == 0;
    checkboxItem._lastItem = index == (checkboxContainer._checkboxItemCount - 1);

    checkboxItem._index = index;

    checkboxItem._enabledClass = getClassName("enabledClass");
    checkboxItem._disabledClass = getClassName("disabledClass");
    checkboxItem._focusedClass = getClassName("focusedItemClass");
    checkboxItem._rolloverClass = getClassName("rolloverItemClass");
    checkboxItem._pressedClass = getClassName("pressedItemClass");
    checkboxItem._selectedClass = getClassName("selectedItemClass");

    checkboxItem._onchange = onchange;

    checkboxItem._disabled = disabled || (images && checkboxContainer._checkboxElems[index].disabled);
    checkboxItem._readonly = readonly;

    if (checkboxItem._disabled || checkboxItem._readonly) {
      checkboxItem._tabIndex = checkboxItem.tabIndex;
      checkboxItem.tabIndex = -1;
    }

    if (images) {
      for (var stateKey in images) {
        var effects = images[stateKey];
        for (var effectKey in effects) {
          O$.preloadImage(effects[effectKey]);
        }
      }

      updateImage(checkboxItem);

      checkboxItem._indents = {};
      checkboxItem._defaultIndents = {
        marginLeft: O$.getNumericElementStyle(checkboxItem, "margin-left") + "px",
        marginRight: O$.getNumericElementStyle(checkboxItem, "margin-right") + "px",
        marginBottom: O$.getNumericElementStyle(checkboxItem, "margin-bottom") + "px"
      };

      checkboxItem.getDisabled = function() {
        return this._disabled;
      };

      checkboxItem.setDisabled = function(flag) {
        if (this._disabled !== flag) {
          this._disabled = flag;
          if (flag) {
            checkboxItem._tabIndex = checkboxItem.tabIndex;
            checkboxItem.tabIndex = -1;
          } else {
            checkboxItem.tabIndex = checkboxItem._tabIndex;
          }
          updateImage(this);
          updateStyles(this);
        }
      };

      checkboxItem.isSelected = function() {
        return this._state === "on";
      };

      checkboxItem.setSelected = function(flag) {
        if (this.isSelected() !== flag) {
          this._state = flag ? "on" : "off";
          updateImage(this);
          updateStyles(this);
        }
      };

      if (isOpera) {
        O$.addEventHandler(checkboxItem, "keypress", function(e) {
          if (!checkboxItem._disabled && !checkboxItem._readonly) {
            if (e.which === 32) {
              checkboxItem._pressed = false;
              nextState(checkboxItem);
              fireOnChange(checkboxItem);
            }
          }
        });
      }
    } else {
      checkboxItem.setDisabled = function(flag) {
        this.disabled = flag;
        updateStyles(this);
      };

      checkboxItem.getDisabled = function() {
        return this.disabled;
      };

      checkboxItem.setSelected = function(flag) {
        this.checked = flag;
        updateStyles(this);
      };

      checkboxItem.isSelected = function() {
        return this.checked;
      };
    }

    O$.addEventHandler(checkboxItem, "keydown", function(e) {
      if (!checkboxItem._disabled && !checkboxItem._readonly) {
        if (isSpacebar(e)) {
          checkboxItem._pressed = true;
          if (checkboxItem._images) {
            updateImage(checkboxItem);
          }
          updateStyles(checkboxItem);
        }
      }
    });

    O$.addEventHandler(checkboxItem, "mousedown", function() { itemMouseDown(); });
    O$.addEventHandler(checkboxItem._label, "mousedown", function() {
      itemMouseDown();
      if (!checkboxItem.getDisabled() && !checkboxItem._readonly) {
        checkboxItem.focus();
      }
    });

    function itemMouseDown() {
      if (!checkboxItem.getDisabled() && !checkboxItem._readonly) {
        checkboxItem._pressed = true;
        checkboxItem._focused = true;
        if (checkboxItem._images) {
          updateImage(checkboxItem);
        }
        updateStyles(checkboxItem);
      }
    }

    checkboxContainer._checkboxItems[index] = checkboxItem;
    updateStyles(checkboxItem);

    O$.addEventHandler(checkboxItem, "focus", function() {
      if (shouldProcessEvents(checkboxItem)) {
        checkboxItem._focused = true;
        updateStyles(checkboxItem);
      }
    });

    O$.addEventHandler(checkboxItem, "blur", function() {
      if (shouldProcessEvents(checkboxItem)) {
        checkboxItem._focused = false;
        updateStyles(checkboxItem);
      }
    });

    O$.addEventHandler(checkboxItem, "click", function(e) { itemClick(e); });
    O$.addEventHandler(checkboxItem._label, "click", function(e) { itemClick(e); });

    function itemClick(e) {
      if (!checkboxItem.getDisabled() && !checkboxItem._readonly) {
        checkboxItem._focused = true;
        checkboxItem.focus();
      }
      if (shouldProcessEvents(checkboxItem) && !checkboxItem._readonly) {
        checkboxItem._pressed = false;
        if (checkboxItem._images) {
          nextState(checkboxItem);
        } else {
          updateAllItemsStyles(checkboxItem);
        }
        fireOnChange(checkboxItem);
      }
      if (checkboxItem._images) {
        O$.preventDefaultEvent(e); // no form submission
      } else if (checkboxItem._readonly) {
        checkboxItem.checked = checkboxItem._state == "on" ? "checked" : "";
      }
    }

    O$.addEventHandler(checkboxItem, "mouseover", function(e) { itemMouseOver(e); });
    O$.addEventHandler(checkboxItem._label, "mouseover", function(e) { itemMouseOver(e); });

    function itemMouseOver(e) {
      if (shouldProcessEvents(checkboxItem)) {
        checkboxItem._rollover = !checkboxItem.getDisabled();
        checkboxItem._container._rollover = true;
        if (checkboxItem._images) {
          updateImage(checkboxItem);
        }
        updateStyles(checkboxItem);
      }
      if (checkboxItem._images) {
        // no form URL in status bar
        if (e && e.preventDefault) {
          e.preventDefault();
        } else {
          return true; // IE
        }
      }
    }


    O$.addEventHandler(checkboxItem, "mouseout", function() { itemMouseOut(); });
    O$.addEventHandler(checkboxItem._label, "mouseout", function() { itemMouseOut(); });

    function itemMouseOut() {
      if (shouldProcessEvents(checkboxItem)) {
        checkboxItem._rollover = false;
        checkboxItem._pressed = false;
        if (checkboxItem._images) {
          updateImage(checkboxItem);
        }
        updateStyles(checkboxItem);
      }
    }

    function shouldProcessEvents(checkboxItem) {
      return (!checkboxItem._images || !checkboxItem._disabled) && !checkboxItem._readonly;
    }

    function updateStyles(checkboxItem) {
      resetPosition(checkboxItem);

      O$.setStyleMappings(checkboxItem._label, {
        enabled: !checkboxItem.getDisabled() ? checkboxItem._enabledClass : null,
        disabled: checkboxItem.getDisabled() ? checkboxItem._disabledClass : null,
        rollover: checkboxItem._rollover ? checkboxItem._rolloverClass : null,
        focused: checkboxItem._focused ? checkboxItem._focusedClass : null,
        pressed: checkboxItem._pressed ? checkboxItem._pressedClass : null,
        selected: checkboxItem.isSelected() ? checkboxItem._selectedClass : null
      });

      O$.ManyCheckbox.updateContainerStyles(checkboxItem._container);

      fixPosition(checkboxItem);
    }

    function updateAllItemsStyles(checkboxItem) {
      checkboxItem._state = checkboxItem.checked ? "on" : "off";
      for (var i = 0; i < checkboxContainer._checkboxItemCount; i++) {
        updateStyles(checkboxContainer._checkboxItems[i]);
      }
    }

    function resetPosition(checkboxItem) {
      if (checkboxItem._images) {
        for (var indentKey in checkboxItem._defaultIndents) {
          checkboxItem.style[indentKey] = checkboxItem._defaultIndents[indentKey];
        }
      }
    }

    function fixPosition(checkboxItem) {
      if (checkboxItem._images) {
        var styleKey = getStyleKey(checkboxItem);
        var indent = checkboxItem._indents[styleKey];

        if (!indent) {
          indent = O$.getIndentData(checkboxItem, O$.CheckboxItem.indentDelta);
          checkboxItem._indents[styleKey] = indent;
        }

        for (var indentKey in indent) {
          checkboxItem.style[indentKey] = indent[indentKey];
        }
      }
    }

    function nextState(checkboxItem) {
      checkboxItem._state = checkboxItem._state == "on" ? "off" : "on";
      checkboxContainer._checkboxElems[checkboxItem._index].checked = checkboxItem._state == "on";
      updateImage(checkboxItem);
      updateStyles(checkboxItem);
    }

    function fireOnChange(checkboxItem) {
      if (checkboxItem._onchange) {
        var event = O$.createEvent("change");
        var returnValue = checkboxItem._onchange(event);
        if (returnValue == undefined) {
          returnValue = event.returnValue;
        }
        return returnValue;
      }
      return null;
    }

    function updateImage(checkboxItem) {
      var effect;
      if (checkboxItem._disabled) {
        effect = "disabled";
      } else if (checkboxItem._pressed) {
        effect = "pressed";
      } else if (checkboxItem._rollover) {
        effect = "rollover";
      } else {
        effect = "plain";
      }
      checkboxItem.src = checkboxItem._images[checkboxItem._state][effect];
    }

    function isSpacebar(e) {
      var evt = O$.getEvent(e);
      return (evt.which || evt.keyCode) === 32;
    }

    function getStyleKey(checkboxItem) {
      var result = checkboxItem.isSelected() ? "selected" : "unselected";
      if (checkboxItem._rollover) {
        result += "+rollover";
      }
      if (checkboxItem._focused) {
        result += "+focused";
      }
      return result;
    }
  },

  indentDelta :
    // in-place call
          function() {
            var delta = {};

            if (O$.isExplorer8()) {
              delta.marginLeft = 3;
            } else if (!O$.isOpera()) {
              delta.marginLeft = 4;
            }

            if (O$.isSafari3()) {
              delta.marginRight = 1;
            } else {
              delta.marginRight = 5;
            }

            if (O$.isExplorer7() || O$.isExplorer6() || O$.isOpera()) {
              delta.marginBottom = -1;
            } else if (O$.isSafari3()) {
              delta.marginBottom = -3;
            }

            return delta;
          }()
};
