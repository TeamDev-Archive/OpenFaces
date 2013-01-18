/*
 * OpenFaces - JSF Component Library 3.0
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IncomeData implements Serializable {
    private ChartView chartView;
    private List<MonthIncome> income = new ArrayList<MonthIncome>();

    public IncomeData(ChartView chartView) {
        this.chartView = chartView;

        income.add(new MonthIncome("January", "Q1"));
        income.add(new MonthIncome("February", "Q1"));
        income.add(new MonthIncome("March", "Q1"));

        income.add(new MonthIncome("April", "Q2"));
        income.add(new MonthIncome("May", "Q2"));
        income.add(new MonthIncome("June", "Q2"));

        income.add(new MonthIncome("July", "Q3"));
        income.add(new MonthIncome("August", "Q3"));
        income.add(new MonthIncome("September", "Q3"));

        income.add(new MonthIncome("October", "Q4"));
        income.add(new MonthIncome("November", "Q4"));
        income.add(new MonthIncome("December", "Q4"));

        makeData();
    }

    public List<MonthIncome> getIncome() {
        return income;
    }

    public void makeData() {
        for (int i = 0, count = income.size(); i < count; i++) {
            MonthIncome income = this.income.get(i);
            int a = 200 + (int) (Math.random() * 800);
            int b = 200 + (int) (Math.random() * 800);
            income.setSecondProduct(a);
            income.setFirstProduct(b);
        }
    }

    public Object generateNewData() {
        chartView.setEditedQuarter("Q1");
        makeData();
        return null;
    }

}
