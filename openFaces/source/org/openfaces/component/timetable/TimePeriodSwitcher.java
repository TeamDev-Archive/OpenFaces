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

import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

public class TimePeriodSwitcher extends AbstractSwitcher<Timetable> {
    public static final String COMPONENT_TYPE = "org.openfaces.TimePeriodSwitcher";
    public static final String COMPONENT_FAMILY = "org.openfaces.TimePeriodSwitcher";
    private String monthPattern;
    private String fromWeekPattern;
    private String toWeekPattern;
    private String dayPattern;
    private String dayUpperPattern;

    public TimePeriodSwitcher() {
        setRendererType("org.openfaces.TimePeriodSwitcherRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                monthPattern,
                fromWeekPattern,
                toWeekPattern,
                dayPattern,
                dayUpperPattern
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        monthPattern = (String) state[i++];
        fromWeekPattern = (String) state[i++];
        toWeekPattern = (String) state[i++];
        dayPattern = (String) state[i++];
        dayUpperPattern = (String) state[i++];
    }

    @Override
    public Timetable.ViewType getApplicableViewType() {
        throw new UnsupportedOperationException("getApplicableViewType() is not expected to be invoked for " +
                "TimePeriodSwitcher, which supports all view types");
    }


    public DaySwitcher getDaySwitcher() {
        DaySwitcher result = Components.getChildWithClass(this, DaySwitcher.class, "daySwitcher");
        return result;
    }

    public WeekSwitcher getWeekSwitcher() {
        WeekSwitcher result = Components.getChildWithClass(this, WeekSwitcher.class, "weekSwitcher");
        return result;
    }

    public MonthSwitcher getMonthSwitcher() {
        MonthSwitcher result = Components.getChildWithClass(this, MonthSwitcher.class, "monthSwitcher");
        return result;
    }

    public String getMonthPattern() {
        return ValueBindings.get(this, "monthPattern", monthPattern);
    }

    public void setMonthPattern(String monthPattern) {
        this.monthPattern = monthPattern;
    }

    public String getFromWeekPattern() {
        return ValueBindings.get(this, "fromWeekPattern", fromWeekPattern);
    }

    public void setFromWeekPattern(String fromWeekPattern) {
        this.fromWeekPattern = fromWeekPattern;
    }

    public String getToWeekPattern() {
        return ValueBindings.get(this, "toWeekPattern", toWeekPattern);
    }

    public void setToWeekPattern(String toWeekPattern) {
        this.toWeekPattern = toWeekPattern;
    }

    public String getDayPattern() {
        return ValueBindings.get(this, "dayPattern", dayPattern);
    }

    public void setDayPattern(String dayPattern) {
        this.dayPattern = dayPattern;
    }

    public String getDayUpperPattern() {
        return ValueBindings.get(this, "dayUpperPattern", dayUpperPattern);
    }

    public void setDayUpperPattern(String dayUpperPattern) {
        this.dayUpperPattern = dayUpperPattern;
    }
}
