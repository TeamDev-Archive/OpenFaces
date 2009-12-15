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
package org.openfaces.component.foldingpanel;

import com.thoughtworks.selenium.Selenium;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.ServerLoadingMode;
import org.seleniuminspector.openfaces.*;

/**
 * @author Darya Shumilina
 */
public class ServerFoldingPanelIncludeOFComponentsTest extends OpenFacesTestCase {
    @Test
    public void testCalendarInside() {
        testAppFunctionalPage("/components/foldingpanel/calendarIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        ElementInspector calendar = element("fn:calendarID");
        calendar.assertElementExists(false);
        foldingPanel("fn:calendarFoldingPanel").toggle().clickAndWait();
        calendar.assertElementExists(true);
        calendar.assertVisible(true);
    }

    @Test
    public void testChartInside() {
        testAppFunctionalPage("/components/foldingpanel/chartIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        ElementInspector chart = element("fn:first_chartID");
        chart.assertElementExists(false);
        foldingPanel("fn:chartFoldingPanel").toggle().clickAndWait();
        chart.assertElementExists(true);
        chart.assertVisible(true);
    }

    @Test
    public void testConfirmationInside() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foldingpanel/confirmationIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        ConfirmationInspector confirmationElement = confirmation("fn:conf1");
        confirmationElement.assertElementExists(false);
        foldingPanel("fn:confirmationFoldingPanel").toggle().clickAndWait();
        for (int i = 0; i < 2; i++) {
            element("button1").click();
            confirmationElement.assertVisible(true);
            confirmationElement.okButton().click();
            assertTrue(selenium.isAlertPresent());
            assertEquals("done", selenium.getAlert());
            confirmationElement.assertVisible(false);
        }
    }

    @Test
    public void testDataTableInside() {
        testAppFunctionalPage("/components/foldingpanel/dataTableIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        DataTableInspector dataTable = dataTable("fn:dataTableID");
        dataTable.assertElementExists(false);
        foldingPanel("fn:dataTableFoldingPanel").toggle().clickAndWait();
        dataTable.assertVisible(true);
        dataTable.makeAndCheckSingleSelection(1, 1);
        dataTable.column(0).makeSorting();
        dataTablePaginator("fn:dataTableID:dataTablePaginator_A4J").makePagination(3);
        dataTable.column(1).filter(InputTextFilterInspector.class, "fn:dataTableID:filter1").makeFiltering("col2_row1");
    }

    @Test
    public void testDateChooserInside() {
        testAppFunctionalPage("/components/foldingpanel/dateChooserIn.jsf");
        try {
            tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        } catch (Exception e) {
            // trying to avoid strange intermittent build failures
            testAppFunctionalPage("/components/foldingpanel/dateChooserIn.jsf");
            tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        }

        DateChooserInspector dateChooser = dateChooser("fn:dateChooserID");
        dateChooser.assertElementExists(false);

        foldingPanel("fn:dateChooserFoldingPanel").toggle().clickAndWait();
        dateChooser.button().assertElementExists(true);
        dateChooser.button().assertVisible(true);
        dateChooser.field().assertElementExists(true);
        dateChooser.field().assertVisible(true);
        dateChooser.field().assertValue("Jul 30, 2006");
    }

    @Test
    public void testDropDownInside() {
        testAppFunctionalPage("/components/foldingpanel/dropDownIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        DropDownFieldInspector dropDownField = dropDownField("fn:dropDownID");
        dropDownField.assertElementExists(false);

        foldingPanel("fn:dropDownFoldingPanel").toggle().clickAndWait();
        dropDownField.assertElementExists(true);
        dropDownField.assertVisible(true);
        dropDownField.popup().items().get(1).click();
        dropDownField.field().assertValue("Yellow");
    }

    @Test
    public void testDynamicImageInside() {
        testAppFunctionalPage("/components/foldingpanel/dynamicImageIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        ElementInspector dynamicImage = element("fn:dynamicImageID");
        dynamicImage.assertElementExists(false);
        foldingPanel("fn:dynamicImageFoldingPanel").toggle().clickAndWait();
        dynamicImage.assertElementExists(true);
        dynamicImage.assertVisible(true);
    }

    @Test
    public void testServerFoldingPanelInside() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanelIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        FoldingPanelInspector foldingPanel = foldingPanel("fn:insiderFoldingPanel");
        foldingPanel.assertElementExists(false);
        foldingPanel("fn:foldingPanel_FoldingPanel").toggle().clickAndWait();
        foldingPanel.caption().assertVisible(true);
        foldingPanel.content().assertElementExists();
        foldingPanel.content().assertVisible(false);
        foldingPanel.toggle().clickAndWait();
        foldingPanel.caption().assertVisible(true);
        foldingPanel.content().assertVisible(true);
    }

    @Test
    public void testHintLabelInside() throws InterruptedException {
        testAppFunctionalPage("/components/foldingpanel/hintLabelIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        HintLabelInspector hintLabel = hintLabel("fn:hintLabelID");
        hintLabel.assertElementExists(false);

        foldingPanel("fn:hintLabelFoldingPanel").toggle().clickAndWait();
        hintLabel.checkVisibilityAndContent("HintLabel Value", "HintLabel Title ;-)");
    }

    @Test
    public void testPopupLayerInside() {
        testAppFunctionalPage("/components/foldingpanel/popupLayerIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        ElementInspector headerPopup = element("fn:header_popup");
        headerPopup.assertElementExists(false);

        foldingPanel("fn:popupLayerFoldingPanel").toggle().clickAndWait();

        element("fn:header_invoker").click();
        headerPopup.assertVisible(true);
        headerPopup.assertSubtext(0, 28, "this is popup layer on tab 1");
    }

    @Test
    public void testAjaxTabbedPaneInside() {
        testAppFunctionalPage("/components/foldingpanel/tabbedPaneIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        element("fn:tabbedPaneID").assertElementExists(false);

        foldingPanel("fn:tabbedPaneFoldingPanel").toggle().clickAndWait();
        element("fn:firstHeader").assertText("First tab");
        element("fn:firstContent").assertText("Some text on the first tab");
        ElementInspector secondHeader = element("fn:secondHeader");
        secondHeader.assertText("Second tab");

        secondHeader.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        element("fn:secondContent").assertText("Some text on the second tab");
    }

    @Test
    public void testTabSetInside() {
        testAppFunctionalPage("/components/foldingpanel/tabSetIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        ElementInspector tabSet = element("fn:tabSetID");
        tabSet.assertElementExists(false);

        foldingPanel("fn:tabSetFoldingPanel").toggle().clickAndWait();
        tabSet.assertElementExists(true);
        tabSet.assertVisible(true);
        element("fn:firstTab").assertText("Client");
        ElementInspector secondTab = element("fn:secondTab");

        secondTab.click();
        secondTab.assertText("Server");
    }

    @Test
    public void testTreeTableInside() {
        testAppFunctionalPage("/components/foldingpanel/treeTableIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        TreeTableInspector treeTable = treeTable("fn:treeTableID");
        treeTable.assertElementExists(false);
        foldingPanel("fn:treeTableFoldingPanel").toggle().clickAndWait();
        treeTable.assertElementExists();
        treeTable.assertVisible(true);
        for (int i = 1; i < 4; i++) {
            window().document().getElementsByTagName("img").get(i)
                    .clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }
        treeTable.column(0).makeSorting();
        treeTable.column(0).filter(InputTextFilterInspector.class, "fn:treeTableID:filter1").makeFiltering("colors");
    }

    @Test
    public void testTwoListSelectionInside() {
        testAppFunctionalPage("/components/foldingpanel/twoListSelectionIn.jsf");

        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        TwoListSelectionInspector twoListSelection = twoListSelection("fn:twoListSelectionID");
        twoListSelection.assertElementExists(false);

        foldingPanel("fn:TLSFoldingPanel").toggle().clickAndWait();
        twoListSelection.assertElementExists(true);
        twoListSelection.assertVisible(true);

        twoListSelection.addAllButton().click();
        twoListSelection.rightList().assertText("item 1 labelitem 5 labelitem 3 label");
    }

    @Test
    public void testValidationInside() {
        testAppFunctionalPage("/components/foldingpanel/validationIn.jsf");

        ElementInspector requiredInput = element("fn:required_input");
        requiredInput.assertElementExists(false);
        ElementInspector message = element("fn:validationMessage");
        message.assertElementExists(false);
        tabSet("fn:loadingModes").setTabIndex(2, ServerLoadingMode.getInstance());
        assertFalse(requiredInput.elementExists() && requiredInput.isVisible());
        assertFalse(message.elementExists() && message.isVisible());

        foldingPanel("fn:validationFoldingPanel").toggle().clickAndWait();
        requiredInput.assertVisible(true);
        message.assertElementExists(true);
        message.assertVisible(false);

        requiredInput.setCursorPosition(0);
        requiredInput.keyPress(13);
        message.assertVisible(true);
        assertTrue(message.text().contains("Validation Error: Value is required.") ||
                message.text().contains("required_input: Value is required."));
    }

}