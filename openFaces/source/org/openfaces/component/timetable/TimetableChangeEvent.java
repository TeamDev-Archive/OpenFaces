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

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * @author Dmitry Pikhulya
 */
public class TimetableChangeEvent extends FacesEvent {
    private TimetableEvent[] addedEvents;
    private TimetableEvent[] changedEvents;
    private String[] removedEventIds;
    private boolean reloadAllEvents;

    public TimetableChangeEvent(UIComponent uiComponent,
                                TimetableEvent[] addedEvents,
                                TimetableEvent[] changedEvents,
                                String[] removedEventIds) {
        super(uiComponent);
        if (addedEvents == null)
            addedEvents = new TimetableEvent[0];
        if (changedEvents == null)
            changedEvents = new TimetableEvent[0];
        if (removedEventIds == null)
            removedEventIds = new String[0];

        this.addedEvents = addedEvents;
        this.changedEvents = changedEvents;
        this.removedEventIds = removedEventIds;
    }

    public TimetableEvent[] getAddedEvents() {
        return addedEvents;
    }

    public TimetableEvent[] getChangedEvents() {
        return changedEvents;
    }

    public String[] getRemovedEventIds() {
        return removedEventIds;
    }

    public boolean getReloadAllEvents() {
        return reloadAllEvents;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void setReloadAllEvents(boolean reloadAllEvents) {
        this.reloadAllEvents = reloadAllEvents;
    }

    public boolean isAppropriateListener(FacesListener faceslistener) {
        return faceslistener instanceof TimetableChangeListener;
    }

    public void processListener(FacesListener faceslistener) {
        ((TimetableChangeListener) faceslistener).processTimetableChange(this);
    }
}
