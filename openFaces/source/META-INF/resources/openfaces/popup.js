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

O$.Popup = {
  PULLED_OUT_ID_SUFFIX: "_pulledOut",
  _init: function(popupId, useDisplayNoneByDefault) {
    var popup = O$.initComponent(popupId, null, {
      _visibilityChangeListeners: null,

      _addVisibilityChangeListener: function(listenerObjectFunction) {
        if (!this._visibilityChangeListeners) {
          this._visibilityChangeListeners = [];
        }
        this._visibilityChangeListeners.push(listenerObjectFunction);
      },

      getLeft: function() {
        return O$.getElementPos(popup, true).x;
      },

      getTop: function() {
        return O$.getElementPos(popup, true).y;
      },

      setLeft: function (left) {
        popup.style.left = left + "px";
        if (popup._ieTransparencyControl)
          popup._ieTransparencyControl.style.left = left + "px";
      },

      setTop: function (top) {
        popup.style.top = top + "px";
        if (popup._ieTransparencyControl)
          popup._ieTransparencyControl.style.top = top + "px";
      },

      _prepareForRearrangementBeforeShowing: function() {
        if (this.isVisible())
          return;
        var style = O$.getElementStyle(this, ["display"]);
        if (style.display == "none")
          this.style.display = this._originalStyleDisplay;
      },

      showAtXY: function(x, y) {
        popup.style.left = x + "px";
        popup.style.top = y + "px";
        this.show();
      },

      _showByElement: function(element, horizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, disableRepositioning) {
        this.show();
        O$.alignPopupByElement(this, element, horizAlignment, vertAlignment, horizDistance, vertDistance, ignoreVisibleArea, disableRepositioning);
      },


      show: function() {
        if (this.isVisible())
          return;

        setTimeout(function() {
          O$.addIETransparencyControl(popup);
        }, 1);
        this.style.visibility = "visible";
        var style = O$.getElementStyle(this, ["display"]);
        if (style.display == "none")
          this.style.display = this._originalStyleDisplay;
        O$.Popup._notifyVisibilityChangeListeners(this);
      },

      hide: function() {
        if (!this.isVisible())
          return;

        O$.removeIETransparencyControl(popup);
        this.style.visibility = "hidden";
        if (O$.isMozillaFF() || O$.isSafari3AndLate() /*todo:check whether O$.isSafari3AndLate check is really needed (it was added by mistake)*/) {
          var oldOverflow = O$.getElementStyle(this, "overflow");
          this.style.overflow = "hide"; // to avoid regaining focus when tabbing out from an opened DropDownField under Mozilla
          setTimeout(function() { // time-out is needed for Mozilla when selecting an item in DropDownField (JSFC-2620)
            popup.style.display = "none";
            popup.style.overflow = oldOverflow;
          }, 1);
        } else {
          this.style.display = "none";
        }
        O$.Popup._notifyVisibilityChangeListeners(this);
      },

      isVisible: function() {
        var style = O$.getElementStyle(this, ["visibility", "display"]);
        var visible = (style.visibility != "hidden") && (style.display != "none");
        return visible;
      },

      _pullFromContainer: function() {
        if (this._pulledOut) return;
        this._pulledOut = true;
        var newParent = O$.getDefaultAbsolutePositionParent();
        var newId = this.id + O$.Popup.PULLED_OUT_ID_SUFFIX;
        var oldPopup = O$(newId);
        if (oldPopup)
          oldPopup.parentNode.removeChild(oldPopup);
        this.id = newId;
        newParent.appendChild(this);
      }
    });

    // there can't be inline pop-ups so we're assuming the regular display value for popups to be the "block" one
    popup._originalStyleDisplay = "block";//O$.getElementStyle(this, "display");//this.style.display;

    O$.initIETransparencyWorkaround(popup);

    if (!O$._popupsOnPage) {
      O$._popupsOnPage = [];
    }
    O$._popupsOnPage.push(popupId);
    if (useDisplayNoneByDefault) {
      if (popup._originalStyleDisplay == undefined)
        popup._originalStyleDisplay = popup.style.display;
      popup.style.display = "none";
    }
    O$.initUnloadableComponent(popup);
    O$.addUnloadHandler(popup, function () {
      for (var index = 0; index < O$._popupsOnPage.length; index++) {
        if (O$(O$._popupsOnPage[index]) == popup) {
          O$._popupsOnPage.splice(index, 1);
        }
      }
    });

  },

  _notifyVisibilityChangeListeners: function(popup) {
    var listeners = popup._visibilityChangeListeners;
    if (listeners && listeners.length > 0) {
      for (var i = 0; i < listeners.length; i ++) {
        listeners[i](popup.isVisible());
      }
    }
  },

  _hideAllPopupsExceptOne: function(popupToRetain) {
    if (!O$._popupsOnPage)
      return;
    O$._popupsOnPage.forEach(function(popupId) {
      var currPopup = O$(popupId) || O$(popupId + O$.Popup.PULLED_OUT_ID_SUFFIX);
      if (!currPopup)
        return; // popup can be removed from page with A4J
      if (currPopup != popupToRetain)
        currPopup.hide();
    });
  }

};

document._addClickListener(function(e) {
  var evt = O$.getEvent(e);
  if (!evt || !O$._popupsOnPage) return;

  var clickedElement;
  if (evt.target) {
    clickedElement = evt.target;
  } else {
    clickedElement = evt.srcElement;
  }

  var clickedElementId = clickedElement.id;

  O$._popupsOnPage.forEach(function(popupId) {
    var popup = O$(popupId) || O$(popupId + O$.Popup.PULLED_OUT_ID_SUFFIX);
    if ((!popup) || (popup == {}) || !(popup.hide))
      return; // popup can be removed from page with A4J
    var clickedOnChild = O$.isChild(popup, clickedElement);
    if (popupId == clickedElementId || clickedOnChild || clickedElementId == popup._dependedFieldId)
      return;
    popup.hide();
  });
});

O$.addEventHandler(document, "keydown", function(e) {
  var evt = O$.getEvent(e);
  if (evt.keyCode != 27 || !O$._popupsOnPage) return;

  O$._popupsOnPage.forEach(function(popupId) {
    var popup = O$(popupId) || O$(popupId + O$.Popup.PULLED_OUT_ID_SUFFIX);
    if (!popup)
      return; // popup can be removed from page with A4J
    if (popup.blur)
      popup.blur();
    popup.hide();
  });

});
