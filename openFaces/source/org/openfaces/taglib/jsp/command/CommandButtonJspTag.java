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
package org.openfaces.taglib.jsp.command;

import org.openfaces.taglib.internal.command.CommandButtonTag;
import org.openfaces.taglib.jsp.OUICommandJspTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class CommandButtonJspTag extends OUICommandJspTag {

    public CommandButtonJspTag() {
        super(new CommandButtonTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setType(ValueExpression type) {
        getDelegate().setPropertyValue("type", type);
    }

    public void setDisabled(ValueExpression disabled) {
        getDelegate().setPropertyValue("disabled", disabled); 
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

    public void setAlt(ValueExpression alt) {
        getDelegate().setPropertyValue("alt", alt);
    }

    public void setDir(ValueExpression dir) {
        getDelegate().setPropertyValue("dir", dir);
    }

    public void setImage(ValueExpression image) {
        getDelegate().setPropertyValue("image", image);
    }
}
