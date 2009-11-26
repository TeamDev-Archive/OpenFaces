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

O$.PopupLayer = {
  FIRST_EXTERNAL_ANCHOR_SUFFIX: "::firstExternalAnchor",
  LAST_EXTERNAL_ANCHOR_SUFFIX: "::lastExternalAnchor",


  _init: function(id, left, top, width, height, rolloverStyle, hidingTimeout, draggable, hideOnEsc, isAjaxRequest) {
    var popup = O$(id);

    O$.initIETransparencyWorkaround(popup);

    popup._visibleField = O$(popup.id + "::visible");
    popup._leftField = O$(popup.id + "::left");
    popup._topField = O$(popup.id + "::top");
    popup._hideOnEsc = hideOnEsc;
    popup.style.display = O$.getElementStyle(popup, "display");

    popup.blockingLayer = O$(popup.id + "::blockingLayer");
    if (popup.blockingLayer)
      popup.blockingLayer.onclick = function() {
        var focusable = popup._getDefaultFocusComponent();
        if (focusable)
          focusable.focus();
      };

    popup._getDefaultFocusComponent = function() {
      return O$.PopupLayer.getFirstFocusableControl(popup);
    };


    //  popup.anchorField = O$(popup.id + "_anchor");

    popup._draggable = draggable;
    popup._draggingDisabled = false;

    popup._rolloverStyleNames = rolloverStyle;

    popup._onmousedown = popup.onmousedown;
    popup._onmouseup = popup.onmouseup;
    popup._onmousemove = popup.onmousemove;
    popup._onmouseover = popup.onmouseover;
    popup._onmouseout = popup.onmouseout;

    // --------------- Popup functions start
    popup.setLeft = function (left) {
      if (left == null || left == undefined || isNaN(left))
        throw "popupLayer.setLeft: the integer number should be passed as a parameter, but the following value was passed: " + left;
      popup.style.left = left + "px";
      if (popup._ieTransparencyControl)
        popup._ieTransparencyControl.style.left = left + "px";

      popup._leftField.value = left;
      popup.left = left;
      if (popup._positionChanged)
        popup._positionChanged();
    };

    popup.setTop = function (top) {
      if (top == null || top == undefined || isNaN(top))
        throw "poupLayer.setTop: the integer number should be passed as a parameter, but the following value was passed: " + top;
      popup.style.top = top + "px";
      if (popup._ieTransparencyControl)
        popup._ieTransparencyControl.style.top = top + "px";

      popup._topField.value = top;
      popup.top = top;
      if (popup._positionChanged)
        popup._positionChanged();
    };

    popup.showCentered = function() {
      this.show();
      O$.PopupLayer._centerPopup(this, O$.getPageScrollPos());
    };

    popup.showByElement = function(element, horizAlignment, vertAlignment, horizDistance, vertDistance) {
      this.style.visibility = "hidden";
      this.show();
      setTimeout(function() {
        O$.alignPopupByElement(popup, element, horizAlignment, vertAlignment, horizDistance, vertDistance);
        popup.style.visibility = "";
      }, 1);
    };

    popup.show = function () { // todo: this overrides popup.show declared in popup.js, merge as much functionality in popupLayer.js and popup.js as possible
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
          var focusable = O$.PopupLayer.getLastFocusableControl(popup);
          if (focusable)
            focusable.focus();
          else
            this.blur();
        };
        popup._firstInternalAnchor.focus();

        popup._lastInternalAnchor = O$.createHiddenFocusElement(popup.id);
        popup._lastInternalAnchor._focusControlElement = true;
        popup._lastInternalAnchor.onfocus = function() {
          var focusable = O$.PopupLayer.getFirstFocusableControl(popup);
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
      //    O$.hideControlsUnderPopup(this);

      popup._visibleField.value = "true";
      if (popup.onshow) {
        popup.onshow();
      }
      if (popup.hideTimer) {
        clearTimeout(popup.hideTimer);
      }

      /*
       if (O$.isExplorer()) {
       popup.setLeft(popup.startX);
       popup.setTop(popup.startY);
       }
       */

      if (popup._hidingTimeout && popup._hidingTimeout > 0) {
        popup.hideTimer = setTimeout(function() {
          popup.hide();
        }, popup._hidingTimeout);
      }
      O$.repaintAreaForOpera(popup, true);
      if (popup._afterShow)
        popup._afterShow();
    };

    popup.hide = function () {
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

      //    O$.unhideControlsUnderPopup(this);
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
    };

    popup.isVisible = function () {
      return popup.style.display != "none";
    };

    if (rolloverStyle != null) {
      popup.onmouseover = function (e) {
        if (popup._onmouseover) {
          popup._onmouseover(e);
        }
        O$.appendClassNames(popup, [popup._rolloverStyleNames]);
        O$.repaintAreaForOpera(popup, true);
      };

      popup.onmouseout = function (e) {
        if (popup._onmouseout) {
          popup._onmouseout(e);
        }
        O$.excludeClassNames(popup, [popup._rolloverStyleNames]);
        O$.repaintAreaForOpera(popup, true);
      };
    } else {
      popup.onmouseover = function (e) {
        if (popup._onmouseover) {
          popup._onmouseover(e);
        }
      };

      popup.onmouseout = function (e) {
        if (popup._onmouseout) {
          popup._onmouseout(e);
        }
      };
    }

    popup.showAtXY = function (x, y) {
      popup.setLeft(x);
      popup.setTop(y);
      popup.show();
    };

    popup.getAnchorElement = function () {
      return popup.anchorElement;
    };
    popup.attachToElement = function (elt, anchorX, anchorY) {
      popup.anchorElement = elt;
      popup.anchorX = anchorX;
      popup.anchorY = anchorY;

      O$.PopupLayer._moveToAnchor(popup);

      //    if (elt.id) {
      //      popup.anchorField.value = elt.id;
      //    }
    };

    popup.onmousedown = function (e) {
      if (popup._onmousedown) {
        popup._onmousedown(e);
      }
      if (popup._draggable && !popup._draggingDisabled) {
        var evt = O$.getEvent(e);
        var pos = O$.getEventPoint(evt, popup);
        if (pos.x < popup.offsetLeft + popup.clientWidth
                && pos.y < popup.offsetTop + popup.clientHeight) {
          O$.startDragAndDrop(e, popup);
        }
      }
    };
    popup.onmouseup = function (e) {
      if (popup._onmouseup) {
        popup._onmouseup(e);
      }
    };
    popup.onmousemove = function (e) {
      if (popup._onmousemove) {
        if (!popup._draggingInProgress) {
          popup._onmousemove(e);
        }
      }
    };

    // --------------- Popup functions end

    popup.left = left;
    if (left != null)
      try {
        popup.setLeft(left);
      } catch (e) {
        O$.logError("Invalid value of the 'left' attribute of PopupLayer: \"" + left + "\" ; it must be an integer value ; original error: " + e.message);
        throw e;
      }

    popup.top = top;
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

    popup._hidingTimeout = hidingTimeout;

    O$.addLoadEvent(function () {
      if (popup._visibleField.value == "true")
        popup.show();
    }, isAjaxRequest);

    //  if(popup.anchorField.value && popup.anchorField.value != ""){
    //    var elt = O$(popup.anchorField.value);
    //    if(elt){
    //      popup.attachToElement(elt, "center", "middle");
    //    }
    //  }
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
            : {left: 0, top: 0};
    var parentLeft = parentPos.left;
    var parentTop = parentPos.top;

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
    var x = visibleAreaSize.width / 2 - popup.offsetWidth / 2 - prntPos.left + oldScrollPos.x;
    var y = visibleAreaSize.height / 2 - popup.offsetHeight / 2 - prntPos.top + oldScrollPos.y;
    popup.setLeft(x);
    popup.setTop(y);
  },

  getFirstFocusableControl: function(parent) {
    for (var i = 0, count = parent.childNodes.length; i < count; i++) {
      var child = parent.childNodes[i];
      if (!child._focusControlElement && O$.isControlFocusable(child))
        return child;
      var focusable = O$.PopupLayer.getFirstFocusableControl(child);
      if (focusable)
        return focusable;
    }
    return null;
  },

  getLastFocusableControl: function(parent) {
    for (var i = parent.childNodes.length - 1; i >= 0; i--) {
      var child = parent.childNodes[i];
      if (!child._focusControlElement && O$.isControlFocusable(child))
        return child;
      var focusable = O$.PopupLayer.getLastFocusableControl(child);
      if (focusable)
        return focusable;
    }
    return null;
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
