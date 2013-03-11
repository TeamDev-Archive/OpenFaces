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

/**
 * @author Roman Porotnikov
 */
public class MonthTable extends TimetableView { // todo: extract some common type for <o:weekTable> and <o:monthTable> to reflect their ability to display week calendar(s)
    public static final String COMPONENT_TYPE = "org.openfaces.MonthTable";
    public static final String COMPONENT_FAMILY = "org.openfaces.MonthTable";

    private Integer firstDayOfWeek;

    private String weekdayHeadersRowStyle;
    private String weekdayHeadersRowClass;
    private String weekdayStyle;
    private String weekdayClass;
    private String weekdayPattern;

    private String weekdayHeadersRowSeparator;
    private String weekdayColumnSeparator;

    private String rowSeparator;
    private String columnSeparator;
    private String dayHeaderRowStyle;
    private String dayHeaderRowClass;

    private String weekdayHeaderCellStyle;
    private String weekdayHeaderCellClass;
    private String weekendWeekdayHeaderCellStyle;
    private String weekendWeekdayHeaderCellClass;
    private String cellHeaderStyle;
    private String cellHeaderClass;
    private String cellStyle;
    private String cellClass;
    private String todayCellHeaderStyle;
    private String todayCellHeaderClass;
    private String todayCellStyle;
    private String todayCellClass;
    private String weekendCellHeaderStyle;
    private String weekendCellHeaderClass;
    private String weekendCellStyle;
    private String weekendCellClass;
    private String inactiveMonthCellHeaderStyle;
    private String inactiveMonthCellHeaderClass;
    private String inactiveMonthCellStyle;
    private String inactiveMonthCellClass;

    private String moreLinkElementStyle;
    private String moreLinkElementClass;
    private String moreLinkStyle;
    private String moreLinkClass;
    private String moreLinkText;
    private String expandedDayViewStyle;
    private String expandedDayViewClass;
    private Integer expandTransitionPeriod;


    private Integer scrollOffset = 0;


    public static final String EXPANDED_DAY_VIEW_HEADER_FACET = "expandedDayHeader";
    public static final String EXPANDED_DAY_VIEW_FOOTER_FACET = "expandedDayFooter";

    public UIComponent getExpandedDayViewFooter() {
        return Components.getFacet(this, EXPANDED_DAY_VIEW_FOOTER_FACET);
    }

    public UIComponent getExpandedDayViewHeader() {
        return Components.getFacet(this, EXPANDED_DAY_VIEW_HEADER_FACET);
    }

    public MonthTable() {
        setRendererType("org.openfaces.MonthTableRenderer");
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

    public String getWeekdayHeadersRowSeparator() {
        return ValueBindings.get(this, "weekdayHeadersRowSeparator", weekdayHeadersRowSeparator);
    }

    public void setWeekdayHeadersRowSeparator(String weekdayHeadersRowSeparator) {
        this.weekdayHeadersRowSeparator = weekdayHeadersRowSeparator;
    }

    public String getWeekdayColumnSeparator() {
        return ValueBindings.get(this, "weekdayColumnSeparator", weekdayColumnSeparator);
    }

    public void setWeekdayColumnSeparator(String weekdayColumnSeparator) {
        this.weekdayColumnSeparator = weekdayColumnSeparator;
    }

    public String getRowSeparator() {
        return ValueBindings.get(this, "rowSeparator", rowSeparator);
    }

    public void setRowSeparator(String rowSeparator) {
        this.rowSeparator = rowSeparator;
    }

    public String getColumnSeparator() {
        return ValueBindings.get(this, "columnSeparator", columnSeparator);
    }

    public void setColumnSeparator(String columnSeparator) {
        this.columnSeparator = columnSeparator;
    }

    public String getDayHeaderRowStyle() {
        return ValueBindings.get(this, "dayHeaderRowStyle", dayHeaderRowStyle);
    }

    public void setDayHeaderRowStyle(String dayHeaderRowStyle) {
        this.dayHeaderRowStyle = dayHeaderRowStyle;
    }

    public String getDayHeaderRowClass() {
        return ValueBindings.get(this, "dayHeaderRowClass", dayHeaderRowClass);
    }

    public void setDayHeaderRowClass(String dayHeaderRowClass) {
        this.dayHeaderRowClass = dayHeaderRowClass;
    }

    public String getWeekdayHeaderCellStyle() {
        return ValueBindings.get(this, "weekdayHeaderCellStyle", weekdayHeaderCellStyle);
    }

    public void setWeekdayHeaderCellStyle(String weekdayHeaderCellStyle) {
        this.weekdayHeaderCellStyle = weekdayHeaderCellStyle;
    }

    public String getWeekdayHeaderCellClass() {
        return ValueBindings.get(this, "weekdayHeaderCellClass", weekdayHeaderCellClass);
    }

    public void setWeekdayHeaderCellClass(String weekdayHeaderCellClass) {
        this.weekdayHeaderCellClass = weekdayHeaderCellClass;
    }

    public String getWeekendWeekdayHeaderCellStyle() {
        return ValueBindings.get(this, "weekendWeekdayHeaderCellStyle", weekendWeekdayHeaderCellStyle);
    }

    public void setWeekendWeekdayHeaderCellStyle(String weekendWeekdayHeaderCellStyle) {
        this.weekendWeekdayHeaderCellStyle = weekendWeekdayHeaderCellStyle;
    }

    public String getWeekendWeekdayHeaderCellClass() {
        return ValueBindings.get(this, "weekendWeekdayHeaderCellClass", weekendWeekdayHeaderCellClass);
    }

    public void setWeekendWeekdayHeaderCellClass(String weekendWeekdayHeaderCellClass) {
        this.weekendWeekdayHeaderCellClass = weekendWeekdayHeaderCellClass;
    }

    public String getCellHeaderStyle() {
        return ValueBindings.get(this, "cellHeaderStyle", cellHeaderStyle);
    }

    public void setCellHeaderStyle(String cellHeaderStyle) {
        this.cellHeaderStyle = cellHeaderStyle;
    }

    public String getCellHeaderClass() {
        return ValueBindings.get(this, "cellHeaderClass", cellHeaderClass);
    }

    public void setCellHeaderClass(String cellHeaderClass) {
        this.cellHeaderClass = cellHeaderClass;
    }

    public String getCellStyle() {
        return ValueBindings.get(this, "cellStyle", cellStyle);
    }

    public void setCellStyle(String cellStyle) {
        this.cellStyle = cellStyle;
    }

    public String getCellClass() {
        return ValueBindings.get(this, "cellClass", cellClass);
    }

    public void setCellClass(String cellClass) {
        this.cellClass = cellClass;
    }

    public String getTodayCellHeaderStyle() {
        return ValueBindings.get(this, "todayCellHeaderStyle", todayCellHeaderStyle);
    }

    public void setTodayCellHeaderStyle(String todayCellHeaderStyle) {
        this.todayCellHeaderStyle = todayCellHeaderStyle;
    }

    public String getTodayCellHeaderClass() {
        return ValueBindings.get(this, "todayCellHeaderClass", todayCellHeaderClass);
    }

    public void setTodayCellHeaderClass(String todayCellHeaderClass) {
        this.todayCellHeaderClass = todayCellHeaderClass;
    }

    public String getTodayCellStyle() {
        return ValueBindings.get(this, "todayCellStyle", todayCellStyle);
    }

    public void setTodayCellStyle(String todayCellStyle) {
        this.todayCellStyle = todayCellStyle;
    }

    public String getTodayCellClass() {
        return ValueBindings.get(this, "todayCellClass", todayCellClass);
    }

    public void setTodayCellClass(String todayCellClass) {
        this.todayCellClass = todayCellClass;
    }

    public String getWeekendCellHeaderStyle() {
        return ValueBindings.get(this, "weekendCellHeaderStyle", weekendCellHeaderStyle);
    }

    public void setWeekendCellHeaderStyle(String weekendCellHeaderStyle) {
        this.weekendCellHeaderStyle = weekendCellHeaderStyle;
    }

    public String getWeekendCellHeaderClass() {
        return ValueBindings.get(this, "weekendCellHeaderClass", weekendCellHeaderClass);
    }

    public void setWeekendCellHeaderClass(String weekendCellHeaderClass) {
        this.weekendCellHeaderClass = weekendCellHeaderClass;
    }

    public String getWeekendCellStyle() {
        return ValueBindings.get(this, "weekendCellStyle", weekendCellStyle);
    }

    public void setWeekendCellStyle(String weekendCellStyle) {
        this.weekendCellStyle = weekendCellStyle;
    }

    public String getWeekendCellClass() {
        return ValueBindings.get(this, "weekendCellClass", weekendCellClass);
    }

    public void setWeekendCellClass(String weekendCellClass) {
        this.weekendCellClass = weekendCellClass;
    }

    public String getInactiveMonthCellHeaderStyle() {
        return ValueBindings.get(this, "inactiveMonthCellHeaderStyle", inactiveMonthCellHeaderStyle);
    }

    public void setInactiveMonthCellHeaderStyle(String inactiveMonthCellHeaderStyle) {
        this.inactiveMonthCellHeaderStyle = inactiveMonthCellHeaderStyle;
    }

    public String getInactiveMonthCellHeaderClass() {
        return ValueBindings.get(this, "inactiveMonthCellHeaderClass", inactiveMonthCellHeaderClass);
    }

    public void setInactiveMonthCellHeaderClass(String inactiveMonthCellHeaderClass) {
        this.inactiveMonthCellHeaderClass = inactiveMonthCellHeaderClass;
    }

    public String getInactiveMonthCellStyle() {
        return ValueBindings.get(this, "inactiveMonthCellStyle", inactiveMonthCellStyle);
    }

    public void setInactiveMonthCellStyle(String inactiveMonthCellStyle) {
        this.inactiveMonthCellStyle = inactiveMonthCellStyle;
    }

    public String getInactiveMonthCellClass() {
        return ValueBindings.get(this, "inactiveMonthCellClass", inactiveMonthCellClass);
    }

    public void setInactiveMonthCellClass(String inactiveMonthCellClass) {
        this.inactiveMonthCellClass = inactiveMonthCellClass;
    }

    public String getMoreLinkElementStyle() {
        return ValueBindings.get(this, "moreLinkElementStyle", moreLinkElementStyle);
    }

    public void setMoreLinkElementStyle(String moreLinkElementStyle) {
        this.moreLinkElementStyle = moreLinkElementStyle;
    }

    public String getMoreLinkElementClass() {
        return ValueBindings.get(this, "moreLinkElementClass", moreLinkElementClass);
    }

    public void setMoreLinkElementClass(String moreLinkElementClass) {
        this.moreLinkElementClass = moreLinkElementClass;
    }

    public String getMoreLinkStyle() {
        return ValueBindings.get(this, "moreLinkStyle", moreLinkStyle);
    }

    public void setMoreLinkStyle(String moreLinkStyle) {
        this.moreLinkStyle = moreLinkStyle;
    }

    public String getMoreLinkClass() {
        return ValueBindings.get(this, "moreLinkClass", moreLinkClass);
    }

    public void setMoreLinkClass(String moreLinkClass) {
        this.moreLinkClass = moreLinkClass;
    }

    public String getMoreLinkText() {
        return ValueBindings.get(this, "moreLinkText", moreLinkText);
    }

    public void setMoreLinkText(String moreLinkText) {
        this.moreLinkText = moreLinkText;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public String getExpandedDayViewStyle() {
        return ValueBindings.get(this, "expandedDayViewStyle", moreLinkClass);
    }

    public void setExpandedDayViewStyle(String expandedDayViewStyle) {
        this.expandedDayViewStyle = expandedDayViewStyle;
    }

    public String getExpandedDayViewClass() {
        return ValueBindings.get(this, "expandedDayViewClass", moreLinkClass);
    }

    public void setExpandedDayViewClass(String expandedDayViewClass) {
        this.expandedDayViewClass = expandedDayViewClass;
    }


    public Integer getExpandTransitionPeriod() {
        return ValueBindings.get(this, "expandTransitionPeriod", expandTransitionPeriod, 200);
    }

    public void setExpandTransitionPeriod(Integer expandTransitionPeriod) {
        this.expandTransitionPeriod = expandTransitionPeriod;
    }



    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                firstDayOfWeek,
                weekdayHeadersRowStyle,
                weekdayHeadersRowClass,
                weekdayStyle,
                weekdayClass,
                weekdayPattern,
                weekdayHeadersRowSeparator,
                weekdayColumnSeparator,
                rowSeparator,
                columnSeparator,
                dayHeaderRowStyle,
                dayHeaderRowClass,
                weekdayHeaderCellStyle,
                weekdayHeaderCellClass,
                weekendWeekdayHeaderCellStyle,
                weekendWeekdayHeaderCellClass,
                cellHeaderStyle,
                cellHeaderClass,
                cellStyle,
                cellClass,
                todayCellHeaderStyle,
                todayCellHeaderClass,
                todayCellStyle,
                todayCellClass,
                weekendCellHeaderStyle,
                weekendCellHeaderClass,
                weekendCellStyle,
                weekendCellClass,
                inactiveMonthCellHeaderStyle,
                inactiveMonthCellHeaderClass,
                inactiveMonthCellStyle,
                inactiveMonthCellClass,
                moreLinkElementStyle,
                moreLinkElementClass,
                moreLinkStyle,
                moreLinkClass,
                moreLinkText,
                expandedDayViewStyle,
                expandedDayViewClass,
                expandTransitionPeriod,
                scrollOffset
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        firstDayOfWeek = (Integer) state[i++];
        weekdayHeadersRowStyle = (String) state[i++];
        weekdayHeadersRowClass = (String) state[i++];
        weekdayStyle = (String) state[i++];
        weekdayClass = (String) state[i++];
        weekdayPattern = (String) state[i++];
        weekdayHeadersRowSeparator = (String) state[i++];
        weekdayColumnSeparator = (String) state[i++];
        rowSeparator = (String) state[i++];
        columnSeparator = (String) state[i++];
        dayHeaderRowStyle = (String) state[i++];
        dayHeaderRowClass = (String) state[i++];
        weekdayHeaderCellStyle = (String) state[i++];
        weekdayHeaderCellClass = (String) state[i++];
        weekendWeekdayHeaderCellStyle = (String) state[i++];
        weekendWeekdayHeaderCellClass = (String) state[i++];
        cellHeaderStyle = (String) state[i++];
        cellHeaderClass = (String) state[i++];
        cellStyle = (String) state[i++];
        cellClass = (String) state[i++];
        todayCellHeaderStyle = (String) state[i++];
        todayCellHeaderClass = (String) state[i++];
        todayCellStyle = (String) state[i++];
        todayCellClass = (String) state[i++];
        weekendCellHeaderStyle = (String) state[i++];
        weekendCellHeaderClass = (String) state[i++];
        weekendCellStyle = (String) state[i++];
        weekendCellClass = (String) state[i++];
        inactiveMonthCellHeaderStyle = (String) state[i++];
        inactiveMonthCellHeaderClass = (String) state[i++];
        inactiveMonthCellStyle = (String) state[i++];
        inactiveMonthCellClass = (String) state[i++];
        moreLinkElementStyle = (String) state[i++];
        moreLinkElementClass = (String) state[i++];
        moreLinkStyle = (String) state[i++];
        moreLinkClass = (String) state[i++];
        moreLinkText = (String) state[i++];
        expandedDayViewStyle = (String) state[i++];
        expandedDayViewClass = (String) state[i++];
        expandTransitionPeriod = (Integer) state[i++];
        scrollOffset = (Integer) state[i++];
    }

    @Override
    public Timetable.ViewType getType() {
        return Timetable.ViewType.MONTH;
    }

}
