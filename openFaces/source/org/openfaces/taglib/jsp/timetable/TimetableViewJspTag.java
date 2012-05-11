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
 * @author Roman Porotnikov
 */
public abstract class TimetableViewJspTag extends AbstractComponentJspTag {

    protected TimetableViewJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }
    
    public void setEventVar(ValueExpression eventVar) {
        getDelegate().setPropertyValue("eventVar", eventVar);
    }

    public void setEvents(ValueExpression events) {
        getDelegate().setPropertyValue("events", events);
    }

    public void setResources(ValueExpression resources) {
        getDelegate().setPropertyValue("resources", resources);
    }

    public void setDay(ValueExpression day) {
        getDelegate().setPropertyValue("day", day);
    }

    public void setLocale(ValueExpression locale) {
        getDelegate().setPropertyValue("locale", locale);
    }

    public void setTimeZone(ValueExpression timeZone) {
        getDelegate().setPropertyValue("timeZone", timeZone);
    }

    public void setEditable(ValueExpression editable) {
        getDelegate().setPropertyValue("editable", editable);
    }

    public void setPreloadedEvents(ValueExpression value) {
        getDelegate().setPropertyValue("preloadedEvents", value);
    }

    public void setHeaderStyle(ValueExpression value) {
        getDelegate().setPropertyValue("headerStyle", value);
    }

    public void setHeaderClass(ValueExpression value) {
        getDelegate().setPropertyValue("headerClass", value);
    }

    public void setFooterStyle(ValueExpression value) {
        getDelegate().setPropertyValue("footerStyle", value);
    }

    public void setFooterClass(ValueExpression value) {
        getDelegate().setPropertyValue("footerClass", value);
    }

    public void setDefaultEventColor(ValueExpression value) {
        getDelegate().setPropertyValue("defaultEventColor", value);
    }

    public void setReservedTimeEventColor(ValueExpression value) {
        getDelegate().setPropertyValue("reservedTimeEventColor", value);
    }

    public void setReservedTimeEventStyle(ValueExpression value) {
        getDelegate().setPropertyValue("reservedTimeEventStyle", value);
    }

    public void setReservedTimeEventClass(ValueExpression value) {
        getDelegate().setPropertyValue("reservedTimeEventClass", value);
    }

    public void setRolloverEventNoteStyle(ValueExpression value) {
        getDelegate().setPropertyValue("rolloverEventNoteStyle", value);
    }

    public void setRolloverEventNoteClass(ValueExpression value) {
        getDelegate().setPropertyValue("rolloverEventNoteClass", value);
    }

    public void setRowStyle(ValueExpression value) {
        getDelegate().setPropertyValue("rowStyle", value);
    }

    public void setRowClass(ValueExpression value) {
        getDelegate().setPropertyValue("rowClass", value);
    }

    public void setTimetableChangeListener(MethodExpression value) {
        getDelegate().setPropertyValue("timetableChangeListener", value);
    }

    public void setOnchange(ValueExpression value) {
        getDelegate().setPropertyValue("onchange", value);
    }

}
