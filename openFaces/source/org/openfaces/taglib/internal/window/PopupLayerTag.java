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
package org.openfaces.taglib.internal.window;

import org.openfaces.component.window.PopupLayer;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class PopupLayerTag extends AbstractComponentTag {

    public String getComponentType() {
        return PopupLayer.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.PopupLayerRenderer";
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "left");
        setStringProperty(component, "top");
        setStringProperty(component, "width");
        setStringProperty(component, "height");
        setStringProperty(component, "style");
        setStringProperty(component, "rolloverStyle");
        setStringProperty(component, "modalLayerStyle");
        setStringProperty(component, "rolloverClass");
        setStringProperty(component, "modalLayerClass");
        setStringProperty(component, "onshow");
        setStringProperty(component, "onhide");

        setIntProperty(component, "hidingTimeout");

        setStringProperty(component, "anchorElementId");
        setStringProperty(component, "anchorX");
        setStringProperty(component, "anchorY");

        setBooleanProperty(component, "draggable");

        setStringProperty(component, "ondragstart");
        setStringProperty(component, "ondragend");

        setBooleanProperty(component, "visible");
        setBooleanProperty(component, "hideOnOuterClick");
        setBooleanProperty(component, "hideOnEsc");
        setBooleanProperty(component, "modal");
    }
}
