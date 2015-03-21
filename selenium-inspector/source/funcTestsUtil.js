function initUtils() {

   function q__isQuirksMode(doc) {
      return doc.compatMode != "CSS1Compat";
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
            var result = (currentStyle = element.currentStyle) ? currentStyle[O$_capitalizeCssPropertyName(propertyNames)] : (computedStyle = !currentStyle && doc.defaultView && doc.defaultView.getComputedStyle(element, "")) ? computedStyle.getPropertyValue(O$_dashizeCssPropertyName(propertyNames)) : "";
            return result ? result : ""
         }
         return O$_getElementStyle(doc, element, [propertyNames], enableValueCaching)[propertyNames]
      }
      if (enableValueCaching) {
         if (!element._cachedStyleValues) {
            element._cachedStyleValues = {}
         }
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
               continue
            }
         }
         if (currentStyle) {
            propertyValue = currentStyle[capitalizedPropertyName]
         } else {
            if (computedStyle) {
               propertyValue = computedStyle.getPropertyValue(dashizedPropertyName)
            }
         }
         if (!propertyValue) {
            propertyValue = ""
         }
         if (enableValueCaching) {
            element._cachedStyleValues[dashizedPropertyName] = propertyValue
         }
         propertyValues[dashizedPropertyName] = propertyValue;
         propertyValues[capitalizedPropertyName] = propertyValue
      }
      return propertyValues
   }

   function O$_isContainingBlock(elt) {
      q__assert(elt, "elt is null");
      if (elt._containingBlock != undefined) {
         return elt._containingBlock
      }
      if (elt == document) {
         return false
      }
      var position = O$_getElementStyle(elt, "position");
      if (!position) {
         return false
      }
      return elt._containingBlock = position != "static"
   }

   function O$_getContainingBlock(element, ignoreThisElement) {
      for (var el = !ignoreThisElement ? element : element.parentNode; el; el = el.parentNode) {
         if (O$_isContainingBlock(el)) {
            return el
         }
      }
      return null
   }

   function q__assert(value, message) {
      if (value !== null && value !== undefined && value !== false)
         return;
      alert(message);
   }

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

   function q__calculateElementStyleProperty(doc, element, propertyName, enableValueCaching) { // <MOD>added doc param</MOD>
      var propertyValues = q__calculateElementStyleProperties(doc, element, [propertyName], enableValueCaching);
      return propertyValues[propertyName];
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

   function q__getVisibleAreaSize(win) {
      var width = 0, height = 0;
         if (q__isQuirksMode(win.document)) {
            width = win.document.body.clientWidth;
            height = win.document.body.clientHeight;
         } else {
            width = win.document.documentElement.clientWidth;
            height = win.document.documentElement.clientHeight;
         }
      return {width:width, height:height};
   }
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
      return {x: x, y: y};
   }
   function O$_getElementPos(win, doc, element, relativeToContainingBlock) {
      var left, top;
      if (element.getBoundingClientRect) {
         var rect = element.getBoundingClientRect();
         left = rect.left;
         top = rect.top;
         var containingBlock;
         if (relativeToContainingBlock) {
            containingBlock = relativeToContainingBlock === true ? O$_getContainingBlock(element, true) : relativeToContainingBlock.offsetParent;
            if (containingBlock && containingBlock.nodeName.toUpperCase() == "BODY") {
               containingBlock = null
            }
            if (containingBlock) {
               var containingRect = containingBlock.getBoundingClientRect();
               left += containingBlock.scrollLeft - containingRect.left - containingBlock.clientLeft;
               top += containingBlock.scrollTop - containingRect.top - containingBlock.clientTop
            }
         }
         if (!containingBlock) {
            var pageScrollPos = O$_getPageScrollPos(win, doc);
            left += pageScrollPos.x;
            top += pageScrollPos.y;
         }
         return{x:left, y:top}
      }
      if (relativeToContainingBlock && relativeToContainingBlock !== true) {
         var pos = O$_getElementPos(win, doc, element);
         var containerPos = O$_getElementPos(win, doc, relativeToContainingBlock.offsetParent);
         return{x:pos.x - containerPos.x, y:pos.y - containerPos.y}
      }
      left = top = 0;
      while (element) {
         left += element.offsetLeft;
         top += element.offsetTop;
         var thisIsContainingBlock = O$_isContainingBlock(element);
         var offsetParent = element.offsetParent;
         if (!offsetParent) {
            break
         }
         var parentIsContainingBlock = O$_isContainingBlock(offsetParent);
         if (relativeToContainingBlock && parentIsContainingBlock) {
            if (offsetParent.tagName.toLowerCase() == "div" && (O$_isOpera9AndLate() || O$_isSafari2())) {
               if (O$_getElementStyle(offsetParent, "border-style") != "none") {
                  var borderLeftWidth = O$_getNumericElementStyle(doc, offsetParent, "border-left-width");
                  var borderTopWidth = O$_getNumericElementStyle(doc, offsetParent, "border-top-width");
                  left -= borderLeftWidth;
                  top -= borderTopWidth
               }
            }
            break
         }
         for (var parent = element.parentNode; parent; parent = parent.parentNode) {
            var parentNodeName = parent.nodeName ? parent.nodeName.toLowerCase() : null;
            var parentIsAPage = parentNodeName == "html" || parentNodeName == "body";
            if (!parentIsAPage) {
               var pScrollLeft = parent.scrollLeft;
               if (pScrollLeft) {
                  left -= pScrollLeft
               }
               var pScrollTop = parent.scrollTop;
               if (pScrollTop) {
                  top -= pScrollTop
               }
            }
            if (parent == offsetParent) {
               break
            }
         }
            var lowerCaseTagName = offsetParent.tagName.toLowerCase();
            if (lowerCaseTagName == "table" && O$_isSafari3AndLate()) {
               var border = O$_calculateNumericCSSValue(doc, offsetParent.border);
               if (border > 0) {
                  left += border;
                  top += border
               }
            } else {
               if (lowerCaseTagName == "div" || lowerCaseTagName == "td") {
                     left += (offsetParent.clientLeft == undefined ? O$_getNumericElementStyle(doc, offsetParent, "border-left-width") : offsetParent.clientLeft);
                     top += (offsetParent.clientTop == undefined ? O$_getNumericElementStyle(doc, offsetParent, "border-top-width") : offsetParent.clientTop)
               }
            }
         element = offsetParent
      }
      return{x:left, y:top}
   }
   document.SeI = {
      getXPath:function (element) {
         if (element.id !== '') {
            return "//*[@id='" + element.id + "']";
         }
         if (element === document.body) {
            return "//" + element.tagName
         }
         var a = 0;
         var e = element.parentNode.childNodes;
         for (var b = 0; b < e.length; b++) {
            var d = e[b];
            if (d === element) {
               return document.SeI.getXPath(element.parentNode) + '/' + element.tagName + '[' + (a + 1) + ']'
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
            if (locator.indexOf("document.SeI.findElement") == 0) {
               var parts = locator.split("/");
               obj = eval(parts[0]);
               for (var i = 1; i < parts.length; i++) {
                  eval("obj = obj." + parts[i]);
               }
               return obj;
            } else {
               var rawPath = locator.replace(/\//g, ".");
               if (!!(document.getElementById(rawPath))) return document.getElementById(rawPath);
               if (!!(document.getElementsByName(rawPath))) return document.getElementsByName(rawPath)[0];
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
         return function q__findElementByPath(node, childPath, ignoreNonExistingElements) {
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
         }(node, childPath, ignoreNonExistingElements);
      },

      getQ__calculateElementStyleProperty:function (element, propertyName) {
         return q__calculateElementStyleProperty(document, element, propertyName);
      },

      getQ__getElementSize:function (element) {
         var size = document.SeI.O$_getElementSize(document, element);
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
            result += document.SeI.getQ__getNodeText(node);
         }
         return result;
      },
      O$_getElementSize:function (doc, element) {
         var width = element.offsetWidth;
         var height = element.offsetHeight;
         return {width:width, height:height};
      },

      getQ__getWindowSize:function () {
         var size = q__getVisibleAreaSize(window);
         return size.width + "," + size.height;
      }
   }
}
initUtils();