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

import java.util.Collections;
import java.util.List;

/**
 * An instance of TableData represents a snapshot of data displayed in a DataTable component, including row data objects,
 * the list of columns, and displayed cell texts. Once created, an instance of TableData class is not bound to the DataTable
 * component for which it was originally created.
 *
 * @author Dmitry Pikhulya
 */
public class TableData {
    private List<String> columnDatas;
    private List<TableRowData> rowDatas;

    public TableData(List<String> columnDatas, List<TableRowData> rowDatas) {
        this.columnDatas = Collections.unmodifiableList(columnDatas);
        this.rowDatas = Collections.unmodifiableList(rowDatas);
    }

    public List<String> getTableColumnDatas() {
        return columnDatas;
    }

    public List<TableRowData> getTableRowDatas() {
        return rowDatas;
    }
}
