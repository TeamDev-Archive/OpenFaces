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
      clickItem: function(event, entityIndex, customOnClick, hasAction, hasSelection) {
        O$.setValue(chartId + "::af", entityIndex);
        if (customOnClick && typeof(customOnClick) == Function) {
          customOnClick(event);
        }

        if (hasAction || hasSelection) {
          chart.selectItem(event, entityIndex);
        }
      },

      selectItem:function(event, itemIndex) {
        O$.ajax.request(chart, event, {render: chart.id, params:{selection:itemIndex}});
      }
    });
  }
};