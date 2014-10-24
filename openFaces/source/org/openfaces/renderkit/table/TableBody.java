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

import org.openfaces.component.ContextDependentComponent;
import org.openfaces.component.TableStyles;
import org.openfaces.component.table.*;
import org.openfaces.component.table.impl.GroupFooterRow;
import org.openfaces.component.table.impl.GroupHeaderOrFooterRow;
import org.openfaces.component.table.impl.GroupHeaderRow;
import org.openfaces.component.table.impl.GroupingStructureColumn;
import org.openfaces.component.table.impl.InGroupFooterRow;
import org.openfaces.component.table.impl.InGroupHeaderRow;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.ELContext;
import javax.el.MethodExpression;
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
import java.util.LinkedHashMap;
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
    private List<String> clientRowKeys;
    private BaseColumn currentlyRenderedColumn;

    public TableBody(TableStructure tableStructure) {
        super(tableStructure);
        this.tableStructure = tableStructure;
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
            Rendering.addJsonParam(result, "rolloverRowClassName", Styles.getCSSClass(context, component,
                    at.getRolloverRowStyle(), StyleGroup.rolloverStyleGroup(), at.getRolloverRowClass()));
        }

        result.put("noDataRows", getNoDataRows());
        result.put("rowKeys", clientRowKeys);
    }

    public boolean getNoDataRows() {
        return noDataRows;
    }

    public List<String> getClientRowKeys() {
        return clientRowKeys;
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
        List<BaseColumn> columns = table.getAdaptedRenderedColumns();
        int first = table.getFirst();
        if (table.getRows() != 0)
            throw new IllegalStateException("table.getRows() should always be equal to 0 in OpenFaces tables, but it is equal to " + table.getRows());

        int rowCount = table.getRowCount();
        int rows = (rowCount != -1) ? rowCount : Integer.MAX_VALUE;
        if (rows == 0)
            noDataRows = true;
        if (table.getDeferBodyLoading()) {
            rows = 0;
        }

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
        ResponseWriter responseWriter = writer.cloneWithWriter(stringWriter);
        if (AjaxUtil.isAjaxRequest(context))
            responseWriter.startCDATA();
        stringWriter.getBuffer().setLength(0);
        context.setResponseWriter(responseWriter);
        try {
            if (tableStructure.getScrolling() == null)
                rows = createNonScrollableRows(context, stringWriter, firstRowIndex, rowCount, columns);
            else
                rows = createScrollableRows(context, stringWriter, firstRowIndex, rowCount, columns);
        } finally {
            if (AjaxUtil.isAjaxRequest(context))
                responseWriter.endCDATA();
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

    private BodyCell scrollingAreaCell(List<BaseColumn> columns, List<BodyRow> rows, int startCol, int endCol,
                                       TableScrollingArea.ScrollingType scrolling) {
        BodyCell cell = new BodyCell();
        TableScrollingArea scrollingArea =
                new TableScrollingArea(cell, columns.subList(startCol, endCol), rows, scrolling, false);
        scrollingArea.setCellpadding(tableStructure.getTableCellPadding());
        scrollingArea.setIndefiniteHeight(true);
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
        TableBodyRenderer renderer = new TableBodyRenderer(context, stringWriter, firstRowIndex, columns);
        return renderer.createDataRows(rowsToRender);
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


    public BaseColumn getCurrentlyRenderedColumn() {
        return currentlyRenderedColumn;
    }

    public void setCurrentlyRenderedColumn(BaseColumn currentlyRenderedColumn) {
        this.currentlyRenderedColumn = currentlyRenderedColumn;
    }

    private class TableBodyRenderer {
        private final FacesContext context;
        private final ResponseWriter writer;
        private final Map<String, Object> requestMap;
        private final StringWriter stringWriter;
        private final int firstRowIndex;
        private final AbstractTable table;
        private final List<BaseColumn> columns;
        private final List<BodyRow> leftRows;
        private final List<BodyRow> rows;
        private final List<BodyRow> rightRows;
        private final int centerAreaStart;
        private final int rightAreaStart;
        private final List<Row> customRows;
        private final Map<Integer, CustomRowRenderingInfo> customRowRenderingInfos;
        private final Map<Integer, Integer> renderedColIndexesByOriginalIndexes;
        private final MethodExpression cellSelectableME;
        private final MethodExpression selectableCellsME;
        private final JSONArray collectedSelectableCells;
        private final AbstractCellSelection cellSelection;
        private final Iterable<Summary> summaries;

        private TableBodyRenderer(
                FacesContext context,
                StringWriter stringWriter,
                int firstRowIndex,
                List<BaseColumn> columns) {

            this.context = context;
            this.stringWriter = stringWriter;
            this.firstRowIndex = firstRowIndex;
            this.columns = columns;

            writer = context.getResponseWriter();
            requestMap = context.getExternalContext().getRequestMap();
            table = (AbstractTable) tableStructure.getComponent();

            Scrolling scrolling = tableStructure.getScrolling();
            leftRows = scrolling != null && tableStructure.getLeftFixedCols() > 0 ? new ArrayList<BodyRow>() : null;
            rows = new ArrayList<BodyRow>();
            rightRows = scrolling != null && tableStructure.getRightFixedCols() > 0 ? new ArrayList<BodyRow>() : null;
            centerAreaStart = scrolling != null ? tableStructure.getLeftFixedCols() : 0;
            rightAreaStart = scrolling != null ? columns.size() - tableStructure.getRightFixedCols() : 0;

            customRows = getCustomRows(table);
            customRowRenderingInfos = (Map<Integer, CustomRowRenderingInfo>) table.getAttributes().get(
                    TableStructure.CUSTOM_ROW_RENDERING_INFOS_KEY);

            final AbstractTableSelection selection = table.getSelection();
            cellSelection = selection instanceof AbstractCellSelection
                    ? (AbstractCellSelection) selection : null;
            collectedSelectableCells = new JSONArray();

            cellSelectableME = (cellSelection == null) ? null : cellSelection.getCellSelectable();
            selectableCellsME = (cellSelection == null) ? null : cellSelection.getSelectableCells();
            if (cellSelectableME != null && selectableCellsME != null) throw new IllegalStateException(
                    "The \"cellSelectable\" and \"selectableCells\" attributes are " +
                            "mutually exclusive and cannot be used at the same time.");
            renderedColIndexesByOriginalIndexes = getRenderedColIndexesByOriginalIndexesMap(table);

            summaries = table.getSummaryComponents();
        }

        private List<Row> getCustomRows(AbstractTable table) {
            List<Row> customRows = new ArrayList<Row>();

            if (table instanceof DataTable) {
                DataTable dataTable = (DataTable) table;
                RowGrouping rowGrouping = dataTable.getRowGrouping();
                if (rowGrouping != null) {
                    customRows.add(new GroupHeaderRow(dataTable));
                    customRows.add(new InGroupHeaderRow(dataTable));
                    customRows.add(new InGroupFooterRow(dataTable));
                    customRows.add(new GroupFooterRow(dataTable));
                }
            }

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

        private Map<Integer, Integer> getRenderedColIndexesByOriginalIndexesMap(AbstractTable table) {
            final List<BaseColumn> renderedColumns = table.getRenderedColumns();
            final List<BaseColumn> allColumns = table.getAllColumns();
            final Map<Integer, Integer> renderedColIndexesByOriginalIndexes =
                    new LinkedHashMap<Integer, Integer>(allColumns.size());
            for (int originalColIndex = 0; originalColIndex < allColumns.size(); originalColIndex++) {
                for (int renderedColIndex = 0; renderedColIndex < renderedColumns.size(); renderedColIndex++) {
                    if (allColumns.get(originalColIndex) == renderedColumns.get(renderedColIndex)) {
                        renderedColIndexesByOriginalIndexes.put(originalColIndex, renderedColIndex);
                        break;
                    }
                }
            }
            return renderedColIndexesByOriginalIndexes;
        }

        public List<BodyRow>[] createDataRows(int rowsToRender) throws IOException {
            int lastRowIndex = firstRowIndex + rowsToRender - 1;
            for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
                table.setRowIndex(rowIndex);
                if (!table.isRowAvailable())
                    break;

                RowRenderer rowRenderer = new RowRenderer(rowIndex);
                rowRenderer.renderRow();
            }
            if (cellSelection != null) {
                cellSelection.setCollectedSelectableCells(collectedSelectableCells);
            }
            table.setRowIndex(-1);

            return new List[]{leftRows, rows, rightRows};
        }


        private class RowRenderer {
            private final List<BodyCell> leftCells;
            private final List<BodyCell> cells;
            private final List<BodyCell> rightCells;
            private final int bodyRowIndex;
            private final int columnCount;
            private final List<SpannedTableCell> alreadyProcessedSpannedCells;
            private final List<Object>[] applicableCustomCells;
            private final JSONArray selectableCellsInColumn;

            private CustomRowRenderingInfo customRowRenderingInfo;
            private int rowIndex;
            private ELContext elContext;

            private RowRenderer(int rowIndex) throws IOException {
                this.elContext = FacesContext.getCurrentInstance().getELContext();
                this.rowIndex = rowIndex;

                String clientRowKey = table.getClientRowKey();
                if (clientRowKeys == null) clientRowKeys = new ArrayList<String>();
                clientRowKeys.add(clientRowKey);

                this.columnCount = columns.size();

                collectSelectableCells(columnCount);

                this.bodyRowIndex = rowIndex - firstRowIndex;
                List<Row> applicableCustomRows = getApplicableCustomRows(customRows);
                this.applicableCustomCells =
                        getApplicableCustomCells(applicableCustomRows, bodyRowIndex, columnCount);

                this.selectableCellsInColumn = new JSONArray();
                BodyRow leftRow = leftRows != null ? new BodyRow() : null;
                BodyRow row = new BodyRow();
                BodyRow rightRow = rightRows != null ? new BodyRow() : null;

                if (leftRows != null)
                    leftRows.add(leftRow);
                rows.add(row);
                if (rightRows != null)
                    rightRows.add(rightRow);

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

                this.leftCells = leftRow != null ? new ArrayList<BodyCell>() : null;
                this.cells = new ArrayList<BodyCell>();
                this.rightCells = rightRow != null ? new ArrayList<BodyCell>() : null;

                if (leftRow != null)
                    leftRow.setCells(leftCells);
                row.setCells(cells);
                if (rightRow != null)
                    rightRow.setCells(rightCells);

                this.alreadyProcessedSpannedCells = new ArrayList<SpannedTableCell>();
            }

            public void renderRow() throws IOException {
                Object rowData = table.getRowData();
                if (!(rowData instanceof GroupHeaderOrFooter)) {
                    for (Summary summary : summaries) {
                        summary.addCurrentRowValue();
                    }
                }

                for (int colIndex = 0; colIndex < columnCount; ) {
                    List<BodyCell> targetCells;
                    if (leftCells != null && colIndex < centerAreaStart)
                        targetCells = leftCells;
                    else if (rightCells != null && colIndex >= rightAreaStart)
                        targetCells = rightCells;
                    else
                        targetCells = cells;

                    BaseColumn column = columns.get(colIndex);
                    if (!column.isRendered())
                        throw new IllegalStateException("Only rendered columns are expected in columns list. " +
                                "column id: " + column.getId() + "; column index = " + colIndex);

                    String columnIndexVar = table.getColumnIndexVar();
                    String columnIdVar = table.getColumnIdVar();
                    Object prevColumnIndexVarValue = null;
                    Object prevColumnIdVarValue = null;
                    int originalColIndex = getOriginalColumnIndex(column);
                    if (columnIndexVar != null)
                        prevColumnIndexVarValue = requestMap.put(columnIndexVar, originalColIndex);
                    if (columnIdVar != null) {
                        String columnId = column.getId();
                        prevColumnIdVarValue = requestMap.put(columnIdVar, columnId);
                    }
                    BodyCell cell = renderCell(column, colIndex);
                    targetCells.add(cell);

                    if (columnIndexVar != null)
                        requestMap.put(columnIndexVar, prevColumnIndexVarValue);
                    if (columnIdVar != null)
                        requestMap.put(columnIdVar, prevColumnIdVarValue);

                    colIndex += cell.getSpan();
                }

                if (cellSelectableME != null) {
                    collectedSelectableCells.put(selectableCellsInColumn);
                }

                if (customRowRenderingInfo != null)
                    customRowRenderingInfos.put(rowIndex, customRowRenderingInfo);
            }

            private BodyCell renderCell(BaseColumn column, int colIndex) throws IOException {
                if (cellSelectableME != null) {
                    selectableCellsInColumn.put(cellSelectableME.invoke(elContext, null));
                }

                List<?> customCells = applicableCustomCells[colIndex];
                SpannedTableCell spannedTableCell =
                        customCells != null && customCells.size() == 1 && customCells.get(0) instanceof SpannedTableCell
                                ? (SpannedTableCell) customCells.get(0) : null;
                boolean remainingPortionOfABrokenSpannedCell = false;
                int span = 1;
                if (spannedTableCell != null) {
                    int testedColIndex = colIndex;
                    while (true) {
                        testedColIndex++;
                        if (testedColIndex == columnCount)
                            break;
                        List testedCells = applicableCustomCells[testedColIndex];
                        if (testedCells == null) break;
                        SpannedTableCell testedSpannedCell =
                                testedCells.size() == 1 && testedCells.get(0) instanceof SpannedTableCell
                                        ? (SpannedTableCell) testedCells.get(0) : null;
                        if (spannedTableCell != testedSpannedCell)
                            break;
                        span++;
                    }
                    column = spannedTableCell.getColumn();
                    customCells = spannedTableCell.getApplicableTableCells();
                    if (alreadyProcessedSpannedCells.contains(spannedTableCell))
                        remainingPortionOfABrokenSpannedCell = true;
                    else
                        alreadyProcessedSpannedCells.add(spannedTableCell);
                }

                setCurrentlyRenderedColumn(column);
                try {
                    UIComponent cellContentsContainer = applyCustomCellDefinitions(
                        column, colIndex, customCells, remainingPortionOfABrokenSpannedCell, span);

                    BodyCell cell = new BodyCell();
                    cell.setSpan(span);
                    cell.extractCustomEvents((List<UIComponent>) customCells);

                    String content = renderCellContent(column, remainingPortionOfABrokenSpannedCell, cellContentsContainer);
                    cell.setContent(content);
                    return cell;
                } finally {
                    setCurrentlyRenderedColumn(null);
                }

            }

            private String renderCellContent(
                    BaseColumn column,
                    boolean remainingPortionOfABrokenSpannedCell,
                    UIComponent cellContentsContainer) throws IOException {

                StringBuffer buf = stringWriter.getBuffer();
                int startIdx = buf.length();
                if (remainingPortionOfABrokenSpannedCell) {
                    if (tableStructure.isEmptyCellsTreatmentRequired())
                        Rendering.writeNonBreakableSpace(writer);
                } else {
                    renderCellContent(column, cellContentsContainer);
                }
                int endIdx = buf.length();
                return buf.substring(startIdx, endIdx);
            }

            private void renderCellContent(BaseColumn column, UIComponent cellContentsContainer) throws IOException {
                boolean renderCustomTreeCell = column instanceof TreeColumn && cellContentsContainer instanceof Cell;
                Column displayedColumn = cellContentsContainer instanceof Column
                        ? (Column) cellContentsContainer
                        : null;
                Object prevColumnValue = null;
                if (displayedColumn != null) {
                    ValueExpression valueExpression = displayedColumn.getValueExpression();
                    if (valueExpression != null) {
                        Object columnValue = valueExpression.getValue(elContext);
                        prevColumnValue = requestMap.put(Column.COLUMN_VALUE_VAR, columnValue);
                    }
                }

                if (!renderCustomTreeCell) {
                    cellContentsContainer.encodeAll(context);
                    if (!(column instanceof Column)) {
                        TableStructure.writeNonBreakableSpaceForEmptyCell(writer, table, cellContentsContainer);
                    }
                } else {
                    column.getAttributes().put(TreeColumnRenderer.ATTR_CUSTOM_CELL, cellContentsContainer);
                    try {
                        column.encodeAll(context);
                    } finally {
                        column.getAttributes().remove(TreeColumnRenderer.ATTR_CUSTOM_CELL);
                    }
                }

                requestMap.put(Column.COLUMN_VALUE_VAR, prevColumnValue);
            }

            private void collectSelectableCells(int columnCount) {
                if (selectableCellsME != null) {
                    boolean[] selectableFlagsForAllColumns = (boolean[]) selectableCellsME.invoke(elContext, null);
                    boolean[] selectableFlagsForRenderedColumns = new boolean[columnCount];
                    for (int i = 0, count = selectableFlagsForAllColumns.length; i < count; i++) {
                        final Integer renderedColIndex = renderedColIndexesByOriginalIndexes.get(i);
                        if (renderedColIndex != null) {
                            selectableFlagsForRenderedColumns[renderedColIndex] = selectableFlagsForAllColumns[i];
                        }
                    }
                    collectedSelectableCells.put(selectableFlagsForRenderedColumns);
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

            private List<Object>[] getApplicableCustomCells(
                    List<Row> applicableCustomRows, int bodyRowIndex, int columnCount) {
                Object rowData = table.getRowData();

                List<String> rowStyles = getApplicableRowStyles(context, customRows, table);
                String rowStyleClass = (rowStyles != null && rowStyles.size() > 0)
                        ? classNamesToClass(rowStyles)
                        : null;
                String additionalClass = tableStructure.getAdditionalRowClass(context, table, rowData, bodyRowIndex);
                if (additionalClass != null)
                    rowStyleClass = Styles.mergeClassNames(rowStyleClass, additionalClass);
                if (!Rendering.isNullOrEmpty(rowStyleClass)) {
                    Map<Object, String> rowStylesMap = tableStructure.getRowStylesMap();
                    rowStylesMap.put(bodyRowIndex, rowStyleClass);
                }


                List<Integer> applicableRowDeclarationIndexes = new ArrayList<Integer>();
                for (Row tableRow : applicableCustomRows) {
                    if (Rendering.isComponentWithA4jAjax(tableRow)) {
                        if (customRowRenderingInfo == null)
                            customRowRenderingInfo = new CustomRowRenderingInfo(columnCount);
                        Integer rowDeclarationIndex =
                                (Integer) tableRow.getAttributes().get(CUSTOM_ROW_INDEX_ATTRIBUTE);
                        if (rowDeclarationIndex == null)
                            throw new IllegalStateException("CUSTOM_ROW_INDEX_ATTRIBUTE can legally be null only for " +
                                    "the implicit grouping-related rows, which don't have any Ajax actions bound to " +
                                    "them, so they can't lead execution to this point in code");
                        applicableRowDeclarationIndexes.add(rowDeclarationIndex);
                    }
                }
                if (customRowRenderingInfo != null)
                    customRowRenderingInfo.setA4jEnabledRowDeclarationIndexes(applicableRowDeclarationIndexes);

                return prepareCustomCells(table, applicableCustomRows);
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

            private List<String> getApplicableRowStyles(FacesContext context, List<Row> customRows, AbstractTable table) {
                List<String> result = new ArrayList<String>();
                for (Row customRow : customRows) {
                    String cls = customRow.getStyleClassForRow(context, table);
                    if (cls != null)
                        result.add(cls);
                }
                return result;
            }

            /**
             * Takes the list of custom row specifications applicable to a row of interest (this is a list of TableRow
             * instances because several TableRow instances can be applicable to the same row), and constructs a list of
             * custom cell specifications applicable to the cells of this row.
             *
             * @param table                a table whose row is being analyzed
             * @param applicableCustomRows a list of TableRow instances applicable to the current row
             * @return An array the size of number of rendered columns. Each array entry corresponds to each rendered
             *         cell. Each array entry contains one of the following: (1) null - means that no custom cells are
             *         applicable for this cell and it will be rendered as usual; (2) List of TableCell instances
             *         applicable to this cell; (3) List containing only one SpannedTableCell instance if an appropriate
             *         cell is part of a cell span - all cells referring to the same SpannedTableCell will be rendered
             *         as one cell spanning across several columns.
             */
            private List<Object>[] prepareCustomCells(AbstractTable table, List<Row> applicableCustomRows) {
                List<BaseColumn> allColumns = table.getAllColumns();
                List<BaseColumn> columnsForRendering = table.getAdaptedRenderedColumns();
                int allColCount = allColumns.size();
                for (int i = 0; i < allColCount; i++) {
                    UIComponent col = allColumns.get(i);
                    col.getAttributes().put(COLUMN_ATTR_ORIGINAL_INDEX, i);
                }

                int visibleColCount = columnsForRendering.size();
                List[] rowCellsByAbsoluteIndex = new List[allColCount];

                boolean thereAreCellSpans = false;
                List<Cell> rowCellsByColReference = new ArrayList<Cell>();
                for (Row row : applicableCustomRows) {
                    Integer customRowIndex = (Integer) row.getAttributes().get(CUSTOM_ROW_INDEX_ATTRIBUTE);
                    List<UIComponent> children = row.getChildren();
                    int freeCellIndex = 0;
                    int customCellIndex = 0;
                    for (UIComponent child : children) {
                        if (!(child instanceof Cell))
                            continue;
                        Cell cell = (Cell) child;

                        cell.getAttributes().put(CUSTOM_CELL_RENDERING_INFO_ATTRIBUTE,
                                new CustomContentCellRenderingInfo(
                                        customRowIndex != null ? customRowIndex : -1,
                                        customCellIndex++));
                        int span = cell.getSpan();
                        Object columnIds = cell.getColumnIds();
                        ValueExpression conditionExpression = cell.getConditionExpression();
                        if (span < 1)
                            throw new IllegalArgumentException("The value of 'span' attribute of <o:cell> tag can't " +
                                    "be less than 1, but encountered: " + span);

                        if (span != 1) {
                            thereAreCellSpans = true;
                        }

                        if (columnIds == null && conditionExpression == null) {
                            int thisColIndex = freeCellIndex;
                            freeCellIndex += span;
                            if (thisColIndex >= allColCount)
                                throw new FacesException("The number of free cells (cells without 'column' attribute)" +
                                        " inside of <o:row> tag should not be greater than the total number of " +
                                        "columns in a table (" + allColCount + ")");
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
                    for (int i = 0; i < allColCount; i++) {
                        BaseColumn col = allColumns.get(i);
                        for (Cell cell : rowCellsByColReference) {
                            if (isColumnApplicable(cell, table, col, i)) {
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
                        boolean groupHeaderCell = false;
                        for (Object cellObj : customCellList) {
                            Cell cell = (Cell) cellObj;
                            groupHeaderCell |= GroupHeaderOrFooterRow.isSyntheticGroupHeaderCell(cell);

                            int currentCellSpan = cell.getSpan();
                            if (currentCellSpan != 1)
                                cellSpan = currentCellSpan;
                        }
                        if (cellSpan == 1)
                            continue;
                        BaseColumn column = allColumns.get(i);
                        if (groupHeaderCell) {
                            // when table row grouping is turned on, the automatically generated implicit tree column is
                            // always generated to be the first rendered column, and spannedTableCell.getColumn() refers
                            // to the first column in the allColumns list, and so it always refers to the first column
                            // declared in the table's markup, which might actually be different than the first column
                            // in the renderedColumns list. Hence we're fixing this so that tree column was properly
                            // rendered on group header cells
                            TreeColumn treeColumn = null;
                            for (BaseColumn c : columnsForRendering) {
                                if (c instanceof TreeColumn) {
                                    treeColumn = (TreeColumn) c;
                                    break;
                                }
                            }
                            if (treeColumn == null)
                                throw new IllegalStateException("There must be an implicitly generated tree column " +
                                        "in the grouped table");
                            column = treeColumn;
                        }
                        SpannedTableCell spannedTableCell = new SpannedTableCell(column, customCellList);
                        for (int cellIndex = i; cellIndex < i + cellSpan; cellIndex++) {
                            rowCellsByAbsoluteIndex[cellIndex] = Collections.singletonList(spannedTableCell);
                        }
                        i += cellSpan - 1;
                    }
                }

                List[] applicableCells = new List[visibleColCount];
                for (int i = 0; i < visibleColCount; i++) {
                    BaseColumn column = columnsForRendering.get(i);
                    int originalColIndex = getOriginalColumnIndex(column);
                    applicableCells[i] = rowCellsByAbsoluteIndex[originalColIndex];
                }
                return (List<Object>[]) applicableCells;
            }

            private boolean isColumnApplicable(
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
                        throw new IllegalArgumentException("'columnIds' attribute of <o:cell> tag should contain " +
                                "either an array or a collection of column id strings, but a value of the following " +
                                "type encountered: " + columnIds.getClass().getName());
                    String colId = column.getId();
                    boolean result = columnIdsCollection.contains(colId);
                    return result;
                }

                ValueExpression conditionExpression = cell.getConditionExpression();

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

                Boolean result = (Boolean) conditionExpression.getValue(elContext);

                if (columnIndexVar != null)
                    requestMap.put(columnIndexVar, prevColumnIndexVarValue);
                if (columnIdVar != null)
                    requestMap.put(columnIdVar, prevColumnIdVarValue);

                return (result == null) || result;
            }

            private int getOriginalColumnIndex(BaseColumn column) {
                if (column instanceof GroupingStructureColumn) {
                    BaseColumn delegate = ((GroupingStructureColumn) column).getDelegate();
                    return getOriginalColumnIndex(delegate);
                }
                return (Integer) column.getAttributes().get(COLUMN_ATTR_ORIGINAL_INDEX);
            }

            private UIComponent applyCustomCellDefinitions(
                    BaseColumn column, int colIndex,
                    List<?> customCells,
                    boolean remainingPortionOfABrokenSpannedCell, int span) {

                UIComponent cellContentsContainer = column;
                if (customCells == null) return cellContentsContainer;

                String customCellStyle = null;
                String columnId = column.getId();
                for (Object customCell : customCells) {
                    Cell cell = (Cell) customCell;
                    Runnable exitContext = cell instanceof ContextDependentComponent
                            ? ((ContextDependentComponent) cell).enterComponentContext() : null;
                    boolean cellWithCustomContent = cell.getChildCount() > 0;
                    if (exitContext != null) exitContext.run();
                    if (cellWithCustomContent || Rendering.isComponentWithA4jAjax(cell)) {
                        if (customRowRenderingInfo == null)
                            customRowRenderingInfo = new CustomRowRenderingInfo(columnCount);
                        if (cellWithCustomContent)
                            cellContentsContainer = cell;
                        CustomContentCellRenderingInfo customCellRenderingInfo = (CustomContentCellRenderingInfo)
                                cell.getAttributes().get(CUSTOM_CELL_RENDERING_INFO_ATTRIBUTE);
                        customRowRenderingInfo.setCustomCellRenderingInfo(colIndex, customCellRenderingInfo);
                    }
                    customCellStyle = Styles.mergeClassNames(
                            customCellStyle,
                            cell.getStyleClassForCell(context, table, colIndex, columnId));
                }
                for (int i = colIndex + (remainingPortionOfABrokenSpannedCell ? 0 : 1), upperBound = colIndex + span;
                     i < upperBound; i++) {
                    if (customRowRenderingInfo == null)
                        customRowRenderingInfo = new CustomRowRenderingInfo(columnCount);
                    customRowRenderingInfo.setCustomCellRenderingInfo(i, new MergedCellRenderingInfo());
                }

                if (customCellStyle != null) {
                    customCellStyle = customCellStyle.trim();
                    if (customCellStyle.length() != 0) {
                        Map<Object, String> cellStylesMap = tableStructure.getCellStylesMap();
                        cellStylesMap.put(bodyRowIndex + "x" + colIndex, customCellStyle);
                    }
                }

                return cellContentsContainer;
            }

        }
    }
}
