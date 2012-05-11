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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.DataTablePaginatorAction;

/**
 * @author Dmitry Pikhulya
 */
public class SelectPageNoAction implements DataTablePaginatorAction {
    private int pageNo;

    public SelectPageNoAction(int pageNo) {
        this.pageNo = pageNo;
    }

    public void execute(DataTable table) {
        int pageIndex = pageNo - 1;
        if (pageIndex < 0)
            pageIndex = 0;
        table.setPageIndex(pageIndex);
    }
}
