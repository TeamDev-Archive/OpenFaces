/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.daytable;

import org.openfaces.util.Faces;
import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.component.timetable.TimetableResource;
import org.openfaces.component.timetable.ReservedTimeEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.Color;

/**
 * @author Dmitry Pikhulya
 */
public class DayTableBean2 extends DayTableBean implements Serializable {
    private static int eventIdCounter = 0;
   
    List<AbstractTimetableEvent> events = new ArrayList<AbstractTimetableEvent>();
    private List<TimetableResource> resources = new ArrayList<TimetableResource>();

    public DayTableBean2() {
        Color green = new Color(41, 142, 1);
        Color blue = new Color(2, 105, 220);
        Color orange = new Color(232, 65, 2);

        Resource andrew = new Resource("Andrew", "#0269dc");
        Resource lucie = new Resource("Lucie", "#df4c11");
        Resource alex = new Resource("Alex", "#298e01");

//        today
        events.add(new TimetableEvent(generateEventId(), todayAt(9, 0), todayAt(19, 30), "WORK", "Usual day, nothing special", blue, andrew.getId()));
        events.add(new TimetableEvent(generateEventId(), todayAt(20, 0), todayAt(23, 0), "ALEX BIRTHDAY PARTY", "Dress-code: Harry Potter", blue, andrew.getId()));

        events.add(new TimetableEvent(generateEventId(), todayAt(7, 0), todayAt(9, 0), "SUNRISE AT THE ROOF", "Don't forget the camera", orange, lucie.getId()));
        events.add(new TimetableEvent(generateEventId(), todayAt(9, 30), todayAt(13, 0), "PAINTING", "...", orange, lucie.getId()));
        events.add(new TimetableEvent(generateEventId(), todayAt(14, 0), todayAt(19, 30), "LOOKING FOR A PRESENT", "Alex loves sweet honey", orange, lucie.getId()));
        events.add(new TimetableEvent(generateEventId(), todayAt(20, 0), todayAt(23, 0), "ALEX BIRTHDAY PARTY", "Dress-code: Harry Potter", orange, lucie.getId()));

        events.add(new TimetableEvent(generateEventId(), todayAt(6, 0), todayAt(20, 0), "PREPARING FOR BIRTHDAY", "Oh my God!", green, alex.getId()));
        events.add(new TimetableEvent(generateEventId(), todayAt(20, 0), todayAt(23, 0), "MY BIRTHDAY", "There will be a lot of magic wands", green, alex.getId()));

//        yesterday
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(9, 0), yesterdayAt(15, 30), "WORK", "Short day, will visit a doctor", blue, andrew.getId()));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(16, 30), yesterdayAt(17, 30), "DOCTOR CONSULTATION", "Once-a-year visit", blue, andrew.getId()));
        events.add(new ReservedTimeEvent(generateEventId(), andrew.getId(), yesterdayAt(18, 30), yesterdayAt(19, 30)));

        events.add(new TimetableEvent(generateEventId(), yesterdayAt(10, 0), yesterdayAt(12, 30), "IREN FOTOSET", "Cute Iren with mother.<br/>Iren's mother phone: +1 (555) 987 98 12", orange, lucie.getId()));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(16, 30), yesterdayAt(19, 0), "NATIONAL GEOGRAPHIC", "Show photos to NG and close the deal.", orange, lucie.getId()));

        events.add(new TimetableEvent(generateEventId(), yesterdayAt(9, 0), yesterdayAt(11, 30), "BUY FOOD", "Beer, bread and circuses", green, alex.getId()));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(13, 30), yesterdayAt(21, 0), "VISIT PARENTS", "Mom asked for my old cell phone.", green, alex.getId()));

//        tomorrow
        events.add(new ReservedTimeEvent(generateEventId(), andrew.getId(), tomorrowAt(6, 0), tomorrowAt(8, 30)));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(9, 0), tomorrowAt(19, 30), "WORK", "As usual", blue, andrew.getId()));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(20, 30), tomorrowAt(23, 30), "WENDY", "Table is reserved at Potato House", blue, andrew.getId()));

        events.add(new ReservedTimeEvent(generateEventId(), lucie.getId(), tomorrowAt(6, 0), tomorrowAt(12, 30)));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(14, 0), tomorrowAt(15, 0), "BRUCE ECKEL PRESS CONFERENCE", "Take 50D with EF 28-300", orange, lucie.getId()));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(17, 30), tomorrowAt(20, 0), "SIGN CONTEST", "", orange, lucie.getId()));

        events.add(new TimetableEvent(generateEventId(), tomorrowAt(8, 30), tomorrowAt(11, 30), "CLEAN HOUSE FROM GUESTS", "Andrew will go the first", green, alex.getId()));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(13, 0), tomorrowAt(16, 0), "CHECK NEW KITE VIDEO", "Lucie promised to give me the \"Lines\".", green, alex.getId()));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(19, 30), tomorrowAt(21, 0), "COUNTER-STRIKE", "The Spawn team wants to repeat.", green, alex.getId()));

        resources.add(new TimetableResource(andrew, andrew.getId(), andrew.getName()));
        resources.add(new TimetableResource(lucie, lucie.getId(), lucie.getName()));
        resources.add(new TimetableResource(alex, alex.getId(), alex.getName()));
    }

    private String generateEventId() {
        return String.valueOf(eventIdCounter++);
    }

    public List<AbstractTimetableEvent> getEvents() {
        Date startTime = Faces.var("startTime", Date.class);
        Date endTime = Faces.var("endTime", Date.class);

        List<AbstractTimetableEvent> result = retrieveEventsForPeriod(startTime, endTime);
        return result;
    }

    private List<AbstractTimetableEvent> retrieveEventsForPeriod(Date startTime, Date endTime) {
        List<AbstractTimetableEvent> result = new ArrayList<AbstractTimetableEvent>();
        for (AbstractTimetableEvent event : events) {
            if (event.getStart().before(endTime) && event.getEnd().after(startTime))
                result.add(event);
        }
        return result;
    }

    public List<TimetableResource> getResources() {
        return resources;
    }

    public void removeEvent(List<AbstractTimetableEvent> events, String id) {
        events.remove(eventById(events, id));
    }

    public void addEvent(List<AbstractTimetableEvent> events, TimetableEvent event) {
        event.setId(generateEventId());
        if(event.getColor() == null) {
            event.setColor(new Color(0, 0x6e, 0xbb));
        }
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

    public void remove() {
        TimetableEvent event = getEvent();
        removeEvent(events, event.getId());
    }

    private TimetableEvent getEvent() {
        return Faces.var("event", TimetableEvent.class);
    }

}
