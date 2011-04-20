/*
 * OpenFaces - JSF Component Library 3.0
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

import org.openfaces.component.calendar.Calendar;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Natalia Zolochevska
 */
public class DaySwitcher extends AbstractSwitcher<DayTable> {
    public static final String COMPONENT_TYPE = "org.openfaces.DaySwitcher";
    public static final String COMPONENT_FAMILY = "org.openfaces.DaySwitcher";

    private String upperDateFormat;
    private String upperPattern;

    private String upperTextStyle;
    private String upperTextClass;
    private Boolean popupCalendarEnabled;

    @Override
    protected String getTimetableNotFoundMsg() {
        return "DaySwitcher's \"for\" attribute must refer to a DayTable component.";
    }

    public String getUpperDateFormat() {
        return ValueBindings.get(this, "upperDateFormat", upperDateFormat);
    }

    public void setUpperDateFormat(String upperDateFormat) {
        this.upperDateFormat = upperDateFormat;
    }

    public String getUpperPattern() {
        return ValueBindings.get(this, "upperPattern", upperPattern);
    }

    public void setUpperPattern(String upperPattern) {
        this.upperPattern = upperPattern;
    }

    public String getUpperTextStyle() {
        return ValueBindings.get(this, "upperTextStyle", upperTextStyle);
    }

    public void setUpperTextStyle(String upperTextStyle) {
        this.upperTextStyle = upperTextStyle;
    }

    public String getUpperTextClass() {
        return ValueBindings.get(this, "upperTextClass", upperTextClass);
    }

    public void setUpperTextClass(String upperTextClass) {
        this.upperTextClass = upperTextClass;
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                upperDateFormat,
                upperPattern,
                upperTextStyle,
                upperTextClass,
                popupCalendarEnabled
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

        upperDateFormat = (String) state[i++];
        upperPattern = (String) state[i++];

        upperTextStyle = (String) state[i++];
        upperTextClass = (String) state[i++];

        popupCalendarEnabled = (Boolean) state[i++];
    }

    public boolean isPopupCalendarEnabled() {
        return ValueBindings.get(this, "popupCalendarEnabled", popupCalendarEnabled, true);
    }

    public void setPopupCalendarEnabled(boolean popupCalendarEnabled) {
        this.popupCalendarEnabled = popupCalendarEnabled;
    }

    public Calendar getPopupCalendar() {
        Calendar calendar = Components.getOrCreateFacet(
                getFacesContext(), this, Calendar.COMPONENT_TYPE, "popupCalendar", Calendar.class);
        return calendar;
    }

}