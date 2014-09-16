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
package org.openfaces.richfaces.demo.beans;

import javax.faces.event.AjaxBehaviorEvent;
import org.openfaces.component.timetable.AbstractTimetableEvent;
import org.openfaces.component.timetable.TimetableChangeEvent;
import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.component.timetable.EventActionEvent;
import org.openfaces.util.Faces;
import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.text.DateFormat;
import java.text.DateFormatSymbols;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

public class DayTableBean implements CalendarDataModel, Serializable {

	private static final long MS_IN_ONE_DAY = 1000 * 60 * 60 * 24;

    private static int eventIdCounter = 0;

    List<AbstractTimetableEvent> events = new ArrayList<AbstractTimetableEvent>();
    List<Task> tasks = new ArrayList<Task>();
    private TaskDateConverter taskDateConverter = new TaskDateConverter();
    private Task currentlyEditedTask;
    private Task selectedTask;
    private Task cachedTask;

    private boolean summaryColumnRendered = true;
    private boolean startTimeColumnRendered = true;
    private boolean endTimeColumnRendered = true;
    private boolean descriptionColumnRendered = true;

    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    public DayTableBean() {
        Color red1 = new Color(220, 0, 0);
        Color red2 = new Color(230, 100, 100);
        Color green = new Color(0, 180, 0);
        Color blue = new Color(51, 102, 255);
        Color orange = new Color(247, 103, 24);

//        today
        events.add(new TimetableEvent(generateEventId(), todayAt(6, 50), todayAt(8, 0), "Yoga, Level 1",
                "Instructor: Ivan Doe <br/>Fee: $40", red1));
        events.add(new TimetableEvent(generateEventId(), todayAt(10, 50), todayAt(12, 0), "Power Yoga, Level 1",
                "Instructor: Gregory House <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), todayAt(13, 0), todayAt(14, 55), "Yoga, Level 2",
                "Instructor: Melany Scott <br/>Fee: $25", red1));
        events.add(new TimetableEvent(generateEventId(), todayAt(15, 30), todayAt(17, 30), "Intro to Yoga",
                "Instructor: Tony Bricks <br/>Fee: Free", orange));
        events.add(new TimetableEvent(generateEventId(), todayAt(17, 55), todayAt(19, 25), "Gentle Yoga, Level 1",
                "Instructor: Alex West <br/>Fee: $30", blue));
        events.add(new TimetableEvent(generateEventId(), todayAt(21, 40), todayAt(23, 30), "Meditation",
                "Instructor: Gregory House <br/>Fee: $20", green));

        //yesterday
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(7, 0), yesterdayAt(8, 20), "Yoga, Level 1",
                "Instructor: Ivan Doe <br/>Fee: $40", red1));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(9, 0), yesterdayAt(11, 30), "Meditation",
                "Instructor: Tony Bricks <br/>Fee: $20", green));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(13, 0), yesterdayAt(14, 55), "Yoga, Level 3",
                "Instructor: Melany Scott <br/>Fee: $25", red2));
        events.add(new TimetableEvent(generateEventId(), yesterdayAt(19, 55), yesterdayAt(19, 25), "Gentle Yoga For Those with Special Considerations",
                "Instructor: Alex West <br/>Fee: $25", blue));

        //tomorrow
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(8, 30), tomorrowAt(11, 30), "Meditation",
                "Instructor: Tony Bricks <br/>Fee: $20", green));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(13, 0), tomorrowAt(14, 30), "Yoga, Level 2/3",
                "Instructor: Ivan Doe <br/>Fee: $40", red1));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(16, 0), tomorrowAt(17, 55), "Yoga and Meditation, Level 2/3",
                "Instructor: Melany Scott <br/>Fee: $45", red2));
        events.add(new TimetableEvent(generateEventId(), tomorrowAt(20, 30), tomorrowAt(22, 0), "Gentle Yoga and Meditation",
                "Instructor: Matt Hunt <br/>Fee: $55", blue));

        for (AbstractTimetableEvent event : events) {
            TimetableEvent timetableEvent = (TimetableEvent) event;
            Task task = new Task(timetableEvent.getName(),
                    timetableEvent.getStart(), timetableEvent.getEnd(),
                    timetableEvent.getDescription());
            tasks.add(task);
        }
    }

    public void postponeEventActionListener(EventActionEvent event) {
        AbstractTimetableEvent localEvent = eventById(events, event.getEventId());
        if (localEvent != null) {
            Date start = modifyDate(localEvent.getStart(), Calendar.DAY_OF_YEAR, 1);
            Date end = modifyDate(localEvent.getEnd(), Calendar.DAY_OF_YEAR, 1);
            if (isTimeAvailable(start, end)) {
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

    public void nextDayEventActionListener(ActionEvent event) {
    	date = new Date(date.getTime() + MS_IN_ONE_DAY);
    }

    public void prevDayEventActionListener(ActionEvent event) {
    	date = new Date(date.getTime() - MS_IN_ONE_DAY);
    }
    
	public void doLater(AjaxBehaviorEvent event) {
        TimetableEvent modifiedEvent = getEvent();
        if (modifiedEvent != null) {
            AbstractTimetableEvent event = eventById(events, modifiedEvent.getId());
            if (event != null) {
                Date startDate = modifyDate(event.getStart(), Calendar.HOUR_OF_DAY, 1);
                Date endDate = modifyDate(event.getEnd(), Calendar.HOUR_OF_DAY, 1);
                if (isTimeAvailable(startDate, endDate)) {
                    event.setStart(startDate);
                    event.setEnd(endDate);
                }
            }
        }
    }

    private boolean isTimeAvailable(Date startDate, Date endDate) {
        return true;
    }

    private TimetableEvent getEvent() {
        return (TimetableEvent) Faces.var("event");
    }

    private Date date = new Date();

    private static String getDateChangeEventSource() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, String> paramMap = externalContext.getRequestParameterMap();
        return paramMap.get("dateChangeEventSource");
    }

    public Date getCalendarDate() {
        return date;
    }

    public void setCalendarDate(Date date) {
        if ("calendar".equals(getDateChangeEventSource()))
            this.date = date;
    }

    public Date getDayTableDate() {
        return date;
    }

    public void setDayTableDate(Date date) {
        if ("dayTable".equals(getDateChangeEventSource()))
            this.date = date;
    }
    
    public String getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
    	return new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
    }

    public int getDayOfMonth() {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public String getMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
    	return new DateFormatSymbols().getMonths()[cal.get(Calendar.MONTH)];
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

    protected AbstractTimetableEvent eventById(List<AbstractTimetableEvent> events, String eventId) {
        for (AbstractTimetableEvent event : events) {
            if (event.getId().equals(eventId))
                return event;
        }
        return null;
    }

    public Task getCachedTask() {
        return cachedTask;
    }

    public void setCachedTask(Task cachedTask) {
        this.cachedTask = cachedTask;
    }

    private static final class CalendarItemStyle implements CalendarDataModelItem {
    	
    	private static final String DAY_WITH_EVENT_CLASS_NAME = "calendar-day-with-event";
    	
    	private final String styleClass;
    	
    	private CalendarItemStyle(boolean hasEvents) {
    		if (hasEvents)
   				styleClass = DAY_WITH_EVENT_CLASS_NAME;
    		else
   				styleClass = null;
    	}

        public boolean isEnabled() {
            return true;
        }

        public boolean hasToolTip() {
            return false;
        }

        public Object getToolTip() {
            return null;
        }

        public String getStyleClass() {
            return styleClass;
        }

        public int getDay() {
            return 0;
        }

        public Object getData() {
            return null;
        }
    };

    public CalendarDataModelItem[] getData(Date[] dates) {
        HashSet<Long> daysWithEvents = new HashSet<Long>();
        for (int i = 0; i != events.size(); i++) {
            AbstractTimetableEvent event = events.get(i);
            long date = event.getStart().getTime() / MS_IN_ONE_DAY;
            daysWithEvents.add(date);
        }

        CalendarDataModelItem[] items = new CalendarDataModelItem[dates.length];
        for (int i = 0; i != dates.length; i++) {
            long date = dates[i].getTime() / MS_IN_ONE_DAY + 1;
            items[i] = new CalendarItemStyle(daysWithEvents.contains(date));
        }

        return items;
    }

    public Object getToolTip(Date date) {
        return null;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Converter getTaskDateConverter() {
        return taskDateConverter;
    }

    public boolean isEditingRequest() {
        return currentlyEditedTask != null;
    }

    public Task getSelectedTask() {
        if (selectedTask == null)
            selectedTask = tasks.get(0);
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public Task getCurrentlyEditedTask() {
        return currentlyEditedTask;
    }

    public void setCurrentlyEditedTask(Task currentlyEditedTask) {
        this.currentlyEditedTask = currentlyEditedTask;
    }

    public boolean isEditingThisRow() {
        Task task = fetchTaskVariable();
        return task.equals(currentlyEditedTask);
    }

    public boolean isSummaryColumnRendered() {
        return summaryColumnRendered;
    }

    public void setSummaryColumnRendered(boolean summaryColumnRendered) {
        this.summaryColumnRendered = summaryColumnRendered;
    }

    public boolean isStartTimeColumnRendered() {
        return startTimeColumnRendered;
    }

    public void setStartTimeColumnRendered(boolean startTimeColumnRendered) {
        this.startTimeColumnRendered = startTimeColumnRendered;
    }

    public boolean isEndTimeColumnRendered() {
        return endTimeColumnRendered;
    }

    public void setEndTimeColumnRendered(boolean endTimeColumnRendered) {
        this.endTimeColumnRendered = endTimeColumnRendered;
    }

    public boolean isDescriptionColumnRendered() {
        return descriptionColumnRendered;
    }

    public void setDescriptionColumnRendered(boolean descriptionColumnRendered) {
        this.descriptionColumnRendered = descriptionColumnRendered;
    }

    public void hideSummaryColumn() {
        summaryColumnRendered = false;
    }

    public void showSummaryColumn() {
        summaryColumnRendered = true;
    }

    public void hideStartTimeColumn() {
        startTimeColumnRendered = false;
    }

    public void showStartTimeColumn() {
        startTimeColumnRendered = true;
    }

    public void hideEndTimeColumn() {
        endTimeColumnRendered = false;
    }

    public void showEndTimeColumn() {
        endTimeColumnRendered = true;
    }

    public void hideDescriptionColumn() {
        descriptionColumnRendered = false;
    }

    public void showDescriptionColumn() {
        descriptionColumnRendered = true;
    }

    public Locale getLocale() {
        return locale;
    }

    private Task fetchTaskVariable() {
        return (Task) Faces.var("task");
    }

    public void editTask(ActionEvent event) {
        cachedTask = new Task(selectedTask.getName(), selectedTask.getStartDate(), selectedTask.getEndDate(), selectedTask.getDescription());
        currentlyEditedTask = selectedTask;
    }

    public void deleteTask(ActionEvent event) {
        tasks.remove(selectedTask);
        selectedTask = tasks.get(0);
    }

    public void addNewTask(ActionEvent event) {
        Task newTask = new Task("", new Date(), new Date(), "");
        tasks.add(newTask);
        currentlyEditedTask = newTask;
        selectedTask = newTask;
    }

    public void saveChanges(ActionEvent event) {
        currentlyEditedTask = null;
    }

    public void cancelEditing(ActionEvent event){
        currentlyEditedTask.setName(cachedTask.getName());
        currentlyEditedTask.setStartDate(cachedTask.getStartDate());
        currentlyEditedTask.setEndDate(cachedTask.getEndDate());
        currentlyEditedTask.setDescription(cachedTask.getDescription());
        currentlyEditedTask = null;
    }



    private class TaskDateConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return null;
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            Date receivedDate = (Date) value;

            return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale).format(receivedDate);
        }
    }
}