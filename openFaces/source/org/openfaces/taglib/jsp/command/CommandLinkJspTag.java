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
package org.openfaces.taglib.jsp.command;

import org.openfaces.taglib.internal.command.CommandLinkTag;
import org.openfaces.taglib.jsp.OUICommandJspTag;

import javax.el.ValueExpression;

public class CommandLinkJspTag extends OUICommandJspTag {

    public CommandLinkJspTag() {
        super(new CommandLinkTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setAccesskey(ValueExpression accesskey) {
        getDelegate().setPropertyValue("accesskey", accesskey);
    }

    public void setTabindex(ValueExpression tabindex) {
        getDelegate().setPropertyValue("tabindex", tabindex);
    }

    public void setLang(ValueExpression lang) {
        getDelegate().setPropertyValue("lang", lang);
    }

    public void setTitle(ValueExpression title) {
        getDelegate().setPropertyValue("title", title);
    }

    public void setDir(ValueExpression dir) {
        getDelegate().setPropertyValue("dir", dir);
    }

    public void setCharset(ValueExpression charset) {
        getDelegate().setPropertyValue("charset", charset);
    }

    public void setCoords(ValueExpression coords) {
        getDelegate().setPropertyValue("coords", coords);
    }

    public void setHreflang(ValueExpression hreflang) {
        getDelegate().setPropertyValue("hreflang", hreflang);
    }

    public void setRel(ValueExpression rel) {
        getDelegate().setPropertyValue("rel", rel);
    }

    public void setRev(ValueExpression rev) {
        getDelegate().setPropertyValue("rev", rev);
    }

    public void setShape(ValueExpression shape) {
        getDelegate().setPropertyValue("shape", shape);
    }

    public void setTarget(ValueExpression target) {
        getDelegate().setPropertyValue("target", target);
    }

    public void setType(ValueExpression type) {
        getDelegate().setPropertyValue("type", type);
    }
}
