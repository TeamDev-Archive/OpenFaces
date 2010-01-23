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
public class ExpressionFilterJspTag extends FilterJspTag {
    public ExpressionFilterJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setExpression(ValueExpression expression) {
        getDelegate().setPropertyValue("expression", expression);
    }

    public void setCondition(ValueExpression condition) {
        getDelegate().setPropertyValue("condition", condition);
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setOptions(ValueExpression options) {
        getDelegate().setPropertyValue("options", options);
    }

    public void setCaseSensitive(ValueExpression caseSensitive) {
        getDelegate().setPropertyValue("caseSensitive", caseSensitive);
    }

    public void setConverter(ValueExpression converter) {
        getDelegate().setPropertyValue("converter", converter);
    }

    public void setAllRecordsText(ValueExpression allRecordsText) {
        getDelegate().setPropertyValue("allRecordsText", allRecordsText);
    }

    public void setEmptyRecordsText(ValueExpression emptyRecordsText) {
        getDelegate().setPropertyValue("emptyRecordsText", emptyRecordsText);
    }

    public void setNonEmptyRecordsText(ValueExpression nonEmptyRecordsText) {
        getDelegate().setPropertyValue("nonEmptyRecordsText", nonEmptyRecordsText);
    }

    public void setPromptText(ValueExpression promptText) {
        getDelegate().setPropertyValue("promptText", promptText);
    }

    public void setPromptTextStyle(ValueExpression promptTextStyle) {
        getDelegate().setPropertyValue("promptTextStyle", promptTextStyle);
    }

    public void setPromptTextClass(ValueExpression promptTextClass) {
        getDelegate().setPropertyValue("promptTextClass", promptTextClass);
    }
    
    public void setTitle(ValueExpression title) {
        getDelegate().setPropertyValue("title", title);
    }

    public void setAccesskey(ValueExpression accesskey) {
        getDelegate().setPropertyValue("accesskey", accesskey);
    }

    public void setTabindex(ValueExpression tabindex) {
        getDelegate().setPropertyValue("tabindex", tabindex);
    }

    public void setAutoFilterDelay(ValueExpression autoFilterDelay) {
        getDelegate().setPropertyValue("autoFilterDelay", autoFilterDelay);
    }

}
