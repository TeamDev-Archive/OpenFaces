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

import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.table.DataTableFilter;
import org.openfaces.component.table.TextFilterCriterion;
import org.openfaces.component.table.TextSearchDataTableFilter;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TextSearchDataTableFilterRenderer extends AbstractDataTableFilterRenderer {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResourceUtil.renderJSLinkIfNeeded(ResourceUtil.getUtilJsURL(context), context);
        TextSearchDataTableFilter filter = (TextSearchDataTableFilter) component;

        UIInput inputComponent = (UIInput) filter.getSearchComponent();
        inputComponent.setValue(getStringValue(filter));
        configureInputComponent(context, filter, inputComponent);

        inputComponent.encodeAll(context);

        StyleUtil.renderStyleClasses(context, component);
    }

    protected abstract void configureInputComponent(FacesContext context, DataTableFilter filter, UIInput inputComponent);

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        TextSearchDataTableFilter filter = (TextSearchDataTableFilter) component;
        UIInput input = (UIInput) filter.getSearchComponent();
        String newSearchString = (String) input.getSubmittedValue();
        if (newSearchString == null) {
            newSearchString = "";
        }
        setDecodedSearchString(filter, new TextFilterCriterion(newSearchString));
    }

    protected String getFilterOnEnterScript(FacesContext context, DataTableFilter filter) {
        AbstractTable table = getTable(filter);
        String tableId = table.getClientId(context);
        return "return O$._filterFieldKeyPressHandler('" + tableId + "', this, event);";
    }

    protected String getStringValue(DataTableFilter filter) {
        return ((TextSearchDataTableFilter) filter).getStringValue();
    }
}
