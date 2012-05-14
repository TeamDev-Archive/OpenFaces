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
package org.openfaces.component.calendar;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.util.Date;

/**
 * @author Kharchenko
 */
public class SimpleDateRange extends AbstractDateRange {
    public static final String COMPONENT_TYPE = "org.openfaces.SimpleDateRange";
    public static final String COMPONENT_FAMILY = "org.openfaces.SimpleDateRange";

    private String dayStyle;
    private String rolloverDayStyle;

    private String dayClass;
    private String rolloverDayClass;

    private String selectedDayStyle;
    private String rolloverSelectedDayStyle;

    private String selectedDayClass;
    private String rolloverSelectedDayClass;

    private Date fromDate; // required property
    private Date toDate; // required property

    public SimpleDateRange() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Date getFromDate() {
        return ValueBindings.get(this, "fromDate", fromDate, Date.class);
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return ValueBindings.get(this, "toDate", toDate, Date.class);
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getDayStyle() {
        return ValueBindings.get(this, "style", dayStyle);
    }

    public void setDayStyle(String dayStyle) {
        this.dayStyle = dayStyle;
    }

    public String getRolloverDayStyle() {
        return ValueBindings.get(this, "rolloverStyle", rolloverDayStyle);
    }

    public void setRolloverDayStyle(String rolloverDayStyle) {
        this.rolloverDayStyle = rolloverDayStyle;
    }

    public String getDayClass() {
        return ValueBindings.get(this, "dayClass", dayClass);
    }

    public void setDayClass(String dayClass) {
        this.dayClass = dayClass;
    }

    public String getRolloverDayClass() {
        return ValueBindings.get(this, "rolloverDayClass", rolloverDayClass);
    }

    public void setRolloverDayClass(String rolloverDayClass) {
        this.rolloverDayClass = rolloverDayClass;
    }

    public String getSelectedDayStyle() {
        return ValueBindings.get(this, "selectedDayStyle", selectedDayStyle);
    }

    public void setSelectedDayStyle(String selectedDayStyle) {
        this.selectedDayStyle = selectedDayStyle;
    }

    public String getRolloverSelectedDayStyle() {
        return ValueBindings.get(this, "rolloverSelectedDayStyle", rolloverSelectedDayStyle);
    }

    public void setRolloverSelectedDayStyle(String rolloverSelectedDayStyle) {
        this.rolloverSelectedDayStyle = rolloverSelectedDayStyle;
    }

    public String getSelectedDayClass() {
        return ValueBindings.get(this, "selectedDayClass", selectedDayClass);
    }

    public void setSelectedDayClass(String selectedDayClass) {
        this.selectedDayClass = selectedDayClass;
    }

    public String getRolloverSelectedDayClass() {
        return ValueBindings.get(this, "rolloverSelectedDayClass", rolloverSelectedDayClass);
    }

    public void setRolloverSelectedDayClass(String rolloverSelectedDayClass) {
        this.rolloverSelectedDayClass = rolloverSelectedDayClass;
    }

    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context), fromDate, toDate, dayStyle, rolloverDayStyle, dayClass,
                rolloverDayClass, selectedDayStyle, rolloverSelectedDayStyle, selectedDayClass,
                rolloverSelectedDayClass};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        fromDate = (Date) values[i++];
        toDate = (Date) values[i++];
        dayStyle = (String) values[i++];
        rolloverDayStyle = (String) values[i++];
        dayClass = (String) values[i++];
        rolloverDayClass = (String) values[i++];
        selectedDayStyle = (String) values[i++];
        rolloverSelectedDayStyle = (String) values[i++];
        selectedDayClass = (String) values[i++];
        rolloverSelectedDayClass = (String) values[i++];
    }

}
