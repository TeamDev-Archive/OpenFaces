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
package org.openfaces.testapp.datatable;

import java.util.Locale;

/**
 * @author Darya Shumilina
 */
public class CalendarItem {

    private String calendarId;
    private Locale locale;
    private String dayStyle;

    public CalendarItem(String calendarId, Locale locale, String dayStyle) {
        this.calendarId = calendarId;
        this.locale = locale;
        this.dayStyle = dayStyle;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getDayStyle() {
        return dayStyle;
    }

    public void setDayStyle(String dayStyle) {
        this.dayStyle = dayStyle;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }
}