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
package org.openfaces.taglib.jsp.ajax;

import org.openfaces.taglib.internal.ajax.AjaxSettingsTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Eugene Goncharov
 */
public class AjaxSettingsJspTag extends AbstractComponentJspTag {
    public AjaxSettingsJspTag() {
        super(new AjaxSettingsTag());
    }


    public void setOnerror(ValueExpression onerror) {
        getDelegate().setPropertyValue("onerror", onerror);
    }

    public void setSuccess(ValueExpression onsuccess) {
        getDelegate().setPropertyValue("onsuccess", onsuccess);
    }

    public void setOnsessionexpired(ValueExpression onsessionexpired) {
        getDelegate().setPropertyValue("onsessionexpired", onsessionexpired);
    }

    public void setOnajaxstart(ValueExpression onajaxstart) {
        getDelegate().setPropertyValue("onajaxstart", onajaxstart);
    }

    public void setOnajaxend(ValueExpression onajaxend) {
        getDelegate().setPropertyValue("onajaxend", onajaxend);
    }
}
