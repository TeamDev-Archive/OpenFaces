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

//------------------------------------------------------------------------------------------------------------------
function d_getElementLeft(elt, ignoreAbsolutePosition) {
  var left = 0;
  if (elt != null && (elt.style.position != "absolute" && d_calculateStyleProperty(elt, "position") != "absolute" || ignoreAbsolutePosition)) {
    left = elt.offsetLeft;
    if (d_isExplorer() || d_isOpera()) {
      var lowerCaseTagName = elt.tagName.toLowerCase();
      if (lowerCaseTagName == 'table') {
        if (elt.border > 0) {
          left ++;
        }
      } else if (lowerCaseTagName == 'div' || lowerCaseTagName == 'td') {
        left += elt.clientLeft;
      }
    }
    if (elt.offsetParent) {
      left += d_getElementLeft(elt.offsetParent, ignoreAbsolutePosition);
    }
  }
  return left;
}
//------------------------------------------------------------------------------------------------------------------
function d_getElementTop(elt, ignoreAbsolutePosition) {
  var top = 0;
  if (elt != null && (elt.style.position != "absolute" && d_calculateStyleProperty(elt, "position") != "absolute" || ignoreAbsolutePosition)) {
    top = elt.offsetTop;
    if (d_isExplorer() || d_isOpera()) {
      var lowerCaseTagName = elt.tagName.toLowerCase();
      if (lowerCaseTagName == 'table') {
        if (elt.border > 0) {
          top ++;
        }
      } else if (lowerCaseTagName == 'div' || lowerCaseTagName == 'td') {
        top += elt.clientTop;
      }
    }
    if (elt.offsetParent) {
      top += d_getElementTop(elt.offsetParent, ignoreAbsolutePosition);
    }
  }
  return top;
}
//------------------------------------------------------------------------------------------------------------------
function d_calculateStyleProperty(element, propertyName) {
  var result;
  if (element.currentStyle) {
    var capitalizedProperty = d_capitalizeCssPropertyName(propertyName);
    result = element.currentStyle[capitalizedProperty];
  } else {
    var computedStyle = document.defaultView.getComputedStyle(element, "");
    if (computedStyle != null) {
      result = computedStyle.getPropertyValue(propertyName);
    }
  }
  if (!result)
    result = "";
  return result;
}
//------------------------------------------------------------------------------------------------------------------
function d_isExplorer() {
  return d_checkBrowser("msie") && !d_checkBrowser("opera");
}
//------------------------------------------------------------------------------------------------------------------
function d_isOpera() {
  return d_checkBrowser("opera");
}
//------------------------------------------------------------------------------------------------------------------
function d_capitalizeCssPropertyName(propertyName) {
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
//------------------------------------------------------------------------------------------------------------------
function d_checkBrowser(browserName) {
  return navigator.userAgent.toLowerCase().indexOf(browserName.toLowerCase()) > -1;
}
//------------------------------------------------------------------------------------------------------------------