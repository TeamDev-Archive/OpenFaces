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
// -------------------------- COMMON TABLE FUNCTIONS

O$.Table = {
  _initDataTableAPI: function(table) {
    O$.extend(table, {
      _of_dataTableComponentMarker: true,
      selectAllRows: function() {
        this.__selectAllRows();
      },
      clearSelection: function() {
        this.__clearSelection();
      },
      isSelectionEmpty: function() {
        return this.__isSelectionEmpty();
      },
      getSelectedRowIndex: function() {
        return this.__getSelectedRowIndex();
      },
      setSelectedRowIndex: function(rowIndex) {
        this.__setSelectedRowIndex(rowIndex);
      },
      getSelectedRowIndexes: function() {
        return this.__getSelectedRowIndexes();
      },
      setSelectedRowIndexes: function(rowIndexes) {
        this.__setSelectedRowIndexes(rowIndexes);
      },
      getSelectedRowKey: function() {
        return this.__getSelectedRowKey();
      },
      setSelectedRowKey: function(rowKey) {
        this.__setSelectedRowKey(rowKey);
      },
      getSelectedRowKeys: function() {
        return this.__getSelectedRowKeys();
      },
      setSelectedRowKeys: function(rowKey) {
        this.__setSelectedRowKeys(rowKey);
      },
      getRowCount: function() {
        return this.__getRowCount();
      }

    });
  },

  _init: function(tableId, initParams, useAjax, rolloverClass, apiInitializationFunctionName, deferredBodyLoading) {
    var table = O$.initComponent(tableId, {rollover: rolloverClass}, {
      _useAjax: useAjax,

      getCurrentColumn: function() {
        return this._showingMenuForColumn ? this._showingMenuForColumn : null;
      },
      _loadRows: function(completionCallback) {
        O$.Ajax.requestComponentPortions(this.id, ["rows"], null, function(table, portionName, portionHTML, portionScripts, portionData) {
          if (portionName != "rows") throw "Unknown portionName: " + portionName;
          table.body._removeAllRows();
          O$.Table._acceptLoadedRows(table, portionName, portionHTML, portionScripts, portionData);
          if (completionCallback)
            completionCallback();
        });
      },
      _addLoadedRows: function(rowsData) {
        var newRows = this.__newRows;
        var afterRowIndex = this.__afterRowIndex;
        var newRowsToStylesMap = rowsData["rowStylesMap"];
        var newRowCellsToStylesMap = rowsData["cellStylesMap"];
        var rowKeys = rowsData["rowKeys"];

        this._insertRowsAfter(afterRowIndex, newRows, newRowsToStylesMap, newRowCellsToStylesMap, rowKeys);
      }

    });
    if (table._commonTableFunctionsInitialized)
      return;
    table._commonTableFunctionsInitialized = true;

    try {
      O$.Tables._init(table, initParams);
    } finally {
      table.style.visibility = "visible";
      // can't just exclude the "o_initially_invisible" from table.className because of IE issue (JSFC-2337)
    }
    O$.Table._initApiFunctions(table);

    table._originalClassName = table.className;

    table._unloadHandlers = [];
    var i, count;
    table.onComponentUnload = function() {
      for (i = 0,count = table._unloadHandlers.length; i < count; i++) {
        table._unloadHandlers[i]();
      }

      var filtersToHide = this._filtersToHide;
      if (!O$.isExplorer6() || !filtersToHide)
        return false;

      for (i = 0,count = filtersToHide.length; i < count; i++) {
        var filter = filtersToHide[i];
        filter.style.visibility = "hidden";
      }
      return true;
    };
    if (apiInitializationFunctionName) {
      var initFunction = eval(apiInitializationFunctionName);
      if (initFunction)
        initFunction(table);
    }

    table._cleanUp = function() {
      [table.header, table.body, table.footer].forEach(function (section) {
        if (section) section._rows = [];
      });
    };
    if (deferredBodyLoading)
      O$.addInternalLoadEvent(function() {
        var auxiliaryTags = O$(table.id + "::auxiliaryTags");
        table.parentNode.appendChild(auxiliaryTags);
        table._loadRows(function() {
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

  _initApiFunctions: function(table) {
    O$.extend(table, {
      __selectAllRows: function() {
        if (this._selectableItems != "rows")
          throw "selectAllRows: The table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (!this._multipleSelectionAllowed)
          throw "selectAllRows: The table is not set up for multiple selection. Table's clientId is: " + this.id;
        this._selectAllItems();
      },
      __clearSelection: function() {
        this._unselectAllItems();
      },
      __isSelectionEmpty: function() {
        var selectedItems = this._getSelectedItems();
        if (!selectedItems || selectedItems.length == 0)
          return true;
        return selectedItems[0] == -1;
      },
      __getSelectedRowIndex: function() {
        if (this._selectableItems != "rows")
          throw "getSelectedRowIndex: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (this._multipleSelectionAllowed)
          throw "getSelectedRowIndex can only used on a table with single selection mode; table's clientId is: " + this.id;

        var selectedItems = this._getSelectedItems();
        if (selectedItems.length == 0)
          return -1;
        return selectedItems[0];
      },
      __setSelectedRowIndex: function(rowIndex) {
        if (this._selectableItems != "rows")
          throw "setSelectedRowIndex: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (this._multipleSelectionAllowed)
          throw "setSelectedRowIndex can only used on a table with single selection mode; table's clientId is: " + this.id;
        var bodyRows = table.body._getRows();
        if ((rowIndex != -1) && (rowIndex < 0 || rowIndex >= bodyRows.length))
          throw "setSelectedRowIndex parameter is out of range (" + rowIndex + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;
        this._setSelectedItems(rowIndex != -1 ? [rowIndex] : []);
      },
      __getSelectedRowIndexes: function() {
        if (this._selectableItems != "rows")
          throw "getSelectedRowIndexes: The specified table is not set up for row selection. Selectable items are: " + this._selectableItems + "; table's clientId is: " + this.id;
        if (!this._multipleSelectionAllowed)
          throw "getSelectedRowIndexes can only used on a table with multiple selection mode; table's clientId is: " + this.id;

        var selectedItems = this._getSelectedItems();
        if (!selectedItems || (selectedItems.length == 1 && selectedItems[0] == -1))
          selectedItems = [];
        return selectedItems;
      },
      __setSelectedRowIndexes: function(rowIndexes) {
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
      __getSelectedRowKey: function() {
        var rowIndex = this.__getSelectedRowIndex();
        if (rowIndex == -1) return null;
        var rowKey = this.__getRowKey(rowIndex);
        return rowKey;
      },
      __setSelectedRowKey: function(rowKey) {
        var rowIndex = this.__getRowIndexByKey(rowKey);
        this.__setSelectedRowIndex(rowIndex);
      },
      __getSelectedRowKeys: function() {
        var indexes = this.__getSelectedRowIndexes();
        var keys = [];
        for (var i = 0, count = indexes.length; i < count; i++) {
          var idx = indexes[i];
          keys[i] = this.__getRowKey(idx);
        }
        return keys;
      },
      __setSelectedRowKeys: function(keys) {
        var indexes = [];
        for (var i = 0, count = keys.length; i < count; i++) {
          var key = keys[i];
          indexes[i] = this.__getRowIndexByKey(key);
        }
        this.__setSelectedRowIndexes(indexes);
      },
      __getRowCount: function() {
        if (this._params.body.noDataRows)
          return 0;
        var bodyRows = this.body._getRows();
        return bodyRows.length;
      },
      __getRowKey: function(rowIndex) {
        if (this._params.body.noDataRows)
          throw "There are no rows in this table";
        var bodyRows = this.body._getRows();
        if (rowIndex < 0 || rowIndex >= bodyRows.length)
          throw "getRowKey parameter is out of range (" + rowIndex + "); table's clientId is: " + this.id + "; number of rows is: " + bodyRows.length;
        return bodyRows[rowIndex]._rowKey;
      },
      __getRowIndexByKey: function(rowKey) {
        if (this._params.body.noDataRows)
          return -1;
        var bodyRows = this.body._getRows();
        for (var i = 0, count = bodyRows.length; i < count; i++) {
          var row = bodyRows[i];
          if (row._rowKey == rowKey)
            return i;
        }
        return -1;
      }
    });
  },

  _createTableWithoutTd: function () {
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

  _createImage: function (url) {
    var img = document.createElement("img");
    img.src = url;
    return img;
  },


  _acceptLoadedRows: function(table, portionName, portionHTML, portionScripts, portionData) {
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
          _leftRowNode: leftRowNode,
          _rowNode: centerRowNode,
          _rightRowNode: rightRowNode
        });
      }
      newRows = compositeRows;
    }
    table.__newRows = newRows;

    table._addLoadedRows(portionData);
    O$.Ajax.executeScripts(portionScripts);
  },

  // -------------------------- KEYBOARD NAVIGATION SUPPORT

  _initKeyboardNavigation: function(tableId, controlPaginationWithKeyboard, focusedClassName, canPageBack, canPageForth,
                                    canSelectLastPage, tabIndex) {
    var table = O$.initComponent(tableId, null, {
      _performPagingAction: function(actionStr) {
        O$.setHiddenField(this, this.id + "::pagination", actionStr);
        O$._submitInternal(this);
      },

      _nextPage: function() {
        if (canPageForth) this._performPagingAction("selectNextPage");
      },
      _previousPage: function() {
        if (canPageBack) this._performPagingAction("selectPrevPage");
      },
      _firstPage: function() {
        if (canPageBack) this._performPagingAction("selectFirstPage");
      },
      _lastPage: function() {
        if (canSelectLastPage) this._performPagingAction("selectLastPage");
      },
      _selectPageNo: function(pageNo) {
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
        case 63276: /* Safari for Mac */
          e.pageUpPressed = true;
          break;
        case 34: // page down
        case 63277: /* Safari for Mac */
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
            newIdx = O$.Table._checkNavigation(this, idx, rowCount, e);
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
            var newRangeEndRowIndex = O$.Table._checkNavigation(this, rangeEndRowIndex, rowCount, e);
            if (newRangeEndRowIndex != null) {
              passEvent = false;
              var newSelectedRowIndexes = O$.Table._combineSelectedRowsWithRange(this, this._baseSelectedRowIndexes, baseRowIndex, newRangeEndRowIndex);
              this._rangeEndRowIndex = newRangeEndRowIndex;
              this.__setSelectedRowIndexes(newSelectedRowIndexes);
              O$.Table._scrollToRowIndexes(this, newSelectedRowIndexes);
            }
          }

        }
        if (!this._multipleSelectionAllowed && noModifiersPressed) {              // ------ single selection
          idx = this.__getSelectedRowIndex();
          newIdx = O$.Table._checkNavigation(this, idx, rowCount, e);
          if (newIdx != null) {
            passEvent = false;
            this.__setSelectedRowIndex(newIdx);
            O$.Table._scrollToRowIndexes(this, [newIdx]);
          }
        }
      }
      if (passEvent) {
        if (this._onKeyboardNavigation)
          passEvent = this._onKeyboardNavigation(e);
      }
      if (!passEvent) {
        O$.breakEvent(e);
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

    table._prevOnfocus_kn = table.onfocus;
    table.onfocus = function(e) {
      if (this._submitting)
        return;
      if (this._prevOnfocus_kn)
        this._prevOnfocus_kn(e);
      var focusFld = O$(this.id + "::focused");
      focusFld.value = "true";
    };

    table._prevOnblur_kn = table.onblur;
    table.onblur = function(e) {
      if (this._submitting)
        return;
      if (this._prevOnblur_kn)
        this._prevOnblur_kn(e);
      var focusFld = O$(this.id + "::focused");
      focusFld.value = "false";
    };

    var focusFld = O$(table.id + "::focused");
    if (focusFld.value == "true") {
      setTimeout(function() {
        table.focus();
      }, 1);
    }

    table._unloadHandlers.push(function() {
      O$.Table._deinitializeKeyboardNavigation(table);
    });
  },

  _deinitializeKeyboardNavigation: function(table) {
    table.onfocus = null;
    table.onblur = null;
    if (table._focusControl) {
      table._focusControl.parentNode.removeChild(table._focusControl);
    }
  },

  _scrollToRowIndexes: function(table, rowIndexes) {
    var bodyRows = table.body._getRows();
    O$.scrollElementIntoView(rowIndexes.map(function(i) {
      return i != -1 && bodyRows[i]._rowNode;
    }));
  },

  _combineSelectedRowsWithRange: function(table, baseSelectedRowIndexes, baseRowIndex, rangeEndRowIndex) {
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

  _checkNavigation: function(table, idx, rowCount, e) {
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
    if (e.homePressed) {
      newIndex = O$.Table._getNeighboringVisibleRowIndex(table, idx, -rowCount);
    }
    if (e.endPressed) {
      newIndex = O$.Table._getNeighboringVisibleRowIndex(table, idx, rowCount);
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

  _getNeighboringVisibleRowIndex: function(table, startRowIndex, stepCount) {
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


  // -------------------------- TABLE SELECTION SUPPORT

  _initSelection: function(tableId, enabled, required, selectableItems,
                           selectionMode, selectedItems, selectionClass,
                           selectionChangeHandler, postEventOnSelectionChange, selectionColumnIndexes,
                           mouseSupport, keyboardSupport, trackLeafNodesOnly) {
    var table = O$.initComponent(tableId);
    O$.assert(!table._selectionInitialized, "O$.Table._initSelection shouldn't be called twice on the same table");
    O$.extend(table, {
              _selectionInitialized: true,

              _selectionEnabled: !table._params.body.noDataRows && enabled,
              _selectionRequired: required,
              _selectableItems: selectableItems,
              _multipleSelectionAllowed: selectionMode != "single",
              _selectionMode: selectionMode,
              _selectionClass: selectionClass,
              _selectionColumnIndexes: selectionColumnIndexes,
              _selectionMouseSupport: mouseSupport,
              _selectionKeyboardSupport: keyboardSupport && selectionMode != "hierarchical",
              _selectionTrackLeafNodeOnly: trackLeafNodesOnly,

              _setItemSelected_internal: function(itemIndex, selected) {
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
                  O$.Table._setSelectionCheckboxesSelected(row, selected);
                }
              },

              _getSelectedItems: function() {
                if (!this._selectedItems)
                  this._selectedItems = [];
                return [].concat(this._selectedItems);
              },

              _setSelectionFieldValue: function (value) {
                var selectionFieldId = this.id + "::selection";
                var selectionField = O$(selectionFieldId);
                O$.assert(selectionField, "Couldn't find selectionField by id: " + selectionFieldId);
                selectionField.value = value;
              },

              _setSelectedItems: function(items, forceUpdate) {
                var undefinedSelectionRows = [];

                if (table._selectionMode == "hierarchical") {
                  var bodyRows = this.body._getRows();
                  // first, retain only leaf items in the list of explicitly selected items since parent nodes are
                  // going to be selected implicitly
                  var correctedItems = [];
                  items.forEach(function(rowIndex) {
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
                    row._childRows.forEach(function(childRow) {
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
                    _pseudoRow: true,
                    _hasChildren: true,
                    _childRows: table._rootRows
                  });

                  items = correctedItems;
                }
                this._setSelectedItems_internal(items, forceUpdate);

                undefinedSelectionRows.forEach(function(rowIndex) {
                  table._setItemSelected_internal(rowIndex, null);
                });
              },

              _setSelectedItems_internal: function(items, forceUpdate) {
                if (items == null) items = [];
                var changesArray = [];
                var changesArrayIndexes = [];
                var oldSelectedItemsStr = "";
                var i;
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
                var newSelectedItemsStr = "";
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
                    setTimeout(function() {
                      O$.submitEnclosingForm(window._submittingTable);
                    }, 1);
                  }
                }
              },

              _selectAllItems: function() {
                O$.assert(this._multipleSelectionAllowed, "table._selectAllItems: multiple selection is not allowed for table: " + this.id);

                if (this._params.body.noDataRows)
                  return;
                if (this._selectableItems == "rows") {
                  var rows = this.body._getRows();
                  var allItems = [];
                  for (var i = 0, count = rows.length; i < count; i++)
                    allItems[i] = i;
                  this._setSelectedItems(allItems);
                }
              },

              _unselectAllItems: function() {
                this._setSelectedItems([]);
              },

              _isItemSelected: function(itemIndex) {
                var result = this._selectedItems.indexOf(itemIndex) != -1;
                return result;
              },

              _toggleItemSelected: function(itemIndex) {
                if (itemIndex == -1) {
                  O$.logError("_toggleItemSelected: itemIndex == -1");
                  return;
                }
                var selectedIndexes = this._getSelectedItems();
                var newArray = [];
                for (var i = 0, count = selectedIndexes.length; i < count; i++) {
                  var idx = selectedIndexes[i];
                  if (idx != itemIndex)
                    newArray.push(idx);
                }
                if (newArray.length == selectedIndexes.length)
                  newArray.push(itemIndex);
                this._setSelectedItems(newArray);
              },

              _toggleHierarchicalSelection: function(itemIndex) {
                var bodyRows = this.body._getRows();
                var row = bodyRows[itemIndex];
                var selectedItems = this._getSelectedItems();
                var wasInUndefinedState = row._selected === null;
                var select = wasInUndefinedState ? true : !row._isSelected();
                this._setHierarchicalSelectionForRow(selectedItems, row, select);
                this._setSelectedItems(selectedItems);
              },

              _setHierarchicalSelectionForRow: function(selectedItems, row, select) {
                if (!row._hasChildren || row._childRows.length == 0) {
                  var i = selectedItems.indexOf(row._index);
                  var selected = (i != -1);
                  if (selected && !select)
                    selectedItems.splice(i, 1);
                  if (!selected && select)
                    selectedItems.push(row._index);
                  return;
                }
                row._childRows.forEach(function(subRow) {
                  table._setHierarchicalSelectionForRow(selectedItems, subRow, select);
                });
              }
            });

    // run initialization code
    if (selectableItems == "rows") {
      var rows = table.body._getRows();
      for (var i = 0, count = rows.length; i < count; i++) {
        var row = rows[i];
        O$.Table._initRowForSelection(row);
      }
    }

    table._setSelectedItems(selectedItems);
    table._setSelectionFieldValue("");

    if (selectionChangeHandler) {
      eval("table.onchange = function(event) {if (!event._of_event)return;" + selectionChangeHandler + "}");
      // checking _of_event is needed if this is a bubbled event from some child
      table._fireOnSelectionChange = function() {
        O$.sendEvent(table, "change");
      };
      O$.Table._addSelectionChangeHandler(table, [table, "_fireOnSelectionChange"]);
    }
    table._postEventOnSelectionChange = postEventOnSelectionChange;

    table._addRowInsertionCallback(function(table, insertedAfterIndex, insertedRows) {
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
      if (!table._params.body.noDataRows) {
        if (!table._multipleSelectionAllowed)
          table.__setSelectedRowIndex(0);
        else
          table.__setSelectedRowIndexes([0]);
      }
    }
  },

  _initRowForSelection: function(row) {
    var table = row._table;
    O$.extend(row, {
      _isSelected: function() {
        return table._isItemSelected(this._index);
      }

    });

    if (table._selectionEnabled) {
      [row._leftRowNode, row._rowNode, row._rightRowNode].forEach(function(rowNode) {
        if (!rowNode) return;
        if (rowNode._originalClickHandler)
          O$.logError("O$.Table._initSelection: row click handler already initialized");
        rowNode._originalClickHandler = rowNode.onclick;
        rowNode.onclick = O$.Table._row_handleSelectionOnClick;
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
      [row._leftRowNode, row._rowNode, row._rightRowNode].forEach(function(rowNode) {
        if (!rowNode) return;
        var elements = rowNode.getElementsByTagName("input");
        for (var i = 0, count = elements.length; i < count; i++) {
          inputs.push(elements[i]);
        }
      });
    }

    for (var i = 0, count = inputs.length; i < count; i++) {
      var input = inputs[i];
      if (input.className && input.className.indexOf("o_selectRowCheckbox") != -1) {
        if (!row._selectRowCheckboxes) row._selectRowCheckboxes = [];
        row._selectRowCheckboxes.push(input);
        O$.Table._initSelectRowCheckbox(input, row);
      }
    }
  },

  _addSelectionChangeHandler: function(table, handler) {
    O$.assert(handler, "O$.Table._addSelectionChangeHandler: handler must be specified. table.id = " + table.id);
    var handlers = table._selectionChangeHandlers;
    if (!handlers) {
      handlers = [];
      table._selectionChangeHandlers = handlers;
    }
    handlers.push(handler);
  },

  _initSelectRowCheckbox: function(checkBox, row) {
    var table = row._table;
    checkBox.setSelected(false);
    checkBox.setDisabled(!table._selectionEnabled);
    checkBox.onclick = function(evt) {
      if (evt)
        event = evt;
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
      O$.stopEvent(event);

    };
    checkBox.ondblclick = O$.repeatClickOnDblclick;
  },

  _initSelectionCell: function(cell) {
    var checkBoxAsArray = O$.getChildNodesWithNames(cell, ["input"]);
    if (!checkBoxAsArray || checkBoxAsArray.length == 0)
      return;
    var checkBox = checkBoxAsArray[0];
    if (!checkBox)
      return;
    var row = cell._row;
    var table = row._table;
    O$.extend(cell, {
      _selectionCheckBox: checkBox,
      onclick: function(evt) {
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
            this._selectionCheckBox.onclick(evt);
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
      ondblclick: O$.repeatClickOnDblclick
    });

    O$.extend(checkBox, {
      checked: false,
      // fix for Mozilla's issue: reloading a page retains previous values for inputs regardless of their values received from server
      disabled: !table._selectionEnabled,

      _cell: cell,

      onclick: function(evt) {
        if (evt)
          event = evt;
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
      ondblclick: function(evt) {
        if (O$.isExplorer())
          this.click(evt);
        O$.stopEvent(evt);
      }
    });

    var cellRow = cell._row;
    if (!cellRow._selectionCheckBoxes)
      cellRow._selectionCheckBoxes = [];
    cellRow._selectionCheckBoxes.push(checkBox);
  },

  _row_handleSelectionOnClick: function(evt) {
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

  _formatSelectedItems: function(table, selectableItems, selectedItemIndexes) {
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
    }
    throw "O$.Table._formatSelectedItems: unknown selectableItems: " + selectableItems;
  },

  _setSelectionCheckboxesSelected: function(row, selected) {
    if (row._selectionCheckBoxes)
      row._selectionCheckBoxes.forEach(function(checkbox) {
        if (checkbox.isSelected) {
          if (selected == null)
            checkbox.setDefined(false);
          else
            checkbox.setSelected(selected);
        } else
          checkbox.checked = selected;
      });

    if (row._selectRowCheckboxes)
      row._selectRowCheckboxes.forEach(function(checkbox) {
        if (selected == null)
          checkbox.setDefined(false);
        else
          checkbox.setSelected(selected);
      });
  },

  _initCheckboxColHeader: function(headerId, tableId, colId) {
    var header = O$(headerId);
    var table = O$(tableId);
    if (!table)
      throw "SelectAllCheckbox must be placed in the column's header or footer of <o:dataTable> or <o:treeTable> component. clientId = " + headerId;
    header.style.cursor = "default";

    if (!table._checkBoxColumnHeaders)
      table._checkBoxColumnHeaders = [];
    if (!table._checkBoxColumnHeaders[colId])
      table._checkBoxColumnHeaders[colId] = [];
    var colHeadersArray = table._checkBoxColumnHeaders[colId];
    colHeadersArray.push(header);

    O$.extend(header, {
      _columnObjectId: colId,
      _updateFromCheckboxes: function(tableColumn) {
        var cells = tableColumn.body ? tableColumn.body._cells : [];
        var checkedCount = 0;
        for (var i = 0, count = cells.length; i < count; i++) {
          var cell = cells[i];
          if (!cell) continue;
          var checkBox = cell._checkBox;
          if (checkBox && (checkBox.isSelected ? checkBox.isSelected() : checkBox.checked))
            checkedCount++;
        }
        if (checkedCount == 0)
          this.setSelected(false);
        else if (checkedCount == cells.length)
          this.setSelected(true);
        else
          this.setDefined(false);
      },

      onclick: function(e) {
        var columnObj = O$(this._columnObjectId);
        var col = columnObj._tableColumn;
        O$.Table._setAllCheckboxes(col, this.isSelected());
        columnObj._updateHeaderCheckBoxes();
        col._updateSubmissionField();
        var evt = O$.getEvent(e);
        evt.cancelBubble = true;
      },
      ondblclick: function(e) {
        if (O$.isExplorer())
          this.click();
        var evt = O$.getEvent(e);
        evt.cancelBubble = true;
      }
    });

    setTimeout(function(){header._updateFromCheckboxes()}, 10);
  },

  _setAllCheckboxes: function(col, checked) {
    var cells = col.body ? col.body._cells : [];
    for (var i = 0, count = cells.length; i < count; i++) {
      var cell = cells[i];
      if (cell && cell._checkBox) {// to account for the "no data" row
        if (cell._checkBox.setSelected)
          cell._checkBox.setSelected(checked);
        else
          cell._checkBox.checked = checked;
      }
    }
  },

  _initSelectionHeader: function(headerId, tableId) {
    var header = O$(headerId);
    var table = O$(tableId);
    if (!table)
      throw "SelectAllCheckbox must be placed in a header or footer of <o:dataTable> or <o:treeTable> component. clientId = " + headerId;
    header.style.cursor = "default";
    O$.extend(header, {
      _updateStateFromTable: function() {
        if (!table._getSelectedItems) {
          // wait for table selection to be initialized
          setTimeout(function() {
            header._updateStateFromTable();
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

      onclick: function(e) {
        if (this.isSelected())
          table._selectAllItems();
        else
          table._unselectAllItems();
        var evt = O$.getEvent(e);
        evt.cancelBubble = true;
      },

      ondblclick: function(e) {
        if (O$.isExplorer())
          this.click();
        var evt = O$.getEvent(e);
        evt.cancelBubble = true;
      }
    });
    setTimeout(function() {header._updateStateFromTable()}, 10);
    O$.Table._addSelectionChangeHandler(table, [header, "_updateStateFromTable"]);
  },

  // -------------------------- CHECKBOX COLUMN SUPPORT

  _setCheckboxColIndexes: function(colId, checkedRowIndexes) {
    var columnObj = O$(colId);
    columnObj._setCheckedIndexes(checkedRowIndexes);
  },

  _initCheckboxCol: function(tableId, colIndex, colId, checkedRowIndexes) {
    var table = O$(tableId);
    var tableColumn = table._columns[colIndex];
    var columnObj = O$(colId);
    tableColumn._submissionField = columnObj;
    columnObj._tableColumn = tableColumn;

    columnObj._setCheckedIndexes = function(checkedIndexes) {
      var bodyCells = tableColumn.body ? tableColumn.body._cells : [];
      for (var i = 0, count = bodyCells.length; i < count; i++) {
        var cell = bodyCells[i];
        if (!cell)
          continue;
        cell._column = tableColumn;
        O$.Table._initCheckboxCell(cell, columnObj);
        if (cell._checkBox) {
          cell._checkBox.checked = checkedIndexes.indexOf(i) != -1;
        }
      }
      tableColumn._updateSubmissionField();
    };

    tableColumn._updateSubmissionField = function() {
      var bodyCells = tableColumn.body ? tableColumn.body._cells : [];
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
      this._submissionField.value = selectedRows;
    };

    columnObj._setCheckedIndexes(checkedRowIndexes);

    if (table._checkBoxColumnHeaders) {
      columnObj._headers = table._checkBoxColumnHeaders[colId];
    }
    columnObj._updateHeaderCheckBoxes = function() {
      if (!this._headers)
        return;
      var tableCol = this._tableColumn;
      for (var i = 0, count = this._headers.length; i < count; i++) {
        var header = this._headers[i];
        header._updateFromCheckboxes(tableCol);
      }
    };

    columnObj._updateHeaderCheckBoxes();
  },

  _initCheckboxCell: function(cell, colField) {
    if (cell._checkBoxCellInitialized)
      return;
    cell._checkBoxCellInitialized = true;
    var checkBoxAsArray = O$.getChildNodesWithNames(cell, ["input"]);
    var checkBox = checkBoxAsArray[0];
    if (!checkBox)
      return;
    checkBox._cell = cell;
    cell._checkBox = checkBox;
    cell._columnObj = colField;
    cell.onclick = function(evt) {
      if (evt)
        event = evt;
      if (event._checkBoxClickProcessed) {
        event.cancelBubble = true;
        return;
      }
      this._checkBox.checked = !this._checkBox.checked;
      this._processCheckboxChange();
      event.cancelBubble = true;
      return false;
    };
    cell.ondblclick = O$.repeatClickOnDblclick;

    checkBox.onclick = function(evt) {
      if (evt)
        event = evt;
      var checkBoxCell = this._cell;
      checkBoxCell._processCheckboxChange();
      event._checkBoxClickProcessed = true;
      event.cancelBubble = true;
    };
    checkBox.ondblclick = function(e) {
      if (O$.isExplorer())
        this.click(e);
      event.cancelBubble = true;
    };

    cell._processCheckboxChange = function() {
      var columnObj = this._columnObj;
      columnObj._updateHeaderCheckBoxes();
      cell._column._updateSubmissionField();
    };
  },

  // -------------------------- TABLE SORTING SUPPORT

  _initSorting: function(tableId, columnSortableFlags, sortedColIndex, sortableHeaderClass, sortableHeaderRolloverClass,
                         sortedColClass, sortedColHeaderClass, sortedColBodyClass, sortedColFooterClass,
                         sortingImagesToPreload) {
    var table = O$(tableId);
    O$.assert(table, "Couldn't find table by id: " + tableId);

    O$.preloadImages(sortingImagesToPreload);

    table._columns.forEach(function(column) {
      column._sortable = columnSortableFlags[column._index];
      if (!column._sortable)
        return;

      var colHeader = column.header ? column.header._cell : null;
      if (!colHeader)
        return;

      O$.setStyleMappings(colHeader, {sortableHeaderClass: sortableHeaderClass});

      O$.addEventHandler(colHeader, "click", function() {
        var focusField = O$(table.id + "::focused");
        if (focusField)
          focusField.value = true; // set true explicitly before it gets auto-set when the click bubbles up (JSFC-801)
        O$.Table._toggleColumnSorting(table, column._index);
      });

      O$.setupHoverStateFunction(colHeader, function(mouseInside) {
        O$.setStyleMappings(colHeader, {
          sortableHeaderRolloverClass: mouseInside ? sortableHeaderRolloverClass : null});
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
          sortedColClass: (table._params.forceUsingCellStyles || sortedColumn._useCellStyles) ? sortedColClass : null,
          sortedColHeaderClass: sortedColHeaderClass});

      O$.setStyleMappings(sortedColumn, {sortedColClass: sortedColClass});
      O$.setStyleMappings(sortedColumn.body, {sortedColBodyClass: sortedColBodyClass});
      sortedColumn._updateStyle();

      var footerCell = sortedColumn.footer ? sortedColumn.footer._cell : null;
      if (footerCell)
        O$.Tables._setCellStyleMappings(footerCell, {
          sortedColClass: (table._params.forceUsingCellStyles || sortedColumn._useCellStyles) ? sortedColClass : null,
          sortedColFooterClass: sortedColFooterClass});
    }

  },

  _toggleColumnSorting: function(table, columnIndex) {
    var sortingFieldId = table.id + "::sorting";
    var sortingField = O$(sortingFieldId);
    sortingField.value = "" + columnIndex;
    O$._submitInternal(table);
  },

  _performPaginatorAction: function(tableId, field, paramName, paramValue) {
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

  _initColumnResizing: function(tableId, retainTableWidth, minColWidth, resizeHandleWidth, columnParams, autoSaveState) {
    O$.addLoadEvent(function() {
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

      var table = O$(tableId);
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
        return table._columns.map(function(col) {
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

      table._addCellInsertionCallback(function(cell/*, row, column*/) {
        cell.style.overflow = "hidden";
      });

      table._columns.forEach(function(column) {
        var headerCell = column.header && column.header._cell;
        if (!headerCell) return;

        var widthCompensationColIndex = -1;
        if (retainTableWidth) {
          var cols = !table._params.scrolling || !table._params.scrolling.horizontal
                  ? table._columns
                  : function() {
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
        O$.correctElementZIndex(resizeHandle, headerCell, O$.Table.HEADER_CELL_Z_INDEX_COLUMN_MENU_RESIZE_HANDLE);
        column._resizeHandle = resizeHandle;
        O$.extend(resizeHandle, {
          _column: column,
          onmouseover: function() {
            if (this._draggingInProgress)
              return;
            if (!table.parentNode)
              this.parentNode.removeChild(this);
            else
              this._updatePos();
            // don't let parent header cell hover to be activated since the handle is logically out of column (IE)
            if (!table._showingMenuForColumn)
              table._columns.forEach(function(c) {
                var headerCell = c.header && c.header._cell;
                if (headerCell && headerCell.setForceHover) headerCell.setForceHover(false);
              });
          },
          onmouseout: function() {
            if (table._columnResizingInProgress) return;
            // don't let parent header cell hover to be activated since the handle is logically out of column (IE)
            if (!table._showingMenuForColumn)
              setTimeout(function() {
                table._columns.forEach(function(c) {
                  var headerCell = c.header && c.header._cell;
                  if (headerCell && headerCell.setForceHover) headerCell.setForceHover(null);
                });
              }, 1);
          },
          onmousedown: function (e) {
            setTimeout(function() {
              table._columns.forEach(function(c) {
                var headerCell = c.header && c.header._cell;
                if (headerCell && headerCell.setForceHover) headerCell.setForceHover(false);
              });
            }, 1);
            O$.startDragging(e, this);
          },
          onclick: function(e) {
            O$.breakEvent(e);
          },
          ondragstart: function() {
            table._columnResizingInProgress = true;
            var resizeDecorator = document.createElement("div");
            resizeDecorator.style.position = "absolute";
            resizeDecorator.style.borderLeft = "0px none transparent";//"1px solid gray";
            //            table.parentNode.appendChild(resizeDecorator);
            this._column._resizeDecorator = resizeDecorator;
            resizeDecorator._column = this._column;

            resizeDecorator._updatePos = function() {
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
          setLeft: function(left) {
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
              this._column._verticalArea._areas.forEach(function(a) {
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
          setTop: function(top) {
            this.style.top = top + "px";
          },
          ondragend: function() {
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
              O$.Ajax.requestComponentPortions(table.id, ["columnResizingState"], null, function() {
                // no client-side updates are required -- the request was just for saving data
              }, null, true);
            }
            if (table._focusable) {
              if (!table._focused)
                table.focus();
            }
          },
          _updatePos: function() {
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

        column._resizeHandle._updatePos();
      });

      function fixWidths() {
        if (table._params.scrolling) return;
        var colWidths = getColWidths();
        table.style.tableLayout = "fixed";

        //        if (!table._params.scrolling)
        //          O$.Tables._alignTableColumns(table._columns.map(function(c){return c._colTags[0];}), table, true, null,
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
          setTimeout(function() {
            table.style.tableLayout = "fixed";
          }, 10);
        }

      }

      fixWidths();

      function updateResizeHandlePositions() {
        for (var i = 0, count = table._columns.length; i < count; i++) {
          var column = table._columns[i];
          if (column._resizeHandle && column._resizeHandle.parentNode)
            column._resizeHandle._updatePos();
        }
      }

      O$.addEventHandler(window, "resize", updateResizeHandlePositions);
      O$.addEventHandler(table, "mouseover", function() {
        if (!table._columnResizingInProgress)
          updateResizeHandlePositions();
      });
      if (table._params.scrolling && (O$.isExplorer6() || O$.isExplorer7())) {
        // mouseover can't be handled in these circumstances for some reason
        var updateIntervalId = setInterval(function() {
          if (table.parentNode == null) {
            clearInterval(updateIntervalId);
            return;
          }
          if (!table._columnResizingInProgress)
            updateResizeHandlePositions();
        }, 1000);
      }
      var prevOnscroll = table.onscroll;
      table.onscroll = function(e) {
        if (prevOnscroll) prevOnscroll.call(table, e);
        setTimeout(updateResizeHandlePositions, 10);
      };

      table._unloadHandlers.push(function() {
        O$.removeEventHandler(window, "resize", updateResizeHandlePositions);
      });

      table._fixFF3ColResizingIssue = function() { // See JSFC-3720
        if (! (O$.isMozillaFF3() && O$.isQuirksMode()))
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

  _initColumnReordering: function(tableId,
                                  draggedCellClass, draggedCellTransparency,
                                  autoScrollAreaClass, autoScrollAreaTransparency, autoScrollLeftImage, autoScrollRightImage,
                                  dropTargetClass, dropTargetTopImage, dropTargetBottomImage) {
    var table = O$(tableId);
    var autoscrollingSpeed = 100;
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
      leftAutoScrollArea._update = function() {
        this.style.visibility = mainScroller.scrollLeft > 0 ? "visible" : "hidden";
      };
      var rightAutoScrollArea = autoScrolArea(autoScrollRightImage);
      rightAutoScrollArea._update = function() {
        this.style.visibility = mainScroller.scrollLeft < mainScroller.scrollWidth - mainScroller.clientWidth ? "visible" : "hidden";
      };

    }

    var dropTargetMark = function() {
      var dropTarget = document.createElement("div");
      dropTarget.className = dropTargetClass;
      var width = O$.calculateNumericCSSValue(O$.getStyleClassProperty(dropTargetClass, "width"));
      dropTarget.setPosition = function(x, y1, y2) {
        O$.setElementBorderRectangle(dropTarget, new O$.Rectangle(x - width / 2, y1, width, y2 - y1));
        var topImageSize = O$.getElementSize(topImage);
        O$.setElementPos(topImage, {x: x - topImageSize.width / 2, y: y1 - topImageSize.height});
        var bottomImageSize = O$.getElementSize(bottomImage);
        O$.setElementPos(bottomImage, {x: x - bottomImageSize.width / 2, y: y2});
      };
      var topImage = O$.Table._createImage(dropTargetTopImage);
      var bottomImage = O$.Table._createImage(dropTargetBottomImage);
      topImage.style.position = "absolute";
      bottomImage.style.position = "absolute";
      dropTarget.show = function(container) {
        container.appendChild(dropTarget);
        container.appendChild(topImage);
        container.appendChild(bottomImage);
      };
      dropTarget.hide = function() {
        if (dropTarget.parentNode) dropTarget.parentNode.removeChild(dropTarget);
        if (topImage.parentNode) topImage.parentNode.removeChild(topImage);
        if (bottomImage.parentNode) bottomImage.parentNode.removeChild(bottomImage);
      };
      O$.correctElementZIndex(dropTarget, table, 1);
      return dropTarget;
    }();

    table._columns.forEach(function(sourceColumn) {
      if (!interGroupDraggingAllowed && sourceColumn.parentColumn) {
        if (sourceColumn.parentColumn._columns.length == 1)
          return; // there are no other columns in this group for possible reordering
      }
      if (!columnFixingAllowed && sourceColumn._scrollingArea && sourceColumn._scrollingArea._columns.length == 1) {
        return; // there are no other columns in this scrolling area for possible reordering
      }

      var headerCell = sourceColumn.header ? sourceColumn.header._cell : null;
      if (!headerCell) return;
      headerCell._clone = function() {
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

      O$.makeDraggable(headerCell, function(evt) {
        var dropTargets = [];
        table._columns.forEach(function(targetColumn) {
          function dropTarget(minX, maxX, minY, maxY, columnOrGroup, rightEdge) {
            var container = O$.getContainingBlock(headerCell, true);
            if (!container)
              container = O$.getDefaultAbsolutePositionParent();

            return {
              minX: minX,
              maxX: maxX,
              minY: minY,
              maxY: maxY,
              eventInside: function(evt) {
                var cursorPos = O$.getEventPoint(evt, headerCell);
                return (this.minX == null || cursorPos.x >= this.minX) &&
                       (this.maxX == null || cursorPos.x < this.maxX);
              },
              setActive: function(active) {
                if (active) {
                  dropTargetMark.show(container);
                  var gridLineWidthCorrection = function() {
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
                  dropTargetMark.setPosition((rightEdge ? maxX : minX) - gridLineWidthCorrection / 2, minY, maxY);
                } else {
                  dropTargetMark.hide();
                }
              },
              acceptDraggable: function(cellHeader) {
                var col = columnOrGroup;
                while (col.subColumns)
                  col = !rightEdge ? col.subColumns[0] : col.subColumns[col.subColumns.length - 1];
                var targetColIndex = !rightEdge ? col._index : col._index + 1;
                sendColumnMoveRequest(cellHeader._column._index, targetColIndex);
              }
            };
          }

          if (!interGroupDraggingAllowed && targetColumn._parentColumn != sourceColumn._parentColumn) {
            while (targetColumn._parentColumn) {
              targetColumn = targetColumn._parentColumn;
              if (targetColumn._parentColumn == sourceColumn._parentColumn)
                break;
            }
            if (targetColumn._parentColumn != sourceColumn._parentColumn)
              return;
          }
          if (!columnFixingAllowed) {
            if (targetColumn._scrollingArea != sourceColumn._scrollingArea)
              return;
          }
          var targetCell = targetColumn.header ? targetColumn.header._cell : null;
          if (!targetCell) return;
          var targetCellRect = O$.getElementBorderRectangle(targetCell, true);
          var targetCellRect2 = function() {
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
          dropTargets.push(dropTarget(min, mid, minY, maxY, targetColumn, false));
          dropTargets.push(dropTarget(mid, max, minY, maxY, targetColumn, true));
        });
        dropTargets[0].minX = null;
        dropTargets[dropTargets.length - 1].maxX = null;
        for (var i = 0, count = dropTargets.length; i < count; i++) {
          var dropTarget = dropTargets[i];
          if (dropTarget.eventInside(evt))
            return dropTarget;
        }
        return null;
      });
      var additionalAreaListener;

      var activeHelperArea = null;
      var activeScrollingInterval;

      O$.extend(headerCell, {
        ondragstart: function() {
          if (!(table._params.scrolling && table._params.scrolling.horizontal)) return;
          additionalAreaContainer.appendChild(leftAutoScrollArea);
          additionalAreaContainer.appendChild(rightAutoScrollArea);
          leftAutoScrollArea._update();
          rightAutoScrollArea._update();

          additionalAreaListener = O$.listenProperty(headerScroller, "rectangle", function(rect) {
            var subHeaderIndex = table._subHeaderRowIndex;
            var subHeaderHeight = subHeaderIndex != -1
                    ? O$.getElementSize(table.header._getRows()[subHeaderIndex]._rowNode).height : 0;
            O$.setElementHeight(leftAutoScrollArea, rect.height - subHeaderHeight);
            O$.setElementHeight(rightAutoScrollArea, rect.height - subHeaderHeight);
            O$.alignPopupByElement(leftAutoScrollArea, headerScroller, O$.LEFT, O$.CENTER, 0, -subHeaderHeight / 2, true, true);
            O$.alignPopupByElement(rightAutoScrollArea, headerScroller, O$.RIGHT, O$.CENTER, 0, -subHeaderHeight / 2, true, true);
          }, new O$.Timer(50));
        },
        ondragmove: function(e) {
          e = {clientX: e.clientX, clientY: e.clientY};
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
              activeScrollingInterval = setInterval(function() {
                var scrollLeft = mainScroller.scrollLeft - scrollingStep();
                if (scrollLeft < 0) scrollLeft = 0;
                mainScroller.scrollLeft = scrollLeft;
                O$._draggedElement.updateCurrentDropTarget(e);
                leftAutoScrollArea._update();
                rightAutoScrollArea._update();
              }, 30);
            if (area == rightAutoScrollArea)
              activeScrollingInterval = setInterval(function() {
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
        ondragend: function() {
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


    function sendColumnMoveRequest(srcColIndex, dstColIndex) {
      if (dstColIndex == srcColIndex || dstColIndex == srcColIndex + 1)
        return;
      O$._submitInternal(table, null, [
        [table.id + "::reorderColumns", srcColIndex + "->" + dstColIndex]
      ]);
    }
  },

  HEADER_CELL_Z_INDEX_COLUMN_MENU_BUTTON: 1,
  HEADER_CELL_Z_INDEX_COLUMN_MENU_RESIZE_HANDLE: 2

};

// -------------------------- COLUMN MENU SUPPORT
O$.ColumnMenu = {

  _init: function(columnMenuId, tableId, columnMenuButtonId, sortAscMenuId, sortDescMenuId) {
    var table = O$(tableId);
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
    var columnMenu = O$(columnMenuId);
    columnMenu._sortAscMenuId = sortAscMenuId;
    columnMenu._sortDescMenuId = sortDescMenuId;

    table._unloadHandlers.push(function() {
      if (columnMenu.parentNode)
        columnMenu.parentNode.removeChild(columnMenu);
    });
    var columnMenuButton = O$(columnMenuButtonId);
    var columnMenuButtonTable = function() {
      var result = O$.Table._createTableWithoutTd();
      result.style.position = "absolute";
      result._tr.appendChild(columnMenuButton);
      O$.setOpacityLevel(result, 1 - menuInvokerAreaTransparency);
      O$.extend(result, {
        showForCell: function(cell) {
          this.hide();
          cell.appendChild(this);
          O$.correctElementZIndex(this, cell, O$.Table.HEADER_CELL_Z_INDEX_COLUMN_MENU_BUTTON);
          var rightOffset = O$.getNumericElementStyle(cell, "border-right-width");
          var bottomOffset = O$.getNumericElementStyle(cell, "border-bottom-width");
          O$.setElementHeight(this, O$.getElementSize(cell).height - bottomOffset);
          O$.alignPopupByElement(this, cell, O$.RIGHT, O$.BOTTOM, rightOffset, bottomOffset, false, true);
          this._showForCell = cell;
        },
        hideForCell: function(cell) {
          if (this._showForCell == cell)
            this.hide();
        },
        hide: function() {
          if (this.parentNode) {
            this.parentNode.removeChild(this);
            this._showForCell = null;
          }
        }
      });
      return result;
    }();

    var currentColumn = null;
    var menuOpened = false;
    table._columns.forEach(function(column) {
      if (!column.header || !column.header._cell || !column.menuAllowed) return;
      var headerCell = column.header._cell;
      O$.setupHoverStateFunction(headerCell, function(mouseOver) {
        if (mouseOver && !menuOpened) {
          currentColumn = column;
          columnMenuButtonTable.showForCell(headerCell);
        } else {
          if (currentColumn == column)
            currentColumn = null;
          columnMenuButtonTable.hideForCell(headerCell);
        }
      });

    });

    columnMenuButton.onclick = function(e) {
      O$.breakEvent(e);
    };
    O$.addEventHandler(columnMenuButton, "mousedown", function(evt) {
      O$.breakEvent(evt);
      O$.correctElementZIndex(columnMenu, currentColumn._resizeHandle);
      table._showingMenuForColumn = currentColumn;
      columnMenu._column = currentColumn;
      if (columnMenu.parentNode != newMenuParent) {
        newMenuParent.appendChild(columnMenu);
        O$.correctElementZIndex(columnMenu, columnMenuButton);
      }
      O$.ColumnMenu._checkSortMenuItems(columnMenuId, tableId, columnMenu._sortAscMenuId, columnMenu._sortDescMenuId,
              currentColumn._index);
      columnMenu._showByElement(columnMenuButtonTable, O$.LEFT, O$.BELOW, 0, 0);
      var prevOnhide = columnMenu.onhide;
      var headerCell = currentColumn.header._cell;
      headerCell.setForceHover(true);
      columnMenuButton.setForceHover(true);
      menuOpened = true;
      columnMenu.onhide = function(e) {
        setTimeout(function() {
          table._showingMenuForColumn = null;
          columnMenu._column = null;
        }, 1);

        if (prevOnhide)
          prevOnhide.call(columnMenu, e);
        columnMenuButton.setForceHover(null);
        headerCell.setForceHover(null);
        menuOpened = false;
      };
    });


  },

  _initColumnVisibilityMenu: function(menuId, tableId) {
    var menu = O$(menuId);
    var table = O$(tableId);
    var idx = 0;
    menu._items.forEach(function(menuItem) {
      var colIndex = idx++;
      menuItem._anchor.onclick = function() {
        O$.ColumnMenu._toggleColumnVisibility(table, colIndex);
      };
    });

  },

  _toggleColumnVisibility: function(table, columnIndex) {
    if (columnIndex == undefined) {
      columnIndex = table._showingMenuForColumn._index;
    }
    O$._submitInternal(table, null, [
      [table.id + "::columnVisibility", columnIndex]
    ]);
  },

  _checkSortMenuItems : function(columnMenuId, tableId, sortAscMenuId, sortDescMenuId, columnIndex) {
    if (!sortAscMenuId && !sortDescMenuId) return;
    var table = O$(tableId);
    var columnMenu = O$(columnMenuId);
    if (columnIndex == undefined) {
      columnIndex = table._showingMenuForColumn._index;
    }
    var column = table._columns[columnIndex];
    if (!column._sortable) {
      var sortAscMenuItem = O$(sortAscMenuId);
      if (sortAscMenuItem) {
        table._sortAscMenuItem = sortAscMenuItem;
        for (var i = 0; i < columnMenu.childNodes.length; i++) {
          if (sortAscMenuItem == columnMenu.childNodes[i]) {
            table._sortAscMenuIdx = i;
            break;
          }
        }
        columnMenu.removeChild(sortAscMenuItem);
      }
      var sortDescMenuItem = O$(sortDescMenuId);
      if (sortDescMenuItem) {
        table._sortDescMenuItem = sortDescMenuItem;
        for (i = 0; i < columnMenu.childNodes.length; i++) {
          if (sortDescMenuItem == columnMenu.childNodes[i]) {
            table._sortDescMenuIdx = i;
            break;
          }
        }
        columnMenu.removeChild(sortDescMenuItem);
      }
    } else {
      var sortDescMenuItem = table._sortDescMenuItem;
      if (sortDescMenuItem) {
        if (table._sortDescMenuIdx >= 0
            && columnMenu.childNodes.length > table._sortDescMenuIdx)
          columnMenu.insertBefore(sortDescMenuItem,
              columnMenu.childNodes[table._sortDescMenuIdx]);
        else
          columnMenu.appendChild(sortDescMenuItem);
        table._sortDescMenuItem = null;
        table._sortDescMenuIdx = -1;
      }
      var sortAscMenuItem = table._sortAscMenuItem;
      if (sortAscMenuItem) {
        if (table._sortAscMenuIdx >= 0
            && columnMenu.childNodes.length > table._sortAscMenuIdx)
          columnMenu.insertBefore(sortAscMenuItem,
              columnMenu.childNodes[table._sortAscMenuIdx]);
        else
          columnMenu.appendChild(sortAscMenuItem);
        table._sortAscMenuItem = null;
        table._sortAscMenuIdx = -1;
      }
    }
  },

  _sortColumnAscending: function(tableId, columnIndex) {
    var table = O$(tableId);
    if (columnIndex == undefined) {
      columnIndex = table._showingMenuForColumn._index;
    }
    var column = table._columns[columnIndex];
    if (!column._sortable) return;
    O$._submitInternal(table, null, [
      [table.id + "::sortAscending", columnIndex]
    ]);
  },

  _sortColumnDescending: function(tableId, columnIndex) {
    var table = O$(tableId);
    if (columnIndex == undefined) {
      columnIndex = table._showingMenuForColumn._index;
    }
    var column = table._columns[columnIndex];
    if (!column._sortable) return;
    O$._submitInternal(table, null, [
      [table.id + "::sortDescending", columnIndex]
    ]);
  },


  _hideColumn: function(tableId, columnIndex) {
    var table = O$(tableId);
    if (columnIndex == undefined) {
      columnIndex = table._showingMenuForColumn._index;
    }
    O$._submitInternal(table, null, [
      [table.id + "::hideColumn", columnIndex]
    ]);

  }
};