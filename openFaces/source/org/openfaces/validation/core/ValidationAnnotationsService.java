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

package org.openfaces.validation.core;

import org.openfaces.validator.ClientValidator;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

public class ValidationAnnotationsService {

    public ValidationAnnotationsService() {
    }

    public List<Annotation> findValidationAnnotationsOnProperty(Class aClass, String propertyName) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        List<Annotation> result = Collections.EMPTY_LIST;

        try {
            List<Annotation> allAnnotations = new ArrayList<Annotation>();
            Field property = aClass.getDeclaredField(propertyName);

            if (property != null) {
                Annotation[] propertyAnnotations = property.getAnnotations();
                for (Annotation annotation : propertyAnnotations) {
                    if (!allAnnotations.contains(annotation)) {
                        allAnnotations.add(annotation);
                    }
                }

                PropertyDescriptor[] descriptors = Introspector.getBeanInfo(aClass).getPropertyDescriptors();

                for (PropertyDescriptor descriptor : descriptors) {
                    if (descriptor.getName().equals(propertyName)) {
                        Method readMethod = descriptor.getReadMethod();

                        Annotation[] readMethodAnnotations = readMethod.getAnnotations();
                        for (Annotation annotation : readMethodAnnotations) {
                            if (!allAnnotations.contains(annotation)) {
                                allAnnotations.add(annotation);
                            }
                        }
                    }
                }

                if (!allAnnotations.isEmpty()) {
                    result = new ArrayList<Annotation>();
                    result.addAll(allAnnotations);
                }
            }
        } catch (NoSuchFieldException e) {
            context.log(e.getMessage(), e);
        } catch (IntrospectionException e) {
            context.log(e.getMessage(), e);
        }

        return result;
    }

    public List<ClientValidator> findClientValidatorsByAnnotations(Collection<? extends Annotation> validationAnnotations) {
        List<ClientValidator> clientValidators = Collections.EMPTY_LIST;

        if (validationAnnotations != null) {
            for (Annotation validationAnnotation : validationAnnotations) {
                ClientValidator validator = ClientValidatorsRegistry.getInstance().getValidator(validationAnnotation);

                if (validator != null) {
                    if (clientValidators.isEmpty()) {
                        clientValidators = new ArrayList<ClientValidator>();
                    }

                    clientValidators.add(validator);
                }
            }
        }

        return clientValidators;
    }
}
