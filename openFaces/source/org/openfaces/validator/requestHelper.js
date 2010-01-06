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

O$.RequestHelper = function() {

  /* Formats placeholders in a format specification with supplied replacements. This method demonstrates fixed and variable arguments. */

  var nextId = 0;
  //headers = [{name:teamdev_ajax_VALIDATOR, value:validator}]
  this["call"] = function (url, params, headers, callback) //, params, callback)
  {
    var request = {id : nextId++, params : params };
    if (!callback) {
      //params };
      return callSync(url, request, headers);
      //return callback == null ?
      //    callSync(request) : callAsync(request, callback);
    } else {
      return callAsync(url, request, headers, callback);
    }
  }

  function callSync(url, request, headers) {
    var http = newHTTP();
    http.open("POST", url + "?" + Math.random(), false);
    if (headers && headers.length > 0) {
      for (var i = 0; i < headers.length; i++) {
        setupHeaders(http, headers[i].name, headers[i].value);
      }
    }

    http.send(JSON.stringify(request));

    if (http.status != 200)
      throw { message : http.status + ' ' + http.statusText, toString : function() {
        return this.message;
      } };
    return http.responseText;
  }

  function callAsync(url, request, headers, callback) {
    var http = newHTTP();
    http.open("POST", url, true);
    if (headers && headers.length > 0) {
      for (var i = 0; i < headers.length; i++) {
        setupHeaders(http, headers[i].name, headers[i].value);
      }
    }

    http.onreadystatechange = function() {
      http_onreadystatechange(http, callback);
    };
    http.send(JSON.stringify(request));
    return request.id;
  }

  function setupHeaders(http, name, value) {
    //http.setRequestHeader('Content-Type', 'text/plain; charset=utf-8');
    //http.setRequestHeader('teamdev_ajax_VALIDATOR', 'validator');
    //    http.setRequestHeader('_openFaces_ajax', 'validator');
    if (!name || !value)
      return;

    http.setRequestHeader(name, value);
  }

  function http_onreadystatechange(sender, callback) {
    if (sender.readyState == /* complete */ 4)
      callback(sender.responseText);
  }

  function newHTTP() {
    return typeof(ActiveXObject) === "function" ?
           new ActiveXObject("Microsoft.XMLHTTP") : /* IE 5 */
           new XMLHttpRequest();
    /* Safari 1.2, Mozilla 1.0/Firefox, and Netscape 7 */
  }

};
