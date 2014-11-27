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
package org.openfaces.test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.componentInspector.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.server.SeleniumServer;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.thoughtworks.selenium.SeleneseTestBase.*;

import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class AjaxTest extends WebDriverSeleniumTestCase {
    private DefaultSelenium localhost;
    private SeleniumServer seleniumServer;
    private WebDriver driver;

    @Before
    public void setUp() throws Exception {
//        localhost = new DefaultSelenium("localhost", 4444, "*chrome", "http://openfaces.org.test");
        seleniumServer = new SeleniumServer();
        seleniumServer.start();
        driver = new FirefoxDriver();
//        localhost.start();
    }

    @After
    public void tearDown() throws Exception {
        seleniumServer.stop();
    }


    //All DataTable features, that use Ajax is Tested on the demo (DataTable_general.jsf)
    @Test
    public void testFoldingPanelAjax() throws Exception {
        open("/components/foldingpanel/foldingPanelAjax.jsf");
        assertFalse(element("form1:Output").isElementInPage());
        foldingPanel("form1:fp1").toggle();
        assertTrue(element("form1:output1").isElementInPage());
        assertTrue(element("form1:input1").isElementInPage());

    }

    @Test
    public void testTabbedPaneAjax() throws Exception {
        open("/components/tabbedpane/tabbedPaneAjax.jsf");
        Element tab1Output = element("form1:tab1Output");
        assertTrue(tab1Output.isElementInPage());
        Element tab2Output = element("form1:tab2Output");
        assertFalse(tab2Output.isElementInPage());
        Element tab3Output = element("form1:tab3Output");
        assertFalse(tab3Output.isElementInPage());
        Element tab4Output = element("form1:tab4Output");
        assertFalse(tab4Output.isElementInPage());
        List<Element> tabs = tabbedPane("form1:tp1").tabSet().getTabs();
      /*  tabs.get(1).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tab2Output.assertElementExists(true);
        tabs.get(2).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tab3Output.assertElementExists(true);
        tabs.get(3).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tab4Output.assertElementExists(true);
        tabs.get(0).click();
        tab1Output.assertElementExists(true);*/
    }


}