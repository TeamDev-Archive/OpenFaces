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
      _contentLoaded: loadingMode == "client"
    });

    var popup = O$(chartPopupId);
    var oldOnMouseOver = popup.onmouseover;
    var oldOnMouseOut = popup.onmouseout;


    popup.onmouseover = function() {
      popup.mouseOverPopup = true;
      if (oldOnMouseOver) {
        oldOnMouseOver();
      }
    };

    popup.onmouseout = function() {
      popup.mouseOverPopup = false;

      if (oldOnMouseOut) {
        oldOnMouseOut();
      }
    };
  },

  _ajaxResponseProcessor: function(popup, portionName, portionHTML, portionScripts) {
    var oldComponent, prnt, tempDiv, newControl, oldId;
    if (portionName == "content") {
      oldComponent = O$(popup.id);
      prnt = oldComponent.parentNode;
      tempDiv = document.createElement("div");
      tempDiv.innerHTML = portionHTML;
      newControl = tempDiv.childNodes[0];
      oldId = oldComponent.id;
      prnt.replaceChild(newControl, oldComponent);
      newControl.id = oldId;
    }
    O$.executeScripts(portionScripts);
    newControl._contentLoaded = true;
    newControl.showAtXY(popup._eventX + 2, popup._eventY + 2);
  },

  show:function(event, chartPopupId) {
    var eventPoint = O$.getEventPoint(event);
    var popup = O$(chartPopupId);

    if (popup._loadingMode == "ajaxAlways" || !popup._contentLoaded) {
      popup._eventX = eventPoint.x;
      popup._eventY = eventPoint.y;
      O$.requestComponentPortions(popup.id, ["content"], null, O$.ChartPopup._ajaxResponseProcessor);
    } else {
      if (!popup.isVisible()) {
        popup.showAtXY(eventPoint.x + 2, eventPoint.y + 2);
      }
    }
  },

  hide:function(event, chartPopupId) {
    var popup = O$(chartPopupId);

    if (popup._contentLoaded) {
      setTimeout(function () {
        if (!popup.mouseOverPopup) {
          popup.hide();
        }
      }, 100);
    }
  }
};


