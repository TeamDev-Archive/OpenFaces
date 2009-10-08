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
package org.openfaces.renderkit.filter;

import org.openfaces.component.FilterableComponent;
import org.openfaces.component.filter.ExpressionFilter;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.component.filter.PropertyLocator;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RawScript;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StringConverter;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Dmitry Pikhulya
 */
public abstract class ExpressionFilterRenderer extends RendererBase {
    protected static final String DEFAULT_PREDEFINED_CRITERION_CLASS = "o_table_filter_predefined_criterion";

    protected String getFilterSubmissionScript(ExpressionFilter filter) {
        UIComponent component = (UIComponent) filter.getFilteredComponent();
        ExpressionFilter submittedFilter = ComponentUtil.isChildComponent(filter, component) ? null : filter;
        return new ScriptBuilder().functionCall("O$.Filters._filterComponent", component, submittedFilter, new RawScript("this")).
                semicolon().toString();
    }

    protected String getPredefinedCriterionClass(FacesContext context, ExpressionFilter filter) {
        String predefinedCriterionStyle = filter.getPredefinedCriterionStyle();
        return StyleUtil.getCSSClass(context, filter, predefinedCriterionStyle, DEFAULT_PREDEFINED_CRITERION_CLASS, filter.getPredefinedCriterionClass());
    }

    protected void setDecodedCriterion(ExpressionFilter filter, ExpressionFilterCriterion newCriterion) {
        if (!filter.changeCriterion(newCriterion))
            return;
        FilterableComponent filteredComponent = filter.getFilteredComponent();
        filteredComponent.filterChanged(filter);
    }

    protected boolean isEmptyItem(Object item) {
        return item == null || item.equals("");
    }

    protected void setDecodedString(ExpressionFilter filter, String searchString) {
        Converter converter = getConverter(filter);
        ExpressionFilterCriterion oldCriterion = (ExpressionFilterCriterion) filter.getValue();
        ExpressionFilterCriterion newCriterion;
        if (oldCriterion != null &&
                !oldCriterion.getCondition().equals(FilterCondition.EMPTY) &&
                !oldCriterion.getCondition().equals(FilterCondition.CONTAINS) ) {
            newCriterion = new ExpressionFilterCriterion(oldCriterion);
            Object argAsObject = converter.getAsObject(FacesContext.getCurrentInstance(), filter, searchString);
            newCriterion.setArg1(argAsObject);
        } else
            newCriterion = createDefaultCriterion(filter, searchString);
        setDecodedCriterion(filter, newCriterion);
    }

    protected Converter getConverter(ExpressionFilter filter) {
        Converter converter = filter.getConverter();
        return converter != null ? converter : new StringConverter();
    }

    protected ExpressionFilterCriterion createDefaultCriterion(ExpressionFilter filter, Object specifiedValue) {
        Object expression = filter.getExpression();
        PropertyLocator propertyLocator = new PropertyLocator(expression, filter.getFilteredComponent());
        FilterCondition condition = getDefaultCondition();

        ExpressionFilterCriterion criterion = new ExpressionFilterCriterion(
                propertyLocator,
                condition,
                specifiedValue);
        criterion.setCaseSensitive(filter.isCaseSensitive());
        return criterion;
    }

    protected FilterCondition getDefaultCondition() {
        return FilterCondition.CONTAINS;
    }

}
