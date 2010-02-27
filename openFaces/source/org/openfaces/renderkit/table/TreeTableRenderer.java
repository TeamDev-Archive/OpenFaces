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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.component.table.TreePath;
import org.openfaces.component.table.TreeTable;
import org.openfaces.component.table.NodeInfoForRow;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONObject;
import org.openfaces.org.json.JSONException;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableRenderer extends AbstractTableRenderer {
    private static final String DEFAULT_AUXILIARY_NODE_CLASS = "o_treetable_auxiliary_node";
    private static final String SUB_ROWS_PORTION = "subRows:";
    private static final String DEFAULT_FOLDING_CLASS = "o_treetable_folding";
    private static final String HIDDEN_ROW_CLASS = "o_hiddenRow";

    @Override
    protected String[][] getBodyRowAttributes(FacesContext context, AbstractTable table) throws IOException {
        super.getBodyRowAttributes(context, table);
        TreeTable treeTable = (TreeTable) table;
        boolean nodeVisible = treeTable.isNodeInitiallyVisible();
        if (!nodeVisible) {
            return new String[][]{
                    new String[]{"class", HIDDEN_ROW_CLASS}
            };
        }
        return null;
    }

    @Override
    protected String getAdditionalRowClass(FacesContext context, AbstractTable table, Object rowData, int rowIndex) {
        TreeTable treeTable = (TreeTable) table;
        if (!treeTable.isFilteringPerformed())
            return null;
        boolean nodeAcceptedByFilters = treeTable.isNodeAcceptedByFilters();
        String result = nodeAcceptedByFilters
                ? getFilterAcceptedRowClass(context, treeTable)
                : getFilterSubsidiaryRowClass(context, treeTable);
        return result;
    }

    private String getFilterSubsidiaryRowClass(FacesContext context, TreeTable treeTable) {
        return Styles.getCSSClass(context,
                treeTable, treeTable.getFilterSubsidiaryRowStyle(),
                StyleGroup.rolloverStyleGroup(), treeTable.getFilterSubsidiaryRowClass(),
                DEFAULT_AUXILIARY_NODE_CLASS
        );
    }

    private String getFilterAcceptedRowClass(FacesContext context, TreeTable treeTable) {
        return Styles.getCSSClass(context,
                treeTable, treeTable.getFilterAcceptedRowStyle(),
                StyleGroup.rolloverStyleGroup(), treeTable.getFilterAcceptedRowClass()
        );
    }

    @Override
    protected void encodeAdditionalFeaturesSupport_buf(FacesContext context, AbstractTable table, ScriptBuilder buf) throws IOException {
        super.encodeAdditionalFeaturesSupport_buf(context, table, buf);

        TreeTable treeTable = (TreeTable) table;
        boolean foldingEnabled = treeTable.isFoldingEnabled();
        if (foldingEnabled)
            encodeFoldingSupport(context, treeTable, buf);

        getFilterAcceptedRowClass(context, treeTable);
        getFilterSubsidiaryRowClass(context, treeTable);
    }

    private void encodeFoldingSupport(FacesContext context, TreeTable treeTable, ScriptBuilder buf) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Rendering.renderHiddenField(writer, getExpandedNodesFieldName(context, treeTable), null);

        buf.initScript(context, treeTable, "O$.TreeTable._initFolding",
                DEFAULT_FOLDING_CLASS,
                getClientFoldingParams(context, treeTable));
    }

    private String getExpandedNodesFieldName(FacesContext context, TreeTable treeTable) {
        return treeTable.getClientId(context) + "::expandedNodes";
    }

    private JSONArray getClientFoldingParams(FacesContext context, TreeTable treeTable) {
        JSONArray result = new JSONArray();
        result.put(formatNodeParams(treeTable, context, -1, -1));
        JSONArray expansionDatasArray = new JSONArray();
        List<Object> expansionDatas = new ArrayList<Object>();
        List<BaseColumn> columns = treeTable.getRenderedColumns();
        for (BaseColumn column : columns) {
            if (!(column instanceof TreeColumn))
                continue;
            TreeColumn treeColumn = (TreeColumn) column;
            Object columnExpansionData = treeColumn.encodeExpansionDataAsJsObject(context);
            expansionDatas.add(columnExpansionData);
        }
        for (Object expansionData : expansionDatas) {
            if (expansionData == null)
                expansionData = JSONObject.NULL;
            expansionDatasArray.put(expansionData);
        }
        result.put(expansionDatas);
        return result;
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        TreeTable treeTable = (TreeTable) component;
        decodeFoldingSupport(context, treeTable);
    }

    private void decodeFoldingSupport(FacesContext context, TreeTable treeTable) {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String expandedNodes = requestParameterMap.get(getExpandedNodesFieldName(context, treeTable));
        if (expandedNodes == null)
            return;
        String[] indexStrs = expandedNodes.split(",");
        Set<Integer> expandedRowIndexes = new HashSet<Integer>(indexStrs.length);
        for (String indexStr : indexStrs) {
            if (indexStr.length() == 0)
                continue;
            int rowIndex = Integer.parseInt(indexStr);
            expandedRowIndexes.add(rowIndex);
        }
        treeTable.acceptNewExpandedRowIndexes(expandedRowIndexes);

//    String toggleEventParamName = treeTable.getClientId(context) + "::toggleExpansion";
//    String rowIndexStr = (String) requestParameterMap.get(toggleEventParamName);
//    if (rowIndexStr != null) {
//      int rowIndex = Integer.parseInt(rowIndexStr);
//      TreePath nodeKeyPath = treeTable.getNodeKeyPath(rowIndex);
//      boolean wasExpanded = treeTable.isNodeExpanded(nodeKeyPath);
//      treeTable.setNodeExpanded(nodeKeyPath, !wasExpanded);
//    }
    }

    public static boolean isAjaxFoldingInProgress(FacesContext context) {
        boolean ajaxRequestInProgress = AjaxUtil.isAjaxRequest(context);
        if (!ajaxRequestInProgress)
            return false;

        List<String> portions = AjaxUtil.getRequestedAjaxPortionNames(context);
        if (portions == null)
            return false;

        for (String portionName : portions) {
            if (portionName.startsWith(SUB_ROWS_PORTION))
                return true;
        }
        return false;
    }

    @Override
    protected String getInitJsAPIFunctionName() {
        return "O$.TreeTable._initTreeTableAPI";
    }

    @Override
    protected String[] getNecessaryJsLibs(FacesContext context) {
        String[] parentLibs = super.getNecessaryJsLibs(context);
        String[] libs = new String[]{
                Resources.getInternalURL(context, TreeTableRenderer.class, "treeTable.js")
        };
        String[] resultLibs = new String[parentLibs.length + libs.length];
        System.arraycopy(parentLibs, 0, resultLibs, 0, parentLibs.length);
        System.arraycopy(libs, 0, resultLibs, parentLibs.length, libs.length);
        return resultLibs;
    }

    @Override
    protected String getTextStyle(AbstractTable table) {
        return ((TreeTable) table).getTextStyle();
    }

    @Override
    protected String getTextClass(AbstractTable table) {
        return ((TreeTable) table).getTextClass();
    }

    public JSONObject encodeAjaxPortion(
            FacesContext context,
            UIComponent component,
            String portionName,
            JSONObject jsonParam
    ) throws IOException {
        if (portionName.equals("rows")) {
            return super.encodeAjaxPortion(context, component, portionName, jsonParam);
        }
        if (!portionName.startsWith(SUB_ROWS_PORTION))
            throw new IllegalArgumentException("Unknown portionName: " + portionName);
        String portionNameSuffix = portionName.substring(SUB_ROWS_PORTION.length());
        int rowIndex = Integer.parseInt(portionNameSuffix);
        TreeTable treeTable = (TreeTable) component;
        boolean rowAvailableAfterRestoring = treeTable.isRowAvailableAfterRestoring(rowIndex);
        if (rowAvailableAfterRestoring) {
            treeTable.setRowIndex(rowIndex);
            TreePath nodeKeyPath = treeTable.getNodeKeyPath();
            treeTable.setNodeExpanded(nodeKeyPath, true);
        }
        int addedRowCount = rowAvailableAfterRestoring ? treeTable.loadSubNodes(rowIndex) : 0;

        Map<Integer, CustomRowRenderingInfo> customRowRenderingInfos =
                (Map<Integer, CustomRowRenderingInfo>) treeTable.getAttributes().get(TableStructure.CUSTOM_ROW_RENDERING_INFOS_KEY);
        for (int i = treeTable.getRowCount(); i > rowIndex; i--) {
            CustomRowRenderingInfo rowInfo = customRowRenderingInfos.remove(i);
            if (rowInfo == null)
                continue;
            customRowRenderingInfos.put(i + addedRowCount, rowInfo);
        }

        return serveDynamicRowsRequest(context, treeTable, rowIndex, addedRowCount);
    }

    protected void fillDynamicRowsInitInfo(FacesContext context, AbstractTable table, int rowIndex, int addedRowCount, TableStructure tableStructure, JSONObject newNodesInitInfo) {
        super.fillDynamicRowsInitInfo(context, table, rowIndex, addedRowCount, tableStructure, newNodesInitInfo);
        Rendering.addJsonParam(newNodesInitInfo, "structureMap", formatNodeParams((TreeTable) table, context, rowIndex, addedRowCount));
    }

    private JSONObject formatNodeParams(TreeTable treeTable, FacesContext context, int fromRowIndex, int rowCount) {
        JSONObject result = new JSONObject();
        Map<Object, NodeInfoForRow> map = treeTable.getNodeExpansionDataMap(context);
        Set<Map.Entry<Object, NodeInfoForRow>> entries = map.entrySet();
        for (Map.Entry<Object, NodeInfoForRow> entry : entries) {
            Object rowIndex = entry.getKey();
            if (fromRowIndex != -1) {
                if (!(rowIndex instanceof Integer))
                    continue;
                int intRowIndex = (Integer) rowIndex;
                if (intRowIndex < fromRowIndex || intRowIndex >= fromRowIndex + rowCount)
                    continue;
                intRowIndex -= fromRowIndex;
                rowIndex = intRowIndex;
            }
            NodeInfoForRow expansionData = entry.getValue();
            boolean nodeHasChildren = expansionData.getNodeHasChildren();
            Object childCount = nodeHasChildren
                    ? (expansionData.getChildrenPreloaded() ? String.valueOf(expansionData.getChildNodeCount()) : "?")
                    : 0;
            try {
                result.put(String.valueOf(rowIndex), childCount);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

}
