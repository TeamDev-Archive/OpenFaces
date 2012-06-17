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

package org.openfaces.component.table;

import org.openfaces.component.OUIData;
import org.openfaces.component.table.impl.ContextDependentExpressionWrapper;
import org.openfaces.component.table.impl.DynamicColumn;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Columns extends UIComponentBase implements ValueHolder, NamingContainer {
    public static final String COMPONENT_TYPE = "org.openfaces.Columns";
    public static final String COMPONENT_FAMILY = "org.openfaces.Columns";

    /**
     * This constant is used in OpenFaces internally and it shouldn't be used by application code explicitly.
     */
    public static final String FACET_COLUMN_COMPONENTS = "_column_components_";

    private Object prevValueFromBinding;

    private List<DynamicColumn> columnList;

    private Boolean columnRendered;
    private String headerValue;
    private String footerValue;
    private String var;
    private Boolean sortingEnabled;
    private Comparator sortingComparator;
    private Converter converter;

    private String align;
    private String valign;
    private String width;

    private Boolean resizable;
    private String minResizingWidth;
    private Boolean fixed;
    private Boolean menuAllowed;

    private String style;
    private String styleClass;
    private String headerStyle;
    private String headerClass;
    private String subHeaderStyle;
    private String subHeaderClass;
    private String bodyStyle;
    private String bodyClass;
    private String footerStyle;
    private String footerClass;

    private String onclick;
    private String ondblclick;
    private String onmousedown;
    private String onmouseover;
    private String onmousemove;
    private String onmouseout;
    private String onmouseup;

    private String headerOnclick;
    private String headerOndblclick;
    private String headerOnmousedown;
    private String headerOnmouseover;
    private String headerOnmousemove;
    private String headerOnmouseout;
    private String headerOnmouseup;

    private String bodyOnclick;
    private String bodyOndblclick;
    private String bodyOnmousedown;
    private String bodyOnmouseover;
    private String bodyOnmousemove;
    private String bodyOnmouseout;
    private String bodyOnmouseup;

    private String footerOnclick;
    private String footerOndblclick;
    private String footerOnmousedown;
    private String footerOnmouseover;
    private String footerOnmousemove;
    private String footerOnmouseout;
    private String footerOnmouseup;

    private int columnIndex = -1;
    private Object defaultState;
    private Map<String, Object> columnStates = new HashMap<String, Object>();
    private String indexVar;
    private Class columnType;

    public Columns() {
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        if (getColumnIndex() != -1) {
            // the index might not be reset in some circumstances (e.g. rendering in-group summaries in a grouped table)
            // make sure the state is going to be saved under a correct client id in the JSF's state map
            setColumnIndex(-1);
        }
        return new Object[]{
                super.saveState(context), prevValueFromBinding,
                columnRendered, headerValue, footerValue, var, indexVar, sortingEnabled, sortingComparator,
                align, valign, width, resizable, minResizingWidth, fixed, menuAllowed,
                style, styleClass, headerStyle, headerClass,
                subHeaderStyle, subHeaderClass, bodyStyle, bodyClass, footerStyle, footerClass,
                onclick, ondblclick, onmousedown, onmouseover, onmousemove,
                onmouseout, onmouseup, headerOnclick, headerOndblclick, headerOnmousedown, headerOnmouseover,
                headerOnmousemove, headerOnmouseout, headerOnmouseup, bodyOnclick, bodyOndblclick,
                bodyOnmousedown, bodyOnmouseover, bodyOnmousemove, bodyOnmouseout, bodyOnmouseup,
                footerOnclick, footerOndblclick, footerOnmousedown, footerOnmouseover, footerOnmousemove,
                footerOnmouseout, footerOnmouseup,
                saveAttachedState(context, converter),
                columnType
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        prevValueFromBinding = state[i++];
        columnRendered = (Boolean) state[i++];
        headerValue = (String) state[i++];
        footerValue = (String) state[i++];
        var = (String) state[i++];
        indexVar = (String) state[i++];
        sortingEnabled = (Boolean) state[i++];
        sortingComparator = (Comparator) state[i++];

        align = (String) state[i++];
        valign = (String) state[i++];
        width = (String) state[i++];
        resizable = (Boolean) state[i++];
        minResizingWidth = (String) state[i++];
        fixed = (Boolean) state[i++];
        menuAllowed = (Boolean) state[i++];
        style = (String) state[i++];
        styleClass = (String) state[i++];
        headerStyle = (String) state[i++];
        headerClass = (String) state[i++];
        subHeaderStyle = (String) state[i++];
        subHeaderClass = (String) state[i++];
        bodyStyle = (String) state[i++];
        bodyClass = (String) state[i++];
        footerStyle = (String) state[i++];
        footerClass = (String) state[i++];
        onclick = (String) state[i++];
        ondblclick = (String) state[i++];
        onmousedown = (String) state[i++];
        onmouseover = (String) state[i++];
        onmousemove = (String) state[i++];
        onmouseout = (String) state[i++];
        onmouseup = (String) state[i++];
        headerOnclick = (String) state[i++];
        headerOndblclick = (String) state[i++];
        headerOnmousedown = (String) state[i++];
        headerOnmouseover = (String) state[i++];
        headerOnmousemove = (String) state[i++];
        headerOnmouseout = (String) state[i++];
        headerOnmouseup = (String) state[i++];
        bodyOnclick = (String) state[i++];
        bodyOndblclick = (String) state[i++];
        bodyOnmousedown = (String) state[i++];
        bodyOnmouseover = (String) state[i++];
        bodyOnmousemove = (String) state[i++];
        bodyOnmouseout = (String) state[i++];
        bodyOnmouseup = (String) state[i++];
        footerOnclick = (String) state[i++];
        footerOndblclick = (String) state[i++];
        footerOnmousedown = (String) state[i++];
        footerOnmouseover = (String) state[i++];
        footerOnmousemove = (String) state[i++];
        footerOnmouseout = (String) state[i++];
        footerOnmouseup = (String) state[i++];
        converter = (Converter) restoreAttachedState(context, state[i++]);
        columnType = (Class) state[i++];
    }

    public ValueExpression getValueExpression() {
        return getValueExpression("value");
    }

    public void setValueExpression(ValueExpression binding) {
        setValueExpression("value", binding);
    }

    public Converter getConverter() {
        return ValueBindings.get(this, "converter", converter, Converter.class);
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }


    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public ValueExpression getColumnIdExpression() {
        return getValueExpression("columnId");
    }

    public void setColumnIdExpression(ValueExpression binding) {
        setValueExpression("columnId", binding);
    }

    public boolean getColumnRendered() {
        return ValueBindings.get(this, "columnRendered", columnRendered, true);
    }

    public void setColumnRendered(boolean columnRendered) {
        this.columnRendered = columnRendered;
    }

    public void setColumnValueExpression(ValueExpression ve) {
        setValueExpression("columnValue", ve);
    }

    public ValueExpression getColumnValueExpression() {
        return getValueExpression("columnValue");
    }

    public String getHeaderValue() {
        return ValueBindings.get(this, "headerValue", headerValue);
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getFooterValue() {
        return ValueBindings.get(this, "footerValue", footerValue);
    }

    public void setFooterValue(String footerValue) {
        this.footerValue = footerValue;
    }

    public ValueExpression getSortingExpression() {
        return getValueExpression("sortingExpression");
    }

    public void setSortingExpression(ValueExpression sortingExpression) {
        setValueExpression("sortingExpression", sortingExpression);
    }

    public boolean getSortingEnabled() {
        return ValueBindings.get(this, "sortingEnabled", sortingEnabled, true);
    }

    public void setSortingEnabled(boolean sortingEnabled) {
        this.sortingEnabled = sortingEnabled;
    }

    public Comparator getSortingComparator() {
        return ValueBindings.get(this, "sortingComparator", sortingComparator, Comparator.class);
    }

    public void setSortingComparator(Comparator sortingComparator) {
        this.sortingComparator = sortingComparator;
    }

    public String getAlign() {
        return ValueBindings.get(this, "align", align);
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getValign() {
        return ValueBindings.get(this, "valign", valign);
    }

    public void setValign(String valign) {
        this.valign = valign;
    }

    public String getWidth() {
        return ValueBindings.get(this, "width", width);
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public boolean isResizable() {
        return ValueBindings.get(this, "resizable", resizable, true);
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public String getMinResizingWidth() {
        return ValueBindings.get(this, "minResizingWidth", minResizingWidth);
    }

    public void setMinResizingWidth(String minResizingWidth) {
        this.minResizingWidth = minResizingWidth;
    }

    public boolean getFixed() {
        return ValueBindings.get(this, "fixed", fixed, false);
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean getMenuAllowed() {
        return ValueBindings.get(this, "menuAllowed", menuAllowed, true);
    }

    public void setMenuAllowed(boolean menuAllowed) {
        this.menuAllowed = menuAllowed;
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }


    public String getSubHeaderStyle() {
        return ValueBindings.get(this, "subHeaderStyle", subHeaderStyle);
    }

    public void setSubHeaderStyle(String subHeaderStyle) {
        this.subHeaderStyle = subHeaderStyle;
    }

    public String getBodyStyle() {
        return ValueBindings.get(this, "bodyStyle", bodyStyle);
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public String getFooterStyle() {
        return ValueBindings.get(this, "footerStyle", footerStyle);
    }

    public void setFooterStyle(String footerStyle) {
        this.footerStyle = footerStyle;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getSubHeaderClass() {
        return ValueBindings.get(this, "subHeaderClass", subHeaderClass);
    }

    public void setSubHeaderClass(String subHeaderClass) {
        this.subHeaderClass = subHeaderClass;
    }

    public String getBodyClass() {
        return ValueBindings.get(this, "bodyClass", bodyClass);
    }

    public void setBodyClass(String bodyClass) {
        this.bodyClass = bodyClass;
    }

    public String getFooterClass() {
        return ValueBindings.get(this, "footerClass", footerClass);
    }

    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }


    public String getOnclick() {
        return ValueBindings.get(this, "onclick", onclick);
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getOndblclick() {
        return ValueBindings.get(this, "ondblclick", ondblclick);
    }

    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    public String getOnmousedown() {
        return ValueBindings.get(this, "onmousedown", onmousedown);
    }

    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    public String getOnmouseover() {
        return ValueBindings.get(this, "onmouseover", onmouseover);
    }

    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    public String getOnmousemove() {
        return ValueBindings.get(this, "onmousemove", onmousemove);
    }

    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    public String getOnmouseout() {
        return ValueBindings.get(this, "onmouseout", onmouseout);
    }

    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    public String getOnmouseup() {
        return ValueBindings.get(this, "onmouseup", onmouseup);
    }

    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }


    public String getHeaderOnclick() {
        return ValueBindings.get(this, "headerOnclick", headerOnclick);
    }

    public void setHeaderOnclick(String onclick) {
        headerOnclick = onclick;
    }

    public String getHeaderOndblclick() {
        return ValueBindings.get(this, "headerOndblclick", headerOndblclick);
    }

    public void setHeaderOndblclick(String ondblclick) {
        headerOndblclick = ondblclick;
    }

    public String getHeaderOnmousedown() {
        return ValueBindings.get(this, "headerOnmousedown", headerOnmousedown);
    }

    public void setHeaderOnmousedown(String onmousedown) {
        headerOnmousedown = onmousedown;
    }

    public String getHeaderOnmouseover() {
        return ValueBindings.get(this, "headerOnmouseover", headerOnmouseover);
    }

    public void setHeaderOnmouseover(String onmouseover) {
        headerOnmouseover = onmouseover;
    }

    public String getHeaderOnmousemove() {
        return ValueBindings.get(this, "headerOnmousemove", headerOnmousemove);
    }

    public void setHeaderOnmousemove(String onmousemove) {
        headerOnmousemove = onmousemove;
    }

    public String getHeaderOnmouseout() {
        return ValueBindings.get(this, "headerOnmouseout", headerOnmouseout);
    }

    public void setHeaderOnmouseout(String onmouseout) {
        headerOnmouseout = onmouseout;
    }

    public String getHeaderOnmouseup() {
        return ValueBindings.get(this, "headerOnmouseup", headerOnmouseup);
    }

    public void setHeaderOnmouseup(String onmouseup) {
        headerOnmouseup = onmouseup;
    }


    public String getBodyOnclick() {
        return ValueBindings.get(this, "bodyOnclick", bodyOnclick);
    }

    public void setBodyOnclick(String onclick) {
        bodyOnclick = onclick;
    }

    public String getBodyOndblclick() {
        return ValueBindings.get(this, "bodyOndblclick", bodyOndblclick);
    }

    public void setBodyOndblclick(String ondblclick) {
        bodyOndblclick = ondblclick;
    }

    public String getBodyOnmousedown() {
        return ValueBindings.get(this, "bodyOnmousedown", bodyOnmousedown);
    }

    public void setBodyOnmousedown(String onmousedown) {
        bodyOnmousedown = onmousedown;
    }

    public String getBodyOnmouseover() {
        return ValueBindings.get(this, "bodyOnmouseover", bodyOnmouseover);
    }

    public void setBodyOnmouseover(String onmouseover) {
        bodyOnmouseover = onmouseover;
    }

    public String getBodyOnmousemove() {
        return ValueBindings.get(this, "bodyOnmousemove", bodyOnmousemove);
    }

    public void setBodyOnmousemove(String onmousemove) {
        bodyOnmousemove = onmousemove;
    }

    public String getBodyOnmouseout() {
        return ValueBindings.get(this, "bodyOnmouseout", bodyOnmouseout);
    }

    public void setBodyOnmouseout(String onmouseout) {
        bodyOnmouseout = onmouseout;
    }

    public String getBodyOnmouseup() {
        return ValueBindings.get(this, "bodyOnmouseup", bodyOnmouseup);
    }

    public void setBodyOnmouseup(String onmouseup) {
        bodyOnmouseup = onmouseup;
    }


    public String getFooterOnclick() {
        return ValueBindings.get(this, "footerOnclick", footerOnclick);
    }

    public void setFooterOnclick(String onclick) {
        footerOnclick = onclick;
    }

    public String getFooterOndblclick() {
        return ValueBindings.get(this, "footerOndblclick", footerOndblclick);
    }

    public void setFooterOndblclick(String ondblclick) {
        footerOndblclick = ondblclick;
    }

    public String getFooterOnmousedown() {
        return ValueBindings.get(this, "footerOnmousedown", footerOnmousedown);
    }

    public void setFooterOnmousedown(String onmousedown) {
        footerOnmousedown = onmousedown;
    }

    public String getFooterOnmouseover() {
        return ValueBindings.get(this, "footerOnmouseover", footerOnmouseover);
    }

    public void setFooterOnmouseover(String onmouseover) {
        footerOnmouseover = onmouseover;
    }

    public String getFooterOnmousemove() {
        return ValueBindings.get(this, "footerOnmousemove", footerOnmousemove);
    }

    public void setFooterOnmousemove(String onmousemove) {
        footerOnmousemove = onmousemove;
    }

    public String getFooterOnmouseout() {
        return ValueBindings.get(this, "footerOnmouseout", footerOnmouseout);
    }

    public void setFooterOnmouseout(String onmouseout) {
        footerOnmouseout = onmouseout;
    }

    public String getFooterOnmouseup() {
        return ValueBindings.get(this, "footerOnmouseup", footerOnmouseup);
    }

    public void setFooterOnmouseup(String onmouseup) {
        footerOnmouseup = onmouseup;
    }

    public AbstractTable getTable() {
        UIComponent parent = getParent();
        while (parent instanceof ColumnGroup)
            parent = parent.getParent();
        if (parent != null && !(parent instanceof AbstractTable))
            throw new RuntimeException("Columns can only be inserted inside DataTable or TreeTable. Column id: " + getId());

        return (AbstractTable) parent;
    }

    public void setHeader(UIComponent header) {
        getFacets().put(BaseColumn.FACET_HEADER, header);
    }

    public UIComponent getHeader() {
        return getFacets().get(BaseColumn.FACET_HEADER);
    }

    public void setSubHeader(UIComponent header) {
        getFacets().put(BaseColumn.FACET_SUB_HEADER, header);
    }

    public UIComponent getSubHeader() {
        return getFacets().get(BaseColumn.FACET_SUB_HEADER);
    }

    public void setFooter(UIComponent footer) {
        getFacets().put(BaseColumn.FACET_FOOTER, footer);
    }

    public UIComponent getFooter() {
        return getFacets().get(BaseColumn.FACET_FOOTER);
    }

    public UIComponent getGroupHeader() {
        return getFacets().get(BaseColumn.FACET_GROUP_HEADER);
    }

    public UIComponent getGroupFooter() {
        return getFacets().get(BaseColumn.FACET_GROUP_FOOTER);
    }

    public UIComponent getInGroupHeader() {
        return getFacets().get(BaseColumn.FACET_IN_GROUP_HEADER);
    }

    public UIComponent getInGroupFooter() {
        return getFacets().get(BaseColumn.FACET_IN_GROUP_FOOTER);
    }

    public List<DynamicColumn> toColumnList(FacesContext context) {
        if (columnList == null) {
            columnList = createColumnList(context);

            for (DynamicColumn dynamicColumn : columnList) {
                Components.generateIdIfNotSpecified(dynamicColumn);
            }
        }
        return columnList;
    }

    private List<DynamicColumn> createColumnList(FacesContext context) {
        boolean[] prevValueChangedFlag = new boolean[1];
        Collection colDatas = getColDatas(context, prevValueChangedFlag);

        List oldColumns = getColumnComponents();
        int currentColCount = oldColumns.size();
        boolean justUpdateExistingColumns = !prevValueChangedFlag[0] &&
                currentColCount > 0 && colDatas.size() == currentColCount;

        List<DynamicColumn> newColumns = new ArrayList<DynamicColumn>(colDatas.size());
        String var = getVar();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Object prevVarValue = requestMap.get(var);
        ValueExpression idExpression = getColumnIdExpression();
        int colIndex = 0;
        for (Iterator iterator = colDatas.iterator(); iterator.hasNext(); colIndex++) {
            Object colData = iterator.next();
            requestMap.put(var, colData);
            DynamicColumn column = justUpdateExistingColumns
                    ? (DynamicColumn) oldColumns.get(colIndex)
                    : new DynamicColumn();
            newColumns.add(column);
            if (!justUpdateExistingColumns) {
                if (idExpression != null)
                    column.setId((String) idExpression.getValue(getFacesContext().getELContext()));
                else {
                    column.setId(getId() + "_" + colIndex);
                }
            }

            column.setColumns(this);
            column.setColData(colData);
            column.setColIndex(colIndex);

            column.setRendered(getColumnRendered());
            column.setType(getColumnType());

            TableUtil.copyColumnAttributes(this, column);

            column.setValueExpression(getColumnValueExpression());

            applySortingParameters(column);
            applyFilteringParameters(context, column);

            requestMap.put(var, prevVarValue);
        }

        if (!justUpdateExistingColumns)
            setColumnComponents(newColumns);

        return newColumns;
    }

    private void applyFilteringParameters(FacesContext context, DynamicColumn column) {
        // todo: review this with new filtering API
//        FilterKind filterKind = getFilterKind();
//        if (filterKind != null) {
//            column.setFilterKind(filterKind);
//            ValueExpression filterExpressionExpression = getFilterExpression();
//            if (filterExpressionExpression != null)
//                column.setFilterExpression(new FilterExpressionExpression(column, filterExpressionExpression));
//            ValueExpression filterValuesBinding = getFilterValuesExpression();
//            if (filterValuesBinding != null) {
//                Object filterValues = filterValuesBinding.getValue(context.getELContext());
//                if (filterValues != null)
//                    column.setFilterValuesExpression(new ConstantValueExpression(Object.class, filterValues));
//            }
//            ValueExpression filterValueExpression = getFilterValueExpression();
//            if (filterValueExpression != null) {
//                column.setFilterValueExpression(new FilterValueExpression(column, filterValueExpression));
//            }
//        }
    }

    private boolean objectsEqual(Object o1, Object o2) {
        if (o1 == null)
            return o2 == null;
        if (o2 == null)
            return false;
        if (o1 == o2)
            return true;
        return o1.equals(o2);
    }

    private Collection getColDatas(FacesContext context, boolean[] bindingValueChangedFlag) {
        ValueExpression valueExpression = getValueExpression();
        Object value = valueExpression.getValue(context.getELContext());
        if (!objectsEqual(value, prevValueFromBinding)) {
            bindingValueChangedFlag[0] = true;
        }
        prevValueFromBinding = value;
        Collection colDatas;
        if (value == null)
            colDatas = Collections.EMPTY_LIST;
        else {
            if (value instanceof Collection)
                colDatas = (Collection) value;
            else if (value.getClass().isArray()) {
                colDatas = new ArrayList();
                for (int i = 0, count = Array.getLength(value); i < count; i++) {
                    Object element = Array.get(value, i);
                    colDatas.add(element);
                }
            } else
                throw new IllegalArgumentException("Unsupported object type received from o:columns value attribute: " + value.getClass().getName() + "; should be either array or collection");
        }
        return colDatas;
    }

    private void applySortingParameters(DynamicColumn column) {
        if (getSortingEnabled()) {
            if (sortingComparator != null || getValueExpression("sortingComparator") != null)
                column.setSortingComparatorExpression(new ConstantValueExpression(Comparator.class, sortingComparator) {
                    @Override
                    public Object getValue(ELContext elContext) {
                        return getSortingComparator();
                    }
                });
            ValueExpression sortingExpressionBinding = getSortingExpression();
            if (sortingExpressionBinding != null)
                column.setSortingExpression(sortingExpressionBinding);
        }
    }

    @Override
    public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException {
        if (clientId.equals(this.getClientId(context)))
            return super.invokeOnComponent(context, clientId, callback);

        for (DynamicColumn dynamicColumn : toColumnList(context)) {
            if (dynamicColumn.invokeOnComponent(context, clientId, callback))
                return true;
        }

        return false;
    }

    @Override
    public void processDecodes(FacesContext context) {
//    columnList = null;
        List<DynamicColumn> columns = toColumnList(context);
        for (DynamicColumn column : columns) {
            if (!column.isRendered())
                continue;
            column.processDecodes(context);
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        List<DynamicColumn> columns = toColumnList(context);
        for (DynamicColumn column : columns) {
            if (!column.isRendered())
                continue;
            column.processValidators(context);
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        List<DynamicColumn> columns = toColumnList(context);
        for (DynamicColumn column : columns) {
            if (!column.isRendered())
                continue;
            column.processUpdates(context);
        }
    }

    private List getColumnComponents() {
        UIComponent columnContainer = getFacet(FACET_COLUMN_COMPONENTS);
        if (columnContainer == null)
            return Collections.EMPTY_LIST;
        return columnContainer.getChildren();
    }

    private void setColumnComponents(List<DynamicColumn> columns) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent columnContainer = Components.createComponent(context, "javax.faces.HtmlPanelGroup", UIComponent.class, this, "_columnsContainer_");
        getFacets().put(FACET_COLUMN_COMPONENTS, columnContainer);
        columnContainer.getChildren().addAll(columns);
    }

    public void resetCachedColumnList() {
        columnList = null;
    }

    public String getIndexVar() {
        return indexVar;
    }

    public void setIndexVar(String indexVar) {
        this.indexVar = indexVar;
    }

    public Class getColumnType() {
        return ValueBindings.get(this, "columnType", columnType, null);
    }

    public void setColumnType(Class columnType) {
        this.columnType = columnType;
    }


    public static class ConstantValueExpression extends ValueExpression implements Serializable {
        private transient Class type;
        private transient Object value;

        public ConstantValueExpression() {
        }

        public ConstantValueExpression(Class type, Object value) {
            this.type = type;
            this.value = value;
        }

        public Object getValue(ELContext elContext) {
            return value;
        }

        public void setValue(ELContext elContext, Object value) {
            throw new UnsupportedOperationException("This binding is read-only");
        }

        public boolean isReadOnly(ELContext elContext) {
            return true;
        }

        public Class getType(ELContext elContext) {
            return type;
        }

        public Class getExpectedType() {
            return type;
        }

        public String getExpressionString() {
            return "";
        }

        public boolean equals(Object o) {
            return false;
        }

        public int hashCode() {
            return 0;
        }

        public boolean isLiteralText() {
            return false;
        }
    }

    public static class FilterExpressionExpression extends ContextDependentExpressionWrapper {
        public FilterExpressionExpression() {
        }

        public FilterExpressionExpression(DynamicColumn column, ValueExpression expressionFromColumnsComponent) {
            super(column, expressionFromColumnsComponent);
        }
    }

    public static class FilterValueExpression extends ContextDependentExpressionWrapper {
        public FilterValueExpression() {
        }

        public FilterValueExpression(DynamicColumn column, ValueExpression expressionFromColumnsComponent) {
            super(column, expressionFromColumnsComponent);
        }

        public boolean isReadOnly(ELContext elContext) {
            return false;
        }

    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        if (this.columnIndex == columnIndex)
            return;
        FacesContext facesContext = getFacesContext();
        if (this.columnIndex == -1) {
            if (defaultState == null)
                defaultState = OUIData.saveDescendantComponentStates(getFacetsAndChildren(), false);
        } else {
            columnStates.put(getClientId(facesContext), OUIData.saveDescendantComponentStates(getFacetsAndChildren(), false));
        }
        this.columnIndex = columnIndex;
        if (this.columnIndex == -1) {
            OUIData.restoreDescendantComponentStates(getFacetsAndChildren(), defaultState, false);
        } else {
            Object state = columnStates.get(getClientId(facesContext));
            if (state == null)
                OUIData.restoreDescendantComponentStates(getFacetsAndChildren(), defaultState, false);
            else
                OUIData.restoreDescendantComponentStates(getFacetsAndChildren(), state, false);
        }
        resetClientIdsForChildren(this);
    }

    public Object getColumnStates() {
        return columnStates;
    }

    public void resetColumnStates(boolean noValidationErrors) {
        defaultState = null;
        if (noValidationErrors)
            columnStates = new HashMap<String, Object>();
    }

    public void setColumnStates(Object columnStates) {
        this.columnStates = (Map<String, Object>) columnStates;
    }

    private void resetClientIdsForChildren(UIComponent parent) {
        for (Iterator<UIComponent> iterator = parent.getFacetsAndChildren(); iterator.hasNext(); ) {
            UIComponent component = iterator.next();
            component.setId(component.getId()); // schedule clientId for recalculation (see spec 3.1.6)
            resetClientIdsForChildren(component);
        }
    }

    public String getClientId(FacesContext context) {
        String clientId = super.getClientId(context);
        int columnIndex = getColumnIndex();
        if (columnIndex == -1) {
            return clientId;
        }
        return clientId + UINamingContainer.getSeparatorChar(context) + columnIndex;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public Object getLocalValue() {
        // can't throw UnsupportedOperationException when a column has any converter tag because Facelts' ConvertHandler
        // invokes this method and tries to convert it into Object if this method returns a string value, so we're just
        // returning null here.
        return null;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public Object getValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void setValue(Object value) {
        throw new UnsupportedOperationException();
    }


}
