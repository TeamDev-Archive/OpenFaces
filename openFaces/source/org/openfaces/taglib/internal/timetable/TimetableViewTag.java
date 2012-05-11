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

import org.openfaces.component.timetable.PreloadedEvents;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 * @author Roman Porotnikov
 */
public abstract class TimetableViewTag extends AbstractComponentTag {

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setStringProperty(component, "eventVar", false, false);
        setValueExpressionProperty(component, "events");
        setValueExpressionProperty(component, "resources");
        setValueExpressionProperty(component, "day");
        setLocaleProperty(component, "locale");
        setTimeZoneProperty(component, "timeZone");

        setEnumerationProperty(component, "preloadedEvents", PreloadedEvents.class);

        setBooleanProperty(component, "editable");
        setMethodExpressionProperty(facesContext, component, "timetableChangeListener",
                new Class[]{TimetableChangeEvent.class}, void.class);
        setStringProperty(component, "onchange");

        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");
        setStringProperty(component, "footerStyle");
        setStringProperty(component, "footerClass");

        setColorProperty(component, "defaultEventColor");
        setColorProperty(component, "reservedTimeEventColor");
        setStringProperty(component, "reservedTimeEventStyle");
        setStringProperty(component, "reservedTimeEventClass");
        setStringProperty(component, "rolloverEventNoteStyle");
        setStringProperty(component, "rolloverEventNoteClass");

        setStringProperty(component, "rowStyle");
        setStringProperty(component, "rowClass");
    }
}
