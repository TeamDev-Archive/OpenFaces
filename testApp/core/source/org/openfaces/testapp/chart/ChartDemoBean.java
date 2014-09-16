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

import org.openfaces.component.chart.BarChartView;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.PieSectorInfo;
import org.openfaces.testapp.ContextUtil;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ChartDemoBean implements Serializable {

    private transient PieChartView pieView;
    private transient BarChartView barView;
    private transient BarChartView barViewP;
    private transient LineChartView lineView;
    private transient PieSectorInfo pieInfo;

    public DateFormat getMediumDateFormat() {
        return new SimpleDateFormat("MMM-yyyy", new Locale("en"));
    }

    public LineChartView getLineView() {
        return lineView;
    }

    public void setLineView(LineChartView lineView) {
        this.lineView = lineView;
    }

    public BarChartView getBarView() {
        return barView;
    }

    public void setBarView(BarChartView barView) {
        this.barView = barView;
    }

    public BarChartView getBarViewP() {
        return barViewP;
    }

    public void setBarViewP(BarChartView barViewP) {
        this.barViewP = barViewP;
    }

    public PieSectorInfo getPieInfo() {
        return pieInfo;
    }

    public void setPieInfo(PieSectorInfo pieInfo) {
        this.pieInfo = pieInfo;
    }

    public PieChartView getPieView() {
        return pieView;
    }

    public void setPieView(PieChartView pieView) {
        this.pieView = pieView;

    }

    public void onClickAction() {
        CityPrecipitationBean cityPrecipitationBean = (CityPrecipitationBean) ContextUtil.resolveVariable("CityPrecipitationBean");
        cityPrecipitationBean.makeData();

        AverageTemp averageTemp = (AverageTemp) ContextUtil.resolveVariable("AverageTemp");
        averageTemp.makeData();

        MonthPrecipitation monthPrecipitation = (MonthPrecipitation) ContextUtil.resolveVariable("MonthPrecipitation");
        monthPrecipitation.makeData();

        CityTemperature cityTemperature = (CityTemperature) ContextUtil.resolveVariable("CityTemperature");
        cityTemperature.makeData();
    }

}