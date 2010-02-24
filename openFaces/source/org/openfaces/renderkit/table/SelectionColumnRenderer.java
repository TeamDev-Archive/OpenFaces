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
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class SelectionColumnRenderer extends BaseColumnRenderer {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered())
            return;

        AbstractTable table = getTable((BaseColumn) component);
        if (table == null) {
            throw new IllegalStateException("SelectionColumn must be nested inside a DataTable/TreeTable component");
        }
        AbstractTableSelection selection = table.getSelection();
        if (selection == null) {
            throw new IllegalStateException("<o:selectionColumn> must be inserted into a DataTable/TreeTable with selection enabled. table id: " + table.getClientId(context));
        }
        boolean multipleRowSelection = selection.isMultipleSelectionAllowed();
        boolean rowSelection = true; // selection instanceof RowSelection
        /*if (!rowSelection)
            throw new IllegalStateException("<o:selectionColumn> can only be inserted into a DataTable/TreeTable with row selection. table id: " + table.getClientId(context));
        */
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", component);
        writer.writeAttribute("type", multipleRowSelection ? "checkbox" : "radio", null);
        Styles.renderStyleClasses(context, component);
        writer.endElement("input");
    }

}
