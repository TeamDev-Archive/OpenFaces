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
import org.openfaces.taglib.internal.timetable.EventActionTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class EventActionJspTag extends AbstractComponentJspTag {
    protected EventActionJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public EventActionJspTag() {
        super(new EventActionTag());
    }

    public void setPressedStyle(ValueExpression value) {
        getDelegate().setPropertyValue("pressedStyle", value);
    }

    public void setPressedClass(ValueExpression value) {
        getDelegate().setPropertyValue("pressedClass", value);
    }

    public void setActionListener(MethodExpression value) {
        getDelegate().setPropertyValue("actionListener", value);
    }

    public void setImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("imageUrl", value);
    }

    public void setRolloverImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("rolloverImageUrl", value);
    }

    public void setPressedImageUrl(ValueExpression value) {
        getDelegate().setPropertyValue("pressedImageUrl", value);
    }

    public void setHint(ValueExpression value) {
        getDelegate().setPropertyValue("hint", value);
    }

    public void setScope(ValueExpression scope) {
        getDelegate().setPropertyValue("scope", scope);
    }
}
