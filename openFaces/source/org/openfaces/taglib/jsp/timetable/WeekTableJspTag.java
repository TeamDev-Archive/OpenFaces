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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.WeekTableTag;

import javax.el.ValueExpression;

/**
 * @author Roman Porotnikov
 */
public class WeekTableJspTag extends TimeScaleTableJspTag {
    public WeekTableJspTag() {
        super(new WeekTableTag());
    }

    public void setWeekdayColumnSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayColumnSeparator", value);
    }

    public void setWeekdayHeadersRowSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayHeadersRowSeparator", value);
    }

    public void setWeekdayHeadersRowStyle(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayHeadersRowStyle", value);
    }

    public void setWeekdayHeadersRowClass(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayHeadersRowClass", value);
    }

    public void setWeekdayStyle(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayStyle", value);
    }

    public void setWeekdayClass(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayClass", value);
    }

    public void setWeekdayPattern(ValueExpression value) {
        getDelegate().setPropertyValue("weekdayPattern", value);
    }

}
