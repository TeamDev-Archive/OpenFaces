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

import org.openfaces.taglib.internal.timetable.DaySwitcherTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Natalia Zolochevska
 */
public class DaySwitcherJspTag extends AbstractComponentJspTag {

    public DaySwitcherJspTag() {
        super(new DaySwitcherTag());
    }

    public void setFor(ValueExpression _for) {
        getDelegate().setPropertyValue("for", _for);
    }

    public void setDateFormat(ValueExpression dateFormat) {
        getDelegate().setPropertyValue("dateFormat", dateFormat);
    }

    public void setPattern(ValueExpression pattern) {
        getDelegate().setPropertyValue("pattern", pattern);
    }

    public void setUpperDateFormat(ValueExpression upperDateFormat) {
        getDelegate().setPropertyValue("upperDateFormat", upperDateFormat);
    }

    public void setUpperPattern(ValueExpression upperPattern) {
        getDelegate().setPropertyValue("upperPattern", upperPattern);
    }

    public void setLocale(ValueExpression locale) {
        getDelegate().setPropertyValue("locale", locale);
    }

    public void setTimeZone(ValueExpression timeZone) {
        getDelegate().setPropertyValue("timeZone", timeZone);
    }

    public void setEnabled(ValueExpression enabled) {
        getDelegate().setPropertyValue("enabled", enabled);
    }

    public void setPreviousButtonStyle(ValueExpression previousButtonStyle) {
        getDelegate().setPropertyValue("previousButtonStyle", previousButtonStyle);
    }

    public void setPreviousButtonClass(ValueExpression previousButtonClass) {
        getDelegate().setPropertyValue("previousButtonClass", previousButtonClass);
    }

    public void setPreviousButtonRolloverStyle(ValueExpression previousButtonRolloverStyle) {
        getDelegate().setPropertyValue("previousButtonRolloverStyle", previousButtonRolloverStyle);
    }

    public void setPreviousButtonRolloverClass(ValueExpression previousButtonRolloverClass) {
        getDelegate().setPropertyValue("previousButtonRolloverClass", previousButtonRolloverClass);
    }

    public void setPreviousButtonPressedStyle(ValueExpression previousButtonPressedStyle) {
        getDelegate().setPropertyValue("previousButtonPressedStyle", previousButtonPressedStyle);
    }

    public void setPreviousButtonPressedClass(ValueExpression previousButtonPressedClass) {
        getDelegate().setPropertyValue("previousButtonPressedClass", previousButtonPressedClass);
    }

    public void setPreviousButtonImageUrl(ValueExpression previousButtonImageUrl) {
        getDelegate().setPropertyValue("previousButtonImageUrl", previousButtonImageUrl);
    }

    public void setNextButtonStyle(ValueExpression nextButtonStyle) {
        getDelegate().setPropertyValue("nextButtonStyle", nextButtonStyle);
    }

    public void setNextButtonClass(ValueExpression nextButtonClass) {
        getDelegate().setPropertyValue("nextButtonClass", nextButtonClass);
    }

    public void setNextButtonRolloverStyle(ValueExpression nextButtonRolloverStyle) {
        getDelegate().setPropertyValue("nextButtonRolloverStyle", nextButtonRolloverStyle);
    }

    public void setNextButtonRolloverClass(ValueExpression nextButtonRolloverClass) {
        getDelegate().setPropertyValue("nextButtonRolloverClass", nextButtonRolloverClass);
    }

    public void setNextButtonPressedStyle(ValueExpression nextButtonPressedStyle) {
        getDelegate().setPropertyValue("nextButtonPressedStyle", nextButtonPressedStyle);
    }

    public void setNextButtonPressedClass(ValueExpression nextButtonPressedClass) {
        getDelegate().setPropertyValue("nextButtonPressedClass", nextButtonPressedClass);
    }

    public void setNextButtonImageUrl(ValueExpression nextButtonImageUrl) {
        getDelegate().setPropertyValue("nextButtonImageUrl", nextButtonImageUrl);
    }

    public void setTextStyle(ValueExpression textStyle) {
        getDelegate().setPropertyValue("textStyle", textStyle);
    }

     public void setTextClass(ValueExpression textClass) {
        getDelegate().setPropertyValue("textClass", textClass);
    }

    public void setTextRolloverStyle(ValueExpression textRolloverStyle) {
        getDelegate().setPropertyValue("textRolloverStyle", textRolloverStyle);
    }

    public void setTextRolloverClass(ValueExpression textRolloverClass) {
        getDelegate().setPropertyValue("textRolloverClass", textRolloverClass);
    }

     public void setUpperTextStyle(ValueExpression textStyle) {
        getDelegate().setPropertyValue("upperTextStyle", textStyle);
    }

     public void setUpperTextClass(ValueExpression textClass) {
        getDelegate().setPropertyValue("upperTextClass", textClass);
    }

    public void setUpperTextRolloverStyle(ValueExpression textRolloverStyle) {
        getDelegate().setPropertyValue("upperTextRolloverStyle", textRolloverStyle);
    }

    public void setUpperTextRolloverClass(ValueExpression textRolloverClass) {
        getDelegate().setPropertyValue("upperTextRolloverClass", textRolloverClass);
    }

}