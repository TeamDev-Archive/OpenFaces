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

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.openfaces.validation.CoreValidator;
import org.openfaces.validator.ClientValidator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.beans.FeatureDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * <strong>CoreValidatorImpl</strong>
 * </p>
 * is an implementation of {@link CoreValidator} interface. This class is used
 * to build and maintain annotations validation logic. It uses Hibernate
 * {@link ClassValidator} class to perform validation.
 *
 * @author Eugene Goncharov
 */
public class CoreValidatorImpl implements CoreValidator {
    private static Map<Key, ClassValidator<?>> ourClassValidators = new ConcurrentHashMap<Key, ClassValidator<?>>();
    private ValidationAnnotationsService validationAnnotationsService = new ValidationAnnotationsService();

    public CoreValidatorImpl() {

    }

    class Key {

        private Class<?> validatableClass;
        private java.util.Locale locale;

        public Key(Class<?> validatableClass, java.util.Locale locale) {
            this.validatableClass = validatableClass;
            this.locale = locale;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Key)) {
                return false;
            }
            Key key = (Key) other;
            return key.validatableClass.equals(validatableClass)
                    && key.locale.equals(locale);
        }

        @Override
        public int hashCode() {
            return validatableClass.hashCode() + locale.hashCode();
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see org.sevenhats.jsf.validation.core.CoreValidator#getValidator(T)
      */
    @SuppressWarnings("unchecked")
    public <T> ClassValidator<T> getValidator(T model) {
        Class<T> modelClass = (Class<T>) model.getClass();

        ClassValidator<T> validator = null;
        if (modelClass != null) {
            validator = getValidatorByClass(modelClass);
        }
        return validator;
    }

    /**
     * Get the cached ClassValidator instance.
     *
     * @param modelClass the class to be validated
     */

    @SuppressWarnings("unchecked")
    public <T> ClassValidator<T> getValidatorByClass(Class<T> modelClass) {
        Key key = new Key(modelClass, new Locale("en"));

        ClassValidator<T> result = (ClassValidator<T>) ourClassValidators
                .get(key);

        if (result == null) {
            result = createValidator(modelClass);
            ourClassValidators.put(key, result);
        }
        return result;
    }

    /**
     * Create a new ClassValidator for the given class
     *
     * @param modelClass the class to be validated
     */

    protected <T> ClassValidator<T> createValidator(Class<T> modelClass) {
        return new ClassValidator<T>(modelClass);
    }

    /*
      * (non-Javadoc)
      *
      * @see org.sevenhats.jsf.validation.core.CoreValidator#validate(javax.el.ValueExpression,
      *      javax.el.ELContext, java.lang.Object)
      */
    public InvalidValue[] validate(ValueExpression valueExpression,
                                   ELContext elContext, Object value) {
        ValidationAnnotationsELResolver validationAnnotationsELResolver = new ValidationAnnotationsELResolver(
                elContext.getELResolver());
        ELContext decoratedContext = EL.createELContext(elContext,
                validationAnnotationsELResolver);
        valueExpression.setValue(decoratedContext, value);
        return validationAnnotationsELResolver.getInvalidValues();
        //return new InvalidValue[]{};
    }


    public List<ClientValidator> getClientValidatorsForComponent(UIComponent component, ELContext elContext) {
        ValueExpression valueExpression = component.getValueExpression("value");
        ValidationAnnotationsELResolver validationAnnotationsELResolver = new ValidationAnnotationsELResolver(elContext.getELResolver());
        ELContext decoratedContext = EL.createELContext(elContext,
                validationAnnotationsELResolver);
        String emptyValue = new String("");
        valueExpression.setValue(decoratedContext, emptyValue);

        return validationAnnotationsELResolver.getClientValidators();
    }

    class ValidationAnnotationsELResolver extends ELResolver {
        private ELResolver delegate;
        private boolean clientValidationMode;
        private List<ClientValidator> clientValidators;
        private InvalidValue[] invalidValues;

        public ValidationAnnotationsELResolver(ELResolver delegate) {
            this.delegate = delegate;
        }

        public ValidationAnnotationsELResolver(ELResolver delegate, boolean isClientValidationMode) {
            this.delegate = delegate;
            this.clientValidationMode = isClientValidationMode;
        }

        @Override
        public Class<?> getCommonPropertyType(ELContext context, Object value) {
            return delegate.getCommonPropertyType(context, value);
        }

        @Override
        public Iterator<FeatureDescriptor> getFeatureDescriptors(
                ELContext context, Object value) {
            return delegate.getFeatureDescriptors(context, value);
        }

        @Override
        public Class<?> getType(ELContext context, Object x, Object y)
                throws NullPointerException, ELException {
            return delegate.getType(context, x, y);
        }

        @Override
        public Object getValue(ELContext context, Object base, Object property)
                throws NullPointerException, ELException {
            return delegate.getValue(context, base, property);
        }

        @Override
        public boolean isReadOnly(ELContext context, Object base,
                                  Object property) throws NullPointerException, ELException {
            return delegate.isReadOnly(context, base, property);
        }

        public boolean isClientValidationMode() {
            return clientValidationMode;
        }

        public List<ClientValidator> getClientValidators() {
            return clientValidators;
        }

        public InvalidValue[] getInvalidValues() {
            return invalidValues;
        }

        @Override
        public void setValue(ELContext context, Object base, Object property,
                             Object value) throws NullPointerException, ELException {
            if (base != null && property != null) {
                clientValidators = new ArrayList<ClientValidator>();
                context.setPropertyResolved(true);
                boolean skipValidation = false;

                try {
                    Class<?> notNullValidatorAnnotationClass = Class.forName("org.hibernate.validator.NotNull");
                } catch (ClassNotFoundException e) {
                    FacesContext.getCurrentInstance().
                            getExternalContext().log("Hibernate Validator library was not found in the classpath. Annotation Validation will be disabled.");
                    skipValidation = true;
                }

                if (skipValidation)
                    return;

                context.setPropertyResolved(true);
                String propertyNameString = property.toString();

                ClassValidator<Object> classValidator = getValidator(base);
                invalidValues = classValidator
                        .getPotentialInvalidValues(propertyNameString, value);

                List<Annotation> annotations = validationAnnotationsService.
                        findValidationAnnotationsOnProperty(base.getClass(), property.toString());

                List<ClientValidator> validators = validationAnnotationsService.findClientValidatorsByAnnotations(annotations);

                if (validators != null) {
                    clientValidators.addAll(validators);
                }
            }

        }
    }
}