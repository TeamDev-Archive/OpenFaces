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

// -------------------------- INITIALIZATION

O$.TreeTable = {
  _initTreeTableAPI: function(table) {
    O$.extend(table, {
      _of_treeTableComponentMarker: true,
      __getSelectedNodeCount: function () {
        var selectedItems = this._getSelectedItems();
        if (!selectedItems || (selectedItems.length == 1 && selectedItems[0] == -1))
          return 0;
        return selectedItems.length;
      },
      __setSelectedNodeIndexes: function (nodeIndexes) {
        this.__setSelectedRowIndexes(nodeIndexes);
      },
      __setSelectedNodeIndex: function (nodeIndex) {
        this.__setSelectedRowIndex(nodeIndex);
      },
      clearSelection: function() {
        this.__clearSelection();
      },
      isSelectionEmpty: function() {
        return this.__isSelectionEmpty();
      },
      getSelectedNodeCount: function() {
        return this.__getSelectedNodeCount();
      },
      getSelectedNodeKey: function() {
        return this.__getSelectedRowKey();
      },
      setSelectedNodeKey: function(rowKey) {
        this.__setSelectedRowKey(rowKey);
      },
      getSelectedNodeKeys: function() {
        return this.__getSelectedRowKeys();
      },
      setSelectedNodeKeys: function(rowKey) {
        this.__setSelectedRowKeys(rowKey);
      }

    });
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
    selectedNodeIndexes.forEach(function(idx) {
      if (idx > -1) selectedIndexes.push(idx);
    });
    table._setSelectedItems(selectedIndexes, true);
  },



  // --------------------------------- GENERIC FOLDING SUPPORT (FOR TABLE & TREETABLE)



  /**
   * Initializes the generic folding mechanism for a table. This mechanism consists of the following main aspects:
   *
   * - It organizes the table's rows into the specified tree structure, and activates the folding behavior where user
   *   can expand/collapse nodes by clicking on the (pre-rendered) expansion toggles, and complements the selection
   *   keyboard navigation feature with support for expanding/collapsing the currently selected row via keyboard.
   *
   * - The tree structure is specified in the rowIndexToChildCount entry inside of clientFoldingParams, and represents
   *   an associative array, or a map, where keys are the "root" string, or a parent row index number, and the
   *   respective values contain the number of immediate child nodes of the respective parent node, or the "?" string
   *   if child rows for the respective node has not been loaded yet. Absence of a key for a certain row index means
   *   that the appropriate row can be considered a "leaf" node with no child nodes.
   *
   * - This mechanism requires expansion toggles to be rendered inside of each row that has child rows, and
   *   they should have "o_toggle_e"/"o_toggle_c" class to signify the expanded or collapsed state respectively.
   *
   * - The current table's expansion state is always reflected in the <table-id>::expandedNodes hidden field.
   *
   * - If an attempt is made to expand a node whose child nodes are not preloaded (has the "?" value in the
   *   rowIndexToChildCount map), then this mechanism will send a server request to load the missing rows. This can be
   *   done in two ways depending on the table's _useAjax property. If it is set to true, then it sends the Ajax partial
   *   request for a portion named "subRows:<rowIndex>", and otherwise, it submits the whole page with the
   *   updated <table-id>::expandedNodes field that reflects the requested expansion state.
   *
   * - The table rows API is extended with isExpanded/setExpanded methods which expand and collapse child nodes.
   *
   * @param tableId - client-side id of the table to which folding support should be added
   * @param rowIndexToChildCount - an associative array where key is "root" or parent row index, and value is
   *                               the number of immediate child nodes if their records are already rendered, or "?"
   *                               if child records should be loaded with a separate server request.
   * @param treeColumnParams - an array where each entry corresponds to each tree column (a column with expansion toggle)
   *                           rendered in the table, and stores the following sub-array:
   *                           [expandedImageUrl, collapsedImageUrl]
   * @param toggleClassName - CSS class name which has to be applied to the expansion toggles
   * @param structureImageUrl - not implemented yet
   */
  _initFolding: function(tableId, rowIndexToChildCount, treeColumnParams, toggleClassName, structureImageUrl) {
    var table = O$(tableId);
    var super_addLoadedRows = table._addLoadedRows;
    O$.extend(table, {
      _rowIndexToChildCount: rowIndexToChildCount,
      _treeColumnParams: treeColumnParams,
      _toggleClassName: toggleClassName,
      _structureImageUrl: structureImageUrl,

        /*
           1. Saves the current tree hierarchy (root rows, child/parent references, level) into row objects,
           2. Shows/hides rows according to their current expansion state (as defined by their "expanded" property).
         */
      _updateRowTreeStructure: function() {
        var rowIndexToChildCount = this._rowIndexToChildCount;
        var rootNodeCount = rowIndexToChildCount["root"];
        var rows = table.body._getRows();
        this._styleRecalculationOnNodeExpansionNeeded = !!this._params.body.oddRowClassName;
        var pseudoCommonRootRow = {_pseudoRow: true, _childRows: []};
        O$.Tables._fixChromeCrashWithEmptyTR(table);

        function processRecursively(firstRowIndex, level, thisLevelRowCount, thisLevelVisible, parentRow) {
          for (var i = 0; i < thisLevelRowCount; i++) {
            var row = rows[firstRowIndex];
            O$.assert(row, "processRowVisibility: rowIndex == " + firstRowIndex);
            row._level = level;
            row._setVisible(thisLevelVisible);
            if (!row._table._styleRecalculationOnNodeExpansionNeeded) {
              if (thisLevelVisible && O$.isExplorer()) { // workaround for IE issue: when custom row style is applied to
                            // parent rows and a node is expanded, spacing between cells becomes white after node expansion
                row._updateStyle();
              }
            }
            row._parentRow = !parentRow._pseudoRow ? parentRow : null;
            if (parentRow) {
              parentRow._childRows.push(row);
            }

            var childCount = rowIndexToChildCount[firstRowIndex];
            if (childCount) {
              row._hasChildren = true;
              row._childRows = [];
              row._childrenLoaded = (childCount != "?");
              var nextLevelVisible = thisLevelVisible && row._isExpanded();
              if (row._childrenLoaded) {
                firstRowIndex = processRecursively(firstRowIndex + 1, level + 1, childCount, nextLevelVisible, row);
              } else
                firstRowIndex++;
            } else {
              row._hasChildren = false;
              firstRowIndex++;
            }
          }
          return firstRowIndex;
        }
        processRecursively(0, 0, rootNodeCount, true, pseudoCommonRootRow);

        table._rootRows = pseudoCommonRootRow._childRows;

        if (this._styleRecalculationOnNodeExpansionNeeded) {
          var visibleRows = 0;
          rows.forEach(function(row) {
            if (row._isVisible()) row._notifyRowMoved(visibleRows++);
          });
          rows.forEach(function(row) {
            if (row._isVisible()) {
              // _updateStyle is used for two reasons:
              // - show selection style if selected row was previously in an invisible branch
              // - workaround for IE issue: when custom row style is applied to parent rows and a node is expanded, spacing between cells becomes white after node expansion
              row._updateStyle();
            }
          });

        }
        O$.invokeFunctionAfterDelay(function() {
//          table._alignRowHeights();
          if (table._synchronizeVerticalAreaScrolling)
            table._synchronizeVerticalAreaScrolling();
        }, 50);
      },

      /*
        Saves all indexes for all expanded rows into the <table-id>::expandedNodes field
       */
      _updateExpandedNodesField: function() {
        var field = O$(this.id + "::expandedNodes");
        var expandedRowIndexes = "";
        var rows = this.body._getRows();
        for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
          var row = rows[rowIndex];
          var expanded = row._expandedForStateField !== undefined ? row._expandedForStateField : row._isExpanded();
          if (!expanded) continue;
          if (expandedRowIndexes.length > 0)
            expandedRowIndexes += ",";
          expandedRowIndexes += rowIndex;
        }
        field.value = expandedRowIndexes;
      },

      /*
        Handles key events for expanding/collapsing the selected row
       */
      _onKeyboardNavigation: function(e) {
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
        if ((e.rightPressed || e.plusPressed) && !row._isExpanded()) {
          row._setExpanded(true);
          return false;
        }
        if ((e.leftPressed || e.minusPressed) && row._isExpanded()) {
          row._setExpanded(false);
          return false;
        }

        return true;
      },

      /*
        Initializes the new rows
        Updates the tree structure to account for the newly inserted rows
        Updates the expanded nodes state (to account for changed node indexes)
       */
      _addLoadedRows: function(subRowsData) {
        super_addLoadedRows.apply(this, arguments);

        var newRows = this.__newRows;
        var parentRowIndex = this.__afterRowIndex;
        var newRowIndexToChildCount = subRowsData["structureMap"];

        var rows = this.body._getRows();
        var parentRow = parentRowIndex != -1 ? rows[parentRowIndex] : null;

        if (parentRow) {
          if (newRows == null || newRows.length == 0) {
            parentRow._childrenEmpty = true;
            var toggles = parentRow._toggles;
            toggles.forEach(function(toggle) {
              toggle.style.visibility = "hidden";
              toggle.className = "";
            });
          }
          parentRow._childrenLoaded = true;
        }
        var addedRowCount = newRows.length;
        var i;
        for (i = 0; i < addedRowCount; i++) {
          var newRow = newRows[i];
          O$.TreeTable._initRow(newRow);
        }

        if (parentRowIndex == -1)
          this._rowIndexToChildCount = newRowIndexToChildCount;
        else {
          // move existing row indexes to their new locations in the _rowIndexToChildCount map for the rows which are
          // moved as a result of row insertion (the rows that are located after the insertion point)
          var rowIndexToChildCount = this._rowIndexToChildCount;
          var newRowIndex;
          for (var rowIndex = rows.length - 1 - addedRowCount; rowIndex > parentRowIndex; rowIndex--) {
            if (rowIndexToChildCount[rowIndex] != undefined) {
              newRowIndex = rowIndex + addedRowCount;
              rowIndexToChildCount[newRowIndex] = rowIndexToChildCount[rowIndex];
              rowIndexToChildCount[rowIndex] = undefined;
            }
          }
          // insert the hierarchy of the newly loaded nodes into the _rowIndexToChildCount map.
          rowIndexToChildCount[parentRowIndex] = newRowIndexToChildCount[0];
          for (i = 0; i < addedRowCount; i++) {
            newRowIndex = parentRowIndex + 1 + i;
            rowIndexToChildCount[newRowIndex] = newRowIndexToChildCount[i + 1];
          }
        }

        this._updateExpandedNodesField();
        if (!parentRow._isExpanded()) {
            // This can be the case when the user double-clicks on the expansion toggle quickly, which first sends
            // the node expansion request, and then collapses the node, and then the request completes and appends the
            // nodes below the "collapsed" node, so we should ensure that the new rows are not visible until the node
            // is reopened again in this case.
          table._updateRowTreeStructure();
        }

        if (table._selectionMode == "hierarchical") {
          // _updateRowVisibility is needed to update parent/child references, but we can do it asynchronously to avoid
          // degrading the perceived performance
          setTimeout(function() {
            table._updateRowTreeStructure();
          }, 1);
        }
      }

    });


    var rows = table.body._getRows();
    rows.forEach(O$.TreeTable._initRow);

    table._updateRowTreeStructure();
    table._updateExpandedNodesField();
  },

  /*
    Locates row's expansion toggles, and binds the expansion behavior to them
    Detects row's expansion state (by the presence of appropriate expansion toggles)
    Introduces row expansion API
   */
  _initRow: function(row) {
    var table = row._table;

    var expandedToggles = [];
    var collapsedToggles = [];
    [row._leftRowNode, row._rowNode, row._rightRowNode].forEach(function(n) {
      if (!n) return;
      expandedToggles = expandedToggles.concat(O$.getChildNodesByClass(n, "o_toggle_e"));
      collapsedToggles = collapsedToggles.concat(O$.getChildNodesByClass(n, "o_toggle_c"));
    });
    var expanded = expandedToggles && expandedToggles.length > 0;
    O$.assert(expandedToggles.length == 0 || collapsedToggles.length == 0,
            "A row can't contain both expanded and collapsed nodes. row._index = " + row._index +
            "; table id = " + row._table.id);
    var toggles = expanded ? expandedToggles : collapsedToggles;

    var hasChildren = toggles && toggles.length > 0;
    function updateExpansionStateClass() {
      O$.setStyleMappings(row._rowNode, {expansion: row._isExpanded() ? "o_expandedNode" : "o_collapsedNode"});
    }

    var super_updateStyle = row._updateStyle;
    O$.extend(row, {
      _toggles: toggles,
      _hasChildren: hasChildren,
      _expanded: hasChildren && expanded,

      _updateStructureLine: function() {
        if (false) { //table._structureImageUrl) {
//          var d = document.createElement("div");
//          d.style.position = "absolute";
//          d.style.border = "1px solid red";
//          d.style.left = "0px";
//          d.style.top = "0px";

          var treeCell = row._cells[0];
//          treeCell.appendChild(d);
          var subCells = treeCell.getElementsByTagName("td");

          var level = subCells.length - 2;
          var bkgnd = "";
          var bkgndUrls = "";
          var bkgndPositions = "";
          for (var l = 0; l <= level; l++) {
            if (bkgnd.length > 0) bkgnd += ", ";
            if (bkgndUrls.length > 0) bkgndUrls += ", ";
            if (bkgndPositions.length > 0) bkgndPositions += ", ";
            var structureCell = subCells[l];
            var leftPos = O$.getElementBorderRectangle(structureCell, treeCell).x;
            bkgnd += "url(" + table._structureImageUrl + ") no-repeat " + leftPos + "px center"
          }
          treeCell.style.background = bkgnd;
//          treeCell.style.backgroundUrl = bkgndUrls;
//          treeCell.style.backgroundPosition = bkgndPositions;
        }
      },

      _updateStyle: function() {
        super_updateStyle.apply(this, arguments);
        this._updateStructureLine();
      },

      /*
       If child nodes for this node are loaded, this method expands/collapses the node and updates the presentation
       appropriately, otherwise it sends a row expansion request to the server in one of two ways. If table's _useAjax
       property is true, then it sends the Ajax partial request for a portion named "subRows:<parentRowIndex>",
       otherwise, it submits the whole page with the updated <table-id>::expandedNodes field that reflects the
       requested expansion state.

       This method can be not able to set the "expanded" state to true in some cases.
       Invokers must check _isExpanded() to see if expansion was successful.
       */
      _setExpanded: function(expanded) {
        if (row._childrenEmpty)
          return;
        if (this._expanded == expanded)
          return;
        var prevExpanded = this._expanded;
        this._expanded = expanded;
        updateExpansionStateClass();

        function setToggleImage(showExpandedImage) {
          var toggles = row._toggles;
          for (var toggleIndex = 0, toggleCount = toggles.length; toggleIndex < toggleCount; toggleIndex++) {
            var toggle = toggles[toggleIndex];
            var treeColumnParams = table._treeColumnParams[toggleIndex];
            toggle.src = showExpandedImage ? treeColumnParams[0] : treeColumnParams[1];
          }
        }

        setToggleImage(expanded);
        table._updateExpandedNodesField();

        if (expanded && !this._childrenLoaded && !this._childrenLoadRequestIssued) {
          this._childrenLoadRequestIssued = true;
          if (table._useAjax) {
            // Actual for the multiple simultaneous expansion requests for different rows. It is important not to report
            // the node as expanded in prior Ajax requests for expanding the other nodes, until a turn for expanding
            // this node comes in a queue of deferred expansion requests.
            row._expandedForStateField = prevExpanded;
            table._updateExpandedNodesField();

            function loadSubRowsWithAjax() {
              row._expandedForStateField = undefined;
              table._updateExpandedNodesField();
              var ajaxFailedProcessor = function () {
                row._expanded = prevExpanded;
                setToggleImage(prevExpanded);
                table._updateExpandedNodesField();
              };
              if (O$._ajaxRequestScheduled || O$._ajax_request_processing)
                ajaxFailedProcessor();
              else {
                table._subRowsRequestInProgress = true;
                O$.Ajax.requestComponentPortions(table.id, ["subRows:" + row._index], null, function () {
                          table._subRowsRequestInProgress = false;
                          O$.Table._acceptLoadedRows.apply(null, arguments);
                          if (table._deferredSubRowsRequests && table._deferredSubRowsRequests.length > 0) {
                            var nextDeferredRequest = table._deferredSubRowsRequests.shift();
                            nextDeferredRequest();
                          }
                        }, function () {
                          table._subRowsRequestInProgress = false;
                          ajaxFailedProcessor();
                        }
                );
              }
            }

            if (!table._subRowsRequestInProgress) {
              loadSubRowsWithAjax();
            } else {
              // postpone sub-rows load request till the previous one finishes to avoid row index misuse (see OF-146)
              if (!table._deferredSubRowsRequests) table._deferredSubRowsRequests = [];
              table._deferredSubRowsRequests.push(function () {
                loadSubRowsWithAjax();
              });
            }
          } else
            O$.submitEnclosingForm(table);
        } else {
          table._updateRowTreeStructure();
        }
      },

      _isExpanded: function() {
        return this._expanded;
      }
    });

    toggles.forEach(function(toggle) {
      O$.extend(toggle, {
        className: table._toggleClassName,
        _row: row,
        onclick: function(e) {
          var newExpanded = !row._isExpanded();
          row._setExpanded(newExpanded);
          if (table._focusable) {
            table._preventPageScrolling = true;
            table.focus();
            table._preventPageScrolling = false;
          }
          O$.cancelEvent(e);
        },
        ondblclick: function(e) {
          O$.repeatClickOnDblclick.apply(this, [e]);
          O$.cancelEvent(e);
        }
      });
    });

    updateExpansionStateClass();
    row._updateStructureLine();
  }


};