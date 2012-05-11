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

import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TimeScaleTableJspTag extends TimetableViewJspTag {

    protected TimeScaleTableJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setStartTime(ValueExpression value) {
        getDelegate().setPropertyValue("startTime", value);
    }

    public void setEndTime(ValueExpression value) {
        getDelegate().setPropertyValue("endTime", value);
    }

    public void setScrollTime(ValueExpression value) {
        getDelegate().setPropertyValue("scrollTime", value);
    }

    public void setTimeTextPosition(ValueExpression value) {
        getDelegate().setPropertyValue("timeTextPosition", value);
    }

    public void setResourceHeadersRowStyle(ValueExpression value) {
        getDelegate().setPropertyValue("resourceHeadersRowStyle", value);
    }

    public void setResourceHeadersRowClass(ValueExpression value) {
        getDelegate().setPropertyValue("resourceHeadersRowClass", value);
    }

    public void setTimeColumnStyle(ValueExpression value) {
        getDelegate().setPropertyValue("timeColumnStyle", value);
    }

    public void setTimeColumnClass(ValueExpression value) {
        getDelegate().setPropertyValue("timeColumnClass", value);
    }

    public void setMajorTimeStyle(ValueExpression value) {
        getDelegate().setPropertyValue("majorTimeStyle", value);
    }

    public void setMajorTimeClass(ValueExpression value) {
        getDelegate().setPropertyValue("majorTimeClass", value);
    }

    public void setMinorTimeStyle(ValueExpression value) {
        getDelegate().setPropertyValue("minorTimeStyle", value);
    }

    public void setMinorTimeClass(ValueExpression value) {
        getDelegate().setPropertyValue("minorTimeClass", value);
    }

    public void setTimeSuffixStyle(ValueExpression value) {
        getDelegate().setPropertyValue("timeSuffixStyle", value);
    }

    public void setTimeSuffixClass(ValueExpression value) {
        getDelegate().setPropertyValue("timeSuffixClass", value);
    }

    public void setDragAndDropTransitionPeriod(ValueExpression value) {
        getDelegate().setPropertyValue("dragAndDropTransitionPeriod", value);
    }

    public void setDragAndDropCancelingPeriod(ValueExpression value) {
        getDelegate().setPropertyValue("dragAndDropCancelingPeriod", value);
    }

    public void setUndroppableStateTransitionPeriod(ValueExpression value) {
        getDelegate().setPropertyValue("undroppableStateTransitionPeriod", value);
    }

    public void setUndroppableEventTransparency(ValueExpression value) {
        getDelegate().setPropertyValue("undroppableEventTransparency", value);
    }

    public void setResourceColumnSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("resourceColumnSeparator", value);
    }

    public void setResourceHeadersRowSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("resourceHeadersRowSeparator", value);
    }

    public void setTimeColumnSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("timeColumnSeparator", value);
    }

    public void setPrimaryRowSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("primaryRowSeparator", value);
    }

    public void setSecondaryRowSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("secondaryRowSeparator", value);
    }

    public void setTimeColumnPrimaryRowSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("timeColumnPrimaryRowSeparator", value);
    }

    public void setTimeColumnSecondaryRowSeparator(ValueExpression value) {
        getDelegate().setPropertyValue("timeColumnSecondaryRowSeparator", value);
    }

    public void setTimePattern(ValueExpression value) {
        getDelegate().setPropertyValue("timePattern", value);
    }

    public void setTimeSuffixPattern(ValueExpression value) {
        getDelegate().setPropertyValue("timeSuffixPattern", value);
    }

    public void setMajorTimeInterval(ValueExpression value) {
        getDelegate().setPropertyValue("majorTimeInterval", value);
    }

    public void setMinorTimeInterval(ValueExpression value) {
        getDelegate().setPropertyValue("minorTimeInterval", value);
    }

    public void setShowTimeForMinorIntervals(ValueExpression value) {
        getDelegate().setPropertyValue("showTimeForMinorIntervals", value);
    }

}
