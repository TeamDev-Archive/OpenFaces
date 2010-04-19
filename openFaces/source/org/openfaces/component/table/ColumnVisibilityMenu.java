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

import org.openfaces.component.command.MenuItem;
import org.openfaces.component.command.PopupMenu;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.command.PopupMenuRenderer;
import org.openfaces.renderkit.select.SelectBooleanCheckboxImageManager;
import org.openfaces.util.Resources;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

public class ColumnVisibilityMenu extends PopupMenu {
    public static final String COMPONENT_TYPE = "org.openfaces.ColumnVisibilityMenu";
    public static final String COMPONENT_FAMILY = "org.openfaces.ColumnVisibilityMenu";

    public ColumnVisibilityMenu() {
        setRendererType("org.openfaces.ColumnVisibilityMenuRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
            super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

    }

    public AbstractTable getTable() {
        UIComponent parent = getParent();
        while (parent != null && (parent instanceof MenuItem || parent instanceof PopupMenu))
            parent = parent.getParent();
        if (!(parent instanceof AbstractTable))
            throw new FacesException("<o:columnVisibilityMenu> can only be inserted into the \"columnMenu\" facet of " +
                    "the <o:dataTable> or <o:treeTable> components (either directly or as its sub-menu).");
        AbstractTable table = (AbstractTable) parent;
        return table;
    }

    public void updateMenuItems(FacesContext context) {
        List<UIComponent> menuChildren = getChildren();
        menuChildren.clear();
        AbstractTable table = getTable();
        getAttributes().put(PopupMenuRenderer.ATTR_DEFAULT_INDENT_CLASS, "o_popup_menu_indent o_columnVisibilityMenuIndent");

        List<BaseColumn> visibleColumns = table.getRenderedColumns();
        List<BaseColumn> allColumns = table.getAllColumns();
        for (BaseColumn column : allColumns) {
            MenuItem menuItem = new MenuItem();
            menuItem.setValue(TableUtil.getColumnHeader(column));
            boolean columnVisible = visibleColumns.contains(column);
            menuItem.setIconUrl(Resources.getInternalURL(context,
                    SelectBooleanCheckboxImageManager.class,
                    columnVisible
                            ? SelectBooleanCheckboxImageManager.DEFAULT_SELECTED_IMAGE
                            : SelectBooleanCheckboxImageManager.DEFAULT_UNSELECTED_IMAGE,
                    false));
            menuChildren.add(menuItem);
        }
    }

}
