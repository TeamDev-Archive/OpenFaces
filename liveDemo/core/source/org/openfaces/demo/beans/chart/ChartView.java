/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.chart;

import org.openfaces.util.Faces;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.PieSectorEvent;
import org.openfaces.component.chart.PieSectorInfo;

import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChartView implements Serializable {
    private transient PieChartView pieView;
    private ChartModel pieChartModel;
    private ChartModel lineChartModel = new LineModel(this);
    private IncomeData incomeData = new IncomeData(this);
    private List<MonthIncome> quarterIncome = new ArrayList<MonthIncome>();

    private String quarterName;
    private String editedQuarter;

    private PieSectorInfo selectedSector;

    public ChartView() {
    }

    public String getEditedQuarter() {
        if (editedQuarter == null)
            editedQuarter = "Q1";
        return editedQuarter;
    }

    public void setEditedQuarter(String editedQuarter) {
        this.editedQuarter = editedQuarter;
    }

    public IncomeData getIncomeData() {
        return incomeData;
    }

    public ChartModel getPieChartModel() {
        if (pieChartModel == null)
            pieChartModel = new PieModel(incomeData);
        return pieChartModel;
    }

    public void setPieChartModel(ChartModel pieChartModel) {
        this.pieChartModel = pieChartModel;
    }

    public ChartModel getLineChartModel() {
        return lineChartModel;
    }

    public void setLineChartModel(ChartModel lineChartModel) {
        this.lineChartModel = lineChartModel;
    }

    public PieChartView getPieView() {
        return null;
    }

    public void setPieView(PieChartView pieView) {
        this.pieView = pieView;
    }

    private PieSectorInfo getSector() {
        return Faces.var("sector", PieSectorInfo.class);
    }

    public void quarterClickListener(ActionEvent event) {
        PieSectorEvent pEvent = (PieSectorEvent) event;
        selectedSector = pEvent.getSector();
    }

    public boolean isSelectedSector() {
        if (selectedSector == null)
            return false;

        return getSector().getKey().equals(selectedSector.getKey());
    }

    public boolean isDefaultSector() {
        if (selectedSector != null)
            return false;

        PieSectorInfo sector = getSector();
        if (sector.getKey().equals("Q1")) {
            selectedSector = sector;
            return true;
        }
        return false;
    }

    public boolean isViewDetails() {
        return selectedSector != null;
    }

    public List<MonthIncome> getQuarterIncome() {
        quarterIncome.clear();
        List<MonthIncome> incomeList = incomeData.getIncome();
        for (int i = 0, count = incomeList.size(); i < count; i++) {
            MonthIncome income = incomeList.get(i);
            if (income.getQuarter().equals(selectedSector.getKey()))
                quarterIncome.add(new MonthIncome(
                        income.getMonth(),
                        income.getQuarter(),
                        income.getFirstProduct(),
                        income.getSecondProduct()
                ));
        }
        return quarterIncome;
    }

    public String getQuarterName() {
        if (selectedSector.getKey().equals("Q1")) {
            quarterName = "First Quarter";
        }
        if (selectedSector.getKey().equals("Q2")) {
            quarterName = "Second Quarter";
        }
        if (selectedSector.getKey().equals("Q3")) {
            quarterName = "Third Quarter";
        }
        if (selectedSector.getKey().equals("Q4")) {
            quarterName = "Fourth Quarter";
        }
        return quarterName;
    }

    public Object selectSector() {
        selectedSector.setKey(editedQuarter);
        pieView.setSector(selectedSector);
        return null;
    }

}