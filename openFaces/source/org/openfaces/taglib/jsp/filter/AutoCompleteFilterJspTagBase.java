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
package org.openfaces.taglib.jsp.filter;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class AutoCompleteFilterJspTagBase extends ExpressionFilterJspTag {
    public AutoCompleteFilterJspTagBase(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setCustomValueAllowed(ValueExpression value) {
        getDelegate().setPropertyValue("customValueAllowed", value);
    }

    public void setListItemStyle(ValueExpression listItemStyle) {
        getDelegate().setPropertyValue("listItemStyle", listItemStyle);
    }

    public void setRolloverListItemStyle(ValueExpression rolloverListItemStyle) {
        getDelegate().setPropertyValue("rolloverListItemStyle", rolloverListItemStyle);
    }

    public void setListItemClass(ValueExpression listItemClass) {
        getDelegate().setPropertyValue("listItemClass", listItemClass);
    }

    public void setRolloverListItemClass(ValueExpression rolloverListItemClass) {
        getDelegate().setPropertyValue("rolloverListItemClass", rolloverListItemClass);
    }

    public void setListAlignment(ValueExpression listAlignment) {
        getDelegate().setPropertyValue("listAlignment", listAlignment);
    }

    public void setOndropdown(ValueExpression ondropdown) {
        getDelegate().setPropertyValue("ondropdown", ondropdown);
    }

    public void setOncloseup(ValueExpression oncloseup) {
        getDelegate().setPropertyValue("oncloseup", oncloseup);
    }

    public void setListClass(ValueExpression listClass) {
        getDelegate().setPropertyValue("listClass", listClass);
    }

    public void setRolloverListClass(ValueExpression rolloverListClass) {
        getDelegate().setPropertyValue("rolloverListClass", rolloverListClass);
    }

    public void setTimeout(ValueExpression timeout) {
        getDelegate().setPropertyValue("timeout", timeout);
    }

    public void setListStyle(ValueExpression listStyle) {
        getDelegate().setPropertyValue("listStyle", listStyle);
    }

    public void setRolloverListStyle(ValueExpression rolloverListStyle) {
        getDelegate().setPropertyValue("rolloverListStyle", rolloverListStyle);
    }

    public void setSuggestionMode(ValueExpression suggestionMode) {
        getDelegate().setPropertyValue("suggestionMode", suggestionMode);
    }

    public void setSuggestionDelay(ValueExpression suggestionDelay) {
        getDelegate().setPropertyValue("suggestionDelay", suggestionDelay);
    }

    public void setHorizontalGridLines(ValueExpression horizontalGridLines) {
        getDelegate().setPropertyValue("horizontalGridLines", horizontalGridLines);
    }

    public void setOddListItemStyle(ValueExpression oddListItemStyle) {
        getDelegate().setPropertyValue("oddListItemStyle", oddListItemStyle);
    }

    public void setOddListItemClass(ValueExpression oddListItemClass) {
        getDelegate().setPropertyValue("oddListItemClass", oddListItemClass);
    }


    public void setAutoComplete(ValueExpression autoComplete) {
        getDelegate().setPropertyValue("autoComplete", autoComplete);
    }

    public void setSuggestionMinChars(ValueExpression suggestionMinChars) {
        getDelegate().setPropertyValue("suggestionMinChars", suggestionMinChars);
    }

    public void setMaxlength(ValueExpression maxlength) {
        getDelegate().setPropertyValue("maxlength", maxlength);
    }



    protected void setFieldClass(ValueExpression fieldClass) {
        getDelegate().setPropertyValue("fieldClass", fieldClass);
    }

    protected void setRolloverFieldClass(ValueExpression rolloverFieldClass) {
        getDelegate().setPropertyValue("rolloverFieldClass", rolloverFieldClass);
    }

    protected void setButtonClass(ValueExpression buttonClass) {
        getDelegate().setPropertyValue("buttonClass", buttonClass);
    }

    protected void setRolloverButtonClass(ValueExpression rolloverButtonClass) {
        getDelegate().setPropertyValue("rolloverButtonClass", rolloverButtonClass);
    }

    protected void setButtonAlignment(ValueExpression buttonAlignment) {
        getDelegate().setPropertyValue("buttonAlignment", buttonAlignment);
    }

    protected void setButtonImageUrl(ValueExpression buttonImageUrl) {
        getDelegate().setPropertyValue("buttonImageUrl", buttonImageUrl);
    }

    protected void setFieldStyle(ValueExpression fieldStyle) {
        getDelegate().setPropertyValue("fieldStyle", fieldStyle);
    }

    protected void setRolloverFieldStyle(ValueExpression rolloverFieldStyle) {
        getDelegate().setPropertyValue("rolloverFieldStyle", rolloverFieldStyle);
    }

    protected void setButtonStyle(ValueExpression buttonStyle) {
        getDelegate().setPropertyValue("buttonStyle", buttonStyle);
    }

    protected void setRolloverButtonStyle(ValueExpression rolloverButtonStyle) {
        getDelegate().setPropertyValue("rolloverButtonStyle", rolloverButtonStyle);
    }

    protected void setPressedButtonStyle(ValueExpression pressedButtonStyle) {
        getDelegate().setPropertyValue("pressedButtonStyle", pressedButtonStyle);
    }

    protected void setPressedButtonClass(ValueExpression pressedButtonClass) {
        getDelegate().setPropertyValue("pressedButtonClass", pressedButtonClass);
    }


}
