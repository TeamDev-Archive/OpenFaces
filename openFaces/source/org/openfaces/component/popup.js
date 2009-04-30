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

O$._initEventHandler = function() {
  if (document._openFaces_clickHandler) {
    return;
  }
  document._openFaces_clickHandler = O$._popupClicked;
  var _clickHandler = O$.getEventHandlerFunction("_openFaces_clickHandler", null, document);
  document._addClickListener(_clickHandler);
}

O$._popupClicked = function(e) {
  var evt = O$.getEvent(e);
  if (!evt) return;

  var clickedElement;
  if (evt.target) {
    clickedElement = evt.target;
  } else {
    clickedElement = evt.srcElement;
  }

  var clickedElementId = clickedElement.id;

  if (document._openFaces_popupsOnpage)
    for (var i = 0, count = document._openFaces_popupsOnpage.length; i < count; i ++) {
      var popupId = document._openFaces_popupsOnpage[i];
      var popup = O$(popupId);
      if (!popup)
        continue; // popup can be removed from page with A4J
      var clickedOnChild = O$.isChild(popup, clickedElement);
      if (popupId == clickedElementId || clickedOnChild)
        continue;
      popup.hide();
    }
}

O$._initEventHandler();

O$._initPopup = function(popupId, useDisplayNoneByDefault) {
  var popup = O$(popupId);

  O$.initIETransparencyWorkaround(popup);

  if (!document._openFaces_popupsOnpage) {
    document._openFaces_popupsOnpage = new Array();
  }
  document._openFaces_popupsOnpage.push(popupId);
  if (useDisplayNoneByDefault) {
    if (popup._originalStyleDisplay == undefined)
      popup._originalStyleDisplay = popup.style.display;
    popup.style.display = "none";
  }
  popup._visibilityChangeListeners = null;
  popup._addVisibilityChangeListener = function(listenerObjectFunction) {
    if (!this._visibilityChangeListeners) {
      this._visibilityChangeListeners = new Array();
    }
    this._visibilityChangeListeners.push(listenerObjectFunction);
  }

  popup.setLeft = function (left) {
    popup.style.left = left + "px";
    if (popup._ieTransparencyControl)
      popup._ieTransparencyControl.style.left = left + "px";
  }

  popup.setTop = function (top) {
    popup.style.top = top + "px";
    if (popup._ieTransparencyControl)
      popup._ieTransparencyControl.style.top = top + "px";
  }

  popup._prepareForRearrangementBeforeShowing = function() {
    if (this.isVisible())
      return;
    if (this.style.display == "none")
      this.style.display = this._originalStyleDisplay;
  }

  popup.showAtXY = function(x, y) {
    popup.style.left = x + "px";
    popup.style.top = y + "px";
    this.show();
  }

  popup.show = function() {
    if (this.isVisible())
      return;

    setTimeout(function() {
      O$.addIETransparencyControl(popup);
    }, 1);
    this.style.visibility = "visible";
    if (this.style.display == "none")
      this.style.display = this._originalStyleDisplay;
    O$._popup_notifyVisibilityChangeListeners(this);
  }

  popup.hide = function() {
    if (!this.isVisible())
      return;

    O$.removeIETransparencyControl(popup);
    this.style.visibility = "hidden";
    if (this._originalStyleDisplay == undefined)
      this._originalStyleDisplay = this.style.display;
    if (O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/) {
      var oldOverflow = O$.getElementStyleProperty(this, "overflow");
      this.style.overflow = "hide"; // to avoid regaining focus when tabbing out from an opened DropDownField under Mozilla
      setTimeout(function() { // time-out is needed for Mozilla when selecting an item in DropDownField (JSFC-2620)
        popup.style.display = "none";
        popup.style.overflow = oldOverflow;
      }, 1);
    } else {
      this.style.display = "none";
    }
    O$._popup_notifyVisibilityChangeListeners(this);
  }

  popup.isVisible = function() {
    var visible = (this.style.visibility == "visible") && (this.style.display != "none");
    return visible;
  }
}

O$._popup_notifyVisibilityChangeListeners = function(popup) {
  var listeners = popup._visibilityChangeListeners;
  if (listeners && listeners.length > 0) {
    for (var i = 0; i < listeners.length; i ++) {
      listeners[i](popup.isVisible());
    }
  }
}

O$._hideAllPopupsExceptOne = function(popupToRetain) {
  if (!document._openFaces_popupsOnpage)
    return;
  for (var i = 0, count = document._openFaces_popupsOnpage.length; i < count; i ++) {
    var popupId = document._openFaces_popupsOnpage[i];
    var currPopup = O$(popupId);
    if (!currPopup)
      continue; // popup can be removed from page with A4J
    if (currPopup != popupToRetain)
      currPopup.hide();
  }
}
