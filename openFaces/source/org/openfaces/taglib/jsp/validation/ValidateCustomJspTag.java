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

import org.openfaces.taglib.internal.validation.ValidateCustomTag;
import org.openfaces.taglib.jsp.AbstractCustomValidatorJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ValidateCustomJspTag extends AbstractCustomValidatorJspTag {

    public ValidateCustomJspTag() {
        super(new ValidateCustomTag(), "org.openfaces.Custom");
    }

    public void setServerFunction(MethodExpression serverFunction) {
        getDelegate().setPropertyValue("serverFunction", serverFunction);
    }

    public void setClientFunction(ValueExpression clientFunction) {
        getDelegate().setPropertyValue("clientFunction", clientFunction);
    }

}
