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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class DynamicNodeExpansionState implements ExpansionState, Serializable {
    private ExpansionState defaultNodeExpansion;
    private List<TreePath> nonDefaultExpansionKeyPaths = new ArrayList<TreePath>();

    public DynamicNodeExpansionState() {
        this(new AllNodesCollapsed());
    }

    public DynamicNodeExpansionState(ExpansionState defaultNodeExpansion) {
        this.defaultNodeExpansion = defaultNodeExpansion;
    }

    public boolean isNodeExpanded(TreePath keyPath) {
        if (keyPath == null)
            return false;
        boolean nonDefaultExpansion = nonDefaultExpansionKeyPaths.contains(keyPath);
        boolean expanded = defaultNodeExpansion.isNodeExpanded(keyPath) ^ nonDefaultExpansion;
        return expanded;
    }

    public void setNodeExpanded(TreePath keyPath, boolean expanded) {
        boolean oldExpansion = isNodeExpanded(keyPath);
        if (expanded == oldExpansion)
            return;
        if (oldExpansion == defaultNodeExpansion.isNodeExpanded(keyPath))
            nonDefaultExpansionKeyPaths.add(keyPath);
        else
            nonDefaultExpansionKeyPaths.remove(keyPath);
    }

    /**
     * Expands a node at the specified path, and all of its parent nodes.
     * @param keyPath a tree path consisting of node key objects. Note that if the <o:dynamicTreeStructure> tag doesn't
     * have a nodeKey attribute then  node key objects are considered to be the same as the appropriate node data
     * objects.
     */
    public void expandNodePath(TreePath keyPath) {
        do {
            setNodeExpanded(keyPath, true);
            keyPath = keyPath.getParentPath();
        } while (keyPath != null);
    }

    public ExpansionState getMutableExpansionState() {
        return this;
    }

}
