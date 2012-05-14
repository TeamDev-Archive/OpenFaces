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

/**
 * @author Dmitry Pikhulya
 */
public class DataTableNodeInfo implements NodeInfo {
    private Integer childCount;
    private boolean childrenPreloaded;
    private int nodeLevel;

    public DataTableNodeInfo(int nodeLevel, Integer childCount, boolean childrenPreloaded) {
        this.nodeLevel = nodeLevel;
        this.childCount = childCount;
        this.childrenPreloaded = childrenPreloaded;
    }

    public int getNodeLevel() {
        return nodeLevel;
    }

    public int getChildNodeCount() {
        return childCount != null ? childCount : 0;
    }

    public boolean getNodeHasChildren() {
        return childCount != null;
    }

    public boolean getChildrenPreloaded() {
        return childrenPreloaded;
    }
}
