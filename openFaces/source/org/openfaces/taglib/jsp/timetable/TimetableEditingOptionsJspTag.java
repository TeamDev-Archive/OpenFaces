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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.TimetableEditingOptionsTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class TimetableEditingOptionsJspTag extends AbstractComponentJspTag {

    public TimetableEditingOptionsJspTag() {
        super(new TimetableEditingOptionsTag());
    }

    public void setOverlappedEventsAllowed(ValueExpression value) {
        getDelegate().setPropertyValue("overlappedEventsAllowed", value);
    }

    public void setEventResourceEditable(ValueExpression value) {
        getDelegate().setPropertyValue("eventResourceEditable", value);
    }

    public void setEventDurationEditable(ValueExpression value) {
        getDelegate().setPropertyValue("eventDurationEditable", value);
    }

    public void setDefaultEventDuration(ValueExpression value) {
        getDelegate().setPropertyValue("defaultEventDuration", value);
    }

    public void setAutoSaveChanges(ValueExpression value) {
        getDelegate().setPropertyValue("autoSaveChanges", value);
    }
}
