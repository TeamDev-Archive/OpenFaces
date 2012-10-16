/*
 * OpenFaces - JSF Component Library 2.0
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

import org.openfaces.taglib.internal.timetable.ScrollButtonTag;

import javax.el.ValueExpression;

public class ScrollButtonJspTag extends org.openfaces.taglib.jsp.AbstractComponentJspTag {

    public ScrollButtonJspTag() {
        super(new ScrollButtonTag());
    }

    public void setButtonStyle(ValueExpression buttonStyle) {
        getDelegate().setPropertyValue("buttonStyle", buttonStyle);
    }

    public void setButtonClass(ValueExpression buttonClass) {
        getDelegate().setPropertyValue("buttonClass", buttonClass);
    }

    public void setRolloverButtonStyle(ValueExpression rolloverButtonStyle) {
        getDelegate().setPropertyValue("rolloverButtonStyle", rolloverButtonStyle);
    }

    public void setRolloverButtonClass(ValueExpression rolloverButtonClass) {
        getDelegate().setPropertyValue("rolloverButtonClass", rolloverButtonClass);
    }

    public void setButtonImg(ValueExpression buttonImg) {
        getDelegate().setPropertyValue("buttonImg", buttonImg);
    }

    public void setScrollDirection(ValueExpression scrollDirection) {
        getDelegate().setPropertyValue("scrollDirection", scrollDirection);
    }

}
