/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
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
import org.openfaces.component.chart.Sorter;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class CityTemperature implements ChartModel, Externalizable, Serializable {

    private Series[] series;

    public void makeData() {

        series = new Series[3];
        PlainSeries s0 = new PlainSeries("Minimum");
        PlainSeries s1 = new PlainSeries("Average");
        PlainSeries s2 = new PlainSeries("Maximum");
        Map<Date, Integer> data0 = new HashMap<Date, Integer>();
        Map<Date, Integer> data1 = new HashMap<Date, Integer>();
        Map<Date, Integer> data2 = new HashMap<Date, Integer>();

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            Calendar c = Calendar.getInstance(new Locale("en"));
            c.set(2005, i * 2 - 1, 1);
            Date key = c.getTime();

            data0.put(key, random.nextInt(60) - 30);
            data1.put(key, random.nextInt(60) - 30);
            data2.put(key, random.nextInt(60) - 30);
        }


        s0.setComparator(Sorter.ASCENDING);
        s0.setData(data0);

        s1.setComparator(Sorter.ASCENDING);
        s1.setData(data1);

        s2.setComparator(Sorter.ASCENDING);
        s2.setData(data2);

        series[0] = s0;
        series[1] = s1;
        series[2] = s2;

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