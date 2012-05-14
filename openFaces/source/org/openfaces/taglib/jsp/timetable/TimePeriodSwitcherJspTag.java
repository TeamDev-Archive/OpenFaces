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

import org.openfaces.taglib.internal.timetable.TimePeriodSwitcherTag;

import javax.el.ValueExpression;

public class TimePeriodSwitcherJspTag extends AbstractSwitcherJspTag {

    public TimePeriodSwitcherJspTag() {
        super(new TimePeriodSwitcherTag());
    }

    public void setMonthPattern(ValueExpression monthPattern) {
        getDelegate().setPropertyValue("monthPattern", monthPattern);
    }

    public void setFromWeekPattern(ValueExpression fromWeekPattern) {
        getDelegate().setPropertyValue("fromWeekPattern", fromWeekPattern);
    }

    public void setToWeekPattern(ValueExpression toWeekPattern) {
        getDelegate().setPropertyValue("toWeekPattern", toWeekPattern);
    }

    public void setDayPattern(ValueExpression dayPattern) {
        getDelegate().setPropertyValue("dayPattern", dayPattern);
    }

    public void setDayUpperPattern(ValueExpression dayUpperPattern) {
        getDelegate().setPropertyValue("dayUpperPattern", dayUpperPattern);
    }
}
