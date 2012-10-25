/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.openfaces;

import org.openfaces.util.Rendering;
import org.openfaces.renderkit.table.DataTablePaginatorRenderer;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.ElementByReferenceInspector;
import org.seleniuminspector.ElementInspector;


/**
 * @author Andrii Gorbatov
 */
public class DataTablePaginatorInspector extends ElementByReferenceInspector {

    public DataTablePaginatorInspector(String locator) {
        super(new ElementByLocatorInspector(locator));
    }

    public DataTablePaginatorInspector(ElementInspector element) {
        super(element);
    }

    public ElementInspector nextPage() {
        return new ElementByLocatorInspector(id() + Rendering.SERVER_ID_SUFFIX_SEPARATOR
                + DataTablePaginatorRenderer.NEXT_PAGE_COMPONENT);
    }

    public ElementInspector previousPage() {
        return new ElementByLocatorInspector(id() + Rendering.SERVER_ID_SUFFIX_SEPARATOR
                + DataTablePaginatorRenderer.PREV_PAGE_COMPONENT);
    }

    public ElementInspector firstPage() {
        return new ElementByLocatorInspector(id() + Rendering.SERVER_ID_SUFFIX_SEPARATOR
                + DataTablePaginatorRenderer.FIRST_PAGE_COMPONENT);
    }

    public ElementInspector lastPage() {
        return new ElementByLocatorInspector(id() + Rendering.SERVER_ID_SUFFIX_SEPARATOR
                + DataTablePaginatorRenderer.LAST_PAGE_COMPONENT);
    }

    /**
     * Simple method just performs paging for the DataTable without any data verification
     *
     * @param numberOfPages number of pages in given DataTable
     */
    public void makePagination(int numberOfPages) { // todo: this method has a too specific purpose to be included into functionality of inspector -- this should be moved out as a functionality of OpenFaces tests
        for (int i = 1; i < numberOfPages; i++) {
            nextPage().clickAndWait();
            OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        }

        for (int i = 1; i < numberOfPages; i++) {
            previousPage().clickAndWait();
            OpenFacesAjaxLoadingMode.getInstance().waitForLoad();
        }

    }
}
