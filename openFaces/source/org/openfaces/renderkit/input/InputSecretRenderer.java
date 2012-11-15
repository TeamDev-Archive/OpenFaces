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

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Andre Shapovalov
 */
public class InputSecretRenderer extends AbstractInputTextRenderer {

    protected String getTagName() {
        return "input";
    }

    protected void writeCustomAttributes(FacesContext context, OUIInputText input) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        InputSecret inputSecret = (InputSecret) input;
        String value = getConvertedValue(context, inputSecret);

        if (!value.equals("")) {
            writeAttribute(writer, "value", value);
        }

        writeAttribute(writer, "type", "text");
        Rendering.writeAttributes(writer, inputSecret);
        writeAttribute(writer, "maxlength", inputSecret.getMaxlength(), Integer.MIN_VALUE);
        writeAttribute(writer, "size", inputSecret.getSize(), Integer.MIN_VALUE);
    }

    @Override
    protected void encodeInitScript(FacesContext context, OUIInputText input) throws IOException {

        InputSecret inputSecret = (InputSecret) input;

        ScriptBuilder scriptBuilder = new ScriptBuilder();
        scriptBuilder.initScript(context, inputSecret, "O$.InputSecret._init", inputSecret.getId());
        Rendering.renderInitScript(context, scriptBuilder,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "input/inputSecret.js")
        );
    }

}
