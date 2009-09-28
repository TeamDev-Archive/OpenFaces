package org.openfaces.component.filter.criterion;

import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.OperationType;

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
    private OperationType operation;
    private Map<String, Object> parameters = new HashMap<String, Object>(2);
    private boolean inverse;

    public PropertyFilterCriterion() {
    }

    public boolean acceptsAll() {
        for (Object parameter : parameters.values()) {
            if (parameter != null && !parameter.equals(""))
                return false;
        }
        return true;
    }

    public boolean acceptsValue(Object value) {
        return false; // todo: should this method be removed from FilterCriterion?
    }

    public PropertyFilterCriterion(PropertyFilterCriterion criterion) {
        this.propertyLocator = criterion.propertyLocator;
        this.operation = criterion.operation;
        this.inverse = criterion.inverse;
        this.parameters = new HashMap<String, Object>(criterion.parameters);
    }

    public PropertyLocator getPropertyLocator() {
        return propertyLocator;
    }

    public void setPropertyLocator(PropertyLocator propertyLocator) {
        this.propertyLocator = propertyLocator;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setArg1(Object parameter) {
        parameters.put(PARAM_ARG1, parameter);
    }

    public Object getArg1() {
        return parameters.get(PARAM_ARG1);
    }

    public void setArg2(Object parameter) {
        parameters.put(PARAM_ARG2, parameter);
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
        if (operation != criterion.operation) return false;
        if (parameters != null ? !parameters.equals(criterion.parameters) : criterion.parameters != null) return false;
        if (propertyLocator != null ? !propertyLocator.equals(criterion.propertyLocator) : criterion.propertyLocator != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = propertyLocator != null ? propertyLocator.hashCode() : 0;
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (inverse ? 1 : 0);
        return result;
    }

}
