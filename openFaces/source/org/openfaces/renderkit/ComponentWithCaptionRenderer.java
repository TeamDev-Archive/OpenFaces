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
package org.openfaces.renderkit;

import org.openfaces.component.CaptionArea;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Styles;
import org.openfaces.component.ComponentWithCaption;
import org.openfaces.component.Side;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class ComponentWithCaptionRenderer extends RendererBase {
    public static final String CAPTION_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "caption";
    private static final String CAPTION_CONTENT_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "caption_content";

    public void renderCaption(FacesContext context, ComponentWithCaption component) throws IOException {
        UIComponent uiComponent = (UIComponent) component;

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", uiComponent);
        writer.writeAttribute("id", uiComponent.getClientId(context) + CAPTION_SUFFIX, null);
        writeAttribute(writer, "class", Styles.getCSSClass(
                context, uiComponent, component.getCaptionStyle(), getDefaultCaptionClass(component), component.getCaptionClass()
        ));
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("border", "0", null);
        writer.startElement("tr", uiComponent);

        List<CaptionArea> captionAreas = getCaptionAreas(component);
        renderCaptionAreas(context, captionAreas, Side.LEFT);
        renderCaptionContentCell(context, component);
        renderCaptionAreas(context, captionAreas, Side.RIGHT);

        writer.endElement("tr");
        writer.endElement("table");
    }

    protected String getDefaultCaptionClass(ComponentWithCaption component) {
        return "o_folding_panel_caption";
    }

    private void renderCaptionContentCell(FacesContext context, ComponentWithCaption component) throws IOException {
        UIComponent uiComponent = (UIComponent) component;
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("td", uiComponent);
        writer.startElement("div", uiComponent);
        writer.writeAttribute("id", uiComponent.getClientId(context) + CAPTION_CONTENT_SUFFIX, null);
        writeAdditionalCaptionCellContent(writer, component);
        renderCaptionContent(context, component);
        writer.endElement("div");
        writer.endElement("td");
    }

    protected void renderCaptionContent(FacesContext context, ComponentWithCaption component) throws IOException {
        UIComponent captionFacet = component.getCaptionFacet();
        if (captionFacet != null)
            captionFacet.encodeAll(context);
        else {
            String captionText = component.getCaption();
            if (captionText != null)
                context.getResponseWriter().writeText(captionText, null);
        }
    }

    protected void renderCaptionAreas(FacesContext context, List<CaptionArea> captionAreas, Side alignment) throws IOException {
        if (alignment == null)
            alignment = Side.RIGHT;
        for (CaptionArea area : captionAreas) {
            if (alignment.equals(area.getAlignment()))
                area.encodeAll(context);
        }
    }

    protected void writeAdditionalCaptionCellContent(
            ResponseWriter writer, ComponentWithCaption component) throws IOException {

    }

    protected List<CaptionArea> getCaptionAreas(ComponentWithCaption component) {
        List<CaptionArea> captionAreas = Components.findChildrenWithClass((UIComponent) component, CaptionArea.class);
        if (captionAreas == null || captionAreas.size() == 0)
            captionAreas = Collections.singletonList(getDefaultButtonsArea(component));
        return captionAreas;
    }

    protected CaptionArea getDefaultButtonsArea(ComponentWithCaption component) {
        UIComponent uiComponent = (UIComponent) component;
        CaptionArea area = (CaptionArea) uiComponent.getFacet("_defaultButtonsArea");
        if (area == null) {
            area = new CaptionArea();
            area.setAlignment(Side.RIGHT);
            uiComponent.getFacets().put("_defaultButtonsArea", area);

            FacesContext context = FacesContext.getCurrentInstance();
            createDefaultAreaButtons(context, component, area);
        }
        return area;
    }

    protected void createDefaultAreaButtons(FacesContext context, ComponentWithCaption component, CaptionArea area) {

    }

    public static void renderChildren(FacesContext context, ComponentWithCaption componentWithCaption) throws IOException {
        List<UIComponent> children = ((UIComponent) componentWithCaption).getChildren();
        for (UIComponent child : children) {
            if (!(child instanceof CaptionArea))
                child.encodeAll(context);
        }
    }
}
