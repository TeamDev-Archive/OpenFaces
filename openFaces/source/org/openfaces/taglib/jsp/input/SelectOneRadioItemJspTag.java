/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.jsp.AbstractComponentJspTag;
import org.openfaces.taglib.internal.input.SelectOneRadioItemTag;

import javax.el.ValueExpression;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 3:29:25 PM
 */
public class SelectOneRadioItemJspTag extends AbstractComponentJspTag {

    public SelectOneRadioItemJspTag() {
        super(new SelectOneRadioItemTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setItemLabel(ValueExpression itemLabel) {
        getDelegate().setPropertyValue("itemLabel", itemLabel);
    }

    public void setDisabled(ValueExpression disabled) {
        getDelegate().setPropertyValue("disabled", disabled);
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
