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

import javax.el.ELContext;
import javax.el.ValueExpression;

/**
* @author Dmitry Pikhulya
*/
class AnyColumnValueExpression extends ValueExpression {
    @Override
    public Object getValue(ELContext elContext) {
        return true;
    }

    @Override
    public void setValue(ELContext elContext, Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnly(ELContext elContext) {
        return false;
    }

    @Override
    public Class getType(ELContext elContext) {
        return Boolean.class;
    }

    @Override
    public Class getExpectedType() {
        return Boolean.class;
    }

    @Override
    public String getExpressionString() {
        return "<any column>";
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean isLiteralText() {
        return false;
    }
}
