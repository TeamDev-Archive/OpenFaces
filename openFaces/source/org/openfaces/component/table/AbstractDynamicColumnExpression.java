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
package org.openfaces.component.table;

import javax.el.ELContext;
import javax.el.ValueExpression;
import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractDynamicColumnExpression extends ValueExpression implements Serializable {
    protected transient DynamicColumn column;
    protected transient ValueExpression expressionFromColumnsComponent;

    protected AbstractDynamicColumnExpression() {
    }

    protected AbstractDynamicColumnExpression(DynamicColumn column, ValueExpression expressionFromColumnsComponent) {
        this.column = column;
        this.expressionFromColumnsComponent = expressionFromColumnsComponent;
    }

    @Override
    public abstract Object getValue(ELContext elContext);

    public void setValue(ELContext elContext, Object value) {
        throw new UnsupportedOperationException("This expression is read-only");
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
