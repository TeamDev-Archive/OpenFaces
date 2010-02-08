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

import org.apache.commons.collections.Predicate;
import org.openfaces.component.filter.AndFilterCriterion;
import org.openfaces.component.filter.Filter;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.PredicateBuilder;
import org.openfaces.util.DataUtil;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author Dmitry Pikhulya
 */

public class TableDataModel extends DataModel implements DataModelListener, Externalizable {
    private static final String VAR_FILTER_CRITERIA = "filterCriteria";
    private static final String VAR_PAGE_START = "pageStart";
    private static final String VAR_PAGE_SIZE = "pageSize";
    private static final String VAR_SORT_COLUMN_ID = "sortColumnId";
    private static final String VAR_SORT_COLUMN_INDEX = "sortColumnIndex";
    private static final String VAR_SORT_ASCENDING = "sortAscending";

    private Object wrappedData;
    /**
     * Should be used for iterating over rows if myExtractedRows is null.
     */
    private DataModel sourceDataModel;
    /**
     * If this field is non-null then mySourceDataModel shouldn't be used and myExtractedRows should be used instead.
     */
    private List<RowInfo> extractedRows;
    private List<Object> extractedRowKeys;
    private List<Object> allRetrievedRowKeys;
    private int extractedRowIndex = -1;
    private List<SortingRule> sortingRules;
    private List<Filter> filters;
    private int pageSize;
    private int pageIndex;
    private AbstractTable table;
    private ValueExpression rowKeyExpression;
    private ValueExpression rowDataByKeyExpression;

    private boolean internalIteration;
    private List<RowInfo> allRetrievedRows;
    private List<boolean[]> allRetrievedRowFilteringFlags;
    private List<Filter> currentlyAppliedFilters;
    private Integer totalRowCount;
    private int updateInProgress;
    private List<Object> previousRowKeys;

    public TableDataModel() {
        setWrappedData(null);
    }

    public TableDataModel(AbstractTable table) {
        this.table = table;
        setWrappedData(null);
    }

    public ValueExpression getRowKeyExpression() {
        return rowKeyExpression;
    }

    public void setRowKeyExpression(ValueExpression rowKeyExpression) {
        this.rowKeyExpression = rowKeyExpression;
    }

    public ValueExpression getRowDataByKeyExpression() {
        return rowDataByKeyExpression;
    }

    public void setRowDataByKeyExpression(ValueExpression rowDataByKeyBinding) {
        rowDataByKeyExpression = rowDataByKeyBinding;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        sortingRules = (List<SortingRule>) in.readObject();
        rowKeyExpression = ValueBindings.readValueExpression(in);
        rowDataByKeyExpression = ValueBindings.readValueExpression(in);
        pageSize = in.readInt();
        pageIndex = in.readInt();
        setWrappedData(null);

        // restoring old extracted row keys is needed for correct restoreRows/restoreRowIndexes functionality, which
        // in turn is required for correct data submission in case of concurrent data modifications
        extractedRowKeys = (List) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(sortingRules);
        ValueBindings.writeValueExpression(out, rowKeyExpression);
        ValueBindings.writeValueExpression(out, rowDataByKeyExpression);
        out.writeInt(pageSize);
        out.writeInt(pageIndex);
        out.writeObject(extractedRowKeys);
    }

    public Object getWrappedData() {
        return wrappedData;
    }

    public AbstractTable getTable() {
        return table;
    }

    public void setTable(AbstractTable table) {
        this.table = table;
    }

    public void setWrappedData(Object wrappedData) {
        this.wrappedData = wrappedData;
        DataModel dataModel = (wrappedData instanceof ValueExpression)
                ? new ValueExpressionDataModel((ValueExpression) wrappedData)
                : DataUtil.objectAsDataModel(wrappedData);
        setSourceDataModel(dataModel);
    }

    protected DataModel getSourceDataModel() {
        return sourceDataModel;
    }

    protected void setSourceDataModel(DataModel sourceDataModel) {
        if (this.sourceDataModel == sourceDataModel)
            return;
        if (this.sourceDataModel != null)
            this.sourceDataModel.removeDataModelListener(this);
        this.sourceDataModel = sourceDataModel;
        if (this.sourceDataModel != null)
            this.sourceDataModel.addDataModelListener(this);

        updateExtractedRows();
    }

    public boolean isRowAvailable() {
        if (extractedRows != null) {
            boolean rowIndexInRange = extractedRowIndex >= 0 && extractedRowIndex < extractedRows.size();
            return rowIndexInRange;
        }
        return sourceDataModel.isRowAvailable();
    }

    public int getRowCount() {
        if (extractedRows != null)
            return extractedRows.size();
        return sourceDataModel.getRowCount();
    }

    public Object getRowData() {
        if (extractedRows != null) {
            boolean rowIndexInRange = extractedRowIndex >= 0 && extractedRowIndex < extractedRows.size();
            if (rowIndexInRange) {
                RowInfo rowInfo = extractedRows.get(extractedRowIndex);
                return rowInfo != null ? rowInfo.getRowData() : null;
            } else
                throw new IllegalArgumentException("No row data is available for the current index: " + extractedRowIndex);
        }
        return sourceDataModel.getRowData();
    }

    public int getRowIndex() {
        if (extractedRows != null)
            return extractedRowIndex;
        return sourceDataModel.getRowIndex();
    }

    public Object getRowKey() {
        if (extractedRows != null) {
            boolean rowIndexInRange = extractedRowIndex >= 0 && extractedRowIndex < extractedRows.size();
            if (rowIndexInRange)
                return extractedRowKeys.get(extractedRowIndex);
            else
                throw new IllegalArgumentException("No row is available at the current index: " + extractedRowIndex);
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        Object rowData = getRowData();
        Object result = requestRowKeyByRowData(facesContext, requestMap, table.getVar(), rowData, getRowIndex(), -1);
        return result;
    }

    public void setRowKey(Object rowKey) {
        int rowIndex = getRowIndexByRowKey(rowKey);
        setRowIndex(rowIndex);
    }

    public void setRowData(Object rowData) {
        int rowIndex = getRowIndexByRowData(rowData);
        setRowIndex(rowIndex);
    }

    public void setRowIndex(int rowIndex) {
        if (rowIndex < -1)
            throw new IllegalArgumentException("rowIndex shouldn't be less than -1: " + rowIndex);
        if (extractedRows != null) {
            if (extractedRowIndex == rowIndex)
                return;
            extractedRowIndex = rowIndex;
            boolean rowIndexInRange = extractedRowIndex >= 0 && extractedRowIndex < extractedRows.size();
            if (rowIndexInRange) {
                RowInfo rowInfo = extractedRows.get(rowIndex);
                fireRowSelected(rowIndex, rowInfo != null ? rowInfo.getRowData() : null);
                sourceDataModel.setRowIndex(rowInfo != null ? rowInfo.getIndexInOriginalList() : rowIndex);
            }
            return;
        }
        sourceDataModel.setRowIndex(rowIndex);
    }

    public void rowSelected(DataModelEvent dataModelEvent) {
        if (dataModelEvent.getDataModel() == sourceDataModel)
            originalDataModelRowSelected(dataModelEvent.getRowIndex(), dataModelEvent.getRowData());

    }

    private void fireRowSelected(int rowIndex, Object rowData) {
        DataModelListener[] dataModelListeners = getDataModelListeners();
        if (dataModelListeners != null) {
            DataModelEvent event = new DataModelEvent(this, rowIndex, rowData);
            for (DataModelListener dataModelListener : dataModelListeners) {
                dataModelListener.rowSelected(event);
            }
        }
    }

    private void originalDataModelRowSelected(int rowIndex, Object rowData) {
        if (internalIteration)
            return;
        fireRowSelected(rowIndex, rowData);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 0)
            throw new IllegalArgumentException("pageSize can't be less than zero: " + pageSize);
        if (this.pageSize == pageSize)
            return;
        this.pageSize = pageSize;
        updateExtractedRows();
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        if (pageIndex < 0)
            throw new IllegalArgumentException("pageIndex can't be less than zero: " + pageIndex);
        if (updateInProgress == 0)
            pageIndex = validatePageIndex(pageIndex);
        if (this.pageIndex == pageIndex)
            return;
        this.pageIndex = pageIndex;
        if (getPageSize() != 0)
            updateExtractedRows();
    }

    private int validatePageIndex(int pageIndex) {
        int pageCount = getPageCount();
        if (pageCount != -1 && pageIndex >= pageCount)
            pageIndex = pageCount - 1;
        return pageIndex;
    }

    public List<SortingRule> getSortingRules() {
        return sortingRules;
    }

    public void setSortingRules(List<SortingRule> sortingRules) {
        this.sortingRules = sortingRules;
        updateExtractedRows();
    }

    public List getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        boolean oldFiltersSpecified = this.filters != null;
        this.filters = filters;
        boolean newFiltersSpecified = this.filters != null;
        if (!oldFiltersSpecified && !newFiltersSpecified)
            return;

        updateExtractedRows();
    }

    private void updateExtractedRows() {
        if (updateInProgress > 0)
            return;
        if (areExtractedRowsNeeded()) {
            extractRows();
            extractedRowKeys = extractRowKeys(extractedRows);
            allRetrievedRowKeys = extractRowKeys(allRetrievedRows);
            setRowIndex(0);
        } else {
            updateValueExpressionModel();
            totalRowCount = null;
            extractedRows = null;
            extractedRowKeys = null;

            allRetrievedRows = null;
            allRetrievedRowKeys = null;
            allRetrievedRowFilteringFlags = null;
            currentlyAppliedFilters = null;
            setRowIndex(0);
        }
    }

    private boolean areExtractedRowsNeeded() {
        return true;
//    return (isSortingNeeded()) ||
//            isPaginationNeeded() ||
//            isFilteringNeeded();
    }

    private boolean isFilteringNeeded() {
        return filters != null;
    }

    private void extractRows() {
        totalRowCount = null;
        boolean sortingNeeded = isSortingNeeded();
        boolean filteringNeeded = isFilteringNeeded() && filters.size() > 0;
        boolean paginationNeeded = isPaginationNeeded();

        boolean dataAlreadySorted = prepareForRetrievingSortedData(sortingNeeded);
        boolean dataAlreadyFiltered = dataAlreadySorted && prepareForRetrievingFilteredData(filteringNeeded);
        boolean dataAlreadyPaged = dataAlreadyFiltered && prepareForRetrievingPagedData(paginationNeeded);

        List<RowInfo> rows = extractRowsFromSourceDataModel();

        resetPreparedParameters();

        if (!dataAlreadySorted) {
            if (sortingNeeded)
                sortRows(rows);
        }
        allRetrievedRows = new ArrayList<RowInfo>(rows);

        List filteredRows;
        if (!dataAlreadyFiltered) {
            if (filteringNeeded && filters.size() > 0) {
                allRetrievedRowFilteringFlags = new ArrayList<boolean[]>(rows.size());
                rows = filterRows(filters, rows, allRetrievedRowFilteringFlags);
            } else
                allRetrievedRowFilteringFlags = null;
        } else
            allRetrievedRowFilteringFlags = null;

        filteredRows = new ArrayList<RowInfo>(rows);
        currentlyAppliedFilters = filters != null
                ? new ArrayList<Filter>(filters)
                : Collections.<Filter>emptyList();

        if (totalRowCount == null)
            totalRowCount = filteredRows.size();
        if (!dataAlreadyPaged) {
            if (paginationNeeded) {
                rows = extractCurrentPageRows(rows);
            }
        }
        extractedRows = rows;
    }

    private void resetPreparedParameters() {
        restoreRequestVariable(VAR_PAGE_START);
        restoreRequestVariable(VAR_PAGE_SIZE);
        restoreRequestVariable(VAR_SORT_COLUMN_ID);
        restoreRequestVariable(VAR_SORT_COLUMN_INDEX);
        restoreRequestVariable(VAR_SORT_ASCENDING);
        restoreRequestVariable(VAR_FILTER_CRITERIA);
    }

    private boolean prepareForRetrievingSortedData(boolean sortingNeeded) {
        boolean customDataProvidingRequested = isCustomDataProvidingRequested();
        if (sortingNeeded && !customDataProvidingRequested)
            return false;

        if (customDataProvidingRequested) {
            setRequestVariable(VAR_SORT_COLUMN_ID, table.getSortColumnId());
            setRequestVariable(VAR_SORT_COLUMN_INDEX, table.getSortColumnIndex());
            setRequestVariable(VAR_SORT_ASCENDING, table.isSortAscending());
        }
        return true;
    }

    private boolean isCustomDataProvidingRequested() {
        if (table == null)
            return false;
        if (!(table instanceof DataTable))
            return false;
        return ((DataTable) table).getCustomDataProviding();
    }

    private boolean prepareForRetrievingFilteredData(boolean filteringNeeded) {
        boolean customDataProvidingRequested = isCustomDataProvidingRequested();
        if (!customDataProvidingRequested)
            return !filteringNeeded;

        setFilteringCriteriaToRequestVariable();
        return true;
    }

    private void setFilteringCriteriaToRequestVariable() {
        List<FilterCriterion> criteria = new ArrayList<FilterCriterion>();
        AndFilterCriterion andCriterion = new AndFilterCriterion(criteria);
        if (filters != null)
            for (Filter filter : filters) {
                FilterCriterion filterCriterion = (FilterCriterion) filter.getValue();
                if (filterCriterion == null || filterCriterion.acceptsAll())
                    continue;

                criteria.add(filterCriterion);
            }
        setRequestVariable(VAR_FILTER_CRITERIA, andCriterion);
    }

    private boolean prepareForRetrievingPagedData(boolean paginationNeeded) {
        if (!paginationNeeded)
            return true;
        boolean customDataProvidingRequested = isCustomDataProvidingRequested();
        if (!customDataProvidingRequested)
            return false;

        totalRowCount = requestNonPagedRowCount();
        int pageSize = getPageSize();
        int pageIndex = getPageIndex();
        int pageCount = getPageCount();
        if (pageIndex >= pageCount)
            pageIndex = pageCount - 1;
        int pageStart = pageIndex * pageSize;
        int remainingRows = totalRowCount - pageStart;
        int thisRangeSize = remainingRows < pageSize ? remainingRows : pageSize;

        setRequestVariable(VAR_PAGE_START, pageStart);
        setRequestVariable(VAR_PAGE_SIZE, thisRangeSize);
        return true;
    }

    private void setRequestVariable(String varName, Object varValue) {
        Map<String, Object> requestMap = getRequestMap();
        Object prevVarValue = requestMap.put(varName, varValue);
        String backupVarName = "of:prev_" + varName;
        Stack<Object> backupValues = (Stack<Object>) requestMap.get(backupVarName);
        if (backupValues == null) {
            backupValues = new Stack<Object>();
            requestMap.put(backupVarName, backupValues);
        }
        backupValues.push(prevVarValue);
    }

    private void restoreRequestVariable(String varName) {
        Map<String, Object> requestMap = getRequestMap();
        if (requestMap == null) {
            return;
        }
        String backupVarName = "of:prev_" + varName;
        Stack backupValues = (Stack) requestMap.get(backupVarName);
        if (backupValues == null || backupValues.isEmpty())
            return;
        Object oldValue = backupValues.pop();
        requestMap.put(varName, oldValue);
    }

    private int requestNonPagedRowCount() {
        AbstractTable table = getTable();
        setFilteringCriteriaToRequestVariable();
        ValueExpression valueExpression = table.getValueExpression("totalRowCount");
        if (valueExpression == null)
            throw new IllegalStateException("totalRowCount must be defined for pagination with custom data providing to work. table id = " +
                    table.getClientId(FacesContext.getCurrentInstance()));
        Object value = valueExpression.getValue(FacesContext.getCurrentInstance().getELContext());
        restoreRequestVariable(VAR_FILTER_CRITERIA);
        if (!(value instanceof Integer))
            throw new IllegalStateException("totalRowCount must return an int (or Integer) number, but returned: " +
                    (value != null ? value.getClass().getName() : "null") + "; table id = " + table.getClientId(FacesContext.getCurrentInstance()));
        return (Integer) value;
    }

    private Map<String, Object> getRequestMap() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) return null;
        ExternalContext externalContext = facesContext.getExternalContext();
        return externalContext.getRequestMap();
    }

    private boolean isPaginationNeeded() {
        return getPageSize() > 0;
    }

    private boolean isSortingNeeded() {
        return sortingRules != null && sortingRules.size() > 0;
    }

    /**
     * @return list of RowInfo instances
     */
    private List<RowInfo> extractRowsFromSourceDataModel() {
        List<RowInfo> extractedRows;
        internalIteration = true;
        try {
            updateValueExpressionModel();
            int rowCount = sourceDataModel.getRowCount();
            if (rowCount == -1)
                rowCount = Integer.MAX_VALUE;
            extractedRows = new ArrayList<RowInfo>();
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                sourceDataModel.setRowIndex(rowIndex);
                if (!sourceDataModel.isRowAvailable())
                    break;
                Object currentRowData = sourceDataModel.getRowData();
                if (currentRowData == null)
                    throw new NullPointerException("There must not be null rows in a DataTable/TreeTable. table id: " +
                            getTable().getClientId(FacesContext.getCurrentInstance()));
                extractedRows.add(new RowInfo(currentRowData, rowIndex));
            }
        } finally {
            internalIteration = false;
        }
        return extractedRows;
    }

    private void updateValueExpressionModel() {
        if (sourceDataModel instanceof ValueExpressionDataModel)
            ((ValueExpressionDataModel) sourceDataModel).readData();
    }

    private static List<RowInfo> filterRows(List<Filter> filters, List<RowInfo> sortedRows, List<boolean[]> filteringFlags) {
        List<RowInfo> result = new ArrayList<RowInfo>();
        int sortedRowCount = sortedRows.size();
        for (int i = 0; i < sortedRowCount; i++) {
            RowInfo rowObj = sortedRows.get(i);

            boolean[] flagsArray = new boolean[filters.size()];
            boolean rowAccepted = filterRow(filters, rowObj, flagsArray);
            filteringFlags.add(flagsArray);
            if (rowAccepted)
                result.add(rowObj);
        }
        return result;
    }

    public static boolean filterRow(List<Filter> filters, Object rowObj, boolean[] flagsArray) {
        Object data = (rowObj instanceof RowInfo)
                ? ((RowInfo) rowObj).getRowData() // RowInfo for DataTable (for storing original row indexes)
                : rowObj; // row data object for TreeTable (for there's no notion of index in TreeTable)
        boolean rowAccepted = true;
        for (int filterIndex = 0, filterCount = filters.size(); filterIndex < filterCount; filterIndex++) {
            Filter filter = filters.get(filterIndex);
            FilterCriterion filterValue = (FilterCriterion) filter.getValue();

            Predicate predicate = filterValue != null ? PredicateBuilder.build(filterValue) : null;
            boolean filterAcceptsData = predicate == null || predicate.evaluate(data);
            if (!filterAcceptsData)
                rowAccepted = false;
            flagsArray[filterIndex] = filterAcceptsData;
        }

        return rowAccepted;
    }

    private List<RowInfo> extractCurrentPageRows(List<RowInfo> extractedRows) {
        int rowCount = extractedRows.size();
        if (rowCount == 0)
            extractedRows = Collections.emptyList();
        else {
            int pageSize = getPageSize();
            int pageIndex = getPageIndex();
            int fromIndex = pageIndex * pageSize;

            if (fromIndex >= rowCount)
                extractedRows = Collections.emptyList();
            else {
                int toIndex = fromIndex + pageSize;
                if (toIndex >= rowCount)
                    toIndex = rowCount;
                extractedRows = extractedRows.subList(fromIndex, toIndex);
            }
        }
        return extractedRows;
    }

    private List<Object> extractRowKeys(List<RowInfo> rows) {
        if (rows.size() == 0)
            return Collections.emptyList();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        String var = table.getVar();
        int rowCount = rows.size();
        List<Object> extractedRowKeys = new ArrayList<Object>(rowCount);
        for (int i = 0; i < rowCount; i++) {
            RowInfo rowInfo = rows.get(i);
            Object rowData = rowInfo.getRowData();
            Object rowKey = requestRowKeyByRowData(facesContext, requestMap, var, rowData, i, rowInfo.getIndexInOriginalList());
            extractedRowKeys.add(rowKey);
        }
        return extractedRowKeys;
    }

    private void sortRows(List<RowInfo> extractedRows) {
        if (table == null)
            return;
        final Comparator<Object> rowDataComparator = table.createRowDataComparator(sortingRules);
        Comparator<RowInfo> rowInfoComparator = new Comparator<RowInfo>() {
            public int compare(RowInfo rowInfo1, RowInfo rowInfo2) {
                return rowDataComparator.compare(rowInfo1.getRowData(), rowInfo2.getRowData());
            }
        };
        if (rowDataComparator != null)
            Collections.sort(extractedRows, rowInfoComparator);
    }

    public Object requestRowKeyByRowData(
            FacesContext facesContext,
            Map<String, Object> requestMap, String var,
            Object rowData,
            int rowIndex, int indexInOriginalList) {
        if (rowKeyExpression == null) {
            if (isValidRowKey(rowData))
                return rowData;
            else
                return (indexInOriginalList != -1) ? new DefaultRowKey(rowIndex, indexInOriginalList) : new DefaultRowKey(rowIndex);
        }
        if (requestMap == null) {
            requestMap = facesContext.getExternalContext().getRequestMap();
        }
        if (var == null) {
            var = getTable().getVar();
        }
        Object prevVarValue = requestMap.put(var, rowData);
        Object result = rowKeyExpression.getValue(facesContext.getELContext());
        requestMap.put(var, prevVarValue);
        if (result == null)
            throw new RuntimeException("The rowKey binding \"" + rowKeyExpression.getExpressionString() + "\" of table with client id \"" + getTable().getClientId(facesContext) + "\" must return a non-null value\n");
        if (!isValidRowKey(result))
            throw new RuntimeException("Invalid value returned from rowKey binding \"" + rowKeyExpression.getExpressionString() + "\" of table with client id \"" + getTable().getClientId(facesContext) + "\"\n" +
                    "    It must return a value that implements java.io.Serializable interface and correctly implements the equals and hashCode methods for serialized instances. \n" +
                    "    An instance of the following class that doesn't satisfy these rules has been returned: " + result.getClass().getName() + ", for this row data: " + rowData);

        return result;
    }

    private static Set<Class> approvedRowKeyClasses = new HashSet<Class>();

    public static boolean isValidRowKey(Object rowKey) {
        Class rowKeyClass = rowKey.getClass();
        if (approvedRowKeyClasses.contains(rowKeyClass))
            return true;
        boolean result = rowKey instanceof Serializable && checkSerializableEqualsAndHashcode(rowKey);
        if (result)
            approvedRowKeyClasses.add(rowKeyClass);
        return result;
    }

    private Object requestRowDataByRowKey(FacesContext facesContext, Object rowKey) {
        if (rowDataByKeyExpression == null)
            return null;
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        requestMap.put("rowKey", rowKey);
        Object result = rowDataByKeyExpression.getValue(facesContext.getELContext());
        return result;
    }

    private int getRowIndexByRowKey(Object key) {
        if (key == null)
            return -1;
        if (key instanceof DefaultRowKey) {
            DefaultRowKey defaultRowKey = (DefaultRowKey) key;
            return defaultRowKey.getRowIndex();
        }
        if (extractedRows != null) {
            int index = extractedRowKeys.indexOf(key);
            return index;
        }
        int rowCount = getRowCount();
        if (rowCount == -1)
            rowCount = Integer.MAX_VALUE;
        for (int i = 0; i < rowCount; i++) {
            setRowIndex(i);
            if (!isRowAvailable())
                return -1;
            Object currentRowKey = getRowKey();
            if (key.equals(currentRowKey))
                return i;
        }
        return -1;
    }

    private int getRowIndexByRowData(Object data) {
        if (data == null)
            return -1;
        if (extractedRows != null) {
            for (int index = 0, extractedRowCount = extractedRows.size(); index < extractedRowCount; index++) {
                RowInfo rowInfo = extractedRows.get(index);
                Object rowData = rowInfo != null ? rowInfo.getRowData() : null;
                if (rowData != null && rowData.equals(data))
                    return index;
            }
            return -1;
        }
        int rowCount = getRowCount();
        if (rowCount == -1)
            rowCount = Integer.MAX_VALUE;
        for (int i = 0; i < rowCount; i++) {
            setRowIndex(i);
            if (!isRowAvailable())
                return -1;
            Object currentRowData = getRowData();
            if (data.equals(currentRowData))
                return i;
        }
        // todo: it appears that this method will fail in finding index by data if DataTable's rowKey attribute is defined, but there's no equals/hashCode for node data itself.
        // todo: check this and add search by row key for such situations
        return -1;
    }

    public int getPageCount() {
        int pageSize = getPageSize();
        if (pageSize == 0)
            return -1;
        int rowCount = getTotalRowCount();
        if (rowCount == -1)
            return -1;
        if (rowCount == 0)
            return 1;
        int pageCount = rowCount / pageSize;
        if (rowCount % pageSize > 0)
            pageCount++;
        return pageCount;
    }

    public int getTotalRowCount() {
        return (totalRowCount != null)
                ? totalRowCount
                : sourceDataModel.getRowCount();
    }

    public RowInfo getRowInfoByRowKey(Object key) {
        if (key == null)
            return null;
        if (allRetrievedRows != null) {
            int index = allRetrievedRowKeys.indexOf(key);
            if (index != -1)
                return allRetrievedRows.get(index);
        }
        int rowCount = getRowCount();
        if (rowCount == -1)
            rowCount = Integer.MAX_VALUE;
        for (int i = 0; i < rowCount; i++) {
            setRowIndex(i);
            if (!isRowAvailable())
                return null;
            Object currentRowKey = getRowKey();
            if (key.equals(currentRowKey))
                return new RowInfo(getRowData(), i);
        }
        Object rowData = requestRowDataByRowKey(FacesContext.getCurrentInstance(), key);
        return new RowInfo(rowData, -1);
    }

    public List<Object> getRowListForFiltering(Filter filter) {
        return getRowListForFiltering(filter, currentlyAppliedFilters, allRetrievedRows, allRetrievedRowFilteringFlags);
    }

    public static List<Object> getRowListForFiltering(
            Filter filter, List<Filter> lastFilteringFilters, List<?> allRows, List<boolean[]> allRowFilteringFlags) {
        if (lastFilteringFilters != null && lastFilteringFilters.size() > 0) {
            if (allRowFilteringFlags == null)
                return rowDatasFromRowInfos(allRows);
            int requestedFilterIndex = lastFilteringFilters.indexOf(filter);
            List<Object> result = new ArrayList<Object>();
            rowIteration:
            for (int rowIndex = 0, allRowCount = allRows.size(); rowIndex < allRowCount; rowIndex++) {
                Object rowObj = allRows.get(rowIndex);
                Object data = (rowObj instanceof RowInfo)
                        ? ((RowInfo) rowObj).getRowData() // RowInfo for DataTable (for storing original row indexes)
                        : rowObj; // row data object for TreeTable (for there's no notion of index in TreeTable)
                boolean[] rowFlags = allRowFilteringFlags.get(rowIndex);
                for (int filterIndex = 0; filterIndex < rowFlags.length; filterIndex++) {
                    if (filterIndex == requestedFilterIndex)
                        continue;
                    boolean filterAcceptsRow = rowFlags[filterIndex];
                    if (!filterAcceptsRow)
                        continue rowIteration;
                }
                result.add(data);
            }
            return result;
        } else
            return rowDatasFromRowInfos(allRows);
    }

    private static List<Object> rowDatasFromRowInfos(List<?> allRows) {
        List<Object> result = new ArrayList<Object>(allRows.size());
        for (Object rowObj : allRows) {
            if (rowObj instanceof RowInfo) {
                RowInfo rowInfo = (RowInfo) rowObj;
                result.add(rowInfo.getRowData());
            } else
                result.add(rowObj);
        }
        return result;
    }

    public void startUpdate() {
        updateInProgress++;
    }

    public void endUpdate() {
        if (updateInProgress == 0)
            throw new IllegalStateException("endUpdate is called while the model is not in the update state");
        updateInProgress--;
        if (updateInProgress == 0) {
            updateExtractedRows();
            int pageIndex = getPageIndex();
            int newPageIndex = validatePageIndex(pageIndex);
            if (newPageIndex != pageIndex) {
                this.pageIndex = newPageIndex;
                updateExtractedRows();
            }
        }

    }

    public boolean isSourceDataModelEmpty() {
        DataModel sourceDataModel = getSourceDataModel();
        if (sourceDataModel == null)
            return true;
        int rowCount = sourceDataModel.getRowCount();
        return rowCount == 0;
    }

    private int getOldRowIndexByRowKey(Object key) {
        if (key == null)
            return -1;
        if (extractedRowKeys != null) {
            int index = extractedRowKeys.indexOf(key);
            if (index != -1)
                return index;
        }
        return -1;
    }

    public void setWrappedData(List rowDatas, List rowKeys) {
        extractedRows = new ArrayList<RowInfo>(rowDatas.size());
        for (Object rowData : rowDatas) {
            extractedRows.add(new RowInfo(rowData, -1));
        }
        extractedRowKeys = rowKeys;
    }


    public static class RestoredRowIndexes {
        private final int[] oldIndexes;
        private final Set<Integer> unavailableRowIndexes;

        public RestoredRowIndexes(int[] oldIndexes, Set<Integer> unavailableRowIndexes) {
            this.oldIndexes = oldIndexes;
            this.unavailableRowIndexes = unavailableRowIndexes;
        }

        public int[] getOldIndexes() {
            return oldIndexes;
        }

        public Set<Integer> getUnavailableRowIndexes() {
            return unavailableRowIndexes;
        }
    }

    /**
     * This method should be called before the fresh data has been read into the TableDataModel.
     * So this method should be called early in the request processing lifecycle, then should go the
     * data reading procedure, which updates myExtractedRows in TableDataModel, and then goes the call
     * to restoreRowIndexes() method or restoreRows() method.
     */
    public void prepareForRestoringRowIndexes() {
        previousRowKeys = new ArrayList<Object>(extractedRowKeys);
    }

    public List getStoredRowKeys() {
        return previousRowKeys;
    }

    public RestoredRowIndexes restoreRowIndexes() {
        List<Object> restoredRowKeys = previousRowKeys;
        if (restoredRowKeys == null)
            throw new IllegalStateException();

        Set<Integer> unavailableRowIndexes = new HashSet<Integer>();
        int restoredRowCount = restoredRowKeys.size();
        int[] oldRowIndexes = new int[restoredRowCount];
        List<RowInfo> restoredRowDatas = new ArrayList<RowInfo>(restoredRowCount);
        for (int i = 0; i < restoredRowCount; i++) {
            Object rowKey = restoredRowKeys.get(i);
            int oldRowIndex = getOldRowIndexByRowKey(rowKey);
            oldRowIndexes[i] = oldRowIndex;
            RowInfo rowInfo = oldRowIndex != -1 ? getRowInfoByRowKey(rowKey) : null;
            Object rowData = rowInfo != null ? rowInfo.getRowData() : null;
            if (rowData == null)
                unavailableRowIndexes.add(i);
            restoredRowDatas.add(new RowInfo(rowData, -1));
        }

        extractedRows = restoredRowDatas;
        extractedRowKeys = restoredRowKeys;
        return new RestoredRowIndexes(oldRowIndexes, unavailableRowIndexes);
    }

    public void addRows(int atIndex, List rowDatas, List<?> rowKeys) {
        for (int i = 0; i < rowDatas.size(); i++) {
            Object newRowData = rowDatas.get(i);
            extractedRows.add(atIndex + i, new RowInfo(newRowData, -1));
        }
        extractedRowKeys.addAll(atIndex, rowKeys);
    }

    public Set<Integer> restoreRows(boolean readActualData) {
        List<Object> restoredRowKeys = previousRowKeys;
        if (restoredRowKeys == null)
            throw new IllegalStateException();

        Set<Integer> unavailableRowIndexes = new HashSet<Integer>();
        int restoredRowCount = restoredRowKeys.size();
        List<RowInfo> restoredRowDatas = new ArrayList<RowInfo>(restoredRowCount);
        for (int i = 0; i < restoredRowCount; i++) {
            if (!readActualData) {
                unavailableRowIndexes.add(i);
                continue;
            }
            Object rowKey = restoredRowKeys.get(i);
            RowInfo rowInfo = getRowInfoByRowKey(rowKey);
            Object rowData = rowInfo != null ? rowInfo.getRowData() : null;
            if (rowData == null)
                unavailableRowIndexes.add(i);
            restoredRowDatas.add(rowInfo);
        }

        extractedRows = restoredRowDatas;
        extractedRowKeys = restoredRowKeys;
        return unavailableRowIndexes;
    }

    private static boolean checkSerializableEqualsAndHashcode(Object rowKey) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Object deserializedRowKey;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(rowKey);
            oos.close();
            byte[] serializedObject = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);
            ObjectInputStream ois = new ObjectInputStream(bais);
            deserializedRowKey = ois.readObject();
            bais.close();
        } catch (IOException e) {
            throw new RuntimeException("The rowData or rowKey object is marked as Serializable but can't be serialized: " +
                    rowKey.getClass().getName() + " ; check that all object's fields are also Serializable", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        boolean equalsValid = deserializedRowKey.equals(rowKey);
        boolean hashCodeValid = deserializedRowKey.hashCode() == rowKey.hashCode();
        boolean result = equalsValid && hashCodeValid;
        return result;
    }

    public static class RowInfo {
        private final Object rowData;
        private final int indexInOriginalList;


        public RowInfo(Object rowData, int indexInOriginalList) {
            this.rowData = rowData;
            this.indexInOriginalList = indexInOriginalList;
        }


        public Object getRowData() {
            return rowData;
        }

        public int getIndexInOriginalList() {
            return indexInOriginalList;
        }
    }
}
