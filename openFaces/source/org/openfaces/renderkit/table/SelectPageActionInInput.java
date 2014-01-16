/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.renderkit.table;

import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.DataTablePaginatorAction;

/**
 * @author Sergey Pensov
 *         To change this template use File | Settings | File Templates.
 */
public class SelectPageActionInInput implements DataTablePaginatorAction {
    private int newPageIndex;

    SelectPageActionInInput(int newPageIndex) {
        this.newPageIndex = newPageIndex -1;
    }

    public void execute(DataTable table) {
        int pageIndex = table.getPageIndex();
        if (newPageIndex != pageIndex)
            table.setPageIndex(newPageIndex);
    }
}
