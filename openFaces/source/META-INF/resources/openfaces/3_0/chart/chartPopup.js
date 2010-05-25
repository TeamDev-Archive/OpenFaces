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
// -------------------------- CHART MENU SUPPORT
O$.ChartPopup = {
  _init: function(chartPopupId, loadingMode, chartId) {
    var chartPopup = O$.initComponent(chartPopupId, null, {
      _loadingMode: loadingMode,
      _contentLoaded: loadingMode == "client",
      _hoverStateHandler: function(mouseInside, element) {
        element._mouseInside = mouseInside;

        setTimeout(function () {
          if ((chartPopup._targetArea && !chartPopup._targetArea._mouseInside) && !chartPopup._mouseInside) {
            chartPopup.hide();
          }
        }, 100);
      }
    });

    O$.setupHoverStateFunction(chartPopup, chartPopup._hoverStateHandler);
  },

  _ajaxResponseProcessor: function(popup, portionName, portionHTML, portionScripts) {
    var oldComponent, prnt, tempDiv, newPopup, oldId;
    if (portionName == "content") {
      oldComponent = O$(popup.id);
      prnt = oldComponent.parentNode;
      tempDiv = document.createElement("div");
      tempDiv.innerHTML = portionHTML;
      newPopup = tempDiv.childNodes[0];
      oldId = oldComponent.id;
      prnt.replaceChild(newPopup, oldComponent);
      newPopup.id = oldId;
    }
    O$.executeScripts(portionScripts);
    newPopup._contentLoaded = true;
    newPopup._targetArea = popup._targetArea;
    var chart = newPopup.parentNode;
    if (chart._visiblePopup) {
      chart._visiblePopup.hide();
    }
    newPopup.showAtXY(popup._eventX + 2, popup._eventY + 2);
    chart._visiblePopup = newPopup;
  },

  show:function(event, chartPopupId) {
    var popup = O$(chartPopupId);
    var chart = popup.parentNode;
    var eventPoint = O$.getEventPoint(event);
    var targetArea = event.target ? event.target : event.srcElement;
    popup._targetArea = targetArea;

    if (!targetArea._initialized) {
      O$.setupHoverStateFunction(targetArea, popup._hoverStateHandler);
      targetArea._initialized = true;
    }

    if (popup._loadingMode == "ajaxAlways" || !popup._contentLoaded) {
      popup._eventX = eventPoint.x;
      popup._eventY = eventPoint.y;
      O$.requestComponentPortions(popup.id, ["content"], null, O$.ChartPopup._ajaxResponseProcessor);
    } else {
      if (!popup.isVisible()) {
        if (chart._visiblePopup) {
          chart._visiblePopup.hide();
        }
        popup.showAtXY(eventPoint.x + 2, eventPoint.y + 2);
        chart._visiblePopup = popup;
      }
    }
  },

  hide:function(event, chartPopupId) {
    var popup = O$(chartPopupId);

  }
};


