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

package org.openfaces.validation.core;

import org.hibernate.validator.Digits;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.openfaces.validator.ClientValidator;
import org.openfaces.validator.EMailValidator;
import org.openfaces.validator.LengthClientValidator;
import org.openfaces.validator.NumberConverterClientValidator;
import org.openfaces.validator.RequiredClientValidator;
import org.openfaces.validator.RegexValidator;

import javax.faces.convert.NumberConverter;
import javax.faces.validator.LengthValidator;
import java.lang.annotation.Annotation;

public class ClientValidatorsRegistry {
    private static final ClientValidatorsRegistry REGISTRY = new ClientValidatorsRegistry();

    private ClientValidatorsRegistry() {


    }

    public static ClientValidatorsRegistry getInstance() {
        return REGISTRY;
    }


    /**
     * Method should return proper client validator for validation annotation
     * or null if there is no client validator registered for particular annotation
     *
     * @param annotation
     * @return client validator
     */
    public ClientValidator getValidator(Annotation annotation) {
        Class<? extends Annotation> annotationClass = annotation.annotationType();
        if (annotationClass.equals(NotNull.class)) {
            return new RequiredClientValidator();
        } else if (annotationClass.equals(Email.class)) {
            return new EMailValidator();
        } else if (annotationClass.equals(Length.class)) {
            Length length = (Length) annotation;
            LengthValidator lengthValidator = new LengthValidator(length.max(), length.min());
            LengthClientValidator lengthClientValidator = new LengthClientValidator();
            lengthClientValidator.setLengthValidator(lengthValidator);
            return lengthClientValidator;
        } else if (annotationClass.equals(Pattern.class)) {
            Pattern pattern = (Pattern) annotation;
            RegexValidator regexValidator = new RegexValidator();
            regexValidator.setPattern(pattern.regex());
            return regexValidator;
        }

        return null;
    }
}
