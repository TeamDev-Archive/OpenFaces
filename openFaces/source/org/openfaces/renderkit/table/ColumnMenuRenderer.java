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
import org.openfaces.component.command.MenuSeparator;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.ColumnMenu;
import org.openfaces.component.table.ColumnVisibilityMenu;
import org.openfaces.component.table.GroupByColumnMenuItem;
import org.openfaces.component.table.HideColumnMenuItem;
import org.openfaces.component.table.RemoveFromGroupingMenuItem;
import org.openfaces.component.table.ResetSortingMenuItem;
import org.openfaces.component.table.SortAscendingMenuItem;
import org.openfaces.component.table.SortDescendingMenuItem;
import org.openfaces.renderkit.command.PopupMenuRenderer;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class ColumnMenuRenderer extends PopupMenuRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ColumnMenu menu = (ColumnMenu) component;
        if (menu.getChildren().size() == 0) {
            List<UIComponent> menuItems = menu.getChildren();
            menuItems.add(Components.createComponent(context, SortAscendingMenuItem.COMPONENT_TYPE, SortAscendingMenuItem.class, menu, "sortAscending"));
            menuItems.add(Components.createComponent(context, SortDescendingMenuItem.COMPONENT_TYPE, SortDescendingMenuItem.class, menu, "sortDescend"));
            if (getTable(menu).getUnsortedStateAllowed()) {
                menuItems.add(Components.createComponent(context, ResetSortingMenuItem.COMPONENT_TYPE, ResetSortingMenuItem.class, menu, "resetSorting"));
            }
            menuItems.add(Components.createComponent(context, GroupByColumnMenuItem.COMPONENT_TYPE, GroupByColumnMenuItem.class, menu, "groupByColumn"));
            menuItems.add(Components.createComponent(context, RemoveFromGroupingMenuItem.COMPONENT_TYPE, RemoveFromGroupingMenuItem.class, menu, "removeFromGrouping"));
//            menuItems.add(Components.createComponent(context, CancelGroupingMenuItem.COMPONENT_TYPE, CancelGroupingMenuItem.class, menu, "cancelGrouping");
            menuItems.add(Components.createComponent(context, HideColumnMenuItem.COMPONENT_TYPE, HideColumnMenuItem.class, menu, "hideColumn"));
            menuItems.add(Components.createComponent(context, MenuSeparator.COMPONENT_TYPE, MenuSeparator.class, menu, "separator"));
            ColumnVisibilityMenu columnVisibilityMenu = Components.createComponent(context,
                    ColumnVisibilityMenu.COMPONENT_TYPE, ColumnVisibilityMenu.class, menu, "columnVisibilityMenu");
            MenuItem columns = Components.createComponent(context, MenuItem.COMPONENT_TYPE, MenuItem.class, menu, "columnsSubmenu");
            columns.setValue("Columns");
            columns.getChildren().add(columnVisibilityMenu);
            menuItems.add(columns);
        }

        super.encodeBegin(context, component);
    }

    private AbstractTable getTable(ColumnMenu menu) {
        UIComponent parent = menu.getParent();
        while (parent != null && !(parent instanceof AbstractTable))
            parent = parent.getParent();
        return (AbstractTable) parent;
    }
}
