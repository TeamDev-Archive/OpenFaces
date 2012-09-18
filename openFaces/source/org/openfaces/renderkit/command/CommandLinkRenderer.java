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
import org.openfaces.component.OUICommand;
import org.openfaces.component.command.CommandLink;
import org.openfaces.renderkit.OUICommandRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

public class CommandLinkRenderer extends OUICommandRenderer {
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

        boolean ajaxJsRequired = writeEventsWithAjaxSupport(context, writer, link,
                ((OUICommand)component).getActionTriggerParam());
        if (ajaxJsRequired)
            link.getAttributes().put("_ajaxRequired", Boolean.TRUE);

        Object value = link.getValue();
        if (value != null) {
            boolean hasExplicitContent = false;
            List<UIComponent> children = link.getChildren();
            for (UIComponent child : children) {
                if (!(child instanceof OUIClientAction)) {
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
        return "a";
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        CommandLink link = (CommandLink) component;

        ScriptBuilder sb = new ScriptBuilder().functionCall("O$.Link._init", link, link.isDisabled(),
                Styles.getCSSClass(context, link, link.getDisabledStyle(), StyleGroup.disabledStyleGroup(),
                        link.getDisabledClass(), "o_link_disabled " + DefaultStyles.getTextColorClass()));
        Rendering.renderInitScript(context, sb, Resources.utilJsURL(context));

        writer.endElement(getTagName(link));
        Resources.renderJSLinkIfNeeded(context, Resources.utilJsURL(context));
        if (link.getAttributes().remove("_ajaxRequired") != null)
            AjaxUtil.renderJSLinks(context);
    }
}
