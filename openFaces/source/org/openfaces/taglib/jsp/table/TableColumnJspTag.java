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
package org.openfaces.taglib.jsp.table;

import org.openfaces.taglib.internal.table.AbstractColumnTag;
import org.openfaces.taglib.internal.table.TableColumnTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class TableColumnJspTag extends AbstractColumnJspTag {

    public TableColumnJspTag(AbstractColumnTag delegate) {
        super(delegate);
    }

    public TableColumnJspTag() {
        super(new TableColumnTag());
    }

    public void setSortingExpression(ValueExpression sortingExpression) {
        getDelegate().setPropertyValue("sortingExpression", sortingExpression);
    }

    public void setFilterExpression(ValueExpression filterExpression) {
        getDelegate().setPropertyValue("filterExpression", filterExpression);
    }

    public void setFilterKind(ValueExpression filterKind) {
        getDelegate().setPropertyValue("filterKind", filterKind);
    }

    public void setFilterValues(ValueExpression filterValues) {
        getDelegate().setPropertyValue("filterValues", filterValues);
    }

    public void setFilterValue(ValueExpression filterValue) {
        getDelegate().setPropertyValue("filterValue", filterValue);
    }

    public void setSortingComparator(ValueExpression sortingComparator) {
        getDelegate().setPropertyValue("sortingComparator", sortingComparator);
    }

    public void setFilterPromptText(ValueExpression promptText) {
        getDelegate().setPropertyValue("filterPromptText", promptText);
    }

    public void setFilterPromptTextStyle(ValueExpression promptTextStyle) {
        getDelegate().setPropertyValue("filterPromptTextStyle", promptTextStyle);
    }

    public void setFilterPromptTextClass(ValueExpression promptTextClass) {
        getDelegate().setPropertyValue("filterPromptTextClass", promptTextClass);
    }

}
