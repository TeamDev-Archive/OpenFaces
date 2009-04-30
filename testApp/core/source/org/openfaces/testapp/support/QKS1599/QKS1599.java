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

package org.openfaces.testapp.support.QKS1599;

import org.openfaces.util.FacesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class QKS1599 {
    private List<String> values1 = new ArrayList<String>();
    private List<String> values2 = new ArrayList<String>();
    private String value1 = "";
    private String value2 = "";


    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }


    public QKS1599() {
        values1.add("a");
        values1.add("b");
        values1.add("c");
        values1.add("d");
        values2.add("Acalypha");
        values2.add("Aira");
        values2.add("Artemisia");
        values2.add("Begonia");
        values2.add("Breynia");
        values2.add("Caloscordum");
        values2.add("Celastrus");
        values2.add("Chrysanthemum");
        values2.add("Denmoza");
    }

    public List<String> getItems1() {
        List<String> items = new ArrayList<String>();
        String typedValue = (String) FacesUtil.getRequestMapValue("searchString");
        if (typedValue != null) {
            for (String item : values1) {
                String itemForComparison = item.toLowerCase();
                String typedValueForComparison = typedValue.toLowerCase();
                if (itemForComparison.contains(typedValueForComparison))
                    items.add(item);
            }
        }
        return items;
    }

    public List<String> getItems2() {
        List<String> items = new ArrayList<String>();
        String typedValue = (String) FacesUtil.getRequestMapValue("searchString");
        if (typedValue != null) {
            for (String aMyValues2 : values2) {
                if (aMyValues2.toLowerCase().startsWith(value1.toLowerCase())) {
                    String itemForComparison = aMyValues2.toLowerCase();
                    String typedValueForComparison = typedValue.toLowerCase();
                    if (itemForComparison.contains(typedValueForComparison)) {
                        items.add(aMyValues2);
                    }
                }
            }
        }
        return items;
    }
}
