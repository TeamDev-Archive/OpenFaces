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
package org.openfaces.renderkit.window;

import org.openfaces.component.CaptionArea;
import org.openfaces.component.ComponentWithCaption;
import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.window.AbstractWindow;
import org.openfaces.component.window.CloseWindowButton;
import org.openfaces.component.window.MaximizeWindowButton;
import org.openfaces.component.window.MinimizeWindowButton;
import org.openfaces.component.window.PopupLayer;
import org.openfaces.renderkit.ComponentWithCaptionRenderer;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.AjaxUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractWindowRenderer extends PopupLayerRenderer {
    private static final String DEFAULT_CAPTION_CLASS = "o_window_caption";
    private static final String DEFAULT_CAPTION_WIDTH_CLASS = "o_default_caption_width";
    private static final String PRE_ANCHOR = RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + "preAnchor";
    private static final String POST_ANCHOR = RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + "postAnchor";

    public static String getWindowJs(FacesContext context) {
        return ResourceUtil.getInternalResourceURL(context, AbstractWindowRenderer.class, "window.js");
    }

    private ComponentWithCaptionRenderer componentWithCaptionRenderer = new ComponentWithCaptionRenderer() {
        protected String getDefaultCaptionClass(ComponentWithCaption component) {
            return ((AbstractWindow) component).getWidth() == null
                    ? DEFAULT_CAPTION_CLASS : DEFAULT_CAPTION_CLASS + ' ' + DEFAULT_CAPTION_WIDTH_CLASS;
        }

        protected void renderCaptionContent(FacesContext context, ComponentWithCaption component, UIComponent captionContent) throws IOException {
            if (captionContent != null)
                super.renderCaptionContent(context, component, captionContent);
            else {
                String captionText = ((AbstractWindow) component).getCaptionText();
                if (captionText != null)
                    context.getResponseWriter().writeText(captionText, null);
            }

        }

        protected void createDefaultAreaButtons(FacesContext context, ComponentWithCaption component, CaptionArea area) {
            area.setAlignment(HorizontalAlignment.RIGHT);
            AbstractWindow win = (AbstractWindow) component;
            CloseWindowButton closeButton = new CloseWindowButton();
            closeButton.setId(win.getId() + RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + "closeButton");
            List<UIComponent> areaChildren = area.getChildren();
            if (isMinimizeAllowed())
                areaChildren.add(new MinimizeWindowButton());
            if (win.isResizeable())
                areaChildren.add(new MaximizeWindowButton());
            areaChildren.add(closeButton);
        }
    };

    protected boolean isMinimizeAllowed() {
        return true;
    }

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        ResponseWriter writer = context.getResponseWriter();
        AbstractWindow win = (AbstractWindow) component;
        String clientId = win.getClientId(context);

        super.encodeBegin(context, component);

        encodeAnchor(context, win, PRE_ANCHOR);
        writer.startElement("table", win);
        writer.writeAttribute("id", clientId + "::table", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writeAttribute(writer, "class", getDefaultTableClass());
        writer.writeAttribute("height", "100%", null);
        if (win.getWidth() != null && win.getWidth().length() != 0) {
            writer.writeAttribute("width", "100%", null);
        }

        writer.startElement("tr", win);
        writer.writeAttribute("style", "height: 1px;", null);
        writer.startElement("td", win);

        encodeCaption(context, win);

        writer.endElement("td");
        writer.endElement("tr");
        writer.startElement("tr", win);
        writer.writeAttribute("id", clientId + "::contentRow", null);
        writer.startElement("td", win);
        writer.writeAttribute("height", "100%", null);

        encodeContentPane(context, win);

        writer.endElement("td");
        writer.endElement("tr");
        encodeFooterPane(writer, win, clientId);
        writer.endElement("table");

        encodeAnchor(context, win, POST_ANCHOR);
    }

    protected void encodeCaption(FacesContext context, AbstractWindow window) throws IOException {
        UIComponent captionFacet = window.getCaption();
        String captionText = window.getCaptionText();

        if (captionFacet == null && captionText == null && !getForceRenderCaptionIfNotSpecified())
            return;

        componentWithCaptionRenderer.renderCaption(context, window);
    }


    protected boolean getForceRenderCaptionIfNotSpecified() {
        return true;
    }

    protected void encodeFooterPane(ResponseWriter writer, AbstractWindow window, String clientId) throws IOException {
    }

    protected abstract void encodeContentPane(FacesContext context, AbstractWindow window) throws IOException;

    private void encodeAnchor(FacesContext context, AbstractWindow window, String postfix) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = window.getClientId(context);

        writer.startElement("a", window);
        writer.writeAttribute("href", "javascript: ;", null);
        writer.writeAttribute("id", clientId + postfix, null);
        writer.endElement("a");
    }

    protected String getDefaultContentClass() {
        return null;
    }

    protected String getDefaultTableClass() {
        return null;
    }

    protected void encodeScriptsAndStyles(FacesContext context, PopupLayer component) throws IOException {
        super.encodeScriptsAndStyles(context, component);

        AbstractWindow win = (AbstractWindow) component;
        ScriptBuilder sb = new ScriptBuilder();
        sb.initScript(context, win, "O$._initWindow",
                win.isResizeable(),
                win.isDraggableByContent(),
                win.getMinWidth(),
                win.getMinHeight());
        RenderingUtil.renderInitScript(context, sb, new String[]{
                ResourceUtil.getInternalResourceURL(context, WindowRenderer.class, "window.js")
        });

    }


    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);

        AbstractWindow window = (AbstractWindow) component;
        String sizeKey = window.getClientId(context) + "::size";
        String sizeStr = context.getExternalContext().getRequestParameterMap().get(sizeKey);
        if (sizeStr != null) {
            String[] widthAndHeightArr = sizeStr.split(",");
            int width = Integer.parseInt(widthAndHeightArr[0]);
            int height = Integer.parseInt(widthAndHeightArr[1]);
            window.setWidth(width + "px");
            window.setHeight(height + "px");
        }
    }

    protected String getDefaultClassName() {
        return StyleUtil.mergeClassNames(super.getDefaultClassName(), "o_window");
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }
}
