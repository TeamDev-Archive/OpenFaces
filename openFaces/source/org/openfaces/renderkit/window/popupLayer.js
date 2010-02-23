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

O$.PopupLayer = {
  FIRST_EXTERNAL_ANCHOR_SUFFIX: "::firstExternalAnchor",
  LAST_EXTERNAL_ANCHOR_SUFFIX: "::lastExternalAnchor",


  _init: function(id, left, top, width, height, rolloverStyle, hidingTimeout, draggable, hideOnEsc, isAjaxRequest) {
    var popup = O$(id);
    O$.initComponent(id, {rollover: rolloverStyle}, {
      left: left,
      top: top,
      _hidingTimeout: hidingTimeout,

      _visibleField: O$(id + "::visible"),
      _leftField: O$(id + "::left"),
      _topField: O$(id + "::top"),
      _hideOnEsc: hideOnEsc,

      blockingLayer: O$(id + "::blockingLayer"),

      _getDefaultFocusComponent: function() {
        return O$.getFirstFocusableControl(popup);
      },

      _draggable: draggable,
      _draggingDisabled: false,

      _rolloverStyleNames: rolloverStyle,

      // --------------- Popup functions start

      getLeft: function() {
        return O$.getElementPos(popup, true).x;
      },

      getTop: function() {
        return O$.getElementPos(popup, true).y;
      },

      setLeft: function (left) {
        if (left == null || left == undefined || isNaN(left))
          throw "popupLayer.setLeft: the integer number should be passed as a parameter, but the following value was passed: " + left;
        popup.style.left = left + "px";
        if (popup._ieTransparencyControl)
          popup._ieTransparencyControl.style.left = left + "px";

        popup._leftField.value = left;
        popup.left = left;
        if (popup._positionChanged)
          popup._positionChanged();
      },

      setTop: function (top) {
        if (top == null || top == undefined || isNaN(top))
          throw "poupLayer.setTop: the integer number should be passed as a parameter, but the following value was passed: " + top;
        popup.style.top = top + "px";
        if (popup._ieTransparencyControl)
          popup._ieTransparencyControl.style.top = top + "px";

        popup._topField.value = top;
        popup.top = top;
        if (popup._positionChanged)
          popup._positionChanged();
      },

      showCentered: function() {
        this.show();
        O$.PopupLayer._centerPopup(this, O$.getPageScrollPos());
      },

      showByElement: function(element, horizAlignment, vertAlignment, horizDistance, vertDistance) {
        this.style.visibility = "hidden";
        this.show();
        setTimeout(function() {
          O$.alignPopupByElement(popup, element, horizAlignment, vertAlignment, horizDistance, vertDistance);
          popup.style.visibility = "";
        }, 1);
      },

      show: function () { // todo: this partially duplicates popup.show declared in popup.js, merge as much functionality in popupLayer.js and popup.js as possible
        if (popup.isVisible()) return;
        popup.style.display = "block";
        O$.addIETransparencyControl(popup);
        if (popup.blockingLayer) {
          var body = document.getElementsByTagName("body")[0];
          var firstExternalAnchor = null;
          var firstExternalAnchorOld = O$(popup.id + O$.PopupLayer.FIRST_EXTERNAL_ANCHOR_SUFFIX);

          if (!firstExternalAnchorOld) {
            firstExternalAnchor = O$.createHiddenFocusElement(popup.id);
          }
          else {
            firstExternalAnchor = firstExternalAnchorOld;
          }
          firstExternalAnchor.id = popup.id + O$.PopupLayer.FIRST_EXTERNAL_ANCHOR_SUFFIX;
          firstExternalAnchor.onfocus = function() {
            var focusable = popup._getDefaultFocusComponent();
            if (focusable)
              focusable.focus();
            else
              firstExternalAnchor.blur();
          };

          var firstDocEl = body.firstChild;
          body.insertBefore(firstExternalAnchor, firstDocEl);

          var lastExternalAnchor = null;
          var lastExternalAnchorOld = O$(popup.id + O$.PopupLayer.LAST_EXTERNAL_ANCHOR_SUFFIX);
          if (!lastExternalAnchorOld) {
            lastExternalAnchor = O$.createHiddenFocusElement(popup.id);
          }
          else {
            lastExternalAnchor = lastExternalAnchorOld;
          }
          lastExternalAnchor.id = popup.id + O$.PopupLayer.LAST_EXTERNAL_ANCHOR_SUFFIX;
          lastExternalAnchor.onfocus = function() {
            var focusable = popup._getDefaultFocusComponent();
            if (focusable)
              focusable.focus();
            else
              lastExternalAnchor.blur();
          };
          body.appendChild(lastExternalAnchor);

          popup._firstInternalAnchor = O$.createHiddenFocusElement(popup.id);
          popup._firstInternalAnchor._focusControlElement = true;
          popup.insertBefore(popup._firstInternalAnchor, popup.firstChild);
          popup._firstInternalAnchor.onfocus = function() {
            var focusable = O$.getLastFocusableControl(popup);
            if (focusable)
              focusable.focus();
            else
              this.blur();
          };
          popup._firstInternalAnchor.focus();

          popup._lastInternalAnchor = O$.createHiddenFocusElement(popup.id);
          popup._lastInternalAnchor._focusControlElement = true;
          popup._lastInternalAnchor.onfocus = function() {
            var focusable = O$.getFirstFocusableControl(popup);
            if (focusable)
              focusable.focus();
            else
              this.blur();
          };
          popup.appendChild(popup._lastInternalAnchor);


          popup.blockingLayer.style.display = "";

          document._of_activeModalLayer = popup.blockingLayer;
          O$.PopupLayer._resizeModalLayer();
          if (O$._simulateFixedPosForBlockingLayer()) {
            O$.addEventHandler(window, "resize", O$.PopupLayer._resizeModalLayer);
            O$.addEventHandler(window, "scroll", O$.PopupLayer._alignModalLayer);
            O$.PopupLayer._alignModalLayer();
          } else {
            popup.blockingLayer.style.left = "0px";
            popup.blockingLayer.style.top = "0px";
            popup.blockingLayer.style.position = "fixed";
            window.addEventListener("resize", O$.PopupLayer._resizeModalLayer, true);
          }

          setTimeout(function() {
            var focusable = popup._getDefaultFocusComponent();
            if (focusable)
              focusable.focus();
          }, 1);

          if (!O$.PopupLayer._modalWindows)
            O$.PopupLayer._modalWindows = [];
          O$.PopupLayer._modalWindows.push(popup);
        }

        if (popup.anchorElement != undefined) {
          O$.PopupLayer._moveToAnchor(popup);
        }

        popup._visibleField.value = "true";
        if (popup.onshow) {
          popup.onshow();
        }
        if (popup.hideTimer) {
          clearTimeout(popup.hideTimer);
        }

        if (popup._hidingTimeout && popup._hidingTimeout > 0) {
          popup.hideTimer = setTimeout(function() {
            popup.hide();
          }, popup._hidingTimeout);
        }
        O$.repaintAreaForOpera(popup, true);
        if (popup._afterShow)
          popup._afterShow();
      },

      hide: function () {
        if (!popup.isVisible()) return;
        O$.removeIETransparencyControl(popup);
        if (popup.blockingLayer) {
          var body = document.getElementsByTagName("body")[0];
          var firstExtAnc = O$(popup.id + O$.PopupLayer.FIRST_EXTERNAL_ANCHOR_SUFFIX);
          if (firstExtAnc) {
            body.removeChild(firstExtAnc);
          }
          var lastExtAnc = O$(popup.id + O$.PopupLayer.LAST_EXTERNAL_ANCHOR_SUFFIX);
          if (lastExtAnc) {
            body.removeChild(lastExtAnc);
          }
          if (popup._firstInternalAnchor) {
            popup.removeChild(popup._firstInternalAnchor);
          }
          if (popup._lastInternalAnchor) {
            popup.removeChild(popup._lastInternalAnchor);
          }
        }

        popup.style.display = "none";
        if (popup.onhide) {
          popup.onhide();
        }
        if (popup.hideTimer) {
          clearTimeout(popup.hideTimer);
        }
        popup._visibleField.value = "false";

        if (popup.blockingLayer) {
          popup.blockingLayer.style.display = "none";
          if (O$._simulateFixedPosForBlockingLayer()) {
            O$.removeEventHandler(window, "scroll", O$.PopupLayer._alignModalLayer);
            O$.removeEventHandler(window, "resize", O$.PopupLayer._resizeModalLayer);
          } else {
            document.removeEventListener("resize", O$.PopupLayer._resizeModalLayer, true);
          }
          if (O$.isOpera()) {
            document.body.style.visibility = "hidden";
            document.body.style.visibility = "visible";
          }

          var p = O$.PopupLayer._modalWindows.pop();
          if (p != popup)
            O$.logError("popup.hide: modal window stack failure: " + p);
        }
        O$.repaintAreaForOpera(document.body, true);
        if (popup._afterHide)
          popup._afterHide();
      },

      isVisible: function () {
        return popup.style.display != "none";
      },

      showAtXY: function (x, y) {
        popup.setLeft(x);
        popup.setTop(y);
        popup.show();
      },

      getAnchorElement: function () {
        return popup.anchorElement;
      },

      attachToElement: function (elt, anchorX, anchorY) {
        popup.anchorElement = elt;
        popup.anchorX = anchorX;
        popup.anchorY = anchorY;

        O$.PopupLayer._moveToAnchor(popup);
      },

      _onmousedown: popup.onmousedown,
      onmousedown: function (e) {
        if (popup._onmousedown) {
          popup._onmousedown(e);
        }
        if (popup._draggable && !popup._draggingDisabled) {
          var evt = O$.getEvent(e);
          var pos = O$.getEventPoint(evt, popup);
          if (pos.x < popup.offsetLeft + popup.clientWidth
                  && pos.y < popup.offsetTop + popup.clientHeight) {
            O$.startDragging(e, popup);
          }
        }
      },

      _onmousemove: popup.onmousemove,
      onmousemove: function (e) {
        if (popup._onmousemove) {
          if (!popup._draggingInProgress) {
            popup._onmousemove(e);
          }
        }
      }
    });

    popup.style.display = O$.getElementStyle(popup, "display");

    if (left != null)
      try {
        popup.setLeft(left);
      } catch (e) {
        O$.logError("Invalid value of the 'left' attribute of PopupLayer: \"" + left + "\" ; it must be an integer value ; original error: " + e.message);
        throw e;
      }

    if (top != null)
      try {
        popup.setTop(top);
      } catch (e) {
        O$.logError("Invalid value of the 'top' attribute of PopupLayer: \"" + top + "\" ; it must be an integer value ; original error: " + e.message);
        throw e;
      }

    if (width != null)
      try {
        popup.style.width = width;
      } catch (e) {
        O$.logError("Invalid value of the 'width' attribute of PopupLayer: \"" + width + "\" ; it must be a valid CSS declaration like \"100px\" ; original error: " + e.message);
        throw e;
      }

    if (height != null)
      try {
        popup.style.height = height;
      } catch (e) {
        O$.logError("Invalid value of the 'height' attribute of PopupLayer: \"" + height + "\" ; it must be a valid CSS declaration like \"100px\" ; original error: " + e.message);
        throw e;
      }

    O$.initIETransparencyWorkaround(popup);
    if (popup.blockingLayer)
      popup.blockingLayer.onclick = function() {
        var focusable = popup._getDefaultFocusComponent();
        if (focusable)
          focusable.focus();
      };

    O$.addLoadEvent(function () {
      if (popup._visibleField.value == "true")
        popup.show();
    }, isAjaxRequest);

  },


  _moveToAnchor: function(popup) {
    var elt = popup.anchorElement;
    var anchorX = popup.anchorX;
    var anchorY = popup.anchorY;

    if (anchorX == undefined || anchorX == null) {
      anchorX = "left";
    }
    if (anchorY == undefined || anchorY == null) {
      anchorY = "top";
    }

    var eltRect = O$.getElementBorderRectangle(elt);
    if (anchorX == "right") {
      popup.setLeft(eltRect.getMaxX());
    } else if (anchorX == "center") {
      popup.setLeft(eltRect.getMinX() + eltRect.width / 2);
    } else { // left
      popup.setLeft = eltRect.getMinX();
    }

    if (anchorY == "bottom") {
      popup.setTop(eltRect.getMaxY());
    } else if (anchorY == "middle") {
      popup.setTop(eltRect.getMinY() + eltRect.height / 2);
    } else { // top
      popup.setTop(eltRect.getMinY());
    }
  },

  _alignModalLayer: function() {
    var modalLayer = document._of_activeModalLayer;
    var scrollPos = O$.getPageScrollPos();
    var prnt = modalLayer.offsetParent;
    var parentPos = prnt != null && prnt.nodeName != "HTML" && prnt.nodeName != "BODY"
            ? O$.getElementPos(prnt)
            : {x: 0, y: 0};
    var parentLeft = parentPos.x;
    var parentTop = parentPos.y;

    modalLayer.style.left = (scrollPos.x - parentLeft) + "px";
    modalLayer.style.top = (scrollPos.y - parentTop) + "px";
    O$.PopupLayer._resizeModalLayer();
  },

  _resizeModalLayer: function() {
    var modalLayer = document._of_activeModalLayer;

    var visibleAreaSize = O$.getVisibleAreaRectangle();

    modalLayer.style.width = visibleAreaSize.width + "px";
    modalLayer.style.height = visibleAreaSize.height + "px";
  },

  _centerPopup: function(popup, oldScrollPos) {
    var visibleAreaSize = O$.getVisibleAreaSize();
    var parentToCalculateScrollOffset = popup.offsetParent;
    if (!parentToCalculateScrollOffset) {
      parentToCalculateScrollOffset = document.body;
    }
    var prntPos = O$.getElementPos(parentToCalculateScrollOffset);
    var x = visibleAreaSize.width / 2 - popup.offsetWidth / 2 - prntPos.x + oldScrollPos.x;
    var y = visibleAreaSize.height / 2 - popup.offsetHeight / 2 - prntPos.y + oldScrollPos.y;
    popup.setLeft(x);
    popup.setTop(y);
  }


};

O$.addEventHandler(document, "keydown", function(e) {
  var evt = O$.getEvent(e);
  if (evt.keyCode == 27) {
    if (O$.PopupLayer._modalWindows && O$.PopupLayer._modalWindows.length > 0) {
      var currentModalWindow = O$.PopupLayer._modalWindows[O$.PopupLayer._modalWindows.length - 1];
      if (currentModalWindow._hideOnEsc)
        currentModalWindow.hide();
    }
  }
});
