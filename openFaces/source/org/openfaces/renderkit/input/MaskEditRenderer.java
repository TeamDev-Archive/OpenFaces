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

public class MaskEditRenderer extends AbstractInputTextRenderer {
    private static final String DEFAULT_PROMPT_CLASS = "o_maskedit_prompt";

    @Override
    protected String getTagName() {
        return "input";
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
        String rolloverClass = Styles.getCSSClass(context, inputText, inputText.getRolloverStyle(), StyleGroup.regularStyleGroup(1), inputText.getRolloverClass(), null);
        String focusedClass = Styles.getCSSClass(context, inputText, inputText.getFocusedStyle(), StyleGroup.regularStyleGroup(2), inputText.getFocusedClass(), null);

        String value = Rendering.convertToString(context, inputText, inputText.getValue());
        Script initScript = new ScriptBuilder().initScript(context, inputText, "O$.MaskEdit._init",
                maskEdit.getMask(),
                maskEdit.getBlank(),
                maskEdit.getMaskSymbolArray(),
                rolloverClass,
                focusedClass,
                maskEdit.getDictionary()
        );

        Styles.renderStyleClasses(context, inputText);
        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "input/maskEdit.js")
        );
    }

}
