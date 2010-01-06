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

import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class MultipleRowSelection extends DataTableSelection {
    public static final String COMPONENT_TYPE = "org.openfaces.MultipleRowSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.MultipleRowSelection";

    static final String ROW_INDEXES_PROPERTY = "rowIndexes";
    static final String ROW_DATAS_PROPERTY = "rowDatas";
    static final String ROW_KEYS_PROPERTY = "rowKeys";

    private List<Integer> rowIndexes;
    private List<Object> rowKeys;
    private List<Object> rowDatas;

    public MultipleRowSelection() {
    }

    public MultipleRowSelection(TableDataModel model) {
        super(model);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public List getSelectedRowKeys() {
        return getRowKeys();
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        rememberByKeys();
        Object superState = super.saveState(context);
        return new Object[]{superState, saveAttachedState(context, rowKeys)};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        setRowKeys((List<Object>) restoreAttachedState(context, stateArray[1]));
    }

    public void setRowIndexes(List<Integer> rowIndexes) {
        this.rowIndexes = rowIndexes != null ? new ArrayList<Integer>(rowIndexes) : null;
        rowKeys = null;
        rowDatas = null;
    }

    protected void setRowKeys(List<Object> rowKeys) {
        this.rowKeys = rowKeys != null ? new ArrayList<Object>(rowKeys) : null;
        rowIndexes = null;
        rowDatas = null;
    }

    public void setRowDatas(List<Object> rowDatas) {
        this.rowDatas = rowDatas != null ? new ArrayList<Object>(rowDatas) : null;
        rowIndexes = null;
        rowKeys = null;
    }

    public List<Integer> getRowIndexes() {
        if (rowIndexes != null)
            return rowIndexes;
        if (rowKeys != null) {
            List<Integer> result = new ArrayList<Integer>(rowKeys.size());
            for (Object id : rowKeys) {
                int rowIndexByRowKey = getRowIndexByRowKey(id);
                if (rowIndexByRowKey != -1)
                    result.add(rowIndexByRowKey);
            }
            return Collections.unmodifiableList(result);
        } else if (rowDatas != null) {
            List<Integer> result = new ArrayList<Integer>(rowDatas.size());
            for (Object data : rowDatas) {
                int rowIndexByRowData = getRowIndexByRowData(data);
                if (rowIndexByRowData != -1)
                    result.add(rowIndexByRowData);
            }
            return Collections.unmodifiableList(result);
        } else {
            return Collections.emptyList();
        }
    }

    protected List<Object> getRowKeys() {
        if (rowKeys != null)
            return rowKeys;
        if (rowIndexes != null) {
            List<Object> result = new ArrayList<Object>(rowIndexes.size());
            for (Object myRowIndexe : rowIndexes) {
                int index = (Integer) myRowIndexe;
                Object rowKey = getRowKeyByRowIndex(index);
                if (rowKey != null)
                    result.add(rowKey);
            }
            return Collections.unmodifiableList(result);
        } else if (rowDatas != null) {
            List<Object> result = new ArrayList<Object>(rowDatas.size());
            for (Object data : rowDatas) {
                Object rowKey = getRowKeyByRowData(data);
                if (rowKey != null)
                    result.add(rowKey);
            }
            return Collections.unmodifiableList(result);
        } else {
            return Collections.emptyList();
        }
    }

    public List<Object> getRowDatas() {
        if (rowDatas != null)
            return rowDatas;
        if (rowIndexes != null) {
            List<Object> result = new ArrayList<Object>(rowIndexes.size());
            for (Object myRowIndexe : rowIndexes) {
                Integer index = (Integer) myRowIndexe;
                Object rowData = getRowDataByRowIndex(index);
                if (rowData != null)
                    result.add(rowData);
            }
            return result;
        } else if (rowKeys != null) {
            List<Object> result = new ArrayList<Object>(rowKeys.size());
            for (Object id : rowKeys) {
                Object rowData = getRowDataByRowKey(id);
                if (rowData != null)
                    result.add(rowData);
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public void rememberByKeys() {
        setRowKeys(getRowKeys());
    }

    protected void readSelectionFromBinding() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        ValueExpression selectedRowDatas = getValueExpression(ROW_DATAS_PROPERTY);
        ELContext elContext = facesContext.getELContext();
        if (selectedRowDatas != null) {
            Object value = selectedRowDatas.getValue(elContext);
            setRowDatas(objectToList(value, ROW_DATAS_PROPERTY));
            return;
        }

        ValueExpression selectedRowIndexes = getValueExpression(ROW_INDEXES_PROPERTY);
        if (selectedRowIndexes != null) {
            Object value = selectedRowIndexes.getValue(elContext);
            List<Integer> indexes = objectToList(value, ROW_INDEXES_PROPERTY);
            if (indexes != null) {
                setRowIndexes(indexes);
            }
        }

    }

    protected void writeSelectionToBinding() {
        ValueBindings.setFromList(this, ROW_INDEXES_PROPERTY, getRowIndexes());
        ValueBindings.setFromList(this, ROW_DATAS_PROPERTY, getRowDatas());
    }

    public boolean isMultipleSelectionAllowed() {
        return true;
    }

    protected List<Integer> encodeSelectionIntoIndexes() {
        List<Integer> selectedRowIndexes = getRowIndexes();
        return selectedRowIndexes;
    }

    protected void decodeSelectionFromIndexes(List<Integer> indexes) {
        int selectedItemCount = indexes.size();
        List<Object> allSelectedRowKeys = (getRowKeys() != null)
                ? getRowKeys() : new ArrayList<Object>();

        List<Object> currentSelectedRowKeys = getSelectedRowKeys(selectedItemCount, indexes);

        processSelectionRowsKeys(currentSelectedRowKeys, allSelectedRowKeys);

        setRowKeys(allSelectedRowKeys);
    }

    /**
     * This method add newly selected rowKeys to allSelectedRowKeys, and removes unselected rowKeys
     *
     * @param currentSelectedRowKeys - list of RowKeys of currently selected rows
     * @param allSelectedRowKeys     - list of all RowKeys that in multipleselection now
     */
    private void processSelectionRowsKeys(List<Object> currentSelectedRowKeys, List<Object> allSelectedRowKeys) {
        TableDataModel dataModel = getModel();
        int rowCount = dataModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            dataModel.setRowIndex(i);
            Object currentRowKey = dataModel.getRowKey();
            if (currentRowKey == null)
                continue;
            if (!currentSelectedRowKeys.contains(currentRowKey)) {
                if (allSelectedRowKeys.contains(currentRowKey)) {
                    // Remove rowKey, because current row was unselected
                    allSelectedRowKeys.remove(currentRowKey);
                }
            } else {
                if (!allSelectedRowKeys.contains(currentRowKey)) {
                    // Add new rowKey, because row was selected
                    allSelectedRowKeys.add(currentRowKey);
                }
            }
        }
    }

    private List<Object> getSelectedRowKeys(int selectedItemCount, List<Integer> indexes) {
        List<Object> currentSelectedRowKeys = new ArrayList<Object>(selectedItemCount);
        for (int selectedItemIndex = 0; selectedItemIndex < selectedItemCount; selectedItemIndex++) {
            Integer itemIndex = indexes.get(selectedItemIndex);
            if (itemIndex != null && itemIndex != -1) {
                getModel().setRowIndex(itemIndex.intValue());
                Object currentIndexSelectedRowKey = getModel().getRowKey();
                currentSelectedRowKeys.add(currentIndexSelectedRowKey);
            }
        }
        return currentSelectedRowKeys;
    }

}
