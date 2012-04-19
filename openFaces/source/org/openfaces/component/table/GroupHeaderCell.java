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

import javax.faces.component.UIComponent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 */
public class GroupHeaderCell extends Cell {
    public static final String FACET_GROUP_HEADER_ROW = "groupHeaderRow";
    private DataTable dataTable;
    private List<UIComponent> defaultValue;
    private Map<String, List<UIComponent>> cachedFacetMap;

    public GroupHeaderCell(DataTable dataTable, UIComponent defaultValue) {
        this.dataTable = dataTable;
        this.defaultValue = Collections.singletonList(defaultValue);
        cachedFacetMap = new HashMap<String, List<UIComponent>>();
    }

    @Override
    public List<UIComponent> getChildren() {
        RowGroupHeaderOrFooter row = (RowGroupHeaderOrFooter) dataTable.getRowData();
        String columnId = row.getRowGroup().getColumnId();
        List<UIComponent> facetValue = cachedFacetMap.get(columnId);
        if (facetValue == null) {
            BaseColumn groupingColumn = dataTable.getColumnById(columnId);
            UIComponent groupingFacet = groupingColumn.getFacet(FACET_GROUP_HEADER_ROW);
            if (groupingFacet != null) {
                facetValue = Collections.singletonList(groupingFacet);
                cachedFacetMap.put(columnId, facetValue);
            }
        }
        if (facetValue != null)
            return facetValue;
        else
            return defaultValue;
    }

    @Override
    public int getChildCount() {
        return getChildren().size();
    }

}
