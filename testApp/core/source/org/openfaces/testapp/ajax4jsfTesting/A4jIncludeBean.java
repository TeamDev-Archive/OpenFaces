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

package org.openfaces.testapp.ajax4jsfTesting;

import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PlainModel;
import org.openfaces.component.chart.PlainSeries;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tatyana Matveyeva
 */
public class A4jIncludeBean {
    
    private String includedPage = "/compatibility/richfaces/a4jInclude/main_page_include.jsp";

    public String getIncludedPage() {
        return includedPage;
    }

    public void setIncludedPage(String includedPage) {
        this.includedPage = includedPage;
    }

    public String goToMain() {
        includedPage = "/compatibility/richfaces/a4jInclude/main_page_include.jsp";
        return null;
    }

    public String goToCalendar() {
        includedPage = "/compatibility/richfaces/a4jInclude/calendar_include.jsp";
        return null;
    }

    public String goToChart() {
        includedPage = "/compatibility/richfaces/a4jInclude/chart_include.jsp";
        return null;
    }

    public String goToConfirmation() {
        includedPage = "/compatibility/richfaces/a4jInclude/confirmation_include.jsp";
        return null;
    }

    public String goToDataTable() {
        includedPage = "/compatibility/richfaces/a4jInclude/dataTable_include.jsp";
        return null;
    }

    public String goToDateChooser() {
        includedPage = "/compatibility/richfaces/a4jInclude/dateChooser_include.jsp";
        return null;
    }

    public String goToDropDownField() {
        includedPage = "/compatibility/richfaces/a4jInclude/dropDownField_include.jsp";
        return null;
    }

    public String goToDynamicImage() {
        includedPage = "/compatibility/richfaces/a4jInclude/dynamicImage_include.jsp";
        return null;
    }

    public String goToFoldingPanel() {
        includedPage = "/compatibility/richfaces/a4jInclude/foldingPanel_include.jsp";
        return null;
    }

    public String goToHintLabel() {
        includedPage = "/compatibility/richfaces/a4jInclude/hintLabel_include.jsp";
        return null;
    }

    public String goToMisc() {
        includedPage = "/compatibility/richfaces/a4jInclude/misc_include.jsp";
        return null;
    }

    public String goToPopupLayer() {
        includedPage = "/compatibility/richfaces/a4jInclude/popupLayer_include.jsp";
        return null;
    }

    public String goToTabbedPane() {
        includedPage = "/compatibility/richfaces/a4jInclude/tabbedPane_include.jsp";
        return null;
    }

    public String goToTabSet() {
        includedPage = "/compatibility/richfaces/a4jInclude/tabSet_include.jsp";
        return null;
    }

    public String goToTreeTable() {
        includedPage = "/compatibility/richfaces/a4jInclude/treeTable_include.jsp";
        return null;
    }

    public String goToValidation() {
        includedPage = "/compatibility/richfaces/a4jInclude/validation_include.jsp";
        return null;
    }

    public Date getFromDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -11);
        return c.getTime();
    }

    public Date getToDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 4);
        return c.getTime();
    }

    public RenderedImage getTestImage() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawLine(20, 20, 30, 30);
        image.getGraphics().drawLine(30, 20, 20, 30);
        return image;
    }

    public ChartModel getChart() {

        Map<String, Integer> data = new HashMap<String, Integer>();
        data.put("London, United Kingdom", 7615000);
        data.put("Berlin, Germany", 3396990);
        data.put("Madrid, Spain", 3155359);
        data.put("Rome, Italy", 2542003);
        data.put("Paris, France", 2142800);

        PlainSeries series = new PlainSeries("Largest cities of the European Union");
        series.setData(data);

        PlainModel model = new PlainModel();
        model.addSeries(series);
        return model;
    }
}
