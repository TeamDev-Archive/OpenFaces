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

O$.Tables = {

  // -------------------------- COMMON UTILITIES

  _getUserStylePropertyValue: function(propertyValues, propertyName, defaultValue1, defaultValue2) {
    var value = propertyValues[propertyName];
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
      var paddingNames = ["padding-left", "padding-right", "padding-top", "padding-bottom"];

      var paddings = O$.getElementStyle(row, paddingNames);

      var lineHeight = O$.Tables._getUserClassPropertyValue(row, "line-height", "normal");

      if (paddingNames.some(function (n) {
        var padding = paddings[n];
        return padding && padding != "0px";
      }) || lineHeight) {
        for (cellIndex = 0,cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
          cell = cells[cellIndex];

          paddingNames.forEach(function (n) {
            O$.Tables._setCellStyleProperty(cell, n, paddings[n]);
          });
        }
        O$.Tables._setCellStyleProperty(cell, "line-height", lineHeight);
      }
    }

    var borderNames = ["border-left", "border-right", "border-top", "border-bottom"];
    var borders = O$.Tables._evaluateStyleClassProperties_cached(
            row.className, borderNames,
            row._row ? row._row._table : row._table);
    if (borderNames.some(function (n) {
      return borders[n];
    })) {
      for (cellIndex = 0,cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
        cell = cells[cellIndex];
        borderNames.forEach(function(n) {
          O$.Tables._setCellStyleProperty(cell, n, borders[n]);
        });
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

  _getTableColTags: function(table) {
    var result = O$.getChildNodesWithNames(table, ["col"]);
    if (result.length == 0) {
      var colGroup = O$.getChildNodesWithNames(table, ["colgroup"])[0];
      result = colGroup ? O$.getChildNodesWithNames(colGroup, ["col"]) : [];
    }
    return result;
  },

  // -------------------------- INITIALIZATION

  _init: function(table, params) {
    if (!params.rowStylesMap) params.rowStylesMap = {};
    if (!params.cellStylesMap) params.cellStylesMap = {};
    if (!params.body) params.body = {};
    table._params = params;

    var tempIdx = 0;
    table._gridLines = {
      horizontal: params.gridLines[tempIdx++],
      vertical: params.gridLines[tempIdx++],
      commonHeader: params.gridLines[tempIdx++],
      commonFooter: params.gridLines[tempIdx++],
      headerHoriz: params.gridLines[tempIdx++],
      headerVert: params.gridLines[tempIdx++],
      subHeaderRow: params.gridLines[tempIdx++],
      multiHeader: params.gridLines[tempIdx++],
      multiFooter: params.gridLines[tempIdx++],
      footerHoriz: params.gridLines[tempIdx++],
      footerVert: params.gridLines[tempIdx++]
    };
    table.style.emptyCells = "show";

    O$.extend(table, {
      _rowInsertionCallbacks: [],
      _addRowInsertionCallback: function(callback) {
        table._rowInsertionCallbacks.push(callback);
      },

      _singleRowInsertionCallbacks: [],
      _addSingleRowInsertionCallback: function(callback) {
        table._singleRowInsertionCallbacks.push(callback);
      },

      _cellInsertionCallbacks: [],
      _addCellInsertionCallback: function(callback) {
        table._cellInsertionCallbacks.push(callback);
      },

      _cellMoveCallbacks: [],
      _addCellMoveCallback: function(callback) {
        table._cellMoveCallbacks.push(callback);
      }
    });

    O$.Tables._initRows(table);
    O$.Tables._initColumns(table);
    O$.Tables._initColumnEvents(table);
    O$.Tables._initColumnStyles(table);
    O$.Tables._initGridLines(table);
    if (table._params.scrolling)
      O$.Tables._initScrolling(table);

    [table.header, table.footer].forEach(function (area) {
      if (!area) return;
      function fixInputWidths(element) {
        if (element) O$.fixInputsWidthStrict(element);
      }

      fixInputWidths(area._tag);
      fixInputWidths(area._leftScrollingArea && area._leftScrollingArea._rowContainer);
      fixInputWidths(area._centerScrollingArea && area._centerScrollingArea._rowContainer);
      fixInputWidths(area._rightScrollingArea && area._rightScrollingArea._rowContainer);
    });

    table._insertRowsAfter = O$.Tables._insertRowsAfter;
    table._cellFromPoint = function(x, y, relativeToNearestContainingBlock, cachedDataContainer) {
      var row = table.body._rowFromPoint(x, y, relativeToNearestContainingBlock, cachedDataContainer);
      if (!row)
        return null;
      return row._cellFromPoint(x, y, relativeToNearestContainingBlock, cachedDataContainer);
    };
  },

  _insertRowsAfter: function(afterIndex, rowsToInsert, newRowsToStylesMap, newRowCellsToStylesMap) {
    if (rowsToInsert.length == 0) {
      return;
    }

    if (this._params.body.noDataRows && this.body._getRows().length) {
      this._params.body.noDataRows = false;
      this.body._removeAllRows();
    }

    var bodyRows = this.body._getRows();
    var columns = this._columns;

    var visibleRowCount = 0;
    bodyRows.forEach(function(row) {
      row._visibleRowIndex = visibleRowCount;
      if (row._isVisible())
        visibleRowCount++;
    });

    var i, count, row;
    var visibleRowsUpToReferenceRow; // visible rows including bodyRows[afterIndex]
    if (!this._params.invisibleRowsAllowed)
      visibleRowsUpToReferenceRow = afterIndex + 1;
    else {
      visibleRowsUpToReferenceRow = 0;
      for (i = 0; i <= afterIndex; i++) {
        row = bodyRows[i];
        if (row._isVisible())
          visibleRowsUpToReferenceRow++;
      }
    }

    rowsToInsert.forEach(function(f) {
      if (!f._rowNode && f.nodeName && f.nodeName.toUpperCase() == "TR") f._rowNode = f;
    });

    var visibleInsertedRows;
    if (!this._params.invisibleRowsAllowed)
      visibleInsertedRows = rowsToInsert.length;
    else {
      visibleInsertedRows = 0;
      rowsToInsert.forEach(function(insertedRow) {
        insertedRow._visible = (O$.getElementStyle(insertedRow._rowNode, "display") != "none");
        if (insertedRow._visible)
          visibleInsertedRows++;
      });
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

        this._params.rowStylesMap[newRowIndex] = this._params.rowStylesMap[originalRowIndex];
        this._params.rowStylesMap[originalRowIndex] = null;
      }

      var colIndex, colCount, column, bodyCells;
      for (colIndex = 0,colCount = columns.length; colIndex < colCount; colIndex++) {
        column = columns[colIndex];
        bodyCells = column.body._cells;
        var movedCell = bodyCells[originalRowIndex];

        if (moveRow) {
          bodyCells[newRowIndex] = movedCell;
          bodyCells[originalRowIndex] = null;
          var originalCellKey = originalRowIndex + "x" + colIndex;
          var newCellKey = newRowIndex + "x" + colIndex;
          this._params.cellStylesMap[newCellKey] = this._params.cellStylesMap[originalCellKey];
          this._params.cellStylesMap[originalCellKey] = null;
        }
      }

      movedRow._notifyRowMoved(moveRow ? movedRow._visibleRowIndex + visibleInsertedRows : movedRow._visibleRowIndex);
    }

    var newVisibleRowsForNow = 0;
    var callbackIndex, callbackCount;
    for (i = 0,count = rowsToInsert.length; i < count; i++) {
      var newRow = rowsToInsert[i];
      if (!this._params.scrolling) {
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
        if (newRow instanceof Array)
          newRow = {_leftRowNode: newRow[0], _rowNode: newRow[1], _rightRowNode: newRow[2]};
        function addRow(area, rowNode) {
          if (!area || !rowNode) return;
          var nextRowIdx = afterIndex + 1 + i;
          if (nextRowIdx < area._rowContainer.childNodes.length)
            area._rowContainer.insertBefore(rowNode, area._rowContainer.childNodes[nextRowIdx]);
          else
            area._rowContainer.appendChild(rowNode);
        }

        addRow(this.body._leftScrollingArea, newRow._leftRowNode);
        addRow(this.body._centerScrollingArea, newRow._rowNode);
        addRow(this.body._rightScrollingArea, newRow._rightRowNode);
      }

      newRowIndex = afterIndex + 1 + i;
      bodyRows[newRowIndex] = newRow;
      this._params.rowStylesMap[newRowIndex] = newRowsToStylesMap ? newRowsToStylesMap[i] : undefined;
      if (!this._params.scrolling)
        newRow._cells = newRow.cells;
      else {
        var cells = [];
        [newRow._leftRowNode, newRow._rowNode, newRow._rightRowNode].forEach(function(rowNode) {
          if (!rowNode) return;
          rowNode._row = newRow;
          rowNode._cells = rowNode.cells;
          for (var i = 0, count = rowNode.cells.length; i < count; i++) {
            cells.push(rowNode._cells[i]);
          }
        });
        newRow._cells = cells;
      }

      O$.Tables._initBodyRow(newRow, this, newRowIndex, visibleRowsUpToReferenceRow + newVisibleRowsForNow);
      if (newRow._isVisible())
        newVisibleRowsForNow++;

      for (colIndex = 0,colCount = columns.length; colIndex < colCount; colIndex++) {
        column = columns[colIndex];
        var cellStyleKey = newRowIndex + "x" + colIndex;
        this._params.cellStylesMap[cellStyleKey] = newRowCellsToStylesMap ? newRowCellsToStylesMap[i + "x" + colIndex] : undefined;
        var cell = newRow._getCellByColIndex(colIndex);

        bodyCells = column.body._cells;
        bodyCells[newRowIndex] = cell;
        if (cell)
          for (callbackIndex = 0,callbackCount = this._cellInsertionCallbacks.length; callbackIndex < callbackCount; callbackIndex++)
            this._cellInsertionCallbacks[callbackIndex](cell, newRow, column);
      }

      for (callbackIndex = 0,callbackCount = this._singleRowInsertionCallbacks.length; callbackIndex < callbackCount; i++)
        this._singleRowInsertionCallbacks[callbackIndex](newRow);
    }

    this._rowInsertionCallbacks.forEach(function(callback) {
      callback(this, afterIndex, rowsToInsert);
    }, this);
    // update body section style in case of the simulated sections mode
    this.body._updateStyle();

  },


  // -------------------------- TABLE ROWS SUPPORT

  _initRows: function(table) {
    if (table._params.scrolling) {
      function tableRows(table, sectionTagName) {
        var rows = O$.getChildNodesWithNames(table, ["tr"]);
        if (!rows.length) {
          var body = O$.getChildNodesWithNames(table, [sectionTagName])[0];
          rows = body ? O$.getChildNodesWithNames(body, ["tr"]) : [];
        }
        return rows;
      }

      var rows = tableRows(table, "tbody");
      if (rows.length != 1)
        throw "O$._initRows: one root row expected, but was: " + rows.length;
      var tableContainer = O$.getElementByPath(rows[0], "td/div");
      table._topLevelScrollingDiv = tableContainer;
      tableContainer.style.visibility = "hidden"; // hide the container temporarily until its width is corrected to avoid flickering
      var tables = O$.getChildNodesWithNames(tableContainer, ["table"]);
      var sectionIndex = 0;
      var headTable = (table._params.header && table._params.header.rowCount) ? tables[sectionIndex++] : null;
      var bodyTable = tables[sectionIndex++];
      var footTable = (table._params.footer && table._params.footer.rowCount) ? tables[sectionIndex++] : null;

      function applyStyle(element, style) {
        if (element) O$.setStyleMappings(element, {sectionStyle: style});
      }

      applyStyle(headTable, table._params.header.className);
      applyStyle(bodyTable, table._params.body.className);
      applyStyle(footTable, table._params.footer.className);
    }
    function initTableSection(table, sectionParams, sectionTagName, commonRowAbove) {
      var section = {
        _tag: !table._params.scrolling ? O$.getChildNodesWithNames(table, [sectionTagName])[0] : null,
        _updateStyle: function() {
          if (!sectionParams)
            return;
          var elements = [].concat(this._tag ? [this._tag] : this._getRows());
          if (table._params.scrolling && O$.isQuirksMode()) {
            // allow header sections font-weight and other attributes to be passed onto actual header cells under
            // quirks mode
            this._scrollingAreas.forEach(function(area) {
              elements.push(area._table);
            });
          }
          elements.forEach(function(element) {
            O$.setStyleMappings(element, {sectionStyle: sectionParams.className});
          });
        },
        _getRows: function() {
          if (!this._rows) {
            var fakeRowRequired = O$.Tables._isFakeRowRequired();
            // no-cells fake row is required under IE6/7+strict to make column width calculation uniform across browsers (see OF-24)

            var scrolling = table._params.scrolling;
            if (!scrolling) {
              if (fakeRowRequired) {
                if (!this._fakeRow) {
                  this._fakeRow = document.createElement("tr");
                  this._fakeRow.style.display = "none";
                  if (this._tag)
                    this._tag.insertBefore(this._fakeRow, this._tag.firstChild);
                }
              }
              this._rows = this._tag ? O$.getChildNodesWithNames(this._tag, ["tr"]) : [];
              if (this._fakeRow) this._rows = this._rows.slice(1);
              this._rows.forEach(function(row) {
                row._table = table;
                row._index = i;
                row._cells = row.cells;
              });
              this._cellByCoords = function(coords) {
                var row = this._rows[coords.row];
                return row._cells[coords.cell];
              };
            } else {
              this._sectionTable = function() {
                if (sectionTagName == "thead")
                  return headTable;
                else if (sectionTagName == "tbody")
                  return bodyTable;
                else if (sectionTagName == "tfoot")
                    return footTable;
                  else
                    throw "Unknown section tag name: " + sectionTagName;
              }();
              if (!this._sectionTable)
                return [];
              var immediateRows = tableRows(this._sectionTable, sectionTagName);
              this._sectionTable.style.tableLayout = "fixed";
              var commonRow = commonRowAbove != undefined ? (
                      commonRowAbove ? immediateRows[0] : immediateRows[immediateRows.length - 1]
                      ) : null;
              if (commonRow) {
                commonRow._cells = commonRow.cells;
                commonRow._table = table;
              }
              var containerRowIndex = commonRowAbove != undefined
                      ? (commonRowAbove ? 1 : 0)
                      : 0;
              var containerRow = immediateRows[containerRowIndex];
              var leftCellIndex = scrolling.leftFixedCols ? 0 : undefined;
              var centerCellIndex = leftCellIndex != undefined ? 1 : 0;
              var rightCellIndex = scrolling.rightFixedCols ? centerCellIndex + 1 : undefined;

              function scrollingArea(areaCellIndex, scrollingKind) {
                var td = containerRow.childNodes[areaCellIndex];
                td.style.verticalAlign = "top";
                td.style.textAlign = "left";
                td.style.overflow = "hidden";
                var div = O$.getChildNodesWithNames(td, ["div"])[0];
                var scrollingDivContainer = div && div.className == "o_scrolling_area_container" ? div : null;
                if (scrollingDivContainer && O$.isExplorer() && O$.isQuirksMode()) {
                  scrollingDivContainer.style.display = "none"; // hide the container temporarily to overcome non-working "overflow: hidden" on td under IE quirks-mode
                }
                var scrollingDiv = O$.getChildNodesWithNames(scrollingDivContainer ? scrollingDivContainer : td, ["div"])[0];
                var tbl = O$.getChildNodesByClass(scrollingDiv ? scrollingDiv : td, ["o_scrolling_area_table"])[0];
                var spacer = O$.getChildNodesByClass(scrollingDiv ? scrollingDiv : td, ["o_scrolling_area_spacer"], false, tbl)[0];

                tbl.style.emptyCells = "show";
                applyStyle(tbl, sectionParams.className);
                var rowContainer = O$.getChildNodesWithNames(tbl, ["tbody"])[0];
                if (fakeRowRequired) {
                  if (!rowContainer._fakeRow) {
                    rowContainer._fakeRow = document.createElement("tr");
                    rowContainer._fakeRow.style.display = "none";
                    rowContainer.insertBefore(rowContainer._fakeRow, rowContainer.firstChild);
                  }
                }

                var area = {
                  _horizontalIndex: areaCellIndex,
                  _td: td,
                  _scrollingDivContainer: scrollingDivContainer,
                  _scrollingDiv: scrollingDiv,
                  _table: tbl,
                  _rowContainer: rowContainer,
                  _rows: O$.getChildNodesWithNames(rowContainer, ["tr"]),
                  _spacer: spacer
                };
                if (rowContainer._fakeRow)
                  area._rows = area._rows.slice(1);
                if (area._scrollingDiv) {
                  if (scrollingKind == "none") {
                    // leave overflow as specified during rendering
                  } else if (scrollingKind == "x")
                    area._scrollingDiv.style.overflowX = "scroll";
                  else if (scrollingKind == "y")
                      area._scrollingDiv.style.overflowY = "scroll";
                  else if (scrollingKind == "both")
                      area._scrollingDiv.style.overflow = "scroll";
                  else
                    throw "initTableSection/scrollingArea: unknown scrollingKind: " + scrollingKind;
                }
                return area;
              }

              var scrollingKinds;
              if (sectionTagName == "thead") scrollingKinds = {left: "none", center: "none", right: "none"};
              else if (sectionTagName == "tbody") scrollingKinds = {left: "none", center: scrolling.horizontal ? "both" : "y", right: "none"};
              else if (sectionTagName == "tfoot") scrollingKinds = {left: "none", center: "none", right: "none"};
                else throw "initTableSection: unknown sectionTagName: " + sectionTagName;
              this._leftScrollingArea = leftCellIndex != undefined ? scrollingArea(leftCellIndex, scrollingKinds.left) : null;
              this._centerScrollingArea = scrollingArea(centerCellIndex, scrollingKinds.center);
              this._rightScrollingArea = rightCellIndex != undefined ? scrollingArea(rightCellIndex, scrollingKinds.right) : null;
              var scrollingAreas = [];
              if (this._leftScrollingArea) scrollingAreas.push(this._leftScrollingArea);
              if (this._centerScrollingArea) scrollingAreas.push(this._centerScrollingArea);
              if (this._rightScrollingArea) scrollingAreas.push(this._rightScrollingArea);
              this._scrollingAreas = scrollingAreas;
              this._cellByCoords = function(coords) {
                var area = this._scrollingAreas[coords.scrollingArea];
                var row = area._rows[coords.row];
                return row.cells[coords.cell];
              };
              var rows = [];
              var rowIndex = 0, rowCount = this._centerScrollingArea._rows.length, areaRowIndex = 0;
              if (commonRowAbove === true)
                rows[rowIndex++] = commonRow;
              while (areaRowIndex < rowCount) {
                var row = {
                  _leftRowNode: this._leftScrollingArea ? this._leftScrollingArea._rows[areaRowIndex] : null,
                  _rowNode: this._centerScrollingArea._rows[areaRowIndex],
                  _rightRowNode: this._rightScrollingArea ? this._rightScrollingArea._rows[areaRowIndex] : null,
                  _table: table,
                  _index: rowIndex
                };
                rows[rowIndex] = row;
                var cells = [];
                if (row._leftRowNode) {
                  row._leftRowNode._cells = row._leftRowNode.cells;
                  row._leftRowNode._row = row;
                  O$.addAll(cells, row._leftRowNode._cells);
                }
                row._rowNode._cells = row._rowNode.cells;
                row._rowNode._row = row;
                O$.addAll(cells, row._rowNode._cells);
                if (row._rightRowNode) {
                  row._rightRowNode._cells = row._rightRowNode.cells;
                  row._rightRowNode._row = row;
                  O$.addAll(cells, row._rightRowNode._cells);
                }
                row._cells = cells;
                rowIndex++;
                areaRowIndex++;
              }
              if (commonRowAbove === false)
                rows[rowIndex] = commonRow;
              if (commonRow)
                commonRow._index = commonRowAbove ? 0 : rows.length - 1;
              this._rows = rows;
            }
          }
          return this._rows;
        },
        _createRow: function() {
          function createRow(colCount) {
            if (!colCount) return null;
            var newRow = document.createElement("tr");
            for (var i = 0; i < colCount; i++) {
              var newCell = document.createElement("td");
              newRow.appendChild(newCell);
            }
            return newRow;
          }

          if (!table._params.scrolling)
            return createRow(table._columns.length);
          else {
            return {
              _leftRowNode: createRow(table._params.scrolling.leftFixedCols),
              _rowNode: createRow(table._columns.length - table._params.scrolling.leftFixedCols - table._params.scrolling.rightFixedCols),
              _rightRowNode: createRow(table._params.scrolling.rightFixedCols)
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
          table._insertRowsAfter(afterRow != undefined ? afterRow : this._getRows().length - 1, rows);
        },

        _rowFromPoint: function(x, y, relativeToNearestContainingBlock, cachedDataContainer) {
          var tableRect = function() {
            if (!table._params.scrolling)
              return O$.getElementBorderRectangle(table, relativeToNearestContainingBlock, cachedDataContainer);
            return null;
          }();
          if (tableRect && !tableRect.containsPoint(x, y))
            return null;
          var rows = this._getRows();
          for (var i = 0, count = rows.length; i < count; i++) {
            var row = rows[i];
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
        },

        _removeAllRows: function() {
          var rows = this._getRows();
          rows.forEach(function(row) {
            function removeRow(row) {
              if (row) row.parentNode.removeChild(row);
            }

            removeRow(row._leftRowNode);
            removeRow(row._rowNode);
            removeRow(row._rightRowNode);
          });
          this._rows = [];

          if (sectionTagName == "tbody") {
            table._params.rowStylesMap = {};
            table._params.cellStylesMap = {};
          }
        }



      };
      if (sectionTagName != "tbody" && section._getRows().length == 0)
        return null;
      section._updateStyle();
      return section;
    }

    table.header = initTableSection(table, table._params.header, "thead", table._params.header && table._params.header.commonHeader ? true : undefined);
    table.footer = initTableSection(table, table._params.footer, "tfoot", table._params.footer && table._params.footer.commonHeader ? false : undefined);
    table.body = initTableSection(table, table._params.body, "tbody", undefined);

    if (table.header) {
      var headRows = table.header._getRows();
      headRows.forEach(function(row) {
        O$.Tables._initRow(row);
      });
      var commonHeaderRowIndex = table._params.header && table._params.header.commonHeader ? 0 : -1;
      var subHeaderRowIndex = table._params.header && table._params.header.subHeader ? headRows.length - 1 : -1;
      table._subHeaderRowIndex = subHeaderRowIndex;
      table._columnHeadersRowIndexRange = [commonHeaderRowIndex + 1, table._params.header && table._params.header.subHeader ? headRows.length - 1 : headRows.length];
      table._commonHeaderRowIndex = commonHeaderRowIndex;

      if (commonHeaderRowIndex != -1)
        O$.Tables._setRowStyle(headRows[commonHeaderRowIndex], {commonHeaderRow: table._params.header.commonHeader.className});
      if (subHeaderRowIndex != -1)
        O$.Tables._setRowStyle(headRows[subHeaderRowIndex], {subHeaderRow: table._params.header.subHeader.className});
      for (var i = table._columnHeadersRowIndexRange[0], end = table._columnHeadersRowIndexRange[1]; i < end; i++)
        O$.Tables._setRowStyle(headRows[i], {headerRow: table._params.header ? table._params.header.headingsClassName : null});
    }

    var visibleRowCount = 0;
    var bodyRows = table.body._getRows();
    var rowIndex, rowCount, row;
    for (rowIndex = 0,rowCount = bodyRows.length; rowIndex < rowCount; rowIndex++) {
      row = bodyRows[rowIndex];
      O$.Tables._initBodyRow(row, table, rowIndex, visibleRowCount);
      if (row._isVisible())
        visibleRowCount++;
    }

    if (table.footer) {
      var footRows = table.footer._getRows();
      var lastNonCommonFooter = footRows.length - 1;
      if (table._params.footer && table._params.footer.commonHeader)
        lastNonCommonFooter--;
      for (rowIndex = 0,rowCount = footRows.length; rowIndex < rowCount; rowIndex++) {
        row = footRows[rowIndex];
        O$.Tables._initRow(row);

        if (rowIndex <= lastNonCommonFooter)
          O$.Tables._setRowStyle(footRows[rowIndex], {footerRow: table._params.footer ? table._params.footer.headingsClassName : null});
      }
      if (table._params.footer && table._params.footer.commonHeader)
        O$.Tables._setRowStyle(footRows[footRows.length - 1], {commonFooterRow: table._params.footer ? table._params.footer.commonHeader.className : null});
    }
  },

  _isFakeRowRequired: function() {
    return O$.isStrictMode() && (O$.isExplorer6() || O$.isExplorer7());
  },

  _setRowStyle: function(row, styleMappings) {
    var table = row._table;
    if (table._params.forceUsingCellStyles) {
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
    O$.extend(row, {
      _table: table,
      _index: rowIndex,
      _selected: false,

      _isVisible: function () {
        return this._visible;
      },
      _setVisible: function (visible) {
        if (this._visible == visible)
          return;
        this._visible = visible;
        [this._leftRowNode, this._rowNode, this._rightRowNode].forEach(function(n) {
          if (!n) return;
          if (n.style.display) n.style.display = "";
          O$.setStyleMappings(n, {rowVisibilityStyle: visible ? "" : "o_hiddenRow"});
        });
        O$.Tables._updateCellWrappersStyleForRow(this);
      },

      _mouseOverHandler: function() {
        if (this._row)
          this._row._mouseIsOver = true;
        else
          this._mouseIsOver = true;
        this._updateStyle();
      },

      _mouseOutHandler: function() {
        if (this._row)
          this._row._mouseIsOver = false;
        else
          this._mouseIsOver = false;
        this._updateStyle();
      },

      _cellFromPoint: function(x, y, relativeToNearestContainingBlock, cachedDataContainer) {
        for (var cellIndex = 0, cellCount = row._cells.length; cellIndex < cellCount; cellIndex++) {
          var cell = row._cells[cellIndex];
          var cellRect = O$.getElementBorderRectangle(cell, relativeToNearestContainingBlock, cachedDataContainer);
          if (cellRect.containsPoint(x, y))
            return cell;
        }
        return null;
      }
    });
    if (!row._rowNode) {
      if (!row.nodeName || row.nodeName.toLowerCase() != "tr")
        throw "O$.Tables._initBodyRow: row._rowNode must be initialized for synthetic (scrollable-version) rows";
      row._rowNode = row;
    }
    if (row._visible === undefined)
      row._visible = table._params.invisibleRowsAllowed ? O$.getElementStyle(row._rowNode, "display") != "none" : true;

    row._mouseIsOver = false;
    if (!table._params.body.noDataRows && table._params.body.rolloverRowClassName) {
      function addEventHandler(row, event, handler) {
        O$.addEventHandler(row._rowNode, event, handler);
        if (row._leftRowNode)
          O$.addEventHandler(row._leftRowNode, event, handler);
        if (row._rightRowNode)
          O$.addEventHandler(row._rightRowNode, event, handler);
      }

      addEventHandler(row, "mouseover", function() {
        row._mouseOverHandler();
      });
      addEventHandler(row, "mouseout", function() {
        row._mouseOutHandler();
      });
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

    O$.Tables._initRow(row);

    function reinitializeStyle(visibleRowsBefore) {
      var rowClass;
      if (!table._params.body.noDataRows)
        rowClass = (visibleRowsBefore % 2 == 0)
                ? table._params.body.rowClassName
                : (table._params.body.oddRowClassName ? table._params.body.oddRowClassName : table._params.body.rowClassName);
      else
        rowClass = null;

      row.initialClass = rowClass;
      row._addedClassName = undefined;
      if (!table._params.forceUsingCellStyles) {
        var individualRowClass = table._params.rowStylesMap[row._index];
        O$.Tables._setRowStyle(row, {initialClass: row.initialClass, individualRowClass: individualRowClass});
      }
    }

    reinitializeStyle(visibleRowsBefore);

    row._rowMoveCallbacks = [];
    row._addRowMoveCallback = function(callback) {
      row._rowMoveCallbacks.push(callback);
    };
    row._notifyRowMoved = function(visibleRowsBefore) {
      var callbackIndex, callbackCount;
      row._rowMoveCallbacks.forEach(function(callback) {
        callback(visibleRowsBefore);
      });
      for (var i = 0, count = row._cells.length; i < count; i++) {
        var cell = row._cells[i];
        for (callbackIndex = 0,callbackCount = table._cellMoveCallbacks.length; callbackIndex < callbackCount; callbackIndex++)
          table._cellMoveCallbacks[callbackIndex](cell, row, cell._column);
      }
    };

    row._addRowMoveCallback(function(visibleRowsBefore) {
      reinitializeStyle(visibleRowsBefore);
      var columns = table._columns;
      var rowIndex = row._index;
      columns.forEach(function(column) {
        var bodyCells = column.body._cells;
        var cell = bodyCells[rowIndex];
        if (cell)
          cell._updateStyle();
      });

    });

    row._updateStyle = function() {
      var rowTable = this._table;
      var addedClassName = O$.combineClassNames([
        this._selected ? rowTable._selectionClass : null,
        this._mouseIsOver ? rowTable._params.body.rolloverRowClassName : null]);
      if (row._addedClassName == addedClassName)
        return;
      row._addedClassName = addedClassName;
      var opera = O$.isOpera();

      if (!rowTable._params.forceUsingCellStyles) {
        if (row._leftRowNode) O$.setStyleMappings(row._leftRowNode, {rolloverAndSelectionStyle: addedClassName});
        O$.setStyleMappings(row._rowNode, {rolloverAndSelectionStyle: addedClassName});
        if (row._rightRowNode) O$.setStyleMappings(row._rightRowNode, {rolloverAndSelectionStyle: addedClassName});
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
        for (i = 0,count = columns.length; i < count; i++) {
          col = columns[i];
          if ((col.body && col.body._getCompoundClassName()) || col._useCellStyles) {
            rowTable._needUsingCellStyles = true;
            break;
          }
        }
      }

      if (rowTable._params.forceUsingCellStyles || rowTable._needUsingCellStyles) {
        var cells = this._cells;
        for (i = 0,count = cells.length; i < count; i++) {
          var cell = cells[i];
          col = cell._column;
          if (!rowTable._params.forceUsingCellStyles && !(col.body && col.body._getCompoundClassName()) && !col._useCellStyles)
            continue;
          O$.setStyleMappings(cell, {rolloverAndSelectionStyle: addedClassName});
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
      var colIndex = column._index;
      var cellsByColumns = row._cellsByColumns;
      var cell = cellsByColumns[colIndex];
      while (!cell && colIndex >= 0) {
        cell = cellsByColumns[--colIndex];
      }
      if (!cell)
        throw "row._removeColumn: Can't find cell to remove: column._index = " + column._index;
      cell.parentNode.removeChild(cell);
      row._cells = row.cells;
      if (cell._column != column) {
        if (!(cell._colSpan > 1))
          throw "row._removeColumn: colSpan greater than 1 expected, but was: " + cell._colSpan;
        cell._colSpan--;
        cell.colSpan = cell._colSpan;
      }
      for (var i = colIndex + 1, count = cellsByColumns.length; i < count; i++) {
        var shiftedCell = cellsByColumns[i];
        cellsByColumns[i - 1] = shiftedCell;
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
      var nodes = O$.getChildNodesByClass(cell, "o_cellWrapper", true);
      if (nodes.length == 0) {
        cell._noCellWrapperFound = true;
        return false;
      }
      O$.assert(nodes.length == 1, "O$.Tables._updateCellWrapperStyle: no more than one cellwrapper expected, but found: " + nodes.length);
      cellWrapper = nodes[0];
      O$.assert(cellWrapper, "O$.Tables._updateCellWrapperStyle: non-null cellWrapper expected");
      cell._cellWrapper = cellWrapper;
    }
    var newWrapperClass = O$.combineClassNames([
      cell.className,
      cell._row.className,
      cell._row._table._params.additionalCellWrapperStyle]);
    if (cellWrapper.className != newWrapperClass)
      cellWrapper.className = newWrapperClass;
    cellWrapper.style.tableLayout = "fixed";
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

      function splitColorsStr(str) {
        // take care of possible rgb(r,g,b) color syntax (instead of #rrggbb)
        if (str.indexOf("(") == -1)
          return str.split(",");
        var parts = [];
        while (true) {
          var commaIdx = str.indexOf(",");
          var bracketIdx = str.indexOf("(");
          if (commaIdx == -1) {
            parts.push(str);
            return parts;
          }
          if (commaIdx < bracketIdx) {
            parts.push(str.substring(0, commaIdx));
          } else {
            var closingBracketIdx = str.indexOf(")");
            parts.push(str.substring(0, closingBracketIdx + 1));
            commaIdx = str.indexOf(",", closingBracketIdx);
            if (commaIdx == -1)
              return parts;
          }
          str = str.substring(commaIdx + 1);
        }
      }

      var verticalGridLines = table._gridLines.vertical ? splitColorsStr(table._gridLines.vertical) : [];
      var verticalGridLines_length = verticalGridLines.length;
      result.body = verticalGridLines_length
              ? verticalGridLines[index >= verticalGridLines_length ? verticalGridLines_length - 1 : index]
              : null;

      var headerVerticalGridLines = table._gridLines.headerVert ? splitColorsStr(table._gridLines.headerVert) : [];
      var headerVerticalGridLines_length = headerVerticalGridLines.length;
      result.header = headerVerticalGridLines_length
              ? headerVerticalGridLines[index >= headerVerticalGridLines_length ? headerVerticalGridLines_length - 1 : index]
              : null;

      var footerVerticalGridLines = table._gridLines.footerVert ? splitColorsStr(table._gridLines.footerVert) : [];
      var footerVerticalGridLines_length = footerVerticalGridLines.length;
      result.footer = footerVerticalGridLines_length
              ? footerVerticalGridLines[index >= footerVerticalGridLines_length ? footerVerticalGridLines_length - 1 : index]
              : null;

      var multiHeaderSeparators = table._gridLines.multiHeader ? splitColorsStr(table._gridLines.multiHeader) : [];
      var multiHeaderSeparators_length = multiHeaderSeparators.length;
      result.multiHeaderSeparator = multiHeaderSeparators_length
              ? multiHeaderSeparators[horizSeparationIndex >= multiHeaderSeparators_length ? multiHeaderSeparators_length - 1 : horizSeparationIndex]
              : null;

      var multiFooterSeparators = table._gridLines.multiFooter ? splitColorsStr(table._gridLines.multiFooter) : [];
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
    var headRows = tableHeader ? tableHeader._getRows() : [];
    if (headRows.length > 0) {
      tableHeader._updateCommonHeaderSeparator = function() {
        if (!(table._params.header && table._params.header.commonHeader))
          return;
        var commonHeaderRow = headRows[0];
        var commonHeaderCell = commonHeaderRow._cells[0];
        O$.Tables._setCellStyleProperty(commonHeaderCell, "borderBottom", table._gridLines.commonHeader);
      };

      function getLastRowCells(lastBeforeSubHeader) {
        var lastRowCells;
        if (table._params.header && table._params.header.subHeader && !lastBeforeSubHeader)
          lastRowCells = headRows[headRows.length - 1]._cells;
        else {
          lastRowCells = [];
          // This algorithm searches for the cells adjacent with the header section's bottom edge. It is simplified based
          // on the fact that all the vertically-spanned cells rendered on the server-side always extend to the bottom.
          for (var i = 0, count = headRows.length - (table._params.header && table._params.header.subHeader ? 1 : 0);
               i < count; i++) {
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
          O$.Tables._setCellStyleProperty(cell, "borderBottom", table._gridLines.headerHoriz != null
                  ? table._gridLines.headerHoriz
                  : table._gridLines.horizontal);
        }

      };
      tableHeader._updateSubHeaderRowSeparatorStyle = function() {
        if (!table._params.header || !table._params.header.subHeader)
          return;
        // it is essential to assign bottom border of a row above the sub-header rather than top border of the sub-header
        // row itself because otherwise IE will make a one-pixel space between vertical separators and this horizontal line
        var lastRowCells = getLastRowCells(true);
        for (var i = 0, count = lastRowCells.length; i < count; i++) {
          var cell = lastRowCells[i];
          O$.Tables._setCellStyleProperty(cell, "borderBottom", table._gridLines.subHeaderRow);
        }
      };

      tableHeader._updateColumnSeparatorStyles = function() {
        var subHeaderRow = table._params.header && table._params.header.subHeader ? headRows[table._subHeaderRowIndex] : null;
        var subHeaderRowCells = subHeaderRow ? subHeaderRow._cells : null;

        function setHeaderVerticalGridlines(parentGroup) {
          var separators = getSeparatorsForGroup(parentGroup);
          var verticalSeparator = !isEmptyLineStyle(separators.header) ? separators.header : separators.body;
          var columns = parentGroup == table ? table._params.columns : parentGroup.subColumns;
          for (var i = 0, count = columns.length; i < count; i++) {
            var col = columns[i];
            if (i < count - 1) {
              function setRightBorderForThisGroup(col) {
                var cell = col.header ? col.header._cell : null;
                if (cell) {
                  O$.Tables._setCellRightBorder(cell, verticalSeparator);
                  if (!col.subColumns && subHeaderRowCells)
                    O$.Tables._setCellRightBorder(subHeaderRowCells[col._index], verticalSeparator);
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
      tableHeader._updateSubHeaderRowSeparatorStyle();
      tableHeader._updateColumnSeparatorStyles();
      tableHeader._updateSeparatorStyle();
    }

    var tableFooter = table.footer;
    var footRows = tableFooter ? tableFooter._getRows() : [];
    if (footRows.length > 0) {
      tableFooter._updateSeparatorStyle = function() {
        if (footRows.length == 0)
          return;
        var row = footRows[0];
        var rowCells = row._cells;
        for (var i = 0, count = rowCells.length; i < count; i++) {
          var cell = rowCells[i];
          O$.Tables._setCellStyleProperty(cell, "borderTop", table._gridLines.footerHoriz != null
                  ? table._gridLines.footerHoriz
                  : table._gridLines.horizontal);
        }
      };

      tableFooter._updateCommonFooterSeparator = function() {
        if (!(table._params.footer && table._params.footer.commonHeader))
          return;
        var footerRows = table.footer._getRows();
        var commonFooterRow = footerRows[footerRows.length - 1];
        var commonFooterCell = commonFooterRow._cells[0];
        O$.Tables._setCellStyleProperty(commonFooterCell, "borderTop", table._gridLines.commonFooter);
      };

      tableFooter._updateColumnSeparatorStyles = function() {
        function setFooterVerticalGridlines(parentGroup) {
          var separators = getSeparatorsForGroup(parentGroup);
          var verticalSeparator = !isEmptyLineStyle(separators.footer) ? separators.footer : separators.body;
          var columns = parentGroup == table ? table._params.columns : parentGroup.subColumns;
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
    tableBody._getBorderBottomForCell = function(/*rowIndex, colIndex, cell*/) {
      return table._gridLines.horizontal;
    };
    function updateBodyCellBorders(cell, rowIndex, column, rowCount, colCount) {
      var correctedRowIndex = rowIndex + cell.rowSpan - 1;
      var displayGridLineUnderBottomRow = !!table._params.scrolling;
      var borderBottom = (correctedRowIndex < (displayGridLineUnderBottomRow ? rowCount : rowCount - 1))
              ? tableBody._getBorderBottomForCell(rowIndex, column._index, cell)
              : "0px none white";
      O$.Tables._setCellStyleProperty(cell, "borderBottom", borderBottom);
      if (column._index != colCount - 1) {
        var rightBorder = column.body._rightBorder;
        if (table.body._overriddenVerticalGridlines) {
          var overriddenGridlineStyle = table.body._overriddenVerticalGridlines[column._index];
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
        var columns = parentGroup == table ? table._params.columns : parentGroup.subColumns;
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

  },

  // -------------------------- TABLE COLUMNS SUPPORT

  /**
   *
   * The table._params.columns parameter defines a hierarchy of all table's columns with all their configuration
   * settings. It is an array of column specification objects. Each column specification object contains the following
   * properties:
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
  _initColumns: function(table) {
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
    }(null, table._params.columns, 1);
    table._deepestColumnHierarchyLevel = deepestColumnHierarchyLevel;

    table._columns = realColumns;
    var colCount = realColumns.length;

    var colTags = function() {
      var result;
      if (!table._params.scrolling) {
        result = O$.Tables._getTableColTags(table);
        for (var i = 0, count = result.length; i < count; i++) {
          var col = result[i];
          col._section = table.header || table.body;
          result[i] = [col];
        }
      } else {
        result = [];
        function addSectionCols(section) {
          if (!section) return;
          var colIndex = 0;

          function saveAreaCols(area) {
            if (!area) return;
            var cols = O$.getChildNodesWithNames(area._table, ["col"]);
            if (cols.length == 0) {
              var colGroup = O$.getChildNodesWithNames(area._table, ["colgroup"])[0];
              cols = colGroup ? O$.getChildNodesWithNames(colGroup, ["col"]) : [];
            }
            area._colTags = cols;
            for (var i = 0, count = cols.length; i < count; i++,colIndex++) {
              var arr = result[colIndex];
              if (!arr) {
                arr = [];
                result[colIndex] = arr;
              }
              var col = cols[i];
              col._section = section;
              arr.push(col);
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
    function newClass(declaration) {
      var className = O$.createCssClass(declaration, true);
      var cls = O$.findCssRule("." + className);
      return {className: className, classObj: cls};
    }
    column._headerCellsClass = newClass("overflow: hidden");
    column._bodyCellsClass = newClass("overflow: hidden");
    column._footerCellsClass = newClass("overflow: hidden");
    column._headerColClass = newClass("overflow: hidden");
    column._bodyColClass = newClass("overflow: hidden");
    column._footerColClass = newClass("overflow: hidden");

    column._table = table;

    if (column.header && column.header.pos) {
      var headerPos = column.header.pos;
      column.header._cell = table.header._cellByCoords(headerPos);
      O$.Tables._initHeaderCell(column.header._cell, column);
    }

    if (column.subHeader && column.subHeader.pos) {
      var subHeaderPos = column.subHeader.pos;
      column.subHeader._cell = table.header._cellByCoords(subHeaderPos);
      O$.Tables._initSubHeaderCell(column.subHeader._cell, column);
    }

    if (column.footer && column.footer.pos) {
      var columnFooterPos = column.footer.pos;
      column.footer._cell = table.footer._cellByCoords(columnFooterPos);
      O$.Tables._initFooterCell(column.footer._cell, column);
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
      if (headerCell) {
        O$.setStyleMappings(headerCell, {cellWidthClass: column._headerCellsClass.className});
        headerCell._updateStyle();
      }

      var subHeaderCell = column.subHeader ? column.subHeader._cell : null;
      if (subHeaderCell) {
        O$.setStyleMappings(subHeaderCell, {cellWidthClass: column._headerCellsClass.className});
        subHeaderCell._updateStyle();
      }

      var footerCell = column.footer ? column.footer._cell : null;
      if (footerCell) {
        footerCell._updateStyle();
        O$.setStyleMappings(footerCell, {cellWidthClass: column._footerCellsClass.className});
      }
    };
  },

  _initColumn: function(column, colTagArray, colIndex) {
    function initPublicClientAPI() {
      column.index = colIndex;
    }

    initPublicClientAPI();

    column._colTags = colTagArray;
    column._index = colIndex;

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

    column._colTags.forEach(function(colTag) {
      if (table.header && colTag._section == table.header)
        O$.setStyleMappings(colTag, {cellWidthClass: column._headerColClass.className});
      else if (colTag._section == table.body)
        O$.setStyleMappings(colTag, {cellWidthClass: column._bodyColClass.className});
      else if (table.footer && colTag._section == table.footer)
        O$.setStyleMappings(colTag, {cellWidthClass: column._footerColClass.className});
    });

    if (column._super_updateStyle)
      throw "O$.Tables._initColumn can be called only once per column";
    column._super_updateStyle = column._updateStyle;
    column._updateStyle = function() {
      column._super_updateStyle();

      column._useCellStyles = O$.Tables._getUseCellStylesForColumn(column);
      column._className = column._getCompoundClassName();
      var colTagClassName = !column._useCellStyles ? column._className : "";
      if (column._useCellStyles && O$.Tables._isFakeRowRequired() && column._className) {
        var width = O$.getStyleClassProperty(column._className, "width");
        // don't allow the column width to be ignore due to the fake row, e.g. in the 50px width in DayTable's time column
        if (width) {
          colTagClassName += " " + O$.createCssClass("width: " + width);
        }
      }
      column._colTags.forEach(function(colTag) {
        colTag.className = colTagClassName ? colTagClassName : "";
        colTag._column = column;
      });

      column._cellStyles = O$.Tables._prepareCellStylesForColStyleSimulation(column);

      var bodyCells = column.body ? column.body._cells : [];
      for (var bodyCellIndex = 0, cellCount = bodyCells.length; bodyCellIndex < cellCount; bodyCellIndex++) {
        var cell = bodyCells[bodyCellIndex];
        if (!cell) continue;
        cell._updateStyle();
        if (!cell.colSpan || cell.colSpan == 1)
          O$.setStyleMappings(cell, {cellWidthClass: column._bodyCellsClass.className});
      }

    };
    column.setWidth = function(width) {
      if (width < 0) width = 0;
      if (column._explicitWidth == width)
        return;
      column._explicitWidth = width;

      var tableWidth = null;
      function getTableWidth() {
        if (tableWidth == null)
          tableWidth = O$.getElementSize(table).width;
        return tableWidth;
      };
      function calculateWidthCorrection(cell) {
        if (cell._widthCorrection != undefined) return;
        cell._widthCorrection = !cell ? 0 :
                                  O$.getNumericElementStyle(cell, "padding-left", true, getTableWidth) +
                                  O$.getNumericElementStyle(cell, "padding-right", true, getTableWidth) +
                                  O$.getNumericElementStyle(cell, "border-left-width", true, getTableWidth) +
                                  O$.getNumericElementStyle(cell, "border-right-width", true, getTableWidth);
      }
      var headerCell = (column.header && column.header._cell) || (column.subHeader && column.subHeader._cell);
      var bodyCell = column.body._cells[0];
      var footerCell = column.footer && column.footer._cell;

      function setWidth(cellClass, colClass, cell, tableSection) {
        if (!cell) return;
        calculateWidthCorrection(cell);
        var widthForCell = width - cell._widthCorrection;
        if (widthForCell < 0) widthForCell = 0;
        var widthForCol = width;
        if (widthForCol < 0) widthForCol = 0;

        if (cellClass.style.setProperty) {
          cellClass.style.setProperty("width", widthForCell + "px", "important");
          colClass.style.setProperty("width", widthForCol + "px", "important");
        } else {
          cellClass.style.width = widthForCell + "px";
          colClass.style.width = widthForCol + "px";
        }

        var colTag = null;
        column._colTags.forEach(function(col) {
          if (col._section == tableSection)
            colTag = col;
        });
        if (colTag) O$.setElementWidth(colTag, widthForCol, getTableWidth);
      }
      setWidth(this._headerCellsClass.classObj, this._headerColClass.classObj, headerCell, table.header);
      setWidth(this._bodyCellsClass.classObj, this._bodyColClass.classObj, bodyCell, table.body);
      setWidth(this._footerCellsClass.classObj, this._footerColClass.classObj, footerCell, table.footer);

    };
    column.getDeclaredWidth = function(tableWidth) {
      if (tableWidth == undefined)
        tableWidth = table.offsetWidth;
      if (table._verticalBordersWidth == undefined) {
        table._verticalBordersWidth = O$.getNumericElementStyle(table, "border-left-width", true) +
                                      O$.getNumericElementStyle(table, "border-right-width", true);
      }
      tableWidth -= table._verticalBordersWidth;
      var widthStyleStr = O$.getStyleClassProperty(this._className, "width");
      var colWidth = O$.calculateNumericCSSValue(widthStyleStr, tableWidth);
      if (widthStyleStr && widthStyleStr.indexOf("%") != -1)
        this._relativeWidth = true;
      if (!colWidth) {
        var widthStr = this._colTags[0].width;
        if (widthStr && widthStr.indexOf("%") != -1)
          this._relativeWidth = true;

        colWidth = O$.calculateNumericCSSValue(widthStr, tableWidth);
      }
      return colWidth;
    };
    column.getWidth = function() {
      if (this.header && this.header._cell)
        return this.header._cell.offsetWidth;
      if (this.subHeader && this.subHeader._cell)
        return this.subHeader._cell.offsetWidth;
      for (var idx = 0, count = this.body._cells.length; idx < count; idx++) {
        var cell = this.body._cells[idx];
        if (!cell || cell.colSpan > 1)
          continue;
        return cell.offsetWidth;
      }
      if (this.footer && this.footer._cell)
        return this.footer._cell.offsetWidth;
      return this._colTags[0].offsetWidth;
    };

  },

  _initHeaderCell: function(cell, column) {
    cell._column = column;

    cell._updateStyle = function() {
      O$.Tables._applySimulatedColStylesToCell(cell);

      var column = cell._column;
      O$.Tables._setCellStyleMappings(cell, {
        compoundColumnStyle: column._getCompoundClassName(),
        colHeaderStyle: column.header ? column.header.className : null});
    };

  },

  _initSubHeaderCell: function(cell, column) {
    cell._column = column;

    cell._updateStyle = function() {
      O$.Tables._applySimulatedColStylesToCell(cell);

      var column = cell._column;
      O$.Tables._setCellStyleMappings(cell, {
        compoundColumnStyle: column._getCompoundClassName(),
        colSubHeaderStyle: column.subHeader ? column.subHeader.className : null
      });
    };

  },

  _initFooterCell: function(cell, column) {
    cell._column = column;

    cell._updateStyle = function() {
      O$.Tables._applySimulatedColStylesToCell(cell);

      var column = cell._column;
      O$.Tables._setCellStyleMappings(cell, {
        compoundColumnStyle: column._getCompoundClassName(),
        colFooterStyle: column.footer ? column.footer.className : null
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
      var colIndex = column._index;

      if (!table._params.body.noDataRows) {
        O$.Tables._applySimulatedColStylesToCell(cell);
      }

      var cellStyleMappings = {};
      if (table._params.forceUsingCellStyles || column._useCellStyles) {
        cellStyleMappings.rowInitialClass = row.initialClass;
        cellStyleMappings.rowIndividualClass = table._params.rowStylesMap[row._index];
      }

      if (table._params.body.noDataRows) {
        O$.Tables._assignNoDataCellStyle(cell);
      } else {
        var cellKey = rowIndex + "x" + colIndex;
        cellStyleMappings.individualCellStyle = table._params.cellStylesMap[cellKey];

        var columnClassName = column._useCellStyles ? column._getCompoundClassName() : null;
        if (columnClassName)
          cellStyleMappings.columnClass = columnClassName;
        var compoundBodyClassName = column.body._getCompoundClassName();
        if (compoundBodyClassName) {
          cellStyleMappings.rowInitialClass = row.initialClass;
          cellStyleMappings.columnBodyClass = compoundBodyClassName;
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
    if (table._params.forceUsingCellStyles || column._forceUsingCellStyles)
      return true;
    var thisIsColGroup = column._subColumns;
    if (thisIsColGroup)
      return true;

    var useCellStylesToAvoidApplyingFirstColStyleToCommonHeader =
            !table._params.scrolling && // this issue is not applicable to scrollable tables
            column._index == 0 && (
                    (table._params.header && table._params.header.commonHeader) ||
                    (table._params.footer && table._params.footer.commonHeader)
                    );

    // cell styles should be used instead of col tag style for first column if there is a no-data message row,
    // otherwise first col style will be applied to the no-data message row, which shouldn't be the case (JSFC-2890)
    var useFirstColumnCellStylesWhenNoData = table._params.body.noDataRows && column._index == 0;

    return useCellStylesToAvoidApplyingFirstColStyleToCommonHeader || useFirstColumnCellStylesWhenNoData;
  },

  _assignNoDataCellStyle: function(cell) {
    var column = cell._column;
    var row = cell._row;
    var table = row._table;
    if (!(table._params.forceUsingCellStyles || column._useCellStyles)) {
      // first column style shouldn't be applied to the no-data-row
      // no-data row should derive unspecified properties from body section instead
      var noDataRowClass = table._params.rowStylesMap[0];
      var noDataRowStyle = noDataRowClass
              ? O$.Tables._evaluateStyleClassProperties_cached(noDataRowClass, ["backgroundColor", "background"], table)
              : {};
      if (!noDataRowStyle.backgroundColor && !noDataRowStyle.background) {
        var bodySection = O$.getChildNodesWithNames(table, ["tbody"])[0];
        var propertyValue = O$.getElementStyle(bodySection, "background-color");
        if (!propertyValue || propertyValue == "transparent") {
          propertyValue = O$.getElementStyle(table, "background-color");
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

        if (!column.subColumns && column.body && !table._params.body.noDataRows) {
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

    assignColEvents(table._params.columns, [], []);

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

    assignColStyles(table._params.columns);
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
      colProperties = ["width", "textAlign", "fontFamily", "fontSize", "borderLeft", "borderRight"];
    } else
      colProperties = [];
    var colStyleProperties = O$.Tables._evaluateStyleClassProperties_cached(colTagClassName, colProperties, table);
    if (isMozilla || isOpera) {
      if (!cellStyles) cellStyles = {};
      cellStyles.width = colStyleProperties.width;
      cellStyles.textAlign = colStyleProperties.textAlign;
      var values = O$.getElementStyle(colTag, ["vertical-align", "line-height", "padding-left", "padding-right", "padding-top", "padding-bottom"]);
      cellStyles.verticalAlign = O$.Tables._getUserStylePropertyValue(values, "vertical-align", "baseline", "auto");
      cellStyles.lineHeight = O$.Tables._getUserClassPropertyValue(colTag, "line-height", "normal");

      cellStyles.paddingLeft = O$.Tables._getUserStylePropertyValue(values, "padding-left", "0px");
      cellStyles.paddingRight = O$.Tables._getUserStylePropertyValue(values, "padding-right", "0px");
      cellStyles.paddingTop = O$.Tables._getUserStylePropertyValue(values, "padding-top", "0px");
      cellStyles.paddingBottom = O$.Tables._getUserStylePropertyValue(values, "padding-bottom", "0px");

      cellStyles.color = colStyleProperties.color;
      cellStyles.fontWeight = colStyleProperties.fontWeight;
      cellStyles.fontStyle = colStyleProperties.fontStyle;
    }
    if (isMozilla || isExplorer) {
      if (!cellStyles) cellStyles = {};
      cellStyles.fontFamily = colStyleProperties.fontFamily;
      cellStyles.fontSize = colStyleProperties.fontSize;
      cellStyles.borderLeft = colStyleProperties.borderLeft;
      cellStyles.borderRight = colStyleProperties.borderRight;
    }
    if (isExplorer) {
      if (!O$.isExplorer8() && O$.isStrictMode() &&
          colStyleProperties.width && colStyleProperties.width.indexOf("%") == -1) {
        column._colTags.forEach(function(colTag) {
          O$.setElementWidth(colTag, O$.calculateNumericCSSValue(colStyleProperties.width));
        });
        cellStyles.width = column._colTags[0].width;
      }
      if (O$.isExplorer8() && O$.isStrictMode())
        cellStyles.textAlign = colStyleProperties.textAlign;
    }
    if (cellStyles) {
      cellStyles._names = [];
      for (var pn in cellStyles) {
        if (cellStyles[pn])
          cellStyles._names.push(pn);
      }
    }
    if (cellStyles && cellStyles.color) {
      // use cell styles for this column to solve color CSS attribute precedence issue in Mozilla (JSFC-2823)
      column._useCellStyles = true;
      if (column.body)
        O$.setStyleMappings(column.body, {colTagClassName: colTagClassName});
      cellStyles = null;
    } else {
      if (cellStyles._names.length == 0)
        cellStyles = null;
    }

    return cellStyles;
  },

  _clearCellStyleProperties: function(cell, propertyNames) {
    if (!cell)
      return;

    for (var i = 0, count = propertyNames.length; i < count; i++) {
      var propertyName = propertyNames[i];
      cell.style[propertyName] = "";
    }

  },

  _setCellStyleProperty: function(cell, propertyName, propertyValue) {
    if (!cell || !propertyValue)
      return;

    try {
      propertyName = O$._capitalizeCssPropertyName(propertyName);
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
      var simulatedProperties = cellStyles._names;
      simulatedProperties.forEach(function (property) {
        if (property == "_names") return;
        O$.Tables._setCellStyleProperty(cell, property, cellStyles[property]);
      });
      cell._simulatedColStylesApplied = simulatedProperties;
    } else {
      if (cell._simulatedColStylesApplied) {
        O$.Tables._clearCellStyleProperties(cell, cell._simulatedColStylesApplied);
        cell._simulatedColStylesApplied = null;
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
  },

  // -------------------------- TABLE SCROLLING

  _initScrolling: function(table) {
    var scrolling = table._params.scrolling;
    if (!scrolling)
      throw "O$.Tables._initScrolling can't be invoked on a non-scrollable table";

    var delayedInitFunctions = [];
    var mainScrollingArea = table.body._centerScrollingArea;

    function useWidthAttribute() {
      // .o_scrollable_table CSS class has width: 100%
      var explicitWidth = table.getAttribute("width");
      if (explicitWidth) {
        var intValue = parseInt(explicitWidth);
        if (!isNaN(intValue))
          explicitWidth = intValue + "px";
        table.style.width = explicitWidth;
      }
    }
    useWidthAttribute();

    function alignColumnWidths() {
      var firstSection = table.header ? table.header : table.body;
      var scrollingAreaColTags = [];
      [table.header, table.body, table.footer].forEach(function (area) {
        if (area && area._sectionTable)
          scrollingAreaColTags.push(O$.Tables._getTableColTags(area._sectionTable));
      });
      function areaWidthByColumns(section, areaName, verticalAreaForInitialization) {

        var scrollerWidth = O$.isExplorer() || /* explorer reports too big values*/
                            O$.isMozillaFF() /* mozilla reports 0 in some cases (see TreeTable demo 1) */
                ? 17
                : mainScrollingArea._scrollingDiv.offsetWidth - mainScrollingArea._scrollingDiv.clientWidth;
        var area = section[areaName];
        var areaWidth = O$.Tables._alignTableColumns(
                area._colTags, table, firstInitialization,
                verticalAreaForInitialization, scrolling, scrollerWidth);
        verticalAreaForInitialization._columns.forEach(function(c) {
          c._verticalArea = verticalAreaForInitialization;
        });
        if (areaWidth < 0) areaWidth = 0;
        var width = areaWidth + "px";
        verticalAreaForInitialization._areas = [];
        [table.header, table.body, table.footer].forEach(function (section) {
          if (!section) return;
          var a = section[areaName];
          verticalAreaForInitialization._areas.push(a);
          var areaTable = a._table;
          areaTable.style.width = width;
        });
        return width;
      }

      if (firstSection._leftScrollingArea) {
        table._leftArea = {
          updateWidth: function() {
            var width = areaWidthByColumns(firstSection, "_leftScrollingArea", table._leftArea);
            var areaIndex = firstSection._leftScrollingArea._horizontalIndex;
            scrollingAreaColTags.forEach(function (tagsForSection) {
              tagsForSection[areaIndex].style.width = width;
            });
          }
        };
      }
      table._centerArea = {
        updateWidth: function() {
          var width = areaWidthByColumns(firstSection, "_centerScrollingArea", table._centerArea);
          if (!scrolling.horizontal) {
            var areaIndex = firstSection._centerScrollingArea._horizontalIndex;
            scrollingAreaColTags.forEach(function (tagsForSection) {
              tagsForSection[areaIndex].style.width = width;
            });

          }
        }
      };
      if (firstSection._rightScrollingArea) {
        table._rightArea = {
          updateWidth: function() {
            var width = areaWidthByColumns(firstSection, "_rightScrollingArea", table._rightArea);
            var areaIndex = firstSection._rightScrollingArea._horizontalIndex;
            scrollingAreaColTags.forEach(function (tagsForSection) {
              tagsForSection[areaIndex].style.width = width;
            });
          }
        };
      }
      var firstInitialization = true;
      if (table._leftArea)
        table._leftArea.updateWidth();
      table._centerArea.updateWidth();
      if (table._rightArea)
        table._rightArea.updateWidth();
      firstInitialization = false;

      if (!scrolling.horizontal) {
        var firstUpdate = true;
        O$.listenProperty(table, "width", function(/*width*/) {
          if (firstUpdate) {
            // already updated during inintialization
            firstUpdate = false;
            return;
          }
          table._centerArea.updateWidth();
        });
      }
      if (O$.isChrome() || O$.isSafari()) {
        // fix Chrome/Safari not respecting table-layout="fixed" in _some_ cases
        [table.header, table.body, table.footer].forEach(function (section) {
          if (!section) return;
          section._scrollingAreas.forEach(function(a) {
            if (!a) return;
            var areaTable = a._table;
            areaTable.style.tableLayout = "auto";
            setTimeout(function() {
              areaTable.style.tableLayout = "fixed";
            }, 10);
          });
        });
      }
    }

    alignColumnWidths();
    if (table._relayoutRelativeWidthColumns)
      setTimeout(alignColumnWidths, 100);

    function fixBodyHeight() {
      var fixture = O$.fixElement(table.body._sectionTable, {
        height: function() {
          var height = O$.getElementPaddingRectangle(table).height;
          [table.header, table.footer].forEach(function (section) {
            if (!section) return;
            var sectionTable = section._sectionTable;
            var sectionHeight = sectionTable.offsetHeight;
            height -= sectionHeight;
          });
          return height;
        }
      }, null, {onchange: function() {
        var fixture = this;
        [table.body._leftScrollingArea, table.body._centerScrollingArea, table.body._rightScrollingArea].forEach(function (scrollingArea) {
          if (!scrollingArea || !scrollingArea._scrollingDiv) return;
          var height = fixture.values.height;
          O$.setElementHeight(scrollingArea._scrollingDiv, height);
        });
      }});
      [table.body._leftScrollingArea, table.body._centerScrollingArea, table.body._rightScrollingArea].forEach(function (area) {
        if (!area) return;
        if (area._scrollingDivContainer && area._scrollingDivContainer.style.display == "none")
          delayedInitFunctions.push(function() {
            area._scrollingDivContainer.style.display = "block";
            fixture.update();
          });
      });
    }

    fixBodyHeight();


    function synchronizeAreaScrolling() {
      mainScrollingArea._scrollingDiv.onscroll = function(e) {
        O$.setHiddenField(table, table.id + "::scrollPos",
                "[" + mainScrollingArea._scrollingDiv.scrollLeft + "," + mainScrollingArea._scrollingDiv.scrollTop + "]");
        [table.header, table.footer].forEach(function (section) {
          if (!section || !section._centerScrollingArea) return;
          section._centerScrollingArea._scrollingDiv.scrollLeft = mainScrollingArea._scrollingDiv.scrollLeft;
        });
        [table.body._leftScrollingArea, table.body._rightScrollingArea].forEach(function (area) {
          if (!area) return;
          area._scrollingDiv.scrollTop = mainScrollingArea._scrollingDiv.scrollTop;
        });
        if (table.onscroll)
          table.onscroll(e);
      };
      var synchronizationCorrectionInterval = setInterval(function() {
        if (!O$.isElementPresentInDocument(table)) {
          clearInterval(synchronizationCorrectionInterval);
          return;
        }
        var correctScrollLeft = mainScrollingArea._scrollingDiv.scrollLeft;
        [table.header, table.footer].forEach(function (section) {
          if (!section || !section._centerScrollingArea) return;
          var scrollingDiv = section._centerScrollingArea._scrollingDiv;
          if (scrollingDiv.scrollLeft != correctScrollLeft)
            scrollingDiv.scrollLeft = correctScrollLeft;
        });
      }, 250);
    }

    synchronizeAreaScrolling();

    table._alignRowHeights = function() {
      if (!table._leftArea && !table._rightArea)
        return;
      [table.header, table.body, table.footer].forEach(function(section) {
        if (!section) return;
        var assignCellHeights = O$.isChrome() || O$.isSafari() || (O$.isExplorer() && O$.isStrictMode() && !O$.isExplorer8());

        var areaHeight = 0;
        var rows = section._getRows();
        rows.forEach(function (row) {
          var artificialRow = !row.nodeName || row.nodeName.toUpperCase() != "TR";
          if (!artificialRow) return;
          var rowNodes = [row._leftRowNode, row._rowNode, row._rightRowNode];

          function setRowHeight(rowNode, height) {
            row.__height = height;
            rowNode.style.height = height + "px";
            if (assignCellHeights && !height) {
              for (var i = 0, count = rowNode.cells.length; i < count; i++) {
                rowNode.cells[i].style.height = "0";
              }
            }
          }

          var height = 0;
          rowNodes.forEach(function(rowNode) {
            if (!rowNode) return;
            if (!(O$.isExplorer() && O$.isQuirksMode()))
              setRowHeight(rowNode, 0);
            var rowHeight = rowNode.offsetHeight;
            if (rowHeight > height)
              height = rowHeight;
          });
          rowNodes.forEach(function(rowNode) {
            if (!rowNode) return;
            setRowHeight(rowNode, height);
          });
          areaHeight += height;
        });
        if ((O$.isChrome() || O$.isSafari()) && O$.isQuirksMode())
          section._scrollingAreas.forEach(function(area) {
            O$.setElementHeight(area._table, areaHeight);
          });


        if (assignCellHeights) {
          // setting row height is not enough for Chrome and Safari, setting cell heights solves this issue
          for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
            var row = rows[rowIndex];
            var artificialRow = !row.nodeName || row.nodeName.toUpperCase() != "TR";
            if (!artificialRow) continue;
            row._cells.forEach(function (cell) {
              var rowSpan = cell.rowSpan ? cell.rowSpan : 1;
              var cellHeight = 0;
              for (var i = rowIndex; i < rowIndex + rowSpan; i++) {
                cellHeight += rows[i].__height;
              }
              O$.setElementHeight(cell, cellHeight);
            });

          }
        }


      });
    };
    var delayUnderIE = O$.isExplorer() && !(O$.isExplorer8() && O$.isStrictMode());
    if (!delayUnderIE)
      table._alignRowHeights();
    else
      delayedInitFunctions.push(function() {
        setTimeout(table._alignRowHeights, 100);
      });


    function fixIE6AreaDisappearing() {
      if (!O$.isExplorer6()) return;

      mainScrollingArea._scrollingDiv.onresize = function() {
      };
    }

    fixIE6AreaDisappearing();


    function accountForScrollersWidth() {
      [table.header, table.footer].map(function(s) {
        return s && s._centerScrollingArea;
      }).forEach(function(area) {
        if (!area) return;
        O$.setElementSize(area._spacer, {width: 30, height: 1});
      });
      [table.body._leftScrollingArea, table.body._rightScrollingArea].forEach(function(area) {
        if (!area) return;
        O$.setElementSize(area._spacer, {width: 1, height: 30});
      });
    }

    accountForScrollersWidth();

    function scrollToPosition() {
      var scrollPos = scrolling.position;
      mainScrollingArea._scrollingDiv.scrollLeft = scrollPos[0];
      mainScrollingArea._scrollingDiv.scrollTop = scrollPos[1];
    }

    scrollToPosition();

    function markColumns() {
      [table._leftArea, table._centerArea, table._rightArea].forEach(function(area) {
        if (!area) return;
        area._columns.forEach(function(c) {
          do {
            c._scrollingArea = area;
            c = c.parentColumn;
          } while (c);
        });
      });
    }

    markColumns();

    function fixTopLevelScrollingDivWidth() {
      O$.listenProperty(table, "width", function(width) {
        width -= O$.getNumericElementStyle(table, "border-left-width", true);
        width -= O$.getNumericElementStyle(table, "border-right-width", true);
        if (width < 0)
          width = 0;
        table._topLevelScrollingDiv.style.width = width + "px";
        [table.header, table.body, table.footer].forEach(function (section) {
          if (section && section._sectionTable)
            section._sectionTable.style.width = width + "px";
        });
      }, [
        new O$.Timer("200"),
        new O$.EventListener(window, "resize")
      ]);
      table._topLevelScrollingDiv.style.visibility = "visible";
    }

    fixTopLevelScrollingDivWidth();

    if (delayedInitFunctions.length)
      O$.addLoadEvent(function() {
        delayedInitFunctions.forEach(function (func) {
          func();
        });
      });

  },

  _alignTableColumns: function (colTags, table, firstInitialization, objectForSavingParams, scrolling, scrollerWidth) {
    if (!scrollerWidth) scrollerWidth = 0;
    var defaultColWidth = 120;

    var tblWidth = 0;
    var tableWidth = table.offsetWidth - scrollerWidth;
    tableWidth -= O$.getNumericElementStyle(table, "border-left-width", true);
    tableWidth -= O$.getNumericElementStyle(table, "border-right-width", true);
    if (tableWidth < 0)
      tableWidth = 0;
    var columns = [];
    if (objectForSavingParams)
      objectForSavingParams._columns = columns;
    var relativeWidthColumns = [];
    var thereAreRelativeWidthColumns = false;
    var nonRelativeWidth = 0;
    colTags.forEach(function(colTag) {
      var column = colTag._column;
      columns.push(column);
      var colWidth = firstInitialization ? column.getDeclaredWidth(tableWidth) : column._explicitWidth;
      if (firstInitialization) {
        if (!colWidth) {
          column._widthNotSpecified = true;
          colWidth = defaultColWidth;
        }
        column._tempWidth = colWidth;
      }
      if (!scrolling || !scrolling.horizontal) {
        if (column._relativeWidth || column._widthNotSpecified)
          relativeWidthColumns.push(column);
        else
          nonRelativeWidth += colWidth;
      } else {
        if (column._relativeWidth) thereAreRelativeWidthColumns = true;
      }

      tblWidth += colWidth;
    });
    if (thereAreRelativeWidthColumns && tableWidth == 0 && (O$.isExplorer() && !(O$.isExplorer8() && O$.isStrictMode()))) {
      // table width is not know in the initialization stage under IE (except IE8/strict) so postponed relayout is
      // required to calculate relative width columns properly
      table._relayoutRelativeWidthColumns = true;
    }
    if ((
            !scrolling || (!scrolling.horizontal && !scrolling._widthAlignmentDisabled)
            ) && (! (O$.isExplorer() && !tableWidth))) {
      var remainingWidth = tableWidth - nonRelativeWidth;
      if (remainingWidth > 0 && relativeWidthColumns.length > 0) {
        var unspecifiedWidthColCount = 0;
        var totalSpecifiedWidth = 0;
        relativeWidthColumns.forEach(function(c) {
          if (!c._widthNotSpecified) {
            var relativeWidth = c.getDeclaredWidth(10000) / 100;
            c._tempWidth = relativeWidth;
            totalSpecifiedWidth += relativeWidth;
          } else
            unspecifiedWidthColCount++;
        });
        var unspecifiedWidth = 100 - totalSpecifiedWidth;
        var widthPerUnspecifiedWidthCol = unspecifiedWidthColCount ? (
                unspecifiedWidth > 0
                        ? unspecifiedWidth / unspecifiedWidthColCount
                        : 20 / unspecifiedWidthColCount ) : 0;
        var relativeTotal = totalSpecifiedWidth + widthPerUnspecifiedWidthCol * unspecifiedWidthColCount;
        relativeWidthColumns.forEach(function(c) {
          c.setWidth(Math.floor(
                  (c._widthNotSpecified ? widthPerUnspecifiedWidthCol : c._tempWidth)
                          / relativeTotal * remainingWidth
                  ));
        });
      } else {
        columns.forEach(function(c) {
          var width = firstInitialization ? c._tempWidth : c._explicitWidth;
          c.setWidth(Math.floor((width / tblWidth) * tableWidth));
        });
      }
      tblWidth = tableWidth;
    }
    if (firstInitialization) {
      columns.forEach(function(column) {
        if (!column._explicitWidth) column.setWidth(column._tempWidth);
      });
    }
    return tblWidth;
  }



};

