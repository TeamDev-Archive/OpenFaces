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
package org.openfaces.taglib.internal.calendar;

import org.openfaces.component.calendar.Calendar;
import org.openfaces.taglib.internal.AbstractUIInputTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class CalendarTag extends AbstractUIInputTag {
    public String getComponentType() {
        return Calendar.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.CalendarRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        setValueExpressionProperty(component, "value");

        setTimeZoneProperty(component, "timeZone");

        setIntProperty(component, "firstDayOfWeek");
        setStringProperty(component, "onperiodchange");

        setLocaleProperty(component, "locale");

        setStringProperty(component, "todayText");
        setStringProperty(component, "noneText");
        setBooleanProperty(component, "keepTime");
        setBooleanProperty(component, "showFooter");

        setStringProperty(component, "dayStyle");
        setStringProperty(component, "disabledStyle");
        setStringProperty(component, "rolloverDayStyle");
        setStringProperty(component, "inactiveMonthDayStyle");
        setStringProperty(component, "rolloverInactiveMonthDayStyle");
        setStringProperty(component, "selectedDayStyle");
        setStringProperty(component, "rolloverSelectedDayStyle");
        setStringProperty(component, "todayStyle");
        setStringProperty(component, "rolloverTodayStyle");
        setStringProperty(component, "disabledDayStyle");
        setStringProperty(component, "rolloverDisabledDayStyle");
        setStringProperty(component, "daysHeaderStyle");
        setStringProperty(component, "weekendDayStyle");
        setStringProperty(component, "rolloverWeekendDayStyle");
        setStringProperty(component, "headerStyle");
        setStringProperty(component, "footerStyle");

        setStringProperty(component, "dayClass");
        setStringProperty(component, "disabledClass");
        setStringProperty(component, "rolloverDayClass");
        setStringProperty(component, "inactiveMonthDayClass");
        setStringProperty(component, "rolloverInactiveMonthDayClass");
        setStringProperty(component, "selectedDayClass");
        setStringProperty(component, "rolloverSelectedDayClass");
        setStringProperty(component, "todayClass");
        setStringProperty(component, "rolloverTodayClass");
        setStringProperty(component, "disabledDayClass");
        setStringProperty(component, "rolloverDisabledDayClass");
        setStringProperty(component, "daysHeaderClass");
        setStringProperty(component, "weekendDayClass");
        setStringProperty(component, "rolloverWeekendDayClass");
        setStringProperty(component, "headerClass");
        setStringProperty(component, "footerClass");

        setStringProperty(component, "bodyStyle");
        setStringProperty(component, "bodyClass");

        setBooleanProperty(component, "focusable");
    }

}
