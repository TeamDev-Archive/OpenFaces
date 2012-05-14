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
package org.openfaces.taglib.internal.validation;

import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.validator.AbstractCustomValidator;
import org.openfaces.validator.CustomValidator;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class ValidateCustomTag extends AbstractCustomValidatorTag {
    @Override
    public void setProperties(AbstractCustomValidator abstractCustomValidator) {
        super.setProperties(abstractCustomValidator);
        CustomValidator customValidator = (CustomValidator) abstractCustomValidator;
        String clientFunction = getPropertyValue("clientFunction");
        customValidator.setClientFunction(clientFunction);
        String serverFunction = getPropertyValue("serverFunction");
        if (serverFunction != null) {
            if (AbstractComponentTag.isValueReference(serverFunction)) {
                Class[] args = {FacesContext.class, UIComponent.class, Object.class};
                FacesContext facesContext = FacesContext.getCurrentInstance();
                MethodExpression methodExpression = createMethodExpression(facesContext, "serverFunction", serverFunction, boolean.class, args);
                Class resultType = methodExpression.getMethodInfo(facesContext.getELContext()).getReturnType();
                if (!resultType.equals(boolean.class))
                    throw new IllegalArgumentException("serverFunction error. Requred return value: boolean. Currently defined: " + resultType);
                customValidator.setServerFunction(methodExpression);
            } else {
                throw new IllegalArgumentException("Invalid value of serverFunction attribute. Must be a value reference. Currently defined: " + serverFunction);
            }
        }

    }
}
