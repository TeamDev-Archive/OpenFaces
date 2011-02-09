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

package org.openfaces.demo.beans.chart;

import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PlainSeries;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.Tuple;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LineModel implements ChartModel {
    private static final String JANUARY = "January";
    private static final String FEBRUARY = "February";
    private static final String MARCH = "March";
    private static final String APRIL = "April";
    private static final String MAY = "May";
    private static final String JUNE = "June";
    private static final String JULY = "July";
    private static final String AUGUST = "August";
    private static final String SEPTEMBER = "September";
    private static final String OCTOBER = "October";
    private static final String NOVEMBER = "November";
    private static final String DECEMBER = "December";
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
            if (FEBRUARY.equals(month))
                return 1;
            if (MARCH.equals(month))
                return 2;
            if (APRIL.equals(month))
                return 3;
            if (MAY.equals(month))
                return 4;
            if (JUNE.equals(month))
                return 5;
            if (JULY.equals(month))
                return 6;
            if (AUGUST.equals(month))
                return 7;
            if (SEPTEMBER.equals(month))
                return 8;
            if (OCTOBER.equals(month))
                return 9;
            if (NOVEMBER.equals(month))
                return 10;
            if (DECEMBER.equals(month))
                return 11;

            throw new RuntimeException("Illegal month value: " + month);
        }
    };

    private ChartView chartView;

    public LineModel(ChartView chartView) {
        this.chartView = chartView;
    }

    public Series[] getSeries() {
        PlainSeries s0 = new PlainSeries("Product 1");
        Map<String, Integer> data0 = new HashMap<String, Integer>();
        PlainSeries s1 = new PlainSeries("Product 2");
        Map<String, Integer> data1 = new HashMap<String, Integer>();

        for (int i = 0; i < chartView.getQuarterIncome().size(); i++) {
            MonthIncome income = chartView.getQuarterIncome().get(i);

            data0.put(income.getMonth(), income.getFirstProduct());
            data1.put(income.getMonth(), income.getSecondProduct());
        }

        s0.setComparator(COMPARATOR);
        s0.setData(data0);
        s1.setComparator(COMPARATOR);
        s1.setData(data1);


        return new Series[]{s0, s1};
    }
}
