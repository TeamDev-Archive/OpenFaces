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
package org.seleniuminspector.openfaces;

import org.seleniuminspector.html.TableSectionInspector;
import org.seleniuminspector.html.TableRowInspector;

/**
 * @author Dmitry Pikhulya
 */
public class TreeTableSectionInspector extends TableSectionInspector {
    public TreeTableSectionInspector(TreeTableInspector treeTableInspector, String sectionTagName) {
        super(treeTableInspector, sectionTagName);
    }

    protected TableRowInspector createRowInspector(int rowIndex) {
        return new TreeTableRowInspector(this, rowIndex);
    }
}
