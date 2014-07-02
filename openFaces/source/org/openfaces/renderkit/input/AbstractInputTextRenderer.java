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

package org.openfaces.renderkit.input;

import org.openfaces.component.OUIInputText;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractInputTextRenderer extends RendererBase {
    private static final String DEFAULT_PROMPT_CLASS = "o_inputtext_prompt";
    private static final String PROMPT_VISIBLE_SUFFIX = "::promptVisible";

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
        super.encodeBegin(facesContext, component);
        if (!component.isRendered()) return;

        OUIInputText inputText = (OUIInputText) component;

        renderInputComponent(facesContext, inputText);

        Styles.renderStyleClasses(facesContext, inputText);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Rendering.decodeBehaviors(context, component);
        
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        OUIInputText inputText = (OUIInputText) component;
        String clientId = inputText.getClientId(context);

        String value = (String) requestMap.get(clientId);
        String promptVisibleFlag = (String) requestMap.get(clientId + PROMPT_VISIBLE_SUFFIX);

        if ("false".equals(promptVisibleFlag) && value != null) {
            inputText.setSubmittedValue(value);
        } else {
            inputText.setSubmittedValue("");
        }
    }


    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return Rendering.convertFromString(context, (OUIInputText) component, (String) submittedValue);
    }

    protected String getConvertedValue(FacesContext context, UIInput inputText) {
        return Rendering.convertToString(context, inputText, inputText.getValue());
    }

    protected void renderInputComponent(FacesContext facesContext, OUIInputText inputText) throws IOException {
        String styleClass = Styles.getCSSClass(facesContext, inputText, inputText.getStyle(), StyleGroup.regularStyleGroup(), inputText.getStyleClass(), null);

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
        writeAttribute(writer, "onchange", Rendering.getChangeHandlerScript(inputText));
        writeAttribute(writer, "accesskey", inputText.getAccesskey());
        writeAttribute(writer, "tabindex", inputText.getTabindex());
        Rendering.writeStandardEvents(writer, inputText);

        writeCustomAttributes(facesContext, inputText);

        writeTagContent(facesContext, inputText);

        writer.endElement(tagName);

        encodeInitScript(facesContext, inputText);
    }

    protected void encodeInitScript(FacesContext context, OUIInputText inputText) throws IOException {
        String promptText = inputText.getPromptText();
        String promptTextClass = Styles.getCSSClass(context, inputText, inputText.getPromptTextStyle(), StyleGroup.regularStyleGroup(1), inputText.getPromptTextClass(), DEFAULT_PROMPT_CLASS);
        String rolloverClass = Styles.getCSSClass(context, inputText, inputText.getRolloverStyle(), StyleGroup.regularStyleGroup(2), inputText.getRolloverClass(), null);
        String focusedClass = Styles.getCSSClass(context, inputText, inputText.getFocusedStyle(), StyleGroup.regularStyleGroup(3), inputText.getFocusedClass(), null);

        String value = Rendering.convertToString(context, inputText, inputText.getValue());
        boolean promptVisible = value == null || value.length() == 0;
        
        Script initScript = new ScriptBuilder().initScript(context, inputText, "O$.InputText._init",
                promptText,
                promptTextClass,
                rolloverClass,
                focusedClass,
                inputText.isDisabled(),
                promptVisible);

        Styles.renderStyleClasses(context, inputText);
        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "input/inputText.js")
        );
    }

    protected abstract String getTagName();

    protected abstract void writeCustomAttributes(FacesContext context, OUIInputText input) throws IOException;

    protected void writeTagContent(FacesContext context, OUIInputText quiInputText) throws IOException {
    }
}
