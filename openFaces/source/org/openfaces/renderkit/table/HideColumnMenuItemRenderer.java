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

import org.openfaces.component.command.MenuItem;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class HideColumnMenuItemRenderer extends ColumnMenuItemRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        MenuItem menuItem = (MenuItem) component;
        if (menuItem.getValue() == null)
            menuItem.setValue("Hide");
        menuItem.setOnclick(new ScriptBuilder().functionCall("O$.ColumnMenu._hideColumn",
                getTable("<o:hideColumnMenuItem>", menuItem)).getScript());
        super.encodeBegin(context, component);
    }

}
