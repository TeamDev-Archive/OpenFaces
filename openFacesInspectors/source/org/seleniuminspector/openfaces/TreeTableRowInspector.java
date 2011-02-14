/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.seleniuminspector.openfaces;

import org.seleniuminspector.ElementInspector;
import org.seleniuminspector.html.TableRowInspector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableRowInspector extends TableRowInspector {
    public TreeTableRowInspector(TreeTableSectionInspector section, int rowIndex) {
        super(section, rowIndex);
    }

    public ElementInspector expansionToggle() {
        List<ElementInspector> toggles = expansionToggles();
        if (toggles.size() == 0)
            return null;
        return toggles.get(0);
    }

    public List<ElementInspector> expansionToggles() {
        List<ElementInspector> images = getElementsByTagName("img");
        int imageCount = images.size();
        List<ElementInspector> toggles = new ArrayList<ElementInspector>(imageCount);

        for (int i = 0; i < imageCount; i++) {
            ElementInspector img = images.get(i);
            if ("o_treetable_folding".equals(img.className()))
                toggles.add(img);
        }
        return toggles;
    }

}
