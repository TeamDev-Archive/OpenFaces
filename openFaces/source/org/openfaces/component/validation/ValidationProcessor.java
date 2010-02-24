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
package org.openfaces.component.validation;

import org.openfaces.util.Components;
import org.openfaces.util.Finder;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UIMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Vladimir Korenev
 */
public class ValidationProcessor extends UIComponentBase {
    private static final String APPLICATION_PARAM_CLIENT_VALIDATION = "org.openfaces.validation.clientValidation";
    private static final String APPLICATION_PARAM_DEFAULT_SERVER_VALIDATION_PRESENTATION
            = "org.openfaces.validation.useDefaultServerPresentation";
    private static final String APPLICATION_PARAM_DEFAULT_CLIENT_VALIDATION_PRESENTATION
            = "org.openfaces.validation.useDefaultClientPresentation";
    private static final String APPLICATION_PARAM_DEFAULT_VALIDATION_PRESENTATION_CLASS
            = "org.openfaces.validation.defaultPresentationClass";

    private static final String APPLICATION_PARAM_OPENFACES_VALIDATION_DISABLED = "org.openfaces.validation.disabled";

    public static final String CLEAR_VERIFIABLE_COMPONENTS = "clearVerifiableComponents";

    public static final String COMPONENT_TYPE = "org.openfaces.ValidationProcessor";
    private static final String DEFAULT_ID = "openFacesValidationProcessor";

    private String contextId;

    private ClientValidationMode applicationClientValidation;
    private boolean useDefaultServerValidationPresentationForApplication;
    private boolean useDefaultClientValidationPresentationForApplication;
    private UIMessage defaultPresentationInstance;

    private Set<VerifiableComponent> verifiableComponents;

    private boolean globalMessagesProcessed;
    private transient Map<String, ClientValidationSupport> renderToClientValidationSupport = new HashMap<String, ClientValidationSupport>();

    public ValidationProcessor() {
        setRendered(false);
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                applicationClientValidation != null ? applicationClientValidation.toString() : null,
                useDefaultServerValidationPresentationForApplication,
                useDefaultClientValidationPresentationForApplication,
                saveAttachedState(context, defaultPresentationInstance)
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        applicationClientValidation = ClientValidationMode.fromString((String) stateArray[i++]);
        useDefaultServerValidationPresentationForApplication = (Boolean) stateArray[i++];
        useDefaultClientValidationPresentationForApplication = (Boolean) stateArray[i++];
        defaultPresentationInstance = (UIMessage) restoreAttachedState(context, stateArray[i++]);
    }

    public boolean isGlobalMessagesProcessed() {
        return globalMessagesProcessed;
    }

    public void confirmGlobalMessagesProcessing() {
        globalMessagesProcessed = true;
    }

    public VerifiableComponent[] getVerifiableComponents(FacesContext context) {
        clearVerifiableComponentsIfNecessary(context);
        if (verifiableComponents == null)
            return null;

        return verifiableComponents.toArray(
                new VerifiableComponent[verifiableComponents.size()]);
    }

    public void addVerifiableComponent(VerifiableComponent component) {
        if (component == null)
            return;

        if (verifiableComponents == null) verifiableComponents = new HashSet<VerifiableComponent>();

        verifiableComponents.add(component);
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }


    public String getFamily() {
        return null;
    }

    public void clearVerifiableComponentsIfNecessary(FacesContext context) {
        // JSFC-2524
        Boolean isClear = ((Boolean) context.getExternalContext().getRequestMap().get(CLEAR_VERIFIABLE_COMPONENTS));
        if (isClear != null && isClear) {
            verifiableComponents = null;
            context.getExternalContext().getRequestMap().put(CLEAR_VERIFIABLE_COMPONENTS, Boolean.FALSE);
        }
    }

    /**
     * This method need for case when ViewRoot not recreate after phase RESTORE_VIEW.
     */
    public static void resetVerifiableComponents(FacesContext context) {
        // JSFC-2524
        context.getExternalContext().getRequestMap().put(CLEAR_VERIFIABLE_COMPONENTS, Boolean.TRUE);
    }

    public static ValidationProcessor getInstance(FacesContext context) {
        if (context == null)
            return null;

        UIViewRoot uiViewRoot = context.getViewRoot();
        if (uiViewRoot == null)
            return null;

        if (isOpenFacesValidationDisabled(context))
            return null;

        ValidationProcessor processor = (ValidationProcessor) uiViewRoot.getFacet("_OpenFacesValidationProcessor_");
        if (processor == null) {
            UIViewRoot viewRoot = context.getViewRoot();
            if (viewRoot == null)
                throw new IllegalStateException("viewRoot shouldn't be null");

            processor = new ValidationProcessor();
            processor.setId(DEFAULT_ID);
            viewRoot.getFacets().put("_OpenFacesValidationProcessor_", processor);

            initProcessor(context, processor);
        }
        return processor;
    }

    public static boolean isOpenFacesValidationDisabled(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String validationDisabled = externalContext.getInitParameter(APPLICATION_PARAM_OPENFACES_VALIDATION_DISABLED);
        return validationDisabled != null && Boolean.valueOf(validationDisabled);
    }

    public static void initProcessor(FacesContext context, ValidationProcessor processor) {
        ExternalContext externalContext = context.getExternalContext();
        String appClientValidation = externalContext.getInitParameter(APPLICATION_PARAM_CLIENT_VALIDATION);
        ClientValidationMode clientValidationObj = ClientValidationMode.fromString(appClientValidation);
        processor.setApplicationClientValidation(clientValidationObj);

        String useDefaultServerPresentation = externalContext.getInitParameter(APPLICATION_PARAM_DEFAULT_SERVER_VALIDATION_PRESENTATION);
        processor.setUseDefaultServerValidationPresentationForApplication(useDefaultServerPresentation == null
                || Boolean.valueOf(useDefaultServerPresentation));
        String useDefaultClientPresentation = externalContext.getInitParameter(APPLICATION_PARAM_DEFAULT_CLIENT_VALIDATION_PRESENTATION);
        processor.setUseDefaultClientValidationPresentationForApplication(useDefaultClientPresentation == null
                || Boolean.valueOf(useDefaultClientPresentation));

        String defaultValidationPresentationClass = externalContext.getInitParameter(APPLICATION_PARAM_DEFAULT_VALIDATION_PRESENTATION_CLASS);
        if (defaultValidationPresentationClass != null && defaultValidationPresentationClass.length() > 0) {
            try {
                Class presentationClass = Class.forName(defaultValidationPresentationClass);
                Field field = presentationClass.getField("COMPONENT_TYPE");
                if (field == null)
                    throw new FacesException("Invalid value of " + APPLICATION_PARAM_DEFAULT_VALIDATION_PRESENTATION_CLASS + 
                            " init parameter in web.xml. Seems that the specified class is not a component class since the " +
                            "COMPONENT_TYPE constant field cannot be found in this class: " + defaultValidationPresentationClass);
                String componentType = (String) field.get(null);
                Object presentationInstance = context.getApplication().createComponent(componentType);

                if (!(presentationInstance instanceof UIMessage)) {
                    throw new FacesException("Invalid value of " + APPLICATION_PARAM_DEFAULT_VALIDATION_PRESENTATION_CLASS +
                            " init parameter in web.xml. The specified class is not a validation message class as it " +
                            "doesn't extend the javax.faces.component.UIMessage class: " + defaultValidationPresentationClass);
                }
                configureDefaultPresentationInstance(externalContext, presentationInstance);
                processor.setDefaultPresentationInstance((UIMessage) presentationInstance);

            } catch (ClassNotFoundException e) {
                throw new FacesException("Invalid value of " + APPLICATION_PARAM_DEFAULT_VALIDATION_PRESENTATION_CLASS +
                        " init parameter in web.xml. Could not find the specified class: " + defaultValidationPresentationClass, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void configureDefaultPresentationInstance(ExternalContext externalContext, Object presentationInstance)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Map<String, PropertyDescriptor> propertyDescriptorsMap = getPropertyDescriptors(presentationInstance.getClass());
        Map<String, String> presentationParams = new HashMap<String, String>();
        Map initParameters = externalContext.getInitParameterMap();
        String className = presentationInstance.getClass().getName();
        String paramPrefix = className.substring(className.lastIndexOf('.') + 1) + ".";
        for (Map.Entry entry : (Set<Map.Entry>) initParameters.entrySet()) {
            String key = (String) entry.getKey();
            if (!key.startsWith(paramPrefix)) continue;
            String realParamName = key.substring(key.indexOf(".") + 1);
            presentationParams.put(realParamName, (String) entry.getValue());
        }
        for (Map.Entry<String, String> entry : presentationParams.entrySet()) {
            String propertyName = entry.getKey();
            String propertyValue = entry.getValue();
            PropertyDescriptor pd = propertyDescriptorsMap.get(propertyName);
            if (pd == null) {
                throw new FacesException("Error processing init parameter " + paramPrefix + propertyName + ": couldn't find property \"" + propertyName + "\" in  class: " + presentationInstance.getClass().getName());
            } else {
                Class propertyType = pd.getPropertyType();
                Object resValue;
                if (propertyType.equals(Boolean.TYPE)) {
                    resValue = Boolean.valueOf(propertyValue);
                } else if (propertyType.equals(String.class)) {
                    resValue = propertyValue;
                } else if (propertyType.equals(Short.TYPE)) {
                    resValue = Short.valueOf(propertyValue);
                } else if (propertyType.equals(Integer.TYPE)) {
                    resValue = Integer.valueOf(propertyValue);
                } else if (propertyType.equals(Long.TYPE)) {
                    resValue = Long.valueOf(propertyValue);
                } else if (propertyType.equals(Float.TYPE)) {
                    resValue = Float.valueOf(propertyValue);
                } else if (propertyType.equals(Double.TYPE)) {
                    resValue = Double.valueOf(propertyValue);
                } else if (propertyType.equals(Character.TYPE)) {
                    resValue = propertyValue.length() >= 1 ? propertyValue.charAt(0) : '?';
                } else {
                    throw new UnsupportedOperationException("Attribute type '" + propertyType.getName() + "' for attribute '" + propertyName + "' is not supported.");
                }
                pd.getWriteMethod().invoke(presentationInstance, resValue);
            }
        }
    }

    private static Map<String, PropertyDescriptor> getPropertyDescriptors(Class presentationClass) throws IntrospectionException {
        BeanInfo presentationBeanInfo = Introspector.getBeanInfo(presentationClass);
        PropertyDescriptor[] propertyDescriptors = presentationBeanInfo.getPropertyDescriptors();
        Map<String, PropertyDescriptor> propertyDescriptorsMap = new HashMap<String, PropertyDescriptor>();

        for (int i = 0, propertyCount = propertyDescriptors.length; i < propertyCount; i++) {
            PropertyDescriptor pd = propertyDescriptors[i];
            if (pd.getReadMethod() != null) {
                propertyDescriptorsMap.put(pd.getName(), pd);
            }
        }
        return propertyDescriptorsMap;
    }

    public ClientValidationMode getClientValidationRuleForComponent(VerifiableComponent vc) {
        ClientValidationMode clientValidation = vc.getClientValidation();
        if (clientValidation != null) return clientValidation;
        UIForm parentForm = vc.getParentForm();
        clientValidation = getClientValidationRuleForForm(parentForm);
        return clientValidation;
    }

    public ClientValidationMode getClientValidationRuleForForm(UIForm form) {
        ClientValidationSupport validationSupport = getClientValidationSupport(form);
        if (validationSupport != null) {
            ClientValidationMode clientValidation = validationSupport.getClientValidation();
            if (clientValidation != null) return clientValidation;
        }
        return getClientValidationRuleForApplication();
    }

    public ClientValidationMode getClientValidationRuleForApplication() {
        if (applicationClientValidation == null)
            return ClientValidationMode.OFF;
        return applicationClientValidation;
    }

    public ClientValidationSupport getClientValidationSupport(UIComponent component) {
        if (component == null) {
            return null;
        }

        String componentId = component.getClientId(getFacesContext());
        ClientValidationSupport cvs = renderToClientValidationSupport.get(componentId);
        if (cvs == null) {
            Finder finder = new Finder(component) {
                public boolean test(UIComponent component) {
                    return component instanceof ClientValidationSupport;
                }
            };
            cvs = (ClientValidationSupport) finder.getComponent();
            renderToClientValidationSupport.put(componentId, cvs);
        }
        return cvs;
    }

    public void setApplicationClientValidation(ClientValidationMode applicationClientValidation) {
        this.applicationClientValidation = applicationClientValidation;
    }

    public boolean isUseDefaultServerValidationPresentationForForm(UIForm form) {
        if (form == null) {
            return isUseDefaultServerValidationPresentationForApplication();
        }
        ClientValidationSupport clientValidationSupport = getClientValidationSupport(form);
        if (clientValidationSupport != null) {
            Boolean useDSVP = clientValidationSupport.getUseDefaultServerValidationPresentation();
            if (useDSVP != null) return useDSVP;
        }
        return isUseDefaultServerValidationPresentationForApplication();
    }

    public boolean isUseDefaultServerValidationPresentationForApplication() {
        return useDefaultServerValidationPresentationForApplication;
    }

    public void setUseDefaultServerValidationPresentationForApplication(boolean useDefaultServerValidationPresentationForApplication) {
        this.useDefaultServerValidationPresentationForApplication = useDefaultServerValidationPresentationForApplication;
    }

    public boolean isUseDefaultClientValidationPresentationForForm(UIForm form) {
        if (form == null) {
            return isUseDefaultClientValidationPresentationForApplication();
        }
        ClientValidationSupport clientValidationSupport = getClientValidationSupport(form);
        if (clientValidationSupport != null) {
            Boolean useDCVP = clientValidationSupport.getUseDefaultClientValidationPresentation();
            if (useDCVP != null) {
                return useDCVP;
            }
        }
        return isUseDefaultClientValidationPresentationForApplication();
    }

    public boolean isUseDefaultClientValidationPresentationForApplication() {
        return useDefaultClientValidationPresentationForApplication;
    }

    public void setUseDefaultClientValidationPresentationForApplication(boolean useDefaultClientValidationPresentationForApplication) {
        this.useDefaultClientValidationPresentationForApplication = useDefaultClientValidationPresentationForApplication;
    }

    public UIMessage getDefaultPresentationInstance(FacesContext context, ClientValidationSupport support) {
        UIMessage result;
        if (support == null) {
            result = getDefaultPresentationInstanceForApplication();
        } else {
            result = support.getDefaultPresentation();
            if (result == null) {
                result = getDefaultPresentationInstanceForApplication();
            }
        }
        if (result == null) {
            result = (FloatingIconMessage) context.getApplication().createComponent(FloatingIconMessage.COMPONENT_TYPE);
        }
        return result;
    }

    private UIMessage getDefaultPresentationInstanceForApplication() {
        return defaultPresentationInstance;
    }

    public void setDefaultPresentationInstance(UIMessage defaultPresentationInstance) {
        this.defaultPresentationInstance = defaultPresentationInstance;
    }

    public ClientValidationMode getClientValidationRule(UIComponent component, UIComponent forComponent) {
        ClientValidationMode cv = null;
        if (forComponent != null) {
            String clientValidationAttribute = (String) forComponent.getAttributes().get("clientValidation");
            if (clientValidationAttribute != null) {
                cv = ClientValidationMode.fromString(clientValidationAttribute);
            }
        }
        if (cv == null) {
            UIForm form = Components.getEnclosingForm(component);
            cv = getClientValidationRuleForForm(form);
        }
        if (cv == null) {
            cv = getClientValidationRuleForApplication();
        }
        return cv;
    }
}
