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

/**
 * @author andrii.loboda
 */
public class CellId {
    private final Object rowData;
    private final String columnId;

    public CellId(Object rowData, String columnId) {
        this.rowData = rowData;
        this.columnId = columnId;
    }

    public Object getRowData() {
        return rowData;
    }

    public String getColumnId() {
        return columnId;
    }
}
