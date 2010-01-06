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
package org.openfaces.taglib.internal.validation;

import org.openfaces.component.validation.FloatingIconMessage;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class FloatingIconMessageTag extends AbstractComponentTag {

    private static final String RENDERER_TYPE = "org.openfaces.FloatingIconMessage";

    public String getComponentType() {
        return FloatingIconMessage.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "for");
        setStringProperty(component, "imageUrl");
        setIntProperty(component, "offsetTop");
        setIntProperty(component, "offsetLeft");

        setBooleanProperty(component, "showDetail");
        setBooleanProperty(component, "showSummary");

        setBooleanProperty(component, "noImage");
        setBooleanProperty(component, "noStyle");
    }
}
