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
package org.openfaces.taglib.jsp.timetable;

import org.openfaces.taglib.internal.timetable.EventEditorPageTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class EventEditorPageJspTag extends AbstractComponentJspTag {

    public EventEditorPageJspTag() {
        super(new EventEditorPageTag());
    }

    public void setUrl(ValueExpression url) {
        getDelegate().setPropertyValue("url", url);
    }

    public void setAction(MethodExpression action) {
        getDelegate().setPropertyValue("action", action);
    }

    public void setModeParamName(ValueExpression modeParamName) {
        getDelegate().setPropertyValue("modeParamName", modeParamName);
    }

    public void setEventIdParamName(ValueExpression eventIdParamName) {
        getDelegate().setPropertyValue("eventIdParamName", eventIdParamName);
    }

    public void setEventStartParamName(ValueExpression eventStartParamName) {
        getDelegate().setPropertyValue("eventStartParamName", eventStartParamName);
    }

    public void setEventEndParamName(ValueExpression eventEndParamName) {
        getDelegate().setPropertyValue("eventEndParamName", eventEndParamName);
    }

    public void setResourceIdParamName(ValueExpression resourceIdParamName) {
        getDelegate().setPropertyValue("resourceIdParamName", resourceIdParamName);
    }


}