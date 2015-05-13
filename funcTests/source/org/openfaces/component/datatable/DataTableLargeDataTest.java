/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
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
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.ServerLoadingMode;
import org.seleniuminspector.html.TableRowInspector;
import org.seleniuminspector.openfaces.DataTableInspector;
import org.seleniuminspector.openfaces.DataTablePaginatorInspector;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TabSetInspector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.openfaces.component.datatable.DataTableUtils.TableColumn;

/**
 * @author Darya Shumilina
 */
public class DataTableLargeDataTest extends OpenFacesTestCase {
    public static final String PAGE_URL = "/components/datatable/dataTableLarge.jsf";
    public static final String TABLE_IN_FORM_ID = "formID:banks";
    public static final String PAGINATOR_ID = "formID:banks:paginatorBelow";

    public static final String TABLE_ID = "banks";
    public static final String FIRST_COLUMN_BODY = ":" + TABLE_ID + "_firstColumnBody";
    public static final String SECOND_COLUMN_BODY = ":" + TABLE_ID + "_secondColumnBody";
    public static final String FIRST_COLUMN_FOOTER = ":" + TABLE_ID + "_firstColumnFooter";
    public static final String SECOND_COLUMN_FOOTER = ":" + TABLE_ID + "_secondColumnFooter";
    public static final String FIRST_BODY = ":" + TABLE_ID + "_firstBody";
    public static final String SEMICOLON = ":";
    private static final String NBSP_CHAR = "\u00a0";

    @Test
    public void testPagination() {
        pagination(OpenFacesAjaxLoadingMode.getInstance());
        pagination(ServerLoadingMode.getInstance());
    }


    @Test
    @Ignore
    public void testSingleSelectionAndKeyboardNavigation() {
        testAppFunctionalPage(TABLE_IN_FORM_ID);

        //check keyboard navigation for single selection
        DataTableInspector singleSelectionDataTable = dataTable(TABLE_IN_FORM_ID);
        Actions click = new Actions(getDriver()).moveToElement(
                getDriver().findElement(By.xpath(singleSelectionDataTable.bodyRow(0).getXPath())))
                .click();
        click.build().perform();
        for (int i = 0; i < 8; i++) {
            Actions keyDown = new Actions(getDriver()).sendKeys(Keys.ARROW_DOWN);
            keyDown.build().perform();
            singleSelectionDataTable.checkSelectedIndex(i + 1);
        }

        //check selection with the mouse help
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            singleSelectionDataTable.makeAndCheckSingleSelection(0, rand.nextInt(8));
        }

        //check rowData and rowIndex attributes
        DataTableInspector withRowDataTable = dataTable(TABLE_IN_FORM_ID);
        withRowDataTable.checkSelectedIndex(0);
        DataTableInspector withRowIndexTable = dataTable(TABLE_IN_FORM_ID);
        withRowIndexTable.checkSelectedIndex(3);
        element(TABLE_IN_FORM_ID + ":2:" + TABLE_ID + "_firstBody").click();
        element(TABLE_IN_FORM_ID + ":0:" + TABLE_ID + "_firstBody").click();

        element("formID:submitID").clickAndWait();
        withRowDataTable.checkSelectedIndex(2);
        withRowIndexTable.checkSelectedIndex(0);
    }

    @Test
    @Ignore
    public void testMultipleSelectionAndKeyboardNavigation() {
        testAppFunctionalPage(PAGE_URL);

        DataTableInspector multipleSelectionDataTable = dataTable(TABLE_IN_FORM_ID);

        //check keyboard navigation for multiple selection
        multipleSelectionDataTable.bodyRow(0).click();
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
            element(TABLE_IN_FORM_ID + i + FIRST_BODY).click();
        }

        multipleSelectionDataTable.checkSelectedIndexes(8);

        for (int i = 1; i < 9; i++) {
            element(TABLE_IN_FORM_ID + i + FIRST_BODY).click();
            multipleSelectionDataTable.checkSelectedIndexes(i);
        }

        //check rowDatas and rowIndexes attributes
        DataTableInspector table = dataTable(TABLE_IN_FORM_ID);
        table.checkSelectedIndexes(4, 2);

        element(TABLE_IN_FORM_ID + ":0:" + FIRST_BODY).click();
        for (int i = 0; i < 3; i++) {
            createEvent(table, null, EventType.KEY, "keypress", 40, true);
        }

        table.checkSelectedIndexes(0, 1, 2, 3);
    }

    @Test
    @Ignore
    public void testSortingFeature() {
        sorting(OpenFacesAjaxLoadingMode.getInstance());
        sorting(ServerLoadingMode.getInstance());
    }

    private void sorting(LoadingMode loadingMode) {
        testAppFunctionalPage(PAGE_URL);
        sleep(4000);

        TabSetInspector loadingModes = tabSet("formID:loadingModes");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait();
        }

        final DataTableInspector table = dataTable(TABLE_IN_FORM_ID);
        final Collection<TableColumn> originalTableDatas = Collections.unmodifiableCollection(getTableColumns(table));

    }

    private void pagination(LoadingMode loadingMode) {
        testAppFunctionalPage(PAGE_URL);
        sleep(4000);

        TabSetInspector loadingModes = tabSet("formID:loadingModes");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait();
        }

        final DataTableInspector table = dataTable(TABLE_IN_FORM_ID);
        final Collection<TableColumn> originalTableDatas = Collections.unmodifiableCollection(getTableColumns(table));

        DataTablePaginatorInspector clientPaginator = dataTablePaginator(PAGINATOR_ID);
        clientPaginator.nextPage().clickAndWait(loadingMode);
        checkDataTableColumns(originalTableDatas, getTableColumns(table));

        clientPaginator.lastPage().clickAndWait(loadingMode);
        checkDataTableColumns(originalTableDatas, getTableColumns(table));

        clientPaginator.previousPage().clickAndWait(loadingMode);
        checkDataTableColumns(originalTableDatas, getTableColumns(table));

        clientPaginator.firstPage().clickAndWait(loadingMode);
        checkDataTableColumns(originalTableDatas, getTableColumns(table));
    }

    private List<TableColumn> getTableColumns(DataTableInspector table) {
        List<TableColumn> rowDatas = new ArrayList<TableColumn>();

        for (int i = 0; i < table.body().rowCount(); i++) {
            final TableRowInspector row = table.body().row(i);

            TableColumn column = new TableColumn(i);
            column.add(row.cell(i).text());

            rowDatas.add(column);
        }

        return rowDatas;
    }

    private void checkDataTableColumns(Collection<TableColumn> original, Collection<TableColumn> afterPaging){
        assertNotNull("Checking columns length: ", original);
        assertNotNull("Checking columns length: ", afterPaging);
        assertEquals("Checking columns length: ", original.size(), afterPaging.size());

        for (TableColumn originalColumn : original) {
            final TableColumn changedColumn = afterPaging.iterator().next();

            for (String s1 : originalColumn.getAll()) {
                assertNotSame("Columns content are different",s1, changedColumn.next());
            }
        }
    }

    private void checkDataTableContents(Selenium selenium, List<DataTableUtils.TestDataTableItem> referenceDataTableValues, int pageNo) {
        List<DataTableUtils.TestDataTableItem> currentPageValues = DataTableUtils.getCurrentPageFromReferenceValues(referenceDataTableValues, pageNo);
        for (int j = 0; j < 3; j++) {
            DataTableUtils.TestDataTableItem currentReferenceRow = currentPageValues.get(j);
            //get text from the body rows
            String currentFirstCellValue = selenium.getText(TABLE_IN_FORM_ID + SEMICOLON + j + FIRST_COLUMN_BODY);
            String currentSecondCellValue = selenium.getText(TABLE_IN_FORM_ID + SEMICOLON + j + SECOND_COLUMN_BODY);

            //compare received values with their reference values
            assertEquals("Checking contents for page index: " + pageNo, currentReferenceRow.getFirstColumn(), currentFirstCellValue);
            assertEquals("Checking contents for page index: " + pageNo, currentReferenceRow.getSecondColumn(), currentSecondCellValue);
        }
    }
}