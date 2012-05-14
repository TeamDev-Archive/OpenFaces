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
package org.openfaces.taglib.internal.ajax;

import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.VerticalAlignment;
import org.openfaces.component.ajax.DefaultProgressMessage;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Eugene Goncharov
 */
public class DefaultProgressMessageTag extends AbstractComponentTag {
    public String getComponentType() {
        return DefaultProgressMessage.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "text");
        setStringProperty(component, "imageUrl");
        setEnumerationProperty(component, "horizontalAlignment", HorizontalAlignment.class);
        setEnumerationProperty(component, "verticalAlignment", VerticalAlignment.class);
        setDoubleProperty(component, "transparency");
        setIntProperty(component, "transparencyTransitionPeriod");
        setBooleanProperty(component, "fillBackground");
        setDoubleProperty(component, "backgroundTransparency");
        setIntProperty(component, "backgroundTransparencyTransitionPeriod");
        setStringProperty(component, "backgroundStyle");
        setStringProperty(component, "backgroundClass");
        setEnumerationProperty(component, "mode", org.openfaces.component.ajax.ProgressMessageMode.class);
    }
}

