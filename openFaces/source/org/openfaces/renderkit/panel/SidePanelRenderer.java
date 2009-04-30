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

import org.openfaces.component.panel.SidePanel;
import org.openfaces.util.RawScript;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.EnvironmentUtil;
import org.openfaces.util.StyleGroup;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Alexey Tarasyuk
 */
public class SidePanelRenderer extends RendererBase implements NamingContainer {

    public static final String SPLITTER_SUFFIX = RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + "splitter";
    public static final String PANEL_SUFFIX = RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + "panel";
    public static final String CAPTION_SUFFIX = RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + "caption";
    public static final String CONTENT_SUFFIX = RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + "content";

    private static final String JS_SCRIPT_URL = "sidePanel.js";
    private static final String JS_MY_UTIL_SCRIPT_URL = "sidePanelUtil.js";

    public void decode(FacesContext context, UIComponent component) {
        SidePanel sidePanel = (SidePanel) component;
        String clientId = sidePanel.getClientId(context);
        String key = clientId + "state";
        String value = context.getExternalContext().getRequestParameterMap().get(key);
        if (value == null)
            return;
        int index = value.indexOf(';');
        String size = value.substring(0, index);
        String collapsed = value.substring(index + 1);
        sidePanel.setSize(size);
        sidePanel.setCollapsed(Boolean.valueOf(collapsed));
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        SidePanel sidePanel = (SidePanel) component;
        String clientId = sidePanel.getClientId(context);
        writer.startElement("div", sidePanel);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", "o_sidepanel_container o_sidepanel_container_" + sidePanel.getAlignment().toString(), null);
        //splitter
        writer.startElement("div", sidePanel);
        writer.writeAttribute("id", clientId + SPLITTER_SUFFIX, null);
        String classStr = StyleUtil.getCSSClass(context,
                sidePanel, sidePanel.getSplitterStyle(),
                "o_sidepanel_splitter o_sidepanel_splitter_" + sidePanel.getAlignment().toString(), sidePanel.getSplitterClass()
        );
        writer.writeAttribute("class", classStr, null);
        writer.endElement("div");
        //panel
        writer.startElement("div", sidePanel);
        writer.writeAttribute("id", clientId + PANEL_SUFFIX, null);
        writeStandardEvents(writer, sidePanel);
        classStr = StyleUtil.getCSSClass(context,
                sidePanel, sidePanel.getStyle(),
                "o_sidepanel_panel o_sidepanel_panel_" + sidePanel.getAlignment().toString(), sidePanel.getStyleClass()
        );
        writer.writeAttribute("class", classStr, null);
        if (sidePanel.getCaption() != null) {
            writer.startElement("div", sidePanel);
            writer.writeAttribute("id", clientId + CAPTION_SUFFIX, null);
            classStr = StyleUtil.getCSSClass(context,
                    sidePanel, sidePanel.getCaptionStyle(),
                    "o_sidepanel_caption", sidePanel.getCaptionClass()
            );
            writer.writeAttribute("class", classStr, null);
            sidePanel.getCaption().encodeAll(context);
            writer.endElement("div");
        }
        writer.startElement("div", sidePanel);
        writer.writeAttribute("id", clientId + CONTENT_SUFFIX, "id");
        classStr = StyleUtil.getCSSClass(context,
                sidePanel, sidePanel.getContentStyle(),
                "o_sidepanel_content", sidePanel.getContentClass()
        );
        writer.writeAttribute("class", classStr, null);
        StyleUtil.renderStyleClasses(context, sidePanel);
        encodeInitScript(context, component);
    }

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;

        SidePanel sidePanel = (SidePanel) component;
        if (sidePanel.getFacet("content") != null) {
            sidePanel.getFacet("content").encodeAll(context);
        } else {
            List<UIComponent> children = component.getChildren();
            RenderingUtil.renderComponents(context, children);
        }
    }

    private void encodeInitScript(FacesContext context, UIComponent component) throws IOException {
        SidePanel sidePanel = (SidePanel) component;
        String clientId = sidePanel.getClientId(context);

        JSEventsObject events = new JSEventsObject();
        events.putOpt("onsplitterdrag", sidePanel.getOnsplitterdrag());
        events.putOpt("oncollapse", sidePanel.getOncollapse());
        events.putOpt("onrestore", sidePanel.getOnrestore());
        events.putOpt("onmaximize", sidePanel.getOnmaximize());

        ScriptBuilder initScript = new ScriptBuilder();
        if (EnvironmentUtil.isMozillaFF2(context)) { //fix bug with FF2 and Faceletes 1.2 context  //todo add isFacelets() filter
            initScript.append("O$('").append(clientId).append("').style.visibility = 'hidden';\n");
            initScript.append("O$.addLoadEvent( function() {\n");
        }
        initScript.initScript(context, sidePanel, "O$._initSidePanel",
                sidePanel.getAlignment(),
                sidePanel.getSize(),
                sidePanel.getMinSize(),
                sidePanel.getMaxSize(),
                sidePanel.isCollapsible(),
                sidePanel.isResizeable(),
                sidePanel.getCollapsed(),
                RenderingUtil.getRolloverClass(context, sidePanel),
                getSplitterRolloverClass(context, sidePanel),
                new RawScript(events.toString()));

        if (EnvironmentUtil.isMozillaFF2(context)) { //fix bug with FF2 and Faceletes 1.2 context  //todo add isFacelets() filter
            initScript.append("O$('").append(clientId).append("').style.visibility = 'visible';\n");
            initScript.append("});\n");
        }
        RenderingUtil.renderInitScript(context, initScript,
                new String[]{
                        ResourceUtil.getUtilJsURL(context),
                        ResourceUtil.getInternalResourceURL(context, SidePanelRenderer.class, JS_SCRIPT_URL),
                        ResourceUtil.getInternalResourceURL(context, SidePanelRenderer.class, JS_MY_UTIL_SCRIPT_URL)
                });
    }

    private String getSplitterRolloverClass(FacesContext context, SidePanel sidePanel) {
        return StyleUtil.getCSSClass(context,
                sidePanel, sidePanel.getSplitterRolloverStyle(), StyleGroup.rolloverStyleGroup(), sidePanel.getSplitterRolloverClass()
        );
    }

    public boolean getRendersChildren() {
        return true;
    }
}

