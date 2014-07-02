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
package org.openfaces.renderkit.table;

import org.openfaces.component.command.MenuItem;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.renderkit.command.MenuItemRenderer;
import org.openfaces.util.Components;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnMenuItemRenderer extends MenuItemRenderer {
    protected AbstractTable getTable(String tagName, MenuItem menuItem) {
        UIComponent parent = menuItem.getParent();
        while (parent != null && (parent instanceof MenuItem || parent instanceof PopupMenu || Components.isImplicitPanel(parent)))
            parent = parent.getParent();
        if (!(parent instanceof AbstractTable))
            throw new FacesException(tagName + " can only be inserted into the \"columnMenu\" facet of " +
                    "the <o:dataTable> or <o:treeTable> components (either directly or as its sub-menu).");
        return (AbstractTable) parent;
    }
}
