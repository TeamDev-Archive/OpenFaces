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

package org.openfaces.testapp.calendar;

import org.openfaces.component.calendar.SimpleDateRange;

import javax.faces.event.ValueChangeEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Kharchenko
 */
public class CalendarBean {

    private Logger logger = Logger.getLogger(CalendarBean.class.getName());

    private List<Locale> locales;
    private int selectedIndex;
    private Date testSelectedDateChange;
    private Date todayDate = Calendar.getInstance().getTime();

    private Date noneDate;
    private Date fromDate;
    private Date toDate;
    private Date styledCalendarValue;

    private Date testDateRangesValue;
    private Date testSelectedMonthChange;
    private int valueChangeListenerCounter = 0;
    public static boolean testValueChangeListener;

    private Date dateWithTime1;
    private Date dateWithTime2;

    private TimeZone eetTimeZone = TimeZone.getTimeZone("EET");

    public CalendarBean() {
        locales = new ArrayList<Locale>();
        locales.add(Locale.ENGLISH);
        locales.add(Locale.FRANCE);
        locales.add(Locale.GERMANY);
        locales.add(new Locale("ru"));
        locales.add(new Locale("ar"));
        locales.add(new Locale("az"));

        Calendar c = Calendar.getInstance();
        c.set(2007, 11, 3);
        testSelectedDateChange = c.getTime();

        Calendar from = Calendar.getInstance();
        from.set(2007, 10, 3);
        fromDate = from.getTime();

        Calendar styled = Calendar.getInstance();
        styled.set(2007, 10, 12);
        styledCalendarValue = styled.getTime();

        Calendar to = Calendar.getInstance();
        to.set(2007, 10, 11);
        toDate = to.getTime();

        Calendar testDateRanges = Calendar.getInstance();
        testDateRanges.set(2007, 10, 7);
        testDateRangesValue = testDateRanges.getTime();

        Calendar c1 = Calendar.getInstance();
        c1.set(2007, 11, 3);
        testSelectedMonthChange = c1.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(eetTimeZone);
        try {
            dateWithTime1 = dateFormat.parse("12:34:56");
            dateWithTime2 = dateFormat.parse("12:34:56");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void dateChanged(ValueChangeEvent event) {
        logger.info("--- Old Value: " + event.getOldValue());
        logger.info("--- New Value: " + event.getNewValue());
    }

    public Date getFrom() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -10);
        return instance.getTime();
    }

    public Date getTo() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        return c.getTime();
    }

    public Date getFrom2() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -5);
        return instance.getTime();
    }

    public Date getTo2() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 5);
        return c.getTime();
    }

    public Collection<SimpleDateRange> getRanges() {
        SimpleDateRange range = new SimpleDateRange();
        range.setFromDate(getFrom2());
        range.setToDate(getTo2());
        ArrayList<SimpleDateRange> arrayList = new ArrayList<SimpleDateRange>();
        arrayList.add(range);
        return arrayList;
    }

    public Collection getEmptyRanges() {
        return null;
    }

    public Collection getEmptyRanges2() {
        return Collections.EMPTY_LIST;
    }

    public Locale getRussianLocale() {
        return new Locale("ru");
    }

    public Locale getFrenchLocale() {
        return Locale.FRANCE;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public Locale getSelectedLocale() {
        return locales.get(selectedIndex);
    }

    public List<String> getLocaleNames() {
        List<String> res = new ArrayList<String>();
        for (Locale locale : locales) {
            res.add(locale.toString());
        }
        return res;
    }

    public Date getDefaultDate() {
        Random randYear = new Random();
        List<Integer> dates = new ArrayList<Integer>();
        for (int i = 1970; i < 2100; i++) {
            dates.add(i);
        }
        TimeZone timeZone = new SimpleTimeZone(2, "uk");
        Calendar currentDate = Calendar.getInstance(timeZone, new Locale("uk"));
        Integer currentYear = dates.get(randYear.nextInt(dates.size() - 1));
        currentDate.set(currentYear, 11, 25, 23, 59, 0);
        return currentDate.getTime();
    }

    public void setDefaultDate(Date defaultDate) {
    }


    public Date getTestSelectedDateChange() {
        return testSelectedDateChange;
    }

    public void setTestSelectedDateChange(Date testSelectedDateChange) {
        this.testSelectedDateChange = testSelectedDateChange;
    }

    public String getTestSelectedDateChangeString() {
        SimpleDateFormat testDate = new SimpleDateFormat("dd.MM.yyyy");
        return testDate.format(testSelectedDateChange.getTime());
    }

    public void setTestSelectedDateChangeString(String testSelectedDateChangeString) {
    }

    public Date getTestSelectedMonthChange() {
        return testSelectedMonthChange;
    }

    public void setTestSelectedMonthChange(Date testSelectedMonthChange) {
        this.testSelectedMonthChange = testSelectedMonthChange;
    }

    public Date getNoneDate() {
        return noneDate;
    }

    public void setNoneDate(Date noneDate) {
        this.noneDate = noneDate;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getTodayDateString() {
        try {
            SimpleDateFormat testDate = new SimpleDateFormat("d.MM.yyyy");
            return testDate.format(todayDate.getTime());
        } catch (NullPointerException npe) {
            return "null";
        }
    }

    public void setTodayDateString(String todayDateString) {
    }

    public String getNoneDateString() {
        try {
            SimpleDateFormat testDate = new SimpleDateFormat("d.MM.yyyy");
            return testDate.format(noneDate.getTime());
        } catch (NullPointerException npe) {
            return "null";
        }
    }

    public void setNoneDateString(String noneDateString) {
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getStyledCalendarValue() {
        return styledCalendarValue;
    }

    public void setStyledCalendarValue(Date styledCalendarValue) {
        this.styledCalendarValue = styledCalendarValue;
    }

    public Date getTestDateRangesValue() {
        return testDateRangesValue;
    }

    public void setTestDateRangesValue(Date testDateRangesValue) {
        this.testDateRangesValue = testDateRangesValue;
    }

    public String getTestSelectedMonthChangeString() {
        SimpleDateFormat testDate = new SimpleDateFormat("dd.MM.yyyy");
        return testDate.format(testSelectedMonthChange.getTime());
    }

    public void setTestSelectedMonthChangeString(String testSelectedMonthChangeString) {
    }

    public int getValueChangeListenerCounter() {
        return valueChangeListenerCounter;
    }

    public void setValueChangeListenerCounter(int valueChangeListenerCounter) {
        this.valueChangeListenerCounter = valueChangeListenerCounter;
    }

    public String getValueChangeListenerFlag() {
        return String.valueOf(valueChangeListenerCounter);
    }

    public void valueChangedAttribute(ValueChangeEvent event) {
        valueChangeListenerCounter++;
    }

    public boolean isTestValueChangeListener() {
        return testValueChangeListener;
    }

    public Date getDateWithTime1() {
        return dateWithTime1;
    }

    public void setDateWithTime1(Date dateWithTime1) {
        this.dateWithTime1 = dateWithTime1;
    }

    public Date getDateWithTime2() {
        return dateWithTime2;
    }

    public void setDateWithTime2(Date dateWithTime2) {
        this.dateWithTime2 = dateWithTime2;
    }

    public TimeZone getEetTimeZone() {
        return eetTimeZone;
    }
}