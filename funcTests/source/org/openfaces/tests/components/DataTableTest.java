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

package org.openfaces.tests.components;

import org.inspector.components.table.Table;
import org.inspector.components.table.TableCellParams;
import org.inspector.components.table.TableHeader;
import org.inspector.components.table.TableRow;
import org.inspector.navigator.FuncTestsPages;
import org.openfaces.tests.common.BaseSeleniumTest;
import org.testng.annotations.Test;

import java.util.Iterator;

/**
 * @author Max Yurin
 */
public class DataTableTest extends BaseSeleniumTest {
    private static final String TABLE_ID = "formID:twoHeadersTable";
    private FuncTestsPages page = FuncTestsPages.DATATABLE_COLUMN_GROUPS;

    @Test(groups = {"component"})
    public void testColumnGroupsHeaders() {
        navigateTo(page);
        final Table table = getControlFactory().getDataTable(TABLE_ID);
        final TableHeader header = table.header();
        final Iterator<TableRow> iterator = header.iterator();

        final TableRow firstHeaderRow = iterator.next();
        firstHeaderRow.assertCellParams(getFirstHeader());

        final TableRow secondHeaderRow = iterator.next();
        secondHeaderRow.assertCellParams(getSecondHeader());
    }

    private TableCellParams[] getFirstHeader() {
        return new TableCellParams[]{
                new TableCellParams("", 1, 2,
                        "border-right: 1px solid #a0a0a0; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("String Fields", 4, 1,
                        "background: SteelBlue; border-right: 1px solid #a0a0a0; border-bottom: ? none ?"),
                new TableCellParams("Integer Fields", 3, 1,
                        "background: ForestGreen; border-right: ? none ?; border-bottom: ? none ?")
        };
    }

    private TableCellParams[] getSecondHeader() {
        return new TableCellParams[]{
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
        };
    }

    private TableCellParams[] getThirdHeader() {
        return new TableCellParams[]{
                new TableCellParams("Int Field 1", 1, 1,
                        "background: LawnGreen; border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("Int Field 2", 1, 1,
                        "background: LawnGreen; border-right: ? none ?; border-bottom: 1px solid #a0a0a0"),
                new TableCellParams("Int Field 3", 1, 1,
                        "background: LawnGreen; border-right: ? none ?; border-bottom: 1px solid #a0a0a0")
        };
    }


}
