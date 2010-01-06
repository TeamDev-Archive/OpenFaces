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

package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.SpinnerTag;

import javax.el.ValueExpression;

/**
 * @author Alexander Golubev
 */
public class SpinnerJspTag extends DropDownComponentJspTag {

    public SpinnerJspTag() {
        super(new SpinnerTag());
    }

    public void setCycled(ValueExpression cycled) {
        getDelegate().setPropertyValue("cycled", cycled);
    }

    public void setOnchange(ValueExpression onchange) {
        getDelegate().setPropertyValue("onchange", onchange);
    }

    public void setStep(ValueExpression stepValue) {
        getDelegate().setPropertyValue("step", stepValue);
    }

    public void setTypingAllowed(ValueExpression typingAllowed) {
        getDelegate().setPropertyValue("typingAllowed", typingAllowed);
    }

    public void setMaxValue(ValueExpression maxValue) {
        getDelegate().setPropertyValue("maxValue", maxValue);
    }

    public void setMinValue(ValueExpression minValue) {
        getDelegate().setPropertyValue("minValue", minValue);
    }

    public void setDisabledIncreaseButtonImageUrl(ValueExpression disabledButtonImageUrl) {
        getDelegate().setPropertyValue("disabledIncreaseButtonImageUrl", disabledButtonImageUrl);
    }

    public void setDisabledDecreaseButtonImageUrl(ValueExpression disabledButtonImageUrl) {
        getDelegate().setPropertyValue("disabledDecreaseButtonImageUrl", disabledButtonImageUrl);
    }

    public void setIncreaseButtonImageUrl(ValueExpression buttonImageUrl) {
        getDelegate().setPropertyValue("increaseButtonImageUrl", buttonImageUrl);
    }

    public void setDecreaseButtonImageUrl(ValueExpression buttonImageUrl) {
        getDelegate().setPropertyValue("decreaseButtonImageUrl", buttonImageUrl);
    }

    public void setFieldStyle(ValueExpression fieldStyle) {
        super.setFieldStyle(fieldStyle);
    }

    public void setRolloverFieldStyle(ValueExpression rolloverFieldStyle) {
        super.setRolloverFieldStyle(rolloverFieldStyle);
    }

    public void setFieldClass(ValueExpression fieldClass) {
        super.setFieldClass(fieldClass);
    }

    public void setRolloverFieldClass(ValueExpression rolloverFieldClass) {
        super.setRolloverFieldClass(rolloverFieldClass);
    }

    public void setButtonClass(ValueExpression buttonClass) {
        super.setButtonClass(buttonClass);
    }

    public void setRolloverButtonClass(ValueExpression rolloverButtonClass) {
        super.setRolloverButtonClass(rolloverButtonClass);
    }

    public void setButtonAlignment(ValueExpression buttonAlignment) {
        super.setButtonAlignment(buttonAlignment);
    }

    public void setButtonImageUrl(ValueExpression buttonImageUrl) {
        super.setButtonImageUrl(buttonImageUrl);
    }

    public void setButtonStyle(ValueExpression buttonStyle) {
        super.setButtonStyle(buttonStyle);
    }

    public void setRolloverButtonStyle(ValueExpression rolloverButtonStyle) {
        super.setRolloverButtonStyle(rolloverButtonStyle);
    }

    public void setPressedButtonStyle(ValueExpression pressedButtonStyle) {
        super.setPressedButtonStyle(pressedButtonStyle);
    }

    public void setPressedButtonClass(ValueExpression pressedButtonClass) {
        super.setPressedButtonClass(pressedButtonClass);
    }

    public void setDisabledButtonClass(ValueExpression disabledButtonClass) {
        super.setDisabledButtonClass(disabledButtonClass);
    }

    public void setDisabledButtonImageUrl(ValueExpression disabledButtonImageUrl) {
        super.setDisabledButtonImageUrl(disabledButtonImageUrl);
    }

    public void setDisabledButtonStyle(ValueExpression disabledButtonStyle) {
        super.setDisabledButtonStyle(disabledButtonStyle);
    }

    public void setDisabledFieldClass(ValueExpression disabledFieldClass) {
        super.setDisabledFieldClass(disabledFieldClass);
    }

    public void setDisabledFieldStyle(ValueExpression disabledFieldStyle) {
        super.setDisabledFieldStyle(disabledFieldStyle);
    }

}


