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
public abstract class TableHeaderOrFooter {
    private TableStructure tableStructure;
    private boolean isHeader;

    private HeaderRow commonHeaderRow;
    private List<HeaderRow> columnHeaderRows;
    private HeaderRow subHeaderRow;

    protected TableHeaderOrFooter(TableStructure tableStructure, boolean composeHeader) {
        isHeader = composeHeader;
        this.tableStructure = tableStructure;

        boolean applyDefaultStyle = tableStructure.getTableStyles().getApplyDefaultStyle();
        String cellTag = isHeader
                ? (applyDefaultStyle ? "td" : "th")
                : "td";
        commonHeaderRow = composeCommonHeaderRow(cellTag);
        columnHeaderRows = composeColumnHeaderRows(cellTag);
        subHeaderRow = composeSubHeaderRow("td");
    }

    public static int composeColumnHierarchies(List columns, List[] columnHierarchies, boolean isHeader, boolean skipEmptyHeaders) {
        int columnHierarchyLevels = 0;
        for (int colIndex = 0, colCount = columns.size(); colIndex < colCount; colIndex++) {
            List<BaseColumn> columnHierarchy = new ArrayList<BaseColumn>();
            BaseColumn startingColumn = (BaseColumn) columns.get(colIndex);
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

    private HeaderRow composeCommonHeaderRow(String cellTag) {
        List columns = tableStructure.getColumns();
        TableStyles table = tableStructure.getTableStyles();
        UIComponent headerOrFooter = isHeader ? table.getHeader() : table.getFooter();
        if (headerOrFooter == null)
            return null;
        HeaderCell cell = new HeaderCell(tableStructure, null, headerOrFooter, cellTag);

        int colCount = columns.size();
        cell.setSpans(colCount, 0, 0);
        return new HeaderRow(true, Collections.singletonList(cell));
    }

    private HeaderRow composeSubHeaderRow(String cellTag) {
        boolean atLeastOneComponent = false;
        List<HeaderCell> cells = new ArrayList<HeaderCell>();

        List<BaseColumn> columns = tableStructure.getColumns();
        for (BaseColumn column : columns) {
            UIComponent columnHeaderOrFooter =
                    (isHeader ? TableUtil.getColumnSubHeader(column) : null);
            HeaderCell cell = new HeaderCell(tableStructure, column, columnHeaderOrFooter, cellTag);
            if (columnHeaderOrFooter != null)
                atLeastOneComponent = true;
            cell.setSpans(1, 0, 0);
            cells.add(cell);
        }
        if (!atLeastOneComponent)
            return null;
        return new HeaderRow(true, cells);
    }

    private List<HeaderRow> composeColumnHeaderRows(String cellTag) {
        List columns = tableStructure.getColumns();
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
            HeaderRow row = new HeaderRow(atLeastOneComponentInThisRow, rowCells);
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

        HeaderCell cell = new HeaderCell(tableStructure, col, isHeader ? col.getHeader() : col.getFooter(), cellTag, isHeader, isHeader);
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
        boolean subHeaderSpecified = subHeaderRow != null;

        return !commonHeaderSpecified && !columnHeadersSpecified && !subHeaderSpecified;
    }

    public boolean hasCommonHeaderRow() {
        return commonHeaderRow != null;
    }

    public boolean hasSubHeader() {
        return subHeaderRow != null;
    }

    public void render(FacesContext facesContext,
                       HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        UIComponent component = tableStructure.getTable();
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

        renderRows(facesContext, additionalContentWriter);

        writer.endElement(sectionTag);
    }

    private void renderRows(FacesContext facesContext,
                            HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        int lastVisibleRowIndex = getLastVisibleColHeadersRow();

        UIComponent component = tableStructure.getTable();

        if (!isHeader) {
            if (subHeaderRow != null)
                subHeaderRow.render(facesContext, component, null,
                        lastVisibleRowIndex == -1 && commonHeaderRow == null ? additionalContentWriter : null);
        } else {
            if (commonHeaderRow != null)
                commonHeaderRow.render(facesContext, component, null, null);
        }

        boolean putAdditionalContentInColHeaders = !isHeader && commonHeaderRow == null;
        for (int i = 0, count = columnHeaderRows.size(); i < count; i++) {
            HeaderRow row = columnHeaderRows.get(i);
            if (!row.isAtLeastOneComponentInThisRow())
                continue;

            row.render(facesContext, component, columnHeaderRows,
                    putAdditionalContentInColHeaders && i == lastVisibleRowIndex ? additionalContentWriter : null);
        }

        if (isHeader) {
            if (subHeaderRow != null)
                subHeaderRow.render(facesContext, component, null, null);
        } else {
            if (commonHeaderRow != null)
                commonHeaderRow.render(facesContext, component, null, additionalContentWriter);
        }
    }

    private int getLastVisibleColHeadersRow() {
        int lastVisibleRowIndex = -1;
        for (int i = 0, count = columnHeaderRows.size(); i < count; i++) {
            HeaderRow row = columnHeaderRows.get(i);
            if (row.isAtLeastOneComponentInThisRow())
                lastVisibleRowIndex = i;
        }
        return lastVisibleRowIndex;
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
                        rowIndex += subHeaderRow != null ? 1 : 0;
                    return new CellCoordinates(rowIndex, cellIndex);
                }
            }
            rowIndex++;
        }

        return null;
    }
}
