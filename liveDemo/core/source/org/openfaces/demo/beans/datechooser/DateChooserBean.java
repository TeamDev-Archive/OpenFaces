/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.datechooser;

import org.openfaces.component.select.TabSetItem;
import org.openfaces.demo.beans.calendar.CalendarBean;
import org.openfaces.demo.beans.calendar.LocaleItem;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateChooserBean implements Serializable {
    private static final Date TODAY = Calendar.getInstance().getTime();

    private List<TabSetItem> locales;
    private Date defaultDate = TODAY;
    private Date patternDate = TODAY;
    private Date fullDate = TODAY;
    private Date longDate = TODAY;
    private Date mediumDate = TODAY;
    private Date shortDate = TODAY;
    private LocaleItem selectedLocaleItem;

    public DateChooserBean() {
        locales = new ArrayList<TabSetItem>();
        List<LocaleItem> locales = CalendarBean.calendarLocales();
        for (LocaleItem localeItem : locales) {
            TabSetItem tabSetItem = new TabSetItem();
            HtmlOutputText component = (HtmlOutputText) FacesContext.getCurrentInstance().getApplication().createComponent(HtmlOutputText.COMPONENT_TYPE);
            String stringValue = localeItem.toString();
            component.setValue(stringValue);
            tabSetItem.getChildren().add(component);
            tabSetItem.setItemValue(localeItem);
            this.locales.add(tabSetItem);
        }
        selectedLocaleItem = locales.get(0);
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
        return getSelectedLocaleItem() != null ? getSelectedLocaleItem().getTodayText() : "";
    }

    public String getCurrentNoneText() {
        return getSelectedLocaleItem() != null ? getSelectedLocaleItem().getNoneText() : "";
    }

    public Date getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
    }

    public Date getPatternDate() {
        return patternDate;
    }

    public void setPatternDate(Date patternDate) {
        this.patternDate = patternDate;
    }

    public Date getFullDate() {
        return fullDate;
    }

    public void setFullDate(Date fullDate) {
        this.fullDate = fullDate;
    }

    public Date getLongDate() {
        return longDate;
    }

    public void setLongDate(Date longDate) {
        this.longDate = longDate;
    }

    public Date getMediumDate() {
        return mediumDate;
    }

    public void setMediumDate(Date mediumDate) {
        this.mediumDate = mediumDate;
    }

    public Date getShortDate() {
        return shortDate;
    }

    public void setShortDate(Date shortDate) {
        this.shortDate = shortDate;
    }
}
