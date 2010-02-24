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
import org.openfaces.component.table.AbstractTableSelection;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.CheckboxColumn;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class SelectAllCheckboxRenderer extends RendererBase {

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        AbstractTable table = getTable(component);
        if (table == null)
            throw new IllegalStateException("SelectionColumn must be nested inside a table");
        BaseColumn col = getColumn(component);
        boolean checkBoxColHeader = col instanceof CheckboxColumn;
        AbstractTableSelection selection;
        if (!checkBoxColHeader) {
            selection = table.getSelection();
            if (selection == null)
                throw new IllegalStateException("<o:selectAllCheckbox> can only be inserted into a DataTable or TreeTable with multiple selection. clientId = " + component.getClientId(context));
            boolean multipleRowSelection = selection.isMultipleSelectionAllowed();
            if (!multipleRowSelection)
                throw new IllegalStateException("<o:selectAllCheckbox> can only be inserted into a DataTable or TreeTable with multiple selection. clientId = " + component.getClientId(context));
        } else
            selection = null;

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", component);
        writeIdAttribute(context, component);
        if (!checkBoxColHeader) {
            if (!selection.isEnabled())
                writer.writeAttribute("disabled", "disabled", null);
        }
        writer.writeAttribute("type", "checkbox", null);

        ScriptBuilder buf = new ScriptBuilder();
        if (checkBoxColHeader) {
            buf.initScript(context, component, "O$.Table._initCheckboxColHeader", table, col).semicolon();
        } else {
            buf.initScript(context, component, "O$.Table._initSelectionHeader", table).semicolon();
        }

        Rendering.renderInitScript(context, buf,
                Resources.getUtilJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                AbstractTableRenderer.getTableJsURL(context)
        );

        Styles.renderStyleClasses(context, component);
        writer.endElement("input");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private static AbstractTable getTable(UIComponent header) {
        for (UIComponent component = header.getParent(); component != null; component = component.getParent())
            if (component instanceof AbstractTable)
                return (AbstractTable) component;
        return null;
    }

    private static BaseColumn getColumn(UIComponent header) {
        for (UIComponent component = header.getParent(); component != null; component = component.getParent())
            if (component instanceof BaseColumn)
                return (BaseColumn) component;
        return null;
    }
}
