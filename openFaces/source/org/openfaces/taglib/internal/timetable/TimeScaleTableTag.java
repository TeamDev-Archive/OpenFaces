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

import org.openfaces.component.timetable.TimeTextPosition;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 * @author Roman Porotnikov
 */
public abstract class TimeScaleTableTag extends TimetableViewTag {

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setTimePropertyAsString(component, "startTime");
        setTimePropertyAsString(component, "endTime");
        setTimePropertyAsString(component, "scrollTime");
        setEnumerationProperty(component, "timeTextPosition", TimeTextPosition.class);
        setBooleanProperty(component, "showTimeAgainstSeparator");

        setStringProperty(component, "resourceHeadersRowStyle");
        setStringProperty(component, "resourceHeadersRowClass");
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
