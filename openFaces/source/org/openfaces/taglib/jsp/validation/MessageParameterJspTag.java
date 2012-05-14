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
package org.openfaces.taglib.jsp.validation;

import org.openfaces.taglib.jsp.AbstractCustomValidatorJspTag;

import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class MessageParameterJspTag extends TagSupport {
    private ValueExpression value;

    public void setValue(ValueExpression value) {
        this.value = value;
    }

    public int doStartTag() throws JspException {
        super.doStartTag();

        Tag parentTag = this.getParent();
        if (!(parentTag instanceof AbstractCustomValidatorJspTag))
            throw new JspException("no parent ValidateCustomJspTag found");

        ValidateCustomJspTag validateCustomTag = (ValidateCustomJspTag) parentTag;
        validateCustomTag.getParams().add(value.getExpressionString());

        return Tag.SKIP_BODY;
    }
}
