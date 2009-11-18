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
package org.openfaces.demo.beans.chart;

import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChartDemo implements Serializable {
    private CityPrecipitation cityPrecipitation; // todo: simplify backing beans structure for Chart demos
    private AverageTemp averageTemp;
    private MonthPrecipitation monthPrecipitation;
    private CityTemperature cityTemperature;

    public DateFormat getMediumDateFormat() {
        return new SimpleDateFormat("MMM-yyyy", new Locale("en"));
    }

    public CityPrecipitation getCityPrecipitation() {
        return cityPrecipitation;
    }

    public void setCityPrecipitation(CityPrecipitation cityPrecipitation) {
        this.cityPrecipitation = cityPrecipitation;
    }

    public AverageTemp getAverageTemp() {
        return averageTemp;
    }

    public void setAverageTemp(AverageTemp averageTemp) {
        this.averageTemp = averageTemp;
    }

    public MonthPrecipitation getMonthPrecipitation() {
        return monthPrecipitation;
    }

    public void setMonthPrecipitation(MonthPrecipitation monthPrecipitation) {
        this.monthPrecipitation = monthPrecipitation;
    }

    public CityTemperature getCityTemperature() {
        return cityTemperature;
    }

    public void setCityTemperature(CityTemperature cityTemperature) {
        this.cityTemperature = cityTemperature;
    }

    public void generateNewData(ActionEvent event) {
        cityPrecipitation.makeData();
        averageTemp.makeData();
        monthPrecipitation.makeData();
        cityTemperature.makeData();        
    }

}