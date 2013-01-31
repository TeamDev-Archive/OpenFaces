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
        return new Object[]{superState, rowKeys};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        setRowKeys((List<Object>) stateArray[1]);
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

    public List<Integer> getRowIndexes(Boolean unDisplayedSelectionAllowed) {
        List<Object> rowsForRemove = new ArrayList<Object>();
        if (rowIndexes != null)
            return rowIndexes;
        if (rowKeys != null) {
            List<Integer> result = new ArrayList<Integer>(rowKeys.size());
            for (Object id : rowKeys) {
                int rowIndexByRowKey = getRowIndexByRowKey(id);
                if (rowIndexByRowKey != -1){
                    result.add(rowIndexByRowKey);
                }else{
                    rowsForRemove.add(id);
                }
            }
            if (!unDisplayedSelectionAllowed) rowKeys.removeAll(rowsForRemove);
            return Collections.unmodifiableList(result);
        } else if (rowDatas != null) {
            List<Integer> result = new ArrayList<Integer>(rowDatas.size());
            for (Object data : rowDatas) {
                int rowIndexByRowData = getRowIndexByRowData(data);
                if (rowIndexByRowData != -1){
                    result.add(rowIndexByRowData);
                }else{
                    rowsForRemove.add(data);
                }
            }
            if (!unDisplayedSelectionAllowed) rowDatas.removeAll(rowsForRemove);
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
            for (Object myRowIndexes : rowIndexes) {
                int index = (Integer) myRowIndexes;
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
            for (Object myRowIndexes : rowIndexes) {
                Integer index = (Integer) myRowIndexes;
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
            return new ArrayList<Object>();
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
        ValueBindings.setFromList(this, ROW_INDEXES_PROPERTY, getRowIndexes(true));
        ValueBindings.setFromList(this, ROW_DATAS_PROPERTY, validateRowDatas(getRowDatas()));
    }

    private List<Object> validateRowDatas(List<Object> rowDatas) {
        RowGroupingSelectionMode rowGroupingSelectionMode = getRowGroupingSelectionMode();
        if (rowGroupingSelectionMode == RowGroupingSelectionMode.ALL_ROWS) return rowDatas;
        List<Object> filteredList = new ArrayList<Object>(rowDatas.size());
        for (Object rowData : rowDatas) {
            Object validatedRowData = validateRowData(rowGroupingSelectionMode, rowData);
            if (validatedRowData != null)
                filteredList.add(validatedRowData);
        }
        return filteredList;
    }

    @Override
    public Mode getSelectionMode() {
        return Mode.MULTIPLE;
    }

    @Override
    protected List<?> encodeSelectionIntoIndexes() {
        throw new RuntimeException();
    }

    @Override
    protected List<?> encodeSelectionIntoIndexes(Boolean unDisplayedSelectionAllowed) {
        List<Integer> selectedRowIndexes = getRowIndexes(unDisplayedSelectionAllowed);
        return selectedRowIndexes;
    }

    @Override
    protected void decodeSelectionFromIndexes(List<?> indexes) {
        int selectedItemCount = indexes.size();
        List<Object> allSelectedRowKeys = (getRowKeys() != null)
                ? getRowKeys() : new ArrayList<Object>();
        /*getSelectedRowKeys can return List of Integer if it's rowSelection or
           JSONArray if it's cellSelection.
        In this implementation we use getSelectedRowKeys that return List of Integer*/
        @SuppressWarnings("unchecked") List<Object> currentSelectedRowKeys = getSelectedRowKeys(selectedItemCount, (List<Integer>) indexes);

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
            if (currentRowKey == null){
                continue;
            }
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

    @Override
    public Object getFirstSelectedRowData(){
        List<Object> rowDatas = getRowDatas();
        return rowDatas != null ? rowDatas.get(0) : null;
    }


}
