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
package org.openfaces.testapp.datatable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class DataTableRequestBean {

    private List<TestTableItem> a4JTestCollection = new ArrayList<TestTableItem>();
    private String firstColumnHeader;
    private String secondColumnHeader;
    private String firstColumnFooter;
    private String secondColumnFooter;


    public DataTableRequestBean() {
        Random rand = new Random();
        firstColumnHeader = "First column header" + String.valueOf(rand.nextInt(37000));
        secondColumnHeader = "Second column header" + String.valueOf(rand.nextInt(37000));
        firstColumnFooter = "First column footer" + String.valueOf(rand.nextInt(37000));
        secondColumnFooter = "Second column footer" + String.valueOf(rand.nextInt(37000));

        a4JTestCollection.add(new TestTableItem("col1_row1" + String.valueOf(rand.nextInt(37000)),
                "col2_row1" + String.valueOf(rand.nextInt(37000)),
                "col3_row1" + String.valueOf(rand.nextInt(37000)),
                "col4_row1" + String.valueOf(rand.nextInt(37000))));
        a4JTestCollection.add(new TestTableItem("col1_row2" + String.valueOf(rand.nextInt(37000)),
                "col2_row2" + String.valueOf(rand.nextInt(37000)),
                "col3_row2" + String.valueOf(rand.nextInt(37000)),
                "col4_row2" + String.valueOf(rand.nextInt(37000))));
        a4JTestCollection.add(new TestTableItem("col1_row3" + String.valueOf(rand.nextInt(37000)),
                "col2_row3" + String.valueOf(rand.nextInt(37000)),
                "col3_row3" + String.valueOf(rand.nextInt(37000)),
                "col4_row3" + String.valueOf(rand.nextInt(37000))));
        a4JTestCollection.add(new TestTableItem("col1_row4" + String.valueOf(rand.nextInt(37000)),
                "col2_row4" + String.valueOf(rand.nextInt(37000)),
                "col3_row4" + String.valueOf(rand.nextInt(37000)),
                "col4_row4" + String.valueOf(rand.nextInt(37000))));
        a4JTestCollection.add(new TestTableItem("col1_row5" + String.valueOf(rand.nextInt(37000)),
                "col2_row5" + String.valueOf(rand.nextInt(37000)),
                "col3_row5" + String.valueOf(rand.nextInt(37000)),
                "col4_row5" + String.valueOf(rand.nextInt(37000))));
        a4JTestCollection.add(new TestTableItem("col1_row6" + String.valueOf(rand.nextInt(37000)),
                "col2_row6" + String.valueOf(rand.nextInt(37000)),
                "col3_row6" + String.valueOf(rand.nextInt(37000)),
                "col4_row6" + String.valueOf(rand.nextInt(37000))));
        a4JTestCollection.add(new TestTableItem("col1_row7" + String.valueOf(rand.nextInt(37000)),
                "col2_row7" + String.valueOf(rand.nextInt(37000)),
                "col3_row7" + String.valueOf(rand.nextInt(37000)),
                "col4_row7" + String.valueOf(rand.nextInt(37000))));
        a4JTestCollection.add(new TestTableItem("col1_row8" + String.valueOf(rand.nextInt(37000)),
                "col2_row8" + String.valueOf(rand.nextInt(37000)),
                "col3_row8" + String.valueOf(rand.nextInt(37000)),
                "col4_row8" + String.valueOf(rand.nextInt(37000))));
        a4JTestCollection.add(new TestTableItem("col1_row9" + String.valueOf(rand.nextInt(37000)),
                "col2_row9" + String.valueOf(rand.nextInt(37000)),
                "col3_row9" + String.valueOf(rand.nextInt(37000)),
                "col4_row9" + String.valueOf(rand.nextInt(37000))));
    }

    public List<TestTableItem> getA4JTestCollection() {
        return a4JTestCollection;
    }

    public void setA4JTestCollection(List<TestTableItem> a4JTestCollection) {
        this.a4JTestCollection = a4JTestCollection;
    }

    public String getSecondColumnHeader() {
        return secondColumnHeader;
    }

    public void setSecondColumnHeader(String secondColumnHeader) {
        this.secondColumnHeader = secondColumnHeader;
    }

    public String getFirstColumnFooter() {
        return firstColumnFooter;
    }

    public void setFirstColumnFooter(String firstColumnFooter) {
        this.firstColumnFooter = firstColumnFooter;
    }

    public String getSecondColumnFooter() {
        return secondColumnFooter;
    }

    public void setSecondColumnFooter(String secondColumnFooter) {
        this.secondColumnFooter = secondColumnFooter;
    }

    public void setFirstColumnHeader(String firstColumnHeader) {
        this.firstColumnHeader = firstColumnHeader;
    }

    public String getFirstColumnHeader() {
        return firstColumnHeader;
    }
}