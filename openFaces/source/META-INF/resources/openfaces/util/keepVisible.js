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
O$.KeepVisible = {
  _init:function (keepVisibleId, elementId, topMargin, bottomMargin) {
    O$.addEventHandler(window, "load", function () {
      var element = O$(elementId);
      if (!element) throw "There are no element with id '" + elementId + "'";
      O$.KeepVisible._extractElement(element);
      var keepVisible = O$.KeepVisible._initComponent(elementId, topMargin, bottomMargin);
      keepVisible._setEvents();
      keepVisible._handleScroll();
    });
  },

  _initComponent:function (elementId, topMargin, bottomMargin) {
    var component = O$.initComponent(elementId, null, {
      _elementId:elementId,
      _topMargin:topMargin,
      _bottomMargin:bottomMargin,
      _numericTopMargin:O$.calculateNumericCSSValue(topMargin),
      _numericBottomMargin:O$.calculateNumericCSSValue(bottomMargin),


      _handleScroll:function () {
        if (!component._marginsAreCorrect()) return;
        var element = O$(component._elementId);
        if (!element) throw "There is no element with id '" + component._elementId + "'";

        var elementHeight = O$.getElementSize(element).height;
        var viewportHeight = O$.getVisibleAreaSize().height;
        if (elementHeight > viewportHeight) return;
        if (!component._lastPosition)
          component._lastPosition = O$.getElementPos(O$(elementId)).y;

        if (component._topMargin && component._bottomMargin) {
          if (component._numericTopMargin + component._numericBottomMargin + elementHeight  < viewportHeight) {
            component._keepVisibleWithBothMargins(element);
          }
        } else if (component._topMargin) {
          component._keepVisibleWithTopMargin(element);
        } else if (component._bottomMargin) {
          component._keepVisibleWithBottomMargin(element);
        }
      },

      _keepVisibleWithBothMargins:function (element) {
        component._keepVisibleWithTopMargin(element);
        if (element.style.position != 'fixed') {
          component._keepVisibleWithBottomMargin(element);
        }
      },

      _keepVisibleWithTopMargin:function (element) {
        var scrollingOffset = O$.getPageScrollPos().y;
        var topMargin = component._numericTopMargin;
        var topLimit = (scrollingOffset + topMargin);
        if (component._lastPosition - topLimit <= 0) {
          component._makeElementFixed(element, topMargin);
        } else {
          if (element.style.position == 'fixed') {
            component._makeElementAbsolute(element, component._lastPosition);
          }
        }
      },

      _keepVisibleWithBottomMargin:function (element) {
        var bottomMargin = component._numericBottomMargin;
        var elementHeight = O$.getElementSize(element).height;
        var viewportHeight = O$.getVisibleAreaSize().height;
        var scrollingOffset = O$.getPageScrollPos().y;
        var bottomLimit = (viewportHeight + scrollingOffset - bottomMargin);
        var bottomEdgeOfElement = component._lastPosition + elementHeight;

        if (bottomEdgeOfElement >= bottomLimit) {
          component._makeElementFixed(element, bottomLimit - elementHeight - scrollingOffset);
        } else {
          if (element.style.position == 'fixed') {
            component._makeElementAbsolute(element, component._lastPosition);
          }
        }
      },
      _makeElementFixed:function (element, yCoordinate) {
        if (!element || yCoordinate == null) return;

        if (O$.isQuirksMode() && O$.isExplorer() || O$.isExplorer7() || O$.isExplorer6()) {
          element.style.position = 'absolute';
          element.style.top = (yCoordinate + O$.getPageScrollPos().y) + "px";
        } else {
          element.style.position = 'fixed';
          element.style.top = yCoordinate + "px";
        }
      },

      _makeElementAbsolute:function (element, yCoordinate) {
        if (!element || yCoordinate == null) return;
        element.style.position = 'absolute';
        element.style.top = yCoordinate + "px";
      },

      _setEvents:function () {
        O$.addEventHandler(window, "scroll", component._handleScroll);
        O$.addEventHandler(window, "resize", component._handleScroll);
      },

      _marginsAreCorrect:function () {
        var sum = component._numericTopMargin + component._numericBottomMargin;
        return sum < O$.getElementSize(document.body).height;
      }
    });
    return component;
  },

  _extractElement:function (element) {
    var x = O$.getElementPos(element).x;
    var y = O$.getElementPos(element).y;
    O$.correctElementZIndex(element, element.parentNode);
    element.style.position = 'absolute';
    element.style.top = y + "px";
    element.style.left = x + "px";
    element.style.right = '';
    element.style.bottom = '';
    element.style.marginTop = '';
    element.style.marginBottom = '';
    document.body.appendChild(element);
  }
};
