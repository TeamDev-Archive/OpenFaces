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

O$.Radio = {

  _init: function(radioId, images, styles, stylesItems, disabled, readonly, onchange) {

    var radioTable = O$(radioId);

    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    radioTable.className = getClassName("styleClass");

    //        radioTable._enabledClass = getClassName("enabledClass");
    //        radioTable._disabledClass = getClassName("disabledClass");
    //        radioTable._rolloverClass = getClassName("rolloverClass");
    //        radioTable._focusedClass = getClassName("focusedClass");
    //        radioTable._selectedClass = getClassName("selectedClass");
    //        radioTable._unselectedClass = getClassName("unselectedClass");


    radioTable._radioItems = new Array();
    radioTable._radioElems = new Array();
    radioTable._radioItemCount = stylesItems.length;
    for (var i = 0; i < stylesItems.length; i++) {
      var radioItemId = radioId + ":" + i;
      radioTable._radioElems[i] = O$(radioItemId);;

      O$.RadioItem._init(radioTable, i, radioItemId, images, styles, stylesItems, disabled, readonly, onchange);
    }

  }
};

O$.RadioItem = {

  _init: function(radioTable, index, radioItemId, images, styles, stylesItems, disabled, readonly, onchange) {

    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    function getItemClassName(classKey, index) {
      var className = stylesItems[index] ? stylesItems[index][classKey] : null;
      var mergeClassName = (className == null) ? "" : className;
      return getClassName(classKey).length > 0 ? getClassName(classKey) + " " + mergeClassName : mergeClassName;
    }

    var isOpera = O$.isOpera();

    var radioItem;
    if (images) {
      radioItem = O$(radioItemId + "::image");
      radioItem._state = radioTable._radioElems[index].checked ? "on" : "off";
      radioItem._images = images;
    } else {
      radioItem = O$(radioItemId);
      radioItem._state = radioItem.checked ? "on" : "off";
    }

    radioItem._index = index;

    radioItem._enabledClass = getClassName("enabledClass");
    radioItem._disabledClass = getClassName("disabledClass");
    radioItem._rolloverClass = getItemClassName("rolloverClass", index);
    radioItem._focusedClass = getItemClassName("focusedClass", index);
    radioItem._pressedClass = getItemClassName("pressedClass", index);
    radioItem._selectedClass = getItemClassName("selectedClass", index);
    radioItem._unselectedClass = getItemClassName("unselectedClass", index);

    radioItem._onchange = onchange;

    radioItem._disabled = disabled || (images && radioTable._radioElems[index].disabled);
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
        marginLeft: O$.getNumericStyleProperty(radioItem, "margin-left") + "px",
        marginRight: O$.getNumericStyleProperty(radioItem, "margin-right") + "px",
        marginBottom: O$.getNumericStyleProperty(radioItem, "margin-bottom") + "px"
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

      O$.addEventHandler(radioItem, "mousedown", function() {
        if (!radioItem._disabled && !radioItem._readonly) {
          radioItem._pressed = true;
          updateImage(radioItem);
          updateStyles(radioItem);
        }
      });

      O$.addEventHandler(radioItem, "keydown", function(e) {
        if (!radioItem._disabled && !radioItem._readonly) {
          if (isSpacebar(e)) {
            radioItem._pressed = true;
            updateImage(radioItem);
            updateStyles(radioItem);
          } else if (isPrevious(e)) {
            radioItem._focused = false;
            var radioItemPrevious = findPreviousItem(radioItem._index);
            radioItemPrevious._focused = true;
            nextState(radioItemPrevious);
            radioItemPrevious.focus();
            fireOnChange(radioItemPrevious);
            O$.preventDefaultEvent(e);
          } else if (isNext(e)) {
            radioItem._focused = false;
            var radioItemNext = findNextItem(radioItem._index);
            radioItemNext._focused = true;
            nextState(radioItemNext);
            radioItemNext.focus();
            fireOnChange(radioItemNext);
            O$.preventDefaultEvent(e);
          }
        }
      });

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

    radioTable._radioItems[index] = radioItem;
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

    O$.addEventHandler(radioItem, "click", function(e) {
      if (shouldProcessEvents(radioItem) && !radioItem._readonly) {
        radioItem._pressed = false;
        if (radioItem._images) {
          nextState(radioItem);
        } else {
          updateStyles(radioItem);
        }
        fireOnChange(radioItem);
      }
      if (radioItem._images) {
        O$.preventDefaultEvent(e); // no form submission
      } else if (radioItem._readonly) {
        radioItem.checked = radioItem._state == "on" ? "checked" : "";
      }
    });

    O$.addEventHandler(radioItem, "mouseover", function(e) {
      if (shouldProcessEvents(radioItem)) {
        radioItem._rollover = true;
        if (radioItem._images) {
          updateImage(radioItem);
        }
        updateStyles(radioItem);
      }
      if (radioItem._images) {
        // no form URL in status bar
        if (e && e.preventDefault) {
          e.preventDefault();
        } else {
          return true; // IE
        }
      }
    });

    O$.addEventHandler(radioItem, "mouseout", function() {
      if (shouldProcessEvents(radioItem)) {
        radioItem._rollover = false;
        if (radioItem._images) {
          radioItem._pressed = false;
          updateImage(radioItem);
        }
        updateStyles(radioItem);
      }
    });


    function shouldProcessEvents(radioItem) {
      return (!radioItem._images || !radioItem._disabled) && !radioItem._readonly;
    }

    function updateStyles(radioItem) {
      resetPosition(radioItem);

      O$.setStyleMappings(radioItem, {
        enabled: !radioItem.getDisabled() ? radioItem._enabledClass : null,
        disabled: radioItem.getDisabled() ? radioItem._disabledClass : null,
        rollover: radioItem._rollover ? radioItem._rolloverClass : null,
        focused: radioItem._focused ? radioItem._focusedClass : null,
        pressed: radioItem._pressed ? radioItem._pressedClass : null,
        selected: radioItem.isSelected() ? radioItem._selectedClass : null,
        unselected: !radioItem.isSelected() ? radioItem._unselectedClass : null
      });

      fixPosition(radioItem);
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
          indent = getIndentData(radioItem);
          radioItem._indents[styleKey] = indent;
        }

        for (var indentKey in indent) {
          radioItem.style[indentKey] = indent[indentKey];
        }
      }
    }

    function nextState(radioItem) {
      for (var i = 0; i < radioTable._radioItemCount; i++) {
        if (radioItem._index == i) {
          radioItem._state = "on";
          radioTable._radioElems[i].checked = true;
          updateImage(radioItem);
          updateStyles(radioItem);
        } else {
          radioTable._radioItems[i]._state = "off";
          radioTable._radioElems[i].checked = false;
          updateImage(radioTable._radioItems[i]);
          updateStyles(radioTable._radioItems[i]);
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
          if (radioTable._radioItems[i - 1]._disabled) {
            i--;
          } else {
            return radioTable._radioItems[i - 1];
          }
        } else if (i == 0) {
          i = radioTable._radioItems.length;
        }
      }
    }

    function findNextItem(index) {
      var i = index;
      while (true) {
        if (radioTable._radioItems.length > (i + 1)) {
          if (radioTable._radioItems[i + 1]._disabled) {
            i++;
          } else {
            return radioTable._radioItems[i + 1];
          }
        } else if (radioTable._radioItems.length == (i + 1)) {
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

    function getIndentData(radioItem) {
      var indent = {};
      var delta = O$.RadioItem.indentDelta;

      if (delta.marginLeft) {
        indent.marginLeft = (delta.marginLeft + O$.getNumericStyleProperty(radioItem, "margin-left")) + "px";
      }
      if (delta.marginRight) {
        indent.marginRight = (delta.marginRight + O$.getNumericStyleProperty(radioItem, "margin-right")) + "px";
      }
      if (delta.marginBottom) {
        indent.marginBottom = (delta.marginBottom + O$.getNumericStyleProperty(radioItem, "margin-bottom")) + "px";
      }
      return indent;
    }

    return radioItem;
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
              delta.marginRight = 3;
            }

            if (O$.isExplorer7() || O$.isExplorer6() || O$.isOpera()) {
              delta.marginBottom = -1;
            } else if (O$.isSafari3()) {
              delta.marginBottom = -3;
            }

            return delta;
          }()
};
