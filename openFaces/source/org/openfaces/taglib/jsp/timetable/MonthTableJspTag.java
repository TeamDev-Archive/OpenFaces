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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.MonthTableTag;

import javax.el.ValueExpression;

/**
 * @author Roman Porotnikov
 */
public class MonthTableJspTag extends TimetableViewJspTag {

    public MonthTableJspTag() {
        super(new MonthTableTag());
    }

    public void setFirstDayOfWeek(ValueExpression value) {
        getDelegate().setPropertyValue("firstDayOfWeek", value);
    }

    public void setWeekdayHeadersRowStyle(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayHeadersRowStyle", value);
    }

    public void setWeekdayHeadersRowClass(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayHeadersRowClass", value);
    }

    public void setWeekdayStyle(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayStyle", value);
    }

    public void setWeekdayClass(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayClass", value);
    }

    public void setWeekdayPattern(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayPattern", value);
    }

    public void setWeekdayHeadersRowSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayHeadersRowSeparator", value);
    }

    public void setWeekdayColumnSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayColumnSeparator", value);
    }

    public void setRowSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("rowSeparator", value);
    }

    public void setColumnSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("columnSeparator", value);
    }

    public void setDayHeaderRowStyle(ValueExpression value) {
        getDelegate().setPropertyValue("dayHeaderRowStyle", value);
    }

    public void setDayHeaderRowClass(ValueExpression value) {
        getDelegate().setPropertyValue("dayHeaderRowClass", value);
    }

    public void setWeekdayHeaderCellStyle(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayHeaderCellStyle", value);
    }

    public void setWeekdayHeaderCellClass(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayHeaderCellClass", value);
    }

    public void setWeekendWeekdayHeaderCellStyle(ValueExpression value) {
        getDelegate().setPropertyValue("weekendWeekdayHeaderCellStyle", value);
    }

    public void setWeekendWeekdayHeaderCellClass(ValueExpression value) {
        getDelegate().setPropertyValue("weekendWeekdayHeaderCellClass", value);
    }

    public void setCellHeaderStyle(ValueExpression value) {
        getDelegate().setPropertyValue("cellHeaderStyle", value);
    }

    public void setCellHeaderClass(ValueExpression value) {
        getDelegate().setPropertyValue("cellHeaderClass", value);
    }

    public void setCellStyle(ValueExpression value) {
        getDelegate().setPropertyValue("cellStyle", value);
    }

    public void setCellClass(ValueExpression value) {
        getDelegate().setPropertyValue("cellClass", value);
    }

    public void setTodayCellHeaderStyle(ValueExpression value) {
        getDelegate().setPropertyValue("todayCellHeaderStyle", value);
    }

    public void setTodayCellHeaderClass(ValueExpression value) {
        getDelegate().setPropertyValue("todayCellHeaderClass", value);
    }

    public void setTodayCellStyle(ValueExpression value) {
        getDelegate().setPropertyValue("todayCellStyle", value);
    }

    public void setTodayCellClass(ValueExpression value) {
        getDelegate().setPropertyValue("todayCellClass", value);
    }

    public void setWeekendCellHeaderStyle(ValueExpression value) {
        getDelegate().setPropertyValue("weekendCellHeaderStyle", value);
    }

    public void setWeekendCellHeaderClass(ValueExpression value) {
        getDelegate().setPropertyValue("weekendCellHeaderClass", value);
    }

    public void setWeekendCellStyle(ValueExpression value) {
        getDelegate().setPropertyValue("weekendCellStyle", value);
    }

    public void setWeekendCellClass(ValueExpression value) {
        getDelegate().setPropertyValue("weekendCellClass", value);
    }

    public void setInactiveMonthCellHeaderStyle(ValueExpression value) {
        getDelegate().setPropertyValue("inactiveMonthCellHeaderStyle", value);
    }

    public void setInactiveMonthCellHeaderClass(ValueExpression value) {
        getDelegate().setPropertyValue("inactiveMonthCellHeaderClass", value);
    }

    public void setInactiveMonthCellStyle(ValueExpression value) {
        getDelegate().setPropertyValue("inactiveMonthCellStyle", value);
    }

    public void setInactiveMonthCellClass(ValueExpression value) {
        getDelegate().setPropertyValue("inactiveMonthCellClass", value);
    }

    public void setMoreLinkElementStyle(ValueExpression value) {
        getDelegate().setPropertyValue("moreLinkElementStyle", value);
    }

    public void setMoreLinkElementClass(ValueExpression value) {
        getDelegate().setPropertyValue("moreLinkElementClass", value);
    }

    public void setMoreLinkStyle(ValueExpression value) {
        getDelegate().setPropertyValue("moreLinkStyle", value);
    }

    public void setMoreLinkClass(ValueExpression value) {
        getDelegate().setPropertyValue("moreLinkClass", value);
    }

    public void setMoreLinkText(ValueExpression value) {
        getDelegate().setPropertyValue("moreLinkText", value);
    }

    public void setExpandedDayViewStyle(ValueExpression value) {
        getDelegate().setPropertyValue("expandedDayViewStyle", value);
    }

    public void setExpandedDayViewClass(ValueExpression value) {
        getDelegate().setPropertyValue("expandedDayViewClass", value);
    }

    public void setExpandTransitionPeriod(ValueExpression value) {
        getDelegate().setPropertyValue("expandTransitionPeriod", value);
    }
}
