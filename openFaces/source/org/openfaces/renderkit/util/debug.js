/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */


O$._initDebug = function(clientId) {
  var debug = O$(clientId);
  debug._pages = O$(clientId + "--pages");
  debug._logText = O$(clientId + "--log");
  debug._elementProperties = O$(clientId + "--elementProperties");

  debug.log = function(text) {
    if (!this.isVisible())
      this.show();
    var date = new Date();

    function logElement(el) {
      if (debug._logText.childNodes.length > 0)
        debug._logText.insertBefore(el, debug._logText.childNodes[0]);
      else
        debug._logText.appendChild(el);
    }

    logElement(document.createElement("br"));
    logElement(document.createTextNode(text));
    logElement(document.createTextNode(date + " : "));
    this._pages.setSelectedIndex(0);
  }
  debug.logObject = function(obj) {
    var txt = "";
    for (var fld in obj) {
      if (txt.length > 0)
        txt += ", ";
      txt += fld + " : " + obj[fld]; 
    }
    txt = "[" + txt + "]";
    debug.log(txt);
  }
  debug.inspectElement = function(element) {
    this._elementProperties._removeAllRows();
    if (!element)
      return;
    for (var fld in element) {
      var row = this._elementProperties.body._newRow();
      row._cells[0].innerHTML = fld;
      row._cells[1].appendChild(document.createTextNode(element[fld]));
    }
  }

  O$.onfocuschange = function() {
//    debug.inspectElement(O$._activeElement);
  }

}