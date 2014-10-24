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

import org.openfaces.component.ContextDependentComponent;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Cell;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.GroupHeaderOrFooter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 */
public class GroupHeaderOrFooterCell extends Cell implements ContextDependentComponent {
    private DataTable dataTable;
    private List<UIComponent> defaultChildList;
    private String facetName;

    private Map<String, List<UIComponent>> cachedFacetMap = new HashMap<String, List<UIComponent>>();

    public GroupHeaderOrFooterCell(DataTable dataTable, List<? extends UIComponent> defaultChildList, String facetName) {
        this.dataTable = dataTable;
        if (defaultChildList != null)
            this.defaultChildList = (List<UIComponent>) defaultChildList;
        else
            this.defaultChildList = Collections.emptyList();
        this.facetName = facetName;
    }

    protected DataTable getDataTable() {
        return dataTable;
    }

    protected String getFacetName() {
        return facetName;
    }

    protected List<UIComponent> getDefaultChildList() {
        return defaultChildList;
    }

    @Override
    public List<UIComponent> getChildren() {
        String columnId = getCurrentColumnId();
        List<UIComponent> facetValue = getColumnFacet(columnId);
        return facetValue != null ? facetValue : defaultChildList;
    }

    private String getCurrentColumnId() {
        GroupHeaderOrFooter row = (GroupHeaderOrFooter) dataTable.getRowData();
        return row.getRowGroup().getColumnId();
    }

    private List<UIComponent> getColumnFacet(String columnId) {
        if (cachedFacetMap.containsKey(columnId))
            return cachedFacetMap.get(columnId);

        BaseColumn groupingColumn = dataTable.getColumnById(columnId);
        UIComponent groupingFacet = facetName != null ? groupingColumn.getFacet(facetName) : null;
        List<UIComponent> facetValue = groupingFacet != null ? Collections.singletonList(groupingFacet) : null;
        cachedFacetMap.put(columnId, facetValue);
        return facetValue;
    }

    @Override
    public int getChildCount() {
        return getChildren().size();
    }

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        Runnable exitContext = enterComponentContext();
        try {
            super.encodeAll(context);
        } finally {
            if (exitContext != null) exitContext.run();
        }
    }

    protected BaseColumn getCurrentlyRenderedColumn() {
        String columnId = getCurrentColumnId();
        return dataTable.getColumnById(columnId);
    }

    public Runnable enterComponentContext() {
        BaseColumn column = getCurrentlyRenderedColumn();
        Runnable undeclareContextVariables = (column instanceof ContextDependentComponent)
                ? ((ContextDependentComponent) column).enterComponentContext()
                : null;
        return undeclareContextVariables;
    }

    public boolean isComponentInContext() {
        BaseColumn column = getCurrentlyRenderedColumn();
        return !(column instanceof ContextDependentComponent) ||
                ((ContextDependentComponent) column).isComponentInContext();
    }

}
