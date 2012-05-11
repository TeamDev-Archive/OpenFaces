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
public class SeveralLevelsPreloaded implements PreloadedNodes, Serializable {
    private int level;

    public SeveralLevelsPreloaded(int level) {
        this.level = level;
    }

    public boolean getPreloadChildren(TreePath keyPath) {
        int childrenLevel = keyPath.getLevel() + 1;
        boolean preloadChildren = (childrenLevel < level);
        return preloadChildren;
    }
}
