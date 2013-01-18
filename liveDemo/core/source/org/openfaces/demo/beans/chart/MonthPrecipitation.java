/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.chart;

import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PlainSeries;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.Tuple;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MonthPrecipitation implements ChartModel, Externalizable, Serializable {

    private static final String JANUARY = "January";
    private static final String APRIL = "April";
    private static final String JULY = "July";
    private static final String OCTOBER = "October";
    private static final Comparator<Tuple> COMPARATOR = new Comparator<Tuple>() {
        public int compare(Tuple t1, Tuple t2) {
            int num1 = convertMonthToInteger((String) t1.getKey());
            int num2 = convertMonthToInteger((String) t2.getKey());
            if (num1 == num2)
                return 0;
            return num1 < num2 ? -1 : 1;
        }

        private int convertMonthToInteger(String month) {
            if (JANUARY.equals(month))
                return 0;
            if (APRIL.equals(month))
                return 1;
            if (JULY.equals(month))
                return 2;
            if (OCTOBER.equals(month))
                return 3;
            throw new RuntimeException("Illegal month value: " + month);
        }
    };

    private Series[] series;

    public void makeData() {
        series = new Series[2];
        PlainSeries s0 = new PlainSeries("2007");
        PlainSeries s1 = new PlainSeries("2008");
        Map<String, Integer> data0 = new HashMap<String, Integer>();
        Map<String, Integer> data1 = new HashMap<String, Integer>();

        Random random = new Random();
        data0.put(JANUARY, random.nextInt(50));
        data0.put(APRIL, random.nextInt(50));
        data0.put(JULY, random.nextInt(50));
        data0.put(OCTOBER, random.nextInt(50));

        data1.put(JANUARY, random.nextInt(50));
        data1.put(APRIL, random.nextInt(50));
        data1.put(JULY, random.nextInt(50));
        data1.put(OCTOBER, random.nextInt(50));

        s0.setComparator(COMPARATOR);
        s0.setData(data0);

        s1.setComparator(COMPARATOR);
        s1.setData(data1);

        series[0] = s0;
        series[1] = s1;


    }

    public Series[] getSeries() {
        if (series == null) makeData();
        return series;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(series);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        series = (Series[]) in.readObject();
    }
}