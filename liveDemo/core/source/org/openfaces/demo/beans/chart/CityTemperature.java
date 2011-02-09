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
        PlainSeries minSeries = new PlainSeries("Minimum");
        PlainSeries avgSeries = new PlainSeries("Average");
        PlainSeries maxSeries = new PlainSeries("Maximum");
        Map<Date, Integer> minData = new HashMap<Date, Integer>();
        Map<Date, Integer> avgData = new HashMap<Date, Integer>();
        Map<Date, Integer> maxData = new HashMap<Date, Integer>();

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            Calendar c = Calendar.getInstance(new Locale("en"));
            c.set(2005, i * 2 - 1, 1);
            Date key = c.getTime();

            int min, max;
            do {
                min = random.nextInt(60) - 30;
                max = random.nextInt(60) - 30;
            } while (max - min < 10);
            int range = max - min;
            int avg = (int) Math.round(min + range * 0.25 + random.nextDouble() * range * 0.5);

            minData.put(key, min);
            avgData.put(key, avg);
            maxData.put(key, max);
        }

        minSeries.setComparator(Sorter.ASCENDING);
        minSeries.setData(minData);

        avgSeries.setComparator(Sorter.ASCENDING);
        avgSeries.setData(avgData);

        maxSeries.setComparator(Sorter.ASCENDING);
        maxSeries.setData(maxData);

        series[0] = minSeries;
        series[1] = avgSeries;
        series[2] = maxSeries;
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