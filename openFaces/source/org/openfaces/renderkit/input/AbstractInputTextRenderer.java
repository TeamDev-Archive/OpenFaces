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

package org.openfaces.renderkit.input;

import org.openfaces.component.OUIInputText;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.StyleGroup;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractInputTextRenderer extends RendererBase {
    private static final String DEFAULT_PROMPT_CLASS = "o_inputtext_prompt";
    private static final String STATE_PROMPT_SUFFIX = "::statePrompt";

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        super.encodeBegin(facesContext, uiComponent);
        if (!uiComponent.isRendered()) return;

        OUIInputText inputText = (OUIInputText) uiComponent;

        renderInputComponent(facesContext, inputText);

        StyleUtil.renderStyleClasses(facesContext, inputText);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        OUIInputText inputText = (OUIInputText) component;
        String clientId = inputText.getClientId(context);

        String value = (String) requestMap.get(clientId);
        String state = (String) requestMap.get(clientId + STATE_PROMPT_SUFFIX);

        if ((state != null && state.equals("false")) && value != null) {
            inputText.setSubmittedValue(value);
        }
    }


    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return RenderingUtil.convertFromString(context, component, (String) submittedValue);
    }

    protected String getConvertedValue(FacesContext context, UIInput inputText) {
        return RenderingUtil.convertToString(context, inputText, inputText.getValue());
    }

    protected void renderInputComponent(FacesContext facesContext, OUIInputText inputText) throws IOException {
        String styleClass = StyleUtil.getCSSClass(facesContext, inputText, inputText.getStyle(), StyleGroup.regularStyleGroup(), inputText.getStyleClass(), null);
        String promptTextStyleClass = StyleUtil.getCSSClass(facesContext, inputText, inputText.getPromptTextStyle(), StyleGroup.regularStyleGroup(1), inputText.getPromptTextClass(), DEFAULT_PROMPT_CLASS);
        String rolloverStyleClass = StyleUtil.getCSSClass(facesContext, inputText, inputText.getRolloverStyle(), StyleGroup.regularStyleGroup(2), inputText.getRolloverClass(), null);
        String focusedStyleClass = StyleUtil.getCSSClass(facesContext, inputText, inputText.getFocusedStyle(), StyleGroup.regularStyleGroup(3), inputText.getFocusedClass(), null);

        ResponseWriter writer = facesContext.getResponseWriter();

        String tagName = getTagName();
        writer.startElement(tagName, inputText);

        String clientId = inputText.getClientId(facesContext);
        writeAttribute(writer, "name", clientId);
        writeAttribute(writer, "id", clientId);
        writeAttribute(writer, "class", styleClass);
        writeAttribute(writer, "title", inputText.getTitle());
        if (inputText.isDisabled())
            writeAttribute(writer, "disabled", "disabled");
        writeAttribute(writer, "onchange", inputText.getOnchange());
        writeAttribute(writer, "accesskey", inputText.getAccesskey());
        writeAttribute(writer, "tabindex", inputText.getTabindex());
        writeStandardEvents(writer, inputText);

        writeCustomAttributes(facesContext, inputText);

        writeTagContent(facesContext, inputText);

        writer.endElement(tagName);

        encodePromptInfo(facesContext, inputText);

        String promptText = inputText.getPromptText();
        Script initScript = new ScriptBuilder().initScript(facesContext, inputText, "O$._initInputText",
                promptText,
                styleClass,
                promptTextStyleClass,
                rolloverStyleClass,
                focusedStyleClass,
                inputText.isDisabled());

        StyleUtil.renderStyleClasses(facesContext, inputText);
        RenderingUtil.renderInitScript(facesContext, initScript,
                new String[]{
                        ResourceUtil.getUtilJsURL(facesContext),
                        ResourceUtil.getInternalResourceURL(facesContext, InputTextRenderer.class, "inputText.js")
                });
    }

    protected abstract String getTagName();

    protected abstract void writeCustomAttributes(FacesContext context, OUIInputText input) throws IOException;

    protected void writeTagContent(FacesContext context, OUIInputText quiInputText) throws IOException {
    }

    private void encodePromptInfo(FacesContext context, OUIInputText inputText) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = inputText.getClientId(context);
        writer.startElement("input", inputText);
        writeAttribute(writer, "type", "hidden");
        writeAttribute(writer, "id", clientId + STATE_PROMPT_SUFFIX);
        writeAttribute(writer, "name", clientId + STATE_PROMPT_SUFFIX);
        String value = RenderingUtil.convertToString(context, inputText, inputText.getValue());
        boolean valueExists = value != null && value.length() > 0;
        writeAttribute(writer, "value", String.valueOf(!valueExists));
        writer.endElement("input");
    }

}
