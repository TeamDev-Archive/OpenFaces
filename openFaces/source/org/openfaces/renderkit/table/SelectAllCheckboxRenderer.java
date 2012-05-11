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
import org.openfaces.component.table.CheckboxColumn;
import org.openfaces.component.table.SelectAllCheckbox;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.renderkit.select.SelectBooleanCheckboxRenderer;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public class SelectAllCheckboxRenderer extends SelectBooleanCheckboxRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
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

        SelectAllCheckbox selectAllCheckbox = (SelectAllCheckbox) component;
        if (!checkBoxColHeader) {
            if (!selection.isEnabled()) {
                selectAllCheckbox.setDisabled(true);
            }
        }

        selectAllCheckbox.setSelected(false);

        super.encodeBegin(context, component);
    }

    @Override
    protected void renderInitScript(
            FacesContext facesContext,
            SelectBooleanCheckbox checkbox,
            JSONObject imagesObj,
            JSONObject stylesObj,
            AnonymousFunction onchangeFunction,
            boolean triStateAllowed) throws IOException {
        super.renderInitScript(facesContext, checkbox, imagesObj, stylesObj, onchangeFunction, triStateAllowed);

        AbstractTable table = getTable(checkbox);
        if (table == null)
            throw new IllegalStateException("SelectionColumn must be nested inside a table");
        BaseColumn col = getColumn(checkbox);

        FacesContext context = FacesContext.getCurrentInstance();
        ScriptBuilder buf = new ScriptBuilder();
        Integer checkboxColIndex = col instanceof CheckboxColumn ? table.getRenderedColumns().indexOf(col) : null;
        buf.initScript(context, checkbox, "O$.Table._initSelectAllCheckbox", table, checkboxColIndex).semicolon();

        Rendering.renderInitScript(context, buf,
                Resources.utilJsURL(context),
                TableUtil.getTableUtilJsURL(context),
                AbstractTableRenderer.getTableJsURL(context)
        );

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
