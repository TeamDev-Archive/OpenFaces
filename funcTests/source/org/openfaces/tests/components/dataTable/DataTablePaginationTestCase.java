/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.tests.components.dataTable;

import org.inspector.api.Table;
import org.inspector.components.table.Pagination;
import org.inspector.components.TabSet;
import org.inspector.components.table.TableCell;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * @author Max Yurin
 */
public class DataTablePaginationTestCase extends BaseSeleniumTest {
    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int PAGE_COUNT = 3;
    private static final FuncTestsPages PAGE = FuncTestsPages.DATATABLE_PAGINATION;
    private static final String TAB_SET_ID = "formID:loadingModes";
    private static final String PAGINABLE_TABLE_ID = "formID:paginableDataTable";
    private static final String PAGINATOR_ID = "formID:paginableDataTable:paginableDataTablePaginator";

    @Test(groups = {"component"})
    public void testPagination_common() {
        navigateTo(PAGE);

        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);

        assertThat("Page index is not equals default value: ", pagination.getIndex(), is(DEFAULT_PAGE_INDEX));
        assertThat("Page count index is not equals: ", pagination.getMaxIndex(), is(PAGE_COUNT));
    }

    @Test(groups = {"component"}, enabled = false)
    public void testStyles_common(){
        navigateTo(PAGE);

        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);
    }

    @Test(groups = {"component"}, expectedExceptions = NoSuchElementException.class)
    public void test_moveToFirstPageIsNotAvailable() {
        navigateTo(PAGE);

        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);

        pagination.moveToFirst();
    }

    @Test(groups = {"component"}, expectedExceptions = NoSuchElementException.class)
    public void test_movePreviousIsNotAvailable() {
        navigateTo(PAGE);
        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);

        pagination.movePrevious();
    }

    @Test(groups = {"component"})
    public void testPagination_moveToPage() {
        navigateTo(PAGE);

        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Table firstPage = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell firstPageCell = firstPage.body().nextRow().nextCell();
        final String firstPageCellValue = firstPageCell.text();

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);
        pagination.moveToPageByNumber(2);
        pagination.keyboard().keyPress(Keys.ENTER);

        assertThat("Page number has not changed!", pagination.getIndex(), is(not(DEFAULT_PAGE_INDEX)));

        final Table secondPage = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell secondPageCell = secondPage.body().nextRow().nextCell();

        assertThat("Cells of the first and second table pages should be different!",
                secondPageCell.text(), is(not(firstPageCellValue)));
    }

    @Test(groups = {"component"})
    public void testPagination_moveForward() {
        navigateTo(PAGE);

        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);

        final Table firstPageTable = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell firstPageCell = firstPageTable.body().nextRow().nextCell();
        final String firstPageCellValue = firstPageCell.text();
        final int firstPage = pagination.getIndex();

        pagination.moveNext();

        assertThat("Paginator is not moved to next page!", pagination.getIndex(), is(not(firstPage)));

        final Table secondPageTable = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell secondPageCell = secondPageTable.body().nextRow().nextCell();

        assertThat("Cells of the first and second table pages should be different!",
                secondPageCell.text(), is(not(firstPageCellValue)));
    }

    @Test(groups = {"component"})
    public void testPagination_moveBackward() {
        navigateTo(PAGE);

        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);

        pagination.moveToLast();

        final Table lastPageTable = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell lastPageCell = lastPageTable.body().nextRow().nextCell();
        final String lastPageCellValue = lastPageCell.text();

        final int lastPage = pagination.getIndex();
        pagination.movePrevious();

        assertThat("Paginator is not moved to next page!", pagination.getIndex(), is(not(lastPage)));

        final Table secondPageTable = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell secondPageCell = secondPageTable.body().nextRow().nextCell();

        assertThat("Cells of the first and second table pages should be different!",
                secondPageCell.text(), is(not(lastPageCellValue)));
    }

    @Test(groups = {"component"})
    public void testPagination_moveToFirstPage() {
        navigateTo(PAGE);

        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);

        pagination.moveToLast();

        final Table lastPageTable = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell lastPageCell = lastPageTable.body().nextRow().nextCell();
        final String lastPageCellValue = lastPageCell.text();

        pagination.moveToFirst();

        assertThat("Paginator is not moved to next page!", pagination.getIndex(), is(1));

        final Table firstPageTable = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell firstPageCell = firstPageTable.body().nextRow().nextCell();

        assertThat("Cells of the first and second table pages should be different!",
                firstPageCell.text(), is(not(lastPageCellValue)));
    }

    @Test(groups = {"component"})
    public void testPagination_moveToLastPage() {
        navigateTo(PAGE);

        final TabSet tabSet = getControlFactory().getTabSet(TAB_SET_ID);
        tabSet.openTab("1");

        final Pagination pagination = getControlFactory().getPagination(PAGINATOR_ID);

        final Table firstPageTable = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell firstPageCell = firstPageTable.body().nextRow().nextCell();
        final String firstPageCellValue = firstPageCell.text();
        final int firstPage = pagination.getIndex();

        pagination.moveToLast();

        assertThat("Paginator is not moved to next page!", pagination.getIndex(), is(not(firstPage)));

        final Table lastPageTable = getControlFactory().getDataTable(PAGINABLE_TABLE_ID);
        final TableCell lastPageCell = lastPageTable.body().nextRow().nextCell();

        assertThat("Cells of the first and second table pages should be different!",
                lastPageCell.text(), is(not(firstPageCellValue)));
    }
}
