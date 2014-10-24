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
package org.openfaces.renderkit.command;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.command.CommandButton;
import org.openfaces.renderkit.OUICommandRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Environment;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class CommandButtonRenderer extends OUICommandRenderer {

    private static final String ATTR_AJAX_REQUIRED = "_ajaxRequired";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CommandButton btn = (CommandButton) component;
        String tagName = getTagName(btn);
        writer.startElement(tagName, btn);
        Rendering.writeIdAttribute(context, component);
        Rendering.writeNameAttribute(context, component);
        String type = btn.getType();
        if (!("submit".equals(type) || "reset".equals(type) || "button".equals(type)))
            type = "submit";
        if ("input".equals(tagName)) {
            String image = btn.getImage();
            if (image != null)
                type = "image";
            writer.writeAttribute("src", Resources.applicationURL(context, image), "image");
        }
        if ("submit".equals(type) && Environment.isExplorer6()) {

        }
        writer.writeAttribute("type", type, "type");
        if (btn.isDisabled())
            writer.writeAttribute("disabled", "disabled", "");

        Rendering.writeAttributes(writer, btn,
                "accesskey",
                "tabindex",
                "lang",
                "title",
                "alt",
                "dir");
        Object value = btn.getValue();
        if (value == null) value = "";
        writer.writeAttribute("value", value, "value");

        Rendering.writeStyleAndClassAttributes(writer, btn);

        if (!btn.isDisabled()) {
            boolean ajaxJsRequired = writeEventsWithAjaxSupport(context, writer, btn);
            if (ajaxJsRequired)
                btn.getAttributes().put(ATTR_AJAX_REQUIRED, Boolean.TRUE);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CommandButton btn = (CommandButton) component;
        writer.endElement(getTagName(btn));
        if (btn.getAttributes().remove(ATTR_AJAX_REQUIRED) != null)
            AjaxUtil.renderJSLinks(context);

    }

    private String getTagName(CommandButton button) {
        return isButtonTagMode(button) ? "button" : "input";
    }

    private boolean isButtonTagMode(CommandButton button) {
        List<UIComponent> children = button.getChildren();
        for (UIComponent child : children) {
            if (!(child instanceof OUIClientAction || child instanceof UIParameter))
                return true;
        }
        return false;
    }

}
