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
package org.openfaces.component.calendar;

import org.openfaces.component.OUIInputBase;
import org.openfaces.util.ValueBindings;
import org.openfaces.util.CalendarUtil;
import org.openfaces.util.DataUtil;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The Calendar component enables the user to select a date from a one-month calendar
 * and easily navigate between months and years. A specific group of dates can be included
 * in a date range. The Calendar component can use the client's locale or a specified one.
 * Various style options for different parts of the Calendar component let you create the
 * desired look-and-feel.
 * 
 * @author Kharchenko
 */
public class Calendar extends OUIInputBase {
    public static final String COMPONENT_TYPE = "org.openfaces.Calendar";
    public static final String COMPONENT_FAMILY = "org.openfaces.Calendar";

    protected static final List<String> EVENT_NAMES;
    static {
        List<String> eventNames = new ArrayList<String>(Arrays.asList("periodchange"));
        eventNames.addAll(OUIInputBase.EVENT_NAMES);
        EVENT_NAMES = Collections.unmodifiableList(eventNames);
    }

    private static final Converter DATE_CONVERTER = new DateConverter();

    private String disabledStyle;
    private String dayStyle;
    private String rolloverDayStyle;
    private String inactiveMonthDayStyle;
    private String rolloverInactiveMonthDayStyle;
    private String selectedDayStyle;
    private String rolloverSelectedDayStyle;
    private String todayStyle;
    private String rolloverTodayStyle;
    private String disabledDayStyle;
    private String rolloverDisabledDayStyle;
    private String weekendDayStyle;
    private String rolloverWeekendDayStyle;

    private String daysHeaderStyle;
    private String headerStyle;
    private String bodyStyle;
    private String footerStyle;

    private String disabledClass;
    private String dayClass;
    private String rolloverDayClass;
    private String inactiveMonthDayClass;
    private String rolloverInactiveMonthDayClass;
    private String selectedDayClass;
    private String rolloverSelectedDayClass;
    private String todayClass;
    private String rolloverTodayClass;
    private String disabledDayClass;
    private String rolloverDisabledDayClass;
    private String weekendDayClass;
    private String rolloverWeekendDayClass;
    private String daysHeaderClass;
    private String headerClass;
    private String bodyClass;
    private String footerClass;

    private Integer firstDayOfWeek;

    private String onperiodchange;

    private Locale locale;

    private String todayText;
    private String noneText;

    private Boolean keepTime;
    private Boolean showFooter;

    private Boolean focusable;

    private TimeZone timeZone;

    public Calendar() {
        setRendererType("org.openfaces.CalendarRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void setConverter(Converter converter) {
        throw new IllegalStateException("Converter for Calendar component shouldn't be set");
    }

    @Override
    public Converter getConverter() {
        return DATE_CONVERTER;
    }

    public String getDayStyle() {
        return ValueBindings.get(this, "dayStyle", dayStyle);
    }

    public void setDayStyle(String dayStyle) {
        this.dayStyle = dayStyle;
    }

    public String getRolloverDayStyle() {
        return ValueBindings.get(this, "rolloverDayStyle", rolloverDayStyle);
    }

    public void setRolloverDayStyle(String rolloverDayStyle) {
        this.rolloverDayStyle = rolloverDayStyle;
    }

    /**
     * Gets user defined string for "Today" button.
     *
     * @return user defined string for "Today" button or "Today" by default.
     */
    public String getTodayText() {
        return ValueBindings.get(this, "todayText", todayText, "Today");
    }

    /**
     * Sets user defined string value for "Today" button.
     *
     * @param todayText string value for "Today" button.
     */
    public void setTodayText(String todayText) {
        this.todayText = todayText;
    }

    /**
     * Gets user defined string for "None" button.
     *
     * @return user defined string for "None" button or "None" by default.
     */
    public String getNoneText() {
        return ValueBindings.get(this, "noneText", noneText, "None");
    }

    /**
     * Sets user defined string value for "None" button.
     *
     * @param noneText string value for "None" button.
     */
    public void setNoneText(String noneText) {
        this.noneText = noneText;
    }

    public String getInactiveMonthDayStyle() {
        return ValueBindings.get(this, "inactiveMonthDayStyle", inactiveMonthDayStyle);
    }

    public void setInactiveMonthDayStyle(String inactiveMonthDayStyle) {
        this.inactiveMonthDayStyle = inactiveMonthDayStyle;
    }

    public String getRolloverInactiveMonthDayStyle() {
        return ValueBindings.get(this, "rolloverInactiveMonthDayStyle",
                rolloverInactiveMonthDayStyle);
    }

    public void setRolloverInactiveMonthDayStyle(String rolloverInactiveMonthDayStyle) {
        this.rolloverInactiveMonthDayStyle = rolloverInactiveMonthDayStyle;
    }

    public String getSelectedDayStyle() {
        return ValueBindings.get(this, "selectedDayStyle", selectedDayStyle);
    }

    public void setSelectedDayStyle(String selectedDayStyle) {
        this.selectedDayStyle = selectedDayStyle;
    }

    public String getRolloverSelectedDayStyle() {
        return ValueBindings.get(this, "rolloverSelectedDayStyle", rolloverSelectedDayStyle);
    }

    public void setRolloverSelectedDayStyle(String rolloverSelectedDayStyle) {
        this.rolloverSelectedDayStyle = rolloverSelectedDayStyle;
    }

    public String getTodayStyle() {
        return ValueBindings.get(this, "todayStyle", todayStyle);
    }

    public void setTodayStyle(String todayStyle) {
        this.todayStyle = todayStyle;
    }

    public String getRolloverTodayStyle() {
        return ValueBindings.get(this, "rolloverTodayStyle", rolloverTodayStyle);
    }

    public void setRolloverTodayStyle(String rolloverTodayStyle) {
        this.rolloverTodayStyle = rolloverTodayStyle;
    }

    public String getDisabledDayStyle() {
        return ValueBindings.get(this, "disabledDayStyle", disabledDayStyle);
    }

    public void setDisabledDayStyle(String disabledDayStyle) {
        this.disabledDayStyle = disabledDayStyle;
    }

    public String getRolloverDisabledDayStyle() {
        return ValueBindings.get(this, "rolloverDisabledDayStyle", rolloverDisabledDayStyle);
    }

    public void setRolloverDisabledDayStyle(String rolloverDisabledDayStyle) {
        this.rolloverDisabledDayStyle = rolloverDisabledDayStyle;
    }

    public String getDaysHeaderStyle() {
        return ValueBindings.get(this, "daysHeaderStyle", daysHeaderStyle);
    }

    public void setDaysHeaderStyle(String daysHeaderStyle) {
        this.daysHeaderStyle = daysHeaderStyle;
    }

    public String getWeekendDayStyle() {
        return ValueBindings.get(this, "weekendDayStyle", weekendDayStyle);
    }

    public void setWeekendDayStyle(String weekendDayStyle) {
        this.weekendDayStyle = weekendDayStyle;
    }

    public String getRolloverWeekendDayStyle() {
        return ValueBindings.get(this, "rolloverWeekendDayStyle", rolloverWeekendDayStyle);
    }

    public void setRolloverWeekendDayStyle(String rolloverWeekendDayStyle) {
        this.rolloverWeekendDayStyle = rolloverWeekendDayStyle;
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getFooterStyle() {
        return ValueBindings.get(this, "footerStyle", footerStyle);
    }

    public void setFooterStyle(String footerStyle) {
        this.footerStyle = footerStyle;
    }

    public int getFirstDayOfWeek() {
        return ValueBindings.get(this, "firstDayOfWeek", firstDayOfWeek,
                java.util.Calendar.getInstance(getLocale()).getFirstDayOfWeek());
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        if (firstDayOfWeek < 1 || firstDayOfWeek > 7)
            firstDayOfWeek = java.util.Calendar.getInstance(getLocale()).getFirstDayOfWeek();
        this.firstDayOfWeek = firstDayOfWeek;
    }

    public String getOnperiodchange() {
        return ValueBindings.get(this, "onperiodchange", onperiodchange);
    }

    public void setOnperiodchange(String onperiodchange) {
        this.onperiodchange = onperiodchange;
    }

    public Locale getLocale() {
        return CalendarUtil.getBoundPropertyValueAsLocale(this, "locale", locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getDayClass() {
        return ValueBindings.get(this, "dayClass", dayClass);
    }

    public void setDayClass(String dayClass) {
        this.dayClass = dayClass;
    }

    public String getRolloverDayClass() {
        return ValueBindings.get(this, "rolloverDayClass", rolloverDayClass);
    }

    public void setRolloverDayClass(String rolloverDayClass) {
        this.rolloverDayClass = rolloverDayClass;
    }

    public String getInactiveMonthDayClass() {
        return ValueBindings.get(this, "inactiveMonthDayClass", inactiveMonthDayClass);
    }

    public void setInactiveMonthDayClass(String inactiveMonthDayClass) {
        this.inactiveMonthDayClass = inactiveMonthDayClass;
    }

    public String getRolloverInactiveMonthDayClass() {
        return ValueBindings.get(this, "rolloverInactiveMonthDayClass", rolloverInactiveMonthDayClass);
    }

    public void setRolloverInactiveMonthDayClass(String rolloverInactiveMonthDayClass) {
        this.rolloverInactiveMonthDayClass = rolloverInactiveMonthDayClass;
    }

    public String getSelectedDayClass() {
        return ValueBindings.get(this, "selectedDayClass", selectedDayClass);
    }

    public void setSelectedDayClass(String selectedDayClass) {
        this.selectedDayClass = selectedDayClass;
    }

    public String getRolloverSelectedDayClass() {
        return ValueBindings.get(this, "rolloverSleectedDayClass", rolloverSelectedDayClass);
    }

    public void setRolloverSelectedDayClass(String rolloverSelectedDayClass) {
        this.rolloverSelectedDayClass = rolloverSelectedDayClass;
    }

    public String getTodayClass() {
        return ValueBindings.get(this, "todayClass", todayClass);
    }

    public void setTodayClass(String todayClass) {
        this.todayClass = todayClass;
    }

    public String getRolloverTodayClass() {
        return ValueBindings.get(this, "rolloverTodayClass", rolloverTodayClass);
    }

    public void setRolloverTodayClass(String rolloverTodayClass) {
        this.rolloverTodayClass = rolloverTodayClass;
    }

    public String getDisabledDayClass() {
        return ValueBindings.get(this, "disabledDayClass", disabledDayClass);
    }

    public void setDisabledDayClass(String disabledDayClass) {
        this.disabledDayClass = disabledDayClass;
    }

    public String getRolloverDisabledDayClass() {
        return ValueBindings.get(this, "rolloverDisabledDayClass", rolloverDisabledDayClass);
    }

    public void setRolloverDisabledDayClass(String rolloverDisabledDayClass) {
        this.rolloverDisabledDayClass = rolloverDisabledDayClass;
    }

    public String getWeekendDayClass() {
        return ValueBindings.get(this, "weekendDayClass", weekendDayClass);
    }

    public void setWeekendDayClass(String weekendDayClass) {
        this.weekendDayClass = weekendDayClass;
    }

    public String getRolloverWeekendDayClass() {
        return ValueBindings.get(this, "rolloverWeekendDayClass", rolloverWeekendDayClass);
    }

    public void setRolloverWeekendDayClass(String rolloverWeekendDayClass) {
        this.rolloverWeekendDayClass = rolloverWeekendDayClass;
    }

    public String getDaysHeaderClass() {
        return ValueBindings.get(this, "daysHeaderClass", daysHeaderClass);
    }

    public void setDaysHeaderClass(String daysHeaderClass) {
        this.daysHeaderClass = daysHeaderClass;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getFooterClass() {
        return ValueBindings.get(this, "footerClass", footerClass);
    }

    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }

    public String getBodyStyle() {
        return ValueBindings.get(this, "bodyStyle", bodyStyle);
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public String getBodyClass() {
        return ValueBindings.get(this, "bodyClass", bodyClass);
    }

    public void setBodyClass(String bodyClass) {
        this.bodyClass = bodyClass;
    }

    public String getDisabledStyle() {
        return disabledStyle;
    }

    public void setDisabledStyle(String disabledStyle) {
        this.disabledStyle = disabledStyle;
    }

    public String getDisabledClass() {
        return disabledClass;
    }

    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

    public TimeZone getTimeZone() {
        return ValueBindings.get(this, "timeZone", timeZone, TimeZone.getDefault(), TimeZone.class);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }


    public boolean isFocusable() {
        return ValueBindings.get(this, "focusable", focusable, true);
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }


    /**
     * Indicates whether or not to keep time portion of the bound date value.
     *
     * @return <code>true</code> if time is kept; <code>false</code> if time is set to 00:00:00. Default value is <code>false</code>.
     */
    public boolean isKeepTime() {
        return ValueBindings.get(this, "keepTime", keepTime, false);
    }

    /**
     * Sets whether or not to keep time of the bound date value.
     *
     * @param keepTime
     */
    public void setKeepTime(boolean keepTime) {
        this.keepTime = keepTime;
    }

    public boolean isShowFooter() {
        return ValueBindings.get(this, "showFooter", showFooter, true);
    }

    public void setShowFooter(boolean showFooter) {
        this.showFooter = showFooter;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context), dayStyle, disabledStyle, rolloverDayStyle, inactiveMonthDayStyle,
                rolloverInactiveMonthDayStyle, selectedDayStyle, rolloverSelectedDayStyle, todayStyle,
                rolloverTodayStyle, disabledDayStyle, rolloverDisabledDayStyle, daysHeaderStyle, weekendDayStyle,
                rolloverWeekendDayStyle, headerStyle, footerStyle, firstDayOfWeek, locale, timeZone,
                disabledClass, dayClass, rolloverDayClass, inactiveMonthDayClass, rolloverInactiveMonthDayClass,
                selectedDayClass, rolloverSelectedDayClass, todayClass, rolloverTodayClass, disabledDayClass,
                rolloverDisabledDayClass, weekendDayClass, rolloverWeekendDayClass, daysHeaderClass, headerClass,
                footerClass, todayText, noneText, keepTime, onperiodchange, bodyStyle,
                bodyClass, showFooter, focusable
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        dayStyle = (String) values[i++];
        disabledStyle = (String) values[i++];
        rolloverDayStyle = (String) values[i++];
        inactiveMonthDayStyle = (String) values[i++];
        rolloverInactiveMonthDayStyle = (String) values[i++];
        selectedDayStyle = (String) values[i++];
        rolloverSelectedDayStyle = (String) values[i++];
        todayStyle = (String) values[i++];
        rolloverTodayStyle = (String) values[i++];
        disabledDayStyle = (String) values[i++];
        rolloverDisabledDayStyle = (String) values[i++];
        daysHeaderStyle = (String) values[i++];
        weekendDayStyle = (String) values[i++];
        rolloverWeekendDayStyle = (String) values[i++];
        headerStyle = (String) values[i++];
        footerStyle = (String) values[i++];
        firstDayOfWeek = (Integer) values[i++];
        locale = (Locale) values[i++];
        timeZone = (TimeZone) values[i++];

        disabledClass = (String) values[i++];
        dayClass = (String) values[i++];
        rolloverDayClass = (String) values[i++];
        inactiveMonthDayClass = (String) values[i++];
        rolloverInactiveMonthDayClass = (String) values[i++];
        selectedDayClass = (String) values[i++];
        rolloverSelectedDayClass = (String) values[i++];
        todayClass = (String) values[i++];
        rolloverTodayClass = (String) values[i++];
        disabledDayClass = (String) values[i++];
        rolloverDisabledDayClass = (String) values[i++];
        weekendDayClass = (String) values[i++];
        rolloverWeekendDayClass = (String) values[i++];
        daysHeaderClass = (String) values[i++];
        headerClass = (String) values[i++];
        footerClass = (String) values[i++];

        todayText = (String) values[i++];
        noneText = (String) values[i++];
        keepTime = (Boolean) values[i++];
        onperiodchange = (String) values[i++];
        bodyStyle = (String) values[i++];
        bodyClass = (String) values[i++];
        showFooter = (Boolean) values[i++];
        focusable = (Boolean) values[i++];
    }

    @Override
    protected boolean compareValues(Object previous, Object value) {
        if (previous == null) {
            return (value != null);
        } else if (value == null) {
            return (true);
        } else {
            java.util.Calendar valueCalendar = java.util.Calendar.getInstance();
            valueCalendar.setTime((Date) value);

            java.util.Calendar previousCalendar = java.util.Calendar.getInstance();
            previousCalendar.setTime((Date) previous);

            return valueCalendar.get(java.util.Calendar.YEAR) == previousCalendar.get(java.util.Calendar.YEAR)
                    && valueCalendar.get(java.util.Calendar.MONTH) == previousCalendar.get(java.util.Calendar.MONTH)
                    && valueCalendar.get(java.util.Calendar.DATE) == previousCalendar.get(java.util.Calendar.DATE);
        }
    }

    private static class DateConverter implements Converter {
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            if (value == null || value.length() == 0)
                return null;

            Calendar calendar = (Calendar) component;
            TimeZone timeZone = (calendar.getTimeZone() != null)
                    ? calendar.getTimeZone()
                    : TimeZone.getDefault();

            return DataUtil.parseDateFromJs(value, timeZone);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            if (value == null) {
                return "";
            }
            DateFormat format = new SimpleDateFormat();
            Calendar calendar = (Calendar) component;
            TimeZone timeZone = (calendar.getTimeZone() != null)
                    ? calendar.getTimeZone()
                    : TimeZone.getDefault();

            format.setTimeZone(timeZone);
            try {
                return format.format(value);
            }
            catch (IllegalArgumentException e) {
                throw new ConverterException("Cannot convert value '" + value + "'");
            }
        }
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }
}
