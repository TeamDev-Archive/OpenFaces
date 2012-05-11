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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.TreePath;
import org.openfaces.component.table.TreeTable;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableRenderer extends AbstractTableRenderer {
    private static final String DEFAULT_AUXILIARY_NODE_CLASS = "o_treetable_auxiliary_node";
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
    protected void encodeAdditionalFeaturesSupport_buf(
            FacesContext context,
            AbstractTable table,
            ScriptBuilder buf) throws IOException {
        super.encodeAdditionalFeaturesSupport_buf(context, table, buf);

        TreeTable treeTable = (TreeTable) table;
        boolean foldingEnabled = treeTable.isFoldingEnabled();
        if (foldingEnabled) {
            encodeFoldingSupport(context, buf, treeTable);
        }

        getFilterAcceptedRowClass(context, treeTable);
        getFilterSubsidiaryRowClass(context, treeTable);
    }

    @Override
    protected String getInitJsAPIFunctionName() {
        return "O$.TreeTable._initTreeTableAPI";
    }

    @Override
    protected String[] getNecessaryJsLibs(FacesContext context) {
        String[] parentLibs = super.getNecessaryJsLibs(context);
        String[] libs = new String[]{
                treeTableJsURL(context)
        };
        String[] resultLibs = new String[parentLibs.length + libs.length];
        System.arraycopy(parentLibs, 0, resultLibs, 0, parentLibs.length);
        System.arraycopy(libs, 0, resultLibs, parentLibs.length, libs.length);
        return resultLibs;
    }

    @Override
    protected String getTextStyle(AbstractTable table) {
        return table.getTextStyle();
    }

    @Override
    protected String getTextClass(AbstractTable table) {
        return table.getTextClass();
    }

    public JSONObject encodeAjaxPortion(
            FacesContext context,
            UIComponent component,
            String portionName,
            JSONObject jsonParam
    ) throws IOException {
        if (!portionName.startsWith(SUB_ROWS_PORTION))
            return super.encodeAjaxPortion(context, component, portionName, jsonParam);
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

        Map<Integer, CustomRowRenderingInfo> customRowRenderingInfos = (Map<Integer, CustomRowRenderingInfo>)
                treeTable.getAttributes().get(TableStructure.CUSTOM_ROW_RENDERING_INFOS_KEY);
        for (int i = treeTable.getRowCount(); i > rowIndex; i--) {
            CustomRowRenderingInfo rowInfo = customRowRenderingInfos.remove(i);
            if (rowInfo == null)
                continue;
            customRowRenderingInfos.put(i + addedRowCount, rowInfo);
        }

        return serveDynamicRowsRequest(context, treeTable, rowIndex + 1, addedRowCount);
    }

    protected void fillDynamicRowsInitInfo(FacesContext context, AbstractTable table, int rowIndex, int addedRowCount,
                                           TableStructure tableStructure, JSONObject newNodesInitInfo) {
        super.fillDynamicRowsInitInfo(context, table, rowIndex, addedRowCount, tableStructure, newNodesInitInfo);
        JSONObject structureMapObj = formatTreeStructureMap(context, table, rowIndex - 1, addedRowCount);
        Rendering.addJsonParam(newNodesInitInfo, "structureMap", structureMapObj);
    }

}
