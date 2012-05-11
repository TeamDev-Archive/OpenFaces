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
package org.openfaces.taglib.jsp.calendar;

import org.openfaces.taglib.internal.calendar.SimpleDateRangeTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Kharchenko
 */
public class SimpleDateRangeJspTag extends AbstractComponentJspTag {

    public SimpleDateRangeJspTag() {
        super(new SimpleDateRangeTag());
    }

    public void setFromDate(ValueExpression fromDate) {
        getDelegate().setPropertyValue("fromDate", fromDate);
    }

    public void setToDate(ValueExpression toDate) {
        getDelegate().setPropertyValue("toDate", toDate);
    }

    public void setDayStyle(ValueExpression style) {
        getDelegate().setPropertyValue("dayStyle", style);
    }

    public void setRolloverDayStyle(ValueExpression rolloverDayStyle) {
        getDelegate().setPropertyValue("rolloverDayStyle", rolloverDayStyle);
    }

    public void setDayClass(ValueExpression dayClass) {
        getDelegate().setPropertyValue("dayClass", dayClass);
    }

    public void setRolloverDayClass(ValueExpression rolloverDayClass) {
        getDelegate().setPropertyValue("rolloverDayClass", rolloverDayClass);
    }

    public void setSelectedDayStyle(ValueExpression selectedDayStyle) {
        getDelegate().setPropertyValue("selectedDayStyle", selectedDayStyle);
    }

    public void setRolloverSelectedDayStyle(ValueExpression rolloverSelectedDayStyle) {
        getDelegate().setPropertyValue("rolloverSelectedDayStyle", rolloverSelectedDayStyle);
    }

    public void setSelectedDayClass(ValueExpression selectedDayClass) {
        getDelegate().setPropertyValue("selectedDayClass", selectedDayClass);
    }

    public void setRolloverSelectedDayClass(ValueExpression rolloverSelectedDayClass) {
        getDelegate().setPropertyValue("rolloverSelectedDayClass", rolloverSelectedDayClass);
    }
}
