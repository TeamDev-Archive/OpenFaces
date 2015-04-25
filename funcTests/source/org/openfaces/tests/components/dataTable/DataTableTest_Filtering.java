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
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * @author Max Yurin
 */
public class DataTableTest_Filtering extends BaseSeleniumTest {
    private static final FuncTestsPages PAGE = FuncTestsPages.DATATABLE_FILTERING;
    private static final String FILTERING_WITH_COMBOBOX = "formID:filterableDataTable_comboBox";
    private static final String FILTERING_WITH_DROPDOWN_FIELD = "formID:filterableDataTable_dropDownField";
    private static final String FILTERING_WITH_INPUT_FIELD = "formID:filterableDataTable_searchField";

    @Test(groups = {"component"})
    public void testFilteringWithInputTextField() {
        navigateTo(PAGE);

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_INPUT_FIELD);

        assertThat("Check count not filtered rows of table: ", table.body().rowsCount(), is(9));

        final Filter filter = table.createFilter(Filter.FilterType.INPUTTEXT);
        filter.doFilter("row1");
        sleep(4000);

        assertThat("Check count filtered rows of table: ", table.body().rowsCount(), is(not(9)));
        assertThat("Check count filtered rows of table: ",table.body().rowsCount(), is(1));
    }

    @Test(groups = {"component"})
    public void testFilteringWithCombobox() {
        navigateTo(PAGE);
        final String col3_row1 = "col3_row1";

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_COMBOBOX);

        assertThat("Check count not filtered rows of table: ", table.body().rowsCount(), is(9));

        final Filter filter = table.createFilter(Filter.FilterType.COMBOBOX);

        filter.doFilter(col3_row1);
        sleep(2000);

        assertThat("Check count filtered rows of table: ",table.body().rowsCount(), is(1));
        assertThat("Check count filtered rows of table: ", table.body().nextRow().nextCell().text(), is(col3_row1));
    }

    @Test(groups = {"component"}, enabled = false)
    public void testFilteringWithDropDown() {
        navigateTo(PAGE);

        final Table table = getControlFactory().getDataTable(FILTERING_WITH_DROPDOWN_FIELD);

        assertThat("Check count not filtered rows of table: ", table.body().rowsCount(), is(9));

        final Filter filter = table.createFilter(Filter.FilterType.DROPDOWN);
        filter.doFilter("row1");
        sleep(4000);

        assertThat("Check count filtered rows of table: ", table.body().rowsCount(), is(not(9)));
        assertThat("Check count filtered rows of table: ",table.body().rowsCount(), is(1));
    }
}
