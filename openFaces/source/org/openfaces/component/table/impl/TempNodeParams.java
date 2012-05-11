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

import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TempNodeParams {
    private Object nodeData;
    private NodeInfoForRow nodeInfoForRow;
    private List<NodeInfoForRow> subtreeNodeInfosForRendering;
    private List<NodeInfoForRow> subtreeNodeInfosAllFiltered;
    private List<NodeInfoForRow> subtreeNodeInfos_unfiltered;
    private boolean nodeExpanded;
    private boolean preloadChildrenToClient;
    private boolean rowAccepted;
    private boolean[] filteringFlags;

    public TempNodeParams(
            Object nodeData,
            NodeInfoForRow nodeInfoForRow,
            boolean nodeExpanded,
            boolean preloadChildrenToClient) {
        this.nodeData = nodeData;
        this.nodeInfoForRow = nodeInfoForRow;
        this.nodeExpanded = nodeExpanded;
        this.preloadChildrenToClient = preloadChildrenToClient;
    }

    public void setSubtreeNodeInfosForRendering(List<NodeInfoForRow> subtreeNodeInfosForRendering) {
        this.subtreeNodeInfosForRendering = subtreeNodeInfosForRendering;
    }

    public void setSubtreeNodeInfosAllFiltered(List<NodeInfoForRow> subtreeNodeInfosAllFiltered) {
        this.subtreeNodeInfosAllFiltered = subtreeNodeInfosAllFiltered;
    }

    public void setSubtreeNodeInfos_unfiltered(List<NodeInfoForRow> subtreeNodeInfos_unfiltered) {
        this.subtreeNodeInfos_unfiltered = subtreeNodeInfos_unfiltered;
    }

    public boolean getPreloadChildrenToClient() {
        return preloadChildrenToClient;
    }

    public List<NodeInfoForRow> getSubtreeNodeInfosAllFiltered() {
        return subtreeNodeInfosAllFiltered;
    }

    public Object getNodeData() {
        return nodeData;
    }

    public NodeInfoForRow getNodeInfoForRow() {
        return nodeInfoForRow;
    }

    public List<NodeInfoForRow> getSubtreeNodeInfosForRendering() {
        return subtreeNodeInfosForRendering;
    }

    public List<NodeInfoForRow> getSubtreeNodeInfos_unfiltered() {
        return subtreeNodeInfos_unfiltered;
    }

    public boolean isNodeExpanded() {
        return nodeExpanded;
    }

    public boolean isRowAccepted() {
        return rowAccepted;
    }

    public boolean[] getFilteringFlags() {
        return filteringFlags;
    }

    public void setRowAccepted(boolean rowAccepted) {
        this.rowAccepted = rowAccepted;
    }

    public void setFilteringFlags(boolean[] filteringFlags) {
        this.filteringFlags = filteringFlags;
    }
}
