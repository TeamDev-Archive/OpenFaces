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

import org.openfaces.component.input.InputText;
import org.openfaces.component.table.DataTableFilter;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class SearchFieldDataTableFilterRenderer extends TextSearchDataTableFilterRenderer {
    protected void configureInputComponent(FacesContext context, DataTableFilter filter, UIInput inputComponent) {
        InputText input = (InputText) inputComponent;
        input.setOnkeypress(getFilterOnEnterScript(context, filter));
        input.setOnchange(getFilterSubmissionScript(filter, context));
        input.setStyle(filter.getStyle());
        input.setStyleClass(filter.getStyleClass());

        input.setPromptText(filter.getPromptText());
        input.setPromptTextClass(filter.getPromptTextClass());
        input.setPromptTextStyle(filter.getPromptTextStyle());

        input.setOnkeydown("O$.cancelBubble(event);");
    }

}
