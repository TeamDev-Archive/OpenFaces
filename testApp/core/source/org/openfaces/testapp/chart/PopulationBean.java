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

package org.openfaces.testapp.chart;

import org.openfaces.util.Faces;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PieSectorInfo;
import org.openfaces.component.chart.PlainModel;
import org.openfaces.component.chart.PlainSeries;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PopulationBean {

    public ChartModel getPopulation() {

        HashMap<String, Long> data = new HashMap<String, Long>();
        data.put("London, United Kingdom", (long) 7615000);
        data.put("Berlin, Germany", (long) 3396990);
        data.put("Madrid, Spain", (long) 3155359);
        data.put("Rome, Italy", (long) 2542003);
        data.put("Paris, France", (long) 2142800);

        PlainSeries series = new PlainSeries();
        series.setData(data);
        series.setKey("Largest cities of the European Union");

        PlainModel model = new PlainModel();
        model.addSeries(series);
        return model;
    }

    public String getTitle() {
        return "Largest Cities of the European Union by Population";
    }

    public String getTooltip() {
        PieSectorInfo sector = Faces.var("sector", PieSectorInfo.class);
        DecimalFormat format = new DecimalFormat("#,###");
        return sector.getKey() + " - " + format.format(sector.getValue());
    }

}
