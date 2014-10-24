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
import org.openfaces.taglib.internal.input.DropDownFieldTag;

import javax.el.ValueExpression;

/**
 * @author Andriy Palval
 */
public class DropDownFieldJspTag extends DropDownFieldJspTagBase {

    public DropDownFieldJspTag() {
        super(new DropDownFieldTag());
    }

    protected DropDownFieldJspTag(DropDownComponentTag delegate) {
        super(delegate);
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

    public void setChangeValueOnSelect(ValueExpression changeValueOnSelect) {
        super.setChangeValueOnSelect(changeValueOnSelect);
    }

}
