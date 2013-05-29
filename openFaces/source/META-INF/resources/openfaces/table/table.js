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
// -------------------------- COMMON TABLE FUNCTIONS

O$.Table = {
  SortingRule:O$.createClass(null, {
    constructor:function (columnId, ascending) {
      this.columnId = columnId;
      this.ascending = ascending;
    }
  }),

  GroupingRule:O$.createClass(null, {
    constructor:function (columnId, ascending) {
      this.columnId = columnId;
      this.ascending = ascending;
    }
  }),
  _tableLoadingHandlers:[],
  _onTableLoaded:function (tableId, func) {
    this._tableLoadingHandlers[tableId] = func; //todo: make multimap
  },
  _tableLoaded:function (tableId) {
    var listener = this._tableLoadingHandlers[tableId];
    if (listener) listener();
  },
  _initDataTableAPI:function (table) {
    O$.extend(table, {
      _of_dataTableComponentMarker:true,
      selectAllRows:function () {
        this.__selectAllRows();
      },
      clearSelection:function () {
        this.__clearSelection();
      },
      isSelectionEmpty:function () {
        return this.__isSelectionEmpty();
      },
      getSelectedRowIndex:function () {
        return this.__getSelectedRowIndex();
      },
      setSelectedRowIndex:function (rowIndex) {
        this.__setSelectedRowIndex(rowIndex);
      },
      getSelectedRowIndexes:function () {
        return this.__getSelectedRowIndexes();
      },
      setSelectedRowIndexes:function (rowIndexes) {
        this.__setSelectedRowIndexes(rowIndexes);
      },
      getSelectedRowKey:function () {
        return this.__getSelectedRowKey();
      },
      setSelectedRowKey:function (rowKey) {
        this.__setSelectedRowKey(rowKey);
      },
      getSelectedRowKeys:function () {
        return this.__getSelectedRowKeys();
      },
      setSelectedRowKeys:function (rowKey) {
        this.__setSelectedRowKeys(rowKey);
      },
      getRowCount:function () {
        return this.__getRowCount();
      },
      getSelectedCellId:function () {
        return this.__getSelectedCellId();
      },
      setSelectedCellId:function (cellId) {
        this.__setSelectedCellId(cellId);
      },
      getSelectedCellIds:function () {
        return this.__getSelectedCellIds();
      },
      setSelectedCellIds:function (cellIds) {
        this.__setSelectedCellIds(cellIds);
      }

    });
  },

  _init:function (tableId, initParams, useAjax, rolloverClass, apiInitializationFunctionName, deferredBodyLoading) {
    var table = O$.initComponent(tableId, {rollover:rolloverClass}, {
      _useAjax:useAjax,

      getCurrentColumn:function () {
        return this._showingMenuForColumn ? table._getColumn(this._showingMenuForColumn) : null;
      },
      _loadRows:function (completionCallback) {
        O$.Ajax.requestComponentPortions(this.id, ["rows"], null, function (table, portionName, portionHTML, portionScripts, portionData) {
          if (portionName != "rows") throw "Unknown portionName: " + portionName;
          table.body._removeAllRows();
          O$.Table._acceptLoadedRows(table, portionName, portionHTML, portionScripts, portionData);
          if (completionCallback)
            completionCallback();
        });
      },
      _addLoadedRows:function (rowsData) {
        var newRows = this.__newRows;
        var afterRowIndex = this.__afterRowIndex;
        var newRowsToStylesMap = rowsData["rowStylesMap"];
        var newRowCellsToStylesMap = rowsData["cellStylesMap"];
        var rowKeys = rowsData["rowKeys"];

        this._insertRowsAfter(afterRowIndex, newRows, newRowsToStylesMap, newRowCellsToStylesMap, rowKeys);
      }

    }, {
      onbeforeajaxreload:initParams.onbeforeajaxreload,
      onafterajaxreload:initParams.onafterajaxreload
    });

    //check if the table is using ajax
    if (O$._addComponentAjaxReloadHandler && (table.onbeforeajaxreload || table.onafterajaxreload)) {
      O$._addComponentAjaxReloadHandler(table,
              table.onbeforeajaxreload,
              table.onafterajaxreload
      );
    }
    ;

    try {
      O$.Tables._init(table, initParams);
    } finally {
      table.style.visibility = "visible";
      // can't just exclude the "o_initially_invisible" from table.className because of IE issue (JSFC-2337)
    }
    O$.Table._initApiFunctions(table);
    O$.Table._initInnerFunctions(table);
    O$.addUnloadHandler(table, function () {
      table._cellInsertionCallbacks = [];
      table._cellMoveCallbacks = [];
    });
    O$.addThisComponentToAllParents(table);
    O$.extend(table, {
      _originalClassName:table.className,

      onComponentUnload:function () {
        var i, count;
        O$.unloadAllHandlersAndEvents(table);

        var filtersToHide = this._filtersToHide;
        if (!O$.isExplorer6() || !filtersToHide)
          return false;

        for (i = 0, count = filtersToHide.length; i < count; i++) {
          var filter = filtersToHide[i];
          filter.style.visibility = "hidden";
        }
        return true;
      },

      _cleanUp:function () {
        [table.header, table.body, table.footer].forEach(function (section) {
          if (section) section._rows = [];
        });
      }
    });

    if (apiInitializationFunctionName) {
      var initFunction = eval(apiInitializationFunctionName);
      if (initFunction)
        initFunction(table);
    }

    if (deferredBodyLoading)
      O$.addInternalLoadEvent(function () {
        var auxiliaryTags = O$(table.id + "::auxiliaryTags");
        table.parentNode.appendChild(auxiliaryTags);
        table._loadRows(function () {
          [table.footer, table.body, table.header].forEach(function (section) {
            if (auxiliaryTags == null || !section || section._rows.length == 0) return;
            var row = section._rows[section._rows.length - 1];
            if (row._cells.length > 0) {
              row._cells[row._cells.length - 1].appendChild(auxiliaryTags);
              auxiliaryTags = null;
            }
          });
          if (auxiliaryTags)
            table.appendChild(auxiliaryTags);
        });
      });
  },

  _initApiFunctions:function (table) {
    O$.extend(table, {
      __selectAllRows:function () {
        if (this._selectableItems != "rows")
          throw "selectAllRows: The table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (!this._multipleSelectionAllowed)
          throw "selectAllRows: The table is not set up for multiple selection. Table's clientId is: " + this.id;
        this._selectAllItems();
      },
      __clearSelection:function () {
        this._unselectAllItems();
      },
      __isSelectionEmpty:function () {
        var selectedItems = this._getSelectedItems();
        if (!selectedItems || selectedItems.length == 0)
          return true;
        return selectedItems[0] == -1;
      },
      __getSelectedRowIndex:function () {
        if (this._selectableItems != "rows")
          throw "getSelectedRowIndex: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (this._multipleSelectionAllowed)
          throw "getSelectedRowIndex can only used on a table with single selection mode; table's clientId is: " + this.id;

        var selectedItems = this._getSelectedItems();
        if (selectedItems.length == 0)
          return -1;
        return selectedItems[0];
      },
      __setSelectedRowIndex:function (rowIndex) {
        if (this._selectableItems != "rows")
          throw "setSelectedRowIndex: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (this._multipleSelectionAllowed)
          throw "setSelectedRowIndex can only used on a table with single selection mode; table's clientId is: " + this.id;
        var bodyRows = table.body._getRows();
        if ((rowIndex != -1) && (rowIndex < 0 || rowIndex >= bodyRows.length))
          throw "setSelectedRowIndex parameter is out of range (" + rowIndex + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;
        this._setSelectedItems(rowIndex != -1 ? [rowIndex] : []);
      },
      __getSelectedRowIndexes:function () {
        if (this._selectableItems != "rows")
          throw "getSelectedRowIndexes: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (!this._multipleSelectionAllowed)
          throw "getSelectedRowIndexes can only used on a table with multiple selection mode; table's clientId is: " + this.id;

        var selectedItems = this._getSelectedItems();
        if (!selectedItems || (selectedItems.length == 1 && selectedItems[0] == -1))
          selectedItems = [];
        return selectedItems;
      },
      __setSelectedRowIndexes:function (rowIndexes) {
        if (this._selectableItems != "rows")
          throw "setSelectedRowIndexes: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (!this._multipleSelectionAllowed)
          throw "setSelectedRowIndexes can only used on a table with multiple selection mode; table's clientId is: " + this.id;
        if (!rowIndexes)
          rowIndexes = [];

        var bodyRows = table.__getRowCount();
        for (var i = 0, count = rowIndexes.length; i < count; i++) {
          var rowIndex = rowIndexes[i];
          if (rowIndex < 0 || rowIndex >= bodyRows.length)
            throw "setSelectedRowIndexes parameter is out of range (" + rowIndex + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;
        }
        this._setSelectedItems(rowIndexes);
      },
      __getSelectedRowKey:function () {
        var rowIndex = this.__getSelectedRowIndex();
        if (rowIndex == -1) return null;
        var rowKey = this.__getRowKey(rowIndex);
        return rowKey;
      },
      __setSelectedRowKey:function (rowKey) {
        var rowIndex = this.__getRowIndexByKey(rowKey);
        this.__setSelectedRowIndex(rowIndex);
      },
      __getSelectedRowKeys:function () {
        var indexes = this.__getSelectedRowIndexes();
        var keys = [];
        for (var i = 0, count = indexes.length; i < count; i++) {
          var idx = indexes[i];
          keys[i] = this.__getRowKey(idx);
        }
        return keys;
      },
      __setSelectedRowKeys:function (keys) {
        var indexes = [];
        for (var i = 0, count = keys.length; i < count; i++) {
          var key = keys[i];
          indexes[i] = this.__getRowIndexByKey(key);
        }
        this.__setSelectedRowIndexes(indexes);
      },
      __getRowCount:function () {
        if (this._params.body.noDataRows)
          return 0;
        var bodyRows = this.body._getRows();
        return bodyRows.length;
      },
      __getRowKey:function (rowIndex) {
        if (this._params.body.noDataRows)
          throw "There are no rows in this table";
        var bodyRows = this.body._getRows();
        if (rowIndex < 0 || rowIndex >= bodyRows.length)
          throw "getRowKey parameter is out of range (" + rowIndex + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;
        return bodyRows[rowIndex]._rowKey;
      },
      __getRowIndexByKey:function (rowKey) {
        if (this._params.body.noDataRows)
          return -1;
        var bodyRows = this.body._getRows();
        for (var i = 0, count = bodyRows.length; i < count; i++) {
          var row = bodyRows[i];
          if (row._rowKey == rowKey)
            return i;
        }
        return -1;
      },
      __getSelectedCellId:function () {
        if (this._selectableItems != "cells")
          throw "getSelectedCellId: The specified table is not set up for cell selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (this._multipleSelectionAllowed)
          throw "getSelectedCellId can only used on a table with single selection mode; table's clientId is: " + this.id;

        var selectedItems = this._getSelectedItems();
        if (selectedItems.length == 0)
          return [];
        return selectedItems[0];
      },
      __setSelectedCellId:function (cellId) {
        if (this._selectableItems != "cells")
          throw "setSelectedCellId: The specified table is not set up for cell selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (this._multipleSelectionAllowed)
          throw "setSelectedCellId can only used on a table with single selection mode; table's clientId is: " + this.id;
        var bodyRows = table.body._getRows();
        if ((cellId[0] != -1) && (cellId[0] < 0 || cellId[0] >= bodyRows.length))
          throw "setSelectedCellId row parameter is out of range (" + cellId[0] + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;

        if (table._columns.byId(cellId[1]) == null) {
          throw "setSelectedCellId column parameter is not correct (" + cellId[1] + "); table's clientId is: " + this.id;
        }
        this._setSelectedItems([cellId]);
        var cursorCell = bodyRows[cellId[0]]._cells[table._columns.byId(cellId[1])._index];
        cursorCell._setAsCursor();
      },
      __getSelectedCellIds:function () {
        if (this._selectableItems != "cells")
          throw "getSelectedCellIds: The specified table is not set up for cell selection. Selectable items are " + this._selectableItems + "; table's clientId is: " + this.id;
        if (!this._multipleSelectionAllowed)
          throw "getSelectedCellIds can only used on a table with multiple selection mode; table's clientId is: " + this.id;

        var selectedItems = this._getSelectedItems();
        if (!selectedItems || (selectedItems.length == 1 && (selectedItems[0][0] == -1 || selectedItems[0][1] == null)))
          selectedItems = [];

        O$.extend(selectedItems, {
          _contains:function (anCellId) {
            var isContain = false;
            selectedItems.forEach(function (cellId) {
              if (anCellId[0] == cellId[0] && anCellId[1] == cellId[1]) {
                isContain = true;
              }
            });
            return isContain;
          }
        });
        return selectedItems;
      },
      __setSelectedCellIds:function (cellIds) {
        if (this._selectableItems != "cells")
          throw "setSelectedCellIds: The specified table is not set up for cells selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (!this._multipleSelectionAllowed)
          throw "setSelectedCellIds can only used on a table with multiple selection mode; table's clientId is: " + this.id;
        if (!cellIds)
          cellIds = [];

        var bodyRows = table.__getRowCount();
        for (var i = 0, count = cellIds.length; i < count; i++) {
          var cellId = cellIds[i];
          if ((cellId[0] != -1) && (cellId[0] < 0 || cellId[0] >= bodyRows.length))
            throw "setSelectedCellIds row parameter is out of range (" + cellId[0] + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;

          if (table._columns.byId(cellId[1]) == null) {
            throw "setSelectedCellId column parameter is not correct (" + cellId[1] + "); table's clientId is: " + this.id;
          }
        }
        this._setSelectedItems(cellIds);
        if (cellIds.length != 0) {
          var cursorCellId = cellIds[cellIds.length - 1];
          var cursorCell = bodyRows[cursorCellId[0]]._cells[table._columns.byId(cursorCellId[1])._index];
          cursorCell._setAsCursor();
        }
      },
      /**
       * Call this method to avoid any successive calls to such methods as table.sorting.setSortingRules,
       * table.grouping.setGroupingRules, etc. to send Ajax requests right away as they are invoked, but accumulate and
       * postpone the appropriate Ajax actions while passed function is executed, so that all of them be
       * performed at once with a single Ajax request.
       */
      combineSubmissions:function (func) {
        O$._combineSubmissions(table, func);
      },

      getColumnsOrder:function () {
        var columnIds = [];
        this._columns.forEach(function (column) {
          columnIds.push(column.columnId);
        });
        return columnIds;
      },

      setColumnsOrder:function (columnIds) {
        var columnIdsStr = columnIds.join(",");
        var prevColumnIdsStr = this.getColumnsOrder().join(",");
        if (columnIdsStr == prevColumnIdsStr) return;

        O$._submitInternal(table, null, [
          [table.id + "::columnsOrder", columnIdsStr]
        ]);
      },

      isColumnVisible:function (columnId) {
        var columnIds = table.getColumnsOrder();
        var idx = columnIds.indexOf(columnId);
        return idx >= 0;
      },

      setColumnVisible:function (columnId, visible) {
        if (visible)
          this.showColumn(columnId);
        else
          this.hideColumn(columnId);
      },

      hideColumn:function (columnId) {
        if (!this.isColumnVisible(columnId)) return;
        var currentColumns = table.getColumnsOrder();
        var currentIndex = currentColumns.indexOf(columnId);
        currentColumns.splice(currentIndex, 1);
        table.setColumnsOrder(currentColumns);
      },

      showColumn:function (columnId) {
        if (this.isColumnVisible(columnId)) return;
        var currentColumns = table.getColumnsOrder();

        function insertColumn(newIndex) {
          currentColumns.splice(newIndex, 0, columnId);
        }

        var done = false;
        (function tryDoItFine() {
          var structure = table._columnsLogicalStructure.root();
          var leafs = structure.allLeafs(true);
          var visible = leafs.filter(function (l) {
            return l.isVisible();
          });

          function onlyIds(inPut) {
            return inPut.map(function (l) {
              return l.columnId;
            });
          }

          var originalIndex = onlyIds(leafs).indexOf(columnId),
                  leftVisibleNeighborhood = function () {
                    for (var i = originalIndex - 1; i >= 0; i--)if (leafs[i].isVisible())return leafs[i];
                  }(),
                  rightVisibleNeighborhood = function () {
                    for (var i = originalIndex + 1; i < leafs.length; i++)if (leafs[i].isVisible())return leafs[i];
                  }();
          if (leftVisibleNeighborhood || rightVisibleNeighborhood) {
            if (leftVisibleNeighborhood) {
              insertColumn(currentColumns.indexOf(leftVisibleNeighborhood.columnId) + 1);
            } else {
              insertColumn(currentColumns.indexOf(rightVisibleNeighborhood.columnId));
            }
            done = true;
          }
        }());
        if (!done)(function doItSomehow() {
          var index = 0;
          currentColumns.forEach(function (current) {
            if (done)return;
            table._columnsReorderingSupport(columnId, current)
                    .onLeftEdgePermit(function () {
                      if (!done)insertColumn(index);
                      done = true;
                    })
                    .onRightEdgePermit(function () {
                      if (!done)insertColumn(index + 1);
                      done = true;

                    });
            index++;
          });
        }());
        if (!done)O$.assert(done, "Can't show column: " + columnId);
        table.setColumnsOrder(currentColumns);
      }
    });
  },

  _initInnerFunctions:function (table) {
    table._columnsLogicalStructure = function () {
      var result;
      if (!result) result = function () {
        var currentColumnsOrder = table.getColumnsOrder();

        function visibilityPredicate(node) {
          if (node.isLeaf()) {
            return currentColumnsOrder.indexOf(node.columnId) >= 0;
          }
          return node.visibleChildren().length > 0;
        }

        function helper(logicalDescription, parent) {
          var self = {
            columnId:logicalDescription.columnId,
            parent:function () {
              return parent;
            },
            root:function () {
              var node = self;
              while (node.parent())node = node.parent();
              return node;
            },
            isLeaf:function () {
              return !logicalDescription.subColumns;
            },
            children:function (dontApplySorting) {
              function indexOfAnyVisibleLeaf(node) {
                if (node.isLeaf()) {
                  return currentColumnsOrder.indexOf(node.columnId);
                }
                var visibleChildren = node.visibleChildren();
                return visibleChildren.length > 0 ? indexOfAnyVisibleLeaf(visibleChildren[0]) : -1;
              }

              var result = [];
              if (!self.isLeaf())logicalDescription.subColumns.forEach(function (subColumn) {
                result.push(helper(subColumn, self));
              });
              if (!dontApplySorting)result.sort(function (a, b) {
                return indexOfAnyVisibleLeaf(a) - indexOfAnyVisibleLeaf(b);
              });
              return result;
            },
            visibleChildren:function () {
              return self.children().filter(visibilityPredicate);
            },
            firstVisibleLeaf:function () {
              var visibleChild = self;
              while (!visibleChild.isLeaf()) visibleChild = visibleChild.visibleChildren()[0];
              return visibleChild;
            },
            lastVisibleLeaf:function () {
              var visibleChild = self;
              while (!visibleChild.isLeaf()) {
                var visibleChildren = visibleChild.visibleChildren();
                visibleChild = visibleChildren[visibleChildren.length - 1];
              }
              return visibleChild;
            },
            isVisible:function () {
              return visibilityPredicate(self);
            },
            allLeafs:function (dontApplySorting) {
              var result = [];
              var candidates = self.children(dontApplySorting).slice(0);
              while (candidates.length > 0) {
                var current = candidates.shift();
                if (!current.isLeaf()) {
                  candidates = current.children(dontApplySorting).concat(candidates);
                } else {
                  result.push(current);
                }
              }
              return result;
            },
            find:function (columnId) {
              if (self.columnId == columnId) {
                return self;
              }
              var result = null;
              self.children().forEach(function (child) {
                if (!result) result = child.find(columnId);
              });
              return result;
            }
          };
          return self;
        }

        return  helper({subColumns:table._params.logicalColumns}, null);
      }();
      return result;
    }();
    table._columnsReorderingSupport = function (sourceColumnId, targetColumnId) {
      function canBeInserted(where, what, atLeft) {
        if (atLeft != false) {
          //todo: [s.kurilin]  should be rewritten for allow colGroup reordering
          var currentOrder = table.getColumnsOrder().slice(0);
          where = currentOrder[currentOrder.indexOf(where) - 1];
        }

        function findFirstVisibleParent(node) {
          var parent = node.parent();
          while (parent && !parent.isVisible()) {
            parent = parent.parent();
          }
          return parent;
        }

        var sourceNode = table._columnsLogicalStructure.root().find(what),
                firstVisibleParent = findFirstVisibleParent(sourceNode);

        function canBePlacedInOrAfter() {
          return firstVisibleParent.visibleChildren().filter(
                  function (child) {
                    return child.lastVisibleLeaf().columnId == where;
                  }).length > 0
        }

        function canBePlacedBefore() {
          var leftsVisibleNode = firstVisibleParent.firstVisibleLeaf();
          var currentIndex = table.getColumnsOrder().indexOf(leftsVisibleNode.columnId);
          return (currentIndex == 0 && !where)
                  || (currentIndex > 0 && table.getColumnsOrder()[currentIndex - 1] == where);
        }

        return  canBePlacedBefore() || canBePlacedInOrAfter();
      }

      var self = {
        onLeftEdgePermit:function (func) {
          if (canBeInserted(targetColumnId, sourceColumnId, true))func();
          return self;
        },
        onRightEdgePermit:function (func) {
          if (canBeInserted(targetColumnId, sourceColumnId, false))func();
          return self;
        }
      };
      return self;
    };
  },

  _createTableWithoutTd:function () {
    var tbl = document.createElement("table");
    tbl.cellSpacing = "0";
    tbl.cellPadding = "0";
    tbl.border = "0";
    var tbody = document.createElement("tbody");
    tbl.appendChild(tbody);
    var tr = document.createElement("tr");
    tbody.appendChild(tr);
    tbl._tr = tr;
    return tbl;
  },

  _createImage:function (url) {
    var img = document.createElement("img");
    img.src = url;
    return img;
  },


  _acceptLoadedRows:function (table, portionName, portionHTML, portionScripts, portionData) {
    var sepIdx = portionName.indexOf(":");
    table.__afterRowIndex = sepIdx != -1 ? eval(portionName.substring(sepIdx + 1)) : -1;

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

    var scrolling = table._params.scrolling;
    if (scrolling) {
      var compositeRows = [];
      var i = 0, count = newRows.length;
      while (i < count) {
        var leftRowNode = scrolling.leftFixedCols ? newRows[i++] : null;
        var centerRowNode = newRows[i++];
        var rightRowNode = scrolling.rightFixedCols ? newRows[i++] : null;

        compositeRows.push({
          _leftRowNode:leftRowNode,
          _rowNode:centerRowNode,
          _rightRowNode:rightRowNode
        });
      }
      newRows = compositeRows;
    }
    table.__newRows = newRows;

    table._addLoadedRows(portionData);
    O$.Ajax.executeScripts(portionScripts);
  },

  // -------------------------- KEYBOARD NAVIGATION SUPPORT

  _initKeyboardNavigation:function (tableId, controlPaginationWithKeyboard, focusedClassName, canPageBack, canPageForth, canSelectLastPage, tabIndex) {
    var table = O$.initComponent(tableId, null, {
      _performPagingAction:function (actionStr) {
        O$.setHiddenField(this, this.id + "::pagination", actionStr);
        O$._submitInternal(this);
      },

      _nextPage:function () {
        if (canPageForth) this._performPagingAction("selectNextPage");
      },
      _previousPage:function () {
        if (canPageBack) this._performPagingAction("selectPrevPage");
      },
      _firstPage:function () {
        if (canPageBack) this._performPagingAction("selectFirstPage");
      },
      _lastPage:function () {
        if (canSelectLastPage) this._performPagingAction("selectLastPage");
      },
      _selectPageNo:function (pageNo) {
        this._performPagingAction("selectPageNo:" + pageNo);
      }
    });

    O$.setupArtificialFocus(table, focusedClassName, tabIndex);

    var pagingFld = O$(table.id + "::pagination");
    if (pagingFld)
      pagingFld.value = "";


    var eventName = (O$.isSafariOnMac() || O$.isOpera() || O$.isMozillaFF()) ? "onkeypress" : "onkeydown";
    table._prevKeyHandler = table[eventName];
    table[eventName] = function (evt) {
      var e = O$.getEvent(evt);

      if (this._prevKeyHandler)
        this._prevKeyHandler(evt);

      var ctrlPressed = e.ctrlKey;
      var altPressed = e.altKey;
      var shiftPressed = e.shiftKey;
      var noModifiersPressed = !ctrlPressed && !altPressed && !shiftPressed;

      e.upPressed = false;
      e.downPressed = false;
      e.homePressed = false;
      e.endPressed = false;
      e.pageUpPressed = false;
      e.pageDownPressed = false;
      e.leftPressed = false;
      e.rightPressed = false;
      e.plusPressed = false;
      e.minusPressed = false;
      switch (e.keyCode) {
        case 33: // page up
        case 63276: // Safari for Mac
          e.pageUpPressed = true;
          break;
        case 34: // page down
        case 63277: // Safari for Mac
          e.pageDownPressed = true;
          break;
        case 35: // end
        case 63275:
          e.endPressed = true;
          break;
        case 36: // home
        case 63273:
          e.homePressed = true;
          break;
        case 37: // left
        case 63234:
          e.leftPressed = true;
          break;
        case 38: // up
        case 63232:
          e.upPressed = true;
          break;
        case 39: // right
        case 63235:
          e.rightPressed = true;
          break;
        case 40: // down
        case 63233:
          e.downPressed = true;
          break;
        case 107: // plus
        case 43:
          e.plusPressed = true;
          break;
        case 109: // minus
        case 45:
          e.minusPressed = true;
          break;
      }

      var passEvent = true;

      if (controlPaginationWithKeyboard && !altPressed && !shiftPressed) {
        if (e.pageUpPressed) {
          passEvent = false;
          if (!(this._params.scrolling && this._selectionKeyboardSupport && this._selectionEnabled))
            this._previousPage();
        }
        if (e.pageDownPressed) {
          passEvent = false;
          if (!(this._params.scrolling && this._selectionKeyboardSupport && this._selectionEnabled))
            this._nextPage();
        }
        if (ctrlPressed && e.homePressed) {
          passEvent = false;
          this._firstPage();
        }
        if (ctrlPressed && e.endPressed) {
          passEvent = false;
          this._lastPage();
        }
      }

      if (this._selectionKeyboardSupport && this._selectionEnabled) {
        var rowCount = this.__getRowCount();
        if (this._selectableItems == "rows") {
          if (this._multipleSelectionAllowed && !altPressed && !ctrlPressed) {      // ------ multiple selection
            var selectedRowIndexes = this.__getSelectedRowIndexes();
            var idx, newIdx;
            if (selectedRowIndexes.length == 0)
              idx = -1;
            else if (selectedRowIndexes.length == 1)
              idx = selectedRowIndexes[0];
            else {
              idx = this._rangeEndRowIndex;
              if (!idx)
                idx = selectedRowIndexes[0];
            }

            if (!shiftPressed) {
              newIdx = O$.Table._checkRowNavigation(this, idx, rowCount, e);
              if (newIdx != null) {
                passEvent = false;
                this.__setSelectedRowIndexes([newIdx]);
                O$.Table._scrollToRowIndexes(this, [newIdx]);
                this._baseRowIndex = null;
                this._baseSelectedRowIndexes = null;
                this._rangeEndRowIndex = null;
              }
            } else {
              var baseRowIndex = this._baseRowIndex;
              if (baseRowIndex == null) {
                baseRowIndex = idx != -1 ? idx : 0;
                this._baseRowIndex = baseRowIndex;
                this._baseSelectedRowIndexes = selectedRowIndexes;
              }
              var rangeEndRowIndex = this._rangeEndRowIndex;
              if (rangeEndRowIndex == null)
                rangeEndRowIndex = baseRowIndex;
              var newRangeEndRowIndex = O$.Table._checkRowNavigation(this, rangeEndRowIndex, rowCount, e);
              if (newRangeEndRowIndex != null) {
                passEvent = false;
                var newSelectedRowIndexes = O$.Table._combineSelectedRowsWithRange(this, this._baseSelectedRowIndexes, baseRowIndex, newRangeEndRowIndex);
                this._rangeEndRowIndex = newRangeEndRowIndex;
                this.__setSelectedRowIndexes(newSelectedRowIndexes);
                O$.Table._scrollToRowIndexes(this, [newRangeEndRowIndex]);
              }
            }

          }
          if (!this._multipleSelectionAllowed && noModifiersPressed) {              // ------ single selection
            idx = this.__getSelectedRowIndex();
            newIdx = O$.Table._checkRowNavigation(this, idx, rowCount, e);
            if (newIdx != null) {
              passEvent = false;
              this.__setSelectedRowIndex(newIdx);
              O$.Table._scrollToRowIndexes(this, [newIdx]);
            }
          }
        } else if (this._selectableItems == "cells") {
          var bodyRows = table.body._getRows();
          var cursorCell;
          if (!this._multipleSelectionAllowed) {
            if (!shiftPressed) {
              var cellId = (table._cursorCell != null) ?
                      [table._cursorCell._row._index, table._cursorCell._column.columnId] : [-1, null];
              var newRowId = O$.Table._checkRowNavigation(this, cellId[0], rowCount, e);
              if (newRowId != null) {
                passEvent = false;
                if (cellId[1] == null) {
                  cellId[1] = table._columns[0].columnId;
                }
                this._setSelectedItems([
                  [newRowId, cellId[1]]
                ]);
                cursorCell = bodyRows[newRowId]._cells[table._columns.byId(cellId[1])._index];
                cursorCell._setAsCursor();
                O$.Table._scrollToRowIndexes(this, [newRowId]);
              }
              if (newRowId != cellId[0]) {
                var newColumnId = O$.Table._checkColumnNavigation(this, cellId[1], table._columns.length, e);
                if (newColumnId != null && newColumnId != cellId[1]) {
                  passEvent = false;
                  if (cellId[0] == -1) {
                    cellId[0] = 0;
                  }
                  this._setSelectedItems([
                    [cellId[0], newColumnId]
                  ]);

                  cursorCell = bodyRows[cellId[0]]._cells[table._columns.byId(newColumnId)._index];
                  cursorCell._setAsCursor();
                  O$.Table._scrollToCells(this, [
                    [cellId[0], newColumnId]
                  ]);
                } else if (table._fillDirection == "document") {
                  var newCellId = O$.Table._checkCellNavigationInDocumentMode(this, cellId, e);
                  if (newCellId != null) {
                    this._setSelectedItems([newCellId]);
                    cursorCell = bodyRows[newCellId[0]]._cells[table._columns.byId(newCellId[1])._index];
                    cursorCell._setAsCursor();
                    O$.Table._scrollToCells(this, [newCellId]);
                  }
                }
              }
            }
          } else {
            var selectedCellIds = this.__getSelectedCellIds();
            var cellId, newRowId;
            if (selectedCellIds.length == 0) {
              if (table._cursorCell != null) {
                cellId = [table._cursorCell._row._index, table._cursorCell._column.columnId];
              } else {
                cellId = [-1, null];
              }
            }
            else if (selectedCellIds.length == 1)
              cellId = selectedCellIds[0];
            else {
              cellId = this._rangeEndCellId;
              if (!cellId)
                cellId = selectedCellIds[0];
            }
            if (!shiftPressed) {
              newRowId = O$.Table._checkRowNavigation(this, cellId[0], rowCount, e);
              if (newRowId != null) {
                passEvent = false;
                this._baseCellId = null;
                this._baseSelectedCellIds = null;
                this._rangeEndCellId = null;
                if (cellId[1] == null) {
                  cellId[1] = table._columns[0].columnId;
                }
                this._setSelectedItems([
                  [newRowId, cellId[1]]
                ]);

                cursorCell = bodyRows[newRowId]._cells[table._columns.byId(cellId[1])._index];
                cursorCell._setAsCursor();
                O$.Table._scrollToRowIndexes(this, [newRowId]);
              }
              if (newRowId != cellId[0]) {
                var newColumnId = O$.Table._checkColumnNavigation(this, cellId[1], table._columns.length, e);
                if (newColumnId != null && newColumnId != cellId[1]) {
                  passEvent = false;
                  this._baseCellId = null;
                  this._rangeEndCellId = null;
                  if (cellId[0] == -1) {
                    cellId[0] = 0;
                  }
                  this._setSelectedItems([
                    [cellId[0], newColumnId]
                  ]);

                  cursorCell = bodyRows[cellId[0]]._cells[table._columns.byId(newColumnId)._index];
                  cursorCell._setAsCursor();
                  O$.Table._scrollToCells(this, [
                    [cellId[0], newColumnId]
                  ]);
                } else if (table._fillDirection == "document") {
                  var newCellId = O$.Table._checkCellNavigationInDocumentMode(this, cellId, e);
                  if (newCellId != null) {
                    this._baseCellId = null;
                    this._rangeEndCellId = null;
                    passEvent = false;
                    this._setSelectedItems([newCellId]);

                    cursorCell = bodyRows[newCellId[0]]._cells[table._columns.byId(newCellId[1])._index];
                    cursorCell._setAsCursor();
                    O$.Table._scrollToCells(this, [newCellId]);
                  }
                }
              }
            } else if (cellId[0] != -1 && cellId[1] != null) {
              var baseCellId = this._baseCellId;
              if (baseCellId == null) {
                baseCellId = cellId;
                this._baseCellId = baseCellId;
                this._baseSelectedCellIds = [baseCellId];
              }
              var rangeEndCellId = this._rangeEndCellId;
              var newSelectedCellIdsIndexes;
              if (rangeEndCellId == null)
                rangeEndCellId = baseCellId;
              var newRangeEndRowIndex = O$.Table._checkRowNavigation(this, rangeEndCellId[0], rowCount, e);
              if (newRangeEndRowIndex != null) {
                passEvent = false;
                newSelectedCellIdsIndexes = O$.Table._combineSelectedCellsWithRange(this, this._baseSelectedCellIds, baseCellId, [newRangeEndRowIndex, rangeEndCellId[1]]);
                this._rangeEndCellId = [newRangeEndRowIndex, rangeEndCellId[1]];
                this._setSelectedItems(newSelectedCellIdsIndexes);

                cursorCell = bodyRows[this._rangeEndCellId[0]]._cells[table._columns.byId(this._rangeEndCellId[1])._index];
                cursorCell._setAsCursor();
                O$.Table._scrollToCells(this, [this._rangeEndCellId]);
              }

              if (newRangeEndRowIndex != rangeEndCellId[0]) {
                var newColumnId = O$.Table._checkColumnNavigation(this, rangeEndCellId[1], table._columns.length, e);
                if (newColumnId != null && newColumnId != rangeEndCellId[1]) {
                  passEvent = false;
                  newSelectedCellIdsIndexes = O$.Table._combineSelectedCellsWithRange(this, this._baseSelectedCellIds, baseCellId, [rangeEndCellId[0], newColumnId]);
                  this._rangeEndCellId = [rangeEndCellId[0], newColumnId];
                  this._setSelectedItems(newSelectedCellIdsIndexes);

                  cursorCell = bodyRows[this._rangeEndCellId[0]]._cells[table._columns.byId(this._rangeEndCellId[1])._index];
                  cursorCell._setAsCursor();
                  O$.Table._scrollToCells(this, [this._rangeEndCellId]);
                } else if (table._fillDirection == "document") {
                  var cellId = O$.Table._checkCellNavigationInDocumentMode(this, rangeEndCellId, e);
                  if (cellId != null) {
                    passEvent = false;
                    newSelectedCellIdsIndexes = O$.Table._combineSelectedCellsWithRange(this, this._baseSelectedCellIds, baseCellId, cellId);
                    this._rangeEndCellId = cellId;
                    this._setSelectedItems(newSelectedCellIdsIndexes);

                    cursorCell = bodyRows[this._rangeEndCellId[0]]._cells[table._columns.byId(this._rangeEndCellId[1])._index];
                    cursorCell._setAsCursor();
                    O$.Table._scrollToCells(this, [this._rangeEndCellId]);
                  }
                }
              }
            }
          }
        }
      }
      if (passEvent) {
        if (this._onKeyboardNavigation)
          passEvent = this._onKeyboardNavigation(e);
      }
      if (!passEvent) {
        O$.cancelEvent(e);
      }

      var tabPressed = e.keyCode == 9;
      if (!tabPressed && O$.isExplorer6()) {
        // cancel the event to avoid rare IE6 crashes (JSFC-3783)
        var target = (e != null)
                ? (e.target ? e.target : e.srcElement)
                : null;

        var isTableFocused = false;
        if (target._table) {
          isTableFocused = target._table._of_dataTableComponentMarker || target._table._of_treeTableComponentMarker;
        } else if (target._of_dataTableComponentMarker || target._of_treeTableComponentMarker) {
          isTableFocused = true;
        }

        if (!isTableFocused && O$.isControlFocusable(target)) {
          return true;
        }

        if (e.preventDefault)
          e.preventDefault();
        e.returnValue = false;
        return false;
      } else {
        // allow focus traversal with Tab key while the table is focused
        return true;
      }
    };
    O$.addUnloadHandler(table, function () {
      table[eventName] = null;
    });

    table._prevOnfocus_kn = table.onfocus;
    table.onfocus = function (e) {
      if (this._submitting)
        return;
      if (this._prevOnfocus_kn)
        this._prevOnfocus_kn(e);
      var focusFld = O$(this.id + "::focused");
      focusFld.value = "true";
    };
    O$.addUnloadHandler(table, function () {
      table.onfocus = null;
    });

    table._prevOnblur_kn = table.onblur;
    table.onblur = function (e) {
      if (this._submitting)
        return;
      if (this._prevOnblur_kn)
        this._prevOnblur_kn(e);
      var focusFld = O$(this.id + "::focused");
      focusFld.value = "false";
    };
    O$.addUnloadHandler(table, function () {
      table.onblur = null;
    });

    var focusFld = O$(table.id + "::focused");
    if (focusFld.value == "true") {
      setTimeout(function () {
        table.focus();
      }, 1);
    }

    O$.addUnloadHandler(table, function () {
      O$.Table._deinitializeKeyboardNavigation(table);
    });
  },

  _deinitializeKeyboardNavigation:function (table) {
    table.onfocus = null;
    table.onblur = null;
    /*if (table._focusControl && table._focusControl.parentNode) {
     table._focusControl.parentNode.removeChild(table._focusControl);
     } */
  },

  _scrollToRowIndexes:function (table, rowIndexes) {
    var bodyRows = table.body._getRows();
    O$.scrollElementIntoView(rowIndexes.map(function (i) {
      return i != -1 && bodyRows[i]._rowNode;
    }), true, false);
  },

  _scrollToCells:function (table, cells) {
    var bodyRows = table.body._getRows();
    var columns = table._columns;
    O$.scrollElementIntoView(cells.map(function (cell) {
      if (cell[0] == -1 || cell[1] == null) {
        return false;
      }
      return bodyRows[cell[0]]._rowNode._cells[columns.byId(cell[1])._index];
    }), true, true);
  },

  _combineSelectedRowsWithRange:function (table, baseSelectedRowIndexes, baseRowIndex, rangeEndRowIndex) {
    O$.assert(baseRowIndex, "O$.Table._combineSelectedRowsWithRange: baseRowIndex should be specified");
    O$.assert(rangeEndRowIndex, "O$.Table._combineSelectedRowsWithRange: rangeEndRowIndex should be specified");

    var result = [];
    var alreadyIncludedIndexes = [];
    var rangeStart, rangeEnd;
    if (baseRowIndex < rangeEndRowIndex) {
      rangeStart = baseRowIndex;
      rangeEnd = rangeEndRowIndex;
    } else {
      rangeStart = rangeEndRowIndex;
      rangeEnd = baseRowIndex;
    }

    var i;
    if (baseSelectedRowIndexes) {
      var count = baseSelectedRowIndexes.length;
      for (i = 0; i < count; i++) {
        var idx = baseSelectedRowIndexes[i];
        result.push(idx);
        if (idx >= rangeStart && idx <= rangeEnd)
          alreadyIncludedIndexes.push(idx);
      }
    }

    var bodyRows = table.body._getRows();
    for (i = rangeStart; i <= rangeEnd; i++) {
      if (alreadyIncludedIndexes.indexOf(i) == -1) {
        var row = bodyRows[i];
        if (row._visible)
          result.push(i);
      }
    }
    return result;
  },

  _combineSelectedCellsWithRange:function (table, baseSelectedCellIds, baseCellId, rangeEndCellId) {

    O$.assert(baseCellId, "O$.Table._combineSelectedCellsWithRange: baseCellId should be specified");
    O$.assert(rangeEndCellId, "O$.Table._combineSelectedCellsWithRange: rangeEndCellId should be specified");

    var result = [];
    var alreadyIncludedCellsIds = [];
    O$.extend(alreadyIncludedCellsIds, {
      _contains:function (anCellId) {
        var isContain = false;
        alreadyIncludedCellsIds.forEach(function (cellId) {
          if (anCellId[0] == cellId[0] && anCellId[1] == cellId[1]) {
            isContain = true;
          }
        });
        return isContain;
      }
    });
    var rangeRowStart, rangeRowEnd;
    var rangeColumnStart, rangeColumnEnd;
    var columns = table._columns;
    if (baseCellId[0] < rangeEndCellId[0]) {
      rangeRowStart = baseCellId[0];
      rangeRowEnd = rangeEndCellId[0];
    } else {
      rangeRowStart = rangeEndCellId[0];
      rangeRowEnd = baseCellId[0];
    }

    var tempBaseColumn = columns.byId(baseCellId[1])._index;
    var tempEndColumn = columns.byId(rangeEndCellId[1])._index;
    if (tempBaseColumn < tempEndColumn) {
      rangeColumnStart = tempBaseColumn;
      rangeColumnEnd = tempEndColumn;
    } else {
      rangeColumnStart = tempEndColumn;
      rangeColumnEnd = tempBaseColumn;
    }

    var i;
    var cellId;
    if (baseSelectedCellIds) {
      var count = baseSelectedCellIds.length;
      for (i = 0; i < count; i++) {
        cellId = baseSelectedCellIds[i];
        result.push(cellId);
        if (table._fillDirection == "document") {
          alreadyIncludedCellsIds.push(cellId);
        } else {
          var colIndex = columns.byId(cellId[1])._index;

          if (cellId[0] >= rangeRowStart && cellId[0] <= rangeRowEnd
                  && colIndex >= rangeColumnStart && colIndex <= rangeColumnEnd)
            alreadyIncludedCellsIds.push(cellId);
        }
      }
    }
    var fromUpToDown = function (firstCelllSelected, lastCellSelected) {
      return (firstCelllSelected[0] <= lastCellSelected[0]);
    }(baseCellId, rangeEndCellId);

    for (i = rangeRowStart; i <= rangeRowEnd; i++) {
      var k;
      if (table._fillDirection == "document") {
        var colIndexStart;
        var colIndexEnd;
        if ((i == baseCellId[0] && fromUpToDown) || (i == rangeEndCellId[0] && !fromUpToDown)) {
          colIndexStart = (fromUpToDown) ? columns.byId(baseCellId[1])._index : columns.byId(rangeEndCellId[1])._index;
          colIndexEnd = (rangeRowStart == rangeRowEnd)
                  ? columns.byId(rangeEndCellId[1])._index : columns[columns.length - 1]._index;
        } else if ((i == rangeEndCellId[0] && fromUpToDown) || (i == baseCellId[0] && !fromUpToDown)) {
          colIndexStart = 0;
          colIndexEnd = (fromUpToDown) ? columns.byId(rangeEndCellId[1])._index : columns.byId(baseCellId[1])._index;
        } else if (i != rangeEndCellId[0] && i != baseCellId[0]) { // for rows between start and end
          colIndexStart = columns[0]._index;
          colIndexEnd = columns[columns.length - 1]._index;
        }
        if (colIndexStart > colIndexEnd) {//switch them
          colIndexEnd = colIndexStart + colIndexEnd;
          colIndexStart = colIndexEnd - colIndexStart;
          colIndexEnd = colIndexEnd - colIndexStart;
        }
        for (k = colIndexStart; k <= colIndexEnd; k++) {
          cellId = [i, columns[k].columnId];
          if (!alreadyIncludedCellsIds._contains(cellId)) {
            //if (row._visible)
            result.push(cellId);
          }
        }
      } else {
        for (k = rangeColumnStart; k <= rangeColumnEnd; k++) {
          cellId = [i, columns[k].columnId];
          if (!alreadyIncludedCellsIds._contains(cellId)) {
            //if (row._visible)
            result.push(cellId);
          }
        }
      }
    }

    return result;
  },

  _checkRowNavigation:function (table, idx, rowCount, e) {
    var newIndex = null;
    if (e.upPressed) {
      if (idx == -1)
        newIndex = O$.Table._getNeighboringVisibleRowIndex(table, -1, +1);
      else
        newIndex = O$.Table._getNeighboringVisibleRowIndex(table, idx, -1);
    }
    if (e.downPressed) {
      if (idx == -1)
        newIndex = O$.Table._getNeighboringVisibleRowIndex(table, -1, +1);
      else
        newIndex = O$.Table._getNeighboringVisibleRowIndex(table, idx, +1);
    }
    if (table._selectableItems == "rows" ||
            (table._selectableItems == "cells" && table._fillDirection != "document" && e.ctrlKey )) {
      if (e.homePressed) {
        newIndex = O$.Table._getNeighboringVisibleRowIndex(table, idx, -rowCount);
      }
      if (e.endPressed) {
        newIndex = O$.Table._getNeighboringVisibleRowIndex(table, idx, rowCount);
      }
    }

    if (table._params.scrolling) {
      if (e.pageUpPressed || e.pageDownPressed) {
        var scrollingAreaRect = O$.getElementClientRectangle(table.body._centerScrollingArea._scrollingDiv);
        var row = table.body._rowFromPoint(
                scrollingAreaRect.x + 10,
                e.pageUpPressed ? scrollingAreaRect.getMinY() + 1 : scrollingAreaRect.getMaxY() - 1);
        if (!row) return O$.Table._getNeighboringVisibleRowIndex(table, idx, rowCount);
        var selectedRowY = null;
        if (e.pageUpPressed) {
          if (idx > row._index)
            newIndex = row._index;
          else
            selectedRowY = scrollingAreaRect.getMinY() - scrollingAreaRect.height;
        } else {
          if (idx < row._index)
            newIndex = row._index;
          else
            selectedRowY = scrollingAreaRect.getMaxY() + scrollingAreaRect.height;
        }
        if (selectedRowY != null) {
          row = table.body._rowFromPoint(scrollingAreaRect.x + 10, selectedRowY);
          if (row == null) {
            var rows = table.body._getRows();
            row = e.pageUpPressed
                    ? rows[O$.Table._getNeighboringVisibleRowIndex(table, idx, -rowCount)]
                    : rows[O$.Table._getNeighboringVisibleRowIndex(table, idx, rowCount)];
          }
          newIndex = row._index;
        }

      }
    }

    return newIndex;
  },

  _checkColumnNavigation:function (table, columnId, columnCount, e) {
    var newIndex = null;
    if (e.leftPressed) {
      if (columnId == null)
        newIndex = O$.Table._getNeighboringVisibleColumnId(table, null, +1);
      else
        newIndex = O$.Table._getNeighboringVisibleColumnId(table, columnId, -1);
    }
    if (e.rightPressed) {
      if (columnId == null)
        newIndex = O$.Table._getNeighboringVisibleColumnId(table, null, +1);
      else
        newIndex = O$.Table._getNeighboringVisibleColumnId(table, columnId, +1);
    }
    if (!e.ctrlKey) {
      if (e.homePressed) {
        newIndex = O$.Table._getNeighboringVisibleColumnId(table, columnId, -columnCount);
      }
      if (e.endPressed) {
        newIndex = O$.Table._getNeighboringVisibleColumnId(table, columnId, columnCount);
      }
    }

    return newIndex;
  },

  _checkCellNavigationInDocumentMode:function (table, cellId, e) {
    function getBorderCellForEvent(table, cellId, goDown) {
      var rowId, columnId;
      if (cellId[0] == -1) {
        rowId = O$.Table._getNeighboringVisibleRowIndex(table, -1, +1);
      } else {
        rowId = O$.Table._getNeighboringVisibleRowIndex(table, cellId[0], (goDown) ? +1 : -1);
      }
      if (rowId != null && rowId != cellId[0]) {
        var tLength;
        if (cellId[1] == null)
          tLength = (goDown) ? table._columns.length - 1 : 1 - table._columns.length;
        else
          tLength = (goDown) ?
                  -table._columns.length :
                  table._columns.length;
        columnId = O$.Table._getNeighboringVisibleColumnId(table, cellId[1], tLength);
        if (columnId != null) {
          return [rowId, columnId];
        }
      }
      return null;
    }

    if (e.leftPressed) {
      return getBorderCellForEvent(table, cellId, false);
    }
    if (e.rightPressed) {
      return getBorderCellForEvent(table, cellId, true);
    }
    if (e.ctrlKey) {
      var columns = table._columns;
      if (e.homePressed) {
        return [0, columns[0].columnId];
      }
      if (e.endPressed) {
        return [(table.__getRowCount() - 1), columns[columns.length - 1].columnId];
      }
    }
    return null;
  },

  _getNeighboringVisibleRowIndex:function (table, startRowIndex, stepCount) {
    var bodyRows = table.body._getRows();
    if (stepCount == 0)
      return bodyRows[startRowIndex];
    var dir = (stepCount > 0) ? +1 : -1;
    var rowIndex = startRowIndex;
    var destRowIndex = startRowIndex;
    var stepsRemaining = (stepCount > 0) ? stepCount : -stepCount;
    while (stepsRemaining > 0) {
      rowIndex += dir;
      if (rowIndex < 0 || rowIndex >= bodyRows.length)
        break;
      var row = bodyRows[rowIndex];
      if (row._visible) {
        destRowIndex = rowIndex;
        stepsRemaining--;
      }
    }
    return destRowIndex;
  },

  _getNeighboringVisibleColumnId:function (table, columnId, stepCount) {
    var bodyColumns = table._columns;
    var columnStartIndex;
    if (columnId == null) {
      columnStartIndex = -1;
    } else {
      columnStartIndex = bodyColumns.byId(columnId)._index;
    }
    if (stepCount == 0)
      return bodyColumns[columnStartIndex].columnId;
    var dir = (stepCount > 0) ? +1 : -1;
    var columnIndex = columnStartIndex;
    var destColumnIndex = columnStartIndex;
    var stepsRemaining = (stepCount > 0) ? stepCount : -stepCount;
    while (stepsRemaining > 0) {
      columnIndex += dir;
      if (columnIndex < 0 || columnIndex >= bodyColumns.length)
        break;
      destColumnIndex = columnIndex;
      stepsRemaining--;
    }
    return bodyColumns[destColumnIndex].columnId;
  },

  // -------------------------- TABLE SELECTION SUPPORT

  _initSelection:function (tableId, enabled, required, selectableItems, selectionMode, selectedItems, selectionClass, rawSelectionClass, selectionChangeHandler, postEventOnSelectionChange, selectionColumnIndexes, mouseSupport, keyboardSupport, trackLeafNodesOnly, fillDirection, selectablesCells, cursorStyle) {
    var table = O$.initComponent(tableId);
    table._initializingSelection = true;
    O$.assert(!table._selectionInitialized, "O$.Table._initSelection shouldn't be called twice on the same table");
    O$.extend(table, {
      _selectionInitialized:true,

      _selectionEnabled:!table._params.body.noDataRows && enabled,
      _selectionRequired:required,
      _selectableItems:selectableItems,
      _multipleSelectionAllowed:selectionMode != "single",
      _selectionMode:selectionMode,
      _selectionClass:selectionClass,
      _rawSelectionClass:rawSelectionClass,
      _selectionColumnIndexes:selectionColumnIndexes,
      _selectionMouseSupport:mouseSupport,
      _selectionKeyboardSupport:keyboardSupport && selectionMode != "hierarchical",
      _selectionTrackLeafNodeOnly:trackLeafNodesOnly,
      _fillDirection:fillDirection,
      _selectablesCells:selectablesCells,
      _cursorStyle:cursorStyle,

      _setItemSelected_internal:function (itemIndex, selected) {
        O$.assert(itemIndex, "_setItemSelected: itemIndex should be specified");
        if (this._selectableItems == "rows") {
          if (itemIndex == -1)
            return;
          var rows = this.body._getRows();
          if (itemIndex < 0 || itemIndex >= rows.length)
            throw "Row index out of range: " + itemIndex;
          var row = rows[itemIndex];
          row._selected = selected;
          row._updateStyle();
          O$.Table._setRowSelectionCheckboxesSelected(row, selected);
        } else if (this._selectableItems == "cells") {
          if (itemIndex[0] == -1)
            return;
          var rows = this.body._getRows();
          if (itemIndex[0] < 0 || itemIndex[0] >= rows.length)
            throw "Row index out of range: " + itemIndex[0];

          var row = rows[itemIndex[0]];
          var cells = row._cells;
          for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
            if (cells[cellIndex]._column.columnId == itemIndex[1]) {
              cells[cellIndex]._selected = selected;
              cells[cellIndex]._updateStyle();
              return;
            }
          }
          throw "Column Id is not correct: " + itemIndex[1];
        } else {
          throw "Not supported selectable item type: " + table._selectableItems;
        }
      },

      _getSelectedItems:function () {
        if (!this._selectedItems)
          this._selectedItems = [];
        return [].concat(this._selectedItems);
      },

      _setSelectionFieldValue:function (value) {
        var selectionFieldId = this.id + "::selection";
        var selectionField = O$(selectionFieldId);
        O$.assert(selectionField, "Couldn't find selectionField by id: " + selectionFieldId);
        selectionField.value = value;
      },

      _setSelectedItems:function (items, forceUpdate) {
        var undefinedSelectionRows = [];

        if (table._selectionMode == "hierarchical") {
          var bodyRows = this.body._getRows();
          // first, retain only leaf items in the list of explicitly selected items since parent nodes are
          // going to be selected implicitly
          var correctedItems = [];
          items.forEach(function (rowIndex) {
            var row = bodyRows[rowIndex];
            if (!row._hasChildren || row._childRows.length == 0)
              correctedItems.push(rowIndex);
          });
          if (!table._rootRows && !table._of_treeTableComponentMarker)
            throw "Hierarchical selection can only be used in a TreeTable component";

          function deriveHierarchicalSelectionState(row) {
            if (!row._hasChildren || row._childRows.length == 0) {
              var scheduledForSelection = correctedItems.indexOf(row._index) != -1;
              return scheduledForSelection;
            }

            var rowSelected = undefined; // undefined means "not processed yet", and null means a mixed true/false state, or an "undefined" state
            row._childRows.forEach(function (childRow) {
              var childSelected = deriveHierarchicalSelectionState(childRow);
              if (rowSelected === undefined)
                rowSelected = childSelected;
              if (rowSelected === true && childSelected !== true)
                rowSelected = null;
              if (rowSelected === false && childSelected !== false)
                rowSelected = null;
            });
            if (rowSelected === undefined) {
              // this means that child nodes for this node are not loaded to the client, and we imply an
              // unselected state for such parent node in this case
              rowSelected = false;
            }

            if (!row._pseudoRow) {
              if (rowSelected === true)
                correctedItems.push(row._index);
              if (rowSelected === null)
                undefinedSelectionRows.push(row._index);
              if (rowSelected === false && row._selected == null) {
                // reset formerly undefined rows to their new unselected state here,
                // since they won't be reset in the upcoming _setSelectedItems_internal call (because it treats
                // undefined nodes the same as unselected)
                table._setItemSelected_internal(row._index, false);
              }

            }
            return rowSelected;
          }

          table._entireHierarchySelected = deriveHierarchicalSelectionState({
            _pseudoRow:true,
            _hasChildren:true,
            _childRows:table._rootRows
          });

          items = correctedItems;
        }
        this._setSelectedItems_internal(items, forceUpdate);

        undefinedSelectionRows.forEach(function (rowIndex) {
          table._setItemSelected_internal(rowIndex, null);
        });
      },

      _setSelectedItems_internal:function (items, forceUpdate) {
        if (items == null) items = [];
        var changesArray = [];
        var changesArrayIndexes = [];
        var oldSelectedItemsStr = "";
        var newSelectedItemsStr = "";
        var i;
        if (this._selectableItems == "rows") {
          if (this._selectedItems)
            for (i = 0; i < this._selectedItems.length; i++) {
              var item = this._selectedItems[i];
              if (i > 0)
                oldSelectedItemsStr += ",";
              oldSelectedItemsStr += item;
              changesArray[item] = "unselect";
              changesArrayIndexes.push(item);
            }
          if (!this._multipleSelectionAllowed && items && items.length > 1)
            items = [items[0]];
          if (items.length == 1 && items[0] == -1)
            items = [];
          this._selectedItems = items;
          if (this._selectedItems) {
            for (i = 0; i < this._selectedItems.length; i++) {
              if (i > 0)
                newSelectedItemsStr += ",";
              var itemToSelect = this._selectedItems[i];
              O$.assert(itemToSelect, "table._setSelectedItems: itemToSelect is undefined for index " + i);
              newSelectedItemsStr += itemToSelect;
              if (changesArray[itemToSelect] == "unselect" && !forceUpdate)
                changesArray[itemToSelect] = null;
              else
                changesArray[itemToSelect] = "select";
              changesArrayIndexes.push(itemToSelect);
            }
          }
        } else if (this._selectableItems == "cells") {
          if (this._selectedItems) {
            for (i = 0; i < this._selectedItems.length; i++) {
              var item = this._selectedItems[i];
              if (i > 0)
                oldSelectedItemsStr += ",";
              oldSelectedItemsStr += "[" + item[0] + "," + item[1] + "]";
              changesArray[item] = "unselect";
              changesArrayIndexes.push(item);
            }
          }
          if (!this._multipleSelectionAllowed && items && items.length > 1)
            items = [items[0]];
          if (items.length == 1 && (items[0][0] == -1 || items[0][1] == null))
            items = [];
          function getOnlySelectableCellIdsFrom(cellIds) {
            var columns = table._columns;
            var selectableCells = table._selectablesCells;
            if (!selectableCells || selectableCells.length == 0)
              return cellIds;
            var onlySelectable = [];
            for (var n = 0; n < cellIds.length; n++) {
              var cellId = cellIds[n];
              if (selectableCells[cellId[0]][columns.byId(cellId[1])._index])
                onlySelectable.push(cellId);
            }
            return onlySelectable;
          }

          this._selectedItems = getOnlySelectableCellIdsFrom(items);
          if (this._selectedItems) {
            for (i = 0; i < this._selectedItems.length; i++) {
              if (i > 0)
                newSelectedItemsStr += ",";
              var itemToSelect = this._selectedItems[i];
              O$.assert(itemToSelect, "table._setSelectedItems: itemToSelect is undefined for index " + i);
              newSelectedItemsStr += "[" + itemToSelect[0] + "," + itemToSelect[1] + "]";
              if (changesArray[itemToSelect] == "unselect" && !forceUpdate)
                changesArray[itemToSelect] = null;
              else
                changesArray[itemToSelect] = "select";

              changesArrayIndexes.push(itemToSelect);
            }
          }
        }
        var count = changesArrayIndexes.length;
        for (i = 0; i < count; i++) {
          var changesArrayIndex = changesArrayIndexes[i];
          var change = changesArray[changesArrayIndex];
          if (change) {
            if (change == "select")
              this._setItemSelected_internal(changesArrayIndex, true);
            if (change == "unselect")
              this._setItemSelected_internal(changesArrayIndex, false);
            changesArray[changesArrayIndex] = null;
          }
        }

        var selectionFieldValue = O$.Table._formatSelectedItems(this, this._selectableItems, this._selectedItems);
        this._setSelectionFieldValue(selectionFieldValue);
        if (!this._blockSelectionChangeNotifications && oldSelectedItemsStr != newSelectedItemsStr) {
          if (this._selectionChangeHandlers) {
            for (var handlerIdx = 0, handlerCount = this._selectionChangeHandlers.length;
                 handlerIdx < handlerCount;
                 handlerIdx++) {
              var handler = this._selectionChangeHandlers[handlerIdx];
              var obj = handler[0];
              var methodName = handler[1];
              obj[methodName]();
            }
          }
          if (this._postEventOnSelectionChange) {
            var eventFieldId = this.id + "::selectionEvent";
            var eventField = O$(eventFieldId);
            eventField.value = this._postEventOnSelectionChange;
            window._submittingTable = this;
            setTimeout(function () {
              O$.submitEnclosingForm(window._submittingTable);
            }, 1);
          }
        }
      },

      _selectAllItems:function () {
        O$.assert(this._multipleSelectionAllowed, "table._selectAllItems: multiple selection is not allowed for table: " + this.id);

        if (this._params.body.noDataRows)
          return;
        if (this._selectableItems == "rows") {
          var rows = this.body._getRows();
          var allItems = [];
          for (var i = 0, count = rows.length; i < count; i++)
            allItems[i] = i;
          this._setSelectedItems(allItems);
        } else {
          throw "Not supported selectable item type: " + table._selectableItems;
        }
      },

      _unselectAllItems:function () {
        this._setSelectedItems([]);
      },

      _isItemSelected:function (itemIndex) {
        var result = this._selectedItems.indexOf(itemIndex) != -1;
        return result;
      },

      _toggleItemSelected:function (itemIndex) {
        var selectedIndexes = this._getSelectedItems();
        var newArray = [];
        var i, count;
        if (table._selectableItems == "rows") {
          if (itemIndex == -1) {
            O$.logError("_toggleItemSelected: itemIndex == " + itemIndex);
            return;
          }
          for (i = 0, count = selectedIndexes.length; i < count; i++) {
            var idx = selectedIndexes[i];
            if (idx != itemIndex)
              newArray.push(idx);
          }
        } else if (table._selectableItems == "cells") {
          if (itemIndex == [-1, null]) {
            O$.logError("_toggleItemSelected: itemIndex == " + itemIndex);
            return;
          }
          for (i = 0, count = selectedIndexes.length; i < count; i++) {
            var cellId = selectedIndexes[i];
            if (cellId[0] != itemIndex[0] || cellId[1] != itemIndex[1])
              newArray.push(cellId);
          }
        }

        if (newArray.length == selectedIndexes.length)
          newArray.push(itemIndex);
        this._setSelectedItems(newArray);
      },

      _toggleHierarchicalSelection:function (itemIndex) {
        var bodyRows = this.body._getRows();
        var row = bodyRows[itemIndex];
        var selectedItems = this._getSelectedItems();
        var wasInUndefinedState = row._selected === null;
        var select = wasInUndefinedState ? true : !row._isSelected();
        this._setHierarchicalSelectionForRow(selectedItems, row, select);
        this._setSelectedItems(selectedItems);
      },

      _setHierarchicalSelectionForRow:function (selectedItems, row, select) {
        if (!row._hasChildren || row._childRows.length == 0) {
          var i = selectedItems.indexOf(row._index);
          var selected = (i != -1);
          if (selected && !select)
            selectedItems.splice(i, 1);
          if (!selected && select)
            selectedItems.push(row._index);
          return;
        }
        row._childRows.forEach(function (subRow) {
          table._setHierarchicalSelectionForRow(selectedItems, subRow, select);
        });
      }
    });

    // run initialization code
    var rows = table.body._getRows();
    if (selectableItems == "rows") {
      for (var i = 0, count = rows.length; i < count; i++) {
        var row = rows[i];
        O$.Table._initRowForSelection(row);
      }
      table._setSelectedItems(selectedItems);
    } else if (selectableItems == "cells") {
      if (table._selectionEnabled) {
        var columns = table._columns;
        for (var colIndex = 0; colIndex < columns.length; colIndex++) {
          var col = columns[colIndex];
          col._onresizing = function (index) {
            return function () {
              table._cursorCell._setAsCursor(true);
            }
          }(col._index);
        }
        if (table._cursorStyle) {
          table._cursor = {};
          table._cursor._borderWidth = O$.getStyleClassProperty(table._cursorStyle, "border-top-width").replace("px", "") * 1;
          function createPartOfCursor() {
            var el = document.createElement("div");
            el.style.position = "absolute";
            return el;
          }

          var borderProp = O$.getStyleClassProperty(table._cursorStyle, "border-top");
          table._cursor.left = createPartOfCursor();
          table._cursor.left.style.borderLeft = borderProp;

          table._cursor.top = createPartOfCursor();
          table._cursor.top.style.borderTop = borderProp;

          table._cursor.bottom = createPartOfCursor();
          table._cursor.bottom.style.borderBottom = borderProp;

          table._cursor.right = createPartOfCursor();
          table._cursor.right.style.borderRight = borderProp;
          function removeBorderProperty(styleClass) {
            if (!styleClass)
              return;
            var classNames = styleClass.split(" ");
            var classSelectors = [];
            var i, count;
            for (i = 0, count = classNames.length; i < count; i++) {
              var className = classNames[i];
              if (className)
                classSelectors.push("." + className);
            }
            var cssRules = O$.findCssRules(classSelectors);

            if (!cssRules)
              return;

            for (i = 0, count = cssRules.length; i < count; i++) {
              var style = cssRules[i].style;
              style.border = "";
              style.borderColor = "";
              style.borderWidth = "";
              style.borderStyle = "";
            }
          }

          removeBorderProperty(table._cursorStyle);
        } else {
          table._cursor = null;
        }

        // disable text selection
        //if (O$.isOpera || O$.isExplorer()) {
        function makeUnselectable(node) {
          if (node.nodeType == 1) {
            node.unselectable = "on";
            node.onselectstart = function (evt) {
              O$.cancelEvent(evt);
            }
          }
          var child = node.firstChild;
          while (child) {
            makeUnselectable(child);
            child = child.nextSibling;
          }
        }

        makeUnselectable(table);
        var tables = table.getElementsByTagName("table");
        for (var k = 0; k < tables.length; k++) {
          O$.setStyleMappings(tables[k], {unselectable:"o_table_unselectable"});
        }
        O$.setStyleMappings(table, {unselectable:"o_table_unselectable"});
        for (var rowIndex = 0, countRows = rows.length; rowIndex < countRows; rowIndex++) {
          O$.addEventHandler(rows[rowIndex]._rowNode, "mousedown", function (event) {
            if (!table._isDragSelectionEnabled) {
              var e = O$.getEvent(event);
              var cell = (e.target) ? e.target : e.srcElement;
              cell = (cell._row) ? cell : cell.parentNode;
              var cellId = [cell._row._index, cell._column.columnId];
              table._baseCellIdIfNotDnD = table._baseCellId;
              table._baseCellId = cellId;
              table._baseSelectedCellIds = [cellId];
              table._isDragSelectionEnabled = true;
            }
          });
          O$.addEventHandler(rows[rowIndex]._rowNode, "mouseup", function (event) {
            var e = O$.getEvent(event);
            var cell = (e.target) ? e.target : e.srcElement;
            cell = (cell._row) ? cell : cell.parentNode;
            var cellId = [cell._row._index, cell._column.columnId];
            table._isDragSelectionEnabled = false;
            if (table._baseCellId && (cellId[0] == table._baseCellId[0]) && (cellId[1] == table._baseCellId[1])) {
              table._baseCellId = table._baseCellIdIfNotDnD;
              table._baseSelectedCellIds = [table._baseCellIdIfNotDnD];
              O$.Table._cell_handleSelectionOnClick(event, false);
            }
            table.focus();
          });
          O$.addEventHandler(rows[rowIndex]._rowNode, "mousemove", function (event) {
            if (table._isDragSelectionEnabled) {
              O$.Table._cell_handleSelectionOnClick(event, true);
            }
          });
        }
      }
      selectedItems = function removeCellsIfColumnIsHidden(cells) {
        var validatedCells = [];
        var cols = table._columns;
        for (var i = 0; i < cells.length; i++) {
          var cellId = cells[i];
          if (cols.byId(cellId[1]) != null) {
            validatedCells.push(cellId);
          }
        }
        return validatedCells;
      }(selectedItems);

      function setSelectedCells() {
        function waitAndSetCursor(cursorCell) {
          if (O$.isExplorer) {
            var size = O$.getElementSize(cursorCell);
            if (size.width == 0 || size.height == 0) {
              setTimeout(function () {
                waitAndSetCursor(cursorCell);
              }, 100);
            } else {
              cursorCell._setAsCursor();
            }
          } else {
            cursorCell._setAsCursor();
          }
        }

        table._setSelectedItems(selectedItems);
        if (selectedItems.length != 0) {
          var cellId = selectedItems[selectedItems.length - 1];
          var cursorCell = rows[cellId[0]]._cells[table._columns.byId(cellId[1])._index];
          waitAndSetCursor(cursorCell);
          table._baseCellId = selectedItems[0];
          table._rangeEndCellId = cellId;

        }
      }

      /*At Safari and Chrome this script is started before cell's size  will be correct.
       Thus, we need to wait while table get right appearance
       */
      if (O$.isChrome() || O$.isSafari() || (O$.isQuirksMode() && O$.isExplorer())) {
        setTimeout(function () {
          setSelectedCells();
        }, 100);//todo: listen event,when table is completely rendered
      } else {
        setSelectedCells();
      }
    }
    table._setSelectionFieldValue("");

    if (selectionChangeHandler) {
      eval("table.onchange = function(event) {if (!event._of_event)return;" + selectionChangeHandler + "}");
      // checking _of_event is needed if this is a bubbled event from some child
      table._fireOnSelectionChange = function () {
        O$.sendEvent(table, "change");
      };
      O$.Table._addSelectionChangeHandler(table, [table, "_fireOnSelectionChange"]);
    }
    table._postEventOnSelectionChange = postEventOnSelectionChange;

    table._addRowInsertionCallback(function (table, insertedAfterIndex, insertedRows) {
      var insertedRowCount = insertedRows.length;
      var i;
      for (i = 0; i < insertedRowCount; i++) {
        var insertedRow = insertedRows[i];
        O$.Table._initRowForSelection(insertedRow);
      }
      var selectedItems = table._getSelectedItems();
      if (table._selectableItems != "rows")
        throw "Not supported selectable item type: " + table._selectableItems;

      var selectedItemCount = selectedItems.length;
      if (selectedItemCount > 0) {
        var newSelectedItems = [];
        var selectionChanged = false;
        for (i = 0; i < selectedItemCount; i++) {
          var rowIndex = selectedItems[i];
          if (rowIndex > insertedAfterIndex) {
            rowIndex += insertedRowCount;
            selectionChanged = true;
          }
          newSelectedItems.push(rowIndex);
        }
        table._blockSelectionChangeNotifications = true;
        table._setSelectedItems(newSelectedItems, true);
        table._blockSelectionChangeNotifications = false;
      }
    });

    if (table._selectionRequired && table.__isSelectionEmpty()) {
      if (table._selectableItems == "rows") {
        if (!table._params.body.noDataRows) {
          if (!table._multipleSelectionAllowed)
            table.__setSelectedRowIndex(0);
          else
            table.__setSelectedRowIndexes([0]);
        }
      } else {
        throw "Not supported selectable item type: " + table._selectableItems;
      }
    }
    table._initializingSelection = false;
  },

  _initRowForSelection:function (row) {
    var table = row._table;
    O$.extend(row, {
      _isSelected:function () {
        return table._isItemSelected(this._index);
      }

    });

    if (table._selectionEnabled) {
      [row._leftRowNode, row._rowNode, row._rightRowNode].forEach(function (rowNode) {
        if (!rowNode) return;
        if (rowNode._originalClickHandler)
          O$.logError("O$.Table._initSelection: row click handler already initialized");
        rowNode._originalClickHandler = rowNode.onclick;
        rowNode.onclick = O$.Table._row_handleSelectionOnClick;
        O$.addUnloadHandler(table, function () {
          rowNode.onclick = null;
        });
      });
    }
    var cells = row._cells;
    for (var cellIndex = 0, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
      var cell = cells[cellIndex];
      var cellSpan = O$.Tables._getCellColSpan(cell);
      var colIndex = cell._column._index;
      if ((colIndex != undefined) && (table._selectionColumnIndexes.indexOf(colIndex) != -1))
        O$.Table._initSelectionCell(cell);
      colIndex += cellSpan;
    }
    var inputs;
    if (row.getElementsByTagName)
      inputs = row.getElementsByTagName("input");
    else {
      inputs = [];
      [row._leftRowNode, row._rowNode, row._rightRowNode].forEach(function (rowNode) {
        if (!rowNode) return;
        var elements = rowNode.getElementsByTagName("input");
        for (var i = 0, count = elements.length; i < count; i++) {
          inputs.push(elements[i]);
        }
      });
    }

    function locateSelectionCheckboxes(inputs, row) {
      for (var i = 0, count = inputs.length; i < count; i++) {
        var input = inputs[i];
        if (input.className && input.className.indexOf("o_selectRowCheckbox") != -1) {
          if (!row._selectRowCheckboxes) row._selectRowCheckboxes = [];
          row._selectRowCheckboxes.push(input);
          O$.Table._initSelectRowCheckbox(input, row);
        }
      }
    }

    if (table._initializingSelection)
      locateSelectionCheckboxes(inputs, row);
    else
      setTimeout(function () {
        // This timeout is required in case of expanding tree nodes with Ajax. If such nodes contain selectRowCheckbox'es
        // then these check-boxes have not run their initialization code by the time when this method is invoked, and so
        // are missing the "o_selectRowCheckbox", and cannot be found here yet, so we're doing this asynchronously
        locateSelectionCheckboxes(inputs, row);
        // update checkboxes once more because the previous time when we invoked setTimeout, these check-boxes were not
        // registered
        O$.Table._setRowSelectionCheckboxesSelected(row, row._selected)
      }, 1);
  },

  _addSelectionChangeHandler:function (table, handler) {
    O$.assert(handler, "O$.Table._addSelectionChangeHandler: handler must be specified. table.id = " + table.id);
    var handlers = table._selectionChangeHandlers;
    if (!handlers) {
      handlers = [];
      table._selectionChangeHandlers = handlers;
    }
    handlers.push(handler);
  },

  _initSelectRowCheckbox:function (checkBox, row) {
    var table = row._table;
    checkBox.setSelected(false);
    checkBox.setDisabled(!table._selectionEnabled);
    O$.extend(checkBox, {
      onclick:function (e) {
        var evt = O$.getEvent(e);
        if (!table._selectionEnabled)
          return;
        if (table._selectableItems != "rows")
          return;
        if (!table._multipleSelectionAllowed) {
          if (this.isSelected())
            table._setSelectedItems([row._index]);
          else
            table._setSelectedItems([]);
        } else if (table._selectionMode != "hierarchical") {
          table._toggleItemSelected(row._index);
        } else {
          table._toggleHierarchicalSelection(row._index);
        }
        O$.stopEvent(evt);
      },
      ondblclick:O$.repeatClickOnDblclick
    });
    O$.addUnloadHandler(table, function () {
      checkBox.onclick = null;
      checkBox.ondblclick = null;
    });
  },

  _initSelectionCell:function (cell) {
    var checkBoxAsArray = O$.getChildNodesWithNames(cell, ["input"]);
    if (!checkBoxAsArray || checkBoxAsArray.length == 0)
      return;
    var checkBox = checkBoxAsArray[0];
    if (!checkBox)
      return;
    var row = cell._row;
    var table = row._table;
    O$.extend(cell, {
      _selectionCheckBox:checkBox,
      onclick:function (evt) {
        cell._handlingClick = true;
        try {
          var cellRow = this._row;
          var cellTable = cellRow._table;
          if (!cellTable._selectionEnabled)
            return;
          if (cellTable._multipleSelectionAllowed) {
            if (this._selectionCheckBox.isSelected)
              this._selectionCheckBox.setSelected(!this._selectionCheckBox.isSelected());
            else
              this._selectionCheckBox.checked = !this._selectionCheckBox.checked;
            this._selectionCheckBox._handleClick(evt);
          } else {
            if (this._selectionCheckBox.isSelected) {
              if (!this._selectionCheckBox.isSelected()) {
                this._selectionCheckBox.setSelected(true);
              }
            } else {
              if (!this._selectionCheckBox.checked) {
                this._selectionCheckBox.checked = true;
              }
            }
            this._selectionCheckBox.onclick(evt);
          }
        } finally {
          cell._handlingClick = false;
        }
      },
      ondblclick:O$.repeatClickOnDblclick
    });
    O$.addUnloadHandler(table, function () {
      cell.onclick = null;
      cell.ondblclick = null;
    });

    O$.extend(checkBox, {
      checked:false,
      // fix for Mozilla's issue: reloading a page retains previous values for inputs regardless of their values received from server
      disabled:!table._selectionEnabled,

      _cell:cell,

      onclick:function (e) {
        this._handleClick(e);
      },

      _handleClick:function (e) {
        var evt = O$.getEvent(e);
        var checkBoxCell = this._cell;
        if (!checkBoxCell._handlingClick)
          return;
        var checkBoxRow = checkBoxCell._row;
        var checkBoxTable = checkBoxRow._table;
        if (!checkBoxTable._selectionEnabled)
          return;
        if (checkBoxTable._selectableItems != "rows")
          return;
        if (!checkBoxTable._multipleSelectionAllowed) {
          if (this.checked)
            checkBoxTable._setSelectedItems([checkBoxRow._index]);
          else
            checkBoxTable._setSelectedItems([]);
        } else if (checkBoxTable._selectionMode != "hierarchical") {
          checkBoxTable._toggleItemSelected(row._index);
        } else {
          checkBoxTable._toggleHierarchicalSelection(row._index);
        }
        O$.stopEvent(evt);
      },
      ondblclick:function (evt) {
        if (O$.isExplorer())
          this.click(evt);
        O$.stopEvent(evt);
      }
    });
    O$.addUnloadHandler(table, function () {
      checkBox.onclick = null;
      checkBox.ondblclick = null;
    });

    var cellRow = cell._row;
    if (!cellRow._selectionCheckBoxes)
      cellRow._selectionCheckBoxes = [];
    cellRow._selectionCheckBoxes.push(checkBox);
  },

  _row_handleSelectionOnClick:function (evt) {
    if (this._originalClickHandler)
      this._originalClickHandler(evt);

    var e = O$.getEvent(evt);
    var row = this._row ? this._row : this;
    var table = row._table;
    if (!table._selectionMouseSupport)
      return;
    if (table._selectableItems == "rows") {
      table._baseRowIndex = null;
      table._baseSelectedRowIndexes = null;
      table._rangeEndRowIndex = null;
      if (!table._multipleSelectionAllowed) {
        table._setSelectedItems([row._index]);
      } else if (table._selectionMode != "hierarchical") {
        if (e.ctrlKey || e.metaKey) {
          table._toggleItemSelected(row._index);
          var newSelectedRowIndexes = table.__getSelectedRowIndexes();
          table._baseRowIndex = (newSelectedRowIndexes.indexOf(row._index) != -1) ? row._index : null;
          table._baseSelectedRowIndexes = newSelectedRowIndexes;
          table._rangeEndRowIndex = null;
        } else
          table._setSelectedItems([row._index]);
      } else {
        // don't change hierarchical selection on row click
      }
    }
  },

  _cell_handleSelectionOnClick:function (evt, isScrollEnabled) {
    if (this._originalClickHandler)
      this._originalClickHandler(evt);

    var e = O$.getEvent(evt);
    var cell = (e.target) ? e.target : e.srcElement;
    cell = (cell._row) ? cell : cell.parentNode;
    var table = cell._row._table;
    if (!table._selectionMouseSupport)
      return;
    if (table._selectableItems == "cells") {
      table._baseRowIndex = null;
      table._baseSelectedRowIndexes = null;
      table._rangeEndRowIndex = null;
      var cellId = [cell._row._index, cell._column.columnId];
      var bodyRows = table.body._getRows();
      var columns = table._columns;
      var cursorCell;
      if (!table._multipleSelectionAllowed) {
        table._setSelectedItems([cellId]);
        cursorCell = bodyRows[cellId[0]]._cells[columns.byId(cellId[1])._index];
        cursorCell._setAsCursor();
      } else {
        var newSelectedCellIds;
        if (e.ctrlKey) {
          table._toggleItemSelected(cellId);
          newSelectedCellIds = table.__getSelectedCellIds();
          table._baseCellId = cellId;
          table._baseSelectedCellIds = newSelectedCellIds;
          table._rangeEndCellId = null;
          table._ctrlForSelectionWasPressed = true;
        } else if (e.shiftKey || table._isDragSelectionEnabled) {
          var baseCellId = table._baseCellId;
          var baseCells = table.__getSelectedCellIds();
          if (baseCellId == null) {
            baseCellId = (baseCells.length != 0 && baseCells[0] != [-1, null]) ? baseCells[0] : cellId;
            table._baseCellId = baseCellId;
            table._baseSelectedCellIds = [baseCellId];
          } else if (table._ctrlForSelectionWasPressed) {
            table._baseSelectedCellIds = baseCells;
            table._ctrlForSelectionWasPressed = false;
          }
          var newSelectedCellIdsIndexes = O$.Table._combineSelectedCellsWithRange(table, table._baseSelectedCellIds, baseCellId, cellId);
          table._rangeEndCellId = cellId;
          table._setSelectedItems(newSelectedCellIdsIndexes);
        } else {
          table._baseCellId = null;
          table._baseSelectedCellIds = null;
          table._rangeEndCellId = null;
          table._setSelectedItems([cellId]);
        }
        if (isScrollEnabled) {
          function prepareCellsRectangleToScroll(cellId) {
            var cellsToScroll = [cellId];
            if (cellId[0] != 0) {
              cellsToScroll.push([(cellId[0] - 1), cellId[1]]);
            }
            if (cellId[0] != ( bodyRows.length - 1 )) {
              cellsToScroll.push([(cellId[0] + 1), cellId[1]]);
            }
            var columnIndex = columns.byId(cellId[1])._index;
            if (cellId[1] != columns[0].columnId) {
              cellsToScroll.push([cellId[0], columns[columnIndex - 1].columnId]);
            }
            if (cellId[1] != columns[columns.length - 1].columnId) {
              cellsToScroll.push([cellId[0], columns[columnIndex + 1].columnId]);
            }
            return cellsToScroll;
          }

          O$.Table._scrollToCells(table, prepareCellsRectangleToScroll(cellId));
        }
        cursorCell = bodyRows[cellId[0]]._cells[columns.byId(cellId[1])._index];
        cursorCell._setAsCursor();
      }
    } else {
      throw "This method should been called only if cellSelection is turned on";
    }
  },

  _formatSelectedItems:function (table, selectableItems, selectedItemIndexes) {
    if (selectableItems == "rows" || selectableItems == "columns") {
      var result = "[";
      var bodyRows = table.body._getRows();
      for (var i = 0; i < selectedItemIndexes.length; i++) {
        var itemIndex = selectedItemIndexes[i];
        if (table._selectionTrackLeafNodeOnly) {
          var row = bodyRows[itemIndex];
          if (row._hasChildren) continue;
        }

        if (result.length > 1)
          result += ",";
        result += itemIndex;
      }
      result += "]";
      return result;
    } else if (selectableItems == "cells") {
      var result = "[";
      for (var i = 0; i < selectedItemIndexes.length; i++) {
        var itemIndex = selectedItemIndexes[i];

        if (result.length > 1)
          result += ",";
        result += "[";
        result += itemIndex[0];
        result += ",";
        result += itemIndex[1];
        result += "]";
      }
      result += "]";
      return result;
    }
    throw "O$.Table._formatSelectedItems: unknown selectableItems: " + selectableItems;
  },

  _setRowSelectionCheckboxesSelected:function (row, selected) {
    if (row._selectionCheckBoxes)
      row._selectionCheckBoxes.forEach(function (checkbox) {
        if (checkbox.isSelected) {
          if (selected == null)
            checkbox.setDefined(false);
          else
            checkbox.setSelected(selected);
        } else
          checkbox.checked = selected;
      });

    if (row._selectRowCheckboxes)
      row._selectRowCheckboxes.forEach(function (checkbox) {
        if (selected == null)
          checkbox.setDefined(false);
        else
          checkbox.setSelected(selected);
      });
  },

  _initSelectAllCheckbox:function (checkBoxId, tableId, columnIndex) {
    var selectAllCheckbox = O$(checkBoxId);
    var table = O$(tableId);
    if (!table)
      throw "SelectAllCheckbox must be placed in a header or footer of <o:dataTable> or <o:treeTable> component. clientId = " + checkBoxId;
    selectAllCheckbox.style.cursor = "default";

    function initForCheckboxColumn() {
      if (!table._checkBoxColumnHeaders)
        table._checkBoxColumnHeaders = [];
      if (!table._checkBoxColumnHeaders[columnIndex])
        table._checkBoxColumnHeaders[columnIndex] = [];
      var colHeadersArray = table._checkBoxColumnHeaders[columnIndex];
      colHeadersArray.push(selectAllCheckbox);

      O$.extend(selectAllCheckbox, {
        _getColumn:function () {
          if (!this._column)
            this._column = table._columns[columnIndex];
          return this._column;
        },

        _updateState:function () {
          var col = this._getColumn();
          var cells = col && col.body ? col.body._cells : [];
          var checkedCount = 0;
          var checkboxCount = 0;
          for (var i = 0, count = cells.length; i < count; i++) {
            var cell = cells[i];
            if (!cell) continue;
            var checkBox = cell._checkBox;
            if (!checkBox) continue;
            checkboxCount++;
            if (checkBox.isSelected ? checkBox.isSelected() : checkBox.checked)
              checkedCount++;
          }
          if (checkedCount == 0)
            this.setSelected(false);
          else if (checkedCount == checkboxCount)
            this.setSelected(true);
          else
            this.setDefined(false);
        },

        onclick:function (e) {
          var col = this._getColumn();

          function setAllColumnCheckboxesSelected(col, checked) {
            var cells = col.body ? col.body._cells : [];
            for (var i = 0, count = cells.length; i < count; i++) {
              var cell = cells[i];
              if (cell && cell._checkBox) { // to account for the "no data" row
                if (cell._checkBox.setSelected)
                  cell._checkBox.setSelected(checked);
                else
                  cell._checkBox.checked = checked;
              }
            }
          }

          setAllColumnCheckboxesSelected(col, this.isSelected());
          col._updateHeaderCheckBoxes();
          col._updateSubmissionField();
          col._fireOnChange();
          O$.stopEvent(e);
        }
      });
      O$.addUnloadHandler(table, function () {
        selectAllCheckbox.onclick = null;
      });

    }

    function initForSelection() {
      O$.extend(selectAllCheckbox, {
        _updateState:function () {
          if (!table._getSelectedItems) {
            // wait for table selection to be initialized
            setTimeout(function () {
              selectAllCheckbox._updateState();
            }, 30);
            return;
          }
          var selectedItems = table._getSelectedItems();
          var bodyRows = table.body._getRows();
          if (selectedItems.length == 0) {
            this.setSelected(false);
          } else if (selectedItems.length == bodyRows.length) {
            this.setSelected(true);
          } else {
            this.setDefined(false);
          }
        },

        onclick:function (e) {
          //Fix bug OF-229
          e = e || window.event;
          if (e.pageX == null && e.clientX != null) {
            var html = document.documentElement;
            var body = document.body;
            e.pageX = e.clientX + (html && html.scrollLeft || body && body.scrollLeft || 0) - (html.clientLeft || 0);
            e.pageY = e.clientY + (html && html.scrollTop || body && body.scrollTop || 0) - (html.clientTop || 0);
          }
          if ((e.pageX != 0) && (e.pageY != 0)) {
            if (this.isSelected())
              table._selectAllItems();
            else
              table._unselectAllItems();
          }
          O$.stopEvent(e);
        }

      });
      O$.addUnloadHandler(table, function () {
        selectAllCheckbox.onclick = null;
      });
      O$.Table._addSelectionChangeHandler(table, [selectAllCheckbox, "_updateState"]);
    }

    if (columnIndex != null) {
      initForCheckboxColumn();
    } else
      initForSelection();

    O$.extend(selectAllCheckbox, {
      _guaranteedStopEventOnClickRequested:true,

      ondblclick:function (e) {
        if (O$.isExplorer())
          this.click();
        var evt = O$.getEvent(e);
        O$.stopEvent(e);
      }
    });
    O$.addUnloadHandler(table, function () {
      selectAllCheckbox.ondblclick = null;
    });

    setTimeout(function () {
      selectAllCheckbox._updateState()
    }, 10);
  },

  // -------------------------- CHECKBOX COLUMN SUPPORT

  _setCheckboxColValues:function (tableId, colIndex, checkedRowIndexes) {
    var table = O$(tableId);
    var columnObj = table._columns[colIndex];
    columnObj._setCheckedIndexes(checkedRowIndexes);
  },

  _initCheckboxColumn:function (tableId, colIndex, valueFieldName, checkedRowIndexes, changeHandler) {
    var table = O$(tableId);
    var col = table._columns[colIndex];

    O$.extend(col, {
      _valueFieldName:valueFieldName,
      _setCheckedIndexes:function (checkedIndexes) {

        function initCheckboxCell(cell, column) {
          if (cell._checkBoxCellInitialized)
            return;
          cell._checkBoxCellInitialized = true;
          var checkBoxAsArray = O$.getChildNodesWithNames(cell, ["input"]);
          var checkBox = checkBoxAsArray[0];
          if (!checkBox)
            return;
          checkBox._cell = cell;
          O$.extend(cell, {
            _checkBox:checkBox,
            _column:column,

            onclick:function (e) {
              var evt = O$.getEvent(e);
              if (evt._checkBoxClickProcessed) {
                O$.stopEvent(evt);
                return;
              }
              this._checkBox.checked = !this._checkBox.checked;
              this._processCheckboxChange();
              O$.cancelEvent(evt);
            },

            ondblclick:O$.repeatClickOnDblclick,

            _processCheckboxChange:function () {
              var col = this._column;
              col._updateHeaderCheckBoxes();
              col._updateSubmissionField();
              col._fireOnChange();
            }
          });
          O$.addUnloadHandler(table, function () {
            cell.onclick = null;
            cell.ondblclick = null;
          });

          O$.extend(checkBox, {
            onclick:function (e) {
              var evt = O$.getEvent(e);
              var checkBoxCell = this._cell;
              checkBoxCell._processCheckboxChange();
              evt._checkBoxClickProcessed = true;
              O$.stopEvent(evt);
            },

            ondblclick:function (e) {
              if (O$.isExplorer())
                this.click(e);
              O$.stopEvent(e);
            }
          });
          O$.addUnloadHandler(table, function () {
            checkBox.onclick = null;
            checkBox.ondblclick = null;
          });
        }

        var bodyCells = col.body ? col.body._cells : [];
        for (var i = 0, count = bodyCells.length; i < count; i++) {
          var cell = bodyCells[i];
          if (!cell)
            continue;
          cell._column = col;

          initCheckboxCell(cell, col);
          if (cell._checkBox) {
            cell._checkBox.checked = checkedIndexes.indexOf(i) != -1;
          }
        }
        col._updateSubmissionField();
      },

      _updateHeaderCheckBoxes:function () {
        if (!this._headers)
          return;
        for (var i = 0, count = this._headers.length; i < count; i++) {
          var header = this._headers[i];
          header._updateState(col);
        }
      }

    });

    col._updateSubmissionField = function () {
      var bodyCells = col.body ? col.body._cells : [];
      var selectedRows = "";
      for (var i = 0, count = bodyCells.length; i < count; i++) {
        var cell = bodyCells[i];
        if (!cell)
          continue;
        if (cell._checkBox && cell._checkBox.checked) {
          if (selectedRows.length > 0)
            selectedRows += ",";
          selectedRows += i;
        }
      }
      O$.setHiddenField(table, this._valueFieldName, selectedRows);
    };

    col._setCheckedIndexes(checkedRowIndexes);

    if (table._checkBoxColumnHeaders) {
      col._headers = table._checkBoxColumnHeaders[colIndex];
    }
    col._updateHeaderCheckBoxes();

    if (changeHandler) {
      eval("col.onchange = function(event) {if (!event._of_event)return;" + changeHandler + "}");
      // checking _of_event is needed if this is a bubbled event from some child
    }
    col._fireOnChange = function () {
      if (changeHandler)
        O$.sendEvent(col, "change");
    };


  },

  // -------------------------- TABLE SORTING SUPPORT

  _initSorting:function (tableId, sortingRules, sortableColumnsIds, sortedColIndex, sortableHeaderClass, sortableHeaderRolloverClass, sortedColClass, sortedColHeaderClass, sortedColBodyClass, sortedColFooterClass, sortedAscImageUrl, sortedDescImageUrl, unsortedStateAllowed) {
    var table = O$.initComponent(tableId, null, {
      sorting:{
        _sortingRules:sortingRules != null ? sortingRules : [],

        getSortingRules:function () {
          return this._sortingRules;
        },

        setSortingRules:function (rules) {
          this._sortingRules = rules;
          var setSortingRulesStr = JSON.stringify(rules, ["columnId", "ascending"]);
          O$._submitInternal(table, null, [
            [table.id + "::setSortingRules", setSortingRulesStr]
          ]);
        },

        _getPrimarySortingRule:function () {
          var sortingRules = table.sorting.getSortingRules();

          if (sortingRules.length == 0) return null;
          var rule = sortingRules[0];
          return new O$.Table.SortingRule(rule.columnId, rule.ascending);
        },

        _setPrimarySortingRule:function (rule) {
          var sortingRules = table.sorting.getSortingRules();
          if (rule == null) {
            sortingRules = [];
          } else {
            sortingRules = [].concat(sortingRules);
            sortingRules[0] = new O$.Table.SortingRule(rule.columnId, rule.ascending);
          }
          table.sorting.setSortingRules(sortingRules);
        },

        sortedAscendingImageUrl:sortedAscImageUrl,
        sortedDescendingImageUrl:sortedDescImageUrl
      }
    });
    table._sortableHeaderRolloverClass = sortableHeaderRolloverClass;
    table._sortableColumnsIds = sortableColumnsIds;
    O$.assert(table, "Couldn't find table by id: " + tableId);

    O$.preloadImages([sortedAscImageUrl, sortedDescImageUrl]);

    table._columns.forEach(function (column) {
      column._sortable = sortableColumnsIds.indexOf(column.columnId) >= 0;
      if (!column._sortable)
        return;

      var colHeader = column.header ? column.header._cell : null;
      if (!colHeader)
        return;

      O$.setStyleMappings(colHeader, {sortableHeaderClass:sortableHeaderClass});

      O$.initUnloadableComponent(colHeader);
      O$.addEventHandler(colHeader, "click", function () {
        var focusField = O$(table.id + "::focused");
        if (focusField)
          focusField.value = true; // set true explicitly before it gets auto-set when the click bubbles up (JSFC-801)
        var columnIndex = column._index;

        var columnId = table._columns[columnIndex].columnId;
        var rule = table.sorting._getPrimarySortingRule();
        if (rule == null)
          rule = new O$.Table.SortingRule(columnId, true);
        else {
          if (rule.columnId == columnId)
            if (rule.ascending) {
              rule.ascending = false;
            } else {
              if (unsortedStateAllowed)
                rule = null;
              else
                rule.ascending = true;
            }
          else {
            rule.columnId = columnId;
            rule.ascending = true;
          }
        }
        table.combineSubmissions(function () {
          table.sorting._setPrimarySortingRule(rule);
          if (table.grouping && table.grouping._groupOnHeaderClick) {
            table.grouping.setGroupingRules([new O$.Table.GroupingRule(rule.columnId, rule.ascending)]);
          }
        });
      });

      O$.setupHoverStateFunction(colHeader, function (mouseInside) {
        O$.setStyleMappings(colHeader, {
          sortableHeaderRolloverClass:mouseInside ? sortableHeaderRolloverClass : null});
      });
    });

    if (sortedColIndex != -1) {
      var sortedColumn = table._columns[sortedColIndex];
      // Applying style to cells is needed for sorted column styles to have priority over
      // even/odd row styles - for backward compatibility with versions earlier than 1.2.2 (JSFC-2884)
      sortedColumn._forceUsingCellStyles = true;

      var headerCell = (sortedColumn.header) ? sortedColumn.header._cell : null;
      if (headerCell)
        O$.Tables._setCellStyleMappings(headerCell, {
          sortedColClass:(table._params.forceUsingCellStyles || sortedColumn._useCellStyles) ? sortedColClass : null,
          sortedColHeaderClass:sortedColHeaderClass});

      O$.setStyleMappings(sortedColumn, {sortedColClass:sortedColClass});
      O$.setStyleMappings(sortedColumn.body, {sortedColBodyClass:sortedColBodyClass});
      sortedColumn._updateStyle();

      var footerCell = sortedColumn.footer ? sortedColumn.footer._cell : null;
      if (footerCell)
        O$.Tables._setCellStyleMappings(footerCell, {
          sortedColClass:(table._params.forceUsingCellStyles || sortedColumn._useCellStyles) ? sortedColClass : null,
          sortedColFooterClass:sortedColFooterClass});
    }

  },

  _performPaginatorAction:function (tableId, field, paramName, paramValue) {
    if (!field) {
      // focus the table after pagination
      var focusedFld = O$(tableId + "::focused");
      if (focusedFld) focusedFld.value = "true";
    }
    O$._submitComponentWithField(tableId, field, [
      [paramName, paramValue]
    ]);
  },

  // -------------------------- COLUMN RESIZING SUPPORT

  _initColumnResizing:function (tableId, retainTableWidth, minColWidth, resizeHandleWidth, columnParams, autoSaveState) {
    var thisRef = this;
    var args = arguments;
    var table = O$(tableId)
    var visibleParent = O$.isVisibleParentRecursive(table)
    O$.addLoadEvent(function () {
      if (!O$.isVisible(visibleParent) && visibleParent != null) {
        setTimeout(function () {
          O$.Table._initColumnResizing.apply(thisRef, args);
          args = null;
          thisRef = null;
        }, 100);
        return;
      }

      if (minColWidth == null)
        minColWidth = 10;

      if (resizeHandleWidth == null)
        resizeHandleWidth = 7;
      else
        resizeHandleWidth = O$.calculateNumericCSSValue(resizeHandleWidth);
      if (resizeHandleWidth < 1)
        resizeHandleWidth = 1;
      // this offset moves the resize handle slightly to the right to reduce overlapping space conflict between column
      // menu invoker and resize handle
      var resizeHandleOffset = 1;

      var tableBordersCollapsed = O$.getElementStyle(table, "border-collapse") == "collapse";
      var colWidthsFieldId = table.id + "::colWidths";
      var colWidthsField = O$.setHiddenField(table, colWidthsFieldId);

      function recalculateTableWidth(colWidths) {
        if (table._params.scrolling)
          return undefined;
        var totalWidth = 0;
        for (var i = 0, count = table._columns.length; i < count; i++) {
          var column = table._columns[i];
          var thisColumnWidth = colWidths ? colWidths[i] : column.getWidth();
          totalWidth += thisColumnWidth;
        }

        var borderLeft = O$.getNumericElementStyle(table, "border-left-width", true);
        var borderRight = O$.getNumericElementStyle(table, "border-right-width", true);
        if (tableBordersCollapsed) {
          if (O$.isOpera() || O$.isSafari()) {
            borderLeft = Math.floor(borderLeft / 2);
            borderRight = Math.ceil(borderRight / 2);
          }
        }
        var width = totalWidth + borderLeft + borderRight;
        table.style.width = width + "px";
        return width;
      }

      function getColWidths() {
        return table._columns.map(function (col) {
          return col.getWidth();
        });
      }

      table._columns.forEach(function (col) {
        var thisColumnParams = columnParams[col._index];
        if (thisColumnParams) {
          col._resizable = thisColumnParams.resizable;
          col._minResizingWidth = thisColumnParams.minWidth;
        }
        if (col._resizable == undefined)
          col._resizable = true;
        if (col._minResizingWidth == undefined)
          col._minResizingWidth = minColWidth;
        col._minResizingWidth = O$.calculateNumericCSSValue(col._minResizingWidth);
        if (col._minResizingWidth < 0)
          col._minResizingWidth = 0;
      });

      table._addCellInsertionCallback(function (cell/*, row, column*/) {
        cell.style.overflow = "hidden";
      });

      table._columns.forEach(function (column) {
        var headerCell = column.header && column.header._cell;
        if (!headerCell) return;

        var widthCompensationColIndex = -1;
        if (retainTableWidth) {
          var cols = !table._params.scrolling || !table._params.scrolling.horizontal
                  ? table._columns
                  : function () {
            var verticalArea = column._verticalArea;
            if (verticalArea == table._centerArea) return [];
            if (verticalArea == table._leftArea)
              return column != verticalArea._columns[verticalArea._columns.length - 1]
                      ? verticalArea._columns
                      : [];
            return verticalArea._columns;
          }();
          for (var idx = cols.length - 1; idx >= 0; idx--) {
            var c = cols[idx];
            if (c._resizable) {
              widthCompensationColIndex = c._index;
              break;
            }
          }
        }
        if (widthCompensationColIndex != -1 && column._index >= widthCompensationColIndex)
          return;

        column._widthCompensationColIndex = widthCompensationColIndex;
        if (!column._resizable)
          return;
        var resizeHandle = document.createElement("div");
        resizeHandle.style.cursor = "e-resize";
        resizeHandle.style.position = "absolute";
        resizeHandle.style.border = "0px none transparent";

        if (O$.isExplorer()) {
          // IE needs an explicit background because otherwise this absolute div will "leak" some events to the underlying
          // component (when a mouse is directly over any of table's gridline)
          resizeHandle.style.background = "silver";
          resizeHandle.style.filter = "alpha(opacity=0)";
        }

        headerCell.appendChild(resizeHandle);
        O$.correctElementZIndexOptimized(resizeHandle, headerCell, O$.Table.HEADER_CELL_Z_INDEX_COLUMN_MENU_RESIZE_HANDLE);
        column._resizeHandle = resizeHandle;
        resizeHandle._dragEl = column;
        O$.extend(resizeHandle, {
          _column:column,
          onmouseover:function () {
            if (this._draggingInProgress)
              return;
            if (!table.parentNode)
              this.parentNode.removeChild(this);
            else
              this._updatePos();
            // don't let parent header cell hover to be activated since the handle is logically out of column (IE)
            if (!table._showingMenuForColumn)
              table._columns.forEach(function (c) {
                var headerCell = c.header && c.header._cell;
                if (headerCell && headerCell.setForceHover) headerCell.setForceHover(false);
              });
          },
          onmouseout:function () {
            if (table._columnResizingInProgress) return;
            // don't let parent header cell hover to be activated since the handle is logically out of column (IE)
            if (!table._showingMenuForColumn)
              setTimeout(function () {
                table._columns.forEach(function (c) {
                  var headerCell = c.header && c.header._cell;
                  if (headerCell && headerCell.setForceHover) headerCell.setForceHover(null);
                });
              }, 1);
          },
          onmousedown:function (e) {
            setTimeout(function () {
              table._columns.forEach(function (c) {
                var headerCell = c.header && c.header._cell;
                if (headerCell && headerCell.setForceHover) headerCell.setForceHover(false);
              });
            }, 1);
            O$.startDragging(e, this);
          },
          onclick:function (e) {
            O$.cancelEvent(e);
          },
          ondragstart:function () {
            table._columnResizingInProgress = true;
            var resizeDecorator = document.createElement("div");
            resizeDecorator.style.position = "absolute";
            resizeDecorator.style.borderLeft = "0px none transparent";//"1px solid gray";
            //            table.parentNode.appendChild(resizeDecorator);
            this._column._resizeDecorator = resizeDecorator;
            resizeDecorator._column = this._column;

            resizeDecorator._updatePos = function () {
              var cellPos = O$.getElementBorderRectangle(headerCell, resizeHandle);
              var tablePos = O$.getElementPos(table, true);

              this.style.top = cellPos.getMinY() + "px";
              this.style.left = cellPos.getMaxX() + "px";
              this.style.width = "0px";//"1px";
              this.style.height = tablePos.y + table.offsetHeight - cellPos.getMinY() + "px";
            };
            //            resizeDecorator._updatePos();
            this._dragStartCellPos = O$.getElementBorderRectangle(headerCell, this);
          },
          setLeft:function (left) {
            this.style.left = left + "px";
            var newColRightEdge = left + Math.floor(resizeHandleWidth / 2) + 1 - resizeHandleOffset;
            var newColWidth = newColRightEdge - this._dragStartCellPos.getMinX();
            if (newColWidth < this._column._minResizingWidth)
              newColWidth = this._column._minResizingWidth;

            var colWidths = getColWidths();
            if (!table._params.scrolling) {
              table.style.width = "auto";
            } else {
              var scrollingDiv = table.body._centerScrollingArea._scrollingDiv;
              var scrollLeft = scrollingDiv.scrollLeft;
              var scrollTop = scrollingDiv.scrollTop;
              this._column._verticalArea._areas.forEach(function (a) {
                if (!a._table) return;
                a._table.style.width = "auto";
              });
            }
            var thisAndNextColWidth;
            var nextCol;
            if (this._column._widthCompensationColIndex != -1) {
              var nextColWidth = colWidths[this._column._widthCompensationColIndex];
              var thisColWidth = colWidths[this._column._index];
              thisAndNextColWidth = thisColWidth + nextColWidth;
              nextCol = table._columns[this._column._widthCompensationColIndex];
              var maxWidthForThisCol = thisAndNextColWidth - nextCol._minResizingWidth;
              if (newColWidth > maxWidthForThisCol)
                newColWidth = maxWidthForThisCol;
              var newNextColWidth = thisAndNextColWidth - newColWidth;
              nextCol.setWidth(newNextColWidth);
              colWidths[this._column._widthCompensationColIndex] = newNextColWidth;
            }

            this._column.setWidth(newColWidth);
            colWidths[this._column._index] = newColWidth;

            if (!table._params.scrolling) {
              if (!retainTableWidth)
                recalculateTableWidth(colWidths);
              else
                table.style.width = table._originalWidth + "px";
            } else {
              table._params.scrolling._widthAlignmentDisabled = true;
              try {
                this._column._verticalArea.updateWidth();
              } finally {
                table._params.scrolling._widthAlignmentDisabled = false;
              }
              scrollingDiv.scrollLeft = scrollLeft;
              scrollingDiv.scrollTop = scrollTop;
            }
            this._column._resizeDecorator._updatePos();
            O$.repaintAreaForOpera(table);
            table._fixFF3ColResizingIssue();
            if (table._params.scrolling)
              O$.invokeFunctionAfterDelay(table._alignRowHeights, 500);
          },
          setTop:function (top) {
            this.style.top = top + "px";
          },
          ondragend:function () {
            table._columnResizingInProgress = false;
            //            this._column._resizeDecorator.parentNode.removeChild(this._column._resizeDecorator);
            updateResizeHandlePositions();

            var totalWidth = 0;
            var colWidths = [];
            for (var i = 0, count = table._columns.length; i < count; i++) {
              var col = table._columns[i];
              var colWidth = col.getWidth();
              colWidths[i] = colWidth + "px";
              totalWidth += colWidth;
            }

            colWidthsField.value = (O$.isOpera() ? table.style.width : totalWidth + "px") + ":" +
                    "[" + colWidths.join(",") + "]";
            if (autoSaveState) {
              if (table._params.additionalParams.forceAjax)
                O$.Ajax.requestComponentPortions(table.id, ["columnResizingState"], null, function () {
                  // no client-side updates are required -- the request was just for saving data
                }, null, true, [table.id + "::columnsOrder", table.getColumnsOrder()])
              else
                O$.Ajax.requestComponentPortions(table.id, ["columnResizingState"], null, function () {
                  // no client-side updates are required -- the request was just for saving data
                }, null, true);
            }
            if (table._focusable) {
              if (!table._focused)
                table.focus();
            }
          },
          _updatePos:function () {
            var parentColumn = null;
            for (var col = this._column; col._parentColumn; col = col._parentColumn) {
              var indexAmongSiblings = col._parentColumn.subColumns.indexOf(col);
              var lastColInGroup = indexAmongSiblings == col._parentColumn.subColumns.length - 1;
              if (!lastColInGroup)
                break;
              if (col._parentColumn.header && col._parentColumn.header._cell)
                parentColumn = col._parentColumn;
            }
            function isCellVisible(cell) {
              if (!cell || !O$.isVisible(cell)) return false;
              var row = cell.parentNode;
              return O$.isVisible(row);
            }

            var bottomCell = this._column.subHeader && isCellVisible(this._column.subHeader._cell)
                    ? this._column.subHeader._cell : this._column.header._cell;
            var bottomCellPos = O$.getElementBorderRectangle(bottomCell, this);

            var topCellPos = parentColumn
                    ? O$.getElementBorderRectangle(parentColumn.header._cell, this)
                    : O$.getElementBorderRectangle(this._column.header._cell, this);

            var minY = topCellPos.getMinY();
            var x = bottomCellPos.getMaxX() - Math.floor(resizeHandleWidth / 2) - 1 + resizeHandleOffset;
            var y = minY;
            var height = bottomCellPos.getMaxY() - minY;
            var container = O$.getContainingBlock(this, true);
            var visible = true;
            if (table._params.scrolling && table._params.scrolling.horizontal) {
              var bottomCellAbsPos = O$.getElementBorderRectangle(bottomCell);
              var xAbs = bottomCellAbsPos.getMaxX() - Math.floor(resizeHandleWidth / 2) - 1 + resizeHandleOffset;
              var yAbs = bottomCellAbsPos.getMinY();
              var containerRect = O$.getElementBorderRectangle(container);

              visible = containerRect.intersects(new O$.Rectangle(xAbs, yAbs, resizeHandleWidth, height));
            }
            var newDisplay = visible ? "block" : "none";
            if (this.style.display != newDisplay)
              this.style.display = newDisplay;
            if (visible) {
              this.style.top = y + "px";
              this.style.left = x + "px";
              this.style.width = resizeHandleWidth + "px";
              this.style.height = height + "px";
            }
          }
        });

        O$.addUnloadHandler(table, function () {
          resizeHandle.ondragend = null;
          resizeHandle.ondragstart = null;
          resizeHandle.onclick = null;
          resizeHandle.onmousedown = null;
          resizeHandle.onmouseout = null;
          resizeHandle.onmouseover = null;
        });
        //column._resizeHandle._updatePos();
      });

      function fixWidths() {
        if (table._params.scrolling) return;
        var colWidths = getColWidths();
        table.style.tableLayout = "fixed";

        //        if (!table._params.scrolling)
        //          O$.Tables._alignTableColumns(table._columns.map(function(c) {return c._colTags[0];}), table, true, null,
        //                  table._params.scrolling, 0);

        if (!table._params.scrolling)
          table.style.width = "auto";
        for (var i = 0, count = colWidths.length; i < count; i++) {
          var column = table._columns[i];
          column.setWidth(colWidths[i]);
        }
        table._originalWidth = recalculateTableWidth(colWidths);

        if (O$.isChrome() || O$.isSafari()) {
          // fix Chrome/Safari not respecting table-layout="fixed" in _some_ cases
          table.style.tableLayout = "auto";
          setTimeout(function () {
            table.style.tableLayout = "fixed";
          }, 10);
        }

      }

      fixWidths();

      function updateResizeHandlePositions() {
        if (table._columns) {
          for (var i = 0, count = table._columns.length; i < count; i++) {
            var column = table._columns[i];
            if (column._resizeHandle && column._resizeHandle.parentNode)
              column._resizeHandle._updatePos();
          }
        }
      }

      O$.addEventHandler(window, "resize", updateResizeHandlePositions);
      O$.addEventHandler(table, "mouseover", function () {
        if (!table._columnResizingInProgress)
          updateResizeHandlePositions();
      });
      if (table._params.scrolling && (O$.isExplorer6() || O$.isExplorer7())) {
        // mouseover can't be handled in these circumstances for some reason
        var updateIntervalId = setInterval(function () {
          if (table.parentNode == null) {
            clearInterval(updateIntervalId);
            return;
          }
          if (!table._columnResizingInProgress)
            updateResizeHandlePositions();
        }, 1000);
      }
      var prevOnscroll = table.onscroll;
      table.onscroll = function (e) {
        if (prevOnscroll) prevOnscroll.call(table, e);
        setTimeout(updateResizeHandlePositions, 10);
        if (table._params.scrolling && table._params.scrolling.autoSaveState) {
          O$.invokeFunctionAfterDelay(function () {
            O$.Ajax.requestComponentPortions(table.id, ["scrollingState"], null, function () {
              // no client-side updates are required -- the request was just for saving data
            }, null, true);
          }, table._params.scrolling.autoSaveStateDelay, table.id + "::scrollingStateSaving")
        }
      };

      O$.addUnloadHandler(table, function () {
        O$.removeEventHandler(window, "resize", updateResizeHandlePositions);
        table.onscroll = null;
      });

      table._fixFF3ColResizingIssue = function () { // See JSFC-3720
        if (!(O$.isMozillaFF3() && O$.isQuirksMode()))
          return;
        if (!table._params.scrolling && table._deepestColumnHierarchyLevel > 1) {
          var prevWidth = table.style.width;
          table.style.width = "0";
          table.styleWidth = prevWidth;
        }
      };


    });

  },

  // -------------------------- COLUMN REORDERING SUPPORT

  _initColumnReordering:function (tableId, draggedCellClass, draggedCellTransparency, autoScrollAreaClass, autoScrollAreaTransparency, autoScrollLeftImage, autoScrollRightImage, dropTargetClass, dropTargetTopImage, dropTargetBottomImage) {

    var table = O$(tableId);
    var autoscrollingSpeed = 200;
    var interGroupDraggingAllowed = false;
    var columnFixingAllowed = false;

    O$.preloadImages([autoScrollLeftImage, autoScrollRightImage, dropTargetTopImage, dropTargetBottomImage]);

    if (table._params.scrolling && table._params.scrolling.horizontal) {
      function autoScrolArea(imageUrl) {
        var result = O$.Table._createTableWithoutTd();
        result.className = autoScrollAreaClass;
        var img = O$.Table._createImage(imageUrl);
        var td = document.createElement("td");
        td.align = "center";
        result._tr.appendChild(td);
        td.appendChild(img);
        O$.setOpacityLevel(result, 1 - autoScrollAreaTransparency);
        return result;
      }

      var headerScroller = table.header._centerScrollingArea._scrollingDiv;
      var additionalAreaContainer = O$.getContainingBlock(headerScroller);
      if (!additionalAreaContainer) additionalAreaContainer = O$.getDefaultAbsolutePositionParent();
      var mainScroller = table.body._centerScrollingArea._scrollingDiv;

      var leftAutoScrollArea = autoScrolArea(autoScrollLeftImage);
      leftAutoScrollArea._update = function () {
        this.style.visibility = mainScroller.scrollLeft > 0 ? "visible" : "hidden";
      };
      var rightAutoScrollArea = autoScrolArea(autoScrollRightImage);
      rightAutoScrollArea._update = function () {
        this.style.visibility = mainScroller.scrollLeft < mainScroller.scrollWidth - mainScroller.clientWidth ? "visible" : "hidden";
      };

    }

    table._dropTargetMark = function (withVerticalDelimiter) {
      return function () {
        var dropTarget = document.createElement("div");
        if (withVerticalDelimiter) {
          //just as quick solution. We should come up with better idea
          dropTarget.className = dropTargetClass;
        }
        var width = O$.calculateNumericCSSValue(O$.getStyleClassProperty(dropTargetClass, "width"));
        dropTarget.setPosition = function (x, y1, y2) {
          O$.setElementBorderRectangle(dropTarget, new O$.Rectangle(x - width / 2, y1, width, y2 - y1));
          var topImageSize = O$.getElementSize(topImage);
          O$.setElementPos(topImage, {x:x - topImageSize.width / 2, y:y1 - topImageSize.height});
          var bottomImageSize = O$.getElementSize(bottomImage);
          O$.setElementPos(bottomImage, {x:x - bottomImageSize.width / 2, y:y2});
        };
        var topImage = O$.Table._createImage(dropTargetTopImage);
        var bottomImage = O$.Table._createImage(dropTargetBottomImage);
        topImage.style.position = "absolute";
        bottomImage.style.position = "absolute";
        dropTarget.show = function (container) {
          container.appendChild(dropTarget);
          container.appendChild(topImage);
          container.appendChild(bottomImage);
        };
        dropTarget.hide = function () {
          if (dropTarget.parentNode) dropTarget.parentNode.removeChild(dropTarget);
          if (topImage.parentNode) topImage.parentNode.removeChild(topImage);
          if (bottomImage.parentNode) bottomImage.parentNode.removeChild(bottomImage);
        };
        O$.correctElementZIndex(dropTarget, table, 1);
        return dropTarget;
      }();
    };

    var dropTargetMark = table._dropTargetMark(true);

    table._columns.forEach(function (sourceColumn) {
      if (!interGroupDraggingAllowed && sourceColumn.parentColumn) {
        if (sourceColumn.parentColumn._columns.length == 1)
          return; // there are no other columns in this group for possible reordering
      }
      if (!columnFixingAllowed && sourceColumn._scrollingArea && sourceColumn._scrollingArea._columns.length == 1) {
        return; // there are no other columns in this scrolling area for possible reordering
      }

      var headerCell = sourceColumn.header ? sourceColumn.header._cell : null;
      if (!headerCell) return;
      headerCell._clone = function () {
        var tbl = O$.Table._createTableWithoutTd();
        var td = headerCell.cloneNode(true);

        function processAbsoluteChildren(children) {
          var childArray = [];
          for (var i = 0, count = children.length; i < count; i++)
            childArray[i] = children[i];
          childArray.forEach(function (el) {
            if (O$.stringStartsWith(el.nodeName, "#")) return;
            if (el.style.position == "absolute" || O$.getElementStyle(el, "position") == "absolute")
              el.parentNode.removeChild(el);
            else
              processAbsoluteChildren(el.childNodes);
          });
        }

        processAbsoluteChildren(td.childNodes);
        tbl._tr.appendChild(td);
        tbl.className = O$.combineClassNames(
                [draggedCellClass, table._params.header.className, this._row.className, this._column.className]);
        tbl.style.border.borderWidth = O$.getElementStyle(tbl, "border-width");
        tbl.style.border.borderStyle = O$.getElementStyle(tbl, "border-style");
        tbl.style.border.borderColor = O$.getElementStyle(tbl, "border-color");
        O$.setOpacityLevel(tbl, 1 - draggedCellTransparency);
        O$.correctElementZIndex(tbl, table, 2);
        return tbl;
      };
      var makeDraggable = function () {
        var inAdditionalTargets = function (evt) {
          if (!table._rowGroupingBox)return false;
          return table._rowGroupingBox._innerDropTargets(headerCell).filter(
                  function (target) {
                    return target.eventInside(evt);
                  }).length > 0;
        };
        var dropTargets = null;

        function allDropTargets() {
          if (dropTargets) return dropTargets;
          dropTargets = [];
          var column = headerCell._column;
          if (table._rowGroupingBox && column._groupable) {
            dropTargets = dropTargets.concat(table._rowGroupingBox._innerDropTargets(column.columnId));
          }
          dropTargets = dropTargets.concat(table._innerDropTargetsByColumnId(sourceColumn.columnId, function (newIndex) {
            var columnIds = table.getColumnsOrder();
            var oldIndex = columnIds.indexOf(sourceColumn.columnId);
            columnIds.splice(newIndex, 0, sourceColumn.columnId);
            columnIds.splice(oldIndex < newIndex ? oldIndex : oldIndex + 1, 1);
            table.setColumnsOrder(columnIds);
          }));
          return dropTargets;
        }

        O$.makeDraggable(headerCell, function (evt) {
          for (var i = 0, count = allDropTargets().length; i < count; i++) {
            var dropTarget = allDropTargets()[i];
            if (dropTarget.eventInside(evt))
              return dropTarget;
          }
          return null;
        });
      }();
      var additionalAreaListener;

      var activeHelperArea = null;
      var activeScrollingInterval;

      O$.extend(headerCell, {
        ondragstart:function () {
          if (!(table._params.scrolling && table._params.scrolling.horizontal)) return;
          additionalAreaContainer.appendChild(leftAutoScrollArea);
          additionalAreaContainer.appendChild(rightAutoScrollArea);
          leftAutoScrollArea._update();
          rightAutoScrollArea._update();

          additionalAreaListener = O$.listenProperty(headerScroller, "rectangle", function (rect) {
            var subHeaderIndex = table._subHeaderRowIndex;
            var subHeaderHeight = subHeaderIndex != -1
                    ? O$.getElementHeight(table.header._getRows()[subHeaderIndex]._rowNode) : 0;
            O$.setElementHeight(leftAutoScrollArea, rect.height - subHeaderHeight);
            O$.setElementHeight(rightAutoScrollArea, rect.height - subHeaderHeight);
            O$.alignPopupByElement(leftAutoScrollArea, headerScroller, O$.LEFT, O$.CENTER, 0, -subHeaderHeight / 2, true, true);
            O$.alignPopupByElement(rightAutoScrollArea, headerScroller, O$.RIGHT, O$.CENTER, 0, -subHeaderHeight / 2, true, true);
          }, new O$.Timer(50));
        },
        ondragmove:function (e) {
          e = {clientX:e.clientX, clientY:e.clientY};
          if (!(table._params.scrolling && table._params.scrolling.horizontal)) return;

          function setActiveHelperArea(area) {
            if (activeHelperArea == area) return;
            if (activeScrollingInterval)
              clearInterval(activeScrollingInterval);
            activeHelperArea = area;
            var lastTimestamp = new Date().getTime();

            function scrollingStep() {
              var thisTimestamp = new Date().getTime();
              var scrollingStep = autoscrollingSpeed * (thisTimestamp - lastTimestamp) / 1000;
              lastTimestamp = thisTimestamp;
              return scrollingStep;
            }

            if (area == leftAutoScrollArea)
              activeScrollingInterval = setInterval(function () {
                var scrollLeft = mainScroller.scrollLeft - scrollingStep();
                if (scrollLeft < 0) scrollLeft = 0;
                mainScroller.scrollLeft = scrollLeft;
                O$._draggedElement.updateCurrentDropTarget(e);
                leftAutoScrollArea._update();
                rightAutoScrollArea._update();
              }, 30);
            if (area == rightAutoScrollArea)
              activeScrollingInterval = setInterval(function () {
                mainScroller.scrollLeft = mainScroller.scrollLeft + scrollingStep();
                O$._draggedElement.updateCurrentDropTarget(e);
                leftAutoScrollArea._update();
                rightAutoScrollArea._update();
              }, 30);
          }

          if (O$.isCursorOverElement(e, leftAutoScrollArea))
            setActiveHelperArea(leftAutoScrollArea);
          else if (O$.isCursorOverElement(e, rightAutoScrollArea))
            setActiveHelperArea(rightAutoScrollArea);
          else
            setActiveHelperArea(null);
        },
        ondragend:function () {
          if (activeScrollingInterval) clearInterval(activeScrollingInterval);
          if (additionalAreaContainer) {
            additionalAreaContainer.removeChild(leftAutoScrollArea);
            additionalAreaContainer.removeChild(rightAutoScrollArea);
            additionalAreaListener.release();
          }
          dropTargetMark.hide();
        }
      });
    });
    table._columnsLogicalStructure = function () {
      var currentColumnsOrder = table.getColumnsOrder();

      function visibilityPredicate(node) {
        if (node.isLeaf()) {
          return currentColumnsOrder.indexOf(node.columnId) >= 0;
        }
        return node.visibleChildren().length > 0;
      }

      function helper(logicalDescription, parent) {
        var self = {
          columnId:logicalDescription.columnId,
          parent:function () {
            return parent;
          },
          root:function () {
            var node = self;
            while (node.parent())node = node.parent();
            return node;
          },
          isLeaf:function () {
            return !logicalDescription.subColumns;
          },
          children:function (dontApplySorting) {
            function indexOfAnyVisibleLeaf(node) {
              if (node.isLeaf()) {
                return currentColumnsOrder.indexOf(node.columnId);
              }
              var visibleChildren = node.visibleChildren();
              return visibleChildren.length > 0 ? indexOfAnyVisibleLeaf(visibleChildren[0]) : -1;
            }

            var result = [];
            if (!self.isLeaf())logicalDescription.subColumns.forEach(function (subColumn) {
              result.push(helper(subColumn, self));
            });
            if (!dontApplySorting)result.sort(function (a, b) {
              return indexOfAnyVisibleLeaf(a) - indexOfAnyVisibleLeaf(b);
            });
            return result;
          },
          visibleChildren:function () {
            return self.children().filter(visibilityPredicate);
          },
          firstVisibleLeaf:function () {
            var visibleChild = self;
            while (!visibleChild.isLeaf()) visibleChild = visibleChild.visibleChildren()[0];
            return visibleChild;
          },
          lastVisibleLeaf:function () {
            var visibleChild = self;
            while (!visibleChild.isLeaf()) {
              var visibleChildren = visibleChild.visibleChildren();
              visibleChild = visibleChildren[visibleChildren.length - 1];
            }
            return visibleChild;
          },
          isVisible:function () {
            return visibilityPredicate(self);
          },
          allLeafs:function (dontApplySorting) {
            var result = [];
            var candidates = self.children(dontApplySorting).slice(0);
            while (candidates.length > 0) {
              var current = candidates.shift();
              if (!current.isLeaf()) {
                candidates = current.children(dontApplySorting).concat(candidates);
              } else {
                result.push(current);
              }
            }
            return result;
          },
          find:function (columnId) {
            if (self.columnId == columnId) {
              return self;
            }
            var result = null;
            self.children().forEach(function (child) {
              if (!result) result = child.find(columnId);
            });
            return result;
          }
        };
        return self;
      }


      return  helper({subColumns:table._params.logicalColumns}, null);
    }();

    table._columnsReorderingSupport = function (sourceColumnId, targetColumnId) {
      function canBeInserted(where, what, atLeft) {
        if (atLeft != false) {
          //todo: [s.kurilin]  should be rewritten for allow colGroup reordering
          var currentOrder = table.getColumnsOrder().slice(0);
          where = currentOrder[currentOrder.indexOf(where) - 1];
        }

        function findFirstVisibleParent(node) {
          var parent = node.parent();
          while (parent && !parent.isVisible()) {
            parent = parent.parent();
          }
          return parent;
        }

        var sourceNode = table._columnsLogicalStructure.root().find(what),
                firstVisibleParent = findFirstVisibleParent(sourceNode);

        function canBePlacedInOrAfter() {
          return firstVisibleParent.visibleChildren().filter(
                  function (child) {
                    return child.lastVisibleLeaf().columnId == where;
                  }).length > 0
        }

        function canBePlacedBefore() {
          var leftsVisibleNode = firstVisibleParent.firstVisibleLeaf();
          var currentIndex = table.getColumnsOrder().indexOf(leftsVisibleNode.columnId);
          return (currentIndex == 0 && !where)
                  || (currentIndex > 0 && table.getColumnsOrder()[currentIndex - 1] == where);
        }

        return  canBePlacedBefore() || canBePlacedInOrAfter();
      }

      var self = {
        onLeftEdgePermit:function (func) {
          if (canBeInserted(targetColumnId, sourceColumnId, true))func();
          return self;
        },
        onRightEdgePermit:function (func) {
          if (canBeInserted(targetColumnId, sourceColumnId, false))func();
          return self;
        }
      };
      return self;
    };
    table._innerDropTargetsByColumnId = function (columnId, dropHandler) {
      var dropTargets = [];
      //TODO: [s.kurilin] we shouldn't use this counter
      var counter = 0;
      table._columns.forEach(function (targetColumn) {
        var index = counter;
        var headerCell = targetColumn.header ? targetColumn.header._cell : null;
        var targetCell = headerCell;

        function dropTarget(minX, maxX, minY, maxY, sourceColumnId, columnOrGroup, rightEdge) {
          var container = O$.getContainingBlock(headerCell, true);
          if (!container)
            container = O$.getDefaultAbsolutePositionParent();

          return {
            minX:minX,
            maxX:maxX,
            minY:minY,
            maxY:maxY,
            eventInside:function (evt) {
              var cursorPos = O$.getEventPoint(evt, headerCell);
              return (this.minX == null || cursorPos.x >= this.minX) &&
                      (this.maxX == null || cursorPos.x < this.maxX);
            },
            setActive:function (active) {
              if (active) {
                dropTargetMark.show(container);
                var gridLineWidthCorrection = function () {
                  var parentColumnList = columnOrGroup._parentColumn ? columnOrGroup._parentColumn.subColumns : table._columns;
                  var thisIdx = parentColumnList.indexOf(columnOrGroup);
                  var col = rightEdge
                          ? thisIdx < parentColumnList.length - 1 ? columnOrGroup : null
                          : parentColumnList[thisIdx - 1];
                  if (col) {
                    var cell = col.header ? col.header._cell : null;
                    return cell ? O$.getNumericElementStyle(cell, "border-right-width") : 0;
                  } else {
                    return O$.getNumericElementStyle(table, rightEdge ? "border-right-width" : "border-left-width");
                  }
                }();

                function firstVisibleParent(left) {
                  var parent = table._columnsLogicalStructure.root().find(left).parent();
                  while (!parent.isVisible()) parent = parent.parent();
                  return parent.columnId;
                }

                var parent = firstVisibleParent(sourceColumnId);
                var markMinY;
                if (parent == null) {
                  var anyHighLevelColumn = table._columnsLogicalStructure.root().visibleChildren()[0].columnId;
                  var rect = O$.getElementBorderRectangle(table._getHeaderCell(anyHighLevelColumn).header._cell, true);
                  markMinY = rect.getMinY();
                } else {
                  var rect = O$.getElementBorderRectangle(table._getHeaderCell(parent).header._cell, true);
                  markMinY = rect.getMaxY();
                }
                dropTargetMark.setPosition((rightEdge ? maxX : minX) - gridLineWidthCorrection / 2, markMinY, maxY);
              } else {
                dropTargetMark.hide();
              }
            },
            acceptDraggable:function (cellHeader) {
              var col = columnOrGroup;
              while (col.subColumns)
                col = !rightEdge ? col.subColumns[0] : col.subColumns[col.subColumns.length - 1];
              var targetColIndex = !rightEdge ? col._index : col._index + 1;
              dropHandler(targetColIndex);
            }
          };
        }

        var targetCellRect = O$.getElementBorderRectangle(targetCell, true);
        var targetCellRect2 = function () {
          var bottomCell = targetCell;
          var col = targetColumn;
          while (col.subColumns) {
            col = col.subColumns[0];
            if (col.header && col.header._cell)
              bottomCell = col.header._cell;
          }
          return O$.getElementBorderRectangle(bottomCell, true);
        }();
        var min = targetCellRect.getMinX();
        var max = targetCellRect.getMaxX();
        var mid = (min + max) / 2;
        var minY = targetCellRect.getMinY();
        var maxY = targetCellRect2.getMaxY();
        table._columnsReorderingSupport(columnId, targetColumn.columnId)
                .onLeftEdgePermit(function () {
                  dropTargets.push(dropTarget(min, mid, minY, maxY, columnId, targetColumn, false));
                })
                .onRightEdgePermit(function () {
                  dropTargets.push(dropTarget(mid, max, minY, maxY, columnId, targetColumn, true));
                });
        counter++;
      });
      var fillEmptySpace = function () {
        for (var i = 0; i < dropTargets.length; i++) {
          var current = dropTargets[i];
          if (i == 0) {
            current.minX = null;
          }
          if (i == dropTargets.length - 1) {
            current.maxX = null;
          } else {
            var next = dropTargets[i + 1];
            var avg = (current.maxX + next.minX) / 2;
            current.maxX = Math.floor(avg);
            next.minX = Math.ceil(avg);
          }
        }
      }();
      return dropTargets;
    };

    function sendColumnMoveRequest(srcColIndex, dstColIndex) {
      if (dstColIndex == srcColIndex || dstColIndex == srcColIndex + 1)
        return;

      var columnIds = table.getColumnsOrder();
      var columnId = columnIds.splice(srcColIndex, 1);
      columnIds.splice(dstColIndex < srcColIndex ? dstColIndex : dstColIndex - 1, 0, columnId);
      table.setColumnsOrder(columnIds);
    }

    table._setRowGroupingBox = function (rowGroupingBox) {
      table._rowGroupingBox = rowGroupingBox;
    };
    //todo: move it out of here
    table._getColumn = function (columnId) {
      return table._columns.filter(function (column) {
        return column.columnId == columnId
      })[0];
    };
    //todo: move it out of here
    table._getHeaderCell = function (columnId) {
      function retrieveAllCells() {
        var candidates = table._columns.slice(0);
        var allCells = [];
        while (candidates.length > 0) {
          var current = candidates.pop();
          allCells.push(current);
          if (current._parentColumn) {
            candidates.push(current._parentColumn);
          }
        }
        return allCells;
      }

      return retrieveAllCells().filter(function (column) {
        return column.columnId == columnId;
      })[0];
    };
  },


// -------------------------- ROW GROUPING SUPPORT
  _initRowGrouping:function (tableId, activeColumnIds, groupableColumnIds, groupingRules, headerClassName, groupOnHeaderClick, hideGroupingColumns) {

    var table = O$.initComponent(tableId, null, {
      grouping:{
        _columnHeaderBoxes:{},
        _groupingRules:groupingRules,
        _groupOnHeaderClick:groupOnHeaderClick,
        _hideGroupingColumns:hideGroupingColumns,

        _getColumnHeaderBox:function (columnId) {
          return this._columnHeaderBoxes[columnId];
        },

        getGroupingRules:function () {
          return this._groupingRules;
        },

        setGroupingRules:function (rules) {
          this._groupingRules = rules;
          var setGroupingRulesStr = JSON.stringify(rules, ["columnId", "ascending"]);
          O$._submitInternal(table, null, [
            [table.id + "::setGroupingRules", setGroupingRulesStr]
          ]);

        },

        isGroupedByColumn:function (columnId) {
          var groupingRules = table.grouping.getGroupingRules();
          var columnInGroupingRules = groupingRules.some(function (groupingRule) {
            return groupingRule.columnId == columnId;
          });
          return columnInGroupingRules;
        },

        groupByColumn:function (columnId) {
          if (this.isGroupedByColumn(columnId)) return;

          var groupingRules = [].concat(this.getGroupingRules());
          groupingRules.push(new O$.Table.GroupingRule(columnId, true));
          table.combineSubmissions(function () {
            table.grouping.setGroupingRules(groupingRules);
            if (table.grouping._hideGroupingColumns)
              table.hideColumn(columnId);
          });

        },

        removeFromGrouping:function (columnId) {
          var groupingRules = [].concat(this.getGroupingRules());
          for (var i = 0, count = groupingRules.length; i < count; i++) {
            var groupingRule = groupingRules[i];
            if (groupingRule.columnId == columnId) {
              groupingRules.splice(i, 1);
              table.combineSubmissions(function () {
                table.grouping.setGroupingRules(groupingRules);
                if (table.grouping._hideGroupingColumns)
                  table.showColumn(columnId);
              });

              return;
            }
          }
        },

        cancelGrouping:function () {
          var groupingRules = table.grouping.getGroupingRules();
          if (groupingRules.length == 0) return;

          table.combineSubmissions(function () {
            table.grouping.setGroupingRules([]);
            if (table.grouping._hideGroupingColumns)
              groupingRules.forEach(function (groupingRule) {
                table.showColumn(groupingRule.columnId);
              });
          });
        },

        _toggleSortingTypeInGroupingRule:function (columnId, ruleIndex) {
          var rules = table.grouping.getGroupingRules();
          rules.forEach(function (rule) {
            if (rule.columnId == columnId)rule.ascending = !rule.ascending;
          });
          table.grouping.setGroupingRules(rules);
        },

        _applyGroupingRule:function (rule, ruleIndex) {
          table.combineSubmissions(function () {
            (function removePreviousRuleIfItWasExistAndAddNew() {
              function onlyIds(rules) {
                return rules.map(function (r) {
                  return r.columnId
                });
              }

              var rules = table.grouping.getGroupingRules(),
                      toDelete = onlyIds(rules).indexOf(rule.columnId);
              if (toDelete >= 0)rules.splice(toDelete, 1);
              rules.splice(ruleIndex, 0, rule);
              table.grouping.setGroupingRules(rules);
            }());
            if (table.grouping._hideGroupingColumns)(function hideColumn() {
              var displayedColumnIds = table.getColumnsOrder();
              var index = displayedColumnIds.indexOf(rule.columnId);
              if (index >= 0) {
                displayedColumnIds.splice(index, 1);
              }
              table.setColumnsOrder(displayedColumnIds);
            }());
          });
        },

        _cancelGroupingRule:function (columnId, newIndex) {
          table.combineSubmissions(function () {
            (function removeFromGrouping() {
              var index = 0;
              var groupingRules = table.grouping.getGroupingRules();
              groupingRules.forEach(function (eachRule) {
                if (eachRule.columnId == columnId) {
                  groupingRules = groupingRules.slice(0, index).concat(groupingRules.slice(index + 1));
                }
                index++;
              });
              table.grouping.setGroupingRules(groupingRules);
            }());
            (function removePreviousColumnIfItWasExistAndAddNew() {
              var displayedColumnIds = table.getColumnsOrder(),
                      toDelete = displayedColumnIds.indexOf(columnId);
              if (toDelete >= 0)displayedColumnIds.splice(toDelete, 1);
              displayedColumnIds.splice(newIndex, 0, columnId);
              table.setColumnsOrder(displayedColumnIds);
            }());
          });
        }
      }
    });

    activeColumnIds.forEach(function (columnId) {
      var boxId = tableId + "::groupingHeaderCell:" + columnId;
      var columnHeaderBox = O$(boxId);
      if (!columnHeaderBox) throw "Couldn't find column header box. columnId: " + columnId + "; box id: " + boxId;

      O$.extend(columnHeaderBox, {
        id:null, // reset ids to avoid clashes when it's pulled out of the table, and the table is reloaded with Ajax
        sortingToggleImg:O$.getChildNodesByClass(columnHeaderBox, "o_table_sortingToggle", true)
      });

      table.grouping._columnHeaderBoxes[columnId] = columnHeaderBox;

      O$.Tables._assignHeaderBoxStyle(columnHeaderBox, table, columnId, headerClassName);
    });

    groupableColumnIds.forEach(function (columnId) {
      var col = table._columns.byId(columnId);
      if (col)
        col._groupable = true;
    });

    O$.Table._tableLoaded(tableId);
  },
  _GroupingBoxLayout:function (rowGroupingBox, tableId, connectorStyle, headerStyleClassName, headerOffset, padding) {
    var table = O$(tableId);
    var dropAreas = [];
    var headers = [];

    function Connector(left, right) {
      function connectorDescription(left, right) {
        var rightMaxY = right.y + right.height , leftMaxY = left.y + left.height;
        return {
          horizontalOffset:left.width - 10,
          height:Math.round(leftMaxY < right.y ? right.y - leftMaxY + right.height / 2 : (rightMaxY - leftMaxY) / 2)
        };
      }

      var alignment = O$.GraphicLine.ALIGN_BY_TOP_OR_LEFT;
      var self = {
        _leftRect:O$.getElementBorderRectangle(left, true),
        _rightRect:O$.getElementBorderRectangle(right, true),
        _toRemove:[],
        show:function () {
          if (self._vertical != null) {
            self.destroy();
          }
          self._toRemove.forEach(function (e) {
            e.parentNode.removeChild(e);
          });
          self._leftRect = O$.getElementBorderRectangle(left, true);
          self._rightRect = O$.getElementBorderRectangle(right, true);
          self._toRemove = [];
          var desc = connectorDescription(self._leftRect, self._rightRect),
                  leftBorder = self._leftRect.x + desc.horizontalOffset,
                  topBorder = self._leftRect.y + self._leftRect.height,
                  lowBorder = topBorder + desc.height,
                  rightBorder = self._rightRect.x;
          self._vertical = new O$.GraphicLine(connectorStyle, alignment, leftBorder, topBorder, leftBorder, lowBorder);
          self._horizontal = new O$.GraphicLine(connectorStyle, alignment, leftBorder, lowBorder, rightBorder, lowBorder);
          self._vertical.show(rowGroupingBox);
          self._horizontal.show(rowGroupingBox);
          self._vertical.updatePresentation();

        },
        destroy:function () {
          self._vertical.parentNode.removeChild(self._vertical);
          self._horizontal.parentNode.removeChild(self._horizontal);

        }
      };
      return self;
    }

    var self = {
      _toRemove:[],
      _toShow:[],
      _directWrappers:[], //for quick search
      _dragByColumnId:function (columnId) {
        return self._directWrappers[columnId];
      },
      insertByColumnId:function (index, columnId) {
        function newCoordinates() {
          var zero = {
            x:padding.left,
            y:padding.top
          };
          if (index == 0) {
            return zero;
          }
          var previous = self.draggable()[index - 1],
                  previousPos = O$.getElementPos(previous, true),
                  previousSize = O$.getElementSize(previous),
                  headerHorizOffsetVal = O$.calculateNumericCSSValue(headerOffset.horizontal, previousSize.width),
                  headerVertOffsetVal = O$.calculateNumericCSSValue(headerOffset.vertical, previousSize.height);
          return {
            x:Math.round(previousPos.x + previousSize.width + headerHorizOffsetVal),
            y:Math.round(previousPos.y + headerVertOffsetVal)
          };
        }

        function draggingArea(directWrapper) {
          var result = document.createElement('div');
          result.columnId = columnId;
          result.show = function () {
            if ('\v' == 'v') {
              result.style.styleFloat = "left"; //for ie
            } else {
              result.style.cssFloat = "left";  //for browsers
            }
            result.style.height = "100%";
            var directWrapperWidth = O$.getElementWidth(directWrapper),
                    headerHorizOffsetVal = O$.calculateNumericCSSValue(headerOffset.horizontal, directWrapperWidth);
            result.style.width = (directWrapperWidth + headerHorizOffsetVal) + "px";
            rowGroupingBox.appendChild(result);
          };
          return result;
        }

        function directWrapper() {
          var result = document.createElement('div');
          result.className = headerStyleClassName;
          result.appendChild(header);
          result.show = function () {
            var coordinates = newCoordinates();
            result.style.top = coordinates.y + "px";
            result.style.left = coordinates.x + "px";
            result.style.position = "absolute";
            rowGroupingBox.appendChild(result);
            (function movePaddings() {
              var HORIZONTAL = 1, VERTICAL = 0,
                      size = O$.getElementSize(result);

              function val(cssName, horizontalOrVertical) {
                return O$.calculateNumericCSSValue(
                        O$.getElementStyle(result, cssName),
                        horizontalOrVertical == HORIZONTAL ? size.width : size.height);
              }

              [
                ["padding-left", "paddingLeft", HORIZONTAL],
                ["padding-right", "paddingRight", HORIZONTAL],
                ["padding-top", "paddingTop", VERTICAL],
                ["padding-bottom", "paddingBottom", VERTICAL]
              ].forEach(function (d) {
                        header.style[d[1]] = val(d[0], d[2]) + "px";
                        result.style[d[1 ]] = "0px";
                      });
            }());
          };
          result.connect = function (nextElement) {
            result.connector = Connector(result, nextElement);
            self._toShow.push(result.connector);
          };
          result.loseConnection = function () {
            result.connector.destroy();
          };
          return result;
        }

        var header = table.grouping._getColumnHeaderBox(columnId);
        var wrapper = directWrapper();
        wrapper._columnId = columnId;
        self._directWrappers[columnId] = wrapper;

        self._toShow.push(wrapper);
        var area = draggingArea(header);
        self._toShow.push(area);

        dropAreas.splice(index, 0, area);
        headers.splice(index, 0, wrapper);

        if (index != headers.length - 1) {
          wrapper.connect(headers[index + 1]);
        }
        if (index != 0) {
          headers[index - 1].connect(wrapper);
        }
      },
      addAll:function (columnIds) {
        var index = dropAreas.length;
        columnIds.forEach(function (columnId) {
          self.insertByColumnId(index++, columnId);
        });

      },
      dropAreas:function () {
        return dropAreas;
      },
      draggable:function () {
        return headers;
      },
      isEmpty:function () {
        return dropAreas.length == 0;
      },
      redraw:function () {
        self._toRemove.forEach(function (e) {
          e.parentNode.removeChild(e);
        });
        self._toRemove = [];
        self._toShow.forEach(function (e) {
          e.show();
        });
        self._toShow = [];
        rowGroupingBox.validate();
      },
      removeByIndex:function (index) {
        dropAreas.splice(index, 0);
        headers.splice(index, 0);

        headers[index].loseConnection();
        if (index != 0) {  //not first
          headers[index - 1].loseConnection();
          if (index != headers.length - 1) {  //not last
            headers[index - 1].connect(headers[index + 1]);
          }
        }

        self._toRemove(dropAreas[index]);
        self._toRemove(headers[index]);
      }
    };
    return self;
  },
  _initRowGroupingBox:function (rowGroupingBoxId, tableId, connectorStyle, headerStyleClassName, headerHorizOffset, headerVertOffset) {
    O$.Table._onTableLoaded(tableId, function () {
      var table = O$(tableId);
      O$.addLoadEvent(function () {
        function initWhenReady() {
          if (!O$.isElementPresentInDocument(table)) {
            // cancel the deferred grouping box initialization if the table has been removed with Ajax (or with other
            // means) without being shown
            return;
          }
          var ready = O$.isVisibleRecursive(table);
          if (!ready) {
            setTimeout(initWhenReady, 100);
            return;
          }

          doActualInitialization();
        }

        initWhenReady();
      });
    });
    function doActualInitialization() {
      var table = O$(tableId);
      var rowGroupingBoxTable = O$(rowGroupingBoxId);
      var rowGroupingBox = rowGroupingBoxTable.firstChild.firstChild.firstChild;
      var rules = function () {
        return table.grouping.getGroupingRules();
      };

      var dropTargetMark = function () {
        var delegate = table._dropTargetMark(false);
        var copyOfOuterContainer = null;

        function findColumnIndex(columnId) {
          var counter = 0;
          var index = -1;
          rules().forEach(function (rule) {
            if (rule.columnId == columnId) {
              index = counter;
            }
            counter++;
          });
          return index;
        }

        function beforeFirst() {
          var headerRect = O$.getElementBorderRectangle(groupingBoxLayout().draggable()[0], true);
          dropTargetMark.setPosition(headerRect.x, headerRect.y, headerRect.y + headerRect.height);
        }

        function afterLast() {
          var headerRect = O$.getElementBorderRectangle(groupingBoxLayout().draggable()[groupingBoxLayout().draggable().length - 1], true);
          dropTargetMark.setPosition(headerRect.getMaxX(), headerRect.y, headerRect.y + headerRect.height);
        }

        function between(left, right) {
          function avr(a, b) {
            return Math.round((a + b) / 2);
          }

          var leftRect = O$.getElementBorderRectangle(groupingBoxLayout()._dragByColumnId(left), true);
          var rightRect = O$.getElementBorderRectangle(groupingBoxLayout()._dragByColumnId(right), true);
          dropTargetMark.setPosition(avr(leftRect.getMaxX(), rightRect.getMinX()), leftRect.getMinY(), rightRect.getMaxY());
        }

        return {
          highline:function (columnId, rightEdge) {
            if (rightEdge) {
              dropTargetMark.displayAfter(columnId);
            } else {
              dropTargetMark.displayBefore(columnId);
            }
          },
          setPosition:function (x, y1, y2) {
            var offset = copyOfOuterContainer ? O$.getElementPos(copyOfOuterContainer, true) : {x:0, y:0};
            delegate.setPosition(x + offset.x, y1 + offset.y, y2 + offset.y);
          },
          show:function (container, outerContainer) {
            copyOfOuterContainer = outerContainer;
            delegate.show(container);
          },
          hide:function () {
            delegate.hide();
          },
          displayAfter:function (columnId) {
            var index = findColumnIndex(columnId);
            if (index == rules().length - 1) {
              afterLast()
            } else {
              between(columnId, rules()[index + 1].columnId);
            }
          },
          displayBefore:function (columnId) {
            var index = findColumnIndex(columnId);
            if (index == 0) {
              beforeFirst();
            } else {
              between(rules()[index - 1].columnId, columnId);
            }
          }
        };
      }();
      var groupingBoxPaddings = function () {
        var size = O$.getElementSize(rowGroupingBoxTable),
                HORIZONTAL = 1,
                VERTICAL = 0;

        function val(cssName, horizontalOrVertical) {
          return O$.calculateNumericCSSValue(
                  O$.getElementStyle(rowGroupingBoxTable, cssName),
                  horizontalOrVertical == HORIZONTAL ? size.width : size.height);
        }

        return {left:val("padding-left", HORIZONTAL),
          top:val("padding-top", VERTICAL),
          right:val("padding-right", HORIZONTAL),
          bottom:val("padding-bottom", VERTICAL)};
      }();
      var groupingBoxLayout = function () {
        var result;
        return function () {
          if (!result) {
            result = O$.Table._GroupingBoxLayout(
                    rowGroupingBox, tableId,
                    connectorStyle, headerStyleClassName,
                    {horizontal:headerHorizOffset, vertical:headerVertOffset},
                    groupingBoxPaddings);
          }
          return result;
        }
      }();

      var layoutStrategy = function (justNameAsComment) {
        var isOnlyPromptText = rules().length == 0;
        var self = {
          promptText:function (func) {
            if (isOnlyPromptText)func();
            return self;
          },
          groupingBoxes:function (func) {
            if (!isOnlyPromptText)func();
            return self;
          },
          any:function (func) {
            func();
            return self;
          }
        };
        return self;
      };
      var groupingColumnIds = function () {
        return table.grouping.getGroupingRules().map(function (rule) {
          return rule.columnId;
        });
      };
      var innerDropTargets = function () {
        var result = null;
        return function () {
          if (result == null) {
            function appendToGroupingBox(columnId, newColumnIndex) {
              var newRule = new O$.Table.GroupingRule(columnId, true);
              table.grouping._applyGroupingRule(newRule, newColumnIndex);
            }

            var pos = O$.getElementPos(rowGroupingBox, true);
            var rowGroupingBoxMinX = parseInt(pos.x);
            var rowGroupingBoxMaxX = parseInt(rowGroupingBoxMinX) + parseInt(rowGroupingBox.clientWidth);
            var rowGroupingBoxMinY = parseInt(pos.y);
            var rowGroupingBoxMaxY = parseInt(rowGroupingBoxMinY) + parseInt(rowGroupingBox.clientHeight);
            var inRowGroupingBox = function () {
              return function (x, y) {
                return (x >= rowGroupingBoxMinX) && (x < rowGroupingBoxMaxX) && (y >= rowGroupingBoxMinY) && (y < rowGroupingBoxMaxY);
              }
            }();

            layoutStrategy("init inner drop targets")
                    .promptText(function () {
                      result = [
                        {
                          eventInside:function (evt) {
                            var cursorPos = O$.getEventPoint(evt, rowGroupingBox);
                            return inRowGroupingBox(cursorPos.x, cursorPos.y)
                          },
                          setActive:function (active) {
                            if (active) {
                              var container = O$.getContainingBlock(rowGroupingBox, true);
                              if (!container)
                                container = O$.getDefaultAbsolutePositionParent();
                              var rightEdge = false;
                              dropTargetMark.show(container);
                              var gridLineWidthCorrection = function () {
                                return O$.getNumericElementStyle(table, rightEdge ? "border-right-width" : "border-left-width");
                              }();
                              var truePos = O$.getElementPos(rowGroupingBox);
                              dropTargetMark.setPosition(rowGroupingBoxMinX, rowGroupingBoxMinY, rowGroupingBoxMaxY);
                            } else {
                              dropTargetMark.hide();
                            }
                          },
                          acceptDraggable:function (cellHeader) {
                            appendToGroupingBox(cellHeader._column.columnId, 0);
                          }
                        }
                      ];
                    })
                    .groupingBoxes(function () {
                      var dropTargets = [];

                      function dropTarget(minX, maxX, minY, maxY, targetColumn, newColumnIndex, rightEdge, columnId) {
                        var container = O$.getContainingBlock(rowGroupingBox, true);
                        if (!container)
                          container = O$.getDefaultAbsolutePositionParent();
                        return {
                          minX:minX,
                          maxX:maxX,
                          minY:minY,
                          maxY:maxY,
                          eventInside:function (evt) {
                            var cursorPos = O$.getEventPoint(evt, rowGroupingBox);
                            return inRowGroupingBox(cursorPos.x, cursorPos.y) &&
                                    (this.minX == null || cursorPos.x >= this.minX) &&
                                    (this.maxX == null || cursorPos.x < this.maxX);
                          },
                          setActive:function (active) {
                            if (active) {
                              dropTargetMark.show(container, rowGroupingBox);
                              dropTargetMark.highline(columnId, rightEdge);
                            } else {
                              dropTargetMark.hide();
                            }
                          },
                          acceptDraggable:function (cellHeader) {
                            if (groupingBoxLayout().draggable().indexOf(cellHeader) >= 0) {
                              //moving inside grouping box
                              var currentIndex = groupingBoxLayout().draggable().indexOf(cellHeader);
                              var currentColumnId = table.grouping.getGroupingRules()[currentIndex].columnId;
                              if (currentIndex < newColumnIndex) newColumnIndex--;
                              appendToGroupingBox(currentColumnId, newColumnIndex);
                            } else {
                              //move from table
                              appendToGroupingBox(cellHeader._column.columnId, newColumnIndex);
                            }
                          }
                        };
                      }

                      var _groupingBoxWrappers = groupingBoxLayout().dropAreas();
                      for (var i = 0; i < _groupingBoxWrappers.length; i++) {
                        var targetCell = _groupingBoxWrappers[i];
                        var targetCellRect = O$.getElementClientRectangle(targetCell, true);
                        var min = targetCellRect.getMinX() ? targetCellRect.getMinX() : 0;
                        var max = targetCellRect.getMaxX() ? targetCellRect.getMaxX() : 0;
                        var mid = (min + max) / 2;
                        var minY = targetCellRect.getMinY() ? targetCellRect.getMinY() : 0;
                        var maxY = targetCellRect.getMaxY() ? targetCellRect.getMaxY() : 0;
                        dropTargets.push(dropTarget(min, mid, minY, maxY, targetCell, i, false, targetCell.columnId));
                        dropTargets.push(dropTarget(mid, max, minY, maxY, targetCell, i + 1, true, targetCell.columnId));
                      }
                      dropTargets[0].minX = rowGroupingBoxMinX;
                      dropTargets[dropTargets.length - 1].maxX = rowGroupingBoxMaxX;
                      result = dropTargets;
                    });

          }
          return result;
        };
      }();
      layoutStrategy("Boxes: position offsets instead of padding; PromptText: move paddings to corresponding container")
              .promptText(function () {
                function copyProperty(jsName, cssName) {
                  rowGroupingBox.style[jsName] = O$.getElementStyle(rowGroupingBoxTable, cssName);
                }

                copyProperty("paddingLeft", "padding-left");
                copyProperty("paddingRight", "padding-right");
                copyProperty("paddingTop", "padding-top");
                copyProperty("paddingBottom", "padding-bottom");

                copyProperty("verticalAlign", "vertical-align");
                copyProperty("textAlign", "text-align");
              })
              .any(function () {
                ["paddingLeft", "paddingRight", "paddingTop", "paddingBottom"].forEach(function (property) {
                  rowGroupingBoxTable.style[property] = "0px";
                });
              });
      layoutStrategy("Fill container with boxes according to grouping rules")
              .groupingBoxes(function () {
                (function initGroupingBoxSizeValidationFunction() {
                  rowGroupingBox.validate = function () {
                    var currentSize = function () {
                      return O$.getElementSize(rowGroupingBox);
                    };
                    if (!rowGroupingBox.minHeight) {
                      rowGroupingBox.minHeight = currentSize().height;
                    }
                    function setHeight(val) {
                      rowGroupingBox.style.height = val + "px";
                    }

                    var headers = groupingBoxLayout().draggable();
                    if (headers.length > 0) {
                      var last = headers[headers.length - 1],
                              lowBorder = O$.getElementPos(last, true).y
                                      + O$.getElementHeight(last) + groupingBoxPaddings.bottom,
                              actualLowBorder = O$.getElementHeight(rowGroupingBox);
                      if (lowBorder > actualLowBorder) {
                        setHeight(lowBorder);
                      }

                      if (lowBorder < actualLowBorder && lowBorder > rowGroupingBox.minHeight) {  //magic number is just an eps
                        setHeight(Math.max(rowGroupingBox.minHeight, lowBorder));
                      }
                    }
                  };
                }());
                (function cleanPreviousContent() {
                  while (rowGroupingBox.childNodes.length > 0) {
                    var child = rowGroupingBox.firstChild;
                    child.parentNode.removeChild(child);
                  }
                }());
                (function drawNewContent() {
                  groupingBoxLayout().addAll(groupingColumnIds());
                  groupingBoxLayout().redraw();
                }());
                (function prepareHeadersForDragging() {
                  groupingBoxLayout().draggable().forEach(function (item) {
                    if (item._clone) return;
                    function processAbsoluteChildren(children) {
                      var childArray = [];
                      for (var i = 0, count = children.length; i < count; i++)
                        childArray[i] = children[i];
                      childArray.forEach(function (el) {
                        if (O$.stringStartsWith(el.nodeName, "#")) return;
                        if (el.style.position == "absolute" || O$.getElementStyle(el, "position") == "absolute")
                          el.parentNode.removeChild(el);
                        else
                          processAbsoluteChildren(el.childNodes);
                      });
                    }

                    item._clone = function () {
                      var res = item.cloneNode(true);
                      processAbsoluteChildren(res.childNodes);
                      //[stanislav.kurilin] : for FF and IE in quirks mode it's works fine without it,
                      //[stanislav.kurilin] : but in other browsers child styles doesn't apply fully
                      //[stanislav.kurilin] : so that there is a gap after near child
                      //[stanislav.kurilin] : tip to reproduce: set some big value to border-width
                      if (!(O$.isMozillaFF() || O$.isExplorer() && O$.isStrictMode())) {
                        ["borderWidth", "borderLeftWidth", "borderRightWidth", "borderTopWidth", "borderBottomWidth",
                          "borderColor", "borderLeftColor", "borderRightColor", "borderTopColor", "borderBottomColor",
                          "borderStyle", "borderLeftStyle", "borderRightStyle", "borderTopStyle", "borderBottomStyle"]
                                .forEach(function (prop) {
                                  res.firstChild.style[prop] = O$.getStyleClassProperty(res.className, prop);
                                });
                        res.style.borderWidth = "0px";
                      }
                      O$.setOpacityLevel(res, 1 - table._draggedCellTransparency || 0.5);
                      O$.correctElementZIndex(res, table, 2);
                      return res;
                    };
                  });
                }());
                (function makeHeadersDraggable() {
                  var innerDropTargetsVal = innerDropTargets();
                  groupingBoxLayout().draggable().forEach(function (item) {
                    if (!item.draggable) {
                      O$.makeDraggable(item, function (evt) {
                        var groupingRules = rules();
                        var groupingColumnIds = groupingRules.map(function (rule) {
                          return rule.columnId;
                        });
                        var dropTargets = innerDropTargetsVal.concat(table._innerDropTargetsByColumnId(item._columnId,
                                function (newIndex) {
                                  table.grouping._cancelGroupingRule(item._columnId, newIndex);
                                }));
                        for (var i = 0, count = dropTargets.length; i < count; i++) {
                          var dropTarget = dropTargets[i];
                          if (dropTarget.eventInside(evt))
                            return dropTarget;
                        }
                        return null;
                      });
                      item.draggable = true;
                    }
                  });
                }());
                (function makeHeadersSortable() {
                  var counter = 0;
                  var groupingRules = rules();
                  groupingBoxLayout().draggable().forEach(function (colHeader) {
                    if (!table.sorting || table._sortableColumnsIds.indexOf(colHeader._columnId) < 0)return;
                    O$.addEventHandler(colHeader, "click", function () {
                      var focusField = O$(table.id + "::focused");
                      if (focusField)
                        focusField.value = true; // set true explicitly before it gets auto-set when the click bubbles up (JSFC-801)
                      table.grouping._toggleSortingTypeInGroupingRule(colHeader._columnId);
                    });
                    O$.setupHoverStateFunction(colHeader, function (mouseInside) {
                      O$.setStyleMappings(colHeader.firstChild, {
                        sortableHeaderRolloverClass:mouseInside ? table._sortableHeaderRolloverClass : null});
                    });
                    counter++;
                  });
                }());
                (function attachColumnMenu() {
                  if (!table._columnMenu) return;
                  groupingBoxLayout().draggable().forEach(function (colHeader) {
                    O$.ColumnMenu._appendMenu(tableId, colHeader, colHeader._columnId, false);
                  });
                }());
              });
      (function attacheRowGroupingBoxToTable() {
        table._setRowGroupingBox({_innerDropTargets:innerDropTargets});
      }());
    }
  },

  HEADER_CELL_Z_INDEX_COLUMN_MENU_BUTTON:1,
  HEADER_CELL_Z_INDEX_COLUMN_MENU_RESIZE_HANDLE:2

};


// -------------------------- COLUMN MENU SUPPORT
O$.ColumnMenu = {

  _appendMenu:function (tableId, cell, columnId, hidingEnabled) {
    var table = O$(tableId);
    var columnMenuButtonTable = table._columnMenuButtonTable;
    var columnMenuId = table._columnMenuId;
    var columnMenu = O$(columnMenuId);

    function menuFixer() {
      O$.ColumnMenu._checkSortMenuItems(columnMenuId, tableId, columnId);
      O$.ColumnMenu._checkGroupingMenuItems(columnMenuId, tableId, columnId);
      var column = table._getColumn(columnId);

      if (!hidingEnabled) {
        var hideMenuItem = columnMenu._hideMenuItem;
        if (hideMenuItem) {
          table._hideMenuItem = hideMenuItem;
          for (var i = 0; i < columnMenu.childNodes.length; i++) {
            if (hideMenuItem == columnMenu.childNodes[i]) {
              table._hideMenuIdx = i;
              break;
            }
          }
          columnMenu.removeChild(hideMenuItem);
        }
      } else {
        var hideMenuItem = table._hideMenuItem;
        if (hideMenuItem) {
          if (table._hideMenuIdx >= 0
                  && columnMenu.childNodes.length > table._hideMenuIdx)
            columnMenu.insertBefore(hideMenuItem,
                    columnMenu.childNodes[table._hideMenuIdx]);
          else
            columnMenu.appendChild(hideMenuItem);
          table._hideMenuItem = null;
          table._hideMenuIdx = -1;
        }
      }
    }

    O$.setupHoverStateFunction(cell, function (mouseOver) {
      if (mouseOver && !O$.ColumnMenu._menuOpened) {
        O$.ColumnMenu._currentColumnId = columnId;
        O$.ColumnMenu._menuFixer = menuFixer;
        columnMenuButtonTable.showForCell(cell);
      } else {
        if (O$.ColumnMenu._currentColumnId == columnId) {
          O$.ColumnMenu._currentColumnId = null;
          O$.ColumnMenu._menuFixer = null;
        }
        columnMenuButtonTable.hideForCell(cell);
      }
    });
  },

  _currentColumnId:null,
  _menuOpened:false,

  _init:function (columnMenuId, tableId, columnMenuButtonId, sortAscMenuId, sortDescMenuId, hideMenuId, groupByColumnMenuId, removeFromGroupingMenuId, cancelGroupingMenuId) {
    var table = O$(tableId);
    table._columnMenu = {
      // todo: move columnMenu related members out from table instance right here, similar to table.sorting approach
    };
    table._columnMenuId = columnMenuId;
    O$.ColumnMenu._menuOpened = false;
    function findColumnById(columnId) {
      return table._columns.filter(function (column) {
        return column.columnId == columnId;
      })[0];
    }

    var menuInvokerAreaTransparency = 0;
    var newMenuParent = O$.getDefaultAbsolutePositionParent();
    var parentChildren = newMenuParent.childNodes;
    for (var i = parentChildren.length - 1; i >= 0; i--) {
      var c = parentChildren[i];
      if (c.id == columnMenuId) {
        // remove the previous menu that may have remained before the table was reloaded with Ajax (a4j or jsf 2.0's one)
        newMenuParent.removeChild(c);
        break;
      }
    }
    var columnMenu = O$.initComponent(columnMenuId, null, {
      _sortAscMenuItem:O$(sortAscMenuId),
      _sortDescMenuItem:O$(sortDescMenuId),
      _hideMenuItem:O$(hideMenuId),
      _groupByColumnMenuItem:O$(groupByColumnMenuId),
      _removeFromGroupingMenuItem:O$(removeFromGroupingMenuId),
      _cancelGroupingMenuItem:O$(cancelGroupingMenuId)
    });

    var columnMenuButton = O$(columnMenuButtonId);
    var columnMenuButtonTable = function () {
      function safeAppend(parent, child) {
        child.style.position = "";
        child.style.width = "";
        parent.appendChild(child);
        var width = O$.getElementWidth(child);
        child.style.position = "absolute";
        child.style.width = width + "px";
      }

      var result = O$.Table._createTableWithoutTd();
      result.style.position = "absolute";
      result._tr.appendChild(columnMenuButton);
      O$.setOpacityLevel(result, 1 - menuInvokerAreaTransparency);
      O$.extend(result, {
        showForCell:function (cell) {
          this.hide();
          safeAppend(cell, this);
          O$.correctElementZIndex(this, cell, O$.Table.HEADER_CELL_Z_INDEX_COLUMN_MENU_BUTTON);
          var rightOffset = O$.getNumericElementStyle(cell, "border-right-width");
          var bottomOffset = O$.getNumericElementStyle(cell, "border-bottom-width");
          var leftOffset = O$.getNumericElementStyle(cell, "border-left-width");
          var topOffset = O$.getNumericElementStyle(cell, "border-top-width");
          O$.setElementHeight(this, O$.getElementHeight(cell) - (bottomOffset + topOffset));
          O$.alignPopupByElement(this, cell, O$.RIGHT, O$.BOTTOM, rightOffset + leftOffset, bottomOffset, false, true);
          this._showForCell = cell;
        },
        hideForCell:function (cell) {
          if (this._showForCell == cell)
            this.hide();
        },
        hide:function () {
          if (this.parentNode) {
            this.parentNode.removeChild(this);
            this._showForCell = null;
          }
        }
      });
      return result;
    }();
    table._columnMenuButtonTable = columnMenuButtonTable;
    table._columns.forEach(function (column) {
      if (!column.header || !column.header._cell || !column.menuAllowed) return;
      var headerCell = column.header._cell;
      headerCell.columnId = column.columnId;
      O$.ColumnMenu._appendMenu(tableId, headerCell, column.columnId, true);
    });

    columnMenuButton.onclick = function (e) {
      O$.cancelEvent(e);
    };

    O$.initUnloadableComponent(columnMenuButton);
    O$.addUnloadHandler(columnMenuButton, function () {
      columnMenuButton.onclick = null;
    });
    O$.addEventHandler(columnMenuButton, "mousedown", function (evt) {
      O$.cancelEvent(evt);
      var columnId = O$.ColumnMenu._currentColumnId;
      var currentColumn = findColumnById(columnId);
      if (currentColumn) {
        O$.correctElementZIndex(columnMenu, currentColumn._resizeHandle);
        columnMenu._column = currentColumn;
      }
      table._showingMenuForColumn = columnId;
      if (columnMenu.parentNode != newMenuParent) {
        newMenuParent.appendChild(columnMenu);
        O$.correctElementZIndex(columnMenu, columnMenuButton);
      }

      columnMenu._showByElement(columnMenuButtonTable, O$.LEFT, O$.BELOW, 0, 0);
      var prevOnhide = columnMenu.onhide;
      if (O$.ColumnMenu._menuFixer)O$.ColumnMenu._menuFixer();

//      var headerCell = currentColumn.header._cell;
//      headerCell.setForceHover(true);

      columnMenuButton.setForceHover(true);
      O$.ColumnMenu._menuOpened = true;
      columnMenu.onhide = function (e) {
        setTimeout(function () {
          table._showingMenuForColumn = null;
          columnMenu._column = null;
        }, 1);

        if (prevOnhide)
          prevOnhide.call(columnMenu, e);
        columnMenuButton.setForceHover(null);
//        headerCell.setForceHover(null);
        O$.ColumnMenu._menuOpened = false;
      };
      O$.addUnloadHandler(table, function () {
        columnMenu.onhide = null;
      });
    });
  },

  _initColumnVisibilityMenu:function (menuId, tableId, columnIds) {
    var menu = O$(menuId);
    var table = O$(tableId);
    var idx = 0;
    menu._items.forEach(function (menuItem) {
      var colIndex = idx++;
      menuItem._anchor.onclick = function () {
        O$.ColumnMenu._toggleColumnVisibility(table, columnIds[colIndex]);
      };
      O$.addUnloadHandler(menuItem._anchor, function () {
        menuItem._anchor.onclick = null;
      });
    });
  },

  _toggleColumnVisibility:function (table, columnId) {
    var currentlyVisible = table.isColumnVisible(columnId);
    table.setColumnVisible(columnId, !currentlyVisible);
  },

  _checkGroupingMenuItems:function (columnMenuId, tableId, columnId) {
    var table = O$(tableId);
    var columnMenu = O$(columnMenuId);

    function setMenuItemVisible(menuItem, visible) {
      if (!menuItem) return;
      menuItem.style.display = visible ? "" : "none";
    }

    var column = table._columns.byId(columnId);
    var columnGroupable = column != null && column._groupable;
    setMenuItemVisible(columnMenu._groupByColumnMenuItem, table.grouping && !table.grouping.isGroupedByColumn(columnId) && columnGroupable);
    setMenuItemVisible(columnMenu._removeFromGroupingMenuItem, table.grouping && table.grouping.isGroupedByColumn(columnId));
    setMenuItemVisible(columnMenu._cancelGroupingMenuItem, table.grouping && table.grouping.getGroupingRules().length > 0);
  },

  _checkSortMenuItems:function (columnMenuId, tableId, columnId) {
    var table = O$(tableId);
    var columnMenu = O$(columnMenuId);

    function setMenuItemVisible(menuItem, visible) {
      if (!menuItem) return;
      menuItem.style.display = visible ? "" : "none";
    }

    var column = table._columns.byId(columnId);
    setMenuItemVisible(columnMenu._sortAscMenuItem, column == null || column._sortable);
    setMenuItemVisible(columnMenu._sortDescMenuItem, column == null || column._sortable);
  },
  _sortColumn:function (tableId, columnIndex, isAscending) {
    var table = O$(tableId);
    if (!table.sorting) return;
//    O$.assert(table.sorting, "O$._sortColumn: table is not sortable");
    var groupingRules = table.grouping ? table.grouping.getGroupingRules() : null;

    function associatedGroupingRule() {
      return groupingRules ? groupingRules.filter(function (rule) {
        return rule.columnId == columnId;
      })[0] : null;
    }

    var columnId = columnIndex ? table._columns[columnIndex].columnId : table._showingMenuForColumn;
    if (table._sortableColumnsIds.indexOf(columnId) < 0) return;
    var groupingRule = associatedGroupingRule();
    if (groupingRule) {
      groupingRule.ascending = isAscending;
      table.grouping.setGroupingRules(groupingRules);
    } else {
      table.sorting._setPrimarySortingRule(new O$.Table.SortingRule(columnId, isAscending));
    }
  },
  _sortColumnAscending:function (tableId, columnIndex) {
    O$.ColumnMenu._sortColumn(tableId, columnIndex, true);
  },

  _sortColumnDescending:function (tableId, columnIndex) {
    O$.ColumnMenu._sortColumn(tableId, columnIndex, false);
  },

  _groupByColumn:function (tableId, columnIndex) {
    var table = O$(tableId);
    O$.assert(table.grouping, "Cannot change grouping for a table that doesn't have row grouping capability turned on");

    var columnId = columnIndex ? table._columns[columnIndex].columnId : table._showingMenuForColumn;
    table.grouping.groupByColumn(columnId);
  },

  _removeFromGrouping:function (tableId, columnIndex) {
    var table = O$(tableId);
    O$.assert(table.grouping, "Cannot change grouping for a table that doesn't have row grouping capability turned on");

    var columnId = columnIndex ? table._columns[columnIndex].columnId : table._showingMenuForColumn;
    table.grouping.removeFromGrouping(columnId);
  },

  _cancelGrouping:function (tableId) {
    var table = O$(tableId);
    O$.assert(table.grouping, "Cannot change grouping for a table that doesn't have row grouping capability turned on");

    table.grouping.cancelGrouping();
  },

  _hideColumn:function (tableId, columnIndex) {
    var table = O$(tableId);
    var columnId = columnIndex ? table._columns[columnIndex].columnId : table._showingMenuForColumn;
    table.hideColumn(columnId);
  },
  _resetSorting:function (tableId, columnIndex) {
    var table = O$(tableId);
    if (!table.sorting) return;
    var columnId = columnIndex ? table._columns[columnIndex].columnId : table._showingMenuForColumn;
    table.sorting._setPrimarySortingRule(null);
  }
};

O$.Summary = {
  _init:function (componentId, originalClientId, value, tableId, popupMenuId, selectedItemId, selectedIconUrl, unselectedIconUrl, initializationAttemptNo) {
    if (!O$(componentId)) {
      if (!initializationAttemptNo) initializationAttemptNo = 0;
      if (initializationAttemptNo < 20) {
        // the case for the below facet whose _init function call is rendered prior to the appropriate Summary
        // component's placeholder, so we're invoking this asynchronously to give a chance for the tags below to get
        // into the DOM (when the JavaScript processing thread is freed again)
        setTimeout(function () {
          O$.Summary._init(componentId, originalClientId, value, tableId,
                  popupMenuId, selectedItemId, selectedIconUrl, unselectedIconUrl,
                  initializationAttemptNo++);
        }, initializationAttemptNo == 0 ? 1 : initializationAttemptNo < 5 ? 100 : 1000);
      }
      return;
    }

    var table = O$(tableId);
    var popupMenu = popupMenuId ? O$(popupMenuId) : null;

    var summary = O$.initComponent(componentId, null, {
      _setFunction:function (functionName) {
        O$._submitInternal(table, null, [
          [originalClientId + "::setFunction", functionName]
        ]);
      }
    });
    if (popupMenu) {
      if (!popupMenu._repositioned) {
        popupMenu._repositioned = true;

        var newMenuParent = O$.getDefaultAbsolutePositionParent();
        var parentChildren = newMenuParent.childNodes;
        for (var i = parentChildren.length - 1; i >= 0; i--) {
          var c = parentChildren[i];
          if (c.id == popupMenuId) {
            // remove the previous menu that may have remained before the table was reloaded with Ajax (a4j or jsf 2.0's one)
            newMenuParent.removeChild(c);
            break;
          }
        }

        if (popupMenu.parentNode != newMenuParent) {
          newMenuParent.appendChild(popupMenu);
          O$.correctElementZIndex(popupMenu, table);
        }

      }

      summary.oncontextmenu = function (e) {
        O$.Summary._summaryForCurrentPopup = summary;
        var allItems = popupMenu._items;
        allItems.forEach(function (item) {
          item._icon.src = item.id == selectedItemId ? selectedIconUrl : unselectedIconUrl;
        });
        popupMenu.showForEvent(e);
        O$.cancelEvent(e);
      };
    }

    summary.innerHTML = value;
  },

  _setFunction:function (functionName) {
    O$.Summary._summaryForCurrentPopup._setFunction(functionName);
  }


};

