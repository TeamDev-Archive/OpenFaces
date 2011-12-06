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
package org.openfaces.component.table;

import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Dmitry Pikhulya
 */
public class Column extends BaseColumn {
    public static final String COMPONENT_TYPE = "org.openfaces.Column";
    public static final String COMPONENT_FAMILY = "org.openfaces.Column";
    private javax.faces.convert.Converter groupingValueConverter;

    public Column() {
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
    }

    public ValueExpression getSortingExpression() {
        return getValueExpression("sortingExpression");
    }

    public void setSortingExpression(ValueExpression sortingExpression) {
        setValueExpression("sortingExpression", sortingExpression);
    }

    public ValueExpression getGroupingExpression() {
        return getValueExpression("groupingExpression");
    }

    public void setGroupingExpression(ValueExpression groupingExpression) {
        setValueExpression("groupingExpression", groupingExpression);
    }

    public ValueExpression getSortingComparatorBinding() {
        return getValueExpression("sortingComparator");
    }

    public void setSortingComparatorExpression(ValueExpression sortingComparatorBinding) {
        setValueExpression("sortingComparator", sortingComparatorBinding);
    }


    public UIComponent getSubHeader() {
        return Components.getFacet(this, "subHeader");
    }

    public void setSubHeader(UIComponent component) {
        getFacets().put("subHeader", component);
    }

    public Converter getGroupingValueConverter() {
        if (groupingValueConverter != null) return groupingValueConverter;
        ValueExpression ve = getValueExpression("converter");
        if (ve != null)
            return (Converter) ve.getValue(getFacesContext().getELContext());
        boolean explicitExpression = getGroupingExpression() != null;
        if (explicitExpression) {
            // don't derive converter from a column if an groupingExpression is specified explicitly for the column
            // groupingValueConverter attribute should be specified explicitly as well in this case, and if not,
            // the default type converter should be used (we just return null to signify this)
            return null;
        }
        groupingValueConverter = TableUtil.getColumnValueConverter(this);
        return groupingValueConverter;
    }

    public void setGroupingValueConverter(Converter groupingValueConverter) {
        this.groupingValueConverter = groupingValueConverter;
    }
}
