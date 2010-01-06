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
package org.openfaces.taglib.internal.timetable;

import org.openfaces.component.timetable.EventEditorDialog;
import org.openfaces.taglib.internal.window.WindowTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorDialogTag extends WindowTag {
    @Override
    public String getComponentType() {
        return EventEditorDialog.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return "org.openfaces.EventEditorDialogRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "createEventCaption");
        setStringProperty(component, "editEventCaption");
        setStringProperty(component, "nameLabel");
        setStringProperty(component, "resourceLabel");
        setStringProperty(component, "startLabel");
        setStringProperty(component, "endLabel");
        setStringProperty(component, "colorLabel");
        setStringProperty(component, "descriptionLabel");
        setStringProperty(component, "okButtonText");
        setStringProperty(component, "cancelButtonText");
        setStringProperty(component, "deleteButtonText");
        setStringProperty(component, "labelStyle");
        setStringProperty(component, "labelClass");
        setStringProperty(component, "okButtonStyle");
        setStringProperty(component, "okButtonClass");
        setStringProperty(component, "cancelButtonStyle");
        setStringProperty(component, "cancelButtonClass");
        setStringProperty(component, "deleteButtonStyle");
        setStringProperty(component, "deleteButtonClass");

        setBooleanProperty(component, "centered");
    }
}
