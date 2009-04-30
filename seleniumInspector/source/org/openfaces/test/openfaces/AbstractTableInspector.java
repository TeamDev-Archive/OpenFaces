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

import org.openfaces.test.ElementInspector;
import org.openfaces.test.html.TableInspector;

/**
 * @author Dmitry Pikhulya
 */
public class AbstractTableInspector extends TableInspector {

    private LoadingMode loadingMode = LoadingMode.AJAX;

    public AbstractTableInspector(String locator) {
        super(locator);
    }

    public AbstractTableInspector(ElementInspector tableElement) {
        super(tableElement);
    }

    public void setLoadingMode(LoadingMode loadingMode) {
        this.loadingMode = loadingMode;
    }

    public LoadingMode getLoadingMode() {
        return loadingMode;
    }

    public DataTableColumnInspector column(int columnIndex) {
        return new DataTableColumnInspector(this, columnIndex, getLoadingMode());
    }
}
