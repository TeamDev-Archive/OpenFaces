/*
 * OpenFaces - JSF Component Library 2.0
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
import org.openfaces.component.input.InputSecret;
import org.openfaces.component.input.InputText;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Andre Shapovalov
 */
public class InputSecretRenderer extends AbstractInputTextRenderer {
    private static final String DEFAULT_PROMPT_CLASS = "o_inputtext_prompt";
    public static final String SUBSTITUTIONAL_TAG_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "inputSecretValue";

    protected String getTagName() {
        return "input";
    }

    @Override
    protected void renderInputComponent(FacesContext facesContext, OUIInputText inputText) throws IOException {
        String styleClass = Styles.getCSSClass(facesContext, inputText, inputText.getStyle(), StyleGroup.regularStyleGroup(), inputText.getStyleClass(), null);

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = inputText.getClientId(facesContext);
        String tagName = getTagName();

        //rendering of hidden element
        writer.startElement(tagName,inputText);
        writeAttribute(writer,"type","password");
        writeAttribute(writer,"style","display:none");
        writeAttribute(writer,"id",clientId);
        writeAttribute(writer,"name",clientId);
        writer.endElement(tagName);

        //rendering of  visible element
        writer.startElement(tagName, inputText);
        writeAttribute(writer, "id", clientId + SUBSTITUTIONAL_TAG_SUFFIX);
        writeAttribute(writer, "class", styleClass);
        writeAttribute(writer, "title", inputText.getTitle());
        if (inputText.isDisabled())
            writeAttribute(writer, "disabled", "disabled");
        writeAttribute(writer, "onchange", inputText.getOnchange());
        writeAttribute(writer, "accesskey", inputText.getAccesskey());
        writeAttribute(writer, "tabindex", inputText.getTabindex());
        writeAttribute(writer, "autocomplete", "off");
        Rendering.writeStandardEvents(writer, inputText);

        writeCustomAttributes(facesContext, inputText);

        writeTagContent(facesContext, inputText);

        writer.endElement(tagName);



        encodeInitScript(facesContext, inputText);
    }

    protected void writeCustomAttributes(FacesContext context, OUIInputText input) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        InputSecret inputSecret = (InputSecret) input;
        writeAttribute(writer, "type", "text");
        Rendering.writeAttributes(writer, inputSecret);
        writeAttribute(writer, "maxlength", inputSecret.getMaxlength(), Integer.MIN_VALUE);
        writeAttribute(writer, "size", inputSecret.getSize(), Integer.MIN_VALUE);
    }

    @Override
    protected void encodeInitScript(FacesContext context, OUIInputText input) throws IOException {

        InputSecret inputSecret = (InputSecret) input;

        String promptText = inputSecret.getPromptText();
        String promptTextClass = Styles.getCSSClass(context, inputSecret, inputSecret.getPromptTextStyle(), StyleGroup.regularStyleGroup(1), inputSecret.getPromptTextClass(), DEFAULT_PROMPT_CLASS);
        String rolloverClass = Styles.getCSSClass(context, inputSecret, inputSecret.getRolloverStyle(), StyleGroup.regularStyleGroup(2), inputSecret.getRolloverClass(), null);
        String focusedClass = Styles.getCSSClass(context, inputSecret, inputSecret.getFocusedStyle(), StyleGroup.regularStyleGroup(3), inputSecret.getFocusedClass(), null);

        String value = Rendering.convertToString(context, inputSecret, inputSecret.getValue());
        boolean promptVisible = value == null || value.length() == 0;

        ScriptBuilder scriptBuilder = new ScriptBuilder();
        scriptBuilder.initScript(context, inputSecret, "O$.InputSecret._init",
                inputSecret.getInterval(),
                inputSecret.getDuration(),
                inputSecret.getReplacement(),
                promptVisible,
                promptText,
                promptTextClass,
                rolloverClass,
                focusedClass);
        Rendering.renderInitScript(context, scriptBuilder,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "input/inputSecret.js")
        );
    }

}
