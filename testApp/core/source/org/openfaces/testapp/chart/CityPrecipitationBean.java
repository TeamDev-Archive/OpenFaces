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
package org.openfaces.testapp.chart;

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

public class CityPrecipitationBean implements ChartModel, Externalizable, Serializable {

    private Series[] series;
    private static final String WINTER = "Winter";
    private static final String SPRING = "Spring";
    private static final String SUMMER = "Summer";
    private static final String AUTUMN = "Autumn";

    public CityPrecipitationBean() {
    }

    public void makeData() {

        series = new Series[2];
        PlainSeries series = new PlainSeries("Seasons");
        Map<String, Integer> data = new HashMap<String, Integer>();

        Random random = new Random();
        data.put(WINTER, random.nextInt(100));
        data.put(SPRING, random.nextInt(100));
        data.put(SUMMER, random.nextInt(100));
        data.put(AUTUMN, random.nextInt(100));

        series.setComparator(new TupleComparator());
        series.setData(data);

        this.series[0] = series;
    }


    public Series[] getSeries() {
        if (series == null) {
            makeData();
        }
        return series;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(series);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        series = (Series[]) in.readObject();
    }

    private static class TupleComparator implements Comparator<Tuple> {
        public int compare(Tuple t1, Tuple t2) {
            int num1 = convertMonthToInteger((String) t1.getKey());
            int num2 = convertMonthToInteger((String) t2.getKey());
            if (num1 == num2) {
                return 0;
            }
            return num1 < num2 ? -1 : 1;
        }

        private int convertMonthToInteger(String month) {
            if (WINTER.equals(month))
                return 0;
            if (SPRING.equals(month))
                return 1;
            if (SUMMER.equals(month))
                return 2;
            if (AUTUMN.equals(month))
                return 3;
            throw new RuntimeException("Illegal month value: " + month);
        }
    }
}