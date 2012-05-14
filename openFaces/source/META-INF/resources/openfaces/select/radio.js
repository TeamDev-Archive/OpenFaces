/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.Radio = {

  _init: function(radioId, images, styles, radioItemCount, disabled, readonly, onchange) {

    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    var radioContainer = O$.initComponent(radioId, null, {
      className: getClassName("styleClass"),
      _rolloverClass: getClassName("rolloverClass"),
      _focusedClass: getClassName("focusedClass"),

      _radioItems: [],
      _radioElems: [],
      _radioItemCount: radioItemCount
    });
    radioContainer._styleClass = radioContainer.className;
    for (var i = 0; i < radioItemCount; i++) {
      var radioItemId = radioId + ":" + i;
      radioContainer._radioElems[i] = O$(radioItemId);

      O$.RadioItem._init(radioContainer, i, radioItemId, images, styles, disabled, readonly, onchange);
    }

    O$.addEventHandler(radioContainer, "mouseover", function(e) {
      if (!disabled) {
        radioContainer._rollover = true;
        O$.Radio.updateContainerStyles(radioContainer);
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

    O$.addEventHandler(radioContainer, "mouseout", function() {
      if (!disabled) {
        radioContainer._rollover = false;
        O$.Radio.updateContainerStyles(radioContainer);
      }
    });

  },

  updateContainerStyles : function(radioContainer) {
    var containerFocused = radioContainer._focused;
    var containerRollover = radioContainer._rollover;
    for (var i = 0; i < radioContainer._radioItems.length; i++) {
      containerFocused |= radioContainer._radioItems[i]._focused || radioContainer._radioItems[i]._pressed;
      containerRollover |= radioContainer._radioItems[i]._rollover;
    }

    O$.setStyleMappings(radioContainer, {
      rollover: containerRollover ? radioContainer._rolloverClass : null,
      focused: containerFocused ? radioContainer._focusedClass : null
    });
  }
};

O$.RadioItem = {

  _init: function(radioContainer, index, radioItemId, images, styles, disabled, readonly, onchange) {

    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    var isOpera = O$.isOpera();

    var radioItem;
    if (images) {
      radioItem = O$(radioItemId + "::image");
      radioItem._state = radioContainer._radioElems[index].checked ? "on" : "off";
      radioItem._images = images;
    } else {
      radioItem = O$(radioItemId);
      radioItem._state = radioItem.checked ? "on" : "off";
    }
    radioItem._container = radioContainer;
    radioItem._label = O$(radioItemId + "::label");
    radioItem._firstItem = index == 0;
    radioItem._lastItem = index == (radioContainer._radioItemCount - 1);

    radioItem._index = index;

    radioItem._enabledClass = getClassName("enabledClass");
    radioItem._disabledClass = getClassName("disabledClass");
    radioItem._focusedClass = getClassName("focusedItemClass");
    radioItem._rolloverClass = getClassName("rolloverItemClass");
    radioItem._pressedClass = getClassName("pressedItemClass");
    radioItem._selectedClass = getClassName("selectedItemClass");

    radioItem._onchange = onchange;

    radioItem._disabled = disabled || (images && radioContainer._radioElems[index].disabled);
    radioItem._readonly = readonly;

    if (radioItem._disabled || radioItem._readonly) {
      radioItem._tabIndex = radioItem.tabIndex;
      radioItem.tabIndex = -1;
    }

    if (images) {
      for (var stateKey in images) {
        var effects = images[stateKey];
        for (var effectKey in effects) {
          O$.preloadImage(effects[effectKey]);
        }
      }

      updateImage(radioItem);

      radioItem._indents = {};
      radioItem._defaultIndents = {
        marginLeft: O$.getNumericElementStyle(radioItem, "margin-left") + "px",
        marginRight: O$.getNumericElementStyle(radioItem, "margin-right") + "px",
        marginBottom: O$.getNumericElementStyle(radioItem, "margin-bottom") + "px"
      };

      radioItem.getDisabled = function() {
        return this._disabled;
      };

      radioItem.setDisabled = function(flag) {
        if (this._disabled !== flag) {
          this._disabled = flag;
          if (flag) {
            radioItem._tabIndex = radioItem.tabIndex;
            radioItem.tabIndex = -1;
          } else {
            radioItem.tabIndex = radioItem._tabIndex;
          }
          updateImage(this);
          updateStyles(this);
        }
      };

      radioItem.isSelected = function() {
        return this._state === "on";
      };

      radioItem.setSelected = function(flag) {
        if (this.isSelected() !== flag) {
          this._state = flag ? "on" : "off";
          updateImage(this);
          updateStyles(this);
        }
      };

      if (isOpera) {
        O$.addEventHandler(radioItem, "keypress", function(e) {
          if (!radioItem._disabled && !radioItem._readonly) {
            if (e.which === 32) {
              radioItem._pressed = false;
              nextState(radioItem);
              fireOnChange(radioItem);
            } else if (isNext(e) || isPrevious(e)) {
              O$.preventDefaultEvent(e);
            }
          }
        });
      }
    } else {
      radioItem.setDisabled = function(flag) {
        this.disabled = flag;
        updateStyles(this);
      };

      radioItem.getDisabled = function() {
        return this.disabled;
      };

      radioItem.setSelected = function(flag) {
        this.checked = flag;
        updateStyles(this);
      };

      radioItem.isSelected = function() {
        return this.checked;
      };
    }

    O$.addEventHandler(radioItem, "keydown", function(e) {
      if (!radioItem._disabled && !radioItem._readonly) {
        if (isSpacebar(e)) {
          radioItem._pressed = true;
          if (radioItem._images) {
            updateImage(radioItem);
          }
          updateStyles(radioItem);
        } else if ((isOpera && isPrevious(e) && !radioItem._firstItem) ||
                   (isOpera && isNext(e) && !radioItem._lastItem) ||
                   ((!isOpera || radioItem._images) && (isPrevious(e) || isNext(e)))) {
          radioItem._focused = false;
          var radioItemSibling;
          if (isPrevious(e)) {
            radioItemSibling = findPreviousItem(radioItem._index);
          } else if (isNext(e)) {
            radioItemSibling = findNextItem(radioItem._index);
          }
          radioItemSibling._focused = true;
          if (radioItemSibling._images) {
            nextState(radioItemSibling);
          } else {
            radioItem.checked = false;
            radioItemSibling.checked = true;
            updateAllItemsStyles(radioItemSibling);
          }
          if (!(isOpera && !radioItem._images)) {
            radioItemSibling.focus();
            fireOnChange(radioItemSibling);
            O$.preventDefaultEvent(e);
          }
        } else if (isTab(e) && radioItem._images) {
          var bodyElement = document.getElementsByTagName("body")[0];
          if (bodyElement == null)
            return;

          var reg = /\b(input|select|textarea|button|a|div|span)\b/i;
          var startRadioItem;
          var focusableElements = O$.getElementsByTagNameRegexp(bodyElement, reg, O$.isControlFocusable);
          if (O$.isShiftPressed(e)) {
            startRadioItem = radioContainer._radioItems[0];
            focusableElements = focusableElements.reverse();
          } else {
            var count = radioContainer._radioItems.length;
            startRadioItem = radioContainer._radioItems[count - 1];
          }
          var isFound = false;
          for (var i = 0; i < focusableElements.length; i++) {
            var focusableElement = focusableElements[i];
            isFound |= focusableElement.id == startRadioItem.id;
            if (isFound && O$.getElementSize(focusableElement).width > 0 && O$.getElementSize(focusableElement).height > 0) {
              focusableElement.focus();
              return;
            }
          }
        }
      }
    });

    O$.addEventHandler(radioItem, "mousedown", function() { itemMouseDown(); });
    O$.addEventHandler(radioItem._label, "mousedown", function() { itemMouseDown(); });

    function itemMouseDown() {
      if (!radioItem.getDisabled() && !radioItem._readonly) {
        radioItem._pressed = true;
        radioItem._focused = true;
        if (radioItem._images) {
          updateImage(radioItem);
        }
        updateStyles(radioItem);
      }
    }

    radioContainer._radioItems[index] = radioItem;
    updateStyles(radioItem);

    O$.addEventHandler(radioItem, "focus", function() {
      if (shouldProcessEvents(radioItem)) {
        radioItem._focused = true;
        updateStyles(radioItem);
      }
    });

    O$.addEventHandler(radioItem, "blur", function() {
      if (shouldProcessEvents(radioItem)) {
        radioItem._focused = false;
        updateStyles(radioItem);
      }
    });

    O$.addEventHandler(radioItem, "click", function(e) { itemClick(e); });
    O$.addEventHandler(radioItem._label, "click", function(e) { itemClick(e); });

    function itemClick(e) {
      if (!radioItem.getDisabled() && !radioItem._readonly) {
        radioItem._focused = true;
        radioItem.focus();
      }
      if (shouldProcessEvents(radioItem) && !radioItem._readonly) {
        radioItem._pressed = false;
        if (radioItem._images) {
          nextState(radioItem);
        } else {
          updateAllItemsStyles(radioItem);
        }
        fireOnChange(radioItem);
      }
      if (radioItem._images) {
        O$.preventDefaultEvent(e); // no form submission
      } else if (radioItem._readonly) {
        radioItem.checked = radioItem._state == "on" ? "checked" : "";
      }
    }

    O$.addEventHandler(radioItem, "mouseover", function(e) { itemMouseOver(e); });
    O$.addEventHandler(radioItem._label, "mouseover", function(e) { itemMouseOver(e); });

    function itemMouseOver(e) {
      if (shouldProcessEvents(radioItem)) {
        radioItem._rollover = !radioItem.getDisabled();
        radioItem._container._rollover = true;
        if (radioItem._images) {
          updateImage(radioItem);
        }
        updateStyles(radioItem);
      }
      if (radioItem._images) {
        // no form URL in status bar
        if (e && e.preventDefault) {
          e.preventDefault();
        }
      }
    }


    O$.addEventHandler(radioItem, "mouseout", function() { itemMouseOut(); });
    O$.addEventHandler(radioItem._label, "mouseout", function() { itemMouseOut(); });

    function itemMouseOut() {
      if (shouldProcessEvents(radioItem)) {
        radioItem._rollover = false;
        radioItem._pressed = false;
        if (radioItem._images) {
          updateImage(radioItem);
        }
        updateStyles(radioItem);
      }
    }

    function shouldProcessEvents(radioItem) {
      return (!radioItem._images || !radioItem._disabled) && !radioItem._readonly;
    }

    function updateStyles(radioItem) {
      resetPosition(radioItem);

      O$.setStyleMappings(radioItem._label, {
        enabled: !radioItem.getDisabled() ? radioItem._enabledClass : null,
        disabled: radioItem.getDisabled() ? radioItem._disabledClass : null,
        rollover: radioItem._rollover ? radioItem._rolloverClass : null,
        focused: radioItem._focused ? radioItem._focusedClass : null,
        pressed: radioItem._pressed ? radioItem._pressedClass : null,
        selected: radioItem.isSelected() ? radioItem._selectedClass : null
      });

      O$.Radio.updateContainerStyles(radioItem._container);

      fixPosition(radioItem);
    }

    function updateAllItemsStyles(radioItem) {
      for (var i = 0; i < radioContainer._radioItemCount; i++) {
        if (radioItem._index == i) {
          radioItem._state = "on";
          updateStyles(radioItem);
        } else {
          radioContainer._radioItems[i]._state = "off";
          updateStyles(radioContainer._radioItems[i]);
        }
      }
    }

    function resetPosition(radioItem) {
      if (radioItem._images) {
        for (var indentKey in radioItem._defaultIndents) {
          radioItem.style[indentKey] = radioItem._defaultIndents[indentKey];
        }
      }
    }

    function fixPosition(radioItem) {
      if (radioItem._images) {
        var styleKey = getStyleKey(radioItem);
        var indent = radioItem._indents[styleKey];

        if (!indent) {
          indent = O$.getIndentData(radioItem, O$.RadioItem.indentDelta);
          radioItem._indents[styleKey] = indent;
        }

        for (var indentKey in indent) {
          radioItem.style[indentKey] = indent[indentKey];
        }
      }
    }

    function nextState(radioItem) {
      for (var i = 0; i < radioContainer._radioItemCount; i++) {
        if (radioItem._index == i) {
          radioItem._state = "on";
          radioContainer._radioElems[i].checked = true;
          updateImage(radioItem);
          updateStyles(radioItem);
        } else {
          radioContainer._radioItems[i]._state = "off";
          radioContainer._radioElems[i].checked = false;
          updateImage(radioContainer._radioItems[i]);
          updateStyles(radioContainer._radioItems[i]);
        }
      }
    }

    function fireOnChange(radioItem) {
      if (radioItem._onchange) {
        var event = O$.createEvent("change");
        var returnValue = radioItem._onchange(event);
        if (returnValue == undefined) {
          returnValue = event.returnValue;
        }
        return returnValue;
      }
      return null;
    }

    function updateImage(radioItem) {
      var effect;
      if (radioItem._disabled) {
        effect = "disabled";
      } else if (radioItem._pressed) {
        effect = "pressed";
      } else if (radioItem._rollover) {
        effect = "rollover";
      } else {
        effect = "plain";
      }
      radioItem.src = radioItem._images[radioItem._state][effect];
    }

    function isSpacebar(e) {
      var evt = O$.getEvent(e);
      return (evt.which || evt.keyCode) === 32;
    }

    function isTab(e) {
      var evt = O$.getEvent(e);
      return (evt.which || evt.keyCode) === 9;
    }

    function isPrevious(e) {
      var evt = O$.getEvent(e);
      return (evt.which || evt.keyCode) === 37 || (evt.which || evt.keyCode) === 38;
    }

    function isNext(e) {
      var evt = O$.getEvent(e);
      return (evt.which || evt.keyCode) === 39 || (evt.which || evt.keyCode) === 40;
    }

    function findPreviousItem(index) {
      var i = index;
      while (true) {
        if (i > 0) {
          if (radioContainer._radioItems[i - 1].getDisabled()) {
            i--;
          } else {
            return radioContainer._radioItems[i - 1];
          }
        } else if (i == 0) {
          i = radioContainer._radioItems.length;
        }
      }
    }

    function findNextItem(index) {
      var i = index;
      while (true) {
        if (radioContainer._radioItems.length > (i + 1)) {
          if (radioContainer._radioItems[i + 1].getDisabled()) {
            i++;
          } else {
            return radioContainer._radioItems[i + 1];
          }
        } else if (radioContainer._radioItems.length == (i + 1)) {
          i = -1;
        }
      }
    }

    function getStyleKey(radioItem) {
      var result = radioItem.isSelected() ? "selected" : "unselected";
      if (radioItem._rollover) {
        result += "+rollover";
      }
      if (radioItem._focused) {
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
