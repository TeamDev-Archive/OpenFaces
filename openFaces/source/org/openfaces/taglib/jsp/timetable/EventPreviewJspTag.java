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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.EventPreviewTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class EventPreviewJspTag extends AbstractComponentJspTag {

    public EventPreviewJspTag() {
        super(new EventPreviewTag());
        setId("_eventPreview"); // this component should be only the one at DayTable. See JSFC-3916
    }

    public void setEventNameStyle(ValueExpression eventNameStyle) {
        getDelegate().setPropertyValue("eventNameStyle", eventNameStyle);
    }

    public void setEventNameClass(ValueExpression eventNameClass) {
        getDelegate().setPropertyValue("eventNameClass", eventNameClass);
    }

    public void setEventDescriptionStyle(ValueExpression eventDescriptionStyle) {
        getDelegate().setPropertyValue("eventDescriptionStyle", eventDescriptionStyle);
    }

    public void setEventDescriptionClass(ValueExpression eventDescriptionClass) {
        getDelegate().setPropertyValue("eventDescriptionClass", eventDescriptionClass);
    }

    public void setEscapeEventName(ValueExpression escapeEventName) {
        getDelegate().setPropertyValue("escapeEventName", escapeEventName);
    }

    public void setEscapeEventDescription(ValueExpression escapeEventDescription) {
        getDelegate().setPropertyValue("escapeEventDescription", escapeEventDescription);
    }

    public void setHorizontalAlignment(ValueExpression horizontalAlignment) {
        getDelegate().setPropertyValue("horizontalAlignment", horizontalAlignment);
    }

    public void setVerticalAlignment(ValueExpression verticalAlignment) {
        getDelegate().setPropertyValue("verticalAlignment", verticalAlignment);
    }

    public void setHorizontalDistance(ValueExpression horizontalDistance) {
        getDelegate().setPropertyValue("horizontalDistance", horizontalDistance);
    }

    public void setVerticalDistance(ValueExpression verticalDistance) {
        getDelegate().setPropertyValue("verticalDistance", verticalDistance);
    }

    public void setShowingDelay(ValueExpression showingDelay) {
        getDelegate().setPropertyValue("showingDelay", showingDelay);
    }
}
