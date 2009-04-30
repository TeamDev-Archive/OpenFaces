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

package org.openfaces.testapp.support.QKS1140;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class QKS1140 {

    private List<QKS1140TableItem> records = new ArrayList<QKS1140TableItem>();


    public QKS1140() {
        for (int i = 0; i < 4000; i++) {
            records.add(new QKS1140TableItem("column 1 value" + i, "column 2 value" + i, "column 3 value" + i, "column 4 value" + i, "column 5 value" + i, "column 6 value" + i, "column 7 value" + i, "column 8 value" + i, "column 9 value" + i, "column 10 value" + i, "column 11 value" + i, "column 12 value" + i, "column 13 value" + i, "column 14 value" + i, "column 15 value" + i, "column 16 value" + i, "column 17 value" + i, "column 18 value" + i, "column 19 value" + i, "column 20 value" + i, "column 21 value" + i, "column 22 value" + i, "column 23 value" + i, "column 24 value" + i, "column 25 value" + i));
        }
    }

    public List<QKS1140TableItem> getRecords() {
        return records;
    }

    public void setRecords(List<QKS1140TableItem> records) {
        this.records = records;
    }
}
