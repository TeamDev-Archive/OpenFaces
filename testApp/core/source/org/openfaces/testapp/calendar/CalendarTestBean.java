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
package org.openfaces.testapp.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Darya Shumilina
 */
public class CalendarTestBean {
    private Date fromDate;
    private Date toDate;

    public CalendarTestBean() {
        fromDate = getNextMonday().getTime();
        Calendar tempToDate = getNextMonday();
        tempToDate.add(Calendar.DAY_OF_YEAR, 14);
        toDate = tempToDate.getTime();
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    private Calendar getNextMonday() {
        Calendar tempFromDate = Calendar.getInstance();
        int currentDayOfWeek = tempFromDate.get(Calendar.DAY_OF_WEEK);
        if (Calendar.MONDAY != currentDayOfWeek) {

            if (currentDayOfWeek == Calendar.TUESDAY)
                tempFromDate.add(Calendar.DAY_OF_MONTH, 6);

            if (currentDayOfWeek == Calendar.WEDNESDAY)
                tempFromDate.add(Calendar.DAY_OF_MONTH, 5);

            if (currentDayOfWeek == Calendar.THURSDAY)
                tempFromDate.add(Calendar.DAY_OF_MONTH, 4);

            if (currentDayOfWeek == Calendar.FRIDAY)
                tempFromDate.add(Calendar.DAY_OF_MONTH, 3);

            if (currentDayOfWeek == Calendar.SATURDAY)
                tempFromDate.add(Calendar.DAY_OF_MONTH, 2);

            if (currentDayOfWeek == Calendar.SUNDAY)
                tempFromDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return tempFromDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public Date getIntroductionFromDate() {
        return getDate(2006, 6, 1);
    }

    public Date getLecturesFromDate() {
        return getDate(2006, 6, 2);
    }

    public Date getPracticeFromDate() {
        return getDate(2006, 6, 13);
    }

    public Date getExaminationFromDate() {
        return getDate(2006, 6, 24);
    }

    public Date getIntroductionToDate() {
        return getDate(2006, 6, 1);
    }

    public Date getLecturesToDate() {
        return getDate(2006, 6, 12);
    }

    public Date getPracticeToDate() {
        return getDate(2006, 6, 23);
    }

    public Date getExaminationToDate() {
        return getDate(2006, 6, 30);
    }

    public void setExaminationToDate(Date examinationToDate) {
    }

    private Date getDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        return c.getTime();
    }
}