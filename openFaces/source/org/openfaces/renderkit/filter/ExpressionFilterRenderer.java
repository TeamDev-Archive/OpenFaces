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
package org.openfaces.renderkit.filter;

import org.openfaces.component.FilterableComponent;
import org.openfaces.component.filter.ExpressionFilter;
import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.component.filter.FilterCondition;
import org.openfaces.component.filter.PropertyLocator;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Components;
import org.openfaces.util.RawScript;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StringConverter;
import org.openfaces.util.Styles;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * @author Dmitry Pikhulya
 */
public abstract class ExpressionFilterRenderer extends RendererBase {
    protected static final String DEFAULT_PREDEFINED_CRITERION_CLASS = "o_table_filter_predefined_criterion";

    protected String getFilterSubmissionScript(ExpressionFilter filter) {
        UIComponent component = (UIComponent) filter.getFilteredComponent();
        ExpressionFilter submittedFilter = Components.isChildComponent(filter, component) ? null : filter;
        return new ScriptBuilder().functionCall("O$.Filters._filterComponent", component, submittedFilter, new RawScript("this")).
                semicolon().toString();
    }

    protected String getPredefinedCriterionClass(FacesContext context, ExpressionFilter filter) {
        String predefinedCriterionStyle = filter.getPredefinedCriterionStyle();
        return Styles.getCSSClass(context, filter, predefinedCriterionStyle, DEFAULT_PREDEFINED_CRITERION_CLASS, filter.getPredefinedCriterionClass());
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
        Object argAsObject = null;
        try {
            argAsObject = converter.getAsObject(FacesContext.getCurrentInstance(), filter, searchString);
        } catch (ConverterException e) {
            FacesMessage facesMessage = e.getFacesMessage();
            FacesContext context = FacesContext.getCurrentInstance();
            if (facesMessage == null)
                facesMessage = new FacesMessage("Conversion error");
            context.addMessage(filter.getClientId(context), facesMessage);
        }
        ExpressionFilterCriterion oldCriterion = (ExpressionFilterCriterion) filter.getValue();
        ExpressionFilterCriterion newCriterion;
        if (oldCriterion != null &&
                !oldCriterion.getCondition().equals(FilterCondition.EMPTY) &&
                !oldCriterion.getCondition().equals(FilterCondition.CONTAINS)) {
            newCriterion = new ExpressionFilterCriterion(oldCriterion);
            newCriterion.setArg1(argAsObject);
        } else
            newCriterion = createDefaultCriterion(filter, argAsObject);
        setDecodedCriterion(filter, newCriterion);
    }

    protected Converter getConverter(ExpressionFilter filter) {
        Converter converter = filter.getConverter();
        return converter != null ? converter : new StringConverter();
    }

    protected FilterCondition getForceDefaultCondition(ExpressionFilter filter) {
        return null;
    }

    protected ExpressionFilterCriterion createDefaultCriterion(ExpressionFilter filter, Object specifiedValue) {
        Object expression = filter.getExpression();
        PropertyLocator propertyLocator = new PropertyLocator(expression, filter.getFilteredComponent());
        FilterCondition condition;

        boolean inverse = false;
        ExpressionFilterCriterion defaultCriterion = (ExpressionFilterCriterion) filter.getAttributes().get("condition");
        if (defaultCriterion != null) {
            condition = defaultCriterion.getCondition();
            inverse = defaultCriterion.isInverse();
        } else {
            FilterCondition forcedCondition = getForceDefaultCondition(filter);
            if (forcedCondition != null)
                condition = forcedCondition;
            else {
                UIComponent parent = filter.getParent();
                if (parent == null || !(parent instanceof BaseColumn))
                    condition = FilterCondition.CONTAINS;
                else {
                    BaseColumn column = (BaseColumn) parent;
                    if (expression instanceof ValueExpression) {
                        TableUtil.ColumnExpressionData data = TableUtil.getColumnExpressionData(column, (ValueExpression) expression);
                        Class type = data.getValueType();
                        if (String.class.equals(type))
                            condition = FilterCondition.CONTAINS;
                        else
                            condition = FilterCondition.EQUALS;
                    } else {
                        // it is allowed to specify expression as a string, e.g. in case of custom data providing,
                        // in which case the actual filtering is performed by the application code so the condition
                        // specified in the ValueExpression object doesn't matter 
                        condition = FilterCondition.EQUALS;
                    }
                }
            }
        }

        ExpressionFilterCriterion criterion = new ExpressionFilterCriterion(
                propertyLocator,
                condition,
                specifiedValue);
        criterion.setInverse(inverse);
        criterion.setCaseSensitive(filter.isCaseSensitive());
        return criterion;
    }


}
