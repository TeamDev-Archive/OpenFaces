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

/**
 * @author Dmitry Pikhulya
 */
public abstract class PropertyLocator implements Serializable {

    private static final PropertyLocatorFactory DEFAULT_FACTORY = new SimplePropertyLocatorFactory();

    public static PropertyLocator getDefaultInstance(Object expression) {
        return DEFAULT_FACTORY.create(expression);
    }

    protected Object expression;

    protected PropertyLocator(Object expression) {
        this.expression = expression;
    }

    public Object getExpression() {
        return expression;
    }

    public abstract Object getPropertyValue(Object obj);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertyLocator that = (PropertyLocator) o;

        if (expression != null ? !expression.equals(expression) : that.expression != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = expression != null ? expression.hashCode() : 0;
        return result;
    }

}
