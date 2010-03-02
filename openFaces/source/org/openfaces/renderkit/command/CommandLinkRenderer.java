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
import org.openfaces.component.command.CommandLink;
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

public class CommandLinkRenderer extends RendererBase {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CommandLink link = (CommandLink) component;
        writer.startElement(getTagName(link), link);
        Rendering.writeIdAttribute(context, component);
        writer.writeAttribute("href", "#", null);
        Rendering.writeAttributes(writer, link,
                "accesskey",
                "tabindex",
                "lang",
                "title",
                "dir",
                "charset",
                "coords",
                "hreflang",
                "rel",
                "rev",
                "shape",
                "target",
                "type");

        Rendering.writeStyleAndClassAttributes(writer, link);

        if (!link.isDisabled()) {
            boolean ajaxJsRequired = writeEventsWithAjaxSupport(context, writer, link,
                    getClickedRequestKey(context, component));
            if (ajaxJsRequired)
                link.getAttributes().put("_ajaxRequired", Boolean.TRUE);
        }

        Object value = link.getValue();
        if (value != null) {
            boolean hasExplicitContent = false;
            List<UIComponent> children = link.getChildren();
            for (UIComponent child : children) {
                if (!(child instanceof OUIClientAction) && !Rendering.isA4jSupportComponent(child)) {
                    hasExplicitContent = true;
                    break;
                }
            }
            if (!hasExplicitContent) {
                writer.writeText(value, "value");
            }
        }

    }


    private String getTagName(CommandLink link) {
        return link.isDisabled() ? "span" : "a";
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CommandLink link = (CommandLink) component;
        writer.endElement(getTagName(link));
        Resources.renderJSLinkIfNeeded(context, Resources.getUtilJsURL(context));
        if (link.getAttributes().remove("_ajaxRequired") != null)
            AjaxUtil.renderJSLinks(context);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        String key = getClickedRequestKey(context, component);
        if (requestParameters.containsKey(key)) {
            component.queueEvent(new ActionEvent(component));
        }
    }

    private String getClickedRequestKey(FacesContext context, UIComponent component) {
        return component.getClientId(context) + "::clicked";
    }
}
