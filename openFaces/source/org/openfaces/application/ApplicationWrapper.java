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

package org.openfaces.application;

import javax.el.ELContextListener;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Dmitry Pikhulya
 */
public abstract class ApplicationWrapper extends Application {
    protected final Application wrapped;

    public ApplicationWrapper(Application application) {
        wrapped = application;
    }

    @Override
    public ActionListener getActionListener() {
        return wrapped.getActionListener();
    }

    @Override
    public void setActionListener(ActionListener actionListener) {
        wrapped.setActionListener(actionListener);
    }

    @Override
    public Locale getDefaultLocale() {
        return wrapped.getDefaultLocale();
    }

    @Override
    public void setDefaultLocale(Locale locale) {
        wrapped.setDefaultLocale(locale);
    }

    @Override
    public String getDefaultRenderKitId() {
        return wrapped.getDefaultRenderKitId();
    }

    @Override
    public void setDefaultRenderKitId(String renderKitId) {
        wrapped.setDefaultRenderKitId(renderKitId);
    }

    @Override
    public String getMessageBundle() {
        return wrapped.getMessageBundle();
    }

    @Override
    public void setMessageBundle(String bundle) {
        wrapped.setMessageBundle(bundle);
    }

    @Override
    public NavigationHandler getNavigationHandler() {
        return wrapped.getNavigationHandler();
    }

    @Override
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        wrapped.setNavigationHandler(navigationHandler);
    }

    @Override
    public PropertyResolver getPropertyResolver() {
        return wrapped.getPropertyResolver();
    }

    @Override
    public void setPropertyResolver(PropertyResolver propertyResolver) {
        wrapped.setPropertyResolver(propertyResolver);
    }

    @Override
    public VariableResolver getVariableResolver() {
        return wrapped.getVariableResolver();
    }

    @Override
    public void setVariableResolver(VariableResolver variableResolver) {
        wrapped.setVariableResolver(variableResolver);
    }

    @Override
    public ViewHandler getViewHandler() {
        return wrapped.getViewHandler();
    }

    @Override
    public StateManager getStateManager() {
        return wrapped.getStateManager();
    }

    @Override
    public void setStateManager(StateManager stateManager) {
        wrapped.setStateManager(stateManager);
    }

    @Override
    public void addComponent(String componentType, String componentClass) {
        wrapped.addComponent(componentType, componentClass);
    }

    @Override
    public UIComponent createComponent(ValueBinding valueBinding, FacesContext facesContext, String s) throws FacesException {
        return wrapped.createComponent(valueBinding, facesContext, s);
    }

    @Override
    public Iterator<String> getComponentTypes() {
        return wrapped.getComponentTypes();
    }

    @Override
    public void addConverter(String converterId, String converterClass) {
        wrapped.addConverter(converterId, converterClass);
    }

    @Override
    public void addConverter(Class targetClass, String converterClass) {
        wrapped.addConverter(targetClass, converterClass);
    }

    @Override
    public Converter createConverter(String converterId) {
        return wrapped.createConverter(converterId);
    }

    @Override
    public Converter createConverter(Class targetClass) {
        return wrapped.createConverter(targetClass);
    }

    @Override
    public Iterator<String> getConverterIds() {
        return wrapped.getConverterIds();
    }

    @Override
    public Iterator<Class> getConverterTypes() {
        return wrapped.getConverterTypes();
    }

    @Override
    public MethodBinding createMethodBinding(String ref, Class[] params) throws ReferenceSyntaxException {
        return wrapped.createMethodBinding(ref, params);
    }

    @Override
    public Iterator<Locale> getSupportedLocales() {
        return wrapped.getSupportedLocales();
    }

    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
        wrapped.setSupportedLocales(locales);
    }

    @Override
    public void addValidator(String validatorId, String validatorClass) {
        wrapped.addValidator(validatorId, validatorClass);
    }

    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        return wrapped.createValidator(validatorId);
    }

    @Override
    public Iterator<String> getValidatorIds() {
        return wrapped.getValidatorIds();
    }

    @Override
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException {
        return wrapped.createValueBinding(ref);
    }

    @Override
    public ResourceBundle getResourceBundle(FacesContext context, String name) {
        return wrapped.getResourceBundle(context, name);
    }

    @Override
    public void addELResolver(ELResolver resolver) {
        wrapped.addELResolver(resolver);
    }

    @Override
    public ELResolver getELResolver() {
        return wrapped.getELResolver();
    }

    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
                                       FacesContext context,
                                       String componentType) {
        return wrapped.createComponent(componentExpression, context, componentType);
    }

    @Override
    public ExpressionFactory getExpressionFactory() {
        return wrapped.getExpressionFactory();
    }

    @Override
    public Object evaluateExpressionGet(FacesContext context,
                                        String expression,
                                        Class expectedType) throws ELException {
        return wrapped.evaluateExpressionGet(context, expression, expectedType);
    }

    @Override
    public void addELContextListener(ELContextListener listener) {
        wrapped.addELContextListener(listener);
    }

    @Override
    public void removeELContextListener(ELContextListener listener) {
        wrapped.removeELContextListener(listener);
    }

    @Override
    public ELContextListener[] getELContextListeners() {
        return wrapped.getELContextListeners();
    }
}
