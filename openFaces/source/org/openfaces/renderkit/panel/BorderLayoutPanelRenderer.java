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
package org.openfaces.renderkit.panel;

import org.openfaces.component.panel.BorderLayoutPanel;
import org.openfaces.component.panel.SidePanel;
import org.openfaces.util.RawScript;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.EnvironmentUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Tarasyuk
 */
public class BorderLayoutPanelRenderer extends RendererBase {

    public static final String CONTENT_SUFFIX = RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + "content";

    private static final String JS_SCRIPT_URL = "borderLayoutPanel.js";
    private static final String JS_MY_UTIL_SCRIPT_URL = "sidePanelUtil.js";

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        BorderLayoutPanel borderLayoutPanel = (BorderLayoutPanel) component;
        String clientId = borderLayoutPanel.getClientId(context);
        writer.startElement("div", borderLayoutPanel);
        writer.writeAttribute("id", clientId, null);
        String classStr = StyleUtil.getCSSClass(context,
                borderLayoutPanel, borderLayoutPanel.getStyle(),
                "o_borderlayoutpanel_container", borderLayoutPanel.getStyleClass()
        );
        writer.writeAttribute("class", classStr, null);

        writeStandardEvents(writer, borderLayoutPanel);
        encodeScriptsAndStyles_part1(context, clientId);
        encodeSidePanels(context, borderLayoutPanel);

        writer.startElement("div", borderLayoutPanel);
        writer.writeAttribute("id", clientId + "::content", null);
        classStr = StyleUtil.getCSSClass(context,
                borderLayoutPanel, borderLayoutPanel.getContentStyle(),
                "o_borderlayoutpanel_content", borderLayoutPanel.getContentClass()
        );
        writer.writeAttribute("class", classStr, null);

        encodeScriptsAndStyles_part2(context, borderLayoutPanel, clientId);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
        StyleUtil.renderStyleClasses(context, component);
        writer.endElement("div");
    }

    private void encodeScriptsAndStyles_part1(FacesContext context, String clientId) throws IOException {
        ScriptBuilder initScript = new ScriptBuilder();

        if (EnvironmentUtil.isMozillaFF2(context)) { // fix bug with FF2 + Faceletes + JSF 1.2 context  // todo add isFacelets() filter
            initScript.append("O$('").append(clientId).append("').style.visibility = 'hidden';\n");
            initScript.append("O$.addLoadEvent(function() {\n");
        }

        initScript.functionCall("O$._initBorderLayoutPanel_part1", clientId).semicolon();

        if (EnvironmentUtil.isMozillaFF2(context)) { // fix bug with FF2 + Faceletes + JSF 1.2 context  // todo add isFacelets() filter
            initScript.append("});\n");
        }

        RenderingUtil.renderInitScript(context, initScript,
                new String[]{ResourceUtil.getUtilJsURL(context),
                        ResourceUtil.getInternalResourceURL(context, BorderLayoutPanelRenderer.class, JS_SCRIPT_URL),
                        ResourceUtil.getInternalResourceURL(context, BorderLayoutPanelRenderer.class, JS_MY_UTIL_SCRIPT_URL)});
    }

    private void encodeScriptsAndStyles_part2(FacesContext context, BorderLayoutPanel borderLayoutPanel, String clientId) throws IOException {
        ScriptBuilder initScript = new ScriptBuilder();

        if (EnvironmentUtil.isMozillaFF2(context)) { // fix bug with FF2 + Faceletes + JSF 1.2 context  // todo add isFacelets() filter
            initScript.append("O$.addLoadEvent( function() {\n");
        }

        initScript.initScript(context, borderLayoutPanel, "O$._initBorderLayoutPanel_part2",
                RenderingUtil.getRolloverClass(context, borderLayoutPanel),
                new RawScript(JSEventsObject.JSEventObject("oncontentresize", borderLayoutPanel.getOncontentresize()))
        );

        if (EnvironmentUtil.isMozillaFF2(context)) { // fix bug with FF2 + Faceletes + JSF 1.2 context  // todo add isFacelets() filter
            initScript.append("O$('").append(clientId).append("').style.visibility = 'visible';\n"); // todo: why isn't this built-in into O$._initBorderLayoutPanel_part2 function iteself?
            initScript.append("});\n");
        }

        RenderingUtil.renderInitScript(context, initScript);
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        BorderLayoutPanel borderLayoutPanel = (BorderLayoutPanel) component;
        List<UIComponent> children = borderLayoutPanel.getChildren();
        List<UIComponent> contentElements = new ArrayList<UIComponent>();
        for (UIComponent child : children) {
            if (!(child instanceof SidePanel)) {
                contentElements.add(child);
            }
        }
        RenderingUtil.renderComponents(context, contentElements);
    }

    private void encodeSidePanels(FacesContext context, BorderLayoutPanel borderLayoutPanel) throws IOException {
        List<UIComponent> children = borderLayoutPanel.getChildren();
        List<UIComponent> sidePanels = new ArrayList<UIComponent>();
        for (UIComponent child : children) {
            if (child instanceof SidePanel) {
                sidePanels.add(child);
            }
        }
        RenderingUtil.renderComponents(context, sidePanels);
    }

    public boolean getRendersChildren() {
        return true;
    }
}
