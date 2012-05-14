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
// -------------------------- CHART SUPPORT
O$.Chart = {
  _init: function(chartId) {
    var chart = O$.initComponent(chartId, null, {
      _areaContextMenuClick: function(event, chartMenuId) {
        var rightClick = false;
        if (event.which) rightClick = (event.which == 3);
        else if (event.button) rightClick = (event.button == 2);

        if (rightClick) {
          var chartMenu = O$(chartMenuId);
          O$.disableNativeContextMenuFor(event.target);
          chartMenu.showForEvent(event);
          chartMenu.focus();

          O$.cancelEvent(event);
        }
      },

      _clickItem: function(event, entityIndex, customOnClick, hasAction, hasSelection) {
        var oldEntityIndex = O$(chartId + "::af").value;
        if (customOnClick && typeof(customOnClick) == Function) {
          customOnClick(event);
        }

        var valueChanged = oldEntityIndex != entityIndex;
        if (valueChanged) {
          O$.setValue(chartId + "::af", entityIndex);
        }

        if (hasAction || hasSelection) {
          chart._selectItem(event, entityIndex, valueChanged);
        }
      },

      _selectItem:function(event, itemIndex, valueChanged) {
        var onAjaxEndFunction = function() {
          if (valueChanged) {
            if (chart._selectionChangeHandlers) {
              for (var handlerIdx = 0, handlerCount = chart._selectionChangeHandlers.length;
                   handlerIdx < handlerCount;
                   handlerIdx++) {
                var handler = chart._selectionChangeHandlers[handlerIdx];
                var obj = handler[0];
                var methodName = handler[1];
                obj[methodName]();
              }
            }
          }
        };

        O$.ajax.request(chart, event, {render: chart.id, params:{selection:itemIndex}, onajaxend: onAjaxEndFunction});
      }
    });
  },

  _addSelectionChangeHandler: function(chart, handler) {
    O$.assert(handler, "O$.Chart._addSelectionChangeHandler: handler must be specified. chart.id = " + chart.id);
    var handlers = chart._selectionChangeHandlers;
    if (!handlers) {
      handlers = [];
      chart._selectionChangeHandlers = handlers;
    }

    handlers.push(handler);
  },

  _initSelection: function(chartId, selectionChangeHandler) {
    var chart = O$.initComponent(chartId);
    O$.assert(!chart._selectionInitialized, "O$.Chart._initSelection shouldn't be called twice on the same chart component");
    O$.extend(chart, {
      _selectionInitialized: true
    });

    if (selectionChangeHandler) {
      eval("chart.onchange = function(event) {" + selectionChangeHandler + "}");
      chart._fireOnSelectionChange = function() {
        O$.sendEvent(chart, "change");
      };

      O$.Chart._addSelectionChangeHandler(chart, [chart, "_fireOnSelectionChange"]);
    }
  }
};

// -------------------------- CHART MENU SUPPORT
O$.ChartMenu = {
  _init: function(chartMenuId, chartId) {
    var chartMenu = O$.initComponent(chartMenuId, null, {_chart: O$(chartId)});

    chartMenu._chart._download = function() {
      var paramsString = "download=true";
      var url = O$(this.id + ":img").src;
      var params = paramsString.split('&');
      var inputs = '';

      for (var parameterIndex = 0; parameterIndex < params.length; parameterIndex++) {
        var value = params[parameterIndex];
        var pair = value.split('=');
        inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
      }

      //send request
      var form = document.createElement("form");
      form.action = url;
      form.method = "post";
      form.innerHTML = inputs;
      document.body.appendChild(form);
      form.submit();
      document.body.removeChild(form);
    };
  },

  _saveChart : function(chartId) {
    var chart = O$(chartId);
    chart._download();
  },

  _printChart :function(chartId) {
    var chart = O$(chartId);
    var img = O$(chart.id + ":img");
    jQuery(img).printElement({printBodyOptions:{styleToAdd: 'background:none; background-color: white;'}});
  }
};

// -------------------------- CHART POPUP SUPPORT
O$.ChartPopup = {
  _init: function(chartPopupId, loadingMode) {
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
    O$.Ajax.executeScripts(portionScripts);
    newPopup._contentLoaded = true;
    newPopup._targetArea = popup._targetArea;
    var chart = newPopup.parentNode;
    if (chart._visiblePopup) {
      chart._visiblePopup.hide();
    }
    newPopup.showAtXY(popup._eventX + 2, popup._eventY + 2);
    chart._visiblePopup = newPopup;
  },

  _show:function(event, chartPopupId) {
    var popup = O$(chartPopupId);
    var chart = popup.parentNode;
    var eventPoint = O$.getEventPoint(event, popup);
    var targetArea = event.target ? event.target : event.srcElement;
    popup._targetArea = targetArea;

    if (!targetArea._initialized) {
      O$.setupHoverStateFunction(targetArea, popup._hoverStateHandler);
      targetArea._initialized = true;
    }

    if (popup._loadingMode == "ajaxAlways" || !popup._contentLoaded) {
      popup._eventX = eventPoint.x;
      popup._eventY = eventPoint.y;
      O$.Ajax.requestComponentPortions(popup.id, ["content"], null, O$.ChartPopup._ajaxResponseProcessor);
    } else {
      if (!popup.isVisible()) {
        if (chart._visiblePopup) {
          chart._visiblePopup.hide();
        }
        popup.showAtXY(eventPoint.x + 2, eventPoint.y + 2);
        chart._visiblePopup = popup;
      }
    }
  }
};
