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
package org.openfaces.renderkit.window;

import org.openfaces.component.CaptionArea;
import org.openfaces.component.ComponentWithCaption;
import org.openfaces.component.Side;
import org.openfaces.component.window.AbstractWindow;
import org.openfaces.component.window.CloseWindowButton;
import org.openfaces.component.window.MaximizeWindowButton;
import org.openfaces.component.window.MinimizeWindowButton;
import org.openfaces.component.window.PopupLayer;
import org.openfaces.renderkit.ComponentWithCaptionRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AbstractWindowRenderer extends PopupLayerRenderer {
    private static final String DEFAULT_CAPTION_CLASS = "o_window_caption";
    private static final String DEFAULT_CAPTION_WIDTH_CLASS = "o_default_caption_width";
    private static int uniqIdSuffix = 0;
    public static final String MIDDLE_AREA_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "content";

    public static String getWindowJs(FacesContext context) {
        return Resources.internalURL(context, "window/window.js");
    }

    private ComponentWithCaptionRenderer componentWithCaptionRenderer = new ComponentWithCaptionRenderer() {
        @Override
        protected String getDefaultCaptionClass(ComponentWithCaption component) {
            return DEFAULT_CAPTION_CLASS + ' ' + DEFAULT_CAPTION_WIDTH_CLASS;
        }

        @Override
        protected void createDefaultAreaButtons(FacesContext context, ComponentWithCaption component, CaptionArea area) {
            area.setAlignment(Side.RIGHT);
            AbstractWindow win = (AbstractWindow) component;
            CloseWindowButton closeButton = new CloseWindowButton();
            //TODO: this is temporary solution for duplicate ID's inside dataTable rows it mus be removed when the problem will be solved

            String uid = UUID.randomUUID().toString();

            uid = uid.replaceAll("-","");

            closeButton.setId(win.getId() + Rendering.SERVER_ID_SUFFIX_SEPARATOR + "closeButton" + uid);
            List<UIComponent> areaChildren = area.getChildren();
            if (isMinimizeAllowed())
                areaChildren.add(new MinimizeWindowButton());
            if (win.isResizable())
                areaChildren.add(new MaximizeWindowButton());
            areaChildren.add(closeButton);
        }
    };

    protected boolean isMinimizeAllowed() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        ResponseWriter writer = context.getResponseWriter();
        AbstractWindow win = (AbstractWindow) component;
        String clientId = win.getClientId(context);

        super.encodeBegin(context, component);

        writer.startElement("table", win);
        writer.writeAttribute("id", clientId + "::table", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writeAttribute(writer, "class", getDefaultTableClass());
        writer.writeAttribute("height", "100%", null);
        writer.writeAttribute("width", "100%", null);

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
    }

    protected void encodeCaption(FacesContext context, AbstractWindow window) throws IOException {
        UIComponent captionFacet = window.getCaptionFacet();
        String captionText = window.getCaption();

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

    protected String getDefaultContentClass() {
        return null;
    }

    protected String getDefaultTableClass() {
        return null;
    }

    @Override
    protected String getDefaultWidth() {
        return "300px";
    }

    @Override
    protected void encodeScriptsAndStyles(FacesContext context, PopupLayer component) throws IOException {
        super.encodeScriptsAndStyles(context, component);

        AbstractWindow win = (AbstractWindow) component;
        ScriptBuilder sb = new ScriptBuilder();
        sb.initScript(context, win, "O$.Window._init",
                win.isResizable(),
                win.isDraggableByContent(),
                win.getMinWidth(),
                win.getMinHeight());
        Rendering.renderInitScript(context, sb, getWindowJs(context));
    }


    @Override
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

    @Override
    protected String getDefaultClassName() {
        return Styles.mergeClassNames(super.getDefaultClassName(), "o_window");
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }
}
