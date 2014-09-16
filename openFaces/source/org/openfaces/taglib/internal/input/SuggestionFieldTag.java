/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.internal.input;

import org.openfaces.component.input.SuggestionField;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class SuggestionFieldTag extends DropDownFieldTag {

    @Override
    public String getComponentType() {
        return SuggestionField.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return "org.openfaces.SuggestionFieldRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        setBooleanProperty(component, "manualListOpeningAllowed");
    }
}
