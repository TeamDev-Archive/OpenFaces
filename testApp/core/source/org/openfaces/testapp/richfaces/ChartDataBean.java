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
package org.openfaces.testapp.richfaces;

import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PlainSeries;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.Sorter;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChartDataBean implements ChartModel, Externalizable, Serializable {

    private Series[] series;

    public void makeData() {
        series = new Series[3];
        PlainSeries s0 = new PlainSeries("1");
        PlainSeries s1 = new PlainSeries("2");
        PlainSeries s2 = new PlainSeries("3");
        Map<String, Integer> data0 = new HashMap<String, Integer>();
        Map<String, Integer> data1 = new HashMap<String, Integer>();
        Map<String, Integer> data2 = new HashMap<String, Integer>();

        Random random = new Random();
        data0.put("Field 1", random.nextInt(50));
        data0.put("Field 2", random.nextInt(50));
        data0.put("Field 3", random.nextInt(50));
        data0.put("Field 4", null);

        data1.put("Field 1", random.nextInt(50));
        data1.put("Field 2", random.nextInt(50));
        data1.put("Field 3", random.nextInt(50));
        data1.put("Field 4", random.nextInt(50));

        data2.put("Field 1", random.nextInt(50));
        data2.put("Field 2", random.nextInt(50));
        data2.put("Field 3", random.nextInt(50));
        data2.put("Field 4", random.nextInt(50));

        s0.setComparator(Sorter.ASCENDING);
        s0.setData(data0);
        s0.setComparator(Sorter.ASCENDING);
        s1.setData(data1);
        s0.setComparator(Sorter.ASCENDING);
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