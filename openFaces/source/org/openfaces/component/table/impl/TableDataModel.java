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
package org.openfaces.component.table.impl;

import org.apache.commons.collections.Predicate;
import org.openfaces.component.filter.AndFilterCriterion;
import org.openfaces.component.filter.Filter;
import org.openfaces.component.filter.FilterCriterion;
import org.openfaces.component.filter.PredicateBuilder;
import org.openfaces.component.table.*;
import org.openfaces.util.Components;
import org.openfaces.util.DataUtil;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 *
 * @author Dmitry Pikhulya
 */

public class TableDataModel extends DataModel implements DataModelListener, StateHolder {
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
    private Map<Object, ? extends NodeInfo> derivedRowHierarchy;
    private List<Object> extractedRowKeys;
    private List<Object> allRetrievedRowKeys;
    private int extractedRowIndex = -1;
    private List<GroupingRule> groupingRules;
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
    private Boolean clearUnDisplayedSelection;

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

    public Object saveState(FacesContext context) {
        return new Object[]{
                groupingRules,
                sortingRules,
                rowKeyExpression,
                rowDataByKeyExpression,
                pageSize,
                pageIndex,
                extractedRowKeys
        };
    }

    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        groupingRules = (List<GroupingRule>) state[i++];
        sortingRules = (List<SortingRule>) state[i++];
        rowKeyExpression = (ValueExpression) state[i++];
        rowDataByKeyExpression = (ValueExpression) state[i++];
        pageSize = (Integer) state[i++];
        pageIndex = (Integer) state[i++];
        setWrappedData(null);

        // restoring old extracted row keys is needed for correct restoreRows/restoreRowIndexes functionality, which
        // in turn is required for correct data submission in case of concurrent data modifications
        extractedRowKeys = (List) state[i++];
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean newTransientValue) {
        throw new UnsupportedOperationException();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        groupingRules = (List<GroupingRule>) in.readObject();
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
        out.writeObject(groupingRules);
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

    public int getNodeLevel() {
        RowInfo rowInfo = getRowInfo();
        return rowInfo != null ? rowInfo.getLevel() : 0;
    }

    public RowInfo getRowInfo() {
        if (extractedRows != null) {
            boolean rowIndexInRange = extractedRowIndex >= 0 && extractedRowIndex < extractedRows.size();
            if (rowIndexInRange) {
                RowInfo rowInfo = extractedRows.get(extractedRowIndex);
                return rowInfo;
            } else
                throw new IllegalArgumentException("No row info is available for the current index: " + extractedRowIndex);
        }
        return null;
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

    public List<GroupingRule> getGroupingRules() {
        return groupingRules;
    }

    public void setGroupingRules(List<GroupingRule> groupingRules) {
        this.groupingRules = groupingRules;
        updateExtractedRows();
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
        extractRows();
        extractedRowKeys = extractRowKeys(extractedRows);
        allRetrievedRowKeys = extractRowKeys(allRetrievedRows);
        setRowIndex(0);

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

        if (groupingRules != null && groupingRules.size() > 0) {
            derivedRowHierarchy = groupRows(groupingRules, rows);
        } else {
            derivedRowHierarchy = null;
        }

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

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public List<RowInfo> getAllRetrievedRows() {
        return allRetrievedRows;
    }

    public Map<Object, ? extends NodeInfo> getDerivedRowHierarchy() {
        return derivedRowHierarchy;
    }

    /**
     * @param groupingRules a list of GroupingRule instances representing the requested grouping hierarchy
     * @param rows          RowInfo objects representing the data rows that are already sorted according to the passed grouping
     *                      rules hierarchy
     * @param level         hierarchy level of the rows passed in the rows parameters. The group rows created by this method
     *                      invocation (excluding the recursive invocations) will have this value of their level property.
     * @return a list of RowInfo instances representing the newly created group header rows for the top-level grouping
     *         rule (the first one in the list), and having the group header rows for its lower-level grouping rules
     *         in the immediateSubRows field of the appropriate top-level RowInfos, and so deeper into the hierarchy of
     *         grouping rules. The "leaf" RowInfos (the ones stored in the immediateSubRows field of the deepest
     *         hierarchy level represent the actual data rows and not group header rows.
     */
    private List<RowInfo> constructGroupingTree(
            List<GroupingRule> groupingRules,
            List<RowInfo> rows,
            int level,
            RowGroup parentRowGroup) {

        if (groupingRules.size() == 0)
            return rows;
        int rowCount = rows.size();
        if (rowCount == 0)
            return Collections.emptyList();

        GroupingRule groupingRule = groupingRules.get(0);

        FacesContext context = FacesContext.getCurrentInstance();
        RowComparator ruleComparator = table.createRuleComparator(context, groupingRule);

        String columnId = groupingRule.getColumnId();
        ColumnGroupingInfo columnGroupingInfo = getColumnGroupingInfo(columnId);

        List<RowInfo> thisLevelGroupHeaderRowInfos = new ArrayList<RowInfo>();

        Runnable exitColumnContext = columnGroupingInfo.enterColumnContext();
        try {
            RowInfo currentGroupRowInfo = null;
            int subRowsLevel = level + 1;
            for (int i = 0; i < rowCount; i++) {
                RowInfo rowInfo = rows.get(i);
                RowInfo nextRowInfo = i < rowCount - 1 ? rows.get(i + 1) : null;
                if (currentGroupRowInfo == null) {
                    currentGroupRowInfo = createHeaderRowInfo(context, columnGroupingInfo, rowInfo.getRowData(),
                            level, parentRowGroup);
                    currentGroupRowInfo.setAllDataRowsInThisGroup(new ArrayList<RowInfo>());
                }

                rowInfo.setLevel(subRowsLevel);
                currentGroupRowInfo.getAllDataRowsInThisGroup().add(rowInfo);

                boolean lastRowInThisGroup = nextRowInfo == null ||
                        !recordsInTheSameGroup(ruleComparator, rowInfo, nextRowInfo);
                if (lastRowInThisGroup) {
                    thisLevelGroupHeaderRowInfos.add(currentGroupRowInfo);
                    GroupHeader groupHeader = (GroupHeader) currentGroupRowInfo.getRowData();
                    RowGroup rowGroup = groupHeader.getRowGroup();
                    List<RowInfo> subRowInfos = constructGroupingTree(
                            groupingRules.subList(1, groupingRules.size()),
                            currentGroupRowInfo.getAllDataRowsInThisGroup(),
                            subRowsLevel,
                            rowGroup);
                    if (columnGroupingInfo.isInHeadersSpecified()) {
                        subRowInfos.add(0, new RowInfo(new InGroupHeader(rowGroup), -1, subRowsLevel));
                    }
                    if (columnGroupingInfo.isInGroupFootersSpecified()) {
                        if (columnGroupingInfo.isInGroupFootersCollapsible())
                            subRowInfos.add(new RowInfo(new InGroupFooter(rowGroup), -1, subRowsLevel));
                        else
                            thisLevelGroupHeaderRowInfos.add(new RowInfo(new InGroupFooter(rowGroup), -1, level));
                    }
                    if (columnGroupingInfo.isGroupFooterSpecified()) {
                        if (columnGroupingInfo.isGroupFootersCollapsible())
                            subRowInfos.add(new RowInfo(new GroupFooter(rowGroup), -1, subRowsLevel));
                        else
                            thisLevelGroupHeaderRowInfos.add(new RowInfo(new GroupFooter(rowGroup), -1, level));
                    }

                    currentGroupRowInfo.setImmediateSubRows(subRowInfos);
                    currentGroupRowInfo = null;
                }

            }
        } finally {
            if (exitColumnContext != null) exitColumnContext.run();
        }
        return thisLevelGroupHeaderRowInfos;
    }

    private void linearizeGroupingTree(List<RowInfo> topLevelRowInfos, List<RowInfo> targetRowList) {
        for (RowInfo rowInfo : topLevelRowInfos) {
            targetRowList.add(rowInfo);
            List<RowInfo> subRows = rowInfo.getImmediateSubRows();
            if (subRows != null && subRows.size() > 0)
                linearizeGroupingTree(subRows, targetRowList);
        }
    }


    private Map<Object, ? extends NodeInfo> groupRows(List<GroupingRule> groupingRules, List<RowInfo> rows) {
        clearCachedColumnGroupingInfos();
        List<RowInfo> topLevelRowInfos = constructGroupingTree(groupingRules, rows, 0, null);

        rows.clear();
        linearizeGroupingTree(topLevelRowInfos, rows);

        Map<Object, NodeInfo> rowIndexToChildCount = new HashMap<Object, NodeInfo>();
        rowIndexToChildCount.put("root", createNodeInfo(-1, topLevelRowInfos.size()));
        for (int rowIndex = 0, rowCount = rows.size(); rowIndex < rowCount; rowIndex++) {
            RowInfo rowInfo = rows.get(rowIndex);
            Object rowData = rowInfo.getRowData();
            if (!(rowData instanceof GroupHeader)) continue;

            List<RowInfo> immediateSubRows = rowInfo.getImmediateSubRows();
            rowIndexToChildCount.put(rowIndex,
                    immediateSubRows != null
                            ? createNodeInfo(rowInfo.getLevel(), immediateSubRows.size())
                            : createNodeInfo(rowInfo.getLevel(), rowInfo.getAllDataRowsInThisGroup().size()));
        }
        return rowIndexToChildCount;
    }

    private DataTableNodeInfo createNodeInfo(int nodeLevel, Integer childCount) {
        return new DataTableNodeInfo(nodeLevel, childCount, true);
    }

    private boolean recordsInTheSameGroup(Comparator<Object> comparator, RowInfo rowInfo1, RowInfo rowInfo2) {
        Object record1 = rowInfo1 != null ? rowInfo1.getRowData() : null;
        Object record2 = rowInfo2 != null ? rowInfo2.getRowData() : null;
        if (record1 == null || record2 == null) return false;
        if (record1 instanceof GroupHeaderOrFooter || record2 instanceof GroupHeaderOrFooter) return false;
        return comparator.compare(record1, record2) == 0;
    }

    private RowInfo createHeaderRowInfo(
            FacesContext context,
            ColumnGroupingInfo columnGroupingInfo,
            Object anyRowDataInThisGroup,
            int level,
            RowGroup parentRowGroup) {
        RowGroup currentGroup = createRowGroup(context, columnGroupingInfo, anyRowDataInThisGroup, parentRowGroup);
        GroupHeader groupHeader = new GroupHeader(currentGroup);
        return new RowInfo(groupHeader, -1, level);
    }

    private RowGroup createRowGroup(
            FacesContext context,
            ColumnGroupingInfo columnGroupingInfo,
            Object anyRowDataInThisGroup,
            RowGroup parentRowGroup) {

        Runnable restoreParams = table.populateSortingExpressionParams(
                table.getVar(), context.getExternalContext().getRequestMap(), anyRowDataInThisGroup);
        Object groupingValue;
        try {
            groupingValue = columnGroupingInfo.getColumnGroupingValueExpression().getValue(context.getELContext());
        } finally {
            restoreParams.run();
        }
        return new RowGroup(columnGroupingInfo.getColumnId(), groupingValue, parentRowGroup);
    }


    private void resetPreparedParameters() {
        Components.restoreRequestVariable(VAR_PAGE_START);
        Components.restoreRequestVariable(VAR_PAGE_SIZE);
        Components.restoreRequestVariable(VAR_SORT_COLUMN_ID);
        Components.restoreRequestVariable(VAR_SORT_COLUMN_INDEX);
        Components.restoreRequestVariable(VAR_SORT_ASCENDING);
        Components.restoreRequestVariable(VAR_FILTER_CRITERIA);
    }

    private boolean prepareForRetrievingSortedData(boolean sortingNeeded) {
        boolean customDataProvidingRequested = isCustomDataProvidingRequested();
        if (sortingNeeded && !customDataProvidingRequested)
            return false;

        if (customDataProvidingRequested) {
            Components.setRequestVariable(VAR_SORT_COLUMN_ID, table.getSortColumnId());
            Components.setRequestVariable(VAR_SORT_COLUMN_INDEX, table.getSortColumnIndex());
            Components.setRequestVariable(VAR_SORT_ASCENDING, table.isSortAscending());
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
        Components.setRequestVariable(VAR_FILTER_CRITERIA, andCriterion);
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

        Components.setRequestVariable(VAR_PAGE_START, pageStart);
        Components.setRequestVariable(VAR_PAGE_SIZE, thisRangeSize);
        return true;
    }

    private int requestNonPagedRowCount() {
        AbstractTable table = getTable();
        setFilteringCriteriaToRequestVariable();
        if (!table.getRowsDecodingRequired()) {
            return table.getTotalRowCount();
        }
        ValueExpression valueExpression = table.getValueExpression("totalRowCount");
        if (valueExpression == null)
            throw new IllegalStateException("totalRowCount must be defined for pagination with custom data providing to work. table id = " +
                    table.getClientId(FacesContext.getCurrentInstance()));
        Object value = valueExpression.getValue(FacesContext.getCurrentInstance().getELContext());
        Components.restoreRequestVariable(VAR_FILTER_CRITERIA);
        if (!(value instanceof Integer))
            throw new IllegalStateException("totalRowCount must return an int (or Integer) number, but returned: " +
                    (value != null ? value.getClass().getName() : "null") + "; table id = " + table.getClientId(FacesContext.getCurrentInstance()));
        return (Integer) value;
    }


    private boolean isPaginationNeeded() {
        return getPageSize() > 0;
    }

    private boolean isSortingNeeded() {
        return (sortingRules != null && sortingRules.size() > 0) ||
                (groupingRules != null && groupingRules.size() > 0);
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
        for (RowInfo rowObj : sortedRows) {
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
        final Comparator<Object> rowDataComparator = table.createRowDataComparator(groupingRules, sortingRules);
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
        if (rowData instanceof GroupHeaderOrFooter) {
            return rowData;
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
            throw new RuntimeException("The rowKey binding \"" + rowKeyExpression.getExpressionString() +
                    "\" of table with client id \"" + getTable().getClientId(facesContext) +
                    "\" must return a non-null value\n");
        if (!isValidRowKey(result))
            throw new RuntimeException("Invalid value returned from rowKey binding \"" + rowKeyExpression.getExpressionString() + "\" of table with client id \"" + getTable().getClientId(facesContext) + "\"\n" +
                    "    It must return a value that implements java.io.Serializable interface and correctly implements the equals and hashCode methods for serialized instances. \n" +
                    "    An instance of the following class that doesn't satisfy these rules has been returned: " + result.getClass().getName() + ", for this row data: " + rowData);

        return result;
    }

    private static final Map<Class, Boolean> rowKeyClassesValidFlags = new HashMap<Class, Boolean>();

    public static boolean isValidRowKey(Object rowKey) {
        Class rowKeyClass = rowKey.getClass();

        synchronized (rowKeyClassesValidFlags) {
            Boolean rowKeyValid = rowKeyClassesValidFlags.get(rowKeyClass);
            if (rowKeyValid == null) {
                rowKeyValid = checkSerializableEqualsAndHashcode(rowKey);
                rowKeyClassesValidFlags.put(rowKeyClass, rowKeyValid);
            }
            return rowKeyValid;
        }
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
        if (!(rowKey instanceof Serializable))
            return false;
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
            throw new RuntimeException("The rowData or rowKey object is marked as Serializable, but can't be serialized: " +
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
        private int level;
        private List<RowInfo> immediateSubRows;
        private List<RowInfo> allDataRowsInThisGroup;
        private RowInfo parentGroup;

        public RowInfo(Object rowData, int indexInOriginalList) {
            this(rowData, indexInOriginalList, 0);
        }

        public RowInfo(Object rowData, int indexInOriginalList, int level) {
            this.rowData = rowData;
            this.indexInOriginalList = indexInOriginalList;
            this.level = level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public RowInfo getParentGroup() {
            return parentGroup;
        }

        public void setParentGroup(RowInfo parentGroup) {
            this.parentGroup = parentGroup;
        }

        public Object getRowData() {
            return rowData;
        }

        public int getIndexInOriginalList() {
            return indexInOriginalList;
        }

        public int getLevel() {
            return level;
        }

        public List<RowInfo> getImmediateSubRows() {
            return immediateSubRows;
        }

        public void setImmediateSubRows(List<RowInfo> immediateSubRows) {
            for (RowInfo subRow : immediateSubRows) {
                subRow.setParentGroup(this);
            }
            this.immediateSubRows = immediateSubRows;
        }

        /**
         * @return a list of all data rows in this group and all of its sub-groups
         */
        public List<RowInfo> getAllDataRowsInThisGroup() {
            return allDataRowsInThisGroup;
        }

        public void setAllDataRowsInThisGroup(List<RowInfo> allDataRowsInThisGroup) {
            this.allDataRowsInThisGroup = allDataRowsInThisGroup;
        }
    }

    private Map<String, ColumnGroupingInfo> columnGroupingInfos = new HashMap<String, ColumnGroupingInfo>();

    /**
     * Invoking this once for data iteration is needed to stay up to date to the current RowGrouping settings cached
     * here.
     */
    private void clearCachedColumnGroupingInfos() {
        columnGroupingInfos.clear();
    }

    private ColumnGroupingInfo getColumnGroupingInfo(String columnId) {
        ColumnGroupingInfo columnGroupingInfo = columnGroupingInfos.get(columnId);
        if (columnGroupingInfo != null)
            return columnGroupingInfo;
        BaseColumn column = table.getColumnById(columnId);
        columnGroupingInfo = new ColumnGroupingInfo(column);
        columnGroupingInfos.put(columnId, columnGroupingInfo);
        return columnGroupingInfo;
    }

    public Boolean isObjectInList(Object rowData) {
        for (RowInfo extractedRow : extractedRows) {
            if (extractedRow.getRowData().equals(rowData))
                return true;
        }
        return false;
    }

    /**
     * This class contains the pre-extracted column's data that is repeatedly required during the row grouping process,
     * to avoid having to retrieve this information each time it is needed.
     */
    private class ColumnGroupingInfo {
        private BaseColumn column;
        private String columnId;
        private boolean inHeadersSpecified;
        private boolean inGroupFootersSpecified;
        private boolean groupFooterSpecified;
        private boolean inGroupFootersCollapsible;
        private boolean groupFootersCollapsible;
        private ValueExpression columnGroupingValueExpression;

        public ColumnGroupingInfo(BaseColumn column) {
            this.column = column;
            columnId = column.getId();
            columnGroupingValueExpression = getColumnGroupingValueExpression(columnId);
            if (columnGroupingValueExpression == null)
                throw new FacesException("The column by which grouping is performed should have its " +
                        "value, groupingExpression or sortingExpression attribute defined, or have a " +
                        "UIOutputComponent from which the grouping expression can be derived automatically. " +
                        "Column id: " + columnId);

            // The presence of per-column group headers and footers for a certain group depends not on the declaration
            // of the column whose data unites the records in this group, but on the presence of per-column group
            // header/footer facets in at least of one of the rendered columns
            DataTable table = (DataTable) column.getTable();
            List<BaseColumn> renderedColumns = table.getRenderedColumns();
            for (BaseColumn renderedColumn : renderedColumns) {
                inHeadersSpecified |= renderedColumn.getInGroupHeader() != null;
                inGroupFootersSpecified |= renderedColumn.getInGroupFooter() != null;
                if (inHeadersSpecified && inGroupFootersSpecified) break;
            }
            groupFooterSpecified = column.getGroupFooter() != null;

            RowGrouping rowGrouping = table.getRowGrouping();
            inGroupFootersCollapsible = rowGrouping.getInGroupFootersCollapsible();
            groupFootersCollapsible = rowGrouping.getGroupFootersCollapsible();
        }

        private ValueExpression getColumnGroupingValueExpression(String columnId) {
            if (columnId == null) return null;

            List<BaseColumn> allColumns = table.getAllColumns();
            BaseColumn baseColumn = table.findColumnById(allColumns, columnId);
            return baseColumn.getColumnGroupingExpression();
        }

        /**
         * @return a Runnable instance which restores the context to the old state as it was prior to invoking this method
         */
        public Runnable enterColumnContext() {
            if (column instanceof DynamicColumn) {
                return ((DynamicColumn) column).enterComponentContext();
            }
            return null;
        }

        public String getColumnId() {
            return columnId;
        }

        public ValueExpression getColumnGroupingValueExpression() {
            return columnGroupingValueExpression;
        }

        public boolean isInHeadersSpecified() {
            return inHeadersSpecified;
        }

        public boolean isInGroupFootersSpecified() {
            return inGroupFootersSpecified;
        }

        public boolean isGroupFooterSpecified() {
            return groupFooterSpecified;
        }

        public boolean isInGroupFootersCollapsible() {
            return inGroupFootersCollapsible;
        }

        public boolean isGroupFootersCollapsible() {
            return groupFootersCollapsible;
        }
    }

    public Boolean getClearUnDisplayedSelection() {
        return clearUnDisplayedSelection;
    }

    public void setClearUnDisplayedSelection(Boolean clearUnDisplayedSelection) {
        this.clearUnDisplayedSelection = clearUnDisplayedSelection;
    }
}
