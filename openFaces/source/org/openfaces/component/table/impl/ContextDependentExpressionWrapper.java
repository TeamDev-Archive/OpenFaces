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
package org.openfaces.component.table.impl;

import org.openfaces.component.ContextDependentComponent;

import javax.el.ELContext;
import javax.el.ValueExpression;
import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public class ContextDependentExpressionWrapper extends ValueExpression implements Serializable {
    protected transient ContextDependentComponent component;
    protected transient ValueExpression expression;

    protected ContextDependentExpressionWrapper() {
    }

    protected ContextDependentExpressionWrapper(ContextDependentComponent component, ValueExpression expression) {
        this.component = component;
        this.expression = expression;
    }

    public Object getValue(ELContext elContext) {
        Runnable exitContext = component.enterComponentContext();
        Object result = expression.getValue(elContext);
        if (exitContext != null) exitContext.run();
        return result;
    }

    public void setValue(ELContext elContext, Object value) {
        Runnable exitContext = component.enterComponentContext();
        expression.setValue(elContext, value);
        if (exitContext != null) exitContext.run();
    }

    public boolean isReadOnly(ELContext elContext) {
        return true;
    }

    public Class getType(ELContext elContext) {
        return Object.class;
    }

    public Class getExpectedType() {
        return Object.class;
    }

    public String getExpressionString() {
        return null;
    }

    public boolean equals(Object o) {
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public boolean isLiteralText() {
        return false;
    }
}
