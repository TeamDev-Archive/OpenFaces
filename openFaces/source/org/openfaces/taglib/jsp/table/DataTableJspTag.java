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

import org.openfaces.taglib.internal.table.DataTableTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */

public class DataTableJspTag extends AbstractTableJspTag {

    public DataTableJspTag() {
        super(new DataTableTag());
    }

    public void setRowKey(ValueExpression rowKey) {
        getDelegate().setPropertyValue("rowKey", rowKey);
    }

    public void setPageSize(ValueExpression pageSize) {
        getDelegate().setPropertyValue("pageSize", pageSize);
    }

    public void setPageIndex(ValueExpression pageIndex) {
        getDelegate().setPropertyValue("pageIndex", pageIndex);
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setRowIndexVar(ValueExpression rowIndexVar) {
        getDelegate().setPropertyValue("rowIndexVar", rowIndexVar);
    }

    public void setPaginationKeyboardSupport(ValueExpression paginationKeyboardSupport) {
        getDelegate().setPropertyValue("paginationKeyboardSupport", paginationKeyboardSupport);
    }

    public void setPaginationOnSorting(ValueExpression paginationOnSorting) {
        getDelegate().setPropertyValue("paginationOnSorting", paginationOnSorting);
    }

    public void setTotalRowCount(ValueExpression totalRowCount) {
        getDelegate().setPropertyValue("totalRowCount", totalRowCount);
    }

    public void setRowDataByKey(ValueExpression rowDataByKey) {
        getDelegate().setPropertyValue("rowDataByKey", rowDataByKey);
    }


    public void setCustomDataProviding(ValueExpression customDataProviding) {
        getDelegate().setPropertyValue("customDataProviding", customDataProviding);
    }
}