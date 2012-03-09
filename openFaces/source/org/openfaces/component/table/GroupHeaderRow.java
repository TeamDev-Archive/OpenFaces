/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.List;

public class GroupHeaderRow extends GroupHeaderOrFooterRow {
    public static final String COMPONENT_TYPE = "org.openfaces.GroupHeaderRow";
    public static final String COMPONENT_FAMILY = "org.openfaces.GroupHeaderRow";
    private static final String SYNTHETIC_GROUP_HEADER_CELL_MARKER = "_syntheticGroupHeaderCell";

    private List<UIComponent> cells;

    public GroupHeaderRow() {
    }

    @Override
    protected Class<? extends RowGroupHeaderOrFooter> getExpectedRowDataClass() {
        return RowGroupHeader.class;
    }

    public GroupHeaderRow(DataTable dataTable) {
        super(dataTable);

        Cell groupHeaderCell = createDefaultCell(dataTable);
        cells = Collections.singletonList((UIComponent) groupHeaderCell);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

    }

    private Cell createDefaultCell(DataTable dataTable) {
        FacesContext context = FacesContext.getCurrentInstance();
        RowGrouping rowGrouping = dataTable.getRowGrouping();
        HtmlOutputText outputText = (HtmlOutputText) context.getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
        outputText.setValueExpression("value", rowGrouping.getGroupHeaderTextExpression());

        Cell groupHeaderCell = new GroupHeaderCell(dataTable, outputText);
        groupHeaderCell.getAttributes().put(SYNTHETIC_GROUP_HEADER_CELL_MARKER, true);

        // skip selection/check-box columns for them to be displayed on the left side untouched by tree structure
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
        return groupHeaderCell;
    }

    public static boolean isSyntheticGroupHeaderCell(Cell cell) {
        return cell.getAttributes().containsKey(SYNTHETIC_GROUP_HEADER_CELL_MARKER);
    }

    @Override
    protected String getDefaultStyleClass() {
        return "o_rowGroupHeader";
    }

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

}
