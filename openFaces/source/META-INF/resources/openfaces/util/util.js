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

if (!window.O$) {
  window.OpenFaces = window.O$ = function (id) {
    return id ? document.getElementById(id) : null;
  };

  O$.extend = function (obj, withObj) {
    var previousPropertyValues = {};
    /*Added explorer memory leak fix because of circular references - IE itself doesn't resolve them,
     * so we call destroyAllFunctions where all functions added with extend method will be cleared */
    var ie;
    if (O$.isExplorer) {
      ie = O$.isExplorer();
      if (ie && !obj.customPropertiesForIE) {
        obj.customPropertiesForIE = [];
      }
    }
    for (var propertyName in withObj) {
      if (propertyName != "prototype") {
        previousPropertyValues[propertyName] = obj[propertyName];
        obj[propertyName] = withObj[propertyName];
        if (ie) {
          obj.customPropertiesForIE.push(propertyName);
        }
      }
    }
    return previousPropertyValues;
  };

  O$.createClass = function (staticClassMembers, instanceMembers) {
    var constructor = instanceMembers.constructor;
    instanceMembers.constructor = undefined;
    var classFunction = function () {
      if (constructor)
        constructor.apply(this, arguments);
    };
    if (staticClassMembers)
      O$.extend(classFunction, staticClassMembers);
    O$.extend(classFunction.prototype, instanceMembers);
    return classFunction;
  };

  O$.extend(O$, {
    DEBUG:true,

    ACTION_LISTENER:"_of_actionListener",
    ACTION:"_of_action",
    ACTION_COMPONENT:"_of_actionComponent",
    IMMEDIATE:"_of_immediate"
  });

  O$.getDisabledCommandLink = function (element, disabled) {
    O$.setHiddenField(element, element.id + "::disabled", disabled);
    if (element._disabled == disabled) return;
    element._disabled = disabled;
    O$.setStyleMappings(element, {"disabled":element._disabled ? O$._disabledClassLink : ""});
    O$.events.forEach(function (eventName, element) {
      if (disabled) {
        element[eventName] = null;
      } else {
        element[eventName] = O$.originalEventHandlers[eventName];
      }
    });
    if (disabled) {
      element.onclick = O$.preventDefaultEvent;
    } else if (element.onclick == O$.preventDefaultEvent) {
      element.onclick = null;
    }
  }
  O$.originalEventHandlers = {};
  O$.events = ["onfocus", "onblur", "onkeydown", "onkeypress", "onkeyup",
    "onmouseover", "onmousemove", "onmousedown", "onclick", "ondblclick", "onmouseup", "onmouseout"];
  O$.events.forEach(function (eventName, element) {
    O$.originalEventHandlers[eventName] = element[eventName];

  });
  O$._setOriginalEventHandler = function () {

  }

  O$._getOriginalEventHandler = function () {

  }
  O$.initComponent = function (clientId, styles, properties, events) {
    O$._checkDefaultCssPresence();
    var component = O$(clientId);
    if (!component)
      throw "O$.initComponent: couldn't find component by id: " + clientId;

    if (styles) {
      if (styles.rollover)
        O$.setupHoverStateFunction(component, function (mouseInside) {
          O$.setStyleMappings(component, {_rolloverStyle:mouseInside ? styles.rollover : null});
        });
      if (styles.focused)
        O$.setupFocusedStateFunction(component, function (focused) {
          O$.setStyleMappings(component, {_focusedStyle:focused ? styles.focused : null});
        });
      if (styles.pressed)
        O$.setupMousePressedStateFunction(component, function (pressed) {
          O$.setStyleMappings(component, {_pressedStyle:pressed ? styles.pressed : null});
        });
    }
    if (properties) {
      O$.extend(component, properties);
    }
    if (events) {
      for (var eventName in events) {
        var handlerScript = events[eventName];
        if (!handlerScript)
          continue;
        var handlerFunction;
        if (typeof handlerScript == "string")
          eval("handlerFunction = function(event) {" + handlerScript + "}");
        else if (typeof handlerScript == "function")
          handlerFunction = handlerScript;
        else
          throw "Type of a handler script should either be a string or a function, but it was a " +
                  (typeof handlerScript) + "; " + handlerScript;
        if (!component[eventName])
          component[eventName] = handlerFunction;
        else {
          function appendHandler() {
            var prevHandler = component[eventName];
            component[eventName] = function () {
              prevHandler.apply(component, arguments);
              handlerFunction.apply(component, arguments);
            }
          }

          appendHandler();
        }
      }
    }
    return component;
  };

  O$.extend(O$, { /* Rectangle class */
    Rectangle:O$.createClass(null, {
      constructor:function (x, y, width, height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
      },
      clone:function () {
        return new O$.Rectangle(this.x, this.y, this.width, this.height);
      },
      getMinX:function () {
        return this.x;
      },
      getMinY:function () {
        return this.y;
      },
      getMaxX:function () {
        return this.x + this.width;
      },
      getMaxY:function () {
        return this.y + this.height;
      },

      addRectangle:function (rect) {
        O$.assert(rect, "rect parameter should be passed");
        var x1 = this.getMinX();
        if (rect.getMinX() < x1)
          x1 = rect.getMinX();
        var y1 = this.getMinY();
        if (rect.getMinY() < y1)
          y1 = rect.getMinY();
        var x2 = this.getMaxX();
        if (rect.getMaxX() > x2)
          x2 = rect.getMaxX();
        var y2 = this.getMaxY();
        if (rect.getMaxY() > y2)
          y2 = rect.getMaxY();
        this.x = x1;
        this.y = y1;
        this.width = x2 - x1;
        this.height = y2 - y1;
      },

      intersectWith:function (rect) {
        O$.assert(rect, "rect parameter should be passed");

        var x1 = O$.maxDefined(this.getMinX(), rect.getMinX());
        var x2 = O$.minDefined(this.getMaxX(), rect.getMaxX());
        var y1 = O$.maxDefined(this.getMinY(), rect.getMinY());
        var y2 = O$.minDefined(this.getMaxY(), rect.getMaxY());
        if (x2 < x1)
          x2 = x1;
        if (y2 < y1)
          y2 = y1;

        this.x = x1;
        this.y = y1;

        this.width = x1 !== undefined && x2 !== undefined ? x2 - x1 : undefined;
        this.height = y1 !== undefined && y2 !== undefined ? y2 - y1 : undefined;
      },

      intersects:function (rect) {
        var x1 = this.getMinX();
        var x2 = this.getMaxX();
        var rectX1 = rect.getMinX();
        var rectX2 = rect.getMaxX();
        var y1 = this.getMinY();
        var y2 = this.getMaxY();
        var rectY1 = rect.getMinY();
        var rectY2 = rect.getMaxY();

        return (rectX2 > x1 && rectY2 > y1 && rectX1 < x2 && rectY1 < y2);
      },

      containsRectangle:function (rect) {
        var x1 = this.getMinX();
        var x2 = this.getMaxX();
        var rectX1 = rect.getMinX();
        var rectX2 = rect.getMaxX();
        var y1 = this.getMinY();
        var y2 = this.getMaxY();
        var rectY1 = rect.getMinY();
        var rectY2 = rect.getMaxY();

        return (x1 <= rectX1 && x2 >= rectX2 && y1 <= rectY1 && y2 >= rectY2);
      },

      containsPoint:function (x, y) {
        return x >= this.getMinX() && x <= this.getMaxX() &&
                y >= this.getMinY() && y <= this.getMaxY();
      }

    })
  });

  /* GraphicLine class -- displays a horizontal or vertical line on the specified element. */
  O$.GraphicLine = O$.createClass({
    ALIGN_BY_TOP_OR_LEFT:"alignByTopOrLeft",
    ALIGN_BY_CENTER:"alignByCenter",
    ALIGN_BY_BOTTOM_OR_RIGHT:"alignByBottomOrRight"
  }, {
    constructor:function (lineStyle, alignment, x1, y1, x2, y2) {
      this._element = document.createElement("div");
      this._element.style.visibility = "hidden";
      this._element.style.position = "absolute";
      this._element.style.fontSize = "0";

      if (x1)
        this.setLine(x1, y1, x2, y2, true);
      if (!lineStyle)
        lineStyle = "1px solid black";
      this.setLineStyle(lineStyle, true);
      if (!alignment)
        alignment = O$.GraphicLine.ALIGN_BY_CENTER;
      this.setAlignment(alignment, false);
    },

    setLine:function (x1, y1, x2, y2, dontUpdateNow) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
      var horizontal = y1 == y2;
      if (!horizontal && x1 != x2)
        throw "GraphicLine.setLine: Only horizontal and vertical lines are supported.";
      if (!dontUpdateNow)
        this.updatePresentation();
    },

    setLineStyle:function (lineStyle, dontUpdateNow) {
      this.lineStyle = lineStyle;
      if (!dontUpdateNow)
        this.updatePresentation();
    },

    setAlignment:function (alignment, dontUpdateNow) {
      this.alignment = alignment;
      if (!dontUpdateNow)
        this.updatePresentation();
    },

    updatePresentation:function () {
      if (this.x1 === undefined || this.y1 === undefined || this.x2 === undefined || this.y2 === undefined) {
        this._element.style.visibility = "hidden";
        return;
      }
      var horizontal = this.y1 == this.y2;
      if (!horizontal && this.x1 != this.x2)
        throw "GraphicLine.updatePresentation: Only horizontal and vertical lines are supported.";
      var width;
      if (horizontal) {
        this._element.style.borderTop = this.lineStyle;
        this._element.style.borderLeft = "none";
        width = O$.getNumericElementStyle(this._element, "border-top-width");
      } else {
        this._element.style.borderLeft = this.lineStyle;
        this._element.style.borderTop = "none";
        width = O$.getNumericElementStyle(this._element, "border-left-width");
      }
      var alignment = this.alignment;
      var alignmentCorrection =
              alignment == O$.GraphicLine.ALIGN_BY_TOP_OR_LEFT ? 0 :
                      alignment == O$.GraphicLine.ALIGN_BY_CENTER || !alignment ? -width / 2 :
                              alignment == O$.GraphicLine.ALIGN_BY_BOTTOM_OR_RIGHT ? -width :
                                      (function () {
                                        throw "Invalid alignment: " + alignment;
                                      })();
      O$.setElementBorderRectangle(this._element,
              horizontal ? new O$.Rectangle(this.x1, this.y1 + alignmentCorrection, this.x2 - this.x1, width)
                      : new O$.Rectangle(this.x1 + alignmentCorrection, this.y1, width, this.y2 - this.y1));
    },

    show:function (parentElement) {
      if (!parentElement)
        parentElement = O$.getDefaultAbsolutePositionParent();
      this._element.style.visibility = "visible";
      if (this._element.parentNode != parentElement)
        parentElement.appendChild(this._element);
      // updatePresentation here is needed for recalculating border width after an element is already in DOM
      this.updatePresentation();
    },

    hide:function () {
      this._element.style.visibility = "hidden";
    },

    remove:function () {
      this._element.parentNode.remove(this._element);
    }
  });

  /* GraphicRectangle class -- displays a rectangle on the specified element. */
  O$.GraphicRectangle = O$.createClass({
    /* the following constants deptermine the way that outline line is aligned with respect to the rectangle's edge
     * (this is especially important for thick lines) */
    ALIGN_INSIDE:"alignInside", // the entire outline line is aligned to be inside of the rectangle
    ALIGN_CENTER_LINE:"alignCenterLine", // center of the outline line is aligned with the rectangle edge
    ALIGN_OUTSIDE:"alignOutside" // the entire contents of outline line is aligned to be outside of the rectangle
  }, {
    constructor:function (lineStyle, lineAlignment, rectangle) {
      this._leftLine = new O$.GraphicLine();
      this._rightLine = new O$.GraphicLine();
      this._topLine = new O$.GraphicLine();
      this._bottomLine = new O$.GraphicLine();
      if (rectangle)
        this.setRectangle(rectangle, true);
      this.setLineStyle(lineStyle, true);
      if (!lineAlignment)
        lineAlignment = O$.GraphicRectangle.ALIGN_INSIDE;
      this.setLineAlignment(lineAlignment, false);
    },

    setRectangle:function (rectangle, dontUpdateNow) {
      this.rectangle = rectangle;
      if (!dontUpdateNow)
        this._updateRect();
    },

    setLineStyle:function (lineStyle, dontUpdateNow) {
      this.lineStyle = lineStyle;
      this._leftLine.setLineStyle(lineStyle, dontUpdateNow);
      this._rightLine.setLineStyle(lineStyle, dontUpdateNow);
      this._topLine.setLineStyle(lineStyle, dontUpdateNow);
      this._bottomLine.setLineStyle(lineStyle, dontUpdateNow);
      if (!dontUpdateNow)
        this._updateRect();
    },

    setLineAlignment:function (lineAlignment, dontUpdateNow) {
      var topAndLeftLinesAlignment =
              lineAlignment == O$.GraphicRectangle.ALIGN_INSIDE ? O$.GraphicLine.ALIGN_BY_TOP_OR_LEFT :
                      lineAlignment == O$.GraphicRectangle.ALIGN_OUTSIDE ? O$.GraphicLine.ALIGN_BY_BOTTOM_OR_RIGHT :
                              O$.GraphicLine.ALIGN_BY_CENTER;
      var bottomAndRightLinesAlignment =
              lineAlignment == O$.GraphicRectangle.ALIGN_INSIDE ? O$.GraphicLine.ALIGN_BY_BOTTOM_OR_RIGHT :
                      lineAlignment == O$.GraphicRectangle.ALIGN_OUTSIDE ? O$.GraphicLine.ALIGN_BY_TOP_OR_LEFT :
                              O$.GraphicLine.ALIGN_BY_CENTER;
      this._leftLine.setAlignment(topAndLeftLinesAlignment, dontUpdateNow);
      this._topLine.setAlignment(topAndLeftLinesAlignment, dontUpdateNow);
      this._rightLine.setAlignment(bottomAndRightLinesAlignment, dontUpdateNow);
      this._bottomLine.setAlignment(bottomAndRightLinesAlignment, dontUpdateNow);
      if (!dontUpdateNow)
        this._updateRect();
    },

    _updateRect:function () {
      var rect = this.rectangle;
      if (!rect)
        return;
      var x1 = rect.getMinX();
      var x2 = rect.getMaxX();
      var y1 = rect.getMinY();
      var y2 = rect.getMaxY();
      var lineWidth = O$.calculateLineWidth(this.lineStyle);
      var alignment = this.alignment;
      var cornerSpacing =
              alignment == O$.GraphicRectangle.ALIGN_INSIDE ? 0 :
                      alignment == O$.GraphicRectangle.ALIGN_OUTSIDE ? lineWidth :
                              alignment == O$.GraphicRectangle.ALIGN_CENTER_LINE || !alignment ? lineWidth / 2 :
                                      (function () {
                                        throw "Unknown alignment: " + alignment;
                                      })();

      this._leftLine.setLine(x1, y1 - cornerSpacing, x1, y2 + cornerSpacing);
      this._rightLine.setLine(x2, y1 - cornerSpacing, x2, y2 + cornerSpacing);
      this._topLine.setLine(x1 - cornerSpacing, y1, x2 + cornerSpacing, y1);
      this._bottomLine.setLine(x1 - cornerSpacing, y2, x2 + cornerSpacing, y2);
    },

    show:function (parentElement) {
      this._leftLine.show(parentElement);
      this._rightLine.show(parentElement);
      this._topLine.show(parentElement);
      this._bottomLine.show(parentElement);
    },

    hide:function () {
      this._leftLine.hide();
      this._rightLine.hide();
      this._topLine.hide();
      this._bottomLine.hide();
    },

    remove:function () {
      this._leftLine.remove();
      this._rightLine.remove();
      this._topLine.remove();
      this._bottomLine.remove();
    }
  });


  // ----------------- DEBUG ---------------------------------------------------

  O$.logError = function (message) {
    if (O$.DEBUG)
      alert("ERROR: " + message);
  };

  O$.logWarning = function (message) {
    //  if (O$.DEBUG)
    //    alert("WARNING: " + message);
  };


  O$.assert = function (value, message) {
    if (value !== null && value !== undefined && value !== false)
      return;
    if (!message)
      throw "assert failed with out message";
    else
      throw message;
  };

  O$.assertEquals = function (expectedValue, actualValue, message) {
    if (expectedValue == actualValue)
      return;
    O$.logError((message ? message + " ; " : "") + "expected: " + expectedValue + ", but was: " + actualValue);
  };


  setTimeout(function () {
    O$._logEnabled = true;
  }, 0);
  O$.log = function (text) {
    if (O$.debug) {
      O$.debug.log(text);
      return;
    }

    disableMenu();
    if (!O$._logEnabled)
      return;
    if (!O$._logger)
      O$._logger = new O$.Logger();
    O$._logger.log(text);
  };

  O$.Logger = function () {
    var div = document.createElement("div");
    div.style.position = "absolute";
    div.style.left = "200px";
    div.style.top = "400px";
    div.style.width = "700px";
    div.style.height = "200px";
    div.style.overflow = "auto";
    div.style.background = "white";
    div.style.border = "2px solid black";
    div.style.zIndex = 10000;
    document.body.appendChild(div);

    if (!O$._loggersCreated)
      O$._loggersCreated = 1;
    else
      O$._loggersCreated++;
    div.id = "_o_logger_" + O$._loggersCreated;
    //  O$.initPopupLayer(div.id, 100, 100, 700, 200, null, null, true);

    this._scrollingDiv = div;
    //  this._win = window.open("about:blank", "O$.Logger");
    this.log = function (text) {
      var date = new Date();
      var br = document.createElement("br");
      if (this._scrollingDiv.childNodes.length > 0)
        this._scrollingDiv.insertBefore(br, this._scrollingDiv.childNodes[0]);
      else
        this._scrollingDiv.appendChild(br);
      this._scrollingDiv.insertBefore(document.createTextNode(text), this._scrollingDiv.childNodes[0]);
      this._scrollingDiv.insertBefore(document.createTextNode(date + " : "), this._scrollingDiv.childNodes[0]);
      //    this._div.innerHTML = date + " : " + text + "<br/>" + this._div.innerHTML;
    };
  };

  O$.Profiler = function () {
    this._timeStamps = [];
    this._timeAccumulators = [];

    this.logTimeStamp = function (name) {
      this._timeStamps.push({time:new Date(), name:name});
    };

    this.startMeasuring = function (name) {
      var timeAccumulator = this._timeAccumulators[name];
      if (timeAccumulator == null) {
        timeAccumulator = {name:name, secondsElapsed:0.0, lastPeriodStartDate:null};
        this._timeAccumulators[name] = timeAccumulator;
        this._timeAccumulators.push(timeAccumulator);
      }
      O$.assert(!timeAccumulator.lastPeriodStartDate, "O$.Profiler.startMeasuring cannot be called twice for the same name without endMeasuring being called: " + name);
      timeAccumulator.lastPeriodStartDate = new Date();
    };

    this.endMeasuring = function (name) {
      var dateAfter = new Date();
      var timeAccumulator = this._timeAccumulators[name];
      O$.assert(timeAccumulator, "O$.Profiler.endMeasuring: startMeasuring wasn't called for name: " + name);
      O$.assert(timeAccumulator.lastPeriodStartDate, "O$.Profiler.endMeasuring: startMeasuring wasn't called for name: " + name);
      var dateBefore = timeAccumulator.lastPeriodStartDate;
      timeAccumulator.lastPeriodStartDate = null;
      var secondsElapsed = (dateAfter.getTime() - dateBefore.getTime()) / 1000;
      timeAccumulator.secondsElapsed += secondsElapsed;
    };

    this.showTimeStamps = function () {
      var result = "";
      for (var i = 0, count = this._timeStamps.length - 1; i < count; i++) {
        var stampBefore = this._timeStamps[i];
        var stampAfter = this._timeStamps[i + 1];
        var elapsed = (stampAfter.time.getTime() - stampBefore.time.getTime()) / 1000;
        result += stampBefore.name + " - " + stampAfter.name + " : " + elapsed + "\n";
      }
      alert(result);
    };

    this.showTimeMeasurements = function () {
      var result = "";
      for (var i = 0, count = this._timeAccumulators.length; i < count; i++) {
        var accumulator = this._timeAccumulators[i];
        var name = accumulator.name;
        var elapsed = accumulator.secondsElapsed;
        result += name + " : " + elapsed + "\n";
      }
      alert(result);
    };

    this.showAllTimings = function () {
      var result = "--- Time-stamps: --- \n\n";
      var i, count, elapsed;
      for (i = 0, count = this._timeStamps.length - 1; i < count; i++) {
        var stampBefore = this._timeStamps[i];
        var stampAfter = this._timeStamps[i + 1];
        elapsed = (stampAfter.time.getTime() - stampBefore.time.getTime()) / 1000;
        result += stampBefore.name + " - " + stampAfter.name + " : " + elapsed + "\n";
      }
      result += "\n--- Time measurements: --- \n\n";
      for (i = 0, count = this._timeAccumulators.length; i < count; i++) {
        var accumulator = this._timeAccumulators[i];
        var name = accumulator.name;
        elapsed = accumulator.secondsElapsed;
        result += name + " : " + elapsed + "\n";
      }
      alert(result);
    };

  };

  // ----------------- STRING, ARRAYS, OTHER LANGUAGE UTILITIES ---------------------------------------------------

  O$.stringsEqualIgnoreCase = function (str1, str2) {
    if (str1)
      str1 = str1.toLowerCase();
    if (str2)
      str2 = str2.toLowerCase();
    return str1 == str2;
  };

  O$.StringBuffer = function () {
    this._strings = [];
    this.append = function (value) {
      this._strings.push(value);
      return this;
    };
    this.toString = function () {
      return this._strings.join("");
    };
    this.getNextIndex = function () {
      return this._strings.length;
    };
    this.setValueAtIndex = function (index, value) {
      this._strings[index] = value;
    };
  };

  O$.ltrim = function (value) {
    var re = /\s*((\S+\s*)*)/;
    return value.replace(re, "$1");
  };

  O$.rtrim = function (value) {
    var re = /((\s*\S+)*)\s*/;
    return value.replace(re, "$1");
  };

  O$.trim = function (value) {
    if (value instanceof Array) {
      var strValue = "";
      for (var i = 0; i < value.length; i++) {
        strValue += value[i];
      }
      value = strValue;
    }
    return O$.ltrim(O$.rtrim(value));
  };

  O$.stringEndsWith = function (str, ending) {
    if (!str || !ending)
      return false;
    var endingLength = ending.length;
    var length = str.length;
    if (length < endingLength)
      return false;
    var actualEnding = str.substring(length - endingLength, length);
    return actualEnding == ending;
  };

  O$.stringStartsWith = function (str, text) {
    if (!str || !text)
      return false;
    var textLength = text.length;
    var length = str.length;
    if (length < textLength)
      return false;
    var actualStartText = str.substring(0, textLength);
    return actualStartText == text;
  };

  O$.escapeSymbol = function (str, escapedChars) {
    var res = new O$.StringBuffer();
    for (var i = 0, count = str.length; i < count; i++) {
      var currChar = str.charAt(i);
      if (currChar == "\\") {
        res.append("\\");
      } else {
        var index = escapedChars.indexOf(currChar);
        if (index != -1) {
          var fullCharCode = new String(escapedChars[index].charCodeAt() + 10000);
          res.append("\\" + fullCharCode.substr(1, fullCharCode.length));
          continue;
        }
      }
      res.append(currChar);
    }
    return res.toString();
  };

  O$.unescapeSymbol = function (str) {
    var LENGTH_UNICODE = 4;
    var LENGTH_BACKSLASH_AND_UNICODE = 5;
    var buf = "";
    for (var i = 0; i < str.length;) {
      var c = str.charAt(i);
      if (c == '\\' && i < str.length - LENGTH_UNICODE) {
        if (str.charAt(i + 1) == '\\') {
          buf += '\\';
          i = i + 2;
          continue;
        } else {
          var charCode = eval(str.substring(i + 1, i + LENGTH_BACKSLASH_AND_UNICODE));
          buf += String.fromCharCode(charCode);
          i = i + LENGTH_BACKSLASH_AND_UNICODE;
          continue;
        }
      }
      buf += c;
      i++;
    }
    return buf;
  };

  O$.getArrayFromString = function (str, delimiter) {
    var idx = str.indexOf(delimiter);
    var arr = [];
    var arrIdx = 0;
    while (idx != -1) {
      arr[arrIdx++] = str.substring(0, idx);
      str = str.substring(idx + 1);
      idx = str.indexOf(delimiter);
    }
    arr[arrIdx] = str;
    return arr;
  };

  O$.arrayContainsValue = function (array, value) {
    var idx = array.indexOf(value);
    return idx != -1;
  };

  O$.unescapeHtml = function (val) {
    var re = /\&\#(\d+)\;/;
    while (re.test(val)) {
      val = val.replace(re, String.fromCharCode(RegExp.$1));
    }
    return val;
  };

  if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (elt /*, from*/) {
      var len = this.length >>> 0;

      var from = Number(arguments[1]) || 0;
      from = (from < 0)
              ? Math.ceil(from)
              : Math.floor(from);
      if (from < 0)
        from += len;

      for (; from < len; from++) {
        if (from in this &&
                this[from] === elt)
          return from;
      }
      return -1;
    };
  }

  if (!Array.prototype.every) {
    Array.prototype.every = function (fun /*, thisp*/) {
      var len = this.length >>> 0;
      if (typeof fun != "function")
        throw new TypeError();

      var thisp = arguments[1];
      for (var i = 0; i < len; i++) {
        if (i in this &&
                !fun.call(thisp, this[i], i, this))
          return false;
      }

      return true;
    };
  }

  if (!Array.prototype.some) {
    Array.prototype.some = function (fun /*, thisp*/) {
      var i = 0;
      var len = this.length >>> 0;

      if (typeof fun != "function")
        throw new TypeError();

      var thisp = arguments[1];
      for (; i < len; i++) {
        if (i in this &&
                fun.call(thisp, this[i], i, this))
          return true;
      }

      return false;
    };
  }

  if (!Array.prototype.forEach) {
    Array.prototype.forEach = function (fun, thisp) {
      var len = this.length >>> 0;
      if (typeof fun != "function")
        throw new TypeError();

      for (var i = 0; i < len; i++) {
        if (i in this)
          fun.call(thisp, this[i], i, this);
      }
    };
  }

  if (!Array.prototype.map) {
    Array.prototype.map = function (fun /*, thisp*/) {
      var len = this.length >>> 0;
      if (typeof fun != "function")
        throw new TypeError();

      var res = new Array(len);
      var thisp = arguments[1];
      for (var i = 0; i < len; i++) {
        if (i in this)
          res[i] = fun.call(thisp, this[i], i, this);
      }

      return res;
    };
  }

  if (!Array.prototype.filter) {
    Array.prototype.filter = function (fun /*, thisp*/) {
      var len = this.length;
      if (typeof fun != "function")
        throw new TypeError();

      var res = new Array();
      var thisp = arguments[1];
      for (var i = 0; i < len; i++) {
        if (i in this) {
          var val = this[i]; // in case fun mutates this
          if (fun.call(thisp, val, i, this))
            res.push(val);
        }
      }

      return res;
    };
  }
  O$.dateByTimeMillis = function (time) {
    var date = new Date();
    date.setTime(time);
    return date;
  };

  O$.cloneDate = function (date) {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate());
  };

  O$.cloneDateTime = function (date) {
    var clonedDate = new Date();
    clonedDate.setTime(date.getTime());
    return clonedDate;
  };

  O$.incDay = function (date, increment) {
    if (increment == undefined)
      increment = 1;
    var backupDate = new Date(date.getTime());
    date.setHours(12, 0, 0, 0);
    var newDate = new Date(date.getTime() + increment * 86400000);
    var removedDstHours = newDate.getHours() - 12; // accounts for DST transition days, e.g. on Oct 10->11 in Chile (Santiago) UTC-4
    if (removedDstHours < 0)
      removedDstHours = 0;
    newDate.setHours(backupDate.getHours() + removedDstHours,
            backupDate.getMinutes(),
            backupDate.getSeconds(),
            backupDate.getMilliseconds());
    return newDate;
  };

  O$._datesEqual = function (date1, date2) {
    if (!date1)
      return !date2;
    if (!date2)
      return false;
    return date1.getDate() == date2.getDate() &&
            date1.getMonth() == date2.getMonth() &&
            date1.getFullYear() == date2.getFullYear();
  };

  O$.minDefined = function (value1, value2) {
    if (value1 != undefined && value2 != undefined && !isNaN(value1) && !isNaN(value2))
      return Math.min(value1, value2);
    return value1 !== undefined && !isNaN(value1) ? value1 : value2;
  };

  O$.maxDefined = function (value1, value2) {
    if (value1 != undefined && value2 != undefined && !isNaN(value1) && !isNaN(value2))
      return Math.max(value1, value2);
    return value1 !== undefined && !isNaN(value1) ? value1 : value2;
  };

  O$.invokeOnce = function (func, funcId) {
    if (!O$.isInvoked(funcId)) {
      func();
      O$.getInvokedFunctions().push(funcId);
    }
  };

  O$.isInvoked = function (funcId) {
    var invokedFunctions = O$.getInvokedFunctions();
    return O$.contains(invokedFunctions, funcId);
  };

  O$.getInvokedFunctions = function () {
    var invokedFunctions = O$._invokedFunctions;
    if (!invokedFunctions) {
      invokedFunctions = [];
      O$._invokedFunctions = invokedFunctions;
    }
    return invokedFunctions;
  };

  O$.contains = function (array, object) {
    for (var i = 0, count = array.length; i < count; i++) {
      if (object == array[i])
        return true;
    }
    return false;
  };

  O$.addAll = function (dest, src) {
    for (var i = 0, count = src.length; i < count; i++)
      dest.push(src[i]);
  };

  O$.isUpperCase = function (str) {
    return str.toUpperCase() == str;
  };

  O$.isLowerCase = function (str) {
    return str.toLowerCase() == str;
  };


  // ----------------- BROWSER DETECTION ---------------------------------------------------

  O$.userAgentContains = function (browserName) {
    return navigator.userAgent.toLowerCase().indexOf(browserName.toLowerCase()) > -1;
  };

  O$.isStrictMode = function () {
    return !O$.isQuirksMode();
  };
  O$.isQuirksMode = function () {
    return document.compatMode == "BackCompat";
  };

  O$.isMozillaFF = function () {
    if (O$._mozilla == undefined)
      O$._mozilla = O$.userAgentContains("mozilla") &&
              !O$.userAgentContains("msie") &&
              !O$.userAgentContains("safari");
    return O$._mozilla;
  };

  O$.isMozillaFF2 = function () {
    return O$.isMozillaFF() && O$.userAgentContains("Firefox/2");
  };

  O$.isMozillaFF3 = function () {
    return O$.isMozillaFF() && O$.userAgentContains("Firefox/3.");
  };

  O$.isExplorer = function () {
    if (O$._explorer == undefined)
      O$._explorer = O$.userAgentContains("msie") && !O$.userAgentContains("opera");
    return O$._explorer;
  };

  O$.isExplorer6 = function () {
    if (O$._explorer6 == undefined)
      O$._explorer6 = O$.isExplorer() && O$.userAgentContains("MSIE 6");
    return O$._explorer6;
  };

  O$.isExplorer7 = function () {
    if (O$._explorer7 == undefined)
      O$._explorer7 = O$.isExplorer() && O$.userAgentContains("MSIE 7");
    return O$._explorer7;
  };

  O$.isExplorer8 = function () {
    if (O$._explorer8 == undefined)
      O$._explorer8 = O$.isExplorer() && O$.userAgentContains("MSIE 8");
    return O$._explorer8;
  };

  O$.isExplorer8AndOlder = function () {
    if (O$._explorer8AndOlder == undefined)
      O$._explorer8AndOlder = O$.isExplorer6() || O$.isExplorer7() || O$.isExplorer8();
    return O$._explorer8AndOlder;
  };

  O$.isExplorer9 = function () {
    if (O$._explorer9 == undefined)
      O$._explorer9 = O$.isExplorer() && O$.userAgentContains("MSIE 9");
    return O$._explorer9;
  };

  O$.isOpera9AndLate = function () {
    if (O$._opera9 == undefined) {
      if (O$.isOpera()) {
        O$._opera9 = /\bOpera(\s|\/)(\d{2,}|([9]){1})/i.test(navigator.userAgent);
      }
    }
    return O$._opera9;
  };

  O$.isSafari3AndLate = function () {
    if (O$._safari3 == undefined)
      if (O$.isSafari()) {
        O$._safari3 = /\bversion(\s|\/)(\d{2,}|[3-9]{1})/i.test(navigator.userAgent);
      }
    return O$._safari3;
  };

  O$.isSafari4AndLate = function () {
    if (O$._safari4 == undefined)
      if (O$.isSafari()) {
        O$._safari4 = /\bversion(\s|\/)(\d{2,}|[4-9]{1})/i.test(navigator.userAgent);
      }
    return O$._safari4;
  };

  O$.isSafari3 = function () {
    if (O$._safari3only == undefined) {
      O$._safari3only = O$.isSafari3AndLate() && !O$.isSafari4AndLate();
    }
    return O$._safari3only;
  };

  O$.isSafariOnMac = function () {
    if (O$._safariOnMac == undefined) {
      O$._safariOnMac = O$.isSafari() && O$.userAgentContains("Macintosh");
    }
    return O$._safariOnMac;
  };

  O$.isSafariOnWindows = function () {
    if (O$._safariOnWindows == undefined) {
      O$._safariOnWindows = O$.isSafari() && O$.userAgentContains("Windows");
    }
    return O$._safariOnWindows;
  };

  O$.isSafari2 = function () {
    if (O$._safari2 == undefined) {
      O$._safari2 = O$.isSafari() && !O$.isSafari3AndLate() && !O$.isChrome();
    }
    return O$._safari2;
  };

  O$.isChrome = function () {
    if (O$._chrome == undefined)
      O$._chrome = O$.userAgentContains("Chrome");
    return O$._chrome;
  };

  O$.isOpera = function () {
    if (O$._opera == undefined)
      O$._opera = O$.userAgentContains("opera");
    return O$._opera;
  };

  O$.isOpera10_5AndLater = function () {
    return O$.isOperaLaterThan(10.5);
  };

  O$.isOperaLaterThan = function (version) {
    if (!O$.isOpera())
      return false;

    if (/version[\/\s](\d+\.\d+)/.test(navigator.userAgent.toLowerCase())) {
      var operaVersionNumber = new Number(RegExp.$1);
      if (operaVersionNumber >= version) {
        return true;
      }
    }

    return false;
  };

  O$.isSafari = function () { // todo: returns true for Chrome as well -- fix this and update usages to account Chrome
    if (O$._safari == undefined)
      O$._safari = O$.userAgentContains("safari");
    return O$._safari;
  };

  // ----------------- DOM FUNCTIONS ---------------------------------------------------

  O$.byIdOrName = function (idOrName) {
    var el = O$(idOrName);
    if (!el)
      el = document.getElementsByName(idOrName)[0];
    return el;
  };


  O$.getParentNode = function (element, tagName) {
    tagName = tagName.toUpperCase();
    while (element) {
      var elementNodeName = element.nodeName;
      if (elementNodeName)
        elementNodeName = elementNodeName.toUpperCase();
      if (elementNodeName == tagName)
        break;
      element = element.parentNode;
    }
    return element;
  };

  O$.getAnyParentNode = function (element, tagNames) {
    for (var i = 0, count = tagNames.length; i < count; i++)
      tagNames[i] = tagNames[i].toUpperCase();
    while (element && tagNames.indexOf(element.nodeName.toUpperCase()) == -1)
      element = element.parentNode;
    return element;
  };

  O$.isChild = function (parent, child) {
    if (parent.id && child.id && parent.id == child.id) return true;
    if (child.parentNode && child.parentNode.nodeName && child.parentNode.nodeName.toUpperCase() != "BODY") {
      return O$.isChild(parent, child.parentNode);
    }
    return false;
  };

  O$.isChildNode = function (parent, child) {
    for (var node = child; node; node = node.parentNode) {
      if (node.parentNode == parent)
        return true;
    }
    return false;
  };

  O$.getChildNodesByClass = function (node, className, searchTopLevelOnly, excludeElementFromSearch) {
    var result = [];
    var children = node.childNodes;
    for (var i = 0, count = children.length; i < count; i++) {
      var child = children[i];
      if (child.className == className)
        result.push(child);
      //    var childClass = child.className;
      //    var childClassNames = childClass ? childClass.split(" ") : [];
      //    if (childClassNames.O$.indexOf(className))
      if (child == excludeElementFromSearch)
        continue;
      var subResult = !searchTopLevelOnly && O$.getChildNodesByClass(child, className, false, excludeElementFromSearch);
      for (var childIndex = 0, subResultCount = subResult.length; childIndex < subResultCount; childIndex++) {
        var innerResult = subResult[childIndex];
        result.push(innerResult);
      }
    }
    return result;
  };

  O$.getChildNodesWithNames = function (node, nodeNames) {
    var selectedChildren = [];
    var children = node.childNodes;
    for (var i = 0, count = children.length; i < count; i++) {
      var child = children[i];
      var childNodeName = child.nodeName;
      if (childNodeName)
        childNodeName = childNodeName.toLowerCase();
      for (var j = 0, jCount = nodeNames.length; j < jCount; j++) {
        var nodeName = nodeNames[j];
        nodeName = nodeName.toLowerCase();
        if (childNodeName == nodeName)
          selectedChildren.push(child);
      }
    }
    return selectedChildren;
  };

  O$.getElementsByTagNameRegexp = function (parent, regexp, optionalFilter) {
    var elem_array = [];
    if (typeof parent.firstChild != "undefined") {
      var elem = parent.firstChild;
      while (elem != null) {
        if (typeof elem.firstChild != "undefined") {
          elem_array = elem_array.concat(O$.getElementsByTagNameRegexp(elem, regexp, optionalFilter));
        }
        var reg = new RegExp(regexp);
        if (elem.nodeName.match(reg)) {
          if (!optionalFilter || optionalFilter(elem))
            elem_array.push(elem);
        }
        elem = elem.nextSibling;
      }
    }
    return elem_array;
  };

  O$.getElementByPath = function (node, childPath, ignoreNonExistingElements) {
    var separatorIndex = childPath.indexOf("/");
    var locator = separatorIndex == -1 ? childPath : childPath.substring(0, separatorIndex);
    var remainingPath = separatorIndex != -1 ? childPath.substring(separatorIndex + 1) : null;

    var bracketIndex = locator.indexOf("[");
    var childElementName = bracketIndex == -1 ? locator : locator.substring(0, bracketIndex);
    var childElementIndex;
    if (bracketIndex == -1) {
      childElementIndex = 0;
    } else {
      O$.assert(O$.stringEndsWith(locator, "]"), "O$.getElementByPath: unparsable element locator - non-matching brackets: " + childPath);
      var indexStr = locator.substring(bracketIndex + 1, locator.length - 1);
      try {
        childElementIndex = parseInt(indexStr);
      } catch (e) {
        if (ignoreNonExistingElements)
          return null;
        throw "O$.getElementByPath: Couldn't parse child index (" + indexStr + "); childPath = " + childPath;
      }
    }
    var childrenByName = O$.getChildNodesWithNames(node, [childElementName]);
    if (childrenByName.length == 0) {
      if (ignoreNonExistingElements)
        return null;
      throw "O$.getElementByPath: Couldn't find child nodes by element name: " + childElementName + " ; childPath = " + childPath;
    }
    var child = childrenByName[childElementIndex];
    if (!child) {
      if (ignoreNonExistingElements)
        return null;
      throw "O$.getElementByPath: Child not found by index: " + childElementIndex + " ; childPath = " + childPath;
    }
    if (remainingPath == null)
      return child;
    else
      return O$.getElementByPath(child, remainingPath);
  };

  O$.isElementPresentInDocument = function (element) {
    var result = false;
    if (element == document) {
      return true;
    }
    if (element.parentNode != null) {
      result = O$.isElementPresentInDocument(element.parentNode);
    }
    return result;
  };

  O$.removeAllChildNodes = function (element) {
    while (element.childNodes.length > 0)
      element.removeChild(element.childNodes[0]);
  };

  O$.setInnerText = function (element, text, escapeHtml) {
    if (escapeHtml === false) {
      try {
        element.innerHTML = text;
      } catch (e) {
        alert("Error: " + e + "; Couldn't set innerHTML to: \"" + text + "\"");
        throw e;
      }
      return;
    }
    O$.removeAllChildNodes(element);
    element.appendChild(document.createTextNode(text));
  };

  O$.createStyledText = function (text, styleClass) {
    var container = document.createElement("span");
    container.appendChild(document.createTextNode(text));
    container.className = styleClass;
    return container;
  };

  O$.removeIdsFromNode = function removeIDs(rootNode) {
    if (rootNode.attributes && (rootNode.getAttribute("id") != undefined))
      rootNode.removeAttribute("id");
    if (rootNode.hasChildNodes()) {
      for (var i = 0; i < rootNode.childNodes.length; i++) {
        removeIDs(rootNode.childNodes[i]);
      }
    }
  }

  // ----------------- AJAX RELATED UTILITY FUNCTIONS -----------------------------------------------------
  O$.lockAjax = function () {
    O$._ajaxTemporaryLocked = true;
  };

  O$.isAjaxInLockedState = function () {
    return O$._ajaxTemporaryLocked;
  };

  O$.resetAjaxState = function () {
    O$._ajaxTemporaryLocked = false;
  };

  // ----------------- FORM, FORM ELEMENTS MANIPULATION ---------------------------------------------------

  O$.submitEnclosingForm = function (element) {
    O$.assert(element, "element should be passed to O$.submitEnclosingForm");
    var frm = O$.getParentNode(element, "FORM");
    O$.assert(frm, "O$.submitEnclosingForm: Enclosing form not found for element with id: " + element.id + "; element tag name: " + element.tagName);
    if (frm.onsubmit)
      if (!frm.onsubmit())
        return;

    frm.submit();
  };

  O$.submitEnclosingElementsForm = function (elements) {
    O$.assert(elements[0], "elements should be passed to O$.submitEnclosingElementsForm");
    var frm = O$.getParentNode(elements[0], "FORM");
    if (!elements.every(function (element) {
      return (frm == O$.getParentNode(element, "FORM"));
    })) {
      O$.logError("O$.submitEnclosingElementsForm: Enclosing forms differ for components");
    }
    O$.assert(frm, "O$.submitEnclosingElementsForm: Enclosing form not found for elements");
    if (frm.onsubmit)
      if (!frm.onsubmit())
        return;

    frm.submit();
  };

  O$.submitWithParams = function (element, params) {
    O$.assert(element, "element should be passed to O$.submitWithParams");
    O$.assert(params, "params should be passed to O$.submitWithParams");
    for (var i = 0, count = params.length; i < count; i++) {
      var param = params[i];
      O$.setHiddenField(element, param[0], param[1]);
    }

    var frm = O$.getParentNode(element, "FORM");
    frm.submit();
  };

  O$.submitWithParam = function (elt, paramName, paramValue) {
    if (typeof elt == "string")
      elt = O$(elt);
    O$.submitWithParams(elt, [
      [paramName, paramValue]
    ]);
  };

  O$.setHiddenField = function (element, fieldName, fieldValue) {
    var silent = true;
    var frm;
    if (!element) {
      frm = document.forms[0];
      if (!frm) {
        if (!silent)
          O$.assert(frm, "O$.setHiddenField: There must be a form in the document");
      }
    } else {
      frm = element._form ? element._form : O$.getParentNode(element, "FORM");
      if (!frm) {
        if (!silent)
          O$.assert(frm, "O$.setHiddenField: Enclosing form not found for element with id: " + element.id + "; element tag name: " + element.tagName);
      }
    }
    if (!frm)
      frm = O$.getDefaultAbsolutePositionParent();
    var existingField = O$(fieldName);
    var newParamField = existingField ? existingField : document.createElement("input");
    if (!existingField) {
      newParamField.type = "hidden";
      newParamField.id = fieldName;
      newParamField.name = fieldName;
      frm.appendChild(newParamField);
    }
    if (fieldValue == null)
      fieldValue = "";
    newParamField.value = fieldValue;
    return newParamField;
  };

  O$.submitById = function (elementId) {
    var element = O$(elementId);
    O$.assert(element, "correct element id should be passed to O$.submitById");
    O$.submitEnclosingForm(element);
  };

  O$.submitByIds = function (elementIds) {
    var elements = [];
    for (var i = 0, count = elementIds.length; i < count; i++) {
      elements[elements.length] = O$(elementIds[i]);
      O$.assert(elements[elements.length - 1], "correct element id should be passed to O$.submitByIds");
    }
    O$.submitEnclosingElementsForm(elements);
  };

  O$.setValue = function (elementId, value) {
    var field = O$(elementId);
    O$.assert(field, "Correct element id should be passed to O$.setValue; elementId = " + elementId);
    if (field) {
      field.value = value;
    }
  };

  O$._getCaretPosition = function (field) {
    var result = 0;

    if (document.selection) {
      field.focus();
      var range = document.selection.createRange();
      range.moveStart("character", -field.value.length);
      result = range.text.length;
    } else if (field.selectionStart || field.selectionStart == "0") {
      result = field.selectionStart;
    }

    return result;
  };

  O$._setCaretPosition = function (field, caretPos) {
    if (document.selection) {
      field.focus();
      var range = document.selection.createRange();
      range.moveStart("character", -field.value.length);
      range.moveStart("character", caretPos);
      range.moveEnd("character", 0);
      range.select();
    } else if (field.selectionStart || field.selectionStart == "0") {
      field.selectionStart = caretPos;
      field.selectionEnd = caretPos;
      field.focus();
    }
  };

  O$._selectTextRange = function (field, beginIdx, endIdx) {
    if (field._o_inputField) field = field._o_inputField;
    if (field.setSelectionRange) {
      field.setSelectionRange(beginIdx, endIdx);
    } else {
      O$.assert(field.createTextRange, "Field should support either setSelectionRange, or createTextRange, but it supports neither.");

      var range = field.createTextRange();
      range.moveStart("character", beginIdx);
      range.moveEnd("character", endIdx);
      range.select();
    }
  };

  O$.markNextFormSubmissionAsDownloadAction = function () {
    O$._formSubmissionAsDownloadAction = true;
  };

  O$.isFormSubmission_A_DownloadAction = function () {
    return O$._formSubmissionAsDownloadAction;
  };

  O$.setSubmissionAjaxInactivityTimeout = function (timeout) {
    O$._submissionAjaxInactivityTimeout = timeout;
  };

  O$.getSubmissionAjaxInactivityTimeout = function () {
    return O$._submissionAjaxInactivityTimeout;
  };

  // ----------------- EVENT UTILITIES ---------------------------------------------------

  O$.getEventHandlerFunction = function (handlerName, handlerArgs, mainObj) { // todo: rework usages of this function with explicit closure creation
    var argString = handlerArgs ? ("[" + handlerArgs + "]") : "arguments";
    var handlerFunction;
    eval("handlerFunction = function() { return arguments.callee.prototype._ownObj." + handlerName + ".apply(arguments.callee.prototype._ownObj," + argString + "); }");
    handlerFunction.prototype._ownObj = mainObj;
    return handlerFunction;
  };

  O$.addEvent = function (componentClientId, eventName, functionScript) {
    var element = O$.byIdOrName(componentClientId);
    if (element) {
      O$.addEventHandler(element, eventName, functionScript);
    }
  };

  O$.addEventHandler = function (elt, evtName, evtScript, useCapture) {
    if (!elt._attachedEvents)
      elt._attachedEvents = [];

    var eventToAttach = {
      functionScript:evtScript
    };

    if (elt.addEventListener) {
      elt.addEventListener(evtName, evtScript, !!useCapture);
      eventToAttach.eventName = evtName;
    } else if (elt.attachEvent) {
      elt.attachEvent("on" + evtName, evtScript);
      eventToAttach.eventName = "on" + evtName;
    }
    elt._attachedEvents.push(eventToAttach);
  };

  O$.removeEventHandler = function (elt, evtName, evtScript, useCapture) {
    var eventToDetach = {
      eventName:"",
      functionScript:evtScript
    };

    if (elt.addEventListener) {
      elt.removeEventListener(evtName, evtScript, !!useCapture);
      eventToDetach.eventName = evtName;
    } else if (elt.attachEvent) {
      if (evtName.indexOf("on") != 0) {
        evtName = "on" + evtName;
      }
      elt.detachEvent(evtName, evtScript);
      eventToDetach.eventName = evtName;
    }
    if (elt._attachedEvents) {
      for (var index = 0; index < elt._attachedEvents.length; index++) {
        if (elt._attachedEvents[index] != null && elt._attachedEvents[index].eventName == eventToDetach.eventName
                && elt._attachedEvents[index].functionScript == eventToDetach.functionScript) {
          elt._attachedEvents.splice(index, 1);
          break;
        }
      }
    }
  };

  /* Use this function instead of direct field assignment to work around Mozilla problems firing old events when
   new page is loading (JSFC-2276) */
  O$.assignEventHandlerField = function (element, fieldName, handler) {
    element[fieldName] = handler;
    if (O$.isMozillaFF()) {
      O$.addUnloadEvent(function () {
        element[fieldName] = null;
      });
    }
  };

  O$.assignEvents = function (element, eventsContainer, useEventFields, additionalEventProperties) {
    if (!eventsContainer || eventsContainer.length == 0)
      return;
    var eventPrefix = "on";
    for (var propertyName in eventsContainer) {
      if (!O$.stringStartsWith(propertyName, eventPrefix))
        continue;
      var eventScript = eventsContainer[propertyName];
      if (!eventScript)
        continue;

      var handlerFunction;
      if (typeof eventScript == "string")
        eval("handlerFunction = function(event) {return eval(arguments.callee.prototype._handlerScript); }");
      else
        handlerFunction = eventScript;
      handlerFunction.prototype._handlerScript = eventScript;
      if (additionalEventProperties) {
        function wrapHandlerFunction(originalHandlerFunction) {
          return function (event) {
            for (var propertyName in additionalEventProperties) {
              var propertyValue = additionalEventProperties[propertyName];
              if (typeof propertyValue != "function")
                event[propertyName] = propertyValue;
            }
            originalHandlerFunction(event);
          };
        }

        handlerFunction = wrapHandlerFunction(handlerFunction);
      }
      var eventName = propertyName.substring(eventPrefix.length);
      if (useEventFields)
        element[propertyName] = handlerFunction;
      else
        O$.addEventHandler(element, eventName, handlerFunction);
    }
  };

  O$.repeatClickOnDblclick = function (e) {
    if (O$.isExplorer() && (this._stolenClickHandler || this.onclick)) {
      if (this._stolenClickHandler)
        this._stolenClickHandler(e);
      else
        this.onclick(e);
    }
    if (O$.isExplorer() && this.onmousedown) this.onmousedown(e);
    O$.stopEvent(e);
  };

  O$.initDocumentMouseClickListeners = function () {
    if (O$._initDocumentMouseClickListeners_called) {
      return;
    }

    if (!document._mouseClickListeners) {
      document._mouseClickListeners = [];
    }
    document._addClickListener = function (listener) {
      if (listener) {
        this._mouseClickListeners.push(listener);
      }
    };
    document._fireDocumentClicked = function (event) {
      for (var i = 0; i < document._mouseClickListeners.length; i++) {
        document._mouseClickListeners[i](event);
      }
    };
    O$.addEventHandler(document, "mousedown", document._fireDocumentClicked);

    O$._initDocumentMouseClickListeners_called = true;
  };

  O$.invokeOnce(O$.initDocumentMouseClickListeners, "O$.initDocumentMouseClickListeners");

  /*
   * eventName should be without the "on" prefix, for example: "change", "click", etc.
   */
  O$.createEvent = function (eventName) {
    var e;
    try {
      e = document.createEvent ? document.createEvent("Events") : document.createEventObject();
    } catch (exception) {
      e = [];
    }

    var eventInitialized = false;
    try {
      if (document.createEvent)
        e.initEvent(eventName, true, true);
    } catch (exception) {
      // e.g. takes place in Safari
    }
    if (!eventInitialized)
      e.name = "on" + eventName;

    return e;
  };

  /*
   * eventName should be without the "on" prefix, for example: "change", "click", etc.
   */
  O$.sendEvent = function (elt, eventName) {
    var safari = O$.isSafari();
    var e = safari ? {} :
            document.createEvent ? document.createEvent("Event") :
                    document.createEventObject();
    if (document.createEvent && !safari) {
      e.initEvent(eventName, true, true);
      e._of_event = true;
      e.returnValue = true;
      if (elt.dispatchEvent) {
        elt.dispatchEvent(e);
      } else {
        var eventField = elt["on" + eventName];
        if (!eventField)
          return true;
        return eventField.call(elt, e);
      }
      return e.returnValue;
    } else {
      e.name = "on" + eventName;
      e._of_event = true;
      var handler = elt[e.name];
      if (!handler)
        return true;
      return handler(e);
      //    object.fireEvent(e.name, e); // - didn't work for firing "onchange" for <table>
    }

  };


  O$.isAltPressed = function (event) {
    if (event == null || event.altKey == null)
      return false;
    return event.altKey;
  };
  //

  O$.isCtrlPressed = function (event) {
    if (event == null || event.ctrlKey == null)
      return false;
    return event.ctrlKey;
  };
  //

  O$.isShiftPressed = function (event) {
    if (event == null || event.shiftKey == null)
      return false;
    return event.shiftKey;
  };

  O$.getEvent = function (e) {
    return e ? e : event;
  };

  O$.cancelEvent = function (e) {
    O$.stopEvent(e);
    O$.preventDefaultEvent(e);
  };

  O$.preventDefaultEvent = function (e) {
    var evt = O$.getEvent(e);

    if (evt.preventDefault) {
      evt.preventDefault();
    }
    evt.returnValue = false;
  };

  O$.stopEvent = function (e) {
    var evt = O$.getEvent(e);

    if (evt.stopPropagation)
      evt.stopPropagation();
    evt.cancelBubble = true;
  };

  /*
   If the forElement parameter is specified, the event coordinates will be calculated relative to the nearest parent
   containing block of the element referred to by the forElement parameter, otherwise, the coordinates will be calculated
   relative to the document.
   */
  O$.getEventPoint = function (e, forElement) {
    var evt = O$.getEvent(e);
    var pageScrollPos = O$.getPageScrollPos();
    var pos = {x:evt.clientX + pageScrollPos.x, y:evt.clientY + pageScrollPos.y};

    var container = forElement ? O$.getContainingBlock(forElement, true) : null;
    if (container) {
      var containerPos = O$.getElementPos(container);
      pos.x -= containerPos.x;
      pos.y -= containerPos.y;
    }

    return pos;
  };

  O$.addInternalLoadEvent = function (func) {
    if (O$._documentLoaded) {
      func();
      return;
    }
    if (O$._internalLoadEventCount == undefined)
      O$._internalLoadEventCount = 0;
    if (!O$._internalLoadHandlers)
      O$._internalLoadHandlers = [];
    O$._internalLoadHandlers.push(func);

  };


  O$.addLoadEvent = function (func) {
    if (O$._documentLoaded) {
      func();
    } else {
      if (!O$._loadHandlers)
        O$._loadHandlers = [];
      O$._loadHandlers.push(func);
    }
  };

  O$.addEventHandler(window, "load", function loadDocumentHandler() {
    O$._documentLoaded = true;
    var i, count;
    if (O$._internalLoadHandlers) {
      for (i = 0, count = O$._internalLoadHandlers.length; i < count; i++) {
        var internalLoadHandler = O$._internalLoadHandlers[i];
        internalLoadHandler();
      }
      O$._internalLoadHandlers = [];
    }
    if (O$._loadHandlers) {
      for (i = 0, count = O$._loadHandlers.length; i < count; i++) {
        var loadHandler = O$._loadHandlers[i];
        loadHandler();
      }
      O$._loadHandlers = [];
    }
    O$.removeEventHandler(window, "load", loadDocumentHandler);
  });

  O$.addUnloadEvent = function (func) {
    var invokeOnUnloadHandlersFunction = function () {
      for (var i = 0, count = O$._onUnloadEvents.length; i < count; i++) {
        var onUnloadHandler = O$._onUnloadEvents[i];
        onUnloadHandler();
      }
    };

    if (!O$._onUnloadEvents) {
      O$._onUnloadEvents = [];

      var oldOnunload = window.onunload;
      if (typeof window.onunload != "function") {
        window.onunload = function () {
          invokeOnUnloadHandlersFunction();
        };
      } else {
        window.onunload = function () {
          oldOnunload();
          invokeOnUnloadHandlersFunction();
        };
      }
    }

    O$._onUnloadEvents.push(func);
  };


  O$.isLoadedFullPage = function () {
    return O$._documentLoaded;
  };

  O$.getFirstFocusableControl = function (parent, approvalFunction) {
    for (var i = 0, count = parent.childNodes.length; i < count; i++) {
      var child = parent.childNodes[i];
      if (!child._focusControlElement && O$.isControlFocusable(child) && (!approvalFunction || approvalFunction(child)))
        return child;
      var focusable = O$.getFirstFocusableControl(child, approvalFunction);
      if (focusable)
        return focusable;
    }
    return null;
  };

  O$.getLastFocusableControl = function (parent, approvalFunction) {
    for (var i = parent.childNodes.length - 1; i >= 0; i--) {
      var child = parent.childNodes[i];
      if (!child._focusControlElement && O$.isControlFocusable(child) && (!approvalFunction || approvalFunction(child)))
        return child;
      var focusable = O$.getLastFocusableControl(child, approvalFunction);
      if (focusable)
        return focusable;
    }
    return null;
  };


  O$.createHiddenFocusElement = function (componentId, tabIndex) {
    var focusElementId = componentId + ":::focus";
    var focusElement = O$(focusElementId);
    if (!focusElement) {
      var createTextArea = true;
      focusElement = document.createElement(createTextArea ? "textarea" : "input");
      if (!createTextArea)
        focusElement.type = "button";
      focusElement.className = (O$.isChrome() || O$.isSafari())
              ? "o_hiddenFocusChromeSafari"
              : "o_hiddenFocus";
      if (O$.isSafari()) {
        focusElement.style.border = "1px solid transparent !important";
      }
      if (componentId) {
        focusElement.id = focusElementId;
        // assigning name is required just to overcome "Invalid chunk ignored" warning during JSF 2 Ajax calls
        focusElement.name = focusElementId;
      }
    }
    if (tabIndex)
      focusElement.tabIndex = tabIndex;

    return focusElement;
  };

  O$.initDefaultScrollPosition = function (trackerFieldId, scrollPos) {
    O$.addLoadEvent(function () {
      O$.setHiddenField(null, trackerFieldId, scrollPos);
    });
    O$.initScrollPosition_(trackerFieldId, true, 1, null);
  };

  O$.initScrollPosition = function (scrollPosFieldId, autoSaveScrollPos, scrollableComponentId) {
    O$.initScrollPosition_(scrollPosFieldId, autoSaveScrollPos, 2, scrollableComponentId);
  };

  O$.initScrollPosition_ = function (scrollPosFieldId, autoSaveScrollPos, priority, scrollableComponentId) {
    if (scrollableComponentId) {
      var scrollableComponent = O$(scrollableComponentId);
      if (scrollableComponent && scrollableComponent._of_scrollPosTrackingParams)
        if (scrollableComponent._of_scrollPosTrackingParams.priority > priority)
          return;
    } else {
      if (window._of_scrollPosTrackingParams)
        if (window._of_scrollPosTrackingParams.priority > priority)
          return;
    }

    var targetComponent = (scrollableComponent) ? scrollableComponent : window;

    targetComponent._of_scrollPosTrackingParams = {fieldId:scrollPosFieldId, autoSave:autoSaveScrollPos,
      priority:priority, scrollableId:scrollableComponentId};

    O$.addLoadEvent(function () {
      if (!targetComponent._of_scrollPosTrackingParams)
        return;
      var scrollPosFieldId = targetComponent._of_scrollPosTrackingParams.fieldId;
      var autoSaveScrollPos = targetComponent._of_scrollPosTrackingParams.autoSave;
      targetComponent._of_scrollPosTrackingParams = null;
      var fld = O$(scrollPosFieldId);
      if (targetComponent == window) {
        document._of_scrollPositionField = fld;
      } else {
        targetComponent._of_scrollPositionField = fld;
      }

      var scrollPos = fld.value;

      O$.scrollToPosition(targetComponent, scrollPos);

      O$.addEventHandler(targetComponent, "scroll", function () {
        var scrollPos = (targetComponent == window)
                ? O$.getPageScrollPos() : O$.getScrollPos(scrollableComponent);

        if (targetComponent == window) {
          document._of_scrollPositionField.value = "[" + scrollPos.x + "," + scrollPos.y + "]";
        } else {
          targetComponent._of_scrollPositionField.value = "[" + scrollPos.x + "," + scrollPos.y + "]";
        }
      });
    });
  };

  O$.saveScrollPositionIfNeeded = function () {
    // needed for saving scroll position between ajax request under Mozilla Firefox
    var isMozilla = O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/;
    var isScrollPositionTrackingEnabled = (document._of_scrollPositionField);

    // remember scrollPosition before replacing oldElement by new one
    if (isMozilla && isScrollPositionTrackingEnabled) {
      var scrollPosFieldId = document._of_scrollPositionField.id;
      var fld = O$(scrollPosFieldId);
      document._of_scrollPositionBeforeAjaxRequest = fld.value;
    }
  };

  O$.restoreScrollPositionIfNeeded = function () {
    var isMozilla = O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/;
    var isScrollPositionTrackingEnabled = (document._of_scrollPositionField);

    // scroll to previous scrollPosition
    if (isMozilla && isScrollPositionTrackingEnabled) {
      var scrollPos = document._of_scrollPositionBeforeAjaxRequest;
      O$.scrollToPosition(window, scrollPos);
    }
  };

  O$.scrollToPosition = function (scrollableComponent, scrollPos) {
    var x, y, separatorIndex, currentScrollPos;
    if (scrollableComponent.scrollTo && scrollPos && scrollPos != "") {
      scrollPos = scrollPos.substring(1, scrollPos.length - 1);
      separatorIndex = scrollPos.indexOf(",");
      x = scrollPos.substring(0, separatorIndex);
      y = scrollPos.substring(separatorIndex + 1, scrollPos.length);
      currentScrollPos = (scrollableComponent == window)
              ? O$.getScrollPos(scrollableComponent)
              : O$.getPageScrollPos();

      if (x == currentScrollPos.x && y == currentScrollPos.y) {
        return;
      }
      scrollableComponent.scrollTo(x, y);
      if (O$.isExplorer()) {
        setTimeout(function () {
          scrollableComponent.scrollTo(x, y);
        }, 10);
      }
    } else if (scrollPos && scrollPos != "") {
      scrollPos = scrollPos.substring(1, scrollPos.length - 1);
      separatorIndex = scrollPos.indexOf(",");
      x = scrollPos.substring(0, separatorIndex);
      y = scrollPos.substring(separatorIndex + 1, scrollPos.length);
      currentScrollPos = (scrollableComponent == window)
              ? O$.getScrollPos(scrollableComponent)
              : O$.getPageScrollPos();

      if (x == currentScrollPos.x && y == currentScrollPos.y) {
        return;
      }

      scrollableComponent.scrollLeft = x;
      scrollableComponent.scrollTop = y;

      if (O$.isExplorer()) {
        setTimeout(function () {
          scrollableComponent.scrollLeft = x;
          scrollableComponent.scrollTop = y;
        }, 10);
      }
    }
  };

  O$.initDefaultFocus = function (trackerFieldId, focusedComponentId) {
    O$.addLoadEvent(function () {
      try {
        O$.setHiddenField(null, trackerFieldId, focusedComponentId);
      } catch (e) {
        // absence of a form on one page in case of application-wide focus tracking configuration is not critical
      }
    });
    O$.initFocus_(trackerFieldId, true, 1);
  };

  O$.initFocus = function (trackerFieldId, autoSaveFocus) {
    O$.initFocus_(trackerFieldId, autoSaveFocus, 2);
  };

  O$.initFocus_ = function (trackerFieldId, autoSaveFocus, priority) {
    function setupFocus() {
      if (O$._focusPriority && priority < O$._focusPriority)
        return;
      O$._focusPriority = priority;
      var trackerField = O$(trackerFieldId);
      O$._focusField = trackerField;
      var componentId = trackerField.value;
      var focused = false;
      if (componentId) {
        var c = O$(componentId);
        if (O$._activeElement && O$._activeElement == c) {
          focused = true;
        } else {
          if (c && !O$.isControlFocusable(c))
            c = O$.getFirstFocusableControl(c);
          if (c && c.focus) {
            try {
              c.focus();
              var rect = O$.getElementBorderRectangle(c);
              O$.scrollRectIntoView(rect);

              if (c.nodeName.toLowerCase() == "input" && c.type == "text")
                O$._setCaretPosition(c, c.value.length);
            } catch (ex) {
            }
            O$._activeElement = c;
            focused = true;
          }
        }
      }
      if (!focused && O$._activeElement && O$._activeElement.blur) {
        O$._activeElement.blur();
      }

      if (!autoSaveFocus)
        return;

      if (O$._autoSavingFocusInitialized)
        return;
      O$._autoSavingFocusInitialized = true;

      var bodyElement = document.getElementsByTagName("body")[0];
      if (bodyElement == null)
        return;
      O$.setupFocusOnTags(bodyElement, "input");
      O$.setupFocusOnTags(bodyElement, "a");
      O$.setupFocusOnTags(bodyElement, "button");
      O$.setupFocusOnTags(bodyElement, "textarea");
      O$.setupFocusOnTags(bodyElement, "select");
    }

    O$.addLoadEvent(setupFocus);
    O$.addLoadEvent(function () {
      if (O$.Ajax) {
        OpenFaces.Ajax.setCommonAjaxEventHandler("onajaxend", function (e) {
          O$._autoSavingFocusInitialized = false;
          setTimeout(setupFocus, 1);
        });
      }
    });
  };

  O$.setupFocusOnTags = function (parent, tagName) {
    var elements = parent.getElementsByTagName(tagName);
    for (var i = 0; i < elements.length; i++) {
      var element = elements[i];

      if (element._ofUtil_focusTrackerInstalled)
        continue;
      element._ofUtil_focusTrackerInstalled = true;
      element._of_prevOnFocusHandler = element.onfocus;
      element.onfocus = function (e) {
        O$._activeElement = this;
        O$._focusField.value = this.id;
        if (this._of_prevOnFocusHandler)
          this._of_prevOnFocusHandler(e);
        if (O$.onfocuschange)
          O$.onfocuschange(e);
      };
      O$.addUnloadHandler(element, function () {
        element.onfocus = null;
        element.onblur = null;
      });
      element._of_prevOnBlurHandler = element.onblur;
      element.onblur = function (e) {
        if (O$._activeElement == this) {
          O$._activeElement = null;
          O$._focusField.value = "";
        }
        if (this._of_prevOnBlurHandler)
          this._of_prevOnBlurHandler(e);
        if (O$.onfocuschange)
          O$.onfocuschange(e);
      };
    }

  };

  O$.addLoadEvent(function () {
    var predefinedTimeout = O$.getSubmissionAjaxInactivityTimeout();
    var timeoutToUnlockAjaxRequests = (O$.isFormSubmission_A_DownloadAction()) ? 100 : predefinedTimeout;

    if (O$.isExplorer6()) { // workaround for <button> tag submission bug in IE6, see OF-112
      var buttons = document.getElementsByTagName("button");
      for (var i = 0; i < buttons.length; i++) {
        var btn = buttons[i];

        function setClickHandler(btn) {
          O$.addEventHandler(btn, "click", function () {
            O$._clickedButton = btn;
            setTimeout(function () {
              btn._clickedButton = null;
            }, 500);
          });
        }

        setClickHandler(btn);
      }
    }

    function fixIE6ButtonsSubmission() {
      // workaround for <button> tag submission bug in IE6, see OF-112
      if (!O$.isExplorer6()) return;

      var buttons = document.getElementsByTagName("button");
      for (var i = 0; i < buttons.length; i++) {
        var btn = buttons[i];
        btn._o_prevDisabled = btn.disabled;
        if (btn != O$._clickedButton)
          btn.disabled = true;
      }
      setTimeout(function () {
        for (var i = 0; i < buttons.length; i++) {
          var btn = buttons[i];
          btn.disabled = btn._o_prevDisabled;
        }
      }, 500);
    }

    for (var bi = 0, count = document.forms.length; bi < count; bi++) {
      var frm = document.forms[bi];
      O$.addEventHandler(frm, "submit", function () {
        fixIE6ButtonsSubmission();
        if (!this.target || this.target == "_self") {
          O$.lockAjax();
          // _formSubmissionJustStarted should be reset so as not to block further ajax actions if this is not actually
          // a normal form submission, but file download (JSFC-2940)
          setTimeout(function () {
            O$.resetAjaxState();
          }, timeoutToUnlockAjaxRequests);
        }
      });
      if (frm._of_prevSubmit) continue;
      frm._of_prevSubmit = frm.submit;
      frm.submit = function () {
        fixIE6ButtonsSubmission();
        if (!this.target || this.target == "_self") {
          O$.lockAjax();
          // _formSubmissionJustStarted should be reset so as not to block further ajax actions if this is not actually
          // a normal form submission, but file download (JSFC-2940)
          setTimeout(function () {
            O$.resetAjaxState();
          }, timeoutToUnlockAjaxRequests);
        }
        this._of_prevSubmit();
      };
    }

  });

  O$.initMouseListenerUtils = function () {
    if (O$._mouseListenerUtilsInitialized)
      return;
    O$._mouseListenerUtilsInitialized = true;
    O$._elementUnderMouse = null;
    var prevMouseMove = document.onmousemove;
    document.onmousemove = function (e) {
      var result = undefined;
      if (prevMouseMove) {
        result = prevMouseMove.call(this, e);
      }

      var element;
      var evt = O$.getEvent(e);
      var elementList = [];
      if (O$._elementUnderMouse) {
        for (element = O$._elementUnderMouse; element; element = element.parentNode) {
          element._of_mouseInside = false;
          element._of_fireMouseOut = true;
          if (element._of_excludeParentFromMouseEventNotifications)
            break;
          elementList.push(element);
        }
      }
      O$._elementUnderMouse = evt.target ? evt.target : evt.srcElement;
      if (O$._elementUnderMouse) {
        for (element = O$._elementUnderMouse; element; element = element.parentNode) {
          element._of_mouseInside = true;
          element._of_fireMouseOver = true;
          if (element._of_excludeParentFromMouseEventNotifications)
            break;
          elementList.push(element);
        }
      }

      for (var i = 0, count = elementList.length; i < count; i++) {
        var el = elementList[i];
        if (!el._of_fireMouseOut && !el._of_fireMouseOver)
          continue;
        if (el._of_fireMouseOut && el._of_fireMouseOver) {
          el._of_fireMouseOut = undefined;
          el._of_fireMouseOver = undefined;
          continue;
        }
        var listener, listenerIndex, listenerCount;
        if (el._of_fireMouseOut) {
          if (el._of_mouseOutListeners)
            for (listenerIndex = 0, listenerCount = el._of_mouseOutListeners.length; listenerIndex < listenerCount; listenerIndex++) {
              listener = el._of_mouseOutListeners[listenerIndex];
              listener(e);
            }
          el._of_fireMouseOut = undefined;
        }
        if (el._of_fireMouseOver) {
          if (el._of_mouseOverListeners)
            for (listenerIndex = 0, listenerCount = el._of_mouseOverListeners.length; listenerIndex < listenerCount; listenerIndex++) {
              listener = el._of_mouseOverListeners[listenerIndex];
              listener(e);
            }
          el._of_fireMouseOver = undefined;
        }
      }

      return result;
    };
  };

  O$.waitForCondition = function (conditionFunction, func, conditionCheckInterval) {
    if (!conditionCheckInterval)
      conditionCheckInterval = 50;
    var intervalId = setInterval(function () {
      if (!conditionFunction())
        return;
      clearInterval(intervalId);
      func();
    }, conditionCheckInterval);
  };

  O$.invokeFunctionAfterDelay = function (func, delay, actionId) {
    // Invokes the specified function after the specified delay in milliseconds. If another invocation request comes for
    // the same function during the delay, then the invocation is postponed until that new invocation delay expires, etc.
    // thus making only one call in case of multiple frequent invocations.
    // actionId parameter can optionally be specified as a string that allows several "postponable" actions for the same
    // function, or several functions for the same action, that is actionId instead of function serves as a criteria for
    // canceling the previous action and scheduling the new one.
    var actionReference = func;
    if (typeof actionId == "string") {
      if (!OpenFaces._actionReferences)
        OpenFaces._actionReferences = {};
      actionReference = OpenFaces._actionReferences[actionId];
      if (!actionReference) {
        actionReference = {};
        OpenFaces._actionReferences[actionId] = actionReference;
      }
    }
    if (!actionReference._delayedInvocationCount)
      actionReference._delayedInvocationCount = 0;
    actionReference._delayedInvocationCount++;
    if (!actionReference._timeoutIds)
      actionReference._timeoutIds = [];
    actionReference._timeoutIds.push(setTimeout(function () {
      actionReference._delayedInvocationCount--;
      if (actionReference._delayedInvocationCount == 0) {
        actionReference._timeoutIds = [];
        func();
      }
    }, delay));
  };

  O$.cancelDelayedAction = function (func, actionId) {
    var actionReference = func;
    if (typeof actionId == "string") {
      if (!OpenFaces._actionReferences)
        OpenFaces._actionReferences = {};
      actionReference = OpenFaces._actionReferences[actionId];
      if (!actionReference) {
        actionReference = {};
        OpenFaces._actionReferences[actionId] = actionReference;
      }
    }
    if (!actionReference._timeoutIds)
      return;
    for (var i = 0, count = actionReference._timeoutIds.length; i < count; i++) {
      clearTimeout(actionReference._timeoutIds[i]);
      actionReference._timeoutIds = [];
      actionReference._delayedInvocationCount = 0;
    }

  };

  O$.setupHoverAndPressStateFunction = function (element, fn) {
    var state = {
      mouseInside:false,
      pressed:false,
      reset:function () {
        this.mouseInside = false;
        this.pressed = false;
        this._update();
      },
      _update:function () {
        fn(this.mouseInside, this.pressed);
      }
    };
    O$.setupHoverStateFunction(element, function (mouseInside) {
      state.mouseInside = mouseInside;
      if (!mouseInside)
        state.pressed = false;
      state._update();

    });
    O$.setupMousePressedStateFunction(element, function (pressed) {
      state.pressed = pressed;
      state._update();
    });
    return state;
  };

  O$.setupHoverStateFunction = function (element, fn) {
    if (!element._hoverListeners) {
      element._hoverListeners = [];
      element._of_hoverState = {
        forceHover:null,
        mouseInside:false,
        hoverValue:false
      };

      element._updateHover = function () {
        var newHoverValue = element._of_hoverState.forceHover != null
                ? element._of_hoverState.forceHover
                : element._of_hoverState.mouseInside;
        if (element._of_hoverState.hoverValue == newHoverValue) return;

        element._of_hoverState.hoverValue = newHoverValue;
        element._hoverListeners.forEach(function (fn) {
          fn.call(element, newHoverValue, element);
        });
      };
      element.setForceHover = function (forceHover) {
        element._of_hoverState.forceHover = forceHover;
        element._updateHover();
      };
      O$.setupHoverStateFunction_(element, function (mouseInside) {
        element._of_hoverState.mouseInside = mouseInside;
        element._updateHover();
      });
    }
    element._hoverListeners.push(fn);
  };

  O$.setupHoverStateFunction_ = function (element, fn) {
    O$.addMouseOverListener(element, function () {
      fn.call(element, true, element);
    });
    O$.addMouseOutListener(element, function () {
      fn.call(element, false, element);
    });
  };

  O$.setupFocusedStateFunction = function (element, fn) {
    O$.addEventHandler(element, "focus", function () {
      fn.call(element, true, element);
    });
    O$.addEventHandler(element, "blur", function () {
      fn.call(element, false, element);
    });
  };


  O$.setupMousePressedStateFunction = function (element, fn) {
    O$.addEventHandler(element, "mousedown", function () {
      fn.call(element, true, element);
    });
    O$.addEventHandler(element, "mouseup", function () {
      fn.call(element, false, element);
    });
    O$.initUnloadableComponent(element);
  };

  O$.addMouseOverListener = function (element, listener) {
    if (O$.isExplorer()) {
      element.onmouseenter = listener;
      return;
    }
    O$.initMouseListenerUtils();
    if (!element._of_mouseOverListeners)
      element._of_mouseOverListeners = [];
    element._of_mouseOverListeners.push(listener);
  };

  O$.addMouseOutListener = function (element, listener) {
    if (O$.isExplorer()) {
      element.onmouseleave = listener;
      return;
    }
    O$.initMouseListenerUtils();
    if (!element._of_mouseOutListeners)
      element._of_mouseOutListeners = [];
    element._of_mouseOutListeners.push(listener);
  };

  O$.isEventFromInsideOfElement = function (e, element) {
    var evt = O$.getEvent(e);
    var eventTarget = evt.target ? evt.target : evt.srcElement;
    for (var currElement = eventTarget; currElement; currElement = currElement.parentNode) {
      if (currElement == element)
        return true;
    }
    return false;
  };

  O$.disableNativeContextMenuFor = function (element) {
    if (element._isDisabledContextMenu) return;
    if (element) {
      element._oldoncontextmenu = element.oncontextmenu;
      element.oncontextmenu = function () {
        if (element._oldoncontextmenu) element._oldoncontextmenu();
        return false;
      };
      element._isDisabledContextMenu = true;
    }
  };

  O$.enableNativeContextMenuFor = function (element) {
    if (element) {
      if (element._oldoncontextmenu)
        element.oncontextmenu = element._oldoncontextmenu;
      else element.oncontextmenu = function () {
        return true;
      };
      element._isDisabledContextMenu = false;
    }
  };


  O$._isScrollableElement = function (element) {
    var elementOverflow = O$.getElementStyle(element, "overflow");
    if (!(elementOverflow == "scroll" || elementOverflow == "auto")) return false;
    var hasVerticalScrollBar = elementOverflow == "scroll" || element.scrollHeight > element.clientHeight;
    var hasHorizontalScrollBar = elementOverflow == "scroll" || element.scrollWidth > element.clientWidth;
    return hasHorizontalScrollBar || hasVerticalScrollBar;
  };

  O$.getTargetComponentHasOwnMouseBehavior = function (evt) {
    var element = evt.target ? evt.target : evt.srcElement;
    var tagName = element ? element.tagName : null;
    if (tagName)
      tagName = tagName.toLowerCase();
    var elementHasItsOwnMouseBehavior =
            tagName == "input" ||
                    tagName == "textarea" ||
                    tagName == "select" ||
                    tagName == "option" ||
                    tagName == "button" ||
                    tagName == "a" ||
                    O$._isScrollableElement(element);
    if (!elementHasItsOwnMouseBehavior) {
      elementHasItsOwnMouseBehavior = function (elem) {
        while (elem) {
          if (elem._hasItsOwnMouseBehavior) {
            return true;
          }
          elem = elem.parentNode;
        }
        return false;
      }(element);
    }
    return elementHasItsOwnMouseBehavior;
  };

  O$.makeDraggable = function (element, dropTargetLocator) {
    var startPos = null;
    O$.initUnloadableComponent(element);
    O$.addEventHandler(element, "mousedown", function (evt) {
      startPos = O$.getEventPoint(evt);
      O$.cancelEvent(evt);
      function mouseMove(e) {
        if (!startPos) return;
        var pt = O$.getEventPoint(e);
        O$.cancelEvent(evt);
        var dist = Math.sqrt(Math.pow(startPos.x - pt.x, 2) + Math.pow(startPos.y - pt.y, 2));
        if (dist > 7) {
          startPos = null;
          startDragging(evt);
        }
      }

      function mouseUp() {
        O$.removeEventHandler(document, "mousemove", mouseMove, true);
        O$.removeEventHandler(document, "mouseup", mouseUp, true);
      }

      O$.addEventHandler(document, "mousemove", mouseMove, true);
      O$.addEventHandler(document, "mouseup", mouseUp, true);

    });

    function startDragging(evt) {
      var elementCopy = O$.cloneElement(element);
      elementCopy.style.position = "absolute";
      var container = O$.getContainingBlock(element, true);
      if (!container)
        container = O$.getDefaultAbsolutePositionParent();
      var rect = O$.getElementBorderRectangle(element);
      var containerPos = O$.getElementPos(container);
      rect.x -= containerPos.x;
      rect.y -= containerPos.y;
      O$.setElementBorderRectangle(elementCopy, rect);

      container.appendChild(elementCopy);
      elementCopy.setPosition = function (x, y) {
        this.style.left = x + "px";
        this.style.top = y + "px";
      };
      var currentDropTarget = null;
      if (element.ondragstart)
        element.ondragstart(evt);
      O$.startDragging(evt, elementCopy);
      elementCopy._originalElement = element;
      elementCopy.updateCurrentDropTarget = function (evt) {
        var dropTarget = dropTargetLocator(evt);
        if (dropTarget != currentDropTarget) {
          if (currentDropTarget) currentDropTarget.setActive(false);
          currentDropTarget = dropTarget;
          if (currentDropTarget) currentDropTarget.setActive(true);
        }
      };
      elementCopy.ondragmove = function (evt) {
        if (element.ondragmove)
          element.ondragmove(evt);
        this.updateCurrentDropTarget(evt);
      };
      elementCopy.ondragend = function (evt) {
        if (element.ondragend)
          element.ondragend(evt);
        this.parentNode.removeChild(this);
        if (currentDropTarget) {
          currentDropTarget.setActive(false);
          currentDropTarget.acceptDraggable(element);
        }
      };
    }
  };

  O$.cloneElement = function (element) {
    if (element._clone)
      return element._clone();
    else
      return element.cloneNode(true);
  };

  O$.getContainmentRectangle = function (containment, containingBlock) {
    var containmentRect = !containment ? null
            : containment == "viewport" ? O$.getVisibleAreaRectangle()
            : containment == "document" ? O$.getDocumentRectangle()
            : containment == "containingBlock" ? O$.getElementBorderRectangle(containingBlock)
            : containment.substring(0, 1) == "#" ? function () {
      var id = containment.substring(1);
      var c = O$(id);
      if (!c) throw "Couldn't find containment by id: \"" + id + "\"";
      return O$.getElementBorderRectangle(c);
    }()
            : function () {
      throw "Unsupported containment string: " + containment
    }();
    return containmentRect;

  };

  O$.startDragging = function (e, draggable, simulateDblClickForElement) {
    var evt = O$.getEvent(e);
    if (simulateDblClickForElement && evt.type == "mousedown") {
      var thisTime = new Date().getTime();
      if (draggable._lastClickTime && thisTime - draggable._lastClickTime < 400) {
        // allow double-click despite being disabled with O$.cancelEvent by this function in specified browsers
        if (simulateDblClickForElement.ondblclick && (O$.isMozillaFF() || O$.isSafari() || O$.isChrome()))
          simulateDblClickForElement.ondblclick(evt);
      }
      draggable._lastClickTime = thisTime;
    }

    if (O$.getTargetComponentHasOwnMouseBehavior(evt))
      return; // don't drag native components to avoid unwanted effects (see JSFC-2347 and all related requests)

    draggable._draggingInProgress = true;
    draggable._draggingWasStarted = false;

    var pos = O$.getEventPoint(evt, draggable);
    draggable._lastDragX = pos.x;
    draggable._lastDragY = pos.y;
    if (!draggable._getPositionLeft)
      draggable._getPositionLeft = function () {
        return this.offsetLeft;
      };
    if (!draggable._getPositionTop)
      draggable._getPositionTop = function () {
        return this.offsetTop;
      };
    draggable._lastDragOffsetLeft = draggable._getPositionLeft();
    draggable._lastDragOffsetTop = draggable._getPositionTop();

    O$._draggedElement = draggable;
    O$.cancelEvent(evt);
    if (document._fireDocumentClicked)
      document._fireDocumentClicked(evt);

    function handleDragMove(e) {
      var evt = O$.getEvent(e);
      var draggable = O$._draggedElement;

      draggable._draggingInProgress = true;
      if (!draggable._draggingWasStarted) {
        draggable._draggingWasStarted = true;
        if (draggable.ondragstart)
          draggable.ondragstart(evt);
      }

      var pos = O$.getEventPoint(evt, draggable);
      var dragX = pos.x;
      var dragY = pos.y;
      var left = draggable._getPositionLeft();
      var top = draggable._getPositionTop();
      var xChangeSinceLastDrag = left - draggable._lastDragOffsetLeft;
      var yChangeSinceLastDrag = top - draggable._lastDragOffsetTop;
      var dx = dragX - draggable._lastDragX - xChangeSinceLastDrag;
      var dy = dragY - draggable._lastDragY - yChangeSinceLastDrag;
      var newLeft = left + dx;
      var newTop = top + dy;

      var containmentCorrectedLeft = newLeft;
      var containmentCorrectedTop = newTop;
      var containingBlock = draggable.offsetParent;
      if (!containingBlock) containingBlock = document.body;

      var containmentRect = draggable._containment && (!draggable._containment ||
              draggable._containmentRole == "restrictMovement" ||
              draggable._containmentRole == "restrictMovementAndSize")
              ? O$.getContainmentRectangle(draggable._containment, containingBlock)
              : null;

      if (containmentRect) {
        var prntPos = O$.getElementPos(containingBlock);
        var draggableSize = O$.getElementSize(draggable);

        var minLeft = containmentRect.x - prntPos.x;
        var minTop = containmentRect.y - prntPos.y;
        var maxLeft = minLeft + containmentRect.width - draggableSize.width - 1;
        var maxTop = minTop + containmentRect.height - draggableSize.height - 1;

        if (containmentCorrectedLeft > maxLeft) containmentCorrectedLeft = maxLeft;
        if (containmentCorrectedTop > maxTop) containmentCorrectedTop = maxTop;
        if (containmentCorrectedLeft < minLeft) containmentCorrectedLeft = minLeft;
        if (containmentCorrectedTop < minTop) containmentCorrectedTop = minTop;
      }

      if (draggable.setPosition) {
        draggable.setPosition(containmentCorrectedLeft, containmentCorrectedTop, dx, dy);
      } else {
        draggable.setLeft(containmentCorrectedLeft, dx);
        draggable.setTop(containmentCorrectedTop, dy);
      }
      if (draggable.ondragmove)
        draggable.ondragmove(evt, containmentCorrectedLeft, containmentCorrectedTop, dx, dy);
      var offsetLeftAfterDragging = draggable._getPositionLeft();
      var offsetTopAfterDragging = draggable._getPositionTop();
      dragX -= newLeft - offsetLeftAfterDragging;
      dragY -= newTop - offsetTopAfterDragging;

      draggable._lastDragX = dragX;
      draggable._lastDragY = dragY;
      draggable._lastDragOffsetLeft = offsetLeftAfterDragging;
      draggable._lastDragOffsetTop = offsetTopAfterDragging;
      if (draggable._dragEl && draggable._dragEl._onresizing) {
        draggable._dragEl._onresizing();
      }
      O$.cancelEvent(evt);
    }

    function handleDragEnd(e) {
      var evt = O$.getEvent(e);

      O$.removeEventHandler(document, "mousemove", handleDragMove, true);
      O$.removeEventHandler(document, "mouseup", handleDragEnd, true);

      var draggable = O$._draggedElement;
      O$._draggedElement = null;
      if (draggable._draggingWasStarted) {
        draggable._draggingWasStarted = undefined;
        if (draggable.ondragend)
          draggable.ondragend(evt);
      }

      if (!draggable._onmouseup) {
        O$.cancelEvent(evt);
      }
      draggable._draggingInProgress = false;
    }

    O$.addEventHandler(document, "mousemove", handleDragMove, true);
    O$.addEventHandler(document, "mouseup", handleDragEnd, true);
  };


  /**
   * Avoids IE issue where mouse events on an absolute element without backgroud and content "leak" to the underlying layer
   * instead of being processed by the element itself.
   */
  O$.fixIEEventsForTransparentLayer = function (layerElement) {
    if (O$.isExplorer()) {
      // specify background for an element to avoid passing events to the underlying layer
      layerElement.style.background = "white";
      O$.setOpacityLevel(layerElement, 0);
    }
  };

  O$._enableIFrameFix = function () {
    if (document._of_transparentLayer) {
      document._of_transparentLayer.style.display = "block";
    } else {
      var transparentLayer = document.createElement("div");
      if (O$._simulateFixedPosForBlockingLayer()) {
        transparentLayer.style.position = "absolute";
        transparentLayer.style.left = document.body.scrollLeft + "px";
        transparentLayer.style.top = document.body.scrollTop + "px";
      } else {
        transparentLayer.style.position = "fixed";
        transparentLayer.style.left = "0px";
        transparentLayer.style.top = "0px";
      }
      transparentLayer.style.width = "100%";
      transparentLayer.style.height = "100%";
      transparentLayer.style.display = "block";
      transparentLayer.style.zIndex = "999999";
      O$.fixIEEventsForTransparentLayer(transparentLayer);
      document._of_transparentLayer = transparentLayer;
      document.body.appendChild(transparentLayer);
      O$.addEventHandler(transparentLayer, "mouseup", function () {
        O$.sendEvent(document, "mouseup");
      }, false);

      O$.addEventHandler(transparentLayer, "mousemove", function () {
        O$.sendEvent(document, "mousemove");
      }, false);
    }
  };

  O$._disableIframeFix = function () {
    if (document._of_transparentLayer) {
      document._of_transparentLayer.style.display = "none";
    }
  };

  O$._simulateFixedPosForBlockingLayer = function () {
    return O$.isExplorer6(); // ie6 doesn't support fixed pos
  };


  O$.showFocusOutline = function (component, outlineStyleClass) {
    if (!outlineStyleClass)
      return;

    if (!component._focusOutline) {
      var outline = new O$.GraphicRectangle("2px solid blue", O$.GraphicRectangle.ALIGN_OUTSIDE);
      outline._control = component;
      outline._updatePosition = function () {
        var outlineToControlSpacingPx = 2;
        var rect = O$.getElementBorderRectangle(component);
        var x = rect.x;
        var y = rect.y;
        var width = rect.width;
        var height = rect.height;
        this.setRectangle(new O$.Rectangle(
                x - outlineToControlSpacingPx,
                y - outlineToControlSpacingPx,
                width + outlineToControlSpacingPx * 2,
                height + outlineToControlSpacingPx * 2
        ));
      };
      component._focusOutline = outline;
    }
    component._focusOutline._updatePosition();
    component._focusOutline.show();
  };

  O$.hideFocusOutline = function (component) {
    if (!component._focusOutline)
      return;
    component._focusOutline.hide();
  };

  O$._tableBlurCounter = 0;

  O$.isControlFocusable = function (control, componentToIgnore) {
    if (!control)
      return false;

    if (control._focusable)
      return true;
    var tagName = control.tagName;
    if (!tagName)
      return false;
    tagName = tagName.toLowerCase();
    var focusable =
            (tagName == "input" && control.type != "hidden") ||
                    tagName == "select" ||
                    tagName == "textarea" ||
                    tagName == "button" ||
                    tagName == "a" ||
                    (tagName == "span" && O$.checkClassNameUsed(control, "rich-inplace-select")) ||
                    (tagName == "div" && O$.checkClassNameUsed(control, "rich-inplace"));
    if (focusable && !control.disabled)
      return true;
    while (control) {
      if (control == componentToIgnore) return false;
      if (control._focusable) return true;
      control = control.parentNode;
    }
    return false;
  };

  O$.setupArtificialFocus = function (component, focusedClassName, tabIndex) {
    var prevOnfocusHandler = component.onfocus;
    var prevOnblurHandler = component.onblur;

    O$.extend(component, {
      _focused:false,
      _updateOutline:function () {
        if (this._outlineUpdateBlocked)
          return;
        O$.setStyleMappings(this, {
          focused:this._focused ? focusedClassName : null
        });

        if (this._focused)
          O$.showFocusOutline(this, null);
        else
          O$.hideFocusOutline(this);
      },
      _doUnblockOutlineUpdate:function () {
        this._outlineUpdateBlocked = false;
        if (this._focusedBeforeBlocking != null && this._focusedBeforeBlocking != this._focused) {
          this._focusedBeforeBlocking = null;
          if (this._focused) {
            if (prevOnfocusHandler)
              prevOnfocusHandler.call(this, null);
          } else {
            if (prevOnblurHandler)
              prevOnblurHandler.call(this, null);
          }
        }
        this._updateOutline();
      },
      onfocus:function (evt) {
        if (this._submitting)
          return;
        this._focused = true;
        if (prevOnfocusHandler && !this._outlineUpdateBlocked)
          prevOnfocusHandler.call(this, evt);

        component._updateOutline();
      },
      onblur:function (evt) {
        if (this._submitting)
          return;
        this._focused = false;
        if (prevOnblurHandler && !this._outlineUpdateBlocked)
          prevOnblurHandler.call(this, evt);
        if (!O$.isMozillaFF()) {
          setTimeout(function () {
            component._updateOutline();
          }, 1);
        } else {
          component._updateOutline();
        }
      }

    });

    var focusControl = O$.createHiddenFocusElement(component.id, tabIndex);

    function fireEvent(object, eventName, param) {
      var handler = object[eventName];
      if (!handler)
        return undefined;
      if (O$.isExplorer())
        return object[eventName]();
      else
        return object[eventName](param);
    }

    focusControl._destComponent = component;
    focusControl.onfocus = function (evt) {
      this._prevStatusText = window.status;
      window.status = "";
      return fireEvent(this._destComponent, "onfocus", evt);
    };
    focusControl.onblur = function (evt) {
      window.status = this._prevStatusText;
      return fireEvent(this._destComponent, "onblur", evt);
    };
    focusControl.onkeydown = function (evt) {
      return fireEvent(this._destComponent, "onkeydown", evt);
    };
    focusControl.onkeyup = function (evt) {
      return fireEvent(this._destComponent, "onkeyup", evt);
    };
    focusControl.onkeypress = function (evt) {
      return fireEvent(this._destComponent, "onkeypress", evt);
    };

    component._focusControl = focusControl;

    component._focusable = true;
    component.focus = function () {
      if (O$.getElementStyle(component, "position") == "absolute" || this._preventPageScrolling) {
        var container = O$.getContainingBlock(this._focusControl, true);
        var containerRect = container ? O$.getElementBorderRectangle(container) : {x:0, y:0};
        var pageScrollPos = O$.getPageScrollPos();
        var pageScrollRect = new O$.Rectangle(pageScrollPos.x, pageScrollPos.y, O$.getVisibleAreaSize().width, O$.getVisibleAreaSize().height);

        if (!pageScrollRect.intersects(containerRect)) {
          // if rectangle not intersect - we put focus to the mid of container
          this._focusControl.style.top = (containerRect.getMaxY() - containerRect.getMinY()) / 2 + "px";
          this._focusControl.style.left = (containerRect.getMaxX() - containerRect.getMinX()) / 2 + "px";
        } else {
          // if rectangles intersects - we put focus to the mid of intersection
          pageScrollRect.intersectWith(containerRect);
          this._focusControl.style.left = -containerRect.getMinX() + (pageScrollRect.getMaxX() + pageScrollRect.getMinX()) / 2 + "px";
          this._focusControl.style.top = -containerRect.getMinY() + (pageScrollRect.getMaxY() + pageScrollRect.getMinY()) / 2 + "px";
        }
      } else {
        this._focusControl.style.left = "";
        this._focusControl.style.top = "";
      }
      try {
        this._focusControl.focus();
      } catch (e) {
        // in IE hidden element can't receive focus
      }
    };

    component.blur = function () {
      this._focusControl.blur();
    };

    O$.addEventHandler(component, "click", function (evt) {
      var selectedText = window.getSelection
              ? window.getSelection() :
              (document.selection && document.selection.createRange) ? document.selection.createRange().text : "";

      if (selectedText != "")
        return; // don't switch focus to make text selection possible under FF (JSFC-1134) and IE

      var e = evt ? evt : event;
      if (component._focused)
        return;

      var target = (e != null)
              ? (e.target ? e.target : e.srcElement)
              : null;
      if (target.id && target.id == component.id)
        return;
      if (O$.isControlFocusable(target, component))
        return;
      component._preventPageScrolling = true;
      component.focus();
      component._preventPageScrolling = false;
    });

    function blockOutlineUpdate() {
      component._outlineUpdateBlocked = true;
      component._focusedBeforeBlocking = this._focused;
    }

    function unblockOutlineUpdate() {
      if (!O$._tableBlurCounter)
        O$._tableBlurCounter = 0;
      if (!O$.isMozillaFF()) {
        setTimeout(function () {
          if (!component._doUnblockOutlineUpdate) {
            // can be the case in case of O$.destroyAllFunctions functioning during Ajax when
            // org.openfaces.ajaxCleanupRequired init parameter is set to true
            return;
          }
          component._doUnblockOutlineUpdate();
        }, 1);
      } else {
        component._doUnblockOutlineUpdate();
      }
    }

    O$.addEventHandler(component, "mousedown", blockOutlineUpdate);
    O$.addEventHandler(component, "mouseup", unblockOutlineUpdate);
    O$.addEventHandler(component, "mouseout", unblockOutlineUpdate);

    component.parentNode.insertBefore(focusControl, component);

    var oldUnloadHandler = component.onComponentUnload;
    if (!oldUnloadHandler) {
      O$.addThisComponentToAllParents(component);
    }
    component.onComponentUnload = function () {
      if (oldUnloadHandler)
        oldUnloadHandler();
      else
        O$.unloadAllHandlersAndEvents(component);

      if (component._focusOutline)
        component._focusOutline.remove();
    };
  };

  // ----------------- STYLE UTILITIES ---------------------------------------------------

  O$.addCssRules = function (ruleArray) {
    for (var i = 0, count = ruleArray.length; i < count; i++) {
      var rule = ruleArray[i];
      O$.addCssRule(rule);
    }
  };

  O$.addUnloadableCssRules = function (componentId, ruleArray) {
    var element = O$(componentId);
    if (element != null) {
      function getNameOfCssClass(fullRule) {
        return fullRule.substring(0, fullRule.indexOf("{"));
      }

      O$.initUnloadableComponent(element);
      for (var i = 0, count = ruleArray.length; i < count; i++) {
        O$.addCssRule(ruleArray[i]);
      }
      O$.addUnloadHandler(element, function () {
        for (var i = 0, count = ruleArray.length; i < count; i++) {
          O$.removeCssRule(getNameOfCssClass(ruleArray[i]));
        }
      });
    } else {
      O$.addCssRules(ruleArray);
    }
  };

  O$.getLocalStyleSheet = function () {
    if (document._of_localStyleSheet)
      return document._of_localStyleSheet;

    if (document.createStyleSheet) {
      document._of_localStyleSheet = document.createStyleSheet();
    } else {
      var styleElement = document.createElement("style");
      var headTags = document.getElementsByTagName("head");
      var styleParent = headTags.length > 0 ? headTags[0] : document.getElementsByTagName("body")[0];
      styleParent.appendChild(styleElement);
      if (styleElement.styleSheet)
        document._of_localStyleSheet = styleElement.styleSheet;
      else
        document._of_localStyleSheet = styleElement.sheet;
    }
    return document._of_localStyleSheet;
  };

  O$.addCssRule = function (strRule) {
    var styleSheet = O$.getLocalStyleSheet();
    if (!styleSheet)
      return;

    try {
      if (styleSheet.addRule) { // IE only
        var idx1 = strRule.indexOf("{");
        var idx2 = strRule.indexOf("}");
        O$.assert(idx1 != -1 && idx2 != -1 && idx2 > idx1, "O$.addCssRule: Couldn't parse CSS rule \"{...}\"  boundaries: " + strRule);
        var selector = strRule.substring(0, idx1);
        var declaration = strRule.substring(idx1 + 1, idx2);

        styleSheet.addRule(selector, declaration);
      } else { // all others
        styleSheet.insertRule(strRule, styleSheet.cssRules.length);
      }
      return styleSheet;
    } catch (e) {
      O$.logError("O$.addCssRule threw an exception " + (e ? e.message : e) +
              "; tried to add the following rule: " + strRule);
      throw e;
    }

  };

  O$.removeCssRule = function (nameOfCssClass, _iePredefClasses) {

    if (_iePredefClasses) {
      _iePredefClasses._obtained--;
      return;
    }

    var styleSheet = O$.getLocalStyleSheet();
    if (!styleSheet)
      return;

    try {
      if (styleSheet.removeRule) { // IE only
        var rules = styleSheet.rules;
        for (var i = 0; i < rules.length; i++) {
          if (rules[i].selectorText == nameOfCssClass) {
            var ruleBySelector = O$._cssRulesBySelectors ? O$._cssRulesBySelectors[nameOfCssClass] : null;
            if (ruleBySelector) delete ruleBySelector;
            styleSheet.removeRule(i);
            break;
          }
        }
      } else { // all others
        var rules = styleSheet.cssRules;
        for (var i = 0; i < rules.length; i++) {
          if (rules[i].selectorText == nameOfCssClass) {
            var ruleBySelector = O$._cssRulesBySelectors ? O$._cssRulesBySelectors[nameOfCssClass] : null;
            if (ruleBySelector) delete ruleBySelector;
            styleSheet.deleteRule(i);
            break;
          }
        }
      }
      return styleSheet;
    } catch (e) {
      O$.logError("O$.removeCssRule threw an exception " + (e ? e.message : e) +
              "; tried to remove rule:" + nameOfCssClass);
      throw e;
    }

  };

  O$._dynamicRulesCreated = 0;
  O$.createCssClass = function (declaration, disableCaching) {
    if (!disableCaching) {
      if (!document._cachedDynamicCssRules)
        document._cachedDynamicCssRules = {};
      var cachedClassName = document._cachedDynamicCssRules[declaration];
      if (cachedClassName)
        return cachedClassName;
    }
    var className = "of_dynamicRule" + O$._dynamicRulesCreated++;
    var selector = "." + className;
    var ruleText = selector + " {" + declaration + "}";
    var styleSheet = O$.addCssRule(ruleText);
    if (!styleSheet)
      return null;
    var rules = styleSheet.cssRules ? styleSheet.cssRules : styleSheet.rules;
    var sel_lc = selector.toLowerCase();
    var rule = null;
    for (var i = rules.length - 1; i >= 0; i--) {
      var r = rules[i];
      var sel = r.selectorText;
      if (sel && sel.toLowerCase() == sel_lc) {
        rule = r;
        break;
      }
    }
    if (rule) {
      if (!O$._cssRulesBySelectors) O$._cssRulesBySelectors = {};
      O$._cssRulesBySelectors[selector] = [rule];
    }

    if (!disableCaching) {
      document._cachedDynamicCssRules[declaration] = className;
    }
    return className;
  };

  O$.findStyleSheet = function (hrefFragment) {
    var styleSheets = document.styleSheets;
    if (!styleSheets)
      return undefined;
    for (var sheetIndex = 0; sheetIndex < styleSheets.length; sheetIndex++) {
      if (styleSheets[sheetIndex].href && styleSheets[sheetIndex].href.indexOf(hrefFragment) != -1)
        return styleSheets[sheetIndex];
    }
    return null;
  };

  O$.findCssRule = function (selector) {
    var rules = O$.findCssRules([selector]);
    if (rules === undefined)
      return undefined;
    if (rules.length == 0)
      return null;
    return rules[0];
  };

  /**
   * Searches for CSS rules with the specified selectors, and returns an array of CSSStyleRule (or CSSRule) objects
   * in order of their occurrence in DOM
   */
  O$.findCssRules = function (selectors) {
    if (!O$._cssRulesBySelectors) O$._cssRulesBySelectors = {};
    var selectorsKey = selectors.join(" ");
    var cachedRules = O$._cssRulesBySelectors[selectorsKey];
    if (cachedRules) return cachedRules;

    var styleSheets = document.styleSheets;
    if (!styleSheets)
      return undefined;

    var selectorCount = selectors.length;
    if (selectorCount == 0)
      return [];

    for (var i = 0; i < selectorCount; i++)
      selectors[i] = selectors[i].toLowerCase();

    var rulesFound = [];
    for (var sheetIndex = 0; sheetIndex < styleSheets.length; sheetIndex++) {
      var ss = styleSheets[sheetIndex];
      var rules;
      try {
        rules = ss.cssRules ? ss.cssRules : ss.rules;
        if (!rules) continue;
      } catch (e) {
        // account for the NS_ERROR_DOM_SECURITY_ERR error that might occur if there's a StyleSheet from another domain,
        // e.g. when Skype plug-in is installed
        continue;
      }
      for (var ruleIndex = 0, ruleCount = rules.length; ruleIndex < ruleCount; ruleIndex++) {
        var rule = rules[ruleIndex];
        var selectorName = rule.selectorText;
        if (!selectorName)
          continue;
        selectorName = selectorName.toLowerCase();
        if (selectors.indexOf(selectorName) != -1)
          rulesFound.push(rule);
      }
    }
    O$._cssRulesBySelectors[selectorsKey] = rulesFound;
    return rulesFound;
  };

  O$.getStyleClassProperty = function (styleClass, propertyName) {
    var propertyValues = O$.getStyleClassProperties(styleClass, [propertyName]);
    return propertyValues[propertyName];
  };

  O$.getStyleClassProperties = function (styleClass, propertyNames) {
    if (!styleClass || propertyNames.length == 0)
      return {};
    var classNames = styleClass.split(" ");
    var classSelectors = [];
    var i, count;
    for (i = 0, count = classNames.length; i < count; i++) {
      var className = classNames[i];
      if (className)
        classSelectors.push("." + className);
    }
    var cssRules = O$.findCssRules(classSelectors);
    if (!cssRules)
      return {};

    var propertyCount = propertyNames.length;
    var propertyValues = {};
    var propertyImportantFlags = [];
    for (i = 0, count = cssRules.length; i < count; i++) {
      var cssRule = cssRules[i];
      var ruleStyle = cssRule.style;
      for (var propertyIndex = 0; propertyIndex < propertyCount; propertyIndex++) {
        var propertyName = propertyNames[propertyIndex];
        var capitalizedPropertyName = O$._capitalizeCssPropertyName(propertyName);
        var dashizedPropertyName = O$._dashizeCssPropertyName(capitalizedPropertyName);
        var thisPropertyValue = ruleStyle[capitalizedPropertyName];
        if (!thisPropertyValue)
          continue;
        var thisPropertyImportant = ruleStyle.getPropertyPriority && (ruleStyle.getPropertyPriority(capitalizedPropertyName) == "important");
        if (!propertyImportantFlags[propertyIndex]) {
          propertyValues[dashizedPropertyName] = thisPropertyValue;
          propertyValues[capitalizedPropertyName] = thisPropertyValue;
          if (thisPropertyImportant)
            propertyImportantFlags[propertyIndex] = true;
        } else {
          if (thisPropertyImportant) {
            propertyValues[dashizedPropertyName] = thisPropertyValue;
            propertyValues[capitalizedPropertyName] = thisPropertyValue;
          }
        }
      }
    }

    return propertyValues;
  };


  O$.combineClassNames = function (classNames) {
    var nonNullClassNames = [];
    for (var i = 0, count = classNames.length; i < count; i++) {
      var className = classNames[i];
      if (className)
        nonNullClassNames.push(className);
    }
    return nonNullClassNames.join(" ");
  };

  O$.checkClassNameUsed = function (element, className) {
    var classNames = element.className.split(" ");
    for (var i = 0, count = classNames.length; i < count; i++) {
      var usedClassName = classNames[i];
      if (usedClassName && usedClassName == className)
        return true;
    }

    return false;
  };

  /*
   * @deprecated use O$.setStyleMappings instead
   */
  O$.appendClassNames = function (element, classesToAppend) {
    var oldClassName = element.className;
    var newClassName = O$.combineClassNames(classesToAppend);
    if (oldClassName)
      newClassName = newClassName ? oldClassName + " " + newClassName : oldClassName;
    if (newClassName != oldClassName)
      element.className = newClassName;
    return oldClassName;
  };

  /*
   * @deprecated use O$.setStyleMappings instead
   */
  O$.excludeClassNames = function (element, classesToExclude) {
    var newClassesToExclude = [];
    for (var i = 0, count = classesToExclude.length; i < count; i++) {
      var clsToExclude = classesToExclude[i];
      if (!clsToExclude)
        continue;
      var subClassesToExclude = clsToExclude.split(" ");
      for (var j = 0, jCount = subClassesToExclude.length; j < jCount; j++) {
        var subClassToExclude = subClassesToExclude[j];
        newClassesToExclude.push(subClassToExclude);
      }
    }

    var someClassesExcluded = false;
    var clsName = element.className;
    var clsNames = clsName ? clsName.split(" ") : [];
    var newNames = [];
    for (var nameIndex = 0, nameCount = clsNames.length; nameIndex < nameCount; nameIndex++) {
      var currName = clsNames[nameIndex];
      if (currName) {
        if (newClassesToExclude.indexOf(currName) == -1)
          newNames.push(currName);
        else
          someClassesExcluded = true;
      }
    }
    var newClsName = newNames.join(" ");
    if (element.className != newClsName)
      element.className = newClsName;
    return someClassesExcluded;
  };

  O$.getElementOwnStyle = function (element) {
    var styleMappings = element._styleMappings;
    if (!styleMappings)
      return element.className;
    else
      return styleMappings.__initialStyle;
  };

  O$.setElementOwnStyle = function (element, value) {
    var styleMappings = element._styleMappings;
    if (!styleMappings)
      return element.className = value;
    else
      return styleMappings.__initialStyle = value;
  };

  O$.setStyleMappings = function (element, styleMappings) {
    if (!element._styleMappings)
      element._styleMappings = {__initialStyle:element.className};
    var elementStyleMappings = element._styleMappings;
    var styleName, styleValue;
    for (styleName in styleMappings) {
      if (!styleName)
        continue;
      styleValue = styleMappings[styleName];
      if (styleValue && typeof styleValue != "string")
        continue;
      elementStyleMappings[styleName] = styleValue;
    }
    var compoundClassName = "";
    for (styleName in elementStyleMappings) {
      if (!styleName)
        continue;
      styleValue = elementStyleMappings[styleName];
      if (styleValue && typeof styleValue != "string")
        continue;
      if (styleValue) {
        if (compoundClassName.length > 0)
          compoundClassName += " ";
        compoundClassName += styleValue;
      }
    }
    if (element.className != compoundClassName) {
      element.className = compoundClassName;
      if (element._classNameChangeHandler)
        element._classNameChangeHandler();
    }
  };

  O$.getElementStyle = function (element, propertyNames, enableValueCaching) {
    if (typeof propertyNames == "string") {
      if (!enableValueCaching) {
        var currentStyle, computedStyle;
        var result = (currentStyle = element.currentStyle)
                ? currentStyle[O$._capitalizeCssPropertyName(propertyNames)]
                : (computedStyle = !currentStyle && document.defaultView && document.defaultView.getComputedStyle(element, ""))
                ? computedStyle.getPropertyValue(O$._dashizeCssPropertyName(propertyNames)) : "";
        return result ? result : "";
      }
      return O$.getElementStyle(element, [propertyNames], enableValueCaching)[propertyNames];
    }
    if (enableValueCaching) {
      if (!element._cachedStyleValues)
        element._cachedStyleValues = {};
    }
    var propertyValues = {};
    currentStyle = element.currentStyle;
    computedStyle = !currentStyle && document.defaultView && document.defaultView.getComputedStyle(element, "");
    for (var i = 0, count = propertyNames.length; i < count; i++) {
      var propertyName = propertyNames[i];
      var capitalizedPropertyName = O$._capitalizeCssPropertyName(propertyName);
      var dashizedPropertyName = O$._dashizeCssPropertyName(capitalizedPropertyName);

      var propertyValue = undefined;
      if (enableValueCaching) {
        propertyValue = element._cachedStyleValues[dashizedPropertyName];
        if (propertyValue != undefined) {
          propertyValues[dashizedPropertyName] = propertyValue;
          propertyValues[capitalizedPropertyName] = propertyValue;
          continue;
        }
      }

      if (currentStyle) {
        propertyValue = currentStyle[capitalizedPropertyName];
      } else if (computedStyle) {
        propertyValue = computedStyle.getPropertyValue(dashizedPropertyName);
      }
      if (!propertyValue)
        propertyValue = "";
      if (enableValueCaching) {
        element._cachedStyleValues[dashizedPropertyName] = propertyValue;
      }
      propertyValues[dashizedPropertyName] = propertyValue;
      propertyValues[capitalizedPropertyName] = propertyValue;
    }

    return propertyValues;
  };

  O$._capitalizeCssPropertyName = function (propertyName) {
    if (!O$._capitalizedValues) O$._capitalizedValues = {};
    var value = O$._capitalizedValues[propertyName];
    if (value) return value;
    var _propertyName = propertyName;
    while (true) {
      var idx = _propertyName.indexOf("-");
      if (idx == -1) {
        O$._capitalizedValues[propertyName] = _propertyName;
        return _propertyName;
      }
      var firstPart = _propertyName.substring(0, idx);
      var secondPart = _propertyName.substring(idx + 1);
      if (secondPart.length > 0) {
        var firstChar = secondPart.substring(0, 1);
        firstChar = firstChar.toUpperCase();
        var otherChars = secondPart.substring(1);
        secondPart = firstChar + otherChars;
      }
      _propertyName = firstPart + secondPart;
    }
  };

  O$._dashizeCssPropertyName = function (propertyName) {
    if (!O$._dashizedValues) O$._dashizedValues = {};
    var value = O$._dashizedValues[propertyName];
    if (value) return value;
    var result = "";
    for (var i = 0, count = propertyName.length; i < count; i++) {
      var ch = propertyName.substring(i, i + 1);
      if (ch != "-" && O$.isUpperCase(ch))
        result += "-" + ch.toLowerCase();
      else
        result += ch;
    }
    O$._dashizedValues[propertyName] = result;
    return result;
  };


  O$.repaintAreaForOpera = function (element, deferredRepainting) {
    if (!O$.isOpera())
      return;
    if (!element)
      return;
    if (deferredRepainting) {
      setTimeout(function () {
        O$.repaintAreaForOpera(element, false);
      }, 1);
      return;
    }
    // using backgroundColor instead of just background is important here for cases when image is set as background
    // NOTE: setting the calculated border is an erroneous pattern because any further background changes through
    // element's className will be overridden by this in-place style declaration. However it must be used for document.body
    // because this element behaves differently when assigned old background - assigning a non-specified old background
    // makes the background white disregarding the appropriate stylesheets (JSFC-2346, JSFC-2275)
    var oldBackgroundColor = element != document.body
            ? element.style.backgroundColor
            : O$.getElementStyle(element, "background-color");
    element.style.backgroundColor = "white";
    element.style.backgroundColor = "#fefefe";
    element.style.backgroundColor = oldBackgroundColor;
  };

  // JSFC-3270
  O$.repaintWindowForSafari = function (deferredRepainting) {
    if (!O$.isSafari())
      return;
    if (deferredRepainting) {
      setTimeout(function () {               // for fast machine
        O$.repaintWindowForSafari(false);
      }, 50);
      setTimeout(function () {               // for slow machine
        O$.repaintWindowForSafari(false);
      }, 300);
      return;
    }
    var tempDiv = document.createElement("div");
    tempDiv.innerHTML = "<div> <style type='text/css'> .d_u_m_p_c_l_a_s_s_ { background: black; filter: alpha( opacity = 50 ); opacity: .50; } </style> </div>";
  };

  O$.preloadImage = function (imageUrl) {
    if (!imageUrl)
      return;
    var image = new Image();
    image.src = imageUrl;
  };

  O$.preloadImages = function (imageUrls) {
    for (var i = 0, count = imageUrls.length; i < count; i++) {
      var imageUrl = imageUrls[i];
      O$.preloadImage(imageUrl);
    }
  };

  O$.blendColors = function (colorString1, colorString2, color2Proportion) {
    if (color2Proportion < 0)
      color2Proportion = 0;
    else if (color2Proportion > 1)
      color2Proportion = 1;
    return new O$.Color(colorString1).blendWith(new O$.Color(colorString2), color2Proportion).toHtmlColorString();
  };

  O$.Color = function (htmlColorString) {
    if (!O$.stringStartsWith(htmlColorString, "#"))
      throw "Color string should start with '#', but was: " + htmlColorString;
    this.r = parseInt(htmlColorString.substring(1, 3), 16);
    this.g = parseInt(htmlColorString.substring(3, 5), 16);
    this.b = parseInt(htmlColorString.substring(5, 7), 16);

    this.toHtmlColorString = function () {
      function intTo2DigitHex(intValue) {
        var str = intValue.toString(16);
        if (str.length < 2)
          str = "0" + str;
        return str;
      }

      return "#" + intTo2DigitHex(this.r) + intTo2DigitHex(this.g) + intTo2DigitHex(this.b);
    };

    this.blendWith = function (anotherColor, anotherColorPortion) {
      function blendValues(val1, val2, val2Portion) {
        return Math.round(val1 + (val2 - val1) * val2Portion);
      }

      this.r = blendValues(this.r, anotherColor.r, anotherColorPortion);
      this.g = blendValues(this.g, anotherColor.g, anotherColorPortion);
      this.b = blendValues(this.b, anotherColor.b, anotherColorPortion);
      return this;
    };

  };

  O$.getOpacityLevel = function (element) {
    return element._of_opacity !== undefined ? element._of_opacity : 1;
  };

  O$.setOpacityLevel = function (element, opacity) {
    if (opacity < 0)
      opacity = 0;
    else if (opacity > 1)
      opacity = 1;
    if (opacity != 1) {
      element.style.opacity = opacity;
      element.style.MozOpacity = opacity;
      element.style.filter = "alpha(opacity=" + (opacity * 100) + ");";
      element.style.KhtmlOpacity = opacity;
    } else {
      element.style.opacity = "";
      element.style.MozOpacity = "";
      element.style.filter = "";
      element.style.KhtmlOpacity = "";
    }
    element._of_opacity = opacity;
  };

  /*
   Ensures that z-index of the specified element is greater than that of the specified reference element.
   */
  O$.correctElementZIndex = function (element, referenceElement, zIndexIncrement) {
    if (!referenceElement)
      return;
    if (zIndexIncrement === undefined)
      zIndexIncrement = 1;
    if (element.parentNode && (element.offsetParent == O$.getDefaultAbsolutePositionParent())) {
      var ref = referenceElement;
      var elt = ref;
      while (elt && elt.offsetParent && elt.offsetParent != element.offsetParent) {
        elt = elt.offsetParent;
        if (O$.getElementStyle(elt, "position") != "static")
          ref = elt;
      }
      referenceElement = ref;
    }
    var zIndex = O$.getElementZIndex(element);
    var refZIndex = referenceElement._maxZIndex ? referenceElement._maxZIndex : O$.getElementZIndex(referenceElement);
    if (zIndex <= refZIndex)
      element.style.zIndex = refZIndex + zIndexIncrement;
  };

  /*
   Calculates z-index for the specified element, or if it is not a positioned element itself,
   returns z-index of the nearest parent containing block if it exists, otherwise returns the default z-index of 0.
   */
  O$.getElementZIndex = function (element) {
    var container = O$.getContainingBlock(element);
    if (!container)
      return 0;
    var zIndex = O$.getNumericElementStyle(container, "z-index");
    return zIndex;
  };

  O$.getContainingBlock = function (element, ignoreThisElement) {
    for (var el = !ignoreThisElement ? element : element.parentNode; el; el = el.parentNode) {
      if (O$.isContainingBlock(el))
        return el;
    }
    return null;
  };

  // ----------------- DATE/TIME UTILITIES ---------------------------------------------------

  O$.initDateTimeFormatObject = function (months, shortMonths, days, shortDays, localeStr) {
    if (!this._dateTimeFormatMap) this._dateTimeFormatMap = [];
    if (!this._dateTimeFormatLocales) this._dateTimeFormatLocales = [];

    if (!this._dateTimeFormatMap[localeStr]) {
      this._dateTimeFormatMap[localeStr] = new O$.DateTimeFormat(months, shortMonths, days, shortDays);
      this._dateTimeFormatLocales.push(localeStr);
    }

  };

  O$.getDateTimeFormatObject = function (locale) {
    if (this._dateTimeFormatMap)
      return this._dateTimeFormatMap[locale];
    return null;
  };

  O$.parseDateTime = function (str) {
    var fields = str.split(" ");
    var dateStr = fields[0];
    var timeStr = fields[1];

    var dtf = O$.getDateTimeFormatObject(this._dateTimeFormatLocales[0]);
    var date = dtf.parse(dateStr, "dd/MM/yyyy");

    O$.parseTime(timeStr, date);
    return date;
  };

  O$.parseTime = function (timeStr, destDate) {
    if (!destDate)
      destDate = new Date();
    var timeFields = timeStr.split(":");
    var hours = timeFields[0];
    var minutes = timeFields[1];
    destDate.setHours(hours, minutes, 0, 0);
    return destDate;

  };

  O$.formatDateTime = function (date) {
    if (!date)
      return "";
    var dtf = O$.getDateTimeFormatObject(this._dateTimeFormatLocales[0]);
    var str = dtf.format(date, "dd/MM/yyyy");
    str += " " + O$.formatTime(date);
    return str;
  };

  O$.formatTime = function (date) {
    if (!date)
      return "";
    var hours = date.getHours();
    var minutes = date.getMinutes();
    if (minutes < 10)
      minutes = "0" + minutes;
    return hours + ":" + minutes;
  };

  // ----------------- ABSOLUTE POSITIONING / METRICS UTILITIES ---------------------------------------------------

  O$.getDefaultAbsolutePositionParent = function () {
    var prnt = document.forms[0];
    if (prnt)
      prnt = prnt.parent;
    if (!prnt) {
      prnt = document.body;
      if (!prnt) // document.body can be undefined in Mozilla with application/xhtml+xml mime type
        prnt = document.documentElement;
    }
    return prnt;
  };

  O$.createAbsolutePositionedElement = function (parent) {
    var elt = document.createElement("div");
    elt.style.position = "absolute";
    if (!parent)
      parent = O$.getDefaultAbsolutePositionParent();
    parent.appendChild(elt);
    return elt;
  };

  /*
   This function fixes the use-case when components such as DropDownField, need to move their own pop-up component out
   of their own markup into the "default absolute position parent" element. Then, when such component is reloaded with
   Ajax, the previous "moved out" pop-up remains intact although it's "owner" component is dismissed during reloading
   and replaced with the new one with its own new pop-up. As a result there is two pop-ups, and they usually have an
   equal id, which causes some problems (see for example OF-66). So this method just helps such components to remove
   the old copy before locating the new popup by id.
   */
  O$.removeRelocatedPopupIfExists = function (popupId) {
    var dapp = O$.getDefaultAbsolutePositionParent();
    var potentialPopups = dapp.childNodes;
    for (var i = 0, count = potentialPopups.length; i < count; i++) {
      var potentialPopup = potentialPopups[i];
      if (potentialPopup.id == popupId) {
        dapp.removeChild(potentialPopup);
      }
    }
  };

  /*
   O$.calculateNumericStyleProperty doesn't work when calculating margin on a table under Mozilla 2 for some reason,
   so here's an alternative implementation that works for this case.
   */
  O$.calculateMozillaMargins = function (element) {
    var styleClassProperties = {};
    styleClassProperties = O$.getStyleClassProperties(
            element.className, ["marginLeft", "marginRight", "marginTop", "marginBottom"]);
    var marginLeft = O$.calculateNumericCSSValue(styleClassProperties.marginLeft);
    var marginRight = O$.calculateNumericCSSValue(styleClassProperties.marginRight);
    var marginTop = O$.calculateNumericCSSValue(styleClassProperties.marginTop);
    var marginBottom = O$.calculateNumericCSSValue(styleClassProperties.marginBottom);
    marginLeft = Math.max(marginLeft, O$.calculateNumericCSSValue(element.style.marginLeft));
    marginRight = Math.max(marginRight, O$.calculateNumericCSSValue(element.style.marginRight));
    marginTop = Math.max(marginTop, O$.calculateNumericCSSValue(element.style.marginTop));
    marginBottom = Math.max(marginBottom, O$.calculateNumericCSSValue(element.style.marginBottom));
    return {marginLeft:marginLeft, marginRight:marginRight, marginTop:marginTop, marginBottom:marginBottom};
  };

  /*
   Returns an object {x, y} that points to the top-left corner of the specified element. This method takes into account
   element's border if it exists, that is it determines the position of the element's border box.

   If relativeToContainingBlock parameter is not specified or specified as false, this method
   calculates the elements' "absolute" position relative to the top-left corner of the entire document.

   If relativeToContainingBlock is specified as true, it returns an offset relative to the nearest containing block
   (see http://www.w3.org/TR/REC-CSS2/visuren.html#containing-block for definition of a containing block). More exactly
   it calculates offset relative to the containing block's client area. In other words if there is another absolutely
   positioned element in the same containing block, placing it at the position returned by this function will place that
   element at the same position as the element being measured with this function.

   Note that this function can't calculate position of an element that is hidden using "display: none" CSS declaration.
   Though you may work around such problems by using "visibility: hidden" instead of "display: none".

   See also: O$.setElementPos, O$.getElementBorderRectangle, O$.setElementBorderRectangle.
   */
  O$.getElementPos = function (element, relativeToContainingBlock) {
    var left, top;

    if (element.getBoundingClientRect) {
      var rect = element.getBoundingClientRect();
      left = rect.left;
      top = rect.top;
      var containingBlock;
      if (relativeToContainingBlock) {
        containingBlock = relativeToContainingBlock === true ? O$.getContainingBlock(element, true) : relativeToContainingBlock.offsetParent;
        if (containingBlock && containingBlock.nodeName.toUpperCase() == "BODY")
          containingBlock = null;
        if (containingBlock) {
          var containingRect = containingBlock.getBoundingClientRect();
          left += containingBlock.scrollLeft - containingRect.left - containingBlock.clientLeft;
          top += containingBlock.scrollTop - containingRect.top - containingBlock.clientTop;
        }
      }
      if (!containingBlock) {
        var pageScrollPos = O$.getPageScrollPos();
        left += pageScrollPos.x;
        top += pageScrollPos.y;
        if (O$.isExplorer()) {
          if (O$.isQuirksMode()) {
            left -= 2;
            top -= 2;
          } else {
            left -= document.documentElement.clientLeft;
            top -= document.documentElement.clientTop;
          }
        }
      }
      return {x:left, y:top};
    }

    if (relativeToContainingBlock && relativeToContainingBlock !== true) {
      var pos = O$.getElementPos(element);
      var containerPos = O$.getElementPos(relativeToContainingBlock.offsetParent);
      return {x:pos.x - containerPos.x, y:pos.y - containerPos.y};
    }

    left = top = 0;
    // Mozilla 2.0.x strict reports margins on tables to be part of table when measuring it with offsetXXX attributes
    if (O$.isMozillaFF2() && O$.isStrictMode() && element.tagName && element.tagName.toLowerCase() == "table") {
      var margins = O$.calculateMozillaMargins(element);
      left += margins.marginLeft;
      top += margins.marginTop;
    }

    while (element) {
      left += element.offsetLeft;
      top += element.offsetTop;
      var thisIsContainingBlock = O$.isContainingBlock(element);

      var offsetParent = element.offsetParent;
      if (!offsetParent)
        break;

      if (O$.isMozillaFF2() && O$.getElementStyle(offsetParent, "overflow") != "visible") {
        left += O$.getNumericElementStyle(offsetParent, "border-left-width");
        top += O$.getNumericElementStyle(offsetParent, "border-top-width");
      }

      var parentIsContainingBlock = O$.isContainingBlock(offsetParent);
      if (relativeToContainingBlock && parentIsContainingBlock) {
        if (offsetParent.tagName.toLowerCase() == "div" && (O$.isOpera9AndLate() || O$.isSafari2())) {
          if (O$.getElementStyle(offsetParent, "border-style") != "none") {
            var borderLeftWidth = O$.getNumericElementStyle(offsetParent, "border-left-width");
            var borderTopWidth = O$.getNumericElementStyle(offsetParent, "border-top-width");
            left -= borderLeftWidth;
            top -= borderTopWidth;
          }
        }
        break;
      }

      for (var parent = element.parentNode; parent; parent = parent.parentNode) {
        var parentNodeName = parent.nodeName ? parent.nodeName.toLowerCase() : null;
        var parentIsAPage = parentNodeName == "html" || parentNodeName == "body";
        if (!parentIsAPage) {
          var pScrollLeft = parent.scrollLeft;
          if (pScrollLeft)
            left -= pScrollLeft;
          var pScrollTop = parent.scrollTop;
          if (pScrollTop)
            top -= pScrollTop;
        }

        if (!thisIsContainingBlock) {
          // containing blocks already have the following corrections as part of offsetLeft/offsetTop
          if (O$.isMozillaFF2()) {
            if (parentNodeName == "td") {
              left -= O$.getNumericElementStyle(parent, "border-left-width");
              top -= O$.getNumericElementStyle(parent, "border-top-width");
            }
            if (parentNodeName == "table" && O$.isStrictMode()) {
              var parentMargins = O$.calculateMozillaMargins(parent);
              left += parentMargins.marginLeft;
              top += parentMargins.marginTop;
            }
          }
          if (O$.isSafari()) {
            if (parentNodeName == "table") {
              left += O$.getNumericElementStyle(parent, "border-left-width");
              top += O$.getNumericElementStyle(parent, "border-top-width");
            }
          }
        }

        if (parent == offsetParent)
          break;
      }

      // account for offsetParent's border
      if (!O$.isSafari2()) {
        var lowerCaseTagName = offsetParent.tagName.toLowerCase();
        if (lowerCaseTagName == "table" && O$.isSafari3AndLate()) {
          var border = O$.calculateNumericCSSValue(offsetParent.border);
          if (border > 0) {
            left += border;
            top += border;
          }
        } else {
          if (lowerCaseTagName == "div" || lowerCaseTagName == "td") {
            if (!O$.isOpera9AndLate()) { // border in Opera 9 included in offsetLeft and offsetTop
              left += (offsetParent.clientLeft == undefined ? O$.getNumericElementStyle(offsetParent, "border-left-width") : offsetParent.clientLeft);
              top += (offsetParent.clientTop == undefined ? O$.getNumericElementStyle(offsetParent, "border-top-width") : offsetParent.clientTop);
            }
          }
        }
      }

      element = offsetParent;
    }
    return {x:left, y:top};
  };

  O$.getCuttingContainingRectangle = function (element, cachedDataContainer) {
    var left, right, top, bottom;

    var position = O$.getElementStyle(element, "position", true);
    var clippingByNonStaticOnly = position != "static" && !(O$.isExplorer() && O$.isQuirksMode());
    while (true) {
      var container = element.parentNode;
      if (!container || container == document)
        break;
      if (clippingByNonStaticOnly) {
        var containerPosition = O$.getElementStyle(container, "position", true);
        if (containerPosition == "static") {
          element = container;
          continue;
        }
      }
      var overflowX = O$.getElementStyle(container, "overflow-x");
      var overflowY = O$.getElementStyle(container, "overflow-y");
      var containerRect = (overflowX != "visible" || overflowY != "visible") ?
              O$.getElementPaddingRectangle(container, false, cachedDataContainer) : null;
      if (overflowX != "visible") {
        left = O$.maxDefined(left, containerRect.getMinX());
        right = O$.minDefined(right, containerRect.getMaxX());
      }
      if (overflowY != "visible") {
        top = O$.maxDefined(top, containerRect.getMinY());
        bottom = O$.minDefined(bottom, containerRect.getMaxY());
      }
      element = container;
    }
    var result = new O$.Rectangle(left, top, right ? right - left : undefined, bottom ? bottom - top : undefined);
    var visibleAreaRect = O$.getVisibleAreaRectangle();
    result.intersectWith(visibleAreaRect);
    return result;
  };

  O$.getVisibleElementBorderRectangle = function (element, relativeToContainingBlock, cachedDataContainer) {
    var rect = O$.getElementBorderRectangle(element, relativeToContainingBlock, cachedDataContainer);
    var cuttingRect = O$.getCuttingContainingRectangle(element, cachedDataContainer);
    if (relativeToContainingBlock) {
      var elementContainer = O$.getContainingBlock(element, true);
      var containerPos = elementContainer ? O$.getElementPos(elementContainer) : null;
      if (cuttingRect.left && containerPos)
        cuttingRect.left -= containerPos.x;
      if (cuttingRect.top && containerPos)
        cuttingRect.top -= containerPos.y;
    }
    rect.intersectWith(cuttingRect);
    return rect;
  };

  /*
   Returns the O$.Rectangle object that corresponds to the bounding rectangle of the passed element. This method takes
   into account element's border. That is it returns element's border box rectangle.

   relativeToContainingBlock is an optional parameter that specifies whether the position is calculated relative
   to the document (if not specified or specified as false), or the nearest containing block's client area.

   cachedDataContainer is an optional parameter that if specifies introduces caching of the calculated value for improved
   performance. It should be specified as a reference to any object where the data should be cached. If this parameter is
   specified and rectangle of this element was already calculated with the same cachedDataContainer object before, then
   the previously calculated value is returned. Otherwise it calculates the current rectangle and saves it into the
   specified object.

   Note that this function can't calculate a rectangle of an element that is hidden using "display: none" CSS declaration.
   Though you may work around such problems by using "visibility: hidden" instead of "display: none".

   See also: O$.setElementBorderRectangle, O$.getElementPos, O$.setElementPos.
   */
  O$.getElementBorderRectangle = function (element, relativeToContainingBlock, cachedDataContainer) {
    if (cachedDataContainer) {
      if (!element._of_getElementRectangle)
        element._of_getElementRectangle = {};
      if (element._of_getElementRectangle._currentCache == cachedDataContainer)
        return element._of_getElementRectangle._cachedValue;
    }
    var pos = O$.getElementPos(element, relativeToContainingBlock);
    var size = O$.getElementSize(element);
    var rect = new O$.Rectangle(pos.x, pos.y, size.width, size.height);
    if (cachedDataContainer) {
      element._of_getElementRectangle._currentCache = cachedDataContainer;
      element._of_getElementRectangle._cachedValue = rect;
    }
    return rect;
  };

  O$.getElementSize = function (element) {
    var width = element.offsetWidth;
    var height = element.offsetHeight;
    // Mozilla 2.0.x strict reports margins on tables to be part of table when measuring it with offsetXXX attributes
    if (O$.isMozillaFF2() && O$.isStrictMode() && element.tagName && element.tagName.toLowerCase() == "table") {
      var margins = O$.calculateMozillaMargins(element);
      width -= margins.marginLeft + margins.marginRight;
      height -= margins.marginTop + margins.marginBottom;
    }
    return {width:width, height:height};
  };

  O$.getElementPaddingRectangle = function (element, relativeToContainingBlock, cachedDataContainer) {
    var rect = O$.getElementBorderRectangle(element, relativeToContainingBlock, cachedDataContainer);
    var borderLeftWidth = O$.getNumericElementStyle(element, "border-left-width");
    var borderRightWidth = O$.getNumericElementStyle(element, "border-right-width");
    var borderTopWidth = O$.getNumericElementStyle(element, "border-top-width");
    var borderBottomWidth = O$.getNumericElementStyle(element, "border-bottom-width");
    rect.x += borderLeftWidth;
    rect.y += borderTopWidth;
    rect.width -= borderLeftWidth + borderRightWidth;
    rect.height -= borderTopWidth + borderBottomWidth;
    return rect;
  };

  O$.getElementClientRectangle = function (element, relativeToContainingBlock, cachedDataContainer) {
    var rect = O$.getElementPaddingRectangle(element, relativeToContainingBlock, cachedDataContainer);
    rect.width = element.clientWidth;
    rect.height = element.clientHeight;
    return rect;
  };


  /*
   Moves the specified element to the specified position (specified as {x, y} object). More exactly the top-left
   corner of element's border box will be at the specified position after this method is invoked.

   See also: O$.getElementPos, O$.getElementBorderRectangle, O$.setElementBorderRectangle.
   */
  O$.setElementPos = function (element, pos) {
    element.style.left = pos.x + "px";
    element.style.top = pos.y + "px";
  };

  O$.setElementSize = function (element, size, _paddingsHaveBeenReset) {
    var width = size.width;
    var height = size.height;

    if (!_paddingsHaveBeenReset)
      O$.excludeClassNames(element, ["o_zeroPaddings"]);
    if (!O$.isExplorer() || O$.isStrictMode()) {
      if (width != null) {
        width -= O$.getNumericElementStyle(element, "padding-left") + O$.getNumericElementStyle(element, "padding-right");
        width -= O$.getNumericElementStyle(element, "border-left-width") + O$.getNumericElementStyle(element, "border-right-width");
      }
      if (height != null) {
        height -= O$.getNumericElementStyle(element, "padding-top") + O$.getNumericElementStyle(element, "padding-bottom");
        height -= O$.getNumericElementStyle(element, "border-top-width") + O$.getNumericElementStyle(element, "border-bottom-width");
      }
    }
    if (!_paddingsHaveBeenReset && (width < 0 || height < 0)) {
      // make it possible to specify element rectangle less than the size of element's paddings
      O$.appendClassNames(element, ["o_zeroPaddings"]);
      O$.setElementSize(element, size, true);
      return;
    }
    if (width < 0)
      width = 0;
    if (height < 0)
      height = 0;
    if (width != null)
      element.style.width = width + "px";
    if (height != null)
      element.style.height = height + "px";
  };

  O$._setElementWidthOrHeight = function (element, property, edge1Property, edge2Property, value, hundredPercentValue, _paddingsHaveBeenReset) {
    var valueParam = value;

    // hundredPercentValue is required to handle percent-based paddings in O$.getNumericElementStyle
    if (!_paddingsHaveBeenReset)
      O$.excludeClassNames(element, ["o_zeroPaddings"]);
    if (element.nodeName.toLowerCase() != "table") {
      if (!O$.isExplorer() || O$.isStrictMode()) {
        if (value != null) {
          if (O$._setElementWidthOrHeight._totalPaddingsAndBordersWidth != null) {
            value -= O$._setElementWidthOrHeight._totalPaddingsAndBordersWidth;
            O$._setElementWidthOrHeight._totalPaddingsAndBordersWidth = null;
          } else {
            value -= O$.getNumericElementStyle(element, "padding-" + edge1Property, false, hundredPercentValue) + O$.getNumericElementStyle(element, "padding-" + edge2Property, false, hundredPercentValue);
            value -= O$.getNumericElementStyle(element, "border-" + edge1Property + "-width", false, hundredPercentValue) + O$.getNumericElementStyle(element, "border-" + edge2Property + "-width", false, hundredPercentValue);
          }
        }
      }
    }
    if (!_paddingsHaveBeenReset && (value < 0)) {
      // make it possible to specify element rectangle less than the size of element's paddings
      O$.appendClassNames(element, ["o_zeroPaddings"]);
      O$._setElementWidthOrHeight(element, property, edge1Property, edge2Property, value, hundredPercentValue, true);
      return;
    }
    if (value < 0)
      value = 0;
    if (value != null)
      try {
        element.style[property] = value + "px";
      } catch (e) {
        alert("O$._setElementWidthOrHeight error. property = " + property + "; valueParam = " + valueParam + "; value = " + value);
        throw e;
      }
  };

  O$.setElementWidth = function (element, value, hundredPercentValue) {
    O$._setElementWidthOrHeight(element, "width", "left", "right", value, hundredPercentValue);
  };

  O$.setElementHeight = function (element, value, hundredPercentValue) {
    O$._setElementWidthOrHeight(element, "height", "top", "bottom", value, hundredPercentValue);
  };


  /*
   Changes size and position of the specified element according to the specified rectangle. The rectangle specifies
   the element's border box.

   See also: O$.getElementBorderRectangle, O$.getElementPos, O$.setElementPos.
   */
  O$.setElementBorderRectangle = function (element, rect) {
    O$.setElementSize(element, {width:rect.width, height:rect.height});
    O$.setElementPos(element, {x:rect.x, y:rect.y});
  };

  /**
   * Introduced according to CSS spec http://www.w3.org/TR/REC-CSS2/visudet.html#containing-block-details
   * See JSFC-2045 Popups displayed incorrectly under JBoss Portal
   */
  O$.isContainingBlock = function (elt) {
    O$.assert(elt, "elt is null");
    if (elt._containingBlock != undefined) return elt._containingBlock;
    if (elt == document) {
      // O$.calculateElementStyleProperty fails to determine position on the document element, and
      // document element can't have a non-static position
      return false;
    }
    var position = O$.getElementStyle(elt, "position");
    if (!position) return false;
    return elt._containingBlock = position != "static";
  };


  O$.getVisibleAreaSize = function () {
    var width = 0, height = 0;
    if (O$.isMozillaFF()) {
      if (O$.isQuirksMode()) {
        width = document.body.clientWidth;
        height = document.body.clientHeight;
      } else {
        width = document.documentElement.clientWidth;
        height = document.documentElement.clientHeight;
      }
    } else if (O$.isSafari()) {
      width = document.documentElement.clientWidth;
      height = document.documentElement.clientHeight;
    } else if (typeof window.innerWidth == "number") {
      width = window.innerWidth;
      height = window.innerHeight;
    } else if (document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
      width = document.documentElement.clientWidth;
      height = document.documentElement.clientHeight;
    } else if (document.body && (document.body.clientWidth || document.body.clientHeight)) {
      width = document.body.clientWidth;
      height = document.body.clientHeight;
    }
    return {width:width, height:height};
  };

  O$.getDocumentSize = function () {
    var width = Math.max(
            document.body.clientWidth, document.documentElement.clientWidth,
            document.body.scrollWidth, document.documentElement.scrollWidth,
            document.body.offsetWidth, document.documentElement.offsetWidth
    );
    var height = Math.max(
            document.body.clientHeight, document.documentElement.clientHeight,
            document.body.scrollHeight, document.documentElement.scrollHeight,
            document.body.offsetHeight, document.documentElement.offsetHeight
    );

    return {width:width, height:height};
  };


  O$.getVisibleAreaRectangle = function () {
    var pageScrollPos = O$.getPageScrollPos();
    var x = pageScrollPos.x;
    var y = pageScrollPos.y;
    var visibleAreaSize = O$.getVisibleAreaSize();
    var width = visibleAreaSize.width;
    var height = visibleAreaSize.height;
    return new O$.Rectangle(x, y, width, height);
  };

  O$.getDocumentRectangle = function () {
    var documentSize = O$.getDocumentSize();
    return new O$.Rectangle(0, 0, documentSize.width, documentSize.height);
  };

  O$.scrollElementIntoView = function (element, scrollVertical, scrollHorizontal, cachedDataContainer) {
    var scrollingOccurred = false;
    var elements = element instanceof Array ? element : [element];
    var rect;
    elements.forEach(function (el) {
      if (!rect)
        rect = O$.getElementBorderRectangle(el);
      else
        rect.addRectangle(O$.getElementBorderRectangle(el));
    });

    for (var parent = element instanceof Array ? element[0].parentNode : element.parentNode;
         parent;
         parent = parent.parentNode) {
      var parentScrollable;
      if (parent != document) {
        if (scrollVertical) {
          var overflowY = O$.getElementStyle(parent, "overflow-y");
          parentScrollable = overflowY != "visible";
        }
        if (!parentScrollable && scrollHorizontal) {
          var overflowX = O$.getElementStyle(parent, "overflow-x");
          parentScrollable = overflowX != "visible";
        }
      } else {
        parentScrollable = true;
      }
      if (!parentScrollable)
        continue;
      var parentRect = parent != document
              ? O$.getElementPaddingRectangle(parent, false, cachedDataContainer)
              : O$.getVisibleAreaRectangle();
      if (parent != document) {
        parentRect.width = parent.clientWidth;
        parentRect.height = parent.clientHeight;
      }
      if (scrollVertical) {
        var scrollTopAdjustment = 0;
        if (parentRect.getMinY() > rect.getMinY())
          scrollTopAdjustment = rect.getMinY() - parentRect.getMinY();
        else if (parentRect.getMaxY() < rect.getMaxY())
          scrollTopAdjustment = rect.getMaxY() - parentRect.getMaxY();
        if (scrollTopAdjustment) {
          if (parent != document)
            parent.scrollTop += scrollTopAdjustment;
          else
            window.scrollBy(0, scrollTopAdjustment);
          rect.y -= scrollTopAdjustment;
          scrollingOccurred = true;
        }
      }
      if (scrollHorizontal) {
        var scrollLeftAdjustment = 0;
        if (parentRect.getMinX() > rect.getMinX())
          scrollLeftAdjustment = rect.getMinX() - parentRect.getMinX();
        else if (parentRect.getMaxX() < rect.getMaxX())
          scrollLeftAdjustment = rect.getMaxX() - parentRect.getMaxX();
        if (scrollLeftAdjustment) {
          if (parent != document)
            parent.scrollLeft += scrollLeftAdjustment;
          else
            window.scrollBy(scrollLeftAdjustment, 0);
          rect.x -= scrollLeftAdjustment;
          scrollingOccurred = true;
        }
      }
    }
    return scrollingOccurred;
  };

  O$.scrollRectIntoView = function (rect) {
    var visibleRect = O$.getVisibleAreaRectangle();
    var dx = 0;
    if (rect.getMinX() < visibleRect.getMinX())
      dx = rect.getMinX() - visibleRect.getMinX();
    if (rect.getMaxX() > visibleRect.getMaxX())
      dx = rect.getMaxX() - visibleRect.getMaxX();
    var dy = 0;
    if (rect.getMinY() < visibleRect.getMinY())
      dy = rect.getMinY() - visibleRect.getMinY();
    if (rect.getMaxY() > visibleRect.getMaxY())
      dy = rect.getMaxY() - visibleRect.getMaxY();

    // account for rect being bigger than visibleRect - align by top, right edges should have a priority in this case
    var newMinX = visibleRect.getMinX() + dx;
    if (rect.getMinX() < newMinX)
      dx = rect.getMinX() - newMinX;
    var newMinY = visibleRect.getMinY() + dy;
    if (rect.getMinY() < newMinY)
      dy = rect.getMinY() - newMinY;

    window.scrollBy(dx, dy);
  };

  O$.getScrollPos = function (scrollableComponent) {
    var x = 0;
    var y = 0;

    if (typeof( scrollableComponent.pageYOffset ) == "number") {
      // Netscape compliant
      y = scrollableComponent.pageYOffset;
      x = scrollableComponent.pageXOffset;
    } else if (scrollableComponent.scrollLeft || scrollableComponent.scrollTop) {
      // DOM compliant
      y = scrollableComponent.scrollTop;
      x = scrollableComponent.scrollLeft;
    }

    return {x:x, y:y};
  };

  O$.getPageScrollPos = function () {
    var x = 0;
    var y = 0;
    if (typeof( window.pageYOffset ) == "number") {
      y = window.pageYOffset;
      x = window.pageXOffset;
    } else if (document.body && ( document.body.scrollLeft || document.body.scrollTop )) {
      y = document.body.scrollTop;
      x = document.body.scrollLeft;
    } else if (document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop )) {
      y = document.documentElement.scrollTop;
      x = document.documentElement.scrollLeft;
    }
    return {x:x, y:y};
  };

  O$.setPageScrollPos = function (scrollPos) {
    window.scrollTo(scrollPos.x, scrollPos.y);
  };

  O$.isCursorOverElement = function (event, element) {
    var evt = O$.getEvent(event);
    if (!element ||
            !O$.isElementPresentInDocument(element) ||
            !O$.isVisibleRecursive(element))
      return false;
    var rect = O$.getElementBorderRectangle(element);
    var cursorPos = O$.getEventPoint(evt);
    return rect.containsPoint(cursorPos.x, cursorPos.y);
  };

  O$.getNumericElementStyle = function (element, propertyName, enableValueCaching, hundredPercentValue) {
    var capitalizedPropertyName = O$._capitalizeCssPropertyName(propertyName);
    if (O$.stringStartsWith(capitalizedPropertyName, "border") && O$.stringEndsWith(capitalizedPropertyName, "Width")) {
      var borderName = capitalizedPropertyName.substring(0, capitalizedPropertyName.length - "Width".length);
      var borderStyleName = borderName + "Style";
      if (O$.isOpera()) {
        if (O$.getElementStyle(element, borderStyleName) == "none")
          return 0;
      } else if (O$.isExplorer()) {
        var borderWidthName = borderName + "Width";
        if (O$.getElementStyle(element, borderStyleName) == "none" &&
                O$.getElementStyle(element, borderWidthName) == "medium")
          return 0;
      }
    }
    var str = O$.getElementStyle(element, propertyName, enableValueCaching);
    var result = O$.calculateNumericCSSValue(str, hundredPercentValue);
    return result;
  };

  O$.getIndentData = function (element, indentDelta) {
    var indent = {};

    if (indentDelta.marginLeft) {
      indent.marginLeft = (indentDelta.marginLeft + O$.getNumericElementStyle(element, "margin-left")) + "px";
    }
    if (indentDelta.marginRight) {
      indent.marginRight = (indentDelta.marginRight + O$.getNumericElementStyle(element, "margin-right")) + "px";
    }
    if (indentDelta.marginBottom) {
      indent.marginBottom = (indentDelta.marginBottom + O$.getNumericElementStyle(element, "margin-bottom")) + "px";
    }
    return indent;
  };

  O$.calculateNumericCSSValue = function (value, hundredPercentValue) {
    if (!value)
      return 0;
    if (!isNaN(1 * value))
      return 1 * value;
    if (O$.stringEndsWith(value, "px"))
      return 1 * value.substring(0, value.length - 2);
    if (value == "auto")
      return 0; // todo: can't calculate "auto" (e.g. from margin property) on a simulated border -- consider simulating such "non-border" values on other properties
    if (value.indexOf("%") == value.length - 1) {
      var val = value.substring(0, value.length - 1);
      if (typeof hundredPercentValue == "function")
        hundredPercentValue = hundredPercentValue();
      return val / 100.0 * hundredPercentValue;
    }

    if (!O$._nonPixelValueMeasurements)
      O$._nonPixelValueMeasurements = [];
    var pixelValue = O$._nonPixelValueMeasurements[value];
    if (pixelValue != undefined)
      return pixelValue;

    pixelValue = O$.calculateLineWidth(value + " solid white");
    O$._nonPixelValueMeasurements[value] = pixelValue;
    return pixelValue;
  };

  O$.calculateLineWidth = function (lineStyleStr) {
    if (!lineStyleStr)
      return 0;
    if (!window._of_lineWidthValueMeasurements)
      window._of_lineWidthValueMeasurements = [];
    var width = window._of_lineWidthValueMeasurements[lineStyleStr];
    if (width != undefined)
      return width;

    var outerDiv = O$._nonPixelMeasurements_outerDiv;
    var innerDiv = O$._nonPixelMeasurements_innerDiv;
    if (!outerDiv) {
      outerDiv = document.createElement("div");
      outerDiv.style.visibility = "hidden";
      outerDiv.style.padding = "0px";
      outerDiv.style.margin = "0px";
      innerDiv = document.createElement("div");
      innerDiv.style.padding = "0px";
      innerDiv.style.margin = "0px";
      outerDiv.appendChild(innerDiv);
      O$._nonPixelMeasurements_outerDiv = outerDiv;
      O$._nonPixelMeasurements_innerDiv = innerDiv;
    }
    outerDiv.style.border = lineStyleStr;
    document.body.appendChild(outerDiv);
    width = innerDiv.offsetTop - outerDiv.offsetTop;
    document.body.removeChild(outerDiv);
    window._of_lineWidthValueMeasurements[lineStyleStr] = width;
    return width;
  };

  O$.CENTER = "center";
  O$.LEFT_OUTSIDE = "leftOutside";
  O$.LEFT = "left";
  O$.RIGHT = "right";
  O$.RIGHT_OUTSIDE = "rightOutside";
  O$.ABOVE = "above";
  O$.TOP = "top";
  O$.BOTTOM = "bottom";
  O$.BELOW = "below";


  O$.alignPopupByPoint = function (popup, x, y, horizAlignment, vertAlignment) {
    O$.alignPopupByElement(popup, new O$.Rectangle(x, y, 0, 0), horizAlignment, vertAlignment);
  };
  /**
   *
   *
   * @param popup
   * @param element
   * @param horizAlignment
   * @param vertAlignment
   * @param horizDistance
   * @param vertDistance
   * @param ignoreVisibleArea missing or false declaration will use position the popup relatively the visible portion of
   *                          the reference element (e.g. when it is partially occluded due to scrolling and hidden
   *                          overflow declarations)
   * @param disableRepositioning
   * @param repositioningAttempt
   */
  O$.alignPopupByElement = function (popup, element, horizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, disableRepositioning, repositioningAttempt, xDelta, yDelta) {
    if (!horizAlignment) horizAlignment = O$.LEFT;
    if (!vertAlignment) vertAlignment = O$.BELOW;
    if (!horizDistance) horizDistance = 0;
    if (!vertDistance) vertDistance = 0;
    horizDistance = O$.calculateNumericCSSValue(horizDistance);
    vertDistance = O$.calculateNumericCSSValue(vertDistance);

    var elementRect = function () {
      if (element instanceof O$.Rectangle)
        return element;
      if (element != window)
        return ignoreVisibleArea
                ? O$.getElementBorderRectangle(element)
                : O$.getVisibleElementBorderRectangle(element);
      return O$.getVisibleAreaRectangle();
    }();
    var popupSize = O$.getElementSize(popup);
    var popupWidth = popupSize.width;
    var popupHeight = popupSize.height;

    xDelta = xDelta ? xDelta : 0;
    yDelta = yDelta ? yDelta : 0;

    var x;
    switch (horizAlignment) {
      case O$.LEFT_OUTSIDE:
        x = elementRect.getMinX() - popupWidth - horizDistance;
        break;
      case O$.LEFT:
        x = elementRect.getMinX() + horizDistance;
        break;
      case O$.CENTER:
        x = elementRect.getMinX() + (elementRect.width - popupWidth) / 2 + horizDistance;
        break;
      case O$.RIGHT:
        x = elementRect.getMaxX() - popupWidth - horizDistance;
        break;
      case O$.RIGHT_OUTSIDE:
        x = elementRect.getMaxX() + horizDistance;
        break;
      default:
        O$.logError("O$.alignPopupByElement: unrecognized horizAlignment: " + horizAlignment);
    }
    var y;
    switch (vertAlignment) {
      case O$.ABOVE:
        y = elementRect.getMinY() - popupHeight - vertDistance;
        break;
      case O$.TOP:
        y = elementRect.getMinY() + vertDistance;
        break;
      case O$.CENTER:
        y = elementRect.getMinY() + (elementRect.height - popupHeight) / 2 + vertDistance;
        break;
      case O$.BOTTOM:
        y = elementRect.getMaxY() - popupHeight - vertDistance;
        break;
      case O$.BELOW:
        y = elementRect.getMaxY() + vertDistance;
        break;
      default:
        O$.logError("O$.alignPopupByElement: unrecognized vertAlignment: " + vertAlignment);
    }
    x += xDelta;
    y += yDelta;

    if (!disableRepositioning) {
      var allowedRectangle = O$.getCuttingContainingRectangle(popup);
      var shouldBeRepositioned = !allowedRectangle.containsRectangle(new O$.Rectangle(x, y, popupWidth, popupHeight)) &&
              allowedRectangle.width >= popupWidth && allowedRectangle.height >= popupHeight;
      if (shouldBeRepositioned) {
        if (repositioningAttempt)
          return false;
        if (popup.setLeft) {
          popup.setLeft(x);
          popup.setTop(y);
        }
        var alternativeVertAlignment = vertAlignment == O$.BELOW || vertAlignment == O$.ABOVE
                ? vertAlignment == O$.BELOW ? O$.ABOVE : O$.BELOW
                : null;
        if (alternativeVertAlignment) {
          if (O$.alignPopupByElement(popup, element, horizAlignment, alternativeVertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true))
            return true;
        }
        var alternativeHorizAlignment = horizAlignment == O$.LEFT_OUTSIDE || horizAlignment == O$.RIGHT_OUTSIDE
                ? horizAlignment == O$.RIGHT_OUTSIDE ? O$.LEFT_OUTSIDE : O$.RIGHT_OUTSIDE
                : null;
        if (alternativeHorizAlignment) {
          if (O$.alignPopupByElement(popup, element, alternativeHorizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true))
            return true;
        }
        if (alternativeHorizAlignment && alternativeVertAlignment) {
          if (O$.alignPopupByElement(popup, element, alternativeHorizAlignment, alternativeVertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true))
            return true;
        }
        // possible align correction with offsets
        if (!alternativeHorizAlignment && alternativeVertAlignment){
          var xCorrection =  x < allowedRectangle.getMinX()
                  ? allowedRectangle.getMinX() - x
                  : x + popupWidth > allowedRectangle.getMaxX() ? allowedRectangle.getMaxX() - (x + popupWidth) : 0;
          if (O$.alignPopupByElement(popup, element, horizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true, xCorrection, 0)){
            return true;
          }
          if (O$.alignPopupByElement(popup, element, horizAlignment, alternativeVertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true, xCorrection, 0)){
            return true;
          }
        }

        // possible align correction with offsets
        if (alternativeHorizAlignment && !alternativeVertAlignment){
          var yCorrection =  y < allowedRectangle.getMinY()
                  ? allowedRectangle.getMinY() - y
                  : y + popupHeight > allowedRectangle.getMaxY() ? allowedRectangle.getMaxY() - (y + popupHeight) : 0;
          if (O$.alignPopupByElement(popup, element, horizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true,  0, yCorrection)){
            return true;
          }
          if (O$.alignPopupByElement(popup, element, alternativeHorizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true, 0, yCorrection)){
            return true;
          }
        }
        var xOffset = x < allowedRectangle.getMinX()
                ? allowedRectangle.getMinX() - x
                : x + popupWidth > allowedRectangle.getMaxX() ? allowedRectangle.getMaxX() - (x + popupWidth) : 0;
        var yOffset = y < allowedRectangle.getMinY()
                ? allowedRectangle.getMinY() - y
                : y + popupHeight > allowedRectangle.getMaxY() ? allowedRectangle.getMaxY() - (y + popupHeight) : 0;
        x += xOffset;
        y += yOffset;
      }
    }

    var popupContainer = popup.offsetParent;
    if (popupContainer && popupContainer.nodeName.toLowerCase() != "body") {
      var containerRect = O$.getElementPaddingRectangle(popupContainer);
      x -= containerRect.x;
      y -= containerRect.y;
      if (popupContainer.scrollLeft) x += popupContainer.scrollLeft;
      if (popupContainer.scrollTop) y += popupContainer.scrollTop;
    }
    if (popup.setLeft) {
      popup.setLeft(x);
      popup.setTop(y);
    } else {
      O$.setElementPos(popup, {x:x, y:y});
    }
    if (repositioningAttempt)
      return true;
  };

  O$.isAlignmentInsideOfElement = function (horizAlignment, vertAlignment) {
    var insideHorizontally = horizAlignment == O$.LEFT || horizAlignment == O$.CENTER || horizAlignment == O$.RIGHT;
    var insideVertically = vertAlignment == O$.TOP || vertAlignment == O$.CENTER || vertAlignment == O$.BOTTOM;
    return insideHorizontally && insideVertically;
  };

  O$.isAlignmentInTail = function (vertAlignment) {
    return vertAlignment == O$.BOTTOM || vertAlignment == O$.BELOW;
  };

  O$.fixInputsWidthStrict = function (container) {
    if (!O$.isStrictMode())
      return;

    function processInput(input) {
      if (input._strictWidthFixed || input.type == "hidden")
        return;
      input._strictWidthFixed = true;
      var parent = input.parentNode;
      var widthCorrection;
      if ((input._o_fullWidth == undefined && O$.getStyleClassProperty(input.className, "width") == "100%") || input._o_fullWidth) {
        var bordersX = input._o_zeroBorders ? 0 : O$.getNumericElementStyle(input, "border-left-width") + O$.getNumericElementStyle(input, "border-right-width");
        var paddingsX = O$.getNumericElementStyle(input, "padding-left") + O$.getNumericElementStyle(input, "padding-right");
        widthCorrection = bordersX + paddingsX;
      }
      var heightCorrection;
      if ((input._o_fullHeight == undefined && O$.getStyleClassProperty(input.className, "height") == "100%") || input._o_fullHeight) {
        var bordersY = O$.getNumericElementStyle(input, "border-top-width") + O$.getNumericElementStyle(input, "border-bottom-width");
        var paddingsY = O$.getNumericElementStyle(input, "padding-top") + O$.getNumericElementStyle(input, "padding-bottom");
        heightCorrection = bordersY + paddingsY;
      }

      if (widthCorrection) {
        var parentPaddingRight = O$.getNumericElementStyle(parent, "padding-right");
        parent.style.paddingRight = parentPaddingRight + widthCorrection + "px";
      }
      if (heightCorrection) {
        var parentPaddingBottom = O$.getNumericElementStyle(parent, "padding-bottom");
        parent.style.paddingBottom = parentPaddingBottom + heightCorrection + "px";
      }

    }

    var inputs = container.getElementsByTagName("input");
    for (var i = 0, count = inputs.length; i < count; i++) {
      processInput(inputs[i]);
    }
    var textAreas = container.getElementsByTagName("textarea");
    for (i = 0, count = textAreas.length; i < count; i++) {
      processInput(textAreas[i]);
    }

  };

  // ----------------- HIDE <SELECT> CONTROLS UNDER POPUP IN IE ---------------------------------------------------

  O$._controlsToHide = O$.isExplorer() ? ["select-one", "select-multiple"] : null;
  O$._controlsHiddenControlsMap = {};

  O$.walkControlsToHide = function (popup, runFunction) {
    if (popup._coveredControls) {
      for (var i = 0; i < popup._coveredControls.length; i++) {
        var control = popup._coveredControls[i];
        runFunction.call(this, control);
      }
    }
  };

  O$.initIETransparencyWorkaround = function (popup) {
    if (!O$.isExplorer6())
      return;
    popup._requireTransparencyWorkaround = true;
    if (popup._preCreatedIETransparencyControl)
      return;

    var iframe = document.createElement("iframe");
    iframe.src = "javascript:'';";
    // to overcome the "non-secure items" message on HTTPS pages
    iframe.id = popup.id + "::ieTransparencyControl";
    iframe.scrolling = "No";
    iframe.frameBorder = "0";
    iframe.style.position = "absolute";
    iframe.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)";
    // to support transparency of the popup itself

    iframe._updatePositionAndSize = function () {
      if (this.parentNode != popup.parentNode) {
        popup.parentNode.appendChild(this);
      }
      this.style.width = popup.offsetWidth + "px";
      this.style.height = popup.offsetHeight + "px";
      this.style.left = popup.offsetLeft + "px";
      this.style.top = popup.offsetTop + "px";
      O$.correctElementZIndex(this, popup, -1);
    };

    var popupZIndex = O$.getElementStyle(popup, "z-index");
    if (!popupZIndex) {
      popupZIndex = 10;
      popup.style.zIndex = popupZIndex;
    }
    iframe.zIndex = popup.zIndex - 1;

    iframe.style.display = "none";
    popup.parentNode.insertBefore(iframe, popup);
    popup._preCreatedIETransparencyControl = iframe;
  };

  O$.addIETransparencyControl = function (popup) {
    if (!popup._requireTransparencyWorkaround)
      return;
    if (popup._ieTransparencyControl) {
      popup._ieTransparencyControl._updatePositionAndSize();
      return;
    }

    if (!popup._preCreatedIETransparencyControl)
      O$.initIETransparencyWorkaround(popup);
    var iframe = popup._preCreatedIETransparencyControl;
    iframe.style.display = "";
    iframe._updatePositionAndSize();
    popup._ieTransparencyControl = iframe;
  };

  O$.removeIETransparencyControl = function (popup) {
    if (!popup._requireTransparencyWorkaround || !popup._ieTransparencyControl)
      return;

    popup._ieTransparencyControl.style.display = "none";
    popup._ieTransparencyControl = undefined;
  };

  O$.isVisible = function (element) {
    return element && O$.getElementStyle(element, "display") != "none" && O$.getElementStyle(element, "visibility") != "hidden";
    // todo: check floatingIconMessage behavior in different browsers before reusing this function for O$.isInvisible implementation
  };

  O$.isInvisible = function (element) {
    if (!element.style) {
      return false;
    }
    return element.style.display == "none" || element.style.visibility == "hidden";
  };

  O$.isVisibleRecursive = function (element) {
    if (!O$.isVisible(element))
      return false;

    var parentNode = element.parentNode;
    if (!parentNode || parentNode == document)
      return true;

    return O$.isVisibleRecursive(parentNode);
  };


  // ----------------- EFFECTS ---------------------------------------------------

  O$.getInterpolatedValue = function (value1, value2, value2Proportion) {
    // handle numbers
    if (!isNaN(1 * value1) && !isNaN(1 * value2))
      return value1 + (value2 - value1) * value2Proportion;

    // handle colors
    if (typeof value1 === "string" && O$.stringStartsWith(value1, "#"))
      return O$.blendColors(value1, value2, value2Proportion);

    // handle points
    if (typeof value1 === "object" && value1 != null && value1.x !== undefined && value1.width === undefined)
      return {
        x:O$.getInterpolatedValue(value1.x, value2.x, value2Proportion),
        y:O$.getInterpolatedValue(value1.y, value2.y, value2Proportion)};

    // handle rectangles
    if (typeof value1 === "object" && value1 != null && value1.width !== undefined)
      return new O$.Rectangle(
              O$.getInterpolatedValue(value1.x, value2.x, value2Proportion),
              O$.getInterpolatedValue(value1.y, value2.y, value2Proportion),
              O$.getInterpolatedValue(value1.width, value2.width, value2Proportion),
              O$.getInterpolatedValue(value1.height, value2.height, value2Proportion)
      );

    // the rest is treated as CSS units such as px, cm, etc.
    value1 = O$.calculateNumericCSSValue(value1);
    value2 = O$.calculateNumericCSSValue(value2);
    return value1 + (value2 - value1) * value2Proportion + "px";
  };

  /*
   Similar to O$.calculateElementStyleProperty, but it supports some "pseudo" properties for the sake of simplicity and
   portability, such as element's opacity, position, rectangle, etc.
   */
  O$.getElementEffectProperty = function (element, property) {
    if (property == "opacity")
      return O$.getOpacityLevel(element);
    if (property == "position")
      return O$.getElementPos(element, true);
    if (property == "rectangle")
      return O$.getElementBorderRectangle(element, true);
    if (property == "width")
      return O$.getElementSize(element).width;
    if (property == "height")
      return O$.getElementSize(element).height;
    if (property)

      return O$.getElementStyle(element, property);
  };

  O$.setElementEffectProperty = function (element, property, value) {
    if (property == "opacity")
      O$.setOpacityLevel(element, value);
    else if (property == "position")
      O$.setElementPos(element, value);
    else if (property == "rectangle")
      O$.setElementBorderRectangle(element, value);
    else if (property == "height")
      O$.setElementHeight(element, value);
    else if (property == "width")
      O$.setElementWidth(element, value);
    else
      element.style[property] = value;
  };

  O$.runTransitionEffect = function (element, propertyNames, newValues, transitionPeriod, updateInterval, events) {
    if (transitionPeriod === undefined || transitionPeriod < 0)
      transitionPeriod = 0;
    if (!updateInterval)
      updateInterval = 40;
    var initialValues = {};
    if (!(propertyNames instanceof Array)) {
      propertyNames = [propertyNames];
      if (newValues instanceof Array) throw "newValues must be a single value (not an array) in case when propertyNames is not an array";
      newValues = [newValues];
    }
    var propertyCount = propertyNames.length;
    O$.assertEquals(propertyCount, newValues.length, "O$.runTransitionEffect: checking propertyNames.length == newValues.length");

    var startTime = new Date().getTime();
    var endTime = startTime + transitionPeriod;
    var transition = {
      active:true,
      propertyValues:{},
      completionProportion:0,
      getValueForCompletionProportion:function (propertyName, proportion) {
        return O$.getInterpolatedValue(initialValues[propertyName], newValues[propertyName], proportion);
      },
      update:function (forceProportion) {
        var time = new Date().getTime();
        var timeElapsed = time - startTime;
        this.completionProportion = forceProportion === undefined ? timeElapsed / (endTime - startTime) : forceProportion;
        if (this.completionProportion > 1)
          this.completionProportion = 1;

        for (var i = 0; i < propertyCount; i++) {
          var propertyName = propertyNames[i];
          if (element._of_performingTransitionForProperties[propertyName] != this) {
            // another transition has begun before this one has finished
            this.propertyValues[propertyName] = undefined;
            if (propertyNames.length == 1)
              this.stop();
            continue;
          }
          var currentValue = this.getValueForCompletionProportion(propertyName, this.completionProportion);
          this.propertyValues[propertyName] = currentValue;
          O$.setElementEffectProperty(element, propertyName, currentValue);
        }
        if (this.onupdate)
          this.onupdate();
      },
      stop:function (forceCompletionProportion) {
        if (!this.active)
          return;
        this.active = false;
        if (forceCompletionProportion !== undefined)
          this.update(forceCompletionProportion);
        clearInterval(transition.intervalId);
        for (var i = 0; i < propertyCount; i++) {
          var propertyName = propertyNames[i];
          if (element._of_performingTransitionForProperties[propertyName] == this)
            element._of_performingTransitionForProperties[propertyName] = undefined;
        }
        if (this.onstop)
          this.onstop();
      },
      intervalId:setInterval(function () {
        transition.update();
        if (transition.completionProportion == 1) {
          transition.stop();
          if (transition.oncomplete)
            transition.oncomplete();
        }
      }, updateInterval)
    };
    O$.assignEvents(transition, events, true);

    if (!element._of_performingTransitionForProperties)
      element._of_performingTransitionForProperties = {};
    for (var i = 0; i < propertyCount; i++) {
      var propertyName = propertyNames[i];
      element._of_performingTransitionForProperties[propertyName] = transition;
      initialValues[propertyName] = O$.getElementEffectProperty(element, propertyName);
      newValues[propertyName] = newValues[i];
    }
    if (transitionPeriod == 0) {
      transition.stop(1.0);
      if (transition.oncomplete)
        transition.oncomplete();
    }

    return transition;
  };

  O$.fixElement = function (element, properties, workingCondition, events, interval) {
    if (!interval)
      interval = 200;
    var fixture = {
      values:{},
      update:function () {
        var elements = element instanceof Array ? element : [element];
        if (
                !elements.every(function (el) {
                  return O$.isElementPresentInDocument(el);
                }) ||
                        (workingCondition && !workingCondition())
                ) {
          clearInterval(fixture.intervalId);
          return;
        }
        for (var propertyName in properties) {
          var propertyValue = properties[propertyName]();

          if (this.values[propertyName] != propertyValue) {
            this.values[propertyName] = propertyValue;
            if (element instanceof Array)
              element.forEach(function (el) {
                O$.setElementEffectProperty(el, propertyName, propertyValue);
              });
            else
              O$.setElementEffectProperty(element, propertyName, propertyValue);

            if (this.onchange)
              this.onchange();
          }
        }
      },
      intervalId:setInterval(function () {
        fixture.update();
      }, interval),

      release:function () {
        clearInterval(this.intervalId);
      }
    };
    for (var event in events)
      fixture[event] = events[event];
    fixture.update();
    return fixture;
  };

  O$.listenProperty = function (element, propertyNames, listenerFunction, notifiers) {
    if (!(propertyNames instanceof Array))
      propertyNames = [propertyNames];
    if (!notifiers)
      notifiers = [new O$.Timer(200)];
    if (!(notifiers instanceof Array))
      notifiers = [notifiers];
    var listener = {
      values:{},
      update:function () {
        if (!O$.isElementPresentInDocument(element)) {
          this.release();
          return;
        }
        var changed = false;
        var currentValues = [];
        propertyNames.forEach(function (propertyName) {
          var propertyValue = O$.getElementEffectProperty(element, propertyName);
          currentValues.push(propertyValue);
          if (listener.values[propertyName] != propertyValue) {
            listener.values[propertyName] = propertyValue;
            changed = true;
          }
        });
        if (changed)
          listenerFunction.apply(null, currentValues);
      },

      release:function () {
        notifiers.forEach(function (n) {
          n.release();
        });
      }
    };
    notifiers.forEach(function (n) {
      n.addListener(function () {
        listener.update();
      });
      if (n.setActive) n.setActive(true);
    });
    listener.update();

    return listener;

  };

  O$.EventListener = O$.createClass(null, {
    constructor:function (element, eventName, listener) {
      this._element = element;
      this._eventName = eventName;
      this._listeners = [];
      if (listener) this.addListener(listener);

      var eventListener = this;
      O$.addEventHandler(this._element, this._eventName, this.handlerOfEvent = function (evt) {
        eventListener._dispatchNotifications(evt);
      });
    },
    _dispatchNotifications:function (evt) {
      var eventListener = this;
      this._listeners.forEach(function (listener) {
        listener(evt, eventListener);
      });
    },
    addListener:function (listener) {
      this.removeListener(listener);
      this._listeners.push(listener);
    },
    removeListener:function (listener) {
      var idx = this._listeners.indexOf(listener);
      if (idx == -1) return;
      this._listeners = this._listeners.slice(idx, 1);
    },
    release:function () {
      O$.removeEventHandler(this._element, this._eventName, this.handlerOfEvent);
    }

  });

  O$.Timer = O$.createClass(null, {
    constructor:function (period, listener) {
      if (!period || period < 0)
        throw "O$.Timer constructor: period parameter must be specified with a positive value: " + period;
      this._period = period;
      this._intervalId = null;
      this._listeners = [];
      if (listener) addListener(listener);
    },
    addListener:function (listener) {
      this.removeListener(listener);
      this._listeners.push(listener);
    },
    removeListener:function (listener) {
      var idx = this._listeners.indexOf(listener);
      if (idx == -1) return;
      this._listeners = this._listeners.slice(idx, 1);
    },
    start:function () {
      if (this._intervalId) return;
      var timer = this;
      this._intervalId = setInterval(function () {
        timer._listeners.forEach(function (listener) {
          listener(timer);
        });
      }, timer._period);
    },
    stop:function () {
      if (!this._intervalId) return;
      clearInterval(this._intervalId);
      this._intervalId = null;
    },
    isActive:function () {
      return !!this._intervalId;
    },
    setActive:function (active) {
      if (active == this.isActive()) return;
      if (active)
        this.start();
      else
        this.stop();
    },
    release:function () {
      this.stop();
    }
  });

  // ----------------- COMPONENT UTILS -------------------------------------------

  O$._checkDefaultCssPresence = function () {
    if (O$._defaultCssPresenceChecked) return;
    O$._defaultCssPresenceChecked = true;
    O$.addLoadEvent(function () {
      if (!O$.findStyleSheet("default.css"))
        O$.logError("OpenFaces default.css file is not loaded. Did you use <head> tag instead of <h:head> tag?");
    });
  };

  /**
   * Fixes the Prototype 1.6.0.2 conflict with the JSON2 library.
   * See http://stackoverflow.com/questions/710586/json-stringify-bizarreness
   */
  O$.cleanUpPrototypeJsonIncompatibility = function () {
    delete Date.prototype.toJSON;
    delete String.prototype.toJSON;
    delete Array.prototype.toJSON;
    delete Number.prototype.toJSON;
  };

  /**
   * Fixes the wrong position(priority) of default.css after updating mojara version to 2.0.5 or above.
   * http://requests.openfaces.org/browse/OF-163
   */
  O$._lowerPriorityOfDefaultCss = function () {
    var styleSheets = document.styleSheets;
    if (!styleSheets)
      return;
    var firstCss = styleSheets[0];
    var defaultCss = O$.findStyleSheet("default.css");
    if (!defaultCss)
      return;
    if (document.createStyleSheet) {
      document.createStyleSheet(defaultCss.href, 0);
      domElements = document.getElementsByTagName("link");
      for (var elementIndex = 0; elementIndex < domElements.length; elementIndex++) {
        if (domElements[elementIndex].href.indexOf("default.css") > 0) {
          firstDefaultCSSLink = domElements[elementIndex];
          break;
        }
      }
      firstDefaultCSSLink.onreadystatechange = function () {
        if (this.readyState == "complete" || this.readyState == "loaded") {
          defaultCss.cssText = "";
        }
      }
    } else {
      firstCss.ownerNode.parentNode.insertBefore(defaultCss.ownerNode, firstCss.ownerNode);
    }
  };

  O$.cleanUpPrototypeJsonIncompatibility();

  O$.addLoadEvent(function () {
    O$._loaded = true;
    O$._lowerPriorityOfDefaultCss();
    // we're repeating the O$.cleanUpPrototypeJsonIncompatibility(); call on load event to account for prototype.js
    // which might be included after util.js
    O$.cleanUpPrototypeJsonIncompatibility();
  });

  O$._submitComponentWithField = function (componentId, focusedField, additionalParams, execute) {
    var focusedFieldId = focusedField ? focusedField.id : null;
    var component = O$(componentId);
    var focusFilterField = function () {
      if (!focusedFieldId)
        return;
      var field = O$(focusedFieldId);
      if (!field)
        return;
      if (field.focus) {
        try {
          field.focus();
        } catch (e) {
          // ignore failed focus attempts
        }
        var len = field.value.length;
        var fieldTag = field.nodeName.toLowerCase();
        if (fieldTag != "select")
          O$._selectTextRange(field, len, len);
      }
    };
    O$._submitInternal(component, function () {
      setTimeout(focusFilterField, 1);
    }, additionalParams, execute);
  };
  O$._combineSubmissions = function (component, func) {
    O$._startCompoundSubmission(component);
    try {
      func();
    } finally {
      O$._finishCompoundSubmission(component);
    }
  };
  O$._startCompoundSubmission = function (component) {
    if (component._compoundSubmission) throw "O$._startCompoundSubmission has already been called for " +
            "this component (id = " + component.id + ")";
    component._compoundSubmission = {
      completionCallbacks:[],
      additionalParams:[],
      execute:[]
    };
  };

  O$._finishCompoundSubmission = function (component) {
    if (!component._compoundSubmission) throw "O$._finishCompoundSubmission: " +
            "O$._startCompoundSubmission hasn't been called for this component (id = " + component.id + ")";
    var submissionData = component._compoundSubmission;
    component._compoundSubmission = null;
    O$._submitInternal(component, function () {
      submissionData.completionCallbacks.forEach(function (callback) {
        callback();
      })
    }, submissionData.additionalParams, submissionData.execute);
  };

  O$._submitInternal = function (component, completionCallback, additionalParams, execute) {
    if (additionalParams && typeof additionalParams == "object" && !(additionalParams instanceof Array)) {
      var paramsAsArray = [];
      for (var paramName in additionalParams) {
        var paramValue = additionalParams[paramName];
        paramsAsArray.push([paramName, paramValue]);
      }
      additionalParams = paramsAsArray;
    }
    if (component._compoundSubmission) {
      if (completionCallback) {
        component._compoundSubmission.completionCallbacks.push(completionCallback);
      }
      if (additionalParams) {
        var existingParams = component._compoundSubmission.additionalParams;
        var newParams = [];
        additionalParams.forEach(function (param) {
          var paramName = param[0];
          var paramValue = param[1];
          var existingParamUpdated = false;
          existingParams.forEach(function (existingParam) {
            var existingParamName = existingParam[0];
            if (paramName == existingParamName) {
              existingParam[1] = paramValue;
              existingParamUpdated = true;
            }
          });
          if (!existingParamUpdated)
            newParams.push(param);
        });
        component._compoundSubmission.additionalParams =
                component._compoundSubmission.additionalParams.concat(newParams);
      }
      if (execute) {
        component._compoundSubmission.execute =
                component._compoundSubmission.execute.concat(execute);
      }
      return;
    }
    var useAjax = component._useAjax;
    if (!useAjax) {
      if (additionalParams)
        for (var i = 0, count = additionalParams.length; i < count; i++) {
          var paramEntry = additionalParams[i];
          O$.setHiddenField(component, paramEntry[0], paramEntry[1]);
        }
      O$.submitEnclosingForm(component);
    } else {
      O$.Ajax._reload([component.id], {
        onajaxend:completionCallback,
        params:additionalParams,
        execute:execute});
    }
  };

  O$._submitAction = function (componentId, action, actionListener) {
    var c = O$(componentId);
    if (!c)
      c = document.forms[0];
    var params = [
      [componentId, "true"],
      [O$.ACTION_COMPONENT, componentId]
    ];
    if (action)
      params.push([O$.ACTION, action]);
    if (actionListener)
      params.push([O$.ACTION_LISTENER, actionListener]);

    O$.submitWithParams(c, params);
  };

  O$._initAction = function (id, action, actionListener) {
    function initComponent() {
      var component = O$(id);
      if (!component) {
        setTimeout(function () {
          initComponent();
        }, 100);
        return;
      }
      component.run = function () {
        O$._submitAction(id, action, actionListener);
      };
    }

    if (O$(id))
      initComponent();
    else
      setTimeout(function () {
        if (O$(id))
          initComponent();
        else
          O$.addLoadEvent(function () {
            initComponent();
          });
      }, 1);
  };

  O$.initUnloadableComponent = function (component) {
    if (!component.onComponentUnload) {
      O$.addThisComponentToAllParents(component);
      component.onComponentUnload = function () {
        O$.unloadAllHandlersAndEvents(component);
      }
    }
  };

  O$.addUnloadHandler = function (component, func) {
    if (!component._unloadHandlers) {
      component._unloadHandlers = [];
    }
    component._unloadHandlers.push(func);
  };

  O$.unloadAllHandlersAndEvents = function (component) {
    var attachedEvents = component._attachedEvents;
    if (attachedEvents) {
      var length = attachedEvents.length;
      for (var eventsIndex = 0; eventsIndex < length; eventsIndex++) {
        if (attachedEvents[0] != null)
          O$.removeEventHandler(component, attachedEvents[0].eventName, attachedEvents[0].functionScript);
      }
      component._attachedEvents = [];
    }
    if (component._unloadHandlers && component._unloadHandlers != null) {
      for (var i = 0, count = component._unloadHandlers.length; i < count; i++) {
        component._unloadHandlers[i]();
      }
      component._unloadHandlers = [];
    }
  };

  O$.addThisComponentToAllParents = function (component) {
    var parent = component.parentNode;

    function isAlreadyInArray(component) {
      var parent = component.parentNode;
      if (parent != null && parent._unloadableComponents) {
        for (var i = 0; i < parent._unloadableComponents.length; i++) {
          if (parent._unloadableComponents[i] == component) {
            return true;
          }
        }
      }
      return false;
    }

    if (!isAlreadyInArray(component)) {
      while (parent != document && parent != null) {
        if (!parent._unloadableComponents) {
          parent._unloadableComponents = [];
        }
        parent._unloadableComponents.push(component);
        parent = parent.parentNode;
      }
    }
  };

  O$.removeThisComponentFromAllDocument = function (component) {
    var parent = O$.removeThisComponentFromParentsAbove(component, component.parentNode);
    if (parent != document) {
      return false;
    } else {
      return true;
    }
  };

  O$.removeThisComponentFromParentsAbove = function (component, parent) {
    while (parent != document && parent != null) {
      if (parent._unloadableComponents) {
        for (var index = 0; index < parent._unloadableComponents.length; index++) {
          if (parent._unloadableComponents[index] == component) {
            parent._unloadableComponents.splice(index, 1);
            break;
          }
        }
      }
      parent = parent.parentNode;
    }
    return parent;
  };
  O$._disabledClassLink = null;
  O$.Link = {
    _init:function (id, disabled, disabledStyle) {
      O$._disabledClassLink = disabledStyle;
      var link = O$.initComponent(id, null, {
        _disabled:false,


        getDisabled:function () {
          return this._disabled;
        },

        setDisabled:function (disabled) {
          O$.setHiddenField(this, id + "::disabled", disabled);
          if (this._disabled == disabled) return;
          this._disabled = disabled;
          O$.setStyleMappings(this, {"disabled":this._disabled ? disabledStyle : ""});
          events.forEach(function (eventName) {
            link[eventName] = disabled ? null : originalEventHandlers[eventName];
          });
          if (disabled) {
            link.onclick = O$.preventDefaultEvent;
          } else if (link.onclick == O$.preventDefaultEvent) {
            link.onclick = null;
          }
        }

      });

      var events = ["onfocus", "onblur", "onkeydown", "onkeypress", "onkeyup",
        "onmouseover", "onmousemove", "onmousedown", "onclick", "ondblclick", "onmouseup", "onmouseout"];
      var originalEventHandlers = {};
      events.forEach(function (eventName) {
        originalEventHandlers[eventName] = link[eventName];

      });

      link.setDisabled(disabled);
    }

  };

  O$.isComponentInDOM = function (component) {
    var parent = component.parentNode;
    while (parent != document && parent != null) {
      parent = parent.parentNode;
    }
    if (parent == document) {
      return true;
    } else {
      return false;
    }
  };

}


