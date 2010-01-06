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
package org.openfaces.taglib.internal.input;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class DateChooserTag extends DropDownComponentTag {
    public String getComponentType() {
        return "org.openfaces.DateChooser";
    }

    public String getRendererType() {
        return "org.openfaces.DateChooserRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setIntProperty(component, "firstDayOfWeek");
        setPropertyBinding(component, "value");
        setTimeZoneProperty(component, "timeZone");
        setStringProperty(component, "dateFormat");
        setStringProperty(component, "pattern");
        setLocaleProperty(component, "locale");

        setStringProperty(component, "todayText");
        setStringProperty(component, "noneText");
        setBooleanProperty(component, "keepTime");
        setBooleanProperty(component, "showFooter");
        setBooleanProperty(component, "typingAllowed");

        setStringProperty(component, "calendarStyle");
        setStringProperty(component, "dayStyle");
        setStringProperty(component, "rolloverDayStyle");
        setStringProperty(component, "inactiveMonthDayStyle");
        setStringProperty(component, "rolloverInactiveMonthDayStyle");
        setStringProperty(component, "selectedDayStyle");
        setStringProperty(component, "rolloverSelectedDayStyle");
        setStringProperty(component, "todayStyle");
        setStringProperty(component, "rolloverTodayStyle");
        setStringProperty(component, "disabledDayStyle");
        setStringProperty(component, "rolloverDisabledDayStyle");
        setStringProperty(component, "weekendDayStyle");
        setStringProperty(component, "rolloverWeekendDayStyle");
        setStringProperty(component, "daysHeaderStyle");
        setStringProperty(component, "headerStyle");
        setStringProperty(component, "footerStyle");

        setStringProperty(component, "calendarClass");
        setStringProperty(component, "dayClass");
        setStringProperty(component, "rolloverDayClass");
        setStringProperty(component, "inactiveMonthDayClass");
        setStringProperty(component, "rolloverInactiveMonthDayClass");
        setStringProperty(component, "selectedDayClass");
        setStringProperty(component, "rolloverSelectedDayClass");
        setStringProperty(component, "todayClass");
        setStringProperty(component, "rolloverTodayClass");
        setStringProperty(component, "disabledDayClass");
        setStringProperty(component, "rolloverDisabledDayClass");
        setStringProperty(component, "weekendDayClass");
        setStringProperty(component, "rolloverWeekendDayClass");
        setStringProperty(component, "daysHeaderClass");
        setStringProperty(component, "headerClass");
        setStringProperty(component, "footerClass");
    }
}
