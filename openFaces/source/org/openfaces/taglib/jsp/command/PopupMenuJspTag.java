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

import org.openfaces.taglib.internal.command.PopupMenuTag;
import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Vladimir Kurganov
 */
public class PopupMenuJspTag extends AbstractComponentJspTag {

    public PopupMenuJspTag() {
        super(new PopupMenuTag());
    }

    public PopupMenuJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setFor(ValueExpression forElement) {
        getDelegate().setPropertyValue("for", forElement);
    }

    public void setEvent(ValueExpression event) {
        getDelegate().setPropertyValue("event", event);
    }

    public void setStandalone(ValueExpression standalone) {
        getDelegate().setPropertyValue("standalone", standalone);
    }

    public void setSubmenuShowDelay(ValueExpression submenuShowDelay) {
        getDelegate().setPropertyValue("submenuShowDelay", submenuShowDelay);
    }

    public void setSubmenuHideDelay(ValueExpression submenuHideDelay) {
        getDelegate().setPropertyValue("submenuHideDelay", submenuHideDelay);
    }

    public void setIndentVisible(ValueExpression indentVisible) {
        getDelegate().setPropertyValue("indentVisible", indentVisible);
    }

    public void setSelectDisabledItems(ValueExpression selectDisabledItems) {
        getDelegate().setPropertyValue("selectDisabledItems", selectDisabledItems);
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

    public void setSelectedDisabledSubmenuImageUrl(ValueExpression selectedDisabledSubmenuImageUrl) {
        getDelegate().setPropertyValue("selectedDisabledSubmenuImageUrl", selectedDisabledSubmenuImageUrl);
    }

    public void setItemIconUrl(ValueExpression itemIconUrl) {
        getDelegate().setPropertyValue("itemIconUrl", itemIconUrl);
    }

    public void setDisabledItemIconUrl(ValueExpression disabledItemIconUrl) {
        getDelegate().setPropertyValue("disabledItemIconUrl", disabledItemIconUrl);
    }

    public void setSelectedItemIconUrl(ValueExpression selectedItemIconUrl) {
        getDelegate().setPropertyValue("selectedIconItemUrl", selectedItemIconUrl);
    }

    public void setSelectedDisabledItemIconUrl(ValueExpression selectedDisabledItemIconUrl) {
        getDelegate().setPropertyValue("selectedDisabledIconItemUrl", selectedDisabledItemIconUrl);
    }

    public void setItemStyle(ValueExpression itemStyle) {
        getDelegate().setPropertyValue("itemStyle", itemStyle);
    }

    public void setItemClass(ValueExpression itemClass) {
        getDelegate().setPropertyValue("itemClass", itemClass);
    }

    public void setSelectedItemStyle(ValueExpression selectedItemStyle) {
        getDelegate().setPropertyValue("selectedItemStyle", selectedItemStyle);
    }

    public void setSelectedItemClass(ValueExpression selectedItemClass) {
        getDelegate().setPropertyValue("selectedItemClass", selectedItemClass);
    }

    public void setDisabledItemStyle(ValueExpression disabledItemStyle) {
        getDelegate().setPropertyValue("disabledItemStyle", disabledItemStyle);
    }

    public void setDisabledItemClass(ValueExpression disabledItemClass) {
        getDelegate().setPropertyValue("disabledItemClass", disabledItemClass);
    }

    public void setItemContentStyle(ValueExpression itemContentStyle) {
        getDelegate().setPropertyValue("itemContentStyle", itemContentStyle);
    }

    public void setItemContentClass(ValueExpression itemContentClass) {
        getDelegate().setPropertyValue("itemContentClass", itemContentClass);
    }

    public void setIndentStyle(ValueExpression indentStyle) {
        getDelegate().setPropertyValue("indentStyle", indentStyle);
    }

    public void setIndentClass(ValueExpression indentClass) {
        getDelegate().setPropertyValue("indentClass", indentClass);
    }

    public void setItemIndentStyle(ValueExpression itemIndentStyle) {
        getDelegate().setPropertyValue("itemIndentStyle", itemIndentStyle);
    }

    public void setItemIndentClass(ValueExpression itemIndentClass) {
        getDelegate().setPropertyValue("itemIndentClass", itemIndentClass);
    }


    public void setItemSubmenuIconStyle(ValueExpression itemSubmenuIconStyle) {
        getDelegate().setPropertyValue("itemSubmenuIconStyle", itemSubmenuIconStyle);
    }

    public void setItemSubmenuIconClass(ValueExpression itemSubmenuIconClass) {
        getDelegate().setPropertyValue("itemSubmenuIconClass", itemSubmenuIconClass);
    }

    public void setSelectedDisabledItemStyle(ValueExpression selectedDisabledItemStyle) {
        getDelegate().setPropertyValue("selectedDisabledItemStyle", selectedDisabledItemStyle);
    }

    public void setselectedDisabledItemClass(ValueExpression selectedDisabledItemClass) {
        getDelegate().setPropertyValue("selectedDisabledItemClass", selectedDisabledItemClass);
    }

    public void setOnshow(ValueExpression onshow) {
        getDelegate().setPropertyValue("onshow", onshow);
    }

    public void setOnhide(ValueExpression onhide) {
        getDelegate().setPropertyValue("onhide", onhide);
    }

}
