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
package org.openfaces.renderkit;

import org.openfaces.component.TableStyles;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.ColumnResizingState;
import org.openfaces.component.table.TableColumn;
import org.openfaces.component.table.TableColumnGroup;
import org.openfaces.component.table.TableColumns;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.component.table.TreeTable;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.table.CellCoordinates;
import org.openfaces.renderkit.table.TableFooter;
import org.openfaces.renderkit.table.TableHeader;
import org.openfaces.renderkit.table.TableHeaderOrFooter;
import org.openfaces.renderkit.table.TableStructure;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.EnvironmentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableUtil {
    public static final String DEFAULT_HEADER_SECTION_CLASS = "o_table_header_section";
    public static final String DEFAULT_FOOTER_SECTION_CLASS = "o_table_footer_section";

    private static final String DEFAULT_HEADER_CELL_CLASS = "";
    private static final String DEFAULT_FILTER_ROW_CELLS_CLASS = "o_filter_row_cells";

    private static final String DEFAULT_FILTER_ROW_SEPARATOR = "1px solid #a0a0a0";

    private static final String ADDITIONAL_CELL_WRAPPER_STYLE =
            "width: 100% !important; border: none !important; padding: 0 !important; margin: 0 !important; " +
                    "background-image: none !important; background-color: transparent !important; height: auto !important;";

    private TableUtil() {
    }

    public static String getTableUtilJsURL(FacesContext context) {
        return ResourceUtil.getInternalResourceURL(context, TableUtil.class, "tableUtil.js");
    }

    public static class TableParams {
        private UIComponent styleOwnerComponent;
        private TableStructure tableStructure;
        private boolean forceUsingCellStyles;
        private boolean noDataRows;

        public TableParams(UIComponent styleOwnerComponent, TableStructure tableStructure, boolean noDataRows) {
            this.styleOwnerComponent = styleOwnerComponent;
            this.tableStructure = tableStructure;
            this.noDataRows = noDataRows;
            this.forceUsingCellStyles = getForceUsingCellStyles(styleOwnerComponent);
        }

        public UIComponent getStyleOwnerComponent() {
            return styleOwnerComponent;
        }

        public TableStructure getTableStructure() {
            return tableStructure;
        }

        public boolean isForceUsingCellStyles() {
            return forceUsingCellStyles;
        }

        public boolean isNoDataRows() {
            return noDataRows;
        }
    }

    public static void writeColumnTags(FacesContext context, UIComponent component, List columns) throws IOException {
        ColumnResizing columnResizing = (component instanceof AbstractTable) ?
                ((AbstractTable) component).getColumnResizing() : null;
        ColumnResizingState columnResizingState = columnResizing != null ? columnResizing.getResizingState() : null;
        int colCount = columns.size();
        if (columnResizingState != null && columnResizingState.getColumnCount() != colCount)
            columnResizingState = null;
        ResponseWriter writer = context.getResponseWriter();
        for (int colIndex = 0; colIndex < colCount; colIndex++) {
            BaseColumn column = (BaseColumn) columns.get(colIndex);
            writer.startElement("col", component);
            String colWidth = columnResizingState != null ? columnResizingState.getColumnWidth(colIndex) : column.getWidth();
            RenderingUtil.writeAttribute(writer, "width", colWidth);
            RenderingUtil.writeAttribute(writer, "align", column.getAlign());
            RenderingUtil.writeAttribute(writer, "valign", column.getValign());
            writer.endElement("col");
        }
    }

    public static JSONArray getStructureAndStyleParams(
            FacesContext facesContext,
            TableStyles defaultStyles,
            TableStructure tableStructure
    ) {
        UIComponent styleOwnerComponent = tableStructure.getComponent();
        boolean noDataRows = tableStructure.getBody().isNoDataRows();
        TableParams params = new TableParams(styleOwnerComponent, tableStructure, noDataRows);
        boolean forceUsingCellStyles = params.isForceUsingCellStyles();

        List columns = tableStructure.getColumns();
        TableStyles tableStyles = tableStructure.getTableStyles();
        Map<Object, String> rowStylesMap = tableStructure.getRowStylesMap();
        Map<Object, String> cellStylesMap = tableStructure.getCellStylesMap();

        JSONArray result = new JSONArray();
        result.put(getColumnHierarchyParam(facesContext, columns, params));
        result.put(tableStructure.getHeader().hasCommonHeaderRow());
        result.put(tableStructure.getHeader().hasSubHeader());
        result.put(tableStructure.getFooter().hasCommonHeaderRow());
        result.put(noDataRows);
        result.put(getGridLineParams(tableStyles, defaultStyles));
        result.put(getRowStyleParams(facesContext, tableStyles, defaultStyles, styleOwnerComponent));
        result.put(getStylesMapAsJSONObject(rowStylesMap));
        result.put(getStylesMapAsJSONObject(cellStylesMap));
        result.put(forceUsingCellStyles);
        if (tableStyles instanceof TreeTable)
            result.put(StyleUtil.getCSSClass(facesContext, (UIComponent) tableStyles, ADDITIONAL_CELL_WRAPPER_STYLE,
                    StyleGroup.disabledStyleGroup(10), null));
        else
            result.put(JSONObject.NULL);
        result.put(tableStyles instanceof TreeTable);
        return result;
    }

    private static boolean getForceUsingCellStyles(UIComponent styleOwnerComponent) {
        boolean requireCellStylesForCorrectColWidthBehavior =
                EnvironmentUtil.isSafari() || /* doesn't handle column width in TreeTable if width is applied to <col> tags */
                        EnvironmentUtil.isOpera(); /* DataTable, TreeTable are jerking when reloading them with QK Ajax if width is applied to <col> tags */
        String forceUsingCellStylesAttr = (String) styleOwnerComponent.getAttributes().get("forceUsingCellStyles");
        boolean forceUsingCellStyles = requireCellStylesForCorrectColWidthBehavior ||
                Boolean.valueOf(forceUsingCellStylesAttr);
        return forceUsingCellStyles;
    }

    private static JSONArray getColumnHierarchyParam(
            FacesContext facesContext,
            List columns,
            TableParams tableParams) {
        int colCount = columns.size();
        List[] columnHierarchies = new List[colCount];
        TableHeaderOrFooter.composeColumnHierarchies(columns, columnHierarchies, true, false);

        JSONArray columnHierarchyArray;
        try {
            columnHierarchyArray = processColumnHierarchy(facesContext, columnHierarchies, 0, 0, colCount - 1, tableParams);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return columnHierarchyArray;
    }

    private static JSONArray processColumnHierarchy(
            FacesContext context, List[] columnHierarchies, int level, int startCol, int finishCol,
            TableParams tableParams) throws JSONException {
        JSONArray columnsArray = new JSONArray();
        int thisGroupStart = -1;
        BaseColumn thisGroup = null;
        for (int colIndex = startCol; colIndex <= finishCol + 1; colIndex++) {
            List thisColumnHierarchy = colIndex <= finishCol ? columnHierarchies[colIndex] : null;
            BaseColumn columnOrGroup = thisColumnHierarchy != null ? (BaseColumn) thisColumnHierarchy.get(level) : null;
            if (thisGroup == null) {
                thisGroup = columnOrGroup;
                thisGroupStart = startCol;
            } else if (columnOrGroup != thisGroup) {
                int thisGroupEnd = colIndex - 1;
                List lastColumnHierarchy = columnHierarchies[thisGroupEnd];

                JSONObject columnObj = getColumnParams(context, thisGroup, tableParams, thisGroupEnd, level);
                columnsArray.put(columnObj);
                boolean hasSubColumns = lastColumnHierarchy.size() - 1 > level;
                if (hasSubColumns) {
                    JSONArray subColumns = processColumnHierarchy(context, columnHierarchies, level + 1, thisGroupStart, thisGroupEnd, tableParams);
                    columnObj.put("subColumns", subColumns);
                }
                thisGroup = columnOrGroup;
                thisGroupStart = colIndex;
            } 
        }
        return columnsArray;
    }

    private static JSONObject getColumnParams(FacesContext context, BaseColumn columnOrGroup,
                                              TableParams tableParams, int colIndex, int level) throws JSONException {
        JSONObject columnObj = new JSONObject();

        UIComponent styleOwnerComponent = tableParams.getStyleOwnerComponent();
        boolean noDataRows = tableParams.isNoDataRows();
        boolean ordinaryColumn = !(columnOrGroup instanceof TableColumnGroup);

        String defaultColumnStyleClass = getColumnDefaultClass(columnOrGroup);
        String colClassName = StyleUtil.getCSSClass(context,
                styleOwnerComponent, columnOrGroup.getStyle(), StyleGroup.regularStyleGroup(level), columnOrGroup.getStyleClass(), defaultColumnStyleClass
        );

        String resizingWidthClass;
        {
            AbstractTable table = styleOwnerComponent instanceof AbstractTable ? (AbstractTable) styleOwnerComponent : null;
            ColumnResizing columnResizing = (table != null) ? table.getColumnResizing() : null;
            ColumnResizingState columnResizingState = columnResizing != null ? columnResizing.getResizingState() : null;
            String resizingWidth = columnResizingState != null ? columnResizingState.getColumnWidth(colIndex) : null;
            if (resizingWidth != null) {
                resizingWidthClass = StyleUtil.getCSSClass(context, table, "width: " + resizingWidth, StyleGroup.selectedStyleGroup(), null
                );
            } else
                resizingWidthClass = null;
        }

        columnObj.put("className", StyleUtil.mergeClassNames(colClassName, resizingWidthClass));

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

        TableStructure tableStructure = tableParams.getTableStructure();
        TableHeader tableHeader = tableStructure.getHeader();
        CellCoordinates headerCellCoordinates = tableHeader.findColumnHeaderCell(columnOrGroup);
        if (headerCellCoordinates != null) {
            JSONObject header = new JSONObject();
            columnObj.put("header", header);
            header.put("pos", headerCellCoordinates.asJSONObject());
            header.put("className", StyleUtil.getCSSClass(context, styleOwnerComponent, columnOrGroup.getHeaderStyle(), columnOrGroup.getHeaderClass()));
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
            JSONObject filter = new JSONObject();
            columnObj.put("filter", filter);
            filter.put("pos", new CellCoordinates(tableHeader.getSubHeaderRowIndex(), colIndex).asJSONObject());
            filter.put("className", StyleUtil.getCSSClass(context, styleOwnerComponent, columnOrGroup.getSubHeaderStyle(), columnOrGroup.getSubHeaderClass()));
        }
        if (!noDataRows) {
            JSONObject body = new JSONObject();
            columnObj.put("body", body);
            body.put("className", StyleUtil.getCSSClass(context,
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
        TableFooter tableFooter = tableStructure.getFooter();
        CellCoordinates footerCellCoordinates = tableFooter.findColumnHeaderCell(columnOrGroup);
        if (footerCellCoordinates != null) {
            JSONObject footer = new JSONObject();
            columnObj.put("footer", footer);
            footer.put("pos", footerCellCoordinates.asJSONObject());
            footer.put("className", StyleUtil.getCSSClass(context, styleOwnerComponent, columnOrGroup.getFooterStyle(), columnOrGroup.getFooterClass()));
            appendColumnEventsArray(footer,
                    columnOrGroup.getFooterOnclick(),
                    columnOrGroup.getFooterOndblclick(),
                    columnOrGroup.getFooterOnmousedown(),
                    columnOrGroup.getFooterOnmouseover(),
                    columnOrGroup.getFooterOnmousemove(),
                    columnOrGroup.getFooterOnmouseout(),
                    columnOrGroup.getFooterOnmouseup());
        }
        return columnObj;
    }

    private static String getColumnDefaultClass(BaseColumn column) {
        return (String) column.getAttributes().get("defaultStyle");
    }

    private static void addJSONEntry(JSONArray array, String entry) {
        if (entry != null)
            array.put(entry);
        else
            array.put(JSONObject.NULL);
    }

    private static JSONArray getGridLineParams(TableStyles tableStyles, TableStyles defaultStyles) {
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

    private static JSONArray getRowStyleParams(
            FacesContext facesContext,
            TableStyles tableStyles,
            TableStyles defaultStyles,
            UIComponent styleOwnerComponent) {
        AbstractTable table = tableStyles instanceof AbstractTable ? ((AbstractTable) tableStyles) : null;

        JSONArray result = new JSONArray();
        result.put(StyleUtil.getCSSClass(facesContext, styleOwnerComponent, tableStyles.getBodyRowStyle(),
                StyleGroup.regularStyleGroup(), tableStyles.getBodyRowClass(),
                defaultStyles != null ? defaultStyles.getBodyRowClass() : null));
        result.put(StyleUtil.getCSSClass(facesContext, styleOwnerComponent, tableStyles.getBodyOddRowStyle(),
                getBodyOddRowClass(tableStyles, defaultStyles)));
        if (table != null)
            result.put(StyleUtil.getCSSClass(facesContext, styleOwnerComponent, table.getRolloverRowStyle(),
                    StyleGroup.rolloverStyleGroup(), table.getRolloverRowClass()));
        else
            result.put(JSONObject.NULL);
        if (table != null)
            result.put(StyleUtil.getCSSClass(facesContext, styleOwnerComponent, table.getCommonHeaderRowStyle(),
                    StyleGroup.regularStyleGroup(), table.getCommonHeaderRowClass(), DEFAULT_HEADER_CELL_CLASS));
        else
            result.put(JSONObject.NULL);
        result.put(StyleUtil.getCSSClass(facesContext, styleOwnerComponent, tableStyles.getHeaderRowStyle(),
                StyleGroup.regularStyleGroup(), tableStyles.getHeaderRowClass(), DEFAULT_HEADER_CELL_CLASS));
        result.put(StyleUtil.getCSSClass(facesContext, styleOwnerComponent, getFilterRowStyle(tableStyles),
                StyleGroup.regularStyleGroup(), getFilterRowClass(tableStyles), DEFAULT_FILTER_ROW_CELLS_CLASS));
        if (table != null)
            result.put(StyleUtil.getCSSClass(facesContext, styleOwnerComponent,
                    table.getCommonFooterRowStyle(), table.getCommonFooterRowClass()));
        else
            result.put(JSONObject.NULL);
        result.put(StyleUtil.getCSSClass(facesContext, styleOwnerComponent,
                tableStyles.getFooterRowStyle(), tableStyles.getFooterRowClass()));
        return result;
    }

    private static String getColumnBodyStyle(BaseColumn column) {
        String style = column.getBodyStyle();
        if (column instanceof TreeColumn) {
            UIComponent columnParent = column.getParent();
            while (columnParent instanceof TableColumnGroup)
                columnParent = columnParent.getParent();
            TreeTable treeTable = (TreeTable) columnParent;
            String textStyle = treeTable.getTextStyle();
            style = StyleUtil.mergeStyles(style, textStyle);
        }
        return style;
    }

    private static String getColumnBodyClass(BaseColumn column) {
        String cls = column.getBodyClass();
        if (column instanceof TreeColumn) {
            UIComponent columnParent = column.getParent();
            while (columnParent instanceof TableColumnGroup)
                columnParent = columnParent.getParent();
            TreeTable treeTable = (TreeTable) columnParent;
            String textClass = treeTable.getTextClass();
            cls = StyleUtil.mergeClassNames(cls, textClass);
        }
        return cls;
    }

    private static void appendColumnEventsArray(JSONObject columnObj,
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

    private static String getFilterRowClass(TableStyles tableStyles) {
        if (!(tableStyles instanceof AbstractTable))
            return null;
        AbstractTable table = ((AbstractTable) tableStyles);
        return table.getFilterRowClass();
    }

    private static String getFilterRowStyle(TableStyles tableStyles) {
        if (!(tableStyles instanceof AbstractTable))
            return null;
        AbstractTable table = ((AbstractTable) tableStyles);
        return table.getFilterRowStyle();
    }

    private static String getFilterRowSeparator(TableStyles tableStyles) {
        if (!(tableStyles instanceof AbstractTable))
            return null;
        AbstractTable table = ((AbstractTable) tableStyles);
        String result = table.getFilterRowSeparator();
        if (result == null && table.getApplyDefaultStyle())
            result = DEFAULT_FILTER_ROW_SEPARATOR;
        return result;
    }

    public static JSONObject getStylesMapAsJSONObject(Map<Object, String> map) {
        JSONObject result = new JSONObject();
        if (map == null)
            return result;
        for (Map.Entry<Object, String> entry : map.entrySet()) {
            Object key = entry.getKey();
            String className = entry.getValue();
            try {
                result.put(key.toString(), className);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private static String getBodyOddRowClass(TableStyles table, TableStyles defaultStyles) {
        String bodyOddRowClass = table.getBodyOddRowClass();
        return getClassWithDefaultStyleClass(
                defaultStyles != null,
                defaultStyles != null ? defaultStyles.getBodyOddRowStyle() : null,
                bodyOddRowClass);
    }

    private static void appendHandlerEntry(JSONObject obj, String eventName, String handler) throws JSONException {
        if (handler == null)
            return;
        obj.put(eventName, handler);
    }

    public static String getClassWithDefaultStyleClass(boolean applyDefaultStyle, String defaultStyleClass, String styleClass) {
        if (!applyDefaultStyle || defaultStyleClass == null)
            return styleClass;
        return StyleUtil.mergeClassNames(defaultStyleClass, styleClass);
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

    public static UIComponent getColumnSubHeader(BaseColumn column) {
        if (!(column instanceof TableColumn))
            return null;
        TableColumn tableColumn = ((TableColumn) column);

        return tableColumn.getSubHeader();
    }

    public static boolean areGridLinesRequested(TableStyles tableStyles, TableStyles defaultStyles) {
        AbstractTable table = tableStyles instanceof AbstractTable ? ((AbstractTable) tableStyles) : null;
        return getHorizontalGridLines(tableStyles, defaultStyles) != null || getVerticalGridLines(tableStyles, defaultStyles) != null ||
                getCommonHeaderSeparator(tableStyles, defaultStyles) != null || getCommonFooterSeparator(tableStyles, defaultStyles) != null ||
                getHeaderHorizSeparator(tableStyles, defaultStyles) != null || getHeaderVertSeparator(tableStyles, defaultStyles) != null ||
                getFooterHorizSeparator(tableStyles, defaultStyles) != null || getFooterVertSeparator(tableStyles, defaultStyles) != null ||
                getMultiHeaderSeparator(tableStyles, defaultStyles) != null || getMultiFooterSeparator(tableStyles, defaultStyles) != null ||
                (table != null && table.getFilterRowSeparator() != null);
    }


    public static List<BaseColumn> getColumnsFromList(FacesContext context, List<UIComponent> children) {
        List<BaseColumn> columns = new ArrayList<BaseColumn>();
        for (UIComponent child : children) {
            if (child instanceof BaseColumn && !(child instanceof TableColumnGroup)) {
                ComponentUtil.generateIdIfNotSpecified(child);
                columns.add((BaseColumn) child);
            } else if (child instanceof TableColumns) {
                TableColumns tableColumns = (TableColumns) child;
                columns.addAll(tableColumns.toColumnList(context));
            } else if (child instanceof TableColumnGroup) {
                TableColumnGroup tcg = (TableColumnGroup) child;
                columns.addAll(getColumnsFromList(context, tcg.getChildren()));
            }
        }
        return columns;
    }

}
