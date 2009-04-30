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

package org.openfaces.testapp.support.QKS632;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class QKS632 {

    private List<TestItem> tableTestCollection = new ArrayList<TestItem>();
    private List<TestItem> list = new ArrayList<TestItem>();

    public QKS632() {
        Random rand = new Random();
        tableTestCollection.add(new TestItem("col1_row1" + String.valueOf(rand.nextInt(37000)),
                "col2_row1" + String.valueOf(rand.nextInt(37000))));
        tableTestCollection.add(new TestItem("col1_row2" + String.valueOf(rand.nextInt(37000)),
                "col2_row2" + String.valueOf(rand.nextInt(37000))));
        tableTestCollection.add(new TestItem("col1_row3" + String.valueOf(rand.nextInt(37000)),
                "col2_row3" + String.valueOf(rand.nextInt(37000))));
        tableTestCollection.add(new TestItem("col1_row4" + String.valueOf(rand.nextInt(37000)),
                "col2_row4" + String.valueOf(rand.nextInt(37000))));
        tableTestCollection.add(new TestItem("col1_row5" + String.valueOf(rand.nextInt(37000)),
                "col2_row5" + String.valueOf(rand.nextInt(37000))));
        tableTestCollection.add(new TestItem("col1_row6" + String.valueOf(rand.nextInt(37000)),
                "col2_row6" + String.valueOf(rand.nextInt(37000))));
        tableTestCollection.add(new TestItem("col1_row7" + String.valueOf(rand.nextInt(37000)),
                "col2_row7" + String.valueOf(rand.nextInt(37000))));
        tableTestCollection.add(new TestItem("col1_row8" + String.valueOf(rand.nextInt(37000)),
                "col2_row8" + String.valueOf(rand.nextInt(37000))));
        tableTestCollection.add(new TestItem("col1_row9" + String.valueOf(rand.nextInt(37000)),
                "col2_row9" + String.valueOf(rand.nextInt(37000))));
    }

    public List<TestItem> getTableTestCollection() {
        return tableTestCollection;
    }

    public List<TestItem> getList() {
        list.add(tableTestCollection.get(0));
        list.add(tableTestCollection.get(1));
        list.add(tableTestCollection.get(3));
        return list;
    }

    public void setList(List<TestItem> list) {
        this.list = list;
    }

}