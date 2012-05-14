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

import org.openfaces.taglib.internal.timetable.DaySwitcherTag;

import javax.el.ValueExpression;

/**
 * @author Natalia Zolochevska
 */
public class DaySwitcherJspTag extends AbstractSwitcherJspTag {

    public DaySwitcherJspTag() {
        super(new DaySwitcherTag());
    }

    public void setUpperDateFormat(ValueExpression upperDateFormat) {
        getDelegate().setPropertyValue("upperDateFormat", upperDateFormat);
    }

    public void setUpperPattern(ValueExpression upperPattern) {
        getDelegate().setPropertyValue("upperPattern", upperPattern);
    }

    public void setUpperTextStyle(ValueExpression textStyle) {
        getDelegate().setPropertyValue("upperTextStyle", textStyle);
    }

    public void setUpperTextClass(ValueExpression textClass) {
        getDelegate().setPropertyValue("upperTextClass", textClass);
    }

    public void setPopupCalendarEnabled(ValueExpression popupCalendarEnabled) {
        getDelegate().setPropertyValue("popupCalendarEnabled", popupCalendarEnabled);
    }

}