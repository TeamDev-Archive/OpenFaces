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

package org.openfaces.component.table.export;

import org.openfaces.component.filter.ExpressionFilter;
import org.openfaces.component.table.BaseColumn;

import javax.el.ValueExpression;
import javax.faces.component.UIColumn;
import javax.faces.context.FacesContext;

import static org.openfaces.util.Components.findChildWithClass;

/**
 * @author Natalia.Zolochevska@Teamdev.com
 */
class FilterExpressionExtractor implements CellDataExtractor {

    public boolean isApplicableFor(Object rowData, UIColumn column) {
        ExpressionFilter filter = findChildWithClass(column, ExpressionFilter.class);
        return filter != null;
    }

    public Object getData(Object rowData, UIColumn column) {
        ExpressionFilter filter = findChildWithClass(column, ExpressionFilter.class);
        if (filter == null) {
            return null;
        }
        Object expression = filter.getExpression();
        if (expression instanceof ValueExpression) {
            return ((ValueExpression) expression).getValue(FacesContext.getCurrentInstance().getELContext());
        } else {
            return expression;
        }
    }
}
