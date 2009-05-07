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

O$.POPUP_FIRST_EXTERNAL_ANCHOR_SUFFIX = "::firstExternalAnchor";
O$.POPUP_LAST_EXTERNAL_ANCHOR_SUFFIX = "::lastExternalAnchor";
O$.POPUP_PRE_FIRST_INTERNAL_ANCHOR_SUFFIX = "::preFirstInternalFocusableAnchor";
O$.POPUP_FIRST_INTERNAL_ANCHOR_SUFFIX = "::firstInternalFocusableAnchor";
O$.POPUP_LAST_INTERNAL_ANCHOR_SUFFIX = "::lastInternalFocusableAnchor";

O$._initPopupLayer = function(id, left, top, width, height, rolloverStyle, hidingTimeout, draggable, isAjaxRequest) {
  var popup = O$(id);

  O$.initIETransparencyWorkaround(popup);

  popup._visibleField = O$(popup.id + "::visible");
  popup._leftField = O$(popup.id + "::left");
  popup._topField = O$(popup.id + "::top");
  popup.style.display = O$.getElementStyleProperty(popup, "display");

  popup.blockingLayer = O$(popup.id + "::blockingLayer");

  //  if (popup.blockingLayer) {
  //      // This is needed for situations when blocking layer with style="filter:alpha(opacity: 50)" is positioned inside
  //    // <td> element of <table>. Under IE quirks mode this situation will cause the filter not to be applied. So we
  //    // need to move blocking layer outside of <table> tag.
  //    O$.addLoadEvent(function() {
  //      var newParent = O$.getDefaultAbsolutePositionParent();
  //      newParent.appendChild(popup.blockingLayer);
  //    });
  //  }


  //  popup.anchorField = O$(popup.id + "_anchor");

  popup._draggable = draggable;

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
  }

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
  }

  popup.showCentered = function() {
    this.show();
    O$._centerPopup(this, O$.getPageScrollPos());
  }

  popup.showByElement = function(element, horizAlignment, vertAlignment, horizDistance, vertDistance) {
    this.style.visibility = "hidden";
    this.show();
    setTimeout(function() {
      O$.alignPopupByElement(popup, element, horizAlignment, vertAlignment, horizDistance, vertDistance);
      popup.style.visibility = "";
    }, 1);
  }

  popup.show = function () { // todo: this overrides popup.show declared in popup.js, merge as much functionality in popupLayer.js and popup.js as possible
    if (popup.isVisible()) return;
    popup.style.display = "block";
    O$.addIETransparencyControl(popup);
    if (popup.blockingLayer) {
      var body = document.getElementsByTagName("body")[0];
      var firstExternalAnchor = null;
      var firstExternalAnchorOld = O$(popup.id + O$.POPUP_FIRST_EXTERNAL_ANCHOR_SUFFIX);

      if (!firstExternalAnchorOld) {
        firstExternalAnchor = O$.createHiddenFocusElement();
      }
      else {
        firstExternalAnchor = firstExternalAnchorOld;
      }
      firstExternalAnchor.id = popup.id + O$.POPUP_FIRST_EXTERNAL_ANCHOR_SUFFIX;
      firstExternalAnchor.onfocus = function() {
        var el = O$(popup.id + O$.POPUP_FIRST_INTERNAL_ANCHOR_SUFFIX);
        el.focus();
      };

      var firstDocEl = body.firstChild;
      body.insertBefore(firstExternalAnchor, firstDocEl);

      var lastExternalAnchor = null;
      var lastExternalAnchorOld = O$(popup.id + O$.POPUP_LAST_EXTERNAL_ANCHOR_SUFFIX);
      if (!lastExternalAnchorOld) {
        lastExternalAnchor = O$.createHiddenFocusElement();
      }
      else {
        lastExternalAnchor = lastExternalAnchorOld;
      }
      lastExternalAnchor.id = popup.id + O$.POPUP_LAST_EXTERNAL_ANCHOR_SUFFIX;
      lastExternalAnchor.onfocus = function() {
        var el = O$(popup.id + O$.POPUP_FIRST_INTERNAL_ANCHOR_SUFFIX);
        el.focus();
      };
      body.appendChild(lastExternalAnchor);

      var firstInternalAnchor = O$.createHiddenFocusElement();
      firstInternalAnchor.id = popup.id + O$.POPUP_FIRST_INTERNAL_ANCHOR_SUFFIX;
      var firstPopupEl = popup.firstChild;
      popup.insertBefore(firstInternalAnchor, firstPopupEl);
      var preFirstInternalAnchor = O$.createHiddenFocusElement();
      preFirstInternalAnchor.id = popup.id + O$.POPUP_PRE_FIRST_INTERNAL_ANCHOR_SUFFIX;
      preFirstInternalAnchor.onfocus = function() {
        var el = O$(popup.id + O$.POPUP_FIRST_INTERNAL_ANCHOR_SUFFIX);
        el.focus();
      };
      popup.insertBefore(preFirstInternalAnchor, firstInternalAnchor);
      firstInternalAnchor.focus();

      var lastInternalAnchor = O$.createHiddenFocusElement();
      lastInternalAnchor.id = popup.id + O$.POPUP_LAST_INTERNAL_ANCHOR_SUFFIX;
      lastInternalAnchor.onfocus = function () {
        var el = O$(popup.id + O$.POPUP_FIRST_INTERNAL_ANCHOR_SUFFIX);
        el.focus();
      };
      popup.appendChild(lastInternalAnchor);
    }

    if (popup.anchorElement != undefined) {
      O$._popup_moveToAnchor(popup);
    }
    //    O$.hideControlsUnderPopup(this);

    if (popup.blockingLayer) { //modal popup
      //todo: rework blocking layer creation to make <body> its parent (to prevent other controls in table cell, when this cell is big, moving up or down)
      //todo: jsfc-1497
      popup.blockingLayer.style.display = "";

      document._of_activeModalLayer = popup.blockingLayer;
      O$._popupLayer_resizeModalLayer();
      if (O$._simulateFixedPosForBlockingLayer()) {
        var prnt = popup.offsetParent;
        if (prnt) {
          prnt = prnt.offsetParent;
        }
        var prntPos = prnt != null ? O$.getElementPos(prnt) : {left: 0, top: 0};
        var parentLeft = prntPos.left;
        var parentTop = prntPos.top;
        popup.blockingLayer.style.left = (document.body.scrollLeft - parentLeft) + "px";
        popup.blockingLayer.style.top = (document.body.scrollTop - parentTop) + "px";
        O$.addEventHandler(window, "resize", O$._popupLayer_resizeModalLayer);
        O$.addEventHandler(window, "scroll", O$._popupLayer_alignModalLayer);
        O$._popupLayer_alignModalLayer();
      } else {
        popup.blockingLayer.style.left = "0px";
        popup.blockingLayer.style.top = "0px";
        popup.blockingLayer.style.position = "fixed";
        window.addEventListener("resize", O$._popupLayer_resizeModalLayer, true);
      }

    }
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
        popup.hide()
      }, popup._hidingTimeout);
    }
    O$.repaintAreaForOpera(popup, true);
    if (popup._afterShow)
      popup._afterShow();
  }

  popup.hide = function () {
    if (!popup.isVisible()) return;
    O$.removeIETransparencyControl(popup);
    if (popup.blockingLayer) {
      var body = document.getElementsByTagName("body")[0];
      var firstExtAnc = O$(popup.id + O$.POPUP_FIRST_EXTERNAL_ANCHOR_SUFFIX);
      if (firstExtAnc) {
        body.removeChild(firstExtAnc);
      }
      var lastExtAnc = O$(popup.id + O$.POPUP_LAST_EXTERNAL_ANCHOR_SUFFIX);
      if (lastExtAnc) {
        body.removeChild(lastExtAnc);
      }
      var el = O$(popup.id + O$.POPUP_FIRST_INTERNAL_ANCHOR_SUFFIX);
      if (el) {
        popup.removeChild(el);
      }
      el = O$(popup.id + O$.POPUP_LAST_INTERNAL_ANCHOR_SUFFIX);
      if (el) {
        popup.removeChild(el);
      }
      el = O$(popup.id + O$.POPUP_PRE_FIRST_INTERNAL_ANCHOR_SUFFIX);
      if (el) {
        popup.removeChild(el);
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
        O$.removeEventHandler(window, "scroll", O$._popupLayer_alignModalLayer);
        O$.removeEventHandler(window, "resize", O$._popupLayer_resizeModalLayer);
      } else {
        document.removeEventListener("resize", O$._popupLayer_resizeModalLayer, true);
      }
      if (O$.isOpera()) {
        document.body.style.visibility = "hidden";
        document.body.style.visibility = "visible";
      }
    }
    O$.repaintAreaForOpera(document.body, true);
    if (popup._afterHide)
      popup._afterHide();
  }

  popup.isVisible = function () {
    return popup.style.display != "none";
  }

  if (rolloverStyle != null) {
    popup.onmouseover = function (e) {
      if (popup._onmouseover) {
        popup._onmouseover(e);
      }
      O$.appendClassNames(popup, [popup._rolloverStyleNames]);
      O$.repaintAreaForOpera(popup, true);
    }

    popup.onmouseout = function (e) {
      if (popup._onmouseout) {
        popup._onmouseout(e);
      }
      O$.excludeClassNames(popup, [popup._rolloverStyleNames]);
      O$.repaintAreaForOpera(popup, true);
    }
  } else {
    popup.onmouseover = function (e) {
      if (popup._onmouseover) {
        popup._onmouseover(e);
      }
    }

    popup.onmouseout = function (e) {
      if (popup._onmouseout) {
        popup._onmouseout(e);
      }
    }
  }

  popup.showAtXY = function (x, y) {
    popup.setLeft(x);
    popup.setTop(y);
    popup.show();
  }

  popup.getAnchorElement = function () {
    return popup.anchorElement;
  }
  popup.attachToElement = function (elt, anchorX, anchorY) {
    popup.anchorElement = elt;
    popup.anchorX = anchorX;
    popup.anchorY = anchorY;

    O$._popup_moveToAnchor(popup);

    //    if (elt.id) {
    //      popup.anchorField.value = elt.id;
    //    }
  }

  popup.onmousedown = function (e) {
    if (popup._onmousedown) {
      popup._onmousedown(e);
    }
    if (popup._draggable) {
      var evt = O$.getEvent(e);
      var pos = O$.getEventPoint(evt, popup)
      if (pos.x < popup.offsetLeft + popup.clientWidth
              && pos.y < popup.offsetTop + popup.clientHeight) {
        O$.startDragAndDrop(e, popup);
      }
    }
  }
  popup.onmouseup = function (e) {
    if (popup._onmouseup) {
      popup._onmouseup(e);
    }
  }
  popup.onmousemove = function (e) {
    if (popup._onmousemove) {
      if (!popup._draggingInProgress) {
        popup._onmousemove(e);
      }
    }
  }

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
}



// -- Standalone popup functions

O$._popup_moveToAnchor = function(popup) {
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
}

O$._getLeftBodyMargin = function() {
  return O$.getNumericStyleProperty(document.body, "margin-left");
}

O$._getTopBodyMargin = function() {
  return O$.getNumericStyleProperty(document.body, "margin-top");
}

O$._popupLayer_alignModalLayer = function() {
  var modalLayer = document._of_activeModalLayer;
  var scrollPos = O$.getPageScrollPos();
  var prnt = modalLayer.offsetParent;
  var parentPos = prnt != null && prnt.nodeName != "HTML" && prnt.nodeName != "BODY"
          ? O$.getElementPos(prnt)
          : {left: 0, top: 0};
  var parentLeft = parentPos.left;
  var parentTop = parentPos.top;
  // the comments below are caused by JSFC-2030

  modalLayer.style.left = (/*O$._getLeftBodyMargin() + */ scrollPos.x - parentLeft) + "px";
  modalLayer.style.top = (/*O$._getTopBodyMargin() + */ scrollPos.y - parentTop) + "px";
  O$._popupLayer_resizeModalLayer();
}

O$._popupLayer_resizeModalLayer = function() {
  var modalLayer = document._of_activeModalLayer;

  var visibleAreaSize = O$.getVisibleAreaRectangle();

  modalLayer.style.width = visibleAreaSize.width + "px";
  modalLayer.style.height = visibleAreaSize.height + "px";
}

O$._getCoordsForCenteredPopup = function(popup, oldScrollPos) {
  var visibleAreaSize = O$.getVisibleAreaSize();
  var parentToCalculateScrollOffset = O$._getParentToCalculateScrollOffset(popup);
  var prntPos = O$.getElementPos(parentToCalculateScrollOffset);
  var x = visibleAreaSize.width / 2 - popup.offsetWidth / 2 - prntPos.left + oldScrollPos.x;
  var y = visibleAreaSize.height / 2 - popup.offsetHeight / 2 - prntPos.top + oldScrollPos.y;
  return {x : x, y : y};
}

O$._centerPopup = function(popup, oldScrollPos) {
  var coords = O$._getCoordsForCenteredPopup(popup, oldScrollPos);
  popup.setLeft(coords.x);
  popup.setTop(coords.y);
}


O$._getParentToCalculateScrollOffset = function(popup) {
  var parentToCalculate = popup.offsetParent;
  if (!parentToCalculate) {
    parentToCalculate = document.body;
  }
  return parentToCalculate;
}
