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

package org.openfaces.testapp.support.QKS145;

import org.openfaces.component.table.DataTable;
import org.openfaces.testapp.datatable.TestTableItem;

import javax.faces.component.html.HtmlDataTable;
import java.util.ArrayList;
import java.util.List;


public class QKS145 {
    public List<DataTableItem> dataTableItems = new ArrayList<DataTableItem>();
    public HtmlDataTable dataTable;
    private List selectedRows = new ArrayList();
    private DataTable testTable = new DataTable();
    private List<TestTableItem> tableTestCollection = new ArrayList<TestTableItem>();


    public List getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(List selectedRows) {
        this.selectedRows = selectedRows;
    }


    public QKS145() {
        for (int i = 0; i < 5; i++) dataTableItems.add(new DataTableItem(i, "item" + i));
        tableTestCollection.add(new TestTableItem("col1_row1", "col2_row1", "col3_row1", "col4_row1"));
        tableTestCollection.add(new TestTableItem("col1_row2", "col2_row2", "col3_row2", "col4_row2"));
        tableTestCollection.add(new TestTableItem("col1_row3", "col2_row3", "col3_row3", "col4_row3"));
        tableTestCollection.add(new TestTableItem("col1_row4", "col2_row4", "col3_row4", "col4_row4"));
        tableTestCollection.add(new TestTableItem("col1_row5", "col2_row5", "col3_row5", "col4_row5"));
        tableTestCollection.add(new TestTableItem("col1_row6", "col2_row6", "col3_row6", "col4_row6"));
        tableTestCollection.add(new TestTableItem("col1_row7", "col2_row7", "col3_row7", "col4_row7"));
        tableTestCollection.add(new TestTableItem("col1_row8", "col2_row8", "col3_row8", "col4_row8"));
        tableTestCollection.add(new TestTableItem("col1_row9", "col2_row9", "col3_row9", "col4_row9"));


    }

    public List<TestTableItem> getTableTestCollection() {
        return tableTestCollection;
    }

    public List<DataTableItem> getDataTableItems() {
        return dataTableItems;
    }

    public HtmlDataTable getDataTable() {
        return dataTable;
    }


    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }

    public int getCurrentRowIndex() {
        return getDataTable().getRowIndex();
    }

    public DataTable getTestTable() {
        return testTable;
    }

    public void setTestTable(DataTable testTable) {
        this.testTable = testTable;
    }

    public int getCurrentRowIndex1() {
        return getTestTable().getRowIndex();
    }
}
