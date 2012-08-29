/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

var Selenium = {
  getXPath:function (element) {
    if (element.id !== '') {
      return "//*[@id='"+element.id+"']";
    }
    if (element === document.body) {
      return element.tagName
    }
    var a = 0;
    var e = element.parentNode.childNodes;
    for (var b = 0; b < e.length; b++) {
      var d = e[b];
      if (d === element) {
        return Selenium.getXPath(element.parentNode) + '/' + element.tagName + '[' + (a + 1) + ']'
      }
      if (d.nodeType === 1 && d.tagName === element.tagName) {
        a++
      }
    }
  },
  findElement:function (locator) {
    if (locator.indexOf("//") == 0) {
      var element = document.evaluate(locator, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
      return element;
    } else {
      var obj;
      if (locator.indexOf("Selenium.findElement") == 0) {
        var parts = locator.split("/");
        obj = eval(parts[0]);
        for (var i = 1; i < parts.length; i++) {
          eval("obj = obj." + parts[i]);
        }
        return obj;
      } else {
        var rawPath = locator.replace(/\//g, ".");
        if (!!(document.getElementById(rawPath))) return document.getElementById(rawPath);
        try {
          eval("obj = " + rawPath);
        } catch (exception) {
          return null;
        }
        return obj;
      }
    }
  },

  getQ__getChildNodesWithNames:function (node, nodeNames) {
    return q__getChildNodesWithNames(node, nodeNames);
  },

  getQ__findElementByPath:function (node, childPath, ignoreNonExistingElements) {
    return q__findElementByPath(node, childPath, ignoreNonExistingElements);
  },

  getQ__calculateElementStyleProperty:function (element, propertyName) {
    return q__calculateElementStyleProperty(document, element, propertyName);
  },

  getQ__getElementSize:function (element) {
    var size = O$_getElementSize(document, element);
    return size.width + "," + size.height;
  },

  getQ__getWindowSize:function () {
    var size = q__getVisibleAreaSize(window);
    return size.width + "," + size.height;
  },

  getQ__getElementPos:function (element, relativeToNearestContainingBlock) {
    var pos = O$_getElementPos(
            window,
            document,
            element, relativeToNearestContainingBlock);
    return pos.x + "," + pos.y;
  },

  getQ__getElementRect:function (element, relativeToNearestContainingBlock) {
    var rect = O$_getElementBorderRectangle(
            window,
            document,
            element, relativeToNearestContainingBlock);
    return rect.x + "," + rect.y + "," + rect.width + "," + rect.height;
  },
  q__calculateBorderProperty:function (element, propertyName) {
    var doc = document;
    return  q__calculateElementStyleProperty(doc, element, propertyName + "-width") + " " +
            q__calculateElementStyleProperty(doc, element, propertyName + "-style") + " " +
            q__calculateElementStyleProperty(doc, element, propertyName + "-color");
  },
  getQ__checkElementBorderProperty:function (element, borderProperty, value) {
    var testElement = document.createElement('div');
    document.body.appendChild(testElement);
    testElement.style[borderProperty] = value;
    var expectedValue = q__calculateBorderProperty(testElement, borderProperty);
    var actualValue = q__calculateBorderProperty(element, borderProperty);
    if (expectedValue != actualValue)
      return "Expected[" + expectedValue + "] != Actual[" + actualValue + "]";
    else
      return "";
  },


  /**
   * Returns concatenated contents of all text nodes inside of the specified element.
   * This method exists because the standard "textContent" attribute returns script contents
   * in addition to the displayed text.
   */
  getQ__getNodeText:function (element) {
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
      result += Selenium.getQ__getNodeText(node);
    }
    return result;
  }
}

//var Selenium = {
//
//  findElement:function (locator) {
//    if (locator.indexOf("//") == 0) {
//      var element = document.evaluate(locator, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
//      return element;
//    } else {
//      return document.getElementById(locator);
//    }
//  },
//
//  getQ__getChildNodesWithNames:function (node, nodeNames) {
//    return q__getChildNodesWithNames(node, nodeNames);
//  },
//
//  getQ__findElementByPath:function (node, childPath, ignoreNonExistingElements) {
//    return q__findElementByPath(node, childPath, ignoreNonExistingElements);
//  },
//
//  getQ__calculateElementStyleProperty:function (element, propertyName) {
//    return q__calculateElementStyleProperty(document, element, propertyName);
//  },
//
//  getQ__getElementSize:function (element) {
//    var size = O$_getElementSize(document, element);
//    return size.width + "," + size.height;
//  },
//
//  getQ__getWindowSize:function () {
//    var size = q__getVisibleAreaSize(window);
//    return size.width + "," + size.height;
//  },
//
//  getQ__getElementPos:function (element, relativeToNearestContainingBlock) {
//    var pos = O$_getElementPos(
//            window,
//            document,
//            element, relativeToNearestContainingBlock);
//    return pos.x + "," + pos.y;
//  },
//
//  getQ__getElementRect:function (element, relativeToNearestContainingBlock) {
//    var rect = O$_getElementBorderRectangle(
//            window,
//            document,
//            element, relativeToNearestContainingBlock);
//    return rect.x + "," + rect.y + "," + rect.width + "," + rect.height;
//  },
//  q__calculateBorderProperty:function (element, propertyName) {
//    var doc = document;
//    return  q__calculateElementStyleProperty(doc, element, propertyName + "-width") + " " +
//            q__calculateElementStyleProperty(doc, element, propertyName + "-style") + " " +
//            q__calculateElementStyleProperty(doc, element, propertyName + "-color");
//  },
//  getQ__checkElementBorderProperty:function (element, borderProperty, value) {
//    var testElement = document.createElement('div');
//    document.body.appendChild(testElement);
//    testElement.style[borderProperty] = value;
//    var expectedValue = q__calculateBorderProperty(testElement, borderProperty);
//    var actualValue = q__calculateBorderProperty(element, borderProperty);
//    if (expectedValue != actualValue)
//      return "Expected[" + expectedValue + "] != Actual[" + actualValue + "]";
//    else
//      return "";
//  },
//
//
//  /**
//   * Returns concatenated contents of all text nodes inside of the specified element.
//   * This method exists because the standard "textContent" attribute returns script contents
//   * in addition to the displayed text.
//   */
//  getQ__getNodeText:function (element) {
//    var nodeName = element.nodeName;
//    if (nodeName) nodeName = nodeName.toLowerCase();
//    if (nodeName == "#text")
//      return element.data;
//    if (nodeName == "style" || nodeName == "script" || nodeName == "#comment")
//      return "";
//    var result = "";
//    var childNodes = element.childNodes;
//    for (var i = 0, count = childNodes.length; i < count; i++) {
//      var node = childNodes[i];
//      result += Selenium.getQ__getNodeText(node);
//    }
//    return result;
//  }
//}

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
function q__isSafari() {
  if (window._q_safari == undefined)
    window._q_safari = q__userAgentContains("safari");
  return window._q_safari;
}

// ---- newer versions ---- remove older versions above when all usages are transferred to the new ones

function O$_userAgentContains(browserName) {
  return navigator.userAgent.toLowerCase().indexOf(browserName.toLowerCase()) > -1;
}

function O$_isStrictMode(doc) {
  return !O$_isQuirksMode(doc);
}
function O$_isQuirksMode(doc) {
  return doc.compatMode == "BackCompat";
}

function O$_isMozillaFF() {
  if (window.O$__mozilla == undefined)
    window.O$__mozilla = O$_userAgentContains("mozilla") &&
            !O$_userAgentContains("msie") &&
            !O$_userAgentContains("safari");
  return window.O$__mozilla;
}


function O$_isMozillaFF3() {
  return O$_isMozillaFF() && O$_userAgentContains("Firefox/3.");
}

function O$_isExplorer() {
  if (window.O$_explorer == undefined)
    window.O$_explorer = O$_userAgentContains("msie") && !O$_userAgentContains("opera");
  return window.O$_explorer;
}

function O$_isExplorer6() {
  if (window.O$_explorer6 == undefined)
    window.O$_explorer6 = O$_isExplorer() && O$_userAgentContains("MSIE 6");
  return window.O$_explorer6;
}

function O$_isExplorer7() {
  if (window.O$_explorer7 == undefined)
    window.O$_explorer7 = O$_isExplorer() && O$_userAgentContains("MSIE 7");
  return window.O$_explorer7;
}

function O$_isExplorer8() {
  if (window.O$_explorer8 == undefined)
    window.O$_explorer8 = O$_isExplorer() && O$_userAgentContains("MSIE 8");
  return window.O$_explorer8;
}

function O$_isExplorer9() {
  if (window.O$_explorer9 == undefined)
    window.O$_explorer9 = O$_isExplorer() && O$_userAgentContains("MSIE 9");
  return window.O$_explorer9;
}

function O$_isOpera9AndLate() {
  if (window.O$_opera9 == undefined) {
    if (O$_isOpera()) {
      window.O$_opera9 = /\bOpera(\s|\/)(\d{2,}|([9]){1})/i.test(navigator.userAgent);
    }
  }
  return window.O$_opera9;
}

function O$_isSafari3AndLate() {
  if (window.O$_safari3 == undefined)
    if (O$_isSafari()) {
      window.O$_safari3 = /\bversion(\s|\/)(\d{2,}|[3-9]{1})/i.test(navigator.userAgent);
    }
  return window.O$_safari3;
}

function O$_isSafari4AndLate() {
  if (window.O$_safari4 == undefined)
    if (O$_isSafari()) {
      window.O$_safari4 = /\bversion(\s|\/)(\d{2,}|[4-9]{1})/i.test(navigator.userAgent);
    }
  return window.O$_safari4;
}

function O$_isSafari3() {
  if (window.O$_safari3only == undefined) {
    window.O$_safari3only = O$_isSafari3AndLate() && !O$_isSafari4AndLate();
  }
  return window.O$_safari3only;
}

function O$_isSafariOnMac() {
  if (window.O$_safariOnMac == undefined) {
    window.O$_safariOnMac = O$_isSafari() && O$_userAgentContains("Macintosh");
  }
  return window.O$_safariOnMac;
}

function O$_isSafariOnWindows() {
  if (window.O$_safariOnWindows == undefined) {
    window.O$_safariOnWindows = O$_isSafari() && O$_userAgentContains("Windows");
  }
  return window.O$_safariOnWindows;
}

function O$_isSafari2() {
  if (window.O$_safari2 == undefined) {
    window.O$_safari2 = O$_isSafari() && !O$_isSafari3AndLate() && !O$_isChrome();
  }
  return window.O$_safari2;
}

function O$_isChrome() {
  if (window.O$_chrome == undefined)
    window.O$_chrome = O$_userAgentContains("Chrome");
  return window.O$_chrome;
}

function O$_isOpera() {
  if (window.O$_opera == undefined)
    window.O$_opera = O$_userAgentContains("opera");
  return window.O$_opera;
}

function O$_isOpera10_5AndLater() {
  return O$_isOperaLaterThan(10.5);
}

function O$_isOperaLaterThan(version) {
  if (!O$_isOpera())
    return false;

  if (/version[\/\s](\d+\.\d+)/.test(navigator.userAgent.toLowerCase())) {
    var operaVersionNumber = new Number(RegExp.$1);
    if (operaVersionNumber >= version) {
      return true;
    }
  }

  return false;
}

function O$_isSafari() { // todo: returns true for Chrome as well -- fix this and update usages to account Chrome
  if (window.O$_safari == undefined)
    window.O$_safari = O$_userAgentContains("safari");
  return window.O$_safari;
}


// ----------------- STRING, ARRAYS, OTHER LANGUAGE UTILITIES ---------------------------------------------------

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

// ----------------- ABSOLUTE POSITIONING / METRICS UTILITIES ---------------------------------------------------

O$_getNumericElementStyle = function (doc, element, propertyName, enableValueCaching, hundredPercentValue) {
  var capitalizedPropertyName = O$__capitalizeCssPropertyName(propertyName);
  if (O$_stringStartsWith(capitalizedPropertyName, "border") && O$_stringEndsWith(capitalizedPropertyName, "Width")) {
    var borderName = capitalizedPropertyName.substring(0, capitalizedPropertyName.length - "Width".length);
    var borderStyleName = borderName + "Style";
    if (O$_isOpera()) {
      if (O$_getElementStyle(doc, element, borderStyleName) == "none")
        return 0;
    } else if (O$_isExplorer()) {
      var borderWidthName = borderName + "Width";
      if (O$_getElementStyle(doc, element, borderStyleName) == "none" &&
              O$_getElementStyle(doc, element, borderWidthName) == "medium")
        return 0;
    }
  }
  var str = O$_getElementStyle(doc, element, propertyName, enableValueCaching);
  var result = O$_calculateNumericCSSValue(doc, str, hundredPercentValue);
  return result;
};


O$_findCssRule = function (doc, selector) {
  var rules = O$_findCssRules(doc, [selector]);
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
O$_findCssRules = function (doc, selectors) {
  if (!window.O$_cssRulesBySelectors) window.O$_cssRulesBySelectors = {};
  var selectorsKey = selectors.join(" ");
  var cachedRules = window.O$_cssRulesBySelectors[selectorsKey];
  if (cachedRules) return cachedRules;

  var styleSheets = doc.styleSheets;
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
  window.O$_cssRulesBySelectors[selectorsKey] = rulesFound;
  return rulesFound;
};

O$_getStyleClassProperty = function (doc, styleClass, propertyName) {
  var propertyValues = O$_getStyleClassProperties(doc, styleClass, [propertyName]);
  return propertyValues[propertyName];
};

O$_getStyleClassProperties = function (doc, styleClass, propertyNames) {
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
  var cssRules = O$_findCssRules(doc, classSelectors);
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
      var capitalizedPropertyName = O$__capitalizeCssPropertyName(propertyName);
      var dashizedPropertyName = O$__dashizeCssPropertyName(capitalizedPropertyName);
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


O$_stringStartsWith = function (str, text) {
  if (!str || !text)
    return false;
  var textLength = text.length;
  var length = str.length;
  if (length < textLength)
    return false;
  var actualStartText = str.substring(0, textLength);
  return actualStartText == text;
};

O$_stringEndsWith = function (str, ending) {
  if (!str || !ending)
    return false;
  var endingLength = ending.length;
  var length = str.length;
  if (length < endingLength)
    return false;
  var actualEnding = str.substring(length - endingLength, length);
  return actualEnding == ending;
};


O$_calculateNumericCSSValue = function (doc, value, hundredPercentValue) {
  if (!value)
    return 0;
  if (!isNaN(1 * value))
    return 1 * value;
  if (O$_stringEndsWith(value, "px"))
    return 1 * value.substring(0, value.length - 2);
  if (value == "auto")
    return 0; // todo: can't calculate "auto" (e.g. from margin property) on a simulated border -- consider simulating such "non-border" values on other properties
  if (value.indexOf("%") == value.length - 1) {
    var val = value.substring(0, value.length - 1);
    if (typeof hundredPercentValue == "function")
      hundredPercentValue = hundredPercentValue();
    return val / 100.0 * hundredPercentValue;
  }

  if (!window.O$_nonPixelValueMeasurements)
    window.O$_nonPixelValueMeasurements = [];
  var pixelValue = window.O$_nonPixelValueMeasurements[value];
  if (pixelValue != undefined)
    return pixelValue;

  pixelValue = O$_calculateLineWidth(doc, value + " solid white");
  window.O$_nonPixelValueMeasurements[value] = pixelValue;
  return pixelValue;
};

O$_calculateLineWidth = function (doc, lineStyleStr) {
  if (!lineStyleStr)
    return 0;
  if (!window._of_lineWidthValueMeasurements)
    window._of_lineWidthValueMeasurements = [];
  var width = window._of_lineWidthValueMeasurements[lineStyleStr];
  if (width != undefined)
    return width;

  var outerDiv = window.O$_nonPixelMeasurements_outerDiv;
  var innerDiv = window.O$_nonPixelMeasurements_innerDiv;
  if (!outerDiv) {
    outerDiv = doc.createElement("div");
    outerDiv.style.visibility = "hidden";
    outerDiv.style.padding = "0px";
    outerDiv.style.margin = "0px";
    innerDiv = doc.createElement("div");
    innerDiv.style.padding = "0px";
    innerDiv.style.margin = "0px";
    outerDiv.appendChild(innerDiv);
    window.O$_nonPixelMeasurements_outerDiv = outerDiv;
    window.O$_nonPixelMeasurements_innerDiv = innerDiv;
  }
  outerDiv.style.border = lineStyleStr;
  doc.body.appendChild(outerDiv);
  width = innerDiv.offsetTop - outerDiv.offsetTop;
  doc.body.removeChild(outerDiv);
  window._of_lineWidthValueMeasurements[lineStyleStr] = width;
  return width;
};


function O$_getPageScrollPos(win, doc) {
  var x = 0;
  var y = 0;
  if (typeof( win.pageYOffset ) == "number") {
    y = win.pageYOffset;
    x = win.pageXOffset;
  } else if (doc.body && ( doc.body.scrollLeft || doc.body.scrollTop )) {
    y = doc.body.scrollTop;
    x = doc.body.scrollLeft;
  } else if (doc.documentElement && ( doc.documentElement.scrollLeft || doc.documentElement.scrollTop )) {
    y = doc.documentElement.scrollTop;
    x = doc.documentElement.scrollLeft;
  }
  return {x:x, y:y};
}

function O$_isUpperCase(str) {
  return str.toUpperCase() == str;
}

function O$__capitalizeCssPropertyName(propertyName) {
  if (!window.O$_capitalizedValues) window.O$_capitalizedValues = {};
  var value = window.O$_capitalizedValues[propertyName];
  if (value) return value;
  var _propertyName = propertyName;
  while (true) {
    var idx = _propertyName.indexOf("-");
    if (idx == -1) {
      O$_capitalizedValues[propertyName] = _propertyName;
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
}

function O$__dashizeCssPropertyName(propertyName) {
  if (!window.O$_dashizedValues) window.O$_dashizedValues = {};
  var value = window.O$_dashizedValues[propertyName];
  if (value) return value;
  var result = "";
  for (var i = 0, count = propertyName.length; i < count; i++) {
    var ch = propertyName.substring(i, i + 1);
    if (ch != "-" && O$_isUpperCase(ch))
      result += "-" + ch.toLowerCase();
    else
      result += ch;
  }
  window.O$_dashizedValues[propertyName] = result;
  return result;
}

function O$_getElementStyle(doc, element, propertyNames, enableValueCaching) {
  if (typeof propertyNames == "string") {
    if (!enableValueCaching) {
      var currentStyle, computedStyle;
      var result = (currentStyle = element.currentStyle)
              ? currentStyle[O$_capitalizeCssPropertyName(propertyNames)]
              : (computedStyle = !currentStyle && doc.defaultView && doc.defaultView.getComputedStyle(element, ""))
              ? computedStyle.getPropertyValue(O$_dashizeCssPropertyName(propertyNames)) : "";
      return result ? result : "";
    }
    return O$_getElementStyle(doc, element, [propertyNames], enableValueCaching)[propertyNames];
  }
  if (enableValueCaching) {
    if (!element._cachedStyleValues)
      element._cachedStyleValues = {};
  }
  var propertyValues = {};
  currentStyle = element.currentStyle;
  computedStyle = !currentStyle && doc.defaultView && doc.defaultView.getComputedStyle(element, "");
  for (var i = 0, count = propertyNames.length; i < count; i++) {
    var propertyName = propertyNames[i];
    var capitalizedPropertyName = O$_capitalizeCssPropertyName(propertyName);
    var dashizedPropertyName = O$_dashizeCssPropertyName(capitalizedPropertyName);

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
}

function O$_isContainingBlock(elt) {
  q__assert(elt, "elt is null");
  if (elt._containingBlock != undefined) return elt._containingBlock;
  if (elt == document) {
    // O$_calculateElementStyleProperty fails to determine position on the document element, and
    // document element can't have a non-static position
    return false;
  }
  var position = O$_getElementStyle(elt, "position");
  if (!position) return false;
  return elt._containingBlock = position != "static";
}


function O$_getContainingBlock(element, ignoreThisElement) {
  for (var el = !ignoreThisElement ? element : element.parentNode; el; el = el.parentNode) {
    if (O$_isContainingBlock(el))
      return el;
  }
  return null;
}


/*
 O$_calculateNumericStyleProperty doesn't work when calculating margin on a table under Mozilla 2 for some reason,
 so here's an alternative implementation that works for this case.
 */
O$_calculateMozillaMargins = function (doc, element) {
  var styleClassProperties = {};
  styleClassProperties = O$_getStyleClassProperties(doc,
          element.className, ["marginLeft", "marginRight", "marginTop", "marginBottom"]);
  var marginLeft = O$_calculateNumericCSSValue(doc, styleClassProperties.marginLeft);
  var marginRight = O$_calculateNumericCSSValue(doc, styleClassProperties.marginRight);
  var marginTop = O$_calculateNumericCSSValue(doc, styleClassProperties.marginTop);
  var marginBottom = O$_calculateNumericCSSValue(doc, styleClassProperties.marginBottom);
  marginLeft = Math.max(marginLeft, O$_calculateNumericCSSValue(doc, element.style.marginLeft));
  marginRight = Math.max(marginRight, O$_calculateNumericCSSValue(doc, element.style.marginRight));
  marginTop = Math.max(marginTop, O$_calculateNumericCSSValue(doc, element.style.marginTop));
  marginBottom = Math.max(marginBottom, O$_calculateNumericCSSValue(doc, element.style.marginBottom));
  return {marginLeft:marginLeft, marginRight:marginRight, marginTop:marginTop, marginBottom:marginBottom};
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
function O$_getElementPos(win, doc, element, relativeToContainingBlock) {
  var left, top;

  if (element.getBoundingClientRect) {
    var rect = element.getBoundingClientRect();
    left = rect.left;
    top = rect.top;
    var containingBlock;
    if (relativeToContainingBlock) {
      containingBlock = relativeToContainingBlock === true ? O$_getContainingBlock(element, true) : relativeToContainingBlock.offsetParent;
      if (containingBlock && containingBlock.nodeName.toUpperCase() == "BODY")
        containingBlock = null;
      if (containingBlock) {
        var containingRect = containingBlock.getBoundingClientRect();
        left += containingBlock.scrollLeft - containingRect.left - containingBlock.clientLeft;
        top += containingBlock.scrollTop - containingRect.top - containingBlock.clientTop;
      }
    }
    if (!containingBlock) {
      var pageScrollPos = O$_getPageScrollPos(win, doc);
      left += pageScrollPos.x;
      top += pageScrollPos.y;
      if (O$_isExplorer()) {
        if (O$_isQuirksMode(doc)) {
          left -= 2;
          top -= 2;
        } else {
          left -= doc.documentElement.clientLeft;
          top -= doc.documentElement.clientTop;
        }
      }
    }
    return {x:left, y:top};
  }

  if (relativeToContainingBlock && relativeToContainingBlock !== true) {
    var pos = O$_getElementPos(win, doc, element);
    var containerPos = O$_getElementPos(win, doc, relativeToContainingBlock.offsetParent);
    return {x:pos.x - containerPos.x, y:pos.y - containerPos.y};
  }

  left = top = 0;
  // Mozilla 2.0.x strict reports margins on tables to be part of table when measuring it with offsetXXX attributes
//  if (O$.isMozillaFF2() && O$_isStrictMode(doc) && element.tagName && element.tagName.toLowerCase() == "table") {
//    var margins = O$_calculateMozillaMargins(doc, element);
//    left += margins.marginLeft;
//    top += margins.marginTop;
//  }

  while (element) {
    left += element.offsetLeft;
    top += element.offsetTop;
    var thisIsContainingBlock = O$_isContainingBlock(element);

    var offsetParent = element.offsetParent;
    if (!offsetParent)
      break;

//    if (O$.isMozillaFF2() && O$_getElementStyle(offsetParent, "overflow") != "visible") {
//      left += O$_getNumericElementStyle(doc, offsetParent, "border-left-width");
//      top += O$_getNumericElementStyle(doc, offsetParent, "border-top-width");
//    }

    var parentIsContainingBlock = O$_isContainingBlock(offsetParent);
    if (relativeToContainingBlock && parentIsContainingBlock) {
      if (offsetParent.tagName.toLowerCase() == "div" && (O$_isOpera9AndLate() || O$_isSafari2())) {
        if (O$_getElementStyle(offsetParent, "border-style") != "none") {
          var borderLeftWidth = O$_getNumericElementStyle(doc, offsetParent, "border-left-width");
          var borderTopWidth = O$_getNumericElementStyle(doc, offsetParent, "border-top-width");
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
//        if (O$.isMozillaFF2()) {
//          if (parentNodeName == "td") {
//            left -= O$_getNumericElementStyle(doc, parent, "border-left-width");
//            top -= O$_getNumericElementStyle(doc, parent, "border-top-width");
//          }
//          if (parentNodeName == "table" && O$_isStrictMode(doc)) {
//            var parentMargins = O$_calculateMozillaMargins(doc, parent);
//            left += parentMargins.marginLeft;
//            top += parentMargins.marginTop;
//          }
//        }
        if (O$_isSafari()) {
          if (parentNodeName == "table") {
            left += O$_getNumericElementStyle(doc, parent, "border-left-width");
            top += O$_getNumericElementStyle(doc, parent, "border-top-width");
          }
        }
      }

      if (parent == offsetParent)
        break;
    }

    // account for offsetParent's border
    if (!O$_isSafari2()) {
      var lowerCaseTagName = offsetParent.tagName.toLowerCase();
      if (lowerCaseTagName == "table" && O$_isSafari3AndLate()) {
        var border = O$_calculateNumericCSSValue(doc, offsetParent.border);
        if (border > 0) {
          left += border;
          top += border;
        }
      } else {
        if (lowerCaseTagName == "div" || lowerCaseTagName == "td") {
          if (!O$_isOpera9AndLate()) { // border in Opera 9 included in offsetLeft and offsetTop
            left += (offsetParent.clientLeft == undefined ? O$_getNumericElementStyle(doc, offsetParent, "border-left-width") : offsetParent.clientLeft);
            top += (offsetParent.clientTop == undefined ? O$_getNumericElementStyle(doc, offsetParent, "border-top-width") : offsetParent.clientTop);
          }
        }
      }
    }

    element = offsetParent;
  }
  return {x:left, y:top};
}

O$_getElementBorderRectangle = function (win, doc, element, relativeToContainingBlock, cachedDataContainer) {
  if (cachedDataContainer) {
    if (!element._of_getElementRectangle)
      element._of_getElementRectangle = {};
    if (element._of_getElementRectangle._currentCache == cachedDataContainer)
      return element._of_getElementRectangle._cachedValue;
  }
  var pos = O$_getElementPos(doc, element, relativeToContainingBlock);
  var size = O$_getElementSize(doc, element);
  var rect = new O$_Rectangle(pos.x, pos.y, size.width, size.height);
  if (cachedDataContainer) {
    element._of_getElementRectangle._currentCache = cachedDataContainer;
    element._of_getElementRectangle._cachedValue = rect;
  }
  return rect;
};


O$_getElementSize = function (doc, element) {
  var width = element.offsetWidth;
  var height = element.offsetHeight;
  // Mozilla 2.0.x strict reports margins on tables to be part of table when measuring it with offsetXXX attributes
//  if (O$.isMozillaFF2() && O$_isStrictMode(doc) && element.tagName && element.tagName.toLowerCase() == "table") {
//    var margins = O$_calculateMozillaMargins(doc, element);
//    width -= margins.marginLeft + margins.marginRight;
//    height -= margins.marginTop + margins.marginBottom;
//  }
  return {width:width, height:height};
};

function O$_Rectangle(x, y, width, height) {
  this.x = x;
  this.y = y;
  this.width = width;
  this.height = height;
}
//var q__Rectangle = {};
//q__Rectangle.prototype.getMinX = function () {
//  return this.x;
//};
//q__Rectangle.prototype.getMinY = function () {
//  return this.y;
//};
//q__Rectangle.prototype.getMaxX = function () {
//  var result = this.x + this.width;
//  return result;
//};
//q__Rectangle.prototype.getMaxY = function () {
//  var result = this.y + this.height;
//  return result;
//};


function q__getVisibleAreaSize(win) {
  var width = 0, height = 0;
  if (q__isMozillaFF()) {
    if (q__isQuirksMode(document)) {
      width = document.body.clientWidth;
      height = document.body.clientHeight;
    } else {
      width = document.documentElement.clientWidth;
      height = document.documentElement.clientHeight;
    }
  } else if (q__isSafari()) {
    width = document.documentElement.clientWidth;
    height = document.documentElement.clientHeight;
  } else if (typeof win.innerWidth == "number") {
    width = win.innerWidth;
    height = win.innerHeight;
  } else if (document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
    width = document.documentElement.clientWidth;
    height = document.documentElement.clientHeight;
  } else if (document.body && (document.body.clientWidth || document.body.clientHeight)) {
    width = document.body.clientWidth;
    height = document.body.clientHeight;
  }
  return {width:width, height:height};
}


//return gPt(arguments[0]).toLowerCase();

var buttonSuffix = '::button';
var todaySuffix = '--popup--calendar::today';
var noneSuffix = '--popup--calendar::none';
var calendarBodySuffix = '--popup--calendar::body';
var monthSuffix = "--popup--calendar--month";
var yearSuffix = "--popup--calendar--year";

function singleSelectionChanged(dataTableID, divID) { // todo: reimplement this with TableInspector instead of client-side script
  var elDataTable = document.getElementById(dataTableID);
  var tbody = elDataTable.childNodes[1];
  var childerNumber = tbody.childNodes.length;
  for (var i = 0; i < childerNumber; i++) {
    var currentRow = tbody.childNodes[i];
    if (currentRow.tagName && currentRow.tagName && currentRow.tagName.toLowerCase() == "tr") {
      var rowClass = currentRow.className;
      if (rowClass && rowClass.indexOf("o_hiddenRow") == -1 && rowClass != "o_expandedNode" && rowClass != "o_collapsedNode") // selection style is applied to <tr> or embedded <td> tags depending on browser and other conditions
        showSelectedIndex(i, divID);
      else {
        var firstRowChild = currentRow.childNodes[0];
        if (firstRowChild) {
          if (firstRowChild.className && firstRowChild.className.indexOf("o_class") != -1) {
            showSelectedIndex(i, divID);
            break;
          }
        }
      }
    }
  }
}

function showSelectedIndex(index, divID) {
  var empty = getControl(divID);
  listItemInnerHTML(empty, index);
  var children = empty.childNodes;
  for (var i = 0; i < children.length; i++) {
    children[i].className = "programmed";
  }
  empty.style.color = "green";
}

function listItemInnerHTML(el, text) {
  el.innerHTML = "<div class='programmed'>" + text + "</div>";
}

function multipleSelectionChanged(dataTableID, divID) {// todo: reimplement this with TableInspector instead of client-side script
  var elDataTable = getControl(dataTableID);
  var tbody = elDataTable.childNodes[1];
  var childerNumber = tbody.childNodes.length;
  var result = " ";
  for (var i = 0; i < childerNumber; i++) {
    var currentRow = tbody.childNodes[i];
    if (currentRow.tagName && currentRow.tagName.toLowerCase() == "tr") {
      var rowClass = currentRow.className;
      if (rowClass && rowClass.indexOf("o_hiddenRow") == -1 && rowClass != "o_expandedNode" && rowClass != "o_collapsedNode") // selection style is applied to <tr> or embedded <td> tags depending on browser and other conditions
        result += " " + i;
      else {
        var firstRowChild = currentRow.childNodes[0];
        if (firstRowChild) {
          if (firstRowChild.className && rowClass.indexOf("o_class") != -1) {
            result += " " + i;
          }
        }
      }
    }
  }
  showSelectedIndex(result, divID);
}

function printInfo(textToOutput, divID, add) {
  var empty = getControl(divID);
  addListItemInnerHTML(empty, textToOutput, add);
  var children = empty.childNodes;
  for (var i = 0; i < children.length; i++) {
    children[i].className = "programmed";
  }
  empty.style.color = "green";
}

function logJSEvents(textToOutput, divID) {
  printInfo(textToOutput, divID, true);
}

function addListItemInnerHTML(el, text, add) {
  if (add) {
    el.innerHTML += "<div class='programmed'></div>";
    el.innerHTML += "<div class='programmed'>" + text + "</div>";
  } else {
    el.innerHTML = "<div class='programmed'>" + text + "</div>";
    el.style.borderLeftcolor = '';
  }
}

function getControl(id) { // todo: replace usages with O$ and remove this function
  return document.getElementById(id);
}

function determineDateRange(calendarId, selectedClassName, divToPrint) {
  var calendar = getControl(calendarId + '::body');
  var calendarRows = calendar.childNodes;
  var dates = "";

  // loop starts with 1 because zero row is day names
  for (var i = 1; i < calendarRows.length; i++) {
    var row = calendarRows[i];
    var week = row.childNodes;
    for (var j = 0; j < week.length; j++) {
      var date = week[j].childNodes[0];
      var dateClass = date.className;
      if (dateClass.indexOf(selectedClassName) != -1)
        dates += date.textContent + " ";
    }
  }
  printInfo(dates, divToPrint, true);
}

/*to use this function define in DateChooser following: selectedDayStyle="color: red; background: yellow;"*/
function printDateChooserSelectedDate(dateChooserID, outputDiv) {
  var mousedownEvt = O$.createEvent('mousedown');
  getControl(dateChooserID + buttonSuffix).onmousedown(mousedownEvt);
  var dateChooser = getControl(dateChooserID + calendarBodySuffix);
  var weeks = dateChooser.childNodes;
  var date = 'null';

  for (var i = 1; i < weeks.length; i++) {
    var days = weeks[i].childNodes;
    for (var j = 0; j < days.length; j++) {
      var day = days[j].childNodes[0];
      var color = O$.getElementStyle(day, 'color');
      var bgColor = O$.getElementStyle(day, 'background-color');
      if ((color == '"red"' && bgColor == '"yellow"') || (color == 'rgb(255, 0, 0)' && bgColor == 'rgb(255, 255, 0)')) {
        var dayText;
        if (day.textContent.length == 1)
          dayText = '0' + day.textContent;
        else
          dayText = day.textContent;
        date = dayText + ' ' + getControl(dateChooserID + monthSuffix).textContent + ', ' + getControl(dateChooserID + yearSuffix).textContent;
        break;
      }
    }
  }
  printInfo(date, outputDiv, false);
  getControl(dateChooserID + buttonSuffix).onmousedown(mousedownEvt);
}

function printFirstDayOfWeek(elementId, outputDiv, isDateChooser) {
  if (isDateChooser) {
    var mousedownEvt = O$.createEvent('mousedown');
    getControl(elementId + buttonSuffix).onmousedown(mousedownEvt);
  }
  var calendarId;
  if (isDateChooser) {
    calendarId = elementId + calendarBodySuffix;
  } else {
    calendarId = elementId + "::body";
  }
  printInfo(getControl(calendarId).childNodes[0].childNodes[0].textContent, outputDiv, false);

}