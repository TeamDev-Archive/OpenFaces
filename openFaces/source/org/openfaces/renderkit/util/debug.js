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

O$.Debug = {
  _init: function(clientId) {
    var debug = O$(clientId);
    O$.debug = debug;

    O$.extend(debug, {
      _pages: O$(clientId + "--pages"),
      _logTable: O$(clientId + "--consoleContainer--log"),
      _clearLogBtn: O$.byIdOrName(clientId + "--consoleContainer--consoleToolbar--clearLog"),
      _pauseLogBtn: O$.byIdOrName(clientId + "--consoleContainer--consoleToolbar--pauseLog"),
      _elementProperties: O$(clientId + "--elementProperties"),
      _paused: false,
      _delayedMessages: [],
      _maxLogEntries: 10000,

      log: function(text) {
        if (this._paused) {
          text._asText = true;
          this._delayedMessages.push(text);
          return;
        }
        if (!this.isVisible())
          this.show();
        var date = new Date();

        if (this._logTable.body._getRows().length == this._maxLogEntries) {
          this._logTable.body._removeAllRows(); // todo: remove the last row only
        }
        var row = this._logTable.body._createRow();
        this._logTable._insertRowsAfter(-1, [row]);
        row._cells[0].innerHTML = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + ":" + date.getMilliseconds();
        row._cells[1].appendChild(document.createTextNode(text));
        this._pages.setSelectedIndex(0);
      },

      clearLog: function() {
        this._logTable.body._removeAllRows();
        this._delayedMessages = [];
      },

      logObject: function(obj) {
        if (this._paused) {
          this._delayedMessages.push(text);
          return;
        }
        var txt = "";
        for (var fld in obj) {
          if (txt.length > 0)
            txt += ", ";
          txt += fld + " : " + obj[fld];
        }
        txt = "[" + txt + "]";
        debug.log(txt);
      },
      inspectElement: function(element) {
        this._elementProperties.body._removeAllRows();
        if (!element)
          return;
        for (var fld in element) {
          var row = this._elementProperties.body._newRow();
          row._cells[0].innerHTML = fld;
          row._cells[1].appendChild(document.createTextNode(element[fld]));
        }
      },

      onfocuschange: function() {
    //    debug.inspectElement(O$._activeElement);
      }
    });

    debug._clearLogBtn.onclick = function() {
      debug.clearLog();
      return false;
    };

    debug._pauseLogBtn.onclick = function() {
      if (!debug._paused) {
        debug._paused = true;
        this.value = "Resume";
      } else {
        debug._paused = false;
        this.value = "Pause";
//        debug._delayedMessages.forEach(function(msg){
//          if (msg._asText)
//            debug.log(msg);
//          else
//            debug.logObject(msg);
//        });
        debug._delayedMessages = [];
      }
      return false;
    };


  }
};