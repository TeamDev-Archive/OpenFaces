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
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

/**
 * @author andrii.loboda
 */
public class SingleCellSelection extends AbstractCellSelection {
    public static final String COMPONENT_TYPE = "org.openfaces.SingleCellSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.SingleCellSelection";

    private static final String CELL_ID_PROPERTY = "cellId";

    /*for saving state*/
    private CellId rowKeyCellId;

    private CellId cellId;

    public SingleCellSelection() {
    }

    public SingleCellSelection(TableDataModel model) {
        super(model);
    }

    @Override
    public Object saveState(FacesContext context) {
        rememberByKeys();
        Object superState = super.saveState(context);
        return new Object[]{superState, rowKeyCellId};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        final CellId rowKeyCellId = (CellId) stateArray[1];
        setRowKeyCellId(rowKeyCellId);
        cellId = new CellId(null, rowKeyCellId.getColumnId());
    }


    public void rememberByKeys() {
        setRowKeyCellId(getRowKeyCellId());
    }

    protected void readSelectionFromBinding() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueExpression selectedCellId = getValueExpression(CELL_ID_PROPERTY);

        if (selectedCellId != null) {
            CellId cellId = (CellId) selectedCellId.getValue(facesContext.getELContext());
            if (cellId != null) {
                setCellId(cellId.getRowData(), cellId.getColumnId());
            }
        }
    }

    @Override
    protected void writeSelectionToBinding() {
        CellId cellId1 = getCellId();
        if (cellId1 != null && (cellId1.getRowData() == null || cellId1.getColumnId() == null)) {
            cellId1 = null;
        }
        ValueBindings.set(this, CELL_ID_PROPERTY, cellId1);
    }

    @Override
    public Mode getSelectionMode() {
        return Mode.SINGLE;
    }

    @Override
    protected List<?> encodeSelectionIntoIndexes() {
        try {
            return Collections.singletonList(transformToJSON(getCellId()));
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected void decodeSelectionFromIndexes(List<?> indexes) {
        if (indexes.size() != 0) {
            JSONArray item = (JSONArray) indexes.get(0);
            try {
                Integer rowIndex = (Integer) item.get(0);
                String columnId = (String) item.get(1);
                setCellId(getRowDataByRowIndex(rowIndex), columnId);
            } catch (JSONException e) {
                throw new IllegalArgumentException("Cell selection doesn't have correct attributes : rowIndex and columnId", e);
            }
        } else {
            setCellId(getRowDataByRowIndex(-1), null);
        }
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }


    public void setCellId(Object rowData, String columnId) {
        cellId = new CellId(rowData, columnId);
        rowKeyCellId = null;
    }

    public CellId getCellId() {
        Object rowData = null;
        String columnId = null;
        if (cellId != null) {
            if (cellId.getRowData() != null) {
                rowData = cellId.getRowData();
            }
            columnId = cellId.getColumnId();
        }

        if (rowKeyCellId != null) {
            if (rowKeyCellId.getRowData() != null) {
                rowData = getRowDataByRowKey(rowKeyCellId.getRowData());
            }
        }
        return new CellId(rowData, columnId);
    }


    private CellId getRowKeyCellId() {
        Object rowKey = null;
        if (rowKeyCellId != null) {
            rowKey = rowKeyCellId.getRowData();
        } else {

            if (cellId != null && cellId.getRowData() != null) {
                rowKey = getRowKeyByRowData(cellId.getRowData());
            } else {
                rowKey = null;
            }
        }
        return new CellId(rowKey, null);
    }

    private void setRowKeyCellId(CellId rowKeyCell) {
        rowKeyCellId = new CellId(rowKeyCell == null ? null : rowKeyCell.getRowData(), cellId == null ? null : cellId.getColumnId());
        cellId = new CellId(null, cellId == null ? null : cellId.getColumnId());
    }

}
