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
import org.openfaces.component.input.InputTextarea;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Alexander Golubev
 */
public class InputTextareaRenderer extends AbstractInputTextRenderer {

    protected String getTagName() {
        return "textarea";
    }

    protected void writeCustomAttributes(FacesContext context, OUIInputText input) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        InputTextarea inputTextarea = (InputTextarea) input;
        writeAttribute(writer, "cols", inputTextarea.getCols(), Integer.MIN_VALUE);
        writeAttribute(writer, "rows", inputTextarea.getRows(), Integer.MIN_VALUE);
        if (inputTextarea.isReadonly())
            writeAttribute(writer, "readonly", "readonly");
    }

    protected void writeTagContent(FacesContext context, OUIInputText quiInputText) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String value = getConvertedValue(context, quiInputText);
        writer.writeText(value, null);
    }

}
