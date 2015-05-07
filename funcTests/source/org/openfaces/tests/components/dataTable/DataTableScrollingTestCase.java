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

import org.inspector.components.table.DataTable;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.openqa.selenium.Dimension;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Max Yurin
 */
public class DataTableScrollingTestCase extends BaseSeleniumTest{
    private static final FuncTestsPages PAGE = FuncTestsPages.DATATABLE_GENERAL;
    private static final String TABLE_ID = "formID:banks";

    @Test(groups = {"components"})
    public void testHorizontalScrolBarIsVisible(){
        navigateTo(PAGE);

        final DataTable dataTable = getControlFactory().getDataTable(TABLE_ID);

        setWindowSize(new Dimension(400, 400));

        assertThat("Horizontal scrollbar is not displayed!", dataTable.hasHorizontalScrollBar(), is(true));
        assertThat("Vertical scrollbar is not displayed!", dataTable.hasVerticalScrollBar(), is(true));
    }
}
