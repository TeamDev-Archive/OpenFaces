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
package org.openfaces.taglib.internal.timetable;

import org.openfaces.component.timetable.EventAction;
import org.openfaces.component.timetable.EventActionEvent;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class EventActionTag extends AbstractComponentTag {
    public String getComponentType() {
        return EventAction.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "pressedStyle");
        setStringProperty(component, "pressedClass");

        setStringProperty(component, "imageUrl");
        setStringProperty(component, "rolloverImageUrl");
        setStringProperty(component, "pressedImageUrl");

        setActionListener(facesContext, (EventAction) component, new Class[]{EventActionEvent.class});

        setStringProperty(component, "hint");
        setEnumerationProperty(component, "scope", org.openfaces.component.timetable.EventActionScope.class);
    }

}
