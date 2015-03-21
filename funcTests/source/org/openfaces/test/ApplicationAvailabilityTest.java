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
import org.junit.Test;

/**
 * @author Darya Shumilina
 */
public class ApplicationAvailabilityTest extends BaseSeleniumTest {

     @Test
    public void testAvailability() {
        Selenium selenium = getSelenium();
        selenium.setTimeout("60000");

        assertTestPageAvailable("/main.jsf", "Main");
        assertTestPageAvailable("/components/calendar/calendarTest.jsf", "Calendar Test");
        assertTestPageAvailable("/components/calendar/calendarTest2.jsf", "Calendar Test 2");
        assertTestPageAvailable("/components/chart/demography.jsf", "Demography");
        assertTestPageAvailable("/components/chart/documentation.jsf", "Documentation");
        assertTestPageAvailable("/components/confirmation/confirmationTest.jsf", "Confirmation Test");
        assertTestPageAvailable("/components/confirmation/confirmationTestInvokers.jsf", "Confirmation Test Invokers");
        assertTestPageAvailable("/components/datatable/tableTest.jsf", "Table Test");
        assertTestPageAvailable("/components/datatable/JSFC1359.jsf", "JSFC 1359");
        assertTestPageAvailable("/components/datechooser/dateChooserTest.jsf", "DateChooser Test");
        assertTestPageAvailable("/components/datechooser/dateChooserLocales.jsf", "DateChooser Locales");
        assertTestPageAvailable("/default/defaultViewTest.jsf", "Default View Test");
        assertTestPageAvailable("/doc/screenshot.jsf", "Screenshot");
        assertTestPageAvailable("/components/dropdown/dropDownTest.jsf", "DropDown Test");
        assertTestPageAvailable("/components/dynamicimage/dimg_test.jsf", "dimg_test");
        assertTestPageAvailable("/components/foldingpanel/foldingPanelTest.jsf", "FoldingPanel Test");
        assertTestPageAvailable("/components/foldingpanel/foldingPanelTest2.jsf", "FoldingPanel Test 2");
        assertTestPageAvailable("/compatibility/HTMLInteroperation/InDiv.jsf", "Components in div");
        assertTestPageAvailable("/compatibility/HTMLInteroperation/InTable.jsf", "Components in table");
        assertTestPageAvailable("/components/output/hintLabelTest.jsf", "HintLabel Test");
        assertTestPageAvailable("/components/output/hintLabelTest2.jsf", "HintLabel Test 2");
        assertTestPageAvailable("/components/popuplayer/popupLayerTest.jsf", "PopupLayer Test");
        //todo:uncomment when duplicateComponentId is fixed
//    assertTestPageAvailable("/tabbedpane/tabbedPaneTest.jsf", "TabbedPane Test");
        assertTestPageAvailable("/components/tabset/tabSetTest.jsf", "TabSet Test");
        assertTestPageAvailable("/components/confirmation/confirmation_t.jsf", "Confirmation Testing");
        assertTestPageAvailable("/components/scrollfocus/focusScrollTest_t.jsf", "Focus tracking and scroll position tracking features");
        assertTestPageAvailable("/components/datatable/QKComponentsInsideDataTable.jsf", "All Components Inside DataTable");
        assertTestPageAvailable("/components/treetable/QKComponentsInsideTreeTable.jsf", "All Components Inside TreeTable");
        assertTestPageAvailable("/components/treetable/InTreeTable.jsf", "Components Inside TreeTable");
        assertTestPageAvailable("/components/datatable/InDataTable.jsf", "Components Inside DataTable");
        assertTestPageAvailable("/compatibility/tomahawk/tomahawkInteroperability_t.jsf", "Tomahawk Interaction with QK Container Components");
        assertTestPageAvailable("/compatibility/nonlatin/withNonLatinSymbols_t.jsf", "OpenFaces components with non-latin symbols");
//todo: uncomment when page is fixed
//    assertTestPageAvailable("/tomahawk/withDataTable.jsf", "Tomahawk Components in DataTable");
        assertTestPageAvailable("/compatibility/tomahawk/withFoldingPanel.jsf", "Tomahawk Components in FoldingPanel");
//todo: uncomment when duplicateComponentID is fixed
//    assertTestPageAvailable("/tomahawk/withTabbedPane.jsf", "Tomahawk Components in TabbedPane");
//        assertTestPageAvailable("/compatibility/tomahawk/withTreeTable.jsf", "Tomahawk Components in TreeTable");
        assertTestPageAvailable("/components/treetable/treeTableTest2.jsf", "TreeTable Test 2");
        assertTestPageAvailable("/components/treetable/treeTableTest.jsf", "TreeTable Test");
        assertTestPageAvailable("/components/treetable/treeTableDemo.jsf", "TreeTable Demo");
        assertTestPageAvailable("/components/treetable/newForumMessage.jsf", "New Forum Message");
        assertTestPageAvailable("/components/twolistselection/twoListSelectionTest2.jsf", "TwoListSelection Test 2");
        assertTestPageAvailable("/components/twolistselection/twoListSelectionTest.jsf", "TwoListSelection Test");
        assertTestPageAvailable("/components/validator/dateValidatorTest.jsf", "Date Validator");
        assertTestPageAvailable("/components/validator/demo4.jsf", "demo4");
        assertTestPageAvailable("/components/validator/equalValidatorTest.jsf", "Equal Validator Test");
        assertTestPageAvailable("/components/validator/localization.jsf", "Localization");
        assertTestPageAvailable("/components/validator/messagesTest.jsf", "Messages Test");
        assertTestPageAvailable("/components/validator/tableValidatorTest.jsf", "Table Validator Test");
        assertTestPageAvailable("/components/validator/validatorTest.jsf", "Validator Test");
        assertTestPageAvailable("/error.jsf", "Error page");
        assertTestPageAvailable("/testPageIndex.jsf", "Components Test");
        assertTestPageAvailable("/components/treetable/treeTableAjax.jsf", "TreeTable with Ajax");
        assertTestPageAvailable("/compatibility/richfaces/QKValidation/Validators_custom.jsf", "Custom Validators");
        assertTestPageAvailable("/compatibility/richfaces/QKValidation/Validators_standard.jsf", "Standard Validators");
        assertTestPageAvailable("/compatibility/richfaces/QKValidation/ValidatorsInOneFormStandart.jsf", "Validators in one form (standard)");
        assertTestPageAvailable("/compatibility/richfaces/QKValidation/ValidatorsInOneFormCustom.jsf", "Validators in one form (custom)");
        assertTestPageAvailable("/components/validation/validationMessages.jsf", "Validation messages");
        assertTestPageAvailable("/components/treetable/calendarIn.jsf", "Calendar Inside TreeTable");
        assertTestPageAvailable("/components/treetable/confirmationIn.jsf", "Confirmation Inside TreeTable");
        assertTestPageAvailable("/components/treetable/dataTableIn.jsf", "DataTable Inside TreeTable");
        assertTestPageAvailable("/components/treetable/dateChooserIn.jsf", "DateChooser Inside TreeTable");
        assertTestPageAvailable("/components/treetable/dropDownIn.jsf", "DropDown Inside TreeTable");
        assertTestPageAvailable("/components/treetable/dynamicImageIn.jsf", "DynamicImage Inside TreeTable");
        assertTestPageAvailable("/components/treetable/foldingPanelIn.jsf", "FoldingPanel Inside TreeTable");
        assertTestPageAvailable("/components/treetable/hintLabelIn.jsf", "HintLabel Inside TreeTable");
        assertTestPageAvailable("/components/treetable/popupLayerIn.jsf", "PopupLayer Inside TreeTable");
        assertTestPageAvailable("/components/treetable/tabbedPaneIn.jsf", "TabbedPane Inside TreeTable");
        assertTestPageAvailable("/components/treetable/tabSetIn.jsf", "TabSet Inside TreeTable");
        assertTestPageAvailable("/components/treetable/treeTableIn.jsf", "TreeTable Inside TreeTable");
        assertTestPageAvailable("/components/treetable/twoListSelectionIn.jsf", "TLS Inside TreeTable");
        assertTestPageAvailable("/components/treetable/validationIn.jsf", "Validation Inside TreeTable");
        assertTestPageAvailable("/components/timetable/dayTable.jsf", "DayTable Basics");
        assertTestPageAvailable("/components/window/window.jsf", "Window Basics");
        assertTestPageAvailable("/components/graphictext/graphicText.jsf", "GraphicText Test");
        assertTestPageAvailable("/components/graphictext/graphicTextStyles.jsf", "Styled GraphicTexts");
    }
}