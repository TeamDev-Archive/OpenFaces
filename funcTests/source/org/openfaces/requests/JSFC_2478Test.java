/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.requests;

import org.junit.Test;
import org.openfaces.test.OpenFacesTestCase;
import org.seleniuminspector.openfaces.DataTableInspector;

/**
 * @author Darya Shumilina
 */
public class JSFC_2478Test extends OpenFacesTestCase {
     //@Test
    public void testJSFC_2478Fix() {
        testAppFunctionalPage("/requests/JSFC-2478.jsf");

        /* check DataTable without select all checkbox tag */
        DataTableInspector withoutSelectAllCheckboxDataTable = dataTable("formID:withoutSelectAllCheckboxDataTableID");
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            withoutSelectAllCheckboxDataTable.bodyRow(rowIndex).cell(0).click();
            withoutSelectAllCheckboxDataTable.bodyRow(rowIndex).cell(0).assertChecked(true);
        }
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            withoutSelectAllCheckboxDataTable.bodyRow(rowIndex).cell(0).click();
            withoutSelectAllCheckboxDataTable.bodyRow(rowIndex).cell(0).assertChecked(false);
        }

        /* check DataTable with select all checkbox tag */
        DataTableInspector withSelectAllCheckboxDataTable = dataTable("formID:withSelectAllCheckboxDataTableID");
        element("formID:withSelectAllCheckboxDataTableID:selectAllHeaderID").click();
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            withSelectAllCheckboxDataTable.bodyRow(rowIndex).cell(0).assertChecked(true);
        }
        element("formID:withSelectAllCheckboxDataTableID:selectAllFooterID").click();
        for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
            withSelectAllCheckboxDataTable.bodyRow(rowIndex).cell(0).assertChecked(false);
        }
    }

}