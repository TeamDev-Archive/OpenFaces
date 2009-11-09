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
package org.openfaces.renderkit.table;

import org.openfaces.component.TableStyles;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Scrolling;
import org.openfaces.component.table.TableColumn;
import org.openfaces.util.RenderingUtil;
import org.openfaces.renderkit.TableUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TableHeaderOrFooter extends TableElement {
    private boolean isHeader;

    private HeaderRow commonHeaderRow;
    private List<HeaderRow> allRows;
    private boolean hasSubHeader;
    private int lastVisibleColHeadersRow = -1;

    protected TableHeaderOrFooter(TableStructure tableStructure, boolean composeHeader) {
        super(tableStructure);
        isHeader = composeHeader;

        boolean applyDefaultStyle = tableStructure.getTableStyles().getApplyDefaultStyle();
        String cellTag = isHeader
                ? (applyDefaultStyle ? "td" : "th")
                : "td";

        allRows = new ArrayList<HeaderRow>();
        List<BaseColumn> columns = tableStructure.getColumns();
        commonHeaderRow = composeCommonHeaderRow(tableStructure, cellTag);
        if (tableStructure.getScrolling() == null) {
            composeNonScrollingContent(tableStructure, cellTag, columns);
        } else {
            composeScrollingContent(tableStructure, cellTag, columns);
        }
    }

    private void composeScrollingContent(TableStructure tableStructure, String cellTag, List<BaseColumn> columns) {
        List<HeaderCell> cells = new ArrayList<HeaderCell>();
        int leftFixedCols = tableStructure.getLeftFixedCols();
        int rightFixedCols = tableStructure.getRightFixedCols();
        int totalColCount = columns.size();

        if (leftFixedCols > 0) {
            List<BaseColumn> leftCols = columns.subList(0, leftFixedCols);
            cells.add(scrollingAreaCell(tableStructure, cellTag, leftCols, false));
        }

        List<BaseColumn> centerCols = columns.subList(leftFixedCols, totalColCount - rightFixedCols);
        cells.add(scrollingAreaCell(tableStructure, cellTag, centerCols, true));

        if (rightFixedCols > 0) {
            List<BaseColumn> rightCols = columns.subList(totalColCount - rightFixedCols, totalColCount);
            cells.add(scrollingAreaCell(tableStructure, cellTag, rightCols, false));
        }
        allRows.add(new HeaderRow(this, true, cells));
        if (commonHeaderRow != null)
            allRows.add(isHeader ? 0 : allRows.size(), commonHeaderRow);
    }

    private HeaderCell scrollingAreaCell(
            TableStructure tableStructure,
            String cellTag,
            List<BaseColumn> columns,
            boolean scrollable
    ) {
        List<HeaderRow> rows = composeColumnHeaderRows(tableStructure, columns, cellTag);
        for (int i = 0; i < rows.size(); i++) {
            HeaderRow row = rows.get(i);
            if (row.isAtLeastOneComponentInThisRow())
                lastVisibleColHeadersRow = i;
        }
        HeaderRow subHeaderRow = composeSubHeaderRow(tableStructure, columns, cellTag);
        if (subHeaderRow != null) {
            rows.add(isHeader ? rows.size() : 0, subHeaderRow);
            hasSubHeader = true;
        }
        TableScrollingArea tableScrollingArea = new TableScrollingArea(this, columns, rows, scrollable);
        return new HeaderCell(null, tableScrollingArea, "td");
    }

    private void composeNonScrollingContent(TableStructure tableStructure, String cellTag, List<BaseColumn> columns) {
        List<HeaderRow> columnHeaderRows = composeColumnHeaderRows(tableStructure, columns, cellTag);
        for (int i = 0; i < columnHeaderRows.size(); i++) {
            HeaderRow row = columnHeaderRows.get(i);
            if (row.isAtLeastOneComponentInThisRow())
                lastVisibleColHeadersRow = i;
        }
        HeaderRow subHeaderRow = composeSubHeaderRow(tableStructure, columns, "td");
        hasSubHeader = subHeaderRow != null;
        if (!isHeader) {
            if (subHeaderRow != null)
                allRows.add(subHeaderRow);
        } else {
            if (commonHeaderRow != null)
                allRows.add(commonHeaderRow);
        }

        for (int i = 0, count = columnHeaderRows.size(); i < count; i++) {
            HeaderRow row = columnHeaderRows.get(i);
            allRows.add(row);
        }

        if (isHeader) {
            if (subHeaderRow != null)
                allRows.add(subHeaderRow);
        } else {
            if (commonHeaderRow != null)
                allRows.add(commonHeaderRow);
        }
    }

    public static int composeColumnHierarchies(List<BaseColumn> columns, List[] columnHierarchies, boolean isHeader, boolean skipEmptyHeaders) {
        int columnHierarchyLevels = 0;
        for (int colIndex = 0, colCount = columns.size(); colIndex < colCount; colIndex++) {
            List<BaseColumn> columnHierarchy = new ArrayList<BaseColumn>();
            BaseColumn startingColumn = columns.get(colIndex);
            for (BaseColumn column = startingColumn;
                 column != null;
                 column = column.getParent() instanceof BaseColumn ? (BaseColumn) column.getParent() : null) {
                if (skipEmptyHeaders && column != startingColumn) {
                    UIComponent header = isHeader ? column.getHeader() : column.getFooter();
                    if (header == null)
                        continue;
                }
                columnHierarchy.add(0, column);
            }
            columnHierarchies[colIndex] = columnHierarchy;
            columnHierarchyLevels = Math.max(columnHierarchyLevels, columnHierarchy.size());
        }
        return columnHierarchyLevels;
    }

    private HeaderRow composeCommonHeaderRow(TableStructure tableStructure, String cellTag) {
        List columns = tableStructure.getColumns();
        TableStyles table = tableStructure.getTableStyles();
        UIComponent headerOrFooter = isHeader ? table.getHeader() : table.getFooter();
        if (headerOrFooter == null)
            return null;
        HeaderCell cell = new HeaderCell(null, headerOrFooter, cellTag);

        int colSpan;
        Scrolling scrolling = tableStructure.getScrolling();
        if (scrolling == null)
            colSpan = columns.size();
        else {
            colSpan = 1;
            if (tableStructure.getLeftFixedCols() > 0)
                colSpan++;
            if (tableStructure.getRightFixedCols() > 0)
                colSpan++;
        }
        cell.setSpans(colSpan, 0, 0);
        return new HeaderRow(this, true, Collections.singletonList(cell));
    }

    private HeaderRow composeSubHeaderRow(TableStructure tableStructure, List<BaseColumn> columns, String cellTag) {
        boolean atLeastOneComponent = false;
        List<HeaderCell> cells = new ArrayList<HeaderCell>();

        for (BaseColumn column : columns) {
            UIComponent columnHeaderOrFooter =
                    (isHeader ? getColumnSubHeader(column) : null);
            HeaderCell cell = new HeaderCell(column, columnHeaderOrFooter, cellTag);
            if (columnHeaderOrFooter != null)
                atLeastOneComponent = true;
            cell.setSpans(1, 0, 0);
            cells.add(cell);
        }
        if (!atLeastOneComponent)
            return null;
        return new HeaderRow(this, true, cells);
    }

    private List<HeaderRow> composeColumnHeaderRows(
            TableStructure tableStructure, List<BaseColumn> columns,
            String cellTag) {
        int colCount = columns.size();
        List[] columnHierarchies = new ArrayList[colCount];
        int columnHierarchyLevels = composeColumnHierarchies(columns, columnHierarchies, isHeader, true);

        List<HeaderRow> rows = new ArrayList<HeaderRow>();
        for (int rowIndex = 0; rowIndex < columnHierarchyLevels; rowIndex++) {
            List<HeaderCell> rowCells = new ArrayList<HeaderCell>();
            boolean atLeastOneComponentInThisRow = false;
            for (int colIndex = 0; colIndex < colCount; colIndex++) {
                HeaderCell cell = createCell(cellTag, columnHierarchies, columnHierarchyLevels, rowIndex, colIndex);
                if (cell == null)
                    continue;
                rowCells.add(cell);
                if (cell.getComponent() != null)
                    atLeastOneComponentInThisRow = true;
                colIndex += cell.getColSpan() - 1;
            }
            HeaderRow row = new HeaderRow(this, atLeastOneComponentInThisRow, rowCells);
            row.setRowsForSpans(rows);
            if (isHeader)
                rows.add(row);
            else
                rows.add(0, row);
        }
        return rows;
    }

    private HeaderCell createCell(String cellTag,
                                  List[] columnHierarchies, int columnHierarchyLevels,
                                  int rowIndex, int colIndex) {
        List columnHierarchy = columnHierarchies[colIndex];
        int thisHierarchySize = columnHierarchy.size();

        boolean firstFooterRow = !isHeader && rowIndex == columnHierarchyLevels - 1;
        if (isHeader) {
            if (rowIndex >= thisHierarchySize)
                return null;
        } else {
            if (!firstFooterRow && rowIndex >= thisHierarchySize - 1)
                return null;
        }

        BaseColumn col = (BaseColumn) columnHierarchy.get(firstFooterRow ? thisHierarchySize - 1 : rowIndex);
        int colSpan = 1;
        for (int nextColIndex = colIndex + 1, colCount = columnHierarchies.length; nextColIndex < colCount; nextColIndex++) {
            List nextColumnHierarchy = columnHierarchies[nextColIndex];
            if (rowIndex >= nextColumnHierarchy.size())
                break;
            BaseColumn nextCol = (BaseColumn) nextColumnHierarchy.get(rowIndex);
            if (nextCol != col)
                break;
            colSpan++;
        }

        HeaderCell cell = new HeaderCell(col, isHeader ? col.getHeader() : col.getFooter(), cellTag, isHeader, isHeader);
        if (isHeader) {
            boolean lastInThisHierarchy = thisHierarchySize - 1 == rowIndex;
            cell.setSpans(colSpan, rowIndex, lastInThisHierarchy ? columnHierarchyLevels - 1 : rowIndex);
        } else {
            boolean verticallySpannedCell = rowIndex >= thisHierarchySize;
            cell.setSpans(colSpan, 0, verticallySpannedCell ? columnHierarchyLevels - thisHierarchySize : 0);
        }
        return cell;
    }

    public boolean isEmpty() {
        boolean commonHeaderSpecified = commonHeaderRow != null;
        boolean columnHeadersSpecified = getLastVisibleColHeadersRow() != -1;

        return !commonHeaderSpecified && !columnHeadersSpecified && !hasSubHeader;
    }

    public boolean hasCommonHeaderRow() {
        return commonHeaderRow != null;
    }

    public boolean hasSubHeader() {
        return hasSubHeader;
    }

    public void render(FacesContext facesContext,
                       HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        TableStructure tableStructure = getParent(TableStructure.class);
        UIComponent component = tableStructure.getComponent();
        TableStyles tableStyles = tableStructure.getTableStyles();

        ResponseWriter writer = facesContext.getResponseWriter();
        String sectionTag = isHeader ? "thead" : "tfoot";
        writer.startElement(sectionTag, component);
        String sectionClass = TableUtil.getClassWithDefaultStyleClass(
                tableStyles.getApplyDefaultStyle(),
                isHeader ? TableUtil.DEFAULT_HEADER_SECTION_CLASS : TableUtil.DEFAULT_FOOTER_SECTION_CLASS,
                isHeader ? tableStyles.getHeaderSectionClass() : tableStyles.getFooterSectionClass());
        String sectionStyle = isHeader ? tableStyles.getHeaderSectionStyle() : tableStyles.getFooterSectionStyle();
        RenderingUtil.writeStyleAndClassAttributes(writer, sectionStyle, sectionClass);
        RenderingUtil.writeNewLine(writer);

        for (int i = 0, count = allRows.size(); i < count; i++) {
            HeaderRow row = allRows.get(i);
            row.render(facesContext, i == count - 1 ? additionalContentWriter : null);
        }

        writer.endElement(sectionTag);
    }

    private int getLastVisibleColHeadersRow() {
        return lastVisibleColHeadersRow;
    }

    public int getSubHeaderRowIndex() {
        if (!hasSubHeader())
            return -1;
        if (!isHeader)
            return 0;
        int subHeaderRowIndex = getLastVisibleColHeadersRow() + 1;
        if (commonHeaderRow != null)
            subHeaderRowIndex++;
        return subHeaderRowIndex;
    }


    public CellCoordinates findColumnHeaderCell(BaseColumn column) {
        TableStructure tableStructure = getParent(TableStructure.class);
        if (tableStructure.getScrolling() == null) {
            boolean skipFirstRow = isHeader ? hasCommonHeaderRow() : hasSubHeader;
            boolean skipLastRow = isHeader ? hasSubHeader : hasCommonHeaderRow();
            List<HeaderRow> columnHeaderRows = allRows.subList(skipFirstRow ? 1 : 0, allRows.size() - (skipLastRow ? 1 : 0));
            return findColumnHeaderCell(column, columnHeaderRows);
        } else {
            HeaderRow row = allRows.get(isHeader
                    ? (hasCommonHeaderRow() ? 1 : 0)
                    : 0
            );
            List<HeaderCell> cells = row.getCells();
            for (int i = 0, count = cells.size(); i < count; i++) {
                HeaderCell cell = cells.get(i);
                TableScrollingArea scrollingArea = (TableScrollingArea) cell.getComponent();
                List<? extends TableElement> rows = scrollingArea.getRows();
                boolean skipFirstRow = isHeader ? false : hasSubHeader;
                boolean skipLastRow = isHeader ? hasSubHeader : false;
                List<HeaderRow> columnHeaderRows = (List<HeaderRow>) rows.subList(skipFirstRow ? 1 : 0, rows.size() - (skipLastRow ? 1 : 0));
                CellCoordinates coords = findColumnHeaderCell(column, columnHeaderRows);
                if (coords != null) {
                    coords.setScrollAreaIndex(i);
                    return coords;
                }
            }
            return null;
        }
    }

    private CellCoordinates findColumnHeaderCell(BaseColumn column, List<HeaderRow> columnHeaderRows) {
        int rowIndex = 0;
        for (HeaderRow row : columnHeaderRows) {
            if (!row.isAtLeastOneComponentInThisRow())
                continue;
            List cells = row.getCells();
            for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
                HeaderCell cell = (HeaderCell) cells.get(cellIndex);
                if (cell.getColumn() == column) {
                    if (isHeader)
                        rowIndex += commonHeaderRow != null ? 1 : 0;
                    else
                        rowIndex += hasSubHeader ? 1 : 0;
                    return new CellCoordinates(rowIndex, cellIndex);
                }
            }
            rowIndex++;
        }

        return null;
    }

    private UIComponent getColumnSubHeader(BaseColumn column) {
        if (!(column instanceof TableColumn))
            return null;
        TableColumn tableColumn = ((TableColumn) column);

        return tableColumn.getSubHeader();
    }
}
