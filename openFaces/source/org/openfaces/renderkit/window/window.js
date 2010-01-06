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

O$.Window = {
  STATE_NORMAL: "normal",
  STATE_MAXIMIZED: "maximized",
  STATE_MINIMIZED: "minimized",

  _init: function(windowId, resizable, draggableByContent, minWidth, minHeight, desktopElementId) {
    var win = O$.initComponent(windowId, null, {
      _form: O$.getParentNode(win, "form"),
      _draggableByContent: draggableByContent,
      _minWidth: O$.calculateNumericCSSValue(minWidth),
      _minHeight: O$.calculateNumericCSSValue(minHeight),
      _table: O$(windowId + "::table"),
      _content: O$(windowId + "::content"),
      _caption: O$(windowId + "::caption"),
      _captionContent: O$(windowId + "::caption_content"),
      _contentRow: O$(windowId + "::contentRow"),
      _footerRow: O$(windowId + "::footerRow"),
      _desktopElement: desktopElementId ? O$(desktopElementId) : null,

      _state: O$.Window.STATE_NORMAL,
      _declaredResizable: resizable,

      _resizable: false,
      _setResizable: function(resizable) {
        if (win._resizable == resizable)
          return;
        win._resizable = resizable;
        if (resizable)
          O$.Window._createResizers(win);
        else
          O$.Window._removeResizers(win);
      },

      _setPos: function(left, top) {
        win.setLeft(left);
        win.setTop(top);
      },

      _setSize: function(width, height) {
        O$.setElementSize(win, {width: width, height: height});
        O$.setHiddenField(win, windowId + "::size", width + "," + height);
        if (!win._table._widthInitialized) {
          win._table.style.width = "100%";
          win._table._widthInitialized = true;
        }
        if (this._sizeChanged)
          this._sizeChanged();
      },

      _setRect: function(rect) {
        this._rect = rect;
        O$.setElementBorderRectangle(win, rect);
        this._setPos(rect.x, rect.y);
        this._setSize(rect.width, rect.height);
        if (resizable)
          this._updateResizersPos();
      },

      _afterShow: function() {
        if (win._postponedInitialization) {
          O$.waitForCondition(function() {
            return !win._postponedInitialization;
          }, win._afterShow);
          return;
        }

        if (resizable)
          win._updateResizersPos();
        win._updateContentPos();
      },

      _afterHide: function() {
        if (resizable)
          this._updateResizersPos();
      },

      _positionChanged: function() {
        if (resizable)
          this._updateResizersPos();
      },

      _updateContentPos: function() {
        if (O$.getElementStyle(this._content, "position", true) != "absolute")
          return; // todo: the case for Confirmation -- unify implementation if possible
        var captionHeight = O$.getElementSize(this._caption).height;
        if (!this._rect)
          this._rect = O$.getElementBorderRectangle(this, true);
        var borderLeft = O$.getNumericElementStyle(this, "border-left-width", true);
        var borderRight = O$.getNumericElementStyle(this, "border-right-width", true);
        var borderTop = O$.getNumericElementStyle(this, "border-top-width", true);
        var borderBottom = O$.getNumericElementStyle(this, "border-bottom-width", true);
        O$.setElementBorderRectangle(this._content, new O$.Rectangle(
                0,
                captionHeight,
                this._rect.width - borderLeft - borderRight,
                this._rect.height - borderTop - borderBottom - captionHeight
                ));
        this._content.style.visibility = "visible";
      },

      _sizeChanged: function() {
        this._updateContentPos();
      },

      isMinimized: function() {
        return this._state == O$.Window.STATE_MINIMIZED;
      },

      isNormal: function() {
        return this._state == O$.Window.STATE_NORMAL;
      },

      isMaximized: function() {
        return this._state == O$.Window.STATE_MAXIMIZED;
      },

      minimize: function() {
        if (this._state == O$.Window.STATE_MINIMIZED)
          return;
        this.restore();
        this._setResizable(false);
        this._setState(O$.Window.STATE_MINIMIZED);
        this._normalSize = O$.getElementSize(this);
        var contentSize = O$.getElementSize(this._content);
        var footerSize = this._footerRow ? O$.getElementSize(this._footerRow) : null;
        this._contentRow._originalDisplay = this._contentRow.style.display;
        this._contentRow.style.display = "none";
        if (this._footerRow) {
          this._footerRow._originalDisplay = this._footerRow.style.display;
          this._footerRow.style.display = "none";
        }
        var rect = O$.getElementBorderRectangle(this, true);
        var newHeight = rect.height - contentSize.height - (this._footerRow ? footerSize.height : 0);
        this._setRect(new O$.Rectangle(rect.x, rect.y, rect.width, newHeight));
      },

      maximize: function() {
        if (this._state == O$.Window.STATE_MAXIMIZED)
          return;
        this.restore();
        this._setResizable(false);
        this._draggingDisabled = true;
        this._setState(O$.Window.STATE_MAXIMIZED);

        this._normalRectangle = O$.getElementBorderRectangle(this, true);
        var rect = this._desktopElement ? O$.getElementPaddingRectangle(this._desktopElement) : O$.getVisibleAreaRectangle();
        if (this._desktopElement || O$.isExplorer6() || O$.isExplorer7()) {
          var container = O$.getContainingBlock(this, true);
          if (container) {
            var containerPos = O$.getElementBorderRectangle(container, false);
            rect.x -= containerPos.x;
            rect.y -= containerPos.y;
          }
        } else {
          var scrollPos = O$.getPageScrollPos();
          rect.x -= scrollPos.x;
          rect.y -= scrollPos.y;
          this.style.position = "fixed";
        }
        this._setRect(rect);
      },

      restore: function() {
        if (this._state == O$.Window.STATE_NORMAL)
          return;
        if (this._state == O$.Window.STATE_MINIMIZED) {
          this._contentRow.style.display = this._contentRow._originalDisplay;
          if (this._footerRow)
            this._footerRow.style.display = this._footerRow._originalDisplay;
          var rect = O$.getElementBorderRectangle(this, true);
          this._setRect(new O$.Rectangle(rect.x, rect.y, this._normalSize.width, this._normalSize.height));
          this._normalSize = null;
        }
        if (this._state == O$.Window.STATE_MAXIMIZED) {
          if (!this._desktopElement)
            this.style.position = "absolute";
          this._setRect(this._normalRectangle);
          this._normalRectangle = null;
        }
        this._setState(O$.Window.STATE_NORMAL);
        win._setResizable(win._declaredResizable);
        this._draggingDisabled = false;
      },

      _setState: function(state) {
        this._state = state;
        if (!this._stateChangeListeners)
          return;
        for (var i = 0, count = this._stateChangeListeners.length; i < count; i++) {
          var listener = this._stateChangeListeners[i];
          listener(this);
        }
      }
    });

    win._setResizable(win._declaredResizable);

    if (!draggableByContent)
      win._draggable = false;
    if (win._minWidth < 5)
      win._minWidth = 5;
    if (win._minHeight < 5)
      win._minHeight = 5;

    if (win._caption && !draggableByContent)
      win._caption.onmousedown = function(e) {
        if (win._draggingDisabled)
          return;
        O$._enableIFrameFix();
        O$.addEventHandler(document, "mouseup", O$._disableIframeFix, true);
        O$.startDragging(e, win, win._caption);
      };

    O$.Window._processCaptionStyle(win);

    if (resizable)
      win._caption.ondblclick = function() {
        if (!win.isNormal())
          win.restore();
        else
          win.maximize();
      };

  },

  _addStateChangeListener: function(win, listener) {
    if (!win._stateChangeListeners)
      win._stateChangeListeners = [];
    win._stateChangeListeners.push(listener);
  },


  _initCloseButton: function(clientId) {
    O$._initCaptionButton.apply(null, arguments);
    var btn = O$(clientId);
    O$.addEventHandler(btn, "click", function () {
      btn._container.hide();
      return false;
    });
  },

  _initMinimizeButton: function(clientId) {
    O$._initToggleCaptionButton.apply(null, arguments);
    var btn = O$(clientId);
    btn._addToggleStateChangeListener(function(toggled) {
      if (toggled) {
        btn._container.minimize();
      }
      else {
        btn._container.restore();
      }
    });
    O$.Window._addStateChangeListener(btn._container, function(win) {
      btn.setToggleState(win.isMinimized(), true);
    });
  },

  _initMaximizeButton: function(clientId) {
    O$._initToggleCaptionButton.apply(null, arguments);
    var btn = O$(clientId);
    btn._addToggleStateChangeListener(function(toggled) {
      if (toggled)
        btn._container.maximize();
      else
        btn._container.restore();
    });
    O$.Window._addStateChangeListener(btn._container, function(win) {
      btn.setToggleState(win.isMinimized() || win.isMaximized(), true);
      btn.style.display = win.isMinimized() ? "none" : "";
    });
  },


  _createResizers: function(win) {
    if (O$.isExplorer()) {
      if (!win._initScheduled) {
        win._initScheduled = true;
        win._postponedInitialization = true;
        var initArgs = arguments;
        // postpone initialization to avoid IE failure during page loading
        O$.addLoadEvent(function() {
          O$.Window._createResizers.apply(null, initArgs);
        });
        return;
      }
    }
    win._postponedInitialization = false;

    var resizerWidth = O$.calculateNumericCSSValue("6px");
    var cornerLength = O$.calculateNumericCSSValue("20px");
    var halfResizerWidth = resizerWidth / 2;

    function createResizer(resizerRectFromWindowRect, horizontal,
                           resizeSide, resizeCorner1, resizeCorner2,
                           cursor, corner1Cursor, corner2Cursor) {
      var resizer = document.createElement("div");
      resizer.style.position = "absolute";
      resizer.style.cursor = cursor;
      O$.fixIEEventsForTransparentLayer(resizer);
      win.parentNode.appendChild(resizer);
      resizer._resizeFunction = resizeSide;
      resizer._updatePos = function(windowRect) {
        if (!windowRect)
          windowRect = O$.getElementBorderRectangle(win);
        var rect = resizerRectFromWindowRect(windowRect);
        O$.setElementBorderRectangle(resizer, rect);
        this._rect = rect;
      };
      resizer.onmousedown = function (e) {
        O$._enableIFrameFix();
        O$.addEventHandler(document, "mouseup", O$._disableIframeFix, true);
        O$.startDragging(e, this);
      };

      resizer.setPosition = function(x, y, dx, dy) {
        var rect = O$.getElementBorderRectangle(win, true);
        this._resizeFunction(rect, dx, dy);
        win._setRect(rect);
      };
      resizer.onmousemove = function(e) {
        if (this._draggingInProgress)
          return;
        var eventPoint = O$.getEventPoint(e, win);
        var x = eventPoint.x - this._rect.x;
        var y = eventPoint.y - this._rect.y;
        var coord = horizontal ? x : y;
        var range = horizontal ? this._rect.width : this._rect.height;
        var adjustedCornerLength = horizontal ? cornerLength : cornerLength - resizerWidth;
        if (coord > range - adjustedCornerLength) {
          this.style.cursor = corner2Cursor;
          this._resizeFunction = resizeCorner2;
          this._getPositionLeft = function() {
            return this._rect.getMaxX();
          };
          this._getPositionTop = function() {
            return this._rect.getMaxY();
          };
        } else if (coord < adjustedCornerLength) {
          this.style.cursor = corner1Cursor;
          this._resizeFunction = resizeCorner1;
          this._getPositionLeft = this._getPositionTop = null;
        } else {
          this.style.cursor = cursor;
          this._resizeFunction = resizeSide;
          this._getPositionLeft = this._getPositionTop = null;
        }

      };
      return resizer;
    }

    win._resizeTopEdge = function(rect, dx, dy) {
      dy = Math.min(dy, rect.height - win._minHeight);
      rect.y += dy;
      rect.height -= dy;
    };
    win._resizeBottomEdge = function(rect, dx, dy) {
      dy = Math.max(dy, win._minHeight - rect.height);
      rect.height += dy;
    };
    win._resizeLeftEdge = function(rect, dx/*, dy*/) {
      dx = Math.min(dx, rect.width - win._minWidth);
      rect.x += dx;
      rect.width -= dx;
    };
    win._resizeRightEdge = function(rect, dx/*, dy*/) {
      dx = Math.max(dx, win._minWidth - rect.width);
      rect.width += dx;
    };
    win._resizeTopLeftCorner = function(rect, dx, dy) {
      win._resizeTopEdge(rect, dx, dy);
      win._resizeLeftEdge(rect, dx, dy);
    };
    win._resizeTopRightCorner = function(rect, dx, dy) {
      win._resizeTopEdge(rect, dx, dy);
      win._resizeRightEdge(rect, dx, dy);
    };
    win._resizeBottomLeftCorner = function(rect, dx, dy) {
      win._resizeBottomEdge(rect, dx, dy);
      win._resizeLeftEdge(rect, dx, dy);
    };
    win._resizeBottomRightCorner = function(rect, dx, dy) {
      win._resizeBottomEdge(rect, dx, dy);
      win._resizeRightEdge(rect, dx, dy);
    };


    var topResizer = createResizer(function(rect) {
      return new O$.Rectangle(rect.x - halfResizerWidth, rect.y - halfResizerWidth, rect.width + resizerWidth, resizerWidth);
    }, true, win._resizeTopEdge, win._resizeTopLeftCorner, win._resizeTopRightCorner, "n-resize", "nw-resize", "ne-resize");

    var bottomResizer = createResizer(function(rect) {
      return new O$.Rectangle(rect.x - halfResizerWidth, rect.getMaxY() - halfResizerWidth, rect.width + resizerWidth, resizerWidth);
    }, true, win._resizeBottomEdge, win._resizeBottomLeftCorner, win._resizeBottomRightCorner, "s-resize", "sw-resize", "se-resize");

    var leftResizer = createResizer(function(rect) {
      return new O$.Rectangle(rect.x - halfResizerWidth, rect.y + halfResizerWidth, resizerWidth, rect.height - resizerWidth);
    }, false, win._resizeLeftEdge, win._resizeTopLeftCorner, win._resizeBottomLeftCorner, "w-resize", "nw-resize", "sw-resize");

    var rightResizer = createResizer(function(rect) {
      return new O$.Rectangle(rect.getMaxX() - halfResizerWidth, rect.y + halfResizerWidth, resizerWidth, rect.height - resizerWidth);
    }, false, win._resizeRightEdge, win._resizeTopRightCorner, win._resizeBottomRightCorner, "e-resize", "ne-resize", "se-resize");

    win._resizers = [topResizer, bottomResizer, leftResizer, rightResizer];
    win._updateResizersPos = function() {
      var windowVisible = win.isVisible();
      var resizerDisplay = windowVisible ? "block" : "none";
      var windowRect = O$.getElementBorderRectangle(win, true);
      win._resizers.forEach(function(resizer) {
        resizer.style.display = resizerDisplay;
        resizer._updatePos(windowRect);
        O$.correctElementZIndex(resizer, win);
      });
    };
    win._updateResizersPos();
  },

  _removeResizers: function(win) {
    win._resizers.forEach(function(r) {
      r.parentNode.removeChild(r);
    });
    win._resizers = [];
  },

  _processCaptionStyle: function(win) {
    if (!win._caption)
      return;
    var paddings = O$.getElementStyle(win._caption, ["padding-left", "padding-right", "padding-top", "padding-bottom"]);
    win._captionContent.style.paddingLeft = paddings.paddingLeft;
    win._captionContent.style.paddingRight = paddings.paddingRight;
    win._captionContent.style.paddingTop = paddings.paddingTop;
    win._captionContent.style.paddingBottom = paddings.paddingBottom;
    win._caption.style.paddingLeft = win._caption.style.paddingRight =
                                     win._caption.style.paddingTop = win._caption.style.paddingBottom = "0";
  }
};