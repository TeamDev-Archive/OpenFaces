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

package org.openfaces.component.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An expression-based filter criterion that compares a particular property (or expression) of a filtered object with
 * the specified parameters based on the specified condition. It consists of an expression (see the
 * <code>getPropertyLocator()</code> method, or a quick expression accessor <code>getExpressionStr()</code>), a condition
 * (see the <code>getCondition()</code> method), and a list of parameters (see the <code>getParameters()</code> method,
 * or convenience parameter access methods <code>getArg1()</code>, <code>getArg2()</code>, and <code>isCaseSensitive</code>).
 * There's an additional <code>inverse</code> property that works in conjunction with property expression and inverts
 * the result of comparison if set to <code>true</code>.
 *
 *
 * @author Natalia Zolochevska
 */
public class ExpressionFilterCriterion extends FilterCriterion implements Serializable{
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

    public ExpressionFilterCriterion(PropertyLocator propertyLocator, FilterCondition condition, Map<String, Object> parameters, boolean inverse) {
        this.propertyLocator = propertyLocator;
        this.condition = condition;
        this.parameters = parameters;
        this.inverse = inverse;
    }

    public ExpressionFilterCriterion(PropertyLocator propertyLocator, FilterCondition condition, Object arg1) {
        this.propertyLocator = propertyLocator;
        this.condition = condition;
        setArg1(arg1);
    }

    public ExpressionFilterCriterion(FilterCondition condition) {
        this(condition, false);
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

    /**
     * Returns the expression string for this criterion. This is essentially the expression that is specified for the
     * filter component from which this criterion originates.
     * @return the expression string for this criterion
     */
    public String getExpressionStr() {
        return propertyLocator.getExpression().toString();
    }

    /**
     * Returns the filtering condition for this criterion. Note that the filtering condition affects the set and number
     * of arguments that the filter criterion possesses. E.g. FilterCriterion.EQUALS and FilterCriterion.GREATER require
     * just one argument, FilterCriterion.BETWEEN requires two arguments, and FilterCriterion.EMPTY doesn't require
     * arguments at all.
     * @return the filter condition for this criterion
     */
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

    /**
     * Returns the first argument of this criterion. This method is applicable when the criterion has a condition that
     * requires one or more arguments (e.g. FilterCondition.EQUALS or FilterCondition.BETWEEN).
     * @return the first argument of this criterion
     */
    public Object getArg1() {
        return parameters.get(PARAM_ARG1);
    }

    public void setArg2(Object arg2) {
        parameters.put(PARAM_ARG2, arg2);
    }

    /**
     * Returns the second argument of this criterion. This method is applicable when the criterion has a condition that
     * requires two or more arguments (there's presently just one such condition -- FilterCondition.BETWEEN).
     * @return the second argument of this criterion
     */
    public Object getArg2() {
        return parameters.get(PARAM_ARG2);
    }

    /**
     * Identifies whether text comparison should be performed in a case-sensitive way. This parameter is applicable only
     * when this criterion contains a condition that operates with string values (e.g. FilterCondition.CONTAINS,
     * FilterCondition.BEGINS_WITH, or FilterCondition.EQUALS)
     *
     * @return true if text comparison should be performed in a case-sensitive way
     */
    public boolean isCaseSensitive() {
        Object paramValue = parameters.get(PARAM_CASE_SENSITIVE);
        return paramValue != null && (Boolean) paramValue;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        parameters.put(PARAM_CASE_SENSITIVE, caseSensitive);
    }

    /**
     * Specifies whether the condition specified with the "condition" property should be treated in the
     * opposite way. For example when condition is FilterCondition.EQUALS and "inverse" property is set to true,
     * the expected filtering behavior is matching the values that are _not_ equal to the filter criterion's argument.
     * @return true if filter's condition should be treated in the opposite way.
     */
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
