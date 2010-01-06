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

O$._MessagesRenderer = function(clientId, title, tooltip, showSummary, showDetail, layout, globalOnly, css, styles, params) {
  this.clientId = clientId;
  this.tooltip = tooltip;
  this.title = title;
  this.showSummary = showSummary;
  this.showDetail = showDetail;
  this.layout = layout;
  this.globalOnly = globalOnly;
  this.style = "";
  this.css = css;
  this.params = params;
  this.styles = styles;

  this.renderAllMessages = function() {
    var spanElement = O$.byIdOrName(this.clientId);
    if (!spanElement)
      return;
    spanElement.innerHTML = "";

    var messages = O$.getAllMessages(this.globalOnly);
    if (messages && messages.length > 0) {
      var outerElement = spanElement.appendChild(this.addOuterElement(spanElement));
      for (var i = 0; i < messages.length; i++) {
        var message = messages[i];
        var messageSpan = this.addInnerElement(outerElement);
        this.updateMessageSpan(message, css, messageSpan);

      }

    }
  };

  this.addInnerElement = function(outerElemet) {
    if (this.needTable()) {
      var tr = document.createElement("tr");
      var td = document.createElement("td");
      var tbody = document.createElement("tbody");
      var span = document.createElement("span");
      outerElemet.appendChild(tbody);
      tbody.appendChild(tr);
      tr.appendChild(td);
      td.appendChild(span);

      return span;
    }
    else {
      var li = document.createElement("li");
      var span = document.createElement("span");
      li.appendChild(span);
      outerElemet.appendChild(li);
      return span;
    }
  };
  this.addOuterElement = function(spanElement) {
    if (this.needTable()) {
      var table = document.createElement("table");
      spanElement.appendChild(table);
      return table;
    }
    else {
      var list = document.createElement("ul");
      spanElement.appendChild(list);
      return list;
    }
  };

  this.needTable = function() {
    return this.layout && this.layout == "table";
  };
  this.updateMessageSpan = function(message, css, spanElement) {

    if (message && spanElement) {
      var summary = message.summary;
      var detail = message.detail;
      var spanTitle = this.title;

      var showSummaryAsTooltip = !this.title && this.tooltip;

      if (showSummaryAsTooltip) {
        spanTitle = summary;
      }
      if (spanTitle) {
        spanElement.title = spanTitle;
      }

      var resultSmmary = this.showSummary && summary;
      var resultDetail = this.showDetail && detail;

      var text = "";
      if (resultSmmary && !showSummaryAsTooltip) {
        text = summary;
        if (resultDetail) {
          text = text + " ";
        }
      }
      if (resultDetail) {
        text = text + detail;
      }
      //if (css && css.length > 0)
      //  spanElement.className = css;

      var severetyStyle = this.getSeverityStyle(message.severity);
      var severetyClass = this.getSeverityClass(message.severity);

      spanElement.innerHTML += text;
      if (severetyStyle && severetyStyle.length > 0) {
        spanElement.setAttribute("style", severetyStyle);
      }
      if (severetyClass && severetyClass.length > 0) {
        spanElement.className = severetyClass;
      }
      if (this.params) {
        for (var attr in this.params) {
          spanElement.setAttribute(attr, this.params[attr]);
        }
      }
    }

  };
};

O$.extend(O$._MessagesRenderer.prototype, {
  update: function() {
    this.renderAllMessages();
  },

  getSeverityStyle: function(severity) {
    if (!severity)
      return this.style;
    if (!this.styles)
      return this.style;

    var s = severity + "Style";

    for (var attr in this.styles) {
      if (attr.toLowerCase() == s.toLowerCase())
        return this.styles[attr];
    }
    return this.style;
  },

  getSeverityClass: function(severity) {
    if (!severity)
      return this.css;
    if (!this.styles)
      return this.css;

    var s = severity + "Class";

    for (var attr in this.styles) {
      if (attr.toLowerCase() == s.toLowerCase())
        return this.styles[attr];
    }
    return this.css;
  }
});
