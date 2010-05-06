/*
 * OpenFaces - JSF Component Library 3.0
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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class CitiesDataBean implements ChartModel, Externalizable {

    private Series[] series;

    private String country = "Ukraine";

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private void makeData() {

        series = new Series[2];
        PlainSeries s0 = new PlainSeries();
        PlainSeries s1 = new PlainSeries();
        s0.setKey("2004");
        Map<String, Integer> data0 = new HashMap<String, Integer>();
        s1.setKey("2005");
        Map<String, Integer> data1 = new HashMap<String, Integer>();

        if (country.equals("Ukraine")) {

            data0.put("Kiev", new Random().nextInt(500000));
            data0.put("Kharkov", new Random().nextInt(500000));
            data0.put("L'vov", new Random().nextInt(500000));
            data0.put("Donetsk", new Random().nextInt(500000));
            data0.put("Dnepropetrovsk", new Random().nextInt(500000));

            data1.put("Kiev", new Random().nextInt(500000));
            data1.put("Kharkov", new Random().nextInt(500000));
            data1.put("L'vov", new Random().nextInt(500000));
            data1.put("Donetsk", new Random().nextInt(500000));
            data1.put("Dnepropetrovsk", new Random().nextInt(500000));
        } else if (country.equals("France")) {
            data0.put("Paris", new Random().nextInt(500000));
            data0.put("Lion", new Random().nextInt(500000));
            data0.put("Nice", new Random().nextInt(500000));
            data1.put("Paris", new Random().nextInt(500000));
            data1.put("Lion", new Random().nextInt(500000));
            data1.put("Nice", new Random().nextInt(500000));
        } else {
            data0.put("Rome", new Random().nextInt(500000));
            data0.put("Florence", new Random().nextInt(500000));
            data1.put("Rome", new Random().nextInt(500000));
            data1.put("Florence", new Random().nextInt(500000));
        }

        s0.setComparator(Sorter.ASCENDING);
        s0.setData(data0);
        s1.setComparator(Sorter.ASCENDING);
        s1.setData(data1);
        series[0] = s0;
        series[1] = s1;
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
