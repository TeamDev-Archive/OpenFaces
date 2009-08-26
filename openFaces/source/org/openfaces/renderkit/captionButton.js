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

O$._initCaptionButton = function(componentId,
                                 supportActionAttribute,
                                 containerId,
                                 rolloverStyle,
                                 pressedStyle,
                                 url,
                                 rolloverUrl,
                                 pressedUrl) {
  var btn = O$(componentId);
  btn._image = btn.getElementsByTagName("img")[0];
  btn._container = O$(containerId);

  if (!rolloverUrl)
    rolloverUrl = url;
  if (!pressedUrl)
    pressedUrl = rolloverUrl;

  btn._url = url;
  btn._rolloverUrl = rolloverUrl;
  btn._pressedUrl = pressedUrl;

  O$.preloadImages([url, rolloverUrl, pressedUrl]);

  btn._updateImage = function(mouseInside, mousePressed) {
    if (mouseInside != undefined) btn._mouseInside = mouseInside;
    if (mousePressed != undefined) btn._mousePressed = mousePressed;
    if (!btn._mouseInside)
      btn._image.src = btn._url;
    else if (!btn._mousePressed)
      btn._image.src = btn._rolloverUrl;
    else
      btn._image.src = btn._pressedUrl;
    O$.setStyleMappings(btn, {
      rollover: mouseInside ? rolloverStyle : null,
      pressed: mousePressed ? pressedStyle : null});
  };

  O$.setupHoverAndPressStateFunction(btn, function(mouseInside, mousePressed) {
    btn._updateImage(mouseInside, mousePressed);
  });
  btn._updateImage(false, false);

  btn._prevOnmousedown = btn.onmousedown;
  btn.onmousedown = function(e) {
    if (btn._prevOnmousedown)
      btn._prevOnmousedown(e);
    O$.breakEvent(e);
  };
  if (supportActionAttribute)
    O$.addEventHandler(btn, "click", function() {
      O$.submitFormWithAdditionalParam(btn, componentId + "::clicked", "true");
    });
}

O$._initToggleCaptionButton = function(controlId,
                                       toggled,
                                       containerId,
                                       rolloverStyle,
                                       pressedStyle,
                                       url,
                                       rolloverUrl,
                                       pressedUrl,
                                       toggledUrl,
                                       toggledRolloverUrl,
                                       toggledPressedUrl) {
  var btn = O$(controlId);
  btn._stateHolderId = controlId + "::toggleState";
  btn._toggled = toggled;

  O$._initCaptionButton(controlId, false, containerId, rolloverStyle, pressedStyle, url, rolloverUrl, pressedUrl);

  if (!toggledRolloverUrl)
    toggledRolloverUrl = toggledUrl;
  if (!toggledPressedUrl)
    toggledPressedUrl = toggledRolloverUrl;
  btn._toggleUrl = toggledUrl;
  btn._toggleRolloverUrl = toggledRolloverUrl;
  btn._togglePressedUrl = toggledPressedUrl;
  btn._stateChange_listeners = null;

  O$.preloadImages([toggledUrl, toggledRolloverUrl, toggledPressedUrl]);

  btn._updateImage = function(mouseInside, mousePressed) {
    if (mouseInside != undefined) btn._mouseInside = mouseInside;
    if (mousePressed != undefined) btn._mousePressed = mousePressed;
    if (!btn._mouseInside)
      btn._image.src = btn._toggled ? btn._toggleUrl : btn._url;
    else if (!btn._mousePressed)
      btn._image.src = btn._toggled ? btn._toggleRolloverUrl : btn._rolloverUrl;
    else
      btn._image.src = btn._toggled ? btn._togglePressedUrl : btn._pressedUrl;
    O$.setStyleMappings(btn, {
      rollover: mouseInside ? rolloverStyle : null,
      pressed: mousePressed ? pressedStyle : null});
  };
  btn._updateImage(false, false);

  var oldToggleBtnClickHandler = btn.onclick;
  btn.onclick = function() {
    this.setToggleState(!this._toggled);
    if (oldToggleBtnClickHandler)
      oldToggleBtnClickHandler();
  };
  btn.ondblclick = O$.repeatClickOnDblclick;


  btn._addToggleStateChangeListener = function(listenerObjectFunction) {
    if (!this._stateChangeListeners)
      this._stateChangeListeners = [];

    this._stateChangeListeners.push(listenerObjectFunction);
  };

  btn.setToggleState = function(expanded, skipListenerNotifications) {
    this._toggled = expanded;
    var stateHolder = O$(this._stateHolderId);
    stateHolder.value = expanded;
    this._updateImage();
    if (!skipListenerNotifications)
      O$._notifyToggleStateChange(this);
  };
}

O$._initExpansionToggleButton = function(clientId) {
  O$._initToggleCaptionButton.apply(null, arguments);
  var btn = O$(clientId);
  if (!btn._container._expansionToggleButtons)
    btn._container._expansionToggleButtons = [];
  btn._container._expansionToggleButtons.push(btn);
  btn._addToggleStateChangeListener(function(expanded) {
    btn._container.setExpanded(expanded);
  });
}


O$._notifyToggleStateChange = function(toggleBtn) {
  var listeners = toggleBtn._stateChangeListeners;
  for (var i = 0, count = listeners ? listeners.length : 0; i < count; i++)
    listeners[i](toggleBtn._toggled);
}

