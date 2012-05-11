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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.AbstractTableSelection;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.component.table.SelectRowCheckbox;
import org.openfaces.component.table.TreeTable;
import org.openfaces.renderkit.select.SelectBooleanCheckboxRenderer;
import org.openfaces.util.Components;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectRowCheckboxRenderer extends SelectBooleanCheckboxRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        SelectRowCheckbox selectRowCheckbox = (SelectRowCheckbox) component;
        checkUsageContext(selectRowCheckbox);

        selectRowCheckbox.setStyleClass("o_selectRowCheckbox");
        selectRowCheckbox.setValue(false);
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
    }

    private static void checkUsageContext(SelectRowCheckbox selectRowCheckbox) {
        FacesContext context = FacesContext.getCurrentInstance();

        UIComponent component = selectRowCheckbox.getParent();
        while (component != null) {
            if (component instanceof BaseColumn) {
                BaseColumn column = (BaseColumn) component;
                AbstractTable table = Components.getParentWithClass(column, AbstractTable.class);
                if (table == null)
                    throw new FacesException("<o:selectRowCheckbox> can only be used in columns of table components " +
                            "(<o:dataTable> or <o:treeTable)");

                AbstractTableSelection selection = table.getSelection();
                if (selection == null)
                    throw new IllegalStateException("<o:selectRowCheckbox> can only be used in a DataTable or " +
                            "TreeTable with multiple selection. clientId = " + selectRowCheckbox.getClientId(context));
                boolean multipleRowSelection = selection.isMultipleSelectionAllowed();
                if (!multipleRowSelection)
                    throw new IllegalStateException("<o:selectRowCheckbox> can only be inserted into a DataTable or " +
                            "TreeTable with multiple selection, but single selection is used here. clientId = " + selectRowCheckbox.getClientId(context));

                selectRowCheckbox.setTriStateAllowed(table instanceof TreeTable);
                return;
            }
            UIComponent parent = component.getParent();
            if (parent != null && parent.getFacets().containsValue(component))
                throw new FacesException("<o:selectRowCheckbox> can't be placed inside of column's facets -- " +
                        "it should be among regular child components of <o:column>, or another column tag. " +
                        "clientId = " + selectRowCheckbox.getClientId(context));
            component = parent;
        }
        throw new IllegalStateException("<o:selectRowCheckbox> can only be used in a DataTable or TreeTable " +
                "components. clientId = " + selectRowCheckbox.getClientId(context));
    }

}
