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

import org.openfaces.component.select.SelectBooleanCheckbox;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.AbstractTableSelection;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.HierarchicalNodeSelection;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Arrays;

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
        AbstractTableSelection.Mode selectionMode = selection.getSelectionMode();
        boolean rowSelection = true; // selection instanceof RowSelection
        /*if (!rowSelection)
            throw new IllegalStateException("<o:selectionColumn> can only be inserted into a DataTable/TreeTable with row selection. table id: " + table.getClientId(context));
        */
        ResponseWriter writer = context.getResponseWriter();

        if (selectionMode == AbstractTableSelection.Mode.HIERARCHICAL) {
            SelectBooleanCheckbox checkbox = new SelectBooleanCheckbox();
            if (selection instanceof HierarchicalNodeSelection) {
                checkbox.setTriStateAllowed(true);
                checkbox.setStateList(Arrays.asList(SelectBooleanCheckbox.SELECTED_STATE, SelectBooleanCheckbox.UNSELECTED_STATE));
            }
            checkbox.setSelected(false);
            checkbox.encodeAll(context);

        } else {
            writer.startElement("input", component);
            writer.writeAttribute("type", selectionMode != AbstractTableSelection.Mode.SINGLE ? "checkbox" : "radio", null);
            Styles.renderStyleClasses(context, component);
            writer.endElement("input");
        }
    }

}
