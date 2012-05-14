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

// ================================== END OF PUBLIC API METHODS

O$.Confirmation = {
  _init: function(
          confirmationId,
          invokerId,
          eventHandlerName,
          defaultButton,
          bindToInvoker, styleParams) {
    var confirmation = O$.initComponent(confirmationId, null, {
      _invokerId: invokerId,
      _eventHandlerName: eventHandlerName ? eventHandlerName : "onclick",
      _defaultButton: defaultButton,
      _bindToInvoker: bindToInvoker,

      _buttonArea: O$(confirmationId + "::buttonArea"),

      _listenerMode: 0,

      _icon: O$(confirmationId + "::icon"),
      _messageText: O$(confirmationId + "::headerText"),
      _detailsText: O$(confirmationId + "::detailsText"),
      _okButton: O$(confirmationId + "::yes_button"),
      _cancelButton: O$(confirmationId + "::no_button"),

      setTexts: function (messageText, detailsText, okButtonText, cancelButtonText) {
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
      },

      runConfirmedFunction: function (func) {
        confirmation._listenerMode = 1;
        confirmation._evaluatedFunction = func;
        confirmation._confirmationShow();
        return false;
      },

      _showForEvent: function (e) {
        confirmation.invokerEvent = O$.getEvent(e);
        confirmation._confirmationShow();
        return false;
      },

      _getDefaultFocusComponent: function() {
        if (confirmation._defaultButton == "ok")
          return confirmation._okButton;
        else
          return confirmation._cancelButton;
      },

      _confirmationShow: function () {
        var invoker = O$(confirmation._invokerId);
        var oldScrollPos = O$.getPageScrollPos();
        if (invoker)
          O$.correctElementZIndex(confirmation, invoker);
        O$.Confirmation._layoutConfirmation(confirmation, oldScrollPos);

        confirmation.show();

        // if not left-top set, move it to the center
        O$.Confirmation._layoutConfirmation(confirmation, oldScrollPos);

        // Fix for FF, when caption has a border
        if (confirmation._caption) {
          confirmation._caption.style.top = "0px";
          confirmation._caption.style.left = "0px";
          if (O$.isMozillaFF() || O$.isExplorer()) {
            if (!confirmation._caption.style.width) {
              confirmation._caption.style.width = confirmation.clientWidth - (confirmation._caption.clientWidth - confirmation.clientWidth) + "px";
            }
          }
        }
      }
    });

    confirmation._okButton.onclick = function (event) {
      confirmation.hide();
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

      if (confirmation._listenerMode != 1)
        throw "confirmation._okButton.onclick: unknown confirmation._listenerMode" + confirmation._listenerMode;
      // runConfirmedFunction
      confirmation._listenerMode = 0;
      if (confirmation._evaluatedFunction) {
        confirmation._evaluatedFunction();
      }
    };

    confirmation._cancelButton.onclick = function () {
      confirmation.hide();
      return false;
    };

    // Set listener on element
    if (confirmation._invokerId && confirmation._eventHandlerName) {
      var attachConfirmation = function() {
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
          var idx = invoker._of_confirmationIds.indexOf(confirmation.id);
          invoker._of_confirmationIds.splice(idx - 1, 1);
        }
        if (!O$._confirmationEventHandlers)
          O$._confirmationEventHandlers = [];
        if (!O$._confirmationEventHandlers[confirmation.id]) {
          O$._confirmationEventHandlers[confirmation.id] = invoker[confirmation._eventHandlerName];
        }

        invoker[confirmation._eventHandlerName] = function (event) {
          confirmation._showForEvent(event);
          return false;
        };

        if (!invoker._of_confirmationIds)
          invoker._of_confirmationIds = [];
        // we save ids of all registered confirmations to support use-case when confirmation is reloaded with a4j
        invoker._of_confirmationIds.push(confirmation.id);
      };
      var invoker = O$(confirmation._invokerId);
      if (invoker) { // this branch is to support loading confirmation through ajax/a4j
        attachConfirmation();
      } else {
        O$.addLoadEvent(function () {
          attachConfirmation();
        });
      }
    }

    O$.addLoadEvent(function() {
      // pull confirmation to the top hierarchy level to avoid possible z-index problems. E.g. a problem can occur if
      // a confirmation is placed inside of an absolutely-positioned layer with z-index less than z-index of
      // confirmation's blocking layer
      var newParent = O$.getDefaultAbsolutePositionParent();
      newParent.appendChild(confirmation);
    });

    O$.Confirmation._initInnerStyles.apply(confirmation, styleParams);
  },

  _initInnerStyles: function(iconAreaStyle, rolloverIconAreaStyle,
                             contentStyle, rolloverContentStyle, messageTextStyle, rolloverMessageTextStyle,
                             detailsTextStyle, rolloverDetailsTextStyle, buttonAreaStyle,
                             rolloverButtonAreaStyle, okButtonStyle, rolloverOkButtonStyle,
                             cancelButtonStyle, rolloverCancelButtonStyle) {
    if (this._icon) this._icon.className = iconAreaStyle;

    this._content.className = contentStyle;
    this._buttonArea.className = buttonAreaStyle;
    if (this._messageText) this._messageText.className = messageTextStyle;
    if (this._detailsText) this._detailsText.className = detailsTextStyle;
    this._okButton.className = okButtonStyle;
    this._cancelButton.className = cancelButtonStyle;

    O$.setupHoverStateFunction(this, function(mouseInside) {
      if (mouseInside) {
        if (this._icon) this._icon.className = rolloverIconAreaStyle;
        this._content.className = rolloverContentStyle;
        this._buttonArea.className = rolloverButtonAreaStyle;
        if (this._messageText) this._messageText.className = rolloverMessageTextStyle;
        if (this._detailsText) this._detailsText.className = rolloverDetailsTextStyle;
      } else {
        if (this._icon) this._icon.className = iconAreaStyle;
        this._content.className = contentStyle;
        this._buttonArea.className = buttonAreaStyle;
        if (this._messageText) this._messageText.className = messageTextStyle;
        if (this._detailsText) this._detailsText.className = detailsTextStyle;
      }
    });

    O$.setupHoverStateFunction(this._okButton, function(mouseInside) {
      O$.setStyleMappings(this, {_rolloverStyle: mouseInside ? rolloverOkButtonStyle : null});
    });

    O$.setupHoverStateFunction(this._cancelButton, function(mouseInside) {
      O$.setStyleMappings(this, {_rolloverStyle: mouseInside ? rolloverCancelButtonStyle : null});
    });
  },

  _layoutConfirmation: function(confirmation, oldScrollPos) {
    var invoker = confirmation._invokerId ? O$(confirmation._invokerId) : null;
    confirmation._invoker = invoker;
    if (invoker && confirmation._bindToInvoker) {
      var invokerRect = O$.getElementBorderRectangle(invoker);
      confirmation.setLeft(invokerRect.getMinX());
      confirmation.setTop(invokerRect.getMaxY());
    } else {
      O$.PopupLayer._centerPopup(confirmation, oldScrollPos);
    }
  }
};