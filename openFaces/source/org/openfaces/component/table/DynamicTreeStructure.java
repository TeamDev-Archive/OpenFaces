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

import org.openfaces.component.table.impl.TableDataModel;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Dmitry Pikhulya
 */
public class DynamicTreeStructure extends TreeStructure {
    public static final String COMPONENT_TYPE = "org.openfaces.DynamicTreeStructure";
    public static final String COMPONENT_FAMILY = "org.openfaces.DynamicTreeStructure";

    private int level;
    private Stack<Object> currentNodeParentPath = new Stack<Object>();
    private Stack<Object> currentNodeParentKeysPath = new Stack<Object>();
    private Stack<Object> parentLevelNodeDatas = new Stack<Object>();
    private Stack<Integer> parentLevelNodeIndexes = new Stack<Integer>();
    private List<Object> currentLevelNodeDatas;
    private TreePath parentNodePath;

    private int currentNodeIndex;
    private Object currentNodeData;
    private TreePath currentNodePath;
    private boolean currentNodeHasChildren;
    private Object currentNodeKey;

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public ValueExpression getNodeChildrenExpression() {
        return getValueExpression("nodeChildren");
    }

    public ValueExpression getNodeHasChildrenExpression() {
        return getValueExpression("nodeHasChildren");
    }

    public ValueExpression getNodeKeyExpression() {
        return getValueExpression("nodeKey");
    }

    private List<Object> getNodeChildren(Object nodeData, TreePath nodePath, int nodeLevel) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        setRequestVariables(facesContext, nodeData, nodePath, nodeLevel);
        Object result = getNodeChildrenExpression().getValue(facesContext.getELContext());
        return getAsList(result);
    }

    private void setRequestVariables(FacesContext facesContext, Object nodeData, TreePath nodePath, int nodeLevel) {
        TreeTable treeTable = ((TreeTable) getParent());
        Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
        String var = treeTable.getVar();
        String nodeLevelVar = treeTable.getNodeLevelVar();
        String nodePathVar = treeTable.getNodePathVar();

        requestMap.put(var, nodeData);
        if (nodeLevelVar != null)
            requestMap.put(nodeLevelVar, nodeLevel);
        if (nodePathVar != null)
            requestMap.put(nodePathVar, nodePath);
    }

    @SuppressWarnings("unchecked")
    private List<Object> getAsList(Object result) {
        if (result instanceof List)
            return (List) result;
        else if (result instanceof Collection)
            return new ArrayList<Object>((Collection<Object>) result);
        else if (result == null)
            return Collections.emptyList();
        else if (result.getClass().isArray())
            return Arrays.asList((Object[]) result);
        else
            throw new RuntimeException("Invalid type returned by nodeChildren binding. Expected Collection but was: " + result.getClass());
    }

    private Object getNodeKey(Object nodeData, TreePath nodePath, int nodeLevel) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        setRequestVariables(facesContext, nodeData, nodePath, nodeLevel);
        ValueExpression nodeKeyExpression = getNodeKeyExpression();
        if (nodeKeyExpression == null) {
            if (!TableDataModel.isValidRowKey(nodeData))
                throw new RuntimeException("Invalid collection entry returned from nodeChildren binding \"" + getNodeChildrenExpression().getExpressionString() + "\" of dynamicTreeStructure in a TreeTable with client id \"" + getParent().getClientId(facesContext) + "\"\n" +
                        "    It must return a value that implements java.io.Serializable interface and correctly implements the equals and hashCode methods for serialized instances. \n" +
                        "    An instance of the following class that doesn't satisfy these rules has been returned: " + nodeData.getClass().getName() + ", for this node data: " + nodeData + "\n" +
                        "    Alternatively if node data can't meet these requirements you can define the nodeKey binding. See the TreeTable documentation for details.");
            return nodeData;
        }

        Object result = nodeKeyExpression.getValue(facesContext.getELContext());
        if (!TableDataModel.isValidRowKey(result))
            throw new RuntimeException("Invalid value returned from nodeKey binding \"" + nodeKeyExpression.getExpressionString() + "\" of dynamicTreeStructure in a TreeTable with client id \"" + getParent().getClientId(facesContext) + "\"\n" +
                    "    It must return a value that implements java.io.Serializable interface and correctly implements the equals and hashCode methods for serialized instances. \n" +
                    "    An instance of the following class that doesn't satisfy these rules has been returned: " + result.getClass().getName() + ", for this node data: " + nodeData + "\n" +
                    "    See the TreeTable documentation to learn how to declare dynamicTreeStructure.");

        if (result == null)
            return nodeData;
        return result;
    }

    private boolean getNodeHasChildren(Object nodeData, TreePath nodePath, int nodeLevel) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        setRequestVariables(facesContext, nodeData, nodePath, nodeLevel);
        ValueExpression nodeHasChildrenExpression = getNodeHasChildrenExpression();
        if (nodeHasChildrenExpression == null) {
            List nodeChildren = getNodeChildren(nodeData, nodePath, nodeLevel);
            return nodeChildren.size() > 0;
        }
        Boolean result = (Boolean) nodeHasChildrenExpression.getValue(facesContext.getELContext());
        return result;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void goToTopLevel() {
        level = 0;
        currentNodeParentPath.clear();
        parentLevelNodeDatas.clear();
        parentLevelNodeIndexes.clear();
        currentLevelNodeDatas = getNodeChildren(null, null, -1);
        if (currentLevelNodeDatas == null)
            currentLevelNodeDatas = Collections.emptyList();
        parentNodePath = null;
        currentNodeIndex = -1;
        setNodeIndex(0);
    }

    @Override
    public boolean isEnteringInfiniteRecursion() {
        return currentNodeParentKeysPath.contains(currentNodeKey);
    }

    @Override
    public void goToChildLevel() {
        if (!isNodeAvailable())
            throw new IllegalArgumentException("No node is available at the current index: " + currentNodeIndex);
        currentNodeParentPath.push(currentNodeData);
        currentNodeParentKeysPath.push(currentNodeKey);
        parentLevelNodeDatas.push(currentLevelNodeDatas);
        parentLevelNodeIndexes.push(currentNodeIndex);
        currentLevelNodeDatas = getNodeChildren(currentNodeData, currentNodePath, level);
        if (currentLevelNodeDatas == null)
            currentLevelNodeDatas = Collections.emptyList();
        parentNodePath = currentNodePath;

        level++;
        currentNodeIndex = -1;
        setNodeIndex(0);
    }

    public void goToParentLevel() {
        if (level == 0)
            throw new IllegalArgumentException("Can't go to parent level from top-level nodes");
        currentLevelNodeDatas = (List<Object>) parentLevelNodeDatas.pop();
        currentNodeData = currentNodeParentPath.pop();
        currentNodePath = parentNodePath;
        currentNodeKey = currentNodeParentKeysPath.pop();
        currentNodeIndex = parentLevelNodeIndexes.pop();
        currentNodeHasChildren = false;
        level--;
    }

    @Override
    public int getNodeCount() {
        if (currentLevelNodeDatas == null)
            return 0;
        return currentLevelNodeDatas.size();
    }

    @Override
    public void setNodeIndex(int nodeIndex) {
        if (nodeIndex < 0)
            throw new IllegalArgumentException("nodeIndex shouldn't be less than zero: " + nodeIndex);
        if (currentNodeIndex == nodeIndex)
            return;
        currentNodeIndex = nodeIndex;
        if (currentLevelNodeDatas != null && nodeIndex < currentLevelNodeDatas.size()) {
            currentNodeData = currentLevelNodeDatas.get(nodeIndex);
            currentNodePath = new TreePath(currentNodeData, parentNodePath);
            currentNodeKey = getNodeKey(currentNodeData, currentNodePath, level);
            currentNodeHasChildren = getNodeHasChildren(currentNodeData, currentNodePath, level);
        } else {
            currentNodeData = null;
            currentNodePath = null;
            currentNodeKey = null;
            currentNodeHasChildren = false;
        }
    }

    @Override
    public int getNodeIndex() {
        return currentNodeIndex;
    }

    @Override
    public boolean isNodeAvailable() {
        return currentNodeData != null;
    }

    @Override
    public Object getNodeData() {
        if (!isNodeAvailable())
            throw new IllegalArgumentException("No node is available at the current index: " + currentNodeIndex);
        return currentNodeData;
    }

    @Override
    public Object getNodeKey() {
        if (!isNodeAvailable())
            throw new IllegalArgumentException("No node is available at the current index: " + currentNodeIndex);
        return currentNodeKey != null ? currentNodeKey : currentNodeData;
    }

    @Override
    public boolean getNodeHasChildren() {
        if (!isNodeAvailable())
            throw new IllegalArgumentException("No node is available at the current index: " + currentNodeIndex);
        return currentNodeHasChildren;
    }
}
