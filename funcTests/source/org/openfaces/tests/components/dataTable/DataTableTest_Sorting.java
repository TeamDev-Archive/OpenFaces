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
import org.inspector.components.table.TableRow;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Max Yurin
 */
public class DataTableTest_Sorting extends BaseSeleniumTest {
    private static final String TABLE_ID = "formID:twoHeadersTable";
    private static final FuncTestsPages PAGE = FuncTestsPages.DATATABLE_SORTING;
    private static final String SORTABLE_DATATABLE_WITH_AJAX = "formID:sortableDataTable1";
    private static final String SORTABLE_DATATABLE_WITHOUT_AJAX = "formID:sortableDataTable";

    @Test(groups = {"component"})
    public void testSorting_withoutAjax() {
        navigateTo(PAGE);

        final Table table = getControlFactory().getDataTable(SORTABLE_DATATABLE_WITHOUT_AJAX);

        List<String> firstColumnData = getTableData(table, 0);
        List<String> secondColumnData = getTableData(table, 1);

        assertColumnsAreEqual(firstColumnData, secondColumnData);

        table.header().nextRow().nextCell().click();
        Collections.reverse(firstColumnData);

        secondColumnData = getTableData(table, 1);
        assertColumnsAreEqual(firstColumnData, secondColumnData);
    }

    @Test(groups = {"component"}, enabled = false)
    public void testSorting_withAjax() {
        navigateTo(PAGE);

        final Table table = getControlFactory().getDataTable(SORTABLE_DATATABLE_WITH_AJAX);

        List<String> firstColumnData = getTableData(table, 0);
        List<String> secondColumnData = getTableData(table, 1);

        assertColumnsAreEqual(firstColumnData, secondColumnData);

        table.header().nextRow().nextCell().clickAndWait();
        Collections.reverse(firstColumnData);

        secondColumnData = getTableData(table, 1);
        assertColumnsAreEqual(firstColumnData, secondColumnData);
    }

    @Test(groups = {"component"}, enabled = false)
    public void testFilteringWithInputTextField() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testFilteringWithCombobox() {

    }

    @Test(groups = {"component"}, enabled = false)
    public void testFilteringWithDropDown() {

    }

    private List<String> getTableData(Table table, int index) {
        final List<TableRow> rows = table.body().rows();
        List<String> columnData = newArrayList();

        for (TableRow row : rows) {
            columnData.add(row.cell(index).text());
        }

        return columnData;
    }

    private void assertColumnsAreEqual(List<String> firstColumn, List<String> secondColumn) {
        final Iterator<String> iterator = firstColumn.iterator();
        for (String expected : secondColumn) {
            final String actual = iterator.next();
            assertThat("Cells are not equal!", actual.split("_")[1], is(expected.split("_")[1]));
        }
    }
}
