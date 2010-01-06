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
package org.openfaces.component.table;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class DataTableSelection extends AbstractTableSelection {
    protected DataTableSelection() {
    }

    protected DataTableSelection(TableDataModel model) {
        super(model);
    }

    @Override
    public void setParent(UIComponent parent) {
        if (parent != null && !(parent instanceof DataTable))
            throw new FacesException(getClass().getName() + " component can only be placed in a DataTable component, but it was placed into a component with class: " + parent.getClass().getName() + "; if you need to add selection support to a TreeTable use SingleNodeSelection or MultipleNodeSelection component instead.");
        super.setParent(parent);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public abstract List getSelectedRowKeys();

    protected Object getRowKeyByRowIndex(int index) {
        getModel().setRowIndex(index);
        if (!getModel().isRowAvailable())
            return null;
        return getModel().getRowKey();
    }

    protected Object getRowDataByRowIndex(int index) {
        getModel().setRowIndex(index);
        if (!getModel().isRowAvailable())
            return null;
        return getModel().getRowData();
    }

    protected Object getRowKeyByRowData(Object data) {
        TableDataModel model = getModel();
        model.setRowData(data);
        if (!model.isRowAvailable()) {
            Object rowKey = model.requestRowKeyByRowData(getFacesContext(), null, null, data, -1, -1);
            if (rowKey instanceof DefaultRowKey && ((DefaultRowKey) rowKey).getRowIndex() == -1) {
                return null;
            }
            return rowKey;
        }
        return model.getRowKey();
    }

    protected int getRowIndexByRowKey(Object id) {
        getModel().setRowKey(id);
        if (!getModel().isRowAvailable())
            return -1;
        return getModel().getRowIndex();
    }

    protected Object getRowDataByRowKey(Object id) {
        TableDataModel.RowInfo rowInfo = getModel().getRowInfoByRowKey(id);
        return rowInfo.getRowData();
    }


    protected int getRowIndexByRowData(Object data) {
        getModel().setRowData(data);
        if (!getModel().isRowAvailable())
            return -1;
        return getModel().getRowIndex();
    }

}
