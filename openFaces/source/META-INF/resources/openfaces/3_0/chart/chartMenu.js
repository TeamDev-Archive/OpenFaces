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
O$.ChartMenu = {
  _init: function(chartMenuId, chartId) {
    var chartMenu = O$.initComponent(chartMenuId, null, {_chart:O$(chartId)});

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
    jQuery(img).printElement();
  }
};
