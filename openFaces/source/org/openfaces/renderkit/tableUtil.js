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

// -------------------------- COMMON UTILITIES

O$._getUserStylePropertyValue = function(element, propertyName, defaultValue1, defaultValue2) {
  var value = O$.getElementStyleProperty(element, propertyName);
  if (!value || value == defaultValue1 || value == defaultValue2)
    return null;
  return value;
};

O$._getUserClassPropertyValue = function(element, propertyName, defaultValue1, defaultValue2) {
  var value = O$.getStyleClassProperty(element.className, propertyName);
  if (!value || value == defaultValue1 || value == defaultValue2)
    return null;
  return value;
};

O$._getRowCells = function(row) {
  return row.cells;
};

O$._getCellColSpan = function(cell) {
  var colSpan = cell.colSpan;
  if (!colSpan || colSpan == -1)
    colSpan = 1;
  return colSpan;
};

O$._handleUnsupportedRowStyleProperties = function(row) {
  var cells = row._cells;
  var cellIndex, cellCount, cell;
  if (!O$.isExplorer()) {
    var paddingLeft = O$._getUserStylePropertyValue(row, "padding-left", "0px");
    var paddingRight = O$._getUserStylePropertyValue(row, "padding-right", "0px");
    var paddingTop = O$._getUserStylePropertyValue(row, "padding-top", "0px");
    var paddingBottom = O$._getUserStylePropertyValue(row, "padding-bottom", "0px");
    var lineHeight = O$._getUserClassPropertyValue(row, "line-height", "normal");

    if (paddingLeft || paddingRight || paddingTop || paddingBottom || lineHeight) {
      for (cellIndex = 0, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
        cell = cells[cellIndex];
        O$._setCellStyleProperty(cell, "paddingLeft", paddingLeft);
        O$._setCellStyleProperty(cell, "paddingRight", paddingRight);
        O$._setCellStyleProperty(cell, "paddingTop", paddingTop);
        O$._setCellStyleProperty(cell, "paddingBottom", paddingBottom);
        O$._setCellStyleProperty(cell, "lineHeight", lineHeight);
      }
    }
  }

  var propertyValues = O$._evaluateStyleClassProperties_cached(
          row.className,
          ["borderLeft", "borderRight", "borderTop", "borderBottom"],
          row._table);
  if (propertyValues.borderLeft || propertyValues.borderRight || propertyValues.borderTop || propertyValues.borderBottom) {
    for (cellIndex = 0, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
      cell = cells[cellIndex];
      O$._setCellStyleProperty(cell, "borderLeft", propertyValues.borderLeft);
      O$._setCellStyleProperty(cell, "borderRight", propertyValues.borderRight);
      O$._setCellStyleProperty(cell, "borderTop", propertyValues.borderTop);
      O$._setCellStyleProperty(cell, "borderBottom", propertyValues.borderBottom);
    }
  }
};

O$._evaluateStyleClassProperties_cached = function(className, propertyNames, cacheInElement) {
  if (!cacheInElement.__stylePropertyCache)
    cacheInElement.__stylePropertyCache = new Array();
  var stylePropertiesKey = className + "___" + propertyNames.join("__");
  var propertyValues = cacheInElement.__stylePropertyCache[stylePropertiesKey];
  if (!propertyValues) {
    propertyValues = O$.getStyleClassProperties(className, propertyNames);
    cacheInElement.__stylePropertyCache[stylePropertiesKey] = propertyValues;
  }
  return propertyValues;
};


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
 *   - filter: {
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
O$.Tables = {
  _initStyles: function(scrolling, columns, commonHeaderExists, filterRowExists, commonFooterExists, noDataRows,
                                 gridLines, rowStyles, rowStylesMap, cellStylesMap, forceUsingCellStyles,
                                 additionalCellWrapperStyle, invisibleRowsAllowed) {
    var table = this;
    table._scrolling = scrolling;
    table._commonHeaderExists = commonHeaderExists;
    table._commonFooterExists = commonFooterExists;
    table._filterRowExists = filterRowExists;
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

    table._rowInsertionCallbacks = new Array();
    table._addRowInsertionCallback = function(callback) {
      table._rowInsertionCallbacks.push(callback);
    };
    table._singleRowInsertionCallbacks = new Array();
    table._addSingleRowInsertionCallback = function(callback) {
      table._singleRowInsertionCallbacks.push(callback);
    };
    table._cellInsertionCallbacks = new Array();
    table._addCellInsertionCallback = function(callback) {
      table._cellInsertionCallbacks.push(callback);
    };
    table._cellMoveCallbacks = new Array();
    table._addCellMoveCallback = function(callback) {
      table._cellMoveCallbacks.push(callback);
    };

    O$._initTableRows(table);
    O$._initTableColumns(table, columns);
    O$._initTableColumnEvents(table);
    O$._initTableColumnStyles(table);
    O$._initTableGridLines(table);
    if (filterRowExists)
      O$.fixInputsWidthStrict(table.header._getRows()[table._filterRowIndex]);

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
        var rowRect = O$.getElementBorderRectangle(row, relativeToNearestContainingBlock, cachedDataContainer);
        if (rowRect.containsPoint(x, y))
          return row;
      }
      return null;
    };

  },

  _removeAllRows: function() {
    var bodyRows = this.body._getRows();
    for (var i = 0, count = bodyRows.length; i < count; i++) {
      var row = bodyRows[i];
      row.parentNode.removeChild(row);
    }
    this.body._Rows = undefined;

    this._rowStylesMap = {};
    this._cellStylesMap = {};
  },

  _insertRowsAfter: function(afterIndex, rowsToInsert, newRowsToStylesMap, newRowCellsToStylesMap) {
    if (rowsToInsert.length == 0) {
      return;
    }

    var bodyRows = this.body._getRows();
    var columns = this._columns;
    var afterRow = afterIndex != -1 ? bodyRows[afterIndex] : null;

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

    var nextNode;
    var rowContainer;
    if (afterRow) {
      nextNode = afterRow.nextSibling;
      rowContainer = afterRow.parentNode;
    } else {
      var bodySection = this.body._tag;
      nextNode = bodyRows.length > 0 ? bodyRows[0] : null;
      rowContainer = bodySection;
    }

    var newVisibleRowsForNow = 0;
    var callbackIndex, callbackCount;
    for (i = 0, count = rowsToInsert.length; i < count; i++) {
      var newRow = rowsToInsert[i];
      if (nextNode != null)
        rowContainer.insertBefore(newRow, nextNode);
      else
        rowContainer.appendChild(newRow);

      newRowIndex = afterIndex + 1 + i;
      bodyRows[newRowIndex] = newRow;
      this._rowStylesMap[newRowIndex] = newRowsToStylesMap ? newRowsToStylesMap[i] : undefined;
      O$._initTableRow(newRow, this, newRowIndex, visibleRowsUpToReferenceRow + newVisibleRowsForNow);
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

  }
};

// -------------------------- TABLE ROWS SUPPORT

O$._initTableRows = function(table) {
  function initTableSection(table, sectionTagName, commonRowAbove) {
    var section = {
      _getRows: function() {
        if (!this._rows) {
          var section = O$.getChildNodesWithNames(table, [sectionTagName])[0];
          if (section) {
            if (!table._scrolling) {
              this._rows = O$.getChildNodesWithNames(section, ["tr"]);
            } else {
              
              O$.findElementByPath(section, "tr[0]/table")
            }
          } else {
            this._rows = [];
          }
        }
        return this._rows;
      },
      _createRow: function() {
        var newRow = document.createElement("tr");
        var colCount = table._columns.length;
        for (var i = 0; i < colCount; i++) {
          var newCell = document.createElement("td");
          newRow.appendChild(newCell);
        }
        return newRow;
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
  table.body = initTableSection(table, "tbody");

  var headRows = table.header._getRows();
  var rowIndex, rowCount, row;
  for (rowIndex = 0, rowCount = headRows.length; rowIndex < rowCount; rowIndex++) {
    row = headRows[rowIndex];
    row._table = table;
    row._index = rowIndex;
    O$.Tables._prepareRow(row);
  }
  var commonHeaderRowIndex = table._commonHeaderExists ? 0 : -1;
  var filterRowIndex = table._filterRowExists ? headRows.length - 1 : -1;
  table._filterRowIndex = filterRowIndex;
  table._columnHeadersRowIndexRange = [commonHeaderRowIndex + 1, table._filterRowExists ? headRows.length - 1 : headRows.length];
  table._commonHeaderRowIndex = commonHeaderRowIndex;

  if (commonHeaderRowIndex != -1)
    O$._setRowStyle(headRows[commonHeaderRowIndex], {_commonHeaderRowClass: table._commonHeaderRowClass});
  if (filterRowIndex != -1)
    O$._setRowStyle(headRows[filterRowIndex], {_filterRowClass: table._filterRowClass});
  for (var i = table._columnHeadersRowIndexRange[0], end = table._columnHeadersRowIndexRange[1]; i < end; i++)
    O$._setRowStyle(headRows[i], {_headerRowClass: table._headerRowClass});

  var visibleRowCount = 0;
  var bodyRows = table.body._getRows();
  for (rowIndex = 0, rowCount = bodyRows.length; rowIndex < rowCount; rowIndex++) {
    row = bodyRows[rowIndex];
    O$._initTableRow(row, table, rowIndex, visibleRowCount);
    if (row._isVisible())
      visibleRowCount++;
  }
  var footRows = table.footer._getRows();
  var lastNonCommonFooter = footRows.length - 1;
  if (table._commonFooterExists)
    lastNonCommonFooter--;
  for (rowIndex = 0, rowCount = footRows.length; rowIndex < rowCount; rowIndex++) {
    row = footRows[rowIndex];
    row._table = table;
    row._index = rowIndex;
    O$.Tables._prepareRow(row);

    if (rowIndex <= lastNonCommonFooter)
      O$._setRowStyle(footRows[rowIndex], {_footerRowClass: table._footerRowClass});
  }
  if (table._commonFooterExists)
    O$._setRowStyle(footRows[footRows.length - 1], {_commonFooterRowClass: table._commonFooterRowClass});
};

O$._setRowStyle = function(row, styleMappings) {
  var table = row._table;
  if (table._forceUsingCellStyles) {
    var cells = row._cells;
    for (var i = 0, count = cells.length; i < count; i++) {
      var cell = cells[i];
      O$._setCellStyleMappings(cell, styleMappings);
    }
  } else {
    O$.setStyleMappings(row, styleMappings);
    O$._handleUnsupportedRowStyleProperties(row);
  }
};

O$._initTableRow = function(row, table, rowIndex, visibleRowsBefore) {
  row._table = table;
  row._index = rowIndex;
  row._selected = false;
  if (row._visible === undefined)
    row._visible = table._invisibleRowsAllowed ? O$.getElementStyleProperty(row, "display") != "none" : true;

  row._mouseIsOver = false;
  if (!table._noDataRows && table._rolloverRowClass) {
    O$.addEventHandlerSimple(row, "mouseover", "_mouseOverHandler");
    O$.addEventHandlerSimple(row, "mouseout", "_mouseOutHandler");
  }
  if (row.className == "o_hiddenRow") {
    row.style.display = "none";
    row.className = "";
  }

  row._isVisible = function () {
    return this._visible;
  };
  row._setVisible = function (visible) {
    if (this._visible == visible)
      return;
    this._visible = visible;
    if (this.style.display)
      this.style.display = "";
    O$.setStyleMappings(this, {_rowVisibilityStyle: visible ? "" : "o_hiddenRow"});
    O$._updateCellWrappersStyleForRow(this);
  };

  row._mouseOverHandler = function() {
    this._mouseIsOver = true;
    this._updateStyle();
  };

  row._mouseOutHandler = function() {
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

  O$.Tables._prepareRow(row);

  function reinitializeStyle(visibleRowsBefore) {
    O$._calculateInitialRowClass(row, table, visibleRowsBefore);
    if (!table._forceUsingCellStyles) {
      var individualRowClass = table._rowStylesMap[row._index];
      O$.setStyleMappings(row, {_initialClass: row._initialClass, _individualRowClass: individualRowClass});
      O$._handleUnsupportedRowStyleProperties(row);
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
      O$.setStyleMappings(row, {_rolloverAndSelectionClass: addedClassName});
      O$._updateCellWrappersStyleForRow(row);
      if (opera) {
        var oldBackground1 = row.style.background;
        row.style.background = "white";
        row.style.background = "#fefefe";
        row.style.background = oldBackground1;
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
        O$._updateCellWrapperStyle(cell);
        if (opera) {
          var oldBackground2 = cell.style.background;
          cell.style.background = "white";
          cell.style.background = "#fefefe";
          cell.style.background = oldBackground2;
        }
      }
    }
  };

};

O$.Tables._prepareRow = function(row) {
  var cells = O$._getRowCells(row);
  row._cells = cells;
  row._cellsByColumns = [];
  var colIndex = 0;
  for (var cellIndex = 0, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
    var cell = cells[cellIndex];
    cell._row = row;
    var cellColSpan = O$._getCellColSpan(cell);
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
    row._cells = O$._getRowCells(row);
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
};

O$._updateCellWrappersStyleForRow = function(row) {
  if (row._noCellWrappersFound)
    return;
  var atLeastOneCellWrapperFound = false;
  var cells = row._cells;
  for (var i = 0, count = cells.length; i < count; i++) {
    var cell = cells[i];
    if (O$._updateCellWrapperStyle(cell))
      atLeastOneCellWrapperFound = true;
  }
  if (!atLeastOneCellWrapperFound)
    row._noCellWrappersFound = true;
};

O$._updateCellWrapperStyle = function(cell) {
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
    O$.assert(nodes.length == 1, "O$._updateCellWrapperStyle: no more than one cellwrapper expected, but found: " + nodes.length);
    cellWrapper = nodes[0];
    O$.assert(cellWrapper, "O$._updateCellWrapperStyle: non-null cellWrapper expected");
    cell._cellWrapper = cellWrapper;
  }
  var newWrapperClass = O$.combineClassNames([cell.className, cell._row.className, cell._row._table._additionalCellWrapperStyle]);
  if (cellWrapper.className != newWrapperClass)
    cellWrapper.className = newWrapperClass;
  return true;
};


O$._calculateInitialRowClass = function(row, table, visibleRowIndex) {
  var rowClass;
  if (!table._noDataRows)
    rowClass = (visibleRowIndex % 2 == 0)
            ? table._bodyRowClass
            : (table._bodyOddRowClass ? table._bodyOddRowClass : table._bodyRowClass);
  else
    rowClass = null;

  row._initialClass = rowClass;
  row._addedClassName = undefined;
};


// -------------------------- TABLE GRID-LINES SUPPORT


O$._initTableGridLines = function(table) {

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
      O$._setCellStyleProperty(commonHeaderCell, "borderBottom", table._commonHeaderSeparator);
    };

    function getLastRowCells(lastBeforeFilter) {
      var lastRowCells;
      if (table._filterRowExists && !lastBeforeFilter)
        lastRowCells = headRows[headRows.length - 1]._cells;
      else {
        lastRowCells = [];
        // This algorithm searches for the cells adjacent with the header section's bottom edge. It is simplified based
        // on the fact that all the vertically-spanned cells rendered on the server-side always extend to the bottom.
        for (var i = 0, count = headRows.length - (table._filterRowExists ? 1 : 0); i < count; i++) {
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
        O$._setCellStyleProperty(cell, "borderBottom", table._headerHorizSeparator != null ? table._headerHorizSeparator : table._horizontalGridLines);
      }

    };
    tableHeader._updateFilterRowSeparatorStyle = function() {
      if (!table._filterRowExists)
        return;
      // it is essential to assign bottom border of a row above the filter rather than top border of the filter row
      // itself because otherwise IE will make a one-pixel space between vertical separators and this horizontal line
      var lastRowCells = getLastRowCells(true);
      for (var i = 0, count = lastRowCells.length; i < count; i++) {
        var cell = lastRowCells[i];
        O$._setCellStyleProperty(cell, "borderBottom", table._filterRowSeparator);
      }
    };

    tableHeader._updateColumnSeparatorStyles = function() {
      var filterRow = table._filterRowExists ? headRows[table._filterRowIndex] : null;
      var filterRowCells = filterRow ? filterRow._cells : null;

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
                O$._setCellRightBorder(cell, verticalSeparator);
                if (!col.subColumns && filterRowCells)
                  O$._setCellRightBorder(filterRowCells[col._colIndex], verticalSeparator);
              }
              if (col.subColumns)
                setRightBorderForThisGroup(col.subColumns[col.subColumns.length - 1]);
            }

            setRightBorderForThisGroup(col);
          }
          if (col.subColumns) {
            var cell = col.header ? col.header._cell : null;
            if (cell)
              O$._setCellStyleProperty(cell, "borderBottom", separators.multiHeaderSeparator);
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
        O$._setCellStyleProperty(cell, "borderTop", table._footerHorizSeparator != null ? table._footerHorizSeparator : table._horizontalGridLines);
      }
    };

    tableFooter._updateCommonFooterSeparator = function() {
      if (!table._commonFooterExists)
        return;
      var footerRows = table.footer._getRows();
      var commonFooterRow = footerRows[footerRows.length - 1];
      var commonFooterCell = commonFooterRow._cells[0];
      O$._setCellStyleProperty(commonFooterCell, "borderTop", table._commonFooterSeparator);
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
              if (cell) O$._setCellRightBorder(cell, verticalSeparator);
              if (col.subColumns)
                setRightBorderForThisGroup(col.subColumns[col.subColumns.length - 1]);
            }

            setRightBorderForThisGroup(col);
          }
          if (col.subColumns) {
            var cell = col.footer ? col.footer._cell : null;
            if (cell) {
              O$._setCellStyleProperty(cell, "borderTop", separators.multiFooterSeparator);
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
      O$._setCellStyleProperty(cell, "borderBottom", borderBottom);
      if (column._colIndex != colCount - 1) {
        var rightBorder = column.body._rightBorder;
        if (table.body._overriddenVerticalGridlines) {
          var overriddenGridlineStyle = table.body._overriddenVerticalGridlines[column._colIndex];
          if (overriddenGridlineStyle)
            rightBorder = overriddenGridlineStyle;
        }

        O$._setCellRightBorder(cell, rightBorder);
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

};

// -------------------------- TABLE COLUMNS SUPPORT

O$._initTableColumns = function(table, columnHiearachy) {
  table._columnHierarchy = columnHiearachy;
  var deepestColumnHierarchyLevel = 0;
  var realColumns = function processColumns(parentColumn, oneLevelColumns, hierarchyLevel) {
    if (hierarchyLevel > deepestColumnHierarchyLevel)
      deepestColumnHierarchyLevel = hierarchyLevel;
    var collectedColumns = [];
    for (var i = 0, count = oneLevelColumns.length; i < count; i++) {
      var col = oneLevelColumns[i];
      col._hierarchyLevel = hierarchyLevel;
      O$._initTableColumnOrGroup(col, table);
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

  var colTags = O$.getChildNodesWithNames(table, ["col"]);
  if (colTags.length == 0) {
    var colGroup = O$.getChildNodesWithNames(table, ["colgroup"])[0];
    colTags = colGroup ? O$.getChildNodesWithNames(colGroup, ["col"]) : [];
  }
  O$.assert(colTags.length == colCount, "O$._initTableColumns: colTags.length(" + colTags.length + ") != colCount(" + colCount + ")");

  for (var colIndex = 0; colIndex < colCount; colIndex++) {
    var column = realColumns[colIndex];
    var colTag = colTags[colIndex];
    O$._initTableColumn(column, colTag, colIndex);
  }

  table._addCellInsertionCallback(function(cell, row, column) {
    O$._initTableBodyCell(cell, column);
    cell._updateStyle();
  });
};

O$._initTableColumnOrGroup = function(column, table) {
  column._table = table;

  var headRows = table.header ? table.header._getRows() : [];
  var row, cell;
  if (column.header && column.header.pos) {
    var columnHeaderPos = column.header.pos;
    row = headRows[columnHeaderPos.row];
    cell = column.header._cell = row._cells[columnHeaderPos.cell];
    O$._initTableHeaderCell(cell, column);
  }

  if (column.filter && column.filter.pos) {
    var columnFilterPos = column.filter.pos;
    row = headRows[columnFilterPos.row];
    cell = column.filter._cell = row._cells[columnFilterPos.cell];
    O$._initTableSubHeaderCell(cell, column);
  }

  var footRows = table.footer ? table.footer._getRows() : [];
  if (column.footer && column.footer.pos) {
    var columnFooterPos = column.footer.pos;
    row = footRows[columnFooterPos.row];
    cell = column.footer._cell = row._cells[columnFooterPos.cell];
    O$._initTableFooterCell(cell, column);
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

    var subHeaderCell = column.filter ? column.filter._cell : null;
    if (subHeaderCell)
      subHeaderCell._updateStyle();

    var footerCell = column.footer ? column.footer._cell : null;
    if (footerCell)
      footerCell._updateStyle();
  };
};

O$._initTableColumn = function(column, colTag, colIndex) {
  column._colTag = colTag;
  column._colIndex = colIndex;

  var table = column._table;

  var bodyRows = table.body._getRows();
  var bodyRowCount = bodyRows.length;
  column.body._cells = [];

  for (var i = 0; i < bodyRowCount; i++) {
    var row = bodyRows[i];
    var cell = row._getCellByColIndex(colIndex);
    if (cell) {
      O$._initTableBodyCell(cell, column);
    }
    column.body._cells[i] = cell;
  }

  if (column._super_updateStyle)
    throw "O$._initTableColumn can be called only once per column";
  column._super_updateStyle = column._updateStyle;
  column._updateStyle = function() {
    column._super_updateStyle();

    column._useCellStyles = O$._getUseCellStylesForColumn(column);
    var colTagClassName = !column._useCellStyles ? column._getCompoundClassName() : "";
    column._colTag.className = colTagClassName ? colTagClassName : "";
    column._cellStyles = O$._prepareCellStylesForColStyleSimulation(column);

    var bodyCells = column.body ? column.body._cells : [];
    for (var bodyCellIndex = 0, cellCount = bodyCells.length; bodyCellIndex < cellCount; bodyCellIndex++) {
      var cell = bodyCells[bodyCellIndex];
      if (cell)
        cell._updateStyle();
    }

  };

};

O$._initTableHeaderCell = function(cell, column) {
  cell._column = column;
  cell._colIndex = column._colIndex; // todo: review cell._colIndex usages. Remove cell._colIndex property and use appropriate column's property

  cell._updateStyle = function() {
    O$._applySimulatedColStylesToCell(cell);

    var column = cell._column;
    O$._setCellStyleMappings(cell, {_compoundColumnClassName: column._getCompoundClassName(), _colHeaderClass: column.header ? column.header.className : null});
  };

};

O$._initTableSubHeaderCell = function(cell, column) {
  cell._column = column;
  cell._colIndex = column._colIndex;

  cell._updateStyle = function() {
    O$._applySimulatedColStylesToCell(cell);

    var column = cell._column;
    O$._setCellStyleMappings(cell, {_compoundColumnClassName: column._getCompoundClassName(), _colFilterClass: column.filter ? column.filter.className : null});
  };

};

O$._initTableFooterCell = function(cell, column) {
  cell._column = column;
  cell._colIndex = column._colIndex;

  cell._updateStyle = function() {
    O$._applySimulatedColStylesToCell(cell);

    var column = cell._column;
    O$._setCellStyleMappings(cell, {_compoundColumnClassName: column._getCompoundClassName(), _colFooterClass: column.footer ? column.footer.className : null});
  };
};

O$._initTableBodyCell = function(cell, column) {
  cell._column = column;

  cell._updateStyle = function() {
    var column = cell._column;
    var row = cell._row;
    var table = row._table;
    var rowIndex = row._index;
    var colIndex = column._colIndex;

    if (!table._noDataRows) {
      O$._applySimulatedColStylesToCell(cell);
    }

    var cellStyleMappings = {};
    if (table._forceUsingCellStyles || column._useCellStyles) {
      cellStyleMappings._rowInitialClass = row._initialClass;
      cellStyleMappings._rowIndividualClass = table._rowStylesMap[row._index];
    }

    if (table._noDataRows) {
      O$._assignNoDataCellStyle(cell);
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
    O$._updateCellWrapperStyle(cell);
  };

};

/*
 * Fix for JSFC-2834. First column style should not be applied to column tag in order to avoid applying it to common
 * header and common footer rows that span across all columns.
 */
O$._getUseCellStylesForColumn = function(column) {
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
};

O$._assignNoDataCellStyle = function(cell) {
  var column = cell._column;
  var row = cell._row;
  var table = row._table;
  if (!(table._forceUsingCellStyles || column._useCellStyles)) {
    // first column style shouldn't be applied to the no-data-row
    // no-data row should derive unspecified properties from body section instead
    var noDataRowClass = table._rowStylesMap[0];
    var noDataRowStyle = noDataRowClass ? O$._evaluateStyleClassProperties_cached(noDataRowClass, ["backgroundColor", "background"], table) : {};
    if (!noDataRowStyle.backgroundColor && !noDataRowStyle.background) {
      var bodySection = O$.getChildNodesWithNames(table, ["tbody"])[0];
      var propertyValue = O$.getElementStyleProperty(bodySection, "background-color");
      if (!propertyValue || propertyValue == "transparent") {
        propertyValue = O$.getElementStyleProperty(table, "background-color");
      }
      if (!propertyValue || propertyValue == "transparent")
        propertyValue = !O$.isSafari() ? "Window" : "white";
      if (propertyValue)
        O$._setCellStyleProperty(cell, "backgroundColor", propertyValue);
    }
  }

};

O$._initTableColumnEvents = function(table) {

  function assignBodyCellEvents(cell, compoundBodyEvents, compoundGeneralEvents) {
    O$._assignColumnCellEvents(cell, compoundBodyEvents);
    O$._assignColumnCellEvents(cell, compoundGeneralEvents);
  }

  function assignColEvents(oneLevelColumns, additionalGeneralEvents, additionalBodyEvents) {
    for (var i = 0, count = oneLevelColumns.length; i < count; i++) {
      var column = oneLevelColumns[i];
      var compoundGeneralEvents = additionalGeneralEvents ? [column].concat(additionalGeneralEvents) : [column];
      var compoundBodyEvents = additionalBodyEvents ? [column.body].concat(additionalBodyEvents) : [column.body];

      var headerCell = column.header ? column.header._cell : null;
      if (headerCell)
        O$._assignColumnCellEvents(headerCell, compoundGeneralEvents, column.header);

      var subHeaderCell = column.filter ? column.filter._cell : null;
      if (subHeaderCell)
        O$._assignColumnCellEvents(subHeaderCell, compoundGeneralEvents, column.filter);

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
        O$._assignColumnCellEvents(footerCell, compoundGeneralEvents, column.footer);

      if (column.subColumns) {
        assignColEvents(column.subColumns, compoundGeneralEvents, compoundBodyEvents);
      }
    }
  }

  assignColEvents(table._columnHierarchy, [], []);

  table._addCellInsertionCallback(function(cell, row, column) {
    assignBodyCellEvents(cell, column.body._getCompoundEventContainers(), column._getCompoundEventContainers());
  });

};

O$._initTableColumnStyles = function(table) {
  function assignColStyles(oneLevelColumns) {
    for (var i = 0, count = oneLevelColumns.length; i < count; i++) {
      var col = oneLevelColumns[i];
      col._updateStyle();
      if (col.subColumns)
        assignColStyles(col.subColumns);
    }
  }

  assignColStyles(table._columnHierarchy);
};

/**
 * Some CSS styles that can be applid to <col> tags don't behave in a cross-browser way. We extract these styles here
 * for their further applying to column cells in order to simulate the proper behavior.
 */
O$._prepareCellStylesForColStyleSimulation = function(column) {
  var isExplorer = O$.isExplorer();
  var isMozilla = O$.isMozillaFF();
  var isOpera = O$.isOpera();

  var table = column._table;
  var colTag = column._colTag;
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
  var colStyleProperties = O$._evaluateStyleClassProperties_cached(colTagClassName, colProperties, table);
  if (isMozilla || isOpera) {
    if (!cellStyles)
      cellStyles = {};
    cellStyles._width = colStyleProperties.width;//O$.getStyleClassProperty(colTag.className, "width");
    cellStyles._textAlign = colStyleProperties.textAlign;//O$._getUserStylePropertyValue(colTag, "text-align", "start", "left");
    cellStyles._verticalAlign = O$._getUserStylePropertyValue(colTag, "vertical-align", "baseline", "auto");
    cellStyles._lineHeight = O$._getUserClassPropertyValue(colTag, "line-height", "normal");

    cellStyles._paddingLeft = O$._getUserStylePropertyValue(colTag, "padding-left", "0px");
    cellStyles._paddingRight = O$._getUserStylePropertyValue(colTag, "padding-right", "0px");
    cellStyles._paddingTop = O$._getUserStylePropertyValue(colTag, "padding-top", "0px");
    cellStyles._paddingBottom = O$._getUserStylePropertyValue(colTag, "padding-bottom", "0px");

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
};

O$._clearCellStyleProperties = function(cell, propertyNames) {
  if (!cell)
    return;

  for (var i = 0, count = propertyNames.length; i < count; i++) {
    var propertyName = propertyNames[i];
    cell.style[propertyName] = null;
  }

};

O$._setCellStyleProperty = function(cell, propertyName, propertyValue) {
  if (!cell || !propertyValue)
    return;

  try {
    cell.style[propertyName] = propertyValue;
  } catch (e) {
    O$.logError("O$._setCellStyleProperty: couldn't set style property \"" + propertyName + "\" to \"" + propertyValue + "\" ; original error: " + e.message);
    throw e;
  }
};

O$._setCellRightBorder = function(cell, borderValue) {
  if (!cell || !borderValue)
    return;

  try {
    cell.style.borderRight = borderValue;
  } catch (e) {
    O$.logError("O$._setCellRightBorder: invalid borderValue: \"" + borderValue + "\" ; it must be a valid CSS declaration of form \"1px solid gray\" ; original error: " + e.message);
    throw e;
  }
};

O$._setCellStyleMappings = function(cell, styleMappings) {
  O$.setStyleMappings(cell, styleMappings);
  O$._updateCellWrapperStyle(cell);
};

O$._assignColumnCellEvents = function(cell, eventContainers, additionalEventContainer) {
  if (additionalEventContainer)
    O$.assignEvents(cell, additionalEventContainer);
  for (var i = 0, count = eventContainers.length; i < count; i++) {
    var eventContainer = eventContainers[i];
    O$.assignEvents(cell, eventContainer);
  }
};

O$._applySimulatedColStylesToCell = function(cell) {
  var column = cell._column;

  if (column._forceCellAlign)
    cell.align = column._forceCellAlign;
  if (column._forceCellVAlign)
    cell.vAlign = column._forceCellVAlign;

  var cellStyles = column._cellStyles;
  if (cellStyles) {
    O$._setCellStyleProperty(cell, "textAlign", cellStyles._textAlign);
    O$._setCellStyleProperty(cell, "verticalAlign", cellStyles._verticalAlign);
    O$._setCellStyleProperty(cell, "width", cellStyles._width);
    O$._setCellStyleProperty(cell, "lineHeight", cellStyles._lineHeight);

    O$._setCellStyleProperty(cell, "color", cellStyles._color);
    O$._setCellStyleProperty(cell, "fontFamily", cellStyles._fontFamily);
    O$._setCellStyleProperty(cell, "fontSize", cellStyles._fontSize);
    O$._setCellStyleProperty(cell, "fontWeight", cellStyles._fontWeight);
    O$._setCellStyleProperty(cell, "fontStyle", cellStyles._fontStyle);

    O$._setCellStyleProperty(cell, "paddingLeft", cellStyles._paddingLeft);
    O$._setCellStyleProperty(cell, "paddingRight", cellStyles._paddingRight);
    O$._setCellStyleProperty(cell, "paddingTop", cellStyles._paddingTop);
    O$._setCellStyleProperty(cell, "paddingBottom", cellStyles._paddingBottom);
    O$._setCellStyleProperty(cell, "borderLeft", cellStyles._borderLeft);
    O$._setCellStyleProperty(cell, "borderRight", cellStyles._borderRight);
    cell._simulatedColStylesApplied = true;
  } else {
    if (cell._simulatedColStylesApplied) {
      O$._clearCellStyleProperties(cell,
              ["textAlign", "verticalAlign", "width", "lineHeight", "color", "fontFamily", "fontSize", "fontWeight",
                "fontStyle", "paddingLeft", "paddingRight", "paddingTop", "paddingBottom", "borderLeft", "borderRight"]);
      cell._simulatedColStylesApplied = false;
    }
  }

};

O$._removeTableColumn = function(column) {
  var table = column._table;
  var bodyRows = table.body._getRows();
  for (var i = 0, count = bodyRows.length; i < count; i++) {
    var row = bodyRows[i];
    row._removeColumn(column);
  }
};
