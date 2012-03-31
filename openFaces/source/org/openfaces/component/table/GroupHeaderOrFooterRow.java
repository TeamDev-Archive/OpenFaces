/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table;

import org.openfaces.util.Components;

/**
 * @author Dmitry Pikhulya
 */
public abstract class GroupHeaderOrFooterRow extends Row {
    private DataTable dataTable;

    public GroupHeaderOrFooterRow() {
    }

    public GroupHeaderOrFooterRow(DataTable dataTable) {
        if (dataTable == null) throw new IllegalArgumentException("dataTable shouldn't be null");
        this.dataTable = dataTable;
    }

    private DataTable getDataTable() {
        if (dataTable == null) {
            dataTable = (DataTable) Components.checkParentTag(this, DataTable.class);
        }
        return dataTable;
    }

    @Override
    public boolean getCondition() {
        Object currentRowData = getDataTable().getRowData();
        return currentRowData != null && getExpectedRowDataClass().isAssignableFrom(currentRowData.getClass());
    }

    protected abstract Class<? extends RowGroupHeaderOrFooter> getExpectedRowDataClass();
}
