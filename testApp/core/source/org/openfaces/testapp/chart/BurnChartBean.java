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

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class BurnChartBean implements ChartModel, Serializable {

    private Series[] series;
    private Calendar[] dates = new Calendar[]{
            new GregorianCalendar(2005, 11, 14),
            new GregorianCalendar(2005, 11, 15),
            new GregorianCalendar(2005, 11, 16),
            new GregorianCalendar(2005, 11, 17),
            new GregorianCalendar(2005, 11, 18),
            new GregorianCalendar(2005, 11, 21),
            new GregorianCalendar(2005, 11, 22),
            new GregorianCalendar(2005, 11, 23),
            new GregorianCalendar(2005, 11, 24),
            new GregorianCalendar(2005, 11, 25)};

    public BurnChartBean() {
        int forN = 5;
        series = new Series[6];
        for (int i = 0; i < series.length; i++) {
            PlainSeries ps = new PlainSeries("Employee " + i);
            ps.setData(makeData(forN));
            series[i] = ps;
        }

    }

    public Series[] getSeries() {
        return series;
    }

    private Map<Calendar, Integer> makeData(int i) {
        Map<Calendar, Integer> data = new HashMap<Calendar, Integer>();
        for (int j = 0; j < dates.length; j++) {
            Calendar date = dates[j];
            if (j < i) {
                data.put(date, new Random().nextInt(100) - 50);
            } else {
                data.put(date, null);
            }
        }
        return data;
    }
}

