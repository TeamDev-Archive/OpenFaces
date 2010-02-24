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

package org.openfaces.renderkit.validation;

import org.hibernate.validator.InvalidValue;
import org.openfaces.component.validation.ClientValidationMode;
import org.openfaces.component.validation.UIValidation;
import org.openfaces.component.validation.ValidationProcessor;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.RawScript;
import org.openfaces.validation.CoreValidator;
import org.openfaces.validation.core.CoreValidatorImpl;
import org.openfaces.validation.validators.AnnotationValidator;
import org.openfaces.validator.ClientValidator;
import org.openfaces.validator.ClientValidatorUtil;
import org.openfaces.validator.ValidationJavascriptLibrary;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p><strong>AnnotationValidationRenderer</strong></p> is a JSF renderer of {@link UIValidation} component.
 * During rendering of {@link UIValidation} component, an instance of {@link AnnotationValidator} is added to all of its' child components.
 *
 * @author Eugene Goncharov
 */
public class AnnotationValidationRenderer extends RendererBase {

    private static CoreValidator ourValidator = new CoreValidatorImpl();

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        super.encodeBegin(facesContext, uiComponent);
    }

    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if (component.isRendered()) {
            processChildren(context, component);
        }
    }


    protected void processChildren(FacesContext context, UIComponent component) throws IOException {
        UIValidation validateAll = (UIValidation) component;
        if (!validateAll.isValidatorsAdded()) {
            addValidators(validateAll.getChildren(), context);
            validateAll.setValidatorsAdded(true);
        }

        Rendering.renderChildren(context, component);
    }

    private boolean isValueRequired(FacesContext context, Object child) {
        ELContext elContext = context.getELContext();
        ValueExpression valueExpression = ((UIComponent) child)
                .getValueExpression("value");

        if (valueExpression != null) {
            Object value = valueExpression.getValue(elContext);

            InvalidValue[] invalidValues = null;
            try {
                invalidValues = ourValidator.validate(
                        valueExpression, elContext, null);
            } catch (ELException ele) {
                // Attempt to set null value failed,this means that value is
                // required for this component
                return true;
            } finally {
                // restore the value
                valueExpression.setValue(elContext, value);
            }

            if (invalidValues != null && invalidValues.length > 0) {
                // Attempt to set null value failed,this means that value is
                // required for this component
                return true;
            }
        }

        return false;
    }


    private void addValidators(List<UIComponent> children, FacesContext context) {
        for (Object child : children) {
            if (child instanceof EditableValueHolder) {
                EditableValueHolder evh = (EditableValueHolder) child;
                if (evh.getValidators().length == 0) {

                    evh.addValidator(new AnnotationValidator());
                    if (isValueRequired(context, child)) {
                        evh.setRequired(true);
                        if (child instanceof UIInput) {
                            ((UIInput) child)
                                    .setRequiredMessage("Value is required for component with id "
                                            + ((UIInput) child)
                                            .getId() + "");
                        }
                    }

                }
            } else if (child instanceof UIComponent) {
                addValidators(((UIComponent) child).getChildren(), context);
            }
        }
    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        if (component.isRendered()) {
            UIValidation validateAll = (UIValidation) component;

            renderClientValidatorsIfNeeded(component, validateAll.getChildren(), context);

        }
    }

    private void renderClientValidatorsIfNeeded(UIComponent component, List<UIComponent> children, FacesContext context) throws IOException {
        UIForm parentForm = Components.getEnclosingForm(component);
        ValidationProcessor processor = ValidationProcessor.getInstance(context);
        ClientValidationMode validationMode = processor.getClientValidationRuleForForm(parentForm);
        if (!validationMode.equals(ClientValidationMode.OFF)) {
            renderClientValidators(parentForm, validationMode, children, context);
        }
    }

    private void renderClientValidators(UIForm parentForm, ClientValidationMode validationMode, List<UIComponent> children, FacesContext context) throws IOException {
        for (UIComponent child : children) {
            if (child instanceof EditableValueHolder) {
                ELContext elContext = context.getELContext();
                List<ClientValidator> clientValidators = ourValidator.getClientValidatorsForComponent(child, elContext);
                // Render client validator scripts here
                if (clientValidators != null) {
                    List<String> clientValidatorsScripts = new ArrayList<String>();
                    List<String> javascriptLibraries = new ArrayList<String>();
                    List<String> messagesScripts = new ArrayList<String>();

                    for (ClientValidator validator : clientValidators) {
                        String clientId = child.getClientId(context);
                        clientValidatorsScripts.add(validator.getClientScript(context, child).toString());

                        ValidationJavascriptLibrary[] jsLibraries = validator.getJavascriptLibraries();
                        for (ValidationJavascriptLibrary jsLibrary : jsLibraries) {
                            javascriptLibraries.add(jsLibrary.getUrl(context));
                        }

                        Iterator<FacesMessage> messageIterator = context.getMessages(clientId);
                        while (messageIterator.hasNext()) {
                            String messageScript = ClientValidatorUtil.getScriptAddMessageById(messageIterator.next(), clientId).toString();
                            messagesScripts.add(messageScript);
                        }
                    }


                    StringBuilder commonScript = new StringBuilder();

                    if (clientValidatorsScripts.size() > 0) {
                        commonScript.append("O$.addValidatorsById('").append(child.getClientId(context)).append("',[\n");

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

                        commonScript.append(");\n");
                    }

                    if (messagesScripts.size() > 0) {
                        for (String s : messagesScripts) {
                            commonScript.append(s).append("\n");
                        }
                    }
                    String[] javascriptLibrariesArray = javascriptLibraries.toArray(new String[javascriptLibraries.size()]);
                    Rendering.renderInitScript(context, new RawScript(commonScript.toString()), javascriptLibrariesArray);

                    if (validationMode.equals(ClientValidationMode.ON_SUBMIT)) {
                        String formClientId = parentForm.getClientId(context);
                        Rendering.renderInitScript(context,
                                new RawScript("O$.addOnSubmitEvent(O$._autoValidateForm,'" + formClientId + "');\n"),
                                ValidatorUtil.getValidatorUtilJsUrl(context));

                    } else if (validationMode.equals(ClientValidationMode.ON_DEMAND)) {
                        Rendering.renderInitScript(context,
                                new RawScript("O$.addNotValidatedInput('" + child.getClientId(context) + "');"),
                                ValidatorUtil.getValidatorUtilJsUrl(context));
                    }


                }
            } else if (child instanceof UIComponent) {
                renderClientValidators(parentForm, validationMode, child.getChildren(), context);
            }
        }

    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

}
