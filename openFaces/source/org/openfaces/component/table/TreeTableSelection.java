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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TreeTableSelection extends AbstractTableSelection {
    protected TreeTable getTreeTable() {
        return (TreeTable) getTable();
    }

    @Override
    public void setParent(UIComponent parent) {
        if (parent != null && !(parent instanceof TreeTable))
            throw new FacesException(getClass().getName() + " component can only be placed in a TreeTable component, but it was placed into a component with class: " + parent.getClass().getName() + "; if you need to add selection support to a DataTable use SingleRowSelection or MultipleRowSelection component instead.");
        super.setParent(parent);
    }


    protected int getRowIndexByNodeKey(Object nodeKey) {
        TreeTable treeTable = getTreeTable();
        int rowCount = treeTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object currentKey = treeTable.getNodeKey(i);
            if (currentKey != null && currentKey.equals(nodeKey))
                return i;
        }
        return -1;
    }

    protected int getRowIndexByNodeKeyPath(TreePath nodeKeyPath) {
        TreeTable treeTable = getTreeTable();
        int rowCount = treeTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            TreePath currentKeyPath = treeTable.getNodeKeyPath(i);
            if (currentKeyPath.equals(nodeKeyPath))
                return i;
        }
        return -1;
    }


    protected int getRowIndexForPath(TreePath nodePath) {
        TreeTable treeTable = getTreeTable();
        int rowCount = treeTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            TreePath currentPath = treeTable.getNodePath(i);
            if (currentPath.equals(nodePath))
                return i;
        }
        return -1;
    }

    protected Object getNodeKeyByData(Object nodeData) {
        TreePath keyPathByData = getKeyPathByData(nodeData);
        return (keyPathByData != null) ? keyPathByData.getValue() : null;
    }

    protected TreePath getNodePathByData(Object nodeData) {
        TreePath[] paths = new TreePath[2];
        getKeyPathByData(nodeData, paths);
        return paths[0];
    }

    protected TreePath getKeyPathByData(Object nodeData) {
        TreePath[] paths = new TreePath[2];
        getKeyPathByData(nodeData, paths);
        return paths[1];
    }

    protected void getKeyPathByData(Object nodeData, TreePath[] outputPaths) {
        TreeTable treeTable = getTreeTable();
        int rowCount = treeTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object currentData = treeTable.getNodeData(i);
            if ((currentData == null && nodeData == null) || (currentData != null && currentData.equals(nodeData))) {
                TreePath nodePath = treeTable.getNodePath(i);
                TreePath keyPath = treeTable.getNodeKeyPath(i);
                outputPaths[0] = nodePath;
                outputPaths[1] = keyPath;
                return;
            }
        }

        TreeStructure treeStructure = treeTable.getTreeStructure();
        treeStructure.goToTopLevel();
        findKeyPathByData(treeStructure, nodeData, null, null, outputPaths);
    }

    private void findKeyPathByData(TreeStructure treeStructure, Object nodeData,
                                   TreePath parentPath, TreePath parentKeyPath, TreePath[] outputPaths) {
        int nodeCount = treeStructure.getNodeCount();
        for (int i = 0; i < nodeCount; i++) {
            treeStructure.setNodeIndex(i);
            if (!treeStructure.isNodeAvailable())
                break;
            Object currentNodeData = treeStructure.getNodeData();
            TreePath currentNodePath = new TreePath(currentNodeData, parentPath);
            TreePath currentKeyPath = new TreePath(treeStructure.getNodeKey(), parentKeyPath);
            if ((currentNodeData == null && nodeData == null) || (currentNodeData != null && currentNodeData.equals(nodeData))) {
                outputPaths[0] = currentNodePath;
                outputPaths[1] = currentKeyPath;
                return;
            }
            if (treeStructure.getNodeHasChildren()) {
                treeStructure.goToChildLevel();
                findKeyPathByData(treeStructure, nodeData, currentNodePath, currentKeyPath, outputPaths);
                if (outputPaths[0] != null)
                    return;
                treeStructure.goToParentLevel();
            }
        }
    }

    protected Object getNodeDataByKey(Object nodeKey) {
        TreePath nodePathByKey = getNodePathByKey(nodeKey);
        return nodePathByKey != null ? nodePathByKey.getValue() : null;
    }

    protected TreePath getNodePathByKey(Object nodeKey) {
        TreePath[] paths = new TreePath[2];
        getNodePathByKey(nodeKey, paths);
        return paths[0];
    }

    protected TreePath getKeyPathByKey(Object nodeKey) {
        TreePath[] paths = new TreePath[2];
        getNodePathByKey(nodeKey, paths);
        return paths[1];
    }

    private void getNodePathByKey(Object nodeKey, TreePath[] outputPaths) {
        TreeTable treeTable = getTreeTable();
        int rowCount = treeTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object currentKey = treeTable.getNodeKey(i);
            if ((currentKey == null && nodeKey == null) || (currentKey != null && currentKey.equals(nodeKey))) {
                TreePath nodePath = treeTable.getNodePath(i);
                TreePath keyPath = treeTable.getNodeKeyPath(i);
                outputPaths[0] = nodePath;
                outputPaths[1] = keyPath;
            }
        }
        TreeStructure treeStructure = treeTable.getTreeStructure();
        treeStructure.goToTopLevel();
        findNodePathByKey(treeStructure, nodeKey, null, null, outputPaths);
    }

    private void findNodePathByKey(TreeStructure treeStructure, Object nodeKey,
                                   TreePath parentPath, TreePath parentKeyPath, TreePath[] outputPaths) {
        int nodeCount = treeStructure.getNodeCount();
        for (int i = 0; i < nodeCount; i++) {
            treeStructure.setNodeIndex(i);
            if (!treeStructure.isNodeAvailable())
                break;
            Object currentNodeKey = treeStructure.getNodeKey();
            TreePath nodePath = new TreePath(treeStructure.getNodeData(), parentPath);
            TreePath nodeKeyPath = new TreePath(treeStructure.getNodeKey(), parentKeyPath);
            if ((currentNodeKey == null && nodeKey == null) || (currentNodeKey != null && currentNodeKey.equals(nodeKey))) {
                outputPaths[0] = nodePath;
                outputPaths[1] = nodeKeyPath;
                return;
            }
            if (treeStructure.getNodeHasChildren()) {
                treeStructure.goToChildLevel();
                findNodePathByKey(treeStructure, nodeKey, nodePath, nodeKeyPath, outputPaths);
                if (outputPaths[0] != null)
                    return;
                treeStructure.goToParentLevel();
            }
        }
    }

    protected TreePath getNodePathByKeyPath(TreePath keyPath) {
        TreeTable treeTable = getTreeTable();
        int rowCount = treeTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            TreePath currentKeyPath = treeTable.getNodeKeyPath(i);
            if (currentKeyPath.equals(keyPath))
                return treeTable.getNodePath(i);
        }

        List<Object> path = new ArrayList<Object>();
        for (TreePath p = keyPath; p != null; p = p.getParentPath())
            path.add(0, p.getValue());

        TreeStructure treeStructure = treeTable.getTreeStructure();
        treeStructure.goToTopLevel();
        return nodePathByKeyPath(treeStructure, path, null);
    }

    private TreePath nodePathByKeyPath(TreeStructure treeStructure, List<Object> path, TreePath parentNodePath) {
        while (true) {
            Object lookingForKey = path.remove(0);
            int i = 0;
            int count = treeStructure.getNodeCount();
            for (; i < count; i++) {
                treeStructure.setNodeIndex(i);
                Object currentKey = treeStructure.getNodeKey();
                if ((currentKey == null && lookingForKey == null) || (currentKey != null && currentKey.equals(lookingForKey))) {
                    break;
                }
            }
            if (i == count) {
                return null;
            }

            TreePath foundNodePath = new TreePath(treeStructure.getNodeData(), parentNodePath);
            if (path.isEmpty()) {
                return foundNodePath;
            }

            if (!treeStructure.getNodeHasChildren()) {
                return null;
            }

            treeStructure.goToChildLevel();
            parentNodePath = foundNodePath;
        }
    }

    protected TreePath getKeyPathByNodePath(TreePath nodePath) {
        TreeTable treeTable = getTreeTable();
        int rowCount = treeTable.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            TreePath currentNodePath = treeTable.getNodePath(i);
            if (currentNodePath.equals(nodePath))
                return treeTable.getNodeKeyPath(i);
        }

        List<Object> path = new ArrayList<Object>();
        for (TreePath p = nodePath; p != null; p = p.getParentPath())
            path.add(0, p.getValue());

        TreeStructure treeStructure = treeTable.getTreeStructure();
        treeStructure.goToTopLevel();
        return nodeKeyPathByPath(treeStructure, path, null);
    }

    private TreePath nodeKeyPathByPath(TreeStructure treeStructure, List<Object> path, TreePath parentKeyPath) {
        Object lookingForNode = path.remove(0);
        int i = 0;
        int count = treeStructure.getNodeCount();
        for (; i < count; i++) {
            treeStructure.setNodeIndex(i);
            Object currentNode = treeStructure.getNodeData();
            if ((currentNode == null && lookingForNode == null) || (currentNode != null && currentNode.equals(lookingForNode)))
                break;
        }
        if (i == count)
            return null;

        TreePath foundKeyPath = new TreePath(treeStructure.getNodeKey(), parentKeyPath);
        if (path.size() == 0)
            return foundKeyPath;

        if (!treeStructure.getNodeHasChildren())
            return null;

        treeStructure.goToChildLevel();
        return nodePathByKeyPath(treeStructure, path, foundKeyPath);
    }

    protected Object getNodeKeyByRowIndex(int index) {
        TreeTable treeTable = getTreeTable();
        Object nodeKey = treeTable.getNodeKey(index);
        return nodeKey;
    }

    protected TreePath getNodeKeyPathByRowIndex(int index) {
        TreeTable treeTable = getTreeTable();
        TreePath result = treeTable.getNodeKeyPath(index);
        return result;
    }


    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public abstract List<TreePath> getSelectedNodeKeyPaths();
}
