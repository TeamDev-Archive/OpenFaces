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
package org.openfaces.renderkit.panel;

import org.openfaces.component.panel.BorderLayoutPanel;
import org.openfaces.component.panel.SidePanel;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Environment;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

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
    public static final String CONTENT_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "content";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        BorderLayoutPanel borderLayoutPanel = (BorderLayoutPanel) component;
        String clientId = borderLayoutPanel.getClientId(context);
        writer.startElement("div", borderLayoutPanel);
        writer.writeAttribute("id", clientId, null);
        writeAttribute(writer, "style", borderLayoutPanel.getStyle());
        String classStr = Styles.getCSSClass(context,
                borderLayoutPanel, borderLayoutPanel.getStyle(),
                "o_borderlayoutpanel_container", borderLayoutPanel.getStyleClass()
        );
        writer.writeAttribute("class", classStr, null);

        Rendering.writeStandardEvents(writer, borderLayoutPanel);
        encodeScriptsAndStyles(context, borderLayoutPanel);
        encodeSidePanels(context, borderLayoutPanel);

        writer.startElement("div", borderLayoutPanel);
        writer.writeAttribute("id", clientId + "::content", null);
        String contentClassStr = Styles.getCSSClass(context,
                borderLayoutPanel, borderLayoutPanel.getContentStyle(),
                "o_borderlayoutpanel_content", borderLayoutPanel.getContentClass()
        );
        writer.writeAttribute("class", contentClassStr, null);

        encodeScriptsAndStyles_content(context, borderLayoutPanel, clientId);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
        Styles.renderStyleClasses(context, component);
        writer.endElement("div");
    }

    private void encodeScriptsAndStyles(FacesContext context, BorderLayoutPanel borderLayoutPanel) throws IOException {
        ScriptBuilder initScript = new ScriptBuilder();

        if (Environment.isMozillaFF2(context)) { // fix bug with FF2 + Facelets + JSF 1.2 context  // todo add isFacelets() filter
            initScript.append("O$('").append(borderLayoutPanel.getId()).append("').style.visibility = 'hidden';\n");
            initScript.append("O$.addLoadEvent(function() {\n");
        }

        initScript.initScript(context,
                borderLayoutPanel,
                "O$._initBorderLayoutPanel",
                borderLayoutPanel.getId());

        if (Environment.isMozillaFF2(context)) { // fix bug with FF2 + Facelets + JSF 1.2 context  // todo add isFacelets() filter
            initScript.append("});\n");
        }

        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "panel/borderLayoutPanel.js"),
                Resources.internalURL(context, SidePanelRenderer.SIDE_PANEL_UTIL_JS));
    }

    private void encodeScriptsAndStyles_content(FacesContext context, BorderLayoutPanel borderLayoutPanel, String clientId) throws IOException {
        ScriptBuilder initScript = new ScriptBuilder();

        if (Environment.isMozillaFF2(context)) { // fix bug with FF2 + Facelets + JSF 1.2 context  // todo add isFacelets() filter
            initScript.append("O$.addLoadEvent( function() {\n");
        }

        initScript.initScript(context, borderLayoutPanel, "O$._initBorderLayoutPanel_content",
                Rendering.getRolloverClass(context, borderLayoutPanel),
                new RawScript(JSEventsObject.JSEventObject("oncontentresize", borderLayoutPanel.getOncontentresize()))
        );

        if (Environment.isMozillaFF2(context)) { // fix bug with FF2 + Facelets + JSF 1.2 context  // todo add isFacelets() filter
            initScript.append("O$('").append(clientId).append("').style.visibility = 'visible';\n"); // todo: why isn't this built-in into O$._initBorderLayoutPanel_content function itself?
            initScript.append("});\n");
        }

        Rendering.renderInitScript(context, initScript);
    }

    @Override
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
        Rendering.renderComponents(context, contentElements);
    }

    private void encodeSidePanels(FacesContext context, BorderLayoutPanel borderLayoutPanel) throws IOException {
        List<UIComponent> children = borderLayoutPanel.getChildren();
        List<UIComponent> sidePanels = new ArrayList<UIComponent>();
        for (UIComponent child : children) {
            if (child instanceof SidePanel) {
                sidePanels.add(child);
            }
        }
        Rendering.renderComponents(context, sidePanels);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
