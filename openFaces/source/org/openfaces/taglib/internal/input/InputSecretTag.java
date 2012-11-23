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
package org.openfaces.taglib.internal.input;

import org.openfaces.component.input.InputSecret;
import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.internal.OUIInputTextTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Andre Shapovalov
 */
public class InputSecretTag extends OUIInputTextTag {
    private static final String RENDERER_TYPE = "org.openfaces.InputSecretRenderer";

    public String getComponentType() {
        return InputSecret.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setIntProperty(component, "maxlength");
        setIntProperty(component, "size");
        setIntProperty(component, "interval");
        setIntProperty(component, "duration");
        setObjectProperty(component, "replacement");
        setObjectProperty(component, "promptText");
    }
}
