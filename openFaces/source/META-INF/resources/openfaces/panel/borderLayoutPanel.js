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

//--------------------------------------  BorderLayoutPanel init method

O$._initBorderLayoutPanel = function(borderLayoutPanelId) {
  var borderLayoutPanel = O$.initComponent(borderLayoutPanelId, null, {
    _isBorderLayoutPanel: true,
    _truncateMode: false,
    _isNotResizableComponent: true,
    _newStyle: new O$._createPseudoCSSStyle()
  });
  borderLayoutPanel._isCoupled = borderLayoutPanel.parentNode._isCouplingElement;
  borderLayoutPanel._newStyle.width = borderLayoutPanel.style.width;
  borderLayoutPanel._newStyle.height = borderLayoutPanel.style.height;

  O$._storeSizeProperties(borderLayoutPanel);
  O$._subscribeToOnresizeEvent(borderLayoutPanel, function() {
    borderLayoutPanel._newStyle.applyTo(borderLayoutPanel.style);
    if (!borderLayoutPanel._waitForRefresh) {
      borderLayoutPanel._waitForRefresh = true;
      setTimeout(O$._refreshLaterIfInvisible, 0, borderLayoutPanel);
    }
  });

  borderLayoutPanel.style.width = O$._calculateNumericWidth(borderLayoutPanel, true) + "px";
  borderLayoutPanel.style.height = O$._calculateNumericHeight(borderLayoutPanel, true) + "px";

  if (borderLayoutPanel._isCoupled) {
    var borderLayoutPanelLeft = O$.getNumericElementStyle(borderLayoutPanel, "left");
    if (!borderLayoutPanelLeft) {
      borderLayoutPanelLeft = 0;
    }
    var borderLayoutPanelTop = O$.getNumericElementStyle(borderLayoutPanel, "top");
    if (!borderLayoutPanelTop) {
      borderLayoutPanelTop = 0;
    }
    O$._setInnerElementOuterLeftTopCorner(borderLayoutPanel, borderLayoutPanelLeft, borderLayoutPanelTop, false);
    borderLayoutPanel.style.position = "absolute";
    borderLayoutPanel.parentNode.style.overflow = "hidden";
  } else {
    O$._checkAsRootDoubleBufferedElement(borderLayoutPanel);
  }
};

O$._initBorderLayoutPanel_content = function(borderLayoutPanelId, rolloverClass, events) {
  var borderLayoutPanel = O$(borderLayoutPanelId);
  borderLayoutPanel._content = O$(borderLayoutPanelId + "::content");
  borderLayoutPanel._content._newStyle = new O$._createPseudoCSSStyle();
  borderLayoutPanel._content._isResizableElement = true;
  borderLayoutPanel._content._isCouplingElement = true;
  borderLayoutPanel._content._isCoupled = true;

  O$._storeSizeProperties(borderLayoutPanel._content);
  O$._setupRolloverClass(borderLayoutPanel, rolloverClass);
  borderLayoutPanel.refresh = function() {
    O$._recalculateBorderLayoutPanel(borderLayoutPanel);
    O$._repaintBorderLayoutPanel(borderLayoutPanel);
  };

  // process SidePanel's
  borderLayoutPanel.sidePanels = [];
  var nodeList = borderLayoutPanel.childNodes;
  for (var index = 0; index < nodeList.length; index++) {
    var sidePanel = nodeList[index];
    if (sidePanel._alignment) {
      sidePanel._blockSelfRepaint = true;
      sidePanel._isCoupled = true;
      sidePanel._forBorderLayoutPanel = {};
      borderLayoutPanel.sidePanels.push(sidePanel);

      sidePanel._forBorderLayoutPanel.onsplitterdrag = sidePanel.onsplitterdrag;
      sidePanel.onsplitterdrag = function(curSidePanel) {
        if (curSidePanel._forBorderLayoutPanel.onsplitterdrag)
          curSidePanel._forBorderLayoutPanel.onsplitterdrag(curSidePanel);
        if (curSidePanel._forBorderLayoutPanel._truncateMode) {
          curSidePanel._forBorderLayoutPanel._truncateMode = false;
        } else {
          O$._setCorrectedSidePanelNewSize(curSidePanel, borderLayoutPanel); // compare to free space into borderLayoutPanel
        }
        O$._recalculateBorderLayoutPanel(borderLayoutPanel);
        O$._repaintBorderLayoutPanel(borderLayoutPanel);
      };
      sidePanel._forBorderLayoutPanel.oncollapse = sidePanel.oncollapse;
      sidePanel.oncollapse = function(sidePanel) {
        if (sidePanel._forBorderLayoutPanel.oncollapse)
          sidePanel._forBorderLayoutPanel.oncollapse(sidePanel);
        borderLayoutPanel.refresh();
      };
      sidePanel._forBorderLayoutPanel.onrestore = sidePanel.onrestore;
      sidePanel.onrestore = function(sidePanel) {
        if (sidePanel._forBorderLayoutPanel.onrestore)
          sidePanel._forBorderLayoutPanel.onrestore(sidePanel);
        borderLayoutPanel.refresh();
      };
    }
  }
  O$._calculateBorderLayoutPanelContentRect(borderLayoutPanel, true);
  borderLayoutPanel.refresh();
  if (O$.isExplorer())
    setTimeout(function() {
      borderLayoutPanel.refresh();
    }, 1);
  O$._applyEventsObjectToElement(events, borderLayoutPanel);
  setTimeout(O$._refreshLaterIfInvisible, 1000, borderLayoutPanel);
};

//--------------------------------------  private functions

O$._recalculateBorderLayoutPanel = function(borderLayoutPanel) {
  // borderLayoutPanel
  if (borderLayoutPanel._isCoupled) {
    var borderLayoutPanelOuterWidth = O$._calculateNumericInnerWidth(borderLayoutPanel.parentNode, false);
    var borderLayoutPanelOuterHeight = O$._calculateNumericInnerHeight(borderLayoutPanel.parentNode, false);
    O$._setInnerElementOuterWidth(borderLayoutPanel, borderLayoutPanelOuterWidth, false);
    O$._setInnerElementOuterHeight(borderLayoutPanel, borderLayoutPanelOuterHeight, false);
    O$._bugFix_divNegativeSizeBug(borderLayoutPanel, false);
  } else {
    borderLayoutPanel.style.width = O$._calculateNumericWidth(borderLayoutPanel, true) + "px";
    borderLayoutPanel.style.height = O$._calculateNumericHeight(borderLayoutPanel, true) + "px";
  }

  // sidePanels and content

  O$._calculateBorderLayoutPanelContentRect(borderLayoutPanel, false); //todo create full double buffering
  if (borderLayoutPanel._truncateMode || borderLayoutPanel._contentRect.width < 0 || borderLayoutPanel._contentRect.height < 0) {
    O$._truncateMode_recalculateBorderLayoutPanel(borderLayoutPanel, false);
    O$._calculateBorderLayoutPanelContentRect(borderLayoutPanel, false);
  }
  O$._recalculateInnerSidePanels(borderLayoutPanel);
  O$._recalculateContent(borderLayoutPanel);
};

O$._repaintBorderLayoutPanel = function(borderLayoutPanel) {
  if ((parseInt(borderLayoutPanel._content._newStyle.height) < parseInt(borderLayoutPanel._content.style.height)) ||
      (parseInt(borderLayoutPanel._content._newStyle.width) < parseInt(borderLayoutPanel._content.style.width))) {
    var isDecrase = true;
  }
  if (isDecrase) {
    O$._repaintContent(borderLayoutPanel);
    O$._repaintInnerSidePanels(borderLayoutPanel);
    //borderLayoutPanel._newStyle.applyTo(borderLayoutPanel.style);  //todo create full double buffering
  } else {
    //borderLayoutPanel._newStyle.applyTo(borderLayoutPanel.style);  
    O$._repaintInnerSidePanels(borderLayoutPanel);
    O$._repaintContent(borderLayoutPanel);
  }
};

O$._calculateBorderLayoutPanelContentRect = function(borderLayoutPanel, useDoubleBuffering) {
  var contentRectTop = 0;
  var contentRectLeft = 0;
  var contentRectRight = 0;
  var contentRectBottom = 0;
  for (var index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
    var sidePanel = borderLayoutPanel.sidePanels[index];
    if (sidePanel._alignment) {
      if (sidePanel._alignment == "left") {
        contentRectLeft = contentRectLeft + O$._calculateNumericSizeValue(sidePanel, sidePanel._size, useDoubleBuffering);
      } else if (sidePanel._alignment == "right") {
        contentRectRight = contentRectRight + O$._calculateNumericSizeValue(sidePanel, sidePanel._size, useDoubleBuffering);
      } else if (sidePanel._alignment == "top") {
        contentRectTop = contentRectTop + O$._calculateNumericSizeValue(sidePanel, sidePanel._size, useDoubleBuffering);
      } else if (sidePanel._alignment == "bottom") {
        contentRectBottom = contentRectBottom + O$._calculateNumericSizeValue(sidePanel, sidePanel._size, useDoubleBuffering);
      }
    }
  }
  var borderLayoutPanelWidth = O$._calculateNumericInnerWidth(borderLayoutPanel, useDoubleBuffering);
  var borderLayoutPanelHeight = O$._calculateNumericInnerHeight(borderLayoutPanel, useDoubleBuffering);
  borderLayoutPanel._contentRect = new O$.Rectangle(contentRectLeft,
          contentRectTop,
          borderLayoutPanelWidth - (contentRectLeft + contentRectRight),
          borderLayoutPanelHeight - (contentRectTop + contentRectBottom));
  return borderLayoutPanel._contentRect;
};

O$._recalculateInnerSidePanels = function(borderLayoutPanel, isDecrase) {
  var topSidePanelOffset = 0;
  var leftSidePanelOffset = 0;
  var rightSidePanelOffset = 0;
  var bottomSidePanelOffset = 0;
  var sidePanels = [];
  if (isDecrase) {
    for (var index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
      var sidePanel = borderLayoutPanel.sidePanels[index];
      if ((sidePanel._alignment == "left") || (sidePanel._alignment == "right"))
        sidePanels.push(sidePanel);
    }
    for (index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
      sidePanel = borderLayoutPanel.sidePanels[index];
      if ((sidePanel._alignment == "top") || (sidePanel._alignment == "bottom"))
        sidePanels.push(sidePanel);
    }
  } else {
    for (index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
      sidePanel = borderLayoutPanel.sidePanels[index];
      if ((sidePanel._alignment == "top") || (sidePanel._alignment == "bottom"))
        sidePanels.push(sidePanel);
    }
    for (index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
      sidePanel = borderLayoutPanel.sidePanels[index];
      if ((sidePanel._alignment == "left") || (sidePanel._alignment == "right"))
        sidePanels.push(sidePanel);
    }
  }
  for (index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
    sidePanel = borderLayoutPanel.sidePanels[index];
    if (sidePanel._alignment) {
      if (sidePanel._alignment == "left") {
        O$._setInnerElementOuterLeftTopCorner(sidePanel, leftSidePanelOffset, borderLayoutPanel._contentRect.y, true);
        sidePanel._newStyle.height = borderLayoutPanel._contentRect.height + "px";
        leftSidePanelOffset = leftSidePanelOffset + O$._calculateNumericSizeValue(sidePanel, sidePanel._size, false);
      } else if (sidePanel._alignment == "right") {
        O$._setInnerElementOuterRightTopCorner(sidePanel, rightSidePanelOffset, borderLayoutPanel._contentRect.y, true);
        sidePanel._newStyle.height = borderLayoutPanel._contentRect.height + "px";
        rightSidePanelOffset = rightSidePanelOffset + O$._calculateNumericSizeValue(sidePanel, sidePanel._size, false);
      } else if (sidePanel._alignment == "top") {
        O$._setInnerElementOuterLeftTopCorner(sidePanel, 0, topSidePanelOffset, true);
        sidePanel._newStyle.width = O$._calculateNumericInnerWidth(borderLayoutPanel, false) + "px";
        topSidePanelOffset = topSidePanelOffset + O$._calculateNumericSizeValue(sidePanel, sidePanel._size, false);
      } else if (sidePanel._alignment == "bottom") {
        O$._setInnerElementOuterRightBottomCorner(sidePanel, 0, bottomSidePanelOffset, true);
        sidePanel._newStyle.width = O$._calculateNumericInnerWidth(borderLayoutPanel, false) + "px";
        bottomSidePanelOffset = bottomSidePanelOffset + O$._calculateNumericSizeValue(sidePanel, sidePanel._size, false);
      }
      O$._recalculateSidePanel(sidePanel);
    }
  }
};

O$._recalculateContent = function(borderLayoutPanel) {
  O$._setInnerElementOuterLeftTopCorner(borderLayoutPanel._content, borderLayoutPanel._contentRect.x, borderLayoutPanel._contentRect.y, true);
  O$._setInnerElementOuterWidth(borderLayoutPanel._content, borderLayoutPanel._contentRect.width, true);
  O$._setInnerElementOuterHeight(borderLayoutPanel._content, borderLayoutPanel._contentRect.height, true);
  O$._bugFix_divNegativeSizeBug(borderLayoutPanel._content, true);
};

O$._repaintInnerSidePanels = function(borderLayoutPanel) {
  for (var index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
    var sidePanel = borderLayoutPanel.sidePanels[index];
    O$._repaintSidePanel(sidePanel);
    if (sidePanel._forBorderLayoutPanel._truncateMode) {
      O$._sidePanelSaveState(sidePanel, sidePanel._forBorderLayoutPanel._beforeTruncationSize + ";" + sidePanel._collapsed);
    }
  }
};

O$._repaintContent = function(borderLayoutPanel) {
  borderLayoutPanel._content._newStyle.applyTo(borderLayoutPanel._content.style);
  O$._sendResizeEvent(borderLayoutPanel._content);
  if (borderLayoutPanel.oncontentresize)
    borderLayoutPanel.oncontentresize();
};

O$._setCorrectedSidePanelNewSize = function(sidePanel, borderLayoutPanel) {
  var sidePanelSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._size, true);
  if (sidePanel._alignment == "left" || sidePanel._alignment == "right") {
    var oldSize = O$._calculateNumericWidth(sidePanel, true);
    var freeSpace = borderLayoutPanel._contentRect.width - (sidePanelSize - oldSize);
  }
  else {
    oldSize = O$._calculateNumericHeight(sidePanel, true);
    freeSpace = borderLayoutPanel._contentRect.height - (sidePanelSize - oldSize);
  }
  if (freeSpace < 0) {
    if (O$._isPercentageValue(sidePanel._size)) {
      if (sidePanel._alignment == "left" || sidePanel._alignment == "right") {
        var sizeFactor = O$._calculateNumericWidthFactor(sidePanel);
      }
      else {
        sizeFactor = O$._calculateNumericHeightFactor(sidePanel);
      }
      sidePanel._size = ((sidePanelSize + freeSpace) * 100.0 / sizeFactor) + "%";
    }
    else {
      sidePanel._size = parseInt(sidePanel._size) + freeSpace + "px";
    }
  }
};

//--------------------------------------  truncated mode functions
O$._truncateMode_recalculateBorderLayoutPanel = function(borderLayoutPanel, useDoubleBuffering) {
  if (borderLayoutPanel._truncateMode_Vert || borderLayoutPanel._contentRect.width < 0) {
    borderLayoutPanel._truncateMode_Vert = O$._truncateMode_recalculateVerticalSidePanels(borderLayoutPanel, useDoubleBuffering);
  }
  if (borderLayoutPanel._truncateMode_Hor || borderLayoutPanel._contentRect.height < 0) {
    borderLayoutPanel._truncateMode_Hor = O$._truncateMode_recalculateHorizontalSidePanels(borderLayoutPanel, useDoubleBuffering);
  }
  borderLayoutPanel._truncateMode = borderLayoutPanel._truncateMode_Vert || borderLayoutPanel._truncateMode_Hor;
};

O$._truncateMode_recalculateVerticalSidePanels = function(borderLayoutPanel, useDoubleBuffering) {
  var freeSpace = borderLayoutPanel._contentRect.width;
  if (!borderLayoutPanel._truncateMode_forceVert) borderLayoutPanel._truncateMode_forceVert = 2;
  var force = borderLayoutPanel._truncateMode_forceVert;

  if (freeSpace == 0) {
    return borderLayoutPanel._truncateMode_Vert;
  }
  // truncate sidePanels
  if (freeSpace < 0) {
    var sidePanels = O$._truncateMode_collectSidePanelsSortedList(borderLayoutPanel, true, true);
    for (var index = 0; (freeSpace < 0) && (index < sidePanels.length); index++) {
      var sidePanel = sidePanels[index];
      if (sidePanel._forBorderLayoutPanel._truncateModePriority > force)
        break;
      freeSpace = O$._truncateMode_truncateSidePanel(sidePanel, freeSpace, useDoubleBuffering, force);
    }

    if ((freeSpace < 0) && (force < 8)) { // increase truncation force
      O$._calculateBorderLayoutPanelContentRect(borderLayoutPanel, false); //todo create full double buffering
      borderLayoutPanel._truncateMode_forceVert = borderLayoutPanel._truncateMode_forceVert + 2;
      return O$._truncateMode_recalculateVerticalSidePanels(borderLayoutPanel, useDoubleBuffering);
    }
    return true;
  }
  // untruncate sidePanels
  else if (freeSpace > 0) {
    sidePanels = O$._truncateMode_collectSidePanelsSortedList(borderLayoutPanel, true, false);
    for (index = sidePanels.length - 1; (freeSpace > 0) && (index >= 0); index--) {
      sidePanel = sidePanels[index];
      if (sidePanel._forBorderLayoutPanel._truncateModePriority < (force - 1))
        break;
      freeSpace = O$._truncateMode_untruncateSidePanel(sidePanel, freeSpace, useDoubleBuffering, force);
    }
    if ((freeSpace > 0) && (force > 2)) { // reduce truncation force
      O$._calculateBorderLayoutPanelContentRect(borderLayoutPanel, false);
      borderLayoutPanel._truncateMode_forceVert = borderLayoutPanel._truncateMode_forceVert - 2;
      return O$._truncateMode_recalculateVerticalSidePanels(borderLayoutPanel, useDoubleBuffering);
    }
    return !(freeSpace > 0);
  }
  return undefined;
};

O$._truncateMode_recalculateHorizontalSidePanels = function(borderLayoutPanel, useDoubleBuffering) {
  var freeSpace = borderLayoutPanel._contentRect.height;
  if (!borderLayoutPanel._truncateMode_forceHor) borderLayoutPanel._truncateMode_forceHor = 2;
  var force = borderLayoutPanel._truncateMode_forceHor;

  if (freeSpace == 0) {
    return borderLayoutPanel._truncateMode_Hor;
  }
  // truncate sidePanels
  if (freeSpace < 0) {
    var sidePanels = O$._truncateMode_collectSidePanelsSortedList(borderLayoutPanel, false, true);
    for (var index = 0; (freeSpace < 0) && (index < sidePanels.length); index++) {
      var sidePanel = sidePanels[index];
      if (sidePanel._forBorderLayoutPanel._truncateModePriority > force)
        break;
      freeSpace = O$._truncateMode_truncateSidePanel(sidePanel, freeSpace, useDoubleBuffering);
    }

    if ((freeSpace < 0) && (force < 8)) { // increase truncation force
      O$._calculateBorderLayoutPanelContentRect(borderLayoutPanel, false); //todo create full double buffering
      borderLayoutPanel._truncateMode_forceHor = borderLayoutPanel._truncateMode_forceHor + 2;
      return O$._truncateMode_recalculateHorizontalSidePanels(borderLayoutPanel, useDoubleBuffering);
    }
    return true;
  }
  // untruncate sidePanels
  else if (freeSpace > 0) {
    sidePanels = O$._truncateMode_collectSidePanelsSortedList(borderLayoutPanel, false, false);
    for (index = sidePanels.length - 1; (freeSpace > 0) && (index >= 0); index--) {
      sidePanel = sidePanels[index];
      if (sidePanel._forBorderLayoutPanel._truncateModePriority < (force - 1))
        break;
      freeSpace = O$._truncateMode_untruncateSidePanel(sidePanel, freeSpace, useDoubleBuffering, force);
    }
    if ((freeSpace > 0) && (force > 2)) { // reduce truncation force
      O$._calculateBorderLayoutPanelContentRect(borderLayoutPanel, false);
      borderLayoutPanel._truncateMode_forceHor = borderLayoutPanel._truncateMode_forceHor - 2;
      return O$._truncateMode_recalculateHorizontalSidePanels(borderLayoutPanel, useDoubleBuffering);
    }
    return !(freeSpace > 0);
  }
  return undefined;
};

O$._truncateMode_collectSidePanelsSortedList = function(borderLayoutPanel, isVertDirection, isTruncateOp) {
  // make sorted sidePanels array to truncate it
  for (var index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
    var sidePanel = borderLayoutPanel.sidePanels[index];
    if ((isVertDirection && ((sidePanel._alignment == "top") || (sidePanel._alignment == "bottom")))
            || (!isVertDirection && ((sidePanel._alignment == "left") || (sidePanel._alignment == "right")))) {
      sidePanel._forBorderLayoutPanel._truncateModePriority = 0;
      continue;
    }
    sidePanel._forBorderLayoutPanel._truncateModePriority = O$._truncateMode_calculateSidePanelPriority(sidePanel, isTruncateOp);
  }

  var sidePanels = [];
  for (var priority = 1; priority < 14; priority++) {
    for (index = 0; index < borderLayoutPanel.sidePanels.length; index++) {
      sidePanel = borderLayoutPanel.sidePanels[index];
      if (sidePanel._forBorderLayoutPanel._truncateModePriority == priority) {
        sidePanels.push(sidePanel);
      }
    }
  }

  return sidePanels;
};

O$._truncateMode_calculateSidePanelPriority = function(sidePanel, isTruncateOp) {
  var sidePanelSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._size);
  var sidePanelMinSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._minSize);
  var sidePanelCollapsedSize = parseInt(sidePanel._collapsedSize);
  var priority = 0;
  if ((sidePanel._alignment == "left") || (sidePanel._alignment == "bottom")) {
    priority = priority + 1;
  }
  else if ((sidePanel._alignment == "right") || (sidePanel._alignment == "top")) {
    priority = priority + 2;
  }
  if (isTruncateOp) {
    if (sidePanelSize <= sidePanelCollapsedSize) {
      priority = priority + 4;
    } else if (!sidePanel._resizable) {
      priority = priority + 2;
    } else if (sidePanelSize <= sidePanelMinSize) {
      priority = priority + 2;
    }
  } else {
    if (sidePanelSize < sidePanelCollapsedSize) {
      priority = priority + 4;
    } else if (!sidePanel._resizable) {
      priority = priority + 2;
    } else if (sidePanelSize < sidePanelMinSize) {
      priority = priority + 2;
    }
  }
  if (document._nowResizingSidePanel == sidePanel) {
    priority = priority + 7;
  }
  return priority;
};

O$._truncateMode_truncateSidePanel = function(sidePanel, freeSpace, useDoubleBuffering, force) {
  var needSpace = -freeSpace;
  var sidePanelSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._size, useDoubleBuffering);

  if (force <= 2) {
    var truncationSize = sidePanelSize - O$._calculateNumericSizeValue(sidePanel, sidePanel._minSize, useDoubleBuffering);
  } else if (force <= 4) {
    truncationSize = sidePanelSize - parseInt(sidePanel._collapsedSize);
  } else {
    truncationSize = sidePanelSize;
  }

  if (truncationSize > needSpace) {
    truncationSize = needSpace;
  }

  if (!sidePanel._forBorderLayoutPanel._truncateMode) {
    sidePanel._forBorderLayoutPanel._beforeTruncationSize = sidePanel._size;
  }
  sidePanel._size = (sidePanelSize - truncationSize) + "px";
  sidePanel._forBorderLayoutPanel._truncateMode = true;
  return -(needSpace - truncationSize);
};

O$._truncateMode_untruncateSidePanel = function(sidePanel, freeSpace, useDoubleBuffering, force) {
  if (sidePanel._forBorderLayoutPanel._truncateMode) {
    var sidePanelSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._size, useDoubleBuffering);
    if (force <= 2) {
      var untruncationSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._forBorderLayoutPanel._beforeTruncationSize, useDoubleBuffering) - sidePanelSize;
    } else if (force <= 4) {
      if (!sidePanel._resizable) {
        untruncationSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._minSize, useDoubleBuffering) - sidePanelSize;
      } else {
        untruncationSize = O$._calculateNumericSizeValue(sidePanel, sidePanel._forBorderLayoutPanel._beforeTruncationSize, useDoubleBuffering) - sidePanelSize;
      }
    } else {
      untruncationSize = parseInt(sidePanel._collapsedSize) - sidePanelSize;
    }

    if (untruncationSize > freeSpace) {
      untruncationSize = freeSpace;
      sidePanel._size = (O$._calculateNumericSizeValue(sidePanel, sidePanel._size, useDoubleBuffering) + untruncationSize) + "px";
      return 0;
    } else {
      if (force <= 2) {
        sidePanel._forBorderLayoutPanel._truncateMode = false;
        sidePanel._size = sidePanel._forBorderLayoutPanel._beforeTruncationSize;
      } else {
        sidePanel._size = (O$._calculateNumericSizeValue(sidePanel, sidePanel._size, useDoubleBuffering) + untruncationSize) + "px";
      }
      return (freeSpace - untruncationSize);
    }
  }
  return freeSpace;
};

O$._refreshLaterIfInvisible = function(borderLayoutPanel) {

  var hasHiddenParent = false;
  var currentElement = borderLayoutPanel;
  var hasDocumentParent = O$.isElementPresentInDocument(currentElement);

  hasHiddenParent = !O$.isVisibleRecursive(currentElement);

  if (hasHiddenParent == true) {
    setTimeout(O$._refreshLaterIfInvisible, 200, borderLayoutPanel);
  } else {
    borderLayoutPanel.refresh();
    borderLayoutPanel._waitForRefresh = false;
  }

}
