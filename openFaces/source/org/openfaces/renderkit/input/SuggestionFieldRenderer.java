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

import org.openfaces.component.input.DropDownComponent;
import org.openfaces.component.input.DropDownFieldBase;
import org.openfaces.component.input.SuggestionField;
import org.openfaces.util.RenderingUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class SuggestionFieldRenderer extends DropDownFieldRenderer {

    protected void encodeRootElementStart(ResponseWriter writer, DropDownComponent dropDownComponent) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        writer.startElement("input", dropDownComponent);
        writer.writeAttribute("type", "text", null);

        String fieldId = getFieldClientId(context, dropDownComponent);
        writer.writeAttribute("name", fieldId, null);
        writer.writeAttribute("autocomplete", "off", null);

        if (dropDownComponent.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        }

        String initialStyleClass = getInitialStyleClass(context, dropDownComponent);
        if (initialStyleClass != null)
            writer.writeAttribute("class", initialStyleClass, null);

        writeFieldAttributes(writer, dropDownComponent);
    }

    protected void encodeFieldContentsStart(FacesContext context, DropDownComponent dropDownComponent) throws IOException {
        //this workaround was added for compatibility of all JSF impl and browsers
        //input component need to be closed before all child components will be rendered
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("input");

        String value = RenderingUtil.convertToString(context, dropDownComponent, dropDownComponent.getValue());

        writer.startElement("input", dropDownComponent);
        writeAttribute(writer, "type", "hidden");
        writeAttribute(writer, "id", getFieldClientId(context, dropDownComponent) + STATE_PROMPT_SUFFIX);
        writeAttribute(writer, "name", getFieldClientId(context, dropDownComponent) + STATE_PROMPT_SUFFIX);
        if (value != null && value.length() > 0)
            writeAttribute(writer, "value", "false");
        else
            writeAttribute(writer, "value", "true");
        writer.endElement("input");
    }

    protected void encodeFieldContentsEnd(ResponseWriter writer) throws IOException {
    }


    protected void encodeRootElementEnd(ResponseWriter writer) throws IOException {
        // look at encodeFieldContentsStart method
    }


    protected String getDefaultFieldClass() {
        return null;
    }

    protected String getDefaultDisabledFieldClass() {
        return null;
    }

    protected String getDefaultDisabledClass() {
        return null;
    }

    protected String getDefaultDropDownClass() {
        return null;
    }

    protected boolean isManualListOpeningAllowed(DropDownFieldBase dropDownField) {
        return ((SuggestionField) dropDownField).getManualListOpeningAllowed();
    }

    protected void writeFieldAttributes(ResponseWriter writer, DropDownComponent fieldComponent) throws IOException {
        super.writeFieldAttributes(writer, fieldComponent);
        SuggestionField suggestionField = ((SuggestionField) fieldComponent);
        writeAttribute(writer, "size", suggestionField.getSize(), Integer.MIN_VALUE);
    }

}
