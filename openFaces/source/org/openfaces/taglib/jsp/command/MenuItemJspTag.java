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


import org.openfaces.taglib.internal.command.MenuItemTag;
import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import org.openfaces.taglib.jsp.OUICommandJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Vladimir Kurganov
 */
public class MenuItemJspTag extends OUICommandJspTag {

    public MenuItemJspTag() {
        super(new MenuItemTag());
    }

    public MenuItemJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setIconUrl(ValueExpression iconUrl) {
        getDelegate().setPropertyValue("iconUrl", iconUrl);
    }

    public void setDisabledIconUrl(ValueExpression disabledIconUrl) {
        getDelegate().setPropertyValue("disabledIconUrl", disabledIconUrl);
    }

    public void setSelectedIconUrl(ValueExpression selectedIconUrl) {
        getDelegate().setPropertyValue("selectedIconUrl", selectedIconUrl);
    }

    public void setSelectedDisabledIconUrl(ValueExpression selectedDisabledIconUrl) {
        getDelegate().setPropertyValue("selectedDisabledIconUrl", selectedDisabledIconUrl);
    }

    public void setAccessKey(ValueExpression accessKey) {
        getDelegate().setPropertyValue("accessKey", accessKey);
    }

    public void setSubmenuImageUrl(ValueExpression submenuImageUrl) {
        getDelegate().setPropertyValue("submenuImageUrl", submenuImageUrl);
    }

    public void setDisabledSubmenuImageUrl(ValueExpression disabledSubmenuImageUrl) {
        getDelegate().setPropertyValue("disabledSubmenuImageUrl", disabledSubmenuImageUrl);
    }

    public void setSelectedSubmenuImageUrl(ValueExpression selectedSubmenuImageUrl) {
        getDelegate().setPropertyValue("selectedSubmenuImageUrl", selectedSubmenuImageUrl);
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

    public void setSelectedStyle(ValueExpression selectedStyle) {
        getDelegate().setPropertyValue("selectedStyle", selectedStyle);
    }

    public void setSelectedClass(ValueExpression selectedClass) {
        getDelegate().setPropertyValue("selectedClass", selectedClass);
    }

    public void setContentAreaStyle(ValueExpression contentStyle) {
        getDelegate().setPropertyValue("contentAreaStyle", contentStyle);
    }

    public void setContentAreaClass(ValueExpression contentClass) {
        getDelegate().setPropertyValue("contentAreaClass", contentClass);
    }

    public void setIndentAreaStyle(ValueExpression indentStyle) {
        getDelegate().setPropertyValue("indentAreaStyle", indentStyle);
    }

    public void setIndentAreaClass(ValueExpression indentClass) {
        getDelegate().setPropertyValue("indentAreaClass", indentClass);
    }

    public void setSubmenuIconAreaStyle(ValueExpression submenuIconAreaStyle) {
        getDelegate().setPropertyValue("submenuIconAreaStyle", submenuIconAreaStyle);
    }

    public void setSubmenuIconAreaClass(ValueExpression submenuIconAreaClass) {
        getDelegate().setPropertyValue("submenuIconAreaClass", submenuIconAreaClass);
    }

    public void setSelectedDisabledStyle(ValueExpression selectedDisabledStyle) {
        getDelegate().setPropertyValue("selectedDisabledStyle", selectedDisabledStyle);
    }

    public void setSelectedDisabledClass(ValueExpression selectedDisabledClass) {
        getDelegate().setPropertyValue("selectedDisabledClass", selectedDisabledClass);
    }

}
