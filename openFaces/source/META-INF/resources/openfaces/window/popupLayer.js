/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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

  _modalPopupsStack: [],

  _init: function(id, left, top, width, height, rolloverStyle, hidingTimeout,
                  draggable, autosizing, modalLayerClass, hideOnEsc, isAjaxRequest, containment) {
    var popup = O$(id);
    O$.initComponent(id, {rollover: rolloverStyle}, {
      left: left,
      top: top,
      _hidingTimeout: hidingTimeout,

      _visibleField: O$(id + "::visible"),
      _leftField: O$(id + "::left"),
      _topField: O$(id + "::top"),
      _hideOnEsc: hideOnEsc,
      _modal: !!modalLayerClass,

      _getDefaultFocusComponent: function() {
        return O$.getFirstFocusableControl(popup);
      },

      _draggable: draggable,
      _autosizing: autosizing,
      _containment: containment,
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

      _setSize: function(width, height) {
        O$.setElementSize(this, {width: width, height: height});
      },

      _getAutosizingArea: function() {
        return this;
      },

      _getAutosizingMargins: function() {
        return {width: 0, height: 0};
      },

      _getAutosizingContentPaddings: function() {
        return {width: 0, height: 0};
      },

      _getAutosizingContentBorders: function() {
        return {width: 0, height: 0};
      },

      _getMinSize: function() {
        return {width: 0, height: 0};
      },

      _beforeAutosizing: function() {

      },

      _afterAutosizing: function() {

      },

      /*
       Resizes the popup layer automatically to fit its content. If the "width" attribute (_init parameter) of
       PopupLayer is specified then width is set to the specified value, and only height is detected automatically.

       Otherwise, width is detected automatically to match content width, but not greater than the width of
       PopupLayer's containing block (containing block defines a rectangle relative to which absolutely positioned
       elements such as PopupLayer are positioned. This is the nearest container which has a CSS "position" property
       value of "absolute", "relative" or "fixed", or the document when there are no such elements, see here:
       http://www.w3.org/TR/CSS2/visudet.html#containing-block-details).

       If the PopupLayer's "containment" attribute (_init parameter) is specified, then the automatically detected size
       is not greater than the size of the containment area specified with the "containment" attribute.
       */
      _resizeToContent: function() {
        if (!this._autosizingAllowed())
          return;

        var area = this._getAutosizingArea();
        var autosizingMargins = this._getAutosizingMargins();

        this._beforeAutosizing();
        var prevLeft = area.style.left;
        var prevTop = area.style.top;
        try {
          area.style.left = "0px";
          area.style.top = "0px";

          var maxWidth = null;
          if (O$.isExplorer() && O$.isQuirksMode()) {
            // Workaround for "Internet Explorer box model bug"
            maxWidth = width ? O$.calculateNumericCSSValue(width) - autosizingMargins.width : null;
          } else {
            var autosizingContentPaddings = this._getAutosizingContentPaddings();
            var autosizingContentBorders = this._getAutosizingContentBorders();
            maxWidth = width ? O$.calculateNumericCSSValue(width) - autosizingMargins.width
                    - autosizingContentPaddings.width - autosizingContentBorders.width: null;
          }
          area.style.width = maxWidth != null ? maxWidth + "px" : "auto";
          area.style.height = "auto";
          var size = O$.getElementSize(area);

          var containingBlock = this.offsetParent;
          if (!containingBlock) containingBlock = document.body;
          var containmentRect = O$.getContainmentRectangle(this._containment || "document", containingBlock);
          containmentRect.width -= autosizingMargins.width;
          containmentRect.height -= autosizingMargins.height;
          if (size.width > containmentRect.width)
            size.width = containmentRect.width;
          if (size.height > containmentRect.height)
            size.height = containmentRect.height;
        } finally {
          area.style.left = prevLeft;
          area.style.top = prevTop;
          this._afterAutosizing();
        }

        var newWidth = size.width + autosizingMargins.width;
        var newHeight = size.height + autosizingMargins.height;
        var minSize = this._getMinSize();
        if (newWidth < minSize.width) newWidth = minSize.width;
        if (newHeight < minSize.height) newHeight = minSize.height;
        this._setSize(newWidth, newHeight);
      },

      _autosizingAllowed: function() {
        return true;
      },

      show: function () { // todo: this partially duplicates popup.show declared in popup.js, merge as much functionality in popupLayer.js and popup.js as possible
        if (popup.isVisible()) return;
        popup.style.display = "block";
        if (autosizing == "on") {
          popup._resizeToContent();
        }
        O$.addIETransparencyControl(popup);
        if (popup._blockingLayer) {
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
            if (focusable) {
              try {
                focusable.focus();
              } catch (e) {
                // the control is not focusable for some reason
              }
            } else
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
            if (focusable) {
              try {
                focusable.focus();
              } catch (e) {
                // the control is not focusable for some reason
              }
            } else
              lastExternalAnchor.blur();
          };
          body.appendChild(lastExternalAnchor);

          popup._firstInternalAnchor = O$.createHiddenFocusElement(popup.id);
          popup._firstInternalAnchor._focusControlElement = true;
          popup.insertBefore(popup._firstInternalAnchor, popup.firstChild);
          popup._firstInternalAnchor.onfocus = function() {
            var focusable = O$.getLastFocusableControl(popup);
            if (focusable) {
              try {
                focusable.focus();
              } catch (e) {
                // the control is not focusable for some reason
              }
            } else
              this.blur();
          };
          popup._firstInternalAnchor.focus();

          popup._lastInternalAnchor = O$.createHiddenFocusElement(popup.id);
          popup._lastInternalAnchor._focusControlElement = true;
          popup._lastInternalAnchor.onfocus = function() {
            var focusable = O$.getFirstFocusableControl(popup);
            if (focusable) {
              try {
                focusable.focus();
              } catch (e) {
                // the control is not focusable for some reason
              }
            } else
              this.blur();
          };
          popup.appendChild(popup._lastInternalAnchor);

          O$.initIETransparencyWorkaround(popup._blockingLayer);
          popup._blockingLayer.style.display = "block";
          if (O$.PopupLayer._modalPopupsStack.length > 0) {
            var parentModalPopup = O$.PopupLayer._modalPopupsStack[O$.PopupLayer._modalPopupsStack.length - 1];
            O$.correctElementZIndex(popup._blockingLayer, parentModalPopup);
          }
          O$.correctElementZIndex(popup, popup._blockingLayer);

          O$.PopupLayer._resizeModalLayer();
          if (O$._simulateFixedPosForBlockingLayer()) {
            O$.addEventHandler(window, "resize", O$.PopupLayer._resizeModalLayer);
            O$.addEventHandler(window, "scroll", O$.PopupLayer._alignModalLayer);
            O$.PopupLayer._alignModalLayer();
          } else {
            popup._blockingLayer.style.left = "0px";
            popup._blockingLayer.style.top = "0px";
            popup._blockingLayer.style.position = "fixed";
            O$.addEventHandler(window, "resize", O$.PopupLayer._resizeModalLayer, true);
          }
          O$.addIETransparencyControl(popup._blockingLayer);

          setTimeout(function() {
            var focusable = popup._getDefaultFocusComponent();
            if (focusable) try {
              focusable.focus();
            } catch (e) {
              // the control is not focusable for some reason
            }
          }, 50);

          O$.PopupLayer._modalPopupsStack.push(popup);
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
        else {
          // in case of window, let window populate the _afterShow method in a next call and then try again
          setTimeout(function() {
            if (popup._afterShow)
              popup._afterShow();
          }, 1);
        }
      },

      hide: function () {
        if (!popup.isVisible()) return;
        if (popup._modal) {
          var currentModalPopup = O$.PopupLayer._modalPopupsStack.pop();
          if (currentModalPopup != popup) {
            O$.PopupLayer._modalPopupsStack.push(currentModalPopup);
            throw "Cannot fulfill the request to close a modal window (id=\"" + popup.id + "\"), " +
                    "which has modal sub-windows still open.";
          }
        }

        O$.removeIETransparencyControl(popup);
        if (popup._blockingLayer) {
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

        if (popup._blockingLayer) {
          O$.removeIETransparencyControl(popup._blockingLayer);
          popup._blockingLayer.style.display = "none";
          if (O$._simulateFixedPosForBlockingLayer()) {
            O$.removeEventHandler(window, "scroll", O$.PopupLayer._alignModalLayer);
            O$.removeEventHandler(window, "resize", O$.PopupLayer._resizeModalLayer);
          } else {
            O$.removeEventHandler(window, "resize", O$.PopupLayer._resizeModalLayer);
          }
          if (O$.isOpera()) {
            document.body.style.visibility = "hidden";
            document.body.style.visibility = "visible";
          }

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

    if (popup._modal) {
      var blockingLayerId = id + "::blockingLayer";
      var blockingLayer = O$(blockingLayerId);
      if (blockingLayer != null)
        blockingLayer.parentNode.removeChild(blockingLayer);
      blockingLayer = document.createElement("div");
      blockingLayer.id = blockingLayerId;
      blockingLayer.className = modalLayerClass;
      popup.parentNode.insertBefore(blockingLayer, popup);

      popup._blockingLayer = blockingLayer;
    }

    popup.style.display = O$.getElementStyle(popup, "display");

    if (left != null)
      try {
        popup.setLeft(left);
      } catch (e) {
        O$.logError("Invalid value of the 'left' attribute of window or popup layer (id=" + id + "): \"" + left + "\" ; it must be an integer value ; original error: " + e.message);
        throw e;
      }

    if (top != null)
      try {
        popup.setTop(top);
      } catch (e) {
        O$.logError("Invalid value of the 'top' attribute of window or popup layer (id=" + id + "): \"" + top + "\" ; it must be an integer value ; original error: " + e.message);
        throw e;
      }

    if (width != null)
      try {
        popup.style.width = width;
      } catch (e) {
        O$.logError("Invalid value of the 'width' attribute of window or popup layer (id=" + id + "): \"" + width + "\" ; it must be a valid CSS declaration like \"100px\" ; original error: " + e.message);
        throw e;
      }

    if (height != null)
      try {
        popup.style.height = height;
      } catch (e) {
        O$.logError("Invalid value of the 'height' attribute of window or popup layer (id=" + id + "): \"" + height + "\" ; it must be a valid CSS declaration like \"100px\" ; original error: " + e.message);
        throw e;
      }

    if (autosizing == "on") {
      var intervalId = setInterval(function() {
        if (!O$.isElementPresentInDocument(popup)) {
          clearInterval(intervalId);
          return;
        }

        if (popup.isVisible())
          popup._resizeToContent();
      }, 200);
    }

    O$.initIETransparencyWorkaround(popup);
    if (popup._blockingLayer)
      popup._blockingLayer.onclick = function() {
        var focusable = popup._getDefaultFocusComponent();
        if (focusable) {
          try {
            focusable.focus();
          } catch (e) {
            // the control is not focusable for some reason
          }
        }
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
    var scrollPos = O$.getPageScrollPos();
    O$.PopupLayer._modalPopupsStack.forEach(function(popup) {
      var modalLayer = popup._blockingLayer;

      var prnt = modalLayer.offsetParent;
      var parentPos = prnt != null && prnt.nodeName != "HTML" && prnt.nodeName != "BODY"
              ? O$.getElementPos(prnt)
              : {x: 0, y: 0};
      var parentLeft = parentPos.x;
      var parentTop = parentPos.y;

      modalLayer.style.left = (scrollPos.x - parentLeft) + "px";
      modalLayer.style.top = (scrollPos.y - parentTop) + "px";
      O$.PopupLayer._resizeModalLayer();

    });
  },

  _resizeModalLayer: function() {
    var visibleAreaSize = O$.getVisibleAreaRectangle();

    O$.PopupLayer._modalPopupsStack.forEach(function(popup) {
      var modalLayer = popup._blockingLayer;

      modalLayer.style.width = visibleAreaSize.width + "px";
      modalLayer.style.height = visibleAreaSize.height + "px";
    });

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
  if (evt.keyCode != 27) return;

  if (O$.PopupLayer._modalPopupsStack.length > 0) {
    var currentModalWindow = O$.PopupLayer._modalPopupsStack[O$.PopupLayer._modalPopupsStack.length - 1];
    if (currentModalWindow._hideOnEsc)
      currentModalWindow.hide();
  }
});
