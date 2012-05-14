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

/**
 * @author Dmitry Pikhulya
 */
// todo: review expansion state hierarchy. ExpansionState defines setNodeExpanded method and getMutableExpansionState,
// which seems somewhat counter-exclusive since you cannot generally invoke setNodeExpanded without
// getMutableExpansionState() anyway, so the solution would be either to:
// - Introduce two entities "mutable expansion state" and "static expansion state" instead of just "expansion state",
//   and place the setNodeExpanded, expand... methods there.
// - Another alternative is to make expansion state an immutable entity and change the modifications methods to the
//   ExpansionState expandNodePath(...) style.
public interface ExpansionState {
    boolean isNodeExpanded(TreePath keyPath);

    void setNodeExpanded(TreePath keyPath, boolean expanded);

    /**
     * Expands a node at the specified path, and all of its parent nodes.
     * @param keyPath a tree path consisting of node key objects. Note that if the <o:dynamicTreeStructure> tag doesn't
     * have a nodeKey attribute then  node key objects are considered to be the same as the appropriate node data
     * objects.
     */
    void expandNodePath(TreePath keyPath);

    ExpansionState getMutableExpansionState();
}
