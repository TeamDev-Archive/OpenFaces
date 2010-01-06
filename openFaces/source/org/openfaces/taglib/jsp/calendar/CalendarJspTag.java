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
package org.openfaces.taglib.jsp.calendar;

import org.openfaces.taglib.internal.calendar.CalendarTag;
import org.openfaces.taglib.jsp.UIInputJspTag;

import javax.el.ValueExpression;

/**
 * @author Kharchenko
 */
public class CalendarJspTag extends UIInputJspTag {

    public CalendarJspTag() {
        super(new CalendarTag());
    }

    public void setLocale(ValueExpression locale) {
        getDelegate().setPropertyValue("locale", locale);
    }

    public void setTimeZone(ValueExpression timeZone) {
        getDelegate().setPropertyValue("timeZone", timeZone);
    }

    public void setDayStyle(ValueExpression dayStyle) {
        getDelegate().setPropertyValue("dayStyle", dayStyle);
    }

    public void setRolloverDayStyle(ValueExpression rolloverDayStyle) {
        getDelegate().setPropertyValue("rolloverDayStyle", rolloverDayStyle);
    }

    public void setInactiveMonthDayStyle(ValueExpression inactiveMonthDayStyle) {
        getDelegate().setPropertyValue("inactiveMonthDayStyle", inactiveMonthDayStyle);
    }

    public void setRolloverInactiveMonthDayStyle(ValueExpression rolloverInactiveMonthDayStyle) {
        getDelegate().setPropertyValue("rolloverInactiveMonthDayStyle", rolloverInactiveMonthDayStyle);
    }

    public void setSelectedDayStyle(ValueExpression selectedDayStyle) {
        getDelegate().setPropertyValue("selectedDayStyle", selectedDayStyle);
    }

    public void setRolloverSelectedDayStyle(ValueExpression rolloverSelectedDayStyle) {
        getDelegate().setPropertyValue("rolloverSelectedDayStyle", rolloverSelectedDayStyle);
    }

    public void setTodayStyle(ValueExpression todayStyle) {
        getDelegate().setPropertyValue("todayStyle", todayStyle);
    }

    public void setRolloverTodayStyle(ValueExpression rolloverTodayStyle) {
        getDelegate().setPropertyValue("rolloverTodayStyle", rolloverTodayStyle);
    }

    public void setDisabledDayStyle(ValueExpression disabledDayStyle) {
        getDelegate().setPropertyValue("disabledDayStyle", disabledDayStyle);
    }

    public void setRolloverDisabledDayStyle(ValueExpression rolloverDisabledDayStyle) {
        getDelegate().setPropertyValue("rolloverDisabledDayStyle", rolloverDisabledDayStyle);
    }

    public void setDaysHeaderStyle(ValueExpression daysHeaderStyle) {
        getDelegate().setPropertyValue("daysHeaderStyle", daysHeaderStyle);
    }

    public void setWeekendDayStyle(ValueExpression weekendDayStyle) {
        getDelegate().setPropertyValue("weekendDayStyle", weekendDayStyle);
    }

    public void setRolloverWeekendDayStyle(ValueExpression rolloverWeekendDayStyle) {
        getDelegate().setPropertyValue("rolloverWeekendDayStyle", rolloverWeekendDayStyle);
    }

    public void setHeaderStyle(ValueExpression headerStyle) {
        getDelegate().setPropertyValue("headerStyle", headerStyle);
    }

    public void setFooterStyle(ValueExpression footerStyle) {
        getDelegate().setPropertyValue("footerStyle", footerStyle);
    }

    public void setFirstDayOfWeek(ValueExpression firstDayOfWeek) {
        getDelegate().setPropertyValue("firstDayOfWeek", firstDayOfWeek);
    }

    public void setOnperiodchange(ValueExpression onperiodchange) {
        getDelegate().setPropertyValue("onperiodchange", onperiodchange);
    }

    public void setDayClass(ValueExpression dayClass) {
        getDelegate().setPropertyValue("dayClass", dayClass);
    }

    public void setRolloverDayClass(ValueExpression rolloverDayClass) {
        getDelegate().setPropertyValue("rolloverDayClass", rolloverDayClass);
    }

    public void setInactiveMonthDayClass(ValueExpression inactiveMonthDayClass) {
        getDelegate().setPropertyValue("inactiveMonthDayClass", inactiveMonthDayClass);
    }

    public void setRolloverInactiveMonthDayClass(ValueExpression rolloverInactiveMonthDayClass) {
        getDelegate().setPropertyValue("rolloverInactiveMonthDayClass", rolloverInactiveMonthDayClass);
    }

    public void setSelectedDayClass(ValueExpression selectedDayClass) {
        getDelegate().setPropertyValue("selectedDayClass", selectedDayClass);
    }

    public void setRolloverSelectedDayClass(ValueExpression rolloverSelectedDayClass) {
        getDelegate().setPropertyValue("rolloverSelectedDayClass", rolloverSelectedDayClass);
    }

    public void setTodayClass(ValueExpression todayClass) {
        getDelegate().setPropertyValue("todayClass", todayClass);
    }

    public void setRolloverTodayClass(ValueExpression rolloverTodayClass) {
        getDelegate().setPropertyValue("rolloverTodayClass", rolloverTodayClass);
    }

    public void setDisabledDayClass(ValueExpression disabledDayClass) {
        getDelegate().setPropertyValue("disabledDayClass", disabledDayClass);
    }

    public void setRolloverDisabledDayClass(ValueExpression rolloverDisabledDayClass) {
        getDelegate().setPropertyValue("rolloverDisabledDayClass", rolloverDisabledDayClass);
    }

    public void setWeekendDayClass(ValueExpression weekendDayClass) {
        getDelegate().setPropertyValue("weekendDayClass", weekendDayClass);
    }

    public void setRolloverWeekendDayClass(ValueExpression rolloverWeekendDayClass) {
        getDelegate().setPropertyValue("rolloverWeekendDayClass", rolloverWeekendDayClass);
    }

    public void setDaysHeaderClass(ValueExpression daysHeaderClass) {
        getDelegate().setPropertyValue("daysHeaderClass", daysHeaderClass);
    }

    public void setHeaderClass(ValueExpression headerClass) {
        getDelegate().setPropertyValue("headerClass", headerClass);
    }

    public void setFooterClass(ValueExpression footerClass) {
        getDelegate().setPropertyValue("footerClass", footerClass);
    }

    public void setTodayText(ValueExpression todayText) {
        getDelegate().setPropertyValue("todayText", todayText);
    }

    public void setNoneText(ValueExpression noneText) {
        getDelegate().setPropertyValue("noneText", noneText);
    }

    public void setKeepTime(ValueExpression keepTime) {
        getDelegate().setPropertyValue("keepTime", keepTime);
    }

    public void setBodyStyle(ValueExpression bodyStyle) {
        getDelegate().setPropertyValue("bodyStyle", bodyStyle);
    }

    public void setBodyClass(ValueExpression bodyClass) {
        getDelegate().setPropertyValue("bodyClass", bodyClass);
    }

    public void setShowFooter(ValueExpression showFooter) {
        getDelegate().setPropertyValue("showFooter", showFooter);
    }

    public void setDisabledStyle(ValueExpression disabledStyle) {
        getDelegate().setPropertyValue("disabledStyle", disabledStyle);
    }

    public void setDisabledClass(ValueExpression disabledClass) {
        getDelegate().setPropertyValue("disabledClass", disabledClass);
    }

    public void setFocusable(ValueExpression focusable) {
        getDelegate().setPropertyValue("focusable", focusable);
    }

}
