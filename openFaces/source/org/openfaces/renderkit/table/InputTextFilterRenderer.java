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
import org.openfaces.component.table.Filter;
import org.openfaces.util.StyleUtil;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class InputTextFilterRenderer extends TextSearchFilterRenderer {
    protected void configureInputComponent(FacesContext context, Filter filter, UIInput inputComponent) {
        InputText input = (InputText) inputComponent;
        input.setOnkeypress(getFilterOnEnterScript(filter));
        input.setOnchange(getFilterSubmissionScript(filter));
        input.setStyle(filter.getStyle());
        input.setStyleClass(StyleUtil.mergeClassNames(filter.getStyleClass(), "o_fullWidth"));

        input.setPromptText(filter.getPromptText());
        input.setPromptTextClass(filter.getPromptTextClass());
        input.setPromptTextStyle(filter.getPromptTextStyle());

        input.setOnkeydown("O$.cancelBubble(event);");
    }

}
