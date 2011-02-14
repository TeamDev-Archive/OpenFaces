/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.*;

/**
 * @author Darya Shumilina
 */
public class AjaxTabbedPaneIncludeOFComponentsTest extends OpenFacesTestCase {
    @Test
    public void testCalendarInside() {
        testAppFunctionalPage("/components/tabbedpane/calendarIn.jsf");

        ElementInspector firstCalendar = element("fn:firstCalendar");
        firstCalendar.assertElementExists();
        firstCalendar.assertVisible(true);

        element("fn:secondHeader").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        ElementInspector secondCalendar = element("fn:secondCalendar");
        secondCalendar.assertElementExists();
        secondCalendar.assertVisible(true);
    }

    @Test
    public void testConfirmationInside() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/confirmationIn.jsf");

        ConfirmationInspector confirmation1 = confirmation("fn:conf1");
        confirmation1.assertElementExists();
        ConfirmationInspector confirmation2 = confirmation("fn:conf2");
        confirmation2.assertElementExists(false);

        element("button1").click();
        confirmation1.assertVisible(true);

        confirmation1.okButton().click();
        assertTrue(selenium.isAlertPresent());
        assertEquals("done", selenium.getAlert());
        confirmation1.assertVisible(false);

        element("fn:secondTabID").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        confirmation2.assertElementExists(true);

        element("button2").click();
        confirmation2.assertVisible(true);

        confirmation2.okButton().click();
        assertTrue(selenium.isAlertPresent());
        assertEquals("done", selenium.getAlert());
        confirmation2.assertVisible(false);
    }

    //todo: uncomment this method if JSFC-2452 fixed
    @Ignore
    @Test
    public void _testDataTableInsideAjaxTabbedPane() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/dataTableIn.jsf");

        DataTableInspector firstDataTable = dataTable("fn:firstDataTableID");
        firstDataTable.assertElementExists();
        firstDataTable.assertVisible(true);

        DataTableInspector secondDataTable = dataTable("fn:secondDataTableID");
        secondDataTable.assertElementExists(false);
        secondDataTable.assertVisible(false);

        firstDataTable.makeAndCheckSingleSelection(1, 1);
        firstDataTable.column(0).makeSorting();
        dataTablePaginator("fn:firstDataTableID:firstPaginator").makePagination(3);
        firstDataTable.column(1).filter(DropDownFieldFilterInspector.class, "fn:firstDataTableID:filter1").makeFiltering("col2_row1");
        selenium.click("fn:secondTabID");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        secondDataTable.assertElementExists(false);
        secondDataTable.assertVisible(false);
        secondDataTable.makeAndCheckSingleSelection(0, 1);
        secondDataTable.column(2).makeSorting();
        dataTablePaginator("fn:secondDataTableID:secondPaginator").makePagination(3);
        secondDataTable.column(0).filter(ComboBoxFilterInspector.class, "fn:secondDataTableID:filter1").makeFiltering("col1_row1");
    }


    @Test
    public void testDateChooserInside() {
        testAppFunctionalPage("/components/tabbedpane/dateChooserIn.jsf");

        DateChooserInspector firstDataChooser = dateChooser("fn:firstDateChooser");
        firstDataChooser.assertElementExists();
        firstDataChooser.assertVisible(true);

        DateChooserInspector secondDataChooser = dateChooser("fn:secondDateChooser");
        secondDataChooser.assertElementExists(false);
        firstDataChooser.field().assertValue("Jul 30, 2006");

        element("fn:secondTabID").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        secondDataChooser.assertVisible(true);
        secondDataChooser.field().assertValue("Jul 30, 2006");
    }

    @Test
    public void testDropDownInside() {
        testAppFunctionalPage("/components/tabbedpane/dropDownIn.jsf");

        DropDownFieldInspector firstDropDown = dropDownField("fn:firstDropDown");
        firstDropDown.assertElementExists();
        firstDropDown.assertVisible(true);

        DropDownFieldInspector secondDropDown = dropDownField("fn:secondDropDown");
        secondDropDown.assertElementExists(false);
        firstDropDown.popup().items().get(1).click();
        firstDropDown.field().assertValue("Yellow");

        element("fn:secondTabID").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        secondDropDown.assertElementExists();
        secondDropDown.popup().items().get(1).click();
        firstDropDown.field().assertValue("Yellow");
    }

    @Test
    public void testDynamicImageInside() {
        testAppFunctionalPage("/components/tabbedpane/dynamicImageIn.jsf");

        ElementInspector firstDynamicImage = element("fn:firstDynamicImageID");
        firstDynamicImage.assertElementExists();
        firstDynamicImage.assertVisible(true);

        ElementInspector secondDynamicImage = element("fn:secondDynamicImageID");
        secondDynamicImage.assertElementExists(false);

        element("fn:secondTabID").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        secondDynamicImage.assertElementExists();
        secondDynamicImage.assertVisible(true);
    }

    @Test
    public void testAjaxFoldingPanelInside() {
        testAppFunctionalPage("/components/tabbedpane/foldingPanelIn.jsf");

        ElementInspector firstFoldingPanelCaption = element("fn:firstFoldingPanelCaption");
        firstFoldingPanelCaption.assertVisible(true);
        ElementInspector firstFoldingPanelContent = element("fn:firstFoldingPanelContent");
        firstFoldingPanelContent.assertElementExists(false);
        ElementInspector secondFoldingPanelCaption = element("fn:secondFoldingPanelCaption");
        secondFoldingPanelCaption.assertElementExists(false);
        ElementInspector secondFoldingPanelContent = element("fn:secondFoldingPanelContent");
        secondFoldingPanelCaption.assertElementExists(false);

        foldingPanel("fn:firstFoldingPanelID").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        firstFoldingPanelContent.assertVisible(true);

        element("fn:secondTabID").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        secondFoldingPanelCaption.assertVisible(true);
        secondFoldingPanelContent.assertElementExists(false);

        foldingPanel("fn:secondFoldingPanelID").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        secondFoldingPanelContent.assertElementExists();
    }

    @Test
    public void testHintLabelInside() throws InterruptedException {
        testAppFunctionalPage("/components/tabbedpane/hintLabelIn.jsf");
        hintLabel("fn:firstHintLabelID").checkVisibilityAndContent("First HintLabel Value :-)", "First HintLabel Title ;-)");

        element("fn:secondTab").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        hintLabel("fn:secondHintLabelID").checkVisibilityAndContent("Second HintLabel Value :-)", "Second HintLabel Title ;-)");
    }

    @Test
    public void testPopupLayerInside() {
        testAppFunctionalPage("/components/tabbedpane/popupLayerIn.jsf");

        ElementInspector headerPopup = element("fn:header_popup");
        headerPopup.assertElementExists();
        headerPopup.assertVisible(false);
        ElementInspector headerPopup1 = element("fn:header_popup1");
        headerPopup1.assertElementExists(false);

        element("fn:header_invoker").click();
        headerPopup.assertVisible(true);

        element("fn:secondTabID").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        headerPopup1.assertElementExists();
        headerPopup1.assertVisible(false);

        element("fn:header_invoker1").click();
        headerPopup1.assertVisible(true);
    }

    //todo: uncomment when the  JSFC-3629 is fixed
    @Ignore
    @Test
    public void _testAjaxTabbedPaneInsideAjaxTabbedPane() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/tabbedpane/tabbedPaneIn.jsf");

        assertTrue(selenium.isElementPresent("fn:firstItem"));
        assertTrue(selenium.isVisible("fn:firstItem"));
        assertFalse(selenium.isElementPresent("fn:secondItem"));

        assertTrue(selenium.getText("fn:firstHeader").equals("First tab"));
        assertTrue(selenium.getText("fn:secondHeader").equals("Second tab"));
        assertTrue(selenium.getText("fn:firstItem_firstHeader").equals("First tab"));
        assertTrue(selenium.getText("fn:firstItem_secondHeader").equals("Second tab"));
        assertTrue(selenium.getText("fn:firstItem_firstContent").equals("Some text on the first tab"));
        selenium.click("fn:firstItem_secondHeader");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        assertTrue(selenium.getText("fn:firstItem_secondContent").equals("Some text on the second tab"));

        selenium.click("fn:secondHeader");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        assertTrue(selenium.isElementPresent("fn:secondItem"));
        assertTrue(selenium.isVisible("fn:secondItem"));
        assertTrue(selenium.getText("fn:secondItem_firstHeader").equals("First tab"));
        assertTrue(selenium.getText("fn:secondItem_secondHeader").equals("Second tab"));
        assertTrue(selenium.getText("fn:secondItem_firstContent").equals("Some text on the first tab"));
        selenium.click("fn:secondItem_secondHeader");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        assertTrue(selenium.getText("fn:secondItem_secondContent").equals("Some text on the second tab"));
    }

    @Test
    public void testTabSetInside() {
        testAppFunctionalPage("/components/tabbedpane/tabSetIn.jsf");

        element("fn:firstTabSet_firstTab").assertText("Client");
        ElementInspector firstTabSetSecondTab = element("fn:firstTabSet_secondTab");

        firstTabSetSecondTab.click();
        firstTabSetSecondTab.assertText("Server");

        element("fn:secondHeader").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        element("fn:secondTabSet_firstTab").assertText("Client");
        ElementInspector secondTabSetSecondTab = element("fn:secondTabSet_secondTab");

        secondTabSetSecondTab.click();
        secondTabSetSecondTab.assertText("Server");
    }

    @Test
    public void testTreeTableInside() {
        testAppFunctionalPage("/components/tabbedpane/treeTableIn.jsf");
        element("fn:firstHeader").assertText("First tab");
        for (int i = 0; i < 3; i++) {
            window().document().getElementsByTagName("img").get(i).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }

        TreeTableInspector treeTable = treeTable("fn:firstTreeTable");
        treeTable.column(0).makeSorting();
        treeTable.column(0).filter(InputTextFilterInspector.class, "fn:firstTreeTable:filter1").makeFiltering("color");
        int imagesOnFirstPage = window().document().getElementsByTagName("img").size();
        element("fn:secondHeader").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        for (int i = 0; i < 3; i++) {
            window().document().getElementsByTagName("img").get((imagesOnFirstPage + i)).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }
        TreeTableInspector secondTreeTable = treeTable("fn:secondTreeTable");
        secondTreeTable.column(0).makeSorting();
        secondTreeTable.column(0).filter(DropDownFieldFilterInspector.class, "fn:secondTreeTable:filter1").makeFiltering("color");
    }

    @Test
    public void testTwoListSelectionInside() {
        testAppFunctionalPage("/components/tabbedpane/twoListSelectionIn.jsf");

        TwoListSelectionInspector firstTls = twoListSelection("fn:firstTLS");
        firstTls.assertElementExists();
        firstTls.assertVisible(true);
        TwoListSelectionInspector secondTls = twoListSelection("fn:secondTLS");
        secondTls.assertElementExists(false);

        firstTls.addAllButton().click();
        firstTls.rightList().assertText("item 1 labelitem 5 labelitem 3 label");

        element("fn:secondHeader").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        secondTls.assertElementExists();
        secondTls.assertVisible(true);

        secondTls.addAllButton().click();
        secondTls.rightList().assertText("item 1 labelitem 5 labelitem 3 label");
    }

    @Ignore
    @Test
    public void _testValidationInsideAjaxTabbedPane() {
        testAppFunctionalPage("/components/tabbedpane/validationIn.jsf");

        ElementInspector requiredInput = element("fn:required_input");
        requiredInput.assertElementExists();
        requiredInput.assertVisible(true);

        ElementInspector firstMessage = element("fn:first_messageID");
        assertFalse(firstMessage.elementExists() && firstMessage.isVisible());

        requiredInput.setCursorPosition(0);
        requiredInput.keyPress(13);
        firstMessage.assertVisible(true);

        element("fn:secondHeader").click();
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        ElementInspector requiredInput1 = element("fn:required_input1");
        requiredInput1.setCursorPosition(0);
        requiredInput1.keyPress(13);
        element("fn:second_messageID").assertVisible(true);
    }

}