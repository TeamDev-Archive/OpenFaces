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
import org.openfaces.component.table.CancelGroupingMenuItem;
import org.openfaces.component.table.ColumnMenu;
import org.openfaces.component.table.ColumnVisibilityMenu;
import org.openfaces.component.table.GroupByColumnMenuItem;
import org.openfaces.component.table.HideColumnMenuItem;
import org.openfaces.component.table.RemoveFromGroupingMenuItem;
import org.openfaces.component.table.SortAscendingMenuItem;
import org.openfaces.component.table.SortDescendingMenuItem;
import org.openfaces.renderkit.command.PopupMenuRenderer;

import javax.faces.application.Application;
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
            Application application = FacesContext.getCurrentInstance().getApplication();
            menuItems.add(application.createComponent(SortAscendingMenuItem.COMPONENT_TYPE));
            menuItems.add(application.createComponent(SortDescendingMenuItem.COMPONENT_TYPE));
            menuItems.add(application.createComponent(GroupByColumnMenuItem.COMPONENT_TYPE));
            menuItems.add(application.createComponent(RemoveFromGroupingMenuItem.COMPONENT_TYPE));
//            menuItems.add(application.createComponent(CancelGroupingMenuItem.COMPONENT_TYPE));
            menuItems.add(application.createComponent(HideColumnMenuItem.COMPONENT_TYPE));
            menuItems.add(application.createComponent(MenuSeparator.COMPONENT_TYPE));
            ColumnVisibilityMenu columnVisibilityMenu = (ColumnVisibilityMenu) application.createComponent(ColumnVisibilityMenu.COMPONENT_TYPE);
            menuItems.add(new MenuItem("Columns", columnVisibilityMenu));
        }

        super.encodeBegin(context, component);
    }
}
