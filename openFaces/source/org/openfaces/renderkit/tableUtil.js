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

O$.Tables = {

  // -------------------------- COMMON UTILITIES

  _getUserStylePropertyValue: function(element, propertyName, defaultValue1, defaultValue2) {
    var value = O$.getElementStyleProperty(element, propertyName);
    if (!value || value == defaultValue1 || value == defaultValue2)
      return null;
    return value;
  },

  _getUserClassPropertyValue: function(element, propertyName, defaultValue1, defaultValue2) {
    var value = O$.getStyleClassProperty(element.className, propertyName);
    if (!value || value == defaultValue1 || value == defaultValue2)
      return null;
    return value;
  },

  _getRowCells: function(row) {
    return row.cells;
  },

  _getCellColSpan: function(cell) {
    var colSpan = cell.colSpan;
    if (!colSpan || colSpan == -1)
      colSpan = 1;
    return colSpan;
  },

  _handleUnsupportedRowStyleProperties: function(row) {
    var cells = row._cells;
    var cellIndex, cellCount, cell;
    if (!O$.isExplorer()) {
      var paddingLeft = O$.Tables._getUserStylePropertyValue(row, "padding-left", "0px");
      var paddingRight = O$.Tables._getUserStylePropertyValue(row, "padding-right", "0px");
      var paddingTop = O$.Tables._getUserStylePropertyValue(row, "padding-top", "0px");
      var paddingBottom = O$.Tables._getUserStylePropertyValue(row, "padding-bottom", "0px");
      var lineHeight = O$.Tables._getUserClassPropertyValue(row, "line-height", "normal");

      if (paddingLeft || paddingRight || paddingTop || paddingBottom || lineHeight) {
        for (cellIndex = 0, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
          cell = cells[cellIndex];
          O$.Tables._setCellStyleProperty(cell, "paddingLeft", paddingLeft);
          O$.Tables._setCellStyleProperty(cell, "paddingRight", paddingRight);
          O$.Tables._setCellStyleProperty(cell, "paddingTop", paddingTop);
          O$.Tables._setCellStyleProperty(cell, "paddingBottom", paddingBottom);
          O$.Tables._setCellStyleProperty(cell, "lineHeight", lineHeight);
        }
      }
    }

    var propertyValues = O$.Tables._evaluateStyleClassProperties_cached(
            row.className,
            ["borderLeft", "borderRight", "borderTop", "borderBottom"],
            row._row ? row._row._table : row._table);
    if (propertyValues.borderLeft || propertyValues.borderRight || propertyValues.borderTop || propertyValues.borderBottom) {
      for (cellIndex = 0, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
        cell = cells[cellIndex];
        O$.Tables._setCellStyleProperty(cell, "borderLeft", propertyValues.borderLeft);
        O$.Tables._setCellStyleProperty(cell, "borderRight", propertyValues.borderRight);
        O$.Tables._setCellStyleProperty(cell, "borderTop", propertyValues.borderTop);
        O$.Tables._setCellStyleProperty(cell, "borderBottom", propertyValues.borderBottom);
      }
    }
  },

  _evaluateStyleClassProperties_cached: function(className, propertyNames, cacheInElement) {
    if (!cacheInElement.__stylePropertyCache)
      cacheInElement.__stylePropertyCache = [];
    var stylePropertiesKey = className + "___" + propertyNames.join("__");
    var propertyValues = cacheInElement.__stylePropertyCache[stylePropertiesKey];
    if (!propertyValues) {
      propertyValues = O$.getStyleClassProperties(className, propertyNames);
      cacheInElement.__stylePropertyCache[stylePropertiesKey] = propertyValues;
    }
    return propertyValues;
  },


  // -------------------------- INITIALIZATION

  /**
   *
   * The columns parameter defines a hierarchy of all table's columns with all their configuration settings. It is an
   * array of column specification objects. Each column specification object contains the following properties:
   *   - scrolling
   *   - className
   *   - hasCellWrappers
   *   - header: {
   *     - pos: {row: int, cell: int}
   *     - className: String
   *     - onclick, ondblclick and all other events: String
   *   }
   *   - subHeader: {
   *     - pos: {row: int, cell: int}
   *     - className: String
   *     - onclick, ondblclick and all other events: String
   *   }
   *   - body: {
   *     - className: String
   *     - oddClassName: String
   *     - evenClassName: String
   *     - onclick, ondblclick and all other events: String
   *   }
   *   - footer: {
   *     - pos: {row: int, cell: int}
   *     - className: String
   *     - onclick, ondblclick and all other events: String
   *   - subColumns: [array of column specification objects]
   */
  _init: function(scrolling, columns, commonHeaderExists, subHeaderRowExists, commonFooterExists, noDataRows,
                                 gridLines, rowStyles, rowStylesMap, cellStylesMap, forceUsingCellStyles,
                                 additionalCellWrapperStyle, invisibleRowsAllowed) {
    var table = this;
    table._scrolling = scrolling;
    table._commonHeaderExists = commonHeaderExists;
    table._commonFooterExists = commonFooterExists;
    table._subHeaderRowExists = subHeaderRowExists;
    table._noDataRows = noDataRows;
    table._forceUsingCellStyles = forceUsingCellStyles;
    table._invisibleRowsAllowed = invisibleRowsAllowed;

    (function(bodyRowClass, bodyOddRowClass, rolloverRowClass, commonHeaderRowClass, headerRowClass, filterRowClass, commonFooterRowClass, footerRowClass) {
      table._bodyRowClass = bodyRowClass;
      table._bodyOddRowClass = bodyOddRowClass;
      table._rolloverRowClass = rolloverRowClass;
      table._commonHeaderRowClass = commonHeaderRowClass;
      table._headerRowClass = headerRowClass;
      table._filterRowClass = filterRowClass;
      table._commonFooterRowClass = commonFooterRowClass;
      table._footerRowClass = footerRowClass;
    }).apply(null, rowStyles);
    table._rowStylesMap = rowStylesMap;
    table._cellStylesMap = cellStylesMap;
    table._additionalCellWrapperStyle = additionalCellWrapperStyle;

    // initialize grid lines
    var tempIdx = 0;
    table._horizontalGridLines = gridLines[tempIdx++];
    table._verticalGridLines = gridLines[tempIdx++];
    table._commonHeaderSeparator = gridLines[tempIdx++];
    table._commonFooterSeparator = gridLines[tempIdx++];
    table._headerHorizSeparator = gridLines[tempIdx++];
    table._headerVertSeparator = gridLines[tempIdx++];
    table._filterRowSeparator = gridLines[tempIdx++];
    table._multiHeaderSeparator = gridLines[tempIdx++];
    table._multiFooterSeparator = gridLines[tempIdx++];
    table._footerHorizSeparator = gridLines[tempIdx++];
    table._footerVertSeparator = gridLines[tempIdx++];

    table.style.emptyCells = "show";
    if (O$.isExplorer()) {
      var bordersNeeded =
              table._horizontalGridLines ||
              table._verticalGridLines ||
              table._commonHeaderSeparator ||
              table._commonFooterSeparator ||
              table._headerHorizSeparator ||
              table._headerVertSeparator ||
              table._filterRowSeparator ||
              table._footerHorizSeparator ||
              table._footerVertSeparator;
      if (bordersNeeded)
        table.style.borderCollapse = "collapse";
    }

    table._rowInsertionCallbacks = [];
    table._addRowInsertionCallback = function(callback) {
      table._rowInsertionCallbacks.push(callback);
    };
    table._singleRowInsertionCallbacks = [];
    table._addSingleRowInsertionCallback = function(callback) {
      table._singleRowInsertionCallbacks.push(callback);
    };
    table._cellInsertionCallbacks = [];
    table._addCellInsertionCallback = function(callback) {
      table._cellInsertionCallbacks.push(callback);
    };
    table._cellMoveCallbacks = [];
    table._addCellMoveCallback = function(callback) {
      table._cellMoveCallbacks.push(callback);
    };

    O$.Tables._initRows(table);
    O$.Tables._initColumns(table, columns);
    O$.Tables._initColumnEvents(table);
    O$.Tables._initColumnStyles(table);
    O$.Tables._initGridLines(table);
    if (subHeaderRowExists) {
      var subHeaderRow = table.header._getRows()[table._subHeaderRowIndex];
      if (subHeaderRow._leftRowNode) O$.fixInputsWidthStrict(subHeaderRow._leftRowNode);
      O$.fixInputsWidthStrict(subHeaderRow._rowNode);
      if (subHeaderRow._rightRowNode) O$.fixInputsWidthStrict(subHeaderRow._rightRowNode);
    };

    table._removeAllRows = O$.Tables._removeAllRows;
    table._insertRowsAfter = O$.Tables._insertRowsAfter;
    table._cellFromPoint = function(x, y, relativeToNearestContainingBlock, cachedDataContainer) {
      var row = table._rowFromPoint(x, y, relativeToNearestContainingBlock, cachedDataContainer);
      if (!row)
        return null;
      return row._cellFromPoint(x, y, relativeToNearestContainingBlock, cachedDataContainer);
    };
    table._rowFromPoint = function(x, y, relativeToNearestContainingBlock, cachedDataContainer) {
      var tableRect = O$.getElementBorderRectangle(table, relativeToNearestContainingBlock, cachedDataContainer);
      if (!tableRect.containsPoint(x, y))
        return null;
      var rows = table.body._getRows();
      for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
        var row = rows[rowIndex];
        var rowRect = O$.getElementBorderRectangle(row._rowNode, relativeToNearestContainingBlock, cachedDataContainer);
        if (rowRect.containsPoint(x, y))
          return row;
        if (row._leftRowNode) {
          rowRect = O$.getElementBorderRectangle(row._leftRowNode, relativeToNearestContainingBlock, cachedDataContainer);
          if (rowRect.containsPoint(x, y))
            return row;
        }
        if (row._rightRowNode) {
          rowRect = O$.getElementBorderRectangle(row._rightRowNode, relativeToNearestContainingBlock, cachedDataContainer);
          if (rowRect.containsPoint(x, y))
            return row;
        }
      }
      return null;
    };

  },

  _removeAllRows: function() {
    var bodyRows = this.body._getRows();
    for (var i = 0, count = bodyRows.length; i < count; i++) {
      var row = bodyRows[i];
      function removeRow(row) {if (row) row.parentNode.removeChild(row);}
      removeRow(row._leftRowNode);
      removeRow(row._rowNode);
      removeRow(row._rightRowNode);
    }
    this.body._rows = undefined;

    this._rowStylesMap = {};
    this._cellStylesMap = {};
  },

  _insertRowsAfter: function(afterIndex, rowsToInsert, newRowsToStylesMap, newRowCellsToStylesMap) {
    if (rowsToInsert.length == 0) {
      return;
    }

    var bodyRows = this.body._getRows();
    var columns = this._columns;

    var i, count, row;
    for (i = 0, count = bodyRows.length, visibleRowCount = 0; i < count; i++) {
      row = bodyRows[i];
      row._visibleRowIndex = visibleRowCount;
      if (row._isVisible())
        visibleRowCount++;
    }

    var visibleRowsUpToReferenceRow; // visible rows including bodyRows[afterIndex]
    if (!this._invisibleRowsAllowed)
      visibleRowsUpToReferenceRow = afterIndex + 1;
    else {
      visibleRowsUpToReferenceRow = 0;
      for (i = 0; i <= afterIndex; i++) {
        row = bodyRows[i];
        if (row._isVisible())
          visibleRowsUpToReferenceRow++;
      }
    }

    var visibleInsertedRows;
    if (!this._invisibleRowsAllowed)
      visibleInsertedRows = rowsToInsert.length;
    else {
      visibleInsertedRows = 0;
      for (i = 0, count = rowsToInsert.length; i < count; i++) {
        var insertedRow = rowsToInsert[i];
        insertedRow._visible = (O$.getElementStyleProperty(row, "display") != "none");
        if (insertedRow._visible)
          visibleInsertedRows++;
      }
    }

    var addedRowCount = rowsToInsert.length;
    var newRowIndex;
    for (var originalRowIndex = bodyRows.length - 1; originalRowIndex >= afterIndex && originalRowIndex >= 0; originalRowIndex--) {
      newRowIndex = originalRowIndex + addedRowCount;
      var moveRow = originalRowIndex != afterIndex;
      var movedRow = bodyRows[originalRowIndex];

      if (moveRow) {
        bodyRows[newRowIndex] = movedRow;
        bodyRows[originalRowIndex] = null;
        movedRow._index = newRowIndex;

        this._rowStylesMap[newRowIndex] = this._rowStylesMap[originalRowIndex];
        this._rowStylesMap[originalRowIndex] = null;
      }

      var colIndex, colCount, column, bodyCells;
      for (colIndex = 0, colCount = columns.length; colIndex < colCount; colIndex++) {
        column = columns[colIndex];
        bodyCells = column.body._cells;
        var movedCell = bodyCells[originalRowIndex];

        if (moveRow) {
          bodyCells[newRowIndex] = movedCell;
          bodyCells[originalRowIndex] = null;
          var originalCellKey = originalRowIndex + "x" + colIndex;
          var newCellKey = newRowIndex + "x" + colIndex;
          this._cellStylesMap[newCellKey] = this._cellStylesMap[originalCellKey];
          this._cellStylesMap[originalCellKey] = null;
        }
      }

      movedRow._notifyRowMoved(moveRow ? movedRow._visibleRowIndex + visibleInsertedRows : movedRow._visibleRowIndex);
    }

    var newVisibleRowsForNow = 0;
    var callbackIndex, callbackCount;
    for (i = 0, count = rowsToInsert.length; i < count; i++) {
      var newRow = rowsToInsert[i];
      if (!this.scrolling) {
        if (bodyRows.length > 0) {
          var nextRowIdx = afterIndex + 1 + addedRowCount;
          if (nextRowIdx < bodyRows.length)
            this.body._tag.insertBefore(newRow, bodyRows[nextRowIdx]);
          else
            this.body._tag.appendChild(newRow);
        }
        else
          this.body._tag.appendChild(newRow);
      } else {
        this.header._leftScrollingArea._table
      }

      newRowIndex = afterIndex + 1 + i;
      bodyRows[newRowIndex] = newRow;
      this._rowStylesMap[newRowIndex] = newRowsToStylesMap ? newRowsToStylesMap[i] : undefined;
      newRow._cells = O$.Tables._getRowCells(newRow); // todo: adjust for scrollable version
      O$.Tables._initBodyRow(newRow, this, newRowIndex, visibleRowsUpToReferenceRow + newVisibleRowsForNow);
      if (newRow._isVisible())
        newVisibleRowsForNow++;

      for (colIndex = 0, colCount = columns.length; colIndex < colCount; colIndex++) {
        column = columns[colIndex];
        var cellStyleKey = newRowIndex + "x" + colIndex;
        this._cellStylesMap[cellStyleKey] = newRowCellsToStylesMap ? newRowCellsToStylesMap[i + "x" + colIndex] : undefined;
        var cell = newRow._getCellByColIndex(colIndex);

        bodyCells = column.body._cells;
        bodyCells[newRowIndex] = cell;
        if (cell)
          for (callbackIndex = 0, callbackCount = this._cellInsertionCallbacks.length; callbackIndex < callbackCount; callbackIndex++)
            this._cellInsertionCallbacks[callbackIndex](cell, newRow, column);
      }

      for (callbackIndex = 0, callbackCount = this._singleRowInsertionCallbacks.length; callbackIndex < callbackCount; i++)
        this._singleRowInsertionCallbacks[callbackIndex](newRow);
    }

    for (i = 0, count = this._rowInsertionCallbacks.length; i < count; i++) {
      this._rowInsertionCallbacks[i](this, afterIndex, rowsToInsert);
    }

  },


  // -------------------------- TABLE ROWS SUPPORT

  _initRows: function(table) {
    function initTableSection(table, sectionTagName, commonRowAbove) {
      var section = {
        _getRows: function() {
          if (!this._rows) {
            var section = O$.getChildNodesWithNames(table, [sectionTagName])[0];
            if (section) {
              if (!table._scrolling) {
                this._rows = O$.getChildNodesWithNames(section, ["tr"]);
                for (var i = 0, count = this._rows.length; i < count; i++) {
                  var r = this._rows[i];
                  r._table = table;
                  r._index = i;
                  r._cells = O$.Tables._getRowCells(r);
                }
              } else {
                var immediateRows = O$.getChildNodesWithNames(section, ["tr"]);
                var commonRow = commonRowAbove != undefined ? (
                        commonRowAbove ? immediateRows[0] : immediateRows[immediateRows.length - 1]
                        ) : null;
                if (commonRow)
                  commonRow._cells = O$.Tables._getRowCells(commonRow);
                var containerRowIndex = commonRowAbove != undefined
                        ? (commonRowAbove ? 1 : 0)
                        : 0;
                var containerRow = immediateRows[containerRowIndex];
                var leftCellIndex = table._scrolling.leftFixedCols ? 0 : undefined;
                var centerCellIndex = leftCellIndex != undefined ? 1 : 0;
                var rightCellIndex = table._scrolling.rightFixedCols ? centerCellIndex + 1 : undefined;
                var section = this;
                function scrollingAreaRows(areaCellIndex, scrollAreaVar) {
                  var td = containerRow.childNodes[areaCellIndex];
                  var div = O$.getChildNodesWithNames(td, ["div"])[0];
                  var table = div ? O$.getChildNodesWithNames(div, ["table"])[0] : O$.getChildNodesWithNames(td, ["table"])[0];
                  section[scrollAreaVar] = div ? div : table;
                  section[scrollAreaVar]._div = div;
                  section[scrollAreaVar]._table = table;
                  return  O$.getChildNodesWithNames(table, ["tr"]);
                }
                var leftRows = leftCellIndex != undefined ? scrollingAreaRows(leftCellIndex, "_leftScrollingArea") : null;
                var centerRows = scrollingAreaRows(centerCellIndex, "_centerScrollingArea");
                var rightRows = rightCellIndex != undefined ? scrollingAreaRows(rightCellIndex, "_rightScrollingArea") : null;
                var rows = [];
                var rowIndex = 0, rowCount = centerRows.length;
                if (commonRowAbove === true)
                  rows[rowIndex++] = commonRow;
                while (rowIndex < rowCount) {
                  var row = {};
                  rows[rowIndex++] = row;
                  row._leftRowNode = leftRows ? leftRows[rowIndex] : null;
                  row._rowNode = centerRows[rowIndex];
                  row._rightRowNode = rightRows ? rightRows[rowIndex] : null;
                  row._table = table;
                  row._index = rowIndex;
                  var cells = [];

                  if (row._leftRowNode) {
                    row._leftRowNode._cells = O$.Tables._getRowCells(row._leftRowNode);
                    row._leftRowNode._row = row;
                    O$.addAll(cells, row._leftRowNode._cells);
                  }
                  row._rowNode._getRowCells = O$.Tables._getRowCells(row._rowNode);
                  row._rowNode._row = row;
                  O$.addAll(cells, row._rowNode._cells);
                  if (row._rightRowNode) {
                    row._rightRowNode._cells = O$.Tables._getRowCells(row._rightRowNode);
                    row._rightRowNode._row = row;
                    O$.addAll(cells, row._rightRowNode._cells);
                  }
                  row._cells = cells;
                }
                if (commonRowAbove === false)
                  rows[rowIndex] = commonRow;
              }
            } else {
              this._rows = [];
            }
          }
          return this._rows;
        },
        _createRow: function() {
          function createRow(colCount) {
            if (colCount == null) return null;
            var newRow = document.createElement("tr");
            for (var i = 0; i < colCount; i++) {
              var newCell = document.createElement("td");
              newRow.appendChild(newCell);
            }
            return newRow;
          }
          if (!table.scrolling)
            return createRow(table._columns.length);
          else {
            return {
              _leftRowNode: createRow(table.scrolling.leftFixedCols),
              _rowNode: createRow(table._columns.length - table.scrolling.leftFixedCols - table.scrolling.rightFixedCols),
              _rightRowNode: createRow(table.scrolling.rightFixedCols)
            };
          }
        },
        _newRow: function(afterRow) {
          var row = this._createRow();
          this._addRow(row, afterRow);
          return row;
        },
        _addRow: function(row, afterRow) {
          this._addRows([row], afterRow);
        },
        _addRows: function(rows, afterRow) {
          table._insertRowsAfter(afterRow != undefined ? afterRow : table.body._getRows().length - 1, rows);
        }
      };
      section._tag = O$.getChildNodesWithNames(table, [sectionTagName])[0];
      return section;
    };

    table.header = initTableSection(table, "thead", table._commonHeaderExists ? true : undefined);
    table.footer = initTableSection(table, "tfoot", table._commonFooterExists ? false : undefined);
    table.body = initTableSection(table, "tbody", undefined);

    var headRows = table.header._getRows();
    var rowIndex, rowCount, row;
    for (rowIndex = 0, rowCount = headRows.length; rowIndex < rowCount; rowIndex++) {
      row = headRows[rowIndex];
      O$.Tables._initRow(row);
    }
    var commonHeaderRowIndex = table._commonHeaderExists ? 0 : -1;
    var subHeaderRowIndex = table._subHeaderRowExists ? headRows.length - 1 : -1;
    table._subHeaderRowIndex = subHeaderRowIndex;
    table._columnHeadersRowIndexRange = [commonHeaderRowIndex + 1, table._subHeaderRowExists ? headRows.length - 1 : headRows.length];
    table._commonHeaderRowIndex = commonHeaderRowIndex;

    if (commonHeaderRowIndex != -1)
      O$.Tables._setRowStyle(headRows[commonHeaderRowIndex], {_commonHeaderRowClass: table._commonHeaderRowClass});
    if (subHeaderRowIndex != -1)
      O$.Tables._setRowStyle(headRows[subHeaderRowIndex], {_filterRowClass: table._filterRowClass});
    for (var i = table._columnHeadersRowIndexRange[0], end = table._columnHeadersRowIndexRange[1]; i < end; i++)
      O$.Tables._setRowStyle(headRows[i], {_headerRowClass: table._headerRowClass});

    var visibleRowCount = 0;
    var bodyRows = table.body._getRows();
    for (rowIndex = 0, rowCount = bodyRows.length; rowIndex < rowCount; rowIndex++) {
      row = bodyRows[rowIndex];
      O$.Tables._initBodyRow(row, table, rowIndex, visibleRowCount);
      if (row._isVisible())
        visibleRowCount++;
    }
    var footRows = table.footer._getRows();
    var lastNonCommonFooter = footRows.length - 1;
    if (table._commonFooterExists)
      lastNonCommonFooter--;
    for (rowIndex = 0, rowCount = footRows.length; rowIndex < rowCount; rowIndex++) {
      row = footRows[rowIndex];
      O$.Tables._initRow(row);

      if (rowIndex <= lastNonCommonFooter)
        O$.Tables._setRowStyle(footRows[rowIndex], {_footerRowClass: table._footerRowClass});
    }
    if (table._commonFooterExists)
      O$.Tables._setRowStyle(footRows[footRows.length - 1], {_commonFooterRowClass: table._commonFooterRowClass});
  },

  _setRowStyle: function(row, styleMappings) {
    var table = row._table;
    if (table._forceUsingCellStyles) {
      var cells = row._cells;
      for (var i = 0, count = cells.length; i < count; i++) {
        var cell = cells[i];
        O$.Tables._setCellStyleMappings(cell, styleMappings);
      }
    } else {
      if (row._leftRowNode) {
        O$.setStyleMappings(row._leftRowNode, styleMappings);
        O$.Tables._handleUnsupportedRowStyleProperties(row._leftRowNode);
      }
      O$.setStyleMappings(row._rowNode, styleMappings);
      O$.Tables._handleUnsupportedRowStyleProperties(row._rowNode);
      if (row._rightRowNode) {
        O$.setStyleMappings(row._rightRowNode, styleMappings);
        O$.Tables._handleUnsupportedRowStyleProperties(row._rightRowNode);
      }

    }
  },

  _initBodyRow: function(row, table, rowIndex, visibleRowsBefore) {
    row._table = table;
    row._index = rowIndex;
    row._selected = false;
    if (!row._rowNode) {
      if (!row.nodeName || row.nodeName.toLowerCase() != "tr")
        throw "O$.Tables._initBodyRow: row._rowNode must be initialized for synthetic (scrollable-version) rows";
      row._rowNode = row;
    }
    if (row._visible === undefined)
      row._visible = table._invisibleRowsAllowed ? O$.getElementStyleProperty(row._rowNode, "display") != "none" : true;

    row._mouseIsOver = false;
    if (!table._noDataRows && table._rolloverRowClass) {
      function addEventHandler(row, event, handler) {
        O$.addEventHandlerSimple(row._rowNode, event, handler);
        if (row._leftRowNode)
          O$.addEventHandlerSimple(row._leftRowNode, event, handler);
        if (row._rightRowNode)
          O$.addEventHandlerSimple(row._rightRowNode, event, handler);
      }
      addEventHandler(row, "mouseover", "_mouseOverHandler");
      addEventHandler(row, "mouseout", "_mouseOutHandler");
    }
    function processHiddenRowClass(row) {
      if (!row) return;
      if (row.className == "o_hiddenRow") {
        row.style.display = "none";
        row.className = "";
      }
    }
    processHiddenRowClass(row._leftRowNode);
    processHiddenRowClass(row._rowNode);
    processHiddenRowClass(row._rightRowNode);

    row._isVisible = function () {
      return this._visible;
    };
    row._setVisible = function (visible) {
      if (this._visible == visible)
        return;
      this._visible = visible;
      function procesDisplayStyle(row) {if (row && row.style.display) row.style.display = "";}
      procesDisplayStyle(this._leftRowNode);
      procesDisplayStyle(this._rowNode);
      procesDisplayStyle(this._rightRowNode);

      O$.setStyleMappings(this, {_rowVisibilityStyle: visible ? "" : "o_hiddenRow"});
      O$.Tables._updateCellWrappersStyleForRow(this);
    };

    row._mouseOverHandler = function() {
      if (this._row)
        this._row._mouseIsOver = true;
      else
        this._mouseIsOver = true;
      this._updateStyle();
    };

    row._mouseOutHandler = function() {
      if (this._row)
        this._row._mouseIsOver = false;
      else
        this._mouseIsOver = false;
      this._updateStyle();
    };

    row._cellFromPoint = function(x, y, relativeToNearestContainingBlock, cachedDataContainer) {
      for (var cellIndex = 0, cellCount = row._cells.length; cellIndex < cellCount; cellIndex++) {
        var cell = row._cells[cellIndex];
        var cellRect = O$.getElementBorderRectangle(cell, relativeToNearestContainingBlock, cachedDataContainer);
        if (cellRect.containsPoint(x, y))
          return cell;
      }
      return null;
    };

    O$.Tables._initRow(row);

    function reinitializeStyle(visibleRowsBefore) {
      var rowClass;
      if (!table._noDataRows)
        rowClass = (visibleRowsBefore % 2 == 0)
                ? table._bodyRowClass
                : (table._bodyOddRowClass ? table._bodyOddRowClass : table._bodyRowClass);
      else
        rowClass = null;

      row._initialClass = rowClass;
      row._addedClassName = undefined;
      if (!table._forceUsingCellStyles) {
        var individualRowClass = table._rowStylesMap[row._index];
        O$.Tables._setRowStyle(row, {_initialClass: row._initialClass, _individualRowClass: individualRowClass});
      }
    }

    reinitializeStyle(visibleRowsBefore);

    row._rowMoveCallbacks = [];
    row._addRowMoveCallback = function(callback) {
      row._rowMoveCallbacks.push(callback);
    };
    row._notifyRowMoved = function(visibleRowsBefore) {
      var callbackIndex, callbackCount;
      for (callbackIndex = 0, callbackCount = row._rowMoveCallbacks.length; callbackIndex < callbackCount; callbackIndex++)
        row._rowMoveCallbacks[callbackIndex](visibleRowsBefore);
      for (var i = 0, count = row._cells.length; i < count; i++) {
        var cell = row._cells[i];
        for (callbackIndex = 0, callbackCount = table._cellMoveCallbacks.length; callbackIndex < callbackCount; callbackIndex++)
          table._cellMoveCallbacks[callbackIndex](cell, row, cell._column);
      }
    };

    row._addRowMoveCallback(function(visibleRowsBefore) {
      reinitializeStyle(visibleRowsBefore);
      var columns = table._columns;
      var rowIndex = row._index;
      for (var colIndex = 0, colCount = columns.length; colIndex < colCount; colIndex++) {
        var column = columns[colIndex];
        var bodyCells = column.body._cells;
        var cell = bodyCells[rowIndex];
        if (cell)
          cell._updateStyle();
      }

    });

    row._updateStyle = function() {
      var rowTable = this._table;
      var addedClassName = O$.combineClassNames([
        this._selected ? rowTable._selectionClass : null,
        this._mouseIsOver ? rowTable._rolloverRowClass : null]);
      if (row._addedClassName == addedClassName)
        return;
      row._addedClassName = addedClassName;
      var opera = O$.isOpera();

      if (!rowTable._forceUsingCellStyles) {
        if (row._leftRowNode) O$.setStyleMappings(row._leftRowNode, {_rolloverAndSelectionClass: addedClassName});
        O$.setStyleMappings(row._rowNode, {_rolloverAndSelectionClass: addedClassName});
        if (row._rightRowNode) O$.setStyleMappings(row._rightRowNode, {_rolloverAndSelectionClass: addedClassName});
        O$.Tables._updateCellWrappersStyleForRow(row);
        if (opera) {
          function resetOperaStyle(row) {
            if (!row) return;
            var oldBackground1 = row.style.background;
            row.style.background = "white";
            row.style.background = "#fefefe";
            row.style.background = oldBackground1;
          }
          resetOperaStyle(row._leftRowNode);
          resetOperaStyle(row._rowNode);
          resetOperaStyle(row._rightRowNode);
        }
      }

      var i, count, col;
      if (rowTable._needUsingCellStyles === undefined) {
        rowTable._needUsingCellStyles = false;
        var columns = rowTable._columns;
        for (i = 0, count = columns.length; i < count; i++) {
          col = columns[i];
          if ((col.body && col.body._getCompoundClassName()) || col._useCellStyles) {
            rowTable._needUsingCellStyles = true;
            break;
          }
        }
      }

      if (rowTable._forceUsingCellStyles || rowTable._needUsingCellStyles) {
        var cells = this._cells;
        for (i = 0, count = cells.length; i < count; i++) {
          var cell = cells[i];
          col = cell._column;
          if (!rowTable._forceUsingCellStyles && !(col.body && col.body._getCompoundClassName()) && !col._useCellStyles)
            continue;
          O$.setStyleMappings(cell, {_rolloverAndSelectionClass: addedClassName});
          O$.Tables._updateCellWrapperStyle(cell);
          if (opera) {
            var oldBackground2 = cell.style.background;
            cell.style.background = "white";
            cell.style.background = "#fefefe";
            cell.style.background = oldBackground2;
          }
        }
      }
    };

  },

  _initRow: function(row) {
    if (!row._cells)
      throw "O$.Tables._initRow: row._cells must alredy be initialized when this method is invoked";
    if (!row._rowNode) {
      if (!row.nodeName || row.nodeName.toLowerCase() != "tr")
        throw "O$.Tables._initRow: row._rowNode must be initialized for synthetic (scrollable-version) rows";
      row._rowNode = row;
    }
    var cells = row._cells;
    row._cellsByColumns = [];
    var colIndex = 0;
    for (var cellIndex = 0, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
      var cell = cells[cellIndex];
      cell._row = row;
      var cellColSpan = O$.Tables._getCellColSpan(cell);
      cell._colSpan = cellColSpan;
      cell._colIndex = colIndex;
      row._cellsByColumns[colIndex] = cell;
      colIndex += cellColSpan;
      if (cell.innerHTML == "")
        cell.innerHTML = "&#160;";
    }
    row._getCellByColIndex = function(index) {
      var cell = this._cellsByColumns[index];
      return cell ? cell : null;
    };
    row._removeColumn = function(column) {
      var colIndex = column._colIndex;
      var cellsByColumns = row._cellsByColumns;
      var cell = cellsByColumns[colIndex];
      while (!cell && colIndex >= 0) {
        cell = cellsByColumns[--colIndex];
      }
      if (!cell)
        throw "row._removeColumn: Can't find cell to remove: column._colIndex = " + column._colIndex;
      cell.parentNode.removeChild(cell);
      row._cells = O$.Tables._getRowCells(row);
      if (cell._column != column) {
        if (!(cell._colSpan > 1))
          throw "row._removeColumn: colSpan greater than 1 expected, but was: " + cell._colSpan;
        cell._colSpan--;
        cell.colSpan = cell._colSpan;
      }
      for (var i = colIndex + 1, count = cellsByColumns.length; i < count; i++) {
        var shiftedCell = cellsByColumns[i];
        cellsByColumns[i - 1] = shiftedCell;
        shiftedCell._colIndex--;
      }
      cellsByColumns[cellsByColumns.length - 1] = undefined;
      cell._row = null;
    };
  },

  _updateCellWrappersStyleForRow: function(row) {
    function f(row) {
      if (!row || row._noCellWrappersFound)
        return;
      var atLeastOneCellWrapperFound = false;
      var cells = row._cells;
      for (var i = 0, count = cells.length; i < count; i++) {
        var cell = cells[i];
        if (O$.Tables._updateCellWrapperStyle(cell))
          atLeastOneCellWrapperFound = true;
      }
      if (!atLeastOneCellWrapperFound)
        row._noCellWrappersFound = true;
    }
    f(row._rowNode);
    f(row._leftRowNode);
    f(row._rightRowNode);
  },

  _updateCellWrapperStyle: function(cell) {
    var col = cell._column;
    if (col && !col.hasCellWrappers)
      return;

    if (cell._noCellWrapperFound)
      return false;
    var cellWrapper = cell._cellWrapper;
    if (!cellWrapper) {
      var nodes = O$.findChildNodesByClass(cell, "o_cellWrapper", true);
      if (nodes.length == 0) {
        cell._noCellWrapperFound = true;
        return false;
      }
      O$.assert(nodes.length == 1, "O$.Tables._updateCellWrapperStyle: no more than one cellwrapper expected, but found: " + nodes.length);
      cellWrapper = nodes[0];
      O$.assert(cellWrapper, "O$.Tables._updateCellWrapperStyle: non-null cellWrapper expected");
      cell._cellWrapper = cellWrapper;
    }
    var newWrapperClass = O$.combineClassNames([cell.className, cell._row.className, cell._row._table._additionalCellWrapperStyle]);
    if (cellWrapper.className != newWrapperClass)
      cellWrapper.className = newWrapperClass;
    return true;
  },

  // -------------------------- TABLE GRID-LINES SUPPORT


  _initGridLines: function(table) {

    function getSeparatorsForGroup(parentGroup) {
      var parentLevel = (parentGroup == table) ? 0 : parentGroup._hierarchyLevel;
      var separatingColumnsAtLevel = parentLevel + 1;
      var index = table._deepestColumnHierarchyLevel - separatingColumnsAtLevel;
      var horizSeparationLevel = separatingColumnsAtLevel + 1;
      var horizSeparationIndex = table._deepestColumnHierarchyLevel - horizSeparationLevel;
      var result = {};

      var verticalGridLines = table._verticalGridLines ? table._verticalGridLines.split(",") : [];
      var verticalGridLines_length = verticalGridLines.length;
      result.body = verticalGridLines_length ? verticalGridLines[index >= verticalGridLines_length ? verticalGridLines_length - 1 : index] : null;

      var headerVerticalGridLines = table._headerVertSeparator ? table._headerVertSeparator.split(",") : [];
      var headerVerticalGridLines_length = headerVerticalGridLines.length;
      result.header = headerVerticalGridLines_length ? headerVerticalGridLines[index >= headerVerticalGridLines_length ? headerVerticalGridLines_length - 1 : index] : null;

      var footerVerticalGridLines = table._footerVertSeparator ? table._footerVertSeparator.split(",") : [];
      var footerVerticalGridLines_length = footerVerticalGridLines.length;
      result.footer = footerVerticalGridLines_length ? footerVerticalGridLines[index >= footerVerticalGridLines_length ? footerVerticalGridLines_length - 1 : index] : null;

      var multiHeaderSeparators = table._multiHeaderSeparator ? table._multiHeaderSeparator.split(",") : [];
      var multiHeaderSeparators_length = multiHeaderSeparators.length;
      result.multiHeaderSeparator = multiHeaderSeparators_length
              ? multiHeaderSeparators[horizSeparationIndex >= multiHeaderSeparators_length ? multiHeaderSeparators_length - 1 : horizSeparationIndex]
              : null;

      var multiFooterSeparators = table._multiFooterSeparator ? table._multiFooterSeparator.split(",") : [];
      var multiFooterSeparators_length = multiFooterSeparators.length;
      result.multiFooterSeparator = multiFooterSeparators_length
              ? multiFooterSeparators[horizSeparationIndex >= multiFooterSeparators_length ? multiFooterSeparators_length - 1 : horizSeparationIndex]
              : null;

      return result;
    }

    function isEmptyLineStyle(lineStyle) {
      return !lineStyle || lineStyle.indexOf("none") != -1 || lineStyle.indexOf("0px") != -1 || lineStyle.indexOf("0 ") != -1;
    }

    var tableHeader = table.header;
    var headRows = tableHeader._getRows();
    if (headRows.length > 0) {
      tableHeader._updateCommonHeaderSeparator = function() {
        if (!table._commonHeaderExists)
          return;
        var commonHeaderRow = headRows[0];
        var commonHeaderCell = commonHeaderRow._cells[0];
        O$.Tables._setCellStyleProperty(commonHeaderCell, "borderBottom", table._commonHeaderSeparator);
      };

      function getLastRowCells(lastBeforeFilter) {
        var lastRowCells;
        if (table._subHeaderRowExists && !lastBeforeFilter)
          lastRowCells = headRows[headRows.length - 1]._cells;
        else {
          lastRowCells = [];
          // This algorithm searches for the cells adjacent with the header section's bottom edge. It is simplified based
          // on the fact that all the vertically-spanned cells rendered on the server-side always extend to the bottom.
          for (var i = 0, count = headRows.length - (table._subHeaderRowExists ? 1 : 0); i < count; i++) {
            var row = headRows[i];
            var cells = row._cells;
            for (var j = 0, jcount = cells.length; j < jcount; j++) {
              var cell = row._cells[j];
              if (cell.rowSpan > 1 || i == count - 1)
                lastRowCells.push(cell);
            }
          }
        }
        return lastRowCells;
      }

      tableHeader._updateSeparatorStyle = function() {
        var lastRowCells = getLastRowCells(false);
        for (var i = 0, count = lastRowCells.length; i < count; i++) {
          var cell = lastRowCells[i];
          O$.Tables._setCellStyleProperty(cell, "borderBottom", table._headerHorizSeparator != null ? table._headerHorizSeparator : table._horizontalGridLines);
        }

      };
      tableHeader._updateFilterRowSeparatorStyle = function() {
        if (!table._subHeaderRowExists)
          return;
        // it is essential to assign bottom border of a row above the sub-header rather than top border of the sub-header
        // row itself because otherwise IE will make a one-pixel space between vertical separators and this horizontal line
        var lastRowCells = getLastRowCells(true);
        for (var i = 0, count = lastRowCells.length; i < count; i++) {
          var cell = lastRowCells[i];
          O$.Tables._setCellStyleProperty(cell, "borderBottom", table._filterRowSeparator);
        }
      };

      tableHeader._updateColumnSeparatorStyles = function() {
        var subHeaderRow = table._subHeaderRowExists ? headRows[table._subHeaderRowIndex] : null;
        var subHeaderRowCells = subHeaderRow ? subHeaderRow._cells : null;

        function setHeaderVerticalGridlines(parentGroup) {
          var separators = getSeparatorsForGroup(parentGroup);
          var verticalSeparator = !isEmptyLineStyle(separators.header) ? separators.header : separators.body;
          var columns = parentGroup == table ? table._columnHierarchy : parentGroup.subColumns;
          for (var i = 0, count = columns.length; i < count; i++) {
            var col = columns[i];
            if (i < count - 1) {
              function setRightBorderForThisGroup(col) {
                var cell = col.header ? col.header._cell : null;
                if (cell) {
                  O$.Tables._setCellRightBorder(cell, verticalSeparator);
                  if (!col.subColumns && subHeaderRowCells)
                    O$.Tables._setCellRightBorder(subHeaderRowCells[col._colIndex], verticalSeparator);
                }
                if (col.subColumns)
                  setRightBorderForThisGroup(col.subColumns[col.subColumns.length - 1]);
              }

              setRightBorderForThisGroup(col);
            }
            if (col.subColumns) {
              var cell = col.header ? col.header._cell : null;
              if (cell)
                O$.Tables._setCellStyleProperty(cell, "borderBottom", separators.multiHeaderSeparator);
              setHeaderVerticalGridlines(col);
            }
          }
        }

        setHeaderVerticalGridlines(table);
      };

      tableHeader._updateCommonHeaderSeparator();
      tableHeader._updateFilterRowSeparatorStyle();
      tableHeader._updateColumnSeparatorStyles();
      tableHeader._updateSeparatorStyle();
    }

    var tableFooter = table.footer;
    var footRows = tableFooter._getRows();
    if (footRows.length > 0) {
      tableFooter._updateSeparatorStyle = function() {
        if (footRows.length == 0)
          return;
        var row = footRows[0];
        var rowCells = row._cells;
        for (var i = 0, count = rowCells.length; i < count; i++) {
          var cell = rowCells[i];
          O$.Tables._setCellStyleProperty(cell, "borderTop", table._footerHorizSeparator != null ? table._footerHorizSeparator : table._horizontalGridLines);
        }
      };

      tableFooter._updateCommonFooterSeparator = function() {
        if (!table._commonFooterExists)
          return;
        var footerRows = table.footer._getRows();
        var commonFooterRow = footerRows[footerRows.length - 1];
        var commonFooterCell = commonFooterRow._cells[0];
        O$.Tables._setCellStyleProperty(commonFooterCell, "borderTop", table._commonFooterSeparator);
      };

      tableFooter._updateColumnSeparatorStyles = function() {
        function setFooterVerticalGridlines(parentGroup) {
          var separators = getSeparatorsForGroup(parentGroup);
          var verticalSeparator = !isEmptyLineStyle(separators.footer) ? separators.footer : separators.body;
          var columns = parentGroup == table ? table._columnHierarchy : parentGroup.subColumns;
          for (var i = 0, count = columns.length; i < count; i++) {
            var col = columns[i];
            if (i < count - 1) {
              function setRightBorderForThisGroup(col) {
                var cell = col.footer ? col.footer._cell : null;
                if (cell) O$.Tables._setCellRightBorder(cell, verticalSeparator);
                if (col.subColumns)
                  setRightBorderForThisGroup(col.subColumns[col.subColumns.length - 1]);
              }

              setRightBorderForThisGroup(col);
            }
            if (col.subColumns) {
              var cell = col.footer ? col.footer._cell : null;
              if (cell) {
                O$.Tables._setCellStyleProperty(cell, "borderTop", separators.multiFooterSeparator);
              }
              setFooterVerticalGridlines(col);
            }
          }
        }

        setFooterVerticalGridlines(table);
      };

      tableFooter._updateCommonFooterSeparator();
      tableFooter._updateColumnSeparatorStyles();
      tableFooter._updateSeparatorStyle();
    }

    table.body._overrideVerticalGridline = function(afterColIndex, gridlineStyle) {
      if (!table.body._overriddenVerticalGridlines)
        table.body._overriddenVerticalGridlines = [];
      table.body._overriddenVerticalGridlines[afterColIndex] = gridlineStyle;
      table.body._updateVerticalGridlines();
    };
    var tableBody = table.body;
    {
      tableBody._getBorderBottomForCell = function(rowIndex, colIndex, cell) {
        return table._horizontalGridLines;
      };
      function updateBodyCellBorders(cell, rowIndex, column, rowCount, colCount) {
        var correctedRowIndex = rowIndex + cell.rowSpan - 1;
        var borderBottom = (correctedRowIndex < rowCount - 1)
                ? tableBody._getBorderBottomForCell(rowIndex, column._colIndex, cell)
                : "0px none white";
        O$.Tables._setCellStyleProperty(cell, "borderBottom", borderBottom);
        if (column._colIndex != colCount - 1) {
          var rightBorder = column.body._rightBorder;
          if (table.body._overriddenVerticalGridlines) {
            var overriddenGridlineStyle = table.body._overriddenVerticalGridlines[column._colIndex];
            if (overriddenGridlineStyle)
              rightBorder = overriddenGridlineStyle;
          }

          O$.Tables._setCellRightBorder(cell, rightBorder);
        }
      }

      tableBody._updateVerticalGridlines = function() {
        function setBodyVerticalGridlines(parentGroup) {
          var separators = getSeparatorsForGroup(parentGroup);
          var gridLine = separators.body;
          var columns = parentGroup == table ? table._columnHierarchy : parentGroup.subColumns;
          for (var i = 0, count = columns.length; i < count; i++) {
            var col = columns[i];
            if (i < count - 1) {
              var rightBorderCol = col;
              while (rightBorderCol.subColumns)
                rightBorderCol = rightBorderCol.subColumns[rightBorderCol.subColumns.length - 1];
              rightBorderCol.body._rightBorder = gridLine;
            }
            if (col.subColumns)
              setBodyVerticalGridlines(col);
          }
        }

        setBodyVerticalGridlines(table);

        var columns = table._columns;
        for (var i = 0, colCount = columns.length; i < colCount; i++) {
          var column = columns[i];
          var bodyCells = column.body ? column.body._cells : [];

          for (var bodyCellIndex = 0, cellCount = bodyCells.length; bodyCellIndex < cellCount; bodyCellIndex++) {
            var cell = bodyCells[bodyCellIndex];
            if (!cell)
              continue;
            updateBodyCellBorders(cell, bodyCellIndex, column, cellCount, colCount);
          }
        }
      };

      table._addCellInsertionCallback(function(cell, row, column) {
        updateBodyCellBorders(cell, row._index, column, table.body._getRows().length, table._columns.length);
      });
      table._addCellMoveCallback(function(cell, row, column) {
        updateBodyCellBorders(cell, row._index, column, table.body._getRows().length, table._columns.length);
      });

      tableBody._updateVerticalGridlines();
    }

  },

  // -------------------------- TABLE COLUMNS SUPPORT

  _initColumns: function(table, columnHiearachy) {
    table._columnHierarchy = columnHiearachy;
    var deepestColumnHierarchyLevel = 0;
    var realColumns = function processColumns(parentColumn, oneLevelColumns, hierarchyLevel) {
      if (hierarchyLevel > deepestColumnHierarchyLevel)
        deepestColumnHierarchyLevel = hierarchyLevel;
      var collectedColumns = [];
      for (var i = 0, count = oneLevelColumns.length; i < count; i++) {
        var col = oneLevelColumns[i];
        col._hierarchyLevel = hierarchyLevel;
        O$.Tables._initColumnOrGroup(col, table);
        col._parentColumn = parentColumn;
        if (col.subColumns) {
          var collectedSubcolumns = processColumns(col, col.subColumns, hierarchyLevel + 1);
          collectedColumns = collectedColumns.concat(collectedSubcolumns);
        } else {
          collectedColumns.push(col);
        }
      }
      return collectedColumns;
    }(null, table._columnHierarchy, 1);
    table._deepestColumnHierarchyLevel = deepestColumnHierarchyLevel;

    table._columns = realColumns;
    var colCount = realColumns.length;

    var colTags = function() {
      var result;
      if (!table.scorlling) {
        result = O$.getChildNodesWithNames(table, ["col"]);
        if (result.length == 0) {
          var colGroup = O$.getChildNodesWithNames(table, ["colgroup"])[0];
          result = colGroup ? O$.getChildNodesWithNames(colGroup, ["col"]) : [];
        }
        for (var i = 0, count = result.length; i < count; i++)
          result[i] = [result[i]];
      } else {
        result = [];
        function addSectionCols(section) {
          var colIndex = 0;
          function saveAreaCols(area) {
            if (!area) return;
            var cols = O$.getChildNodesWithNames(area._table, ["col"]);
            for (var i = 0, count = cols.length; i < count; i++) {
              var arr = result[colIndex++];
              if (!arr) {arr = []; result[i] = arr;}
              arr.push(cols[i]);
            }
          }
          saveAreaCols(section._leftScrollingArea);
          saveAreaCols(section._centerScrollingArea);
          saveAreaCols(section._rightScrollingArea);
        }
        addSectionCols(table.header);
        addSectionCols(table.body);
        addSectionCols(table.footer);
      }
      return result;
    }();
    O$.assert(colTags.length == colCount, "O$.Tables._initColumns: colTags.length(" + colTags.length + ") != colCount(" + colCount + ")");

    for (var colIndex = 0; colIndex < colCount; colIndex++) {
      var column = realColumns[colIndex];
      var colTagArray = colTags[colIndex];
      O$.Tables._initColumn(column, colTagArray, colIndex);
    }

    table._addCellInsertionCallback(function(cell, row, column) {
      O$.Tables._initBodyCell(cell, column);
      cell._updateStyle();
    });
  },

  _initColumnOrGroup: function(column, table) {
    column._table = table;

    var headRows = table.header ? table.header._getRows() : [];
    var row, cell;
    if (column.header && column.header.pos) {
      var columnHeaderPos = column.header.pos;
      row = headRows[columnHeaderPos.row];
      cell = column.header._cell = row._cells[columnHeaderPos.cell];
      O$.Tables._initHeaderCell(cell, column);
    }

    if (column.subHeader && column.subHeader.pos) {
      var columnFilterPos = column.subHeader.pos;
      row = headRows[columnFilterPos.row];
      cell = column.subHeader._cell = row._cells[columnFilterPos.cell];
      O$.Tables._initSubHeaderCell(cell, column);
    }

    var footRows = table.footer ? table.footer._getRows() : [];
    if (column.footer && column.footer.pos) {
      var columnFooterPos = column.footer.pos;
      row = footRows[columnFooterPos.row];
      cell = column.footer._cell = row._cells[columnFooterPos.cell];
      O$.Tables._initFooterCell(cell, column);
    }

    column._getCompoundClassName = function() {
      var className = column.className;
      if (column._parentColumn)
        className = O$.combineClassNames([className, column._parentColumn._getCompoundClassName()]);
      return className;
    };

    if (!column.body)
      column.body = {};
    column.body._getCompoundClassName = function() {
      var className = column.body.className;
      if (column._parentColumn)
        className = O$.combineClassNames([className, column._parentColumn.body._getCompoundClassName()]);
      return className;
    };

    column._getCompoundEventContainers = function() {
      var result = [];
      for (var c = column; c; c = c._parentColumn)
        result.push(c);
      return result;
    };

    column.body._getCompoundEventContainers = function() {
      var result = [];
      for (var c = column; c; c = c._parentColumn)
        result.push(c.body);
      return result;
    };

    column._updateStyle = function() {
      var headerCell = column.header ? column.header._cell : null;
      if (headerCell)
        headerCell._updateStyle();

      var subHeaderCell = column.subHeader ? column.subHeader._cell : null;
      if (subHeaderCell)
        subHeaderCell._updateStyle();

      var footerCell = column.footer ? column.footer._cell : null;
      if (footerCell)
        footerCell._updateStyle();
    };
  },

  _initColumn: function(column, colTagArray, colIndex) {
    column._colTags = colTagArray;
    column._colIndex = colIndex;

    var table = column._table;

    var bodyRows = table.body._getRows();
    var bodyRowCount = bodyRows.length;
    column.body._cells = [];

    for (var i = 0; i < bodyRowCount; i++) {
      var row = bodyRows[i];
      var cell = row._getCellByColIndex(colIndex);
      if (cell) {
        O$.Tables._initBodyCell(cell, column);
      }
      column.body._cells[i] = cell;
    }

    if (column._super_updateStyle)
      throw "O$.Tables._initColumn can be called only once per column";
    column._super_updateStyle = column._updateStyle;
    column._updateStyle = function() {
      column._super_updateStyle();

      column._useCellStyles = O$.Tables._getUseCellStylesForColumn(column);
      var colTagClassName = !column._useCellStyles ? column._getCompoundClassName() : "";
      column._colTags.forEach(function(colTag) {
        colTag.className = colTagClassName ? colTagClassName : "";
      });

      column._cellStyles = O$.Tables._prepareCellStylesForColStyleSimulation(column);

      var bodyCells = column.body ? column.body._cells : [];
      for (var bodyCellIndex = 0, cellCount = bodyCells.length; bodyCellIndex < cellCount; bodyCellIndex++) {
        var cell = bodyCells[bodyCellIndex];
        if (cell)
          cell._updateStyle();
      }

    };

  },

  _initHeaderCell: function(cell, column) {
    cell._column = column;
    cell._colIndex = column._colIndex; // todo: review cell._colIndex usages. Remove cell._colIndex property and use appropriate column's property

    cell._updateStyle = function() {
      O$.Tables._applySimulatedColStylesToCell(cell);

      var column = cell._column;
      O$.Tables._setCellStyleMappings(cell, {_compoundColumnClassName: column._getCompoundClassName(), _colHeaderClass: column.header ? column.header.className : null});
    };

  },

  _initSubHeaderCell: function(cell, column) {
    cell._column = column;
    cell._colIndex = column._colIndex;

    cell._updateStyle = function() {
      O$.Tables._applySimulatedColStylesToCell(cell);

      var column = cell._column;
      O$.Tables._setCellStyleMappings(cell, {
        _compoundColumnClassName: column._getCompoundClassName(),
        _colFilterClass: column.subHeader ? column.subHeader.className : null
      });
    };

  },

  _initFooterCell: function(cell, column) {
    cell._column = column;
    cell._colIndex = column._colIndex;

    cell._updateStyle = function() {
      O$.Tables._applySimulatedColStylesToCell(cell);

      var column = cell._column;
      O$.Tables._setCellStyleMappings(cell, {
        _compoundColumnClassName: column._getCompoundClassName(),
        _colFooterClass: column.footer ? column.footer.className : null
      });
    };
  },

  _initBodyCell: function(cell, column) {
    cell._column = column;

    cell._updateStyle = function() {
      var column = cell._column;
      var row = cell._row;
      var table = row._table;
      var rowIndex = row._index;
      var colIndex = column._colIndex;

      if (!table._noDataRows) {
        O$.Tables._applySimulatedColStylesToCell(cell);
      }

      var cellStyleMappings = {};
      if (table._forceUsingCellStyles || column._useCellStyles) {
        cellStyleMappings._rowInitialClass = row._initialClass;
        cellStyleMappings._rowIndividualClass = table._rowStylesMap[row._index];
      }

      if (table._noDataRows) {
        O$.Tables._assignNoDataCellStyle(cell);
      } else {
        var cellKey = rowIndex + "x" + colIndex;
        cellStyleMappings._individualCellStyle = table._cellStylesMap[cellKey];

        var columnClassName = column._useCellStyles ? column._getCompoundClassName() : null;
        if (columnClassName)
          cellStyleMappings._columnClass = columnClassName;
        var compoundBodyClassName = column.body._getCompoundClassName();
        if (compoundBodyClassName) {
          cellStyleMappings._rowInitialClass = row._initialClass;
          cellStyleMappings._columnBodyClass = compoundBodyClassName;
        }
      }
      O$.setStyleMappings(cell, cellStyleMappings);
      O$.Tables._updateCellWrapperStyle(cell);
    };

  },

  /*
   * Fix for JSFC-2834. First column style should not be applied to column tag in order to avoid applying it to common
   * header and common footer rows that span across all columns.
   */
  _getUseCellStylesForColumn: function(column) {
    var table = column._table;
    if (table._forceUsingCellStyles || column._forceUsingCellStyles)
      return true;
    var thisIsColGroup = column._subColumns;
    if (thisIsColGroup)
      return true;

    var useCellStylesToAvoidApplyingFirstColStyleToCommonHeader =
            column._colIndex == 0 && (table._commonHeaderExists || table._commonFooterExists);

    // cell styles should be used instead of col tag style for first column if there is a no-data message row,
    // otherwise first col style will be applied to the no-data message row, which shouldn't be the case (JSFC-2890)
    var useFirstColumnCellStylesWhenNoData = table._noDataRows && column._colIndex == 0;

    return useCellStylesToAvoidApplyingFirstColStyleToCommonHeader || useFirstColumnCellStylesWhenNoData;
  },

  _assignNoDataCellStyle: function(cell) {
    var column = cell._column;
    var row = cell._row;
    var table = row._table;
    if (!(table._forceUsingCellStyles || column._useCellStyles)) {
      // first column style shouldn't be applied to the no-data-row
      // no-data row should derive unspecified properties from body section instead
      var noDataRowClass = table._rowStylesMap[0];
      var noDataRowStyle = noDataRowClass
              ? O$.Tables._evaluateStyleClassProperties_cached(noDataRowClass, ["backgroundColor", "background"], table)
              : {};
      if (!noDataRowStyle.backgroundColor && !noDataRowStyle.background) {
        var bodySection = O$.getChildNodesWithNames(table, ["tbody"])[0];
        var propertyValue = O$.getElementStyleProperty(bodySection, "background-color");
        if (!propertyValue || propertyValue == "transparent") {
          propertyValue = O$.getElementStyleProperty(table, "background-color");
        }
        if (!propertyValue || propertyValue == "transparent")
          propertyValue = !O$.isSafari() ? "Window" : "white";
        if (propertyValue)
          O$.Tables._setCellStyleProperty(cell, "backgroundColor", propertyValue);
      }
    }

  },

  _initColumnEvents: function(table) {

    function assignBodyCellEvents(cell, compoundBodyEvents, compoundGeneralEvents) {
      O$.Tables._assignColumnCellEvents(cell, compoundBodyEvents);
      O$.Tables._assignColumnCellEvents(cell, compoundGeneralEvents);
    }

    function assignColEvents(oneLevelColumns, additionalGeneralEvents, additionalBodyEvents) {
      for (var i = 0, count = oneLevelColumns.length; i < count; i++) {
        var column = oneLevelColumns[i];
        var compoundGeneralEvents = additionalGeneralEvents ? [column].concat(additionalGeneralEvents) : [column];
        var compoundBodyEvents = additionalBodyEvents ? [column.body].concat(additionalBodyEvents) : [column.body];

        var headerCell = column.header ? column.header._cell : null;
        if (headerCell)
          O$.Tables._assignColumnCellEvents(headerCell, compoundGeneralEvents, column.header);

        var subHeaderCell = column.subHeader ? column.subHeader._cell : null;
        if (subHeaderCell)
          O$.Tables._assignColumnCellEvents(subHeaderCell, compoundGeneralEvents, column.subHeader);

        if (!column.subColumns && column.body && !table._noDataRows) {
          var bodyCells = column.body ? column.body._cells : [];
          for (var bodyCellIndex = 0, cellCount = bodyCells.length; bodyCellIndex < cellCount; bodyCellIndex++) {
            var cell = bodyCells[bodyCellIndex];
            if (cell)
              assignBodyCellEvents(cell, compoundBodyEvents, compoundGeneralEvents);
          }
        }

        var footerCell = column.footer ? column.footer._cell : null;
        if (footerCell)
          O$.Tables._assignColumnCellEvents(footerCell, compoundGeneralEvents, column.footer);

        if (column.subColumns) {
          assignColEvents(column.subColumns, compoundGeneralEvents, compoundBodyEvents);
        }
      }
    }

    assignColEvents(table._columnHierarchy, [], []);

    table._addCellInsertionCallback(function(cell, row, column) {
      assignBodyCellEvents(cell, column.body._getCompoundEventContainers(), column._getCompoundEventContainers());
    });

  },

  _initColumnStyles: function(table) {
    function assignColStyles(oneLevelColumns) {
      for (var i = 0, count = oneLevelColumns.length; i < count; i++) {
        var col = oneLevelColumns[i];
        col._updateStyle();
        if (col.subColumns)
          assignColStyles(col.subColumns);
      }
    }

    assignColStyles(table._columnHierarchy);
  },

  /**
   * Some CSS styles that can be applid to <col> tags don't behave in a cross-browser way. We extract these styles here
   * for their further applying to column cells in order to simulate the proper behavior.
   */
  _prepareCellStylesForColStyleSimulation: function(column) {
    var isExplorer = O$.isExplorer();
    var isMozilla = O$.isMozillaFF();
    var isOpera = O$.isOpera();

    var table = column._table;
    var colTag = column._colTags[0];
    if (isMozilla) {
      column._forceCellAlign = colTag.hasAttribute("align") ? colTag.align : null;
      column._forceCellVAlign = colTag.hasAttribute("valign") ? colTag.vAlign : null;
    }

    var colTagClassName = colTag.className;
    if (column._useCellStyles || !colTagClassName)
      return null;

    var cellStyles = null;
    var colProperties;
    if (isMozilla || isOpera) {
      colProperties = ["width", "textAlign", "color", "fontFamily", "fontSize", "fontWeight", "fontStyle", "borderLeft", "borderRight"];
    } else if (isExplorer) {
      colProperties = ["fontFamily", "fontSize", "borderLeft", "borderRight"];
    } else
      colProperties = [];
    var colStyleProperties = O$.Tables._evaluateStyleClassProperties_cached(colTagClassName, colProperties, table);
    if (isMozilla || isOpera) {
      if (!cellStyles)
        cellStyles = {};
      cellStyles._width = colStyleProperties.width;//O$.getStyleClassProperty(colTag.className, "width");
      cellStyles._textAlign = colStyleProperties.textAlign;//O$.Tables._getUserStylePropertyValue(colTag, "text-align", "start", "left");
      cellStyles._verticalAlign = O$.Tables._getUserStylePropertyValue(colTag, "vertical-align", "baseline", "auto");
      cellStyles._lineHeight = O$.Tables._getUserClassPropertyValue(colTag, "line-height", "normal");

      cellStyles._paddingLeft = O$.Tables._getUserStylePropertyValue(colTag, "padding-left", "0px");
      cellStyles._paddingRight = O$.Tables._getUserStylePropertyValue(colTag, "padding-right", "0px");
      cellStyles._paddingTop = O$.Tables._getUserStylePropertyValue(colTag, "padding-top", "0px");
      cellStyles._paddingBottom = O$.Tables._getUserStylePropertyValue(colTag, "padding-bottom", "0px");

      cellStyles._color = colStyleProperties.color;
      cellStyles._fontWeight = colStyleProperties.fontWeight;
      cellStyles._fontStyle = colStyleProperties.fontStyle;
    }
    if (isMozilla || isExplorer) {
      if (!cellStyles)
        cellStyles = {};
      cellStyles._fontFamily = colStyleProperties.fontFamily;
      cellStyles._fontSize = colStyleProperties.fontSize;
      cellStyles._borderLeft = colStyleProperties.borderLeft;
      cellStyles._borderRight = colStyleProperties.borderRight;
    }
    if (cellStyles && cellStyles._color) {
      // use cell styles for this column to solve color CSS attribute precedence issue in Mozilla (JSFC-2823)
      column._useCellStyles = true;
      if (column.body)
        O$.setStyleMappings(column.body, {_colTagClassName: colTagClassName});
      cellStyles = null;
    } else {
      if (!(cellStyles._textAlign || cellStyles._verticalAlign ||
            cellStyles._paddingLeft || cellStyles._paddingRight || cellStyles._paddingTop ||
            cellStyles._paddingBottom || cellStyles._width || cellStyles._height || cellStyles._color ||
            cellStyles._fontFamily || cellStyles._fontSize || cellStyles._fontWeight || cellStyles._fontStyle ||
            cellStyles._borderLeft || cellStyles._borderRight))
        cellStyles = null;
    }

    return cellStyles;
  },

  _clearCellStyleProperties: function(cell, propertyNames) {
    if (!cell)
      return;

    for (var i = 0, count = propertyNames.length; i < count; i++) {
      var propertyName = propertyNames[i];
      cell.style[propertyName] = null;
    }

  },

  _setCellStyleProperty: function(cell, propertyName, propertyValue) {
    if (!cell || !propertyValue)
      return;

    try {
      cell.style[propertyName] = propertyValue;
    } catch (e) {
      O$.logError("O$.Tables._setCellStyleProperty: couldn't set style property \"" + propertyName + "\" to \"" + propertyValue + "\" ; original error: " + e.message);
      throw e;
    }
  },

  _setCellRightBorder: function(cell, borderValue) {
    if (!cell || !borderValue)
      return;

    try {
      cell.style.borderRight = borderValue;
    } catch (e) {
      O$.logError("O$.Tables._setCellRightBorder: invalid borderValue: \"" + borderValue + "\" ; it must be a valid CSS declaration of form \"1px solid gray\" ; original error: " + e.message);
      throw e;
    }
  },

  _setCellStyleMappings: function(cell, styleMappings) {
    O$.setStyleMappings(cell, styleMappings);
    O$.Tables._updateCellWrapperStyle(cell);
  },

  _assignColumnCellEvents: function(cell, eventContainers, additionalEventContainer) {
    if (additionalEventContainer)
      O$.assignEvents(cell, additionalEventContainer);
    for (var i = 0, count = eventContainers.length; i < count; i++) {
      var eventContainer = eventContainers[i];
      O$.assignEvents(cell, eventContainer);
    }
  },

  _applySimulatedColStylesToCell: function(cell) {
    var column = cell._column;

    if (column._forceCellAlign)
      cell.align = column._forceCellAlign;
    if (column._forceCellVAlign)
      cell.vAlign = column._forceCellVAlign;

    var cellStyles = column._cellStyles;
    if (cellStyles) {
      O$.Tables._setCellStyleProperty(cell, "textAlign", cellStyles._textAlign);
      O$.Tables._setCellStyleProperty(cell, "verticalAlign", cellStyles._verticalAlign);
      O$.Tables._setCellStyleProperty(cell, "width", cellStyles._width);
      O$.Tables._setCellStyleProperty(cell, "lineHeight", cellStyles._lineHeight);

      O$.Tables._setCellStyleProperty(cell, "color", cellStyles._color);
      O$.Tables._setCellStyleProperty(cell, "fontFamily", cellStyles._fontFamily);
      O$.Tables._setCellStyleProperty(cell, "fontSize", cellStyles._fontSize);
      O$.Tables._setCellStyleProperty(cell, "fontWeight", cellStyles._fontWeight);
      O$.Tables._setCellStyleProperty(cell, "fontStyle", cellStyles._fontStyle);

      O$.Tables._setCellStyleProperty(cell, "paddingLeft", cellStyles._paddingLeft);
      O$.Tables._setCellStyleProperty(cell, "paddingRight", cellStyles._paddingRight);
      O$.Tables._setCellStyleProperty(cell, "paddingTop", cellStyles._paddingTop);
      O$.Tables._setCellStyleProperty(cell, "paddingBottom", cellStyles._paddingBottom);
      O$.Tables._setCellStyleProperty(cell, "borderLeft", cellStyles._borderLeft);
      O$.Tables._setCellStyleProperty(cell, "borderRight", cellStyles._borderRight);
      cell._simulatedColStylesApplied = true;
    } else {
      if (cell._simulatedColStylesApplied) {
        O$.Tables._clearCellStyleProperties(cell,
                ["textAlign", "verticalAlign", "width", "lineHeight", "color", "fontFamily", "fontSize", "fontWeight",
                  "fontStyle", "paddingLeft", "paddingRight", "paddingTop", "paddingBottom", "borderLeft", "borderRight"]);
        cell._simulatedColStylesApplied = false;
      }
    }

  },

  _removeTableColumn: function(column) {
    var table = column._table;
    var bodyRows = table.body._getRows();
    for (var i = 0, count = bodyRows.length; i < count; i++) {
      var row = bodyRows[i];
      row._removeColumn(column);
    }
  }

};
