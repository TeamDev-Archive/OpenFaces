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
package org.openfaces.component.chart;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;

/**
 * @author Darya Shumilina
 */
public class ChartTest extends OpenFacesTestCase {

     @Test
    public void testReRenderThroughA4J() {
        testAppFunctionalPage("/components/chart/chart_a4j.jsf");
        Selenium selenium = getSelenium();
        String oldValue = selenium.getHtmlSource();
        selenium.click("formID:refresher");
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        String newValue = selenium.getHtmlSource();
        assertFalse(newValue.equals(oldValue));
    }

     @Test
    public void testDefaultView() {
        testAppFunctionalPage("/components/chart/chart_defaultView.jsf");
        assertAppearanceNotChanged("DefaultView");
    }

    /**
     * Check charts where 'view' and 'model' defined as attributes. All three variants (line, bar , pie)
     */

     @Test
    public void testPieChartViewModelDefinedAsAttributes() {
        testAppFunctionalPage("/components/chart/chartSimplestConfiguration.jsf");
        assertAppearanceNotChanged("PieChartViewModelDefinedAsAttributes", "formID:pieChart");
    }

     @Test
    public void testLineChartViewModelDefinedAsAttributes() {
        testAppFunctionalPage("/components/chart/chartSimplestConfiguration.jsf");
        assertAppearanceNotChanged("LineChartViewModelDefinedAsAttributes", "formID:lineChart");
    }

     @Test
    public void testBarChartViewModelDefinedAsAttributes() {
        testAppFunctionalPage("/components/chart/chartSimplestConfiguration.jsf");
        assertAppearanceNotChanged("BarChartViewModelDefinedAsAttributes", "formID:barChart");
    }

    /**
     * Check charts where 'view' as child tag. All three variants (line, bar , pie)
     */

     @Test
    public void testPieChartViewAsChildTags() {
        testAppFunctionalPage("/components/chart/chartSimplestConfiguration.jsf");
        assertAppearanceNotChanged("PieChartViewAsChildTags", "formID:pieChart");
    }

     @Test
    public void testLineChartViewAsChildTags() {
        testAppFunctionalPage("/components/chart/chartSimplestConfiguration.jsf");
        assertAppearanceNotChanged("LineChartViewAsChildTags", "formID:lineChart");
    }

     @Test
    public void testBarChartViewAsChildTags() {
        testAppFunctionalPage("/components/chart/chartSimplestConfiguration.jsf");
        assertAppearanceNotChanged("BarChartViewAsChildTags", "formID:barChart");
    }

}