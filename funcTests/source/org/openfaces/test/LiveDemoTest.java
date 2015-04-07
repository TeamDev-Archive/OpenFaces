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
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.ConfirmationInspector;
import org.seleniuminspector.openfaces.DateChooserInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TabSetInspector;

/**
 * @author Darya Shumilina
 */
public class LiveDemoTest extends BaseSeleniumTest {

    @Test
    public void testAvailability() {
        assertDemoPageAvailable("/overview/homepage.jsf", "OpenFaces Online Demo \u2014 See Components in action");
        assertDemoPageAvailable("/borderlayoutpanel/BorderLayoutPanel.jsf", "Border Layout Panel \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/calendar/Calendar_styles.jsf", "Calendar \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/calendar/Calendar_dateRanges.jsf", "Calendar \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/calendar/Calendar_internationalization.jsf", "Calendar \u2014 OpenFaces Demo");
//        assertDemoPageAvailable("/chart/ChartTypes.jsf", "Chart \u2014 OpenFaces Demo");

        assertDemoPageAvailable("/chart/ChartInteractive.jsf", "Chart \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/datatable/DataTable_general.jsf", "Data Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/datatable/DataTable_sortingAndSelection.jsf", "Data Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/datatable/DataTable_styling.jsf", "Data Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/datatable/DataTable_embeddingComponents.jsf", "Data Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/datatable/DataTable_handlingLargeData.jsf", "Data Table \u2014 OpenFaces Demo");

        assertDemoPageAvailable("/datechooser/DateChooser.jsf", "Date Chooser \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/timetable/DayTable_general.jsf", "Day Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/timetable/DayTable_multipleResources.jsf", "Day Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/dropdownfield/DropDownField_client.jsf", "Drop Down Field \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/dropdownfield/DropDownField_ajax.jsf", "Drop Down Field \u2014 OpenFaces Demo");

        assertDemoPageAvailable("/dynamicimage/DynamicImage_generated.jsf", "Dynamic Image \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/dynamicimage/DynamicImage_byteArray.jsf", "Dynamic Image \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/foldingpanel/FoldingPanel_basicFeatures.jsf", "Folding Panel \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/foldingpanel/FoldingPanel_dynamicLoading.jsf", "Folding Panel \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/graphictext/GraphicText.jsf", "Graphic Text \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/graphictext/GraphicText_flexibleConfiguration.jsf", "Graphic Text \u2014 OpenFaces Demo");

        assertDemoPageAvailable("/hintlabel/HintLabel.jsf", "Hint Label \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/popuplayer/PopupLayer.jsf", "Popup Layer \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/popupmenu/PopupMenu.jsf", "Popup Menu \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/suggestionfield/SuggestionField.jsf", "Suggestion Field \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/tabset/TabSet_placementAndStyling.jsf", "Tab Set \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/tabset/TabSet_embeddingComponents.jsf", "Tab Set \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/tabbedpane/TabbedPane_placementAndAlignment.jsf", "Tabbed Pane \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/tabbedpane/TabbedPane_loadingModes.jsf", "Tabbed Pane \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/tabbedpane/TabbedPane_styling.jsf", "Tabbed Pane \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/treetable/TreeTable_basicFeatures.jsf", "Tree Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/treetable/TreeTable_selectionAndKeyboard.jsf", "Tree Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/treetable/TreeTable_RowsAndCellsCustomization.jsf", "Tree Table \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/twolistselection/TwoListSelection.jsf", "Two List Selection \u2014 OpenFaces Demo");

        assertDemoPageAvailable("/validators/Validators_standard.jsf", "Validation \u2014 OpenFaces Demo");
        assertDemoPageAvailable("/validators/Validators_custom.jsf", "Validation \u2014 OpenFaces Demo");
    }

    // see JSFC-1840 TabSet with locales can switch own value but doesn't switch locale in DateChoosers if validation triggered
    @Test(enabled = false)
    // todo: works locally, but investigate why it fails on the server
    public void testDateChooserTabSetValidation() {
        liveDemoPage("/datechooser/DateChooser.jsf");
        TabSetInspector localeSelector = tabSet("dcForm:localeSelector");
        assertEquals(0, localeSelector.getTabIndex());
        localeSelector.tabs().get(1).click();
        waitForPageToLoad();
        assertEquals(1, localeSelector.getTabIndex());
        localeSelector.tabs().get(2).click();
        waitForPageToLoad();
        assertEquals(2, localeSelector.getTabIndex());
        localeSelector.tabs().get(0).click();
        waitForPageToLoad();
        assertEquals(0, localeSelector.getTabIndex());
        DateChooserInspector dateChooser = dateChooser("dcForm:dcMMdd");
        dateChooser.field().type("wrong");
        localeSelector.tabs().get(1).click();
        assertEquals(0, localeSelector.getTabIndex());
        dateChooser.field().assertValue("wrong");
    }

    //todo: rework the test appropriately to new demo content
    @Test(enabled = false)
    public void _testSaveFilterInSession() throws Exception {
        Selenium selenium = getSelenium();
        liveDemoPage("/datatable/DataTable_filteringAndPaging.jsf");
        assertEquals("", selenium.getValue("form:allPersons:nameColumn--search_field_o_auto_filter--searchComponent"));
        assertEquals("", selenium.getValue("form:allPersons:professionColumn--search_field_o_auto_filter--searchComponent"));
        assertEquals("", selenium.getValue("form:allPersons:hobbyColumn--drop_down_o_auto_filter--searchComponent::field"));
        selenium.select("form:allPersons:ageRangeColumn--combo_box_o_auto_filter", "label=11-20");

        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();

        selenium.select("form:allPersons:lifestyleColumn--combo_box_o_auto_filter", "label=Extreme");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        selenium.type("form:allPersons:nameColumn--search_field_o_auto_filter--searchComponent", "r");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        selenium.type("form:allPersons:professionColumn--search_field_o_auto_filter--searchComponent", "D");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        selenium.type("form:allPersons:hobbyColumn--drop_down_o_auto_filter--searchComponent::field", "a");
        selenium.keyPress("form:allPersons:hobbyColumn--drop_down_o_auto_filter--searchComponent::field", "\\13");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();

        selenium.click("link=Validation");
        waitForPageToLoad();
        selenium.click("link=Data Table");
        waitForPageToLoad();
        assertEquals("r", selenium.getValue("form:allPersons:nameColumn--search_field_o_auto_filter--searchComponent"));
        assertEquals("D", selenium.getValue("form:allPersons:professionColumn--search_field_o_auto_filter--searchComponent"));
        assertEquals("a", selenium.getValue("form:allPersons:hobbyColumn--drop_down_o_auto_filter--searchComponent::field"));
    }


    @Test(enabled = false)
    public void testConfirmation() throws Exception {
        Selenium selenium = getSelenium();
        liveDemoPage("/confirmation/ConfirmationDefault.jsf");

        element("confirmationForm:buttonInvoker1").click();
        element("confirmationForm:buttonPopup1").assertVisible(true);

        element("confirmationForm:buttonInvoker").click();
        ElementInspector buttonPopup = element("confirmationForm:buttonPopup");
        buttonPopup.assertVisible(false);
        ConfirmationInspector buttonConfirmation = confirmation("confirmationForm:buttonConfirmation");
        buttonConfirmation.assertVisible(true);
        buttonConfirmation.okButton().click();
        buttonPopup.assertVisible(true);
        buttonConfirmation.assertVisible(false);

        element("confirmationForm:linkInvoker").click();
        ElementInspector linkPopup = element("confirmationForm:linkPopup");
        linkPopup.assertVisible(false);
        ConfirmationInspector linkConfirmation = confirmation("confirmationForm:linkConfirmation");
        linkConfirmation.assertVisible(true);
        linkConfirmation.okButton().click();
        linkPopup.assertVisible(true);
        linkConfirmation.assertVisible(false);

        getDriver().findElement(By.id("textInvoker")).click();
        ElementInspector textPopup = element("confirmationForm:textPopup");
        textPopup.assertVisible(false);
        ConfirmationInspector textConfirmation = confirmation("confirmationForm:textConfirmation");
        textConfirmation.assertVisible(true);
        textConfirmation.okButton().click();
        textPopup.assertVisible(true);
        textConfirmation.assertVisible(false);

        element("confirmationForm:imageInvoker").evalExpression("ondblclick.call(this, this)");
        ElementInspector imagePopup = element("confirmationForm:imagePopup");
        imagePopup.assertVisible(false);
        ConfirmationInspector imageConfirmation = confirmation("confirmationForm:imageConfirmation");
//        imageConfirmation.assertVisible();
        imageConfirmation.assertVisible(true);
        imageConfirmation.okButton().click();
        imagePopup.assertVisible(true);
        imageConfirmation.assertVisible(false);

        getDriver().findElement(By.id("message_input")).clear();
        getDriver().findElement(By.id("message_input")).sendKeys("Are you sure?");
        ElementInspector changedInvoker = element("changedInvoker");
        changedInvoker.evalExpression("click()");
        getDriver().findElement(By.id("detail_input")).clear();
        getDriver().findElement(By.id("detail_input")).sendKeys("");
        getDriver().findElement(By.id("detail_input")).sendKeys("bla bla bla");
        changedInvoker.evalExpression("click()");
        getDriver().findElement(By.id("yes_input")).clear();
        getDriver().findElement(By.id("yes_input")).sendKeys("");
        getDriver().findElement(By.id("yes_input")).sendKeys("Confirm");
        changedInvoker.evalExpression("click()");
        getDriver().findElement(By.id("no_input")).clear();
        getDriver().findElement(By.id("no_input")).sendKeys("");
        getDriver().findElement(By.id("no_input")).sendKeys("Decline");
        changedInvoker.evalExpression("click()");
        try {
            getDriver().findElement(By.xpath("//*[@id='confirmationForm:editableConfirmation']//*[contains(text(), 'Are you sure?')]"));
            getDriver().findElement(By.xpath("//*[@id='confirmationForm:editableConfirmation']//*[contains(text(), 'bla bla bla')]"));
        } catch (NoSuchElementException e) {
            assertTrue(false);
        }
        try {
            getDriver().findElement(By.xpath("//*[@id='confirmationForm:editableConfirmation']//*[contains(text(), 'Are you really sure?')]"));
            assertTrue(false);
        } catch (NoSuchElementException e) {
        }
        try {
            getDriver().findElement(By.xpath("//*[@id='confirmationForm:editableConfirmation']//*[contains(text(), 'Think once again before doing it')]"));
            assertTrue(false);
        } catch (NoSuchElementException e) {
        }
        ConfirmationInspector editableConfirmation = confirmation("confirmationForm:editableConfirmation");
        editableConfirmation.okButton().assertValue("Confirm");
        editableConfirmation.cancelButton().assertValue("Decline");
        editableConfirmation.okButton().click();
        assertTrue(window().document().isAlertPresent());
        acceptAlert();
        closeBrowser();

    }

    public void disabledTestDataTableBeanFetches() throws Exception {
        Selenium selenium = getSelenium();
        liveDemoPage("/datatable/DataTable_handlingLargeData.jsf");
        ElementInspector tr7Element = element("//*[@id='form:colorTable:queryTable']/tbody/tr[7]");
        tr7Element.assertElementExists(false);
        element("//*[@id='form:colorTable']/thead/tr[1]/td[2]").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tr7Element.assertElementExists(false);
        element("//*[@id='form:colorTable']/thead/tr[1]/td[3]").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tr7Element.assertElementExists(false);
        element("//*[@id='form:colorTable']/thead/tr[1]/td[4]").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tr7Element.assertElementExists(false);
        element("//*[@id='form:colorTable']/thead/tr[1]/td[5]").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tr7Element.assertElementExists(false);
        element("//*[@id='form:colorTable']/thead/tr[1]/td[6]").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tr7Element.assertElementExists(false);

        liveDemoPage("/datatable/DataTable_handlingLargeData.jsf");
        selenium.type("form:colorTable:name--search_field_o_auto_filter--searchComponent", "white");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        tr7Element.assertElementExists(false);
        liveDemoPage("/datatable/DataTable_handlingLargeData.jsf");
        dataTablePaginator("form:colorTable:paginator").nextPage().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tr7Element.assertElementExists(false);
    }


}