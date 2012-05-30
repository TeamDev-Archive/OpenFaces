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
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.BaseColumnTag;
import org.openfaces.taglib.internal.table.ColumnTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnJspTag extends BaseColumnJspTag {

    public ColumnJspTag(BaseColumnTag delegate) {
        super(delegate);
    }

    public ColumnJspTag() {
        super(new ColumnTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setSortingExpression(ValueExpression sortingExpression) {
        getDelegate().setPropertyValue("sortingExpression", sortingExpression);
    }

    public void setGroupingExpression(ValueExpression groupingExpression) {
        getDelegate().setPropertyValue("groupingExpression", groupingExpression);
    }

    public void setSortingComparator(ValueExpression sortingComparator) {
        getDelegate().setPropertyValue("sortingComparator", sortingComparator);
    }


    public void setConverter(ValueExpression converter) {
        getDelegate().setPropertyValue("converter", converter);
    }

    public void setGroupingValueConverter(ValueExpression groupingValueConverter) {
        getDelegate().setPropertyValue("groupingValueConverter", groupingValueConverter);
    }

    public void setType(ValueExpression type) {
        getDelegate().setPropertyValue("type", type);
    }
}
