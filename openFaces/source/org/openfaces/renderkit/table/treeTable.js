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

// -------------------------- INITIALIZATION

O$.TreeTable = {
  _initTreeTableAPI: function(table) {
    table.__getSelectedNodeCount = function () {
      var selectedItems = this._getSelectedItems();
      if (!selectedItems || (selectedItems.length == 1 && selectedItems[0] == -1))
        return 0;
      return selectedItems.length;
    };
    table.__setSelectedNodeIndexes = function (nodeIndexes) {
      this.__setSelectedRowIndexes(nodeIndexes);
    };
    table.__setSelectedNodeIndex = function (nodeIndex) {
      this.__setSelectedRowIndex(nodeIndex);
    };
    table.isSelectionEmpty = function() {
      return this.__isSelectionEmpty();
    };
    table.getSelectedNodeCount = function() {
      return this.__getSelectedNodeCount();
    };
    table._of_treeTableComponentMarker = true;
  },

  // -------------------------- FOLDING SUPPORT

  _initFolding: function(tableId, toggleClassName, clientFoldingParams) {
    var table = O$(tableId);

    var rowIndexToChildCount = clientFoldingParams ? clientFoldingParams[0] : null;
    var treeColumnExpansionDatas = clientFoldingParams ? clientFoldingParams[1] : null;

    table._rowIndexToChildCount = rowIndexToChildCount;
    table._foldingMode = clientFoldingParams ? "client" : "server";
    table._treeColumnExpansionDatas = treeColumnExpansionDatas;
    table._toggleClassName = toggleClassName;

    table._updateRowVisibility = function() {
      var rootNodeCount = this._rowIndexToChildCount["root"];
      var rows = table.body._getRows();
      var rowIndexToChildCount = this._rowIndexToChildCount;
      this._styleRecalculationOnNodeExpansionNeeded = !!this._params.rowStyles.bodyOddRow;
      var result = O$.TreeTable._processRowVisibility(rows, rowIndexToChildCount, 0, rootNodeCount, true);
      if (this._styleRecalculationOnNodeExpansionNeeded) {
        var rowIndex, count, visibleRows, row;
        for (rowIndex = 0, count = rows.length, visibleRows = 0; rowIndex < count; rowIndex++) {
          row = rows[rowIndex];
          if (!row._isVisible())
            continue;
          row._notifyRowMoved(visibleRows++);
        }
        for (rowIndex = 0, count = rows.length, visibleRows = 0; rowIndex < count; rowIndex++) {
          row = rows[rowIndex];
          if (!row._isVisible())
            continue;
          // _updateStyle is used for two reasons:
          // - show selection style if selected row was previously in an invisible branch
          // - workaround for IE issue: when custom row style is applied to parent rows and a node is expanded, spacing between cells becomes white after node expansion
          row._updateStyle();
        }

      }
      return result;
    };

    table._updateExpandedNodesField = function() {
      var fieldId = this.id + "::expandedNodes";
      var field = O$(fieldId);
      var expandedRowIndexes = "";
      var rows = this.body._getRows();
      for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
        var row = rows[rowIndex];
        var expanded = row._toggled;
        if (expanded) {
          if (expandedRowIndexes.length > 0)
            expandedRowIndexes += ",";
          expandedRowIndexes += rowIndex;
        }
      }
      field.value = expandedRowIndexes;
    };

    var rows = table.body._getRows();
    for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
      var row = rows[rowIndex];
      O$.TreeTable._initRow(row);
    }
    if (table._foldingMode == "client") {
      table._updateRowVisibility();
      table._updateExpandedNodesField();
    }

    table._onKeyboardNavigation = function(e) {
      var selectedItems = this._getSelectedItems();
      if (selectedItems.length != 1)
        return true;
      var selectedRowIndex = selectedItems[0];
      if (selectedRowIndex == -1)
        return true;
      var bodyRows = this.body._getRows();
      var row = bodyRows[selectedRowIndex];
      if (!row._hasChildren)
        return true;
      if ((e.rightPressed || e.plusPressed) && !row._toggled) {
        row._setExpanded(true);
        return false;
      }
      if ((e.leftPressed || e.minusPressed) && row._toggled) {
        row._setExpanded(false);
        return false;
      }

      return true;
    };
  },

  _processRowVisibility: function(rows, rowIndexToChildCount, rowIndex, rowCount, currentLevelVisible) {
    for (var i = 0; i < rowCount; i++) {
      var row = rows[rowIndex];
      O$.assert(row, "processRowVisibility: rowIndex == " + rowIndex);
      row._setVisible(currentLevelVisible);
      if (!row._table._styleRecalculationOnNodeExpansionNeeded) {
        if (currentLevelVisible && O$.isExplorer()) { // workaround for IE issue: when custom row style is applied to parent rows and a node is expanded, spacing between cells becomes white after node expansion
          row._updateStyle();
        }
      }
      var childCount = rowIndexToChildCount[rowIndex];
      if (childCount) {
        row._hasChildren = true;
        row._childrenLoaded = (childCount != "?");
        var nextLevelVisible = currentLevelVisible && row._toggled;
        if (row._childrenLoaded) {
          rowIndex = O$.TreeTable._processRowVisibility(rows, rowIndexToChildCount, rowIndex + 1, childCount, nextLevelVisible);
        } else
          rowIndex++;
      } else {
        row._hasChildren = false;
        rowIndex++;
      }
    }
    return rowIndex;
  },

  _initRow: function(row) {
    var table = row._table;
    var rowIndex = row._index;

    row._setExpanded = function(expanded) { // _setExpanded(expaned) may not be able to set the "expanded" state to true in some cases. Invokers need to check _isExpanded() to see if expansion was successful
      if (row._childrenEmpty)
        return;
      if (this._toggled == expanded)
        return;
      var rowTable = this._table;
      var prevExpanded = this._toggled;
      this._toggled = expanded;
      var thisRow = this;

      function changeToggleImage(showExpandedImage) {
        var toggles = thisRow._toggles;
        for (var toggleIndex = 0, toggleCount = toggles.length; toggleIndex < toggleCount; toggleIndex++) {
          var toggle = toggles[toggleIndex];
          var treeColExpansionData = rowTable._treeColumnExpansionDatas[toggleIndex];
          toggle.src = showExpandedImage ? treeColExpansionData[0] : treeColExpansionData[1];
        }
      }

      changeToggleImage(expanded);
      rowTable._updateExpandedNodesField();
      if (expanded && !this._childrenLoaded) {
        if (rowTable._useAjax) {
          var ajaxFailedProcessor = function() {
            thisRow._toggled = prevExpanded;
            changeToggleImage(prevExpanded);
            rowTable._updateExpandedNodesField();
          };
          if (O$._ajaxRequestScheduled || O$._ajax_request_processing)
            ajaxFailedProcessor();
          else
            O$.requestComponentPortions(rowTable.id, ["subRows:" + this._index], null, O$.TreeTable._subRowsLoaded, ajaxFailedProcessor);
        } else
          O$.submitFormWithAdditionalParam(rowTable, rowTable.id + "::toggleExpansion", this._index);
      } else {
        rowTable._updateRowVisibility();
      }
    };
    row._isExpanded = function() {
      return this._toggled;
    };
    var expandedToggles = O$.findChildNodesByClass(row, "o_toggle_e");
    var collapsedToggles = O$.findChildNodesByClass(row, "o_toggle_c");
    var expanded = expandedToggles && expandedToggles.length > 0;
    O$.assert(expandedToggles.length == 0 || collapsedToggles.length == 0, "A row can't contain both expanded and collapsed nodes. rowIndex = " + rowIndex + "; table id = " + table.id);
    var toggles = expanded ? expandedToggles : collapsedToggles;
    row._toggles = toggles;
    row._hasChildren = toggles && toggles.length > 0;

      if (row._hasChildren) {
        row._toggled = expanded;
      }

    for (var toggleIndex = 0, toggleCount = toggles.length; toggleIndex < toggleCount; toggleIndex++) {
      var toggle = toggles[toggleIndex];
      toggle.className = table._toggleClassName;
      toggle._rowIndex = rowIndex;
      toggle._row = row;
      toggle.onclick = function(e) {
        var evt = O$.getEvent(e);
        var clickedRow = this._row;
        var newExpanded = !clickedRow._toggled;
        clickedRow._setExpanded(newExpanded);
        evt.cancelBubble = true;
        if (table._focusable) {
          table._preventPageScrolling = true;
          table.focus();
          table._preventPageScrolling = false;
        }
        evt.cancelBubble = true;
        return false;
      };
      toggle.ondblclick = function(e) {
        O$.repeatClickOnDblclick.apply(this, [e]);
        O$.cancelBubble(e);
      };
    }
  },

  _subRowsLoaded: function(treeTable, portionName, portionHTML, portionScripts) {
    var sepIdx = portionName.indexOf(":");
    var indexStr = portionName.substring(sepIdx + 1);
    var parentRowIndex = eval(indexStr);

    var tempDiv = document.createElement("div");
    tempDiv.style.display = "none";
    tempDiv.innerHTML = "<table><tbody>" + portionHTML + "</tbody></table>";
    var tableBody = tempDiv.getElementsByTagName("tbody")[0];
    var children = tableBody.childNodes;
    var newNodes = [];
    for (var childIndex = 0, childCount = children.length; childIndex < childCount; childIndex++) {
      var child = children[childIndex];
      newNodes.push(child);
    }

    var newRows = [];
    for (var nodeIndex = 0, nodeCount = newNodes.length; nodeIndex < nodeCount; nodeIndex++) {
      var newNode = newNodes[nodeIndex];
      var prnt = newNode.parentNode;
      prnt.removeChild(newNode);
      if (!newNode || !newNode.tagName)
        continue;
      var tagName = newNode.tagName.toLowerCase();
      if (tagName == "tr") {
        newRows.push(newNode);
      }
    }

    treeTable.__parentRowIndex = parentRowIndex;
    var scrolling = treeTable._params._scrolling;
    if (scrolling) {
      var compositeRows = [];
      var i = 0, count = newRows.length;
      while (i < count) {
        var leftRowNode = scrolling.leftFixedCols ? newRows[i++] : null;
        var centerRowNode = newRows[i++];
        var rightRowNode = scrolling.rightFixedCols ? newRows[i++] : null;

        compositeRows.push({
          _leftRowNode: leftRowNode,
          _rowNode: centerRowNode,
          _rightRowNode: rightRowNode
        });
      }
      newRows = compositeRows;
    }
    treeTable.__newRows = newRows;
    O$.executeScripts(portionScripts);
  },

  _insertSubrows: function(treeTableId, subRowsData) {
    var treeTable = O$(treeTableId);
    var newRows = treeTable.__newRows;
    var parentRowIndex = treeTable.__parentRowIndex;
    var newRowIndexToChildCount = subRowsData[0];
    var newRowsToStylesMap = subRowsData[1];
    var newRowCellsToStylesMap = subRowsData[2];

    treeTable._insertRowsAfter(parentRowIndex, newRows, newRowsToStylesMap, newRowCellsToStylesMap);

    var rows = treeTable.body._getRows();
    var parentRow = rows[parentRowIndex];

    if (newRows == null || newRows.length == 0) {
      parentRow._childrenEmpty = true;
      var toggles = parentRow._toggles;
      for (var toggleIndex = 0, toggleCount = toggles.length; toggleIndex < toggleCount; toggleIndex++) {
        var toggle = toggles[toggleIndex];
        toggle.style.visibility = "hidden";
        toggle.className = "";
      }
    }
    parentRow._childrenLoaded = true;
    var addedRowCount = newRows.length;
    var i;
    for (i = 0; i < addedRowCount; i++) {
      var newRow = newRows[i];
      O$.TreeTable._initRow(newRow);
    }

    var rowIndexToChildCount = treeTable._rowIndexToChildCount;
    var newRowIndex;
    for (var rowIndex = rows.length - 1 - addedRowCount; rowIndex > parentRowIndex; rowIndex--) {
      if (rowIndexToChildCount[rowIndex] != undefined) {
        newRowIndex = rowIndex + addedRowCount;
        rowIndexToChildCount[newRowIndex] = rowIndexToChildCount[rowIndex];
        rowIndexToChildCount[rowIndex] = undefined;
      }
    }
    rowIndexToChildCount[parentRowIndex] = newRowIndexToChildCount[0];
    for (i = 0; i < addedRowCount; i++) {
      newRowIndex = parentRowIndex + 1 + i;
      rowIndexToChildCount[newRowIndex] = newRowIndexToChildCount[i + 1];
    }

    treeTable._updateExpandedNodesField();
  },

  _setSelectedNodeIndexes: function(treeTableId, selectedNodeIndexes) {
    if (!treeTableId)
      throw "O$._setSelectedNodeIndexes: treeTable's clientId must be passed as a parameter";
    var table = O$(treeTableId);
    if (!table)
      throw "O$._setSelectedNodeIndexes: Invalid clientId passed - no such component was found: " + treeTableId;
    if (!table._of_treeTableComponentMarker)
      throw "O$._setSelectedNodeIndexes: The clientId passed refers to a component other than <o:treeTable> : " + treeTableId;
    if (!table._multipleSelectionAllowed)
      throw "O$._setSelectedNodeIndexes: The table is not set up for multiple selection. Table's clientId is: " + treeTableId;
    if (!selectedNodeIndexes) {
      throw "O$._setSelectedNodeIndexes: Invalid selectedNodeIndexes passed : " + treeTableId;
    }
    var selectedIndexes = [];
    for (var i = 0; i < selectedNodeIndexes.length; i++) {
      if (selectedNodeIndexes[i] > -1) {
        selectedIndexes.push(selectedNodeIndexes[i]);
      }
    }
    table._setSelectedItems(selectedIndexes, true);
  }

};