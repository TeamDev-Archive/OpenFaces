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

import org.openfaces.component.filter.Filter;
import org.openfaces.component.table.impl.GroupingStructureColumn;
import org.openfaces.component.table.impl.NodeInfo;
import org.openfaces.component.table.impl.TableDataModel;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Faces;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The DataTable component is used to display data in a tabular format and effectively manipulate it.
 * It supports the features of the JSF HtmlDataTable component and extends the standard functionality
 * with such advanced features as sorting, row selection (both multiple and single), pagination,
 * filtering, keyboard navigation, and dynamic data loading (using Ajax.) Plus, the DataTable
 * component provides special support for handling large datasets with minimal overhead.
 *
 * @author Dmitry Pikhulya
 */
public class DataTable extends AbstractTable {
    public static final String COMPONENT_TYPE = "org.openfaces.DataTable";
    public static final String COMPONENT_FAMILY = "org.openfaces.DataTable";
    private static final String RENDERED_PAGE_COUNT_ATTR = "_renderedPageCount";

    private Integer pageSize;
    private Integer pageIndex;

    private String rowIndexVar;
    private Boolean paginationKeyboardSupport;
    private PaginationOnSorting paginationOnSorting;
    private Boolean customDataProviding;

    public DataTable() {
        setRendererType("org.openfaces.DataTableRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void setValueExpression(String name, ValueExpression expression) {
        super.setValueExpression(name, expression);
        if ("rowKey".equals(name))
            getModel().setRowKeyExpression(expression);
        else if ("rowDataByKey".equals(name))
            getModel().setRowDataByKeyExpression(expression);
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, rowIndexVar, pageSize, pageIndex, paginationKeyboardSupport,
                paginationOnSorting, customDataProviding};
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        rowIndexVar = (String) state[i++];
        pageSize = (Integer) state[i++];
        pageIndex = (Integer) state[i++];
        paginationKeyboardSupport = (Boolean) state[i++];
        paginationOnSorting = (PaginationOnSorting) state[i++];
        customDataProviding = (Boolean) state[i++];
    }

    @Override
    protected void beforeProcessDecodes(FacesContext context) {
        super.beforeProcessDecodes(context);
        TableDataModel model = getModel();
        model.prepareForRestoringRowIndexes();
        restoreRows(false);
    }

    @Override
    protected void restoreRows(boolean forceDecodingRows) {
        TableDataModel model = getModel();
        boolean rowsDecodingRequired = forceDecodingRows || isRowsDecodingRequired();
        updateModel(false, false, rowsDecodingRequired);
        Set unavailableRowIndexes = model.restoreRows(rowsDecodingRequired);
        setUnavailableRowIndexes(unavailableRowIndexes);
    }

    protected void validateSortingGroupingColumns() {
        super.validateSortingGroupingColumns();
        RowGrouping rowGrouping = getRowGrouping();
        if (rowGrouping != null) {
            List<GroupingRule> rules = rowGrouping.getGroupingRules();
            validateSortingOrGroupingRules(rules);
        }
    }

    /**
     * @return Returns the size of a page if the table is being paged and zero if the table is not being paged.
     */
    public int getPageSize() {
        return ValueBindings.get(this, "pageSize", pageSize, 0);
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 0)
            throw new IllegalArgumentException("pageSize should either be zero or a positive number");
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return ValueBindings.get(this, "pageIndex", pageIndex, 0);
    }

    public void setPageIndex(int pageIndex) {
        if (pageIndex < 0)
            throw new IllegalArgumentException("pageIndex should either be zero or a positive number");
        this.pageIndex = pageIndex;
    }

    public String getRowIndexVar() {
        return rowIndexVar;
    }

    public void setRowIndexVar(String rowIndexVar) {
        this.rowIndexVar = rowIndexVar;
    }

    public boolean isPaginationKeyboardSupport() {
        return ValueBindings.get(this, "paginationKeyboardSupport", paginationKeyboardSupport, true);
    }

    public void setPaginationKeyboardSupport(boolean paginationKeyboardSupport) {
        this.paginationKeyboardSupport = paginationKeyboardSupport;
    }

    public PaginationOnSorting getPaginationOnSorting() {
        return ValueBindings.get(this, "paginationOnSorting", paginationOnSorting, PaginationOnSorting.FIRST_PAGE,
                PaginationOnSorting.class);
    }

    public void setPaginationOnSorting(PaginationOnSorting paginationOnSorting) {
        this.paginationOnSorting = paginationOnSorting;
    }

    /**
     * @return the number of pages if pagination is currently enabled. If pagination is disabled (by setting pageSize to 0) or
     *         if the total number of rows is unknown then -1 is returned. Note the that if pagination is enabled then there will
     *         always be at least one page even if there are no rows to show.
     */
    public int getPageCount() {
        Integer renderedPageCount = getRenderedPageCount();
        if (renderedPageCount != null)
            return renderedPageCount;
        else
            return getModel().getPageCount();
    }

    public ValueExpression getValueExpression() {
        return getValueExpression("value");
    }

    public void setValueExpression(ValueExpression value) {
        setValueExpression("value", value);
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        RowGrouping rowGrouping = getRowGrouping();
        if (rowGrouping != null)
            rowGrouping.processUpdates(context);
    }

    /**
     * @param rowKey row key object for a row whose page index should be detected.
     * @return index of a page where a row with the specified rowKey is displayed, or -1 of no such row is being
     *         displayed.
     */
    public int getPageIndexForRowKey(Object rowKey) {
        TableDataModel model = getModel();
        int pageCount = model.getPageCount();
        int prevPageIndex = getPageIndex();
        try {
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                model.setPageIndex(pageIndex);
                model.setRowKey(rowKey);
                int index = model.getRowIndex();
                if (index != -1)
                    return pageIndex;
            }
        } finally {
            model.setPageIndex(prevPageIndex);
        }
        return -1;
    }

    private Integer getRenderedPageCount() {
        Integer renderedPageCountObj = (Integer) getAttributes().get(RENDERED_PAGE_COUNT_ATTR);
        return renderedPageCountObj;
    }

    private void setRenderedPageCount(int renderedPageCount) {
        getAttributes().put(RENDERED_PAGE_COUNT_ATTR, renderedPageCount);
    }

    @Override
    protected void beforeRenderResponse(FacesContext context) {
        super.beforeRenderResponse(context);
        RowGrouping rowGrouping = getRowGrouping();
        if (rowGrouping != null)
            rowGrouping.beforeEncode();
        if ((TableUtil.isSortingToggledInThisRequest(context) || TableUtil.isFilteringToggledInThisRequest(context)) && getPageSize() > 0) {
            Boolean restorePaginationPage = false;
            if (getKeepSelectionVisible()) {
                AbstractTableSelection selection = getSelection();
                if (selection instanceof DataTableSelection) {
                    TableDataModel model = getModel();
                    int pageCount = model.getPageCount();
                    Object firstRowData = ((DataTableSelection) selection).getFirstSelectedRowData();
                    if (firstRowData != null) {
                        for (pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                            model.setPageIndex(pageIndex);
                            if (model.isObjectInList(firstRowData)) {
                                restorePaginationPage = true;
                                break;
                            }
                        }
                    }
                }

            }
            if (!restorePaginationPage && TableUtil.isFilteringToggledInThisRequest(context)) {
                pageIndex = 0;
                getModel().setPageIndex(0);
            }
            if (!restorePaginationPage && TableUtil.isSortingToggledInThisRequest(context)) {
                PaginationOnSorting paginationOnSorting = getPaginationOnSorting();
                switch (paginationOnSorting) {
                    case SAME_PAGE:
                        break;
                    case FIRST_PAGE:
                        pageIndex = 0;
                        break;
                    default:
                        throw new IllegalStateException("Unknown value of PaginationOnSorting enumeration: " +
                                paginationOnSorting);
                }
            }
        }
        if (pageIndex != null)
            rememberSelectionByKeys();
        if (pageIndex != null && ValueBindings.set(this, "pageIndex", pageIndex))
            pageIndex = null;
    }



    @Override
    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(facesContext))
            return;
           beforeRenderResponse(facesContext);

        setRenderedPageCount(getModel().getPageCount());
        super.encodeBegin(facesContext);
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;
        super.encodeChildren(context);
    }


    @Override
    public void encodeEnd(FacesContext context) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;
        super.encodeEnd(context);
    }

    @Override
    protected void updateModel() {
        updateModel(true, true, true);
    }

    /**
     * This method is invoked during the normal DataTable operation, and application developers shouldn't invoke this
     * method explicitly. Note that this method returns the proper value only after the DataTable is rendered for the
     * first time.
     */
    public void updateModel(boolean updateSortingFromBindings, boolean updatePageIndex, boolean readActualData) {
        setRowsDecodingRequired(readActualData);
        TableDataModel model = getModel();
        model.startUpdate();
        int incomingPageIndex = Integer.MIN_VALUE;
        try {
            if (updateSortingFromBindings) {
                updateSortingFromBindings();
                model.setSortingRules(getSortingRules());

                RowGrouping rowGrouping = getRowGrouping();
                if (rowGrouping != null)
                    model.setGroupingRules(rowGrouping.getGroupingRules());
            }

            if (getRowsDecodingRequired())
                model.setWrappedData(getValueExpression());
            else
                model.setWrappedData(Collections.EMPTY_LIST);
            model.setFilters(getActiveFilters());
            model.setPageSize(getPageSize());

            if (updatePageIndex) {
                incomingPageIndex = getPageIndex();
                model.setPageIndex(incomingPageIndex);
            }
        } finally {
            model.endUpdate();
            if (updatePageIndex && incomingPageIndex != Integer.MIN_VALUE) {
                int newPageIndex = model.getPageIndex();
                if (newPageIndex != incomingPageIndex) {
                    setPageIndex(newPageIndex);
                }
            }
        }
    }

    private void validatePageIndex() {
        Integer pageCount = getRenderedPageCount();
        if (pageCount == null) {
            // renderedPageCount is set on rendering. It's possible that there's no previous rendering,
            // and processUpdates is invoked anyway because table's "rendered" attribute just becomes true on the
            // processDecodes phase (see JSFC-3600)
            return;
        }

        int incomingPageIndex = getPageIndex();
        int newPageIndex = incomingPageIndex;
        if (newPageIndex >= pageCount)
            newPageIndex = pageCount - 1;
        if (newPageIndex < 0)
            newPageIndex = 0;
        if (newPageIndex != incomingPageIndex)
            setPageIndex(newPageIndex);
    }


    public List getRowListForFiltering(Filter filter) {
        TableDataModel tableDataModel = (TableDataModel) getUiDataValue();
        List result = tableDataModel.getRowListForFiltering(filter);
        return result;
    }

    public boolean isDataSourceEmpty() {
        TableDataModel model = getModel();
        if (model == null)
            return true;
        boolean result = model.isSourceDataModelEmpty();
        return result;
    }

    public void setRowIndex(int rowIndex) {
        super.setRowIndex(rowIndex);
        if (rowIndexVar != null) {
            int pageSize = getPageSize();
            int recordNo = pageSize > 0 ? pageSize * getPageIndex() + rowIndex : rowIndex;
            ExternalContext externalContext = getFacesContext().getExternalContext();
            Map<String, Object> requestMap = externalContext.getRequestMap();
            requestMap.put(rowIndexVar, recordNo);
        }

        RowGrouping rowGrouping = getRowGrouping();
        if (rowGrouping != null)
            rowGrouping.setupCurrentRowVariables();

    }

    public boolean getCustomDataProviding() {
        return ValueBindings.get(this, "customDataProviding", customDataProviding, false);
    }

    public void setCustomDataProviding(boolean customDataProviding) {
        this.customDataProviding = customDataProviding;
    }

    protected boolean isRowInColumnSelected(BaseColumn column, Map<String, Object> requestMap, String var) {
        Object rowData = Faces.var(getVar());
        Object rowKey = getModel().requestRowKeyByRowData(FacesContext.getCurrentInstance(), requestMap, var, rowData, -1, -1);
        List selectedRowKeys = getSelectedRowKeys(column);
        return selectedRowKeys.contains(rowKey);
    }

    private List getSelectedRowKeys(BaseColumn column) {
        if (column instanceof SelectionColumn) {
            DataTableSelection selection = (DataTableSelection) getSelection();
            return selection.getSelectedRowKeys();
        } else if (column instanceof CheckboxColumn) {
            return ((CheckboxColumn) column).getSelectedRowKeys();
        } else
            throw new IllegalArgumentException("Unknown column type: " +
                    (column != null ? column.getClass().getName() : "null"));
    }

    @Override
    public void filterChanged(Filter filter) {
        if (getPageIndex() > 0)
            setPageIndex(0);
    }

    private RowGrouping[] cachedRowGrouping;

    public RowGrouping getRowGrouping() {
        if (cachedRowGrouping == null) {
            RowGrouping rowGrouping = Components.findChildWithClass(this, RowGrouping.class, "<o:rowGrouping>");
            cachedRowGrouping = new RowGrouping[]{rowGrouping};
        }
        return cachedRowGrouping[0];
    }

    public Map<Object, ? extends NodeInfo> getTreeStructureMap(FacesContext context) {
        return getModel().getDerivedRowHierarchy();
    }

    @Override
    public int getNodeLevel() {
        TableDataModel model = getModel();
        return model.getNodeLevel();
    }

    @Override
    public boolean getNodeHasChildren() {
        int rowIndex = getRowIndex();
        return getNodeHasChildren(rowIndex);
    }

    @Override
    protected boolean getNodeHasChildren(int rowIndex) {
        TableDataModel model = getModel();
        Map<Object, ? extends NodeInfo> rowHierarchy = model.getDerivedRowHierarchy();
        if (rowHierarchy == null) return false;

        if (rowIndex == -1) return false;
        NodeInfo nodeInfo = rowHierarchy.get(rowIndex);
        if (nodeInfo == null) return false;

        return nodeInfo.getNodeHasChildren();
    }

    @Override
    public boolean isNodeExpanded() {
        Object rowKey = getRowKey();
        TreePath keyPath = new TreePath(rowKey, null);
        return isNodeExpanded(keyPath);
    }

    @Override
    protected boolean isNodeExpanded(TreePath keyPath) {
        RowGrouping rowGrouping = getRowGrouping();
        if (rowGrouping == null) throw
                new IllegalStateException("isNodeExpanded can only be called from a table with row grouping capability");
        return rowGrouping.isNodeExpanded(keyPath);
    }

    @Override
    protected void setNodeExpanded(TreePath keyPath, boolean expanded) {
        RowGrouping rowGrouping = getRowGrouping();
        if (rowGrouping == null) throw
                new IllegalStateException("setNodeExpanded can only be called from a table with row grouping capability");
        rowGrouping.setNodeExpanded(keyPath, expanded);
    }

    @Override
    protected void checkExportSupportedInCurrentState() {
        RowGrouping rowGrouping = getRowGrouping();
        if (rowGrouping == null) return;
        if (rowGrouping.getGroupingRules().size() > 0)
            throw new IllegalStateException("DataTable export functionality is not supported on a grouped DataTable " +
                    "component");
    }

    /**
     * Returns the same list as getRenderedColumns(), but after adaptations to some of its entries that might be
     * required for such features as the Row Grouping feature, where the first column is implicitly converted to
     * TreeColumn. This cannot be done in getRenderedColumns() safely because this would require replacing the first
     * column in the original list with a tree column in the component parent/child hierarchy, and this adaptation
     * (replacing column with tree column) should be done only within the scope of a single request because the set and
     * order of rendered columns can be different on each request.
     */
    @Override
    public List<BaseColumn> getAdaptedRenderedColumns() {
        List<BaseColumn> renderedColumns = getRenderedColumns();

        RowGrouping rowGrouping = getRowGrouping();
        if (rowGrouping == null) return renderedColumns;

        List<GroupingRule> groupingRules = rowGrouping.getGroupingRules();
        if (groupingRules == null || groupingRules.size() == 0) return renderedColumns;

        BaseColumn originalColumn = null;
        int originalColumnIndex = -1;
        for (int i = 0, count = renderedColumns.size(); i < count; i++) {
            BaseColumn renderedColumn = renderedColumns.get(i);
            if (renderedColumn instanceof Column) {
                originalColumnIndex = i;
                originalColumn = renderedColumn;
                break;
            }
        }
        if (originalColumn == null)
            return renderedColumns;
        TreeColumn treeColumn = convertToTreeColumn(originalColumn);
        renderedColumns = new ArrayList<BaseColumn>(renderedColumns);
        renderedColumns.set(originalColumnIndex, treeColumn);

        return renderedColumns;
    }

    private TreeColumn convertToTreeColumn(BaseColumn column) {
        return new GroupingStructureColumn(column);
    }

    public static DataTable getGroupedDataTable(TreeColumn treeColumn) {
        return treeColumn instanceof GroupingStructureColumn
                ? (DataTable) treeColumn.getTable()
                : null;
    }

    @Override
    public ColumnReordering getColumnReordering() {
        ColumnReordering columnReordering = super.getColumnReordering();
        if (columnReordering == null && getRowGrouping() != null) {
            if (getGroupingBox() != null) {
                // row grouping box requires column reordering so we're implicitly creating it if it is not specified
                // explicitly
                columnReordering = new ColumnReordering();
                columnReordering.setTable(this);
            }
        }
        return columnReordering;
    }

    private Summaries[] summaries;

    public Summaries getSummaries() {
        if (summaries == null) {
            summaries = new Summaries[]{Components.findChildWithClass(this, Summaries.class, "<o:summaries>")};
        }
        return summaries[0];
    }

    private GroupingBox getGroupingBox() {
        List<GroupingBox> groupingBoxList = Components.findFacetsWithClass(this, GroupingBox.class);
        if (groupingBoxList.size() > 1)
            throw new FacesException("No more than one <o:groupingBox> can be specified for a table, but there are " +
                    groupingBoxList.size() + "; table clientId: " + getClientId(FacesContext.getCurrentInstance()));
        if (groupingBoxList.size() == 1)
            return groupingBoxList.get(0);
        return null;
    }

    @Override
    public Runnable populateRowVariablesWithAnyModelValue() {
        Runnable runnable = super.populateRowVariablesWithAnyModelValue();
        Components.setRequestVariable(getRowIndexVar(), 0);
        return runnable;
    }

    @Override
    public void restoreRowVariables() {
        super.restoreRowVariables();
        Components.restoreRequestVariable(getRowIndexVar());
    }

    @Override
    protected List<UIComponent> getExtensionComponents() {
        ArrayList<UIComponent> result = new ArrayList<UIComponent>(super.getExtensionComponents());
        result.addAll(Arrays.asList(
                getRowGrouping(),
                getSummaries()
        ));
        return result;
    }

}