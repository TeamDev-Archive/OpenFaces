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

import javax.faces.component.UIComponentBase;

/**
 * A base class for all kinds of tree structure implementations. It defines a common interface that all tree structures
 * can be queried with.
 * <p/>
 * There can be any number of nodes on the top level.
 * Each node has the following attributes:
 * <ul>
 * <li> node data (required) : Any object. Can't be null.
 * <li> node key  (optional) : Must be a serializable object that uniquely identifies a node among the whole tree.
 * must implement equals and hashcode methods to compare serialized instances correctly
 * if omitted, node data will be used as node key, so node data must comply to node key
 * requirements in this case. Can't be null.</li>
 * <li> nodeHasChildren attribute (optional) : Informs whether the node has any children without querying the actual list of children.</li>
 * <li> child nodes (required) : List of child nodes.</li>
 * </ul>
 *
 * @author Dmitry Pikhulya
 */
public abstract class TreeStructure extends UIComponentBase {

    protected TreeStructure() {
    }

    public abstract int getLevel();

    public abstract void goToTopLevel();

    public abstract void goToChildLevel();

    public abstract void goToParentLevel();

    public abstract int getNodeCount();

    public abstract void setNodeIndex(int curentNodeIndex);

    public abstract int getNodeIndex();

    public abstract boolean isNodeAvailable();

    public abstract Object getNodeKey();

    public abstract Object getNodeData();

    public abstract boolean getNodeHasChildren();
}
