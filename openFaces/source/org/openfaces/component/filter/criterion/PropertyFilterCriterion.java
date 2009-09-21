package org.openfaces.component.filter.criterion;

import org.openfaces.component.filter.OperationType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Natalia Zolochevska
 */
public class PropertyFilterCriterion implements Serializable {
    private String property;
    private OperationType operation;
    private List<Object> parameters;    
    private boolean inverse;

    public PropertyFilterCriterion() {
    }

    public PropertyFilterCriterion(PropertyFilterCriterion criterion) {
        this.property = criterion.property;
        this.operation = criterion.operation;
        this.inverse = criterion.inverse;
        this.parameters = new ArrayList<Object>(criterion.parameters);
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public void setParameter(Object parameter) {
        parameters = new ArrayList<Object>(1);
        parameters.add(parameter);
    }

    public Object getParameter() {
        return getParameter(0);
    }

    public Object getParameter(int index) {
        if (parameters == null || parameters.size() <= index) {
            return null;
        }
        Object result = parameters.get(index);
        return result;
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
        if (property != null ? !property.equals(criterion.property) : criterion.property != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = property != null ? property.hashCode() : 0;
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (inverse ? 1 : 0);
        return result;
    }

}
