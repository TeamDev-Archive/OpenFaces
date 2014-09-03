/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
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
import org.openfaces.component.input.DefaultMasks;
import org.openfaces.component.input.MaskDynamicConstructor;
import org.openfaces.component.input.MaskEdit;
import org.openfaces.component.input.MaskSymbolConstructor;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class MaskEditRenderer extends AbstractInputTextRenderer {


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
        writeAttribute(writer, "id", clientId);
        writeAttribute(writer, "name", clientId);
        writeAttribute(writer, "class", styleClass);
        writeAttribute(writer, "title", inputText.getTitle());
        if (inputText.isDisabled())
            writeAttribute(writer, "disabled", "disabled");
        writeAttribute(writer, "value", getConvertedValue(facesContext, inputText));

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
        writeAttribute(writer, "type", "text");
        Rendering.writeAttributes(writer, inputText, "dir", "lang", "alt", "onselect");
        if (inputText.isReadonly())
            writeAttribute(writer, "readonly", "readonly");
    }

    @Override
    protected void encodeInitScript(FacesContext context, OUIInputText inputText) throws IOException {
        String mask;
        String blank;
        Collection<MaskDynamicConstructor> dynamicConstructor;
        Collection<MaskSymbolConstructor> symbolConstructors;
        MaskEdit maskEdit = (MaskEdit) inputText;
        if (!(maskEdit.getDefaultMask() == null)) {
            DefaultMasks defaultMask = maskEdit.getDefaultMask();
            mask = defaultMask.getMask();
            blank = defaultMask.getBlank();
            dynamicConstructor = null;
            symbolConstructors = null;
        } else {
            mask = maskEdit.getMask();
            blank = maskEdit.getBlank();
            dynamicConstructor = maskEdit.getDynamicConstructors();
            symbolConstructors = maskEdit.getSymbolConstructors();
        }
        String rolloverClass = Styles.getCSSClass(context, maskEdit, maskEdit.getRolloverStyle(), StyleGroup.regularStyleGroup(1), maskEdit.getRolloverClass(), null);
        String focusedClass = Styles.getCSSClass(context, maskEdit, maskEdit.getFocusedStyle(), StyleGroup.regularStyleGroup(2), maskEdit.getFocusedClass(), null);
        Script initScript = new ScriptBuilder().initScript(context, maskEdit, "O$.MaskEdit._init",
                mask,
                blank,
                dynamicConstructorsToString(dynamicConstructor),
                symbolConstructorsToString(symbolConstructors),
                maskEdit.getDictionary(),
                maskEdit.isBlankVisible(),
                rolloverClass,
                focusedClass
        );
        Styles.renderStyleClasses(context, maskEdit);
        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "input/maskEdit.js"),
                Resources.internalURL(context, "input/mockInput.js")

        );
    }

    private Collection<String> dynamicConstructorsToString(
            Collection<MaskDynamicConstructor> dynamicConstructors) {
        Collection<String> stringCollectionConstructors = new LinkedList<String>();
        if (dynamicConstructors == null) {
            return null;
        }
        for (MaskDynamicConstructor dynamicConstructor : dynamicConstructors) {
            stringCollectionConstructors.add(dynamicConstructor.toString());
        }
        return stringCollectionConstructors;
    }

    private Collection<String> symbolConstructorsToString(
            Collection<MaskSymbolConstructor> symbolConstructors) {
        Collection<String> stringCollectionConstructors = new LinkedList<String>();
        if (symbolConstructors == null) {
            return null;
        }
        for (MaskSymbolConstructor symbolConstructor : symbolConstructors) {
            stringCollectionConstructors.add(symbolConstructor.toString());
        }
        return stringCollectionConstructors;
    }


    public void decode(FacesContext context, UIComponent component) {
        Rendering.decodeBehaviors(context, component);

        Map requestMap = context.getExternalContext().getRequestParameterMap();
        OUIInputText inputText = (OUIInputText) component;
        String clientId = inputText.getClientId(context);

        String value = (String) requestMap.get(clientId);

        inputText.setSubmittedValue(value);
    }

}