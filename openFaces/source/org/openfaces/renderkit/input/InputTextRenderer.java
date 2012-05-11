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
import org.openfaces.component.input.InputText;
import org.openfaces.util.Rendering;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;


/**
 * @author Vladimir Kurganov
 */
public class InputTextRenderer extends AbstractInputTextRenderer {

    protected String getTagName() {
        return "input";
    }

    protected void writeCustomAttributes(FacesContext context, OUIInputText input) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        InputText inputText = (InputText) input;
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

}