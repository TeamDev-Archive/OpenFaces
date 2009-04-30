/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.test.openfaces;

import org.openfaces.component.table.TextSearchDataTableFilter;
import org.openfaces.util.RenderingUtil;

/**
 * @author Andrii Gorbatov
 */
public class DropDownFieldFilterInspector extends DataTableFilterInspector {

    public DropDownFieldFilterInspector(String locator, LoadingMode loadingMode) {
        super(locator, loadingMode);
    }

    @Deprecated
    public DropDownFieldInspector searchComponentOld() {
        return new DropDownFieldInspector(getLocator()
                + RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR
                + TextSearchDataTableFilter.SEARCH_COMPONENT_SUFFIX);
    }

    public DropDownFieldInspector searchComponent() {
        return new DropDownFieldInspector(getLocator());
    }


    public void makeFiltering(String filterValue) {
        searchComponent().field().type(filterValue);
        searchComponent().field().setCursorPosition(0);
        searchComponent().field().keyPress(13);

        getLoadingMode().waitForLoadCompletion();
    }

}
