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
package org.openfaces.taglib.jsp;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class OUICommandJspTag extends AbstractComponentJspTag {
    public OUICommandJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setAction(MethodExpression action) {
        getDelegate().setPropertyValue("action", action);
    }

    public void setActionListener(MethodExpression actionListener) {
        getDelegate().setPropertyValue("actionListener", actionListener);
    }

    public void setImmediate(ValueExpression immediate) {
        getDelegate().setPropertyValue("immediate", immediate);
    }

    public void setRender(ValueExpression render) {
        getDelegate().setPropertyValue("render", render);
    }

    public void setExecute(ValueExpression execute) {
        getDelegate().setPropertyValue("execute", execute);
    }

    public void setDisabled(ValueExpression disabled) {
        getDelegate().setPropertyValue("disabled", disabled);
    }

    public void setDisabledStyle(ValueExpression disabledStyle) {
        getDelegate().setPropertyValue("disabledStyle", disabledStyle);
    }

    public void setDisabledClass(ValueExpression disabledClass) {
        getDelegate().setPropertyValue("disabledClass", disabledClass);
    }

    public void setOnajaxstart(ValueExpression onajaxstart) {
        getDelegate().setPropertyValue("onajaxstart", onajaxstart);
    }

    public void setOnajaxend(ValueExpression onajaxend) {
        getDelegate().setPropertyValue("onajaxend", onajaxend);
    }

    public void setOnerror(ValueExpression onerror) {
        getDelegate().setPropertyValue("onerror", onerror);
    }

    public void setOnsuccess(ValueExpression onsuccess) {
        getDelegate().setPropertyValue("onsuccess", onsuccess);
    }

    public void setExecuteRenderedComponents(ValueExpression executeRenderedComponents) {
        getDelegate().setPropertyValue("executeRenderedComponents", executeRenderedComponents);
    }

    public void setDelay(ValueExpression delay) {
        getDelegate().setPropertyValue("delay", delay);
    }
}
