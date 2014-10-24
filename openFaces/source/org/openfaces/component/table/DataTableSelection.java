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
package org.openfaces.component.table;

import org.openfaces.component.table.impl.TableDataModel;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class DataTableSelection extends AbstractRowSelection {
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

    protected RowGroupingSelectionMode getRowGroupingSelectionMode() {
        DataTable table = (DataTable) getTable();
        RowGrouping rowGrouping = table.getRowGrouping();
        return rowGrouping != null ? rowGrouping.getSelectionMode() : null;
    }

    protected Object validateRowData(RowGroupingSelectionMode rowGroupingSelectionMode, Object rowData) {
        if (rowGroupingSelectionMode == null) return rowData;
        switch (rowGroupingSelectionMode) {
            case ALL_ROWS:
                return rowData;
            case DATA_ROWS:
                return rowData instanceof GroupHeaderOrFooter ? null : rowData;
            default:
                throw new IllegalStateException(
                        "Unknown value of RowGroupingSelectionMode property: " + rowGroupingSelectionMode);
        }
    }

    public abstract Object getFirstSelectedRowData();

}
