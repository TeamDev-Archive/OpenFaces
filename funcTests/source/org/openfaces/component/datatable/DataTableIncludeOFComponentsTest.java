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
package org.openfaces.component.datatable;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.openfaces.DataTableInspector;
import org.seleniuminspector.openfaces.HintLabelInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.InputTextFilterInspector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class DataTableIncludeOFComponentsTest extends OpenFacesTestCase {

    /*
      public void testCalendarInsideDataTable() {
        Selenium selenium = getSelenium();
        testAppPage("/functionalTesting/datatable/calendarIn.jsf");
      }

      public void testConfirmationInsideDataTable() {
        Selenium selenium = getSelenium();
        testAppPage("/functionalTesting/datatable/confirmationIn.jsf");
      }

      public void testDataTableInsideDataTable() {
        Selenium selenium = getSelenium();
        testAppPage("/functionalTesting/datatable/dataTableIn.jsf");
      }

      public void testDateChooserInsideDataTable() {
        Selenium selenium = getSelenium();
        testAppPage("/functionalTesting/datatable/dateChooserIn.jsf");
      }

      public void testDropDownInsideDataTable() {
        Selenium selenium = getSelenium();
        testAppPage("/functionalTesting/datatable/dropDownIn.jsf");
      }

      public void testDynamicImageInsideDataTable() {
        Selenium selenium = getSelenium();
        testAppPage("/functionalTesting/datatable/dynamicImageIn.jsf");
      }

      public void testFoldingPanelInsideDataTable() {
        Selenium selenium = getSelenium();
        testAppPage("/functionalTesting/datatable/foldingPanelIn.jsf");
      }
    */
    @Test
    public void testHintLabelInside() {
        testAppFunctionalPage("/components/datatable/hintLabelIn.jsf");

        //create and fill list of the reference hintLabel values
        List<DataTableUtils.TestDataTableItem> hintLabelDataTableValues = new ArrayList<DataTableUtils.TestDataTableItem>(DataTableUtils.TWO_STRING_COLUMN_LIST);

        /* sorting, pagination, single selection */

        //check is hinLabel in first column header visible
        DataTableInspector hintLabelDataTable = dataTable("fn:hintLabelDataTable");
        HintLabelInspector headerHintLabel1 = hintLabel("fn:hintLabelDataTable:header_hintLabel_1");
        headerHintLabel1.checkVisibilityAndContent("Header hint label value 1", "Header hint label title 1");
        hintLabel("fn:hintLabelDataTable:header_hintLabel_2")
                .checkVisibilityAndContent("Header hint label value 2", "Header hint label title 2");

        // todo: check header cells when iterating over each page

        //click at hinlLabel placed in the header to perform sorting

//    dataTableColumn(headerHintLabel1.asSeleniumLocator()).makeSorting(OpenFacesAjaxLoadingMode.getInstance());
        hintLabelDataTable.column(0).makeSorting();


        for (int pageNo = 1; pageNo <= 2; pageNo++) {
            //execute pagination from first to third page verify data on every page
            List<DataTableUtils.TestDataTableItem> currentPageValues = DataTableUtils.getCurrentPageFromReferenceValues(hintLabelDataTableValues, pageNo);
            for (int rowIndex = 0; rowIndex < 3; rowIndex++) {
                DataTableUtils.TestDataTableItem currentReferenceRow = currentPageValues.get(rowIndex);

                HintLabelInspector bodyHintLabel1 = hintLabel("fn:hintLabelDataTable:" + rowIndex + ":body_hinLabel_1");
                bodyHintLabel1.click();
                bodyHintLabel1.checkVisibilityAndContent(currentReferenceRow.getFirstColumn(), currentReferenceRow.getSecondColumn());

                hintLabel("fn:hintLabelDataTable:" + rowIndex + ":body_hinLabel_2")
                        .checkVisibilityAndContent(currentReferenceRow.getSecondColumn(), currentReferenceRow.getFirstColumn());

                // check is single selection performed well
                hintLabelDataTable.checkSelectedIndex(rowIndex);
            }
            // check footer hintLabels
            hintLabel("fn:hintLabelDataTable:footer_hintLabel_1")
                    .checkVisibilityAndContent("Footer hint label value 1", "Footer hint label title 1");
            hintLabel("fn:hintLabelDataTable:footer_hintLabel_2")
                    .checkVisibilityAndContent("Footer hint label value 2", "Footer hint label title 2");

            dataTablePaginator("fn:hintLabelDataTable:hintLabelDataTablePaginator").nextPage()
                    .clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }

        /* filtering and filtered data verification */
        final String filterCriterion = "col4_row1";
        hintLabelDataTable.column(2).filter(InputTextFilterInspector.class, "fn:hintLabelDataTable:filter1").makeFiltering(filterCriterion);
        HintLabelInspector hintLabel = hintLabel("fn:hintLabelDataTable:0:body_hinLabel_2");
        hintLabel.mouseOver();
        sleep(250);
        hintLabel.assertText(filterCriterion);
        hintLabel.hint().assertText("col3_row1");
    }

    @Test
    public void testPopupLayerInside() {
        testAppFunctionalPage("/components/datatable/popupLayerIn.jsf");

        //create and fill list of the reference popupLayer values
        List<DataTableUtils.TestDataTableItem> popupLayerDataTableValues = new ArrayList<DataTableUtils.TestDataTableItem>(DataTableUtils.TWO_STRING_COLUMN_LIST);

        /* sorting, pagination, single selection */
        ElementInspector nextPage = dataTablePaginator("fn:popupDataTable:popupDataTablePaginator").nextPage();
        int rowCount = 3;
        int pageCount = 3;

        element("fn:popupDataTable:header_invoker").click();
        element("fn:popupDataTable:header_popup").assertVisible(true);
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        for (int pageNo = 1; pageNo <= pageCount; pageNo++) {
            // execute pagination from first to third page and verify data on every page
            List<DataTableUtils.TestDataTableItem> currentPageValues = DataTableUtils.getCurrentPageFromReferenceValues(popupLayerDataTableValues, pageNo);

            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                DataTableUtils.TestDataTableItem currentReferenceRow = currentPageValues.get(rowIndex);

                // invoke popupLayers in current row
                element("fn:popupDataTable:" + rowIndex + ":body_invoker").click();
                element("fn:popupDataTable:" + rowIndex + ":body_invoker1").click();

                //check is single selection performed well
                dataTable("fn:popupDataTable").checkSelectedIndex(rowIndex);

                //check: is popupLayers are visible
                ElementInspector bodyPopup = element("fn:popupDataTable:" + rowIndex + ":body_popup");
                bodyPopup.assertVisible(true);
                ElementInspector bodyPopup1 = element("fn:popupDataTable:" + rowIndex + ":body_popup1");
                bodyPopup1.assertVisible(true);

                //get text from the invoked popupLayers
                String currentFirstCellValue = bodyPopup.text().substring(0, 9);
                String currentSecondCellValue = bodyPopup1.text().substring(0, 9);

                // compare received values with their reference values
                assertEquals(currentReferenceRow.getFirstColumn(), currentFirstCellValue);
                assertEquals(currentReferenceRow.getSecondColumn(), currentSecondCellValue);
            }

            // invoke popupLayers from the footer in current page and check data in it
            element("fn:popupDataTable:footer_invoker").click();
            element("fn:popupDataTable:footer_popup").assertSubtext(0, 29, "this is footer popup layer 1!");
            element("fn:popupDataTable:footer_invoker1").click();
            element("fn:popupDataTable:footer_popup1").assertSubtext(0, 29, "this is footer popup layer 2!");

            if (pageNo < pageCount)
                nextPage.clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        }
        // a little sleep to wait until all visible popupLayers will be hidden
        sleep(4000);
        /* filtering */
        final String filterCriterion = "col4_row1";
        dataTable("fn:popupDataTable").column(2).filter(InputTextFilterInspector.class, "fn:popupDataTable:filter1").makeFiltering(filterCriterion);
        element("fn:popupDataTable:0:body_invoker1").click();
        element("fn:popupDataTable:0:body_popup1").assertSubtext(0, 9, filterCriterion);
    }

/*
  public void testTabbedPaneInsideDataTable() {
    Selenium selenium = getSelenium();
    testAppPage("/functionalTesting/datatable/tabbedPaneIn.jsf");
  }

  public void testTabSetInsideDataTable() {
    Selenium selenium = getSelenium();
    testAppPage("/functionalTesting/datatable/tabSetIn.jsf");
  }

  public void testTreeTableInsideDataTable() {
    Selenium selenium = getSelenium();
    testAppPage("/functionalTesting/datatable/treeTableIn.jsf");
  }

  public void testTLSInsideDataTable() {
    Selenium selenium = getSelenium();
    testAppPage("/functionalTesting/datatable/twoListSelectionIn.jsf");
  }

  public void testValidationInsideDataTable() {
    Selenium selenium = getSelenium();
    testAppPage("/functionalTesting/datatable/validationIn.jsf");
  }

*/
}