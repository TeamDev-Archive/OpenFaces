/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.timetable;

import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.util.Faces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Alexey Chystoprudov
 */
public class TimeTableBean {

    private static int eventIdCounter = 0;

    private List<AbstractTimetableEvent> events = new ArrayList<AbstractTimetableEvent>();
    private Date date = new Date();


    public void addNewEvent(Date start, Date end, String name){
        events.add(new TimetableEvent(generateEventId(), start, end, name, null, null, null));
    }

    public void addNewEvent(Date start, Date end, String name, String description){
        events.add(new TimetableEvent(generateEventId(), start, end, name, description, null, null));
    }

    public void addNewEvent(Date start, Date end, String name, String description, Color color){
        events.add(new TimetableEvent(generateEventId(), start, end, name, description, color, null));
    }

    public void addNewEvent(Date start, Date end, String name, String description, Color color, String resourceId){
        events.add(new TimetableEvent(generateEventId(), start, end, name, description, color, resourceId));
    }

    public void addEvent(AbstractTimetableEvent event){
        if (event.getId() == null){
            event.setId(generateEventId());
        }
        events.add(event);
    }

    public Map<Date, String> getDateSuffixMap() {
        return new DateToSuffixMap();
    }

    public void previousDay() {
        setDate(modifyDate(getDate(), Calendar.DAY_OF_YEAR, -1));
    }

    public void nextDay() {
        setDate(modifyDate(getDate(), Calendar.DAY_OF_YEAR, 1));
    }

    public List<AbstractTimetableEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AbstractTimetableEvent> events) {
        this.events = events;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void processTimetableChanges(TimetableChangeEvent tce) {
        TimetableEvent[] addedEvents = tce.getAddedEvents();
        for (TimetableEvent event : addedEvents) {
            addEvent(event);
        }

        TimetableEvent[] editedEvents = tce.getChangedEvents();
        for (TimetableEvent event : editedEvents) {
            updateEvent(event);
        }

        String[] removedEventIds = tce.getRemovedEventIds();
        for (String eventId : removedEventIds) {
            removeEvent(eventId);
        }
    }

    public void removeEvent(String id) {
        events.remove(eventById(id));
    }

    public void remove() {
        TimetableEvent event = getEvent();
        removeEvent(event.getId());
    }

    public void updateEvent(TimetableEvent editedEvent) {
        TimetableEvent event = (TimetableEvent) eventById(editedEvent.getId());
        event.setName(editedEvent.getName());
        event.setStart(editedEvent.getStart());
        event.setEnd(editedEvent.getEnd());
        event.setDescription(editedEvent.getDescription());
        event.setResourceId(editedEvent.getResourceId());
        event.setColor(editedEvent.getColor());

    }

    protected Date modifyDate(Date date, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }

    protected static Date todayAt(int hour, int minute) {
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.HOUR_OF_DAY, hour);
        c1.set(Calendar.MINUTE, minute);
        return c1.getTime();
    }

    protected static Date tomorrowAt(int hour, int minute) {
        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DAY_OF_YEAR, 1);
        c1.set(Calendar.HOUR_OF_DAY, hour);
        c1.set(Calendar.MINUTE, minute);
        return c1.getTime();
    }

    protected static Date yesterdayAt(int hour, int minute) {
        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DAY_OF_YEAR, -1);
        c1.set(Calendar.HOUR_OF_DAY, hour);
        c1.set(Calendar.MINUTE, minute);
        return c1.getTime();
    }

    protected static Date thisMonth(int date){
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.DAY_OF_MONTH, date);
        c1.set(Calendar.HOUR_OF_DAY, 12);
        c1.set(Calendar.MINUTE, 0);
        return c1.getTime();
    }

    protected AbstractTimetableEvent eventById(String eventId) {
        for (AbstractTimetableEvent event : events) {
            if (event.getId().equals(eventId))
                return event;
        }
        return null;
    }

    protected String generateEventId() {
        return String.valueOf(eventIdCounter++);
    }

    protected List<AbstractTimetableEvent> retrieveEventsForPeriod(Date startTime, Date endTime) {
        List<AbstractTimetableEvent> result = new ArrayList<AbstractTimetableEvent>();
        for (AbstractTimetableEvent event : events) {
            if (event.getStart().before(endTime) && event.getEnd().after(startTime))
                result.add(event);
        }
        return result;
    }

    private TimetableEvent getEvent() {
        return Faces.var("event", TimetableEvent.class);
    }
}