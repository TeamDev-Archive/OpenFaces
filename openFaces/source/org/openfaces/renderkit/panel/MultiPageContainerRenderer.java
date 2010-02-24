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
import org.openfaces.component.panel.MultiPageContainer;
import org.openfaces.component.panel.SubPanel;
import org.openfaces.event.SelectionChangeEvent;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.select.BaseTabSetRenderer;
import org.openfaces.util.Rendering;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public abstract class MultiPageContainerRenderer extends BaseTabSetRenderer implements AjaxPortionRenderer {
    // todo: TabbedPaneRenderer/MultiPageContainerRenderer shouldn't extend BaseTabSetRenderer.
    // todo: TabbedPane can only aggregate TabSet but not extend it or know of any of TabSet's innards.

    public static final String PANE_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "pane";
    private static final String PAGE_PORTION_NAME_PREFIX = "page:";

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component, String portionName, JSONObject jsonParam) throws IOException {
        if (!portionName.startsWith(PAGE_PORTION_NAME_PREFIX))
            throw new IllegalArgumentException("Unknown portionName: " + portionName);
        String pageIndexStr = portionName.substring(PAGE_PORTION_NAME_PREFIX.length());
        int absolutePageIndex = Integer.parseInt(pageIndexStr);
        MultiPageContainer container = (MultiPageContainer) component;
        List subPanels = container.getSubPanels(true);
        encodePageContent(context, container, subPanels, absolutePageIndex, false, null);
        container.setItemRendered(absolutePageIndex, true);
        return null;
    }

    protected void encodePageContent(
            FacesContext context,
            MultiPageContainer container,
            List allSubPanels,
            int absolutePageIndex,
            boolean initiallyVisible,
            String containerClass
    ) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        // implementation note
        // table tag is used as pane content wrapper
        // instead of div tag because there is Firefox bugs for height 100% in div (from time to time) JSFC-746.
        writer.startElement("table", container);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        String clientId = container.getClientId(context);
        writer.writeAttribute("id", clientId + PANE_SUFFIX + absolutePageIndex, null);
        if (!initiallyVisible) {
            writer.writeAttribute("style", "display: none;", null);
        }
        writer.startElement("tr", container);
        writer.startElement("td", container);

        writeAttribute(writer, "class", containerClass);

        SubPanel item = (SubPanel) allSubPanels.get(absolutePageIndex);
        Collection<UIComponent> paneContent = item.getChildren();
        encodeComponents(context, paneContent);

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void encodeComponents(FacesContext context, Collection<UIComponent> components) throws IOException {
        for (UIComponent component : components) {
            component.encodeAll(context);
        }
    }

    protected void changeTabIndex(UIComponent component, int selectedIndex) {
        MultiPageContainer container = (MultiPageContainer) component;
        int oldSelectedIndex = container.getSelectedIndex();
        container.setSelectedIndex(selectedIndex);
        if (oldSelectedIndex != selectedIndex) {
            container.queueEvent(new SelectionChangeEvent(container, oldSelectedIndex, selectedIndex));
        }
    }

    protected void encodePane(
            FacesContext context,
            MultiPageContainer container,
            List<SubPanel> subPanels,
            String containerClass) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.writeAttribute("width", "100%", null);
        writer.writeAttribute("style", "vertical-align: top;", null); // required for correct content alignment with left/right tab placements under strict mode

        int allItemCount = subPanels.size();
        container.setRenderedItemFlags(new boolean[allItemCount]);

        if (allItemCount == 0)
            return;

        int selectedTabIndex = getSelectedIndex(container, subPanels);
        if (selectedTabIndex == -1)
            return; // if there are no rendered items

        LoadingMode loadingMode = container.getLoadingMode();
        if (loadingMode.equals(LoadingMode.SERVER)) {
            encodePageContent(context, container, subPanels, selectedTabIndex, true, containerClass);
            container.setItemRendered(selectedTabIndex, true);
        } else if (loadingMode.equals(LoadingMode.CLIENT)) {
            for (int i = 0; i < allItemCount; i++) {
                boolean thisPageVisible = selectedTabIndex == i;
                SubPanel subPanel = subPanels.get(i);
                if (subPanel.isRendered())
                    encodePageContent(context, container, subPanels, i, thisPageVisible, containerClass);
                container.setItemRendered(i, true);
            }
        } else if (loadingMode.equals(LoadingMode.AJAX_LAZY)) {
            encodePageContent(context, container, subPanels, selectedTabIndex, true, containerClass);
            container.setItemRendered(selectedTabIndex, true);
        } else if (loadingMode.equals(LoadingMode.AJAX_ALWAYS)) {
            encodePageContent(context, container, subPanels, selectedTabIndex, true, containerClass);
            container.setItemRendered(selectedTabIndex, true);
        } else
            throw new IllegalStateException("Invalid loading mode: " + loadingMode);
    }

    /**
     * @return value of container's selectedIndex property. If it points to a non-existing or non-rendered tab then the
     *         index of the first rendered tab is returned. If there are no rendered tabs then -1 is returned.
     */
    protected int getSelectedIndex(MultiPageContainer container, List<SubPanel> subPanels) {
        int selectedTabIndex = container.getSelectedIndex();
        int allItemCount = subPanels.size();
        if (selectedTabIndex < 0 || selectedTabIndex >= allItemCount)
            selectedTabIndex = 0;
        SubPanel selectedItem = subPanels.get(selectedTabIndex);
        if (!selectedItem.isRendered()) {
            selectedTabIndex = -1;
            for (int i = 0; i < allItemCount; i++) {
                SubPanel subPanel = subPanels.get(i);
                if (subPanel.isRendered()) {
                    selectedTabIndex = i;
                    break;
                }
            }
        }
        return selectedTabIndex;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    protected String getContainerClass(FacesContext context, MultiPageContainer container) {
        String containerClass = Styles.getCSSClass(context,
                container, container.getContainerStyle(), container.getContainerClass());
        containerClass = Styles.mergeClassNames(containerClass, "o_tabbedpane_container");
        return containerClass;
    }
}
