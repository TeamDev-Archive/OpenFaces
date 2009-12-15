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
public class ClientFoldingPanelIncludeOFComponentsTest extends OpenFacesTestCase {
    @Test
    public void testCalendarInside() {
        testAppFunctionalPage("/components/foldingpanel/calendarIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        ElementInspector calendar = element("fn:calendarID");
        calendar.assertElementExists();
        calendar.assertVisible(false);

        foldingPanel("fn:calendarFoldingPanel").toggle().click();

        calendar.assertElementExists();
        calendar.assertVisible(true);
    }

    @Test
    public void testChartInside() {
        testAppFunctionalPage("/components/foldingpanel/chartIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        ElementInspector chart = element("fn:first_chartID");
        chart.assertElementExists();

        foldingPanel("fn:chartFoldingPanel").toggle().click();

        chart.assertElementExists();
    }

    @Test
    public void testConfirmationInside() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foldingpanel/confirmationIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        ConfirmationInspector confirmationElement = confirmation("fn:conf1");
        confirmationElement.assertElementExists();
        confirmationElement.assertVisible(false);

        foldingPanel("fn:confirmationFoldingPanel").toggle().click();
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
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        DataTableInspector dataTable = dataTable("fn:dataTableID");
        dataTable.assertElementExists(true);
        dataTable.assertVisible(false);

        foldingPanel("fn:dataTableFoldingPanel").toggle().click();
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
            tabSet("fn:loadingModes").tabs().get(1).clickAndWait();
        } catch (Exception e) {
            sleep(30);
            // trying to avoid strange intermittent build failures
            testAppFunctionalPage("/components/foldingpanel/dateChooserIn.jsf");
            tabSet("fn:loadingModes").tabs().get(1).clickAndWait();
        }
        DateChooserInspector dateChooser = dateChooser("fn:dateChooserID");
        dateChooser.assertElementExists();
        dateChooser.assertVisible(false);

        foldingPanel("fn:dateChooserFoldingPanel").toggle().click();
        sleep(500);

        dateChooser.field().assertElementExists();
        dateChooser.field().assertVisible(true);
        dateChooser.button().assertElementExists();
        dateChooser.button().assertVisible(true);
        dateChooser.field().assertValue("Jul 30, 2006");
    }

    @Test
    public void testDropDownInside() {
        testAppFunctionalPage("/components/foldingpanel/dropDownIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        DropDownFieldInspector dropDownField = dropDownField("fn:dropDownID");
        dropDownField.assertElementExists();
        dropDownField.assertVisible(false);

        foldingPanel("fn:dropDownFoldingPanel").toggle().click();

        dropDownField.assertElementExists();
        dropDownField.assertVisible(true);

        dropDownField.popup().items().get(1).click();
        dropDownField.field().assertValue("Yellow");
    }

    @Test
    public void testDynamicImageInside() {
        testAppFunctionalPage("/components/foldingpanel/dynamicImageIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        ElementInspector dynamicImage = element("fn:dynamicImageID");
        dynamicImage.assertElementExists();
        dynamicImage.assertVisible(false);

        foldingPanel("fn:dynamicImageFoldingPanel").toggle().click();

        dynamicImage.assertElementExists();
        dynamicImage.assertVisible(true);
    }

    @Test
    public void testClientFoldingPanelInside() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanelIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());
        FoldingPanelInspector foldingPanel = foldingPanel("fn:insiderFoldingPanel");
        foldingPanel.assertElementExists();
        foldingPanel.assertVisible(false);

        foldingPanel("fn:foldingPanel_FoldingPanel").toggle().click();

        foldingPanel.caption().assertElementExists();
        foldingPanel.content().assertVisible(false);

        foldingPanel.toggle().click();

        foldingPanel.caption().assertElementExists();
        foldingPanel.content().assertVisible(true);
    }

    @Test
    public void testHintLabelInside() throws InterruptedException {
        testAppFunctionalPage("/components/foldingpanel/hintLabelIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        HintLabelInspector hintLabel = hintLabel("fn:hintLabelID");
        hintLabel.assertElementExists();
        hintLabel.assertVisible(false);

        hintLabel.hint().assertElementExists();
        hintLabel.hint().assertVisible(false);

        foldingPanel("fn:hintLabelFoldingPanel").toggle().click();
        hintLabel.checkVisibilityAndContent("HintLabel Value", "HintLabel Title ;-)");
    }

    @Test
    public void testPopupLayerInside() {
        testAppFunctionalPage("/components/foldingpanel/popupLayerIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        ElementInspector headerPopup = element("fn:header_popup");
        headerPopup.assertElementExists();
        headerPopup.assertVisible(false);

        foldingPanel("fn:popupLayerFoldingPanel").toggle().click();
        element("fn:header_invoker").click();

        headerPopup.assertVisible(true);
        headerPopup.assertSubtext(0, 28, "this is popup layer on tab 1");
    }

    @Test
    public void testAjaxTabbedPaneInside() {
        testAppFunctionalPage("/components/foldingpanel/tabbedPaneIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        ElementInspector tabbedPane = element("fn:tabbedPaneID");
        tabbedPane.assertElementExists();
        tabbedPane.assertVisible(false);

        foldingPanel("fn:tabbedPaneFoldingPanel").toggle().click();

        tabbedPane.assertVisible(true);

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
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        ElementInspector tabSet = element("fn:tabSetID");
        tabSet.assertElementExists();
        tabSet.assertVisible(false);

        foldingPanel("fn:tabSetFoldingPanel").toggle().click();
        element("fn:firstTab").assertText("Client");

        ElementInspector secondTab = element("fn:secondTab");
        secondTab.click();
        secondTab.assertText("Server");
    }

    @Test
    public void testTreeTableInside() {
        testAppFunctionalPage("/components/foldingpanel/treeTableIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        TreeTableInspector treeTable = treeTable("fn:treeTableID");
        treeTable.assertElementExists();
        treeTable.assertVisible(false);

        foldingPanel("fn:treeTableFoldingPanel").toggle().click();

        treeTable.assertVisible(true);

        for (int i = 1; i < 4; i++) {
            window().document().getElementsByTagName("img").get(i).clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }

        treeTable.column(0).makeSorting();
        treeTable.column(0).filter(InputTextFilterInspector.class, "fn:treeTableID:filter1").makeFiltering("colors");
    }

    @Test
    public void testTwoListSelectionInside() {
        testAppFunctionalPage("/components/foldingpanel/twoListSelectionIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        final String twoListSelectionLocator = "fn:twoListSelectionID";

        TwoListSelectionInspector twoListSelection = twoListSelection(twoListSelectionLocator);
        twoListSelection.assertElementExists();
        twoListSelection.assertVisible(false);

        foldingPanel("fn:TLSFoldingPanel").toggle().click();

        twoListSelection.assertVisible(true);

        twoListSelection.addAllButton().click();
        twoListSelection.rightList().assertText("item 1 labelitem 5 labelitem 3 label");
    }

    @Test
    public void testValidationInside() {
        testAppFunctionalPage("/components/foldingpanel/validationIn.jsf");
        tabSet("fn:loadingModes").setTabIndex(1, ServerLoadingMode.getInstance());

        ElementInspector requiredInput = element("fn:required_input");
        requiredInput.assertElementExists();
        requiredInput.assertVisible(false);

        ElementInspector message = element("fn:validationMessage");
        message.assertElementExists();
        message.assertVisible(false);

        foldingPanel("fn:validationFoldingPanel").toggle().click();

        requiredInput.assertVisible(true);
        message.assertVisible(false);

        requiredInput.setCursorPosition(0);
        requiredInput.keyPress(13);

        message.assertVisible(true);
        message.assertContainsText("Value is required.");
        element("fn:validationMessage").assertVisible(true);
    }

}