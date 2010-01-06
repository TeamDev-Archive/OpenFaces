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

package org.openfaces.taglib.jsp.select;

import org.openfaces.taglib.jsp.UIInputJspTag;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.el.ValueExpression;

/**
 * @author Oleg Marshalenko
 */
public abstract class UISelectManyInputJspTag extends UIInputJspTag {

    protected UISelectManyInputJspTag(AbstractComponentTag delegate) {
        super(delegate);
    }

    public void setAccesskey(ValueExpression accesskey) {
        getDelegate().setPropertyValue("accesskey", accesskey);
    }

    public void setTabindex(ValueExpression tabindex) {
        getDelegate().setPropertyValue("tabindex", tabindex);
    }

    public void setTitle(ValueExpression title) {
        getDelegate().setPropertyValue("title", title);
    }

    public void setDir(ValueExpression dir) {
        getDelegate().setPropertyValue("dir", dir);
    }

    public void setLang(ValueExpression lang) {
        getDelegate().setPropertyValue("lang", lang);
    }

    public void setReadonly(ValueExpression readonly) {
        getDelegate().setPropertyValue("readonly", readonly);
    }

    public void setBorder(ValueExpression border) {
        getDelegate().setPropertyValue("border", border);
    }

    public void setLayout(ValueExpression layout) {
        getDelegate().setPropertyValue("layout", layout);
    }

    public void setOnselect(ValueExpression onselect) {
        getDelegate().setPropertyValue("onselect", onselect);
    }

    public void setDisabled(ValueExpression disabled) {
        getDelegate().setPropertyValue("disabled", disabled);
    }

    public void setEnabledStyle(ValueExpression enabledStyle) {
        getDelegate().setPropertyValue("enabledStyle", enabledStyle);
    }

    public void setEnabledClass(ValueExpression enabledClass) {
        getDelegate().setPropertyValue("enabledClass", enabledClass);
    }

    public void setDisabledStyle(ValueExpression disabledStyle) {
        getDelegate().setPropertyValue("disabledStyle", disabledStyle);
    }

    public void setDisabledClass(ValueExpression disabledClass) {
        getDelegate().setPropertyValue("disabledClass", disabledClass);
    }

    public void setSelectedImageUrl(ValueExpression selectedImageUrl) {
        getDelegate().setPropertyValue("selectedImageUrl", selectedImageUrl);
    }

    public void setUnselectedImageUrl(ValueExpression unselectedImageUrl) {
        getDelegate().setPropertyValue("unselectedImageUrl", unselectedImageUrl);
    }

    public void setRolloverSelectedImageUrl(ValueExpression rolloverSelectedImageUrl) {
        getDelegate().setPropertyValue("rolloverSelectedImageUrl", rolloverSelectedImageUrl);
    }

    public void setRolloverUnselectedImageUrl(ValueExpression rolloverUnselectedImageUrl) {
        getDelegate().setPropertyValue("rolloverUnselectedImageUrl", rolloverUnselectedImageUrl);
    }

    public void setPressedSelectedImageUrl(ValueExpression pressedSelectedImageUrl) {
        getDelegate().setPropertyValue("pressedSelectedImageUrl", pressedSelectedImageUrl);
    }

    public void setPressedUnselectedImageUrl(ValueExpression pressedUnselectedImageUrl) {
        getDelegate().setPropertyValue("pressedUnselectedImageUrl", pressedUnselectedImageUrl);
    }

    public void setDisabledSelectedImageUrl(ValueExpression disabledSelectedImageUrl) {
        getDelegate().setPropertyValue("disabledSelectedImageUrl", disabledSelectedImageUrl);
    }

    public void setDisabledUnselectedImageUrl(ValueExpression disabledUnselectedImageUrl) {
        getDelegate().setPropertyValue("disabledUnselectedImageUrl", disabledUnselectedImageUrl);
    }

    public void setFocusedItemStyle(ValueExpression focusedItemStyle) {
        getDelegate().setPropertyValue("focusedItemStyle", focusedItemStyle);
    }

    public void setFocusedItemClass(ValueExpression focusedItemClass) {
        getDelegate().setPropertyValue("focusedItemClass", focusedItemClass);
    }

    public void setSelectedItemStyle(ValueExpression selectedItemStyle) {
        getDelegate().setPropertyValue("selectedItemStyle", selectedItemStyle);
    }

    public void setSelectedItemClass(ValueExpression selectedItemClass) {
        getDelegate().setPropertyValue("selectedItemClass", selectedItemClass);
    }

    public void setRolloverItemStyle(ValueExpression rolloverItemStyle) {
        getDelegate().setPropertyValue("rolloverItemStyle", rolloverItemStyle);
    }

    public void setRolloverItemClass(ValueExpression rolloverItemClass) {
        getDelegate().setPropertyValue("rolloverItemClass", rolloverItemClass);
    }

    public void setPressedItemStyle(ValueExpression pressedItemStyle) {
        getDelegate().setPropertyValue("pressedItemStyle", pressedItemStyle);
    }

    public void setPressedItemClass(ValueExpression pressedItemClass) {
        getDelegate().setPropertyValue("pressedItemClass", pressedItemClass);
    }

}
