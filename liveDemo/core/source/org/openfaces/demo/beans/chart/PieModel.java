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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class PieModel implements ChartModel {
    private static final String FIRST_QUARTER = "Q1";
    private static final String SECOND_QUARTER = "Q2";
    private static final String THIRD_QUARTER = "Q3";
    private static final String FOURTH_QUARTER = "Q4";
    private static final Comparator<Tuple> COMPARATOR = new Comparator<Tuple>() {
        public int compare(Tuple t1, Tuple t2) {
            int num1 = convertQuarterToInteger((String) t1.getKey());
            int num2 = convertQuarterToInteger((String) t2.getKey());
            if (num1 == num2)
                return 0;
            return num1 < num2 ? -1 : 1;
        }

        private int convertQuarterToInteger(String quarter) {
            if (FIRST_QUARTER.equals(quarter))
                return 0;
            if (SECOND_QUARTER.equals(quarter))
                return 1;
            if (THIRD_QUARTER.equals(quarter))
                return 2;
            if (FOURTH_QUARTER.equals(quarter))
                return 3;


            throw new RuntimeException("Illegal month value: " + quarter);
        }
    };

    private IncomeData incomeData;

    public PieModel(IncomeData incomeData) {
        this.incomeData = incomeData;
    }

    public Series[] getSeries() {
        PlainSeries series = new PlainSeries("quarters");
        Map<String, Integer> data = new HashMap<String, Integer>();
        int firstQuarter = 0;
        int secondQuarter = 0;
        int thirdQuarter = 0;
        int fourthQuarter = 0;
        for (int i = 0; i < incomeData.getIncome().size(); i++) {
            MonthIncome temp = incomeData.getIncome().get(i);
            String quarter = temp.getQuarter();
            if (quarter.equals(FIRST_QUARTER))
                firstQuarter = firstQuarter + getProductsSum(temp);
            if (quarter.equals(SECOND_QUARTER))
                secondQuarter = secondQuarter + getProductsSum(temp);
            if (quarter.equals(THIRD_QUARTER))
                thirdQuarter = thirdQuarter + getProductsSum(temp);
            if (quarter.equals(FOURTH_QUARTER))
                fourthQuarter = fourthQuarter + getProductsSum(temp);
        }

        data.put(FIRST_QUARTER, firstQuarter);
        data.put(SECOND_QUARTER, secondQuarter);
        data.put(THIRD_QUARTER, thirdQuarter);
        data.put(FOURTH_QUARTER, fourthQuarter);

        series.setComparator(COMPARATOR);
        series.setData(data);
        return new Series[]{series};
    }

    private int getProductsSum(MonthIncome income) {
        int result = income.getSecondProduct() + income.getFirstProduct();
        return result;
    }
}

