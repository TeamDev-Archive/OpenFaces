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
 * This class stores TreePath'es of preloaded nodes.
 * @author Vladislav Lubenskiy
 */
class PreloadingState implements Serializable {
    private List<TreePath> preloadedNodes = new ArrayList<TreePath>();

    public void addPreloadedTreePath(TreePath path) {
        if (!preloadedNodes.contains(path))
            preloadedNodes.add(path);
    }

    public boolean isPreloaded(TreePath path) {
        return preloadedNodes.contains(path);
    }
}