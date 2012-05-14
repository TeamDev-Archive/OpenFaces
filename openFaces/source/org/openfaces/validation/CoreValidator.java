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
package org.openfaces.validation;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

import org.hibernate.validator.InvalidValue;
import org.openfaces.validator.ClientValidator;

import java.util.List;

/**
 * <p>A <strong>CoreValidator</strong></p> implementation is a class that can
 * perform validation.
 *
 * @author Eugene Goncharov
 */
public interface CoreValidator {

    /**
     * Validate that the given value can be assigned to the property given by
     * the value expression.
     *
     * @param valueExpression a value expression, referring to a property
     * @param elContext       the ELContext in which to evaluate the expression
     * @param value           a value to be assigned to the property
     * @return a set of potential InvalidValues, from Hibernate Validator
     */
    public InvalidValue[] validate(ValueExpression valueExpression,
                                   ELContext elContext, Object value);

    public List<ClientValidator> getClientValidatorsForComponent(UIComponent component, ELContext elContext);
}