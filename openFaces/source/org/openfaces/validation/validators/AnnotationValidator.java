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
package org.openfaces.validation.validators;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.hibernate.validator.InvalidValue;
import org.openfaces.validation.core.CoreValidatorImpl;
import org.openfaces.validation.core.FacesMessages;
import org.openfaces.validation.CoreValidator;

import javax.el.ELException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;

/**
 * <p><strong>AnnotationValidator</strong></p> is an implementation of {@link Validator} interface
 * that validates a value of corresponding {@link UIComponent} instance.
 * AnnotationValidator use value expression that refers to property for obtaining
 * a set of annotations which will be constraints for validation.
 *
 * @author Eugene Goncharov
 */
public class AnnotationValidator implements Validator {

    private static final String VALUE = "value";
    private CoreValidator myValidator = new CoreValidatorImpl();

    public void validate(FacesContext facesContext, UIComponent component,
                         Object value) throws ValidatorException {
        ValueExpression valueExpression = component.getValueExpression(VALUE);
        if (valueExpression != null) {
            InvalidValue[] invalidValues;
            try {
                invalidValues = myValidator.validate(valueExpression,
                        facesContext.getELContext(), value);
            } catch (ELException elException) {
                Throwable cause = elException.getCause();
                if (cause == null)
                    cause = elException;
                throw new ValidatorException(createMessage(cause), cause);
            }

            if (invalidValues != null && invalidValues.length > 0) {
                throw new ValidatorException(createMessage(invalidValues));
            }
        }
    }

    private FacesMessage createMessage(InvalidValue[] invalidValues) {
        return FacesMessages.createFacesMessage(FacesMessage.SEVERITY_ERROR,
                invalidValues[0].getMessage());
    }

    private FacesMessage createMessage(Throwable cause) {
        return new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Validation failed:" + cause.getMessage(), null);
    }
}
