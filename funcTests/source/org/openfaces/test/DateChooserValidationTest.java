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
package org.openfaces.test;

import com.thoughtworks.selenium.Selenium;
import org.junit.After;
import org.junit.Test;
import org.seleniuminspector.openfaces.DateChooserInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;

/**
 * @author Pavel Kaplin
 */
public class DateChooserValidationTest extends OpenFacesTestCase {

    @Test
    public void testClientValidationOff() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datechooser/dateChooserValid.jsf");
        assertEquals("DateChooser Valid", selenium.getTitle());
        assertConversionErrorOccured(false);
        dateChooser("testForm:dateChooserOff").field().type("wrong");
        assertConversionErrorOccured(false);
        element("testForm:submitBtn").clickAndWait();
        assertEquals("DateChooser Valid", selenium.getTitle());
        assertConversionErrorOccured(true);
    }

    @Test
    public void testClientValidationOnSubmit() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datechooser/dateChooserValid.jsf");
        assertEquals("DateChooser Valid", selenium.getTitle());
        assertConversionErrorOccured(false);
        dateChooser("testForm:dateChooserOnSubmit").field().type("wrong");
        assertConversionErrorOccured(false); // no global message is shown upon just typing, it'll be shown after submission attempt
        element("testForm:submitBtn").click();
        assertEquals("DateChooser Valid", selenium.getTitle());
        assertConversionErrorOccured(true);
    }

    @Test
    public void testClientValidationDefault() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datechooser/dateChooserValid.jsf");
        assertEquals("DateChooser Valid", selenium.getTitle());
        assertConversionErrorOccured(false);
        dateChooser("testForm:dateChooserDefault").field().type("wrong");
        assertConversionErrorOccured(false); // no global message is shown upon just typing, it'll be shown after submission attempt
        element("testForm:submitBtn").click();
        assertEquals("DateChooser Valid", selenium.getTitle());
        assertConversionErrorOccured(true);
    }

    @Test
    public void testChangeLocaleFromBinding() {
        Selenium selenium = getSelenium();
        liveDemoPage("/datechooser/DateChooser.jsf");
        assertPageContainsErrorIcon(false);

        DateChooserInspector defaultDateChooser = dateChooser("dcForm:dcDefault");
        defaultDateChooser.field().clear();
        defaultDateChooser.field().type("Oct 17, 2006sdfsdf");
        DateChooserInspector mMddDateChooser = dateChooser("dcForm:dcMMdd");
        mMddDateChooser.field().clear();
        mMddDateChooser.field().type("10/17/2006");
        assertPageContainsErrorIcon(true);
        defaultDateChooser.field().clear();
        defaultDateChooser.field().type("Oct 10, 2006");
        mMddDateChooser.field().clear();
        mMddDateChooser.field().type("10/17/2006");
        assertPageContainsErrorIcon(false);
        tabSet("dcForm:localeSelector").setTabIndex(1, OpenFacesAjaxLoadingMode.getInstance());
        assertEquals("Date Chooser \u2014 OpenFaces Demo", selenium.getTitle());
        assertPageContainsErrorIcon(false);
        defaultDateChooser.field().clear();
        defaultDateChooser.field().type("Oct 17, 2006sdfsdf");
        mMddDateChooser.field().clear();
        mMddDateChooser.field().type("10/17/2006");
        assertPageContainsErrorIcon(true);
        defaultDateChooser.field().clear();
        defaultDateChooser.field().type("17 oct. 2006");
        assertPageContainsErrorIcon(false);
    }

    @After
    public void closeBrowser() {
        getDriver().quit();
    }
}
