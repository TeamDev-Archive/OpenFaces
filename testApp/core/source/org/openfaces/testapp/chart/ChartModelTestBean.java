/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
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

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartModelTestBean implements ChartModel, Serializable {

    private Series[] series;

    public ChartModelTestBean() {
    }

    private void makeData() {

        series = new Series[2];
        PlainSeries s0 = new PlainSeries();
        s0.setKey("countries");
        Map<String, Long> data0 = new HashMap<String, Long>();

        data0.put("Ukraine", (long) 37541700);
        data0.put("Italy", (long) 60561200);
        data0.put("France", (long) 57600000);

        s0.setComparator(Sorter.ASCENDING);
        s0.setData(data0);

        series[0] = s0;
    }

    public Series[] getSeries() {
        if (series == null) {
            makeData();
        }
        return series;
    }
}