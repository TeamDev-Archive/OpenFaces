/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
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
import org.junit.Ignore;
import org.junit.Test;
import org.seleniuminspector.openfaces.FoldingPanelInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.ElementInspector;

import java.util.List;

/**
 * @author Tatyana Matveyeva
 */
public class AjaxTest extends OpenFacesTestCase {

    //All DataTable features, that use Ajax is Tested on the demo (DataTable_general.jsf)
     //@Test
    public void testFoldingPanelAjax() throws Exception {
        testAppFunctionalPage("/components/foldingpanel/foldingPanelAjax.jsf");
        element("form1:Output").assertElementExists(false);
        foldingPanel("form1:fp1").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        element("form1:output1").assertElementExists(true);
        element("form1:input1").assertElementExists(true);
    }

    //JSFC-1954
    @Ignore
     //@Test
    public void disabledTestFPClientValidation() throws Exception {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foldingpanel/foldingPanelAjax.jsf");
        foldingPanel("form1:fp1").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        selenium.type("form1:input1", "");
        selenium.click("form1:sumbitForm1");
        assertTrue(selenium.isTextPresent("\"input1\": Value is required."));
        assertTrue(selenium.isTextPresent("Validation Error"));
    }

    //JSFC-1954
    @Ignore
     //@Test
    public void disabledTestTPClientValidation() throws Exception {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneAjax.jsf");
        tabbedPane("form1:tp1").tabSet().tabs().get(1).click();
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        selenium.type("form1:input2", "");
        selenium.click("form1:submitForm");
        assertTrue(selenium.isTextPresent("\"input2\": Value is required."));
        assertTrue(selenium.isTextPresent("Validation Error"));
    }

    //JSFC-1953
    @Ignore
     //@Test
    public void disabledTestFPMessages() throws Exception {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foldingpanel/foldingPanelAjax.jsf");
        FoldingPanelInspector fp2 = foldingPanel("form2:fp2");
        fp2.toggle().click();
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        selenium.type("form2:input2", "");
        selenium.click("form2:submitForm2");
        waitForPageToLoad();
        fp2.toggle().click();
        selenium.click("form2:submitForm2");
        waitForPageToLoad();
        assertFalse(selenium.isElementPresent("form1:input2"));
        assertFalse(selenium.isTextPresent("\"input2\": Value is required."));
        assertFalse(selenium.isTextPresent("Validation Error"));
    }

     //@Test
    public void testTabbedPaneAjax() throws Exception {
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneAjax.jsf");
        ElementInspector tab1Output = element("form1:tab1Output");
        tab1Output.assertElementExists(true);
        ElementInspector tab2Output = element("form1:tab2Output");
        tab2Output.assertElementExists(false);
        ElementInspector tab3Output = element("form1:tab3Output");
        tab3Output.assertElementExists(false);
        ElementInspector tab4Output = element("form1:tab4Output");
        tab4Output.assertElementExists(false);
        List<ElementInspector> tabs = tabbedPane("form1:tp1").tabSet().tabs();
        tabs.get(1).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tab2Output.assertElementExists(true);
        tabs.get(2).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tab3Output.assertElementExists(true);
        tabs.get(3).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tab4Output.assertElementExists(true);
        tabs.get(0).click();
        tab1Output.assertElementExists(true);
    }

    @Ignore
     //@Test
    public void disabledTestPagingValidation() throws Exception {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/dataTableAjax.jsf");
        selenium.type("form2:dataTable2:0:inputProfession2", "");
        selenium.click("form2:dataTable2:paginator2--nextPage");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        assertEquals("John Smith", selenium.getValue("form2:dataTable2:0:inputName2"));
        assertTrue(selenium.isTextPresent("\"inputProfession2\": Value is required."));
        assertTrue(selenium.isTextPresent("Validation Error"));
        assertEquals("1", selenium.getValue("form2:dataTable2:paginator2--pageNo"));
    }

    @Ignore
     //@Test
    public void disabledTestPagingClientValidation() throws Exception {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/dataTableAjax.jsf");
        selenium.type("form1:dataTable1:0:inputProfession", "");
        selenium.click("form1:dataTable1:paginator1--nextPage");
        assertTrue(selenium.isTextPresent("\"inputProfession1\": Value is required."));
        assertTrue(selenium.isTextPresent("Validation Error"));
        assertEquals("1", selenium.getValue("form2:dataTable2:paginator2--pageNo"));
    }

}