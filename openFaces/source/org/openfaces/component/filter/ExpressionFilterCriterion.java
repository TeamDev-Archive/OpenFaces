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

package org.openfaces.component.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Natalia Zolochevska
 */
public class ExpressionFilterCriterion extends FilterCriterion {
    private static final String PARAM_ARG1 = "arg1";
    private static final String PARAM_ARG2 = "arg2";
    private static final String PARAM_CASE_SENSITIVE = "caseSensitive";

    private PropertyLocator propertyLocator;
    private FilterCondition condition;
    private Map<String, Object> parameters = new HashMap<String, Object>(2);
    private boolean inverse;

    public ExpressionFilterCriterion() {
    }

    public ExpressionFilterCriterion(Object arg1) {
        setArg1(arg1);
    }

    public ExpressionFilterCriterion(PropertyLocator propertyLocator, FilterCondition condition, Object arg1) {
        this.propertyLocator = propertyLocator;
        this.condition = condition;
        setArg1(arg1);
    }

    public ExpressionFilterCriterion(FilterCondition condition, boolean inverse) {
        this.condition = condition;
        this.inverse = inverse;
    }

    public ExpressionFilterCriterion(ExpressionFilterCriterion criterion) {
        this.propertyLocator = criterion.propertyLocator;
        this.condition = criterion.condition;
        this.inverse = criterion.inverse;
        this.parameters = new HashMap<String, Object>(criterion.parameters);
    }

    public Object process(FilterCriterionProcessor processor) {
        return processor.process(this);
    }

    public boolean acceptsAll() {
        if (condition == null)
            return true;
        return condition.process(new FilterConditionProcessor<Boolean>() {
            @Override
            public Boolean processEmpty() {
                return false;
            }

            private boolean isParamEmpty(Object parameter) {
                return parameter == null || parameter.equals("");
            }

            private boolean isParam1Empty() {
                return isParamEmpty(getArg1());
            }

            @Override
            public Boolean processEquals() {
                return isParam1Empty();
            }

            @Override
            public Boolean processContains() {
                return isParam1Empty();
            }

            @Override
            public Boolean processBegins() {
                return isParam1Empty();
            }

            @Override
            public Boolean processEnds() {
                return isParam1Empty();
            }

            @Override
            public Boolean processLess() {
                return isParam1Empty();
            }

            @Override
            public Boolean processGreater() {
                return isParam1Empty();
            }

            @Override
            public Boolean processLessOrEqual() {
                return isParam1Empty();
            }

            @Override
            public Boolean processGreaterOrEqual() {
                return isParam1Empty();
            }

            @Override
            public Boolean processBetween() {
                return isParamEmpty(getArg1()) && isParamEmpty(getArg2());
            }
        });
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

        ExpressionFilterCriterion criterion = (ExpressionFilterCriterion) o;

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
