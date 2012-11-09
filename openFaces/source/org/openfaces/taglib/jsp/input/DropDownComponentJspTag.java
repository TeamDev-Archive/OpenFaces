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
package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.DropDownComponentTag;
import org.openfaces.taglib.jsp.OUIInputTextJspTag;

import javax.el.ValueExpression;

/**
 * @author Andrew Palval
 */
public abstract class DropDownComponentJspTag extends OUIInputTextJspTag {

    protected DropDownComponentJspTag(DropDownComponentTag delegate) {
        super(delegate);
    }

    protected void setFieldClass(ValueExpression fieldClass) {
        getDelegate().setPropertyValue("fieldClass", fieldClass);
    }

    protected void setRolloverFieldClass(ValueExpression rolloverFieldClass) {
        getDelegate().setPropertyValue("rolloverFieldClass", rolloverFieldClass);
    }

    protected void setButtonClass(ValueExpression buttonClass) {
        getDelegate().setPropertyValue("buttonClass", buttonClass);
    }

    protected void setRolloverButtonClass(ValueExpression rolloverButtonClass) {
        getDelegate().setPropertyValue("rolloverButtonClass", rolloverButtonClass);
    }

    protected void setButtonAlignment(ValueExpression buttonAlignment) {
        getDelegate().setPropertyValue("buttonAlignment", buttonAlignment);
    }

    protected void setButtonImageUrl(ValueExpression buttonImageUrl) {
        getDelegate().setPropertyValue("buttonImageUrl", buttonImageUrl);
    }

    protected void setFieldStyle(ValueExpression fieldStyle) {
        getDelegate().setPropertyValue("fieldStyle", fieldStyle);
    }

    protected void setRolloverFieldStyle(ValueExpression rolloverFieldStyle) {
        getDelegate().setPropertyValue("rolloverFieldStyle", rolloverFieldStyle);
    }

    protected void setButtonStyle(ValueExpression buttonStyle) {
        getDelegate().setPropertyValue("buttonStyle", buttonStyle);
    }

    protected void setRolloverButtonStyle(ValueExpression rolloverButtonStyle) {
        getDelegate().setPropertyValue("rolloverButtonStyle", rolloverButtonStyle);
    }

    protected void setPressedButtonStyle(ValueExpression pressedButtonStyle) {
        getDelegate().setPropertyValue("pressedButtonStyle", pressedButtonStyle);
    }

    protected void setPressedButtonClass(ValueExpression pressedButtonClass) {
        getDelegate().setPropertyValue("pressedButtonClass", pressedButtonClass);
    }

    protected void setDisabledButtonClass(ValueExpression disabledButtonClass) {
        getDelegate().setPropertyValue("disabledButtonClass", disabledButtonClass);
    }

    protected void setDisabledButtonImageUrl(ValueExpression disabledButtonImageUrl) {
        getDelegate().setPropertyValue("disabledButtonImageUrl", disabledButtonImageUrl);
    }

    protected void setDisabledButtonStyle(ValueExpression disabledButtonStyle) {
        getDelegate().setPropertyValue("disabledButtonStyle", disabledButtonStyle);
    }

    protected void setDisabledFieldClass(ValueExpression disabledFieldClass) {
        getDelegate().setPropertyValue("disabledFieldClass", disabledFieldClass);
    }

    protected void setDisabledFieldStyle(ValueExpression disabledFieldStyle) {
        getDelegate().setPropertyValue("disabledFieldStyle", disabledFieldStyle);
    }

    public void setDisabledStyle(ValueExpression disabledStyle) {
        getDelegate().setPropertyValue("disabledStyle", disabledStyle);
    }

    public void setDisabledClass(ValueExpression disabledClass) {
        getDelegate().setPropertyValue("disabledClass", disabledClass);
    }

    protected void setChangeValueOnSelect(ValueExpression changeValueOnSelect) {
        getDelegate().setPropertyValue("changeValueOnSelect", changeValueOnSelect);
    }

}
