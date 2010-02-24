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
package org.openfaces.renderkit.panel;

import org.openfaces.component.CaptionArea;
import org.openfaces.component.ComponentWithCaption;
import org.openfaces.component.ExpansionToggleButton;
import org.openfaces.component.Side;
import org.openfaces.component.LoadingMode;
import org.openfaces.component.ToggleCaptionButton;
import org.openfaces.component.panel.FoldingDirection;
import org.openfaces.component.panel.FoldingPanel;
import org.openfaces.event.StateChangeEvent;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.ComponentWithCaptionRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
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

/**
 * @author Kharchenko
 */
public class FoldingPanelRenderer extends ComponentWithCaptionRenderer implements AjaxPortionRenderer {
    public static final String CONTENT_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "content";
    private static final String STATE_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "state";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        FoldingPanel foldingPanel = (FoldingPanel) component;
        ResponseWriter writer = context.getResponseWriter();
        if (!foldingPanel.isExpanded() && (foldingPanel.getLoadingMode().equals(LoadingMode.AJAX_LAZY) ||
                                           foldingPanel.getLoadingMode().equals(LoadingMode.AJAX_ALWAYS))) {
            AjaxUtil.prepareComponentForAjax(context, component);
        }
        writer.startElement("table", foldingPanel);

        writeIdAttribute(context, foldingPanel);
        Rendering.writeComponentClassAttribute(writer, foldingPanel, "o_folding_panel");
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        Rendering.writeStandardEvents(writer, foldingPanel);
        writer.startElement("tr", foldingPanel);
        writer.startElement("td", foldingPanel);
        List<CaptionArea> captionAreas = getCaptionAreas(foldingPanel);
        for (CaptionArea captionArea : captionAreas) {
            List<ExpansionToggleButton> expansionToggleButtons = Components.findChildrenWithClass(captionArea, ExpansionToggleButton.class);
            for (ExpansionToggleButton expansionToggleButton : expansionToggleButtons) {
                prepareToggleButton(foldingPanel, expansionToggleButton);
            }
        }

        FoldingDirection foldingDirection = foldingPanel.getFoldingDirection();
        if (!FoldingDirection.UP.equals(foldingDirection)) {
            renderCaption(context, foldingPanel);

            writer.endElement("td");
            writer.endElement("tr");

            writer.startElement("tr", foldingPanel);
            boolean horizontalFoldingDirection = FoldingDirection.LEFT.equals(foldingDirection) || FoldingDirection.RIGHT.equals(foldingDirection);
            if (horizontalFoldingDirection) {
                writeAttribute(writer, "style", "height: 100%;");
            }
            writer.startElement("td", foldingPanel);
            if (horizontalFoldingDirection) {
                writeAttribute(writer, "style", "text-align: left; vertical-align: top;");
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        renderContentElement(context, (FoldingPanel) component);
    }

    private void renderContentElement(FacesContext context, FoldingPanel foldingPanel) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", foldingPanel);
        writer.writeAttribute("id", foldingPanel.getClientId(context) + CONTENT_SUFFIX, null);
        String styleClass = Styles.mergeClassNames("o_folding_panel_content", foldingPanel.getContentClass());
        String style = foldingPanel.getContentStyle() != null
                ? foldingPanel.getContentStyle() + (foldingPanel.isExpanded() ? "" : "; display: none;")
                : foldingPanel.isExpanded() ? "" : "display: none;";
        Rendering.writeStyleAndClassAttributes(writer, style, styleClass);

        boolean clientLoadingMode = foldingPanel.getLoadingMode().equals(LoadingMode.CLIENT);
        boolean preloadContent = clientLoadingMode || foldingPanel.isExpanded();
        foldingPanel.getAttributes().put("_contentPreloaded_", preloadContent);
        if (preloadContent) {
            renderChildren(context, foldingPanel);
        }

        writer.endElement("div");
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        String key = component.getClientId(context) + STATE_SUFFIX;
        String value = context.getExternalContext().getRequestParameterMap().get(key);
        if (value == null)
            return;

        boolean newExpanded = Boolean.valueOf(value);
        FoldingPanel panel = (FoldingPanel) component;
        if (panel.isExpanded() != newExpanded) {
            panel.setExpanded(newExpanded);
            component.queueEvent(new StateChangeEvent(component));
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;

        ResponseWriter writer = context.getResponseWriter();
        FoldingPanel panel = (FoldingPanel) component;
        FoldingDirection foldingDirection = panel.getFoldingDirection();
        if (FoldingDirection.UP.equals(foldingDirection)) {
            writer.endElement("td");
            writer.endElement("tr");

            writer.startElement("tr", panel);
            writer.startElement("td", panel);
            renderCaption(context, panel);
        }
        renderStateField(context, component);

        renderInitScript(context, component);
        Styles.renderStyleClasses(context, component);
        Rendering.encodeClientActions(context, panel);
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }


    private void renderStateField(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        FoldingPanel foldingPanel = (FoldingPanel) component;
        String value = String.valueOf(foldingPanel.isExpanded());
        Rendering.renderHiddenField(writer, component.getClientId(context) + STATE_SUFFIX, value);
    }

    private void renderInitScript(FacesContext context, UIComponent component) throws IOException {
        FoldingPanel foldingPanel = (FoldingPanel) component;

        LoadingMode loadingMode = foldingPanel.getLoadingMode();
        String defaultClass = Styles.getCSSClass(context, foldingPanel, Rendering.DEFAULT_FOCUSED_STYLE, StyleGroup.selectedStyleGroup(0));
        String focusedClass = Styles.getCSSClass(context,
                foldingPanel,
                foldingPanel.getFocusedStyle(), StyleGroup.selectedStyleGroup(1), foldingPanel.getFocusedClass(), defaultClass);
        String focusedContentClass = Styles.getCSSClass(context, foldingPanel,
                foldingPanel.getFocusedContentStyle(), StyleGroup.selectedStyleGroup(0), foldingPanel.getFocusedContentClass(), null);

        String focusedCaptionClass = Styles.getCSSClass(context, foldingPanel,
                foldingPanel.getFocusedCaptionStyle(), StyleGroup.selectedStyleGroup(0), foldingPanel.getFocusedCaptionClass(), null);

        ScriptBuilder sb = new ScriptBuilder();
        sb.initScript(context, foldingPanel, "O$.FoldingPanel._init",
                foldingPanel.isExpanded(),
                foldingPanel.getFoldingDirection(),
                Rendering.getRolloverClass(context, foldingPanel),
                loadingMode,
                foldingPanel.isFocusable(),
                focusedClass,
                focusedContentClass,
                focusedCaptionClass);

        Rendering.renderInitScript(context, sb,
                Resources.getInternalURL(context, FoldingPanelRenderer.class, "foldingPanel.js"));
    }

    @Override
    protected void writeAdditionalCaptionCellContent(ResponseWriter writer, ComponentWithCaption component) throws IOException {
        FoldingPanel panel = (FoldingPanel) component;
        FoldingDirection foldingDirection = panel.getFoldingDirection();
        if (!panel.isExpanded() &&
                (FoldingDirection.LEFT.equals(panel.getFoldingDirection()) || FoldingDirection.RIGHT.equals(foldingDirection))) {
            writer.writeAttribute("style", "display: none;", null);
        }
    }

    @Override
    protected CaptionArea getDefaultButtonsArea(ComponentWithCaption component) {
        CaptionArea area = super.getDefaultButtonsArea(component);

        FoldingPanel foldingPanel = (FoldingPanel) component;
        FoldingDirection foldingDirection = foldingPanel.getFoldingDirection();
        boolean horizontalFoldingDirection =
                FoldingDirection.LEFT.equals(foldingDirection) || FoldingDirection.RIGHT.equals(foldingDirection);
        boolean buttonsOnTheLeft = horizontalFoldingDirection && FoldingDirection.RIGHT.equals(foldingDirection);
        area.setAlignment(buttonsOnTheLeft ? Side.LEFT : Side.RIGHT);
        return area;
    }

    private void prepareToggleButton(FoldingPanel foldingPanel, ToggleCaptionButton btn) {
        btn.setToggled(foldingPanel.isExpanded());
        String onStateChange = foldingPanel.getOnstatechange() != null ? foldingPanel.getOnstatechange() : "";
        btn.setOnclick(onStateChange);
    }

    public JSONObject encodeAjaxPortion(FacesContext context,
                                        UIComponent component,
                                        String portionId,
                                        JSONObject jsonParam) throws IOException {
        if (!portionId.equals("content"))
            throw new IllegalArgumentException("Unknown portionId: " + portionId);
        renderContentElement(context, (FoldingPanel) component);
        return null;
    }
}
