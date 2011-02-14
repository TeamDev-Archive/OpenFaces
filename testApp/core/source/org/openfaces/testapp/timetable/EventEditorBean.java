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
package org.openfaces.testapp.timetable;

import org.openfaces.component.timetable.TimetableEvent;
import org.openfaces.util.Faces;

import javax.faces.context.FacesContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorBean {
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    private TimetableEvent editedEvent;

    public TimetableEvent getEditedEvent() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        return (TimetableEvent) context.getExternalContext().getSessionMap().get("editedEvent");


        if (editedEvent == null) {
            String mode = Faces.requestParam("mode");
            FacesContext context = FacesContext.getCurrentInstance();
            if (mode == null)
                return (TimetableEvent) context.getExternalContext().getSessionMap().get("editedEvent");
            if (mode.equals("create")) {
                Date eventStart = Faces.requestParam("eventStart", Date.class);
                Date eventEnd = Faces.requestParam("eventEnd", Date.class);
                String resourceId = Faces.requestParam("resourceId");
                editedEvent = new TimetableEvent(null, eventStart, eventEnd, "", "", null, resourceId);
            } else {
                String eventId = Faces.requestParam("eventId");
                TimeTableBean timetableBean = getTimeTableBean();
                editedEvent = (TimetableEvent) timetableBean.eventById(timetableBean.getEvents(), eventId).clone();
            }
            context.getExternalContext().getSessionMap().put("editedEvent", editedEvent);
        }
        return editedEvent;
    }

    public String getStartTime() {
        return TIME_FORMAT.format(getEditedEvent().getStart());
    }

    public void setStartTime(String startTime) {
        Date date = getEditedEvent().getStart();
        Date newDate = parseAndUpdateTimeFields(startTime, date);
        getEditedEvent().setStart(newDate);
    }

    public String getEndTime() {
        return TIME_FORMAT.format(getEditedEvent().getEnd());
    }

    public void setEndTime(String endTime) {
        Date date = getEditedEvent().getEnd();
        Date newDate = parseAndUpdateTimeFields(endTime, date);
        getEditedEvent().setEnd(newDate);
    }

    private Date parseAndUpdateTimeFields(String startTime, Date date) {
        Date time;
        try {
            time = TIME_FORMAT.parse(startTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar srcCalendar = new GregorianCalendar();
        srcCalendar.setTime(time);
        Calendar destCalendar = new GregorianCalendar();
        destCalendar.setTime(date);
        destCalendar.set(Calendar.HOUR_OF_DAY, srcCalendar.get(Calendar.HOUR_OF_DAY));
        destCalendar.set(Calendar.MINUTE, srcCalendar.get(Calendar.MINUTE));
        return destCalendar.getTime();
    }

    public boolean isRemoveAllowed() {
        return getEditedEvent().getId() != null;
    }


    private TimeTableBean getTimeTableBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (TimeTableBean) context.getApplication().createValueBinding("#{TimeTableBean}").getValue(context);
    }


    public String saveEvent() {
        TimetableEvent editedEvent = getEditedEvent();
        String eventId = editedEvent.getId();

        TimeTableBean timeTableBean = getTimeTableBean();
        if (eventId != null) {
            timeTableBean.updateEvent(timeTableBean.events, editedEvent);
        } else {
            timeTableBean.addEvent(timeTableBean.events, editedEvent);
        }
        return "dayTablePage";
    }

    public String cancelEditing() {
        return "dayTablePage";
    }

    public String removeEvent() {
        TimeTableBean timeTableBean = getTimeTableBean();
        timeTableBean.removeEvent(timeTableBean.events, getEditedEvent().getId());
        return "dayTablePage";
    }


}
