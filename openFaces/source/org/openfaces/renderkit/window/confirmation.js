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

// ================================== END OF PUBLIC API METHODS

O$._initConfirmation = function(
        confirmationId,
        invokerId,
        eventHandlerName,
        defaultButton,
        bindToInvoker) {
  var confirmation = O$(confirmationId);
  confirmation._invokerId = invokerId;
  if (!eventHandlerName)
    eventHandlerName = "onclick";
  confirmation._eventHandlerName = eventHandlerName;
  confirmation._defaultButton = defaultButton;
  confirmation._bindToInvoker = bindToInvoker;

  confirmation._buttonArea = O$(confirmationId + "::buttonArea");

  confirmation._listenerMode = 0;

  confirmation._icon = O$(confirmationId + "::icon");
  confirmation._messageText = O$(confirmationId + "::headerText");
  confirmation._detailsText = O$(confirmationId + "::detailsText");
  confirmation._okButton = O$(confirmationId + "::yes_button");
  confirmation._cancelButton = O$(confirmationId + "::no_button");

  confirmation._okButton.onclick = function (event) {
    confirmation._confirmationHide();
    if (confirmation._listenerMode == 0) { // listen to event with eventHandlerName of element with invokerId
      var invoker = confirmation._invoker ? confirmation._invoker : O$(confirmation._invokerId);
      if (!invoker)
        invoker = confirmation;

      var eventHandler = O$._confirmationEventHandlers[confirmation.id];
      var result = undefined;
      if (eventHandler) { // invoke eventHandler with "this" variable pointing to invoker...
        invoker._of_invokeYesHandler = eventHandler;
        invoker._of_fnc = O$.getEventHandlerFunction("_of_invokeYesHandler", null, invoker);
        result = invoker._of_fnc(event);
      }
      if (invoker.type == "submit") {
        result = result !== false;
        if (result) {
          invoker.onclick = null;
          invoker.click();
        }
      }
      return;
    }

    if (confirmation._listenerMode == 1) { // runConfirmedFunction
      confirmation._listenerMode = 0;
      if (confirmation._evaluatedFunction) {
        confirmation._evaluatedFunction();
      }
      return;
    }

    // runConfirmedFunctionByName
    confirmation._listenerMode = 0;
    var evalString = confirmation._evaluatedFunctionName + "("

    if (confirmation._evaluatedFunctionParameters) {
      for (var i = 0; i < confirmation._evaluatedFunctionParameters.length; i++) {
        if (i > 0) {
          evalString += ", ";
        }
        evalString += "confirmation._evaluatedFunctionParameters[" + i + "]";
      }
    }
    evalString += ");"

    eval(evalString);
  }

  confirmation._cancelButton.onclick = function () {
    confirmation._confirmationHide();
    return false;
  }

  if (confirmation._closeButton) {
    confirmation._closeButton.onclick = function () {
      confirmation._confirmationHide();
      return false;
    }
  }

  // Set listener on element
  if (confirmation._invokerId && confirmation._eventHandlerName) {
    var attachConfirmation = function () {
      var invoker = O$(confirmation._invokerId);
      if (!invoker) {
        var thisTime = new Date().getTime();
        if (!confirmation._firstAttachAttemptTime) {
          confirmation._firstAttachAttemptTime = thisTime;
          confirmation._elapsedSinceFirstAttachAttempt = 0;
        } else {
          // don't take large chunks of javascript-busy time into account, count only execution-free time when timeouts flow reliably
          var elapsedSinceLastAttempt = thisTime - confirmation._previousAttachAttemptTime;
          if (elapsedSinceLastAttempt < 100)
            confirmation._elapsedSinceFirstAttachAttempt += elapsedSinceLastAttempt;
        }
        if (confirmation._elapsedSinceFirstAttachAttempt > 5000)
          throw "Invalid invokerId for confirmation. Couldn't find component with clientId: " + confirmation._invokerId;
        confirmation._previousAttachAttemptTime = thisTime;
        setTimeout(attachConfirmation, 30);
        return;
      }
      if (invoker._of_confirmationIds && O$.arrayContainsValue(invoker._of_confirmationIds, confirmation.id)) {
        var idx = O$.findValueInArray(confirmation.id, invoker._of_confirmationIds);
        invoker._of_confirmationIds.splice(idx - 1, 1);
      }
      if (!O$._confirmationEventHandlers)
        O$._confirmationEventHandlers = new Array();
      if (!O$._confirmationEventHandlers[confirmation.id]) {
        O$._confirmationEventHandlers[confirmation.id] = invoker[confirmation._eventHandlerName];
      }

      invoker[confirmation._eventHandlerName] = function (event) {
        confirmation._showForEvent(event);
        return false;
      };

      if (!invoker._of_confirmationIds)
        invoker._of_confirmationIds = new Array();
      // we save ids of all registered confirmations to support use-case when confirmation is reloaded with a4j
      invoker._of_confirmationIds.push(confirmation.id);
    }
    var invoker = O$(confirmation._invokerId);
    if (invoker) { // this branch is to support loading confirmation through ajax/a4j
      attachConfirmation();
    } else {
      O$.addLoadEvent(function () {
        attachConfirmation();
      });
    }
  }

  confirmation._showForEvent = function (e) {
    confirmation.invokerEvent = O$.getEvent(e);
    confirmation._confirmationShow();
    return false;
  }


  confirmation._confirmationShow = function () {
    var invoker = O$(confirmation._invokerId);
    var oldScrollPos = O$.getPageScrollPos();
    if (invoker)
      O$.correctElementZIndex(confirmation, invoker);
    O$._layoutConfirmation(confirmation, oldScrollPos);

    confirmation.show();

    // if not left-top set, move it to the center
    O$._layoutConfirmation(confirmation, oldScrollPos);

    // IE fixes for modality
    /*
        if(document.body.leftMargin) {
          confirmation.blockingLayer.style.left = (confirmation.blockingLayer.offsetLeft*1 + document.body.leftMargin*1) + "px";
        }
    */

    confirmation._okButton.onfocus = function () {
      confirmation._currentFocus = 0;
    }

    confirmation._cancelButton.onfocus = function () {
      confirmation._currentFocus = 1;
    }

    if (O$.isOpera()) {
      confirmation._oldDocumentOnMouseDown = document.onmousedown;
      document.onmousedown = function () {
        if (confirmation._currentFocus == 0) {
          confirmation._okButton.focus();
        } else {
          confirmation._cancelButton.focus();
        }
      }
    } else {
      confirmation._oldDocumentOnClick = document.onclick;
      document.onclick = function () {
        if (confirmation.isVisible()) { // needed for IE for cases when hideOnOuterClick == true
          if (confirmation._currentFocus == 0) {
            confirmation._okButton.focus();
          } else {
            confirmation._cancelButton.focus();
          }
        }
      }
    }

    // Fix for FF, when caption has a border
    if (confirmation._caption) {
      confirmation._caption.style.top = "0px";
      confirmation._caption.style.left = "0px";
      if (O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/ || O$.isExplorer()) { // todo: checking O$.isExplorer might not be needed here -- check this
        if (!confirmation._caption.style.width) {
          confirmation._caption.style.width = confirmation.clientWidth - (confirmation._caption.clientWidth - confirmation.clientWidth) + "px";
        }
      }
    }

    // set focus on button
    if (confirmation._defaultButton == "ok") {
      confirmation._currentFocus = 0;
      confirmation._okButton.focus();
    } else {
      confirmation._currentFocus = 1;
      confirmation._cancelButton.focus();
    }
  }

  confirmation._confirmationHide = function () {
    if (!this.isVisible()) return;

    if (O$.isOpera()) {
      document.onmousedown = confirmation._oldDocumentOnMouseDown;
    } else {
      document.onclick = confirmation._oldDocumentOnClick;
    }

    confirmation.hide();
  }

  // set escape behavior
  confirmation._okButton.onkeydown = function (e) {
    var evt = (e != undefined) ? e : (event ? event : null);
    if (!evt) return;
    var keyCode = evt.keyCode;
    if (keyCode == 27) {
      confirmation._confirmationHide();
    }
  }

  confirmation._cancelButton.onkeydown = confirmation._okButton.onkeydown;

  confirmation.setTexts = function (messageText, detailsText, okButtonText, cancelButtonText) {
    if (messageText || messageText == "") {
      confirmation._messageText.innerHTML = messageText;
    }
    if (detailsText || detailsText == "") {
      confirmation._detailsText.innerHTML = detailsText;
    }
    if (okButtonText || okButtonText == "") {
      confirmation._okButton.value = okButtonText;
    }
    if (cancelButtonText || cancelButtonText == "") {
      confirmation._cancelButton.value = cancelButtonText;
    }
  }

  confirmation.runConfirmedFunctionByName = function (funcName, parameters) { // todo: not needed anymore -- remove this function and the related code
    confirmation._listenerMode = 2;
    confirmation._evaluatedFunctionName = funcName;
    confirmation._evaluatedFunctionParameters = parameters;
    confirmation._confirmationShow();
    return false;
  }

  confirmation.runConfirmedFunction = function (func) {
    confirmation._listenerMode = 1;
    confirmation._evaluatedFunction = func;
    confirmation._confirmationShow();
    return false;
  }

  O$.addLoadEvent(function() {
    // pull confirmation to the top hierarchy level to avoid possible z-index problems. E.g. a problem can occur if
    // a confirmation is placed inside of an absolutely-positioned layer with z-index less than z-index of
    // confirmation's blocking layer
    var newParent = O$.getDefaultAbsolutePositionParent();
    newParent.appendChild(confirmation);
  });

}

O$._initConfirmationInnerStyles = function(confirmationId, iconAreaStyle, rolloverIconAreaStyle,
                                           contentStyle, rolloverContentStyle, messageTextStyle, rolloverMessageTextStyle,
                                           detailsTextStyle, rolloverDetailsTextStyle, buttonAreaStyle,
                                           rolloverButtonAreaStyle, okButtonStyle, rolloverOkButtonStyle,
                                           cancelButtonStyle, rolloverCancelButtonStyle) {
  var confirmation = O$(confirmationId);

  confirmation.iconAreaStyle = iconAreaStyle;
  confirmation.rolloverIconAreaStyle = rolloverIconAreaStyle;
  confirmation.contentStyle = contentStyle;
  confirmation.rolloverContentStyle = rolloverContentStyle;
  confirmation.messageTextStyle = messageTextStyle;
  confirmation.rolloverMessageTextStyle = rolloverMessageTextStyle;
  confirmation.detailsTextStyle = detailsTextStyle;
  confirmation.rolloverDetailsTextStyle = rolloverDetailsTextStyle;
  confirmation.okButtonStyle = okButtonStyle;
  confirmation.rolloverOkButtonStyle = rolloverOkButtonStyle;
  confirmation.buttonAreaStyle = buttonAreaStyle;
  confirmation.rolloverButtonAreaStyle = rolloverButtonAreaStyle;
  confirmation.cancelButtonStyle = cancelButtonStyle;
  confirmation.rolloverCancelButtonStyle = rolloverCancelButtonStyle;

  if (confirmation._icon) {
    confirmation._icon.className = confirmation.iconAreaStyle;
  }
  confirmation._content.className = confirmation.contentStyle;
  confirmation._buttonArea.className = confirmation.buttonAreaStyle;
  if (confirmation._messageText) {
    confirmation._messageText.className = confirmation.messageTextStyle;
  }
  if (confirmation._detailsText) {
    confirmation._detailsText.className = confirmation.detailsTextStyle;
  }
  confirmation._okButton.className = confirmation.okButtonStyle;
  confirmation._cancelButton.className = confirmation.cancelButtonStyle;

  confirmation.oldOnMouseOver = confirmation.onmouseover;
  confirmation.onmouseover = function (e) {
    if (confirmation.oldOnMouseOver) {
      confirmation.oldOnMouseOver(e);
    }
    if (confirmation._onmouseover) {
      confirmation._onmouseover(e);
    }
    if (confirmation._icon) {
      confirmation._icon.className = confirmation.rolloverIconAreaStyle;
    }
    confirmation._content.className = confirmation.rolloverContentStyle;
    confirmation._buttonArea.className = confirmation.rolloverButtonAreaStyle;
    if (confirmation._messageText) {
      confirmation._messageText.className = confirmation.rolloverMessageTextStyle;
    }
    if (confirmation._detailsText) {
      confirmation._detailsText.className = confirmation.rolloverDetailsTextStyle;
    }
  }

  confirmation.oldOnMouseOut = confirmation.onmouseout;
  confirmation.onmouseout = function (e) {
    if (confirmation.oldOnMouseOut) {
      confirmation.oldOnMouseOut(e);
    }
    if (confirmation._onmouseout) {
      confirmation._onmouseout(e);
    }
    if (confirmation._icon) {
      confirmation._icon.className = confirmation.iconAreaStyle;
    }
    confirmation._content.className = confirmation.contentStyle;
    confirmation._buttonArea.className = confirmation.buttonAreaStyle;
    if (confirmation._messageText) {
      confirmation._messageText.className = confirmation.messageTextStyle;
    }
    if (confirmation._detailsText) {
      confirmation._detailsText.className = confirmation.detailsTextStyle;
    }
  }

  confirmation._okButton.onmouseover = function () {
    confirmation._okButton.className = confirmation.rolloverOkButtonStyle;
  }

  confirmation._okButton.onmouseout = function () {
    confirmation._okButton.className = confirmation.okButtonStyle;
  }

  confirmation._cancelButton.onmouseover = function () {
    confirmation._cancelButton.className = confirmation.rolloverCancelButtonStyle;
  }

  confirmation._cancelButton.onmouseout = function () {
    confirmation._cancelButton.className = confirmation.cancelButtonStyle;
  }
}

O$._layoutConfirmation = function(confirmation, oldScrollPos) {
  var invoker = confirmation._invokerId ? O$(confirmation._invokerId) : null;
  confirmation._invoker = invoker;
  if (invoker && confirmation._bindToInvoker) {
    var invokerRect = O$.getElementBorderRectangle(invoker);
    confirmation.setLeft(invokerRect.getMinX());
    confirmation.setTop(invokerRect.getMaxY());
  } else {
    O$._centerPopup(confirmation, oldScrollPos);
  }
}