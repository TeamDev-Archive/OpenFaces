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
package org.seleniuminspector.openfaces;

import org.openfaces.component.table.TableColumn;
import org.openfaces.util.RenderingUtil;
import org.seleniuminspector.ElementByLocatorInspector;
import org.seleniuminspector.LoadingMode;

/**
 * @author Andrii Gorbatov
 */
public abstract class DataTableFilterInspector extends ElementByLocatorInspector {

    public enum FilterType {
        DROP_DOWN_FIELD(RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + TableColumn.DROP_DOWN_FILTER_CHILD_ID_SUFFIX),
        COMBO_BOX(RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + TableColumn.COMBO_BOX_FILTER_CHILD_ID_SUFFIX),
        SEARCH_FIELD(RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + TableColumn.SEARCH_FIELD_FILTER_CHILD_ID_SUFFIX);

        private String suffix;

        private FilterType(String suffix) {
            this.suffix = suffix;
        }

        public String getSuffix() {
            return this.suffix;
        }
    }

    private LoadingMode loadingMode;

    public DataTableFilterInspector(String locator, LoadingMode loadingMode) {
        super(locator);
        this.loadingMode = loadingMode;
    }

    public abstract void makeFiltering(String filterValue);

    protected LoadingMode getLoadingMode() {
        return loadingMode;
    }
}
