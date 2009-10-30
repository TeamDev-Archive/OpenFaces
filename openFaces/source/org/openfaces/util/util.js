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

if (!window.O$) {
  window.OpenFaces = window.O$ = function(id) {
    return document.getElementById(id);
  };

  O$.extend = function(obj, withObj) {
    for (propertyName in withObj) {
      if (propertyName != "prototype")
        obj[propertyName] = withObj[propertyName];
    }
  };

  O$.createClass = function(classMembers, instanceMembers) {
    var constructor = instanceMembers.constructor;
    instanceMembers.constructor = undefined;
    var classFunction = function() {
      if (constructor)
        constructor.apply(this, arguments);
    };
    if (classMembers)
      O$.extend(classFunction, classMembers);
    O$.extend(classFunction.prototype, instanceMembers);
    return classFunction;
  };

  O$.extend(O$, {
    DEBUG: true,

    byIdOrName: function(idOrName) {
      var el = O$(idOrName);
      if (!el)
        el = document.getElementsByName(idOrName)[0];
      return el;
    }
  });


  O$.initComponent = function(clientId, styles, events) {
    var component = O$(clientId);
    if (!component)
      throw "O$.initComponent: couldn't find component by id: " + clientId;

    if (styles) {
      if (styles.rollover)
        O$.setupHoverStateFunction(component, function(mouseInside) {
          O$.setStyleMappings(component, {_rolloverStyle: mouseInside ? styles.rollover : null});
        });
    }
    if (events) {
      component._events = {};
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
        component._events[eventName] = handlerFunction;
      }
    }
    return component;
  };

  O$.extend(O$, { /* Rectangle class */
    Rectangle: O$.createClass(null, {
      constructor: function (x, y, width, height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
      },
      clone: function() {
        return new O$.Rectangle(this.x, this.y, this.width, this.height);
      },
      getMinX: function() {
        return this.x;
      },
      getMinY: function() {
        return this.y;
      },
      getMaxX: function() {
        return this.x + this.width;
      },
      getMaxY: function() {
        return this.y + this.height;
      },

      addRectangle: function(rect) {
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

      intersectWith: function(rect) {
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

      intersects: function(rect) {
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

      containsRectangle: function(rect) {
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

      containsPoint: function(x, y) {
        return x >= this.getMinX() && x <= this.getMaxX() &&
               y >= this.getMinY() && y <= this.getMaxY();
      }

    })
  });

  /* GraphicLine class -- displays a horizontal or vertical line on the specified element. */
  O$.GraphicLine = O$.createClass({
    ALIGN_BY_TOP_OR_LEFT: "alignByTopOrLeft",
    ALIGN_BY_CENTER: "alignByCenter",
    ALIGN_BY_BOTTOM_OR_RIGHT: "alignByBottomOrRight"
  }, {
    constructor: function (lineStyle, alignment, x1, y1, x2, y2) {
      this._element = document.createElement("div");
      this._element.style.visibility = "hidden";
      this._element.style.position = "absolute";

      if (x1)
        this.setLine(x1, y1, x2, y2, true);
      if (!lineStyle)
        lineStyle = "1px solid black";
      this.setLineStyle(lineStyle, true);
      if (!alignment)
        alignment = O$.GraphicLine.ALIGN_BY_CENTER;
      this.setAlignment(alignment, false);
    },

    setLine: function(x1, y1, x2, y2, dontUpdateNow) {
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

    setLineStyle: function(lineStyle, dontUpdateNow) {
      this.lineStyle = lineStyle;
      if (!dontUpdateNow)
        this.updatePresentation();
    },

    setAlignment: function(alignment, dontUpdateNow) {
      this.alignment = alignment;
      if (!dontUpdateNow)
        this.updatePresentation();
    },

    updatePresentation: function() {
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
        width = O$.getNumericStyleProperty(this._element, "border-top-width");
      } else {
        this._element.style.borderLeft = this.lineStyle;
        this._element.style.borderTop = "none";
        width = O$.getNumericStyleProperty(this._element, "border-left-width");
      }
      var alignment = this.alignment;
      var alignmentCorrection =
              alignment == O$.GraphicLine.ALIGN_BY_TOP_OR_LEFT ? 0 :
              alignment == O$.GraphicLine.ALIGN_BY_CENTER || !alignment ? -width / 2 :
              alignment == O$.GraphicLine.ALIGN_BY_BOTTOM_OR_RIGHT ? -width :
              (function() {
                throw "Invalid alignment: " + alignment;
              })();
      O$.setElementBorderRectangle(this._element,
              horizontal ? new O$.Rectangle(this.x1, this.y1 + alignmentCorrection, this.x2 - this.x1, width)
                      : new O$.Rectangle(this.x1 + alignmentCorrection, this.y1, width, this.y2 - this.y1));
    },

    show: function (parentElement) {
      if (!parentElement)
        parentElement = O$.getDefaultAbsolutePositionParent();
      this._element.style.visibility = "visible";
      if (this._element.parentNode != parentElement)
        parentElement.appendChild(this._element);
      // updatePresentation here is needed for recalculating border width after an element is already in DOM
      this.updatePresentation();
    },

    hide: function () {
      this._element.style.visibility = "hidden";
    },

    remove: function () {
      this._element.parentNode.remove(this._element);
    }
  });

  /* GraphicRectangle class -- displays a rectangle on the specified element. */
  O$.GraphicRectangle = O$.createClass({
    /* the following constants deptermine the way that outline line is aligned with respect to the rectangle's edge
     * (this is especially important for thick lines) */
    ALIGN_INSIDE: "alignInside", // the entire outline line is aligned to be inside of the rectangle
    ALIGN_CENTER_LINE: "alignCenterLine", // center of the outline line is aligned with the rectangle edge
    ALIGN_OUTSIDE: "alignOutside" // the entire contents of outline line is aligned to be outside of the rectangle
  }, {
    constructor: function (lineStyle, lineAlignment, rectangle) {
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

    setRectangle: function(rectangle, dontUpdateNow) {
      this.rectangle = rectangle;
      if (!dontUpdateNow)
        this._updateRect();
    },

    setLineStyle: function(lineStyle, dontUpdateNow) {
      this.lineStyle = lineStyle;
      this._leftLine.setLineStyle(lineStyle, dontUpdateNow);
      this._rightLine.setLineStyle(lineStyle, dontUpdateNow);
      this._topLine.setLineStyle(lineStyle, dontUpdateNow);
      this._bottomLine.setLineStyle(lineStyle, dontUpdateNow);
      if (!dontUpdateNow)
        this._updateRect();
    },

    setLineAlignment: function(lineAlignment, dontUpdateNow) {
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

    _updateRect: function() {
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
              (function() {
                throw "Unknown alignment: " + alignment;
              })();

      this._leftLine.setLine(x1, y1 - cornerSpacing, x1, y2 + cornerSpacing);
      this._rightLine.setLine(x2, y1 - cornerSpacing, x2, y2 + cornerSpacing);
      this._topLine.setLine(x1 - cornerSpacing, y1, x2 + cornerSpacing, y1);
      this._bottomLine.setLine(x1 - cornerSpacing, y2, x2 + cornerSpacing, y2);
    },

    show: function (parentElement) {
      this._leftLine.show(parentElement);
      this._rightLine.show(parentElement);
      this._topLine.show(parentElement);
      this._bottomLine.show(parentElement);
    },

    hide: function () {
      this._leftLine.hide();
      this._rightLine.hide();
      this._topLine.hide();
      this._bottomLine.hide();
    },

    remove: function() {
      this._leftLine.remove();
      this._rightLine.remove();
      this._topLine.remove();
      this._bottomLine.remove();
    }
  });


  // ----------------- DEBUG ---------------------------------------------------

  O$.logError = function(message) {
    if (O$.DEBUG)
      alert("ERROR: " + message);
  };

  O$.logWarning = function(message) {
    //  if (O$.DEBUG)
    //    alert("WARNING: " + message);
  };


  O$.assert = function(value, message) {
    if (value !== null && value !== undefined && value !== false)
      return;
    O$.logError(message);
  };

  O$.assertEquals = function(expectedValue, actualValue, message) {
    if (expectedValue == actualValue)
      return;
    O$.logError((message ? message + " ; " : "") + "expected: " + expectedValue + ", but was: " + actualValue);
  };


  setTimeout(function() {
    O$._logEnabled = true;
  }, 0);
  O$.log = function(text) {
    if (!O$._logEnabled)
      return;
    if (!O$._logger)
      O$._logger = new O$.Logger();
    O$._logger.log(text);
  };

  O$.Logger = function() {
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

    this._div = div;
    //  this._win = window.open("about:blank", "O$.Logger");
    this.log = function (text) {
      var date = new Date();
      var br = document.createElement("br");
      if (this._div.childNodes.length > 0)
        this._div.insertBefore(br, this._div.childNodes[0]);
      else
        this._div.appendChild(br);
      this._div.insertBefore(document.createTextNode(text), this._div.childNodes[0]);
      this._div.insertBefore(document.createTextNode(date + " : "), this._div.childNodes[0]);
      //    this._div.innerHTML = date + " : " + text + "<br/>" + this._div.innerHTML;
    };
  };

  O$.Profiler = function() {
    this._timeStamps = [];
    this._timeAccumulators = [];

    this.logTimeStamp = function(name) {
      this._timeStamps.push({time: new Date(), name: name});
    };

    this.startMeasuring = function(name) {
      var timeAccumulator = this._timeAccumulators[name];
      if (timeAccumulator == null) {
        timeAccumulator = {name: name, secondsElapsed: 0.0, lastPeriodStartDate: null};
        this._timeAccumulators[name] = timeAccumulator;
        this._timeAccumulators.push(timeAccumulator);
      }
      O$.assert(!timeAccumulator.lastPeriodStartDate, "O$.Profiler.startMeasuring cannot be called twice for the same name without endMeasuring being called: " + name);
      timeAccumulator.lastPeriodStartDate = new Date();
    };

    this.endMeasuring = function(name) {
      var dateAfter = new Date();
      var timeAccumulator = this._timeAccumulators[name];
      O$.assert(timeAccumulator, "O$.Profiler.endMeasuring: startMeasuring wasn't called for name: " + name);
      O$.assert(timeAccumulator.lastPeriodStartDate, "O$.Profiler.endMeasuring: startMeasuring wasn't called for name: " + name);
      var dateBefore = timeAccumulator.lastPeriodStartDate;
      timeAccumulator.lastPeriodStartDate = null;
      var secondsElapsed = (dateAfter.getTime() - dateBefore.getTime()) / 1000;
      timeAccumulator.secondsElapsed += secondsElapsed;
    };

    this.showTimeStamps = function() {
      var result = "";
      for (var i = 0, count = this._timeStamps.length - 1; i < count; i++) {
        var stampBefore = this._timeStamps[i];
        var stampAfter = this._timeStamps[i + 1];
        var elapsed = (stampAfter.time.getTime() - stampBefore.time.getTime()) / 1000;
        result += stampBefore.name + " - " + stampAfter.name + " : " + elapsed + "\n";
      }
      alert(result);
    };

    this.showTimeMeasurements = function() {
      var result = "";
      for (var i = 0, count = this._timeAccumulators.length; i < count; i++) {
        var accumulator = this._timeAccumulators[i];
        var name = accumulator.name;
        var elapsed = accumulator.secondsElapsed;
        result += name + " : " + elapsed + "\n";
      }
      alert(result);
    };

    this.showAllTimings = function() {
      var result = "--- Time-stamps: --- \n\n";
      var i, count, elapsed;
      for (i = 0,count = this._timeStamps.length - 1; i < count; i++) {
        var stampBefore = this._timeStamps[i];
        var stampAfter = this._timeStamps[i + 1];
        elapsed = (stampAfter.time.getTime() - stampBefore.time.getTime()) / 1000;
        result += stampBefore.name + " - " + stampAfter.name + " : " + elapsed + "\n";
      }
      result += "\n--- Time measurements: --- \n\n";
      for (i = 0,count = this._timeAccumulators.length; i < count; i++) {
        var accumulator = this._timeAccumulators[i];
        var name = accumulator.name;
        elapsed = accumulator.secondsElapsed;
        result += name + " : " + elapsed + "\n";
      }
      alert(result);
    };

  };

  // ----------------- STRING, ARRAYS, OTHER LANGUAGE UTILITIES ---------------------------------------------------

  O$.stringsEqualIgnoreCase = function(str1, str2) {
    if (str1)
      str1 = str1.toLowerCase();
    if (str2)
      str2 = str2.toLowerCase();
    return str1 == str2;
  };

  O$.StringBuffer = function() {
    this._strings = [];
    this.append = function(value) {
      this._strings.push(value);
      return this;
    };
    this.toString = function() {
      return this._strings.join("");
    };
    this.getNextIndex = function() {
      return this._strings.length;
    };
    this.setValueAtIndex = function(index, value) {
      this._strings[index] = value;
    };
  };

  O$.ltrim = function(value) {
    var re = /\s*((\S+\s*)*)/;
    return value.replace(re, "$1");
  };

  O$.rtrim = function(value) {
    var re = /((\s*\S+)*)\s*/;
    return value.replace(re, "$1");
  };

  O$.trim = function(value) {
    if (value instanceof Array) {
      var strValue = "";
      for (var i = 0; i < value.length; i++) {
        strValue += value[i];
      }
      value = strValue;
    }
    return O$.ltrim(O$.rtrim(value));
  };

  O$.stringEndsWith = function(str, ending) {
    if (!str || !ending)
      return false;
    var endingLength = ending.length;
    var length = str.length;
    if (length < endingLength)
      return false;
    var actualEnding = str.substring(length - endingLength, length);
    return actualEnding == ending;
  };

  O$.stringStartsWith = function(str, text) {
    if (!str || !text)
      return false;
    var textLength = text.length;
    var length = str.length;
    if (length < textLength)
      return false;
    var actualStartText = str.substring(0, textLength);
    return actualStartText == text;
  };

  O$.findValueInArray = function(value, arr) {
    for (var i = 0, count = arr.length; i < count; i++) {
      var obj = arr[i];
      if (obj == value)
        return i;
    }
    return -1;
  };

  O$.getArrayFromString = function(str, delimiter) {
    var idx = str.indexOf(delimiter);
    var arr = new Array();
    var arrIdx = 0;
    while (idx != -1) {
      arr[arrIdx++] = str.substring(0, idx);
      str = str.substring(idx + 1);
      idx = str.indexOf(delimiter);
    }
    arr[arrIdx] = str;
    return arr;
  };

  O$.arrayContainsValue = function(array, value) {
    var idx = O$.findValueInArray(value, array);
    return idx != -1;
  };

  O$.unescapeHtml = function(val) {
    var re = /\&\#(\d+)\;/;
    while (re.test(val)) {
      val = val.replace(re, String.fromCharCode(RegExp.$1));
    }
    return val;
  };

  if (!Array.prototype.every) {
    Array.prototype.every = function(fun, thisp) {
      var len = this.length;
      if (typeof fun != "function")
        throw new TypeError();
      for (var i = 0; i < len; i++) {
        if (i in this && !fun.call(thisp, this[i], i, this))
          return false;
      }
      return true;
    };
  }

  O$.dateByTimeMillis = function(time) {
    var date = new Date();
    date.setTime(time);
    return date;
  };

  O$.cloneDate = function(date) {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate());
  };

  O$.cloneDateTime = function(date) {
    var clonedDate = new Date();
    clonedDate.setTime(date.getTime());
    return clonedDate;
  };

  O$.incDay = function(date, increment) {
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

  O$.minDefined = function(value1, value2) {
    if (value1 != undefined && value2 != undefined && !isNaN(value1) && !isNaN(value2))
      return Math.min(value1, value2);
    return value1 !== undefined && !isNaN(value1) ? value1 : value2;
  };

  O$.maxDefined = function(value1, value2) {
    if (value1 != undefined && value2 != undefined && !isNaN(value1) && !isNaN(value2))
      return Math.max(value1, value2);
    return value1 !== undefined && !isNaN(value1) ? value1 : value2;
  };

  O$.invokeOnce = function(func, funcId) {
    if (!O$.isInvoked(funcId)) {
      func();
      O$.getInvokedFunctions().push(funcId);
    }
  };

  O$.isInvoked = function(funcId) {
    var invokedFunctions = O$.getInvokedFunctions();
    return O$.contains(invokedFunctions, funcId);
  };

  O$.getInvokedFunctions = function() {
    var invokedFunctions = O$._invokedFunctions;
    if (!invokedFunctions) {
      invokedFunctions = [];
      O$._invokedFunctions = invokedFunctions;
    }
    return invokedFunctions;
  };

  O$.contains = function(array, object) {
    for (var i = 0, count = array.length; i < count; i++) {
      if (object == array[i])
        return true;
    }
    return false;
  };


  // ----------------- BROWSER DETECTION ---------------------------------------------------

  O$.userAgentContains = function(browserName) {
    return navigator.userAgent.toLowerCase().indexOf(browserName.toLowerCase()) > -1;
  };

  O$.isStrictMode = function() {
    return !O$.isQuirksMode();
  };
  O$.isQuirksMode = function() {
    return document.compatMode == "BackCompat";
  };

  O$.isMozillaFF = function() {
    if (O$._mozilla == undefined)
      O$._mozilla = O$.userAgentContains("mozilla") &&
                    !O$.userAgentContains("msie") &&
                    !O$.userAgentContains("safari");
    return O$._mozilla;
  };

  O$.isMozillaFF2 = function() {
    return O$.isMozillaFF() && !O$.userAgentContains("Firefox/3.0");
  };

  O$.isMozillaFF3 = function() {
    return O$.isMozillaFF() && O$.userAgentContains("Firefox/3.0");
  };

  O$.isExplorer = function() {
    if (O$._explorer == undefined)
      O$._explorer = O$.userAgentContains("msie") && !O$.userAgentContains("opera");
    return O$._explorer;
  };

  O$.isExplorer6 = function() {
    return O$.isExplorer() && !O$.isExplorer7() && !O$.isExplorer8();
  };

  O$.isExplorer7 = function() {
    if (O$._explorer7 == undefined)
      O$._explorer7 = O$.isExplorer() && O$.userAgentContains("MSIE 7");
    return O$._explorer7;
  };

  O$.isExplorer8 = function() {
    if (O$._explorer8 == undefined)
      O$._explorer8 = O$.isExplorer() && O$.userAgentContains("MSIE 8");
    return O$._explorer8;
  }

  O$.isOpera9AndLate = function() {
    if (O$._opera9 == undefined) {
      if (O$.isOpera()) {
        O$._opera9 = /\bOpera(\s|\/)(\d{2,}|([9]){1})/i.test(navigator.userAgent);
      }
    }
    return O$._opera9;
  };

  O$.isSafari3AndLate = function() {
    if (O$._safari3 == undefined)
      if (O$.isSafari()) {
        O$._safari3 = /\bversion(\s|\/)(\d{2,}|[3-9]{1})/i.test(navigator.userAgent);
      }
    return O$._safari3;
  };

  O$.isSafari4AndLate = function() {
    if (O$._safari4 == undefined)
      if (O$.isSafari()) {
        O$._safari4 = /\bversion(\s|\/)(\d{2,}|[4-9]{1})/i.test(navigator.userAgent);
      }
    return O$._safari4;
  };

  O$.isSafari3 = function() {
    if (O$._safari3only == undefined) {
      O$._safari3only = O$.isSafari3AndLate() && !O$.isSafari4AndLate();
    }
    return O$._safari3only;
  };

  O$.isSafariOnMac = function() {
    if (O$._safariOnMac == undefined) {
      O$._safariOnMac = O$.isSafari() && O$.userAgentContains("Macintosh");
    }
    return O$._safariOnMac;
  };

  O$.isSafariOnWindows = function() {
    if (O$._safariOnWindows == undefined) {
      O$._safariOnWindows = O$.isSafari() && O$.userAgentContains("Windows");
    }
    return O$._safariOnWindows;
  };

  O$.isSafari2 = function() {
    if (O$._safari2 == undefined) {
      O$._safari2 = O$.isSafari() && !O$.isSafari3AndLate() && !O$.isChrome();
    }
    return O$._safari2;
  };

  O$.isChrome = function() {
    if (O$._chrome == undefined)
      O$._chrome = O$.userAgentContains("Chrome");
    return O$._chrome;
  };

  O$.isOpera = function() {
    if (O$._opera == undefined)
      O$._opera = O$.userAgentContains("opera");
    return O$._opera;
  };

  O$.isSafari = function() {
    if (O$._safari == undefined)
      O$._safari = O$.userAgentContains("safari");
    return O$._safari;
  };

  // ----------------- DOM FUNCTIONS ---------------------------------------------------

  O$.findParentNode = function(element, tagName) {
    tagName = tagName.toUpperCase();
    while (element) {
      var elementNodeName = element.nodeName;
      if (elementNodeName)
        elementNodeName = elementNodeName.toUpperCase();
      if (elementNodeName == tagName)
        break;
      element = element.parentNode;
    }
    if (element != null)
      return element;
    else
      return null;
  };

  O$.findAnyParentNode = function(element, tagNames) {
    for (var i = 0, count = tagNames.length; i < count; i++)
      tagNames[i] = tagNames[i].toUpperCase();
    while (element && O$.findValueInArray(element.nodeName.toUpperCase(), tagNames) == -1)
      element = element.parentNode;
    if (element != null)
      return element;
    else
      return null;
  };

  O$.isChild = function(parent, child) {
    if (parent.id && child.id && parent.id == child.id) return true;
    if (child.parentNode && child.parentNode.nodeName && child.parentNode.nodeName.toUpperCase() != "BODY") {
      return O$.isChild(parent, child.parentNode);
    }
    return false;
  };

  O$.findChildNodesByClass = function(node, className, searchTopLevelOnly) {
    var result = new Array();
    var children = node.childNodes;
    for (var i = 0, count = children.length; i < count; i++) {
      var child = children[i];
      if (child.className == className)
        result.push(child);
      //    var childClass = child.className;
      //    var childClassNames = childClass ? childClass.split(" ") : new Array();
      //    if (childClassNames.O$.indexOf(className))
      var subResult = !searchTopLevelOnly && O$.findChildNodesByClass(child, className);
      for (var childIndex = 0, subResultCount = subResult.length; childIndex < subResultCount; childIndex++) {
        var innerResult = subResult[childIndex];
        result.push(innerResult);
      }
    }
    return result;
  };

  O$.getChildNodesWithNames = function(node, nodeNames) {
    var selectedChildren = [];
    var children = node.childNodes;
    for (var i = 0, count = children.length; i < count; i++) {
      var child = children[i];
      var childNodeName = child.nodeName;
      if (childNodeName)
        childNodeName = childNodeName.toLowerCase();
      for (var j = 0, jcount = nodeNames.length; j < jcount; j++) {
        var nodeName = nodeNames[j];
        nodeName = nodeName.toLowerCase();
        if (childNodeName == nodeName)
          selectedChildren.push(child);
      }
    }
    return selectedChildren;
  };

  O$.findElementByPath = function(node, childPath, ignoreNonExistingElements) {
    var separatorIndex = childPath.indexOf("/");
    var locator = separatorIndex == -1 ? childPath : childPath.substring(0, separatorIndex);
    var remainingPath = separatorIndex != -1 ? childPath.substring(separatorIndex + 1) : null;

    var bracketIndex = locator.indexOf("[");
    var childElementName = bracketIndex == -1 ? locator : locator.substring(0, bracketIndex);
    var childElementIndex;
    if (bracketIndex == -1) {
      childElementIndex = 0;
    } else {
      O$.assert(O$.stringEndsWith(locator, "]"), "O$.findElementByPath: unparsable element locator - non-matching brackets: " + childPath);
      var indexStr = locator.substring(bracketIndex + 1, locator.length - 1);
      try {
        childElementIndex = parseInt(indexStr);
      } catch (e) {
        if (ignoreNonExistingElements)
          return null;
        throw "O$.findElementByPath: Couldn't parse child index (" + indexStr + "); childPath = " + childPath;
      }
    }
    var childrenByName = O$.getChildNodesWithNames(node, [childElementName]);
    if (childrenByName.length == 0) {
      if (ignoreNonExistingElements)
        return null;
      throw "O$.findElementByPath: Couldn't find child nodes by element name: " + childElementName + " ; childPath = " + childPath;
    }
    var child = childrenByName[childElementIndex];
    if (!child) {
      if (ignoreNonExistingElements)
        return null;
      throw "O$.findElementByPath: Child not found by index: " + childElementIndex + " ; childPath = " + childPath;
    }
    if (remainingPath == null)
      return child;
    else
      return O$.findElementByPath(child, remainingPath);
  };

  O$.isElementPresentInDocument = function(element) {
    var result = false;
    if (element == document) {
      return true;
    }
    if (element.parentNode != null) {
      result = O$.isElementPresentInDocument(element.parentNode);
    }
    return result;
  };

  O$.removeAllChildNodes = function(element) {
    while (element.childNodes.length > 0)
      element.removeChild(element.childNodes[0]);
  };

  O$.setInnerText = function(element, text, escapeHtml) {
    if (escapeHtml === false) {
      element.innerHTML = text;
      return;
    }
    O$.removeAllChildNodes(element);
    element.appendChild(document.createTextNode(text));
  };

  O$.createStyledText = function(text, styleClass) {
    var container = document.createElement("span");
    container.appendChild(document.createTextNode(text));
    container.className = styleClass;
    return container;
  };

  // ----------------- FORM, FORM ELEMENTS MANIPULATION ---------------------------------------------------

  O$.submitEnclosingForm = function(element) {
    O$.assert(element, "element should be passed to O$.submitEnclosingForm");
    var frm = O$.findParentNode(element, "FORM");
    O$.assert(frm, "O$.submitEnclosingForm: Enclosing form not found for element with id: " + element.id + "; element tag name: " + element.tagName);
    if (frm.onsubmit)
      if (!frm.onsubmit())
        return;

    frm.submit();
  };

  O$.submitEnclosingElementsForm = function(elements) {
    O$.assert(elements[0], "elements should be passed to O$.submitEnclosingElementsForm");
    var frm = O$.findParentNode(elements[0], "FORM");
    if (!elements.every(function(element) {
      return (frm == O$.findParentNode(element, "FORM"));
    })) {
      O$.logError("O$.submitEnclosingElementsForm: Enclosing forms differ for components");
    }
    O$.assert(frm, "O$.submitEnclosingElementsForm: Enclosing form not found for elements");
    if (frm.onsubmit)
      if (!frm.onsubmit())
        return;

    frm.submit();
  };

  O$.submitFormWithAdditionalParams = function(element, params) {
    O$.assert(element, "element should be passed to O$.submitFormWithAdditionalParams");
    O$.assert(params, "params should be passed to O$.submitFormWithAdditionalParams");
    for (var i = 0, count = params.length; i < count; i++) {
      var param = params[i];
      O$.addHiddenField(element, param[0], param[1]);
    }

    var frm = O$.findParentNode(element, "FORM");
    frm.submit();
  };

  O$.submitFormWithAdditionalParam = function(element, paramName, paramValue) {
    O$.submitFormWithAdditionalParams(element, [
      [paramName, paramValue]
    ]);
  };

  O$.addHiddenField = function(element, fieldName, fieldValue) {
    var frm;
    if (!element) {
      frm = document.forms[0];
      O$.assert(frm, "O$.addHiddenField: There must be a form in the document");
    } else {
      frm = element._form ? element._form : O$.findParentNode(element, "FORM");
      O$.assert(frm, "O$.addHiddenField: Enclosing form not found for element with id: " + element.id + "; element tag name: " + element.tagName);
    }
    var existingField = O$(fieldName);
    var newParamField = existingField ? existingField : document.createElement("input");
    if (!existingField) {
      newParamField.type = "hidden";
      newParamField.id = fieldName;
      newParamField.name = fieldName;
      frm.appendChild(newParamField);
    }
    if (!fieldValue)
      fieldValue = "";
    newParamField.value = fieldValue;
    return newParamField;
  };

  O$.submitById = function(elementId) {
    var element = O$(elementId);
    O$.assert(element, "correct element id should be passed to O$.submitById");
    O$.submitEnclosingForm(element);
  };

  O$.submitByIds = function(elementIds) {
    var elements = new Array();
    for (var i = 0, count = elementIds.length; i < count; i++) {
      elements[elements.length] = O$(elementIds[i]);
      O$.assert(elements[elements.length - 1], "correct element id should be passed to O$.submitByIds");
    }
    O$.submitEnclosingElementsForm(elements);
  };

  O$.setValue = function(elementId, value) {
    var field = O$(elementId);
    O$.assert(field, "Correct element id should be passed to O$.setValue; elementId = " + elementId);
    if (field) {
      field.value = value;
    }
  };


  //{
  //  var submitElement = null;
  //  submitElement = O$("submit");
  //  if (submitElement == null) {
  //    for (var i = 0, count = document.forms.length; i < count; i++) {
  //      submitElement = document.forms.getElementsByName("submit");
  //      if (submitElement)
  //        break;
  //    }
  //  }
  //  if (submitElement) {
  //    O$.logError("The document contains an element with id or name equal to \"submit\". Please use different id/name in order to avoid collision with form.submit() method");
  //  }
  //}
  //
  O$._isFormSubmissionJustStated = function() {
    return O$._formSubmissionJustStarted;
  };

  // ----------------- EVENT UTILITIES ---------------------------------------------------

  O$.getEventHandlerFunction = function(handlerName, handlerArgs, mainObj) {
    var handlerFunction;

    if (!O$.getEventHandlerFunction.apply) {
      eval("handlerFunction = function() { return arguments.callee.prototype._ownObj." + handlerName + "(" + (handlerArgs ? handlerArgs : "") + "); }");
    } else {
      var argString = handlerArgs ? ("[" + handlerArgs + "]") : "arguments";
      eval("handlerFunction = function() { return arguments.callee.prototype._ownObj." + handlerName + ".apply(arguments.callee.prototype._ownObj," + argString + "); }");
    }
    handlerFunction.prototype._ownObj = mainObj;
    return handlerFunction;
  };

  O$.addEvent = function(componentClientId, eventName, functionScript) {
    if (!O$._attachedEvents) {
      O$._attachedEvents = [];
    }

    var element = O$.byIdOrName(componentClientId);
    if (element) {
      O$.addEventHandler(element, eventName, functionScript);
    }

    var eventToDetach = new Object();
    eventToDetach.componentClientId = componentClientId;
    eventToDetach.eventName = eventName;
    eventToDetach.functionScript = functionScript;

    if (!O$._attachedEvents[componentClientId]) {
      O$._attachedEvents[componentClientId] = [];
    }

    var componentAttachedEvents = O$._attachedEvents[componentClientId];
    componentAttachedEvents.push(eventToDetach);
    O$._attachedEvents[componentClientId] = componentAttachedEvents;
  };

  O$.addEventHandler = function(elt, evtName, evtScript, useCapture) {
    if (elt.addEventListener) {
      elt.addEventListener(evtName, evtScript, !!useCapture);
    } else if (elt.attachEvent) {
      elt.attachEvent("on" + evtName, evtScript);
    }
  };

  O$.removeEventHandler = function(elt, evtName, evtScript, useCapture) {
    if (elt.addEventListener) {
      elt.removeEventListener(evtName, evtScript, !!useCapture);
    } else if (elt.attachEvent) {
      elt.detachEvent("on" + evtName, evtScript);
    }
  };

  /* Use this function instead of direct field assignment to work around Mozilla problems firing old events when
   new page is loading (JSFC-2276) */
  O$.assignEventHandlerField = function(element, fieldName, handler) {
    element[fieldName] = handler;
    if (O$.isMozillaFF()) {
      O$.addUnloadEvent(function() {
        element[fieldName] = null;
      });
    }
  };

  O$.assignEvents = function(element, eventsContainer, useEventFields, additionalEventProperties) {
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
          return function(event) {
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

  O$.repeatClickOnDblclick = function(e) {
    if (O$.isExplorer() && this.onclick) this.onclick(e);
    if (O$.isExplorer() && this.onmousedown) this.onmousedown(e);
  };

  O$.initDocumentMouseClickListeners = function() {
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
   * The last "receiverThisRef" parameter can be omitted if it's acceptable that "this" variable in the
   * event handler refer to eventSource.
   */
  O$.addEventHandlerSimple = function(eventSource, eventName, handlerFunctionName, receiverThisRef) {
    var handler = O$.getEventHandlerFunction(handlerFunctionName, null, receiverThisRef ? receiverThisRef : eventSource);
    O$.addEventHandler(eventSource, eventName, handler);
  };

  /*
   * eventName should be without the "on" prefix, for example: "change", "click", etc.
   */
  O$.createEvent = function(eventName) {
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
  O$.sendEvent = function(object, eventName) {
    var safari = O$.isSafari();
    var e = safari ? {} :
            document.createEvent ? document.createEvent("Event") :
            document.createEventObject();
    if (document.createEvent && !safari) {
      e.initEvent(eventName, true, true);
      e._of_event = true;
      e.returnValue = true;
      object.dispatchEvent(e);
      return e.returnValue;
    } else {
      e.name = "on" + eventName;
      e._of_event = true;
      var handler = object[e.name];
      if (!handler)
        return true;
      return object[e.name](e);
      //    object.fireEvent(e.name, e); // - didn't work for firing "onchange" for <table>
    }

  };


  O$.cancelBubble = function(evt) {
    var e = evt ? evt : window.event;
    e.cancelBubble = true;
  };

  O$.isAltPressed = function(event) {
    if (event == null || event.altKey == null)
      return false;
    return event.altKey;
  };
  //

  O$.isCtrlPressed = function(event) {
    if (event == null || event.ctrlKey == null)
      return false;
    return event.ctrlKey;
  };
  //

  O$.isShiftPressed = function(event) {
    if (event == null || event.shiftKey == null)
      return false;
    return event.shiftKey;
  };

  O$.getEvent = function(e) {
    return e ? e : event;
  };

  O$.breakEvent = function(e) {
    O$.stopEvent(e);
    O$.preventDefaultEvent(e);
  };

  O$.preventDefaultEvent = function(e) {
    var evt = O$.getEvent(e);

    if (evt.preventDefault) {
      evt.preventDefault();
    }
    evt.returnValue = false;
  };

  O$.stopEvent = function(e) {
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
  O$.getEventPoint = function(e, forElement) {
    var evt = O$.getEvent(e);
    var pageScrollPos = O$.getPageScrollPos();
    var pos = {x: evt.clientX + pageScrollPos.x, y: evt.clientY + pageScrollPos.y};

    var container = forElement ? O$.getContainingBlock(forElement, true) : null;
    if (container) {
      var containerPos = O$.getElementBorderRectangle(container);
      pos.x -= containerPos.x;
      pos.y -= containerPos.y;
    }

    return pos;
  };

  O$.addInternalLoadEvent = function(func) {
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


  O$.addLoadEvent = function(func) {
    if (O$._documentLoaded) {
      func();
    } else {
      if (!O$._loadHandlers)
        O$._loadHandlers = [];
      O$._loadHandlers.push(func);
    }
  };

  O$.addEventHandler(window, "load", function() {
    O$._documentLoaded = true;
    var i, count;
    if (O$._internalLoadHandlers)
      for (i = 0,count = O$._internalLoadHandlers.length; i < count; i++) {
        var internalLoadHandler = O$._internalLoadHandlers[i];
        internalLoadHandler();
      }
    if (O$._loadHandlers)
      for (i = 0,count = O$._loadHandlers.length; i < count; i++) {
        var loadHandler = O$._loadHandlers[i];
        loadHandler();
      }
  });

  O$.addUnloadEvent = function(func) {
    var invokeOnUnloadHandlersFunction = function() {
      for (var i = 0, count = O$._onUnloadEvents.length; i < count; i++) {
        var onUnloadHandler = O$._onUnloadEvents[i];
        onUnloadHandler();
      }
    };

    if (!O$._onUnloadEvents) {
      O$._onUnloadEvents = [];

      var oldonunload = window.onunload;
      if (typeof window.onunload != "function") {
        window.onunload = function() {
          invokeOnUnloadHandlersFunction();
        };
      } else {
        window.onunload = function() {
          oldonunload();
          invokeOnUnloadHandlersFunction();
        };
      }
    }

    O$._onUnloadEvents.push(func);
  };


  O$.isLoadedFullPage = function() {
    return O$._documentLoaded;
  };

  O$.createHiddenFocusElement = function(tabindex, componentId) {
    var createTextArea = true;
    var focusControl = document.createElement(createTextArea ? "textarea" : "input");
    if (!createTextArea)
      focusControl.type = "button";
    focusControl.className = "o_hiddenFocus";
    if (O$.isSafari()) {
      focusControl.style.border = "1px solid transparent !important";
    }
    if (tabindex)
      focusControl.tabIndex = tabindex;
    if (componentId)
      focusControl.id = componentId + ":::focus";
    return focusControl;
  };

  O$.initDefaultScrollPosition = function(trackerFieldId, scrollPos) {
    O$.addHiddenField(null, trackerFieldId, scrollPos);
    O$.initScrollPosition_(trackerFieldId, true, 1, null);
  };

  O$.initScrollPosition = function(scrollPosFieldId, autoSaveScrollPos, scrollableComponentId) {
    O$.initScrollPosition_(scrollPosFieldId, autoSaveScrollPos, 2, scrollableComponentId);
  };

  O$.initScrollPosition_ = function(scrollPosFieldId, autoSaveScrollPos, priority, scrollableComponentId) {
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

    targetComponent._of_scrollPosTrackingParams = {fieldId: scrollPosFieldId, autoSave: autoSaveScrollPos,
      priority: priority, scrollableId:scrollableComponentId};

    O$.addLoadEvent(function() {
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

      O$.addEventHandler(targetComponent, "scroll", function() {
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

  O$.saveScrollPositionIfNeeded = function() {
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

  O$.retoreScrollPositionIfNeeded = function() {
    var isMozilla = O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/;
    var isScrollPositionTrackingEnabled = (document._of_scrollPositionField);

    // scroll to previous scrollPosition
    if (isMozilla && isScrollPositionTrackingEnabled) {
      var scrollPos = document._of_scrollPositionBeforeAjaxRequest;
      O$.scrollToPosition(window, scrollPos);
    }
  };

  O$.scrollToPosition = function(scrollableComponent, scrollPos) {
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
        setTimeout(function() {
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
        setTimeout(function() {
          scrollableComponent.scrollLeft = x;
          scrollableComponent.scrollTop = y;
        }, 10);
      }
    }
  };

  O$.initDefaultFocus = function(trackerFieldId, focusedComponentId) {
    O$.addHiddenField(null, trackerFieldId, focusedComponentId);
    O$.initFocus_(trackerFieldId, true, 1);
  };

  O$.initFocus = function(trackerFieldId, autoSaveFocus) {
    O$.initFocus_(trackerFieldId, autoSaveFocus, 2);
  };

  O$.initFocus_ = function(trackerFieldId, autoSaveFocus, priority) {
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
        if (c && c.focus) {
          try {
            c.focus();
            var rect = O$.getElementBorderRectangle(c);
            O$.scrollRectIntoView(rect);
          } catch(ex) {
          }
          O$._activeElement = c;
          focused = true;
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
    O$.addLoadEvent(function() {
      if (O$.Ajax) {
        var prevAjaxEnd = O$.Ajax.onajaxend;
        O$.Ajax.onajaxend = function() {
          if (prevAjaxEnd)
            prevAjaxEnd();
          O$._autoSavingFocusInitialized = false;
          setupFocus();
        };
      }
    });
  };

  O$._handleOnFocus = function(e) {
    O$._activeElement = this;
    O$._focusField.value = this.id;
    if (this._of_prevOnFocusHandler)
      this._of_prevOnFocusHandler(e);
    if (O$.onfocuschange)
      O$.onfocuschange(e);
  };

  O$.setupFocusOnTags = function(parent, tagName) {
    var elements = parent.getElementsByTagName(tagName);
    for (var i = 0; i < elements.length; i++) {
      var element = elements[i];

      if (element.onfocus == O$._handleOnFocus)
        continue;
      element._of_prevOnFocusHandler = element.onfocus;
      element.onfocus = O$._handleOnFocus;
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

  O$.addLoadEvent(function() {
    for (var i = 0, count = document.forms.length; i < count; i++) {
      var frm = document.forms[i];
      O$.addEventHandler(frm, "submit", function() {
        if (!this.target || this.target != "_blank") {
          O$._formSubmissionJustStarted = true;
          // _formSubmissionJustStarted should be reset so as not to block further ajax actions if this is not actually
          // a normal form submission, but file download (JSFC-2940)
          setTimeout(function() {
            O$._formSubmissionJustStarted = false;
          }, 100);
        }
      });
      if (frm._of_prevSubmit) continue;
      frm._of_prevSubmit = frm.submit;
      frm.submit = function() {
        if (!this.target || this.target != "_blank") {
          O$._formSubmissionJustStarted = true;
          setTimeout(function() {
            O$._formSubmissionJustStarted = false;
          }, 100);
        }
        this._of_prevSubmit();
      };
    }

  });

  O$.initMouseListenerUtils = function() {
    if (O$._mouseListenerUtilsInitialized)
      return;
    O$._mouseListenerUtilsInitialized = true;
    document._of_elementUnderMouse = null;
    document._of_prevMouseMove = document.onmousemove;
    document.onmousemove = function(e) {
      var result = undefined;
      if (document._of_prevMouseMove) {
        result = document._of_prevMouseMove(e);
      }

      var element;
      var evt = O$.getEvent(e);
      var elementList = new Array();
      if (document._of_elementUnderMouse) {
        for (element = document._of_elementUnderMouse; element; element = element.parentNode) {
          element._of_mouseInside = false;
          element._of_fireMouseOut = true;
          elementList.push(element);
        }
      }
      document._of_elementUnderMouse = evt.target ? evt.target : evt.srcElement;
      if (document._of_elementUnderMouse) {
        for (element = document._of_elementUnderMouse; element; element = element.parentNode) {
          element._of_mouseInside = true;
          element._of_fireMouseOver = true;
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
            for (listenerIndex = 0,listenerCount = el._of_mouseOutListeners.length; listenerIndex < listenerCount; listenerIndex++) {
              listener = el._of_mouseOutListeners[listenerIndex];
              listener(e);
            }
          el._of_fireMouseOut = undefined;
        }
        if (el._of_fireMouseOver) {
          if (el._of_mouseOverListeners)
            for (listenerIndex = 0,listenerCount = el._of_mouseOverListeners.length; listenerIndex < listenerCount; listenerIndex++) {
              listener = el._of_mouseOverListeners[listenerIndex];
              listener(e);
            }
          el._of_fireMouseOver = undefined;
        }
      }

      return result;
    };
  };

  O$.waitForCondition = function(conditionFunction, func, conditionCheckInterval) {
    if (!conditionCheckInterval)
      conditionCheckInterval = 50;
    var intervalId = setInterval(function() {
      if (!conditionFunction())
        return;
      clearInterval(intervalId);
      func();
    }, conditionCheckInterval);
  };

  O$.invokeFunctionAfterDelay = function(func, delay, actionId) {
    // Invokes the specified function after the specified delay in milliseconds. If another invocation request comes for
    // the same function during the delay, then the invocation is postponed until that new invocation delay expires, etc.
    // thus making only one call in case of multiple frequent invocations.
    // actionId parameter can obtionally be specified as a string that allows several "postponable" actions for the same
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
    actionReference._timeoutIds.push(setTimeout(function() {
      actionReference._delayedInvocationCount--;
      if (actionReference._delayedInvocationCount == 0) {
        actionReference._timeoutIds = [];
        func();
      }
    }, delay));
  };

  O$.cancelDelayedAction = function(func, actionId) {
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

  O$.setupHoverAndPressStateFunction = function(element, fn) {
    var state = {
      mouseInside: false,
      pressed: false,
      reset: function() {
        this.mouseInside = false;
        this.pressed = false;
        this._update();
      },
      _update: function() {
        fn(this.mouseInside, this.pressed);
      }
    };
    O$.setupHoverStateFunction(element, function(mouseInside) {
      state.mouseInside = mouseInside;
      if (!mouseInside)
        state.pressed = false;
      state._update();

    });
    O$.setupMousePressedStateFunction(element, function(pressed) {
      state.pressed = pressed;
      state._update();
    });
    return state;
  };

  O$.setupHoverStateFunction = function(element, fn) {
    O$.addMouseOverListener(element, function() {
      fn(true, element);
    });
    O$.addMouseOutListener(element, function() {
      fn(false, element);
    });
  };

  O$.setupMousePressedStateFunction = function(element, fn) {
    O$.addEventHandler(element, "mousedown", function() {
      fn(true, element);
    });
    O$.addEventHandler(element, "mouseup", function() {
      fn(false, element);
    });
  };

  O$.addMouseOverListener = function(element, listener) {
    if (O$.isExplorer()) {
      element.onmouseenter = listener;
      return;
    }
    O$.initMouseListenerUtils();
    if (!element._of_mouseOverListeners)
      element._of_mouseOverListeners = new Array();
    element._of_mouseOverListeners.push(listener);
  };

  O$.addMouseOutListener = function(element, listener) {
    if (O$.isExplorer()) {
      element.onmouseleave = listener;
      return;
    }
    O$.initMouseListenerUtils();
    if (!element._of_mouseOutListeners)
      element._of_mouseOutListeners = new Array();
    element._of_mouseOutListeners.push(listener);
  };

  O$.isEventFromInsideOfElement = function(e, element) {
    var evt = O$.getEvent(e);
    var eventTarget = evt.target ? evt.target : evt.srcElement;
    for (var currElement = eventTarget; currElement; currElement = currElement.parentNode) {
      if (currElement == element)
        return true;
    }
    return false;
  };

  O$.disabledContextMenuFor = function(element) {
    if (element) {
      element._oldoncontextmenu = element.oncontextmenu;
      element.oncontextmenu = function() {
        if (element._oldoncontextmenu) element._oldoncontextmenu();
        return false;
      };
      element._isDisabledContextMenu = true;
    }
  };

  O$.enabledContextMenuFor = function(element) {
    if (element) {
      if (element._oldoncontextmenu)
        element.oncontextmenu = element._oldoncontextmenu;
      else element.oncontextmenu = function() {
        return true;
      };
      element._isDisabledContextMenu = false;
    }
  };


  O$.getTargetComponentHasOwnMouseBehavior = function(evt) {
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
            tagName == "a";
    if (!elementHasItsOwnMouseBehavior) {
      elementHasItsOwnMouseBehavior = function(elem) {
        while (elem) {
          if (elem._hasOwnItsOwnMouseBehavior) {
            return true;
          }
          elem = elem.parentNode;
        }
        return false;
      }(element);
    }
    return elementHasItsOwnMouseBehavior;
  };

  O$.startDragAndDrop = function(e, draggable, simulateDblClickForElement) {
    var evt = O$.getEvent(e);
    if (simulateDblClickForElement && evt.type == "mousedown") {
      var thisTime = new Date().getTime();
      if (draggable._lastClickTime && thisTime - draggable._lastClickTime < 400) {
        // allow double-click despite being disabled with O$.breakEvent by this function in specified browsers
        if (simulateDblClickForElement.ondblclick && (O$.isMozillaFF() || O$.isSafari() || O$.isChrome()))
          simulateDblClickForElement.ondblclick(evt);
      }
      draggable._lastClickTime = thisTime;
    }

    if (O$.getTargetComponentHasOwnMouseBehavior(evt))
      return; // don't drag native components to avoid unwanted effects (see JSFC-2347 and all related requests)

    O$.addEventHandler(document, "mousemove", O$.handleDragMove, true);
    O$.addEventHandler(document, "mouseup", O$.handleDragEnd, true);
    draggable._draggingInProgress = true;
    draggable._draggingWasStarted = false;

    var pos = O$.getEventPoint(evt, draggable);
    draggable._lastDragX = pos.x;
    draggable._lastDragY = pos.y;
    if (!draggable._getPositionLeft)
      draggable._getPositionLeft = function() {
        return this.offsetLeft;
      };
    if (!draggable._getPositionTop)
      draggable._getPositionTop = function() {
        return this.offsetTop;
      };
    draggable._lastDragOffsetLeft = draggable._getPositionLeft();
    draggable._lastDragOffsetTop = draggable._getPositionTop();

    O$._draggedElement = draggable;
    O$.breakEvent(evt);
    if (document._fireDocumentClicked)
      document._fireDocumentClicked(evt);
  };


  O$.handleDragMove = function(e) {
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
    if (draggable.setPosition) {
      draggable.setPosition(newLeft, newTop, dx, dy);
    } else {
      draggable.setLeft(newLeft, dx);
      draggable.setTop(newTop, dy);
    }
    var offsetLeftAfterDragging = draggable._getPositionLeft();
    var offsetTopAfterDragging = draggable._getPositionTop();
    dragX -= newLeft - offsetLeftAfterDragging;
    dragY -= newTop - offsetTopAfterDragging;

    draggable._lastDragX = dragX;
    draggable._lastDragY = dragY;
    draggable._lastDragOffsetLeft = offsetLeftAfterDragging;
    draggable._lastDragOffsetTop = offsetTopAfterDragging;

    O$.breakEvent(evt);
  };

  O$.handleDragEnd = function(e) {
    var evt = O$.getEvent(e);

    var draggable = O$._draggedElement;
    if (draggable._draggingWasStarted) {
      draggable._draggingWasStarted = undefined;
      if (draggable.ondragend)
        draggable.ondragend(evt);
    }

    O$.removeEventHandler(document, "mousemove", O$.handleDragMove, true);
    O$.removeEventHandler(document, "mouseup", O$.handleDragEnd, true);
    if (!draggable._onmouseup) {
      O$.breakEvent(evt);
    }
    draggable._draggingInProgress = false;
  };

  /**
   * Avoids IE issue where mouse events on an absolute element without backgroud and content "leak" to the underlying layer
   * instead of being processed by the element itself.
   */
  O$.fixIEEventsForTransparentLayer = function(layerElement) {
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
      O$.addEventHandler(transparentLayer, "mouseup", function() {
        O$.sendEvent(document, "mouseup");
      }, false);

      O$.addEventHandler(transparentLayer, "mousemove", function() {
        O$.sendEvent(document, "mousemove");
      }, false);
    }
  };

  O$._disableIframeFix = function () {
    if (document._of_transparentLayer) {
      document._of_transparentLayer.style.display = "none";
    }
  };

  O$._simulateFixedPosForBlockingLayer = function() {
    return O$.isExplorer() /* ie doesn't support fixed pos */ ||
           O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/ /* mozilla's blocking layer hides cursor of text-field in fixed-pos popup-layer (JSFC-1930) */;
  };


  O$.showFocusOutline = function(component, outlineStyleClass) {
    if (!outlineStyleClass)
      return;

    if (!component._focusOutline) {
      var outline = new O$.GraphicRectangle("2px solid blue", O$.GraphicRectangle.ALIGN_OUTSIDE);
      outline._control = component;
      outline._updatePosition = function() {
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

  O$.hideFocusOutline = function(component) {
    if (!component._focusOutline)
      return;
    component._focusOutline.hide();
  };

  O$._tableBlurCounter = 0;

  O$.isControlFocusable = function(control) {
    if (!control)
      return false;

    if (control._focusable)
      return true;
    var tagName = control.tagName;
    if (!tagName)
      return false;
    tagName = tagName.toLowerCase();
    return (tagName == "input" && control.type != "hidden") ||
           tagName == "select" ||
           tagName == "textarea" ||
           tagName == "button" ||
           tagName == "a" ||
           (tagName == "span" && O$.checkClassNameUsed(control, "rich-inplace-select")) ||
           (tagName == "div" && O$.checkClassNameUsed(control, "rich-inplace"));
  };

  O$.setupArtificialFocus = function(component, focusedClassName, tabindex) {
    component._focused = false;
    component._updateOutline = function() {
      if (this._outlineUpdateBlocked)
        return;
      O$.setStyleMappings(this, {
        focused: this._focused ? focusedClassName : null
      });

      if (this._focused)
        O$.showFocusOutline(this, null);
      else
        O$.hideFocusOutline(this);
    };

    component._blockOutlineUpdate = function() {
      this._outlineUpdateBlocked = true;
      this._focusedBeforeBlocking = this._focused;
    };

    component._unblockOutlineUpdate = function () {
      if (!O$._tableBlurCounter)
        O$._tableBlurCounter = 0;
      if (!O$.isMozillaFF()) {
        setTimeout(function() {
          component._doUnblockOutlineUpdate();
        }, 1);
      } else {
        component._doUnblockOutlineUpdate();
      }
    };
    component._doUnblockOutlineUpdate = function() {
      this._outlineUpdateBlocked = false;
      if (this._focusedBeforeBlocking != null && this._focusedBeforeBlocking != this._focused) {
        this._focusedBeforeBlocking = null;
        if (this._focused) {
          if (this._prevOnfocusHandler_af)
            this._prevOnfocusHandler_af(null);
        } else {
          if (this._prevOnblurHandler_af)
            this._prevOnblurHandler_af(null);
        }
      }
      this._updateOutline();
    };

    component._prevOnfocusHandler_af = component.onfocus;
    component._prevOnblurHandler_af = component.onblur;

    component.onfocus = function(evt) {
      if (this._submitting)
        return;
      this._focused = true;
      if (this._prevOnfocusHandler_af && !this._outlineUpdateBlocked)
        this._prevOnfocusHandler_af(evt);

      component._updateOutline();
    };
    component.onblur = function(evt) {
      if (this._submitting)
        return;
      this._focused = false;
      if (this._prevOnblurHandler_af && !this._outlineUpdateBlocked)
        this._prevOnblurHandler_af(evt);
      if (!O$.isMozillaFF()) {
        setTimeout(function() {
          component._updateOutline();
        }, 1);
      } else {
        component._updateOutline();
      }
    };

    var focusControl = O$.createHiddenFocusElement(tabindex, component.id);

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
    focusControl.onfocus = function(evt) {
      this._prevStatusText = window.status;
      window.status = "";
      return fireEvent(this._destComponent, "onfocus", evt);
    };
    focusControl.onblur = function(evt) {
      window.status = this._prevStatusText;
      return fireEvent(this._destComponent, "onblur", evt);
    };
    focusControl.onkeydown = function(evt) {
      return fireEvent(this._destComponent, "onkeydown", evt);
    };
    focusControl.onkeyup = function(evt) {
      return fireEvent(this._destComponent, "onkeyup", evt);
    };
    focusControl.onkeypress = function(evt) {
      return fireEvent(this._destComponent, "onkeypress", evt);
    };


    component._focusControl = focusControl;

    component._focusOnClick = function(evt) {
      if (window.getSelection) {
        if (window.getSelection() != "")
          return; // don't switch focus to make text selection possible under FF (JSFC-1134)
      }
      var e = evt ? evt : event;
      if (this._focused)
        return;

      var target = (e != null)
              ? (e.target ? e.target : e.srcElement)
              : null;
      if (target.id && target.id == this.id)
        return;
      if (O$.isControlFocusable(target))
        return;
      this._preventPageScrolling = true;
      this.focus();
      this._preventPageScrolling = false;
    };

    component._focusable = true;
    component.focus = function() {
      if (this._preventPageScrolling) {
        var pageScrollPos = O$.getPageScrollPos();
        this._focusControl.style.left = pageScrollPos.x + "px";
        this._focusControl.style.top = pageScrollPos.y + "px";
      } else {
        this._focusControl.style.left = "";
        this._focusControl.style.top = "";
      }
      try {
        this._focusControl.focus();
      } catch(e) {
        //in IE hidden element can't receive focus
      }
    };

    component.blur = function() {
      this._focusControl.blur();
    };

    O$.addEventHandlerSimple(component, "click", "_focusOnClick");
    O$.addEventHandlerSimple(component, "mousedown", "_blockOutlineUpdate");
    O$.addEventHandlerSimple(component, "mouseup", "_unblockOutlineUpdate");
    O$.addEventHandlerSimple(component, "mouseout", "_unblockOutlineUpdate");

    component.parentNode.insertBefore(focusControl, component);

    var oldUnloadHandler = component.onComponentUnload;
    component.onComponentUnload = function() {
      if (oldUnloadHandler)
        oldUnloadHandler();
      if (component._focusOutline)
        component._focusOutline.remove();
    };
  };

  // ----------------- STYLE UTILITIES ---------------------------------------------------

  O$.addCssRules = function(ruleArray) {
    for (var i = 0, count = ruleArray.length; i < count; i++) {
      var rule = ruleArray[i];
      O$.addCssRule(rule);
    }
  };

  O$.getLocalStyleSheet = function() {
    var styleSheets = document.styleSheets;
    if (!styleSheets)
      return null;

    if (document._of_localStyleSheet) {
      if (styleSheets)
        for (var i = 0, count = styleSheets.length; i < count; i++) {
          var ss = styleSheets[i];
          if (ss == document._of_localStyleSheet)
            return ss;
        }
      // previously located local style sheet may have been removed if it resided inside of a component that was
      // reloaded with Ajax, so we must relocate it
      document._of_localStyleSheet = null;
    }

    function locateLocalStyleSheet() {
      var documentLocation = document.location;
      for (var i = styleSheets.length - 1; i >= 0; i--) {
        var styleSheet = styleSheets[i];
        if (styleSheet.href == documentLocation || /* Mozilla Firefox */
            !styleSheet.href /* other browsers */) {
          document._of_localStyleSheet = styleSheet;
          break;
        }
      }
    }

    if (document.createStyleSheet) {
      document._of_localStyleSheet = document.createStyleSheet();
      if (document._of_localStyleSheet)
        return document._of_localStyleSheet;
    } else {
      var styleElement = document.createElement("style");
      var headTags = document.getElementsByTagName("head");
      var styleParent = headTags.length > 0 ? headTags[0] : document.getElementsByTagName("body")[0];
      styleParent.appendChild(styleElement);
    }
    locateLocalStyleSheet();
    if (!document._of_localStyleSheet) {
      if (styleSheets.length > 0) {
        O$.logWarning(document._of_localStyleSheet, "O$.getLocalStyleSheet: couldn't find or create local style-sheet in this document, using the first available one.");
        document._of_localStyleSheet = styleSheets[0];
        // relative url paths in background won't work in Mozilla in this case
      } else {
        O$.logWarning(document._of_localStyleSheet, "O$.getLocalStyleSheet: no style-sheets could be found or created in this document");
      }
    }
    return document._of_localStyleSheet;
  };

  O$.addCssRule = function(strRule) {
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
    } catch (e) {
      O$.logError("O$.addCssRule throw an exception " + (e ? e.message : e) +
                  "; tried to add the following rule: " + strRule);
      throw e;
    }

  };

  O$._dynamicRulesCreated = 0;
  O$.createCssClass = function(declaration, disableCaching) {
    if (!disableCaching) {
      if (!document._cachedDynamicCssRules)
        document._cachedDynamicCssRules = {};
      var cachedClassName = document._cachedDynamicCssRules[declaration];
      if (cachedClassName)
        return;
    }
    var className = "of_dynamicRule" + O$._dynamicRulesCreated++;
    var ruleText = "." + className + " {" + declaration + "}";
    O$.addCssRule(ruleText);
    if (!disableCaching) {
      document._cachedDynamicCssRules[declaration] = className;
    }
    return className;
  };

  O$.findCssRule = function(selector) {
    var rules = O$.findCssRules([selector]);
    if (rules === undefined)
      return undefined;
    if (rules.length == 0)
      return null;
    return rules[0];
  };

  /**
   * Searches for CSS rules with the specified selectors, and returns an array of CSSStyleRule (or CSSRule) objects
   * in order of their occurence in DOM
   */
  O$.findCssRules = function(selectors) {
    var styleSheets = document.styleSheets;
    if (!styleSheets)
      return undefined;

    var selectorCount = selectors.length;
    if (selectorCount == 0)
      return [];

    for (var i = 0; i < selectorCount; i++)
      selectors[i] = selectors[i].toLowerCase();

    var rulesFound = new Array();
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
        if (O$.findValueInArray(selectorName, selectors) != -1)
          rulesFound.push(rule);
      }
    }
    return rulesFound;
  };

  O$.getStyleClassProperty = function(styleClass, propertyName) {
    var propertyValues = O$.getStyleClassProperties(styleClass, [propertyName]);
    return propertyValues[propertyName];
  };

  O$.getStyleClassProperties = function(styleClass, propertyNames) {
    if (!styleClass || propertyNames.length == 0)
      return {};
    var classNames = styleClass.split(" ");
    var classSelectors = new Array();
    var i, count;
    for (i = 0,count = classNames.length; i < count; i++) {
      var className = classNames[i];
      if (className)
        classSelectors.push("." + className);
    }
    var cssRules = O$.findCssRules(classSelectors);
    if (!cssRules)
      return {};

    var propertyCount = propertyNames.length;
    var propertyValues = new Object();
    var propertyImportantFlags = new Array();
    for (i = 0,count = cssRules.length; i < count; i++) {
      var cssRule = cssRules[i];
      var ruleStyle = cssRule.style;
      for (var propertyIndex = 0; propertyIndex < propertyCount; propertyIndex++) {
        var propertyName = propertyNames[propertyIndex];
        var capitalizedPropertyName = O$.capitalizeCssPropertyName(propertyName);
        var thisPropertyValue = ruleStyle[capitalizedPropertyName];
        if (!thisPropertyValue)
          continue;
        var thisPropertyImportant = ruleStyle.getPropertyPriority && (ruleStyle.getPropertyPriority(capitalizedPropertyName) == "important");
        if (!propertyImportantFlags[propertyIndex]) {
          propertyValues[propertyName] = thisPropertyValue;
          propertyValues[capitalizedPropertyName] = thisPropertyValue;
          if (thisPropertyImportant)
            propertyImportantFlags[propertyIndex] = true;
        } else {
          if (thisPropertyImportant) {
            propertyValues[propertyName] = thisPropertyValue;
            propertyValues[capitalizedPropertyName] = thisPropertyValue;
          }
        }
      }
    }

    return propertyValues;
  };

  //

  O$.combineClassNames = function(classNames) {
    var nonNullClassNames = new Array();
    for (var i = 0, count = classNames.length; i < count; i++) {
      var className = classNames[i];
      if (className)
        nonNullClassNames.push(className);
    }
    return nonNullClassNames.join(" ");
  };

  O$.appendClassNames = function(element, classesToAppend) {
    var oldClassName = element.className;
    var newClassName = O$.combineClassNames(classesToAppend);
    if (oldClassName)
      newClassName = newClassName ? oldClassName + " " + newClassName : oldClassName;
    if (newClassName != oldClassName)
      element.className = newClassName;
    return oldClassName;
  };

  O$.checkClassNameUsed = function(element, className) {
    var classNames = element.className.split(" ");
    for (var i = 0, count = classNames.length; i < count; i++) {
      var usedClassName = classNames[i];
      if (usedClassName && usedClassName == className)
        return true;
    }

    return false;
  };

  O$.excludeClassNames = function(element, classesToExclude) {
    var newClassesToExclude = new Array();
    for (var i = 0, count = classesToExclude.length; i < count; i++) {
      var clsToExclude = classesToExclude[i];
      if (!clsToExclude)
        continue;
      var subClassesToExclude = clsToExclude.split(" ");
      for (var j = 0, jcount = subClassesToExclude.length; j < jcount; j++) {
        var subClassToExclude = subClassesToExclude[j];
        newClassesToExclude.push(subClassToExclude);
      }
    }

    var someClassesExcluded = false;
    var clsName = element.className;
    var clsNames = clsName ? clsName.split(" ") : new Array();
    var newNames = new Array();
    for (var nameIndex = 0, nameCount = clsNames.length; nameIndex < nameCount; nameIndex++) {
      var currName = clsNames[nameIndex];
      if (currName) {
        if (O$.findValueInArray(currName, newClassesToExclude) == -1)
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

  O$.getElementOwnStyle = function(element) {
    var styleMappings = element._styleMappings;
    if (!styleMappings)
      return element.className;
    else
      return styleMappings.__initialStyle;
  };

  O$.setElementOwnStyle = function(element, value) {
    var styleMappings = element._styleMappings;
    if (!styleMappings)
      return element.className = value;
    else
      return styleMappings.__initialStyle = value;
  };

  O$.setStyleMappings = function(element, styleMappings) {
    if (!element._styleMappings)
      element._styleMappings = {__initialStyle: element.className};
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

  O$.getElementStyleProperty = function(element, propertyName, enableValueCaching) {
    var propertyValues = O$.getElementStyleProperties(element, [propertyName], enableValueCaching);
    return propertyValues[propertyName];
  };

  O$.getElementStyleProperties = function(element, propertyNames, enableValueCaching) {
    if (enableValueCaching) {
      if (!element._cachedStyleValues)
        element._cachedStyleValues = {};
    }
    var propertyValues = {};
    var currentStyle = element.currentStyle;
    var computedStyle = !currentStyle && document.defaultView ? document.defaultView.getComputedStyle(element, "") : null;
    for (var i = 0, count = propertyNames.length; i < count; i++) {
      var propertyName = propertyNames[i];
      var capitalizedPropertyName = O$.capitalizeCssPropertyName(propertyName);

      var propertyValue = undefined;
      if (enableValueCaching)
        propertyValue = element._cachedStyleValues[propertyName];
      if (propertyValue != undefined) {
        propertyValues[propertyName] = propertyValue;
        propertyValues[capitalizedPropertyName] = propertyValue;
        continue;
      }

      if (currentStyle) {
        propertyValue = currentStyle[capitalizedPropertyName];
      } else if (computedStyle) {
        propertyValue = computedStyle.getPropertyValue(propertyName);
      }
      if (!propertyValue)
        propertyValue = "";
      if (enableValueCaching) {
        element._cachedStyleValues[propertyName] = propertyValue;
      }
      propertyValues[propertyName] = propertyValue;
      propertyValues[capitalizedPropertyName] = propertyValue;
    }

    return propertyValues;
  };


  O$.capitalizeCssPropertyName = function(propertyName) {
    while (true) {
      var idx = propertyName.indexOf("-");
      if (idx == -1)
        return propertyName;
      var firstPart = propertyName.substring(0, idx);
      var secondPart = propertyName.substring(idx + 1);
      if (secondPart.length > 0) {
        var firstChar = secondPart.substring(0, 1);
        firstChar = firstChar.toUpperCase();
        var otherChars = secondPart.substring(1);
        secondPart = firstChar + otherChars;
      }
      propertyName = firstPart + secondPart;
    }
  };

  O$.repaintAreaForOpera = function(element, deferredRepainting) {
    if (!O$.isOpera())
      return;
    if (!element)
      return;
    if (deferredRepainting) {
      setTimeout(function() {
        O$.repaintAreaForOpera(element, false);
      }, 1);
      return;
    }
    // using backgroundColor instead of just background is important here for cases when image is set as background
    // NOTE: setting the calculated border is an erroneous pattern because any further background changes through
    // element's className will be overriden by this in-place style declaration. However it must be uesd for document.body
    // because this element behaves differently when assigned old background - assigning a non-specified old background
    // makes the background white disregarding the appropriate stylesheets (JSFC-2346, JSFC-2275)
    var oldBackgroundColor = element != document.body
            ? element.style.backgroundColor
            : O$.getElementStyleProperty(element, "background-color");
    element.style.backgroundColor = "white";
    element.style.backgroundColor = "#fefefe";
    element.style.backgroundColor = oldBackgroundColor;
  };

  // JSFC-3270
  O$.repaintWindowForSafari = function(deferredRepainting) {
    if (!O$.isSafari())
      return;
    if (deferredRepainting) {
      setTimeout(function() {               // for fast machine
        O$.repaintWindowForSafari(false);
      }, 50);
      setTimeout(function() {               // for slow machine
        O$.repaintWindowForSafari(false);
      }, 300);
      return;
    }
    var tempDiv = document.createElement("div");
    tempDiv.innerHTML = "<div> <style type='text/css'> .d_u_m_p_c_l_a_s_s_ { background: black; filter: alpha( opacity = 50 ); opacity: .50; } </style> </div>";
  };

  O$.preloadImage = function(imageUrl) {
    if (!imageUrl)
      return;
    var image = new Image();
    image.src = imageUrl;
  };

  O$.preloadImages = function(imageUrls) {
    for (var i = 0, count = imageUrls.length; i < count; i++) {
      var imageUrl = imageUrls[i];
      O$.preloadImage(imageUrl);
    }
  };

  O$.blendColors = function(colorString1, colorString2, color2Proportion) {
    if (color2Proportion < 0)
      color2Proportion = 0;
    else if (color2Proportion > 1)
      color2Proportion = 1;
    return new O$.Color(colorString1).blendWith(new O$.Color(colorString2), color2Proportion).toHtmlColorString();
  };

  O$.Color = function(htmlColorString) {
    if (!O$.stringStartsWith(htmlColorString, "#"))
      throw "Color string should start with '#', but was: " + htmlColorString;
    this.r = parseInt(htmlColorString.substring(1, 3), 16);
    this.g = parseInt(htmlColorString.substring(3, 5), 16);
    this.b = parseInt(htmlColorString.substring(5, 7), 16);

    this.toHtmlColorString = function() {
      function intTo2DigitHex(intValue) {
        var str = intValue.toString(16);
        if (str.length < 2)
          str = "0" + str;
        return str;
      }

      return "#" + intTo2DigitHex(this.r) + intTo2DigitHex(this.g) + intTo2DigitHex(this.b);
    };

    this.blendWith = function(anotherColor, anotherColorPortion) {
      function blendValues(val1, val2, val2Portion) {
        return Math.round(val1 + (val2 - val1) * val2Portion);
      }

      this.r = blendValues(this.r, anotherColor.r, anotherColorPortion);
      this.g = blendValues(this.g, anotherColor.g, anotherColorPortion);
      this.b = blendValues(this.b, anotherColor.b, anotherColorPortion);
      return this;
    };

  };

  O$.getOpacityLevel = function(element) {
    return element._of_opacity !== undefined ? element._of_opacity : 1;
  };

  O$.setOpacityLevel = function(element, opacity) {
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
  O$.correctElementZIndex = function(element, referenceElement, zIndexIncrement) {
    if (zIndexIncrement === undefined)
      zIndexIncrement = 1;
    var zIndex = O$.getElementZIndex(element);
    var refZIndex = referenceElement._maxZIndex ? referenceElement._maxZIndex : O$.getElementZIndex(referenceElement);
    if (zIndex <= refZIndex)
      element.style.zIndex = refZIndex + zIndexIncrement;
  };

  /*
   Calculates z-index for the specified element, or if it is not a positioned element itself,
   returns z-index of the nearest parent containing block if it exists, otherwise returns the default z-index of 0.
   */
  O$.getElementZIndex = function(element) {
    var container = O$.getContainingBlock(element);
    if (!container)
      return 0;
    var zIndex = O$.getNumericStyleProperty(container, "z-index");
    return zIndex;
  };

  O$.getContainingBlock = function(element, ignoreThisElement) {
    for (var el = !ignoreThisElement ? element : element.parentNode; el; el = el.parentNode) {
      if (O$.isContainingBlock(el))
        return el;
    }
    return null;
  };

  // ----------------- DATE/TIME UTILITIES ---------------------------------------------------

  O$.initDateTimeFormatObject = function(months, shortMonths, days, shortDays, localeStr) {
    if (!this._dateTimeFormatMap) this._dateTimeFormatMap = [];
    if (!this._dateTimeFormatLocales) this._dateTimeFormatLocales = [];

    if (!this._dateTimeFormatMap[localeStr]) {
      this._dateTimeFormatMap[localeStr] = new O$.DateTimeFormat(months, shortMonths, days, shortDays);
      this._dateTimeFormatLocales.push(localeStr);
    }

  };

  O$.getDateTimeFormatObject = function(locale) {
    if (this._dateTimeFormatMap)
      return this._dateTimeFormatMap[locale];
    return null;
  };

  O$.parseDateTime = function(str) {
    var fields = str.split(" ");
    var dateStr = fields[0];
    var timeStr = fields[1];

    var dtf = O$.getDateTimeFormatObject(this._dateTimeFormatLocales[0]);
    var date = dtf.parse(dateStr, "dd/MM/yyyy");

    O$.parseTime(timeStr, date);
    return date;
  };

  O$.parseTime = function(timeStr, destDate) {
    if (!destDate)
      destDate = new Date();
    var timeFields = timeStr.split(":");
    var hours = timeFields[0];
    var minutes = timeFields[1];
    destDate.setHours(hours, minutes, 0, 0);
    return destDate;

  };

  O$.formatDateTime = function(date) {
    if (!date)
      return "";
    var dtf = O$.getDateTimeFormatObject(this._dateTimeFormatLocales[0]);
    var str = dtf.format(date, "dd/MM/yyyy");
    str += " " + O$.formatTime(date);
    return str;
  };

  O$.formatTime = function(date) {
    if (!date)
      return "";
    var hours = date.getHours();
    var minutes = date.getMinutes();
    if (minutes < 10)
      minutes = "0" + minutes;
    return hours + ":" + minutes;
  };

  // ----------------- ABSOLUTE POSITIONING / METRICS UTILITIES ---------------------------------------------------

  O$.getDefaultAbsolutePositionParent = function() {
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

  O$.createAbsolutePositionedElement = function(parent) {
    var elt = document.createElement("div");
    elt.style.position = "absolute";
    if (!parent)
      parent = O$.getDefaultAbsolutePositionParent();
    parent.appendChild(elt);
    return elt;
  };

  /*
   O$.calculateNumericStyleProperty doesn't work when calculating margin on a table under Mozilla 2 for some reason,
   so here's an alternative implementation that works for this case.
   */
  O$.calculateMozillaMargins = function(element) {
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
    return {marginLeft: marginLeft, marginRight: marginRight, marginTop: marginTop, marginBottom: marginBottom};
  };

  /*
   Returns an object {left, top} that points to the top-left corner of the specified element. This method takes into account
   element's border if it exists, that is it determines the position of the element's border box.

   If relativeToNearestContainingBlock parameter is not specified or specified as false, this method
   calculates the elements' "absolute" position relative to the top-left corner of the entire document.

   If relativeToNearestContainingBlock is specified as true, it returns an offset relative to the nearest containing block
   (see http://www.w3.org/TR/REC-CSS2/visuren.html#containing-block for definition of a containing block). More exactly
   it calculates offset relative to the containing block's client area. In other words if there is another absolutely
   positioned element in the same containing block, placing it at the position returned by this function will place that
   element at the same position as the element being measured with this function.

   Note that this function can't calculate position of an element that is hidden using "display: none" CSS declaration.
   Though you may work around such problems by using "visibility: hidden" instead of "display: none".

   See also: O$.setElementPos, O$.getElementBorderRectangle, O$.setElementBorderRectangle.
   */
  O$.getElementPos = function(element, relativeToNearestContainingBlock) {
    var left, top;

    if (element.getBoundingClientRect) {
      var rect = element.getBoundingClientRect();
      left = rect.left;
      top = rect.top;
      var containingBlock;
      if (relativeToNearestContainingBlock) {
        containingBlock = O$.getContainingBlock(element, true);
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
      return {left: left, top: top};
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

      if (O$.isMozillaFF2() && O$.getElementStyleProperty(offsetParent, "overflow") != "visible") {
        left += O$.getNumericStyleProperty(offsetParent, "border-left-width");
        top += O$.getNumericStyleProperty(offsetParent, "border-top-width");
      }

      var parentIsContainingBlock = O$.isContainingBlock(offsetParent);
      if (relativeToNearestContainingBlock && parentIsContainingBlock) {
        if (offsetParent.tagName.toLowerCase() == "div" && (O$.isOpera9AndLate() || O$.isSafari2())) {
          if (O$.getElementStyleProperty(offsetParent, "border-style") != "none") {
            var borderLeftWidth = O$.getNumericStyleProperty(offsetParent, "border-left-width");
            var borderTopWidth = O$.getNumericStyleProperty(offsetParent, "border-top-width");
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
          var pscrollLeft = parent.scrollLeft;
          if (pscrollLeft)
            left -= pscrollLeft;
          var pscrollTop = parent.scrollTop;
          if (pscrollTop)
            top -= pscrollTop;
        }

        if (!thisIsContainingBlock) {
          // containing blocks already have the following corrections as part of offsetLeft/offsetTop
          if (O$.isMozillaFF2()) {
            if (parentNodeName == "td") {
              left -= O$.getNumericStyleProperty(parent, "border-left-width");
              top -= O$.getNumericStyleProperty(parent, "border-top-width");
            }
            if (parentNodeName == "table" && O$.isStrictMode()) {
              var parentMargins = O$.calculateMozillaMargins(parent);
              left += parentMargins.marginLeft;
              top += parentMargins.marginTop;
            }
          }
          if (O$.isSafari()) {
            if (parentNodeName == "table") {
              left += O$.getNumericStyleProperty(parent, "border-left-width");
              top += O$.getNumericStyleProperty(parent, "border-top-width");
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
              left += (offsetParent.clientLeft == undefined ? O$.getNumericStyleProperty(offsetParent, "border-left-width") : offsetParent.clientLeft);
              top += (offsetParent.clientTop == undefined ? O$.getNumericStyleProperty(offsetParent, "border-top-width") : offsetParent.clientTop);
            }
          }
        }
      }

      element = offsetParent;
    }
    return {left: left, top: top};
  };

  O$.getCuttingContainingRectangle = function(element, cachedDataContainer) {
    var left, right, top, bottom;

    while (true) {
      var container = O$.getContainingBlock(element, true);
      if (!container)
        break;
      var overflowX = O$.getElementStyleProperty(container, "overflow-x");
      var overflowY = O$.getElementStyleProperty(container, "overflow-y");
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

  O$.getVisibleElementBorderRectangle = function(element, relativeToNearestContainingBlock, cachedDataContainer) {
    var rect = O$.getElementBorderRectangle(element, relativeToNearestContainingBlock, cachedDataContainer);
    var cuttingRect = O$.getCuttingContainingRectangle(element, cachedDataContainer);
    if (relativeToNearestContainingBlock) {
      var elementContainer = O$.getContainingBlock(element, true);
      var containerPos = elementContainer ? O$.getElementPos(elementContainer) : null;
      if (cuttingRect.left && containerPos)
        cuttingRect.left -= containerPos.left;
      if (cuttingRect.top && containerPos)
        cuttingRect.top -= containerPos.top;
    }
    rect.intersectWith(cuttingRect);
    return rect;
  };

  /*
   Returns the O$.Rectangle object that corresponds to the bounding rectangle of the passed element. This method takes
   into account element's border. That is it returns element's border box rectangle.

   relativeToNearestContainingBlock is an optional parameter that specifies whether the position is calculated relative
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
  O$.getElementBorderRectangle = function(element, relativeToNearestContainingBlock, cachedDataContainer) {
    if (cachedDataContainer) {
      if (!element._of_getElementRectangle)
        element._of_getElementRectangle = {}
      if (element._of_getElementRectangle._currentCache == cachedDataContainer)
        return element._of_getElementRectangle._cachedValue;
    }
    var pos = O$.getElementPos(element, relativeToNearestContainingBlock);
    var size = O$.getElementSize(element);
    var rect = new O$.Rectangle(pos.left, pos.top, size.width, size.height);
    if (cachedDataContainer) {
      element._of_getElementRectangle._currentCache = cachedDataContainer;
      element._of_getElementRectangle._cachedValue = rect;
    }
    return rect;
  };

  O$.getElementSize = function(element) {
    var width = element.offsetWidth;
    var height = element.offsetHeight;
    // Mozilla 2.0.x strict reports margins on tables to be part of table when measuring it with offsetXXX attributes
    if (O$.isMozillaFF2() && O$.isStrictMode() && element.tagName && element.tagName.toLowerCase() == "table") {
      var margins = O$.calculateMozillaMargins(element);
      width -= margins.marginLeft + margins.marginRight;
      height -= margins.marginTop + margins.marginBottom;
    }
    return {width: width, height: height};
  };

  O$.getElementPaddingRectangle = function(element, relativeToNearestContainngBlock, cachedDataContainer) {
    var rect = O$.getElementBorderRectangle(element, relativeToNearestContainngBlock, cachedDataContainer);
    var borderLeftWidth = O$.getNumericStyleProperty(element, "border-left-width");
    var borderRightWidth = O$.getNumericStyleProperty(element, "border-right-width");
    var borderTopWidth = O$.getNumericStyleProperty(element, "border-top-width");
    var borderBottomWidth = O$.getNumericStyleProperty(element, "border-bottom-width");
    rect.x += borderLeftWidth;
    rect.y += borderTopWidth;
    rect.width -= borderLeftWidth + borderRightWidth;
    rect.height -= borderTopWidth + borderBottomWidth;
    return rect;
  };

  /*
   Moves the specified element to the specified position (specified as {left, top} object). More exactly the top-left
   corner of element's border box will be at the specified position after this method is invoked.

   See also: O$.getElementPos, O$.getElementBorderRectangle, O$.setElementBorderRectangle.
   */
  O$.setElementPos = function(element, pos) {
    element.style.left = pos.left + "px";
    element.style.top = pos.top + "px";
  };

  O$.setElementSize = function(element, size, _paddingsHaveBeenReset) {
    var width = size.width;
    var height = size.height;

    if (!_paddingsHaveBeenReset)
      O$.excludeClassNames(element, ["o_zeroPaddings"]);
    if (!O$.isExplorer() || O$.isStrictMode()) {
      if (width != null) {
        width -= O$.getNumericStyleProperty(element, "padding-left") + O$.getNumericStyleProperty(element, "padding-right");
        width -= O$.getNumericStyleProperty(element, "border-left-width") + O$.getNumericStyleProperty(element, "border-right-width");
      }
      if (height != null) {
        height -= O$.getNumericStyleProperty(element, "padding-top") + O$.getNumericStyleProperty(element, "padding-bottom");
        height -= O$.getNumericStyleProperty(element, "border-top-width") + O$.getNumericStyleProperty(element, "border-bottom-width");
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

  /*
   Changes size and position of the specified element according to the specified rectangle. The rectangle speicifies
   the element's border box.

   See also: O$.getElementBorderRectangle, O$.getElementPos, O$.setElementPos.
   */
  O$.setElementBorderRectangle = function(element, rect) {
    O$.setElementSize(element, {width: rect.width, height: rect.height});
    O$.setElementPos(element, {left: rect.x, top: rect.y});
  };

  /**
   * Introduced according to CSS spec http://www.w3.org/TR/REC-CSS2/visudet.html#containing-block-details
   * See JSFC-2045 Popups displayed incorrectly under JBoss Portal
   */
  O$.isContainingBlock = function(elt) {
    O$.assert(elt, "elt is null");
    if (elt == document) {
      // O$.calculateElementStyleProperty fails to determine position on the document element, and
      // document element can't have a non-static position
      return false;
    }
    var position = O$.getElementStyleProperty(elt, "position");
    if (!position) return false;
    return position != "static";
  };


  O$.getVisibleAreaSize = function() {
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
    return {width : width, height : height};
  };

  O$.getVisibleAreaRectangle = function() {
    var pageScrollPos = O$.getPageScrollPos();
    var x = pageScrollPos.x;
    var y = pageScrollPos.y;
    var visibleAreaSize = O$.getVisibleAreaSize();
    var width = visibleAreaSize.width;
    var height = visibleAreaSize.height;
    return new O$.Rectangle(x, y, width, height);
  };

  O$.scrollElementIntoView = function(element, cachedDataContainer) {
    var scrollingOccured = false;
    var rect = O$.getElementBorderRectangle(element);
    for (var parent = element.parentNode; parent; parent = parent.parentNode) {
      var parentScrollable;
      if (parent != document) {
        var overflowY = O$.getElementStyleProperty(parent, "overflow-y");
        parentScrollable = overflowY != "visible";
      } else {
        parentScrollable = true;
      }
      if (!parentScrollable)
        continue;
      var parentRect = parent != document
              ? O$.getElementPaddingRectangle(parent, false, cachedDataContainer)
              : O$.getVisibleAreaRectangle();

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
        scrollingOccured = true;
      }
    }
    return scrollingOccured;
  };

  O$.scrollRectIntoView = function(rect) {
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

  O$.getScrollPos = function(scrollableComponent) {
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

    return {x: x, y: y};
  };

  O$.getPageScrollPos = function() {
    var x = 0;
    var y = 0;
    if (typeof( window.pageYOffset ) == "number") {
      // Netscape compliant
      y = window.pageYOffset;
      x = window.pageXOffset;
    } else if (document.body && ( document.body.scrollLeft || document.body.scrollTop )) {
      // DOM compliant
      y = document.body.scrollTop;
      x = document.body.scrollLeft;
    } else if (document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop )) {
      // IE6 standards compliant mode
      y = document.documentElement.scrollTop;
      x = document.documentElement.scrollLeft;
    }
    return {x: x, y: y};
  };

  O$.setPageScrollPos = function(scrollPos) {
    window.scrollTo(scrollPos.x, scrollPos.y);
  };

  O$.getNumericStyleProperty = function(element, propertyName, enableValueCaching) {
    if (O$.isExplorer() || O$.isOpera()) {
      // border "medium none black" under IE is actually displayed with zero width because of "none" style, so we should
      // skip calculating "medium" width and just return 0 in such cases.
      var capitalizedPropertyName = O$.capitalizeCssPropertyName(propertyName);
      if (O$.stringStartsWith(capitalizedPropertyName, "border") && O$.stringEndsWith(capitalizedPropertyName, "Width")) {
        var borderStylePropertyName = capitalizedPropertyName.substring(0, capitalizedPropertyName.length - "Width".length) + "Style";
        if (O$.getElementStyleProperty(element, borderStylePropertyName) == "none")
          return 0;
      }
    }
    var str = O$.getElementStyleProperty(element, propertyName, enableValueCaching);
    var result = O$.calculateNumericCSSValue(str);
    return result;
  };

  O$.calculateNumericCSSValue = function(value) {
    if (!value)
      return 0;
    if (!isNaN(1 * value))
      return 1 * value;
    if (O$.stringEndsWith(value, "px"))
      return 1 * value.substring(0, value.length - 2);
    if (value == "auto")
      return 0; // todo: can't calculate "auto" (e.g. from margin property) on a simulated border -- consider simulating such "non-border" values on other properties

    if (!O$._nonPixelValueMeasurements)
      O$._nonPixelValueMeasurements = [];
    var pixelValue = O$._nonPixelValueMeasurements[value];
    if (pixelValue != undefined)
      return pixelValue;

    pixelValue = O$.calculateLineWidth(value + " solid white");
    O$._nonPixelValueMeasurements[value] = pixelValue;
    return pixelValue;
  };

  O$.calculateLineWidth = function(lineStyleStr) {
    if (!lineStyleStr)
      return 0;
    if (!window._of_lineWidthMeasurements)
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
  O$.LEFT = "left";
  O$.LEFT_EDGE = "leftEdge";
  O$.RIGHT_EDGE = "rightEdge";
  O$.RIGHT = "right";
  O$.ABOVE = "above";
  O$.TOP_EDGE = "topEdge";
  O$.BOTTOM_EDGE = "bottomEdge";
  O$.BELOW = "below";

  O$.alignPopupByElement = function(popup, element, horizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, disableRepositioning, repositioningAttempt) {
    if (!horizAlignment) horizAlignment = O$.LEFT_EDGE;
    if (!vertAlignment) vertAlignment = O$.BELOW;
    if (!horizDistance) horizDistance = 0;
    if (!vertDistance) vertDistance = 0;
    horizDistance = O$.calculateNumericCSSValue(horizDistance);
    vertDistance = O$.calculateNumericCSSValue(vertDistance);

    var elementRect = ignoreVisibleArea
            ? O$.getElementBorderRectangle(element)
            : O$.getVisibleElementBorderRectangle(element);
    var popupSize = O$.getElementSize(popup);
    var popupWidth = popupSize.width;
    var popupHeight = popupSize.height;

    var x;
    switch (horizAlignment) {
      case O$.LEFT:
        x = elementRect.getMinX() - popupWidth - horizDistance;
        break;
      case O$.LEFT_EDGE:
        x = elementRect.getMinX() + horizDistance;
        break;
      case O$.CENTER:
        x = elementRect.getMinX() + (elementRect.width - popupWidth) / 2;
        break;
      case O$.RIGHT_EDGE:
        x = elementRect.getMaxX() - popupWidth - horizDistance;
        break;
      case O$.RIGHT:
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
      case O$.TOP_EDGE:
        y = elementRect.getMinY() + vertDistance;
        break;
      case O$.CENTER:
        y = elementRect.getMinY() + (elementRect.height - popupHeight) / 2;
        break;
      case O$.BOTTOM_EDGE:
        y = elementRect.getMaxY() - popupHeight - vertDistance;
        break;
      case O$.BELOW:
        y = elementRect.getMaxY() + vertDistance;
        break;
      default:
        O$.logError("O$.alignPopupByElement: unrecognized vertAlignment: " + vertAlignment);
    }

    if (!disableRepositioning) {
      var allowedRectangle = O$.getCuttingContainingRectangle(popup);
      var shouldBeRepositioned = !allowedRectangle.containsRectangle(new O$.Rectangle(x, y, popupWidth, popupHeight)) &&
                                 allowedRectangle.width >= popupWidth && allowedRectangle.height >= popupHeight;
      if (shouldBeRepositioned) {
        if (repositioningAttempt)
          return false;
        var alternativeVertAlignment = vertAlignment == O$.BELOW || vertAlignment == O$.ABOVE
                ? vertAlignment == O$.BELOW ? O$.ABOVE : O$.BELOW
                : null;
        if (alternativeVertAlignment) {
          if (O$.alignPopupByElement(popup, element, horizAlignment, alternativeVertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true))
            return;
        }
        var alternativeHorizAlignment = horizAlignment == O$.LEFT || horizAlignment == O$.RIGHT
                ? horizAlignment == O$.RIGHT ? O$.LEFT : O$.RIGHT
                : null;
        if (alternativeHorizAlignment) {
          if (O$.alignPopupByElement(popup, element, alternativeHorizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true))
            return;
        }
        if (alternativeHorizAlignment && alternativeVertAlignment) {
          if (O$.alignPopupByElement(popup, element, alternativeHorizAlignment, alternativeVertAlignment, horizDistance, vertDistance, ignoreVisibleArea, false, true))
            return;
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

    var popupContainer = O$.getContainingBlock(popup, true);
    if (popupContainer) {
      var containerRect = O$.getElementPaddingRectangle(popupContainer);
      x -= containerRect.x;
      y -= containerRect.y;


    }
    if (popup.setLeft) {
      popup.setLeft(x);
      popup.setTop(y);
    } else {
      O$.setElementPos(popup, {left: x, top: y});
    }
    if (repositioningAttempt)
      return true;
  };

  O$.isAlignmentInsideOfElement = function(horizAlignment, vertAlignment) {
    var insideHorizontaly = horizAlignment == O$.LEFT_EDGE || horizAlignment == O$.CENTER || horizAlignment == O$.RIGHT_EDGE;
    var insideVertically = vertAlignment == O$.TOP_EDGE || vertAlignment == O$.CENTER || vertAlignment == O$.BOTTOM_EDGE;
    return insideHorizontaly && insideVertically;
  };

  O$.fixInputsWidthStrict = function(container) {
    if (!O$.isStrictMode())
      return;

    function addFieldForAutocorrection(field, widthCorrection, heightCorrection) {
      if (!O$._fieldsForAutocorrection)
        O$._fieldsForAutocorrection = [];
      O$._fieldsForAutocorrection.push(field);
      field._autocorrectSize = function() {
        var container = field.parentNode;
        var containerSize = O$.getElementSize(container);
        var fieldSize = O$.getElementSize(field);
        if (widthCorrection) {
          fieldSize.width = containerSize.width - widthCorrection;
          fieldSize.width -= O$.getNumericStyleProperty(container, "padding-left") + O$.getNumericStyleProperty(container, "padding-right");
        } else
          fieldSize.width = null;
        if (heightCorrection) {
          fieldSize.height = containerSize.height - heightCorrection;
          fieldSize.height -= O$.getNumericStyleProperty(container, "padding-top") + O$.getNumericStyleProperty(container, "padding-bottom");
        } else
          fieldSize.height = null;
        O$.setElementSize(field, fieldSize);
      };
      if (!O$._fieldSizeAutocorrectionInterval)
        O$._fieldSizeAutocorrectionInterval = setInterval(function() {
          for (var i in O$._fieldsForAutocorrection) {
            var f = O$._fieldsForAutocorrection[i];
            if (typeof f != "function")
              f._autocorrectSize();
          }
        }, 100);
      field._autocorrectSize();
    }

    function processInput(input) {
      if (input._strictWidthFixed)
        return;
      input._strictWidthFixed = true;
      var widthCorrection;
      if (O$.getStyleClassProperty(input.className, "width") == "100%") {
        var bordersX = O$.getNumericStyleProperty(input, "border-left-width") + O$.getNumericStyleProperty(input, "border-right-width");
        var paddingsX = O$.getNumericStyleProperty(input, "padding-left") + O$.getNumericStyleProperty(input, "padding-right");
        var parent = input.parentNode;
        widthCorrection = bordersX + paddingsX;
      }
      var heightCorrection;
      if (O$.getStyleClassProperty(input.className, "height") == "100%") {
        var bordersY = O$.getNumericStyleProperty(input, "border-top-width") + O$.getNumericStyleProperty(input, "border-bottom-width");
        var paddingsY = O$.getNumericStyleProperty(input, "padding-top") + O$.getNumericStyleProperty(input, "padding-bottom");
        heightCorrection = bordersY + paddingsY;
      }

      //    if (!O$.isExplorer()) {
      if (widthCorrection) {
        var parentPaddingRight = O$.getNumericStyleProperty(parent, "padding-right");
        parent.style.paddingRight = parentPaddingRight + widthCorrection + "px";
      }
      if (heightCorrection) {
        var parentPaddingBottom = O$.getNumericStyleProperty(parent, "padding-bottom");
        parent.style.paddingBottom = parentPaddingBottom + heightCorrection + "px";
      }
      //    } else {
      //      if (widthCorrection || heightCorrection)
      //        addFieldForAutocorrection(input, widthCorrection, heightCorrection);
      //    }

    }

    var inputs = container.getElementsByTagName("input");
    for (var i = 0, count = inputs.length; i < count; i++) {
      processInput(inputs[i]);
    }
    var textAreas = container.getElementsByTagName("textarea");
    for (i = 0,count = textAreas.length; i < count; i++) {
      processInput(textAreas[i]);
    }

  };

  // ----------------- HIDE <SELECT> CONTROLS UNDER POPUP IN IE ---------------------------------------------------

  O$._controlsToHide = O$.isExplorer() ? new Array("select-one", "select-multiple") : null;
  O$._controlsHiddenControlsMap = new Object();

  O$.walkControlsToHide = function(popup, runFunction) {
    if (popup._coveredControls) {
      for (var i = 0; i < popup._coveredControls.length; i++) {
        var control = popup._coveredControls[i];
        runFunction.call(this, control);
      }
    }
  };

  O$.hideControlsUnderPopup = function(popup) {
    if (O$.isExplorer() && O$._controlsToHide && O$._controlsToHide.length > 0) {
      var runFunction = function(control) {
        var controlData = new Object();
        controlData.id = popup.id;
        controlData.visibility = control.style.visibility;
        O$._controlsHiddenControlsMap[control.id] = controlData;
        control.style.visibility = "hidden";
      };

      var rectangle = new O$.Rectangle(popup.offsetLeft, popup.offsetTop, popup.offsetWidth, popup.offsetHeight);
      popup._coveredControls = new Array();
      var frm = O$.findParentNode(popup, "FORM");
      var controls = frm.elements;
      var index = 0;
      for (var i = 0; i < controls.length; i++) {
        var control = controls[i];
        if (control.type && O$.arrayContainsValue(O$._controlsToHide, control.type)) {
          if (! O$.isChild(popup, control)) {
            var examRectangle = O$.getElementBorderRectangle(control);
            if (rectangle.intersects(examRectangle)) {
              popup._coveredControls[index++] = control;
            }
          }
        }
      }


      O$.walkControlsToHide(popup, runFunction);
    }
  };

  O$.unhideControlsUnderPopup = function(popup) {
    if (O$.isExplorer() && O$._controlsToHide && O$._controlsToHide.length > 0) {
      var runFunction = function(control) {
        var controlData = O$._controlsHiddenControlsMap[control.id];
        if (controlData && (controlData.id == popup.id)) {
          control.style.visibility = controlData.visibility;
        }
      };
      O$.walkControlsToHide(popup, runFunction);
      popup._coveredControls = null;
    }
  };

  O$.initIETransparencyWorkaround = function(popup) {
    if (!O$.isExplorer6())
      return;
    popup._requireTransparencyWorkaround = true;
    if (popup._preCreatedIETransparencyControl)
      return;

    var iframe = document.createElement("iframe");
    iframe.src = "javascript:'';";
    // to overcome the "nonsecure items" message on HTTPS pages
    iframe.id = popup.id + "::ieTransparencyControl";
    iframe.scrolling = "No";
    iframe.frameBorder = "0";
    iframe.style.position = "absolute";
    iframe.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)";
    // to support transparency of the popup itself

    iframe._updatePositionAndSize = function() {
      iframe.style.width = popup.offsetWidth + "px";
      iframe.style.height = popup.offsetHeight + "px";
      iframe.style.left = popup.offsetLeft + "px";
      iframe.style.top = popup.offsetTop + "px";
    };

    var popupZIndex = O$.getElementStyleProperty(popup, "z-index");
    if (!popupZIndex) {
      popupZIndex = 10;
      popup.style.zIndex = popupZIndex;
    }
    iframe.zIndex = popup.zIndex - 1;

    iframe.style.display = "none";
    popup.parentNode.insertBefore(iframe, popup);
    popup._preCreatedIETransparencyControl = iframe;
  };

  O$.addIETransparencyControl = function(popup) {
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

  O$.removeIETransparencyControl = function(popup) {
    if (!popup._requireTransparencyWorkaround || !popup._ieTransparencyControl)
      return;

    popup._ieTransparencyControl.style.display = "none";
    popup._ieTransparencyControl = undefined;
  };

  O$.isInvisible = function(element) {
    if (!element.style) {
      return false;
    }
    return element.style.display == "none" || element.style.visibility == "hidden";
  };

  O$.isVisibleRecursive = function(element) {
    if (O$.isInvisible(element))
      return false;

    var parentNode = element.parentNode;
    if (!parentNode)
      return true;

    return O$.isVisibleRecursive(parentNode);
  };


  // ----------------- EFFECTS ---------------------------------------------------

  O$.getInterpolatedValue = function(value1, value2, value2Proportion) {
    // handle numbers
    if (!isNaN(1 * value1) && !isNaN(1 * value2))
      return value1 + (value2 - value1) * value2Proportion;

    // handle colors
    if (typeof value1 === "string" && O$.stringStartsWith(value1, "#"))
      return O$.blendColors(value1, value2, value2Proportion);

    // handle points
    if (typeof value1 === "object" && value1 != null && value1.left !== undefined)
      return {left: O$.getInterpolatedValue(value1.left, value2.left, value2Proportion),
        top: O$.getInterpolatedValue(value1.top, value2.top, value2Proportion)};

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
  O$.getElementEffectProperty = function(element, property) {
    if (property == "opacity")
      return O$.getOpacityLevel(element);
    if (property == "position")
      return O$.getElementPos(element, true);
    if (property == "rectangle")
      return O$.getElementBorderRectangle(element, true);

    return O$.getElementStyleProperty(element, property);
  };

  O$.setElementEffectProperty = function(element, property, value) {
    if (property == "opacity")
      O$.setOpacityLevel(element, value);
    else if (property == "position")
      O$.setElementPos(element);
    else if (property == "rectangle")
        O$.setElementBorderRectangle(element, value);
      else
        element.style[property] = value;
  };

  O$.runTransitionEffect = function(element, propertyNames, newValues, transitionPeriod, updateInterval, events) {
    if (transitionPeriod === undefined || transitionPeriod < 0)
      transitionPeriod = 0;
    if (!updateInterval)
      updateInterval = 50;
    var initialValues = {};
    var propertyCount = propertyNames.length;
    O$.assertEquals(propertyCount, newValues.length, "O$.runTransitionEffect: checking propertyNames.length == newValues.length");

    var startTime = new Date().getTime();
    var endTime = startTime + transitionPeriod;
    var transition = {
      active: true,
      propertyValues: {},
      completionProportion: 0,
      getValueForCompletionProportion: function(propertyName, proportion) {
        return O$.getInterpolatedValue(initialValues[propertyName], newValues[propertyName], proportion);
      },
      update: function(forceProportion) {
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
      stop: function(forceCompletionProportion) {
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
      intervalId: setInterval(function() {
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

// ----------------- COMPONENT UTILS -------------------------------------------

  O$.addLoadEvent(function() {
    if (!O$.findCssRule(".o_default_css_marker"))
      O$.logError("default.css file is not loaded. The usual reason is application misconfiguration. See OpenFaces Installation and Configuration guide (resource filter configuration, etc).")
  });

  O$._submitComponentWithField = function(componentId, focusedField, additionalParams, submittedComponentIds) {
    var focusedFieldId = focusedField ? focusedField.id : null;
    var component = O$(componentId);
    var focusFilterField = function() {
      if (!focusedFieldId)
        return;
      var field = O$(focusedFieldId);
      if (!field)
        return;
      if (field.focus)
        try {
          field.focus();
        } catch(e) {
          // ignore failed focus attempts
        }
    };
    O$._submitInternal(component, function() {
      setTimeout(focusFilterField, 1);
    }, additionalParams, submittedComponentIds);
  };

  O$._submitInternal = function(component, completionCallback, additionalParams, submittedComponentIds) {
    var useAjax = component._useAjax;
    if (!useAjax) {
      if (additionalParams)
        for (var i = 0, count = additionalParams.length; i < count; i++) {
          var paramEntry = additionalParams[i];
          O$.addHiddenField(component, paramEntry[0], paramEntry[1]);
        }
      O$.submitEnclosingForm(component);
    } else {
      O$.reloadComponents([component.id], {
        onajaxend: completionCallback,
        additionalParams: additionalParams,
        submittedComponentIds: submittedComponentIds});
    }
  };


}



