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

import org.hibernate.validator.NotNull;
import org.openfaces.validator.ClientValidator;
import org.openfaces.validator.RequiredClientValidator;

import java.util.HashMap;
import java.util.Map;
import java.lang.annotation.Annotation;

public class ClientValidatorsRegistry {
    private static final ClientValidatorsRegistry REGISTRY = new ClientValidatorsRegistry();
    private Map<Class<? extends Annotation>, ClientValidator> validators = new HashMap<Class<? extends Annotation>, ClientValidator>();

    private ClientValidatorsRegistry() {
        validators.put(NotNull.class, new RequiredClientValidator());
    }

    public static ClientValidatorsRegistry getInstance() {
        return REGISTRY;
    }


    /**
     * Method should return proper client validator for validation annotation
     * or null if there is no client validator registered for particular annotation
     *
     * @param annotationClass
     * @return client validator
     */
    public ClientValidator getValidator(Class<? extends Annotation> annotationClass) {
        if (validators.containsKey(annotationClass)) {
            return validators.get(annotationClass);
        }

        return null;
    }
}
