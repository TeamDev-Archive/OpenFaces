/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class Timetable extends TimeScaleTable {
    public static final String COMPONENT_TYPE = "org.openfaces.Timetable";
    public static final String COMPONENT_FAMILY = "org.openfaces.Timetable";
    private static final String FACET_DAY_VIEW = "dayView";
    private static final String FACET_WEEK_VIEW = "weekView";
    private static final String FACET_MONTH_VIEW = "monthView";
    private String headerRightStyle;
    private String headerRightClass;

    public String getHeaderRightStyle() {
        return ValueBindings.get(this, "headerRightStyle", headerRightStyle);
    }

    public void setHeaderRightStyle(String headerRightStyle) {
        this.headerRightStyle = headerRightStyle;
    }

    public String getHeaderRightClass() {
        return ValueBindings.get(this, "headerRightClass", headerRightClass);
    }

    public void setHeaderRightClass(String headerRightClass) {
        this.headerRightClass = headerRightClass;
    }

    public static enum View {
        MONTH("month"),
        WEEK("week"),
        DAY("day");

        private String value;

        View(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return value;
        }
    }

    private View view;

    public Timetable() {
        setRendererType("org.openfaces.TimetableRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                view,
                headerRightClass,
                headerRightClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        view = (View) state[i++];
        headerRightStyle = (String) state[i++];
        headerRightClass = (String) state[i++];
    }

    public View getView() {
        return ValueBindings.get(this, "view", view, View.MONTH, View.class);
    }

    public void setView(View view) {
        this.view = view;
    }

    public DayTable getDayView() {
        DayTable result = Components.getChildWithClass(this, DayTable.class, "dayView");
        return result;
    }

    public WeekTable getWeekView() {
        WeekTable result = Components.getChildWithClass(this, WeekTable.class, "weekView");
        return result;
    }

    public MonthTable getMonthView() {
        MonthTable result = Components.getChildWithClass(this, MonthTable.class, "monthView");
        return result;
    }

    @Override
    public UIComponent getHeaderRight() {
        UIComponent headerRight = super.getHeaderRight();
        if (headerRight == null) {
            return getDefaultViewSwitcher();
        }
        return headerRight;
    }

    private TimetableViewSwitcher getDefaultViewSwitcher() {
        return Components.getOrCreateFacet(getFacesContext(), this, TimetableViewSwitcher.COMPONENT_TYPE,
                "_defaultViewSwitcher", TimetableViewSwitcher.class);
    }
}
