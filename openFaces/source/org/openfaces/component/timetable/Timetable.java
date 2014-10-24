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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Timetable extends TimeScaleTable {
    public static final String COMPONENT_TYPE = "org.openfaces.Timetable";
    public static final String COMPONENT_FAMILY = "org.openfaces.Timetable";

    private static final String FACET_DAY_VIEW = "dayView";
    private static final String FACET_WEEK_VIEW = "weekView";
    private static final String FACET_MONTH_VIEW = "monthView";

    private String onviewtypechange;


    public String getOnviewtypechange() {
        return ValueBindings.get(this, "onviewtypechange", onviewtypechange);
    }

    public void setOnviewtypechange(String onviewtypechange) {
        this.onviewtypechange = onviewtypechange;
    }

    public static enum ViewType {
        MONTH("month"),
        WEEK("week"),
        DAY("day");

        private String value;

        ViewType(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return value;
        }
    }

    private ViewType viewType;

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
                viewType,
                onviewtypechange
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        viewType = (ViewType) state[i++];
        onviewtypechange = (String) state[i++];
    }

    public ViewType getViewType() {
        return ValueBindings.get(this, "viewType", viewType, ViewType.MONTH, ViewType.class);
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
    }

    public TimetableView getViewByType(ViewType viewType) {
        switch(viewType) {
            case MONTH:
                return getMonthView();
            case WEEK:
                return getWeekView();
            case DAY:
                return getDayView();
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
    }

    public DayTable getDayView() {
        DayTable result = Components.getChildWithClass(this, DayTable.class, FACET_DAY_VIEW);
        return result;
    }

    public void setDayView(DayTable dayView) {
        getFacets().put(FACET_DAY_VIEW, dayView);
    }

    public WeekTable getWeekView() {
        WeekTable result = Components.getChildWithClass(this, WeekTable.class, FACET_WEEK_VIEW);
        return result;
    }

    public void setWeekView(WeekTable weekView) {
        getFacets().put(FACET_WEEK_VIEW, weekView);
    }

    public MonthTable getMonthView() {
        MonthTable result = Components.getChildWithClass(this, MonthTable.class, FACET_MONTH_VIEW);
        return result;
    }

    public void setMonthView(MonthTable monthView) {
        getFacets().put(FACET_MONTH_VIEW, monthView);
    }

    @Override
    public UIComponent getHeaderRight() {
        UIComponent headerRight = super.getHeaderRight();
        if (headerRight == null) {
            return getDefaultViewSwitcher();
        }
        return headerRight;
    }

    @Override
    public ViewType getType() {
        throw new UnsupportedOperationException("getType() is not expected to be called on Timetable, which " +
                "embeds all view types");
    }

    private TimetableViewSwitcher getDefaultViewSwitcher() {
        return Components.getOrCreateFacet(getFacesContext(), this, TimetableViewSwitcher.COMPONENT_TYPE,
                "_defaultViewSwitcher", TimetableViewSwitcher.class);
    }

    @Override
    protected AbstractTimetableEvent getLoadedEventByObjectId(String objectId) {
        AbstractTimetableEvent event = super.getLoadedEventByObjectId(objectId);
        if (event != null) return event;
        TimetableView currentView = getViewByType(getViewType());
        event = currentView.getLoadedEventByObjectId(objectId);
        if (event != null) return event;
        List<TimetableView> views = getViews();
        for (TimetableView view : views) {
            if (view == currentView) continue;
            event = view.getLoadedEventByObjectId(objectId);
            if (event != null)
                return event;
        }
        return null;
    }

    protected List<ViewType> getViewTypes() {
        return Arrays.asList(ViewType.MONTH, ViewType.WEEK, ViewType.DAY);
    }

    protected List<TimetableView> getViews() {
        List<ViewType> viewTypes = getViewTypes();
        List<TimetableView> views = new ArrayList(viewTypes.size());
        for (ViewType viewType : viewTypes) {
            TimetableView view = getViewByType(viewType);
            if (view == null)
                throw new IllegalStateException("Couldn't find view by type: " + viewType);
            views.add(view);
        }
        return views;
    }
}
