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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.DataTable;
import org.openfaces.component.table.GroupingRule;
import org.openfaces.component.table.RowGrouping;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RowGroupingRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        RowGrouping rowGrouping = (RowGrouping) component;
        UIComponent parent = rowGrouping.getParent();
        if (!(parent instanceof DataTable))
            throw new IllegalStateException("<o:columnResizing> can only be placed as a child component inside of " +
                    "a <o:dataTable> component. Though the following parent component has been encountered: " +
                    parent.getClass().getName());
        DataTable table = (DataTable) rowGrouping.getParent();

        TableStructure tableStructure = TableStructure.getCurrentInstance(table);
        Set<BaseColumn> visibleAndGroupedColumns = new LinkedHashSet<BaseColumn>(tableStructure.getColumns());
        List<GroupingRule> groupingRules = rowGrouping.getGroupingRules();
        if (groupingRules != null)
            for (GroupingRule groupingRule : groupingRules) {
                String columnId = groupingRule.getColumnId();
                BaseColumn column = table.getColumnById(columnId);
                visibleAndGroupedColumns.add(column);
            }

        String tableClientId = table.getClientId(context);
        List<String> activeColumnIds = new ArrayList<String>();
        List<String> groupableColumnIds = new ArrayList<String>();
        for (BaseColumn column : visibleAndGroupedColumns) {
            Object headerContent = TableHeader.getHeaderOrFooterCellContent(column, true);
            HeaderCell cell = new HeaderCell(column, headerContent, "div",
                    CellKind.COL_HEADER, true, HeaderCell.SortingToggleMode.AUTODETECT);
            String columnId = column.getId();
            activeColumnIds.add(columnId);
            cell.setId(tableClientId + "::groupingHeaderCell:" + columnId);
            cell.setTableStructure(tableStructure);
            cell.render(context, null);

            if (column.isColumnGroupable())
                groupableColumnIds.add(columnId);
        }

        ScriptBuilder buf = new ScriptBuilder();

        boolean foldingRequired = AbstractTableRenderer.encodeFoldingSupport(context, buf, table);

        Rendering.renderInitScript(context, buf.initScript(context, table, "O$.Table._initRowGrouping",
                activeColumnIds,
                groupableColumnIds,
                groupingRules,
                AbstractTableRenderer.DEFAULT_SORTABLE_HEADER_CLASS,
                rowGrouping.getGroupOnHeaderClick(),
                rowGrouping.getHideGroupingColumns()
        ).semicolon(), foldingRequired
                ? new String[]{AbstractTableRenderer.treeTableJsURL(context)}
                : new String[0]);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        RowGrouping rowGrouping = (RowGrouping) component;
        decodeGroupingRules(context, rowGrouping);

    }

    private void decodeGroupingRules(FacesContext context, RowGrouping rowGrouping) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        DataTable table = (DataTable) rowGrouping.getParent();
        String paramName = table.getClientId(context) + "::setGroupingRules";
        String groupingRulesStr = requestParameterMap.get(paramName);
        if (Rendering.isNullOrEmpty(groupingRulesStr)) return;

        List<GroupingRule> groupingRules = new ArrayList<GroupingRule>();
        try {
            JSONArray sortingRulesJson = new JSONArray(groupingRulesStr);
            for (int i = 0, count = sortingRulesJson.length(); i < count; i++) {
                JSONObject jsonObject = sortingRulesJson.getJSONObject(i);
                groupingRules.add(new GroupingRule(jsonObject));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        rowGrouping.acceptNewGroupingRules(groupingRules);
    }

}
