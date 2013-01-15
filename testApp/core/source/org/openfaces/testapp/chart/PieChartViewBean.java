/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
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
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.PieSectorEvent;
import org.openfaces.component.chart.PieSectorInfo;

import javax.faces.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PieChartViewBean {

    private PieChartView pieView;
    private BarChartView barView;
    private LineChartView lineView;
    private boolean titleAction;
    private boolean sectorAction;
    private PieSectorInfo pieInfo;

    public LineChartView getLineView() {
        return lineView;
    }

    public void setLineView(LineChartView lineView) {
        this.lineView = lineView;
    }

    public boolean isTitleAction() {
        return titleAction;
    }

    public BarChartView getBarView() {
        return barView;
    }

    public void setBarView(BarChartView barView) {
        this.barView = barView;
    }


    public void setTitleAction(boolean titleAction) {
        this.titleAction = titleAction;
    }

    public PieSectorInfo getPieInfo() {
        return pieInfo;
    }

    public void setPieInfo(PieSectorInfo pieInfo) {
        this.pieInfo = pieInfo;
    }

    public boolean isSectorAction() {
        return sectorAction;
    }

    public void setSectorAction(boolean sectorAction) {
        this.sectorAction = sectorAction;
    }

    public PieChartView getPieView() {
        return pieView;
    }

    public void setPieView(PieChartView pieView) {
        this.pieView = pieView;
    }

    public boolean getTest() {
        return true;
    }

    public DateFormat getSampleDateFormat() {
        return new SimpleDateFormat("dd-MMM-yyyy", new Locale("fr"));
    }

    public String getTooltips() {
        if (pieView.getSector() != null && pieView.getSector().getKey() != null) {
            return pieView.getSector().getKey().toString() + " - " + pieView.getSector().getValue().toString();
        } else {
            return null;
        }
    }

    public String onTitleClickAction() {
        return null;
    }

    public void onTitleClickActionListener(ActionEvent event) {
        titleAction = true;
    }

    public boolean getCondition() {
        return pieInfo != null && pieInfo.equals(pieView.getSector());
    }

    public void onSectorClickActionListener(ActionEvent event) {
        PieSectorEvent pEvent = (PieSectorEvent) event;
        sectorAction = true;
        pieInfo = pEvent.getSector();
        ChartModel model = (barView.getChart().getModel());
        if (model instanceof CitiesDataBean) {
            CitiesDataBean data = (CitiesDataBean) model;
            data.setCountry((String) pieInfo.getKey());
        }

        ChartModel model1 = (lineView.getChart().getModel());
        if (model1 instanceof WeatherBean) {
            WeatherBean data = (WeatherBean) model1;
            data.setCountry((String) pieInfo.getKey());
            data.makeData();
        }

    }

    public boolean isSelectedSector() {
        return pieInfo != null && pieInfo.equals(pieView.getSector());
    }

    public String getStyleForCountryName() {
        return "color: gray";
    }

    public boolean isDefaultSector() {
        if (pieInfo == null) {
            if (pieView.getSector().getKey().equals("Ukraine")) {
                pieInfo = pieView.getSector();
                return true;
            }
        }
        return false;
    }

    public boolean isViewDetails() {
        return pieInfo != null;
    }

    public boolean isShowHint() {
        return pieInfo == null;
    }

    public String getExpressionInJS() {
        return "ABC";
    }

    public String getSectorInformation() {
        if (pieView.getSector().getKey().equals("Ukraine")) {
            return "Ukraine (Ukrainian: Ukrayina) is a country in Eastern Europe. It borders Russia to the northeast, Belarus to the north, Poland, Slovakia and Hungary to the west, Romania and Moldova to the southwest and the Black Sea to the south. ";
        }
        if (pieView.getSector().getKey().equals("France")) {
            return "The French Republic or France (French: Republique francaise or France) is a country whose metropolitan territory is located in Western Europe, and which is further made up of a collection of overseas islands and territories located in other continents.";
        }
        if (pieView.getSector().getKey().equals("Italy")) {
            return "Italy (Italian: Repubblica Italiana or Italia) is a country in southern Europe.";
        }
        return null;
    }
}
