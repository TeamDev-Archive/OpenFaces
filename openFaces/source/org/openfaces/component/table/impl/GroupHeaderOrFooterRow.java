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

import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.Cell;
import org.openfaces.component.table.Column;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.GroupHeaderOrFooter;
import org.openfaces.component.table.Row;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class GroupHeaderOrFooterRow extends Row {
    protected static final String SYNTHETIC_GROUP_HEADER_CELL_MARKER = "_syntheticGroupHeaderCell";
    private DataTable dataTable;
    private List<UIComponent> cells;

    public GroupHeaderOrFooterRow() {
    }

    public GroupHeaderOrFooterRow(DataTable dataTable) {
        if (dataTable == null) throw new IllegalArgumentException("dataTable shouldn't be null");
        this.dataTable = dataTable;

        Cell groupHeaderCell = createDefaultCell(dataTable);
        cells = Collections.singletonList((UIComponent) groupHeaderCell);
    }

    public static boolean isSyntheticGroupHeaderCell(Cell cell) {
        return cell.getAttributes().containsKey(SYNTHETIC_GROUP_HEADER_CELL_MARKER);
    }

    /**
     * @return a reference to the DataTable only if this row has been created by the table rendering routines and not
     *         by the user, and null otherwise (if this row has been added by the user)
     */
    protected DataTable getExplicitlyAssociatedDataTable() {
        return dataTable;
    }

    private DataTable getDataTable() {
        if (dataTable == null) {
            dataTable = (DataTable) Components.checkParentTag(this, DataTable.class);
        }
        return dataTable;
    }

    @Override
    public boolean getCondition() {
        Object currentRowData = getDataTable().getRowData();
        return currentRowData != null && getExpectedRowDataClass().isAssignableFrom(currentRowData.getClass());
    }

    protected abstract Class<? extends GroupHeaderOrFooter> getExpectedRowDataClass();

    @Override
    public List<UIComponent> getChildren() {
        if (cells == null || super.getChildCount() > 0)
            return super.getChildren();
        else
            return cells;
    }

    @Override
    public int getChildCount() {
        return getChildren().size();
    }

    protected Cell createDefaultCell(DataTable dataTable) {
        FacesContext context = FacesContext.getCurrentInstance();
        List<HtmlOutputText> defaultChildList = getDefaultChildList(dataTable, context);

        String cellContentFacetName = getCellContentFacetName();
        Cell groupHeaderCell = new GroupHeaderOrFooterCell(dataTable, defaultChildList, cellContentFacetName);

        skipLeadingSelectionColumns(dataTable, groupHeaderCell);
        return groupHeaderCell;
    }

    protected List<HtmlOutputText> getDefaultChildList(DataTable dataTable, FacesContext context) {
        return Collections.emptyList();
    }

    protected abstract String getCellContentFacetName();

    /**
     * Updates the passed groupHeaderCell to skip selection/check-box columns for them to be displayed on the left side
     * untouched by tree structure
     */
    private void skipLeadingSelectionColumns(DataTable dataTable, Cell groupHeaderCell) {
        List<BaseColumn> allColumns = dataTable.getAllColumns();
        String firstOrdinaryColumnId = null;
        int firstOrdinaryColumnIndex = -1;
        {
            for (int i = 0, allColumnsSize = allColumns.size(); i < allColumnsSize; i++) {
                BaseColumn column = allColumns.get(i);
                if (column instanceof Column) {
                    firstOrdinaryColumnId = column.getId();
                    firstOrdinaryColumnIndex = i;
                    break;
                }
            }
        }
        if (firstOrdinaryColumnId != null)
            groupHeaderCell.setColumnIds(Collections.singletonList(firstOrdinaryColumnId));
        int allColumnCount = allColumns.size();
        groupHeaderCell.setSpan(allColumnCount - firstOrdinaryColumnIndex);
    }

}
