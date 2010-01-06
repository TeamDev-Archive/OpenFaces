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

//---------------  double buffered element methods  ----------------

O$._calculateOffsetWidth = function(element, useDoubleBuffering) {
  var offsetWidth = element.offsetWidth;
  if (useDoubleBuffering) {
    var styleWidth = O$._calculateNumericWidth(element, false);
    var newStyleWidth = O$._calculateNumericWidth(element, true);
    return offsetWidth + newStyleWidth - styleWidth;
  } else {
    return offsetWidth;
  }
};

O$._calculateOffsetHeight = function(element, useDoubleBuffering) {
  var offsetHeight = element.offsetHeight;
  if (useDoubleBuffering && element._newStyle) {
    var styleHeight = O$._calculateNumericHeight(element, false);
    var newStyleHeight = O$._calculateNumericHeight(element, true);
    return offsetHeight + newStyleHeight - styleHeight;
  } else {
    return offsetHeight;
  }
};

O$._calculateNumericInnerWidth = function(element, useDoubleBuffering) {
  var offsetWidth = O$._calculateOffsetWidth(element, useDoubleBuffering);
  if (element._storedSizeProperties) {
    var paddingsAndBordersWidth = element._storedSizeProperties.paddingsAndBordersWidth;
  } else {
    var leftBorderSize = parseInt(O$.getStyleClassProperty(element.className, "border-left-width"));
    var rightBorderSize = parseInt(O$.getStyleClassProperty(element.className, "border-right-width"));
    var leftPaddingSize = parseInt(O$.getStyleClassProperty(element.className, "padding-left"));
    var rightPaddingSize = parseInt(O$.getStyleClassProperty(element.className, "padding-right"));
    paddingsAndBordersWidth = leftBorderSize + rightBorderSize + leftPaddingSize + rightPaddingSize;
  }
  return offsetWidth - paddingsAndBordersWidth;
};

O$._calculateNumericInnerHeight = function(element, useDoubleBuffering) {
  var offsetHeight = O$._calculateOffsetHeight(element, useDoubleBuffering);
  if (element._storedSizeProperties) {
    var paddingsAndBordersHeight = element._storedSizeProperties.paddingsAndBordersHeight;
  } else {
    var topBorderSize = parseInt(O$.getStyleClassProperty(element.className, "border-top-width"));
    var bottomBorderSize = parseInt(O$.getStyleClassProperty(element.className, "border-bottom-width"));
    var topPaddingSize = parseInt(O$.getStyleClassProperty(element.className, "padding-top"));
    var bottomPaddingSize = parseInt(O$.getStyleClassProperty(element.className, "padding-bottom"));
    paddingsAndBordersHeight = topBorderSize + bottomBorderSize + topPaddingSize + bottomPaddingSize;
  }
  return offsetHeight - paddingsAndBordersHeight;
};

O$._calculateNumericWidth = function(element, useDoubleBuffering) {
  if (useDoubleBuffering && element._newStyle && element._newStyle.width) {
    var width = element._newStyle.width;
  } else {
    if (element.style.width) {
      width = element.style.width;
    } else {
      width = O$.getStyleClassProperty(element.className, "width");
    }
    if (!width) {
      width = element.offsetWidth;
      if (!width) {
        var parentNode = element.parentNode;
        if (parentNode == document.body)
          if (O$.isExplorer()) {
            return document.documentElement.offsetWidth;
          } else {
            return window.innerWidth;
          }
        width = O$._calculateNumericWidth(parentNode, useDoubleBuffering);
      }
      return width;
    }
  }
  if (O$._isPercentageValue(width)) {
    var result = O$._calculateNumericWidthFactor(element, useDoubleBuffering && !element._isRootDoubleBufferedElement) * 0.01 * parseFloat(width);
    return Math.round(result);
  }
  else {
    return parseInt(width);
  }
};

O$._calculateNumericHeight = function(element, useDoubleBuffering) {
  if (useDoubleBuffering && element._newStyle && element._newStyle.height) {
    var height = element._newStyle.height;
  } else {
    if (element.style.height) {
      height = element.style.height;
    } else {
      height = O$.getStyleClassProperty(element.className, "height");
    }
    if (!height) {
      height = element.offsetHeight;
      if (!height) {
        var parentNode = element.parentNode;
        if (parentNode == document.body)
          if (O$.isExplorer()) {
            return document.documentElement.offsetHeight;
          } else if (O$.isOpera()) {
            return document.clientHeight;
          } else {
            return window.innerHeight;
          }
        height = O$._calculateNumericHeight(parentNode, useDoubleBuffering);
      }
      return height;
    }
  }
  if (O$._isPercentageValue(height)) {
    var result = O$._calculateNumericHeightFactor(element, useDoubleBuffering && !element._isRootDoubleBufferedElement) * 0.01 * parseFloat(height);
    return Math.round(result);
  }
  else {
    return parseInt(height);
  }
};

O$._calculateNumericWidthFactor = function(element, useDoubleBuffering) {
  var parent = element.parentNode;
  if (element._isCoupled) {
    var widthFactor = O$._calculateNumericInnerWidth(parent, useDoubleBuffering);
  } else {
    widthFactor = O$._calculateNumericWidth(parent, false); //todo need testing
  }
  return widthFactor;
};

O$._calculateNumericHeightFactor = function(element, useDoubleBuffering) {
  var parent = element.parentNode;
  if (element._isCoupled) {
    var heightFactor = O$._calculateNumericInnerHeight(element.parentNode, useDoubleBuffering);
  } else {
    heightFactor = O$._calculateNumericHeight(parent, false); //todo need testing
  }
  return heightFactor;
};

O$._storeSizeProperties = function(element) {
  //todo don't support "cm", "in", etc. yet
  var leftBorderWidthValue = parseInt(O$.getStyleClassProperty(element.className, "border-left-width"));
  var leftBorderSize = isNaN(leftBorderWidthValue) ? 0 : leftBorderWidthValue;
  var rightBorderWidthValue = parseInt(O$.getStyleClassProperty(element.className, "border-right-width"));
  var rightBorderSize = isNaN(rightBorderWidthValue) ? 0 : rightBorderWidthValue;
  var topBorderWidthValue = parseInt(O$.getStyleClassProperty(element.className, "border-top-width"));
  var topBorderSize = isNaN(topBorderWidthValue) ? 0 : topBorderWidthValue;
  var bottomBorderWidthValue = parseInt(O$.getStyleClassProperty(element.className, "border-bottom-width"));
  var bottomBorderSize = isNaN(bottomBorderWidthValue) ? 0 : bottomBorderWidthValue;
  var leftPaddingSize = parseInt(O$.getStyleClassProperty(element.className, "padding-left"));
  var rightPaddingSize = parseInt(O$.getStyleClassProperty(element.className, "padding-right"));
  var topPaddingSize = parseInt(O$.getStyleClassProperty(element.className, "padding-top"));
  var bottomPaddingSize = parseInt(O$.getStyleClassProperty(element.className, "padding-bottom"));
  var leftMarginSize = parseInt(O$.getStyleClassProperty(element.className, "margin-left"));
  var rightMarginSize = parseInt(O$.getStyleClassProperty(element.className, "margin-right"));
  var topMarginSize = parseInt(O$.getStyleClassProperty(element.className, "margin-top"));
  var bottomMarginSize = parseInt(O$.getStyleClassProperty(element.className, "margin-bottom"));

  element._storedSizeProperties = {
    paddingLeft: leftPaddingSize,
    paddingRight: rightPaddingSize,
    paddingTop: topPaddingSize,
    paddingBottom: bottomPaddingSize,
    borderLeft: leftBorderSize,
    borderRight: rightBorderSize,
    borderTop: topBorderSize,
    borderBottom: bottomBorderSize,
    marginsWidth: leftMarginSize + rightMarginSize,
    marginsHeight: topMarginSize + bottomMarginSize,
    paddingsAndBordersWidth: leftBorderSize + leftPaddingSize + rightBorderSize + rightPaddingSize,
    paddingsAndBordersHeight: topBorderSize + topPaddingSize + bottomBorderSize + bottomPaddingSize,
    paddingsAndBordersAndMarginsWidth: leftBorderSize + leftPaddingSize + leftMarginSize + rightBorderSize + rightPaddingSize + rightMarginSize,
    paddingsAndBordersAndMarginsHeight: topBorderSize + topPaddingSize + topMarginSize + bottomBorderSize + bottomPaddingSize + bottomMarginSize
  };
};

O$._setInnerElementOuterWidth = function(element, outerWidth, useDoubleBuffering) { //elementOuterWidth must be a number

  if (O$._isExplorerQuirksMode()) {
    var width = (outerWidth - element._storedSizeProperties.marginsWidth) + "px";
  } else {
    width = (outerWidth - element._storedSizeProperties.paddingsAndBordersAndMarginsWidth) + "px";
  }

  if (useDoubleBuffering) {
    element._newStyle.width = width;
  } else {
    element.style.width = width;
  }
};

O$._setInnerElementOuterHeight = function(element, outerHeight, useDoubleBuffering) { //elementOuterHeight must be a number
  if (O$._isExplorerQuirksMode()) {
    var height = (outerHeight - element._storedSizeProperties.marginsHeight) + "px";
  } else {
    height = (outerHeight - element._storedSizeProperties.paddingsAndBordersAndMarginsHeight) + "px";
  }

  if (useDoubleBuffering) {
    element._newStyle.height = height;
  } else {
    element.style.height = height;
  }
};

O$._setInnerElementOuterLeftTopCorner = function(element, outerLeft, outerTop, useDoubleBuffering) { //outerLeft and outerTop must be a number
  var parent = element.parentNode;
  var left = (outerLeft + parent._storedSizeProperties.paddingLeft) + "px";
  var top = (outerTop + parent._storedSizeProperties.paddingTop) + "px";

  if (useDoubleBuffering) {
    element._newStyle.left = left;
    element._newStyle.top = top;
  } else {
    element.style.left = left;
    element.style.top = top;
  }
};

O$._setInnerElementOuterRightTopCorner = function(element, outerRight, outerTop, useDoubleBuffering) { //outerRight and outerTop must be a number
  var parent = element.parentNode;
  var right = (outerRight + parent._storedSizeProperties.paddingRight) + "px";
  var top = (outerTop + parent._storedSizeProperties.paddingTop) + "px";

  if (useDoubleBuffering) {
    element._newStyle.right = right;
    element._newStyle.top = top;
  } else {
    element.style.right = right;
    element.style.top = top;
  }
};

O$._setInnerElementOuterRightBottomCorner = function(element, outerRight, outerBottom, useDoubleBuffering) { //outerRight and outerBottom must be a number
  var parent = element.parentNode;
  var right = (outerRight + parent._storedSizeProperties.paddingRight) + "px";
  var bottom = (outerBottom + parent._storedSizeProperties.paddingBottom) + "px";

  if (useDoubleBuffering) {
    element._newStyle.right = right;
    element._newStyle.bottom = bottom;
  } else {
    element.style.right = right;
    element.style.bottom = bottom;
  }
};

// to fix bugs

O$._isCanBeDoubleBufferedElement = function(element) {
  if (element._newStyle) {
    return true;
  } else {
    if (element == document) {
      return false;
    }
    return O$._isCanBeDoubleBufferedElement(element.parentNode);
  }
};

O$._checkAsRootDoubleBufferedElement = function(element) {
  if (!O$._isCanBeDoubleBufferedElement(element.parentNode)) {
    element._isRootDoubleBufferedElement = true;
  }
};

O$._bugFix_divNegativeSizeBug = function(element, useDoubleBuffering) {
  if (useDoubleBuffering) {
    element._newStyle.display = "block";
    if (parseFloat(element._newStyle.height) < 0) {
      element._newStyle.height = "0px";
      element._newStyle.display = "none";
    }
    if (parseFloat(element._newStyle.width) < 0) {
      element._newStyle.width = "0px";
      element._newStyle.display = "none";
    }
  }
  else {
    element.style.display = "block";
    if (parseFloat(element.style.height) < 0) {
      element.style.height = "0px";
      element.style.display = "none";
    }
    if (parseFloat(element.style.width) < 0) {
      element.style.width = "0px";
      element.style.display = "none";
    }
  }
};

//------------------  resizableElement methods  -------------------

O$._subscribeToOnresizeEvent = function(element, func) {
  var widthIsNotFixed = true;
  var heightIsNotFixed = true;
  var targetElement = element;
  while (widthIsNotFixed || heightIsNotFixed) {
    if (targetElement._isNotResizableElement) {
      break;
    }
    else if (targetElement._isResizableElement) {
      O$._subscribeToTargetOnresizeEvent(element, targetElement, func);
      return;
    }
    else if (targetElement == document) {
        O$._subscribeToTargetOnresizeEvent(element, window, func);
        return;
      } else {
        var width = O$.getElementStyle(targetElement, "width", false);
        var height = O$.getElementStyle(targetElement, "height", false);
        if (O$._isPixelValue(width)) {
          widthIsNotFixed = false;
        }
        if (O$._isPixelValue(height)) {
          heightIsNotFixed = false;
        }
        targetElement = targetElement.parentNode;
      }
  }
  element._isNotResizableElement = true;

};

O$._subscribeToTargetOnresizeEvent = function(element, targetElement, func) {
  if (O$.isExplorer()) {
    targetElement.attachEvent("onresize", function(event) {
      event.cancelBubble = true;
      func(event);
    });
  } else {

    if (O$.isMozillaFF2() && (targetElement != window)) {
      targetElement.addEventListener("onresize", function(event) { //previous event type was "resize", but FireFox2 block it
        event.stopPropagation();
        func(event);
      }, false);
    } else {
      targetElement.addEventListener("resize", function(event) {
        event.stopPropagation();
        func(event);
      }, false);
    }
  }
  element._isResizableElement = true;
};

O$._sendResizeEvent = function(element, phase) {
  if (O$.isExplorer()) {
    //todo for fully support of double buffering need to fire "onresize" event in IE
  } else if (O$.isMozillaFF2()) {
    var event = document.createEvent("Events");
    event.initEvent("onresize", false, true);
    event.phase = phase;
    element.dispatchEvent(event);
  } else {
    event = document.createEvent("Events");
    event.initEvent("resize", false, true);
    event.phase = phase;
    element.dispatchEvent(event);
  }
};

//-------------------  General purpose methods  --------------------

O$._mouseButton = function(event) {
  if (event.which == null)
  /* IE case */
    var button = (event.button == 0) ? "none"
            : ((event.button == 1) ? "left"
            : ((event.button == 2) ? "right"
            : ((event.button == 3) ? "left+right"
            : ((event.button == 4) ? "middle"
            : "NaN"))));
  else
  /* All others */
    button = (event.which == 1) ? "left"
            : ((event.which == 2) ? "middle"
            : ((event.which == 3) ? "right"
            : "NaN"));
  return button;
};

O$._disableMouseSelection = function() {
  if (O$.isMozillaFF())
    document.body.style.MozUserSelect = "none";
  else if (O$.isOpera())
  {
  } //todo implement disableMouseSelection in Opera
  else
    document.onselectstart = function () {
      return false;
    };
};

O$._enableMouseSelection = function() {
  if (O$.isMozillaFF())
    document.body.style.MozUserSelect = "normal";
  else if (O$.isOpera())
  {
  } //todo implement enableMouseSelection in Opera
  else
    document.onselectstart = "";
};

O$._createPseudoCSSStyle = function(cssStyle) {
  for (var index in cssStyle) {
    this[index] = cssStyle[index];
  }
  this.toString = function() {
    var result = "";
    for (var index in this) {
      if (index != "toString" && index != "applyTo")
        result += index + ": " + this[index] + "; ";
    }
    return result;
  };
  this.applyTo = function(style) {
    for (index in this) {
      if (index != "toString" && index != "applyTo") {
        style[index] = this[index];
      }
    }
  };
  return this;
};

O$._isPercentageValue = function(str) {
  return (str.indexOf(parseFloat(str).toString() + "%") != -1);
};

O$._isPixelValue = function(str) {
  if (!str) {
    return false;
  }
  return (str.indexOf(parseInt(str).toString() + "px") != -1);
};

O$._setAbsoluteCenterPosition = function(element) {
  element.style.position = "absolute";
  element.style.left = "50%";
  element.style.top = "50%";
  element.style.marginLeft = "-" + Math.round(element.offsetWidth / 2) + "px";
  element.style.marginTop = "-" + Math.round(element.offsetHeight / 2) + "px";
};

O$._setImageAbsoluteCenterPosition = function(imgElement) {
  if (imgElement.readyState == "complete") {
    O$._setAbsoluteCenterPosition(imgElement);
  } else {
    setTimeout(O$._setImageAbsoluteCenterPosition(imgElement), 100);
  }
};

O$._createMouseSattelite = function(element, imgSrc) { //element must be absolute positioned
  function addFunction() {
    element.appendChild(mouseSattelite);
    O$._mouseSatteliteTrackMouse(mouseSattelite);
    element.onmouseover = function () {
      O$._mouseSatteliteShow(mouseSattelite);
    };
    element.onmouseout = function () {
      O$._mouseSatteliteHide(mouseSattelite);
    };
    O$._bugFix_IEPng(mouseSattelite);
  }

  var mouseSattelite = document.createElement("img");
  mouseSattelite.style.position = "absolute";
  mouseSattelite.style.padding = "0px";
  mouseSattelite.style.margin = "0px";
  mouseSattelite.style.border = "0px solid gray";
  mouseSattelite.style.zIndex = 1;
  mouseSattelite._mouseTracking = true;
  mouseSattelite._visible = false;
  mouseSattelite._fixedX = false;
  mouseSattelite._fixedY = false;
  mouseSattelite._posX = 0;
  mouseSattelite._posY = 0;
  mouseSattelite._shiftX = 0;
  mouseSattelite._shiftY = 0;
  mouseSattelite.src = imgSrc;
  element._mouseSattelite = mouseSattelite;
  if (O$.isExplorer() && (document.readyState != "complete")) {
    O$.addLoadEvent(addFunction);
  } else {
    addFunction();
  }
};

O$._mouseSatteliteOnmousemove = function(mouseSattelite) {
  return function(event) {
    if (!mouseSattelite._fixedX) {
      mouseSattelite.style.left = (event.layerX + mouseSattelite._shiftX) + "px";           //todo something wrong
    } else {
      mouseSattelite.style.left = (mouseSattelite._posX + mouseSattelite._shiftX) + "px";
    }
    if (!mouseSattelite._fixedY) {
      mouseSattelite.style.top = (event.layerY + mouseSattelite._shiftY) + "px";
    } else {
      mouseSattelite.style.top = (mouseSattelite._posY + mouseSattelite._shiftY) + "px";
    }
  };
};

O$._mouseSatteliteShow = function(mouseSattelite) {
  mouseSattelite._visible = true;
  if (mouseSattelite._mouseTracking) {
    mouseSattelite._onmousemoveHandler = O$.addEventHandler(mouseSattelite.parentNode, "mousemove", O$._mouseSatteliteOnmousemove(mouseSattelite), false);
    O$._mouseSatteliteOnmousemove(mouseSattelite);
  }
  mouseSattelite.style.display = "visible";
};

O$._mouseSatteliteHide = function(mouseSattelite) {
  mouseSattelite.style.display = "none";
  mouseSattelite._visible = false;
  if (mouseSattelite._mouseTracking) {
    mouseSattelite._onmousemoveHandler = O$.removeEventHandler(mouseSattelite.parentNode, "mousemove", O$._mouseSatteliteOnmousemove(mouseSattelite), false);
  }
};

O$._mouseSatteliteTrackMouse = function(mouseSattelite) {
  mouseSattelite._mouseTracking = true;
  if (mouseSattelite._visible) {
    mouseSattelite._onmousemoveHandler = O$.addEventHandler(mouseSattelite.parentNode, "mousemove", O$._mouseSatteliteOnmousemove(mouseSattelite), false);
  }
};

O$._mouseSatteliteUntrackMouse = function(mouseSattelite) {
  mouseSattelite._mouseTracking = false;
  if (mouseSattelite._visible) {
    mouseSattelite._onmousemoveHandler = O$.removeEventHandler(mouseSattelite.parentNode, "mousemove", O$._mouseSatteliteOnmousemove(mouseSattelite), false);
  }
};

O$._bugFix_IEPng = function(imageElement) {
  if (!O$.isExplorer())
    return;
  imageElement.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + imageElement.src + "')";
  imageElement.src = "clear.gif";
};

O$._isExplorerQuirksMode = function() {
  if (window._of_isExplorerQuirksMode == undefined)
    var result = O$.isExplorer() && O$.isQuirksMode();
  if (result)
    window._of_isExplorerQuirksMode = true;
  return window._of_isExplorerQuirksMode;
};

O$._applyEventsObjectToElement = function(eventsObject, element) {
  for (var index in eventsObject) {
    element[index] = eventsObject[index];
  }
};

O$._setupRolloverClass = function(element, rolloverClass) {
  if (!rolloverClass) return;
  O$.setupHoverStateFunction(element, function(mouseInside) {
    O$.setStyleMappings(element, {_rolloverStyle: mouseInside ? rolloverClass : null});
  });
};