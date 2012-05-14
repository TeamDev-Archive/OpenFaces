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
O$._FloatingIconMessageRenderer = function(clientId, forClientId, imageUrl, topOffset, leftOffset, css, events,
                                           hideImg, showSummary, showDetail, defaultPresentation) {
  this.clientId = clientId;
  var messageElement = O$(this.clientId);
  if (messageElement) {
    var parentNodeElement = messageElement.parentNode;
    parentNodeElement.removeChild(messageElement);
  }
  this.forId = forClientId;
  this.imageUrl = imageUrl;
  this.topOffset = topOffset;
  this.leftOffset = leftOffset;
  this.errorCss = css;
  this.iconEvents = events;
  this.showImg = !hideImg;
  this.showSummary = showSummary;
  this.showDetail = showDetail;
  this.defaultPresentation = defaultPresentation;
};

O$.extend(O$._FloatingIconMessageRenderer.prototype, {
  renderMessage: function(message) {
    if (this.defaultPresentation && O$._isComponentHasPresentation(this.forId)) {
      return;
    }
    var forElement = O$.byIdOrName(this.forId);
    var messageElement = O$(this.clientId);
    if (message) {
      var messageText = "";
      if (this.showSummary) messageText = message.summary;
      if (this.showDetail) messageText = message.detail;
      if (this.showDetail && this.showSummary) messageText = message.summary + "; " + message.detail;
      if (!this.showDetail && !this.showSummary) messageText = message.summary;

      if (this.showImg) {
        if (!messageElement) {
          messageElement = document.createElement("img");
          messageElement.id = this.clientId;
          messageElement.src = this.imageUrl;
          messageElement.style.position = "absolute";
          O$.assignEvents(messageElement, this.iconEvents);
          document.body.appendChild(messageElement);
          O$.getClientMessageRenderersWithVisibleBubble()[this.clientId + "_" + this.forId] = this;
          O$.correctElementZIndex(messageElement, forElement, 0); // same as for the validated component
        }
        this.position();
        messageElement.alt = messageText;
        messageElement.title = messageText;

      }
      if (!forElement._of_titleChanged) {
        forElement._of_title = forElement.title;
        forElement._of_titleChanged = true;
      }

      forElement.title = messageText;

      if (this.errorCss) {
        O$.setStyleMappings(forElement, {validation_floatingIconMessage: this.errorCss});
        O$.repaintAreaForOpera(forElement, true);
      }

    } else {
      if (messageElement) {
        var parentNode = messageElement.parentNode;
        parentNode.removeChild(messageElement);
      }
      if (forElement._of_titleChanged) {
        forElement.title = forElement._of_title;
        forElement._of_title = undefined;
        forElement._of_titleChanged = undefined;
      }
      O$.setStyleMappings(forElement, {validation_floatingIconMessage: null});
      O$.repaintAreaForOpera(forElement, true);
    }
  },

  update: function() {
    var forElement = O$.byIdOrName(this.forId);
    var message;
    if (forElement) {
      message = O$.getMessages(forElement)[0];
      this.renderMessage(message);
    }
  },

  position: function() {
    var messageElement = O$(this.clientId);
    if (messageElement) {
      var forElement = O$.byIdOrName(this.forId);

      if (!O$.isVisibleRecursive(forElement)) {
        messageElement.style.display = "none";
        return;
      }
      messageElement.style.display = "";

      var elementPos = O$.getElementPos(forElement);
      var left = elementPos.x + this.leftOffset;
      var top = elementPos.y + this.topOffset;

      messageElement.style.left = left + "px";
      messageElement.style.top = top + "px";
    }
  }

});

