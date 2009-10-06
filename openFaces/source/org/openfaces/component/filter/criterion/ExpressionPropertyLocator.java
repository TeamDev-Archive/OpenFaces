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
package org.openfaces.component.filter.criterion;

import org.openfaces.component.FilterableComponent;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ExpressionPropertyLocator extends PropertyLocator {
    private ValueExpression expression;
    private FilterableComponent component;

    public ExpressionPropertyLocator(ValueExpression expression, FilterableComponent component) {
        if (expression == null || component == null)
            throw new IllegalArgumentException();
        this.expression = expression;
        this.component = component;
    }

    public Object getPropertyValue(Object obj) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String,Object> requestMap = context.getExternalContext().getRequestMap();
        return component.getFilteredValueByData(context, requestMap, expression, component.getVar(), obj);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpressionPropertyLocator that = (ExpressionPropertyLocator) o;

        if (expression != null ? !expression.equals(that.expression) : that.expression != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = expression != null ? expression.hashCode() : 0;
        result = 31 * result + (component != null ? component.hashCode() : 0);
        return result;
    }
}
