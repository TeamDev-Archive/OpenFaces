/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
import org.openfaces.component.table.DynamicColumn;
import org.openfaces.component.table.RowGroupHeaderOrFooter;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.table.TableBody;
import org.openfaces.util.Components;
import org.openfaces.util.ReflectionUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Styles;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
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
                for (DynamicColumn dynamicColumn : dynamicColumns) {
                    Components.generateIdIfNotSpecified(dynamicColumn);
                }
                columns.addAll(dynamicColumns);
            } else if (child instanceof ColumnGroup) {
                ColumnGroup tcg = (ColumnGroup) child;
                columns.addAll(getColumnsFromList(context, tcg.getChildren()));
            }
        }
        return columns;
    }

    public static void copyColumnAttributes(UIComponent srcColumn, BaseColumn destColumn) {
        String[] copiedAttributes = new String[]{
                "headerValue", "footerValue", "width", "align", "valign", "resizable", "minResizingWidth",
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
            ValueExpression expression = srcColumn.getValueExpression(attrName);
            if (expression != null)
                destColumn.setValueExpression(attrName, expression);
            else {
                Object attributeValue = srcColumn.getAttributes().get(attrName);
                if (attributeValue != null)
                    destColumn.getAttributes().put(attrName, attributeValue);
            }
        }
    }

    /**
     * Matches expression of the form:
     * #{var.a.b}
     *
     * @return Pattern group with index 2 of which selects expression after var
     */
    private static Pattern getExpressionPattern(String var) {
        String pattern = "#\\{" + var + "(\\.)((([\\w\\.])*)+)}";
        return Pattern.compile(pattern);
    }


    private static boolean expressionContainsVar(String expressionString, String var) {
        if (expressionString.equals("#{" + var + "}")) return true;
        Matcher matcher = getExpressionPattern(var).matcher(expressionString);
        return matcher.find();
    }

    public static ValueExpression getColumnValueExpression(BaseColumn column) {
        String var = column.getTable().getVar();
        UIOutput columnOutput = obtainOutput(column, var);
        if (columnOutput == null) return null;
        return columnOutput.getValueExpression("value");
    }

    public static Converter getColumnValueConverter(BaseColumn column) {
        return getColumnExpressionData(column).getValueConverter();
    }

    public static String getColumnHeader(BaseColumn column) {
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
    }

    private static UIOutput obtainOutput(UIComponent component, String var) {

        if (component instanceof UIOutput) {
            ValueExpression valueExpression = component.getValueExpression("value");
            if (valueExpression != null)
                if (expressionContainsVar(valueExpression.getExpressionString(), var)) {
                    return (UIOutput) component;
                }
        }
        List<UIComponent> children;
        if (component instanceof DynamicColumn) {
            children = ((DynamicColumn) component).getChildrenForProcessing();
        } else {
            children = component.getChildren();
        }


        for (UIComponent child : children) {
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

    public static class ColumnExpressionData implements Externalizable {
        private ValueExpression valueExpression;
        private Class valueType;
        private Converter valueConverter;

        public ColumnExpressionData() {
        }

        public ColumnExpressionData(ValueExpression valueExpression, Class valueType, Converter valueConverter) {
            this.valueExpression = valueExpression;
            this.valueType = valueType;
            this.valueConverter = valueConverter;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            ValueBindings.writeValueExpression(out, valueExpression);
            out.writeObject(valueType);
            FacesContext context = FacesContext.getCurrentInstance();
            out.writeObject(UIComponentBase.saveAttachedState(context, valueConverter));
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            valueExpression = ValueBindings.readValueExpression(in);
            valueType = (Class) in.readObject();
            FacesContext context = FacesContext.getCurrentInstance();
            valueConverter = (Converter) UIComponentBase.restoreAttachedState(context, in.readObject());
        }

        public ValueExpression getValueExpression() {
            return valueExpression;
        }

        public Class getValueType() {
            return valueType;
        }

        public Converter getValueConverter() {
            return valueConverter;
        }
    }

    public static ColumnExpressionData getColumnExpressionData(BaseColumn column) {
        return getColumnExpressionData(column, null);
    }

    public static ColumnExpressionData getColumnExpressionData(BaseColumn column, ValueExpression explicitColumnFilterExpression) {
        String cachedDataVar = "_OpenFaces_columnExpressionData";
        ColumnExpressionData data = (ColumnExpressionData) column.getAttributes().get(cachedDataVar);
        if (data != null)
            return data;
        AbstractTable table = column.getTable();
        String var = column.getTable().getVar();
        UIOutput columnOutput = explicitColumnFilterExpression != null ? null : obtainOutput(column, var);
        if (columnOutput == null && explicitColumnFilterExpression == null) throw new FacesException(
                "Can't find column output component (UIOutput component with a value expression containing variable \"" +
                        var + "\") for column with id: \"" + column.getId() + "\"; table id: \"" + table.getId() +
                        "\" ; consider declaring the filter expression explicitly if you're using a filter component in this column.");

        ValueExpression expression = explicitColumnFilterExpression != null
                ? explicitColumnFilterExpression : columnOutput.getValueExpression("value");

        Class valueType = Object.class;
        Converter valueConverter = null;
        FacesContext context = FacesContext.getCurrentInstance();
        int index = table.getRowIndex();
        ELContext elContext = context.getELContext();
        try {
            int i = 0;
            table.setRowIndex(i);
            while (table.isRowAvailable() && table.getRowData() instanceof RowGroupHeaderOrFooter) {
                table.setRowIndex(++i);
            }
            valueType = table.isRowAvailable() ? expression.getType(elContext) : Object.class;
            if (columnOutput != null)
                valueConverter = Rendering.getConverter(context, columnOutput);
            else
                valueConverter = Rendering.getConverterForType(context, valueType);
        } catch (Exception e) {
            // means that there's no row data and row with index == 0
            String expressionString = expression.getExpressionString();
            if (expressionString.split("#").length > 2) { // consist of more than one expression
                valueType = String.class; // multiple expression concatenation result is a string
            } else {
                if (var == null) {
                    throw new IllegalArgumentException("Var isn't specified.");
                }
                ValueExpression rowDataBeanValueExpression = table.getValueExpression("value");
                if (rowDataBeanValueExpression != null) {
                    Object expressionValue = rowDataBeanValueExpression.getValue(elContext);
                    Class rowDataBeanType = expressionValue != null
                            ? expressionValue.getClass()
                            : rowDataBeanValueExpression.getType(elContext);
                    Class rowDataClass = null;
                    if (rowDataBeanType.isArray()) {
                        rowDataClass = rowDataBeanType.getComponentType();
                    } else if (Collection.class.isAssignableFrom(rowDataBeanType)) {
                        rowDataClass = ReflectionUtil.getGenericParameterClass(rowDataBeanType);
                    }
                    if (rowDataClass != null) {
                        Matcher expressionMatcher = getExpressionPattern(var).matcher(expressionString);
                        if (!expressionMatcher.find()) {
                            throw new IllegalArgumentException("Unsupported expression: " + expression);
                        }

                        // String expressionBeforeVar = expressionMatcher.group(2);
                        /*if (expressionBeforeVar != null) {
                            Class typeOfExpressionBefore = ValueBindings.createValueExpression(context, "#{" + expressionBeforeVar + "}").getType(context.getELContext());
                        }*/
                        String expressionAfterVar = expressionMatcher.group(2);
                        valueType = ReflectionUtil.definePropertyType(rowDataClass, expressionAfterVar);
                    }
                }
            }
        } finally {
            table.setRowIndex(index);
        }
        if (valueConverter == null)
            valueConverter = Rendering.getConverterForType(context, valueType);
        ColumnExpressionData result = new ColumnExpressionData(expression, valueType, valueConverter);
        column.getAttributes().put(cachedDataVar, result);
        return result;
    }


}
