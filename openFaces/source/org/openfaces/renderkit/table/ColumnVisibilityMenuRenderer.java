/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.table;

import org.openfaces.component.action.MenuItem;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.ColumnVisibilityMenu;
import org.openfaces.renderkit.action.PopupMenuRenderer;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class ColumnVisibilityMenuRenderer extends PopupMenuRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ColumnVisibilityMenu cvm = (ColumnVisibilityMenu) component;
        List<UIComponent> menuChildren = cvm.getChildren();
        menuChildren.clear();
        UIComponent parent = cvm.getParent();
        if (! (parent instanceof AbstractTable))
            throw new FacesException("<o:columnVisibilityMenu> can only be inserted into the \"columnMenu\" facet of the <o:dataTable> or <o:treeTable> components.");
        AbstractTable table = (AbstractTable) parent;
        List<BaseColumn> columns = table.getColumnsForRendering();
        for (BaseColumn column : columns) {
            MenuItem menuItem = new MenuItem();
            menuItem.setValue(getColumnName(column));
            menuChildren.add(menuItem);
        }
        super.encodeBegin(context, component);
    }

    private String getColumnName(BaseColumn column) {
        UIComponent header = column.getHeader();
        if (header instanceof ValueHolder)
            return (String) ((ValueHolder) header).getValue();
        else
            return "";
    }
}
