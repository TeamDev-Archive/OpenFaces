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

import org.openfaces.taglib.internal.timetable.TimetableEventTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class TimetableEventJspTag extends AbstractComponentJspTag {

    public TimetableEventJspTag() {
        super(new TimetableEventTag());
    }

    public void setOncreate(ValueExpression value) {
        getDelegate().setPropertyValue("oncreate", value);
    }

    public void setEscapeName(ValueExpression value) {
        getDelegate().setPropertyValue("escapeName", value);
    }

    public void setEscapeDescription(ValueExpression value) {
        getDelegate().setPropertyValue("escapeDescription", value);
    }

    public void setNameStyle(ValueExpression value) {
        getDelegate().setPropertyValue("nameStyle", value);
    }

    public void setNameClass(ValueExpression value) {
        getDelegate().setPropertyValue("nameClass", value);
    }

    public void setDescriptionStyle(ValueExpression value) {
        getDelegate().setPropertyValue("descriptionStyle", value);
    }

    public void setDescriptionClass(ValueExpression value) {
        getDelegate().setPropertyValue("descriptionClass", value);
    }

    public void setBackgroundTransparency(ValueExpression value) {
        getDelegate().setPropertyValue("backgroundTransparency", value);
    }

    public void setBackgroundIntensity(ValueExpression value) {
        getDelegate().setPropertyValue("backgroundIntensity", value);
    }


}
