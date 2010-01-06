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
package org.openfaces.taglib.jsp.validation;

import org.openfaces.taglib.internal.validation.MessageTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Vladimir Korenev
 */
public class MessageJspTag extends AbstractComponentJspTag {

    public MessageJspTag() {
        super(new MessageTag());
    }

    public void setFor(ValueExpression aFor) {
        getDelegate().setPropertyValue("for", aFor);
    }

    public void setShowSummary(ValueExpression showSummary) {
        getDelegate().setPropertyValue("showSummary", showSummary);
    }

    public void setShowDetail(ValueExpression showDetail) {
        getDelegate().setPropertyValue("showDetail", showDetail);
    }

    public void setGlobalOnly(ValueExpression globalOnly) {
        getDelegate().setPropertyValue("globalOnly", globalOnly);
    }

    public void setErrorClass(ValueExpression errorClass) {
        getDelegate().setPropertyValue("errorClass", errorClass);
    }

    public void setErrorStyle(ValueExpression errorStyle) {
        getDelegate().setPropertyValue("errorStyle", errorStyle);
    }

    public void setFatalClass(ValueExpression fatalClass) {
        getDelegate().setPropertyValue("fatalClass", fatalClass);
    }

    public void setFatalStyle(ValueExpression fatalStyle) {
        getDelegate().setPropertyValue("fatalStyle", fatalStyle);
    }

    public void setInfoClass(ValueExpression infoClass) {
        getDelegate().setPropertyValue("infoClass", infoClass);
    }

    public void setInfoStyle(ValueExpression infoStyle) {
        getDelegate().setPropertyValue("infoStyle", infoStyle);
    }

    public void setWarnClass(ValueExpression warnClass) {
        getDelegate().setPropertyValue("warnClass", warnClass);
    }

    public void setWarnStyle(ValueExpression warnStyle) {
        getDelegate().setPropertyValue("warnStyle", warnStyle);
    }

    public void setLayout(ValueExpression layout) {
        getDelegate().setPropertyValue("layout", layout);
    }

    public void setTooltip(ValueExpression tooltip) {
        getDelegate().setPropertyValue("tooltip", tooltip);
    }

    public void setDir(ValueExpression dir) {
        getDelegate().setPropertyValue("dir", dir);
    }

    public void setLang(ValueExpression lang) {
        getDelegate().setPropertyValue("lang", lang);
    }

    public void setTitle(ValueExpression title) {
        getDelegate().setPropertyValue("title", title);
    }

}
