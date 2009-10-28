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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.ColumnResizingState;
import org.openfaces.component.table.TableColumnGroup;
import org.openfaces.component.table.TableColumns;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
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

    private TableUtil() {
    }

    public static String getTableUtilJsURL(FacesContext context) {
        return ResourceUtil.getInternalResourceURL(context, TableUtil.class, "tableUtil.js");
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

    public static String getClassWithDefaultStyleClass(boolean applyDefaultStyle, String defaultStyleClass, String styleClass) {
        if (!applyDefaultStyle || defaultStyleClass == null)
            return styleClass;
        return StyleUtil.mergeClassNames(defaultStyleClass, styleClass);
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
