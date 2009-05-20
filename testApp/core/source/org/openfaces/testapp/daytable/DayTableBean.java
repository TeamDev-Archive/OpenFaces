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

package org.openfaces.testapp.daytable;

import org.openfaces.component.timetable.AbstractTimetableEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * @author Alexey Chystoprudov
 */
public class DayTableBean {
    private Date date = new Date();

    public Map<Date, String> getDateSuffixMap() {
        return new DateToSuffixMap();
    }

    public void previousDay() {
        setDate(modifyDate(getDate(), Calendar.DAY_OF_YEAR, -1));
    }

    public void nextDay() {
        setDate(modifyDate(getDate(), Calendar.DAY_OF_YEAR, 1));
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}