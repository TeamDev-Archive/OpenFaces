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

import org.openfaces.ajax.AjaxViewHandler;
import org.openfaces.ajax.AjaxViewRoot;
import org.openfaces.util.Environment;

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
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Eugene Goncharov
 */
@SuppressWarnings({"RawUseOfParameterizedType", "deprecation"})
public class OpenFacesApplication extends Application {
    private final Application delegate;
    private final List<String> viewHandlers = new ArrayList<String>();

    public OpenFacesApplication(Application application) {
        delegate = application;
    }


    @Override
    public ActionListener getActionListener() {
        return delegate.getActionListener();
    }

    @Override
    public void setActionListener(ActionListener actionListener) {
        delegate.setActionListener(actionListener);
    }

    @Override
    public Locale getDefaultLocale() {
        return delegate.getDefaultLocale();
    }

    @Override
    public void setDefaultLocale(Locale locale) {
        delegate.setDefaultLocale(locale);
    }

    @Override
    public String getDefaultRenderKitId() {
        return delegate.getDefaultRenderKitId();
    }

    @Override
    public void setDefaultRenderKitId(String renderKitId) {
        delegate.setDefaultRenderKitId(renderKitId);
    }

    @Override
    public String getMessageBundle() {
        return delegate.getMessageBundle();
    }

    @Override
    public void setMessageBundle(String bundle) {
        delegate.setMessageBundle(bundle);
    }

    @Override
    public NavigationHandler getNavigationHandler() {
        return delegate.getNavigationHandler();
    }

    @Override
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        delegate.setNavigationHandler(navigationHandler);
    }

    @Override
    public PropertyResolver getPropertyResolver() {
        return delegate.getPropertyResolver();
    }

    @Override
    public void setPropertyResolver(PropertyResolver propertyResolver) {
        delegate.setPropertyResolver(propertyResolver);
    }

    @Override
    public VariableResolver getVariableResolver() {
        return delegate.getVariableResolver();
    }

    @Override
    public void setVariableResolver(VariableResolver variableResolver) {
        delegate.setVariableResolver(variableResolver);
    }

    @Override
    public ViewHandler getViewHandler() {
        return delegate.getViewHandler();
    }

    @Override
    public void setViewHandler(ViewHandler viewHandler) {
        final String viewHandlerClass = viewHandler.getClass().getName();
        if (viewHandlers.contains(viewHandlerClass) &&
                !viewHandlerClass.equalsIgnoreCase("org.ajax4jsf.application.AjaxViewHandler")) {
            return;
        }

        AjaxViewHandler ajaxViewHandler = viewHandler instanceof AjaxViewHandler
                ? (AjaxViewHandler) viewHandler
                : new AjaxViewHandler(viewHandler);
        delegate.setViewHandler(ajaxViewHandler);

        viewHandlers.add(viewHandlerClass);
    }

    @Override
    public StateManager getStateManager() {
        return delegate.getStateManager();
    }

    @Override
    public void setStateManager(StateManager stateManager) {
        delegate.setStateManager(stateManager);
    }

    @Override
    public void addComponent(String componentType, String componentClass) {
        delegate.addComponent(componentType, componentClass);
    }

    @SuppressWarnings({"AssignmentToMethodParameter"})
    @Override
    public UIComponent createComponent(String componentType) throws FacesException {
        if (Environment.isTrinidad() && componentType.equals(UIViewRoot.COMPONENT_TYPE)) {
            componentType = AjaxViewRoot.COMPONENT_TYPE;
        }

        return delegate.createComponent(componentType);
    }

    @Override
    public UIComponent createComponent(ValueBinding valueBinding, FacesContext facesContext, String s) throws FacesException {
        return delegate.createComponent(valueBinding, facesContext, s);
    }

    @Override
    public Iterator<String> getComponentTypes() {
        return delegate.getComponentTypes();
    }

    @Override
    public void addConverter(String converterId, String converterClass) {
        delegate.addConverter(converterId, converterClass);
    }

    @Override
    public void addConverter(Class targetClass, String converterClass) {
        delegate.addConverter(targetClass, converterClass);
    }

    @Override
    public Converter createConverter(String converterId) {
        return delegate.createConverter(converterId);
    }

    @Override
    public Converter createConverter(Class targetClass) {
        return delegate.createConverter(targetClass);
    }

    @Override
    public Iterator<String> getConverterIds() {
        return delegate.getConverterIds();
    }

    @Override
    public Iterator<Class> getConverterTypes() {
        return delegate.getConverterTypes();
    }

    @Override
    public MethodBinding createMethodBinding(String ref, Class[] params) throws ReferenceSyntaxException {
        return delegate.createMethodBinding(ref, params);
    }

    @Override
    public Iterator<Locale> getSupportedLocales() {
        return delegate.getSupportedLocales();
    }

    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
        delegate.setSupportedLocales(locales);
    }

    @Override
    public void addValidator(String validatorId, String validatorClass) {
        delegate.addValidator(validatorId, validatorClass);
    }

    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        return delegate.createValidator(validatorId);
    }

    @Override
    public Iterator<String> getValidatorIds() {
        return delegate.getValidatorIds();
    }

    @Override
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException {
        return delegate.createValueBinding(ref);
    }


    @Override
    public ResourceBundle getResourceBundle(FacesContext context, String name) {
        return delegate.getResourceBundle(context, name);
    }

    @Override
    public void addELResolver(ELResolver resolver) {
        delegate.addELResolver(resolver);
    }

    @Override
    public ELResolver getELResolver() {
        return delegate.getELResolver();
    }

    @Override
    public UIComponent createComponent(ValueExpression componentExpression,
                                       FacesContext context,
                                       String componentType) {
        return delegate.createComponent(componentExpression, context, componentType);
    }

    @Override
    public ExpressionFactory getExpressionFactory() {
        return delegate.getExpressionFactory();
    }

    @Override
    public Object evaluateExpressionGet(FacesContext context,
                                        String expression,
                                        Class expectedType) throws ELException {
        return delegate.evaluateExpressionGet(context, expression, expectedType);
    }

    @Override
    public void addELContextListener(ELContextListener listener) {
        delegate.addELContextListener(listener);
    }

    @Override
    public void removeELContextListener(ELContextListener listener) {
        delegate.removeELContextListener(listener);
    }

    @Override
    public ELContextListener[] getELContextListeners() {
        return delegate.getELContextListeners();
    }


}
