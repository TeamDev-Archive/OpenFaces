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
package org.openfaces.renderkit;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.ColumnGroup;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.ColumnResizingState;
import org.openfaces.component.table.Columns;
import org.openfaces.component.table.impl.DynamicColumn;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Styles;

import javax.el.ValueExpression;
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

    private static final String KEY_SORTING_TOGGLED = TableUtil.class + ".sortingToggled";
    private static final String KEY_FILTERING_TOGGLED = TableUtil.class + ".filteringToggled";

    private TableUtil() {
    }

    public static String getTableUtilJsURL(FacesContext context) {
        return Resources.internalURL(context, "tableUtil.js");
    }

    public static void writeColumnTags(
            FacesContext context,
            UIComponent component,
            List<BaseColumn> columns) throws IOException {

        ColumnResizing columnResizing = (component instanceof AbstractTable) ?
                ((AbstractTable) component).getColumnResizing() : null;
        ColumnResizingState columnResizingState = columnResizing != null ? columnResizing.getResizingState() : null;
        ResponseWriter writer = context.getResponseWriter();
        for (BaseColumn column : columns) {
            String colWidth = columnResizingState != null ? columnResizingState.getColumnWidth(column.getId()) : null;
            if (colWidth == null)
                colWidth = column.getWidth();
            String align = column.getAlign();
            String valign = column.getValign();
            writeColTag(component, writer, colWidth, align, valign);
        }
    }

    public static void writeColTag(
            UIComponent component,
            ResponseWriter writer,
            String colWidth,
            String align,
            String valign) throws IOException {
        writer.startElement("col", component);
        Rendering.writeAttribute(writer, "width", colWidth);
        Rendering.writeAttribute(writer, "align", align);
        Rendering.writeAttribute(writer, "valign", valign);
        writer.endElement("col");
    }

    public static Object getStylesMapAsJSONObject(Map<Object, String> map) {
        if (map == null || map.size() == 0)
            return null;
        JSONObject result = new JSONObject();
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
        return Styles.mergeClassNames(defaultStyleClass, styleClass);
    }


    public static List<BaseColumn> getColumnsFromList(FacesContext context, List<UIComponent> children) {
        List<BaseColumn> columns = new ArrayList<BaseColumn>();
        for (UIComponent child : children) {
            if (child instanceof BaseColumn && !(child instanceof ColumnGroup)) {
                Components.generateIdIfNotSpecified(child);
                columns.add((BaseColumn) child);
            } else if (child instanceof Columns) {
                Columns tableColumns = (Columns) child;
                List<DynamicColumn> dynamicColumns = tableColumns.toColumnList(context);
                columns.addAll(dynamicColumns);
            } else if (child instanceof ColumnGroup) {
                ColumnGroup tcg = (ColumnGroup) child;
                columns.addAll(getColumnsFromList(context, tcg.getChildren()));
            }
        }
        return columns;
    }

    public static void copyColumnAttributes(UIComponent srcComponent, BaseColumn destColumn) {
        String[] copiedAttributes = new String[]{
                Column.COLUMN_VALUE_VAR, "headerValue", "footerValue", "converter",
                "width", "align", "valign", "resizable", "minResizingWidth",
                "fixed", "menuAllowed",
                "style", "styleClass", "headerStyle", "headerClass", "subHeaderStyle", "subHeaderClass",
                "bodyStyle", "bodyClass", "footerStyle", "footerClass",
                "onclick", "ondblclick", "onmousedown", "onmouseover", "onmousemove", "onmouseout", "onmouseup",
                "headerOnclick", "headerOndblclick", "headerOnmousedown", "headerOnmouseover",
                "headerOnmousemove", "headerOnmouseout", "headerOnmouseup", "bodyOnclick", "bodyOndblclick",
                "bodyOnmousedown", "bodyOnmouseover", "bodyOnmousemove", "bodyOnmouseout", "bodyOnmouseup",
                "footerOnclick", "footerOndblclick", "footerOnmousedown", "footerOnmouseover", "footerOnmousemove",
                "footerOnmouseout", "footerOnmouseup"};
        for (String attrName : copiedAttributes) {
            ValueExpression expression = srcComponent.getValueExpression(attrName);
            if (expression != null)
                destColumn.setValueExpression(attrName, expression);
            else {
                Object attributeValue = srcComponent.getAttributes().get(attrName);
                if (attributeValue != null)
                    destColumn.getAttributes().put(attrName, attributeValue);
            }
        }
        ValueExpression expression = srcComponent.getValueExpression("value");
        if (expression != null)
            destColumn.setValueExpression("value", expression);

    }

    public static void markSortingToggledInThisRequest(FacesContext context) {
        context.getExternalContext().getRequestMap().put(KEY_SORTING_TOGGLED, true);
    }

    public static boolean isSortingToggledInThisRequest(FacesContext context) {
        return context.getExternalContext().getRequestMap().containsKey(KEY_SORTING_TOGGLED);
    }

    public static void markFilteringToggledInThisRequest(FacesContext context) {
        context.getExternalContext().getRequestMap().put(KEY_FILTERING_TOGGLED, true);
    }

    public static boolean isFilteringToggledInThisRequest(FacesContext context) {
        return context.getExternalContext().getRequestMap().containsKey(KEY_FILTERING_TOGGLED);
    }

}
