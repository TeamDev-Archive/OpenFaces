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

import org.openfaces.component.filter.Filter;
import org.openfaces.renderkit.table.TreeTableRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Environment;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The TreeTable component is used to display hierarchical data in a tabular format.
 * It provides flexible configuration of the tree structure and content and supports
 * such advanced features as sorting, interactive filtering, node selection (both multiple
 * and single), keyboard navigation, and dynamic data loading (using Ajax). You can also
 * specify a node preloading mode for expanding TreeTable nodes on the client or server side.
 *
 * @author Dmitry Pikhulya
 */
public class TreeTable extends AbstractTable {
    public static final String COMPONENT_TYPE = "org.openfaces.TreeTable";
    public static final String COMPONENT_FAMILY = "org.openfaces.TreeTable";

    private String nodeLevelVar;
    private String nodePathVar;
    private String nodeHasChildrenVar;

    private List<NodeInfoForRow> nodeInfoForRows;
    private Map<Object, NodeInfoForRow> rowIndexToExpansionData;

    private ExpansionState expansionState = new AllNodesCollapsed();

    private Integer sortLevel;
    private Boolean foldingEnabled;
    private PreloadedNodes preloadedNodes;

    private String filterAcceptedRowStyle;
    private String filterAcceptedRowClass;
    private String filterSubsidiaryRowStyle;
    private String filterSubsidiaryRowClass;

    private String textStyle;
    private String textClass;


    private List<Filter> collectedFilters;
    private List<NodeInfoForRow> nodeInfoForRows_unfiltered;
    private int deepestLevel;
    private Boolean rowValuesForFilteringNeeded;

    public TreeTable() {
        setRendererType("org.openfaces.TreeTableRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_TYPE;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, nodeLevelVar, nodePathVar, nodeHasChildrenVar, expansionState,
                sortLevel, foldingEnabled, preloadedNodes,
                filterAcceptedRowStyle, filterAcceptedRowClass, filterSubsidiaryRowStyle, filterSubsidiaryRowClass,
                textStyle, textClass};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        nodeLevelVar = (String) state[i++];
        nodePathVar = (String) state[i++];
        nodeHasChildrenVar = (String) state[i++];
        expansionState = (ExpansionState) state[i++];
        sortLevel = (Integer) state[i++];
        foldingEnabled = (Boolean) state[i++];
        preloadedNodes = (PreloadedNodes) state[i++];
        filterAcceptedRowStyle = (String) state[i++];
        filterAcceptedRowClass = (String) state[i++];
        filterSubsidiaryRowStyle = (String) state[i++];
        filterSubsidiaryRowClass = (String) state[i++];
        textStyle = (String) state[i++];
        textClass = (String) state[i++];
    }

    @Override
    protected void beforeProcessDecodes(FacesContext context) {
        super.beforeProcessDecodes(context);
        TableDataModel model = getModel();
        model.prepareForRestoringRowIndexes();
        boolean readFreshData = TreeTableRenderer.isAjaxFoldingInProgress(context) || isRowsDecodingRequired();
        prepareModelFromTreeStructure(readFreshData);

        TableDataModel.RestoredRowIndexes rri = model.restoreRowIndexes();
        Set<Integer> unavailableRowIndexes = rri.getUnavailableRowIndexes();
        setUnavailableRowIndexes(unavailableRowIndexes);

        int[] oldIndexes = rri.getOldIndexes();
        List<NodeInfoForRow> newNodeInfoForRows = new ArrayList<NodeInfoForRow>(oldIndexes.length);
        for (int oldIndex : oldIndexes) {
            NodeInfoForRow nodeInfo = oldIndex != -1 ? nodeInfoForRows.get(oldIndex) : null;
            newNodeInfoForRows.add(nodeInfo);
        }
        nodeInfoForRows = newNodeInfoForRows;
    }

    @Override
    protected String getRowClientSuffixByIndex(int index) {
        if (nodeInfoForRows == null || index >= nodeInfoForRows.size()) {
            // can be the case when pre-rendering TreeTable inspections are made by the filters
            return null;
        }

        NodeInfoForRow nodeInfoForRow = nodeInfoForRows.get(index);
        if (nodeInfoForRow == null)
            return null;
        TreePath indexPath = nodeInfoForRow.getNodeIndexPath();
        return indexPathToStr(indexPath);
    }


    @Override
    public int getRowIndexByClientSuffix(String id) {
        for (int i = 0, count = nodeInfoForRows.size(); i < count; i++) {
            NodeInfoForRow nodeInfoForRow = nodeInfoForRows.get(i);
            TreePath nodeIndexPath = nodeInfoForRow.getNodeIndexPath();
            String indexPathStr = indexPathToStr(nodeIndexPath);
            if (id.equals(indexPathStr))
                return i;
        }
        return -1;
    }

    private String indexPathToStr(TreePath indexPath) {
        StringBuilder sb = new StringBuilder();
        for (TreePath path = indexPath; path != null; path = path.getParentPath()) {
            if (sb.length() > 0) sb.insert(0, "_");
            sb.insert(0, path.getValue());
        }
        return sb.toString();
    }


    public ExpansionState getExpansionState() {
        return expansionState;
    }

    public void setExpansionState(ExpansionState expansionState) {
        this.expansionState = expansionState;
    }

    public boolean isFoldingEnabled() {
        return ValueBindings.get(this, "foldingEnabled", foldingEnabled, true);
    }

    public void setFoldingEnabled(boolean foldingEnabled) {
        this.foldingEnabled = foldingEnabled;
    }

    public PreloadedNodes getPreloadedNodes() {
        return ValueBindings.get(this, "preloadedNodes", preloadedNodes, PreloadedNodes.class);
    }

    public void setPreloadedNodes(PreloadedNodes preloadedNodes) {
        this.preloadedNodes = preloadedNodes;
    }

    public int getSortLevel() {
        return ValueBindings.get(this, "sortLevel", sortLevel, -1);
    }

    public void setSortLevel(int sortLevel) {
        this.sortLevel = sortLevel;
    }

    public String getNodeLevelVar() {
        return nodeLevelVar;
    }

    public void setNodeLevelVar(String nodeLevelVar) {
        this.nodeLevelVar = nodeLevelVar;
    }

    public String getNodeHasChildrenVar() {
        return nodeHasChildrenVar;
    }

    public void setNodeHasChildrenVar(String nodeHasChildrenVar) {
        this.nodeHasChildrenVar = nodeHasChildrenVar;
    }

    public String getNodePathVar() {
        return nodePathVar;
    }

    public void setNodePathVar(String nodePathVar) {
        this.nodePathVar = nodePathVar;
    }

    public String getFilterAcceptedRowStyle() {
        return ValueBindings.get(this, "filterAcceptedRowStyle", filterAcceptedRowStyle);
    }

    public void setFilterAcceptedRowStyle(String filterAcceptedRowStyle) {
        this.filterAcceptedRowStyle = filterAcceptedRowStyle;
    }

    public String getFilterAcceptedRowClass() {
        return ValueBindings.get(this, "filterAcceptedRowClass", filterAcceptedRowClass);
    }

    public void setFilterAcceptedRowClass(String filterAcceptedRowClass) {
        this.filterAcceptedRowClass = filterAcceptedRowClass;
    }

    public String getFilterSubsidiaryRowStyle() {
        return ValueBindings.get(this, "filterSubsidiaryRowStyle", filterSubsidiaryRowStyle);
    }

    public void setFilterSubsidiaryRowStyle(String filterSubsidiaryRowStyle) {
        this.filterSubsidiaryRowStyle = filterSubsidiaryRowStyle;
    }

    public String getFilterSubsidiaryRowClass() {
        return ValueBindings.get(this, "filterSubsidiaryRowClass", filterSubsidiaryRowClass);
    }

    public String getTextStyle() {
        return ValueBindings.get(this, "textStyle", textStyle);
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }

    public String getTextClass() {
        return ValueBindings.get(this, "textClass", textClass);
    }

    public void setTextClass(String textClass) {
        this.textClass = textClass;
    }

    public void setFilterSubsidiaryRowClass(String filterSubsidiaryRowClass) {
        this.filterSubsidiaryRowClass = filterSubsidiaryRowClass;
    }

    public TreeStructure getTreeStructure() {
        return findTreeStructureChild();
    }

    private TreeStructure findTreeStructureChild() {
        List<UIComponent> children = getChildren();
        TreeStructure treeStructure = null;
        for (int i = 0, childCount = children.size(); i < childCount; i++) {
            UIComponent child = children.get(i);
            if (child instanceof TreeStructure) {
                if (treeStructure != null)
                    throw new RuntimeException("There should be only one tree structure child under the TreeTable component");
                treeStructure = (TreeStructure) child;
            }
        }
        return treeStructure;
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);
        ValueExpression expansionStateExpression = getValueExpression("expansionState");
        if (expansionStateExpression != null)
            expansionStateExpression.setValue(context.getELContext(), expansionState);
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        beforeRenderResponse(context);

        ValueExpression expansionStateExpression = getValueExpression("expansionState");
        if (expansionStateExpression != null) {
            ExpansionState newExpansionState = (ExpansionState) expansionStateExpression.getValue(context.getELContext());
            if (newExpansionState != null)
                expansionState = newExpansionState;
        }
        updateSortingFromBindings();
        prepareModelFromTreeStructure(true);

        super.encodeBegin(context);
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

    public int loadSubNodes(int rowIndex) {
        if (!isRowAvailableAfterRestoring(rowIndex))
            return 0;
        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);

        if (!nodeInfo.getNodeHasChildren())
            throw new IllegalArgumentException("The node at this rowIndex doesn't have children: " + rowIndex);
        if (nodeInfo.getChildrenPreloaded())
            throw new IllegalArgumentException("The node at this rowIndex already has its children preloaded: " + rowIndex);
        TreeStructure treeStructure = findTreeStructureChild();
        TreePath nodeKeyPath = nodeInfo.getNodeKeyPath();
        TreePath nodePath = nodeInfo.getNodePath();
        TreePath nodeIndexPath = nodeInfo.getNodeIndexPath();
        if (!locateNodeChildrenByKeyPath(treeStructure, nodeKeyPath))
            return 0; // the node has probably been removed (can be the case here in server-side state saving)

        List<NodeInfoForRow> nodeInfoForRows = new ArrayList<NodeInfoForRow>();
        List<NodeInfoForRow> nodeInfoForRows_unfiltered = new ArrayList<NodeInfoForRow>();
        int sortLevel = getSortLevel();
        List<Filter> filters = getActiveFilters();
        deepestLevel = 0;
        int childNodeCount = collectTreeNodeDatas(treeStructure, filters, nodeInfoForRows, null, nodeInfoForRows_unfiltered,
                nodeKeyPath.getLevel() + 1, sortLevel, nodePath, nodeKeyPath, nodeIndexPath, true);
        nodeInfo.setChildNodeCount(childNodeCount);
        nodeInfo.setChildrenPreloaded(true);
        this.nodeInfoForRows.addAll(rowIndex + 1, nodeInfoForRows);
        this.nodeInfoForRows_unfiltered.addAll(nodeInfoForRows_unfiltered);


        if (isRendered()) {
            Map<Object, NodeInfoForRow> newRowIndexToExpansionData = new HashMap<Object, NodeInfoForRow>();

            int addedRowCount = nodeInfoForRows.size();
            List<Object> addedRowDatas = new ArrayList<Object>(addedRowCount);
            List<TreePath> addedRowKeys = new ArrayList<TreePath>(addedRowCount);
            for (int i = 0; i < addedRowCount; i++) {
                NodeInfoForRow ni = nodeInfoForRows.get(i);
                addedRowDatas.add(ni.getNodeData());
                addedRowKeys.add(ni.getNodeKeyPath());
                newRowIndexToExpansionData.put(rowIndex + 1 + i, ni);
            }

            getModel().addRows(rowIndex + 1, addedRowDatas, addedRowKeys);

            for (Map.Entry<Object, NodeInfoForRow> entry : rowIndexToExpansionData.entrySet()) {
                Object keyObj = entry.getKey();
                if (keyObj instanceof String) {
                    newRowIndexToExpansionData.put(keyObj, entry.getValue());
                    continue;
                }
                Integer i = (Integer) keyObj;
                if (i > rowIndex)
                    i = i + addedRowCount;
                newRowIndexToExpansionData.put(i, entry.getValue());
            }

            rowIndexToExpansionData = newRowIndexToExpansionData;
        }
        return nodeInfoForRows.size();
    }

    private boolean locateNodeChildrenByKeyPath(TreeStructure treeStructure, TreePath nodeKeyPath) {
        List<Object> nodesFromTopToBottom = new LinkedList<Object>();
        do {
            nodesFromTopToBottom.add(0, nodeKeyPath.getValue());
            nodeKeyPath = nodeKeyPath.getParentPath();
        } while (nodeKeyPath != null);

        treeStructure.goToTopLevel();
        while (nodesFromTopToBottom.size() > 0) {
            Object nodeKey = nodesFromTopToBottom.remove(0);
            int nodeCount = treeStructure.getNodeCount();
            int nodeIndex = 0;
            for (; nodeIndex < nodeCount; nodeIndex++) {
                treeStructure.setNodeIndex(nodeIndex);
                Object currentNodeKey = treeStructure.getNodeKey();
                if (nodeKey.equals(currentNodeKey))
                    break;
            }
            if (nodeIndex == nodeCount)
                return false;
            treeStructure.goToChildLevel();
        }
        return true;
    }

    private void prepareModelFromTreeStructure(boolean readActualData) {
        TreeStructure treeStructure = findTreeStructureChild();
        boolean proceedWithReadingData = treeStructure != null && isRendered() && readActualData;
        if (proceedWithReadingData)
            treeStructure.goToTopLevel();

        List<NodeInfoForRow> nodeInfoForRows = new ArrayList<NodeInfoForRow>();
        List<NodeInfoForRow> nodeInfoForRows_unfiltered = new ArrayList<NodeInfoForRow>();
        int sortLevel = getSortLevel();
        List<Filter> filters = getActiveFilters();
        deepestLevel = 0;
        rowValuesForFilteringNeeded = null;
        int rootNodeCount = proceedWithReadingData
                ? collectTreeNodeDatas(treeStructure, filters, nodeInfoForRows, null, nodeInfoForRows_unfiltered, 0, sortLevel, null, null, null, true)
                : 0;

        this.nodeInfoForRows = nodeInfoForRows;
        this.nodeInfoForRows_unfiltered = nodeInfoForRows_unfiltered;
        collectedFilters = filters;

        updateModelRowDatasAndExpansionData(nodeInfoForRows, rootNodeCount);
    }

    private void updateModelRowDatasAndExpansionData(List<NodeInfoForRow> nodeInfoForRows, int rootNodeCount) {
        updateModel(nodeInfoForRows);
        if (!isRendered())
            return;

        Map<Object, NodeInfoForRow> rowIndexToExpansionData = new HashMap<Object, NodeInfoForRow>();
        if (rootNodeCount != -1) {
            NodeInfoForRow nodeInfoForRow = new NodeInfoForRow(null, null, null, -1, true, false);
            nodeInfoForRow.setNodeHasChildren(rootNodeCount > 0);
            nodeInfoForRow.setChildrenPreloaded(true);
            nodeInfoForRow.setChildNodeCount(rootNodeCount);
            rowIndexToExpansionData.put("root", nodeInfoForRow);
        }
        for (int rowIndex = 0; rowIndex < nodeInfoForRows.size(); rowIndex++) {
            NodeInfoForRow nodeInfoForRow = nodeInfoForRows.get(rowIndex);
            if (nodeInfoForRow.getNodeHasChildren())
                rowIndexToExpansionData.put(rowIndex, nodeInfoForRow);
        }
        this.rowIndexToExpansionData = rowIndexToExpansionData;
    }

    private void updateModel(List<NodeInfoForRow> nodeInfoForRows) {
        List<Object> rowDatas = new ArrayList<Object>(nodeInfoForRows.size());
        List<TreePath> rowKeys = new ArrayList<TreePath>(nodeInfoForRows.size());
        for (NodeInfoForRow nodeInfo : nodeInfoForRows) {
            rowDatas.add(nodeInfo != null ? nodeInfo.getNodeData() : null);
            rowKeys.add(nodeInfo != null ? nodeInfo.getNodeKeyPath() : null);
        }

        getModel().setWrappedData(rowDatas, rowKeys);
    }

    public Map<Object, NodeInfoForRow> getNodeExpansionDataMap(FacesContext context) {
        return rowIndexToExpansionData;
    }

    private int collectTreeNodeDatas(TreeStructure treeStructure, List<Filter> activeFilters, // todo: too much parameters. review this method
                                     List<NodeInfoForRow> nodeInfosForRendering,
                                     List<NodeInfoForRow> nodeInfosAllFiltered,
                                     List<NodeInfoForRow> nodeInfos_unfiltered,
                                     int level, int sortLevel,
                                     TreePath parentNodePath,
                                     TreePath parentNodeKeyPath,
                                     TreePath parentNodeIndexPath,
                                     boolean thisLevelInitiallyVisible) {
        if (level > deepestLevel)
            deepestLevel = level;

        List<TempNodeParams> thisLevelNodeParams = new ArrayList<TempNodeParams>();
        PreloadedNodes preloadedNodes = getPreloadedNodes();
        for (int nodeIndex = 0, nodeCount = treeStructure.getNodeCount(); nodeIndex < nodeCount; nodeIndex++) {
            treeStructure.setNodeIndex(nodeIndex);
            if (!treeStructure.isNodeAvailable())
                break;
            Object nodeData = treeStructure.getNodeData();
            Object nodeKey = treeStructure.getNodeKey();
            boolean nodeHasChildren = treeStructure.getNodeHasChildren();
            TreePath nodePath = new TreePath(nodeData, parentNodePath);
            TreePath nodeKeyPath = new TreePath(nodeKey, parentNodeKeyPath);
            TreePath nodeIndexPath = new TreePath(nodeIndex, parentNodeIndexPath);
            boolean nodeExpanded = isNodeExpanded(nodeKeyPath);
            NodeInfoForRow nodeInfo = new NodeInfoForRow(nodePath, nodeKeyPath, nodeIndexPath, level, nodeExpanded, thisLevelInitiallyVisible);
            boolean preloadChildren = nodeHasChildren && (
                    nodeExpanded || (preloadedNodes != null && preloadedNodes.getPreloadChildren(nodeKeyPath))
            );
            TempNodeParams thisNodeParams = new TempNodeParams(nodeData, nodeInfo, nodeExpanded, preloadChildren);
            boolean[] flagsArray = new boolean[activeFilters.size()];
            boolean rowAccepted = TableDataModel.filterRow(activeFilters, thisNodeParams, flagsArray);
            thisNodeParams.setRowAccepted(rowAccepted);
            thisNodeParams.setFilteringFlags(flagsArray);

            boolean lookForFilteredSubrows = activeFilters.size() > 0 && !rowAccepted; // JSFC-1910 -- when some filters are active we shouldn't extract invisible nodes if parent node satisfies filtering criteria

            if (isRowValuesForFilteringNeeded() || lookForFilteredSubrows || preloadChildren) {
                treeStructure.goToChildLevel();
                List<NodeInfoForRow> subtreeNodeInfosForRendering = new ArrayList<NodeInfoForRow>();
                List<NodeInfoForRow> subtreeNodeInfosAllFiltered = new ArrayList<NodeInfoForRow>();
                List<NodeInfoForRow> subtreeNodeInfos_unfiltered = new ArrayList<NodeInfoForRow>();
                int filteredChildCount = collectTreeNodeDatas(
                        treeStructure, activeFilters, subtreeNodeInfosForRendering, subtreeNodeInfosAllFiltered, subtreeNodeInfos_unfiltered,
                        level + 1, sortLevel, nodePath, nodeKeyPath, nodeIndexPath, thisLevelInitiallyVisible && nodeExpanded);
                nodeInfo.setNodeHasChildren(filteredChildCount > 0);
                nodeInfo.setChildrenPreloaded(preloadChildren);
                nodeInfo.setChildNodeCount(filteredChildCount);
                thisNodeParams.setSubtreeNodeInfosForRendering(subtreeNodeInfosForRendering);
                thisNodeParams.setSubtreeNodeInfosAllFiltered(subtreeNodeInfosAllFiltered);
                thisNodeParams.setSubtreeNodeInfos_unfiltered(subtreeNodeInfos_unfiltered);
                treeStructure.goToParentLevel();
            } else {
                nodeInfo.setNodeHasChildren(nodeHasChildren);
                nodeInfo.setChildrenPreloaded(false);
                nodeInfo.setChildNodeCount(0);
            }

            thisLevelNodeParams.add(thisNodeParams);
        }
        if (sortLevel == -1 || level == sortLevel)
            sortNodes(thisLevelNodeParams, level);

        List<boolean[]> thisLevelNodeFilteringFlags = new ArrayList<boolean[]>();
        List<TempNodeParams> filteredNodeParams = new ArrayList<TempNodeParams>();
        int sortedRowCount = thisLevelNodeParams.size();
        for (int i = 0; i < sortedRowCount; i++) {
            TempNodeParams rowObj = thisLevelNodeParams.get(i);
            thisLevelNodeFilteringFlags.add(rowObj.getFilteringFlags());
            if (rowObj.isRowAccepted())
                filteredNodeParams.add(rowObj);
        }

        for (TempNodeParams nodeParams : filteredNodeParams) {
            NodeInfoForRow nodeInfoForRow = nodeParams.getNodeInfoForRow();
            nodeInfoForRow.setAcceptedByFilters(true);
        }

        int passedChildCount = 0;
        for (int i = 0; i < thisLevelNodeParams.size(); i++) {
            TempNodeParams nodeParams = thisLevelNodeParams.get(i);
            boolean[] nodeFilteringFlags = thisLevelNodeFilteringFlags.get(i);
            NodeInfoForRow nodeInfoForRow = nodeParams.getNodeInfoForRow();
            nodeInfoForRow.setNodeFilteringFlags(nodeFilteringFlags);
            List<NodeInfoForRow> subtreeNodeInfos_unfiltered = nodeParams.getSubtreeNodeInfos_unfiltered();
            nodeInfos_unfiltered.add(nodeInfoForRow);
            if (subtreeNodeInfos_unfiltered != null)
                nodeInfos_unfiltered.addAll(subtreeNodeInfos_unfiltered);

            List<NodeInfoForRow> subtreeNodeInfosForRendering = nodeParams.getSubtreeNodeInfosForRendering();
            List<NodeInfoForRow> subtreeNodeInfosAllFiltered = nodeParams.getSubtreeNodeInfosAllFiltered();
            if (nodeInfoForRow.isAcceptedByFilters() || (subtreeNodeInfosAllFiltered != null && subtreeNodeInfosAllFiltered.size() > 0)) {
                nodeInfosForRendering.add(nodeInfoForRow);
                if (nodeInfosAllFiltered != null)
                    nodeInfosAllFiltered.add(nodeInfoForRow);
                passedChildCount++;
                boolean preloadChildrenToClient = nodeParams.getPreloadChildrenToClient();
                if (preloadChildrenToClient)
                    nodeInfosForRendering.addAll(subtreeNodeInfosForRendering);
                if (nodeInfosAllFiltered != null && subtreeNodeInfosAllFiltered != null)
                    nodeInfosAllFiltered.addAll(subtreeNodeInfosAllFiltered);
            }

        }

        return passedChildCount;
    }

    private boolean isRowValuesForFilteringNeeded() {
        if (rowValuesForFilteringNeeded != null)
            return rowValuesForFilteringNeeded;

        List<Filter> filters = getFilters();
        boolean rowListNeeded = false;
        for (Filter filter : filters) {
            if (filter.getWantsRowList()) {
                rowListNeeded = true;
                break;
            }
        }
        rowValuesForFilteringNeeded = rowListNeeded;
        return rowListNeeded;
    }

    private void sortNodes(List<TempNodeParams> thisLevelArrays, int level) {
        List<SortingRule> sortingRules = getSortingRules();
        Comparator<Object> sortingComparator = createRowDataComparator(sortingRules);
        if (sortingComparator == null)
            return;
        String nodeLevelVar = getNodeLevelVar();
        if (nodeLevelVar != null) {
            Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
            requestMap.put(nodeLevelVar, level);
        }

        Collections.sort(thisLevelArrays, sortingComparator);
    }

    @Override
    protected RowComparator createRowComparator(
            FacesContext facesContext,
            ValueExpression sortingExpression,
            Comparator<Object> sortingComparator,
            Map<String, Object> requestMap,
            boolean sortAscending) {
        return new NodeComparator(facesContext, sortingExpression, sortingComparator, requestMap, sortAscending);
    }

    public void acceptNewExpandedRowIndexes(Set indexes) {
        FacesContext context = getFacesContext();
        boolean dontCollapseNodes = isReloadingThisComponentWithA4j() || TreeTableRenderer.isAjaxFoldingInProgress(context);
        List storedRowKeys = getModel().getStoredRowKeys();
        int rowCount = storedRowKeys.size();
        for (int i = 0; i < rowCount; i++) {
            TreePath keyPath = (TreePath) storedRowKeys.get(i);
            boolean expanded = isNodeExpanded(keyPath);
            boolean shouldBeExpanded = indexes.contains(Integer.valueOf(i));
            if (expanded == shouldBeExpanded)
                continue;
            if (!shouldBeExpanded && dontCollapseNodes)
                continue;
            if (!getNodeHasChildren()) { // rows without children should have expanded state by default
                setNodeExpanded(keyPath, true);
            } else {
                setNodeExpanded(keyPath, shouldBeExpanded);
            }
        }
    }

    private boolean isReloadingThisComponentWithA4j() {
        // needed for JSFC-2526. It doesn't seem possible to know the a4j rerendered components on the decoding stage,
        // so we suppose that this component is rerendered if it is an A4j request.
        return Environment.isAjax4jsfRequest();
    }

    public int getMaxLevel() {
        return deepestLevel;
    }


    public class NodeComparator extends RowComparator {
        private String nodePathVar;
        private String nodeHasChildrenVar;

        public NodeComparator(
                FacesContext facesContext,
                ValueExpression sortingExpression,
                Comparator<Object> sortingComparator,
                Map<String, Object> requestMap,
                boolean sortAscending) {
            super(facesContext, sortingExpression, sortingComparator, requestMap, sortAscending);
            nodePathVar = getNodePathVar();
            nodeHasChildrenVar = getNodeHasChildrenVar();
        }


        protected Runnable populateSortingExpressionParams(final Map<String, Object> requestMap, Object collectionObject) {
            TempNodeParams nodeParams = (TempNodeParams) collectionObject;
            Object nodeData = nodeParams.getNodeData();
            NodeInfoForRow nodeInfo = nodeParams.getNodeInfoForRow();

            requestMap.put(var, nodeData);
            final Object prevNodePathVarValue;
            if (nodePathVar != null)
                prevNodePathVarValue = requestMap.put(nodePathVar, nodeInfo.getNodePath());
            else
                prevNodePathVarValue = null;
            final Object prevNodeHasChildrenVarValue;
            if (nodeHasChildrenVar != null)
                prevNodeHasChildrenVarValue = requestMap.put(nodeHasChildrenVar, nodeInfo.getNodeHasChildren());
            else
                prevNodeHasChildrenVarValue = null;
            String nodeParamsKey = TempNodeParams.class.getName();
            final Object prevNodeParamsValue = requestMap.put(nodeParamsKey, nodeParams);
            return new Runnable() {
                public void run() {
                    if (nodePathVar != null)
                        requestMap.put(nodePathVar, prevNodePathVarValue);
                    if (nodeHasChildrenVar != null)
                        requestMap.put(nodeHasChildrenVar, prevNodeHasChildrenVarValue);
                    String nodeParamsKey = TempNodeParams.class.getName();
                    requestMap.put(nodeParamsKey, prevNodeParamsValue);
                }
            };
        }

    }

    @Override
    public void setRowIndex(int rowIndex) {
        super.setRowIndex(rowIndex);
        boolean rowAvailable = isRowAvailable();
        Map<String, Object> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        if (nodePathVar != null)
            requestMap.put(nodePathVar, rowAvailable ? getNodePath() : null);
        if (nodeLevelVar != null)
            requestMap.put(nodeLevelVar, rowAvailable ? getNodeLevel() : null);
        if (nodeHasChildrenVar != null)
            requestMap.put(nodeHasChildrenVar, rowAvailable ? getNodeHasChildren() : null);
    }


    public Object getNodeKey() {
        if (nodeInfoForRows == null)
            return null;
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
            return null;
        if (!isRowAvailableAfterRestoring(rowIndex))
            return null;
        return getNodeKey(rowIndex);
    }

    public TreePath getNodeKeyPath() {
        if (nodeInfoForRows == null)
            return null;
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
            return null;
        if (!isRowAvailableAfterRestoring(rowIndex))
            return null;
        return getNodeKeyPath(rowIndex);
    }

    public Object getNodeKey(int rowIndex) {
        if (!isRowAvailableAfterRestoring(rowIndex))
            return null;
        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.getNodeKey();
    }

    public Object getNodeData(int rowIndex) {
        if (!isRowAvailableAfterRestoring(rowIndex))
            return null;
        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.getNodeData();
    }

    public int getNodeLevel() {
        if (nodeInfoForRows == null)
            return 0;
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
            return 0;
        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.getNodeLevel();
    }

    public TreePath getNodePath() {
        if (nodeInfoForRows == null)
            return null;
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
            return null;
        return getNodePath(rowIndex);
    }

    public TreePath getNodePath(int rowIndex) {
        if (!isRowAvailableAfterRestoring(rowIndex))
            return null;
        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.getNodePath();
    }

    public TreePath getNodeKeyPath(int rowIndex) {
        if (!isRowAvailableAfterRestoring(rowIndex))
            return null;
        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.getNodeKeyPath();
    }

    public boolean getNodeHasChildren() {
        if (nodeInfoForRows == null)
            return false;
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
            return false;
        if (!isRowAvailableAfterRestoring(rowIndex))
            return false;
        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.getNodeHasChildren();
    }

    public boolean isNodeExpanded() {
        if (nodeInfoForRows == null)
            return false;
        int rowIndex = getRowIndex();

        if (rowIndex == -1)
            return false;
        if (!isRowAvailableAfterRestoring(rowIndex))
            return false;

        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.isExpanded();
    }

    public boolean isNodeInitiallyVisible() {
        if (nodeInfoForRows == null)
            return false;
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
            return false;
        if (!isRowAvailableAfterRestoring(rowIndex))
            return false;

        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.isNodeInitiallyVisible();
    }

    public boolean isNodeAcceptedByFilters() {
        if (nodeInfoForRows == null)
            return false;
        int rowIndex = getRowIndex();
        if (rowIndex == -1)
            return false;
        if (!isRowAvailableAfterRestoring(rowIndex))
            return false;

        NodeInfoForRow nodeInfo = nodeInfoForRows.get(rowIndex);
        return nodeInfo.isAcceptedByFilters();
    }

    public boolean isFilteringPerformed() {
        if (collectedFilters == null || collectedFilters.size() == 0)
            return false;
        for (Filter filter : collectedFilters) {
            if (!filter.isAcceptingAllRecords())
                return true;
        }
        return false;
    }

    public boolean isNodeExpanded(TreePath keyPath) {
        if (keyPath == null)
            return false;
        return expansionState.isNodeExpanded(keyPath);
    }

    public void setNodeExpanded(TreePath keyPath, boolean expanded) {
        boolean oldExpansion = isNodeExpanded(keyPath);
        if (expanded == oldExpansion)
            return;
        expansionState = expansionState.getMutableExpansionState();
        expansionState.setNodeExpanded(keyPath, expanded);
    }

    public List getRowListForFiltering(Filter filter) {
        List<boolean[]> allRowFilteringFlags = new ArrayList<boolean[]>();
        for (NodeInfoForRow info : nodeInfoForRows_unfiltered) {
            allRowFilteringFlags.add(info.getNodeFilteringFlags());
        }
        List<Object> result = TableDataModel.getRowListForFiltering(filter, collectedFilters, nodeInfoForRows_unfiltered, allRowFilteringFlags);
        return result;
    }

    protected Object setupDataRetrievalContext(Object data, Map<String, Object> requestMap, String var) {
        NodeInfoForRow nodeInfo = (data instanceof NodeInfoForRow) ? (NodeInfoForRow) data : ((TempNodeParams) data).getNodeInfoForRow();
        requestMap.get(var);
        Object nodeData = nodeInfo.getNodeData();
        requestMap.put(var, nodeData);

        String nodeLevelVar = getNodeLevelVar();
        if (nodeLevelVar != null)
            requestMap.put(nodeLevelVar, nodeInfo.getNodeLevel());

        String nodePathVar = getNodePathVar();
        if (nodePathVar != null)
            requestMap.put(nodePathVar, nodeInfo.getNodePath());

        String nodeHasChildrenVar = getNodeHasChildrenVar();
        if (nodeHasChildrenVar != null)
            requestMap.put(nodeHasChildrenVar, nodeInfo.getNodeHasChildren());
        return nodeData;
    }

    public boolean isDataSourceEmpty() {
        if (nodeInfoForRows_unfiltered == null)
            return true;
        int originalRowCount = nodeInfoForRows_unfiltered.size();
        return originalRowCount == 0;
    }


    protected boolean isRowInColumnSelected(BaseColumn column, Map<String, Object> requestMap, String var) {
        String nodeParamsKey = TempNodeParams.class.getName();
        TempNodeParams params = (TempNodeParams) requestMap.get(nodeParamsKey);
        TreePath nodeKeyPath = params.getNodeInfoForRow().getNodeKeyPath();
        List selectedNodePaths = getSelectedNodePaths(column);
        return selectedNodePaths.contains(nodeKeyPath);
    }

    private List getSelectedNodePaths(BaseColumn column) {
        if (column instanceof SelectionColumn) {
            TreeTableSelection selection = (TreeTableSelection) getSelection();
            return selection.getSelectedNodeKeyPaths();
        } else if (column instanceof CheckboxColumn) {
            return ((CheckboxColumn) column).getSelectedRowKeys();
        } else
            throw new IllegalArgumentException("Unkown column type: " + (column != null ? column.getClass().getName() : "null"));
    }

}
