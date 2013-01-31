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
import org.openfaces.component.table.*;
import org.openfaces.component.table.impl.DynamicCol;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.DefaultTableStyles;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class TableStructure extends TableElement {
    public static final String CUSTOM_ROW_RENDERING_INFOS_KEY = "_customRowRenderingInfos";
    private static final String INIT_PARAM_FORCE_PARTIAL_AJAX = "org.openfaces.table.ajax.columnorder.submit";
    public static final String INIT_PARAM_ADDITIONAL_IEDOCMODE7_SUPPORT = "org.openfaces.table.ajax.iedocmode.support";

    private static final String DEFAULT_STYLE_CLASS = "o_table";
    private static final String DEFAULT_SCROLLABLE_STYLE_CLASS = "o_scrollable_table";
    private static final String DEFAULT_CELL_PADDING = "2";
    private static TableStyles DEFAULT_STYLES = new DefaultTableStyles();
    private static final String DEFAULT_NO_DATA_ROW_CLASS = "o_table_no_data_row";
    private static final String TABLE_LAYOUT_FIXED_STYLE_CLASS = "o_table_layout_fixed";

    private static final String DEFAULT_SUBHEADER_ROW_SEPARATOR = "1px solid #a0a0a0";
    private static final String ADDITIONAL_CELL_WRAPPER_STYLE =
            "width: 100% !important; border: none !important; padding: 0 !important; margin: 0 !important; " +
                    "background-image: none !important; background-color: transparent !important; height: auto !important;";

    static final String TABLE_STRUCTURE_ATTR = "_of_tableStructure";

    private final UIComponent component;
    private final TableStyles tableStyles;
    private final List<BaseColumn> columns;
    private List<BaseColumn> allColumns;

    private final TableHeader header;
    private final TableBody body;
    private final TableFooter footer;
    private Scrolling scrolling;
    private int leftFixedCols;
    private int rightFixedCols;

    private Map<Object, String> rowStylesMap = new HashMap<Object, String>();
    private Map<Object, String> cellStylesMap = new HashMap<Object, String>();

    private JSONObject additionalParams = new JSONObject();

    public TableStructure(UIComponent component, TableStyles tableStyles) {
        super(null);
        this.component = component;
        this.tableStyles = tableStyles;
        columns = tableStyles.getRenderedColumns();
        allColumns = tableStyles.getAllColumns();
        scrolling = tableStyles.getScrolling();
        leftFixedCols = 0;
        rightFixedCols = 0;
        if (scrolling != null && scrolling.isHorizontal()) {
            for (BaseColumn column : columns) {
                if (!isFixedColumn(column))
                    break;
                leftFixedCols++;
            }
            for (int i = columns.size() - 1; i > leftFixedCols; i--) {
                BaseColumn column = columns.get(i);
                if (!isFixedColumn(column))
                    break;
                rightFixedCols++;
            }
        }

        header = new TableHeader(this);
        body = new TableBody(this);
        footer = new TableFooter(this);

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        try{
            String paramValue = externalContext.getInitParameter(INIT_PARAM_FORCE_PARTIAL_AJAX);
            if  (paramValue != null){
                additionalParams.put("forceAjax", Boolean.valueOf(paramValue));
            }else{
                additionalParams.put("forceAjax", Boolean.FALSE);
            }
            paramValue = externalContext.getInitParameter(INIT_PARAM_ADDITIONAL_IEDOCMODE7_SUPPORT);
            if  (paramValue != null){
                additionalParams.put("additionalRowRendered", Boolean.valueOf(paramValue));
            }else{
                additionalParams.put("additionalRowRendered", Boolean.FALSE);
            }
        }  catch (Exception e){
            e.printStackTrace();
        }
    }

    public static TableStructure getCurrentInstance(AbstractTable table) {
        return (TableStructure) table.getAttributes().get(TABLE_STRUCTURE_ATTR);

    }

    private boolean isFixedColumn(BaseColumn column) {
        if (column.isFixed())
            return true;
        UIComponent parent = column.getParent();
        return parent instanceof BaseColumn && isFixedColumn((BaseColumn) parent);
    }


    public UIComponent getComponent() {
        return component;
    }

    public TableStyles getTableStyles() {
        return tableStyles;
    }

    public Scrolling getScrolling() {
        return scrolling;
    }

    public int getLeftFixedCols() {
        return leftFixedCols;
    }

    public void setLeftFixedCols(int leftFixedCols) {
        this.leftFixedCols = leftFixedCols;
    }

    public int getRightFixedCols() {
        return rightFixedCols;
    }

    public void setRightFixedCols(int rightFixedCols) {
        this.rightFixedCols = rightFixedCols;
    }

    /**
     * @return a list of all rendered columns in all column groups
     */
    public List<BaseColumn> getColumns() {
        return columns;
    }

    public List<BaseColumn> getAllColumns() {
        return allColumns;
    }

    public TableHeader getHeader() {
        return header;
    }

    public TableBody getBody() {
        return body;
    }

    public TableFooter getFooter() {
        return footer;
    }

    private boolean isDirectTableRenderingRequest(FacesContext context, AbstractTable table) {
        PartialViewContext partialViewContext = context.getPartialViewContext();
        if (!partialViewContext.isAjaxRequest()) return false;
        String tableClientId = table.getClientId(context);
        return partialViewContext.getRenderIds().contains(tableClientId);
    }

    public void render(FacesContext context, HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        AbstractTable table = (AbstractTable) component;
        table.setRowIndex(-1);
        boolean skipExternalFacets = isDirectTableRenderingRequest(context, table);
        UIComponent aboveFacet = table.getAbove();
        if (aboveFacet != null && !skipExternalFacets)
            aboveFacet.encodeAll(context);
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("table", table);
        Rendering.writeIdAttribute(context, table);
        Rendering.writeStandardEvents(writer, table);
        writeStyleAndClass(context, table, writer);
        Rendering.writeAttribute(writer, "dir", table.getDir());
        Rendering.writeAttribute(writer, "width", table.getWidth());
        Rendering.writeAttribute(writer, "align", table.getAlign());
        Rendering.writeAttribute(writer, "bgcolor", table.getBgcolor());
        Rendering.writeAttribute(writer, "rules", table.getRules());
        Rendering.writeAttribute(writer, "border", table.getBorder(), Integer.MIN_VALUE);

        Scrolling scrolling = getScrolling();
        if (scrolling == null) {
            String cellspacing = table.getCellspacing();
            if (areGridLinesRequested(table, TableStructure.getDefaultStyles(table)))
                cellspacing = "0";
            Rendering.writeAttribute(writer, "cellspacing", cellspacing);
            Rendering.writeAttribute(writer, "cellpadding", getTableCellPadding());

            List<BaseColumn> columns = table.getRenderedColumns();
            TableUtil.writeColumnTags(context, table, columns);
        } else {
            Rendering.writeAttribute(writer, "cellspacing", "0");
            Rendering.writeAttribute(writer, "cellpadding", "0");
            writer.startElement("tr", table);
            writer.startElement("td", table);
            writer.writeAttribute("style", "vertical-align: top", null);
            writer.startElement("div", table);
            writer.writeAttribute("style", "position: absolute; ", null);
        }

        TableHeader header = getHeader();
        if (header.isContentSpecified())
            header.render(context, null);

        TableBody body = getBody();
        body.render(context, additionalContentWriter);

        TableFooter footer = getFooter();
        if (footer.isContentSpecified()) {
            footer.render(context, additionalContentWriter);
        }

        if (scrolling != null) {
            writer.endElement("div");
            writer.endElement("td");
            writer.endElement("tr");
        }

        writer.endElement("table");
        Rendering.writeNewLine(writer);
        UIComponent belowFacet = table.getBelow();
        if (belowFacet != null && !skipExternalFacets)
            belowFacet.encodeAll(context);
    }

    private void writeStyleAndClass(FacesContext context, AbstractTable table, ResponseWriter writer) throws IOException {
        String style = table.getStyle();
        String textStyle = getTextStyle(table);
        style = Styles.mergeStyles(style, textStyle);
        boolean applyDefaultStyle = table.getApplyDefaultStyle();
        String styleClass = TableUtil.getClassWithDefaultStyleClass(applyDefaultStyle, DEFAULT_STYLE_CLASS, table.getStyleClass());
        if (scrolling != null)
            styleClass = Styles.mergeClassNames(styleClass, DEFAULT_SCROLLABLE_STYLE_CLASS);
        String tableWidth = table.getWidth();
        if (scrolling == null) {
            // scrollable tables shouldn't have "table-layout: fixed" declaration, since it's declared in sub-tables.
            ColumnResizing columnResizing = table.getColumnResizing();
            if (columnResizing != null) {
                ColumnResizingState resizingState = columnResizing.getResizingState();
                if (resizingState != null)
                    tableWidth = resizingState.getTableWidth();
                if (columnResizing.isEnabled()) {
                    if (tableWidth != null || Environment.isMozilla()) {
                        // "table-layout: fixed" style can't be set on client-side and should be rendered from the server, though
                        // it shouldn't be rendered under IE because all tables without explicit widths will occupy 100% width as a result
                        styleClass = Styles.mergeClassNames(styleClass, TABLE_LAYOUT_FIXED_STYLE_CLASS);
                    }
                }
            }
        } else {
            styleClass = Styles.mergeClassNames(styleClass, TABLE_LAYOUT_FIXED_STYLE_CLASS);
        }
        String textClass = getTextClass(table);
        styleClass = Styles.mergeClassNames(styleClass, textClass);
        String rolloverStyle = table.getRolloverStyle();
        if (Rendering.isNullOrEmpty(rolloverStyle)) {
            styleClass = Styles.mergeClassNames(DefaultStyles.CLASS_INITIALLY_INVISIBLE, styleClass);
            Rendering.writeStyleAndClassAttributes(writer, style, styleClass);
        } else {
            String cls = Styles.getCSSClass(context, component, style, styleClass);
            if (!Environment.isOpera())
                cls = Styles.mergeClassNames(DefaultStyles.CLASS_INITIALLY_INVISIBLE, cls);
            if (!Rendering.isNullOrEmpty(cls))
                writer.writeAttribute("class", cls, null);
        }
    }

    String getTableCellPadding() {
        AbstractTable table = (AbstractTable) component;
        String cellpadding = table.getCellpadding();
        if (cellpadding == null && table.getApplyDefaultStyle())
            cellpadding = DEFAULT_CELL_PADDING;
        return cellpadding;
    }

    static void writeNonBreakableSpaceForEmptyCell(
            ResponseWriter writer,
            AbstractTable table,
            UIComponent cellComponentsContainer) throws IOException {
        if (!(cellComponentsContainer instanceof Column) && !(cellComponentsContainer instanceof Cell))
            return;
        boolean childrenEmpty = true;
        DynamicCol dynamicCol = cellComponentsContainer instanceof DynamicCol ? (DynamicCol) cellComponentsContainer : null;
        Runnable exitContext = dynamicCol != null ? dynamicCol.enterComponentContext() : null;
        try {
            List<UIComponent> children = (cellComponentsContainer instanceof SyntheticColumn)
                    ? ((SyntheticColumn) cellComponentsContainer).getChildrenForProcessing()
                    : cellComponentsContainer.getChildren();

            for (UIComponent child : children) {
                if (!isComponentEmpty(child)) {
                    childrenEmpty = false;
                    break;
                }
            }
        } finally {
            if (exitContext != null) exitContext.run();
        }
        TableStructure tableStructure = getCurrentInstance(table);
        if (childrenEmpty && tableStructure.isEmptyCellsTreatmentRequired())
            Rendering.writeNonBreakableSpace(writer);
    }


    boolean isEmptyCellsTreatmentRequired() {
        if (!(this.component instanceof AbstractTable))
            return true;
        AbstractTable table = (AbstractTable) this.component;
        return areGridLinesRequested(table, getDefaultStyles(table)) ||
                table.getBorder() != Integer.MIN_VALUE;
    }

    static TableStyles getDefaultStyles(TableStyles table) {
        return table.getApplyDefaultStyle() ? DEFAULT_STYLES : null;
    }

    public Map<Object, String> getRowStylesMap() {
        return rowStylesMap;
    }

    public Map<Object, String> getCellStylesMap() {
        return cellStylesMap;
    }

    protected String getAdditionalRowClass(FacesContext facesContext, AbstractTable table, Object rowData, int rowIndex) {
        return null;
    }

    protected String[][] getBodyRowAttributes(FacesContext facesContext, AbstractTable table) throws IOException {
        return null;
    }

    protected String getTextClass(AbstractTable table) {
        return null;
    }

    protected String getTextStyle(AbstractTable table) {
        return null;
    }

    static boolean isComponentEmpty(UIComponent child) {
        if (child == null || !child.isRendered())
            return true;
        if (!(child instanceof HtmlOutputText))
            return false;
        HtmlOutputText outputText = (HtmlOutputText) child;
        Object value = outputText.getValue();
        return value == null || value.toString().trim().length() == 0;
    }

    static String getNoDataRowClassName(FacesContext facesContext, AbstractTable table) {
        return Styles.getCSSClass(facesContext, table,
                table.getNoDataRowStyle(),
                table.getApplyDefaultStyle() ? DEFAULT_NO_DATA_ROW_CLASS : null,
                table.getNoDataRowClass()
        );
    }

    public JSONObject getInitParam(FacesContext facesContext, TableStyles defaultStyles) {
        UIComponent styleOwnerComponent = getComponent();
        boolean forceUsingCellStyles = getForceUsingCellStyles(styleOwnerComponent);

        List<BaseColumn> visibleColumns = getColumns();
        List<BaseColumn> allColumns = getAllColumns();
        TableStyles tableStyles = getTableStyles();
        Map<Object, String> rowStylesMap = getRowStylesMap();
        Map<Object, String> cellStylesMap = getCellStylesMap();

        JSONObject result = new JSONObject();
        Rendering.addJsonParam(result, "header", getHeader().getInitParam(defaultStyles));
        Rendering.addJsonParam(result, "body", getBody().getInitParam(defaultStyles));
        Rendering.addJsonParam(result, "footer", getFooter().getInitParam(defaultStyles));
        Rendering.addJsonParam(result, "columns", getColumnHierarchyParam(facesContext, visibleColumns));
        Rendering.addJsonParam(result, "logicalColumns", getColumnHierarchyParam(facesContext, allColumns));
        Rendering.addJsonParam(result, "gridLines", getGridLineParams(tableStyles, defaultStyles));

        Rendering.addJsonParam(result, "additionalParams", additionalParams);

        Rendering.addJsonParam(result, "scrolling", getScrollingParam());

        Rendering.addJsonParam(result, "rowStylesMap", TableUtil.getStylesMapAsJSONObject(rowStylesMap));
        Rendering.addJsonParam(result, "cellStylesMap", TableUtil.getStylesMapAsJSONObject(cellStylesMap));
        Rendering.addJsonParam(result, "forceUsingCellStyles", forceUsingCellStyles, false);
        if (tableStyles instanceof TreeTable)
            Rendering.addJsonParam(result, "additionalCellWrapperStyle", Styles.getCSSClass(facesContext, (UIComponent) tableStyles, ADDITIONAL_CELL_WRAPPER_STYLE,
                    StyleGroup.disabledStyleGroup(10), null));
        Rendering.addJsonParam(result, "invisibleRowsAllowed", tableStyles instanceof TreeTable, false);

        if (component instanceof AbstractTable) {
            AbstractTable table = (AbstractTable) component;

            Rendering.addJsonParam(result, "onafterajaxreload", table.getOnafterajaxreload(), null);
            Rendering.addJsonParam(result, "onbeforeajaxreload", table.getOnbeforeajaxreload(), null);

            // Column initialization code in tableUtil.js needs to know whether there are any features in the table,
            // which will require column width control (setting width for columns on client-side, which might be
            // required not only for interactive column resizing). This information is needed for performance
            // optimization purposes to allow column initialization script to avoid unnecessary preparations (creating
            // individual CSS classes for each column).
            //
            // These features are initialized after columns so this script cannot know if there are these features by
            // itself.
            //
            // Lazy initialization of this (creating individual CSS classes) would require additional performance hit
            // when column resizing is turned on, due to the need to refresh column style after lazy CSS style creation,
            // so detecting the presence of these features beforehand, like this, seems to be the best approach.

            String forceColumnWidthControlRequiredStr =
                    (String) table.getAttributes().get("forceColumnWidthControlRequired");
            boolean columnWidthControlRequired = forceColumnWidthControlRequiredStr != null
                    ? Boolean.parseBoolean(forceColumnWidthControlRequiredStr)
                    : table.getColumnResizing() != null || table.getScrolling() != null;
            if (columnWidthControlRequired)
                Rendering.addJsonParam(result, "columnWidthControlRequired", true);
        }

        return result;
    }

    private Object getScrollingParam() {
        if (scrolling == null)
            return null;
        JSONObject result = new JSONObject();
        int leftFixedCols = getLeftFixedCols();
        int rightFixedCols = getRightFixedCols();
        try {
            result.put("leftFixedCols", leftFixedCols);
            result.put("rightFixedCols", rightFixedCols);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        boolean autoSaveState = scrolling.getAutoSaveState();
        Rendering.addJsonParam(result, "horizontal", scrolling.isHorizontal(), false);
        Rendering.addJsonParam(result, "vertical", scrolling.isVertical(), false);
        Rendering.addJsonParam(result, "autoScrollbars", scrolling.getAutoScrollbars(), false);
        Rendering.addJsonParam(result, "minimizeHeight", scrolling.getMinimizeHeight(), false);
        Rendering.addJsonParam(result, "autoSaveState", autoSaveState, false);
        if (autoSaveState)
            Rendering.addJsonParam(result, "autoSaveStateDelay", scrolling.getAutoSaveStateDelay());
        Point pos = scrolling.getPosition();
        Rendering.addJsonParam(result, "position", new Object[]{pos.x, pos.y});

        return result;
    }

    private static boolean getForceUsingCellStyles(UIComponent styleOwnerComponent) {
        boolean requireCellStylesForCorrectColWidthBehavior =
                Environment.isChrome() ||
                        Environment.isSafari() || /* doesn't handle column width in TreeTable if width is applied to <col> tags */
                        Environment.isOpera(); /* DataTable, TreeTable are jerking when reloading them with OF Ajax if width is applied to <col> tags */
        String forceUsingCellStylesAttr = (String) styleOwnerComponent.getAttributes().get("forceUsingCellStyles");
        boolean forceUsingCellStyles = requireCellStylesForCorrectColWidthBehavior ||
                Boolean.valueOf(forceUsingCellStylesAttr);
        return forceUsingCellStyles;
    }

    private JSONArray getColumnHierarchyParam(
            FacesContext facesContext,
            List<BaseColumn> columns
    ) {
        int colCount = columns.size();
        List<BaseColumn>[] columnHierarchies = new List[colCount];
        TableHeaderOrFooter.composeColumnHierarchies(columns, columnHierarchies, true, false);

        JSONArray columnHierarchyArray;
        try {
            columnHierarchyArray = processColumnHierarchy(facesContext, columnHierarchies, 0, 0, colCount - 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return columnHierarchyArray;
    }

    private JSONArray processColumnHierarchy(
            FacesContext context, List<BaseColumn>[] columnHierarchies, int level, int startCol, int finishCol
    ) throws JSONException {
        JSONArray columnsArray = new JSONArray();
        int thisGroupStart = -1;
        BaseColumn thisGroup = null;
        for (int colIndex = startCol; colIndex <= finishCol + 1; colIndex++) {
            List<BaseColumn> thisColumnHierarchy = colIndex <= finishCol ? columnHierarchies[colIndex] : null;
            BaseColumn columnOrGroup = thisColumnHierarchy != null ? thisColumnHierarchy.get(level) : null;
            if (thisGroup == null) {
                thisGroup = columnOrGroup;
                thisGroupStart = startCol;
            } else if (columnOrGroup != thisGroup) {
                int thisGroupEnd = colIndex - 1;
                List<BaseColumn> lastColumnHierarchy = columnHierarchies[thisGroupEnd];

                JSONObject columnObj = getColumnParams(context, thisGroup, level);
                columnsArray.put(columnObj);
                boolean hasSubColumns = lastColumnHierarchy.size() - 1 > level;
                if (hasSubColumns) {
                    JSONArray subColumns = processColumnHierarchy(context, columnHierarchies, level + 1, thisGroupStart, thisGroupEnd);
                    columnObj.put("subColumns", subColumns);
                }
                thisGroup = columnOrGroup;
                thisGroupStart = colIndex;
            }
        }
        return columnsArray;
    }

    private JSONObject getColumnParams(FacesContext context, BaseColumn columnOrGroup, int level) throws JSONException {
        JSONObject columnObj = new JSONObject();

        columnObj.put("columnId", columnOrGroup.getId());

        UIComponent styleOwnerComponent = getComponent();
        boolean noDataRows = getBody().getNoDataRows();
        boolean ordinaryColumn = !(columnOrGroup instanceof ColumnGroup);

        String defaultColumnStyleClass = getColumnDefaultClass(columnOrGroup);
        String colClassName = Styles.getCSSClass(context,
                styleOwnerComponent, columnOrGroup.getStyle(), StyleGroup.regularStyleGroup(level), columnOrGroup.getStyleClass(), defaultColumnStyleClass
        );

        String resizingWidthClass;
        {
            AbstractTable table = styleOwnerComponent instanceof AbstractTable ? (AbstractTable) styleOwnerComponent : null;
            ColumnResizing columnResizing = (table != null) ? table.getColumnResizing() : null;
            ColumnResizingState columnResizingState = columnResizing != null ? columnResizing.getResizingState() : null;
            String resizingWidth = columnResizingState != null ? columnResizingState.getColumnWidth(columnOrGroup.getId()) : null;
            if (resizingWidth != null) {
                resizingWidthClass = Styles.getCSSClass(context, table, "width: " + resizingWidth, StyleGroup.selectedStyleGroup(), null
                );
            } else
                resizingWidthClass = null;
        }

        columnObj.put("className", Styles.mergeClassNames(colClassName, resizingWidthClass));

        appendColumnEventsArray(columnObj,
                columnOrGroup.getOnclick(),
                columnOrGroup.getOndblclick(),
                columnOrGroup.getOnmousedown(),
                columnOrGroup.getOnmouseover(),
                columnOrGroup.getOnmousemove(),
                columnOrGroup.getOnmouseout(),
                columnOrGroup.getOnmouseup());
        boolean hasCellWrappers = columnOrGroup instanceof TreeColumn;
        if (hasCellWrappers)
            columnObj.put("hasCellWrappers", hasCellWrappers);

        TableHeader tableHeader = getHeader();
        CellCoordinates headerCellCoordinates = tableHeader.findCell(columnOrGroup, CellKind.COL_HEADER);
        if (headerCellCoordinates != null) {
            JSONObject header = new JSONObject();
            columnObj.put("header", header);
            header.put("pos", headerCellCoordinates.asJSONObject());
            header.put("className", Styles.getCSSClass(context, styleOwnerComponent,
                    columnOrGroup.getHeaderStyle(), columnOrGroup.getHeaderClass()));
            appendColumnEventsArray(header,
                    columnOrGroup.getHeaderOnclick(),
                    columnOrGroup.getHeaderOndblclick(),
                    columnOrGroup.getHeaderOnmousedown(),
                    columnOrGroup.getHeaderOnmouseover(),
                    columnOrGroup.getHeaderOnmousemove(),
                    columnOrGroup.getHeaderOnmouseout(),
                    columnOrGroup.getHeaderOnmouseup());
        }
        if (ordinaryColumn && tableHeader.hasSubHeader()) {
            CellCoordinates subHeaderCellCoordinates = tableHeader.findCell(columnOrGroup, CellKind.COL_SUBHEADER);
            if (subHeaderCellCoordinates != null) {
                JSONObject subHeader = new JSONObject();
                columnObj.put("subHeader", subHeader);
                subHeader.put("pos", subHeaderCellCoordinates.asJSONObject());
                subHeader.put("className", Styles.getCSSClass(context, styleOwnerComponent, columnOrGroup.getSubHeaderStyle(), columnOrGroup.getSubHeaderClass()));
            }
        }
        if (!noDataRows) {
            JSONObject body = new JSONObject();
            columnObj.put("body", body);
            body.put("className", Styles.getCSSClass(context,
                    styleOwnerComponent, getColumnBodyStyle(columnOrGroup), StyleGroup.regularStyleGroup(level), getColumnBodyClass(columnOrGroup), null
            ));
            appendColumnEventsArray(body,
                    columnOrGroup.getBodyOnclick(),
                    columnOrGroup.getBodyOndblclick(),
                    columnOrGroup.getBodyOnmousedown(),
                    columnOrGroup.getBodyOnmouseover(),
                    columnOrGroup.getBodyOnmousemove(),
                    columnOrGroup.getBodyOnmouseout(),
                    columnOrGroup.getBodyOnmouseup());
        }
        TableFooter tableFooter = getFooter();
        CellCoordinates footerCellCoordinates = tableFooter.findCell(columnOrGroup, CellKind.COL_HEADER);
        if (footerCellCoordinates != null) {
            JSONObject footer = new JSONObject();
            columnObj.put("footer", footer);
            footer.put("pos", footerCellCoordinates.asJSONObject());
            footer.put("className", Styles.getCSSClass(context, styleOwnerComponent, columnOrGroup.getFooterStyle(), columnOrGroup.getFooterClass()));
            appendColumnEventsArray(footer,
                    columnOrGroup.getFooterOnclick(),
                    columnOrGroup.getFooterOndblclick(),
                    columnOrGroup.getFooterOnmousedown(),
                    columnOrGroup.getFooterOnmouseover(),
                    columnOrGroup.getFooterOnmousemove(),
                    columnOrGroup.getFooterOnmouseout(),
                    columnOrGroup.getFooterOnmouseup());
        }
        if (!(columnOrGroup instanceof ColumnGroup))
            columnObj.put("menuAllowed", columnOrGroup.getMenuAllowed());
        return columnObj;
    }

    private String getColumnDefaultClass(BaseColumn column) {
        return (String) column.getAttributes().get("defaultStyle");
    }

    private void addJSONEntry(JSONArray array, String entry) {
        if (entry != null)
            array.put(entry);
        else
            array.put(JSONObject.NULL);
    }

    private JSONArray getGridLineParams(TableStyles tableStyles, TableStyles defaultStyles) {
        JSONArray result = new JSONArray();
        addJSONEntry(result, getHorizontalGridLines(tableStyles, defaultStyles));
        addJSONEntry(result, getVerticalGridLines(tableStyles, defaultStyles));
        addJSONEntry(result, getCommonHeaderSeparator(tableStyles, defaultStyles));
        addJSONEntry(result, getCommonFooterSeparator(tableStyles, defaultStyles));
        addJSONEntry(result, getHeaderHorizSeparator(tableStyles, defaultStyles));
        addJSONEntry(result, getHeaderVertSeparator(tableStyles, defaultStyles));
        addJSONEntry(result, getFilterRowSeparator(tableStyles));
        addJSONEntry(result, getMultiHeaderSeparator(tableStyles, defaultStyles));
        addJSONEntry(result, getMultiFooterSeparator(tableStyles, defaultStyles));
        addJSONEntry(result, getFooterHorizSeparator(tableStyles, defaultStyles));
        addJSONEntry(result, getFooterVertSeparator(tableStyles, defaultStyles));
        return result;
    }

    private String getColumnBodyStyle(BaseColumn column) {
        String style = column.getBodyStyle();
        if (column instanceof TreeColumn) {
            UIComponent columnParent = column.getParent();
            while (columnParent instanceof ColumnGroup)
                columnParent = columnParent.getParent();
            AbstractTable table = (AbstractTable) columnParent;
            String textStyle = table.getTextStyle();
            style = Styles.mergeStyles(style, textStyle);
        }
        return style;
    }

    private String getColumnBodyClass(BaseColumn column) {
        String cls = column.getBodyClass();
        if (column instanceof TreeColumn) {
            UIComponent columnParent = column.getParent();
            while (columnParent instanceof ColumnGroup)
                columnParent = columnParent.getParent();
            AbstractTable table = (AbstractTable) columnParent;
            String textClass = table.getTextClass();
            cls = Styles.mergeClassNames(cls, textClass);
        }
        return cls;
    }

    private void appendColumnEventsArray(JSONObject columnObj,
                                         String onclick,
                                         String ondblclick,
                                         String onmosedown,
                                         String onmouseover,
                                         String onmousemove,
                                         String onmouseout,
                                         String onmouseup) throws JSONException {
        appendHandlerEntry(columnObj, "onclick", onclick);
        appendHandlerEntry(columnObj, "ondblclick", ondblclick);
        appendHandlerEntry(columnObj, "onmousedown", onmosedown);
        appendHandlerEntry(columnObj, "onmouseover", onmouseover);
        appendHandlerEntry(columnObj, "onmousemove", onmousemove);
        appendHandlerEntry(columnObj, "onmouseout", onmouseout);
        appendHandlerEntry(columnObj, "onmouseup", onmouseup);
    }

    private String getFilterRowSeparator(TableStyles tableStyles) {
        if (!(tableStyles instanceof AbstractTable))
            return null;
        AbstractTable table = ((AbstractTable) tableStyles);
        String result = table.getSubHeaderRowSeparator();
        if (result == null && table.getApplyDefaultStyle())
            result = DEFAULT_SUBHEADER_ROW_SEPARATOR;
        return result;
    }

    private void appendHandlerEntry(JSONObject obj, String eventName, String handler) throws JSONException {
        if (handler == null)
            return;
        obj.put(eventName, handler);
    }

    private static String getHorizontalGridLines(TableStyles table, TableStyles defaultStyles) {
        String horizontalGridLines = table.getHorizontalGridLines();
        if (horizontalGridLines == null && defaultStyles != null)
            horizontalGridLines = defaultStyles.getHorizontalGridLines();
        return horizontalGridLines;
    }

    private static String getVerticalGridLines(TableStyles table, TableStyles defaultStyles) {
        String verticalGridLines = table.getVerticalGridLines();
        if (verticalGridLines == null && defaultStyles != null)
            verticalGridLines = defaultStyles.getVerticalGridLines();
        return verticalGridLines;
    }

    private static String getHeaderHorizSeparator(TableStyles table, TableStyles defaultStyles) {
        String result = table.getHeaderHorizSeparator();
        if (result == null && defaultStyles != null)
            result = defaultStyles.getHeaderHorizSeparator();
        return result;
    }

    private static String getFooterHorizSeparator(TableStyles table, TableStyles defaultStyles) {
        String result = table.getFooterHorizSeparator();
        if (result == null && defaultStyles != null)
            result = defaultStyles.getFooterHorizSeparator();
        return result;
    }

    private static String getHeaderVertSeparator(TableStyles table, TableStyles defaultStyles) {
        String result = table.getHeaderVertSeparator();
        if (result == null && defaultStyles != null)
            result = defaultStyles.getHeaderVertSeparator();
        return result;
    }

    private static String getFooterVertSeparator(TableStyles table, TableStyles defaultStyles) {
        String result = table.getFooterVertSeparator();
        if (result == null && defaultStyles != null)
            result = defaultStyles.getFooterVertSeparator();
        return result;
    }

    private static String getCommonHeaderSeparator(TableStyles table, TableStyles defaultStyles) {
        String result = table.getCommonHeaderSeparator();
        if (result == null && defaultStyles != null)
            result = defaultStyles.getCommonHeaderSeparator();
        return result;
    }

    private static String getCommonFooterSeparator(TableStyles table, TableStyles defaultStyles) {
        String result = table.getCommonFooterSeparator();
        if (result == null && defaultStyles != null)
            result = defaultStyles.getCommonFooterSeparator();
        return result;
    }

    private static String getMultiHeaderSeparator(TableStyles table, TableStyles defaultStyles) {
        String result = table.getMultiHeaderSeparator();
        if (result == null && defaultStyles != null)
            result = defaultStyles.getMultiHeaderSeparator();
        return result;
    }

    private static String getMultiFooterSeparator(TableStyles table, TableStyles defaultStyles) {
        String result = table.getMultiFooterSeparator();
        if (result == null && defaultStyles != null)
            result = defaultStyles.getMultiFooterSeparator();
        return result;
    }

    public static boolean areGridLinesRequested(TableStyles tableStyles, TableStyles defaultStyles) {
        AbstractTable table = tableStyles instanceof AbstractTable ? ((AbstractTable) tableStyles) : null;
        return getHorizontalGridLines(tableStyles, defaultStyles) != null || getVerticalGridLines(tableStyles, defaultStyles) != null ||
                getCommonHeaderSeparator(tableStyles, defaultStyles) != null || getCommonFooterSeparator(tableStyles, defaultStyles) != null ||
                getHeaderHorizSeparator(tableStyles, defaultStyles) != null || getHeaderVertSeparator(tableStyles, defaultStyles) != null ||
                getFooterHorizSeparator(tableStyles, defaultStyles) != null || getFooterVertSeparator(tableStyles, defaultStyles) != null ||
                getMultiHeaderSeparator(tableStyles, defaultStyles) != null || getMultiFooterSeparator(tableStyles, defaultStyles) != null ||
                (table != null && table.getSubHeaderRowSeparator() != null);
    }

}
