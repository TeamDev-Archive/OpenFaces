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

/**
 * @author Dmitry Pikhulya
 */
public class NodeInfoForRow {
    private TreePath nodePath;
    private TreePath nodeKeyPath;
    private TreePath nodeIndexPath;
    private int nodeLevel;
    private int childNodeCount;
    private boolean expanded;
    private boolean nodeInitiallyVisible;
    private boolean[] nodeFilteringFlags;
    private boolean acceptedByFilters;
    private boolean nodeHasChildren;
    private boolean childrenPreloaded;

    public NodeInfoForRow(TreePath nodePath, TreePath nodeKeyPath, TreePath nodeIndexPath, int nodeLevel, boolean expanded, boolean nodeInitiallyVisible) {
        this.nodeLevel = nodeLevel;
        this.nodePath = nodePath;
        this.nodeIndexPath = nodeIndexPath;
        this.nodeKeyPath = nodeKeyPath;
        this.expanded = expanded;
        this.nodeInitiallyVisible = nodeInitiallyVisible;
    }

    public int getChildNodeCount() {
        return childNodeCount;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public Object getNodeData() {
        return nodePath.getValue();
    }

    public TreePath getNodeIndexPath() {
        return nodeIndexPath;
    }

    public int getNodeLevel() {
        return nodeLevel;
    }

    public boolean getNodeHasChildren() {
        return nodeHasChildren;
    }

    public boolean getChildrenPreloaded() {
        return childrenPreloaded;
    }

    public TreePath getNodePath() {
        return nodePath;
    }

    public Object getNodeKey() {
        return nodeKeyPath.getValue();
    }

    public TreePath getNodeKeyPath() {
        return nodeKeyPath;
    }

    public boolean isNodeInitiallyVisible() {
        return nodeInitiallyVisible;
    }

    public void setNodeFilteringFlags(boolean[] nodeFilteringFlags) {
        this.nodeFilteringFlags = nodeFilteringFlags;
    }

    public boolean[] getNodeFilteringFlags() {
        return nodeFilteringFlags;
    }

    public void setAcceptedByFilters(boolean value) {
        acceptedByFilters = value;
    }

    public boolean isAcceptedByFilters() {
        return acceptedByFilters;
    }

    public void setChildNodeCount(int childNodeCount) {
        this.childNodeCount = childNodeCount;
    }

    public void setChildrenPreloaded(boolean childrenPreloaded) {
        this.childrenPreloaded = childrenPreloaded;
    }

    public void setNodeHasChildren(boolean nodeHasChildren) {
        this.nodeHasChildren = nodeHasChildren;
    }
}
