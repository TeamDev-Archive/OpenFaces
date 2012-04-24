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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author andrii.loboda
 */
public class MultipleCellSelection extends AbstractCellSelection {
    public static final String COMPONENT_TYPE = "org.openfaces.MultipleCellSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.MultipleCellSelection";
    public static final String CELL_IDS_PROPERTY = "cellIds";

    private List<CellId> cellIds;
    private List<CellId> rowKeyCellIds;


    public MultipleCellSelection() {
    }

    public MultipleCellSelection(TableDataModel model) {
        super(model);
    }

    @Override
    public Object saveState(FacesContext context) {
        rememberByKeys();
        Object superState = super.saveState(context);
        return new Object[]{superState, rowKeyCellIds};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        final List<CellId> rowKeyCellIds = (List<CellId>) stateArray[1];
        setRowKeyCellIds(rowKeyCellIds);
        setColumnsForCellsId(rowKeyCellIds);
    }

    private void setColumnsForCellsId(List<CellId> rowKeyCellIds) {
        List<CellId> tempCellIds = new ArrayList<CellId>(rowKeyCellIds.size());
        for (CellId tempCell : rowKeyCellIds) {
            tempCellIds.add(new CellId(null, tempCell.getColumnId()));
        }
        this.cellIds = tempCellIds;
    }


    @Override
    public Mode getSelectionMode() {
        return Mode.MULTIPLE;
    }

    @Override
    public void rememberByKeys() {
        setRowKeyCellIds(getRowKeyCellIds());
    }

    @Override
    protected void readSelectionFromBinding() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueExpression selectedCellIds = getValueExpression(CELL_IDS_PROPERTY);

        if (selectedCellIds != null) {
            List<CellId> cellIds = objectToList(selectedCellIds.getValue(facesContext.getELContext()), CELL_IDS_PROPERTY);
            if (cellIds == null) {
                cellIds = Collections.emptyList();
            }
            setCellIds(cellIds);
        } else {
            setCellIds(new ArrayList<CellId>());
        }
    }

    @Override
    protected void writeSelectionToBinding() {
        ValueBindings.setFromList(this, CELL_IDS_PROPERTY, getCellIds());
    }

    @Override
    protected List<?> encodeSelectionIntoIndexes() {
        try {
            final List<CellId> cellIds = getCellIds();
            List<JSONArray> jsonArrayList = new ArrayList<JSONArray>(cellIds.size());
            for (CellId cellId : cellIds) {
                jsonArrayList.add(transformToJSON(cellId));
            }
            return jsonArrayList;
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }

    }

    @Override
    protected void decodeSelectionFromIndexes(List<?> indexes) {
        if (indexes.size() != 0) {
            /*In this implementation decodeSelectionFrom indexes should always be of List<JSONArray> class
            */
            @SuppressWarnings("unchecked") List<JSONArray> selectedCellsIndexes = (List<JSONArray>) indexes;
            List<CellId> cellIds = new ArrayList<CellId>(selectedCellsIndexes.size());
            try {
                for (JSONArray item : selectedCellsIndexes) {
                    Integer rowIndex = (Integer) item.get(0);
                    String columnId = (String) item.get(1);
                    cellIds.add(new CellId(getRowDataByRowIndex(rowIndex), columnId));
                }
                setCellIds(cellIds);
            } catch (JSONException e) {
                throw new IllegalArgumentException("Cell selection doesn't have correct attributes : rowIndex and columnId", e);
            }
        } else {
            setCellIds(new ArrayList<CellId>());
        }
    }

    private void setCellIds(List<CellId> cellIds) {
        this.cellIds = cellIds;
        this.rowKeyCellIds = Collections.emptyList();
    }

    private List<CellId> getCellIds() {
        List<CellId> cellIds = new ArrayList<CellId>();

        if (this.cellIds != null || this.rowKeyCellIds != null) {
            int length = (this.cellIds == null) ? this.rowKeyCellIds.size() : this.cellIds.size();
            for (int i = 0; i < length; i++) {
                CellId cellId = (this.cellIds != null) ? this.cellIds.get(i) : null;
                CellId rowKeyCellId = (this.rowKeyCellIds != null && this.rowKeyCellIds.size() >= this.cellIds.size()) ? this.rowKeyCellIds.get(i) : null;
                Object rowData = null;
                String columnId = null;
                if (cellId != null) {
                    if (cellId.getRowData() != null) {
                        rowData = cellId.getRowData();
                    }
                    columnId = cellId.getColumnId();
                }

                if (rowKeyCellId != null) {
                    rowData = getRowDataByRowKey(rowKeyCellId.getRowData());
                }
                cellIds.add(new CellId(rowData, columnId));
            }
            return cellIds;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    private List<CellId> getRowKeyCellIds() {
        List<CellId> tempRowKeysCellIds = new ArrayList<CellId>();
        Object rowKey = null;
        if (cellIds != null) {
            for (int i = 0; i < cellIds.size(); i++) {
                CellId cellId = cellIds.get(i);
                if (this.rowKeyCellIds.size() >= cellIds.size()) {
                    rowKey = rowKeyCellIds.get(i).getRowData();
                } else {
                    if (cellId != null && cellId.getRowData() != null) {
                        rowKey = getRowKeyByRowData(cellId.getRowData());
                    } else {
                        rowKey = null;
                    }
                }
                tempRowKeysCellIds.add(new CellId(rowKey, null));
            }
        }
        return tempRowKeysCellIds;
    }

    private void setRowKeyCellIds(List<CellId> rowKeyCellIds) {
        List<CellId> tempCellIds = new ArrayList<CellId>();
        List<CellId> tempRowKeysCellIds = new ArrayList<CellId>();
        for (int i = 0; i < rowKeyCellIds.size(); i++) {
            CellId rowKeyCell = rowKeyCellIds.get(i);
            CellId cellId = (this.cellIds != null && this.cellIds.size() >= rowKeyCellIds.size()) ? this.cellIds.get(i) : null;
            tempRowKeysCellIds.add(new CellId(rowKeyCell == null ? null : rowKeyCell.getRowData(), cellId == null ? null : cellId.getColumnId()));
            tempCellIds.add(new CellId(null, cellId == null ? null : cellId.getColumnId()));
        }

        this.rowKeyCellIds = tempRowKeysCellIds;
        this.cellIds = tempCellIds;

    }

}
