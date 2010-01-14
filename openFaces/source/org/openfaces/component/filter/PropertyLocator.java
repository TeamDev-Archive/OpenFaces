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

import org.openfaces.component.FilterableComponent;
import org.openfaces.util.ReflectionUtil;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public class PropertyLocator implements Serializable {
    protected Object expression;
    protected FilterableComponent component;

    public PropertyLocator(Object expression) {
        this(expression, null);
    }

    public PropertyLocator(Object expression, FilterableComponent component) {
        if (expression == null)
            throw new IllegalArgumentException("expression can't be null");
        if (expression instanceof ValueExpression) {
            if (component == null)
                throw new IllegalArgumentException("component can't be null when expression is ValueExpression");
        } else if (!(expression instanceof String))
            throw new IllegalArgumentException("expression can be either ValueExpression or String, but it is: " + expression.getClass().getName());
        this.expression = expression;
        this.component = component;
    }

    public Object getPropertyValue(Object obj) {
        if (component != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            return component.getFilteredValueByData(context, obj, expression);
        } else {
            return ReflectionUtil.readProperty(obj, (String) expression);
        }

    }

    public Object getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyLocator that = (PropertyLocator) o;

        if (expression != null ? !expressionToComparableString(expression).equals(expressionToComparableString(that.expression)) : that.expression != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = expression != null ? expressionToComparableString(expression).hashCode() : 0;
        return result;
    }

    private String expressionToComparableString(Object expression) {
        // address the case when com.sun.facelets.el.TagValueExpression instance reports inequality with itself
        if (expression instanceof ValueExpression)
            return ((ValueExpression) expression).getExpressionString();
        return expression.toString();
    }
}
