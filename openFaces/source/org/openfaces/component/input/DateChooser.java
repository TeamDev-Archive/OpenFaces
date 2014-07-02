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
package org.openfaces.component.input;

import org.openfaces.component.Side;
import org.openfaces.util.CalendarUtil;
import org.openfaces.util.MessageUtil;
import org.openfaces.util.ValueBindings;

import javax.faces.application.FacesMessage;
import javax.faces.component.PartialStateHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.Boolean.valueOf;

/**
 * The DateChooser component enables the user to enter a date either by typing it in the text
 * field or selecting it from the drop-down calendar. With internationalization support, a
 * selected date can be displayed in different date formats and languages. The appearance of
 * virtually every element of the ?DateChooser component can be customized.
 *
 * @author Kharchenko
 */
public class DateChooser extends DropDownComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.DateChooser";
    public static final String COMPONENT_FAMILY = "org.openfaces.DateChooser";

    public static final String FORMAT_SHORT = "short";
    public static final String FORMAT_MEDIUM = "medium";
    public static final String FORMAT_LONG = "long";
    public static final String FORMAT_FULL = "full";

    private static final String DEFAULT_DATE_FORMAT = "medium";
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    private String calendarStyle;
    private String calendarClass;
    private String dayStyle;
    private String dayClass;
    private String rolloverDayStyle;
    private String rolloverDayClass;
    private String inactiveMonthDayStyle;
    private String inactiveMonthDayClass;
    private String rolloverInactiveMonthDayStyle;
    private String rolloverInactiveMonthDayClass;
    private String selectedDayStyle;
    private String selectedDayClass;
    private String rolloverSelectedDayStyle;
    private String rolloverSelectedDayClass;
    private String todayStyle;
    private String todayClass;
    private String rolloverTodayStyle;
    private String rolloverTodayClass;

    private String disabledDayStyle;
    private String disabledDayClass;
    private String rolloverDisabledDayStyle;
    private String rolloverDisabledDayClass;

    private String weekendDayStyle;
    private String weekendDayClass;
    private String rolloverWeekendDayStyle;
    private String rolloverWeekendDayClass;

    private String daysHeaderStyle;
    private String daysHeaderClass;

    private String headerStyle;
    private String headerClass;
    private String footerStyle;
    private String footerClass;

    private Integer firstDayOfWeek;

    private String dateFormat;
    private String pattern;
    private Locale locale;
    private TimeZone timeZone;

    private String todayText;
    private String noneText;

    private Boolean keepTime;
    private Boolean showFooter;
    private Boolean typingAllowed;

    public DateChooser() {
        setRendererType("org.openfaces.DateChooserRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Converter converter = getConverter();
        if (converter instanceof PartialStateHolder) {
            // make programmatically specified converter to be saved in state
            ((PartialStateHolder) converter).clearInitialState();
            clearInitialState();
        }
        return new Object[]{super.saveState(context), calendarStyle, dayStyle,
                rolloverDayStyle, inactiveMonthDayStyle, rolloverInactiveMonthDayStyle, selectedDayStyle,
                rolloverSelectedDayStyle, todayStyle, rolloverTodayStyle, disabledDayStyle,
                rolloverDisabledDayStyle, weekendDayStyle, rolloverWeekendDayStyle, daysHeaderStyle,
                headerStyle, footerStyle, calendarClass, dayClass, rolloverDayClass, inactiveMonthDayClass,
                rolloverInactiveMonthDayClass, selectedDayClass, rolloverSelectedDayClass, todayClass,
                rolloverTodayClass, disabledDayClass, rolloverDisabledDayClass, weekendDayClass,
                rolloverWeekendDayClass, daysHeaderClass, headerClass, footerClass, firstDayOfWeek, dateFormat,
                pattern, locale, timeZone, todayText, noneText, keepTime, showFooter, typingAllowed
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        calendarStyle = (String) values[i++];
        dayStyle = (String) values[i++];
        rolloverDayStyle = (String) values[i++];
        inactiveMonthDayStyle = (String) values[i++];
        rolloverInactiveMonthDayStyle = (String) values[i++];
        selectedDayStyle = (String) values[i++];
        rolloverSelectedDayStyle = (String) values[i++];
        todayStyle = (String) values[i++];
        rolloverTodayStyle = (String) values[i++];
        disabledDayStyle = (String) values[i++];
        rolloverDisabledDayStyle = (String) values[i++];
        weekendDayStyle = (String) values[i++];
        rolloverWeekendDayStyle = (String) values[i++];
        daysHeaderStyle = (String) values[i++];
        headerStyle = (String) values[i++];
        footerStyle = (String) values[i++];

        calendarClass = (String) values[i++];
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

        firstDayOfWeek = (Integer) values[i++];
        dateFormat = (String) values[i++];
        pattern = (String) values[i++];
        locale = (Locale) values[i++];
        timeZone = (TimeZone) values[i++];

        todayText = (String) values[i++];
        noneText = (String) values[i++];
        keepTime = (Boolean) values[i++];
        showFooter = (Boolean) values[i++];
        typingAllowed = (Boolean) values[i++];
    }

    @Override
    public void validate(FacesContext context) {
        Object submittedValue = getSubmittedValue();
        if (submittedValue == null && isRequired()) {
            context.addMessage(getClientId(context), MessageUtil.getMessage(context,
                    FacesMessage.SEVERITY_ERROR, REQUIRED_MESSAGE_ID, new Object[]{this.getId()}));
        }
        super.validate(context);
    }

    public String getCalendarStyle() {
        return ValueBindings.get(this, "calendarStyle", calendarStyle);
    }

    public void setCalendarStyle(String calendarStyle) {
        this.calendarStyle = calendarStyle;
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

    public String getDateFormat() {
        return ValueBindings.get(this, "dateFormat", dateFormat, DEFAULT_DATE_FORMAT);
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getPattern() {
        Locale locale = getLocale();
        if (locale == null) {
            locale = DEFAULT_LOCALE;
        }
        return CalendarUtil.getDatePattern(this, getDateFormat(), pattern, locale);
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Locale getLocale() {
        return CalendarUtil.getBoundPropertyValueAsLocale(this, "locale", locale);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public TimeZone getTimeZone() {
        return ValueBindings.get(this, "timeZone", timeZone, TimeZone.getDefault(), TimeZone.class);
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String getCalendarClass() {
        return ValueBindings.get(this, "calendarClass", calendarClass);
    }

    public void setCalendarClass(String calendarStyle) {
        calendarClass = calendarStyle;
    }

    public String getDayClass() {
        return ValueBindings.get(this, "dayClass", dayClass);
    }

    public void setDayClass(String dayStyle) {
        dayClass = dayStyle;
    }

    public String getRolloverDayClass() {
        return ValueBindings.get(this, "rolloverDayClass", rolloverDayClass);
    }

    public void setRolloverDayClass(String rolloverDayStyle) {
        rolloverDayClass = rolloverDayStyle;
    }

    public String getInactiveMonthDayClass() {
        return ValueBindings.get(this, "inactiveMonthDayClass", inactiveMonthDayClass);
    }

    public void setInactiveMonthDayClass(String inactiveMonthDayStyle) {
        inactiveMonthDayClass = inactiveMonthDayStyle;
    }

    public String getRolloverInactiveMonthDayClass() {
        return ValueBindings.get(this, "rolloverInactiveMonthDayClass",
                rolloverInactiveMonthDayClass);
    }

    public void setRolloverInactiveMonthDayClass(String rolloverInactiveMonthDayStyle) {
        rolloverInactiveMonthDayClass = rolloverInactiveMonthDayStyle;
    }

    public String getSelectedDayClass() {
        return ValueBindings.get(this, "selectedDayClass", selectedDayClass);
    }

    public void setSelectedDayClass(String selectedDayStyle) {
        selectedDayClass = selectedDayStyle;
    }

    public String getRolloverSelectedDayClass() {
        return ValueBindings.get(this, "rolloverSelectedDayClass", rolloverSelectedDayClass);
    }

    public void setRolloverSelectedDayClass(String rolloverSelectedDayStyle) {
        rolloverSelectedDayClass = rolloverSelectedDayStyle;
    }

    public String getTodayClass() {
        return ValueBindings.get(this, "todayClass", todayClass);
    }

    public void setTodayClass(String todayStyle) {
        todayClass = todayStyle;
    }

    public String getRolloverTodayClass() {
        return ValueBindings.get(this, "rolloverTodayClass", rolloverTodayClass);
    }

    public void setRolloverTodayClass(String rolloverTodayStyle) {
        rolloverTodayClass = rolloverTodayStyle;
    }

    public String getDisabledDayClass() {
        return ValueBindings.get(this, "disabledDayClass", disabledDayClass);
    }

    public void setDisabledDayClass(String disabledDayStyle) {
        disabledDayClass = disabledDayStyle;
    }

    public String getRolloverDisabledDayClass() {
        return ValueBindings.get(this, "rolloverDisabledDayClass", rolloverDisabledDayClass);
    }

    public void setRolloverDisabledDayClass(String rolloverDisabledDayStyle) {
        rolloverDisabledDayClass = rolloverDisabledDayStyle;
    }

    public String getDaysHeaderClass() {
        return ValueBindings.get(this, "daysHeaderClass", daysHeaderClass);
    }

    public void setDaysHeaderClass(String daysHeaderStyle) {
        daysHeaderClass = daysHeaderStyle;
    }

    public String getWeekendDayClass() {
        return ValueBindings.get(this, "weekendDayClass", weekendDayClass);
    }

    public void setWeekendDayClass(String weekendDayStyle) {
        weekendDayClass = weekendDayStyle;
    }

    public String getRolloverWeekendDayClass() {
        return ValueBindings.get(this, "rolloverWeekendDayClass", rolloverWeekendDayClass);
    }

    public void setRolloverWeekendDayClass(String rolloverWeekendDayStyle) {
        rolloverWeekendDayClass = rolloverWeekendDayStyle;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerStyle) {
        headerClass = headerStyle;
    }

    public String getFooterClass() {
        return ValueBindings.get(this, "footerClass", footerClass);
    }

    public void setFooterClass(String footerStyle) {
        footerClass = footerStyle;
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

    /**
     * Indicates whether or not to keep time portion of the bound date value.
     *
     * @return <code>true</code> if time is kept; <code>false</code> if time is set to 00:00:00. Default value is <code>false</code>.
     */
    public boolean isKeepTime() {
        return ValueBindings.get(this, "keepTime", keepTime, false);
    }

    /**
     * Specifies whether to keep time of a bound date value or not.
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
        this.showFooter = valueOf(showFooter);
    }

    public boolean isTypingAllowed() {
        return ValueBindings.get(this, "typingAllowed", typingAllowed, true);
    }

    public void setTypingAllowed(boolean typingAllowed) {
        this.typingAllowed = typingAllowed;
    }

    @Override
    public String getListClass() {
        return super.getListClass();
    }

    @Override
    public void setListClass(String listClass) {
        super.setListClass(listClass);
    }

    @Override
    public String getRolloverListClass() {
        return super.getRolloverListClass();
    }

    @Override
    public void setRolloverListClass(String rolloverListClass) {
        super.setRolloverButtonClass(rolloverListClass);
    }

    @Override
    public String getListStyle() {
        return super.getListStyle();
    }

    @Override
    public void setListStyle(String listStyle) {
        super.setListStyle(listStyle);
    }

    @Override
    public String getRolloverListStyle() {
        return super.getRolloverButtonClass();
    }

    @Override
    public void setRolloverListStyle(String rolloverListStyle) {
        super.setRolloverButtonClass(rolloverListStyle);
    }

    @Override
    public String getFieldStyle() {
        return super.getFieldStyle();
    }

    @Override
    public void setFieldStyle(String fieldStyle) {
        super.setFieldStyle(fieldStyle);
    }

    @Override
    public String getRolloverFieldStyle() {
        return super.getRolloverFieldStyle();
    }

    @Override
    public void setRolloverFieldStyle(String rolloverFieldStyle) {
        super.setRolloverFieldStyle(rolloverFieldStyle);
    }


    @Override
    public String getFieldClass() {
        return super.getFieldClass();
    }

    @Override
    public void setFieldClass(String fieldClass) {
        super.setFieldClass(fieldClass);
    }

    @Override
    public String getRolloverFieldClass() {
        return super.getRolloverFieldClass();
    }

    @Override
    public void setRolloverFieldClass(String rolloverFieldClass) {
        super.setRolloverFieldClass(rolloverFieldClass);
    }


    @Override
    public String getButtonStyle() {
        return super.getButtonStyle();
    }

    @Override
    public void setButtonStyle(String buttonStyle) {
        super.setButtonStyle(buttonStyle);
    }

    @Override
    public String getRolloverButtonStyle() {
        return super.getRolloverButtonStyle();
    }

    @Override
    public void setRolloverButtonStyle(String rolloverButtonStyle) {
        super.setRolloverButtonStyle(rolloverButtonStyle);
    }

    @Override
    public String getButtonImageUrl() {
        return super.getButtonImageUrl();
    }

    @Override
    public void setButtonImageUrl(String buttonImageUrl) {
        super.setButtonImageUrl(buttonImageUrl);
    }

    @Override
    public String getPressedButtonStyle() {
        return super.getPressedButtonStyle();
    }

    @Override
    public void setPressedButtonStyle(String pressedButtonStyle) {
        super.setPressedButtonStyle(pressedButtonStyle);
    }

    @Override
    public String getPressedButtonClass() {
        return super.getPressedButtonClass();
    }

    @Override
    public void setPressedButtonClass(String pressedButtonClass) {
        super.setPressedButtonClass(pressedButtonClass);
    }

    @Override
    public String getButtonClass() {
        return super.getButtonClass();
    }

    @Override
    public void setButtonClass(String buttonClass) {
        super.setButtonClass(buttonClass);
    }

    @Override
    public String getRolloverButtonClass() {
        return super.getRolloverButtonClass();
    }

    @Override
    public void setRolloverButtonClass(String rolloverButtonClass) {
        super.setRolloverButtonClass(rolloverButtonClass);
    }

    @Override
    public Side getButtonAlignment() {
        return super.getButtonAlignment();
    }

    @Override
    public void setButtonAlignment(Side buttonAlignment) {
        super.setButtonAlignment(buttonAlignment);
    }

    @Override
    public void setDisabledButtonClass(String disabledButtonClass) {
        super.setDisabledButtonClass(disabledButtonClass);
    }

    @Override
    public void setDisabledButtonImageUrl(String disabledButtonImageUrl) {
        super.setDisabledButtonImageUrl(disabledButtonImageUrl);
    }

    @Override
    public void setDisabledButtonStyle(String disabledButtonStyle) {
        super.setDisabledButtonStyle(disabledButtonStyle);
    }

    @Override
    public String getDisabledButtonClass() {
        return super.getDisabledButtonClass();
    }

    @Override
    public String getDisabledButtonImageUrl() {
        return super.getDisabledButtonImageUrl();
    }

    @Override
    public String getDisabledButtonStyle() {
        return super.getDisabledButtonStyle();
    }
}
