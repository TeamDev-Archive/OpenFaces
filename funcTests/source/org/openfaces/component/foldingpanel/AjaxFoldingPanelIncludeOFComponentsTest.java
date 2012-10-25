/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.foldingpanel;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.DataTableInspector;
import org.seleniuminspector.openfaces.DateChooserInspector;
import org.seleniuminspector.openfaces.FoldingPanelInspector;
import org.seleniuminspector.openfaces.InputTextFilterInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TreeTableInspector;
import org.seleniuminspector.openfaces.TwoListSelectionInspector;

/**
 * @author Darya Shumilina
 */
public class AjaxFoldingPanelIncludeOFComponentsTest extends OpenFacesTestCase {
    @Test
    public void testCalendarInside() {
        testAppFunctionalPage("/components/foldingpanel/calendarIn.jsf");

        ElementInspector calendar = element("fn:calendarID");
        calendar.assertElementExists(false);

        foldingPanel("fn:calendarFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        assertFalse(getSelenium().isAlertPresent());
        calendar.assertElementExists(true);
        if (isAlertPresent()) {
            acceptAlert();
        }
    }

    @Test
    public void testChartInside() {
        testAppFunctionalPage("/components/foldingpanel/chartIn.jsf");

        ElementInspector chart = element("fn:first_chartID");
        chart.assertElementExists(false);

        foldingPanel("fn:chartFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        chart.assertElementExists(true);
        chart.assertVisible(true);
    }

    @Test
    public void testConfirmationInside() {
        testAppFunctionalPage("/components/foldingpanel/confirmationIn.jsf");
        foldingPanel("fn:confirmationFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        for (int i = 0; i < 2; i++) {
            element("button1").click();
            confirmation("fn:conf1").okButton().click();
            assertTrue(isAlertPresent());
            assertEquals("done", getAlert());
        }
        acceptAlert();
    }

    @Test
    public void testDataTableInside() throws InterruptedException {
        testAppFunctionalPage("/components/foldingpanel/dataTableIn.jsf");
        foldingPanel("fn:dataTableFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        DataTableInspector dataTable = dataTable("fn:dataTableID");
        dataTable.makeAndCheckSingleSelection(1, 1);
        dataTable.column(0).makeSorting();
        dataTablePaginator("fn:dataTableID:dataTablePaginator_A4J").makePagination(3);
        dataTable.column(1).filter(InputTextFilterInspector.class, "fn:dataTableID:filter1").makeFiltering("col2_row1");
    }

    @Test
    public void testDateChooserInside() {
        testAppFunctionalPage("/components/foldingpanel/dateChooserIn.jsf");

        foldingPanel("fn:dateChooserFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        DateChooserInspector dateChooser = dateChooser("fn:dateChooserID");
        dateChooser.field().assertElementExists();
        dateChooser.field().assertValue("Jul 30, 2006");
        dateChooser.button().assertElementExists();
    }

    @Test
    public void testDropDownInside() {
        testAppFunctionalPage("/components/foldingpanel/dropDownIn.jsf");

        foldingPanel("fn:dropDownFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        DateChooserInspector dateChooser = dateChooser("fn:dropDownID");
        dateChooser.assertElementExists();
        dateChooser.button().click();
        dateChooser.popup().items().get(1).click();
        dateChooser.field().assertValue("Yellow");
    }

    @Test
    public void testDynamicImageInside() {
        testAppFunctionalPage("/components/foldingpanel/dynamicImageIn.jsf");
        foldingPanel("fn:dynamicImageFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        element("fn:dynamicImageID").assertElementExists();
    }

    @Test
    public void testAjaxFoldingPanelInside() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanelIn.jsf");

        foldingPanel("fn:foldingPanel_FoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        FoldingPanelInspector foldingPanel = foldingPanel("fn:insiderFoldingPanel");
        foldingPanel.caption().assertElementExists();
        foldingPanel.content().assertVisible(false);

        foldingPanel.toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        foldingPanel.caption().assertElementExists();
        foldingPanel.content().assertVisible(true);
    }

    @Test
    public void testHintLabelInside() throws InterruptedException {
        testAppFunctionalPage("/components/foldingpanel/hintLabelIn.jsf");

        foldingPanel("fn:hintLabelFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        hintLabel("fn:hintLabelID").checkVisibilityAndContent("HintLabel Value", "HintLabel Title ;-)");
    }

    @Test
    public void testPopupLayerInside() {
        testAppFunctionalPage("/components/foldingpanel/popupLayerIn.jsf");

        foldingPanel("fn:popupLayerFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        element("fn:header_invoker").click();
        ElementInspector headerPopup = element("fn:header_popup");
        headerPopup.assertVisible(true);
        headerPopup.assertSubtext(0, 28, "this is popup layer on tab 1");
    }

    @Test
    public void testAjaxTabbedPaneInside() {
        testAppFunctionalPage("/components/foldingpanel/tabbedPaneIn.jsf");

        ElementInspector tabbedPane = element("fn:tabbedPaneID");
        tabbedPane.assertElementExists(false);

        foldingPanel("fn:tabbedPaneFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tabbedPane.assertElementExists(true);
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

        ElementInspector tabSet = element("fn:tabSetID");
        tabSet.assertElementExists(false);

        foldingPanel("fn:tabSetFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        tabSet.assertElementExists(true);
        tabSet.assertVisible(true);
        element("fn:firstTab").assertText("Client");
        ElementInspector secondTab = element("fn:secondTab");

        secondTab.click();
        secondTab.assertText("Server");
    }

    //todo: selection test is absent
    @Test
    public void testTreeTableInside() {
        testAppFunctionalPage("/components/foldingpanel/treeTableIn.jsf");

        TreeTableInspector treeTable = treeTable("fn:treeTableID");
        treeTable.assertElementExists(false);
        foldingPanel("fn:treeTableFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        treeTable.assertElementExists(true);
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

        TwoListSelectionInspector twoListSelection = twoListSelection("fn:twoListSelectionID");
        twoListSelection.assertElementExists(false);

        foldingPanel("fn:TLSFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        twoListSelection.assertElementExists(true);
        twoListSelection.assertVisible(true);

        twoListSelection.addAllButton().click();
        twoListSelection.rightList().assertText("item 1 labelitem 5 labelitem 3 label");
    }

    @Test
    public void testValidationInside() {
        testAppFunctionalPage("/components/foldingpanel/validationIn.jsf");

        ElementInspector requiredInput = element("fn:required_input");
        assertFalse(requiredInput.elementExists() && requiredInput.isVisible());
        ElementInspector message = element("fn:validationMessage");
        assertFalse(message.elementExists() && message.isVisible());

        foldingPanel("fn:validationFoldingPanel").toggle().clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        requiredInput.assertVisible(true);
        message.assertVisible(false);

        requiredInput.keyPress(13);

        message.assertVisible(true);
        message.assertContainsText("Value is required.");
    }

}