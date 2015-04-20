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

import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.testng.annotations.Test;

/**
 * @author Max Yurin
 */
public class DataTableTest_FilteringAndSorting extends BaseSeleniumTest {
    private static final String TABLE_ID = "formID:twoHeadersTable";
    private FuncTestsPages page = FuncTestsPages.DATATABLE_COLUMN_GROUPS;

    @Test(groups = {"component"}, enabled = false)
    public void testSorting(){

    }

    @Test(groups = {"component"}, enabled = false)
    public void testFilteringWithInputTextField(){

    }

    @Test(groups = {"component"}, enabled = false)
    public void testFilteringWithCombobox(){

    }

    @Test(groups = {"component"}, enabled = false)
    public void testFilteringWithDropDown(){

    }
}
