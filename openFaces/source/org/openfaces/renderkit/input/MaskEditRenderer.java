/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
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
import org.openfaces.component.input.InputText;
import org.openfaces.component.input.MaskEdit;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.LinkedList;

public class MaskEditRenderer extends AbstractInputTextRenderer {
    public static final String SUBSTITUTIONAL_TAG_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "maskEditValue";

    @Override
    protected String getTagName() {
        return "input";
    }

    @Override
    protected void renderInputComponent(FacesContext facesContext, OUIInputText inputText) throws IOException {
        String styleClass = Styles.getCSSClass(facesContext, inputText, inputText.getStyle(), StyleGroup.regularStyleGroup(), inputText.getStyleClass(), null);

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = inputText.getClientId(facesContext);
        String tagName = getTagName();

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

    @Override
    protected void writeCustomAttributes(FacesContext context, OUIInputText input) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        MaskEdit inputText = (MaskEdit) input;
        String value = getConvertedValue(context, inputText);
        if (!"".equals(value))
            writeAttribute(writer, "value", value);
        writeAttribute(writer, "type", "text");
        Rendering.writeAttributes(writer, inputText, "dir", "lang", "alt", "onselect");
        writeAttribute(writer, "maxlength", inputText.getMaxlength(), Integer.MIN_VALUE);
        writeAttribute(writer, "size", inputText.getSize(), Integer.MIN_VALUE);
        if (inputText.isReadonly())
            writeAttribute(writer, "readonly", "readonly");
    }

    @Override
    protected void encodeInitScript(FacesContext context, OUIInputText inputText) throws IOException {
        MaskEdit maskEdit = (MaskEdit) inputText;
        String rolloverClass = Styles.getCSSClass(context, maskEdit, maskEdit.getRolloverStyle(), StyleGroup.regularStyleGroup(1), maskEdit.getRolloverClass(), null);
        String focusedClass = Styles.getCSSClass(context, maskEdit, maskEdit.getFocusedStyle(), StyleGroup.regularStyleGroup(2), maskEdit.getFocusedClass(), null);

        String value = Rendering.convertToString(context, maskEdit, maskEdit.getValue());
        Script initScript = new ScriptBuilder().initScript(context, maskEdit, "O$.MaskEdit._init",
                maskEdit.getMask(),
                maskEdit.getBlank(),
                maskSymbolStringToArray(maskEdit.getMaskSymbolArray()),
                maskEdit.getDictionary(),
                rolloverClass,
                focusedClass
        );

        Styles.renderStyleClasses(context, maskEdit);
        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "input/maskEdit.js")
        );
    }

    private LinkedList<Character> maskSymbolStringToArray(String maskSymbolString) {
        if (maskSymbolString == null) return null;
        LinkedList<Character> maskSymbolArray = new LinkedList<Character>();
        for (int i = 0; i < maskSymbolString.length(); i++) {
            maskSymbolArray.add(maskSymbolString.charAt(i));
        }
        return maskSymbolArray;
    }

}
