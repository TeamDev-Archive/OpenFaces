/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

PageBot.prototype.locateElementByInternalscript = function(locator, doc) {
  var window = selenium.browserbot.getCurrentWindow();
  selenium._locateElementByInternalscript = function() {
    return eval(locator);
  }
  return selenium._locateElementByInternalscript(locator);
}

Selenium.prototype.getQ__getChildNodesWithNames = function (node, nodeNames) {
  return q__getChildNodesWithNames(node, nodeNames);
}

Selenium.prototype.getQ__findElementByPath = function(node, childPath, ignoreNonExistingElements) {
  return q__findElementByPath(node, childPath, ignoreNonExistingElements);
}

Selenium.prototype.getQ__calculateElementStyleProperty = function (element, propertyName) {
  return q__calculateElementStyleProperty(this.page().getCurrentWindow().document, element, propertyName);
}

Selenium.prototype.getQ__getElementSize = function (element) {
  var size = q__getElementSize(window.document, element);
  return size.width + "," + size.height;
}

Selenium.prototype.getQ__getWindowSize = function () {
  var size = q__getVisibleAreaSize(this.page().getCurrentWindow());
  return size.width + "," + size.height;
}

Selenium.prototype.getQ__getElementPos = function (element, relativeToNearestContainingBlock) {
  var pos = q__getElementPos(this.page().getCurrentWindow().document, element, relativeToNearestContainingBlock);
  return pos.left + "," + pos.top;
}

Selenium.prototype.getQ__getElementRect = function (element, relativeToNearestContainingBlock) {
  var rect = q__getElementBorderRectangle(this.page().getCurrentWindow().document, element, relativeToNearestContainingBlock);
  return rect.x + "," + rect.y + "," + rect.width + "," + rect.height;
}

Selenium.prototype.getQ__checkElementBorderProperty = function (element, borderProperty, value) {
  var testElement = document.createElement('div');
  document.body.appendChild(testElement);
  testElement.style[borderProperty] = value;
  var expectedValue = q__calculateBorderProperty(testElement, borderProperty);
  var actualValue = q__calculateBorderProperty(element, borderProperty);
  if (expectedValue != actualValue)
    return "Expected[" + expectedValue + "] != Actual[" + actualValue + "]";
  else
    return "";
}

function q__calculateBorderProperty(element, propertyName) {
  var doc = this.page().getCurrentWindow().document;
  return  q__calculateElementStyleProperty(doc, element, propertyName + "-width") + " " +
          q__calculateElementStyleProperty(doc, element, propertyName + "-style") + " " +
          q__calculateElementStyleProperty(doc, element, propertyName + "-color");
}

/**
 * Returns concatenated contents of all text nodes inside of the specified element.
 * This method exists because the standard "textContent" attribute returns script contents
 * in addition to the displayed text.
 */
Selenium.prototype.getQ__getNodeText = function (element) {
  var nodeName = element.nodeName;
  if (nodeName) nodeName = nodeName.toLowerCase();
  if (nodeName == "#text")
    return element.data;
  if (nodeName == "style" || nodeName == "script" || nodeName == "#comment")
    return "";
  var result = "";
  var childNodes = element.childNodes;
  for (var i = 0, count = childNodes.length; i < count; i++) {
    var node = childNodes[i];
    result += this.getQ__getNodeText(node);
  }
  return result;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////  BELOW IS A PARTIAL COPY OF UTIL.JS FILE FROM OPENFACES LIBRARY FOR JSF //////////////
////////////////////////////// (See modifications from original version marked with <MOD> pseudo-tag) //////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


function q__assert(value, message) {
  if (value !== null && value !== undefined && value !== false)
    return;
  alert(message);
}

// ----------------- BROWSER DETECTION ---------------------------------------------------

function q__userAgentContains(browserName) {
  return navigator.userAgent.toLowerCase().indexOf(browserName.toLowerCase()) > -1;
}

function q__isStrictMode(doc) {
  return doc.compatMode == "CSS1Compat";
}
function q__isQuirksMode(doc) {
  return doc.compatMode != "CSS1Compat";
}

function q__isMozillaFF() {
  if (window._q_mozilla == undefined)
    window._q_mozilla = q__userAgentContains("mozilla") &&
                        !q__userAgentContains("msie") &&
                        !q__userAgentContains("safari");
  return window._q_mozilla;
}

function q__isMozillaFF2() {
  return q__isMozillaFF() && !q__userAgentContains("Firefox/3.0");
}

function q__isMozillaFF3() {
  return q__isMozillaFF() && q__userAgentContains("Firefox/3.0");
}

function q__isExplorer() {
  if (window._q_explorer == undefined)
    window._q_explorer = q__userAgentContains("msie") && !q__userAgentContains("opera");
  return window._q_explorer;
}

function q__isExplorer7() {
  if (window._q_explorer7 == undefined)
    window._q_explorer7 = q__isExplorer() && q__userAgentContains("MSIE 7");
  return window._q_explorer7;
}

function q__isOpera9AndLate() {
  if (window._q_opera9 == undefined) {
    if (q__isOpera()) {
      window._q_opera9 = /\bOpera(\s|\/)(\d{2,}|([9]){1})/i.test(navigator.userAgent);
    }
  }
  return window._q_opera9;
}

function q__isSafari3AndLate() {
  if (window._q_safari3 == undefined)
    if (q__isSafari()) {
      window._q_safari3 = /\bversion(\s|\/)(\d{2,}|[3-9]{1})/i.test(navigator.userAgent);
    }
  return window._q_safari3;
}

function q__isSafariOnMac() {
  if (window._q_safariOnMac == undefined) {
    window._q_safariOnMac = q__isSafari() && q__userAgentContains("Macintosh");
  }
  return window._q_safariOnMac;
}

function q__isSafariOnWindows() {
  if (window._q_safariOnWindows == undefined) {
    window._q_safariOnWindows = q__isSafari() && q__userAgentContains("Windows");
  }
  return window._q_safariOnWindows;
}

function q__isSafari2() {
  if (window._q_safari2 == undefined) {
    window._q_safari2 = q__isSafari() && !q__isSafari3AndLate() && !q__isChrome();
  }
  return window._q_safari2;
}

function q__isChrome() {
  if (window._q_chrome == undefined)
    window._q_chrome = q__userAgentContains("Chrome");
  return window._q_chrome;
}

function q__isOpera() {
  if (window._q_opera == undefined)
    window._q_opera = q__userAgentContains("opera");
  return window._q_opera;
}

function q__isSafari() {
  if (window._q_safari == undefined)
    window._q_safari = q__userAgentContains("safari");
  return window._q_safari;
}

// ----------------- STRING, ARRAYS, OTHER LANGUAGE UTILITIES ---------------------------------------------------

function q__minDefined(value1, value2) {
  if (value1 != undefined && value2 != undefined && !isNaN(value1) && !isNaN(value2))
    return Math.min(value1, value2);
  return value1 !== undefined && !isNaN(value1) ? value1 : value2;
}

function q__maxDefined(value1, value2) {
  if (value1 != undefined && value2 != undefined && !isNaN(value1) && !isNaN(value2))
    return Math.max(value1, value2);
  return value1 !== undefined && !isNaN(value1) ? value1 : value2;
}

function q__stringEndsWith(str, ending) {
  if (!str || !ending)
    return false;
  var endingLength = ending.length;
  var length = str.length;
  if (length < endingLength)
    return false;
  var actualEnding = str.substring(length - endingLength, length);
  return actualEnding == ending;
}


// ----------------- DOM FUNCTIONS ---------------------------------------------------

function q__getChildNodesWithNames(node, nodeNames) {
  var selectedChildren = new Array();
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
}

function q__findElementByPath(node, childPath, ignoreNonExistingElements) {
  var separatorIndex = childPath.indexOf("/");
  var locator = separatorIndex == -1 ? childPath : childPath.substring(0, separatorIndex);
  var remainingPath = separatorIndex != -1 ? childPath.substring(separatorIndex + 1) : null;

  var bracketIndex = locator.indexOf("[");
  var childElementName = bracketIndex == -1 ? locator : locator.substring(0, bracketIndex);
  var childElementIndex;
  if (bracketIndex == -1) {
    childElementIndex = 0;
  } else {
    q__assert(q__stringEndsWith(locator, "]"), "q__findElementByPath: unparsable element locator - non-matching brackets: " + childPath);
    var indexStr = locator.substring(bracketIndex + 1, locator.length - 1);
    try {
      childElementIndex = parseInt(indexStr);
    } catch (e) {
      if (ignoreNonExistingElements)
        return null;
      throw "q__findElementByPath: Couldn't parse child index (" + indexStr + "); childPath = " + childPath;
    }
  }
  var childrenByName = q__getChildNodesWithNames(node, [childElementName]);
  if (childrenByName.length == 0) {
    if (ignoreNonExistingElements)
      return null;
    throw "q__findElementByPath: Couldn't find child nodes by element name: " + childElementName + " ; childPath = " + childPath;
  }
  var child = childrenByName[childElementIndex];
  if (!child) {
    if (ignoreNonExistingElements)
      return null;
    throw "q__findElementByPath: Child not found by index: " + childElementIndex + " ; childPath = " + childPath;
  }
  if (remainingPath == null)
    return child;
  else
    return q__findElementByPath(child, remainingPath);
}


function q__calculateElementStyleProperty(doc, element, propertyName, enableValueCaching) { // <MOD>added doc param</MOD>
  var propertyValues = q__calculateElementStyleProperties(doc, element, [propertyName], enableValueCaching);
  return propertyValues[propertyName];
}

function q__calculateElementStyleProperties(doc, element, propertyNames, enableValueCaching) {
  if (enableValueCaching) {
    if (!element._cachedStyleValues)
      element._cachedStyleValues = {};
  }
  var propertyValues = {};
  var currentStyle = element.currentStyle;
  var computedStyle = (!currentStyle) ? doc.defaultView.getComputedStyle(element, "") : null;
  for (var i = 0, count = propertyNames.length; i < count; i++) {
    var propertyName = propertyNames[i];
    var capitalizedPropertyName = q__capitalizeCssPropertyName(propertyName);

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
}


function q__capitalizeCssPropertyName(propertyName) {
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
}

function q__getStyleClassProperties(styleClass, propertyNames) {
  if (!styleClass || propertyNames.length == 0)
    return {};
  var classNames = styleClass.split(" ");
  var classSelectors = new Array();
  for (var i = 0, count = classNames.length; i < count; i++) {
    var className = classNames[i];
    if (className)
      classSelectors.push("." + className);
  }
  var cssRules = q__findCssRules(classSelectors);
  if (!cssRules)
    return {};

  var propertyCount = propertyNames.length;
  var propertyValues = new Object();
  var propertyImportantFlags = new Array();
  for (var i = 0, count = cssRules.length; i < count; i++) {
    var cssRule = cssRules[i];
    var ruleStyle = cssRule.style;
    for (var propertyIndex = 0; propertyIndex < propertyCount; propertyIndex++) {
      var propertyName = propertyNames[propertyIndex];
      var capitalizedPropertyName = q__capitalizeCssPropertyName(propertyName);
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
}


function q__getContainingBlock(doc, element, ignoreThisElement) {
  for (var el = !ignoreThisElement ? element : element.parentNode; el; el = el.parentNode) {
    if (q__isContainingBlock(doc, el))
      return el;
  }
  return null;
}

// ----------------- ABSOLUTE POSITIONING / METRICS UTILITIES ---------------------------------------------------

/*
 q__calculateNumericStyleProperty doesn't work when calculating margin on a table under Mozilla 2 for some reason,
 so here's an alternative implementation that works for this case.
 */
function q__calculateMozillaMargins(element) {
  var styleClassProperties = {};
  styleClassProperties = q__getStyleClassProperties(
          element.className, ["marginLeft", "marginRight", "marginTop", "marginBottom"]);
  var marginLeft = q__calculateNumericCSSValue(styleClassProperties.marginLeft);
  var marginRight = q__calculateNumericCSSValue(styleClassProperties.marginRight);
  var marginTop = q__calculateNumericCSSValue(styleClassProperties.marginTop);
  var marginBottom = q__calculateNumericCSSValue(styleClassProperties.marginBottom);
  marginLeft = Math.max(marginLeft, q__calculateNumericCSSValue(element.style.marginLeft));
  marginRight = Math.max(marginRight, q__calculateNumericCSSValue(element.style.marginRight));
  marginTop = Math.max(marginTop, q__calculateNumericCSSValue(element.style.marginTop));
  marginBottom = Math.max(marginBottom, q__calculateNumericCSSValue(element.style.marginBottom));
  return {marginLeft: marginLeft, marginRight: marginRight, marginTop: marginTop, marginBottom: marginBottom};
}

function q__getPageScrollPos() {
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

 See also: q__setElementPos, q__getElementBorderRectangle, q__setElementBorderRectangle.
 */
function q__getElementPos(doc, element, relativeToNearestContainingBlock) {
  var left, top;

  if (element.getBoundingClientRect) {
    var rect = element.getBoundingClientRect();
    left = rect.left;
    top = rect.top;
    var containingBlock;
    if (relativeToNearestContainingBlock) {
      containingBlock = q__getContainingBlock(doc, element, true);
      if (containingBlock) {
        var containingRect = containingBlock.getBoundingClientRect();
        left += containingBlock.scrollLeft - containingRect.left - containingBlock.clientLeft;
        top += containingBlock.scrollTop - containingRect.top - containingBlock.clientTop;
      }
    }
    if (!containingBlock) {
      var pageScrollPos = q__getPageScrollPos();
      left += pageScrollPos.x;
      top += pageScrollPos.y;
      if (q__isExplorer()) {
        if (q__isQuirksMode(doc)) {
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
  if (q__isMozillaFF2() && q__isStrictMode(doc) && element.tagName && element.tagName.toLowerCase() == "table") {
    var margins = q__calculateMozillaMargins(element);
    left += margins.marginLeft;
    top += margins.marginTop;
  }

  while (element) {
    left += element.offsetLeft;
    top += element.offsetTop;
    var thisIsContainingBlock = q__isContainingBlock(doc, element);

    var offsetParent = element.offsetParent;
    if (!offsetParent)
      break;

    if (q__isMozillaFF2() && q__calculateElementStyleProperty(doc, offsetParent, "overflow") != "visible") {
      left += q__calculateNumericStyleProperty(doc, offsetParent, "border-left-width");
      top += q__calculateNumericStyleProperty(doc, offsetParent, "border-top-width");
    }

    var parentIsContainingBlock = q__isContainingBlock(doc, offsetParent);
    if (relativeToNearestContainingBlock && parentIsContainingBlock) {
      if (offsetParent.tagName.toLowerCase() == "div" && (q__isOpera9AndLate() || q__isSafari2())) {
        if (q__calculateElementStyleProperty(doc, offsetParent, "border-style") != "none") {
          var borderLeftWidth = q__calculateNumericStyleProperty(doc, offsetParent, "border-left-width");
          var borderTopWidth = q__calculateNumericStyleProperty(doc, offsetParent, "border-top-width");
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

      //      if (q__calculateElementStyleProperty(parent, "overflow") != "visible") {
      //        left += q__calculateNumericStyleProperty(parent, "padding-left");
      //        top += q__calculateNumericStyleProperty(parent, "padding-top");
      //      }

      if (!thisIsContainingBlock) {
        // containing blocks already have the following corrections as part of offsetLeft/offsetTop
        if (q__isMozillaFF2()) {
          if (parentNodeName == "td") {
            left -= q__calculateNumericStyleProperty(doc, parent, "border-left-width");
            top -= q__calculateNumericStyleProperty(doc, parent, "border-top-width");
          }
          if (parentNodeName == "table" && q__isStrictMode(doc)) {
            var parentMargins = q__calculateMozillaMargins(parent);
            left += parentMargins.marginLeft;
            top += parentMargins.marginTop;
          }
        }
        if (q__isSafari()) {
          if (parentNodeName == "table") {
            left += q__calculateNumericStyleProperty(doc, parent, "border-left-width");
            top += q__calculateNumericStyleProperty(doc, parent, "border-top-width");
          }
        }
      }

      if (parent == offsetParent)
        break;
    }

    // account for offsetParent's border
    if (!q__isSafari2()) {
      var lowerCaseTagName = offsetParent.tagName.toLowerCase();
      if (lowerCaseTagName == "table" && q__isSafari3AndLate()) {
        var border = q__calculateNumericCSSValue(offsetParent.border);
        if (border > 0) {
          left += border;
          top += border;
        }
      } else {
        if (lowerCaseTagName == "div" || lowerCaseTagName == "td") {
          if (!q__isOpera9AndLate()) { // border in Opera 9 included in offsetLeft and offsetTop
            left += (offsetParent.clientLeft == undefined ? q__calculateNumericStyleProperty(doc, offsetParent, "border-left-width") : offsetParent.clientLeft);
            top += (offsetParent.clientTop == undefined ? q__calculateNumericStyleProperty(doc, offsetParent, "border-top-width") : offsetParent.clientTop);
          }
        }
      }
    }

    element = offsetParent;
  }
  return {left: left, top: top};
}

/*
 Returns the q__Rectangle object that corresponds to the bounding rectangle of the passed element. This method takes
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

 See also: q__setElementBorderRectangle, q__getElementPos, q__setElementPos.
 */
function q__getElementBorderRectangle(doc, element, relativeToNearestContainingBlock, cachedDataContainer) {
  if (cachedDataContainer) {
    if (!element._of_getElementRectangle)
      element._of_getElementRectangle = {}
    if (element._of_getElementRectangle._currentCache == cachedDataContainer)
      return element._of_getElementRectangle._cachedValue;
  }
  var pos = q__getElementPos(doc, element, relativeToNearestContainingBlock);
  var size = q__getElementSize(doc, element);
  var rect = new q__Rectangle(pos.left, pos.top, size.width, size.height);
  if (cachedDataContainer) {
    element._of_getElementRectangle._currentCache = cachedDataContainer;
    element._of_getElementRectangle._cachedValue = rect;
  }
  return rect;
}

function q__getElementSize(doc, element) {
  var width = element.offsetWidth;
  var height = element.offsetHeight;
  // Mozilla 2.0.x strict reports margins on tables to be part of table when measuring it with offsetXXX attributes
  if (q__isMozillaFF2() && q__isStrictMode(doc) && element.tagName && element.tagName.toLowerCase() == "table") {
    var margins = q__calculateMozillaMargins(element);
    width -= margins.marginLeft + margins.marginRight;
    height -= margins.marginTop + margins.marginBottom;
  }
  return {width: width, height: height};
}

/**
 * Introduced according to CSS spec http://www.w3.org/TR/REC-CSS2/visudet.html#containing-block-details
 * See JSFC-2045 Popups displayed incorrectly under JBoss Portal
 */
function q__isContainingBlock(doc, elt) {
  q__assert(elt, "elt is null");
  if (elt.nodeName == "#document") {
    // q__calculateElementStyleProperty fails to determine position on the document element, and
    // document element can't have a non-static position
    return false;
  }
  var position = q__calculateElementStyleProperty(doc, elt, "position");
  if (!position) return false;
  return position != "static";
}

function q__Rectangle(x, y, width, height) {
  this.x = x;
  this.y = y;
  this.width = width;
  this.height = height;
}

q__Rectangle.prototype.clone = function() {
  return new q__Rectangle(this.x, this.y, this.width, this.height);
};

q__Rectangle.prototype.getMinX = function() {
  return this.x;
};
q__Rectangle.prototype.getMinY = function() {
  return this.y;
};
q__Rectangle.prototype.getMaxX = function() {
  var result = this.x + this.width;
  return result;
};
q__Rectangle.prototype.getMaxY = function() {
  var result = this.y + this.height;
  return result;
};

q__Rectangle.prototype.addRectangle = function(rect) {
  q__assert(rect, "rect parameter should be passed");
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
  this.y = y1
  this.width = x2 - x1;
  this.height = y2 - y1;
}

q__Rectangle.prototype.intersectWith = function(rect) {
  q__assert(rect, "rect parameter should be passed");

  var x1 = q__maxDefined(this.getMinX(), rect.getMinX());
  var x2 = q__minDefined(this.getMaxX(), rect.getMaxX());
  var y1 = q__maxDefined(this.getMinY(), rect.getMinY());
  var y2 = q__minDefined(this.getMaxY(), rect.getMaxY());
  if (x2 < x1)
    x2 = x1;
  if (y2 < y1)
    y2 = y1;

  this.x = x1;
  this.y = y1

  this.width = x1 !== undefined && x2 !== undefined ? x2 - x1 : undefined;
  this.height = y1 !== undefined && y2 !== undefined ? y2 - y1 : undefined;
}

q__Rectangle.prototype.intersects = function(rect) {
  var x1 = this.getMinX();
  var x2 = this.getMaxX();
  var rectX1 = rect.getMinX();
  var rectX2 = rect.getMaxX();
  var y1 = this.getMinY();
  var y2 = this.getMaxY();
  var rectY1 = rect.getMinY();
  var rectY2 = rect.getMaxY();

  return (rectX2 > x1 && rectY2 > y1 && rectX1 < x2 && rectY1 < y2);
}

q__Rectangle.prototype.containsRectangle = function(rect) {
  var x1 = this.getMinX();
  var x2 = this.getMaxX();
  var rectX1 = rect.getMinX();
  var rectX2 = rect.getMaxX();
  var y1 = this.getMinY();
  var y2 = this.getMaxY();
  var rectY1 = rect.getMinY();
  var rectY2 = rect.getMaxY();

  return (x1 <= rectX1 && x2 >= rectX2 && y1 <= rectY1 && y2 >= rectY2);
}

q__Rectangle.prototype.containsPoint = function(x, y) {
  return x >= this.getMinX() && x <= this.getMaxX() &&
         y >= this.getMinY() && y <= this.getMaxY();
}

function q__calculateNumericStyleProperty(doc, element, propertyName, enableValueCaching) {
  if (q__isExplorer() || q__isOpera()) {
    // border "medium none black" under IE is actually displayed with zero width because of "none" style, so we should
    // skip calculating "medium" width and just return 0 in such cases.
    var capitalizedPropertyName = q__capitalizeCssPropertyName(propertyName);
    if (q__stringStartsWith(capitalizedPropertyName, "border") && q__stringEndsWith(capitalizedPropertyName, "Width")) {
      var borderStylePropertyName = capitalizedPropertyName.substring(0, capitalizedPropertyName.length - "Width".length) + "Style";
      if (q__calculateElementStyleProperty(doc, element, borderStylePropertyName) == "none")
        return 0;
    }
  }
  var str = q__calculateElementStyleProperty(doc, element, propertyName, enableValueCaching);
  var result = q__calculateNumericCSSValue(str);
  return result;
}

function q__calculateNumericCSSValue(value) {
  if (!value)
    return 0;
  if (!isNaN(1 * value))
    return 1 * value;
  if (q__stringEndsWith(value, "px"))
    return 1 * value.substring(0, value.length - 2);
  if (value == "auto")
    return 0; // todo: can't calculate "auto" (e.g. from margin property) on a simulated border -- consider simulating such "non-border" values on other properties

  if (!window._q_nonPixelValueMeasurements)
    window._q_nonPixelValueMeasurements = [];
  var pixelValue = window._q_nonPixelValueMeasurements[value];
  if (pixelValue != undefined)
    return pixelValue;

  var outerDiv = window._q_nonPixelMeasurements_outerDiv;
  var innerDiv = window._q_nonPixelMeasurements_innerDiv;
  if (!outerDiv) {
    outerDiv = document.createElement("div");
    //    outerDiv.style.position = "absolute";
    //    outerDiv.style.left = "0px"
    //    outerDiv.style.top = "0px"
    outerDiv.style.padding = "0px";
    outerDiv.style.margin = "0px";
    innerDiv = document.createElement("div");
    innerDiv.style.padding = "0px";
    innerDiv.style.margin = "0px";
    outerDiv.appendChild(innerDiv);
    window._q_nonPixelMeasurements_outerDiv = outerDiv;
    window._q_nonPixelMeasurements_innerDiv = innerDiv;
  }
  outerDiv.style.border = value + " solid white";
  document.body.appendChild(outerDiv);
  pixelValue = innerDiv.offsetTop - outerDiv.offsetTop;
  document.body.removeChild(outerDiv);
  window._q_nonPixelValueMeasurements[value] = pixelValue;
  return pixelValue;
}

function q__getVisibleAreaSize(win) {
  var width = 0, height = 0;
  if (q__isMozillaFF()) {
    if (q__isQuirksMode(win.document)) {
      width = win.document.body.clientWidth;
      height = win.document.body.clientHeight;
    } else {
      width = win.document.documentElement.clientWidth;
      height = win.document.documentElement.clientHeight;
    }
  } else if (q__isSafari()) {
    width = win.document.documentElement.clientWidth;
    height = win.document.documentElement.clientHeight;
  } else if (typeof win.innerWidth == "number") {
    width = win.innerWidth;
    height = win.innerHeight;
  } else if (win.document.documentElement && (win.document.documentElement.clientWidth || win.document.documentElement.clientHeight)) {
    width = win.document.documentElement.clientWidth;
    height = win.document.documentElement.clientHeight;
  } else if (win.document.body && (win.document.body.clientWidth || win.document.body.clientHeight)) {
    width = win.document.body.clientWidth;
    height = win.document.body.clientHeight;
  }
  return {width : width, height : height};
}


