/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.calendar;

import org.openfaces.component.select.TabSetItem;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarBean implements Serializable {
    private Date fromDate;
    private Date toDate;
    private List<TabSetItem> locales = new ArrayList<TabSetItem>();
    private LocaleItem selectedLocaleItem;

    public CalendarBean() {
        fromDate = getNextMonday().getTime();
        Calendar tempToDate = getNextMonday();
        tempToDate.add(Calendar.DAY_OF_YEAR, 14);
        toDate = tempToDate.getTime();
        List<LocaleItem> locales = calendarLocales();
        for (LocaleItem localeItem : locales) {
            TabSetItem tabSetItem = new TabSetItem();
            HtmlOutputText component = (HtmlOutputText) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
            component.setValue(localeItem.toString());
            tabSetItem.getChildren().add(component);
            tabSetItem.setItemValue(localeItem);
            this.locales.add(tabSetItem);
        }
        selectedLocaleItem = locales.get(0);
    }

    public static List<LocaleItem> calendarLocales() {
        List<LocaleItem> locales = new ArrayList<LocaleItem>();
        locales.add(new LocaleItem("English", Locale.ENGLISH, "Today", "None"));
        locales.add(new LocaleItem("French", Locale.FRENCH, "Aujourd'hui", "Aucun"));
        locales.add(new LocaleItem("German", Locale.GERMAN, "Heute", "Kein"));
        locales.add(new LocaleItem("Italian", Locale.ITALY, "Oggi", "Nessun"));
        locales.add(new LocaleItem("Japanese", Locale.JAPAN, "\u4ECA\u65E5", "\u524A\u9664"));
        locales.add(new LocaleItem("Russian", new Locale("ru"), "\u0421\u0435\u0433\u043E\u0434\u043D\u044F", "\u0421\u0431\u0440\u043E\u0441"));
        locales.add(new LocaleItem("Arabic", new Locale("ar"), "\u0645\u0648\u064A\u0644\u0627", "\u0644\u0627\u0634\u0626"));
        return locales;
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
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 1);
        return c.getTime();
    }

    public void setIntroductionFromDate(Date date) {
    }

    public Date getLecturesFromDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 2);
        return c.getTime();
    }

    public Date getPracticeFromDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 13);
        return c.getTime();
    }

    public Date getExaminationFromDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 24);
        return c.getTime();
    }

    public Date getIntroductionToDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 1);
        return c.getTime();
    }

    public Date getLecturesToDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 12);
        return c.getTime();
    }

    public Date getPracticeToDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 23);
        return c.getTime();
    }

    public Date getExaminationToDate() {
        Calendar c = Calendar.getInstance();
        c.set(2007, 9, 30);
        return c.getTime();
    }

    public List<TabSetItem> getLocales() {
        return locales;
    }

    public Locale getSelectedLocale() {
        return getSelectedLocaleItem().getLocale();
    }

    public LocaleItem getSelectedLocaleItem() {
        return selectedLocaleItem;
    }

    public void setSelectedLocaleItem(LocaleItem selectedLocaleItem) {
        this.selectedLocaleItem = selectedLocaleItem;
    }

    public String getCurrentTodayText() {
        return selectedLocaleItem != null ? selectedLocaleItem.getTodayText() : "";
    }

    public String getCurrentNoneText() {
        return selectedLocaleItem != null ? selectedLocaleItem.getNoneText() : "";
    }


}
