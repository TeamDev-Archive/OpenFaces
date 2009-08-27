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
import org.openfaces.component.table.*;
import org.openfaces.org.json.JSONArray;
import org.openfaces.renderkit.DefaultTableStyles;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.EnvironmentUtil;
import org.openfaces.util.StyleGroup;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractTableRenderer extends RendererBase {
    public static final String CUSTOM_ROW_RENDERING_INFOS_KEY = "_customRowRenderingInfos";

    private static final String COLUMN_ATTR_ORIGINAL_INDEX = "_originalIndex";

    private static final String DEFAULT_STYLE_CLASS = "o_table";
    private static final String DEFAULT_CELL_PADDING = "2";

    private static final String DEFAULT_SORTED_COLUMN_CLASS = null;//"o_table_sorted_column";
    private static final String DEFAULT_SORTED_COLUMN_HEADER_CLASS = "o_table_sorted_column_header";
    private static final String DEFAULT_SORTED_COLUMN_BODY_CLASS = "o_table_sorted_column_body";
    private static final String DEFAULT_SORTED_COLUMN_FOOTER_CLASS = "o_table_sorted_column_footer";
    private static final String DEFAULT_SORTABLE_HEADER_CLASS = "o_table_sortable_header";
    private static final String DEFAULT_SORTABLE_HEADER_ROLLOVER_CLASS = null;//"o_table_sortable_header_rollover";
    private static final String DEFAULT_NO_RECORDS_MSG = "No records";
    private static final String DEFAULT_NO_FILTER_RECORDS_MSG = "No records satisfying the filtering criteria";
    private static final String DEFAULT_NO_DATA_ROW_CLASS = "o_table_no_data_row";
    private static final String DEFAULT_FOCUSED_STYLE = "border: 1px dotted black;";

    private static TableStyles DEFAULT_STYLES = new DefaultTableStyles();
    public static final String CUSTOM_ROW_INDEX_ATTRIBUTE = "_customRowIndex";
    public static final String CUSTOM_CELL_INDEX_ATTRIBUTE = "_customCellIndex";
    private static final String CUSTOM_CELL_RENDERING_INFO_ATTRIBUTE = "_customCellRenderingInfo";
    private static final String TABLE_STRUCTURE_ATTR = "_of_tableStructure";
    private static final String TABLE_LAYOUT_FIXED_STYLE_CLASS = "o_table_layout_fixed";


    public static String getTableJsURL(FacesContext facesContext) {
        return ResourceUtil.getInternalResourceURL(facesContext, AbstractTableRenderer.class, "table.js");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        AbstractTable table = (AbstractTable) component;
        if (table.getUseAjax())
            AjaxUtil.prepareComponentForAjax(context, component);

        // this hack is needed for working around strange IE issue
        // JSFC-2081 Filter drop-downs in TreeTable have improper style on demo (regression) - IE only
        encodeJsLinks(context);

        table.setRowIndex(-1);
        UIComponent aboveFacet = table.getFacet("above");
        if (aboveFacet != null)
            aboveFacet.encodeAll(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", table);
        writeIdAttribute(context, table);

        List<BaseColumn> columns = table.getColumnsForRendering();

        String style = table.getStyle();
        String textStyle = getTextStyle(table);
        style = StyleUtil.mergeStyles(style, textStyle);
        boolean applyDefaultStyle = table.getApplyDefaultStyle();
        String styleClass = TableUtil.getClassWithDefaultStyleClass(applyDefaultStyle, DEFAULT_STYLE_CLASS, table.getStyleClass());
        ColumnResizing columnResizing = table.getColumnResizing();
        String tableWidth = table.getWidth();
        if (columnResizing != null) {
            ColumnResizingState resizingState = columnResizing.getResizingState();
            if (resizingState != null && resizingState.getColumnCount() == columns.size())
                tableWidth = resizingState.getTableWidth();
            if (columnResizing.isEnabled()) {
                if (tableWidth != null || EnvironmentUtil.isMozilla()) {
                    // "table-layout: fixed" style can't be set on client-side and should be rendered from the server, though
                    // it shouldn't be rendered under IE because all tables without explicit widths will occupy 100% width as a result
                    styleClass = StyleUtil.mergeClassNames(styleClass, TABLE_LAYOUT_FIXED_STYLE_CLASS);
                }
            }
        }
        String textClass = getTextClass(table);
        styleClass = StyleUtil.mergeClassNames(styleClass, textClass);
        String rolloverStyle = table.getRolloverStyle();
        if (RenderingUtil.isNullOrEmpty(rolloverStyle)) {
            styleClass = StyleUtil.mergeClassNames(DefaultStyles.CLASS_INITIALLY_INVISIBLE, styleClass);
            RenderingUtil.writeStyleAndClassAttributes(writer, style, styleClass);
        } else {
            String cls = StyleUtil.getCSSClass(context, component, style, styleClass);
            if (!EnvironmentUtil.isOpera())
                cls = StyleUtil.mergeClassNames(DefaultStyles.CLASS_INITIALLY_INVISIBLE, cls);
            if (!RenderingUtil.isNullOrEmpty(cls))
                writer.writeAttribute("class", cls, null);
        }

        writeAttribute(writer, "align", table.getAlign());
        writeAttribute(writer, "bgcolor", table.getBgcolor());
        writeAttribute(writer, "dir", table.getDir());
        writeAttribute(writer, "rules", table.getRules());
        writeAttribute(writer, "width", tableWidth);
        writeAttribute(writer, "border", table.getBorder(), Integer.MIN_VALUE);
        String cellspacing = table.getCellspacing();
        if (TableUtil.areGridLinesRequested(table, getDefaultStyles(table)))
            cellspacing = "0";
        writeAttribute(writer, "cellspacing", cellspacing);
        String cellpadding = table.getCellpadding();
        if (cellpadding == null && applyDefaultStyle)
            cellpadding = DEFAULT_CELL_PADDING;
        writeAttribute(writer, "cellpadding", cellpadding);
        writeAttribute(writer, "onclick ", table.getOnclick());
        writeAttribute(writer, "ondblclick", table.getOndblclick());
        writeAttribute(writer, "onmousedown", table.getOnmousedown());
        writeAttribute(writer, "onmouseover", table.getOnmouseover());
        writeAttribute(writer, "onmousemove", table.getOnmousemove());
        writeAttribute(writer, "onmouseout", table.getOnmouseout());
        writeAttribute(writer, "onmouseup", table.getOnmouseup());

        writeKeyboardEvents(writer, table);

        TableUtil.writeColumnTags(context, table, columns);

        TableStructure tableStructure = new TableStructure(table, table);
        TableHeader header = tableStructure.getHeader();
        if (!header.isEmpty())
            header.render(context, null);
        table.getAttributes().put(TABLE_STRUCTURE_ATTR, tableStructure);
    }

    private void encodeJsLinks(FacesContext context) throws IOException {
        String[] libs = getNecessaryJsLibs(context);
        for (String lib : libs) {
            ResourceUtil.renderJSLinkIfNeeded(lib, context);
        }
    }

    protected String getTextClass(AbstractTable table) {
        return null;
    }

    protected String getTextStyle(AbstractTable table) {
        return null;
    }

    protected void writeKeyboardEvents(ResponseWriter writer, AbstractTable table) throws IOException {
        writeAttribute(writer, "onfocus", table.getOnfocus());
        writeAttribute(writer, "onblur", table.getOnblur());
        writeAttribute(writer, "onkeydown", table.getOnkeydown());
        writeAttribute(writer, "onkeyup", table.getOnkeyup());
        writeAttribute(writer, "onkeypress", table.getOnkeypress());
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        if (!uiComponent.isRendered())
            return;

        AbstractTable table = ((AbstractTable) uiComponent);
        ResponseWriter writer = facesContext.getResponseWriter();
        List<BaseColumn> columnsForRendering = table.getColumnsForRendering();

        writer.startElement("tbody", table);
        RenderingUtil.writeStyleAndClassAttributes(writer, table.getBodySectionStyle(), table.getBodySectionClass());
        writeNewLine(writer);

        int first = table.getFirst();
        int rows = table.getRows();
        if (rows == 0) {
            int rowCount = table.getRowCount();
            rows = (rowCount != -1) ? rowCount : Integer.MAX_VALUE;
        }
        encodeRows(facesContext, table, first, rows, columnsForRendering);

        writeNewLine(writer);
        writer.endElement("tbody");
        writeNewLine(writer);
    }

    protected void encodeRows(
            FacesContext facesContext,
            AbstractTable table,
            int firstRowIndex,
            int rowCount,
            List<BaseColumn> columnsForRendering,
            boolean renderInitScripts) throws IOException {
        Map<Serializable, String> rowStylesMap = new HashMap<Serializable, String>();
        Map<Serializable, String> cellStylesMap = new HashMap<Serializable, String>();
        boolean thereAreRows = rowCount > 0;
        if (thereAreRows)
            encodeDataRows(facesContext, table, firstRowIndex, rowCount, columnsForRendering, rowStylesMap, cellStylesMap);
        else
            encodeNoDataRow(facesContext, table, columnsForRendering, rowStylesMap);
        if (!thereAreRows && !table.getNoDataMessageAllowed()) {
            encodeFakeRow(facesContext, table, columnsForRendering);
        }
        String rowStylesKey = getRowStylesKey(facesContext, table);
        String cellStylesKey = getCellStylesKey(facesContext, table);
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        requestMap.put(rowStylesKey, rowStylesMap);
        requestMap.put(cellStylesKey, cellStylesMap);

        TableStructure tableStructure = getTableStructure(table);
        TableFooter footer = tableStructure != null ? tableStructure.getFooter() : null;
        boolean hasFooter = footer != null && !footer.isEmpty();
        if (renderInitScripts && !hasFooter) {
            encodeScriptsAndStyles(facesContext, table);
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement("td");
        writer.endElement("tr");
    }

    private void encodeFakeRow(FacesContext facesContext, AbstractTable table, List<BaseColumn> columns) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("tr", table);
        writer.startElement("td", table);
        int allColCount = columns.size();
        writer.writeAttribute("colspan", String.valueOf(allColCount), null);
        RenderingUtil.writeStyleAndClassAttributes(writer, "display:none;", null);
    }

    protected void encodeRows(
            FacesContext facesContext,
            AbstractTable table,
            int firstRowIndex,
            int rowCount,
            List<BaseColumn> columnsForRendering) throws IOException {
        encodeRows(facesContext, table, firstRowIndex, rowCount, columnsForRendering, true);
    }

    private void encodeNoDataRow(FacesContext facesContext, AbstractTable table, List<BaseColumn> columns, Map<Serializable, String> rowStylesMap) throws IOException {
        if (!table.getNoDataMessageAllowed())
            return;
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("tr", table);
        writer.startElement("td", table);
        int allColCount = columns.size();
        if (allColCount != 0)
            writer.writeAttribute("colspan", String.valueOf(allColCount), null);
        RenderingUtil.writeStyleAndClassAttributes(writer, table.getNoDataRowStyle(), table.getNoDataRowClass());
        boolean dataSourceEmpty = table.isDataSourceEmpty();
        UIComponent noDataMessage = dataSourceEmpty ? table.getNoDataMessage() : table.getNoFilterDataMessage();
        if (noDataMessage != null) {
            noDataMessage.encodeAll(facesContext);
        } else {
            String message = (dataSourceEmpty)
                    ? DEFAULT_NO_RECORDS_MSG
                    : DEFAULT_NO_FILTER_RECORDS_MSG;
            writer.writeText(message, null);
        }

        rowStylesMap.put(0, getNoDataRowClassName(facesContext, table));
    }

    private String getNoDataRowClassName(FacesContext facesContext, AbstractTable table) {
        String styleClassesStr = StyleUtil.getCSSClass(
                facesContext, table, table.getNoDataRowStyle(), table.getApplyDefaultStyle() ? DEFAULT_NO_DATA_ROW_CLASS : null, table.getNoDataRowClass()
        );
        return styleClassesStr;
    }

    private void encodeDataRows(FacesContext facesContext, AbstractTable table,
                                int firstRowIndex, int rowsToRender,
                                List<BaseColumn> columnsForRendering, Map<Serializable, String> rowStylesMap, Map<Serializable, String> cellStylesMap) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        List<TableRow> customRows = getCustomRows(table);
        int lastRowIndex = firstRowIndex + rowsToRender - 1;
        Map<Integer, CustomRowRenderingInfo> customRowRenderingInfos = (Map<Integer, CustomRowRenderingInfo>) table.getAttributes().get(CUSTOM_ROW_RENDERING_INFOS_KEY);

        for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
            table.setRowIndex(rowIndex);
            if (!table.isRowAvailable())
                break;
            writer.startElement("tr", table);
            List<TableRow> applicableCustomRows = getApplicableCustomRows(customRows);
            writeCustomRowOrCellEvents(writer, applicableCustomRows);

            writeBodyRowAttributes(facesContext, table);
            Object rowData = table.getRowData();
            int bodyRowIndex = rowIndex - firstRowIndex;

            List<String> rowStyles = getApplicableRowStyles(facesContext, customRows, table);
            String rowStyleClass = (rowStyles != null && rowStyles.size() > 0) ? classNamesToClass(rowStyles) : null;
            String additionalClass = getAdditionalRowClass(facesContext, table, rowData, bodyRowIndex);
            if (additionalClass != null)
                rowStyleClass = StyleUtil.mergeClassNames(rowStyleClass, additionalClass);
            if (!RenderingUtil.isNullOrEmpty(rowStyleClass))
                rowStylesMap.put(bodyRowIndex, rowStyleClass);

            int columnCount = columnsForRendering.size();

            CustomRowRenderingInfo customRowRenderingInfo = null;
            List<Integer> applicableRowDeclarationIndexes = new ArrayList<Integer>();
            for (TableRow tableRow : applicableCustomRows) {
                if (RenderingUtil.isComponentWithA4jSupport(tableRow)) {
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

            for (int colIndex = 0; colIndex < columnCount;) {
                BaseColumn column = columnsForRendering.get(colIndex);
                if (!column.isRendered())
                    throw new IllegalStateException("Only rendered columns are expected in columns list. column id: " + column.getId() + "; column index = " + colIndex);

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
                        TableCell cell = (TableCell) customCells.get(i);
                        boolean cellWithCustomContent = cell.getChildCount() > 0;
                        if (cellWithCustomContent || RenderingUtil.isComponentWithA4jSupport(cell)) {
                            if (customRowRenderingInfo == null)
                                customRowRenderingInfo = new CustomRowRenderingInfo(columnCount);
                            if (cellWithCustomContent)
                                cellContentsContainer = cell;
                            CustomContentCellRenderingInfo customCellRenderingInfo =
                                    (CustomContentCellRenderingInfo) cell.getAttributes().get(CUSTOM_CELL_RENDERING_INFO_ATTRIBUTE);
                            customRowRenderingInfo.setCustomCellRenderingInfo(colIndex, customCellRenderingInfo);
                        }
                        customCellStyle = StyleUtil.mergeClassNames(customCellStyle, cell.getStyleClassForCell(facesContext, table, colIndex, columnId));
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
                    if (!RenderingUtil.isNullOrEmpty(styleClass))
                        cellStylesMap.put(bodyRowIndex + "x" + colIndex, styleClass);
                }

                writer.startElement("td", table);
                if (span > 1)
                    writer.writeAttribute("colspan", String.valueOf(span), null);
                writeCustomRowOrCellEvents(writer, customCells);
                if (!remainingPortionOfBrokenSpannedCell) {
                    boolean renderCustomTreeCell = column instanceof TreeColumn && cellContentsContainer instanceof TableCell;
                    if (renderCustomTreeCell) {
                        column.getAttributes().put(TreeColumnRenderer.ATTR_CUSTOM_CELL, cellContentsContainer);
                        try {
                            column.encodeAll(facesContext);
                        } finally {
                            column.getAttributes().remove(TreeColumnRenderer.ATTR_CUSTOM_CELL);
                        }
                    } else {
                        cellContentsContainer.encodeAll(facesContext);
                    }
                    writeNonBreakableSpaceForEmptyCell(writer, table, cellContentsContainer);
                } else {
                    if (isEmptyCellsTreatmentRequired(table))
                        RenderingUtil.writeNonBreakableSpace(writer);
                }

                boolean lastRowColumn = rowIndex == lastRowIndex && colIndex == columnCount - span;
                if (lastRowColumn) { // return before rendering last </td></tr>
                    table.setRowIndex(-1);
                    if (customRowRenderingInfo != null)
                        customRowRenderingInfos.put(rowIndex, customRowRenderingInfo);
                    return;
                }

                writer.endElement("td");
                colIndex += span;
            }

            if (customRowRenderingInfo != null)
                customRowRenderingInfos.put(rowIndex, customRowRenderingInfo);

            writer.endElement("tr");
        }
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
    private List[] prepareCustomCells(AbstractTable table, List<TableRow> applicableCustomRows) {
        List<BaseColumn> allColumns = table.getAllColumns();
        List<BaseColumn> columnsForRendering = table.getColumnsForRendering();
        int allColCount = allColumns.size();
        for (int i = 0; i < allColCount; i++) {
            UIComponent col = allColumns.get(i);
            col.getAttributes().put(COLUMN_ATTR_ORIGINAL_INDEX, i);
        }

        int visibleColCount = columnsForRendering.size();
        List[] rowCellsByAbsoluteIndex = new List[allColCount];

        boolean thereAreCellSpans = false;
        List<TableCell> rowCellsByColReference = new ArrayList<TableCell>();
        for (int i = 0, icount = applicableCustomRows.size(); i < icount; i++) {
            TableRow row = applicableCustomRows.get(i);
            int customRowIndex = (Integer) row.getAttributes().get(CUSTOM_ROW_INDEX_ATTRIBUTE);
            List<UIComponent> children = row.getChildren();
            int freeCellIndex = 0;
            int customCellIndex = 0;
            for (int j = 0, jcount = children.size(); j < jcount; j++) {
                Object child = children.get(j);
                if (!(child instanceof TableCell))
                    continue;
                TableCell cell = (TableCell) child;

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
                    List<TableCell> applicableCells = rowCellsByAbsoluteIndex[thisColIndex];
                    if (applicableCells == null) {
                        applicableCells = new ArrayList<TableCell>();
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
                for (TableCell cell : rowCellsByColReference) {
                    if (isColumnApplicable(context, cell, table, col, i)) {
                        List<TableCell> applicableCells = rowCellsByAbsoluteIndex[i];
                        if (applicableCells == null) {
                            applicableCells = new ArrayList<TableCell>();
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
                    TableCell cell = (TableCell) customCellList.get(j);

                    int currentCellSpan =  cell.getSpan();
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

    private boolean isColumnApplicable(
            FacesContext context,
            TableCell cell,
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

    private int calculateFreeCells(List<UIComponent> children) {
        int freeCellCount = 0;
        for (UIComponent child : children) {
            if (!(child instanceof TableCell))
                continue;
            TableCell cell = (TableCell) child;
            Object columnIds = cell.getColumnIds();
            ValueExpression conditionExpression = cell.getConditionExpression();
            if (columnIds == null && conditionExpression == null)
                freeCellCount++;
        }
        return freeCellCount;
    }

    private void writeNonBreakableSpaceForEmptyCell(ResponseWriter writer, AbstractTable table, UIComponent cellComponentsContainer) throws IOException {
        if (cellComponentsContainer instanceof TableColumn || cellComponentsContainer instanceof TableCell) {
            List<UIComponent> children = cellComponentsContainer.getChildren();
            boolean childrenEmpty = true;
            for (int childIndex = 0, childCount = children.size(); childIndex < childCount; childIndex++) {
                UIComponent child = children.get(childIndex);
                if (!isComponentEmpty(child)) {
                    childrenEmpty = false;
                    break;
                }
            }
            if (childrenEmpty && isEmptyCellsTreatmentRequired(table))
                RenderingUtil.writeNonBreakableSpace(writer);
        }
    }

    private List<TableRow> getApplicableCustomRows(List<TableRow> customRows) {
        List<TableRow> applicableRows = new ArrayList<TableRow>(customRows.size());
        for (TableRow tableRow : customRows) {
            if (tableRow.getCondition())
                applicableRows.add(tableRow);
        }
        return applicableRows;
    }

    private void writeCustomRowOrCellEvents(ResponseWriter writer, List<? extends UIComponent> customRowsOrCells) throws IOException {
        if (customRowsOrCells == null || customRowsOrCells.size() == 0)
            return;
        String[] eventNames = new String[]{
                "onclick", "ondblclick", "onmousedown", "onmouseover", "onmousemove",
                "onmouseout", "onmouseup", "onkeydown", "onkeyup", "onkeypress"};

        for (String eventName : eventNames) {
            String compoundEventHandler = null;
            for (UIComponent customRowOrCell : customRowsOrCells) {
                String eventHandler = (String) customRowOrCell.getAttributes().get(eventName);
                compoundEventHandler = RenderingUtil.joinScripts(compoundEventHandler, eventHandler);
            }
            if (compoundEventHandler != null && compoundEventHandler.length() > 0)
                writer.writeAttribute(eventName, compoundEventHandler, null);
        }
    }

    protected String getAdditionalRowClass(FacesContext facesContext, AbstractTable table, Object rowData, int rowIndex) {
        return null;
    }

    protected void writeBodyRowAttributes(FacesContext facesContext, AbstractTable table) throws IOException {
    }

    protected static String getRowStylesKey(FacesContext context, AbstractTable table) {
        String clientId = table.getClientId(context);
        return "OF:row-styles-map-for::" + clientId;
    }

    protected static String getCellStylesKey(FacesContext context, AbstractTable table) {
        String clientId = table.getClientId(context);
        return "OF:cell-styles-map-for::" + clientId;
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

    private List<TableRow> getCustomRows(AbstractTable table) {
        List<TableRow> customRows = new ArrayList<TableRow>();
        List<UIComponent> children = table.getChildren();
        int customRowIndex = 0;
        int customCellIndex = 0;
        for (UIComponent child : children) {
            if (child instanceof TableRow) {
                TableRow customRow = (TableRow) child;
                customRows.add(customRow);
                customRow.getAttributes().put(CUSTOM_ROW_INDEX_ATTRIBUTE, customRowIndex++);
                List<UIComponent> customRowChildren = customRow.getChildren();
                for (UIComponent rowChild : customRowChildren) {
                    if (rowChild instanceof TableCell) {
                        TableCell customCell = (TableCell) rowChild;
                        customCell.getAttributes().put(CUSTOM_CELL_INDEX_ATTRIBUTE, customCellIndex++);
                    }
                }
            }
        }
        return customRows;
    }

    private List<String> getApplicableRowStyles(FacesContext context, List<TableRow> customRows, AbstractTable table) {
        List<String> result = new ArrayList<String>();
        for (TableRow customRow : customRows) {
            String cls = customRow.getStyleClassForRow(context, table);
            if (cls != null)
                result.add(cls);
        }
        return result;
    }

    static boolean isEmptyCellsTreatmentRequired(UIComponent component) {
        if (!(component instanceof AbstractTable))
            return true;
        AbstractTable table = (AbstractTable) component;
        return (TableUtil.areGridLinesRequested(table, getDefaultStyles(table)) || table.getBorder() != Integer.MIN_VALUE);
    }

    static boolean isComponentEmpty(UIComponent child) {
        if (child == null || !child.isRendered())
            return true;
        if (!(child instanceof HtmlOutputText))
            return false;
        HtmlOutputText outputText = (HtmlOutputText) child;
        Object value = outputText.getValue();
        boolean result = (value == null || value.toString().trim().length() == 0);
        return result;
    }

    private TableStructure getTableStructure(AbstractTable table) {
        return (TableStructure) table.getAttributes().get(TABLE_STRUCTURE_ATTR);
    }

    @Override
    public void encodeEnd(final FacesContext facesContext, UIComponent uiComponent) throws IOException {
        if (!uiComponent.isRendered())
            return;
        final AbstractTable table = ((AbstractTable) uiComponent);
        table.setRowIndex(-1);

        TableFooter footer = getTableStructure(table).getFooter();
        if (!footer.isEmpty())
            footer.render(facesContext, new HeaderCell.AdditionalContentWriter() {
                public void writeAdditionalContent(FacesContext context) throws IOException {
                    encodeScriptsAndStyles(facesContext, table);
                }
            });
        table.getAttributes().remove(TABLE_STRUCTURE_ATTR);


        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement("table");
        writeNewLine(writer);
        UIComponent belowFacet = table.getFacet("below");
        if (belowFacet != null)
            belowFacet.encodeAll(facesContext);
    }

    protected void encodeScriptsAndStyles(FacesContext facesContext, AbstractTable table) throws IOException {
        encodeAdditionalFeatureSupport(facesContext, table);
        StyleUtil.renderStyleClasses(facesContext, table);
    }

    protected void encodeAdditionalFeatureSupport(FacesContext facesContext, AbstractTable table) throws IOException {
        ScriptBuilder buf = new ScriptBuilder();

        encodeAdditionalFeaturesSupport_buf(facesContext, table, buf);

        AbstractTableSelection selection = table.getSelection();
        if (selection != null)
            selection.registerSelectionStyle(facesContext);

        StyleUtil.renderStyleClasses(facesContext, table); // encoding styles before scripts is important for tableUtil.js to be able to compute row and column styles correctly

        String[] libs = getNecessaryJsLibs(facesContext);
        RenderingUtil.renderInitScript(facesContext, buf, libs);

        if (selection != null)
            selection.encodeAll(facesContext);

        ColumnResizing columnResizing = table.getColumnResizing();
        if (columnResizing != null)
            columnResizing.encodeAll(facesContext);
    }

    protected void encodeAdditionalFeaturesSupport_buf(FacesContext facesContext, AbstractTable table, ScriptBuilder buf) throws IOException {
        encodeInitialization(facesContext, table, buf);
        encodeKeyboardSupport(facesContext, table, buf);
        encodeSortingSupport(facesContext, table, buf);

        if (!table.isDataSourceEmpty())
            preregisterNoFilterDataRowStyleForOpera(facesContext, table);

        encodeCheckboxColumnSupport(facesContext, table, buf);
    }

    private void preregisterNoFilterDataRowStyleForOpera(FacesContext facesContext, AbstractTable table) {
        if (EnvironmentUtil.isOpera() || EnvironmentUtil.isUndefinedBrowser())
            getNoDataRowClassName(facesContext, table);
    }

    private void encodeInitialization(
            FacesContext facesContext,
            AbstractTable table,
            ScriptBuilder buf) throws IOException {
        Map requestMap = facesContext.getExternalContext().getRequestMap();
        String rowStylesKey = getRowStylesKey(facesContext, table);
        Map rowStylesMap = (Map) requestMap.get(rowStylesKey);
        String cellStylesKey = getCellStylesKey(facesContext, table);
        Map cellStylesMap = (Map) requestMap.get(cellStylesKey);

        List<BaseColumn> columns = table.getColumnsForRendering();
        TableStructure tableStructure = getTableStructure(table);
        boolean noDataRows = table.getRowCount() == 0;
        TableStyles defaultStyles = getDefaultStyles(table);

        buf.initScript(facesContext, table, "O$.Table._init",
                TableUtil.getStructureAndStyleParams(
                        facesContext, table, defaultStyles, table, rowStylesMap, cellStylesMap,
                        columns, tableStructure, noDataRows),
                table.getUseAjax(),
                StyleUtil.getCSSClass(facesContext, table, table.getRolloverStyle(),
                        StyleGroup.rolloverStyleGroup(), table.getRolloverClass()),
                getInitJsAPIFunctionName());
    }

    public static TableStyles getDefaultStyles(TableStyles table) {
        return table.getApplyDefaultStyle() ? DEFAULT_STYLES : null;
    }

    protected String getInitJsAPIFunctionName() {
        return "";
    }

    protected String[] getNecessaryJsLibs(FacesContext context) {
        return new String[]{
                ResourceUtil.getUtilJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                getTableJsURL(context)};
    }

    private void encodeKeyboardSupport(FacesContext facesContext, AbstractTable table, ScriptBuilder buf) throws IOException {
        boolean focusable = isKeyboardNavigationApplicable(table);
        if (!focusable)
            return;

        Boolean focusedAttr = (Boolean) table.getAttributes().get("focused");
        ResponseWriter writer = facesContext.getResponseWriter();
        String focusFieldName = getFocusFieldName(facesContext, table);
        String focused = String.valueOf(focusedAttr != null && focusedAttr);
        RenderingUtil.renderHiddenField(writer, focusFieldName, focused);
        boolean tableIsPaginated = getUseKeyboardForPagination(table);
        boolean applyDefaultStyle = table.getApplyDefaultStyle();
        String focusedClass = StyleUtil.getCSSClass_dontCascade(
                facesContext, table, table.getFocusedStyle(), StyleGroup.selectedStyleGroup(), table.getFocusedClass(),
                applyDefaultStyle ? DEFAULT_FOCUSED_STYLE : null);

        boolean canPageBack = tableIsPaginated && canPageBack(table);
        boolean canPageForth = tableIsPaginated && canPageForth(table);
        boolean canSelectLastPage = tableIsPaginated && canSelectLastPage(table);
        buf.initScript(facesContext, table, "O$.Table._initKeyboardNavigation",
                tableIsPaginated,
                focusedClass,
                canPageBack,
                canPageForth,
                canSelectLastPage,
                table.getTabindex());
    }

    protected boolean canSelectLastPage(AbstractTable table) {
        return false;
    }

    protected boolean canPageForth(AbstractTable table) {
        return false;
    }

    protected boolean canPageBack(AbstractTable table) {
        return false;
    }

    protected boolean getUseKeyboardForPagination(AbstractTable table) {
        return false;
    }

    private String getFocusFieldName(FacesContext context, AbstractTable table) {
        return table.getClientId(context) + "::focused";
    }

    private void encodeSortingSupport(FacesContext context, AbstractTable table, ScriptBuilder buf) throws IOException {
        List<BaseColumn> columns = table.getColumnsForRendering();
        boolean atLeastOneColumnSortable1 = false;
        JSONArray columnSortableFlags = new JSONArray();
        int colCount = columns.size();
        for (int i = 0; i < colCount; i++) {
            BaseColumn column = columns.get(i);
            boolean sortable;
            Boolean columnSortableAttr = (Boolean) column.getAttributes().get("sortable");
            if (columnSortableAttr != null)
                sortable = columnSortableAttr;
            else {
                ValueExpression sortingExpression =
                        (column instanceof TableColumn) ? ((TableColumn) column).getSortingExpression() : null;
                sortable = (sortingExpression != null);
            }
            atLeastOneColumnSortable1 |= sortable;
            columnSortableFlags.put(sortable);
        }
        boolean atLeastOneColumnSortable = atLeastOneColumnSortable1;
        if (!atLeastOneColumnSortable)
            return;

        getSortedColumnClass(table);
        getSortedColumnHeaderClass(table);

        ResponseWriter writer = context.getResponseWriter();
        RenderingUtil.renderHiddenField(writer, getSortingFieldName(context, table), null);

        String oppositeSortingDirectionImageUrl = table.isSortAscending()
                ? HeaderCell.getSortedDescendingImageUrl(context, table)
                : HeaderCell.getSortedAscendingImageUrl(context, table);
        JSONArray preloadedImageUrls = new JSONArray();
        preloadedImageUrls.put(oppositeSortingDirectionImageUrl);
        if (table.getSortColumnIndex() == -1) {
            String anotherSortingDirectionImageUrl = table.isSortAscending()
                    ? HeaderCell.getSortedAscendingImageUrl(context, table)
                    : HeaderCell.getSortedDescendingImageUrl(context, table);
            preloadedImageUrls.put(anotherSortingDirectionImageUrl);
        }

        buf.initScript(context, table, "O$.Table._initSorting",
                columnSortableFlags,
                table.getSortColumnIndex(),
                StyleUtil.getCSSClass(context, table, table.getSortableHeaderStyle(), StyleGroup.regularStyleGroup(), getSortableHeaderClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortableHeaderRolloverStyle(), StyleGroup.regularStyleGroup(), getSortableHeaderRolloverClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortedColumnStyle(), StyleGroup.regularStyleGroup(), getSortedColumnClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortedColumnHeaderStyle(), StyleGroup.regularStyleGroup(), getSortedColumnHeaderClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortedColumnBodyStyle(), StyleGroup.regularStyleGroup(), getSortedColumnBodyClass(table)),
                StyleUtil.getCSSClass(context, table, table.getSortedColumnFooterStyle(), StyleGroup.regularStyleGroup(), getSortedColumnFooterClass(table)),
                preloadedImageUrls);
    }

    private String getSortingFieldName(FacesContext facesContext, UIComponent table) {
        return table.getClientId(facesContext) + "::sorting";
    }

    private String getSortedColumnClass(AbstractTable table) {
        String sortedColumnClass = table.getSortedColumnClass();
        if (!table.getApplyDefaultStyle())
            return sortedColumnClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTED_COLUMN_CLASS, sortedColumnClass);
    }

    private String getSortedColumnHeaderClass(AbstractTable table) {
        String sortedColumnHeaderClass = table.getSortedColumnHeaderClass();
        if (!table.getApplyDefaultStyle())
            return sortedColumnHeaderClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTED_COLUMN_HEADER_CLASS, sortedColumnHeaderClass);
    }

    private String getSortedColumnBodyClass(AbstractTable table) {
        String sortedColumnBodyClass = table.getSortedColumnBodyClass();
        if (!table.getApplyDefaultStyle())
            return sortedColumnBodyClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTED_COLUMN_BODY_CLASS, sortedColumnBodyClass);
    }

    private String getSortedColumnFooterClass(AbstractTable table) {
        String sortedColumnFooterClass = table.getSortedColumnFooterClass();
        if (!table.getApplyDefaultStyle())
            return sortedColumnFooterClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTED_COLUMN_FOOTER_CLASS, sortedColumnFooterClass);
    }

    private String getSortableHeaderClass(AbstractTable table) {
        String sortableHeaderClass = table.getSortableHeaderClass();
        if (!table.getApplyDefaultStyle())
            return sortableHeaderClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTABLE_HEADER_CLASS, sortableHeaderClass);
    }

    private String getSortableHeaderRolloverClass(AbstractTable table) {
        String sortableHeaderRolloverClass = table.getSortableHeaderRolloverClass();
        if (!table.getApplyDefaultStyle())
            return sortableHeaderRolloverClass;
        return TableUtil.getClassWithDefaultStyleClass(table.getApplyDefaultStyle(), DEFAULT_SORTABLE_HEADER_ROLLOVER_CLASS, sortableHeaderRolloverClass);
    }

    @Override
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        super.decode(facesContext, uiComponent);
        if (!uiComponent.isRendered())
            return;
        AbstractTable table = (AbstractTable) uiComponent;

        decodeKeyboardSupport(facesContext, table);
        AbstractTableSelection selection = table.getSelection();
        if (selection != null)
            selection.processDecodes(facesContext);

        ColumnResizing columnResizing = table.getColumnResizing();
        if (columnResizing != null)
            columnResizing.processDecodes(facesContext);

        decodeSorting(facesContext, table);

        decodeCheckboxColumns(facesContext, table);
    }


    private void decodeKeyboardSupport(FacesContext facesContext, AbstractTable table) {
        Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String focusedStr = requestParameterMap.get(getFocusFieldName(facesContext, table));
        boolean focused = focusedStr != null && focusedStr.equals("true");
        table.getAttributes().put("focused", focused);
    }

    private void decodeSorting(FacesContext facesContext, AbstractTable table) {
        Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String sortingFieldName = getSortingFieldName(facesContext, table);
        String sortingFieldValue = requestParameterMap.get(sortingFieldName);
        if (sortingFieldValue != null && sortingFieldValue.length() > 0) {
            int columnToToggle = Integer.parseInt(sortingFieldValue);
            table.toggleSorting(columnToToggle);
        }
    }

    protected boolean isKeyboardNavigationApplicable(AbstractTable table) {
        AbstractTableSelection selection = table.getSelection();
        boolean forSelection = selection != null && selection.isEnabled() && selection.isKeyboardSupport();
        boolean forPaging = getUseKeyboardForPagination(table);
        boolean result = forSelection || forPaging;
        return result;
    }

    private void encodeCheckboxColumnSupport(FacesContext facesContext, AbstractTable table, ScriptBuilder buf) throws IOException {
        List<CheckboxColumn> checkboxColumns = new ArrayList<CheckboxColumn>(1);
        List<Integer> checkBoxColIndexes = new ArrayList<Integer>(1);
        List<BaseColumn> columns = table.getColumnsForRendering();
        for (int i = 0, colIndex = 0, colCount = columns.size(); i < colCount; i++) {
            BaseColumn column = columns.get(i);
            if (column instanceof CheckboxColumn) {
                checkboxColumns.add((CheckboxColumn) column);
                checkBoxColIndexes.add(colIndex);
            }
            colIndex++;
        }
        int checkBoxColCount = checkboxColumns.size();
        if (checkBoxColCount == 0)
            return;

        ResponseWriter writer = facesContext.getResponseWriter();
        for (int i = 0; i < checkBoxColCount; i++) {
            CheckboxColumn col = checkboxColumns.get(i);
            RenderingUtil.renderHiddenField(writer, col.getClientId(facesContext), "");
        }

        for (int checkBoxColIndex = 0; checkBoxColIndex < checkBoxColCount; checkBoxColIndex++) {
            CheckboxColumn col = checkboxColumns.get(checkBoxColIndex);
            Integer colIndex = checkBoxColIndexes.get(checkBoxColIndex);
            JSONArray checkedRowIndexes = new JSONArray();
            List<Integer> rowIndexes = col.encodeSelectionIntoIndexes();
            for (int j = 0, rowIndexCount = rowIndexes.size(); j < rowIndexCount; j++) {
                int checkedRowIdx = rowIndexes.get(j);
                checkedRowIndexes.put(checkedRowIdx);
            }

            buf.initScript(facesContext, table, "O$.Table._initCheckboxCol",
                    colIndex,
                    col.getClientId(facesContext),
                    checkedRowIndexes);
        }
    }

    protected void decodeCheckboxColumns(FacesContext facesContext, AbstractTable table) {
        Map<String, String> requestMap = facesContext.getExternalContext().getRequestParameterMap();
        List<BaseColumn> columns = table.getColumnsForRendering();
        for (BaseColumn column : columns) {
            if (!(column instanceof CheckboxColumn))
                continue;

            String colId = column.getClientId(facesContext);
            String checkedRowIndexesStr = requestMap.get(colId);
            String[] indexes;
            if (checkedRowIndexesStr == null || checkedRowIndexesStr.length() == 0) {
                indexes = new String[0];
            } else {
                indexes = checkedRowIndexesStr.split(",");
            }
            List<Integer> rowIndexes = new ArrayList<Integer>(indexes.length);
            for (String indexStr : indexes) {
                Integer checkedRowIndex = new Integer(indexStr);
                rowIndexes.add(checkedRowIndex);
            }
            CheckboxColumn checkboxColumn = ((CheckboxColumn) column);
            checkboxColumn.decodeSelectionFromIndexes(rowIndexes);
        }
    }
}
