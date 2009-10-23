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
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.ColumnResizingState;
import org.openfaces.component.table.Scrolling;
import org.openfaces.renderkit.DefaultTableStyles;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.EnvironmentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class TableStructure extends TableElement {
    public static final String CUSTOM_ROW_RENDERING_INFOS_KEY = "_customRowRenderingInfos";

    private static final String DEFAULT_STYLE_CLASS = "o_table";
    private static final String DEFAULT_CELL_PADDING = "2";
    private static TableStyles DEFAULT_STYLES = new DefaultTableStyles();
    private static final String DEFAULT_NO_DATA_ROW_CLASS = "o_table_no_data_row";
    private static final String TABLE_LAYOUT_FIXED_STYLE_CLASS = "o_table_layout_fixed";


    private final UIComponent component;
    private final TableStyles tableStyles;
    private final List<BaseColumn> columns;

    private final TableHeader header;
    private final TableBody body;
    private final TableFooter footer;
    private Scrolling scrolling;
    private int fixedLeftColumns;
    private int fixedRightColumns;

    private Map<Object, String> rowStylesMap = new HashMap<Object, String>();
    private Map<Object, String> cellStylesMap = new HashMap<Object, String>();

    public TableStructure(UIComponent component, TableStyles tableStyles) {
        super(null);
        this.component = component;
        this.tableStyles = tableStyles;
        columns = tableStyles.getColumnsForRendering();
        scrolling = tableStyles.getScrolling();
        fixedLeftColumns = 0;
        fixedRightColumns = 0;
        if (scrolling != null && scrolling.isHorizontal()) {
            for (BaseColumn column : columns) {
                if (!isFixedColumn(column))
                    break;
                fixedLeftColumns++;
            }
            for (int i = columns.size() - 1; i > fixedLeftColumns; i--) {
                BaseColumn column = columns.get(i);
                if (!isFixedColumn(column))
                    break;
                fixedRightColumns++;
            }
        }

        header = new TableHeader(this);
        body = new TableBody(this);
        footer = new TableFooter(this);
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

    public int getFixedLeftColumns() {
        return fixedLeftColumns;
    }

    public void setFixedLeftColumns(int fixedLeftColumns) {
        this.fixedLeftColumns = fixedLeftColumns;
    }

    public int getFixedRightColumns() {
        return fixedRightColumns;
    }

    public void setFixedRightColumns(int fixedRightColumns) {
        this.fixedRightColumns = fixedRightColumns;
    }

    public List<BaseColumn> getColumns() {
        return columns;
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

    public void render(FacesContext context, HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException {
        AbstractTable table = (AbstractTable) component;
        table.setRowIndex(-1);
        UIComponent aboveFacet = table.getFacet("above");
        if (aboveFacet != null)
            aboveFacet.encodeAll(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", table);
        RenderingUtil.writeIdAttribute(context, table);

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

        RenderingUtil.writeAttribute(writer, "align", table.getAlign());
        RenderingUtil.writeAttribute(writer, "bgcolor", table.getBgcolor());
        RenderingUtil.writeAttribute(writer, "dir", table.getDir());
        RenderingUtil.writeAttribute(writer, "rules", table.getRules());
        RenderingUtil.writeAttribute(writer, "width", tableWidth);
        RenderingUtil.writeAttribute(writer, "border", table.getBorder(), Integer.MIN_VALUE);
        String cellspacing = table.getCellspacing();
        if (TableUtil.areGridLinesRequested(table, TableStructure.getDefaultStyles(table)))
            cellspacing = "0";
        RenderingUtil.writeAttribute(writer, "cellspacing", cellspacing);
        String cellpadding = table.getCellpadding();
        if (cellpadding == null && applyDefaultStyle)
            cellpadding = DEFAULT_CELL_PADDING;
        RenderingUtil.writeAttribute(writer, "cellpadding", cellpadding);
        RenderingUtil.writeAttribute(writer, "onclick ", table.getOnclick());
        RenderingUtil.writeAttribute(writer, "ondblclick", table.getOndblclick());
        RenderingUtil.writeAttribute(writer, "onmousedown", table.getOnmousedown());
        RenderingUtil.writeAttribute(writer, "onmouseover", table.getOnmouseover());
        RenderingUtil.writeAttribute(writer, "onmousemove", table.getOnmousemove());
        RenderingUtil.writeAttribute(writer, "onmouseout", table.getOnmouseout());
        RenderingUtil.writeAttribute(writer, "onmouseup", table.getOnmouseup());

        writeKeyboardEvents(writer, table);

        if (getScrolling() == null)
            TableUtil.writeColumnTags(context, table, columns);

        TableHeader header = getHeader();
        if (!header.isEmpty())
            header.render(context, null);

        TableBody body = getBody();
        body.render(context, additionalContentWriter);

        table.setRowIndex(-1);

        TableFooter footer = getFooter();
        if (!footer.isEmpty()) {
            footer.render(context, additionalContentWriter);
        }

        writer.endElement("table");
        RenderingUtil.writeNewLine(writer);
        UIComponent belowFacet = table.getFacet("below");
        if (belowFacet != null)
            belowFacet.encodeAll(context);
    }

    boolean isEmptyCellsTreatmentRequired() {
        if (!(this.component instanceof AbstractTable))
            return true;
        AbstractTable table = (AbstractTable) this.component;
        return (TableUtil.areGridLinesRequested(table, getDefaultStyles(table)) || table.getBorder() != Integer.MIN_VALUE);
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

    public void encodeScriptsAndStyles(FacesContext facesContext) throws IOException {
        
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
        boolean result = (value == null || value.toString().trim().length() == 0);
        return result;
    }

    static String getNoDataRowClassName(FacesContext facesContext, AbstractTable table) {
        String styleClassesStr = StyleUtil.getCSSClass(
                facesContext, table, table.getNoDataRowStyle(), table.getApplyDefaultStyle() ? DEFAULT_NO_DATA_ROW_CLASS : null, table.getNoDataRowClass()
        );
        return styleClassesStr;
    }


    private void writeKeyboardEvents(ResponseWriter writer, AbstractTable table) throws IOException {
        RenderingUtil.writeAttribute(writer, "onfocus", table.getOnfocus());
        RenderingUtil.writeAttribute(writer, "onblur", table.getOnblur());
        RenderingUtil.writeAttribute(writer, "onkeydown", table.getOnkeydown());
        RenderingUtil.writeAttribute(writer, "onkeyup", table.getOnkeyup());
        RenderingUtil.writeAttribute(writer, "onkeypress", table.getOnkeypress());
    }

}
