package org.openfaces.component.filter.criterion;

import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.FilterCriterionProcessor;
import org.openfaces.component.filter.FilterCondition;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Natalia Zolochevska
 */
public class PropertyFilterCriterion extends FilterCriterion {
    private static final String PARAM_ARG1 = "arg1";
    private static final String PARAM_ARG2 = "arg2";
    private static final String PARAM_CASE_SENSITIVE = "caseSensitive";

    private PropertyLocator propertyLocator;
    private FilterCondition condition;
    private Map<String, Object> parameters = new HashMap<String, Object>(2);
    private boolean inverse;

    public PropertyFilterCriterion() {
    }

    public PropertyFilterCriterion(PropertyLocator propertyLocator, FilterCondition condition, Object arg1) {
        this.propertyLocator = propertyLocator;
        this.condition = condition;
        setArg1(arg1);
    }

    public PropertyFilterCriterion(PropertyLocator propertyLocator, FilterCondition condition,
                                   Map<String, Object> parameters, boolean inverse) {
        this.propertyLocator = propertyLocator;
        this.condition = condition;
        if (parameters != null)
            this.parameters = parameters;
        this.inverse = inverse;
    }

    public Object process(FilterCriterionProcessor processor) {
        return processor.process(this);
    }

    public boolean acceptsAll() {
        return isParamEmpty(getArg1()) && isParamEmpty(getArg2());
    }

    private boolean isParamEmpty(Object parameter) {
        return parameter == null || parameter.equals("");
    }

    public PropertyFilterCriterion(PropertyFilterCriterion criterion) {
        this.propertyLocator = criterion.propertyLocator;
        this.condition = criterion.condition;
        this.inverse = criterion.inverse;
        this.parameters = new HashMap<String, Object>(criterion.parameters);
    }

    public PropertyLocator getPropertyLocator() {
        return propertyLocator;
    }

    public void setPropertyLocator(PropertyLocator propertyLocator) {
        this.propertyLocator = propertyLocator;
    }

    public String getExpressionStr() {
        return propertyLocator.getExpression().toString();
    }

    public FilterCondition getCondition() {
        return condition;
    }

    public void setCondition(FilterCondition condition) {
        this.condition = condition;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setArg1(Object arg1) {
        parameters.put(PARAM_ARG1, arg1);
    }

    public Object getArg1() {
        return parameters.get(PARAM_ARG1);
    }

    public void setArg2(Object arg2) {
        parameters.put(PARAM_ARG2, arg2);
    }

    public Object getArg2() {
        return parameters.get(PARAM_ARG2);
    }

    public boolean isCaseSensitive() {
        Object paramValue = parameters.get(PARAM_CASE_SENSITIVE);
        return paramValue != null && (Boolean) paramValue;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        parameters.put(PARAM_CASE_SENSITIVE, caseSensitive);
    }

    public boolean isInverse() {
        return inverse;
    }

    public void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyFilterCriterion criterion = (PropertyFilterCriterion) o;

        if (inverse != criterion.inverse) return false;
        if (condition != criterion.condition) return false;
        if (parameters != null ? !parameters.equals(criterion.parameters) : criterion.parameters != null) return false;
        if (propertyLocator != null ? !propertyLocator.equals(criterion.propertyLocator) : criterion.propertyLocator != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = propertyLocator != null ? propertyLocator.hashCode() : 0;
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (inverse ? 1 : 0);
        return result;
    }

}
