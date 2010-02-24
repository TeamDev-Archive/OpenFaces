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

import org.openfaces.component.LoadingMode;
import org.openfaces.component.panel.SubPanel;
import org.openfaces.component.panel.TabbedPane;
import org.openfaces.component.select.TabPlacement;
import org.openfaces.component.select.TabSet;
import org.openfaces.component.select.TabSetItems;
import org.openfaces.renderkit.select.TabSetRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.AnonymousFunction;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew Palval
 */
public class TabbedPaneRenderer extends MultiPageContainerRenderer {

    private static final String TAB_SET_SUFFIX = "tabSet";
    private static final String DEFAULT_BORDER_CLASS_PREFIX = "o_tabbedpane_border_";

    protected String getSelectionHiddenFieldSuffix() {
        return Rendering.SERVER_ID_SUFFIX_SEPARATOR + TAB_SET_SUFFIX + SELECTED_INDEX_SUFFIX;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (AjaxUtil.getSkipExtraRenderingOnPortletsAjax(context))
            return;
        ResponseWriter writer = context.getResponseWriter();
        TabbedPane tabbedPane = (TabbedPane) component;

        LoadingMode loadingMode = tabbedPane.getLoadingMode();
        if (LoadingMode.AJAX_LAZY.equals(loadingMode) || LoadingMode.AJAX_ALWAYS.equals(loadingMode))
            AjaxUtil.prepareComponentForAjax(context, component);

        List<SubPanel> allSubPanels = tabbedPane.getSubPanels(true);

        // implementation note for one who is going to remove outer table rendering
        // need to check, that style="padding: 10px;" (10px for example) do not breaks border under IE
        // JSFC-754 TabbedPane border is not solid if set paddings
        writer.startElement("table", tabbedPane);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("id", tabbedPane.getClientId(context), "id");

        Rendering.writeComponentClassAttribute(writer, tabbedPane);

        Rendering.writeStandardEvents(writer, tabbedPane);

        TabPlacement tabPlacement = tabbedPane.getTabPlacement();
        if (tabPlacement == null)
            tabPlacement = TabPlacement.TOP;

        boolean horizontalPlacement = TabPlacement.LEFT.equals(tabPlacement) || TabPlacement.RIGHT.equals(tabPlacement);
        boolean paneFirst = TabPlacement.RIGHT.equals(tabPlacement) || TabPlacement.BOTTOM.equals(tabPlacement);
        writer.startElement("tr", tabbedPane);
        writer.startElement("td", tabbedPane);
        writer.writeAttribute("height", "100%", null);
        writer.writeAttribute("width", "100%", null);

        writer.startElement("table", tabbedPane);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("height", "100%", null);
        writer.writeAttribute("width", "100%", null);
        writer.startElement("tr", tabbedPane);
        writer.startElement("td", tabbedPane);

        String containerClass = getContainerClass(context, tabbedPane);
        String rolloverContainerClass = Styles.getCSSClass(context,
                tabbedPane, tabbedPane.getRolloverContainerStyle(), StyleGroup.regularStyleGroup(), tabbedPane.getRolloverContainerClass());

        if (paneFirst) {
            encodePane(context, tabbedPane, allSubPanels, containerClass);
        } else {
            encodeTabSet(context, tabbedPane, allSubPanels);
        }
        writer.endElement("td");
        if (!horizontalPlacement) {
            writer.endElement("tr");
            writer.startElement("tr", tabbedPane);
        }
        writer.startElement("td", tabbedPane);
        if (paneFirst) {
            encodeTabSet(context, tabbedPane, allSubPanels);
        } else {
            encodePane(context, tabbedPane, allSubPanels, containerClass);
        }
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");

        encodeScriptsAndStyles(context, tabbedPane, tabPlacement, containerClass, rolloverContainerClass);
        Rendering.encodeClientActions(context, tabbedPane);

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void encodeScriptsAndStyles(
            FacesContext context,
            TabbedPane tabbedPane,
            TabPlacement tabPlacement,
            String containerClass,
            String rolloverContainerClass) throws IOException {
        String borderStyle = tabbedPane.getFrontBorderStyle();
        String fullBorderStyle = null;

        if (borderStyle != null && borderStyle.length() > 0) {
            List borderSideNames = borderNames.getListWithFirst(tabPlacement, 0);
            String borderMask = "border-{0}: {5}; border-{1}: {4}; border-{2}: {4}; border-{3}: {4};";
            fullBorderStyle = MessageFormat.format(borderMask, borderSideNames.get(0),
                    borderSideNames.get(1),
                    borderSideNames.get(2),
                    borderSideNames.get(3),
                    borderStyle,
                    "0px");
        }

        String defaultBorderClass = DEFAULT_BORDER_CLASS_PREFIX + tabPlacement;
        String borderClass = Styles.getCSSClass(context, tabbedPane, fullBorderStyle, defaultBorderClass);

        LoadingMode loadingMode = tabbedPane.getLoadingMode();

        String focusedClass = Styles.getCSSClass(context, tabbedPane,
                tabbedPane.getFocusedStyle(), StyleGroup.selectedStyleGroup(1), tabbedPane.getFocusedClass(), null);

        ScriptBuilder sb = new ScriptBuilder();
        String onselectionchange = tabbedPane.getOnselectionchange();
        sb.initScript(context, tabbedPane, "O$.TabbedPane._init",
                Rendering.getRolloverClass(context, tabbedPane),
                containerClass,
                rolloverContainerClass,
                borderClass,
                loadingMode,
                tabbedPane.isFocusable(),
                focusedClass,
                onselectionchange != null ? new AnonymousFunction(onselectionchange, "event") : null);

        Rendering.renderInitScript(context, sb,
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, TabbedPaneRenderer.class, "multiPage.js"),
                Resources.getInternalURL(context, TabbedPaneRenderer.class, "tabbedPane.js"));

        Styles.renderStyleClasses(context, tabbedPane);
    }

    private void encodeTabSet(FacesContext context, TabbedPane tabbedPane, List<SubPanel> subPanels) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        TabPlacement tabPlacement = tabbedPane.getTabPlacement();
        boolean verticalPlacement = TabPlacement.LEFT.equals(tabPlacement) || TabPlacement.RIGHT.equals(tabPlacement);
        if (verticalPlacement) {
            writeAttribute(writer, "height", "100%");
        }
        TabSet innerTabSet = tabbedPane.getTabSet();
        initInnerTabSet(tabbedPane, innerTabSet, subPanels);
        innerTabSet.encodeAll(context);
    }

    private void initInnerTabSet(TabbedPane tabbedPane, TabSet innerTabSet, List<SubPanel> subPanels) {
        List<UIComponent> tabs = new ArrayList<UIComponent>();
        for (SubPanel item : subPanels) {
            UIComponent captionComponent = item.getCaptionFacet();
            String caption = item.getCaption();
            if (captionComponent != null || caption != null) {
                if (captionComponent == null) {
                    captionComponent = Components.createOutputText(FacesContext.getCurrentInstance(), caption);
                }

                // Note that non-rendered tabs should still be added (though with the "non-rendered" flag) to the TabSet
                // for correct tab indexing.
                // Custom component attribute is used instead of the "rendered" property in order not to interfere with
                // user's specification of the "rendered" property.
                if (!item.isRendered())
                    captionComponent.getAttributes().put(TabSetRenderer.ATTR_CONSIDER_TAB_NON_RENDERED, Boolean.TRUE);
                else
                    captionComponent.getAttributes().remove(TabSetRenderer.ATTR_CONSIDER_TAB_NON_RENDERED);
            }
            tabs.add(captionComponent);
        }
        TabSetItems items = (TabSetItems) innerTabSet.getChildren().get(0);
        items.setValue(tabs);

        innerTabSet.setGapWidth(tabbedPane.getTabGapWidth());

        TabPlacement tabPlacement = tabbedPane.getTabPlacement();
        boolean verticalPlacement = TabPlacement.LEFT.equals(tabPlacement) || TabPlacement.RIGHT.equals(tabPlacement);
        innerTabSet.setStyle(verticalPlacement ? "height: 100%" : "width: 100%");

        innerTabSet.setSelectedIndex(getSelectedIndex(tabbedPane, subPanels));
        innerTabSet.setAlignment(tabbedPane.getTabAlignment());
        innerTabSet.setPlacement(tabPlacement);
        innerTabSet.setTabStyle(tabbedPane.getTabStyle());
        innerTabSet.setRolloverTabStyle(tabbedPane.getRolloverTabStyle());
        innerTabSet.setSelectedTabStyle(tabbedPane.getSelectedTabStyle());
        innerTabSet.setRolloverSelectedTabStyle(tabbedPane.getRolloverSelectedTabStyle());
        innerTabSet.setFocusedTabStyle(tabbedPane.getFocusedTabStyle());

        innerTabSet.setTabClass(tabbedPane.getTabClass());
        innerTabSet.setRolloverTabClass(tabbedPane.getRolloverTabClass());
        innerTabSet.setSelectedTabClass(tabbedPane.getSelectedTabClass());
        innerTabSet.setRolloverSelectedTabClass(tabbedPane.getRolloverSelectedTabClass());

        innerTabSet.setFrontBorderStyle(tabbedPane.getFrontBorderStyle());
        innerTabSet.setBackBorderStyle(tabbedPane.getBackBorderStyle());

        innerTabSet.setEmptySpaceStyle(tabbedPane.getTabEmptySpaceStyle());
        innerTabSet.setEmptySpaceClass(tabbedPane.getTabEmptySpaceClass());

        innerTabSet.setFocusable(tabbedPane.isFocusable());
        innerTabSet.setFocusAreaStyle(tabbedPane.getFocusAreaStyle());
        innerTabSet.setFocusAreaClass(tabbedPane.getFocusAreaClass());
    }

}
