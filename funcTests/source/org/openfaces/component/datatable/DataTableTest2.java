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
package org.openfaces.component.datatable;

import org.openfaces.test.BaseSeleniumTest;
import org.testng.annotations.Test;
import org.seleniuminspector.openfaces.OpenFacesAjaxLoadingMode;
import org.seleniuminspector.openfaces.TabSetInspector;
import org.seleniuminspector.openfaces.DataTableInspector;
import org.seleniuminspector.openfaces.InputTextFilterInspector;
import org.seleniuminspector.ServerLoadingMode;
import org.seleniuminspector.LoadingMode;
import org.seleniuminspector.ElementInspector;

/**
 * @author Dmitry Pikhulya
 */
public class DataTableTest2 extends BaseSeleniumTest {

     @Test
    public void testFilteringSearchField() {
        filteringSearchField(OpenFacesAjaxLoadingMode.getInstance());
        filteringSearchField(ServerLoadingMode.getInstance());
    }

    private void filteringSearchField(LoadingMode loadingMode) {
        testAppFunctionalPage("/components/datatable/datatableFilteringSearchField.jsf");

        TabSetInspector loadingModes = tabSet("formID:loadingModes");
        DataTableInspector dataTable = dataTable("formID:filterableDataTable_searchField");
        if (loadingMode instanceof ServerLoadingMode) {
            loadingModes.tabs().get(1).clickAndWait();
        }

        dataTable.setLoadingMode(loadingMode);
        dataTable.column(0).filter(InputTextFilterInspector.class, "formID:filterableDataTable_searchField:filter1").makeFiltering("col3_row1");

        element("formID:filterableDataTable_searchField:filterableDataTable_searchField_firstHeader")
                .assertText("first column header");
        element("formID:filterableDataTable_searchField:filterableDataTable_searchField_secondHeader")
                .assertText("second column header");
        element("formID:filterableDataTable_searchField:filterableDataTable_searchField_firstFooter")
                .assertText("first column footer");
        element("formID:filterableDataTable_searchField:filterableDataTable_searchField_secondFooter")
                .assertText("second column footer");

        DataTableUtils.TestDataTableItem referenceFilteredRow = DataTableUtils.TWO_STRING_COLUMN_LIST.get(0);

        //check is right row appeared after filtering and other rows is non-visible
        ElementInspector firstBody = element("formID:filterableDataTable_searchField:0:filterableDataTable_searchField_firstBody");
        firstBody.assertVisible(true);
        ElementInspector secondBody = element("formID:filterableDataTable_searchField:0:filterableDataTable_searchField_secondBody");
        secondBody.assertVisible(true);
        firstBody.assertText(referenceFilteredRow.getFirstColumn());
        secondBody.assertText(referenceFilteredRow.getSecondColumn());

        for (int i = 1; i < 9; i++) {
            element("formID:filterableDataTable_searchField:" + i + ":filterableDataTable_searchField_firstBody")
                    .assertElementExists(false);
            element("formID:filterableDataTable_searchField:" + i + ":filterableDataTable_searchField_secondBody")
                    .assertElementExists(false);
        }

        if (loadingMode instanceof ServerLoadingMode) {
            // reset tab index for possible further tests
            loadingModes.tabs().get(0).clickAndWait();
        }

    }



}
