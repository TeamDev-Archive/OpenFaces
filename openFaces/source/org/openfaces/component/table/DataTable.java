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
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Faces;
import org.openfaces.component.filter.Filter;

import javax.el.ValueExpression;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
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
    public Object saveState(FacesContext facesContext) {
        Object superState = super.saveState(facesContext);
        return new Object[]{superState, rowIndexVar, pageSize, pageIndex, paginationKeyboardSupport,
                customDataProviding};
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        rowIndexVar = (String) state[i++];
        pageSize = (Integer) state[i++];
        pageIndex = (Integer) state[i++];
        paginationKeyboardSupport = (Boolean) state[i++];
        customDataProviding = (Boolean) state[i++];
    }

    @Override
    protected void beforeProcessDecodes(FacesContext context) {
        super.beforeProcessDecodes(context);
        TableDataModel model = getModel();
        model.prepareForRestoringRowIndexes();
        boolean rowsDecodingRequired = isRowsDecodingRequired();
        updateModel(false, false, rowsDecodingRequired);
        Set unavailableRowIndexes = model.restoreRows(rowsDecodingRequired);
        setUnavailableRowIndexes(unavailableRowIndexes);
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
    protected void processModelUpdates(FacesContext context) {
        super.processModelUpdates(context);

        if (pageIndex != null)
            rememberSelectionByKeys();
        validatePageIndex();
        if (pageIndex != null && ValueBindings.set(this, "pageIndex", pageIndex))
            pageIndex = null;
    }

    /**
     * @return index of a page where a row with the specified rowKey is displayed, or -1 of no such row is being
     * displayed.
     * @param rowKey row key object for a row whose page index should be detected.
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
    protected void beforeRenderResponse(FacesContext context) {
        super.beforeRenderResponse(context);
        updateModel(true, true, true);
    }

    private void updateModel(boolean updateSortingFromBindings, boolean updatePageIndex, boolean readActualData) {
        TableDataModel model = getModel();
        model.startUpdate();
        int incomingPageIndex = Integer.MIN_VALUE;
        try {
            if (updateSortingFromBindings) {
                updateSortingFromBindings();
                getModel().setSortingRules(getSortingRules());
            }

            if (readActualData)
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
            // renderedPageCount is set on rendering. It's possible that there's no previous rendering, and processUpdates
            // is invoked anyway because table's "rendered" attribute just becamse true on processDecodes phase (see JSFC-3600)
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
            throw new IllegalArgumentException("Unkown column type: " + (column != null ? column.getClass().getName() : "null"));
    }

    @Override
    public void filterChanged(Filter filter) {
        if (getPageIndex() > 0)
            setPageIndex(0);
    }
}