/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
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
import org.openfaces.component.timetable.EventActionEvent;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.util.Faces;

import javax.faces.event.ActionEvent;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class DayTableBean1 extends DayTableBean implements Serializable {
    private static int eventIdCounter = 0;

    List<AbstractTimetableEvent> events = new ArrayList<AbstractTimetableEvent>();
    List<AbstractTimetableEvent> reservedTimes = new ArrayList<AbstractTimetableEvent>();

    public DayTableBean1() {
        Color red1 = new Color(220, 0, 0);
        Color red2 = new Color(230, 100, 100);
        Color green = new Color(0, 180, 0);        
        Color blue = new Color(51, 102, 255);
        Color orange = new Color(247, 103, 24);        

//        today
        /*
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(6, 50), yesterdayAt(8, 0), "1111111111",
                "Instructor: Ivan Doe <br/>Fee: $40", red1));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(10, 50), todayAt(12, 30), "222222222",
                "Instructor: Gregory House <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), todayAt(10, 50), todayAt(12, 30), "333333333",
                "Instructor: Gregory House <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(10, 50), tomorrowAt(12, 30), "444444444",
                "Instructor: Gregory House <br/>Fee: $30", blue));    */
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(10, 50), yesterdayAt(12, 30), "11111111111",
                "11111111111Instructor: Gregory House <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(10, 50), yesterdayAt(12, 30), "22222222222",
                "22222222222Instructor: Gregory House <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(10, 50), yesterdayAt(12, 30), "22222222222",
                "22222222222Instructor: Gregory House <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(10, 50), yesterdayAt(12, 30), "333333333333",
                "333333333333Instructor: Gregory House <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(10, 50), yesterdayAt(12, 30), "44444444444",
                "44444444444Instructor: Gregory House <br/>Fee: $30", blue));

        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DAY_OF_YEAR, 4);
        c1.set(Calendar.HOUR_OF_DAY, 12);
        c1.set(Calendar.MINUTE, 0);
        Date d1 = c1.getTime();
        c1.add(Calendar.DAY_OF_YEAR, 1);
        Date d2 = c1.getTime();

        events.add(new TimetableEvent(generateEventId(), d1, d2, "MULTI LINE",
                "Multi line", blue));


    }

    public void postponeEventActionListener(EventActionEvent event) {
        AbstractTimetableEvent localEvent = eventById(events, event.getEventId());
        if(localEvent != null) {
            Date start = modifyDate(localEvent.getStart(), Calendar.DAY_OF_YEAR, 1);
            Date end = modifyDate(localEvent.getEnd(), Calendar.DAY_OF_YEAR, 1);
            if(isTimeAvailable(start, end)) {
                localEvent.setStart(start);
                localEvent.setEnd(end);
            }
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

    public void doLater(ActionEvent actionEvent) {
        TimetableEvent modifiedEvent = getEvent();
        if (modifiedEvent != null) {
            AbstractTimetableEvent event = eventById(events, modifiedEvent.getId());
            if (event != null) {
                Date startDate = modifyDate(event.getStart(), Calendar.HOUR_OF_DAY, 1);
                Date endDate = modifyDate(event.getEnd(), Calendar.HOUR_OF_DAY, 1);
                if(isTimeAvailable(startDate, endDate)) {
                    event.setStart(startDate);
                    event.setEnd(endDate);
                }
            }
        }
    }

    private boolean isTimeAvailable(Date startDate, Date endDate) {
        for (AbstractTimetableEvent reservedTime : reservedTimes) {
            if ((reservedTime.getStart().after(startDate) && reservedTime.getStart().before(endDate)) ||
                    (reservedTime.getStart().before(startDate) && reservedTime.getEnd().after(startDate))) {
                return false;
            }
        }
        return true;
    }

    private TimetableEvent getEvent() {
        return Faces.var("event", TimetableEvent.class);
    }



}