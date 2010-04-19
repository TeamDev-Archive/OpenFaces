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
package org.openfaces.renderkit.table;

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.ColumnVisibilityMenu;
import org.openfaces.renderkit.command.PopupMenuRenderer;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ColumnVisibilityMenuRenderer extends PopupMenuRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ColumnVisibilityMenu cvm = (ColumnVisibilityMenu) component;
        cvm.updateMenuItems(context);
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
        ColumnVisibilityMenu cvm = (ColumnVisibilityMenu) component;
        AbstractTable table = cvm.getTable();
        Rendering.renderInitScript(context, new ScriptBuilder().initScript(context,
                component, "O$.ColumnMenu._initColumnVisibilityMenu", table),
                AbstractTableRenderer.getTableJsURL(context));

    }
}
