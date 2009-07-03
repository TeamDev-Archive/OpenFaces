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
package org.openfaces.taglib.internal.input;

import org.openfaces.component.input.InputText;
import org.openfaces.taglib.internal.OUIInputTextTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Vladimir Kurganov
 */
public class InputTextTag extends OUIInputTextTag {
    private static final String RENDERER_TYPE = "org.openfaces.InputTextRenderer";

    public String getComponentType() {
        return InputText.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent uiComponent) {
        super.setComponentProperties(facesContext, uiComponent);
        setStringProperty(uiComponent, "dir");
        setStringProperty(uiComponent, "lang");
        setStringProperty(uiComponent, "title");
        setStringProperty(uiComponent, "accesskey");
        setStringProperty(uiComponent, "alt");
        setStringProperty(uiComponent, "onselect");
        setStringProperty(uiComponent, "tabindex");
        setBooleanProperty(uiComponent, "readonly");
        setIntProperty(uiComponent, "maxlength");
        setIntProperty(uiComponent, "size");
    }
}
