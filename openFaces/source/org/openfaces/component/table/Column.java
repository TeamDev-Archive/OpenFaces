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

import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Dmitry Pikhulya
 */
public class Column extends BaseColumn implements ValueHolder {
    public static final String COMPONENT_TYPE = "org.openfaces.Column";
    public static final String COMPONENT_FAMILY = "org.openfaces.Column";

    public static final String FACET_EDITOR = "editor";

    public static final String COLUMN_VALUE_VAR = "columnValue";

    private Converter groupingValueConverter;
    private Converter converter;
    private Class type;

    public Column() {
        setRendererType("org.openfaces.ColumnRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                saveAttachedState(context, converter),
                saveAttachedState(context, groupingValueConverter),
                type
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        converter = (Converter) restoreAttachedState(context, state[i++]);
        groupingValueConverter = (Converter) restoreAttachedState(context, state[i++]);
        type = (Class) state[i++];
    }

    public ValueExpression getSortingExpression() {
        return getValueExpression("sortingExpression");
    }

    public void setSortingExpression(ValueExpression sortingExpression) {
        setValueExpression("sortingExpression", sortingExpression);
    }

    public Class getType() {
        return ValueBindings.get(this, "type", type, null);
    }

    public void setType(Class type) {
        this.type = type;
    }

    public ValueExpression getValueExpression() {
        return getValueExpression("value");
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    @Override
    public ValueExpression getColumnValueExpression() {
        ValueExpression ve = getValueExpression();
        if (ve != null)
            return ve;
        else
            return super.getColumnValueExpression();
    }

    public ValueExpression getColumnSortingExpression() {
        ValueExpression ve = getSortingExpression();
        if (ve == null) {
            Sorting sorting = getTable().getSorting();
            if (sorting.isExplicitMode())
                ve = getColumnValueExpression();
        }
        return ve;
    }


    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    @Override
    public ValueExpression getColumnGroupingExpression() {
        ValueExpression ve = getGroupingExpression();
        if (ve == null)
            ve = getValueExpression();
        if (ve == null)
            ve = getSortingExpression();
        if (ve == null)
            ve = getColumnValueExpression();
        return ve;

    }

    public void setValueExpression(ValueExpression valueExpression) {
        setValueExpression("value", valueExpression);
    }

    public ValueExpression getGroupingExpression() {
        return getValueExpression("groupingExpression");
    }

    public void setGroupingExpression(ValueExpression groupingExpression) {
        setValueExpression("groupingExpression", groupingExpression);
    }

    public Converter getConverter() {
        return ValueBindings.get(this, "converter", converter, Converter.class);
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public ValueExpression getSortingComparatorExpression() {
        return getValueExpression("sortingComparator");
    }

    public void setSortingComparatorExpression(ValueExpression sortingComparatorBinding) {
        setValueExpression("sortingComparator", sortingComparatorBinding);
    }


    public Converter getGroupingValueConverter() {
        if (groupingValueConverter != null) return groupingValueConverter;
        ValueExpression ve = getValueExpression("groupingValueConverter");
        if (ve != null)
            return (Converter) ve.getValue(getFacesContext().getELContext());
        boolean explicitExpression = getGroupingExpression() != null;
        if (explicitExpression) {
            // don't derive converter from a column if an groupingExpression is specified explicitly for the column
            // groupingValueConverter attribute should be specified explicitly as well in this case, and if not,
            // the default type converter should be used (we just return null to signify this)
            return null;
        }
        groupingValueConverter = getColumnValueConverter();
        return groupingValueConverter;
    }

    public void setGroupingValueConverter(Converter groupingValueConverter) {
        this.groupingValueConverter = groupingValueConverter;
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
        //Commented to support some unexpected call to this method by Spring + JSF during ajax reRendering
        //throw new UnsupportedOperationException();
        return super.getValueExpression("value");
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void setValue(Object value) {
        //Commented to support some unexpected call to this method by Spring + JSF during ajax reRendering
        //throw new UnsupportedOperationException();
    }

    public UIComponent getEditor() {
        return Components.getFacet(this, FACET_EDITOR);
    }

    public void setEditor(UIComponent editor) {
        getFacets().put(FACET_EDITOR, editor);
    }

    protected UIComponent createImplicitFacet(String facetName) {
        AbstractTable table = getTable();
        if (!(table instanceof DataTable))
            return null;
        DataTable dataTable = (DataTable) table;
        Summaries summaries = dataTable.getSummaries();
        if (summaries == null) return null;

        if (FACET_FOOTER.equals(facetName) && summaries.getFooterVisible()) {
            Summary summary = new Summary(true);
            return summary;
        }
        if (FACET_IN_GROUP_FOOTER.equals(facetName) && summaries.getInGroupFootersVisible()) {
            Summary summary = new Summary(true);
            return summary;
        }
        return null;
    }

}
