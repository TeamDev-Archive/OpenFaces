/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
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
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class CommandButtonRenderer extends RendererBase {

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
            writer.writeAttribute("src", Resources.getApplicationURL(context, image), "image");
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
                btn.getAttributes().put("_ajaxRequired", Boolean.TRUE);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CommandButton btn = (CommandButton) component;
        writer.endElement(getTagName(btn));
        if (btn.getAttributes().remove("_ajaxRequired") != null)
            AjaxUtil.renderJSLinks(context);

    }

    private String getTagName(CommandButton button) {
        return isButtonTagMode(button) ? "button" : "input";
    }

    private boolean isButtonTagMode(CommandButton button) {
        List<UIComponent> children = button.getChildren();
        for (UIComponent child : children) {
            if (!(child instanceof OUIClientAction) && !Rendering.isA4jSupportComponent(child))
                return true;
        }
        return false;
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        String key = component.getClientId(context);
        if (requestParameters.containsKey(key)) {
            component.queueEvent(new ActionEvent(component));
        }
    }
}
