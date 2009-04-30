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
import org.openfaces.test.ElementInspector;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.openfaces.DataTableInspector;
import org.openfaces.test.openfaces.DateChooserInspector;
import org.openfaces.test.openfaces.FoldingPanelInspector;
import org.openfaces.test.openfaces.LoadingMode;
import org.openfaces.test.openfaces.SearchFieldFilterInspector;
import org.openfaces.test.openfaces.TreeTableInspector;
import org.openfaces.test.openfaces.TwoListSelectionInspector;

/**
 * @author Darya Shumilina
 */
public class AjaxFoldingPanelIncludeQKComponentsTest extends OpenFacesTestCase {
    @Test
    public void testCalendarInside() {
        testAppFunctionalPage("/components/foldingpanel/calendarIn.jsf");

        ElementInspector calendar = element("fn:calendarID");
        calendar.assertElementExists(false);

        foldingPanel("fn:calendarFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
        assertFalse(getSelenium().isAlertPresent());
        calendar.assertElementExists(true);
    }

    @Test
    public void testChartInside() {
        testAppFunctionalPage("/components/foldingpanel/chartIn.jsf");

        ElementInspector chart = element("fn:first_chartID");
        chart.assertElementExists(false);

        foldingPanel("fn:chartFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
        chart.assertElementExists(true);
        chart.assertVisible(true);
    }

    @Test
    public void testConfirmationInside() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/foldingpanel/confirmationIn.jsf");
        foldingPanel("fn:confirmationFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
        for (int i = 0; i < 2; i++) {
            element("button1").click();
            confirmation("fn:conf1").okButton().click();
            assertTrue(selenium.isAlertPresent());
            assertEquals("done", selenium.getAlert());
        }
    }

    @Test
    public void testDataTableInside() throws InterruptedException {
        testAppFunctionalPage("/components/foldingpanel/dataTableIn.jsf");
        foldingPanel("fn:dataTableFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);

        DataTableInspector dataTable = dataTable("fn:dataTableID");
        dataTable.makeAndCheckSingleSelection(1, 1);
        dataTable.column(0).makeSorting();
        dataTablePaginator("fn:dataTableID:dataTablePaginator_A4J").makePagination(3);
        dataTable.column(1).filter(SearchFieldFilterInspector.class).makeFiltering("col2_row1");
    }

    @Test
    public void testDateChooserInside() {
        testAppFunctionalPage("/components/foldingpanel/dateChooserIn.jsf");

        foldingPanel("fn:dateChooserFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);

        DateChooserInspector dateChooser = dateChooser("fn:dateChooserID");
        dateChooser.field().assertElementExists();
        dateChooser.field().assertValue("Jul 30, 2006");
        dateChooser.button().assertElementExists();
    }

    @Test
    public void testDropDownInside() {
        testAppFunctionalPage("/components/foldingpanel/dropDownIn.jsf");

        foldingPanel("fn:dropDownFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);

        DateChooserInspector dateChooser = dateChooser("fn:dropDownID");
        dateChooser.assertElementExists();
        dateChooser.popup().items().get(1).click();
        dateChooser.field().assertValue("Yellow");
    }

    @Test
    public void testDynamicImageInside() {
        testAppFunctionalPage("/components/foldingpanel/dynamicImageIn.jsf");
        foldingPanel("fn:dynamicImageFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
        element("fn:dynamicImageID").assertElementExists();
    }

    @Test
    public void testAjaxFoldingPanelInside() {
        testAppFunctionalPage("/components/foldingpanel/foldingPanelIn.jsf");

        foldingPanel("fn:foldingPanel_FoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
        FoldingPanelInspector foldingPanel = foldingPanel("fn:insiderFoldingPanel");
        foldingPanel.caption().assertElementExists();
        foldingPanel.content().assertVisible(false);

        foldingPanel.toggle().clickAndWait(LoadingMode.AJAX);
        foldingPanel.caption().assertElementExists();
        foldingPanel.content().assertVisible(true);
    }

    @Test
    public void testHintLabelInside() throws InterruptedException {
        testAppFunctionalPage("/components/foldingpanel/hintLabelIn.jsf");

        foldingPanel("fn:hintLabelFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
        hintLabel("fn:hintLabelID").checkVisibilityAndContent("HintLabel Value", "HintLabel Title ;-)");
    }

    @Test
    public void testPopupLayerInside() {
        testAppFunctionalPage("/components/foldingpanel/popupLayerIn.jsf");

        foldingPanel("fn:popupLayerFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);

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

        foldingPanel("fn:tabbedPaneFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
        tabbedPane.assertElementExists(true);
        tabbedPane.assertVisible(true);
        element("fn:firstHeader").assertText("First tab");
        element("fn:firstContent").assertText("Some text on the first tab");
        ElementInspector secondHeader = element("fn:secondHeader");
        secondHeader.assertText("Second tab");

        secondHeader.clickAndWait(LoadingMode.AJAX);
        element("fn:secondContent").assertText("Some text on the second tab");
    }

    @Test
    public void testTabSetInside() {
        testAppFunctionalPage("/components/foldingpanel/tabSetIn.jsf");

        ElementInspector tabSet = element("fn:tabSetID");
        tabSet.assertElementExists(false);

        foldingPanel("fn:tabSetFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
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
        foldingPanel("fn:treeTableFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);

        treeTable.assertElementExists(true);
        treeTable.assertVisible(true);

        for (int i = 1; i < 4; i++) {
            window().document().getElementsByTagName("img").get(i).clickAndWait(LoadingMode.AJAX);
        }

        treeTable.column(0).makeSorting();
        treeTable.column(0).filter(SearchFieldFilterInspector.class).makeFiltering("colors");
    }

    @Test
    public void testTwoListSelectionInside() {
        testAppFunctionalPage("/components/foldingpanel/twoListSelectionIn.jsf");

        TwoListSelectionInspector twoListSelection = twoListSelection("fn:twoListSelectionID");
        twoListSelection.assertElementExists(false);

        foldingPanel("fn:TLSFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
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

        foldingPanel("fn:validationFoldingPanel").toggle().clickAndWait(LoadingMode.AJAX);
        requiredInput.assertVisible(true);
        message.assertVisible(false);

        requiredInput.setCursorPosition(0);
        requiredInput.keyPress(13);

        message.assertVisible(true);
        message.assertContainsText("Value is required.");
    }

}