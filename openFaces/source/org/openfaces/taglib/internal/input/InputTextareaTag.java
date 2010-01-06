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
package org.openfaces.taglib.internal.input;

import org.openfaces.component.input.InputTextarea;
import org.openfaces.taglib.internal.OUIInputTextTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Alexander Golubev
 */
public class InputTextareaTag extends OUIInputTextTag {
    private static final String RENDERER_TYPE = "org.openfaces.InputTextareaRenderer";

    public String getComponentType() {
        return InputTextarea.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setIntProperty(component, "rows");
        setIntProperty(component, "cols");

        setStringProperty(component, "dir");
        setStringProperty(component, "lang");
        setStringProperty(component, "onselect");

        setBooleanProperty(component, "readonly");
        setBooleanProperty(component, "autoGrowing");
    }
}
