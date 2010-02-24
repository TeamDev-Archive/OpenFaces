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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.ColumnResizingState;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnResizingRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ColumnResizing columnResizing = (ColumnResizing) component;
        if (!columnResizing.isEnabled())
            return;

        UIComponent parent = component.getParent();
        if (!(parent instanceof AbstractTable))
            throw new IllegalStateException("<o:columnResizing> can only be placed as a child component inside of <o:dataTable> or <o:treeTable> components. Though the following parent component has been encountered: " + parent.getClass().getName());
        AbstractTable table = (AbstractTable) component.getParent();

        JSONObject columnParams = new JSONObject();
        List<BaseColumn> columns = table.getRenderedColumns();
        for (int i = 0; i < columns.size(); i++) {
            BaseColumn column = columns.get(i);
            boolean resizable = column.isResizable();
            String minResizingWidth = column.getMinResizingWidth();
            if (!resizable || minResizingWidth != null) {
                JSONObject thisColumnParams = new JSONObject();
                try {
                    if (!resizable)
                        thisColumnParams.put("resizable", resizable);
                    if (minResizingWidth != null)
                        thisColumnParams.put("minWidth", minResizingWidth);
                    columnParams.put(String.valueOf(i), thisColumnParams);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        ScriptBuilder buf = new ScriptBuilder().initScript(context, table, "O$.Table._initColumnResizing",
                columnResizing.getRetainTableWidth(),
                columnResizing.getMinColWidth(),
                columnResizing.getResizeHandleWidth(),
                columnParams);

        Rendering.renderInitScript(context, buf);
    }

    private String getColumnWidthsFieldName(FacesContext context, AbstractTable table) {
        return table.getClientId(context) + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "colWidths";
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        Map requestParams = context.getExternalContext().getRequestParameterMap();
        AbstractTable table = (AbstractTable) component.getParent();
        String colWidthsFieldName = getColumnWidthsFieldName(context, table);
        String params = (String) requestParams.get(colWidthsFieldName);
        if (params == null || params.length() == 0)
            return;
        String[] splitParameters = params.split(":");
        String tableWidth = splitParameters[0];
        String colWidthsArray = splitParameters[1];
        if (tableWidth.length() == 0)
            tableWidth = null;
        JSONArray widthsArray;
        try {
            widthsArray = new JSONArray(colWidthsArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        List<BaseColumn> columns = table.getRenderedColumns();
        if (columns.size() != widthsArray.length())
            throw new IllegalStateException("columns.size() != widthsArray.length(): " + columns.size() + " != " + widthsArray.length());

        ColumnResizing columnResizing = (ColumnResizing) component;
        ColumnResizingState resizingState = columnResizing.getResizingState();
        if (resizingState == null) {
            resizingState = new ColumnResizingState();
            columnResizing.setResizingState(resizingState);
        }

        resizingState.setTableWidth(tableWidth);

        for (int i = 0, count = columns.size(); i < count; i++) {
            BaseColumn column = columns.get(i);
            String newWidth;
            try {
                newWidth = widthsArray.getString(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            resizingState.setColumnWidth(column.getId(), newWidth);
        }
    }
}
