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
package org.openfaces.taglib.internal.window;

import org.openfaces.component.window.ButtonType;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class ConfirmationTag extends AbstractWindowTag {
    private static final String COMPONENT_TYPE = "org.openfaces.Confirmation";
    private static final String RENDERER_TYPE = "org.openfaces.ConfirmationRenderer";

    @Override
    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "for");
        setStringProperty(component, "event");
        setBooleanProperty(component, "standalone");

        setStringProperty(component, "message");
        setStringProperty(component, "details");
        setStringProperty(component, "okButtonText");
        setStringProperty(component, "cancelButtonText");

        setEnumerationProperty(component, "defaultButton", ButtonType.class);

        setStringProperty(component, "messageIconUrl");
        setBooleanProperty(component, "showMessageIcon");

        setStringProperty(component, "iconAreaStyle");
        setStringProperty(component, "rolloverIconAreaStyle");
        setStringProperty(component, "rolloverContentStyle");
        setStringProperty(component, "messageStyle");
        setStringProperty(component, "rolloverMessageStyle");
        setStringProperty(component, "detailsStyle");
        setStringProperty(component, "rolloverDetailsStyle");
        setStringProperty(component, "buttonAreaStyle");
        setStringProperty(component, "rolloverButtonAreaStyle");
        setStringProperty(component, "rolloverButtonAreaClass");
        setStringProperty(component, "okButtonStyle");
        setStringProperty(component, "rolloverOkButtonStyle");
        setStringProperty(component, "cancelButtonStyle");
        setStringProperty(component, "rolloverCancelButtonStyle");

        setStringProperty(component, "iconAreaClass");
        setStringProperty(component, "rolloverIconAreaClass");
        setStringProperty(component, "rolloverContentClass");
        setStringProperty(component, "messageClass");
        setStringProperty(component, "rolloverMessageClass");
        setStringProperty(component, "detailsClass");
        setStringProperty(component, "rolloverDetailsClass");
        setStringProperty(component, "buttonAreaClass");
        setStringProperty(component, "rolloverButtonBacklaneClass");
        setStringProperty(component, "okButtonClass");
        setStringProperty(component, "rolloverOkButtonClass");
        setStringProperty(component, "cancelButtonClass");
        setStringProperty(component, "rolloverCancelButtonClass");

        setBooleanProperty(component, "alignToInvoker");
    }
}
