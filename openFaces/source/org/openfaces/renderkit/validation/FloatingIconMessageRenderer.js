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
O$._FloatingIconMessageRenderer = function(clientId, forClientId, imageUrl, topOffset, leftOffset, css, hideImg, showSummary, showDetail, defaultPresentation) {
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
  this.showImg = !hideImg;
  this.showSummary = showSummary;
  this.showDetail = showDetail;
  this.defaultPresentation = defaultPresentation;
}

O$._FloatingIconMessageRenderer.prototype.renderMessage = function(message) {
  if (this.defaultPresentation && O$._isComponentHasPresentation(this.forId)) {
    return;
  }
  var forElement = O$.byIdOrName(this.forId);

  if (message) {
    var messageText = "";
    if (this.showSummary) messageText = message.summary;
    if (this.showDetail) messageText = message.detail;
    if (this.showDetail && this.showSummary)messageText = message.summary + "; " + message.detail;
    if (!this.showDetail && !this.showSummary) messageText = message.summary;

    if (this.showImg /*|| (!this.showImg && !this.errorCss)*/) {
      var messageElement = O$(this.clientId);
      if (!messageElement) {
        messageElement = document.createElement("img");
        messageElement.id = this.clientId;
        messageElement.src = this.imageUrl;
        messageElement.style.position = 'absolute';
        messageElement.style.zIndex = 700;
        document.body.appendChild(messageElement);
        O$.getClientMessageRenderersWithVisibleBubble()[this.clientId + '_' + this.forId] = this;
      }
      this.position();
      messageElement.alt = messageText;
      messageElement.title = messageText;

    }
    if (!forElement._of_titleChanged) {
      forElement._of_title = forElement.title;
      forElement._of_titleChanged = true;
    }
    //    forElement.title = message.detail;
    forElement.title = messageText;

    if (this.errorCss && !forElement._of_cssChanged) {
      forElement._of_className = forElement.className;
      forElement.className = forElement.className + " " + this.errorCss;
      forElement._of_cssChanged = true;
      O$.repaintAreaForOpera(forElement, true);
    }

  } else {
    var messageElement = O$(this.clientId);
    if (messageElement) {
      var parentNode = messageElement.parentNode;
      parentNode.removeChild(messageElement);
    }
    if (forElement._of_titleChanged) {
      forElement.title = forElement._of_title;
      forElement._of_title = undefined;
      forElement._of_titleChanged = undefined;
    }
    if (forElement._of_cssChanged) {
      forElement.className = forElement._of_className;
      forElement._of_className = undefined;
      forElement._of_cssChanged = undefined;
      O$.repaintAreaForOpera(forElement, true);
    }
  }
}

O$._FloatingIconMessageRenderer.prototype.update = function() {
  var forElement = O$.byIdOrName(this.forId);
  //changed logic of message rendering
  // now we need to check for component until we'll find it, required for Ajax Support
  //  if (forElement == null) {
  //    setTimeout(function() {
  //      this.update();
  //      alert("This update");
  //    }, 10);
  //    return;
  //  }
  var message;
  if (forElement) {
    message = O$.getMessages(forElement)[0];
    this.renderMessage(message);
  }
}

O$._FloatingIconMessageRenderer.prototype.position = function() {
  var messageElement = O$(this.clientId);
  if (messageElement) {
    var forElement = O$.byIdOrName(this.forId);

    if (!O$.isVisibleRecursive(forElement)) {
      messageElement.style.display = "none";
      return;
    }
    messageElement.style.display = "";

    var elementPos = O$.getElementPos(forElement);
    var left = elementPos.left + this.leftOffset;
    var top = elementPos.top + this.topOffset;

    messageElement.style.left = left + "px";
    messageElement.style.top = top + "px";
  }
}
