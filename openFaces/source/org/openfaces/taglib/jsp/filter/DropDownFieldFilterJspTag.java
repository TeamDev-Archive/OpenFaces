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
package org.openfaces.taglib.jsp.filter;

import org.openfaces.taglib.internal.filter.DropDownFieldFilterTag;

import javax.el.ValueExpression;

/**
 * @author Dmitry Pikhulya
 */
public class DropDownFieldFilterJspTag extends AutoCompleteFilterJspTagBase {
    public DropDownFieldFilterJspTag() {
        super(new DropDownFieldFilterTag());
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


}
