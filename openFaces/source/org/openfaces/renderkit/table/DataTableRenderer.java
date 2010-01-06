/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.table;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.DataTable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class DataTableRenderer extends AbstractTableRenderer {

    @Override
    public void decode(FacesContext context, UIComponent uiComponent) {
        super.decode(context, uiComponent);
        if (!uiComponent.isRendered())
            return;
        DataTable table = (DataTable) uiComponent;
        decodePagination(context, table);
    }

    private void decodePagination(FacesContext context, DataTable table) {
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String pagingAction = (String) requestParameterMap.get(table.getClientId(context) + "::pagination");
        if (pagingAction != null && pagingAction.length() > 0)
            DataTablePaginatorRenderer.executePaginationAction(context, table, pagingAction);
    }

    @Override
    protected boolean getUseKeyboardForPagination(AbstractTable table) {
        DataTable dataTable = (DataTable) table;
        return dataTable.getPageSize() > 0 && dataTable.isPaginationKeyboardSupport();
    }

    @Override
    protected boolean canSelectLastPage(AbstractTable table) {
        return DataTablePaginatorRenderer.canSelectLastPage((DataTable) table);
    }

    @Override
    protected boolean canPageForth(AbstractTable table) {
        return DataTablePaginatorRenderer.canSelectNextPage((DataTable) table);
    }

    @Override
    protected boolean canPageBack(AbstractTable table) {
        return DataTablePaginatorRenderer.canSelectPreviousPage((DataTable) table);
    }

    @Override
    protected String getInitJsAPIFunctionName() {
        return "O$.Table._initDataTableAPI";
    }

}