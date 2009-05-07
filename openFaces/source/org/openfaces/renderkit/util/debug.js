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
  debug._logText = O$(clientId + "--log");
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
  }
}