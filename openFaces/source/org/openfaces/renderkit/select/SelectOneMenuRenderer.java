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
package org.openfaces.renderkit.select;

import org.openfaces.component.input.DropDownComponent;
import org.openfaces.component.input.DropDownFieldBase;
import org.openfaces.component.select.SelectOneMenu;
import org.openfaces.renderkit.input.DropDownFieldRenderer;
import org.openfaces.util.Rendering;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class SelectOneMenuRenderer extends DropDownFieldRenderer {
    private static final String VIEW_SUFFIX = "::itemPresentation";
    private static final String CONTAINER_SUFFIX = "::container";
    private static final String DEFAULT_FOCUSED_CLASS = "o_selectonemenu_focused";
    private static final String PRESENTATION_CONTENT_CLASS = "o_selectonemenu_presentation_content";
    private static final String FIELD_CONTAINER_OUTER_CLASS = "o_selectonemenu_field_container_outer";
    private static final String FIELD_CONTAINER_MIDDLE_CLASS = "o_selectonemenu_field_container_middle";
    private static final String FIELD_CONTAINER_INNER_CLASS = "o_selectonemenu_field_container_inner";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

    }

    protected String getClientItemLabel(UISelectItem item, String convertedItemValue) {
        String itemLabel = item.getItemLabel();
        return itemLabel != null ? itemLabel : convertedItemValue;
    }

    protected String getDefaultDropDownClass() {
        return "o_dropdown o_selectonemenu";
    }

    @Override
    protected int getItemPresentationColumn(DropDownComponent dropDown) {
        SelectOneMenu fieldComponent = (SelectOneMenu) dropDown;
        return fieldComponent.getItemPresentationColumn();
    }

    @Override
    protected String getFocusedClass(FacesContext context, DropDownComponent dropDown) {
        return DEFAULT_FOCUSED_CLASS + " " + super.getFocusedClass(context, dropDown);
    }

    protected String getItemPresentationId(FacesContext context, SelectOneMenu fieldComponent) {
        return fieldComponent.getClientId(context) + VIEW_SUFFIX;
    }

    @Override
    protected String getHiddenFieldValue(FacesContext context, DropDownComponent dropDown){
        String currentValueConverted = Rendering.getStringValue(context, dropDown);
        return isItemValueExists(context, (DropDownFieldBase) dropDown, currentValueConverted)
                ? "[" + currentValueConverted + "]"
                : "";
    }

    protected void encodeField(FacesContext context, UIComponent component) throws IOException {
        SelectOneMenu fieldComponent = (SelectOneMenu) component;
        ResponseWriter writer = context.getResponseWriter();

        String fieldId = getFieldClientId(context, fieldComponent);
        String itemPresentationId = getItemPresentationId(context, fieldComponent);
        writer.writeAttribute("style", "width: 100%; height: 100%", null);

        // Item presentation
        writer.startElement("div", fieldComponent);
        writer.writeAttribute("class", PRESENTATION_CONTENT_CLASS, null);
        writer.writeAttribute("id", itemPresentationId + CONTAINER_SUFFIX, null);
        writer.startElement("table", fieldComponent);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        // 98% - for compatibility with IE quirk mode
        writer.writeAttribute("style", "width: 98%;height: 98%;", null);
        writer.startElement("colgroup", fieldComponent);
        writer.endElement("colgroup");
        writer.startElement("tbody", fieldComponent);
        writer.startElement("tr", fieldComponent);
        writer.writeAttribute("id", itemPresentationId, null);
        writer.startElement("td", fieldComponent);
        Rendering.writeNonBreakableSpace(writer);
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("tbody");
        writer.endElement("table");
        writer.endElement("div");

        // Filter field
        writer.startElement("div", fieldComponent);
        writer.writeAttribute("id", fieldId + CONTAINER_SUFFIX, null);
        writer.writeAttribute("class", FIELD_CONTAINER_OUTER_CLASS, null);
        writer.startElement("div", fieldComponent);
        writer.writeAttribute("class", FIELD_CONTAINER_MIDDLE_CLASS, null);
        writer.startElement("div", fieldComponent);
        writer.writeAttribute("class", FIELD_CONTAINER_INNER_CLASS, null);
        writer.startElement("input", fieldComponent);
        writer.writeAttribute("id", fieldId, null);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("style", "height: 100%;", null);
        if (fieldComponent.isDisabled())
            writer.writeAttribute("disabled", "disabled", null);
        if (fieldComponent.isReadonly())
            writer.writeAttribute("readonly", "readonly", null);
        writer.writeAttribute("name", fieldId, null);
        writeFieldAttributes(writer, fieldComponent);
        writer.endElement("input");
        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");

        // StatePrompt hidden variable
        encodeStatePrompt(context, component);
    }
}
