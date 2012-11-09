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
package org.openfaces.renderkit.table;

import org.openfaces.component.TableStyles;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Scrolling;
import org.openfaces.component.table.impl.DynamicCol;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TableHeaderOrFooter extends TableSection {
    private static final String DEFAULT_SUBHEADER_ROW_CELLS_CLASS = "o_filter_row_cells";

    private boolean isHeader;

    private HeaderRow commonHeaderRow;
    private List<HeaderRow> allRows;
    private boolean hasSubHeader;
    private int lastVisibleColHeadersRow = -1;
    private Boolean ieDocModeAdditionalSupport;

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
            composeNonScrollingContent(cellTag, columns);
        } else {
            composeScrollingContent(tableStructure, cellTag, columns);
        }
    }

    private void composeScrollingContent(TableStructure tableStructure, String cellTag, List<BaseColumn> columns) {
        List<HeaderCell> cells = new ArrayList<HeaderCell>();
        int leftFixedCols = tableStructure.getLeftFixedCols();
        int rightFixedCols = tableStructure.getRightFixedCols();
        int totalColCount = columns.size();

        if (leftFixedCols > 0)
            cells.add(scrollingAreaCell(tableStructure, cellTag, columns, 0, leftFixedCols, false));

        cells.add(scrollingAreaCell(tableStructure, cellTag, columns, leftFixedCols, totalColCount - rightFixedCols, true));

        if (rightFixedCols > 0)
            cells.add(scrollingAreaCell(tableStructure, cellTag, columns, totalColCount - rightFixedCols, totalColCount, false));

        boolean contentsSpecified = false;
        TableScrollingArea firstArea = (TableScrollingArea) cells.get(0).getContent();
        for (int rowIndex = 0, rowCount = firstArea.getRows().size(); rowIndex < rowCount; rowIndex++) {
            boolean rowContentSpecified = false;
            for (HeaderCell areaCell : cells) {
                TableScrollingArea area = (TableScrollingArea) areaCell.getContent();
                if (area.getRows().get(rowIndex).isAtLeastOneComponentInThisRow()) {
                    rowContentSpecified = true;
                    contentsSpecified = true;
                    break;
                }
            }
            for (HeaderCell areaCell : cells) {
                TableScrollingArea area = (TableScrollingArea) areaCell.getContent();
                ((HeaderRow) area.getRows().get(rowIndex)).setAtLeastOneComponentInThisRow(rowContentSpecified);
            }
        }
        if (contentsSpecified)
            allRows.add(new HeaderRow(this, true, cells));
        if (commonHeaderRow != null)
            allRows.add(isHeader ? 0 : allRows.size(), commonHeaderRow);
    }

    private HeaderCell scrollingAreaCell(
            TableStructure tableStructure,
            String cellTag,
            List<BaseColumn> columns,
            int startColIndex, int endColIndex,
            boolean scrollable
    ) {
        List<HeaderRow> rows = composeColumnHeaderRows(columns, startColIndex, endColIndex, cellTag);
        for (int i = 0; i < rows.size(); i++) {
            HeaderRow row = rows.get(i);
            if (row.isAtLeastOneComponentInThisRow())
                lastVisibleColHeadersRow = i;
        }
        HeaderRow subHeaderRow = composeSubHeaderRow(columns.subList(startColIndex, endColIndex), cellTag);
        if (subHeaderRow != null) {
            rows.add(isHeader ? rows.size() : 0, subHeaderRow);
            hasSubHeader = true;
        }

        TableScrollingArea tableScrollingArea = new TableScrollingArea(
                this, columns.subList(startColIndex, endColIndex), rows, 
                scrollable ? TableScrollingArea.ScrollingType.HORIZONTAL : TableScrollingArea.ScrollingType.NONE,isHeader && getIeDocModeAdditionalSupportNeeded());
        tableScrollingArea.setCellpadding(tableStructure.getTableCellPadding());
        return new HeaderCell(null, tableScrollingArea, "td", null);
    }

    private Boolean getIeDocModeAdditionalSupportNeeded(){
        if (ieDocModeAdditionalSupport == null){
            String  paramValue = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(TableStructure.INIT_PARAM_ADDITIONAL_IEDOCMODE7_SUPPORT);
            if  (paramValue != null){
                ieDocModeAdditionalSupport = Boolean.valueOf(paramValue);
            }else{
                ieDocModeAdditionalSupport = Boolean.FALSE;
            }
        }
        return ieDocModeAdditionalSupport;
    }

    private void composeNonScrollingContent(String cellTag, List<BaseColumn> columns) {
        List<HeaderRow> columnHeaderRows = composeColumnHeaderRows(columns, 0, columns.size(), cellTag);
        for (int i = 0; i < columnHeaderRows.size(); i++) {
            HeaderRow row = columnHeaderRows.get(i);
            if (row.isAtLeastOneComponentInThisRow())
                lastVisibleColHeadersRow = i;
        }
        HeaderRow subHeaderRow = composeSubHeaderRow(columns, "td");
        hasSubHeader = subHeaderRow != null;
        if (!isHeader) {
            if (subHeaderRow != null)
                allRows.add(subHeaderRow);
        } else {
            if (commonHeaderRow != null)
                allRows.add(commonHeaderRow);
        }

        for (HeaderRow row : columnHeaderRows) {
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

    public static int composeColumnHierarchies(
            List<BaseColumn> columns,
            List<BaseColumn>[] columnHierarchies,
            boolean isHeader,
            boolean skipEmptyHeaders) {
        int columnHierarchyLevels = 0;
        for (int colIndex = 0, colCount = columns.size(); colIndex < colCount; colIndex++) {
            List<BaseColumn> columnHierarchy = new ArrayList<BaseColumn>();
            BaseColumn startingColumn = columns.get(colIndex);
            for (BaseColumn column = startingColumn;
                 column != null;
                 column = column.getParent() instanceof BaseColumn ? (BaseColumn) column.getParent() : null) {
                if (skipEmptyHeaders && column != startingColumn) {
                    Object header = getHeaderOrFooterCellContent(column, isHeader);
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
        HeaderCell cell = new HeaderCell(null, headerOrFooter, cellTag, null);

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

    private HeaderRow composeSubHeaderRow(List<BaseColumn> columns, String cellTag) {
        boolean atLeastOneComponent = false;
        List<HeaderCell> cells = new ArrayList<HeaderCell>();

        for (BaseColumn column : columns) {
            UIComponent columnHeaderOrFooter = isHeader ? column.getSubHeader() : null;
            HeaderCell cell = new HeaderCell(column, columnHeaderOrFooter, cellTag, CellKind.COL_SUBHEADER);
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
            List<BaseColumn> columns,
            int startColIndex,
            int endColIndex,
            String cellTag) {
        int colCount = columns.size();
        List<BaseColumn>[] columnHierarchies = new ArrayList[colCount];
        int columnHierarchyLevels = composeColumnHierarchies(columns, columnHierarchies, isHeader, true);

        List<HeaderRow> rows = new ArrayList<HeaderRow>();
        for (int rowIndex = 0; rowIndex < columnHierarchyLevels; rowIndex++) {
            List<HeaderCell> rowCells = new ArrayList<HeaderCell>();
            boolean atLeastOneComponentInThisRow = false;
            for (int colIndex = startColIndex; colIndex < endColIndex; colIndex++) {
                HeaderCell cell = createCell(cellTag, columnHierarchies, columnHierarchyLevels, rowIndex, colIndex);
                if (cell == null)
                    continue;
                rowCells.add(cell);
                if (cell.getContent() != null)
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

        Object cellContent = getHeaderOrFooterCellContent(col, isHeader);
        HeaderCell cell = new HeaderCell(col, cellContent, cellTag, CellKind.COL_HEADER,
                isHeader, isHeader ? HeaderCell.SortingToggleMode.AUTODETECT : HeaderCell.SortingToggleMode.OFF);
        cell.setEscapeText(true);
        if (isHeader) {
            boolean lastInThisHierarchy = thisHierarchySize - 1 == rowIndex;
            cell.setSpans(colSpan, rowIndex, lastInThisHierarchy ? columnHierarchyLevels - 1 : rowIndex);
        } else {
            boolean verticallySpannedCell = rowIndex >= thisHierarchySize;
            cell.setSpans(colSpan, 0, verticallySpannedCell ? columnHierarchyLevels - thisHierarchySize : 0);
        }
        return cell;
    }

    public static Object getHeaderOrFooterCellContent(BaseColumn col, boolean isHeader) {
        DynamicCol dynamicCol = col instanceof DynamicCol ? (DynamicCol) col : null;
        Runnable restoreVariables = (dynamicCol != null) ? dynamicCol.enterComponentContext() : null;
        try {
            Object cellContent;
            if (isHeader) {
                cellContent = col.getHeader();
                if (cellContent == null)
                    cellContent = col.getHeaderValue();
            } else {
                cellContent = col.getFooter();
                if (cellContent == null)
                    cellContent = col.getFooterValue();
            }
            return cellContent;
        } finally {
            if (restoreVariables != null) restoreVariables.run();
        }
    }

    public boolean hasSubHeader() {
        return hasSubHeader;
    }

    @Override
    protected void fillInitParam(JSONObject initObject, TableStyles defaultStyles) throws JSONException {
        TableStructure tableStructure = getParent(TableStructure.class);
        TableStyles tableStyles = tableStructure.getTableStyles();
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = tableStructure.getComponent();
        String sectionClass = Styles.getCSSClass(context, component,
                isHeader ? tableStyles.getHeaderSectionStyle() : tableStyles.getFooterSectionStyle(),
                isHeader ? tableStyles.getHeaderSectionClass() : tableStyles.getFooterSectionClass());
        sectionClass = TableUtil.getClassWithDefaultStyleClass(
                tableStyles.getApplyDefaultStyle(),
                isHeader ? TableUtil.DEFAULT_HEADER_SECTION_CLASS : TableUtil.DEFAULT_FOOTER_SECTION_CLASS,
                sectionClass);

        if (!Rendering.isNullOrEmpty(sectionClass))
            initObject.put("className", sectionClass);
        if (tableStructure.getScrolling() != null)
            initObject.put("rowCount", allRows.size());
        if (commonHeaderRow != null)
            initObject.put("commonHeader", getCommonHeaderParam(tableStructure));
        if (hasSubHeader)
            initObject.put("subHeader", getSubHeaderParam(tableStructure));

        String headerClassName = Styles.getCSSClass(context, component,
                isHeader ? tableStyles.getHeaderRowStyle() : tableStyles.getFooterRowStyle(),
                isHeader ? tableStyles.getHeaderRowClass() : tableStyles.getFooterRowClass());
        if (!Rendering.isNullOrEmpty(headerClassName))
            initObject.put("headingsClassName", headerClassName);
    }

    private JSONObject getCommonHeaderParam(TableStructure tableStructure) throws JSONException {
        JSONObject result = new JSONObject();
        FacesContext context = FacesContext.getCurrentInstance();
        AbstractTable table = ((AbstractTable) tableStructure.getComponent());
        String commonHeaderClass = Styles.getCSSClass(context, tableStructure.getComponent(),
                isHeader ? table.getCommonHeaderRowStyle() : table.getCommonFooterRowStyle(),
                isHeader ? table.getCommonHeaderRowClass() : table.getCommonFooterRowClass());
        if (!Rendering.isNullOrEmpty(commonHeaderClass))
            result.put("className", commonHeaderClass);
        return result;
    }

    private JSONObject getSubHeaderParam(TableStructure tableStructure) throws JSONException {
        JSONObject result = new JSONObject();
        FacesContext context = FacesContext.getCurrentInstance();
        AbstractTable table = ((AbstractTable) tableStructure.getComponent());
        String className = Styles.getCSSClass(context, table, getSubHeaderRowStyle(table),
                StyleGroup.regularStyleGroup(), getSubHeaderRowClass(table), DEFAULT_SUBHEADER_ROW_CELLS_CLASS);
        if (!Rendering.isNullOrEmpty(className))
            result.put("className", className);

        return result;
    }

    private String getSubHeaderRowClass(TableStyles tableStyles) {
        if (!(tableStyles instanceof AbstractTable))
            return null;
        AbstractTable table = ((AbstractTable) tableStyles);
        return table.getSubHeaderRowClass();
    }

    private String getSubHeaderRowStyle(TableStyles tableStyles) {
        if (!(tableStyles instanceof AbstractTable))
            return null;
        AbstractTable table = ((AbstractTable) tableStyles);
        return table.getSubHeaderRowStyle();
    }

    protected String getSectionName() {
        return isHeader ? "thead" : "tfoot";
    }

    protected void renderRows(FacesContext facesContext, HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        for (int i = 0, count = allRows.size(); i < count; i++) {
            HeaderRow row = allRows.get(i);
            row.render(facesContext, i == count - 1 ? additionalContentWriter : null);
        }
    }

    @Override
    public boolean isContentSpecified() {
        if (allRows.size() == 0)
            return false;
        for (AbstractRow row : allRows)
            if (row.isAtLeastOneComponentInThisRow())
                return true;
        return false;
    }

    public int getSubHeaderRowIndex() {
        if (!hasSubHeader())
            return -1;
        if (!isHeader)
            return 0;
        int subHeaderRowIndex = lastVisibleColHeadersRow + 1;
        if (commonHeaderRow != null)
            subHeaderRowIndex++;
        return subHeaderRowIndex;
    }

    public CellCoordinates findCell(BaseColumn column, CellKind cellKind) {
        if (allRows.size() == 0)
            return null;
        TableStructure tableStructure = getParent(TableStructure.class);
        if (tableStructure.getScrolling() == null) {
            boolean skipFirstRow = isHeader ? commonHeaderRow != null : hasSubHeader;
            List<HeaderRow> columnHeaderRows = allRows.subList(skipFirstRow ? 1 : 0, allRows.size());
            CellCoordinates cellCoordinates = findCell(column, columnHeaderRows, cellKind);
            if (cellCoordinates != null && isHeader && commonHeaderRow != null)
                cellCoordinates.setRowIndex(cellCoordinates.getRowIndex() + 1);
            return cellCoordinates;
        } else {
            int rowIndex = isHeader
                    ? (commonHeaderRow != null ? 1 : 0)
                    : allRows.size() - 1 - (commonHeaderRow != null ? 1 : 0);
            HeaderRow row = rowIndex >= 0 && rowIndex < allRows.size() ? allRows.get(rowIndex) : null;
            if (row == null) return null;
            
            List<HeaderCell> cells = row.getCells();
            for (int i = 0, count = cells.size(); i < count; i++) {
                HeaderCell cell = cells.get(i);
                TableScrollingArea scrollingArea = (TableScrollingArea) cell.getContent();
                List<HeaderRow> rows = (List<HeaderRow>) scrollingArea.getRows();
                CellCoordinates coords = findCell(column, rows, cellKind);
                if (coords != null) {
                    coords.setScrollAreaIndex(i);
                    return coords;
                }
            }
            return null;
        }
    }

    private CellCoordinates findCell(
            BaseColumn column,
            List<HeaderRow> columnHeaderRows,
            CellKind cellKind) {
        int rowIndex = getIeDocModeAdditionalSupportNeeded() ? 1 : 0;
        for (HeaderRow row : columnHeaderRows) {
            if (!row.isAtLeastOneComponentInThisRow())
                continue;
            List cells = row.getCells();
            for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
                HeaderCell cell = (HeaderCell) cells.get(cellIndex);
                if (cell.getColumn() == column && cell.getCellKind() == cellKind) {
                    return new CellCoordinates(rowIndex, cellIndex);
                }
            }
            rowIndex++;
        }

        return null;
    }

}
