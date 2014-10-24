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
O$.Checkbox = {

  _init:function (checkboxId, images, styles, stateList, disabled, onchange) {
    function getClassName(classKey) {
      var className = styles ? styles[classKey] : null;
      return (className == null) ? "" : className;
    }

    var checkbox = O$.initComponent(checkboxId, null, {
      className:getClassName("styleClass"),

      _rolloverClass:getClassName("rolloverClass"),
      _focusedClass:getClassName("focusedClass"),
      _selectedClass:getClassName("selectedClass"),
      _unselectedClass:getClassName("unselectedClass"),
      _undefinedClass:getClassName("undefinedClass"),

      _onchange:onchange
    });

    if (images) {

      // image-based checkbox

      O$.extend(checkbox, {
        _state:O$(checkboxId + "::state"),
        _images:images,
        _stateList:stateList,
        _disabled:disabled,

        _indents:{},
        _defaultIndents:{
          marginLeft:O$.getNumericElementStyle(checkbox, "margin-left") + "px",
          marginRight:O$.getNumericElementStyle(checkbox, "margin-right") + "px",
          marginBottom:O$.getNumericElementStyle(checkbox, "margin-bottom") + "px"
        },

        // using "getDisabled" instead of "isDisabled "
        // because of standard "isDisabled" property
        getDisabled:function () {
          return this._disabled;
        },

        setDisabled:function (flag) {
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
        },

        isSelected:function () {
          return this._state.value === "selected";
        },

        setSelected:function (flag) {
          if (!this.isDefined() || this.isSelected() !== flag) {
            this._state.value = flag ? "selected" : "unselected";
            updateImage(this);
            updateStyles(this);
          }
        },

        isDefined:function () {
          return this._state.value !== "undefined";
        },

        setDefined:function (flag) {
          if (this.isDefined() !== flag) {
            if (flag) {
              if (this._state.value === "undefined") {
                this._state.value = "unselected";
              }
            } else {
              this._state.value = "undefined";
            }
            updateImage(this);
            updateStyles(this);
          }
        }
      });

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

      O$.addEventHandler(checkbox, "mousedown", function () {
        if (checkbox.onclick && O$.isExplorer() && !checkbox._onclickProcessed) {
          // Prevent the issue when JavaScript onclick handler assigned to the onclick field explicitly is invoked
          // before the new state is actually available. We do it by substituting field-based event with an
          // asynchronous one on the first mouse down (not on initialization to allow other code to set up a check-box
          // after it is initialized)
          var onclickHandler = checkbox.onclick;
          checkbox._stolenClickHandler = onclickHandler;
          checkbox.onclick = checkbox._guaranteedStopEventOnClickRequested ? function (e) {
            O$.stopEvent(e);
          } : null;

          O$.addEventHandler(checkbox, "click", function () {
            setTimeout(function () {
              var e = O$.createEvent("click");
              onclickHandler.call(checkbox, e);
            }, 1);
          });
        }
        if (!checkbox._disabled) {
          checkbox._pressed = true;
          updateImage(checkbox);
        }
        checkbox._onclickProcessed = true;
      });

      O$.addEventHandler(checkbox, "keydown", function (e) {
        if (!checkbox._disabled) {
          if (isSpacebar(e)) {
            checkbox._pressed = true;
            updateImage(checkbox);
          }
        }
      });

      // space-bar also fires "click" in non-Opera browsers
      if (O$.isOpera()) {
        O$.addEventHandler(checkbox, "keypress", function (e) {
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

      O$.extend(checkbox, {
        setDisabled:function (flag) {
          this.disabled = flag;
          updateStyles(this);
        },
        getDisabled:function () {
          return this.disabled;
        },
        setSelected:function (flag) {
          this.checked = flag;
          updateStyles(this);
        },
        isSelected:function () {
          return this.checked;
        },
        setDefined:function () { /* do nothing */
        },
        isDefined:function () {
          return true;
        }
      });

    }

    updateStyles(checkbox);

    O$.addEventHandler(checkbox, "focus", function () {
      if (shouldProcessEvents(checkbox)) {
        checkbox._focused = true;
        updateStyles(checkbox);
      }
    });

    O$.addEventHandler(checkbox, "blur", function () {
      if (shouldProcessEvents(checkbox)) {
        checkbox._focused = false;
        updateStyles(checkbox);
      }
    });

    O$.addEventHandler(checkbox, "click", function (e) {
              //Fix bug OF-229
              e = e || window.event;
              if (e.pageX == null && e.clientX != null) {
                var html = document.documentElement;
                var body = document.body;
                e.pageX = e.clientX + (html && html.scrollLeft || body && body.scrollLeft || 0) - (html.clientLeft || 0);
                e.pageY = e.clientY + (html && html.scrollTop || body && body.scrollTop || 0) - (html.clientTop || 0);
              }
              if ((e.pageX != 0) && (e.pageY != 0)) {
                if (shouldProcessEvents(checkbox)) {
                  checkbox._pressed = false;
                  if (checkbox._images) {
                    nextState(checkbox);
                    fireOnChange(checkbox);
                  } else {
                    updateStyles(checkbox);
                  }
                }
              }

              if (checkbox._images) {
                O$.preventDefaultEvent(e); // no form submission
              }
            }
    );

    O$.addEventHandler(checkbox, "mouseover", function (e) {
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

    O$.addEventHandler(checkbox, "mouseout", function () {
      if (shouldProcessEvents(checkbox)) {
        checkbox._rollover = false;
        if (checkbox._images) {
          checkbox._pressed = false;
          updateImage(checkbox);
        }
        updateStyles(checkbox);
      }
    });

    function nextState(checkbox) {
      var nextStateIndex = 0;
      for (var i = 0; i < checkbox._stateList.length; i++) {
        if (checkbox._stateList[i] == checkbox._state.value) {
          nextStateIndex = i + 1;
        }
      }
      if (nextStateIndex >= checkbox._stateList.length) {
        nextStateIndex = 0;
      }
      checkbox._state.value = checkbox._stateList[nextStateIndex];
      updateImage(checkbox);
      updateStyles(checkbox);
    }

    function fireOnChange(checkbox) {
      if (checkbox._onchange) {
        var event = O$.createEvent("change");
        var returnValue = checkbox._onchange(event);
        if (returnValue == undefined) {
          returnValue = event.returnValue;
        }
        return returnValue;
      }
      return undefined;
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
      resetPosition(checkbox);

      O$.setStyleMappings(checkbox, {
        selected:checkbox.isSelected() ? checkbox._selectedClass : null,
        unselected:(checkbox.isDefined() && !checkbox.isSelected()) ? checkbox._unselectedClass : null,
        _undefined:checkbox.isDefined() ? null : checkbox._undefinedClass,
        rollover:checkbox._rollover ? checkbox._rolloverClass : null,
        focused:checkbox._focused ? checkbox._focusedClass : null
      });

      fixPosition(checkbox);
    }

    function resetPosition(checkbox) {
      if (checkbox._images) {
        for (indentKey in checkbox._defaultIndents) {
          checkbox.style[indentKey] = checkbox._defaultIndents[indentKey];
        }
      }
    }

    function fixPosition(checkbox) {
      if (checkbox._images) {
        var styleKey = getStyleKey(checkbox);
        var indent = checkbox._indents[styleKey];

        if (!indent) {
          indent = O$.getIndentData(checkbox, O$.Checkbox.indentDelta);
          checkbox._indents[styleKey] = indent;
        }

        for (indentKey in indent) {
          checkbox.style[indentKey] = indent[indentKey];
        }
      }
    }

    function getStyleKey(checkbox) {
      var result = checkbox.isDefined() ? (checkbox.isSelected() ? "selected" : "unselected") : "undefined";
      if (checkbox._rollover) {
        result += "+rollover";
      }
      if (checkbox._focused) {
        result += "+focused";
      }
      return result;
    }

  },

  indentDelta:// in-place call
          function () {
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
          }

                  ()

};
