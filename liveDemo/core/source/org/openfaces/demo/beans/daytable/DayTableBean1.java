/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.demo.beans.daytable;

import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.component.timetable.EventActionEvent;
import org.openfaces.component.timetable.ReservedTimeEvent;
import org.openfaces.util.FacesUtil;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class DayTableBean1 extends DayTableBean implements Serializable {
    private static int eventIdCounter = 0;

    List<AbstractTimetableEvent> events = new ArrayList<AbstractTimetableEvent>();

    public DayTableBean1() {
        Color red1 = new Color(220, 0, 0);
        Color red2 = new Color(230, 100, 100);
        Color green = new Color(0, 180, 0);        
        Color blue = new Color(51, 102, 255);
        Color orange = new Color(247, 103, 24);        

//        today
        events.add(new TimetableEvent(generateEventId(), todayAt(6, 50), todayAt(8, 0), "Yoga, <i>Level 1</i>",
                "Instructor: Ivan Doe <br/>Fee: $40", red1));
        events.add(new TimetableEvent(generateEventId(), todayAt(10, 50), todayAt(12, 0), "<b>Power Yoga, <i>Level 1</i></b>",
                "Instructor: Gregory House <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), todayAt(13, 0), todayAt(14, 55), "Yoga, <i>Level 2</i>",
                "Instructor: Melany Scott <br/>Fee: $25", red1));
        events.add(new TimetableEvent(generateEventId(), todayAt(15, 30), todayAt(17, 30), "Intro to Yoga",
                "Instructor: Tony Bricks <br/>Fee: Free", orange));
        events.add(new TimetableEvent(generateEventId(), todayAt(17, 55), todayAt(19, 25), "<b>Gentle Yoga, <i>Level 1</i></b>",
                "Instructor: Alex West <br/>Fee: $30", blue));
        events.add(new ReservedTimeEvent(generateEventId(), null, todayAt(19, 45), todayAt(20, 15)));
        events.add(new TimetableEvent(generateEventId(), todayAt(21, 40), todayAt(23, 30), "<b>Meditation</b>",
                "Instructor: Gregory House <br/>Fee: $20", green));

        //yesterday
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(7, 0), yesterdayAt(8, 20), "Yoga, <i>Level 1</i>",
                "Instructor: Ivan Doe <br/>Fee: $40", red1));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(9, 0), yesterdayAt(11, 30), "<b>Meditation</b>",
                "Instructor: Tony Bricks <br/>Fee: $20", green));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(13, 0), yesterdayAt(14, 55), "Yoga, <i>Level 3</i>",
                "Instructor: Melany Scott <br/>Fee: $25", red2));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(19, 55), yesterdayAt(19, 25), "<b>Gentle Yoga For Those with Special Considerations</b>",
                "Instructor: Alex West <br/>Fee: $25", blue));
        events.add(new ReservedTimeEvent(generateEventId(), null, yesterdayAt(21, 5), yesterdayAt(24, 0)));

        //tomorrow
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(8, 30), tomorrowAt(11, 30), "<b>Meditation</b>",
                "Instructor: Tony Bricks <br/>Fee: $20", green));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(13, 0), tomorrowAt(14, 30), "Yoga, <i>Level 2/3</i>",
                "Instructor: Ivan Doe <br/>Fee: $40", red1));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(16, 0), tomorrowAt(17, 55), "Yoga and Meditation, <i>Level 2/3</i>",
                "Instructor: Melany Scott <br/>Fee: $45", red2));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(20, 30), tomorrowAt(22, 0), "<b>Gentle Yoga and Meditation</b>",
                "Instructor: Matt Hunt <br/>Fee: $55", blue));
    }

    public void postponeEventActionListener(EventActionEvent event) {
        AbstractTimetableEvent localEvent = eventById(events, event.getEventId());
        if(localEvent != null) {
            localEvent.setStart(modifyDate(localEvent.getStart(), Calendar.DAY_OF_YEAR, 1));
            localEvent.setEnd(modifyDate(localEvent.getEnd(), Calendar.DAY_OF_YEAR, 1));
        }
    }


    private String generateEventId() {
        return String.valueOf(eventIdCounter++);
    }

    public List<AbstractTimetableEvent> getEvents() {
        return events;
    }

    public void removeEvent(List<AbstractTimetableEvent> events, String id) {
        events.remove(eventById(events, id));
    }

    public void addEvent(List<AbstractTimetableEvent> events, TimetableEvent event) {
        event.setId(generateEventId());
        events.add(event);
    }

    public void updateEvent(List<AbstractTimetableEvent> events, TimetableEvent editedEvent) {
        TimetableEvent event = (TimetableEvent) eventById(events, editedEvent.getId());
        event.setName(editedEvent.getName());
        event.setStart(editedEvent.getStart());
        event.setEnd(editedEvent.getEnd());
        event.setDescription(editedEvent.getDescription());
        event.setResourceId(editedEvent.getResourceId());
        event.setColor(editedEvent.getColor());

    }

    public void processTimetableChanges(TimetableChangeEvent tce) {
        TimetableEvent[] addedEvents = tce.getAddedEvents();
        for (TimetableEvent event : addedEvents) {
            addEvent(events, event);
        }

        TimetableEvent[] editedEvents = tce.getChangedEvents();
        for (TimetableEvent event : editedEvents) {
            updateEvent(events, event);
        }

        String[] removedEventIds = tce.getRemovedEventIds();
        for (String eventId : removedEventIds) {
            removeEvent(events, eventId);
        }
    }

    public void doLater() {
        TimetableEvent modifiedEvent = getEvent();
        if (modifiedEvent != null) {
            AbstractTimetableEvent event = eventById(events, modifiedEvent.getId());
            if (event != null) {
                 event.setStart(modifyDate(event.getStart(), Calendar.HOUR_OF_DAY, 1));
                 event.setEnd(modifyDate(event.getEnd(), Calendar.HOUR_OF_DAY, 1));
            }
        }
    }

    private TimetableEvent getEvent() {
        return (TimetableEvent) FacesUtil.getRequestMapValue("event");
    }
}