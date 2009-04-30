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

package org.openfaces.testapp.support.QKS2294;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class QKS2294 {

    private List<TableItem> tableData = new ArrayList<TableItem>();

    public QKS2294() {
        for (int i = 0; i < 50; i++) {
            tableData.add(new TableItem(String.valueOf(i), "value" + i, "value" + i, "value" + i, "value" + i, "value" + i));
        }
    }

    public List<TableItem> getTableData() {
        return tableData;
    }

    public void setTableData(List<TableItem> tableData) {
        this.tableData = tableData;
    }
}
