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
package org.openfaces.taglib.internal.validation;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class MessageTag extends AbstractComponentTag {
    private static final String RENDERER_TYPE = "org.openfaces.Message";

    public String getComponentType() {
        return HtmlMessage.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setStringProperty(component, "for");
        setBooleanProperty(component, "showSummary");
        setBooleanProperty(component, "showDetail");
        setBooleanProperty(component, "globalOnly");
        setStringProperty(component, "infoClass");
        setStringProperty(component, "infoStyle");
        setStringProperty(component, "warnClass");
        setStringProperty(component, "warnStyle");
        setStringProperty(component, "errorClass");
        setStringProperty(component, "errorStyle");
        setStringProperty(component, "fatalClass");
        setStringProperty(component, "fatalStyle");
        setStringProperty(component, "layout");
        setBooleanProperty(component, "tooltip");
        setStringProperty(component, "dir");
        setStringProperty(component, "lang");
        setStringProperty(component, "title");
    }
}
