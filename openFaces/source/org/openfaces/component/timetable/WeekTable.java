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
package org.openfaces.component.timetable;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Roman Porotnikov
 */
public class WeekTable extends TimeScaleTable { // todo: extract some common type for <o:weekTable> and <o:monthTable> to reflect their ability to display week calendar(s)
    public static final String COMPONENT_TYPE = "org.openfaces.WeekTable";
    public static final String COMPONENT_FAMILY = "org.openfaces.WeekTable";

    private Integer firstDayOfWeek;

    private String weekdayColumnSeparator;
    private String weekdayHeadersRowSeparator;
    private String weekdayHeadersRowStyle;
    private String weekdayHeadersRowClass;
    private String weekdayStyle;
    private String weekdayClass;
    private String weekdayPattern;

    public WeekTable() {
        setRendererType("org.openfaces.WeekTableRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
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

    public String getWeekdayColumnSeparator() {
        return ValueBindings.get(this, "weekdayColumnSeparator", weekdayColumnSeparator);
    }

    public void setWeekdayColumnSeparator(String weekdayColumnSeparator) {
        this.weekdayColumnSeparator = weekdayColumnSeparator;
    }

    public String getWeekdayHeadersRowSeparator() {
        return ValueBindings.get(this, "weekdayHeadersRowSeparator", weekdayHeadersRowSeparator);
    }

    public void setWeekdayHeadersRowSeparator(String weekdayHeadersRowSeparator) {
        this.weekdayHeadersRowSeparator = weekdayHeadersRowSeparator;
    }

    public String getWeekdayHeadersRowStyle() {
        return ValueBindings.get(this, "weekdayHeadersRowStyle", weekdayHeadersRowStyle);
    }

    public void setWeekdayHeadersRowStyle(String weekdayHeadersRowStyle) {
        this.weekdayHeadersRowStyle = weekdayHeadersRowStyle;
    }

    public String getWeekdayHeadersRowClass() {
        return ValueBindings.get(this, "weekdayHeadersRowClass", weekdayHeadersRowClass);
    }

    public void setWeekdayHeadersRowClass(String weekdayHeadersRowClass) {
        this.weekdayHeadersRowClass = weekdayHeadersRowClass;
    }

    public String getWeekdayStyle() {
        return ValueBindings.get(this, "weekdayStyle", weekdayStyle);
    }

    public void setWeekdayStyle(String weekdayStyle) {
        this.weekdayStyle = weekdayStyle;
    }

    public String getWeekdayClass() {
        return ValueBindings.get(this, "weekdayClass", weekdayClass);
    }

    public void setWeekdayClass(String weekdayClass) {
        this.weekdayClass = weekdayClass;
    }

    public String getWeekdayPattern() {
        return ValueBindings.get(this, "weekdayPattern", weekdayPattern);
    }

    public void setWeekdayPattern(String weekdayPattern) {
        this.weekdayPattern = weekdayPattern;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                firstDayOfWeek,
                weekdayColumnSeparator,
                weekdayHeadersRowSeparator,
                weekdayHeadersRowStyle,
                weekdayHeadersRowClass,
                weekdayStyle,
                weekdayClass,
                weekdayPattern
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        firstDayOfWeek = (Integer) state[i++];
        weekdayColumnSeparator = (String) state[i++];
        weekdayHeadersRowSeparator = (String) state[i++];
        weekdayHeadersRowStyle = (String) state[i++];
        weekdayHeadersRowClass = (String) state[i++];
        weekdayStyle = (String) state[i++];
        weekdayClass = (String) state[i++];
        weekdayPattern = (String) state[i++];
    }

    @Override
    public Timetable.ViewType getType() {
        return Timetable.ViewType.WEEK;
    }
}
