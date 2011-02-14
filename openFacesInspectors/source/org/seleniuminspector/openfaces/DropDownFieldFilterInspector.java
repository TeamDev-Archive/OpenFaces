/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.openfaces;

import org.openfaces.component.filter.TextSearchFilter;
import org.openfaces.util.Rendering;
import org.seleniuminspector.LoadingMode;

/**
 * @author Andrii Gorbatov
 */
public class DropDownFieldFilterInspector extends AbstractFilterInspector {

    public DropDownFieldFilterInspector(String locator, LoadingMode loadingMode) {
        super(locator, loadingMode);
    }

    @Deprecated
    public DropDownFieldInspector searchComponentOld() {
        return new DropDownFieldInspector(getLocator()
                + Rendering.SERVER_ID_SUFFIX_SEPARATOR
                + TextSearchFilter.SEARCH_COMPONENT_SUFFIX);
    }

    public DropDownFieldInspector searchComponent() {
        return new DropDownFieldInspector(getLocator());
    }


    public void makeFiltering(String filterValue) {
        searchComponent().field().type(filterValue);
        searchComponent().field().setCursorPosition(0);
        searchComponent().field().keyPress(13);

        getLoadingMode().waitForLoad();
    }

    public static void main(String[] args) {
        System.out.println((int) ('\r'));
    }

}
