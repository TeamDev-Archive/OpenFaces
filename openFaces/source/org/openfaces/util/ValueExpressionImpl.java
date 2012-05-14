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

package org.openfaces.util;

import javax.el.ELContext;
import javax.el.ValueExpression;

public class ValueExpressionImpl extends ValueExpression {

    public Object getValue(ELContext elContext) {
        return null;
    }

    public void setValue(ELContext elContext, Object o) {
    }

    public boolean isReadOnly(ELContext elContext) {
        return false;
    }

    public Class getType(ELContext elContext) {
        return null;
    }

    public Class getExpectedType() {
        return null;
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
