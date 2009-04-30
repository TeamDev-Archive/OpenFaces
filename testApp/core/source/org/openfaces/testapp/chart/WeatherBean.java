/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
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
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class WeatherBean implements ChartModel, Externalizable {

    private Series[] series;

    private String country = "Ukraine";

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void makeData() {
        series = new Series[1];
        PlainSeries s0 = new PlainSeries(country);
        Map<GregorianCalendar, Integer> data0 = new HashMap<GregorianCalendar, Integer>();
        for (int i = 0; i < 12; i++) {
            data0.put(new GregorianCalendar(2004, i, 1), new Random().nextInt(10));
        }
        s0.setComparator(Sorter.ASCENDING);
        s0.setData(data0);
        series[0] = s0;
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

