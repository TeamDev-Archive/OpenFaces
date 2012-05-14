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
package org.openfaces.testapp.datechooser;

import javax.faces.event.ValueChangeEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Darya Shumilina
 */
public class DateChooserBean {

    private Date testSelectedDateChange;
    private Date testSelectedMonthChange;
    private Date rusDate;
    private Date todayDate = Calendar.getInstance().getTime();

    private Date noneDate;
    private Date formatsLocalesPatterns;
    private int selectedLocaleIndex;
    private List locales = new ArrayList();

    private Date styledCalendarValue;
    private boolean disabled = false;
    private boolean valueChangeListenerFlag;
    public static boolean testValueChangeListener;

    public DateChooserBean() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 11, 3);
        testSelectedDateChange = c.getTime();

        Calendar c1 = Calendar.getInstance();
        c1.set(2007, 11, 3);
        testSelectedMonthChange = c1.getTime();

        Calendar c2 = Calendar.getInstance();
        c2.set(2007, 11, 3);
        formatsLocalesPatterns = c2.getTime();

        locales = Arrays.asList("en", "fr", "de", "ja", "it");

        Calendar styled = Calendar.getInstance();
        styled.set(2007, 10, 12);
        styledCalendarValue = styled.getTime();
    }

    public Date getRusDate() {
        return rusDate;
    }

    public void setRusDate(Date rusDate) {
        this.rusDate = rusDate;
    }

    public int intValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public Date getDefaultDate() {
        return Calendar.getInstance().getTime();
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

    public String getTestSelectedMonthChangeString() {
        SimpleDateFormat testDate = new SimpleDateFormat("dd.MM.yyyy");
        return testDate.format(testSelectedMonthChange.getTime());
    }

    public void setTestSelectedMonthChangeString(String testSelectedMonthChangeString) {
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public String getTodayDateString() {
        try {
            SimpleDateFormat testDate = new SimpleDateFormat("dd MMMM, yyyy", new Locale("en"));
            return testDate.format(todayDate.getTime());
        } catch (NullPointerException npe) {
            return "null";
        }
    }

    public Date getNoneDate() {
        return noneDate;
    }

    public void setNoneDate(Date noneDate) {
        this.noneDate = noneDate;
    }

    public String getNoneDateString() {
        try {
            SimpleDateFormat testDate = new SimpleDateFormat("dd MMMM, yyyy", new Locale("en"));
            return testDate.format(noneDate.getTime());
        } catch (NullPointerException npe) {
            return "null";
        }
    }

    public Date getFormatsLocalesPatterns() {
        return formatsLocalesPatterns;
    }

    public void setFormatsLocalesPatterns(Date formatsLocalesPatterns) {
        this.formatsLocalesPatterns = formatsLocalesPatterns;
    }

    public List getLocales() {
        return locales;
    }

    public int getSelectedLocaleIndex() {
        return selectedLocaleIndex;
    }

    public void setSelectedLocaleIndex(int selectedLocaleIndex) {
        this.selectedLocaleIndex = selectedLocaleIndex;
    }

    public String getSelectedLocale() {
        return (String) locales.get(selectedLocaleIndex);
    }

    public void setSelectedLocale(String selectedLocale) {
    }

    public Date getStyledCalendarValue() {
        return styledCalendarValue;
    }

    public void setStyledCalendarValue(Date styledCalendarValue) {
        this.styledCalendarValue = styledCalendarValue;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void makeDisabled() {
        disabled = true;
    }

    public boolean isValueChangeListenerFlag() {
        return valueChangeListenerFlag;
    }

    public void setValueChangeListenerFlag(boolean valueChangeListenerFlag) {
        this.valueChangeListenerFlag = valueChangeListenerFlag;
    }

    public void valueChangedAttribute(ValueChangeEvent event) {
        valueChangeListenerFlag = !valueChangeListenerFlag;
    }

    public boolean isTestValueChangeListener() {
        return testValueChangeListener;
    }

}