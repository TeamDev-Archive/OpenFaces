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

/**
 * @author Dmitry Pikhulya
 */
public class AllNodesCollapsed implements ExpansionState, Serializable {
    public boolean isNodeExpanded(TreePath keyPath) {
        return false;
    }

    public void setNodeExpanded(TreePath keyPath, boolean expanded) {
        if (expanded)
            throw new UnsupportedOperationException("Can't expand nodes on AllNodesCollapsed");
    }

    public void expandNodePath(TreePath keyPath) {
        throw new UnsupportedOperationException("You cannot expand nodes on AllNodesCollapsed instance directly. " +
                "Obtain a mutable instance with getMutableExpansionState() first.");
    }

    public ExpansionState getMutableExpansionState() {
        return new DynamicNodeExpansionState(this);
    }

}
