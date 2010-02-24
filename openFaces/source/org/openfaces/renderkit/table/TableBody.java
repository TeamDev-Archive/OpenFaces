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
package org.openfaces.renderkit.table;

import org.openfaces.component.TableStyles;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Cell;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.Row;
import org.openfaces.component.table.Scrolling;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class TableBody extends TableSection {
    public static final String CUSTOM_ROW_INDEX_ATTRIBUTE = "_customRowIndex";
    public static final String CUSTOM_CELL_INDEX_ATTRIBUTE = "_customCellIndex";

    private static final String COLUMN_ATTR_ORIGINAL_INDEX = "_originalIndex";

    private static final String DEFAULT_NO_RECORDS_MSG = "No records";
    private static final String DEFAULT_NO_FILTER_RECORDS_MSG = "No records satisfying the filtering criteria";
    private static final String CUSTOM_CELL_RENDERING_INFO_ATTRIBUTE = "_customCellRenderingInfo";

    private TableStructure tableStructure;
    private boolean noDataRows;

    public TableBody(TableStructure tableStructure) {
        super(tableStructure);
        this.tableStructure = tableStructure;
    }

    public boolean isNoDataRows() {
        return noDataRows;
    }

    protected void fillInitParam(JSONObject result, TableStyles defaultStyles) throws JSONException {
        TableStyles table = tableStructure.getTableStyles();
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = tableStructure.getComponent();
        String bodyClass = Styles.getCSSClass(context,
                component, table.getBodySectionStyle(), table.getBodySectionClass());
        result.put("className", bodyClass);
        Rendering.addJsonParam(result, "rowClassName", Styles.getCSSClass(context, component, table.getBodyRowStyle(),
                StyleGroup.regularStyleGroup(), table.getBodyRowClass(),
                defaultStyles != null ? defaultStyles.getBodyRowClass() : null));
        Rendering.addJsonParam(result, "oddRowClassName", Styles.getCSSClass(context, component, table.getBodyOddRowStyle(),
                getBodyOddRowClass(table, defaultStyles)));
        if (table instanceof AbstractTable) {
            AbstractTable at = (AbstractTable) table;
            Rendering.addJsonParam(result, "rolloverRowClassName", Styles.getCSSClass(context, component, at.getRolloverRowStyle(),
                    StyleGroup.rolloverStyleGroup(), at.getRolloverRowClass()));
        }

        result.put("noDataRows", isNoDataRows());
    }

    private String getBodyOddRowClass(TableStyles table, TableStyles defaultStyles) {
        String bodyOddRowClass = table.getBodyOddRowClass();
        return TableUtil.getClassWithDefaultStyleClass(
                defaultStyles != null,
                defaultStyles != null ? defaultStyles.getBodyOddRowStyle() : null,
                bodyOddRowClass);
    }

    protected void renderRows(FacesContext context, HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        AbstractTable table = (AbstractTable) tableStructure.getComponent();
        List<BaseColumn> columns = table.getRenderedColumns();
        int first = table.getFirst();
        if (table.getRows() != 0)
            throw new IllegalStateException("table.getRows() should always be null in OpenFaces tables, but it is: " + table.getRows());

        int rowCount = table.getRowCount();
        int rows = (rowCount != -1) ? rowCount : Integer.MAX_VALUE;
        if (rows == 0)
            noDataRows = true;

        List<BodyRow> bodyRows = createRows(context, first, rows, columns);
        TableFooter footer = tableStructure.getFooter();
        boolean hasFooter = footer != null && footer.isContentSpecified();
        for (int i = 0, count = bodyRows.size(); i < count; i++) {
            BodyRow row = bodyRows.get(i);
            row.render(context, (!hasFooter && i == count - 1) ? additionalContentWriter : null);
        }
        table.setRowIndex(-1);
    }

    protected String getSectionName() {
        return "tbody";
    }

    protected List<BodyRow> createRows(
            FacesContext context,
            int firstRowIndex,
            int rowCount,
            List<BaseColumn> columns
    ) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        StringWriter stringWriter = new StringWriter();
        List<BodyRow> rows = new ArrayList<BodyRow>();
        context.setResponseWriter(writer.cloneWithWriter(stringWriter));
        try {
            if (tableStructure.getScrolling() == null)
                rows = createNonScrollableRows(context, stringWriter, firstRowIndex, rowCount, columns);
            else
                rows = createScrollableRows(context, stringWriter, firstRowIndex, rowCount, columns);
        } finally {
            context.setResponseWriter(writer);
        }
        return rows;
    }

    private List<BodyRow> createScrollableRows(
            FacesContext context,
            StringWriter stringWriter,
            int firstRowIndex,
            int rowCount,
            List<BaseColumn> columns
    ) throws IOException {
        List<BodyRow> leftRows, rows, rightRows;
        AbstractTable table = (AbstractTable) tableStructure.getComponent();
        int fixedLeftColumns = tableStructure.getLeftFixedCols();
        int fixedRightColumns = tableStructure.getRightFixedCols();
        int allColCount = columns.size();
        if (rowCount > 0) {
            List<BodyRow>[] rowsForAreas = createDataRows(context, stringWriter, firstRowIndex, rowCount, columns);
            leftRows = rowsForAreas[0];
            rows = rowsForAreas[1];
            rightRows = rowsForAreas[2];
        } else if (table.getNoDataMessageAllowed()) {
            leftRows = fixedLeftColumns > 0 ? Collections.singletonList(createEmptyNoDataRow(table, fixedLeftColumns)) : null;
            rows = Collections.singletonList(createNoDataRow(context, stringWriter, allColCount - fixedLeftColumns - fixedRightColumns));
            rightRows = fixedRightColumns > 0 ? Collections.singletonList(createEmptyNoDataRow(table, fixedRightColumns)) : null;
        } else {
            leftRows = fixedLeftColumns > 0 ? Collections.singletonList(createFakeRow(fixedLeftColumns)) : null;
            rows = Collections.singletonList(createFakeRow(allColCount - fixedLeftColumns - fixedRightColumns));
            rightRows = fixedRightColumns > 0 ? Collections.singletonList(createFakeRow(fixedRightColumns)) : null;
        }

        List<BodyCell> cells = new ArrayList<BodyCell>();
        if (fixedLeftColumns > 0)
            cells.add(scrollingAreaCell(columns, leftRows, 0, fixedLeftColumns, TableScrollingArea.ScrollingType.VERTICAL));
        cells.add(scrollingAreaCell(columns, rows, fixedLeftColumns, allColCount - fixedRightColumns, TableScrollingArea.ScrollingType.BOTH));
        if (fixedRightColumns > 0)
            cells.add(scrollingAreaCell(columns, rightRows, allColCount - fixedRightColumns, allColCount, TableScrollingArea.ScrollingType.VERTICAL));

        BodyRow containingRow = new BodyRow(this);
        containingRow.setCells(cells);
        return Collections.singletonList(containingRow);
    }

    private BodyCell scrollingAreaCell(List<BaseColumn> columns, List<BodyRow> rows, int startCol, int endCol, TableScrollingArea.ScrollingType scrolling) {
        BodyCell cell = new BodyCell();
        TableScrollingArea scrollingArea = new TableScrollingArea(cell, columns.subList(startCol, endCol), rows, scrolling);
        scrollingArea.setCellpadding(tableStructure.getTableCellPadding());
        scrollingArea.setIndefiniteHight(true);
        cell.setContent(scrollingArea);
        return cell;
    }

    private List<BodyRow> createNonScrollableRows(
            FacesContext context,
            StringWriter stringWriter,
            int firstRowIndex,
            int rowCount,
            List<BaseColumn> columns
    ) throws IOException {
        List<BodyRow> rows;
        AbstractTable table = (AbstractTable) tableStructure.getComponent();
        if (rowCount > 0)
            rows = createDataRows(context, stringWriter, firstRowIndex, rowCount, columns)[1];
        else if (table.getNoDataMessageAllowed())
            rows = Collections.singletonList(
                    createNoDataRow(context, stringWriter, columns.size()));
        else
            rows = Collections.singletonList(
                    createFakeRow(columns.size()));
        return rows;
    }

    private List<BodyRow>[] createDataRows(
            FacesContext context,
            StringWriter stringWriter,
            int firstRowIndex,
            int rowsToRender,
            List<BaseColumn> columns
    ) throws IOException {
        AbstractTable table = (AbstractTable) tableStructure.getComponent();
        Scrolling scrolling = tableStructure.getScrolling();
        int centerAreaStart = scrolling != null ? tableStructure.getLeftFixedCols() : 0;
        int rightAreaStart = scrolling != null ? columns.size() - tableStructure.getRightFixedCols() : 0;
        List<BodyRow> leftRows = scrolling != null && tableStructure.getLeftFixedCols() > 0 ? new ArrayList<BodyRow>() : null;
        List<BodyRow> rows = new ArrayList<BodyRow>();
        List<BodyRow> rightRows = scrolling != null && tableStructure.getRightFixedCols() > 0 ? new ArrayList<BodyRow>() : null;
        ResponseWriter writer = context.getResponseWriter();
        List<Row> customRows = getCustomRows(table);
        int lastRowIndex = firstRowIndex + rowsToRender - 1;
        Map<Integer, CustomRowRenderingInfo> customRowRenderingInfos =
                (Map<Integer, CustomRowRenderingInfo>) table.getAttributes().get(TableStructure.CUSTOM_ROW_RENDERING_INFOS_KEY);

        for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
            table.setRowIndex(rowIndex);
            if (!table.isRowAvailable())
                break;

            BodyRow leftRow = leftRows != null ? new BodyRow() : null;
            BodyRow row = new BodyRow();
            BodyRow rightRow = rightRows != null ? new BodyRow() : null;
            if (leftRows != null)
                leftRows.add(leftRow);
            rows.add(row);
            if (rightRows != null)
                rightRows.add(rightRow);
            List<Row> applicableCustomRows = getApplicableCustomRows(customRows);
            if (leftRow != null)
                leftRow.extractCustomEvents(applicableCustomRows);
            row.extractCustomEvents(applicableCustomRows);
            if (rightRow != null)
                rightRow.extractCustomEvents(applicableCustomRows);
            String[][] attributes = tableStructure.getBodyRowAttributes(context, table);
            if (leftRow != null)
                leftRow.setAttributes(attributes);
            row.setAttributes(attributes);
            if (rightRow != null)
                rightRow.setAttributes(attributes);
            Object rowData = table.getRowData();
            int bodyRowIndex = rowIndex - firstRowIndex;

            List<String> rowStyles = getApplicableRowStyles(context, customRows, table);
            String rowStyleClass = (rowStyles != null && rowStyles.size() > 0) ? classNamesToClass(rowStyles) : null;
            String additionalClass = tableStructure.getAdditionalRowClass(context, table, rowData, bodyRowIndex);
            if (additionalClass != null)
                rowStyleClass = Styles.mergeClassNames(rowStyleClass, additionalClass);
            if (!Rendering.isNullOrEmpty(rowStyleClass)) {
                Map<Object, String> rowStylesMap = tableStructure.getRowStylesMap();
                rowStylesMap.put(bodyRowIndex, rowStyleClass);
            }

            int columnCount = columns.size();

            CustomRowRenderingInfo customRowRenderingInfo = null;
            List<Integer> applicableRowDeclarationIndexes = new ArrayList<Integer>();
            for (Row tableRow : applicableCustomRows) {
                if (Rendering.isComponentWithA4jSupport(tableRow)) {
                    if (customRowRenderingInfo == null)
                        customRowRenderingInfo = new CustomRowRenderingInfo(columnCount);
                    Integer rowDeclarationIndex = (Integer) tableRow.getAttributes().get(CUSTOM_ROW_INDEX_ATTRIBUTE);
                    applicableRowDeclarationIndexes.add(rowDeclarationIndex);
                }
            }
            if (customRowRenderingInfo != null)
                customRowRenderingInfo.setA4jEnabledRowDeclarationIndexes(applicableRowDeclarationIndexes);

            List<SpannedTableCell> alreadyProcessedSpannedCells = new ArrayList<SpannedTableCell>();
            List[] applicableCustomCells = prepareCustomCells(table, applicableCustomRows);

            List<BodyCell> leftCells = leftRow != null ? new ArrayList<BodyCell>() : null;
            List<BodyCell> cells = new ArrayList<BodyCell>();
            List<BodyCell> rightCells = rightRow != null ? new ArrayList<BodyCell>() : null;
            if (leftRow != null)
                leftRow.setCells(leftCells);
            row.setCells(cells);
            if (rightRow != null)
                rightRow.setCells(rightCells);
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            for (int colIndex = 0; colIndex < columnCount;) {
                BaseColumn column = columns.get(colIndex);
                if (!column.isRendered())
                    throw new IllegalStateException("Only rendered columns are expected in columns list. column id: " + column.getId() + "; column index = " + colIndex);

                String columnIndexVar = table.getColumnIndexVar();
                String columnIdVar = table.getColumnIdVar();
                Object prevColumnIndexVarValue = null;
                Object prevColumnIdVarValue = null;
                int originalColIndex = (Integer) column.getAttributes().get(COLUMN_ATTR_ORIGINAL_INDEX);
                if (columnIndexVar != null)
                    prevColumnIndexVarValue = requestMap.put(columnIndexVar, originalColIndex);
                if (columnIdVar != null) {
                    String columnId = column.getId();
                    prevColumnIdVarValue = requestMap.put(columnIdVar, columnId);
                }


                List customCells = applicableCustomCells[colIndex];
                SpannedTableCell spannedTableCell =
                        customCells != null && customCells.size() == 1 && customCells.get(0) instanceof SpannedTableCell
                                ? (SpannedTableCell) customCells.get(0) : null;
                boolean remainingPortionOfBrokenSpannedCell = false;
                int span = 1;
                if (spannedTableCell != null) {
                    int testedColIndex = colIndex;
                    while (true) {
                        testedColIndex++;
                        if (testedColIndex == columnCount)
                            break;
                        List testedCells = applicableCustomCells[testedColIndex];
                        if (testedCells == null)
                            break;
                        SpannedTableCell testedSpannedCell = testedCells != null && testedCells.size() == 1 && testedCells.get(0) instanceof SpannedTableCell
                                ? (SpannedTableCell) testedCells.get(0) : null;
                        if (spannedTableCell != testedSpannedCell)
                            break;
                        span++;
                    }
                    column = spannedTableCell.getColumn();
                    customCells = spannedTableCell.getApplicableTableCells();
                    if (alreadyProcessedSpannedCells.contains(spannedTableCell))
                        remainingPortionOfBrokenSpannedCell = true;
                    else
                        alreadyProcessedSpannedCells.add(spannedTableCell);
                }

                UIComponent cellContentsContainer = column;
                String customCellStyle = null;
                String columnId = column.getId();
                if (customCells != null) {
                    int customCellCount = customCells.size();

                    for (int i = 0; i < customCellCount; i++) {
                        Cell cell = (Cell) customCells.get(i);
                        boolean cellWithCustomContent = cell.getChildCount() > 0;
                        if (cellWithCustomContent || Rendering.isComponentWithA4jSupport(cell)) {
                            if (customRowRenderingInfo == null)
                                customRowRenderingInfo = new CustomRowRenderingInfo(columnCount);
                            if (cellWithCustomContent)
                                cellContentsContainer = cell;
                            CustomContentCellRenderingInfo customCellRenderingInfo =
                                    (CustomContentCellRenderingInfo) cell.getAttributes().get(CUSTOM_CELL_RENDERING_INFO_ATTRIBUTE);
                            customRowRenderingInfo.setCustomCellRenderingInfo(colIndex, customCellRenderingInfo);
                        }
                        customCellStyle = Styles.mergeClassNames(customCellStyle, cell.getStyleClassForCell(context, table, colIndex, columnId));
                    }
                    for (int i = colIndex + (remainingPortionOfBrokenSpannedCell ? 0 : 1), upperBound = colIndex + span;
                         i < upperBound; i++) {
                        if (customRowRenderingInfo == null)
                            customRowRenderingInfo = new CustomRowRenderingInfo(columnCount);
                        customRowRenderingInfo.setCustomCellRenderingInfo(i, new MergedCellRenderingInfo());
                    }
                }

                List<String> cellStyles = new ArrayList<String>();
                if (customCellStyle != null)
                    cellStyles.add(customCellStyle);

                if (!cellStyles.isEmpty()) {
                    String styleClass = classNamesToClass(cellStyles);
                    if (!Rendering.isNullOrEmpty(styleClass)) {
                        Map<Object, String> cellStylesMap = tableStructure.getCellStylesMap();
                        cellStylesMap.put(bodyRowIndex + "x" + colIndex, styleClass);
                    }
                }

                BodyCell cell = new BodyCell();
                if (leftCells != null && colIndex < centerAreaStart)
                    leftCells.add(cell);
                else if (rightCells != null && colIndex >= rightAreaStart)
                    rightCells.add(cell);
                else
                    cells.add(cell);
                cell.setSpan(span);
                cell.extractCustomEvents(customCells);

                StringBuffer buf = stringWriter.getBuffer();
                int startIdx = buf.length();
                if (!remainingPortionOfBrokenSpannedCell) {
                    boolean renderCustomTreeCell = column instanceof TreeColumn && cellContentsContainer instanceof Cell;
                    if (renderCustomTreeCell) {
                        column.getAttributes().put(TreeColumnRenderer.ATTR_CUSTOM_CELL, cellContentsContainer);
                        try {
                            column.encodeAll(context);
                        } finally {
                            column.getAttributes().remove(TreeColumnRenderer.ATTR_CUSTOM_CELL);
                        }
                    } else {
                        cellContentsContainer.encodeAll(context);
                    }
                    writeNonBreakableSpaceForEmptyCell(writer, table, cellContentsContainer);
                } else {
                    if (tableStructure.isEmptyCellsTreatmentRequired())
                        Rendering.writeNonBreakableSpace(writer);
                }
                int endIdx = buf.length();
                String content = buf.substring(startIdx, endIdx);
                cell.setContent(content);
                
                if (columnIndexVar != null)
                    requestMap.put(columnIndexVar, prevColumnIndexVarValue);
                if (columnIdVar != null)
                    requestMap.put(columnIdVar, prevColumnIdVarValue);


                colIndex += span;
            }

            if (customRowRenderingInfo != null)
                customRowRenderingInfos.put(rowIndex, customRowRenderingInfo);
        }
        table.setRowIndex(-1);

        return new List[]{leftRows, rows, rightRows};
    }

    private boolean isColumnApplicable(
            FacesContext context,
            Cell cell,
            AbstractTable table,
            BaseColumn column,
            int originalColIndex) {
        Object columnIds = cell.getColumnIds();
        if (columnIds != null) {
            Collection<Object> columnIdsCollection;
            if (columnIds.getClass().isArray()) {
                columnIdsCollection = Arrays.asList((Object[]) columnIds);
            } else if (columnIds instanceof Collection)
                columnIdsCollection = (Collection<Object>) columnIds;
            else
                throw new IllegalArgumentException("'columnIds' attribute of <o:cell> tag should contain either an array or a collection of column id strings, but a value of the following type encountered: " + columnIds.getClass().getName());
            String colId = column.getId();
            boolean result = columnIdsCollection.contains(colId);
            return result;
        }

        ValueExpression conditionExpression = cell.getConditionExpression();

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String columnIndexVar = table.getColumnIndexVar();
        String columnIdVar = table.getColumnIdVar();
        Object prevColumnIndexVarValue = null;
        Object prevColumnIdVarValue = null;
        if (columnIndexVar != null)
            prevColumnIndexVarValue = requestMap.put(columnIndexVar, originalColIndex);
        if (columnIdVar != null) {
            String columnId = column.getId();
            prevColumnIdVarValue = requestMap.put(columnIdVar, columnId);
        }

        Boolean result = (Boolean) conditionExpression.getValue(context.getELContext());

        if (columnIndexVar != null)
            requestMap.put(columnIndexVar, prevColumnIndexVarValue);
        if (columnIdVar != null)
            requestMap.put(columnIdVar, prevColumnIdVarValue);

        return (result == null) || result;
    }

    private void writeNonBreakableSpaceForEmptyCell(ResponseWriter writer, AbstractTable table, UIComponent cellComponentsContainer) throws IOException {
        if (cellComponentsContainer instanceof Column || cellComponentsContainer instanceof Cell) {
            List<UIComponent> children = cellComponentsContainer.getChildren();
            boolean childrenEmpty = true;
            for (int childIndex = 0, childCount = children.size(); childIndex < childCount; childIndex++) {
                UIComponent child = children.get(childIndex);
                if (!TableStructure.isComponentEmpty(child)) {
                    childrenEmpty = false;
                    break;
                }
            }
            if (childrenEmpty && tableStructure.isEmptyCellsTreatmentRequired())
                Rendering.writeNonBreakableSpace(writer);
        }
    }

    private List<Row> getApplicableCustomRows(List<Row> customRows) {
        List<Row> applicableRows = new ArrayList<Row>(customRows.size());
        for (Row tableRow : customRows) {
            if (tableRow.getCondition())
                applicableRows.add(tableRow);
        }
        return applicableRows;
    }


    private BodyRow createFakeRow(int span) throws IOException {
        BodyRow row = new BodyRow();
        BodyCell cell = new BodyCell();
        row.setCells(Collections.singletonList(cell));
        cell.setSpan(span);
        cell.setStyle("display: none;");
        return row;
    }

    private BodyRow createNoDataRow(FacesContext context, StringWriter stringWriter, int span) throws IOException {
        AbstractTable table = (AbstractTable) tableStructure.getComponent();
        BodyRow row = createEmptyNoDataRow(table, span);

        BodyCell cell = row.getCells().get(0);

        StringBuffer buf = stringWriter.getBuffer();
        int startIdx = buf.length();
        boolean dataSourceEmpty = table.isDataSourceEmpty();
        UIComponent noDataMessage = dataSourceEmpty ? table.getNoDataMessage() : table.getNoFilterDataMessage();
        if (noDataMessage != null) {
            noDataMessage.encodeAll(context);
        } else {
            String message = (dataSourceEmpty)
                    ? DEFAULT_NO_RECORDS_MSG
                    : DEFAULT_NO_FILTER_RECORDS_MSG;
            ResponseWriter writer = context.getResponseWriter();
            writer.writeText(message, null);
        }
        int endIdx = buf.length();
        String content = buf.substring(startIdx, endIdx);
        cell.setContent(content);

        Map<Object, String> rowStylesMap = tableStructure.getRowStylesMap();
        rowStylesMap.put(0, TableStructure.getNoDataRowClassName(context, table));
        return row;
    }

    private BodyRow createEmptyNoDataRow(AbstractTable table, int span) {
        BodyRow row = new BodyRow();
        BodyCell cell = new BodyCell();
        row.setCells(Collections.singletonList(cell));
        if (span > 0)
            cell.setSpan(span);
        cell.setStyle(table.getNoDataRowStyle());
        cell.setStyleClass(table.getNoDataRowClass());
        return row;
    }

    /**
     * Takes the list of custom row specifications applicable to a row of interest (this is a list of TableRow instances
     * because several TableRow instances can be applicable to the same row), and constructs a list of custom cell
     * specifications applicable to the cells of this row.
     *
     * @param table                a table whose row is being analyzed
     * @param applicableCustomRows a list of TableRow instances applicable to the current row
     * @return An array the size of number of rendered columns. Each array entry corresponds to each rendered cell. Each
     *         array entry contains one of the following: (1) null - means that no custom cells are applicable for this
     *         cell and it will be rendered as usual; (2) List of TableCell instances applicable to this cell;
     *         (3) List containing only one SpannedTableCell instance if an appropriate cell is part of a cell span - all
     *         cells referring to the same SpannedTableCell will be rendered as one cell spanning across several columns.
     */
    private List[] prepareCustomCells(AbstractTable table, List<Row> applicableCustomRows) {
        List<BaseColumn> allColumns = table.getAllColumns();
        List<BaseColumn> columnsForRendering = table.getRenderedColumns();
        int allColCount = allColumns.size();
        for (int i = 0; i < allColCount; i++) {
            UIComponent col = allColumns.get(i);
            col.getAttributes().put(COLUMN_ATTR_ORIGINAL_INDEX, i);
        }

        int visibleColCount = columnsForRendering.size();
        List[] rowCellsByAbsoluteIndex = new List[allColCount];

        boolean thereAreCellSpans = false;
        List<Cell> rowCellsByColReference = new ArrayList<Cell>();
        for (int i = 0, icount = applicableCustomRows.size(); i < icount; i++) {
            Row row = applicableCustomRows.get(i);
            int customRowIndex = (Integer) row.getAttributes().get(CUSTOM_ROW_INDEX_ATTRIBUTE);
            List<UIComponent> children = row.getChildren();
            int freeCellIndex = 0;
            int customCellIndex = 0;
            for (int j = 0, jcount = children.size(); j < jcount; j++) {
                Object child = children.get(j);
                if (!(child instanceof Cell))
                    continue;
                Cell cell = (Cell) child;

                cell.getAttributes().put(CUSTOM_CELL_RENDERING_INFO_ATTRIBUTE, new CustomContentCellRenderingInfo(customRowIndex, customCellIndex++));
                int span = cell.getSpan();
                Object columnIds = cell.getColumnIds();
                ValueExpression conditionExpression = cell.getConditionExpression();
                if (span < 1)
                    throw new IllegalArgumentException("The value of 'span' attribute of <o:cell> tag can't be less than 1, but encountered: " + span);

                if (span != 1) {
                    thereAreCellSpans = true;

                }

                if (columnIds == null && conditionExpression == null) {
                    int thisColIndex = freeCellIndex;
                    freeCellIndex += span;
                    if (thisColIndex >= allColCount)
                        throw new FacesException("The number of free cells (cells without 'column' attribute) inside of <o:row> tag should not be greater than the total number of columns in a table (" + allColCount + ")");
                    List<Cell> applicableCells = rowCellsByAbsoluteIndex[thisColIndex];
                    if (applicableCells == null) {
                        applicableCells = new ArrayList<Cell>();
                        rowCellsByAbsoluteIndex[thisColIndex] = applicableCells;
                    }
                    applicableCells.add(cell);
                } else {
                    rowCellsByColReference.add(cell);
                }
            }
        }
        if (rowCellsByColReference.size() > 0) {
            FacesContext context = FacesContext.getCurrentInstance();
            for (int i = 0; i < allColCount; i++) {
                BaseColumn col = allColumns.get(i);
                for (Cell cell : rowCellsByColReference) {
                    if (isColumnApplicable(context, cell, table, col, i)) {
                        List<Cell> applicableCells = rowCellsByAbsoluteIndex[i];
                        if (applicableCells == null) {
                            applicableCells = new ArrayList<Cell>();
                            rowCellsByAbsoluteIndex[i] = applicableCells;
                        }
                        applicableCells.add(cell);
                    }
                }
            }
        }

        if (thereAreCellSpans) {
            for (int i = 0; i < allColCount; i++) {
                List customCellList = rowCellsByAbsoluteIndex[i];
                if (customCellList == null)
                    continue;
                int cellSpan = 1;
                for (int j = 0, jcount = customCellList.size(); j < jcount; j++) {
                    Cell cell = (Cell) customCellList.get(j);

                    int currentCellSpan = cell.getSpan();
                    if (currentCellSpan != 1)
                        cellSpan = currentCellSpan;
                }
                if (cellSpan == 1)
                    continue;
                SpannedTableCell spannedTableCell = new SpannedTableCell(allColumns.get(i), customCellList);
                for (int cellIndex = i; cellIndex < i + cellSpan; cellIndex++) {
                    rowCellsByAbsoluteIndex[cellIndex] = Collections.singletonList(spannedTableCell);
                }
                i += cellSpan - 1;
            }
        }

        List[] applicableCells = new List[visibleColCount];
        for (int i = 0; i < visibleColCount; i++) {
            BaseColumn column = columnsForRendering.get(i);
            int originalColIndex = (Integer) column.getAttributes().get(COLUMN_ATTR_ORIGINAL_INDEX);
            applicableCells[i] = rowCellsByAbsoluteIndex[originalColIndex];
        }
        return applicableCells;
    }

    private List<Row> getCustomRows(AbstractTable table) {
        List<Row> customRows = new ArrayList<Row>();
        List<UIComponent> children = table.getChildren();
        int customRowIndex = 0;
        int customCellIndex = 0;
        for (UIComponent child : children) {
            if (child instanceof Row) {
                Row customRow = (Row) child;
                customRows.add(customRow);
                customRow.getAttributes().put(CUSTOM_ROW_INDEX_ATTRIBUTE, customRowIndex++);
                List<UIComponent> customRowChildren = customRow.getChildren();
                for (UIComponent rowChild : customRowChildren) {
                    if (rowChild instanceof Cell) {
                        Cell customCell = (Cell) rowChild;
                        customCell.getAttributes().put(CUSTOM_CELL_INDEX_ATTRIBUTE, customCellIndex++);
                    }
                }
            }
        }
        return customRows;
    }

    private List<String> getApplicableRowStyles(FacesContext context, List<Row> customRows, AbstractTable table) {
        List<String> result = new ArrayList<String>();
        for (Row customRow : customRows) {
            String cls = customRow.getStyleClassForRow(context, table);
            if (cls != null)
                result.add(cls);
        }
        return result;
    }

    private String classNamesToClass(List<String> cellStyles) {
        StringBuilder combinedClassName = new StringBuilder();
        for (String className : cellStyles) {
            if (className != null) {
                int len = combinedClassName.length();
                if (len > 0)
                    combinedClassName.append(' ');
                combinedClassName.append(className.trim());
            }
        }
        return combinedClassName.toString();
    }


}
