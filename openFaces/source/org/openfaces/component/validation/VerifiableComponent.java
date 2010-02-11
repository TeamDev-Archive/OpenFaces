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


import org.openfaces.validator.ClientValidator;
import org.openfaces.validator.ClientValidatorUtil;
import org.openfaces.validator.RequiredClientValidator;
import org.openfaces.validator.ValidationJavascriptLibrary;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class VerifiableComponent {
    private static final String CLIENT_VALIDATION_PARAM_NAME = "clientValidation";
    private static final String DEFAULT_CLIENT_VALIDATION_PARAM_NAME = "__defaultClientValidation";

    private EditableValueHolder component;
    private Set<Validator> validators;
    private Converter converter;
    private Set<FacesMessage> messages;
    private String clientId;
    private boolean required;
    private boolean excludeFromAutoValidation;
    private UIForm parentForm;

    private List<String> clientValidatorsScripts;
    private List<String> javascriptLibrariesUrls = new ArrayList<String>();
    private List<String> messagesScripts;
    private StringBuilder commonScript;
    private String clientValueFunction;

    private ClientValidationMode clientValidation;

    public VerifiableComponent(EditableValueHolder component, String clientId) {
        if (!(component instanceof UIComponent))
            throw new IllegalArgumentException("Wrong type. Required: UIComponent implementing EditableValueHolder. Currently defined: " + component);
        this.component = component;
        this.clientId = clientId;
        excludeFromAutoValidation = !isAutoValidate((UIComponent) component);
        clientValidation = getClientValidationValue((UIComponent) component);

        clientValueFunction = getClientValueFunction((UIComponent) component);
    }

    private ClientValidationMode getClientValidationValue(UIComponent component) {
        ClientValidationMode result = getClientValidationAttribute(component, CLIENT_VALIDATION_PARAM_NAME);
        if (result == null) {
            result = getClientValidationAttribute(component, DEFAULT_CLIENT_VALIDATION_PARAM_NAME);
        }
        return result;
    }

    private ClientValidationMode getClientValidationAttribute(UIComponent component, String attrName) {
        Object attrValue = component.getAttributes().get(attrName);
        if (attrValue instanceof String) {
            return ClientValidationMode.fromString((String) attrValue);
        } else if (attrValue instanceof ClientValidationMode) {
            return (ClientValidationMode) attrValue;
        }
        return null;
    }

    public String getClientValueFunction() {
        return clientValueFunction;
    }

    public UIForm getParentForm() {
        return parentForm;
    }

    public void setParentForm(UIForm parentForm) {
        this.parentForm = parentForm;
    }


    public StringBuilder getCommonScript() {
        return commonScript;
    }

    public List<String> getClientValidatorsScripts() {
        return clientValidatorsScripts;
    }

    public List<String> getJavascriptLibrariesUrls() {
        return javascriptLibrariesUrls;
    }

    public boolean isExcludeFromAutoValidation() {
        return excludeFromAutoValidation;
    }

    public UIComponent getComponent() {
        return (UIComponent) component;
    }

    public void setExcludeFromAutoValidation(boolean excludeFromAutoValidation) {
        this.excludeFromAutoValidation = excludeFromAutoValidation;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }


    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }


    private boolean isAutoValidate(UIComponent component) {
        Object attributeValue = component.getAttributes().get("clientValidation");
        if (attributeValue instanceof String) {
            if (ClientValidationMode.fromString((String) attributeValue).equals(ClientValidationMode.ON_SUBMIT))
                return true;
        }

        if (attributeValue != null && attributeValue instanceof ClientValidationMode) {
            ClientValidationMode clientValidationMode = ((ClientValidationMode) attributeValue);
            return clientValidationMode.equals(ClientValidationMode.ON_SUBMIT);
        }
        return true;
    }

    private String getClientValueFunction(UIComponent component) {
        Object attributeValue = component.getAttributes().get("clientValueFunction");
        if (attributeValue instanceof String)
            return (String) attributeValue;

        return null;
    }

    public String getClientId() {
        return clientId;
    }

    public void addValidator(Validator validator) {
        if (validator == null)
            return;

        if (validators == null) validators = new HashSet<Validator>();

        validators.add(validator);
    }

    public void addValidators(Validator[] validators) {
        if (validators == null)
            return;

        for (Validator validator : validators) {
            addValidator(validator);
        }
    }

    public void addMessage(FacesMessage message) {
        if (message == null)
            return;

        if (messages == null) messages = new HashSet<FacesMessage>();
        messages.add(message);
    }


    public void addMessages(FacesMessage[] messages) {
        if (messages == null)
            return;

        for (FacesMessage message : messages) {
            addMessage(message);
        }
    }

    public void addMessageScript(String script) {
        if (script == null)
            return;
        if (messagesScripts == null) messagesScripts = new ArrayList<String>();
        messagesScripts.add(script);
    }

    public void addMessagesScripts(ValidationProcessor validationProcessor) {
        if (messages == null)
            return;

        if (validationProcessor.getClientValidationRuleForComponent(this).equals(ClientValidationMode.OFF)) return;
        for (FacesMessage facesMessage : messages) {
            addMessageScript(ClientValidatorUtil.getScriptAddMessageById(facesMessage, clientId).toString());
        }
    }

    public void addClientValidatorScript(String script) {
        if (script == null)
            return;

        if (clientValidatorsScripts == null) clientValidatorsScripts = new ArrayList<String>();
        clientValidatorsScripts.add(script);
    }

    public void addJavascriptLibraryUrl(String url) {
        if (url == null) return;
        javascriptLibrariesUrls.add(url);
    }

    public void addJavascriptLibraryUrls(FacesContext context, ValidationJavascriptLibrary[] libraries) {
        if (libraries == null) return;
        for (ValidationJavascriptLibrary library : libraries) {
            addJavascriptLibraryUrl(library.getUrl(context));
        }
    }

    public void addMessageFromContext(FacesContext context) {
        if (context == null)
            return;
        Iterator<FacesMessage> messageIterator = context.getMessages(clientId);
        while (messageIterator.hasNext()) {
            FacesMessage message = messageIterator.next();
            addMessage(message);
        }
    }

    public Set<Validator> getValidators() {
        return validators;
    }

    public static VerifiableComponent getVerifiableComponent(
            VerifiableComponent[] components,
            EditableValueHolder component,
            String clientId) {
        if (components != null)
            for (VerifiableComponent verifiableComponent : components) {
                if (verifiableComponent.getClientId().equals(clientId))
                    return null;
            }
        return new VerifiableComponent(component, clientId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final VerifiableComponent that = (VerifiableComponent) o;

        if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) return false;
        if (component != null ? !component.equals(that.component) : that.component != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (component != null ? component.hashCode() : 0);
        result = 29 * result + (clientId != null ? clientId.hashCode() : 0);
        return result;
    }

    public void updateCommonScript() {
        if (messagesScripts == null && clientValidatorsScripts == null) {
            commonScript = null;
            return;
        }

        commonScript = new StringBuilder();

        if (clientValidatorsScripts != null && clientValidatorsScripts.size() > 0) {
            commonScript.append("O$.addValidatorsById('").append(clientId).append("',[\n");

            Object[] scripts = clientValidatorsScripts.toArray();
            int scriptCount = scripts.length;
            if (scriptCount > 0) {
                for (int i = 0; i < scriptCount - 1; i++) {
                    String script = (String) scripts[i];
                    commonScript.append(script).append(",\n");
                }

                String script = (String) scripts[scriptCount - 1];
                commonScript.append(script);
            }

            commonScript.append("]");
            if (clientValueFunction != null)
                commonScript.append(",function(input){\n").append(clientValueFunction).append("}");

            commonScript.append(");\n");
        }

        if (messagesScripts != null && messagesScripts.size() > 0) {
            for (String s : messagesScripts) {
                commonScript.append(s).append("\n");
            }
        }

    }

    public List<ClientValidator> getClientValidators() {
        List<ClientValidator> clientValidators = new ArrayList<ClientValidator>();
        if (required)
            clientValidators.add(new RequiredClientValidator());

        if (converter != null) {
            ClientValidator clientValidator = ClientValidatorUtil.getClientValidator(converter, getComponent());
            if (clientValidator != null)
                clientValidators.add(clientValidator);
        }

        if (validators != null) {
            for (Validator validator : validators) {
                ClientValidator clientValidator = ClientValidatorUtil.getClientValidator(validator);
                if (clientValidator != null)
                    clientValidators.add(clientValidator);
            }
        }

        if (clientValidators.size() == 0) clientValidators = null;

        return clientValidators;
    }


    public void updateClientValidatorsScriptsAndLibraries(FacesContext context, ValidationProcessor validationProcessor) {
        List<ClientValidator> clientValidators = getClientValidators();

        if (clientValidators != null) {
            for (ClientValidator clientValidator : clientValidators) {
                addClientValidatorScript(clientValidator.getClientScript(context, getComponent()).toString());
                addJavascriptLibraryUrls(context, clientValidator.getJavascriptLibraries());
            }
        }
        addMessagesScripts(validationProcessor);
        updateCommonScript();
    }


    public Set<Validator> getNativeClientValidators() {
        if (validators == null)
            return null;

        Set<Validator> nativeClientValidators = new HashSet<Validator>();

        for (Validator validator : validators) {
            if (validator instanceof ClientValidator) {
                nativeClientValidators.add(validator);
            }
        }
        return nativeClientValidators;
    }

    public ClientValidationMode getClientValidation() {
        return clientValidation;
    }

}
