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

import javax.el.ValueExpression;

public class DayViewJspTag extends org.openfaces.taglib.jsp.AbstractComponentJspTag {

    public DayViewJspTag() {
        super(new org.openfaces.taglib.internal.timetable.DayViewTag());
    }

    public void setMoreLinkText(ValueExpression moreLinkText) {
        getDelegate().setPropertyValue("moreLinkText", moreLinkText);
    }

    public void setMoreLinkStyle(ValueExpression moreLinkStyle) {
        getDelegate().setPropertyValue("moreLinkStyle", moreLinkStyle);
    }

    public void setMoreLinkClass(ValueExpression moreLinkClass) {
        getDelegate().setPropertyValue("moreLinkClass", moreLinkClass);
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

    public void setDownButtonImg(ValueExpression downButtonImg) {
        getDelegate().setPropertyValue("downButtonImg", downButtonImg);
    }

    public void setUpButtonImg(ValueExpression upButtonImg) {
        getDelegate().setPropertyValue("upButtonImg", upButtonImg);
    }
}
