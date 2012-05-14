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

O$._MessageRenderer = function(clientId, forClientId, title, tooltip, showSummary, showDetail, style, css, styles, params) {
  this.clientId = clientId;
  this.forId = forClientId;
  this.tooltip = tooltip;
  this.title = title;
  this.showSummary = showSummary;
  this.showDetail = showDetail;
  this.style = style;
  this.css = css;
  this.styles = styles;
  this.params = params;
};

O$.extend(O$._MessageRenderer.prototype, {
  update: function() {
    var spanElement = O$(this.clientId);
    if (!spanElement)
      return;
    while (spanElement.firstChild) {
      spanElement.removeChild(spanElement.firstChild);
      spanElement.title = null;
    }
    var forElement = O$.byIdOrName(this.forId);
    if (forElement) {
      var message = O$.getMessages(forElement)[0];
      if (message) {
        var summary = message.summary;
        var detail = message.detail;

        var severityStyle = this.getSeverityStyle(message.severity);
        var severityClass = this.getSeverityClass(message.severity);

        var title = this.title;
        var showSummaryAsTooltip = !title && this.tooltip;
        var showSummary = this.showSummary && summary;
        var showDetail = this.showDetail && detail;
        var summaryRenderedAsTitle = showSummaryAsTooltip && showSummary && showDetail;
        if (summaryRenderedAsTitle) {
          title = summary;
        }
        if (title) {
          spanElement.title = title;
        }
        var text = "";
        if (showSummary && !summaryRenderedAsTitle) {
          text = summary;
          if (showDetail) {
            text = text + " ";
          }
        }
        if (showDetail) {
          text = text + detail;
        }

        var textNode = document.createTextNode(text);

        spanElement.appendChild(textNode);
        if (spanElement.style.display == "none") {
          spanElement.style.display = "block";
        }
        if (severityStyle && severityStyle.length > 0) {
          spanElement.setAttribute("style", severityStyle);
        }
        if (severityClass && severityClass.length > 0) {
          spanElement.className = severityClass;
        }
        if (this.params) {
          for (var attr in this.params) {
            spanElement.setAttribute(attr, this.params[attr]);
          }
        }
      } else {
        if (spanElement.style.display == "block") {
          spanElement.style.display = "none";
        }
      }
    }
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