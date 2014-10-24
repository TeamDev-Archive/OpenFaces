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
package org.openfaces.taglib.internal.timetable;

import org.openfaces.component.timetable.MonthTable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Roman Porotnikov
 */
public class MonthTableTag extends TimetableViewTag {
    public String getComponentType() {
        return MonthTable.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.MonthTableRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setIntProperty(component, "firstDayOfWeek");

        setStringProperty(component, "weekdayHeadersRowStyle");
        setStringProperty(component, "weekdayHeadersRowClass");
        setStringProperty(component, "weekdayStyle");
        setStringProperty(component, "weekdayClass");
        setStringProperty(component, "weekdayPattern");

        setStringProperty(component, "weekdayHeadersRowSeparator");
        setStringProperty(component, "weekdayColumnSeparator");
        setStringProperty(component, "rowSeparator");
        setStringProperty(component, "columnSeparator");
        setStringProperty(component, "dayHeaderRowStyle");
        setStringProperty(component, "dayHeaderRowClass");

        setStringProperty(component, "weekdayHeaderCellStyle");
        setStringProperty(component, "weekdayHeaderCellClass");
        setStringProperty(component, "weekendWeekdayHeaderCellStyle");
        setStringProperty(component, "weekendWeekdayHeaderCellClass");
        setStringProperty(component, "cellHeaderStyle");
        setStringProperty(component, "cellHeaderClass");
        setStringProperty(component, "cellStyle");
        setStringProperty(component, "cellClass");
        setStringProperty(component, "todayCellHeaderStyle");
        setStringProperty(component, "todayCellHeaderClass");
        setStringProperty(component, "todayCellStyle");
        setStringProperty(component, "todayCellClass");
        setStringProperty(component, "weekendCellHeaderStyle");
        setStringProperty(component, "weekendCellHeaderClass");
        setStringProperty(component, "weekendCellStyle");
        setStringProperty(component, "weekendCellClass");
        setStringProperty(component, "inactiveMonthCellHeaderStyle");
        setStringProperty(component, "inactiveMonthCellHeaderClass");
        setStringProperty(component, "inactiveMonthCellStyle");
        setStringProperty(component, "inactiveMonthCellClass");
        setStringProperty(component, "moreLinkElementStyle");
        setStringProperty(component, "moreLinkElementClass");
        setStringProperty(component, "moreLinkStyle");
        setStringProperty(component, "moreLinkClass");
        setStringProperty(component, "moreLinkText");
        setStringProperty(component, "expandedDayViewStyle");
        setStringProperty(component, "expandedDayViewClass");
        setStringProperty(component, "expandTransitionPeriod");

    }
}
