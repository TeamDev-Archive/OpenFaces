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
import org.openfaces.component.table.AbstractTable;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.DefaultTableStyles;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.List;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class TableStructure {
    private static TableStyles DEFAULT_STYLES = new DefaultTableStyles();
    private static final String DEFAULT_NO_DATA_ROW_CLASS = "o_table_no_data_row";
    public static final String CUSTOM_ROW_RENDERING_INFOS_KEY = "_customRowRenderingInfos";
    
    private final UIComponent table;
    private final TableStyles tableStyles;
    private final List<BaseColumn> columns;

    private final TableHeader header;
    private final TableBody body;
    private final TableFooter footer;


    public TableStructure(UIComponent component, TableStyles tableStyles) {
        table = component;
        this.tableStyles = tableStyles;
        columns = tableStyles.getColumnsForRendering();

        header = new TableHeader(this);
        body = new TableBody(this);
        footer = new TableFooter(this);
    }

    public UIComponent getTable() {
        return table;
    }

    public TableStyles getTableStyles() {
        return tableStyles;
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

    boolean isEmptyCellsTreatmentRequired() {
        if (!(this.table instanceof AbstractTable))
            return true;
        AbstractTable table = (AbstractTable) this.table;
        return (TableUtil.areGridLinesRequested(table, getDefaultStyles(table)) || table.getBorder() != Integer.MIN_VALUE);
    }

    static TableStyles getDefaultStyles(TableStyles table) {
        return table.getApplyDefaultStyle() ? DEFAULT_STYLES : null;
    }

    protected static String getRowStylesKey(FacesContext context, AbstractTable table) {
        String clientId = table.getClientId(context);
        return "OF:row-styles-map-for::" + clientId;
    }

    protected static String getCellStylesKey(FacesContext context, AbstractTable table) {
        String clientId = table.getClientId(context);
        return "OF:cell-styles-map-for::" + clientId;
    }


    public void encodeScriptsAndStyles(FacesContext facesContext) throws IOException {
        
    }

    protected String getAdditionalRowClass(FacesContext facesContext, AbstractTable table, Object rowData, int rowIndex) {
        return null;
    }

    protected void writeBodyRowAttributes(FacesContext facesContext, AbstractTable table) throws IOException {
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


}
