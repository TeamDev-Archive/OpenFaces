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

//--------------------  public functions  ----------------------

O$.resizeSidePanel = function(sidePanelId, newSize) {
  var sidePanel = O$(sidePanelId);
  if (!(sidePanel && sidePanel._alignment)) return;
  O$._resizeSidePanel(sidePanel, newSize, false);

  if (sidePanel.onsplitterdrag)
    sidePanel.onsplitterdrag(sidePanel);
};

O$.collapseSidePanel = function(sidePanelId) {
  var sidePanel = O$(sidePanelId);
  if (!(sidePanel && sidePanel._alignment)) return;
  if (sidePanel._collapsed) return;
  sidePanel._collapsed = true;
  sidePanel._splitter._newStyle.cursor = sidePanel._splitterCursor_collapsedState;
  sidePanel._normalSize = sidePanel._size;
  O$._resizeSidePanel(sidePanel, sidePanel._collapsedSize, true);

  if (sidePanel.oncollapse)
    sidePanel.oncollapse(sidePanel);
};

O$.restoreSidePanel = function(sidePanelId) {
  var sidePanel = O$(sidePanelId);
  if (!(sidePanel && sidePanel._alignment)) return;
  if (!sidePanel._collapsed && !sidePanel._maximized) return;
  sidePanel._collapsed = false;
  sidePanel._maximized = false;
  sidePanel._splitter._newStyle.cursor = sidePanel._splitterCursor_normalState;
  var newSize = sidePanel._normalSize;
  O$._resizeSidePanel(sidePanel, newSize, true);

  if (sidePanel.onrestore)
    sidePanel.onrestore(sidePanel);
};

O$.maximizeSidePanel = function(sidePanelId) {
  var sidePanel = O$(sidePanelId);
  if (!(sidePanel && sidePanel._alignment)) return;
  O$._resizeSidePanel(sidePanel, sidePanel._maxSize, false);
  sidePanel._maximized = true;

  if (sidePanel.onmaximize)
    sidePanel.onmaximize(sidePanel);
};

O$.refreshSidePanel = function(sidePanelId) {
  var sidePanel = O$(sidePanelId);
  if (!(sidePanel && sidePanel._alignment)) return;
  O$._recalculateSidePanel(sidePanel);
  O$._repaintSidePanel(sidePanel);
};

//------------------  SidePanel init method  -------------------

O$._initSidePanel = function(sidePanelId,
                             alignment,
                             size,
                             minSize,
                             maxSize,
                             collapsible,
                             resizable,
                             collapsed,
                             rolloverClass,
                             splitterRolloverClass,
                             events) {
  var sidePanel = O$.initComponent(sidePanelId, null, {
    _splitter: O$(sidePanelId + "::splitter"),
    _panel: O$(sidePanelId + "::panel"),
    _caption: O$(sidePanelId + "::caption"),
    _content: O$(sidePanelId + "::content"),

    _isClicking: false,
    _alignment: alignment,
    _collapsed: false,
    _collapsible: collapsible,
    _resizable: resizable,
    _iframeBugCorrection: true,
    _selectBugCorrection: true,
    _blockSelfRepaint: false,
    _size: size,
    _minSize: minSize,
    _maxSize: maxSize
  });
  sidePanel._panel._isCoupled = true;

  O$._initSidePanel_style(sidePanel, rolloverClass, splitterRolloverClass);
  O$._initSidePanel_events(sidePanel, events);

  O$._checkAsRootDoubleBufferedElement(sidePanel);
  O$._subscribeToOnresizeEvent(sidePanel, function() {
    O$.refreshSidePanel(sidePanelId);
    O$._sendResizeEvent(sidePanel._content);
  });
  sidePanel._content._isResizableElement = true;
  if (sidePanel._caption) sidePanel._caption._isResizableElement = true;

  O$._recalculateSidePanel(sidePanel);
  O$._repaintSidePanel(sidePanel);

  if (collapsed) {
    O$.collapseSidePanel(sidePanelId);
  }
};

O$._initSidePanel_style = function(sidePanel, rolloverClass, splitterRolloverClass) {
  var splitter = sidePanel._splitter;
  var panel = sidePanel._panel;
  var caption = sidePanel._caption;
  var content = sidePanel._content;

  sidePanel._newStyle = new O$._createPseudoCSSStyle();
  splitter._newStyle = new O$._createPseudoCSSStyle();
  panel._newStyle = new O$._createPseudoCSSStyle();
  if (caption != null)
    caption._newStyle = new O$._createPseudoCSSStyle();
  content._newStyle = new O$._createPseudoCSSStyle();

  O$._setupRolloverClass(splitter, splitterRolloverClass);
  O$._setupRolloverClass(panel, rolloverClass);
  O$._cacheSidePanelSizeVariables(sidePanel);

  if (O$.isExplorer() && O$.isQuirksMode()) { //need to identical splitter view in IE quirks mode and in other case
    splitter.style.width = (O$._calculateNumericWidth(splitter, false) + splitter._storedSizeProperties.paddingsAndBordersWidth) + "px";
    splitter.style.height = (O$._calculateNumericHeight(splitter, false) + splitter._storedSizeProperties.paddingsAndBordersHeight) + "px";
  }

  //set collapsedSize, minSize, maxSize
  if (sidePanel._alignment == "left" || sidePanel._alignment == "right") {
    sidePanel._collapsedSize = (O$._calculateOffsetWidth(splitter, false) + splitter._storedSizeProperties.marginsWidth) + "px";
  } else {
    sidePanel._collapsedSize = (O$._calculateOffsetHeight(splitter, false) + splitter._storedSizeProperties.marginsHeight) + "px";
  }

  if (sidePanel._minSize == null) {
    sidePanel._minSize = sidePanel._collapsedSize;
  }
  if (sidePanel._maxSize == null) {
    sidePanel._maxSize = "100%";
  }
  O$._correctSizeOnMinMax(sidePanel);

  //sidePanel
  //todo calculate sidePanel._newStyle.width and sidePanel._newStyle.height via panel.style.width and panel.style.height

  //splitter
  sidePanel._splitterCursor_normalState = O$.getElementStyle(splitter, "cursor");
  if (sidePanel._collapsible) {
    sidePanel._splitterCursor_collapsedState = "pointer";
  } else {
    sidePanel._splitterCursor_collapsedState = "auto";
  }
  if (!sidePanel._resizable) {
    sidePanel._splitterCursor_normalState = sidePanel._splitterCursor_collapsedState;
  }
  splitter.style.cursor = sidePanel._splitterCursor_normalState;

  //caption & content
  if (caption != null) {
    O$._setInnerElementOuterRightTopCorner(caption, 0, 0);
  }
  O$._setInnerElementOuterRightBottomCorner(content, 0, 0);
  content._isCouplingElement = true;
};

O$._initSidePanel_events = function(sidePanel, events) {
  O$.addEventHandler(sidePanel._splitter, "mousedown", O$._splitterMouseDown, false);
  O$.addEventHandler(sidePanel._splitter, "click", O$._splitterMouseClick, false);
  sidePanel.resize = function(newSize) {
    O$.resizeSidePanel(sidePanel.id, newSize);
  };
  sidePanel.collapse = function() {
    O$.collapseSidePanel(sidePanel.id);
  };
  sidePanel.restore = function() {
    O$.restoreSidePanel(sidePanel.id);
  };
  sidePanel.maximize = function() {
    O$.maximizeSidePanel(sidePanel.id);
  };
  sidePanel.refresh = function() {
    O$.refreshSidePanel(sidePanel.id);
  };
  O$._applyEventsObjectToElement(events, sidePanel);
  O$._subscribeToOnresizeEvent(sidePanel, sidePanel.refresh);
};

//--------------------  private functions  ---------------------

O$._recalculateSidePanel = function(sidePanel) {
  var splitter = sidePanel._splitter;
  var panel = sidePanel._panel;
  var caption = sidePanel._caption;
  var content = sidePanel._content;

  if ((sidePanel._alignment == "left") || (sidePanel._alignment == "right")) {
    //sidePanel
    var sidePanelWidth = O$._calculateNumericSizeValue(sidePanel, sidePanel._size, false);  //todo create full double buffering
    var sidePanelHeight = O$._calculateNumericHeight(sidePanel, true);
    sidePanel._newStyle.width = sidePanelWidth + "px";
    //splitter
    splitter._newStyle.height = (sidePanelHeight - splitter._heightDiff) + "px";
  }
  else {
    //sidePanel
    sidePanelWidth = O$._calculateNumericWidth(sidePanel, true);
    sidePanelHeight = O$._calculateNumericSizeValue(sidePanel, sidePanel._size, false);  //todo create full double buffering
    sidePanel._newStyle.height = sidePanelHeight + "px";
    //splitter
    splitter._newStyle.width = (sidePanelWidth - splitter._widthDiff) + "px";
  }
  O$._bugFix_divNegativeSizeBug(sidePanel, true);
  O$._bugFix_divNegativeSizeBug(splitter, true);
  //panel
  var panelWidth = sidePanelWidth - panel._widthDiff;
  var panelHeight = sidePanelHeight - panel._heightDiff;
  panel._newStyle.height = panelHeight + "px";
  panel._newStyle.width = panelWidth + "px";
  O$._bugFix_divNegativeSizeBug(panel, true);
  if (O$._isExplorerQuirksMode()) {
    var panelInnerHeight = panelHeight - panel._storedSizeProperties.paddingsAndBordersHeight;
  } else {
    panelInnerHeight = panelHeight;
  }
  //caption
  if (caption) {
    caption._newStyle.width = (panelWidth - caption._widthDiff) + "px";
    if (panelInnerHeight < caption._fullHeight) {
      caption._newStyle.height = (panelHeight - caption._heightDiff) + "px";
      content._newStyle.height = "0px";
      content._newStyle.display = "none";
      captionOuterHeight = panelHeight;
      caption._truncatedMode = true;
    } else {
      if ((!caption._truncatedMode) && (caption.style.display != "none")) {
        caption._fullHeight = O$._calculateOffsetHeight(caption, false) + caption._storedSizeProperties.marginsHeight;
      }
      if (caption._truncatedMode) {
        if (O$._isExplorerQuirksMode()) {
          var captionHeight = caption._fullHeight - caption._storedSizeProperties.marginsHeight;
        } else {
          captionHeight = caption._fullHeight - caption._heightDiff;
        }
        caption.style.display = "block"; //to correct O$._calculateOffsetHeight() work
        caption._truncatedMode = false;
        caption._newStyle.height = captionHeight + "px";
      }
    }
    var captionOuterHeight = caption._fullHeight;
    O$._bugFix_divNegativeSizeBug(sidePanel._caption, true);
  } else {
    captionOuterHeight = 0;
  }

  //content
  content._newStyle.height = (panelHeight - content._heightDiff - captionOuterHeight) + "px";
  content._newStyle.width = (panelWidth - content._widthDiff) + "px";
  O$._bugFix_divNegativeSizeBug(sidePanel._content, true);
};

O$._repaintSidePanel = function(sidePanel) {
  var curHeight = parseInt(sidePanel._panel.style.height);
  var curWidth = parseInt(sidePanel._panel.style.width);
  var newHeight = parseInt(sidePanel._panel._newStyle.height);
  var newWidth = parseInt(sidePanel._panel._newStyle.width);
  var isDecrase = ((newHeight < curHeight) || (newWidth < curWidth));
  if (isDecrase) {
    sidePanel._content._newStyle.applyTo(sidePanel._content.style);
    if (sidePanel._caption) {
      sidePanel._caption._newStyle.applyTo(sidePanel._caption.style);
    }
    sidePanel._panel._newStyle.applyTo(sidePanel._panel.style);
    sidePanel._splitter._newStyle.applyTo(sidePanel._splitter.style);
    sidePanel._newStyle.applyTo(sidePanel.style);
  } else {
    sidePanel._newStyle.applyTo(sidePanel.style);
    sidePanel._splitter._newStyle.applyTo(sidePanel._splitter.style);
    sidePanel._panel._newStyle.applyTo(sidePanel._panel.style);
    sidePanel._content._newStyle.applyTo(sidePanel._content.style);
    if (sidePanel._caption != null) {
      sidePanel._caption._newStyle.applyTo(sidePanel._caption.style);
    }
  }
  O$._sidePanelSaveState(sidePanel);
  O$._sendResizeEvent(sidePanel._content);
};

O$._resizeSidePanel = function(sidePanel, newSize, ignoreMinMaxSize) {
  sidePanel._size = newSize;
  if (!ignoreMinMaxSize) {
    O$._correctSizeOnMinMax(sidePanel);
  }

  if (!sidePanel._blockSelfRepaint) {
    O$._recalculateSidePanel(sidePanel);
    O$._repaintSidePanel(sidePanel);
  }
};

O$._sidePanelResizingBegin = function(sidePanel, event) {
  sidePanel._resizeBeginSize = sidePanel._size;
  if (O$.isExplorer()) {
    var mouseX = event.clientX + document.body.scrollLeft;
    var mouseY = event.clientY + document.body.scrollTop;
  } else {
    mouseX = event.pageX;
    mouseY = event.pageY;
  }
  sidePanel._resizeBeginMouseX = mouseX;
  sidePanel._resizeBeginMouseY = mouseY;

  //add event handlers to DOCUMENT
  document._nowResizingSidePanel = sidePanel;
  O$.addEventHandler(document, "mousemove", O$._documentMouseMoveForSidePanel, false);
  O$.addEventHandler(document, "mouseup", O$._documentMouseUpForSidePanel, false);
  if (sidePanel._iframeBugCorrection) {
    O$._enableIFrameFix();
  }
  if (sidePanel._selectBugCorrection) {
    O$._disableMouseSelection(event);
  }
};

O$._sidePanelResizingEnd = function(sidePanel) {
  if (!document._nowResizingSidePanel) return;
  //remove event handlers from DOCUMENT
  O$.removeEventHandler(document, "mousemove", O$._documentMouseMoveForSidePanel, false);
  O$.removeEventHandler(document, "mouseup", O$._documentMouseUpForSidePanel, false);

  if (O$.isExplorer()) {
    document._nowResizingSidePanel = undefined;
    sidePanel._resizeBeginMouseX = undefined;
    sidePanel._resizeBeginMouseY = undefined;
    sidePanel._resizeBeginSize = undefined;
    sidePanel._isClicking = undefined;
  } else {
    delete document._nowResizingSidePanel;
    delete sidePanel._resizeBeginMouseX;
    delete sidePanel._resizeBeginMouseY;
    delete sidePanel._resizeBeginSize;
    delete sidePanel._isClicking;
  }

  if (sidePanel._iframeBugCorrection) {
    O$._disableIframeFix();
  }
  if (sidePanel._selectBugCorrection) {
    O$._enableMouseSelection();
  }
};

O$._splitterMouseDown = function(event) {
  if (O$._mouseButton(event) == "left") {
    if (O$.isExplorer()) {
      var sidePanel = event.srcElement.parentNode;
    } else {
      sidePanel = this.parentNode;
    }
    O$._sidePanelResizingBegin(sidePanel, event);
    sidePanel._isClicking = true;
  }
  O$.breakEvent(event);
};

O$._splitterMouseClick = function(event) {
  if (O$.isExplorer()) {
    var sidePanel = event.srcElement.parentNode;
  } else {
    sidePanel = this.parentNode;
  }
  if (sidePanel._isClicking && sidePanel._collapsible) {
    if (sidePanel._collapsed) {
      sidePanel.restore();
    } else {
      sidePanel.collapse();
    }
    O$._sidePanelResizingEnd(sidePanel);
  }
};

O$._documentMouseUpForSidePanel = function() {
  if (!document._nowResizingSidePanel) return;
  var sidePanel = document._nowResizingSidePanel;

  if (sidePanel._isClicking && sidePanel._collapsible) {
    if (sidePanel._collapsed) {
      sidePanel.restore();
    } else {
      sidePanel.collapse();
    }
  }
  O$._sidePanelResizingEnd(document._nowResizingSidePanel);
};

O$._documentMouseMoveForSidePanel = function(event) {
  if (!document._nowResizingSidePanel) return;
  var sidePanel = document._nowResizingSidePanel;
  sidePanel._isClicking = false;
  if (sidePanel._resizable && !sidePanel._collapsed) {
    if (O$.isExplorer()) {
      var mouseX = event.clientX + document.body.scrollLeft;
      var mouseY = event.clientY + document.body.scrollTop;
    } else {
      mouseX = event.pageX;
      mouseY = event.pageY;
    }

    var alignment = sidePanel._alignment;
    if (alignment == "left") {
      var sizeChange = mouseX - sidePanel._resizeBeginMouseX;
    } else if (alignment == "right") {
      sizeChange = sidePanel._resizeBeginMouseX - mouseX;
    } else if (alignment == "top") {
      sizeChange = mouseY - sidePanel._resizeBeginMouseY;
    } else if (alignment == "bottom") {
      sizeChange = sidePanel._resizeBeginMouseY - mouseY;
    }
    if (O$._isPercentageValue(sidePanel._resizeBeginSize)) {
      if (alignment == "left" || alignment == "right") {
        var sizeFactor = O$._calculateNumericWidthFactor(sidePanel, false); //todo create full double buffering
      } else {
        sizeFactor = O$._calculateNumericHeightFactor(sidePanel, false);
      }
      var newSize = (parseFloat(sidePanel._resizeBeginSize) + sizeChange * 100.0 / sizeFactor) + "%";
    } else {
      newSize = (parseInt(sidePanel._resizeBeginSize) + sizeChange) + "px";
    }
    O$.resizeSidePanel(sidePanel.id, newSize);
  }
};

O$._calculateNumericSizeValue = function(sidePanel, sizeValue, useDoubleBuffering) {
  if (O$._isPercentageValue(sizeValue)) {
    if (sidePanel._alignment == "left" || sidePanel._alignment == "right") {
      var parentSizeFactor = O$._calculateNumericWidthFactor(sidePanel, useDoubleBuffering);
    }
    else {
      parentSizeFactor = O$._calculateNumericHeightFactor(sidePanel, useDoubleBuffering);
    }
    var size = Math.round(parentSizeFactor * 0.01 * parseFloat(sizeValue));
  }
  else {
    size = parseInt(sizeValue);
  }
  return size;
};

O$._correctSizeOnMinMax = function(sidePanel) {
  var size = O$._calculateNumericSizeValue(sidePanel, sidePanel._size);
  var minSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._minSize);
  var maxSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._maxSize);
  if (size < minSize) {
    size = minSize;
  } else if (size > maxSize) {
    size = maxSize;
  } else {
    return;
  }
  if (O$._isPercentageValue(sidePanel._size)) {
    if (sidePanel._alignment == "left" || sidePanel._alignment == "right") {
      var sizeFactor = O$._calculateNumericWidthFactor(sidePanel, false); //todo create full double buffering
    }
    else {
      sizeFactor = O$._calculateNumericHeightFactor(sidePanel, false);
    }
    size = (size * 100.0 / sizeFactor) + "%";
  } else {
    size = size + "px";
  }
  sidePanel._size = size;
};

O$._sidePanelSaveState = function(sidePanel, stateStr) {
  if (!stateStr) {
    if (sidePanel._collapsed) {
      stateStr = sidePanel._normalSize + ";" + sidePanel._collapsed;
    } else {
      stateStr = sidePanel._size + ";" + sidePanel._collapsed;
    }
  }
  var stateHolderId = sidePanel.id + "state";
  if (sidePanel._stateHolder) {
    sidePanel._stateHolder.value = stateStr;
  } else {
    if (O$.isExplorer()) {
      O$.addLoadEvent(function() {
        sidePanel._stateHolder = O$.setHiddenField(sidePanel, stateHolderId, stateStr);
      });
    } else {
      sidePanel._stateHolder = O$.setHiddenField(sidePanel, stateHolderId, stateStr);
    }
  }
};

O$._cacheSidePanelSizeVariables = function(sidePanel) {
  var splitter = sidePanel._splitter;
  var panel = sidePanel._panel;
  var caption = sidePanel._caption;
  var content = sidePanel._content;

  //O$._storeSizeProperties(sidePanel);
  O$._storeSizeProperties(splitter);
  O$._storeSizeProperties(panel);
  if (caption) {
    O$._storeSizeProperties(caption);
  }
  O$._storeSizeProperties(content);

  if (O$._isExplorerQuirksMode()) {
    splitter._heightDiff = splitter._storedSizeProperties.marginsHeight;
    splitter._widthDiff = splitter._storedSizeProperties.marginsWidth;
    if (sidePanel._alignment == "left" || sidePanel._alignment == "right") {
      panel._heightDiff = panel._storedSizeProperties.marginsHeight;
      panel._widthDiff = panel._storedSizeProperties.marginsWidth + O$._calculateOffsetWidth(splitter, false) + splitter._storedSizeProperties.paddingsAndBordersAndMarginsWidth;
    } else {
      panel._heightDiff = panel._storedSizeProperties.marginsHeight + O$._calculateOffsetHeight(splitter, false) + splitter._storedSizeProperties.paddingsAndBordersAndMarginsHeight;
      panel._widthDiff = panel._storedSizeProperties.marginsWidth;
    }
    if (caption) {
      caption._heightDiff = panel._storedSizeProperties.paddingsAndBordersHeight + caption._storedSizeProperties.marginsHeight;
      caption._widthDiff = panel._storedSizeProperties.paddingsAndBordersWidth + caption._storedSizeProperties.marginsWidth;
    }
    content._heightDiff = panel._storedSizeProperties.paddingsAndBordersHeight + content._storedSizeProperties.marginsHeight;
    content._widthDiff = panel._storedSizeProperties.paddingsAndBordersWidth + content._storedSizeProperties.marginsWidth;
  } else {
    splitter._heightDiff = splitter._storedSizeProperties.paddingsAndBordersAndMarginsHeight;
    splitter._widthDiff = splitter._storedSizeProperties.paddingsAndBordersAndMarginsWidth;
    if (sidePanel._alignment == "left" || sidePanel._alignment == "right") {
      panel._heightDiff = panel._storedSizeProperties.paddingsAndBordersAndMarginsHeight;
      panel._widthDiff = panel._storedSizeProperties.paddingsAndBordersAndMarginsWidth + O$._calculateOffsetWidth(splitter, false) + splitter._storedSizeProperties.marginsWidth;
    } else {
      panel._heightDiff = panel._storedSizeProperties.paddingsAndBordersAndMarginsHeight + O$._calculateOffsetHeight(splitter, false) + splitter._storedSizeProperties.marginsHeight;
      panel._widthDiff = panel._storedSizeProperties.paddingsAndBordersAndMarginsWidth;
    }
    if (caption) {
      caption._heightDiff = caption._storedSizeProperties.paddingsAndBordersAndMarginsHeight;
      caption._widthDiff = caption._storedSizeProperties.paddingsAndBordersAndMarginsWidth;
    }
    content._heightDiff = content._storedSizeProperties.paddingsAndBordersAndMarginsHeight;
    content._widthDiff = content._storedSizeProperties.paddingsAndBordersAndMarginsWidth;
  }

  if (caption) {
    caption._fullHeight = O$._calculateOffsetHeight(caption, false) + caption._storedSizeProperties.marginsHeight;
  }
};

//--------------------  splitter tools  ------------------------

O$._createSplitterImage = function(sidePanel, imgSrc, IEPngBugFixEnabled) {
  var addFunction = function () {
    image.style.padding = "0px";
    image.style.margin = "0px";
    image.style.border = "0px solid gray";
    sidePanel._splitter.appendChild(image);
    O$._setAbsoluteCenterPosition(image);
    sidePanel._splitter._image = image;
    image.onmousedown = function () {
      return false;
    };
    if (IEPngBugFixEnabled)
      O$._bugFix_IEPng(image);
  };

  var image = document.createElement("img");
  image.src = imgSrc;
  if (O$.isExplorer() && (document.readyState != "complete")) {
    O$.addLoadEvent(addFunction);
  } else {
    addFunction();
  }
};

O$._createSplitterButton = function(sidePanel, imgSrc, imgSrc_collapsed, imgSrc_rollover, imgSrc_rollover_collapsed, imgSrc_pressed, imgSrc_pressed_collapsed, IEPngBugFixEnabled) {
  var addFunction = function () {
    sidePanel._splitter.appendChild(button);
    if (IEPngBugFixEnabled)
      O$._bugFix_IEPng(button);
    O$._setAbsoluteCenterPosition(button);
    sidePanel._splitter._button = button;

    button.onmouseover = function() {
      if (sidePanel._collapsed) {
        button.src = imgSrc_rollover_collapsed;
      } else {
        button.src = imgSrc_rollover;
      }
      if (IEPngBugFixEnabled)
        O$._bugFix_IEPng(button);
    };
    button.onmouseout = function() {
      if (sidePanel._collapsed) {
        button.src = imgSrc_collapsed;
      } else {
        button.src = imgSrc;
      }
      if (IEPngBugFixEnabled)
        O$._bugFix_IEPng(button);
    };
    button.onmousedown = function(event) {
      if (sidePanel._collapsed) {
        button.src = imgSrc_pressed_collapsed;
      } else {
        button.src = imgSrc_pressed;
      }
      if (IEPngBugFixEnabled)
        O$._bugFix_IEPng(button);
      if (O$.isExplorer() && event)
        event.cancelBubble = true;
      else
        event.stopPropagation();
      //O$.breakEvent(event);
    };
    button.onmouseup = button.onmouseout;
    button.onclick = function(event) {
      if (sidePanel._collapsed) {
        O$.restoreSidePanel(sidePanel.id);
      } else {
        O$.collapseSidePanel(sidePanel.id);
      }
      O$.breakEvent(event);
    };
  };

  var button = document.createElement("img");
  button.style.padding = "0px";
  button.style.margin = "0px";
  button.style.border = "0px solid gray";
  button.src = imgSrc;
  button.style.cursor = "pointer";
  if (O$.isExplorer() && (document.readyState != "complete")) {
    O$.addLoadEvent(addFunction);
  } else {
    addFunction();
  }
};

O$._createSplitterMouseSattelite = function(sidePanel, imgSrc, imgSrc_flipped, IEPngBugFixEnabled) {
  var addFunction = function () {
    O$._setImageAbsoluteCenterPosition(sidePanel._splitter, imgSrc);
    sidePanel._splitter.style.overflow = "visible";
    var mouseSattelite = sidePanel._splitter._mouseSattelite;
    mouseSattelite.onLoad = O$._refreshSplitterMouseSattelite(sidePanel);
    mouseSattelite._imgSrc = imgSrc;
    mouseSattelite._imgSrc_flipped = imgSrc_flipped;
    mouseSattelite._flipped = false;
    mouseSattelite._IEPngBugFixEnabled = IEPngBugFixEnabled;
    if ((sidePanel._alignment == "left") || (sidePanel._alignment == "right")) {
      mouseSattelite._fixedX = true;
    } else {
      mouseSattelite._fixedY = true;
    }
    mouseSattelite._sidePanelOncollapse = sidePanel.oncollapse;
    sidePanel.oncollapse = function() {
      O$._mouseSatteliteHide(mouseSattelite);
      O$._flipSplitterMouseSattelite(sidePanel);
      mouseSattelite._sidePanelOncollapse();
    };
    mouseSattelite._sidePanelOnrestore = sidePanel.onrestore;
    sidePanel.onrestore = function() {
      O$._mouseSatteliteHide(mouseSattelite);
      O$._flipSplitterMouseSattelite(sidePanel);
      mouseSattelite._sidePanelOnrestore();
    };
    mouseSattelite.onmouseover = O$._mouseSatteliteHide(mouseSattelite);
  };
  if (O$.isExplorer() && (document.readyState != "complete")) {
    O$.addLoadEvent(addFunction);
  } else {
    addFunction();
  }
};

O$._refreshSplitterMouseSattelite = function(sidePanel) {
  var mouseSattelite = sidePanel._splitter._mouseSattelite;
  if (mouseSattelite.readyState == "loading") {
    setTimeout(O$._refreshSplitterMouseSattelite(sidePanel), 100);
  } else {
    var alignment = sidePanel._alignment;
    var flipped = mouseSattelite._flipped;
    if ((alignment == "left" && !flipped) || (alignment == "right" && flipped)) {
      mouseSattelite._posX = - mouseSattelite.offsetWidth;
      mouseSattelite._shiftX = - 10;
      mouseSattelite._shiftY = - Math.round(mouseSattelite.offsetHeight / 2);
    } else if ((alignment == "left" && flipped) || (alignment == "right" && !flipped)) {
      mouseSattelite._posX = sidePanel._splitter.offsetWidth;
      mouseSattelite._shiftX = + 10;
      mouseSattelite._shiftY = - Math.round(mouseSattelite.offsetHeight / 2);
    } else if ((sidePanel._alignment == "top" && !flipped) || (sidePanel._alignment == "bottom" && flipped)) {
      mouseSattelite._posY = - mouseSattelite.offsetHeight;
      mouseSattelite._shiftX = - Math.round(mouseSattelite.offsetWidth / 2);
      mouseSattelite._shiftY = + 10;
    } else {
      mouseSattelite._posY = sidePanel._splitter.offsetHeight;
      mouseSattelite._shiftX = - Math.round(mouseSattelite.offsetWidth / 2);
      mouseSattelite._shiftY = - 10;
    }
    if (mouseSattelite._IEPngBugFixEnabled)
      O$._bugFix_IEPng(mouseSattelite);
  }
};

O$._flipSplitterMouseSattelite = function(sidePanel) {
  var mouseSattelite = sidePanel._splitter._mouseSattelite;
  mouseSattelite._flipped = !mouseSattelite._flipped;
  if (mouseSattelite._flipped) {
    mouseSattelite.src = mouseSattelite._imgSrc_flipped;
  } else {
    mouseSattelite.src = mouseSattelite._imgSrc;
  }
  O$._refreshSplitterMouseSattelite(sidePanel);
};
