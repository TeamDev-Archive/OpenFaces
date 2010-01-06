/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.support.QKS547;

import java.util.ArrayList;
import java.util.List;


public class QKS547 {
    public class TestData {
        private int id;
        private String text;


        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public TestData(int i, String t) {
            id = i;
            text = t;
        }
    }

    private List<TestData> data;

    public List<TestData> getData() {
        return data;
    }

    public QKS547() {
        data = new ArrayList<TestData>();
        data.add(new TestData(398, "foo"));
        data.add(new TestData(209, "barã"));
        data.add(new TestData(45, "barã"));
        data.add(new TestData(590, "ö óôò õ"));
        data.add(new TestData(274, "borã"));
        data.add(new TestData(67, "börã"));
        data.add(new TestData(568, "böra"));
        data.add(new TestData(965, "fóô"));


    }
}
