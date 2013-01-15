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

import org.openfaces.component.chart.ChartDomain;
import org.openfaces.component.chart.GridPointEvent;
import org.openfaces.component.chart.GridPointInfo;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.LineProperties;

import javax.faces.event.ActionEvent;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineChartViewBean {
    private LineChartView lineView;
    private boolean titleAction;
    private boolean pointAction;
    private GridPointInfo point;

    public LineChartViewBean() {
        LineProperties lp = new LineProperties();
        lp.setShapesVisible(true);
        lineView.addLinePropertiesList(lp);
    }

    public ChartDomain getApplyToKey() {
        return ChartDomain.KEY;
    }

    public GridPointInfo getPoint() {
        return point;
    }

    public String getTest() {
        return "1";
    }

    public void setPoint(GridPointInfo point) {
        this.point = point;
    }

    public boolean isPointAction() {
        return pointAction;
    }

    public void setPointAction(boolean pointAction) {
        this.pointAction = pointAction;
    }

    public boolean isTitleAction() {
        return titleAction;
    }

    public void setTitleAction(boolean titleAction) {
        this.titleAction = titleAction;
    }

    public LineChartView getLineView() {
        return lineView;
    }

    public void setLineView(LineChartView lineView) {
        this.lineView = lineView;
    }

    public void onTitleClickActionListener(ActionEvent event) {
        titleAction = true;
    }

    public String onTitleClickAction() {
        return null;
    }

    public void onPointClickActionListener(ActionEvent event) {
        pointAction = true;
        if (event instanceof GridPointEvent) {
            point = ((GridPointEvent) event).getPointInfo();
        }
    }

    public String onPointClickAction() {
        return null;
    }

    public String getTooltips() {
        GridPointInfo info = lineView.getPoint();
        Long key = (Long) info.getKey();
        Date d = new Date(key);
        DateFormat df = DateFormat.getDateTimeInstance();
        return df.format(d);
    }
}
