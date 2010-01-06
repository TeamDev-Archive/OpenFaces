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

import org.openfaces.component.timetable.DayTable;
import org.openfaces.component.timetable.PreloadedEvents;
import org.openfaces.component.timetable.TimeTextPosition;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class DayTableTag extends AbstractComponentTag {
    public String getComponentType() {
        return DayTable.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.DayTableRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "eventVar", false, false);
        setPropertyBinding(component, "events");
        setPropertyBinding(component, "resources");
        setPropertyBinding(component, "day");
        setLocaleProperty(component, "locale");
        setTimeZoneProperty(component, "timeZone");

        setEnumerationProperty(component, "preloadedEvents", PreloadedEvents.class);

        setBooleanProperty(component, "editable");
        setMethodExpressionProperty(facesContext, component, "timetableChangeListener",
                new Class[]{TimetableChangeEvent.class}, void.class);
        setStringProperty(component, "onchange");

        setTimePropertyAsString(component, "startTime");
        setTimePropertyAsString(component, "endTime");
        setTimePropertyAsString(component, "scrollTime");
        setEnumerationProperty(component, "timeTextPosition", TimeTextPosition.class);
        setBooleanProperty(component, "showTimeAgainstSeparator");

        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");
        setStringProperty(component, "footerStyle");
        setStringProperty(component, "footerClass");
        setStringProperty(component, "resourceHeadersRowStyle");
        setStringProperty(component, "resourceHeadersRowClass");
        setStringProperty(component, "rowStyle");
        setStringProperty(component, "rowClass");
        setStringProperty(component, "timeColumnStyle");
        setStringProperty(component, "timeColumnClass");
        setStringProperty(component, "timePattern");
        setStringProperty(component, "timeSuffixPattern");
        setIntProperty(component, "majorTimeInterval");
        setIntProperty(component, "minorTimeInterval");
        setBooleanProperty(component, "showTimeForMinorIntervals");

        setStringProperty(component, "majorTimeStyle");
        setStringProperty(component, "majorTimeClass");
        setStringProperty(component, "minorTimeStyle");
        setStringProperty(component, "minorTimeClass");
        setStringProperty(component, "timeSuffixStyle");
        setStringProperty(component, "timeSuffixClass");

        setColorProperty(component, "defaultEventColor");
        setColorProperty(component, "reservedTimeEventColor");
        setStringProperty(component, "reservedTimeEventStyle");
        setStringProperty(component, "reservedTimeEventClass");
        setStringProperty(component, "rolloverEventNoteStyle");
        setStringProperty(component, "rolloverEventNoteClass");

        setIntProperty(component, "dragAndDropTransitionPeriod");
        setIntProperty(component, "dragAndDropCancelingPeriod");
        setIntProperty(component, "undroppableStateTransitionPeriod");
        setDoubleProperty(component, "undroppableEventTransparency");

        setStringProperty(component, "resourceColumnSeparator");
        setStringProperty(component, "resourceHeadersRowSeparator");
        setStringProperty(component, "timeColumnSeparator");
        setStringProperty(component, "primaryRowSeparator");
        setStringProperty(component, "secondaryRowSeparator");
        setStringProperty(component, "timeColumnPrimaryRowSeparator");
        setStringProperty(component, "timeColumnSecondaryRowSeparator");
    }
}
