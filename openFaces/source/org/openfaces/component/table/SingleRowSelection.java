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

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class SingleRowSelection extends DataTableSelection {
    public static final String COMPONENT_TYPE = "org.openfaces.SingleRowSelection";
    public static final String COMPONENT_FAMILY = "org.openfaces.SingleRowSelection";

    //  private static final String ROW_KEY_PROPERTY = "rowKey";
    private static final String ROW_INDEX_PROPERTY = "rowIndex";
    private static final String ROW_DATA_PROPERTY = "rowData";

    // one or zero of the following variables can be non-null.
    private Integer rowIndex;
    private Object rowKey;
    private Object rowData;

    public SingleRowSelection() {
    }

    public SingleRowSelection(TableDataModel model) {
        super(model);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public List getSelectedRowKeys() {
        Object rowKey = getRowKey();
        return rowKey != null ? Collections.singletonList(rowKey) : Collections.EMPTY_LIST;
    }

    @Override
    public Object saveState(FacesContext context) {
        rememberByKeys();
        Object superState = super.saveState(context);
        return new Object[]{superState, rowKey};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        setRowKey(stateArray[1]);
    }

    public void setRowIndex(int index) {
        if (index < -1)
            throw new IllegalArgumentException("rowIndex shouldn't be less than -1: " + index);
        rowIndex = index;
        rowKey = null;
        rowData = null;
    }

    protected void setRowKey(Object id) {
        rowKey = id;
        rowIndex = null;
        rowData = null;
    }

    public void setRowData(Object data) {
        rowData = data;
        rowKey = null;
        rowIndex = null;
    }

    public int getRowIndex(Boolean unDisplayedSelectionAllowed) {
        int resultIndex;
        if (rowIndex != null)
            resultIndex = rowIndex;
        else if (rowKey != null) {
            int indexById = getRowIndexByRowKey(rowKey);
            resultIndex = indexById;
        } else if (rowData != null) {
            int indexByData = getRowIndexByRowData(rowData);
            resultIndex = indexByData;
        } else
            resultIndex = -1;

        if (resultIndex == -1 && !unDisplayedSelectionAllowed){
            rowIndex = null;
            rowKey = null;
            rowData = null;
            return -1;
        } else {
            return  resultIndex;
        }
    }

    protected Object getRowKey() {
        if (rowKey != null)
            return rowKey;

        if (rowIndex != null) {
            Object id = getRowKeyByRowIndex(rowIndex);
            return id;
        } else if (rowData != null) {
            Object id = getRowKeyByRowData(rowData);
            return id;
        } else {
            return null;
        }
    }

    public Object getRowData() {
        if (rowData != null)
            return rowData;

        if (rowIndex != null) {
            Object data = getRowDataByRowIndex(rowIndex);
            return data;
        } else if (rowKey != null) {
            Object data = getRowDataByRowKey(rowKey);
            return data;
        } else {
            return null;
        }
    }


    public void rememberByKeys() {
        setRowKey(getRowKey());
    }

    protected void readSelectionFromBinding() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueExpression selectedRowIndex = getValueExpression(ROW_INDEX_PROPERTY);
//    ValueExpression selectedRowKey = getValueExpression(ROW_KEY_PROPERTY);
//    if (selectedRowKey != null) {
//      setRowKey(selectedRowKey.getValue(facesContext.getELContext()));
//      return;
//    }

        ValueExpression selectedRowData = getValueExpression(ROW_DATA_PROPERTY);
        if (selectedRowData != null) {
            setRowData(selectedRowData.getValue(facesContext.getELContext()));
            return;
        }

        if (selectedRowIndex != null) {
            Integer index = (Integer) selectedRowIndex.getValue(facesContext.getELContext());
            if (index != null) {
                setRowIndex(index);
            }
        }
    }

    protected void writeSelectionToBinding() {
        RowGroupingSelectionMode selectionMode = getRowGroupingSelectionMode();

//    setBoundPropertyValue(this, ROW_KEY_PROPERTY, getRowKey());
        ValueBindings.set(this, ROW_INDEX_PROPERTY, getRowIndex(true));
        ValueBindings.set(this, ROW_DATA_PROPERTY, validateRowData(selectionMode, getRowData()));
    }

    @Override
    public Mode getSelectionMode() {
        return Mode.SINGLE;
    }

    @Override
    protected List<?> encodeSelectionIntoIndexes() {
        throw new RuntimeException();
    }

    @Override
    protected List<?> encodeSelectionIntoIndexes(Boolean unDisplayedSelectionAllowed) {
        return Collections.singletonList(getRowIndex(unDisplayedSelectionAllowed));
    }

    @Override
    protected void decodeSelectionFromIndexes(List<?> indexes) {
        Integer itemIndex = indexes.size() == 0 ? -1 : (Integer) indexes.get(0);
        setRowIndex(itemIndex);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object getFirstSelectedRowData(){
        return getRowData();
    }

}
