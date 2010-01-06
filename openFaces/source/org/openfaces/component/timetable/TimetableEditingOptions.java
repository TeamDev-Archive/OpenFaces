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
package org.openfaces.component.timetable;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class TimetableEditingOptions extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.TimetableEditingOptions";
    public static final String COMPONENT_FAMILY = "org.openfaces.TimetableEditingOptions";

    private Boolean overlappedEventsAllowed;
    private Boolean eventResourceEditable;
    private Boolean eventDurationEditable;
    private Integer defaultEventDuration;
    private Boolean autoSaveChanges;

    public TimetableEditingOptions() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                overlappedEventsAllowed,
                eventResourceEditable,
                eventDurationEditable,
                defaultEventDuration,
                autoSaveChanges
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        overlappedEventsAllowed = (Boolean) state[i++];
        eventResourceEditable = (Boolean) state[i++];
        eventDurationEditable = (Boolean) state[i++];
        defaultEventDuration = (Integer) state[i++];
        autoSaveChanges = (Boolean) state[i++];
    }

    public boolean getOverlappedEventsAllowed() {
        return ValueBindings.get(this, "overlappedEventsAllowed", overlappedEventsAllowed, true);
    }

    public void setOverlappedEventsAllowed(boolean overlappedEventsAllowed) {
        this.overlappedEventsAllowed = overlappedEventsAllowed;
    }

    public boolean isEventResourceEditable() {
        return ValueBindings.get(this, "eventResourceEditable", eventResourceEditable, true);
    }

    public void setEventResourceEditable(boolean eventResourceEditable) {
        this.eventResourceEditable = eventResourceEditable;
    }

    public boolean isEventDurationEditable() {
        return ValueBindings.get(this, "eventDurationEditable", eventDurationEditable, true);
    }

    public void setEventDurationEditable(boolean eventDurationEditable) {
        this.eventDurationEditable = eventDurationEditable;
    }

    public int getDefaultEventDuration() {
        return ValueBindings.get(this, "defaultEventDuration", defaultEventDuration, 30);
    }

    public void setDefaultEventDuration(int defaultEventDuration) {
        this.defaultEventDuration = defaultEventDuration;
    }

    public boolean getAutoSaveChanges() {
        return ValueBindings.get(this, "autoSaveChanges", autoSaveChanges, true);
    }

    public void setAutoSaveChanges(boolean autoSaveChanges) {
        this.autoSaveChanges = autoSaveChanges;
    }
}
