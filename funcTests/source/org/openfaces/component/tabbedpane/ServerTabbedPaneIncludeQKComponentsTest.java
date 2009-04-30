/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.tabbedpane;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.ElementInspector;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.openfaces.*;

/**
 * @author Darya Shumilina
 */
public class ServerTabbedPaneIncludeQKComponentsTest extends OpenFacesTestCase {

    @Test
    public void testCalendarInside() {
        testAppFunctionalPage("/components/tabbedpane/calendarIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        ElementInspector firstCalendar = element("fn:firstCalendar");
        firstCalendar.assertElementExists();
        firstCalendar.assertVisible(true);

        element("fn:secondHeader").clickAndWait();
        ElementInspector secondCalendar = element("fn:secondCalendar");
        secondCalendar.assertElementExists();
        secondCalendar.assertVisible(true);
    }

    @Test
    public void testConfirmationInside() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/confirmationIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        ConfirmationInspector confirmation1 = confirmation("fn:conf1");
        confirmation1.assertElementExists(true);
        ConfirmationInspector confirmation2 = confirmation("fn:conf2");
        confirmation2.assertElementExists(false);

        element("button1").click();
        confirmation1.assertVisible(true);

        confirmation1.okButton().click();
        assertTrue(selenium.isAlertPresent());
        assertEquals("done", selenium.getAlert());
        confirmation1.assertVisible(false);

        element("fn:secondTabID").clickAndWait();
        element("button2").click();
        confirmation2.assertVisible(true);

        confirmation2.okButton().click();
        assertTrue(selenium.isAlertPresent());
        assertEquals("done", selenium.getAlert());
        confirmation2.assertVisible(false);
    }

    @Test
    public void testDataTableInside() {
        testAppFunctionalPage("/components/tabbedpane/dataTableIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        DataTableInspector firstDataTable = dataTable("fn:firstDataTableID");
        firstDataTable.assertElementExists();
        firstDataTable.assertVisible(true);
        DataTableInspector secondDataTable = dataTable("fn:secondDataTableID");
        secondDataTable.assertElementExists(false);

        firstDataTable.makeAndCheckSingleSelection(1, 1);
        firstDataTable.column(0).makeSorting();
        dataTablePaginator("fn:firstDataTableID:firstPaginator").makePagination(3);
        firstDataTable.column(1).filter(DropDownFieldFilterInspector.class).makeFiltering("col2_row1");

        element("fn:secondTabID").clickAndWait();
        secondDataTable.assertVisible(true);
        secondDataTable.makeAndCheckSingleSelection(0, 1);
        secondDataTable.column(2).makeSorting();
        dataTablePaginator("fn:secondDataTableID:secondPaginator").makePagination(3);
        secondDataTable.column(0).filter(ComboBoxFilterInspector.class).makeFiltering("col1_row1");
    }

    @Test
    public void testDateChooserInside() {
        testAppFunctionalPage("/components/tabbedpane/dateChooserIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        DateChooserInspector firstDateChooser = dateChooser("fn:firstDateChooser");
        firstDateChooser.assertElementExists();
        firstDateChooser.assertVisible(true);
        DateChooserInspector secondDateChooser = dateChooser("fn:secondDateChooser");
        secondDateChooser.assertElementExists(false);
        firstDateChooser.field().assertValue("Jul 30, 2006");

        element("fn:secondTabID").clickAndWait();
        secondDateChooser.assertElementExists();
        secondDateChooser.field().assertValue("Jul 30, 2006");
    }

    @Test
    public void testDropDownInside() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/dropDownIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        DropDownFieldInspector firstDropDown = dropDownField("fn:firstDropDown");
        firstDropDown.assertVisible(true);
        firstDropDown.assertElementExists();
        DropDownFieldInspector secondDropDown = dropDownField("fn:secondDropDown");
        secondDropDown.assertElementExists(false);

        firstDropDown.popup().items().get(1).click();
        assertTrue(selenium.isTextPresent("Yellow"));
        element("fn:secondTabID").clickAndWait();
        secondDropDown.assertElementExists();

        secondDropDown.popup().items().get(1).click();
        secondDropDown.field().assertValue("Yellow");
    }

    @Test
    public void testDynamicImageInside() {
        testAppFunctionalPage("/components/tabbedpane/dynamicImageIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        ElementInspector firstDynamicImage = element("fn:firstDynamicImageID");
        firstDynamicImage.assertElementExists();
        firstDynamicImage.assertVisible(true);
        ElementInspector secondDynamicImage = element("fn:secondDynamicImageID");
        secondDynamicImage.assertElementExists(false);

        element("fn:secondTabID").clickAndWait();
        secondDynamicImage.assertElementExists();
        secondDynamicImage.assertVisible(true);
    }

    @Test
    public void testAjaxFoldingPanelInside() {
        testAppFunctionalPage("/components/tabbedpane/foldingPanelIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        ElementInspector firstPanelCaption = element("fn:firstFoldingPanelCaption");
        firstPanelCaption.assertVisible(true);
        ElementInspector firstPanelContent = element("fn:firstFoldingPanelContent");
        firstPanelContent.assertElementExists(false);

        ElementInspector secondPanelCaption = element("fn:secondFoldingPanelCaption");
        secondPanelCaption.assertElementExists(false);
        ElementInspector secondPanelContent = element("fn:secondFoldingPanelContent");
        secondPanelContent.assertElementExists(false);

        foldingPanel("fn:firstFoldingPanelID").toggle().clickAndWait(LoadingMode.AJAX);
        firstPanelContent.assertVisible(true);

        element("fn:secondTabID").clickAndWait();
        secondPanelCaption.assertVisible(true);
        secondPanelContent.assertElementExists(false);

        foldingPanel("fn:secondFoldingPanelID").toggle().clickAndWait(LoadingMode.AJAX);
        secondPanelContent.assertVisible(true);
    }

    @Test
    public void testHintLabelInside() throws InterruptedException {
        testAppFunctionalPage("/components/tabbedpane/hintLabelIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        hintLabel("fn:firstHintLabelID").checkVisibilityAndContent("First HintLabel Value :-)", "First HintLabel Title ;-)");

        element("fn:secondTab").clickAndWait();
        hintLabel("fn:secondHintLabelID").checkVisibilityAndContent("Second HintLabel Value :-)", "Second HintLabel Title ;-)");
    }

    @Test
    public void testPopupLayerInside() {
        testAppFunctionalPage("/components/tabbedpane/popupLayerIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        ElementInspector headerPopup = element("fn:header_popup");
        headerPopup.assertElementExists();
        headerPopup.assertVisible(false);
        ElementInspector headerPopup1 = element("fn:header_popup1");
        headerPopup1.assertElementExists(false);

        element("fn:header_invoker").click();
        headerPopup.assertVisible(true);

        element("fn:secondTabID").clickAndWait();
        headerPopup1.assertElementExists();
        headerPopup1.assertVisible(false);

        element("fn:header_invoker1").click();
        headerPopup1.assertVisible(true);
    }

    //todo: uncomment when the JSFC-3629 is fixed
    @Ignore
    @Test
    public void _testServerTabbedPaneInsideServerTabbedPane() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        assertTrue(selenium.isElementPresent("fn:firstItem"));
        assertTrue(selenium.isVisible("fn:firstItem"));
        assertFalse(selenium.isElementPresent("fn:secondItem"));

        assertTrue(selenium.getText("fn:firstHeader").equals("First tab"));
        assertTrue(selenium.getText("fn:secondHeader").equals("Second tab"));
        assertTrue(selenium.getText("fn:firstItem_firstHeader").equals("First tab"));
        assertTrue(selenium.getText("fn:firstItem_secondHeader").equals("Second tab"));
        assertTrue(selenium.getText("fn:firstItem_firstContent").equals("Some text on the first tab"));
        selenium.click("fn:firstItem_secondHeader");
        waitForPageToLoad();
        assertTrue(selenium.getText("fn:firstItem_secondContent").equals("Some text on the second tab"));

        selenium.click("fn:secondHeader");
        waitForPageToLoad();
        assertTrue(selenium.isElementPresent("fn:secondItem"));
        assertTrue(selenium.isVisible("fn:secondItem"));
        assertTrue(selenium.getText("fn:secondItem_firstHeader").equals("First tab"));
        assertTrue(selenium.getText("fn:secondItem_secondHeader").equals("Second tab"));
        assertTrue(selenium.getText("fn:secondItem_firstContent").equals("Some text on the first tab"));
        selenium.click("fn:secondItem_secondHeader");
        waitForPageToLoad();
        assertTrue(selenium.getText("fn:secondItem_secondContent").equals("Some text on the second tab"));
    }

    @Test
    public void testTabSetInside() {
        testAppFunctionalPage("/components/tabbedpane/tabSetIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        element("fn:firstTabSet_firstTab").assertText("Client");

        ElementInspector firstTabSetSecondTab = element("fn:firstTabSet_secondTab");
        firstTabSetSecondTab.click();
        firstTabSetSecondTab.assertText("Server");

        element("fn:secondHeader").clickAndWait();
        element("fn:secondTabSet_firstTab").assertText("Client");

        ElementInspector secondTabSetSecondTab = element("fn:secondTabSet_secondTab");
        secondTabSetSecondTab.click();
        secondTabSetSecondTab.assertText("Server");
    }

    @Test
    public void testTreeTableInside() {
        testAppFunctionalPage("/components/tabbedpane/treeTableIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);
        element("fn:firstHeader").assertText("First tab");
        for (int i = 0; i < 3; i++) {
            window().document().getElementsByTagName("img").get(i).clickAndWait(LoadingMode.AJAX);
        }

        TreeTableInspector firstTreeTable = treeTable("fn:firstTreeTable");
        firstTreeTable.column(0).makeSorting();
        firstTreeTable.column(0).filter(SearchFieldFilterInspector.class).makeFiltering("color");

        element("fn:secondHeader").clickAndWait();
        for (int i = 0; i < 3; i++) {
            window().document().getElementsByTagName("img").get(i).clickAndWait(LoadingMode.AJAX);
        }

        TreeTableInspector secondTreeTable = treeTable("fn:secondTreeTable");
        secondTreeTable.column(0).makeSorting();
        secondTreeTable.column(0).filter(DropDownFieldFilterInspector.class).makeFiltering("color");
    }

    @Test
    public void testTwoListSelectionInside() {
        testAppFunctionalPage("/components/tabbedpane/twoListSelectionIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);

        TwoListSelectionInspector firstTls = twoListSelection("fn:firstTLS");
        firstTls.assertElementExists();
        firstTls.assertVisible(true);
        TwoListSelectionInspector secondTls = twoListSelection("fn:secondTLS");
        secondTls.assertElementExists(false);

        firstTls.addAllButton().click();
        firstTls.rightList().assertText("item 1 labelitem 5 labelitem 3 label");

        element("fn:secondHeader").clickAndWait();
        secondTls.assertElementExists();
        secondTls.assertVisible(true);

        secondTls.addAllButton().click();
        secondTls.rightList().assertText("item 1 labelitem 5 labelitem 3 label");
    }

    @Test
    public void testValidationInside() {
        testAppFunctionalPage("/components/tabbedpane/validationIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, LoadingMode.SERVER);
        element("fn:first_messageID").assertVisible(true);
    }

}