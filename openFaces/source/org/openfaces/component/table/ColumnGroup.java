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
package org.openfaces.component.table;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnGroup extends BaseColumn {
    public static final String COMPONENT_TYPE = "org.openfaces.ColumnGroup";
    public static final String COMPONENT_FAMILY = "org.openfaces.ColumnGroup";

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public ValueExpression getColumnValueExpression() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression getColumnSortingExpression() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueExpression getColumnGroupingExpression() {
        throw new UnsupportedOperationException();
    }


}
