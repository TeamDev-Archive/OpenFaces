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

import java.io.Serializable;

/**
 * @author Dmitry Pikhulya
 */
public class SeveralLevelsExpanded implements ExpansionState, Serializable {
    private int level;

    public SeveralLevelsExpanded(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isNodeExpanded(TreePath keyPath) {
        return keyPath.getLevel() < level;
    }

    public void setNodeExpanded(TreePath keyPath, boolean expanded) {
        if (expanded) {
            if (!isNodeExpanded(keyPath))
                throw new UnsupportedOperationException("SeveralLevelsExpanded can't expand a node on level " + level + " or deper: " + keyPath.getLevel());
        } else {
            if (isNodeExpanded(keyPath))
                throw new UnsupportedOperationException("SeveralLevelsExpanded can't collapse a node on top " + level + " levels: " + keyPath.getLevel());
        }
    }

    public ExpansionState getMutableExpansionState() {
        return new DynamicNodeExpansionState(this);
    }
}
