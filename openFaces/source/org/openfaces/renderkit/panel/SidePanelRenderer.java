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

import org.openfaces.component.panel.SidePanel;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Environment;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

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

    public static final String SPLITTER_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "splitter";
    public static final String PANEL_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "panel";
    public static final String CAPTION_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "caption";
    public static final String CONTENT_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "content";

    public static final String SIDE_PANEL_UTIL_JS = "panel/sidePanelUtil.js";

    @Override
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

    @Override
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
        String splitterDefaultClass = Styles.getCSSClass(context,
                sidePanel, sidePanel.getSplitterStyle(),
                "o_sidepanel_splitter o_sidepanel_splitter_" + sidePanel.getAlignment().toString(), sidePanel.getSplitterClass()
        );
        writer.writeAttribute("class", splitterDefaultClass, null);
        writer.endElement("div");

        //panel
        writer.startElement("div", sidePanel);
        writer.writeAttribute("id", clientId + PANEL_SUFFIX, null);
        Rendering.writeStandardEvents(writer, sidePanel);
        String panelDefaultClass = Styles.getCSSClass(context,
                sidePanel, sidePanel.getStyle(),
                "o_sidepanel_panel o_sidepanel_panel_" + sidePanel.getAlignment().toString(), sidePanel.getStyleClass()
        );
        writer.writeAttribute("class", panelDefaultClass, null);
        UIComponent captionFacet = sidePanel.getCaptionFacet();
        String caption = sidePanel.getCaption();
        if (captionFacet != null || caption != null) {
            writer.startElement("div", sidePanel);
            writer.writeAttribute("id", clientId + CAPTION_SUFFIX, null);
            panelDefaultClass = Styles.getCSSClass(context,
                    sidePanel, sidePanel.getCaptionStyle(),
                    "o_sidepanel_caption", sidePanel.getCaptionClass()
            );
            writer.writeAttribute("class", panelDefaultClass, null);
            if (captionFacet != null)
                captionFacet.encodeAll(context);
            else
                writer.writeText(caption, null);
            writer.endElement("div");
        }

        //content
        writer.startElement("div", sidePanel);
        writer.writeAttribute("id", clientId + CONTENT_SUFFIX, "id");
        String contentDefaultClass = Styles.getCSSClass(context,
                sidePanel, sidePanel.getContentStyle(),
                "o_sidepanel_content", sidePanel.getContentClass()
        );
        writer.writeAttribute("class", contentDefaultClass, null);

        encodeInitScript(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;
        ResponseWriter writer = context.getResponseWriter();

        Styles.renderStyleClasses(context, component);

        writer.endElement("div");
        writer.endElement("div");
        writer.endElement("div");
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) return;

        SidePanel sidePanel = (SidePanel) component;
        if (sidePanel.getFacet("content") != null) {
            sidePanel.getFacet("content").encodeAll(context);
        } else {
            List<UIComponent> children = component.getChildren();
            Rendering.renderComponents(context, children);
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
        if (Environment.isMozillaFF2(context)) { //fix bug with FF2 and Facelets 1.2 context  //todo add isFacelets() filter
            initScript.append("O$('").append(clientId).append("').style.visibility = 'hidden';\n");
            initScript.append("O$.addLoadEvent( function() {\n");
        }

        initScript.initScript(context, sidePanel, "O$._initSidePanel",
                sidePanel.getAlignment(),
                sidePanel.getSize(),
                sidePanel.getMinSize(),
                sidePanel.getMaxSize(),
                sidePanel.isCollapsible(),
                sidePanel.isResizable(),
                sidePanel.getCollapsed(),
                getRolloverClass(context, sidePanel),
                getSplitterRolloverClass(context, sidePanel),
                new RawScript(events.toString()));

        if (Environment.isMozillaFF2(context)) { //fix bug with FF2 and Facelets 1.2 context  //todo add isFacelets() filter
            initScript.append("O$('").append(clientId).append("').style.visibility = 'visible';\n");
            initScript.append("});\n");
        }
        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "panel/sidePanel.js"),
                Resources.internalURL(context, SIDE_PANEL_UTIL_JS));
    }

    private String getRolloverClass(FacesContext context, SidePanel sidePanel) {
        return Styles.getCSSClass(context,
                sidePanel, sidePanel.getRolloverStyle(), StyleGroup.rolloverStyleGroup(),
                sidePanel.getRolloverClass());
    }

    private String getSplitterRolloverClass(FacesContext context, SidePanel sidePanel) {
        return Styles.getCSSClass(context,
                sidePanel, sidePanel.getSplitterRolloverStyle(), StyleGroup.rolloverStyleGroup(),
                sidePanel.getSplitterRolloverClass()
        );
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}

