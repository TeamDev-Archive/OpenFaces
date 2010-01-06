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
package org.openfaces.renderkit;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.ColumnGroup;
import org.openfaces.component.table.ColumnResizing;
import org.openfaces.component.table.ColumnResizingState;
import org.openfaces.component.table.Columns;
import org.openfaces.component.table.DynamicCol;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.ReflectionUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.StyleUtil;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ResponseWriter writer = context.getResponseWriter();
        for (int colIndex = 0; colIndex < colCount; colIndex++) {
            BaseColumn column = (BaseColumn) columns.get(colIndex);
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
        RenderingUtil.writeAttribute(writer, "width", colWidth);
        RenderingUtil.writeAttribute(writer, "align", align);
        RenderingUtil.writeAttribute(writer, "valign", valign);
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
        return StyleUtil.mergeClassNames(defaultStyleClass, styleClass);
    }


    public static List<BaseColumn> getColumnsFromList(FacesContext context, List<UIComponent> children) {
        List<BaseColumn> columns = new ArrayList<BaseColumn>();
        for (UIComponent child : children) {
            if (child instanceof BaseColumn && !(child instanceof ColumnGroup)) {
                ComponentUtil.generateIdIfNotSpecified(child);
                columns.add((BaseColumn) child);
            } else if (child instanceof Columns) {
                Columns tableColumns = (Columns) child;
                columns.addAll(tableColumns.toColumnList(context));
            } else if (child instanceof ColumnGroup) {
                ColumnGroup tcg = (ColumnGroup) child;
                columns.addAll(getColumnsFromList(context, tcg.getChildren()));
            }
        }
        return columns;
    }

    /**
     * Matches expression of the form:
     * #{a.b.var.d.c}
     * #{a.b[var].d.c}
     * #{a["b"][var].d.c}
     * #{a['b'][var]d.c}
     * #{a['b'][var]['d']c}
     * #{var.a.b}
     * #{d.c[var]}
     *
     * @return Pattern groups with indexes 2 and 7 of which select expression before and after var accordingly
     */
    /*private static Pattern getExpressionPattern(String var) {
        String pattern = "#\\{((\\w([\\w\\.\\[\\]\\'\\'\\\"\\\"])*)*([\\.\\[])|())" + var + "(\\]\\.|\\]|\\.)((([\\w\\.\\[\\]\\'\\'\\\"\\\"])*)*)}";
        return Pattern.compile(pattern);
    }*/

    /**
     * Matches expression of the form:
     * #{var.a.b}
     *
     * @return Pattern group with indexe 2 of which selects expression after var
     */
    private static Pattern getExpressionPattern(String var) {
        String pattern = "#\\{"+var+"(\\.)((([\\w\\.])*)+)}";
        return Pattern.compile(pattern);
    }


    private static boolean expressionContainsVar(String expressionString, String var) {
        Matcher matcher = getExpressionPattern(var).matcher(expressionString);
        return matcher.find();
    }

    public static UIOutput obtainColumnOutput(BaseColumn column, String var) {
        return obtainOutput(column, var);
    }

    public static String obtainColumnHeader(BaseColumn column) {
        DynamicCol dynamicCol = (column instanceof DynamicCol) ? (DynamicCol) column : null;
        if (dynamicCol != null) dynamicCol.declareContextVariables();
        try {
            String header = column.getHeaderValue();
            if (header != null)
                return header;
            UIComponent component = column.getHeader();
            if (component == null)
                return "";

            return obtainOutputValue(component);
        } finally {
            if (dynamicCol != null) dynamicCol.undeclareContextVariables();
        }

        /*FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter responseWriter = context.getResponseWriter();
        StringWriter content = new StringWriter();
        ResponseWriter contentWriter = responseWriter.cloneWithWriter(content);

        context.setResponseWriter(contentWriter);
        try {
            component.encodeAll(context);
        } catch (IOException e) {
            throw new FacesException(e);
        }
        context.setResponseWriter(responseWriter);
        String componentOutput = content.toString();
        String result = componentOutput.replaceAll("\\<.*?\\>", "");
        return result;*/


    }

    private static UIOutput obtainOutput(UIComponent component, String var) {
        if (component instanceof UIOutput) {
            ValueExpression valueExpression = component.getValueExpression("value");
            if (expressionContainsVar(valueExpression.getExpressionString(), var)) {
                return (UIOutput) component;
            }
        }
        for (UIComponent child : component.getChildren()) {
            UIOutput childOutput = obtainOutput(child, var);
            if (childOutput != null) {
                return childOutput;
            }
        }
        return null;

    }

    private static String obtainOutputValue(UIComponent component) {
        if (component instanceof UIOutput) {
            Object value = ((UIOutput) component).getValue();
            return (value != null) ? value.toString() : "";
        }
        for (UIComponent child : component.getChildren()) {
            String childValue = obtainOutputValue(child);
            if (childValue != null) {
                return childValue;
            }
        }
        return null;

    }

    public static Class getFilteredValueType(
            FacesContext context,
            AbstractTable table,            
            ValueExpression expression) {
        int index = table.getRowIndex();
        try {
            table.setRowIndex(0);
            Class result = expression.getType(context.getELContext());
            table.setRowIndex(index);
            return result;
        } catch (Exception e) {
            // means that there's no row data and row with index=0
            String expressionString = expression.getExpressionString();
            if (expressionString.split("#").length > 2) { //consist of more than one expression
                return String.class; //than concatenation
            }
            String var = table.getVar();
            if (var == null){
                throw new IllegalArgumentException("Var isn't specified.");
            }
            ValueExpression rowDataBeanValueExpression = table.getValueExpression("value");

            Class rowDataBeanType = rowDataBeanValueExpression.getValue(context.getELContext()).getClass();
            Class rowDataClass;
            if (rowDataBeanType.isArray()) {
                rowDataClass = rowDataBeanType.getComponentType();
            } else if (Collection.class.isAssignableFrom(rowDataBeanType)) {
                rowDataClass = ReflectionUtil.getGenericParameterClass(rowDataBeanType);
            } else {
                return Object.class;
            }

            Matcher expressionMatcher = getExpressionPattern(var).matcher(expressionString);
            if (!expressionMatcher.find()) {
                throw new IllegalArgumentException("Unsupported expression: " + expression);
            }

            //String expressionBeforeVar = expressionMatcher.group(2);
            /*if (expressionBeforeVar != null) {
                Class typeOfExpressionBefore = ValueBindings.createValueExpression(context, "#{" + expressionBeforeVar + "}").getType(context.getELContext());
            }*/
            String expressionAfterVar = expressionMatcher.group(2);
            return ReflectionUtil.definePropertyType(rowDataClass, expressionAfterVar);
        }
    }


}
