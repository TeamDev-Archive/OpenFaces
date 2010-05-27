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
O$.Chart = {
  _init: function(chartId) {
    var chart = O$.initComponent(chartId, null, {
      areaContextMenuClick: function(event, chartMenuId) {
        var rightClick = false;
        if (event.which) rightClick = (event.which == 3);
        else if (event.button) rightClick = (event.button == 2);

        if (rightClick) {
          var chartMenu = O$(chartMenuId);
          O$.disableNativeContextMenuFor(event.target);
          chartMenu.showForEvent(event);
          chartMenu.focus();

          O$.breakEvent(event);
        }
      },

      clickItem: function(event, entityIndex, customOnClick, hasAction, hasSelection) {
        var oldEntityIndex = O$(chartId + "::af").value;
        if (customOnClick && typeof(customOnClick) == Function) {
          customOnClick(event);
        }

        var valueChanged = oldEntityIndex != entityIndex;
        if (valueChanged) {
          O$.setValue(chartId + "::af", entityIndex);
        }

        if (hasAction || hasSelection) {
          chart.selectItem(event, entityIndex, valueChanged);
        }
      },

      selectItem:function(event, itemIndex, valueChanged) {
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