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
package org.openfaces.component.validation;

import org.openfaces.renderkit.validation.BaseMessageRenderer;
import org.openfaces.renderkit.validation.ValidatorUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Converters;
import org.openfaces.util.Log;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.validator.ClientValidatorUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Pavel Kaplin
 */
public class ValidationSupportResponseWriter extends ResponseWriterWrapper {
    private static final String[] ELEMENTS_ALLOWED_RENDER_AFTER_THEIR_START = new String[]{
            "td", "tr", "table", "colgroup", "select", "textarea", "option", "script", "optgroup", "map", "fieldset", "frameset"
    };
    private static final String[] ELEMENTS_ALLOW_RENDERER_AFTER_THEIR_END = new String[]{
            "tr", "td", "th", "col", "colgroup", "caption", "thead", "tfoot", "tbody", "legend",
            "optgroup", "area", "frame"
    };
    private static final String[] ELEMENTS_ALLOWED_FOR_ID = new String[]{
            "input", "textarea", "select"
    };

    private ResponseWriter wrapped;
    private StringWriter validationScriptWriter;
    private List<String> validationScripts;
    private Set<String> formsHaveOnSubmitRendered;
    private boolean clientIdRendered;
    private String clientId;
    private boolean processingValidation;
    private String element;
    private ValidationProcessor validationProcessor;

    public ValidationSupportResponseWriter(ResponseWriter writer) {
        this.wrapped = writer;
        formsHaveOnSubmitRendered = new HashSet<String>();
    }

    @Override
    public ResponseWriter getWrapped() {
        return wrapped;
    }

    public ValidationSupportResponseWriter(ResponseWriter writer, ValidationSupportResponseWriter parent) {
        this.wrapped = writer;
        validationScriptWriter = parent.validationScriptWriter;
        validationScripts = parent.validationScripts;
        formsHaveOnSubmitRendered = parent.formsHaveOnSubmitRendered;
        processingValidation = parent.processingValidation;
        element = parent.element;
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        flushValidationScriptInElement();
        wrapped.write(cbuf, off, len);
    }

    public void flush() throws IOException {
        flushValidationScriptInElement();
        wrapped.flush();
    }

    public void close() throws IOException {
        flushValidationScriptInElement();
        wrapped.close();
    }

    public void endDocument() throws IOException {
        flushValidationScriptInElement();
        wrapped.endDocument();
    }

    public void startElement(String name, UIComponent component) throws IOException {
        /* IMPORTANT: THIS METHOD AND ALL ITS CALLEES IS A PERFORMANCE BOTTLENECK.
           MODIFY WITH CARE. ENSURE MINIMAL EXECUTION TIME AND AMOUNT OF OUTPUT */
        flushValidationScriptInElement();
        FacesContext context = FacesContext.getCurrentInstance();
        if (!processingValidation && isValidationNeeded(context, component)) {
            ValidationProcessor processor = getValidationProcessor(context);
            if (processor != null) {
                boolean needProcessGlobalMessages = !processor.isGlobalMessagesProcessed();
                boolean needProcessEditableValueHolder = component instanceof EditableValueHolder && component.isRendered();
                if (needProcessGlobalMessages || needProcessEditableValueHolder) {
                    processingValidation = true;

                    if (validationScriptWriter == null)
                        validationScriptWriter = new StringWriter();

                    ResponseWriter responseWriter = context.getResponseWriter();
                    if (responseWriter == null) {
                        PartialViewContext partialViewContext = context.getPartialViewContext();
                        if (partialViewContext.isPartialRequest())
                            responseWriter = partialViewContext.getPartialResponseWriter();
                    }
                    ResponseWriter clonedResponseWriter = cloneWithWriter(validationScriptWriter);
                    context.setResponseWriter(clonedResponseWriter);
                    List<String> prevRenderedJsLinks = substituteRenderedJsLinks(context);

                    if (needProcessGlobalMessages)
                        processGlobalMessages(context, processor);

                    if (needProcessEditableValueHolder)
                        processEditableValueHolder(context, component, processor);

                    context.setResponseWriter(responseWriter);
                    restoreRenderedJsLinks(context, prevRenderedJsLinks);

                    processingValidation = false;
                }
            }
        }
        wrapped.startElement(name, component);
        element = name;
    }

    public void endElement(String name) throws IOException {
        renderClientIdIfNecessary(true);
        wrapped.endElement(name);
        element = null;
        flushValidationScriptAfterEnd(name);
    }

    public void writeAttribute(String name, Object value, String property) throws IOException {
        wrapped.writeAttribute(name, value, property);
        if ("id".equals(name) && clientId != null && clientId.equals(value)) {
            clientId = null;
            clientIdRendered = true;
        }
    }

    public void writeURIAttribute(String name, Object value, String property) throws IOException {
        wrapped.writeURIAttribute(name, value, property);
        if ("id".equals(name) && clientId != null && clientId.equals(value)) {
            clientId = null;
            clientIdRendered = true;
        }
    }

    public void writeComment(Object comment) throws IOException {
        flushValidationScriptInElement();
        wrapped.writeComment(comment);
    }

    public void writeText(Object text, String property) throws IOException {
        flushValidationScriptInElement();
        wrapped.writeText(text, property);
    }

    public void writeText(char[] text, int off, int len) throws IOException {
        flushValidationScriptInElement();
        wrapped.writeText(text, off, len);
    }

    public ResponseWriter cloneWithWriter(Writer writer) {
        return new ValidationSupportResponseWriter(this.wrapped.cloneWithWriter(writer), this);
    }

    private void flushValidationScriptInElement() throws IOException {
        renderClientIdIfNecessary(false);
        if (isAllowedToRenderAfterElementStart(element)) {
            flushValidationScriptIfNecessary();
        }
    }

    private void flushValidationScriptAfterEnd(String element) throws IOException {
        if (isAllowedToRenderAfterElementEnd(element)) {
            flushValidationScriptIfNecessary();
        }
    }

    private void flushValidationScriptIfNecessary() throws IOException {
        if (isValidationScriptNotEmpty() && clientIdRendered) {
            String validationScript = validationScriptWriter.toString();
            putNewJSLinksInRenderedJsLinks();
            wrapped.write(validationScript);
            validationScriptWriter = null;

            validationScripts = null;
        }
    }

    private void renderClientIdIfNecessary(boolean endingElement) throws IOException {
        if (element != null && clientId != null) {
            if (!clientIdRendered &&
                isValidationScriptNotEmpty() &&
                !processingValidation &&
                isElementAllowedForId(element)) {
                writeAttribute("id", clientId, null);
                clientId = null;
                clientIdRendered = true;
            } else if (endingElement) {
                clientId = null;
            }
        }
    }

    private boolean isValidationScriptNotEmpty() {
        return validationScriptWriter != null && validationScriptWriter.getBuffer().length() > 0;
    }

    private ValidationProcessor getValidationProcessor(FacesContext context) {
        if (validationProcessor == null)
            validationProcessor = ValidationProcessor.getInstance(context);
        return validationProcessor;
    }

    private boolean isValidationNeeded(FacesContext context, UIComponent component) {
        ValidationProcessor processor = getValidationProcessor(context);
        return processor != null && !processor.isGlobalMessagesProcessed() || (component instanceof EditableValueHolder);
    }

    private void processEditableValueHolder(FacesContext context, UIComponent component, ValidationProcessor processor) throws IOException {
        EditableValueHolder editableValueHolder = (EditableValueHolder) component;
        VerifiableComponent[] verifiableComponents = processor.getVerifiableComponents(context);
        VerifiableComponent vc = VerifiableComponent.getVerifiableComponent(
                verifiableComponents, editableValueHolder, component.getClientId(context));
        UIForm parentForm = Components.getEnclosingForm(component);
        if (parentForm == null) {
            String componentClass = component.getClass().getName();
            if (componentClass.equals("org.richfaces.component.html.HtmlModalPanel") ||
                componentClass.equals("org.richfaces.component.html.HtmlTabPanel")) {
                // RichFaces TabPanel and ModalPanel implement EditableValueHolder though it can normally be out of form
                return;
            }
            Log.log(context, "Warn: enclosing form cannot be found for component " + component.getClientId(context) + ". Client-side validation will not be available for it.");
            return; // org.richfaces.component.html.HtmlModalPanel
        }
        if (vc != null) {
            clientId = component.getClientId(context);
            clientIdRendered = false;
            vc.setParentForm(parentForm);
            vc.addValidators(editableValueHolder.getValidators());
            vc.setRequired(editableValueHolder.isRequired());
            vc.setConverter(Converters.getConverter(component, context));
            vc.addMessageFromContext(context);
            vc.updateClientValidatorsScriptsAndLibraries(context, processor);
            processor.addVerifiableComponent(vc);
            handleEditableValueHolder(context, vc, processor);
        }
    }

    private void processGlobalMessages(FacesContext context, ValidationProcessor processor) throws IOException {
        Iterator<FacesMessage> globalMessages = context.getMessages(null);
        if (globalMessages.hasNext()) {
            Resources.renderJSLinkIfNeeded(context, Resources.utilJsURL(context));
            Resources.renderJSLinkIfNeeded(context, ValidatorUtil.getValidatorUtilJsUrl(context));
            while (globalMessages.hasNext()) {
                FacesMessage message = globalMessages.next();
                Rendering.renderInitScript(context, ClientValidatorUtil.getScriptAddGlobalMessage(message));
            }
        }
        processor.confirmGlobalMessagesProcessing();
    }

    private void handleEditableValueHolder(FacesContext context, VerifiableComponent vc, ValidationProcessor vp) throws IOException {
        ClientValidationMode clientValidationRuleForComponent = vp.getClientValidationRuleForComponent(vc);
        // if client validation switched at least for one validatable component - render client script for component validation
        StringBuilder commonScript = vc.getCommonScript();
        if (commonScript == null || commonScript.length() == 0) return;

        // if component does not have defined presentation in page and default client validation presentation switched on
        // then render default validation presentation for component (currently floating icon message)
        UIForm parentForm = vc.getParentForm();
        if (vp.isUseDefaultClientValidationPresentationForForm(parentForm) || vp.isUseDefaultServerValidationPresentationForForm(parentForm)) {
            int bubbleIndex = nextBubbleIndex(context);
            renderPresentationComponent(vc.getComponent(), vc.getParentForm(), bubbleIndex, vp);
        }
        if (!vp.getClientValidationRuleForComponent(vc).equals(ClientValidationMode.OFF)) {
            List<String> javascriptLibraries = vc.getJavascriptLibrariesUrls();
            String[] javascriptLibrariesArray = javascriptLibraries.toArray(new String[javascriptLibraries.size()]);
            Rendering.renderInitScript(context, new RawScript(commonScript.toString()), javascriptLibrariesArray);
        }

        if (clientValidationRuleForComponent.equals(ClientValidationMode.ON_SUBMIT)) {
            String formClientId = parentForm.getClientId(context);
            if (!formsHaveOnSubmitRendered.contains(formClientId)) {
                Rendering.renderInitScript(context,
                        new RawScript("O$.addOnSubmitEvent(O$._autoValidateForm,'" + formClientId + "');\n"),
                        ValidatorUtil.getValidatorUtilJsUrl(context));
                formsHaveOnSubmitRendered.add(formClientId);
            }
        } else if (clientValidationRuleForComponent.equals(ClientValidationMode.ON_DEMAND)) {
            Rendering.renderInitScript(context,
                    new RawScript("O$.addNotValidatedInput('" + vc.getClientId() + "');"),
                    ValidatorUtil.getValidatorUtilJsUrl(context));
        }
    }

    public static void resetBubbleIndex(FacesContext context) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.remove(getBubbleIndexKey());
    }

    public static int nextBubbleIndex(FacesContext context) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        String key = getBubbleIndexKey();
        Integer bubbleIndex = (Integer) sessionMap.get(key);
        if (bubbleIndex == null) bubbleIndex = 0;
        sessionMap.put(key, bubbleIndex + 1);
        return bubbleIndex;
    }

    private static String getBubbleIndexKey() {
        return ValidationSupportResponseWriter.class.getName() + ".bubbleIndex";
    }

    private static void renderPresentationComponent(UIComponent component, UIForm parentForm, int idx, ValidationProcessor vp) throws IOException {
        ClientValidationSupport clientValidationSupport = vp.getClientValidationSupport(parentForm);
        FacesContext context = FacesContext.getCurrentInstance();
        UIMessage defaultMessage = vp.getDefaultPresentationInstance(context, clientValidationSupport);

        if (!(defaultMessage instanceof FloatingIconMessage))
            throw new IllegalStateException("Illegal default presentation component type. Expected FloatingIconMessage, actual " + defaultMessage.getClass());

        renderFloatingIconMessage(context, "dfm" + idx, component, (FloatingIconMessage) defaultMessage);
    }

    private static FloatingIconMessage renderFloatingIconMessage(FacesContext context,
                                                          String id,
                                                          UIComponent component,
                                                          FloatingIconMessage template) throws IOException {
        FloatingIconMessage message = new FloatingIconMessage(template, true);
        message.setId(id);
        message.setFor(":" + component.getClientId(context));
        message.getAttributes().put(BaseMessageRenderer.DEFAULT_PRESENTATION, Boolean.TRUE);
        message.encodeBegin(context);
        message.encodeChildren(context);
        message.encodeEnd(context);
        return message;
    }

    private boolean isAllowedToRenderAfterElementStart(String element) {
        return element != null && !isStringInArray(element, ELEMENTS_ALLOWED_RENDER_AFTER_THEIR_START);

    }

    private boolean isAllowedToRenderAfterElementEnd(String element) {
        return !isStringInArray(element, ELEMENTS_ALLOW_RENDERER_AFTER_THEIR_END);
    }

    private static boolean isElementAllowedForId(String element) {
        return isStringInArray(element, ELEMENTS_ALLOWED_FOR_ID);
    }

    private static boolean isStringInArray(String str, String[] array) {
        str = str.toLowerCase();
        for (String s : array) {
            if (s.equals(str))
                return true;
        }
        return false;
    }

    private List<String> substituteRenderedJsLinks(FacesContext context) {
        if (validationScripts == null) {
            validationScripts = new ArrayList<String>();
        }
        Map<Object, Object> contextAttributes = context.getAttributes();
        List<String> renderedJsLinks = (List<String>) contextAttributes.get(Resources.renderedJsLinksKey(context));
        if (renderedJsLinks != null) {
            for (String js : renderedJsLinks) {
                if (!validationScripts.contains(js))
                    validationScripts.add(js);
            }
        }
        contextAttributes.put(Resources.renderedJsLinksKey(context), validationScripts);
        contextAttributes.put(Resources.POSTPONE_JS_LINK_RENDERING, Boolean.TRUE);
        return renderedJsLinks;
    }

    private void restoreRenderedJsLinks(FacesContext context, List<String> prevRenderedJsLinks) {
        Map<Object, Object> contextAttributes = context.getAttributes();
        contextAttributes.put(Resources.renderedJsLinksKey(context), prevRenderedJsLinks);
        contextAttributes.put(Resources.POSTPONE_JS_LINK_RENDERING, Boolean.FALSE);
    }

    private void putNewJSLinksInRenderedJsLinks() throws IOException {
        if (validationScripts == null)
            return;
        FacesContext context = FacesContext.getCurrentInstance();
        ResponseWriter prevWriter = context.getResponseWriter();
        try {
            // use original writer when writing js links to avoid recursive startElement...putNewJSLinksInRenderedJsLinks
            // call on this ValidationSupportResponseWriter
            context.setResponseWriter(wrapped);

            List<String> renderedJsLinks = Resources.getRenderedJsLinks(context);
            for (String js : validationScripts) {
                if (!renderedJsLinks.contains(js))
                    Resources.renderJSLinkIfNeeded(context, js);
            }
        } finally {
            context.setResponseWriter(prevWriter);
        }
    }
}
