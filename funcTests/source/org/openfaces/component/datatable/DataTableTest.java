/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.datatable;

import com.thoughtworks.selenium.Selenium;
import org.junit.Ignore;
import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.openfaces.test.RichFacesAjaxLoadingMode;
import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.ServerLoadingMode;
import org.seleniuminspector.html.TableCellParams;
import org.seleniuminspector.html.TableInspector;
import org.seleniuminspector.html.TableRowInspector;
import org.seleniuminspector.html.TableSectionInspector;
import org.seleniuminspector.openfaces.ComboBoxFilterInspector;
import org.seleniuminspector.openfaces.DataTableInspector;
import org.seleniuminspector.openfaces.DataTablePaginatorInspector;
import org.seleniuminspector.openfaces.DropDownFieldFilterInspector;
import org.seleniuminspector.openfaces.InputTextFilterInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TabSetInspector;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Darya Shumilina
 */
public class DataTableTest extends OpenFacesTestCase {
    private static final String NBSP_CHAR = "\u00a0";

    //todo: see JSFC-3080 issue
    @Ignore
    @Test
    public void _testDataTableReRenderThroughA4J() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/dataTable_a4j.jsf");
        selenium.setCursorPosition("id=formID:dataTableID:dataTablePaginator_A4J--pageNo", "1");
        selenium.keyPress("formID:dataTableID:dataTablePaginator_A4J--pageNo", "\\8");
        selenium.type("formID:dataTableID:dataTablePaginator_A4J--pageNo", "1");
        selenium.keyPress("formID:dataTableID:dataTablePaginator_A4J--pageNo", "\\13");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        String[] oldTableValues = new String[36];
        int j = 0;
        int n = 1;
        for (int i = 0; i < oldTableValues.length; i++) {
            String currentID = "formID:dataTableID:" + j + ":col" + n + "_content";
            oldTableValues[i] = selenium.getText(currentID);
            if (j != 2) j++;
            else {
                j = 0;
                n++;
            }
            if (i == 11 || i == 23) {
                j = 0;
                n = 1;
                selenium.setCursorPosition("id=formID:dataTableID:dataTablePaginator_A4J--pageNo", "1");
                selenium.keyPress("formID:dataTableID:dataTablePaginator_A4J--pageNo", "\\8");
                if (i == 11) selenium.type("formID:dataTableID:dataTablePaginator_A4J--pageNo", "2");
                if (i == 23) selenium.type("formID:dataTableID:dataTablePaginator_A4J--pageNo", "3");
                OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
                selenium.keyPress("formID:dataTableID:dataTablePaginator_A4J--pageNo", "\\13");
            }
        }
        selenium.click("formID:refresher");
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        selenium.setCursorPosition("id=formID:dataTableID:dataTablePaginator_A4J--pageNo", "1");
        selenium.keyPress("formID:dataTableID:dataTablePaginator_A4J--pageNo", "\\8");
        selenium.type("formID:dataTableID:dataTablePaginator_A4J--pageNo", "1");
        selenium.keyPress("formID:dataTableID:dataTablePaginator_A4J--pageNo", "\\13");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        String[] newTableValues = new String[36];
        int k = 0;
        int m = 1;
        for (int i = 0; i < newTableValues.length; i++) {
            String currentID = "formID:dataTableID:" + k + ":col" + m + "_content";
            newTableValues[i] = selenium.getText(currentID);
            if (k != 2) k++;
            else {
                k = 0;
                m++;
            }
            if (i == 11 || i == 23) {
                k = 0;
                m = 1;
                selenium.setCursorPosition("id=formID:dataTableID:dataTablePaginator_A4J--pageNo", "1");
                selenium.keyPress("formID:dataTableID:dataTablePaginator_A4J--pageNo", "\\8");
                if (i == 11) selenium.type("formID:dataTableID:dataTablePaginator_A4J--pageNo", "2");
                if (i == 23) selenium.type("formID:dataTableID:dataTablePaginator_A4J--pageNo", "3");
                selenium.keyPress("formID:dataTableID:dataTablePaginator_A4J--pageNo", "\\13");
                assertNoAlert("No alert expected, but was: ");
                OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
                assertNoAlert("No alerts are expected when performing ajax request, but the following alert appeared: ");
            }
        }

        for (int l = 0; l < newTableValues.length; l++) {
            assertFalse(newTableValues[l].equals(oldTableValues[l]));
        }
    }

    //todo: see JSFC-3080 issue
    @Ignore
    @Test
    public void _testDataTableWithA4JInside() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/dataTable_a4j.jsf");
        selenium.setCursorPosition("id=formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "1");
        selenium.keyPress("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "\\8");
        selenium.type("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "1");
        selenium.keyPress("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "\\13");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        String[] oldTableValues = new String[36];
        int j = 0;
        int n = 1;
        for (int i = 0; i < oldTableValues.length; i++) {
            String currentID = "formID:dataTable_a4j_ID:" + j + ":col" + n + "_content_a4j";
            oldTableValues[i] = selenium.getText(currentID);
            if (j != 2) j++;
            else {
                j = 0;
                n++;
            }
            if (i == 11 || i == 23) {
                j = 0;
                n = 1;
                selenium.setCursorPosition("id=formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "1");
                selenium.keyPress("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "\\8");
                if (i == 11) selenium.type("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "2");
                if (i == 23) selenium.type("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "3");
                OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
                selenium.keyPress("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "\\13");
            }
        }
        selenium.click("formID:refresher");
        RichFacesAjaxLoadingMode.getInstance().waitForLoad();
        selenium.setCursorPosition("id=formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "1");
        selenium.keyPress("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "\\8");
        selenium.type("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "1");
        selenium.keyPress("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "\\13");
        OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        String[] newTableValues = new String[36];
        int k = 0;
        int m = 1;
        for (int i = 0; i < newTableValues.length; i++) {
            String currentID = "formID:dataTable_a4j_ID:" + k + ":col" + m + "_content_a4j";
            newTableValues[i] = selenium.getText(currentID);
            if (k != 2) k++;
            else {
                k = 0;
                m++;
            }
            if (i == 11 || i == 23) {
                k = 0;
                m = 1;
                selenium.setCursorPosition("id=formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "1");
                selenium.keyPress("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "\\8");
                if (i == 11) selenium.type("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "2");
                if (i == 23) selenium.type("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "3");
                selenium.keyPress("formID:dataTable_a4j_ID:dataTable_a4j_IDPaginator_A4J--pageNo", "\\13");
                OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
            }
        }

        for (int l = 0; l < newTableValues.length; l++) {
            assertTrue(!newTableValues[l].equals(oldTableValues[l]));
        }
    }

    @Test
    public void testPagination() {
        pagination(OpenFacesAjaxLoadingMode.getInstance());
        pagination(ServerLoadingMode.getInstance());
    }

    @Test
    public void testColumnGroups_headers() {
        testAppFunctionalPage("/components/datatable/dataTableColumnGroups.jsf");
        TableInspector table = dataTable("formID:twoHeadersTable");

        TableSectionInspector header = table.header();
        TableRowInspector firstHeaderRow = header.row(0);
        firstHeaderRow.assertCellParams(new TableCellParams[]{
                new TableCellParams(NBSP_CHAR, 1, 2,
                        "border-right: 1px solid #a0a0a0; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("String Fields", 4, 1,
                        "background: SteelBlue; border-right: 1px solid #a0a0a0; border-bottom: ? none ?"),
                new TableCellParams("Integer Fields", 3, 1,
                        "background: ForestGreen; border-right: ? none ?; border-bottom: ? none ?")
        });
        TableRowInspector secondHeaderRow = header.row(1);
        secondHeaderRow.assertCellParams(new TableCellParams[]{
                new TableCellParams(null, 1, 1,
                        "background: LightSteelBlue; border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("String Field 1", 1, 1,
                        "background: LightSteelBlue; border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("String Field 2", 1, 1,
                        "background: LightSteelBlue; border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("String Field 3", 1, 1,
                        "background: LightSteelBlue; border-right: 1px solid #a0a0a0; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("Int Field 1", 1, 1,
                        "background: LawnGreen; border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("Int Field 2", 1, 1,
                        "background: LawnGreen; border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("Int Field 3", 1, 1,
                        "background: LawnGreen; border-right: ? none ?; border-bottom: 1px solid #a0a0a0")
        });

        TableSectionInspector body = table.body();
        assertEquals("Checking body row count", 10, body.rowCount());
        for (int i = 0, count = body.rowCount(); i < count - 1; i++) {
            body.row(i).assertCellStyles("border-bottom: 1px solid #e0e0e0");
        }

        body.assertColumnCellStyles(new String[]{
                "border-right: 1px solid #e0e0e0",
                "border-right: ? none ?",
                "border-right: ? none ?",
                "border-right: ? none ?",
                "border-right: 1px solid #e0e0e0",
                "border-right: ? none ?",
                "border-right: ? none ?",
                "border-right: ? none ?"});

        table.footer().assertElementExists(false);
    }

    @Test
    public void testColumnGroups_styles() {
        testAppFunctionalPage("/components/datatable/dataTableColumnGroups.jsf");
        tabbedPane("formID:testSelector").setPageIndex(1, ServerLoadingMode.getInstance());

        TableInspector table = dataTable("formID:groupStylesTable");
        assertEquals("There should be two header rows", 2, table.header().rowCount());
        assertEquals("There should be 10 body rows", 10, table.body().rowCount());
        assertFalse("There should be no footer", table.footer().elementExists());

        table.header().row(0).assertCellParams(new TableCellParams[]{
                new TableCellParams(NBSP_CHAR),
                new TableCellParams("String Field 1"),
                new TableCellParams("String Field 2"),
                new TableCellParams("String Field 3"),
                new TableCellParams("Int Field 1"),
                new TableCellParams("Int Field 2"),
                new TableCellParams("Int Field 3")
        });

        assertEquals("Checking number of columns", 7, table.getColumnCount());

        for (int i = 0; i <= 3; i++)
            table.column(i).assertStyle("background: Tan");
        table.column(1).assertStyle("color: gray");
        table.column(2).assertStyle("color: green");
        table.column(3).assertStyle("color: blue");
        for (int i = 4; i <= 6; i++)
            table.column(i).assertStyle("color: black");
        table.column(4).assertStyle("background: LightSteelBlue");
        table.column(5).assertStyle("background: LightSteelBlue");
        table.column(6).assertStyle("background: SteelBlue");
    }

    @Test
    public void testColumnGroups_footers() {
        testAppFunctionalPage("/components/datatable/dataTableColumnGroups.jsf");
        tabbedPane("formID:testSelector").setPageIndex(2, ServerLoadingMode.getInstance());

        TableInspector table = dataTable("formID:twoFootersTable");
        TableSectionInspector body = table.body();
        assertFalse("There should be no header", table.header().elementExists());
        assertEquals("There should be 10 body rows", 10, body.rowCount());

        TableSectionInspector footer = table.footer();
        assertEquals("There should be 2 footer rows", 2, footer.rowCount());
        footer.row(0).assertCellStyles("border-top: 1px solid #a0a0a0");
        footer.row(0).assertCellStyles("border-bottom: ? none ?");
        footer.row(1).assertCellStyles("border-top: ? none ?");
        footer.row(1).assertCellStyles("border-bottom: ? none ?");

        table.footer().row(0).assertCellParams(new TableCellParams[]{
                new TableCellParams(null, 1, 1, "background: DarkKhaki"),
                new TableCellParams("String Field 1", 1, 1, "background: DarkKhaki"),
                new TableCellParams("String Field 2", 1, 1, "background: DarkKhaki"),
                new TableCellParams("String Field 3", 1, 1, "background: DarkKhaki"),
                new TableCellParams("Int Field 1", 1, 1, "background: BurlyWood"),
                new TableCellParams("Int Field 2", 1, 1, "background: LightSalmon"),
                new TableCellParams("Int Field 3", 1, 1, "background: BurlyWood")
        });
        table.footer().row(1).assertCellParams(new TableCellParams[]{
                new TableCellParams("String Fields", 4, 1, "background: Olive"),
                new TableCellParams("Integer Fields", 3, 1, "background: Peru")
        });

        assertEquals("Checking number of columns", 7, table.getColumnCount());
        for (int i = 0; i <= 3; i++)
            table.column(i).assertStyle("background: Khaki");
        for (int i = 4; i <= 6; i++)
            table.column(i).assertStyle("background: Bisque");
    }

    @Test
    public void testColumnGroups_mixed() {
        testAppFunctionalPage("/components/datatable/dataTableColumnGroups.jsf");
        tabbedPane("formID:testSelector").setPageIndex(3, ServerLoadingMode.getInstance());

        TableInspector table = dataTable("formID:mixedHeadersTable");

        assertEquals("Checking the number of columns", 9, table.getColumnCount());

        TableSectionInspector header = table.header();
        assertEquals("Checking the number of header rows", 4, header.rowCount());
        header.row(0).assertCellParams(new TableCellParams[]{
                new TableCellParams("Common Header", 9, 1, "border-bottom: 2px solid gray")
        });
        header.row(1).assertCellParams(new TableCellParams[]{
                new TableCellParams("", 1, 2, "border-right: 2px solid gray; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("String Field 1", 1, 2, "border-right: 2px solid gray; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams(NBSP_CHAR, 1, 2, "border-right: 2px solid gray; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("String Fields", 5, 1, "border-right: 1px solid orange; border-bottom: ? none ?"),
                new TableCellParams("Int Field 3 (header only)", 1, 2, "border-right: ? none ?; border-bottom: 1px solid #a0a0a0")
        });
        header.row(2).assertCellParams(new TableCellParams[]{
                new TableCellParams("String Field 3", 1, 1, "border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("Int Field 1", 1, 1, "border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("", 1, 1, "border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams(NBSP_CHAR, 1, 1, "border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams(NBSP_CHAR, 1, 1, "border-right: 1px solid orange; border-bottom: 1px solid #a0a0a0"),
        });
        header.row(3).assertCellStyles("border-bottom: 2px solid gray");
        for (int i = 1; i <= 2; i++)
            header.row(i).assertStyle("color: OrangeRed; font-weight: bold; font-family: Tahoma,Arial; font-size: 19px;");

        TableSectionInspector body = table.body();
        assertEquals("Checking the number of body rows", 10, body.rowCount());
        body.assertColumnCellStyles(new String[]{
                "border-right: 2px solid silver",
                "border-right: 2px solid silver",
                "border-right: 2px solid silver",
                "border-right: ? none ?",
                "border-right: ? none ?",
                "border-right: ? none ?",
                "border-right: ? none ?",
                "border-right: 1px solid silver",
                "border-right: ? none ?",
        });

        TableSectionInspector footer = table.footer();
        assertEquals("Checking the number of footer rows", 4, footer.rowCount());
        footer.row(0).assertCellStyles("border-top: 2px solid gray");
        footer.row(0).assertCellParams(new TableCellParams[]{
                new TableCellParams(NBSP_CHAR, 1, 3, "border-right: 2px solid gray"),
                new TableCellParams("String Field 1 (footer)", 1, 3, "border-right: 2px solid gray"),
                new TableCellParams("String Field 2 (footer only)", 1, 3, "border-right: 2px solid gray"),
                new TableCellParams("String Field 3 (footer)", 1, 1, "border-right: ? none ?"),
                new TableCellParams("Int Field 1 (footer)", 1, 1, "border-right: ? none ?"),
                new TableCellParams("", 1, 1, "border-right: ? none ?"),
                new TableCellParams(NBSP_CHAR, 1, 1, "border-right: ? none ?"),
                new TableCellParams(NBSP_CHAR, 1, 1, "border-right: 1px solid DarkBlue"),
                new TableCellParams(NBSP_CHAR, 1, 2, "border-right: ? none ?")
        });
        footer.row(1).assertCellParams(new TableCellParams[]{
                new TableCellParams("String Fields (footer)", 5, 1)
        });
        footer.row(2).assertCellParams(new TableCellParams[]{
                new TableCellParams("3-rd level footer", 6, 1, "border-top: 1px solid DarkBlue")
        });
        footer.row(3).assertCellParams(new TableCellParams[]{
                new TableCellParams("Common Footer", 9, 1, "border-top: 2px solid gray")
        });

        for (int i = 0; i <= 2; i++)
            footer.row(i).assertStyle("color: DarkBlue; font-weight: bold; font-family: Tahoma,Arial; font-size: 19px;");
    }

    @Test
    public void testSingleSelectionAndKeyboardNavigation() {
        testAppFunctionalPage("/components/datatable/datatableSingleSelection.jsf");

        //check keyboard navigation for single selection
        DataTableInspector singleSelectionDataTable = dataTable("formID:singleSelectionDataTable");
        singleSelectionDataTable.click();
        for (int i = 0; i < 9; i++) {
            singleSelectionDataTable.keyPress(KeyEvent.VK_DOWN);
            singleSelectionDataTable.checkSelectedIndex(i);
        }

        //check selection with the mouse help
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            singleSelectionDataTable.makeAndCheckSingleSelection(0, rand.nextInt(8));
        }

        //check rowData and rowIndex attributes
        DataTableInspector withRowDataTable = dataTable("formID:withRowDataID");
        withRowDataTable.checkSelectedIndex(0);
        DataTableInspector withRowIndexTable = dataTable("formID:withRowIndexID");
        withRowIndexTable.checkSelectedIndex(3);
        element("formID:withRowDataID:2:withRowData_firstBody").click();
        element("formID:withRowIndexID:0:withRowIndex_firstBody").click();
        element("formID:submitID").clickAndWait();
        withRowDataTable.checkSelectedIndex(2);
        withRowIndexTable.checkSelectedIndex(0);
    }

    //todo: row selection checking with 'Ctrl' key is absent
    @Test
    public void testMultipleSelectionAndKeyboardNavigation() {
        testAppFunctionalPage("/components/datatable/datatableMultipleSelection.jsf");

        DataTableInspector multipleSelectionDataTable = dataTable("formID:multipleSelectionDataTable");

        //check keyboard navigation for multiple selection
        multipleSelectionDataTable.click();
        for (int i = 0; i < 9; i++) {
            createEvent(multipleSelectionDataTable, null, EventType.KEY, "keypress", 40, true);
        }

        multipleSelectionDataTable.checkSelectedIndexes(0, 1, 2, 3, 4, 5, 6, 7, 8);
        for (int i = 0; i < 9; i++) {
            createEvent(multipleSelectionDataTable, null, EventType.KEY, "keypress", 38, true);
        }

        multipleSelectionDataTable.checkSelectedIndexes(0);
        //check selection with the mouse help
        for (int i = 0; i < 9; i++) {
            element("formID:multipleSelectionDataTable:" + i + ":multipleSelectionDataTable_firstBody").click();
        }

        multipleSelectionDataTable.checkSelectedIndexes(8);

        for (int i = 1; i < 9; i++) {
            element("formID:multipleSelectionDataTable:" + i + ":multipleSelectionDataTable_firstBody").click();
            multipleSelectionDataTable.checkSelectedIndexes(i);
        }

        //check rowDatas and rowIndexes attributes
        DataTableInspector withRowDataDataTable = dataTable("formID:withRowDatasDataTableID");
        withRowDataDataTable.checkSelectedIndexes(4, 2);
        DataTableInspector withRowIndexesDataTable = dataTable("formID:withRowIndexesDataTableID");
        withRowIndexesDataTable.checkSelectedIndexes(1, 3, 5);

        element("formID:withRowDatasDataTableID:0:withRowDatasDataTableID_firstBody").click();
        for (int i = 0; i < 3; i++) {
            createEvent(withRowDataDataTable, null, EventType.KEY, "keypress", 40, true);
        }
        element("formID:withRowIndexesDataTableID:0:withRowIndexesDataTable_firstBody").click();
        for (int i = 0; i < 3; i++) {
            createEvent(withRowIndexesDataTable, null, EventType.KEY, "keypress", 40, true);
        }
        withRowDataDataTable.checkSelectedIndexes(0, 1, 2, 3);
        withRowIndexesDataTable.checkSelectedIndexes(0, 1, 2, 3);
    }

    @Test
    public void testSortingFeature() {
        sorting(OpenFacesAjaxLoadingMode.getInstance());
        sorting(ServerLoadingMode.getInstance());
    }

    @Test
    public void testFilteringComboBox() {
        filteringComboBox(OpenFacesAjaxLoadingMode.getInstance());
        filteringComboBox(ServerLoadingMode.getInstance());
    }

    @Test
    public void testFilteringDropDown() {
        filteringDropDown(OpenFacesAjaxLoadingMode.getInstance());
        filteringDropDown(ServerLoadingMode.getInstance());
    }

    @Test
    public void testSelectAllCheckboxInsideCheckboxColumnFunctionality() {
        testAppFunctionalPage("/components/datatable/datatableSelectAllCheckbox_chb.jsf");

        DataTableInspector dataTable = dataTable("formID:checkboxColumnDataTable");

        element("formID:checkboxColumnDataTable:selectAllHeaderID").click();
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            dataTable.bodyRow(rowIndex).cell(0).assertChecked(true);
//      assertTableSelectionChecked("formID:checkboxColumnDataTable", rowIndex, 0);
        }
        element("formID:checkboxColumnDataTable:selectAllFooterID").click();
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            dataTable.bodyRow(rowIndex).cell(0).assertChecked(false);
        }
    }

    @Test
    public void test_q_refreshDataTable() {
        testAppFunctionalPage("/components/datatable/datatableQRefresh.jsf");

        //get DataTable footer and header values before refreshing
        ElementInspector firstHeader = element("formID:requestScopeDataTableID:requestScopeDataTableID_firstHeader");
        String beforeRefreshFirstHeader = firstHeader.text();
        ElementInspector secondHeader = element("formID:requestScopeDataTableID:requestScopeDataTableID_secondHeader");
        String beforeRefreshSecondHeader = secondHeader.text();
        ElementInspector firstFooter = element("formID:requestScopeDataTableID:requestScopeDataTableID_firstFooter");
        String beforeRefreshFirstFooter = firstFooter.text();
        ElementInspector secondFooter = element("formID:requestScopeDataTableID:requestScopeDataTableID_secondFooter");
        String beforeRefreshSecondFooter = secondFooter.text();

        //get DataTable body values before refreshing
        String[] beforeRefreshRowValues = new String[9];
        for (int rowIndex = 0; rowIndex < beforeRefreshRowValues.length; rowIndex++) {
            ElementInspector firstBody = element("formID:requestScopeDataTableID:" + rowIndex + ":requestScopeDataTableID_firstBody");
            ElementInspector secondBody = element("formID:requestScopeDataTableID:" + rowIndex + ":requestScopeDataTableID_secondBody");
            beforeRefreshRowValues[rowIndex] = firstBody.text() + secondBody.text();
        }

        //refresh DataTable
        element("formID:refresher").clickAndWait(OpenFacesAjaxLoadingMode.getInstance());

        //get DataTable footer and header values after refreshing
        String afterRefreshFirstHeader = firstHeader.text();
        String afterRefreshSecondHeader = secondHeader.text();
        String afterRefreshFirstFooter = firstFooter.text();
        String afterRefreshSecondFooter = secondFooter.text();

        //compare header and footer values before and after DataTable refresh
        assertFalse(beforeRefreshFirstHeader.equals(afterRefreshFirstHeader));
        assertFalse(beforeRefreshFirstFooter.equals(afterRefreshFirstFooter));
        assertFalse(beforeRefreshSecondHeader.equals(afterRefreshSecondHeader));
        assertFalse(beforeRefreshSecondFooter.equals(afterRefreshSecondFooter));

        //get DataTable body values after refreshing
        String[] afterRefreshRowValues = new String[9];
        for (int rowIndex = 0; rowIndex < afterRefreshRowValues.length; rowIndex++) {
            ElementInspector firstBody = element("formID:requestScopeDataTableID:" + rowIndex + ":requestScopeDataTableID_firstBody");
            ElementInspector secondBody = element("formID:requestScopeDataTableID:" + rowIndex + ":requestScopeDataTableID_secondBody");
            afterRefreshRowValues[rowIndex] = firstBody.text() + secondBody.text();
            assertFalse(afterRefreshRowValues[rowIndex].equals(beforeRefreshRowValues[rowIndex]));
        }
    }

    @Test
    public void testRowKeyFunctionality() {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/datatableRowKey.jsf");

        selenium.click("formID:withoutRowKeyDataTableID:0:withoutRowKeyDataTableID_firstBody");
        selenium.click("formID:withDataTableID:0:withRowKeyDataTableID_firstBody");
        dataTablePaginator("formID:withoutRowKeyDataTableID:dataTablePaginator_A4J").nextPage()
                .clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        dataTablePaginator("formID:withDataTableID:dataTablePaginator_A4J").nextPage()
                .clickAndWait(OpenFacesAjaxLoadingMode.getInstance());
        int withoutRowKeyDataTableIndex = dataTable("formID:withoutRowKeyDataTableID").selectedRowIndex();
        int withRowKeyDataTableIndex = dataTable("formID:withDataTableID").selectedRowIndex();
        assertFalse(withRowKeyDataTableIndex == withoutRowKeyDataTableIndex);
    }

    @Test
    public void testSelectAllCheckboxInsideSelectionColumnFunctionality() {
        testAppFunctionalPage("/components/datatable/datatableSelectAllCheckbox__sel.jsf");

        DataTableInspector selectionColumnDataTable = dataTable("formID:selectionColumnDataTableID");

        ElementInspector selectAllHeader = element("formID:selectionColumnDataTableID:selectAllHeaderID");
        selectAllHeader.click();
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            selectionColumnDataTable.bodyRow(rowIndex).cell(0).assertChecked(true);
        }
        selectionColumnDataTable.checkSelectedIndexes(0, 1, 2, 3, 4, 5, 6, 7, 8);

        element("formID:selectionColumnDataTableID:selectAllFooterID").click();
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            selectionColumnDataTable.bodyRow(rowIndex).cell(0).assertChecked(false);
        }
        selectionColumnDataTable.evalExpression("selectAllRows();");
        String isHeaderChecked = selectAllHeader.evalExpression("checked;");
        assertEquals(isHeaderChecked, "true");
        String isFooterChecked = selectAllHeader.evalExpression("checked;");
        assertEquals(isFooterChecked, "true");
    }

    @Test
    public void testFeaturesCombination_ajax() {
        featuresCombination(OpenFacesAjaxLoadingMode.getInstance());
    }

    @Test
    public void testFeaturesCombination_server() {
        featuresCombination(ServerLoadingMode.getInstance());
    }

    @Test
    public void testNoDataRowMessages() {
        testAppFunctionalPage("/components/datatable/datatableNoData.jsf");

        //check default string for the no data corresponding to filter criterion
        DataTableInspector noDataDefaultDateTable = dataTable("formID:noDataDefaultID");
        noDataDefaultDateTable.column(0).filter(InputTextFilterInspector.class, "formID:noDataDefaultID:filter1").makeFiltering("www");
        noDataDefaultDateTable.body().row(0).cell(0).assertText("No records satisfying the filtering criteria");

        //check is message string for the no data corresponding to filter criterion visible
        DataTableInspector noDataMessageDataTable = dataTable("formID:noDataMessageAllowedID");
        noDataMessageDataTable.column(0).filter(InputTextFilterInspector.class, "formID:noDataMessageAllowedID:filter1").makeFiltering("www");
        assertEquals("There should be one invisible fake row", 1, noDataMessageDataTable.body().rowCount());
        noDataMessageDataTable.body().row(0).assertCellParams(new TableCellParams[]{
                new TableCellParams(null, 2, 1, "display: none")
        });

        //check custom message for the no data corresponding to filter criterion
        DataTableInspector customNoDataMessageTable = dataTable("formID:customNoDataMessageID");
        customNoDataMessageTable.column(0).filter(InputTextFilterInspector.class,"formID:customNoDataMessageID:filter1").makeFiltering("www");
        element("formID:customNoDataMessageID:noFilteredDataCustomMessageID").assertText("Test no filtered data");

        //check default string for the no data corresponding to filter criterion
        dataTable("formID:emptyDataID").bodyRow(0).cell(0).assertText("No records");

        //check is message string for the no data visible
        assertEquals("There should be one invisible fake row", 1, new TableInspector("formID:emptyDataMessageForbiddenID").body().rowCount());
        dataTable("formID:emptyDataMessageForbiddenID").body().row(0).assertCellParams(new TableCellParams[]{
                new TableCellParams(null, 2, 1, "display: none")
        });

        //check custom message for the no data
        element("formID:customEmptyDataID:noDataMessageID").assertText("Test data is empty");

    }

    private void pagination(LoadingMode loadingMode) {
        Selenium selenium = getSelenium();
        testAppFunctionalPage("/components/datatable/datatablePagination.jsf");
        TabSetInspector loadingModes = tabSet("formID:loadingModes");

        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait();
        }

        List<DataTableUtils.TestDataTableItem> referenceDataTableValues = DataTableUtils.TWO_STRING_COLUMN_LIST;
        final String firstColumnHeaderContent = "first column header";
        final String secondColumnHeaderContent = "second column header";
        final String firstColumnFooterContent = "first column footer";
        final String secondColumnFooterContent = "second column footer";

        DataTablePaginatorInspector clientPaginator = dataTablePaginator("formID:paginableDataTable:paginableDataTablePaginator");

        ElementInspector firstColumnHeader = element("formID:paginableDataTable:paginableDataTable_firstColumnHeader");
        ElementInspector secondColumnHeader = element("formID:paginableDataTable:paginableDataTable_secondColumnHeader");
        ElementInspector firstColumnFooter = element("formID:paginableDataTable:paginableDataTable_firstColumnFooter");
        ElementInspector secondColumnFooter = element("formID:paginableDataTable:paginableDataTable_secondColumnFooter");

        for (int i = 1; i < 3; i++) {
            firstColumnHeader.assertText(firstColumnHeaderContent);
            secondColumnHeader.assertText(secondColumnHeaderContent);
            firstColumnFooter.assertText(firstColumnFooterContent);
            secondColumnFooter.assertText(secondColumnFooterContent);

            checkDataTableContents(selenium, referenceDataTableValues, i);
            clientPaginator.nextPage().clickAndWait(loadingMode);
        }
        for (int i = 3; i > 1; i--) {
            firstColumnHeader.assertText(firstColumnHeaderContent);
            secondColumnHeader.assertText(secondColumnHeaderContent);
            firstColumnFooter.assertText(firstColumnFooterContent);
            secondColumnFooter.assertText(secondColumnFooterContent);

            checkDataTableContents(selenium, referenceDataTableValues, i);
            element("formID:paginableDataTable").click();
            //go to the previous page using keyboard
            selenium.getEval("var el = this.page().findElement('formID:paginableDataTable'); var evObj = document.createEvent('KeyEvents'); evObj.initKeyEvent('keypress', true, true, window, false, false, false, false, 33, 0); el.dispatchEvent(evObj);");

            loadingMode.waitForLoad();
        }

        clientPaginator.lastPage().clickAndWait(loadingMode);
        checkDataTableContents(selenium, referenceDataTableValues, 3);
        clientPaginator.firstPage().clickAndWait(loadingMode);
        checkDataTableContents(selenium, referenceDataTableValues, 1);
        if (loadingMode instanceof ServerLoadingMode) {
            // reset tab index for possible further tests
            loadingModes.tabs().get(0).clickAndWait();
        }

    }

    private void checkDataTableContents(Selenium selenium, List<DataTableUtils.TestDataTableItem> referenceDataTableValues, int pageNo) {
        List<DataTableUtils.TestDataTableItem> currentPageValues = DataTableUtils.getCurrentPageFromReferenceValues(referenceDataTableValues, pageNo);
        for (int j = 0; j < 3; j++) {
            DataTableUtils.TestDataTableItem currentReferenceRow = currentPageValues.get(j);
            //get text from the body rows
            String currentFirstCellValue = selenium.getText("formID:paginableDataTable:" + j + ":paginableDataTable_firstColumnBody");
            String currentSecondCellValue = selenium.getText("formID:paginableDataTable:" + j + ":paginableDataTable_secondColumnBody");

            //compare received values with their reference values
            assertEquals("Checking contents for page index: " + pageNo, currentReferenceRow.getFirstColumn(), currentFirstCellValue);
            assertEquals("Checking contents for page index: " + pageNo, currentReferenceRow.getSecondColumn(), currentSecondCellValue);
        }
    }

    private void sorting(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/datatable/datatableSorting.jsf");

        List<DataTableUtils.TestDataTableItem> referenceDataTableValues = DataTableUtils.TWO_STRING_COLUMN_LIST;

        String dataTableID;

        if (loadingMode instanceof OpenFacesAjaxLoadingMode) {
            dataTableID = "sortableDataTable";
        } else {
            dataTableID = "sortableDataTable1";
        }

        element("formID:" + dataTableID + ":" + dataTableID + "_firstHeader").assertText("first column header");
        element("formID:" + dataTableID + ":" + dataTableID + "_secondHeader").assertText("second column header");
        element("formID:" + dataTableID + ":" + dataTableID + "_firstFooter").assertText("first column footer");
        element("formID:" + dataTableID + ":" + dataTableID + "_secondFooter").assertText("second column footer");

        for (int sortIterationsNumber = 0; sortIterationsNumber < 3; sortIterationsNumber++) {
            element("formID:" + dataTableID + ":" + dataTableID + "_firstHeader")
                    .clickAndWait(loadingMode);
            if (sortIterationsNumber > 0) {
                referenceDataTableValues = new ArrayList<DataTableUtils.TestDataTableItem>(referenceDataTableValues);
                Collections.reverse(referenceDataTableValues);
            }
            for (int row = 0; row < 9; row++) {
                DataTableUtils.TestDataTableItem currentReferenceRow = referenceDataTableValues.get(row);
                element("formID:" + dataTableID + ":" + row + ":" + dataTableID + "_firstBody")
                        .assertText(currentReferenceRow.getFirstColumn());
                element("formID:" + dataTableID + ":" + row + ":" + dataTableID + "_secondBody")
                        .assertText(currentReferenceRow.getSecondColumn());
            }
        }
    }

    private void filteringComboBox(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/datatable/datatableFilteringComboBox.jsf");

        TabSetInspector loadingModes = tabSet("formID:loadingModes");

        DataTableInspector filterableDataTable = dataTable("formID:filterableDataTable_comboBox");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait();
        }

        filterableDataTable.setLoadingMode(loadingMode);
        filterableDataTable.column(0).filter(ComboBoxFilterInspector.class, "formID:filterableDataTable_comboBox:filter1").makeFiltering("col3_row1");

        element("formID:filterableDataTable_comboBox:filterableDataTable_comboBox_firstHeader")
                .assertText("first column header");
        element("formID:filterableDataTable_comboBox:filterableDataTable_comboBox_secondHeader")
                .assertText("second column header");
        element("formID:filterableDataTable_comboBox:filterableDataTable_comboBox_firstFooter")
                .assertText("first column footer");
        element("formID:filterableDataTable_comboBox:filterableDataTable_comboBox_secondFooter")
                .assertText("second column footer");

        DataTableUtils.TestDataTableItem referenceFilteredRow = DataTableUtils.TWO_STRING_COLUMN_LIST.get(0);

        //check is right row appeared after filtering and other rows is non-visible
        ElementInspector firstBody = element("formID:filterableDataTable_comboBox:0:filterableDataTable_comboBox_firstBody");
        firstBody.assertVisible(true);
        ElementInspector secondBody = element("formID:filterableDataTable_comboBox:0:filterableDataTable_comboBox_secondBody");
        secondBody.assertVisible(true);

        //compare filtered row with the reference values
        firstBody.assertText(referenceFilteredRow.getFirstColumn());
        secondBody.assertText(referenceFilteredRow.getSecondColumn());

        for (int i = 1; i < 9; i++) {
            element("formID:filterableDataTable_comboBox:" + i + ":filterableDataTable_comboBox_firstBody")
                    .assertElementExists(false);
            element("formID:filterableDataTable_comboBox:" + i + ":filterableDataTable_comboBox_secondBody")
                    .assertElementExists(false);
        }

        if (loadingMode instanceof ServerLoadingMode) {
            // reset tab index for possible further tests
            loadingModes.tabs().get(0).clickAndWait();
        }

    }

    private void filteringDropDown(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/datatable/datatableFilteringDropDownField.jsf");

        TabSetInspector loadingModes = tabSet("formID:loadingModes");
        DataTableInspector filterableDataTable = dataTable("formID:filterableDataTable_dropDownField");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait();
        }
        filterableDataTable.setLoadingMode(loadingMode);

        filterableDataTable.column(0).filter(DropDownFieldFilterInspector.class, "formID:filterableDataTable_dropDownField:filter1").makeFiltering("col3_row1");

        element("formID:filterableDataTable_dropDownField:filterableDataTable_dropDownField_firstHeader")
                .assertText("first column header");
        element("formID:filterableDataTable_dropDownField:filterableDataTable_dropDownField_secondHeader")
                .assertText("second column header");
        element("formID:filterableDataTable_dropDownField:filterableDataTable_dropDownField_firstFooter")
                .assertText("first column footer");
        element("formID:filterableDataTable_dropDownField:filterableDataTable_dropDownField_secondFooter")
                .assertText("second column footer");

        DataTableUtils.TestDataTableItem referenceFilteredRow = DataTableUtils.TWO_STRING_COLUMN_LIST.get(0);

        //check is right row appeared after filtering and other rows is non-visible
        for (int i = 0; i < 9; i++) {
            if (i == 0) {
                ElementInspector firstBody = element("formID:filterableDataTable_dropDownField:0:filterableDataTable_dropDownField_firstBody");
                firstBody.assertVisible(true);
                ElementInspector secondBody = element("formID:filterableDataTable_dropDownField:0:filterableDataTable_dropDownField_secondBody");
                secondBody.assertVisible(true);

                //compare filtered row with the reference values
                firstBody.assertText(referenceFilteredRow.getFirstColumn());
                secondBody.assertText(referenceFilteredRow.getSecondColumn());
            } else {
                element("formID:filterableDataTable_dropDownField:" + i + ":filterableDataTable_dropDownField_firstBody")
                        .assertElementExists(false);
                element("formID:filterableDataTable_dropDownField:" + i + ":filterableDataTable_dropDownField_secondBody")
                        .assertElementExists(false);
            }
        }
        if (loadingMode instanceof ServerLoadingMode) {
            // reset tab index for possible further tests
            loadingModes.tabs().get(0).clickAndWait();
        }

    }

    private void featuresCombination(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/datatable/datatableFeaturesCombination.jsf");
        TabSetInspector loadingModes = tabSet("formID:loadingModes");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait();
        }

        DataTableInspector combinationDataTable = dataTable("formID:featuresCombinationDataTableID");
        if (loadingMode instanceof ServerLoadingMode) {
            combinationDataTable.setLoadingMode(ServerLoadingMode.getInstance());
        }

        //perform filtering using comboBox filter kind
        combinationDataTable.column(2).filter(ComboBoxFilterInspector.class, "formID:featuresCombinationDataTableID:filter2").makeFiltering("criterion_1,2,3");

        //get reference data for checking filtered data
        List referenceDataAfterComboboxFilter = DataTableUtils.getFilteredValuesFeaturesCombinationTable("criterion_1,2,3");
        //get actual data

        int rowsQuantity = combinationDataTable.rowCount();
        List<DataTableUtils.FeaturesCombinationTestDataTableItem> actualDataAfterComboboxFiltering = new ArrayList<DataTableUtils.FeaturesCombinationTestDataTableItem>();
        for (int i = 0; i < rowsQuantity; i++) {
            ElementInspector firstBody = element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_firstBody");
            ElementInspector secondBody = element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_secondBody");
            ElementInspector thirdBody = element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_thirdBody");
            ElementInspector fourthBody = element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_fourthBody");

            actualDataAfterComboboxFiltering.add(new DataTableUtils.FeaturesCombinationTestDataTableItem(firstBody.text(),
                    secondBody.text(), thirdBody.text(), fourthBody.text()));

            DataTableUtils.FeaturesCombinationTestDataTableItem featuresCombinationTestDataTableItem =
                    (DataTableUtils.FeaturesCombinationTestDataTableItem) referenceDataAfterComboboxFilter.get(i);
            firstBody.assertText(featuresCombinationTestDataTableItem.getFirstColumn());
            secondBody.assertText(featuresCombinationTestDataTableItem.getSecondColumn());
            thirdBody.assertText(featuresCombinationTestDataTableItem.getThirdColumn());
            fourthBody.assertText(featuresCombinationTestDataTableItem.getFourthColumn());
        }

        //select first row
        element("formID:featuresCombinationDataTableID:0:featuresCombinationDataTableID_firstBody").click();
        combinationDataTable.checkSelectedIndex(0);

        //sort by first column
        combinationDataTable.column(1).makeSorting();
        //check data after sorting by first column
        checkDataAfterSorting(rowsQuantity, actualDataAfterComboboxFiltering);
        //check selection after sorting by first column
        combinationDataTable.checkSelectedIndex(0);

        //sort by second column
        combinationDataTable.column(2).makeSorting();
        //check data after sorting by second column
        checkDataAfterSorting(rowsQuantity, actualDataAfterComboboxFiltering);
        //check selection after sorting by second column
        combinationDataTable.checkSelectedIndex(0);

        //sort by third column
        combinationDataTable.column(3).makeSorting();
        combinationDataTable.column(3).makeSorting();
        //check data after sorting by third column
        Collections.reverse(actualDataAfterComboboxFiltering);
        checkDataAfterSorting(rowsQuantity, actualDataAfterComboboxFiltering);
        //check selection after sorting by third column
        combinationDataTable.checkSelectedIndex(2);

        //sort by fourth column
        combinationDataTable.column(4).makeSorting();
        //check data after sorting by fourth column
        Collections.reverse(actualDataAfterComboboxFiltering);
        checkDataAfterSorting(rowsQuantity, actualDataAfterComboboxFiltering);
        //check selection after sorting by fourth column
        combinationDataTable.checkSelectedIndex(0);

        /* perform filtering using dropDown filter */
        combinationDataTable.column(2).filter(ComboBoxFilterInspector.class, "formID:featuresCombinationDataTableID:filter2").makeFiltering("<All>");

        combinationDataTable.checkSelectedIndex(0);
        combinationDataTable.column(3).filter(DropDownFieldFilterInspector.class, "formID:featuresCombinationDataTableID:filter3").makeFiltering("criterion_1,4,7");

        combinationDataTable.checkSelectedIndex(0);

        //get reference data for checking filtered data
        List referenceDataAfterDropDownFilter = DataTableUtils.getFilteredValuesFeaturesCombinationTable("criterion_1,4,7");
        int rowsQuantityDropDown = combinationDataTable.rowCount();
        for (int i = 0; i < rowsQuantityDropDown; i++) {
            DataTableUtils.FeaturesCombinationTestDataTableItem featuresCombinationTestDataTableItem = (DataTableUtils.FeaturesCombinationTestDataTableItem) referenceDataAfterDropDownFilter.get(i);
            element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_firstBody")
                    .assertText(featuresCombinationTestDataTableItem.getFirstColumn());
            element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_secondBody")
                    .assertText(featuresCombinationTestDataTableItem.getSecondColumn());
            element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_thirdBody")
                    .assertText(featuresCombinationTestDataTableItem.getThirdColumn());
            element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_fourthBody")
                    .assertText(featuresCombinationTestDataTableItem.getFourthColumn());
        }

        /* perform filtering using searchField filter */
        combinationDataTable.checkSelectedIndex(0);

        combinationDataTable.column(1).filter(InputTextFilterInspector.class, "formID:featuresCombinationDataTableID:filter1").makeFiltering("id_1");

        combinationDataTable.checkSelectedIndex(0);

        //get reference data for checking filtered data
        List<DataTableUtils.FeaturesCombinationTestDataTableItem> referenceDataAfterSearchFieldFilter = DataTableUtils.getFilteredValuesFeaturesCombinationTable("id_1");
        DataTableUtils.FeaturesCombinationTestDataTableItem featuresCombinationTestDataTableItem = referenceDataAfterSearchFieldFilter.get(0);
        element("formID:featuresCombinationDataTableID:0:featuresCombinationDataTableID_firstBody")
                .assertText(featuresCombinationTestDataTableItem.getFirstColumn());
        element("formID:featuresCombinationDataTableID:0:featuresCombinationDataTableID_secondBody")
                .assertText(featuresCombinationTestDataTableItem.getSecondColumn());
        element("formID:featuresCombinationDataTableID:0:featuresCombinationDataTableID_thirdBody")
                .assertText(featuresCombinationTestDataTableItem.getThirdColumn());
        element("formID:featuresCombinationDataTableID:0:featuresCombinationDataTableID_fourthBody")
                .assertText(featuresCombinationTestDataTableItem.getFourthColumn());

        if (loadingMode instanceof ServerLoadingMode) {
            // reset tab index for possible further tests
            loadingModes.tabs().get(0).clickAndWait(loadingMode);
        }
    }

    private void checkDataAfterSorting(int rowsQuantity, List<DataTableUtils.FeaturesCombinationTestDataTableItem> actualDataAfterComboboxFiltering) {
        for (int i = 0; i < rowsQuantity; i++) {
            DataTableUtils.FeaturesCombinationTestDataTableItem featuresCombinationTestDataTableItem = actualDataAfterComboboxFiltering.get(i);
            element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_firstBody")
                    .assertText(featuresCombinationTestDataTableItem.getFirstColumn());
            element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_secondBody")
                    .assertText(featuresCombinationTestDataTableItem.getSecondColumn());
            element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_thirdBody")
                    .assertText(featuresCombinationTestDataTableItem.getThirdColumn());
            element("formID:featuresCombinationDataTableID:" + i + ":featuresCombinationDataTableID_fourthBody")
                    .assertText(featuresCombinationTestDataTableItem.getFourthColumn());
        }
    }

    @Test
    public void testStylesCustomizationAsImage() {
        testAppFunctionalPage("/components/datatable/DataTableStylesCustomization.jsf");
        assertAppearanceNotChanged("StylesCustomizationAsImage", "form1:table1");
    }

}