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
import org.openfaces.component.filter.Filter;
import org.openfaces.component.filter.OperationType;
import org.openfaces.component.filter.criterion.ExpressionPropertyLocator;
import org.openfaces.component.filter.criterion.PropertyFilterCriterion;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.RawScript;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.StringConverter;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Dmitry Pikhulya
 */
public abstract class FilterRenderer extends RendererBase {
    protected static final String DEFAULT_PREDEFINED_CRITERION_CLASS = "o_table_filter_predefined_criterion";

    protected String getFilterSubmissionScript(Filter filter) {
        UIComponent component = (UIComponent) filter.getFilteredComponent();
        Filter submittedFilter = ComponentUtil.isChildComponent(filter, component) ? null : filter;
        return new ScriptBuilder().functionCall("O$.Filters._filterComponent", component, submittedFilter, new RawScript("this")).
                semicolon().toString();
    }

    protected String getPredefinedCriterionClass(FacesContext context, Filter filter) {
        String predefinedCriterionStyle = filter.getPredefinedCriterionStyle();
        return StyleUtil.getCSSClass(context, filter, predefinedCriterionStyle, DEFAULT_PREDEFINED_CRITERION_CLASS, filter.getPredefinedCriterionClass());
    }

    protected void setDecodedCriterion(Filter filter, PropertyFilterCriterion newCriterion) {
        if (!filter.changeCriterion(newCriterion))
            return;
        FilterableComponent filteredComponent = filter.getFilteredComponent();
        filteredComponent.filterChanged(filter);
    }

    protected boolean isEmptyItem(Object item) {
        return item == null || item.equals("");
    }

    protected void setDecodedString(Filter filter, String searchString) {
        Converter converter = getConverter(filter);
        PropertyFilterCriterion oldCriterion = (PropertyFilterCriterion) filter.getValue();
        PropertyFilterCriterion newCriterion;
        if (oldCriterion != null &&
                !oldCriterion.getOperation().equals(OperationType.EMPTY) &&
                !oldCriterion.getOperation().equals(OperationType.CONTAINS) ) {
            newCriterion = new PropertyFilterCriterion(oldCriterion);
            Object argAsObject = converter.getAsObject(FacesContext.getCurrentInstance(), filter, searchString);
            newCriterion.setArg1(argAsObject);
        } else
            newCriterion = createDefaultCriterion(filter, searchString);
        setDecodedCriterion(filter, newCriterion);
    }

    protected Converter getConverter(Filter filter) {
        Converter converter = filter.getConverter();
        return converter != null ? converter : new StringConverter();
    }

    protected PropertyFilterCriterion createDefaultCriterion(Filter filter, Object specifiedValue) {
        ValueExpression expression = filter.getExpression();
        ExpressionPropertyLocator propertyLocator = new ExpressionPropertyLocator(expression, filter.getFilteredComponent());
        OperationType condition = getDefaultCondition();

        PropertyFilterCriterion criterion = new PropertyFilterCriterion(
                propertyLocator,
                condition,
                specifiedValue);
        criterion.setCaseSensitive(filter.isCaseSensitive());
        return criterion;
    }

    protected OperationType getDefaultCondition() {
        return OperationType.CONTAINS;
    }

}
