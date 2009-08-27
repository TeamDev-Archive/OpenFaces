/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
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
import org.openfaces.component.table.CheckboxColumn;
import org.openfaces.component.table.NodeInfoForRow;
import org.openfaces.component.table.TreeColumn;
import org.openfaces.component.table.TreePath;
import org.openfaces.component.table.TreeTable;
import org.openfaces.component.table.TreeTableSelection;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.StyleGroup;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableRenderer extends AbstractTableRenderer implements AjaxPortionRenderer {
    private static final String DEFAULT_AUXILARY_NODE_CLASS = "o_treetable_auxilary_node";
    private static final String SUB_ROWS_PORTION = "subRows:";
    private static final String DEFAULT_FOLDING_CLASS = "o_treetable_folding";
    private static final String HIDDEN_ROW_CLASS = "o_hiddenRow";

    @Override
    protected void writeBodyRowAttributes(FacesContext facesContext, AbstractTable table) throws IOException {
        super.writeBodyRowAttributes(facesContext, table);
        TreeTable treeTable = (TreeTable) table;
        boolean nodeVisible = treeTable.isNodeInitiallyVisible();
        if (!nodeVisible) {
            ResponseWriter responseWriter = facesContext.getResponseWriter();
            responseWriter.writeAttribute("class", HIDDEN_ROW_CLASS, null);
        }
    }

    @Override
    protected String getAdditionalRowClass(FacesContext facesContext, AbstractTable table, Object rowData, int rowIndex) {
        TreeTable treeTable = (TreeTable) table;
        if (!treeTable.isFilteringPerformed())
            return null;
        boolean nodeAcceptedByFilters = treeTable.isNodeAcceptedByFilters();
        String result = nodeAcceptedByFilters
                ? getFilterAcceptedRowClass(facesContext, treeTable)
                : getFilterSubsidiaryRowClass(facesContext, treeTable);
        return result;
    }

    private String getFilterSubsidiaryRowClass(FacesContext facesContext, TreeTable treeTable) {
        return StyleUtil.getCSSClass(facesContext,
                treeTable, treeTable.getFilterSubsidiaryRowStyle(),
                StyleGroup.rolloverStyleGroup(), treeTable.getFilterSubsidiaryRowClass(),
                DEFAULT_AUXILARY_NODE_CLASS
        );
    }

    private String getFilterAcceptedRowClass(FacesContext facesContext, TreeTable treeTable) {
        return StyleUtil.getCSSClass(facesContext,
                treeTable, treeTable.getFilterAcceptedRowStyle(),
                StyleGroup.rolloverStyleGroup(), treeTable.getFilterAcceptedRowClass()
        );
    }

    @Override
    protected void encodeAdditionalFeaturesSupport_buf(FacesContext facesContext, AbstractTable table, ScriptBuilder buf) throws IOException {
        super.encodeAdditionalFeaturesSupport_buf(facesContext, table, buf);

        TreeTable treeTable = (TreeTable) table;
        encodeFoldingSupport(facesContext, treeTable, buf);

        getFilterAcceptedRowClass(facesContext, treeTable);
        getFilterSubsidiaryRowClass(facesContext, treeTable);
    }

    private void encodeFoldingSupport(FacesContext context, TreeTable treeTable, ScriptBuilder buf) throws IOException {
        boolean foldingEnabled = treeTable.isFoldingEnabled();
        if (!foldingEnabled)
            return;

        ResponseWriter writer = context.getResponseWriter();
        RenderingUtil.renderHiddenField(writer, getExpandedNodesFieldName(context, treeTable), null);

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
        List<BaseColumn> columns = treeTable.getColumnsForRendering();
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

    private JSONObject formatNodeParams(
            TreeTable treeTable, FacesContext context, int fromRowIndex, int rowCount) {
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
                ResourceUtil.getInternalResourceURL(context, TreeTableRenderer.class, "treeTable.js")
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

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component, String portionName, JSONObject jsonParam) throws IOException {
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

        ResponseWriter responseWriter = context.getResponseWriter();
        Writer stringWriter = new StringWriter();
        ResponseWriter clonedResponseWriter = responseWriter.cloneWithWriter(stringWriter);
        context.setResponseWriter(clonedResponseWriter);
        try {
            if (addedRowCount > 0) {
                List<BaseColumn> renderedColumns = treeTable.getColumnsForRendering();
                Map<Integer, CustomRowRenderingInfo> customRowRenderingInfos =
                        (Map<Integer, CustomRowRenderingInfo>) treeTable.getAttributes().get(CUSTOM_ROW_RENDERING_INFOS_KEY);
                for (int i = treeTable.getRowCount(); i > rowIndex; i--) {
                    CustomRowRenderingInfo rowInfo = customRowRenderingInfos.remove(i);
                    if (rowInfo == null)
                        continue;
                    customRowRenderingInfos.put(i + addedRowCount, rowInfo);
                }
                encodeRows(context, treeTable, rowIndex + 1, addedRowCount, renderedColumns, false);
                TreeTableSelection selection = (TreeTableSelection) treeTable.getSelection();
                if (selection != null)
                    selection.encodeOnAjaxNodeFolding(context);
                for (BaseColumn column : renderedColumns) {
                    if (column instanceof CheckboxColumn)
                        ((CheckboxColumn) column).encodeOnAjaxNodeFolding(context);
                }
            }
        } finally {
            context.setResponseWriter(responseWriter);
        }

        JSONArray newNodesInitInfo = new JSONArray();
        newNodesInitInfo.put(formatNodeParams(treeTable, context, rowIndex, addedRowCount));
        Map requestMap = context.getExternalContext().getRequestMap();
        String rowStylesKey = getRowStylesKey(context, treeTable);
        Map rowStylesMap = (Map) requestMap.get(rowStylesKey);
        String cellStylesKey = getCellStylesKey(context, treeTable);
        Map cellStylesMap = (Map) requestMap.get(cellStylesKey);
        newNodesInitInfo.put(TableUtil.getStylesMapAsJSONObject(rowStylesMap));
        newNodesInitInfo.put(TableUtil.getStylesMapAsJSONObject(cellStylesMap));

        treeTable.setRowIndex(-1);

        ScriptBuilder sb = new ScriptBuilder();
        sb.initScript(context, treeTable, "O$.TreeTable._insertSubrows", newNodesInitInfo);

        RenderingUtil.renderInitScript(context, sb, null);
        responseWriter.write(stringWriter.toString());
        return null;
    }
}
