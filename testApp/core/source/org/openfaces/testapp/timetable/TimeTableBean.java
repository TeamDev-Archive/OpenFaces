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
package org.openfaces.testapp.timetable;

import org.openfaces.util.Faces;
import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.component.timetable.EventActionEvent;
import org.openfaces.component.timetable.ReservedTimeEvent;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.component.timetable.TimetableResource;
import org.openfaces.renderkit.cssparser.CSSUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Dmitry Pikhulya
 */
public class TimeTableBean {

    Logger logger = Logger.getLogger(TimeTableBean.class.getName());
    
    private static int _eventIdCounter = 0;

    List<AbstractTimetableEvent> events = new ArrayList<AbstractTimetableEvent>();
    List<AbstractTimetableEvent> events1 = events;
    List<AbstractTimetableEvent> events2 = events;
    private List<AbstractTimetableEvent> simpleEvents = new ArrayList<AbstractTimetableEvent>();
    private List<TimetableResource> resources = new ArrayList<TimetableResource>();

    private static Date todayTime(int hour, int minute) {
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.HOUR_OF_DAY, hour);
        c1.set(Calendar.MINUTE, minute);
        return c1.getTime();
    }

    private static Date tomorrowTime(int hour, int minute) {
        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DAY_OF_YEAR, 1);
        c1.set(Calendar.HOUR_OF_DAY, hour);
        c1.set(Calendar.MINUTE, minute);
        return c1.getTime();
    }

    private static Date yesterdayTime(int hour, int minute) {
        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DAY_OF_YEAR, -1);
        c1.set(Calendar.HOUR_OF_DAY, hour);
        c1.set(Calendar.MINUTE, minute);
        return c1.getTime();
    }

    public TimeTableBean() {
        Color red1 = new Color(220, 0, 0);
        Color red2 = new Color(230, 100, 100);
        Color green = new Color(0, 180, 0);
        Color purple = new Color(176, 35, 174);
        Color orange = new Color(247, 103, 24);

        TimetableResource r1 = new TimetableResource("Indy Flaves");
        TimetableResource r2 = new TimetableResource("Daniel Gavin");
        TimetableResource r3 = new TimetableResource("Rachel Zoe");
        TimetableResource r4 = new TimetableResource("Mary Ann Valdes");

        events.add(new TimetableEvent(generateEventId(), todayTime(8, 30), todayTime(10, 0), "Event 1 <i>(italic text)</i> <br/> after &lt;br&gt;", "This is a <span style='color: red'>description</span> of the first event.", null, r1.getId()));
        events.add(new TimetableEvent(generateEventId(), todayTime(11, 15), todayTime(12, 45), "Event 2", "This is a description of the second event. It starts at 12:15, and ends at 13:45. Duration is one and a half hours.", green, r2.getId()));
        events.add(new TimetableEvent(generateEventId(), todayTime(14, 0), todayTime(17, 0), "Event 2", "This is a description of the third event.", orange, r2.getId()));
        events.add(new TimetableEvent(generateEventId(), todayTime(5, 0), todayTime(6, 0), "Event 3", "This is a description of the third event. " +
                "big text big text big text big text big text big text big text big text big text big text big text big text " +
                "big text big text big text big text big text big text big text big text big text big text big text big text " +
                "big text big text big text big text big text big text big text big text big text big text big text big text " +
                "big text big text big text big text big text big text big text big text ", null, r3.getId()));
        events.add(new TimetableEvent(generateEventId(), todayTime(19, 0), todayTime(20, 30), "Event 4", "This is a description of the fourth task.", null, r1.getId()));
        events.add(new TimetableEvent(generateEventId(), todayTime(16, 0), todayTime(20, 0), "Event 8", "This is a description of the eighth task.", purple, r4.getId()));

        events.add(new TimetableEvent(generateEventId(), yesterdayTime(5, 0), yesterdayTime(7, 0), "Event 5", "Какой-то ивэнт", null, r1.getId()));
        events.add(new TimetableEvent(generateEventId(), yesterdayTime(9, 0), yesterdayTime(9, 45), "Event 6", "Какой-то ивэнт", green, r3.getId()));
        events.add(new TimetableEvent(generateEventId(), yesterdayTime(13, 15), yesterdayTime(13, 45), "OpenFaces meeting (кипукит митинг)", "Вчерашний митинг по кипукиту", red2));
        events.add(new TimetableEvent(generateEventId(), todayTime(13, 15), todayTime(13, 45), "OpenFaces meeting", "Сегодняшний митинг по кипукиту", red1));
        events.add(new TimetableEvent(generateEventId(), tomorrowTime(13, 15), tomorrowTime(13, 45), "OpenFaces meeting", "Завтрашний митинг по кипукиту", red1));
        events.add(new TimetableEvent(generateEventId(), tomorrowTime(3, 15), tomorrowTime(3, 45), "Event 7", "Ещё ивэнт", red1, r4.getId()));
        events.add(new TimetableEvent(generateEventId(), tomorrowTime(10, 30), tomorrowTime(11, 0), "Ещё один ивэнт на завтра", "Какой-нибудь дескрипшен", null, r2.getId()));
        events.add(new TimetableEvent(generateEventId(), todayTime(7, 45), todayTime(9, 30), "Ивэнт на сегодня", "Какой-нибудь дескрипшен", null, r3.getId()));
        events.add(new ReservedTimeEvent(generateEventId(), r3.getId(), todayTime(14, 0), todayTime(16, 10)));
        events.add(new ReservedTimeEvent(generateEventId(), r2.getId(), todayTime(15, 30), todayTime(16, 30)));

        for (AbstractTimetableEvent event : events) {
            AbstractTimetableEvent simpleEvent = (AbstractTimetableEvent) event.clone();
            simpleEvent.setResourceId(null);
            simpleEvents.add(simpleEvent);
        }

        resources.add(r1);
        resources.add(r2);
        resources.add(r3);
        resources.add(r4);
    }

    private String generateEventId() {
        return String.valueOf(_eventIdCounter++);
    }

    public List getEmptyList() {
        return Collections.EMPTY_LIST;
    }

    public List<AbstractTimetableEvent> getEvents() {
        Date startTime = (Date) Faces.var("startTime");
        Date endTime = (Date) Faces.var("endTime");
        if (startTime == null || endTime == null)
            return events;
        List<AbstractTimetableEvent> result = new ArrayList<AbstractTimetableEvent>();
        for (AbstractTimetableEvent event : events) {
            if (event.getStart().before(endTime) && event.getEnd().after(startTime))
                result.add(event);
        }
        return result;
    }

    public List<AbstractTimetableEvent> getSimpleEvents() {
        return simpleEvents;
    }

    public List<TimetableResource> getResources() {
        return resources;
    }

    public String editEvent() {
        String mode = Faces.requestParam("mode");
        TimetableEvent editedEvent;
        if (mode.equals("create")) {
            Date eventStart = Faces.requestParam("eventStart", Date.class);
            Date eventEnd = Faces.requestParam("eventEnd", Date.class);
            String resourceId = Faces.requestParam("resourceId");
            editedEvent = new TimetableEvent(null, eventStart, eventEnd, "", "", null, resourceId);
        } else {
            String eventId = Faces.requestParam("eventId");
            editedEvent = (TimetableEvent) eventById(events, eventId).clone();
        }

        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put("editedEvent", editedEvent);
        return "eventEditor";
    }

    public AbstractTimetableEvent eventById(List<AbstractTimetableEvent> events, String eventId) {
        for (AbstractTimetableEvent event : events) {
            if (event.getId().equals(eventId))
                return event;
        }
        return null;
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

    public void processTimetableChanges1(TimetableChangeEvent tce) {
        TimetableEvent[] addedEvents = tce.getAddedEvents();
        for (TimetableEvent event : addedEvents) {
            addEvent(events1, event);
        }

        TimetableEvent[] editedEvents = tce.getChangedEvents();
        for (TimetableEvent event : editedEvents) {
            updateEvent(events1, event);
        }

        String[] removedEventIds = tce.getRemovedEventIds();
        for (String eventId : removedEventIds) {
            removeEvent(events1, eventId);
        }
    }

    public void processTimetableChanges2(TimetableChangeEvent tce) {
        TimetableEvent[] addedEvents = tce.getAddedEvents();
        for (TimetableEvent event : addedEvents) {
            addEvent(events2, event);
        }

        TimetableEvent[] editedEvents = tce.getChangedEvents();
        for (TimetableEvent event : editedEvents) {
            updateEvent(events2, event);
        }

        String[] removedEventIds = tce.getRemovedEventIds();
        for (String eventId : removedEventIds) {
            removeEvent(events2, eventId);
        }
    }


    public void processSimpleTimetableChanges(TimetableChangeEvent tce) {
        TimetableEvent[] addedEvents = tce.getAddedEvents();
        for (TimetableEvent event : addedEvents) {
            addEvent(simpleEvents, event);
        }

        TimetableEvent[] editedEvents = tce.getChangedEvents();
        for (TimetableEvent event : editedEvents) {
            updateEvent(simpleEvents, event);
        }

        String[] removedEventIds = tce.getRemovedEventIds();
        for (String eventId : removedEventIds) {
            removeEvent(simpleEvents, eventId);
        }
    }


    public void customEventAction1Listener(EventActionEvent event) {
        String eventId = event.getEventId();
        logger.info("Custom event 1 has been executed for event with id: " + eventId);
    }

    public void customEventAction2Listener(EventActionEvent event) {
        String eventId = event.getEventId();
        logger.info("Custom event 2 has been executed for event with id: " + eventId);
    }

    public List<AbstractTimetableEvent> getEvents2() {
        return events2;
    }

    public void setEvents2(List<AbstractTimetableEvent> events2) {
        this.events2 = events2;
    }

    public void switchEvents2() {
        if (events2 == events)
            events2 = simpleEvents;
        else
            events2 = events;
    }

    public List<AbstractTimetableEvent> getEvents1() {
        return events1;
    }

    public void setEvents1(List<AbstractTimetableEvent> events1) {
        this.events1 = events1;
    }

    public void switchEvents1() {
        if (events1 == events)
            events1 = simpleEvents;
        else
            events1 = events;
    }

    public void changeEventColor() {
        logger.info("changeEventColor invoked");
    }

    public static class ColorConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return CSSUtil.parseColor(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            if (value == null)
                return "";
            if (value instanceof String)
                return (String) value;
            return CSSUtil.formatColor((Color) value);
        }
    }

    private static ColorConverter COLOR_CONVERTER = new ColorConverter();

    public ColorConverter getColorConverter() {
        return COLOR_CONVERTER;
    }

    public Color getEventColor1() {
        return getEventColor(events1);
    }

    public Color getEventColor2() {
        return getEventColor(events2);
    }

    public Color getEventColor(List<AbstractTimetableEvent> events) {
        TimetableEvent event = getEvent();
        return ((TimetableEvent) eventById(events, event.getId())).getColor();
    }

    public void setEventColor1(Color color) {
        setEventColor(color, events1);
    }

    public void setEventColor2(Color color) {
        setEventColor(color, events2);
    }

    public void setEventColor(Color color, List<AbstractTimetableEvent> events) {
        TimetableEvent event = getEvent();
        ((TimetableEvent) eventById(events, event.getId())).setColor(color);
    }

    private TimetableEvent getEvent() {
        return (TimetableEvent) Faces.var("event");
    }

    public void moveEvent1() {
        moveEvent(events1);
    }

    public void moveEvent() {
        moveEvent(events2);
    }

    public void moveEvent(List<AbstractTimetableEvent> events) {
        TimetableEvent event = getEvent();
        String resourceId = event.getResourceId();
        for (int i = 0; i < resources.size(); i++) {
            TimetableResource timetableResource = resources.get(i);
            if (timetableResource.getId().equals(resourceId)) {
                TimetableResource nextResource = resources.get((i + 1) % resources.size());
                eventById(events, event.getId()).setResourceId(nextResource.getId());
                return;
            }
        }
    }

    public void removeEvent1() {
        removeEvent(events1);
    }

    public void removeEvent() {
        removeEvent(events2);
    }

    public void removeEvent(List<AbstractTimetableEvent> events) {
        TimetableEvent event = getEvent();
        String resourceId = event.getResourceId();
        for (TimetableResource timetableResource : resources) {
            if (timetableResource.getId().equals(resourceId)) {
                AbstractTimetableEvent eventToRemove = eventById(events, event.getId());
                events.remove(eventToRemove);
                return;
            }
        }
    }

    private String scrollTime ="5:45";

    public String getScrollTime() {
        return scrollTime;
    }

    public void setScrollTime(String scrollTime) {
        this.scrollTime = scrollTime;
    }
}
