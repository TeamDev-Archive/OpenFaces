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

import org.inspector.api.Filter;
import org.inspector.api.Table;
import org.inspector.components.table.TableBody;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Max Yurin
 */
public class DataTableFilteringTestCase extends BaseSeleniumTest {
    private static final FuncTestsPages PAGE = FuncTestsPages.DATATABLE_FILTERING;
    private static final String TAB_SET_ID = "formID:loadingModes";
    private static final String FILTERING_WITH_COMBOBOX = "formID:filterableDataTable_comboBox";
    private static final String FILTERING_WITH_DROPDOWN_FIELD = "formID:filterableDataTable_dropDownField";
    private static final String FILTERING_WITH_INPUT_FIELD = "formID:filterableDataTable_searchField";

    private static final String COL3_ROW1 = "col3_row1";
    public static final String NO_AJAX_TAB = "false";
    public static final String AJAX_TAB = "true";

    @Test(groups = {"component"})
    public void testFilteringWithInputTextField() {
        navigateTo(PAGE);
        getControlFactory().getTabSet(TAB_SET_ID).openTab(NO_AJAX_TAB);

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_INPUT_FIELD);
        final TableBody body = table.body();

        assertThat("Check count not filtered rows of table: ", body.rowsCount(), is(9));

        final Filter filter = table.filter(Filter.FilterType.INPUTTEXT);
        filter.doFilter("row1");
        sleep(4000);

        assertThat("Check count filtered rows of table: ", body.rowsCount(), is(not(9)));
        assertThat("Check count filtered rows of table: ", body.rowsCount(), is(1));
    }

    @Test(groups = {"component"})
    public void testFilteringWithInputTextField_WithAjax() {
        navigateTo(PAGE);
        getControlFactory().getTabSet(TAB_SET_ID).openTab(AJAX_TAB);

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_INPUT_FIELD);
        final TableBody body = table.body();

        assertThat("Check count not filtered rows of table: ", body.rowsCount(), is(9));

        final Filter filter = table.filter(Filter.FilterType.INPUTTEXT);
        filter.doFilter("row1");
        sleep(4000);

        assertThat("Check count filtered rows of table: ", body.rowsCount(), is(not(9)));
        assertThat("Check count filtered rows of table: ", body.rowsCount(), is(1));
    }

    @Test(groups = {"component"})
    public void testFilteringWithCombobox() {
        navigateTo(PAGE);
        getControlFactory().getTabSet(TAB_SET_ID).openTab(NO_AJAX_TAB);

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_COMBOBOX);

        assertThat("Check count not filtered rows of table: ", table.body().rowsCount(), is(9));

        table.filter(Filter.FilterType.COMBOBOX).doFilter(COL3_ROW1);
        sleep(2000);

        assertThat("Check count filtered rows of table: ", table.body().rowsCount(), is(1));
        assertThat("Check count filtered rows of table: ", table.body().nextRow().nextCell().text(), is(COL3_ROW1));
    }

    @Test(groups = {"component"})
    public void testFilteringWithCombobox_WithAjax() {
        navigateTo(PAGE);
        getControlFactory().getTabSet(TAB_SET_ID).openTab(AJAX_TAB);

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_COMBOBOX);

        assertThat("Check count not filtered rows of table: ", table.body().rowsCount(), is(9));

        table.filter(Filter.FilterType.COMBOBOX).doFilter(COL3_ROW1);
        sleep(2000);

        assertThat("Check count filtered rows of table: ", table.body().rowsCount(), is(1));
        assertThat("Check count filtered rows of table: ", table.body().nextRow().nextCell().text(), is(COL3_ROW1));
    }

    @Test(groups = {"component"})
    public void testFilteringWithDropDown() {
        navigateTo(PAGE);
        getControlFactory().getTabSet(TAB_SET_ID).openTab(NO_AJAX_TAB);

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_DROPDOWN_FIELD);
        assertThat("Check count not filtered rows of table: ", table.body().rowsCount(), is(9));

        table.filter(Filter.FilterType.DROPDOWN).doFilter(COL3_ROW1);
        sleep(2000);

        assertThat("Check count filtered rows of table: ", table.body().rowsCount(), is(1));
        assertThat("Check count filtered rows of table: ", table.body().nextRow().nextCell().text(), is(COL3_ROW1));
    }

    @Test(groups = {"component"})
    public void testFilteringWithDropDown_WithAjax() {
        navigateTo(PAGE);
        getControlFactory().getTabSet(TAB_SET_ID).openTab(AJAX_TAB);

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_DROPDOWN_FIELD);
        assertThat("Check count not filtered rows of table: ", table.body().rowsCount(), is(9));

        table.filter(Filter.FilterType.DROPDOWN).doFilter(COL3_ROW1);
        sleep(2000);

        assertThat("Check count filtered rows of table: ", table.body().rowsCount(), is(1));
        assertThat("Check count filtered rows of table: ", table.body().nextRow().nextCell().text(), is(COL3_ROW1));
    }
}
