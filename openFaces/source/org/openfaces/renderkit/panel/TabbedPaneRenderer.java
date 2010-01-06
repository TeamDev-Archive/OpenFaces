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

import org.openfaces.component.LoadingMode;
import org.openfaces.component.panel.TabbedPane;
import org.openfaces.component.panel.TabbedPaneItem;
import org.openfaces.component.select.TabPlacement;
import org.openfaces.component.select.TabSet;
import org.openfaces.component.select.TabSetItems;
import org.openfaces.event.SelectionChangeEvent;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.renderkit.select.BaseTabSetRenderer;
import org.openfaces.renderkit.select.TabSetRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.StyleGroup;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Andrew Palval
 */
public class TabbedPaneRenderer extends BaseTabSetRenderer implements AjaxPortionRenderer {
    // todo: TabbedPaneRenderer shouldn't extend BaseTabSetRenderer. It can only aggregate TabSet and it shouldn't know of any of TabSet's innards.

    private static final String JS_SCRIPT_URL = "tabbedPane.js";
    private static final String TAB_SET_SUFFIX = "tabSet";
    public static final String PANE_SUFFIX = RenderingUtil.CLIENT_ID_SUFFIX_SEPARATOR + "pane";
    private static final String PAGE_PORTION_NAME_PREFIX = "page:";
    private static final String DEFAULT_BORDER_CLASS_PREFIX = "o_tabbedpane_border_";

    protected void changeTabIndex(UIComponent component, int selectedIndex) {
        TabbedPane tabbedPane = (TabbedPane) component;
        int oldSelectedIndex = tabbedPane.getSelectedIndex();
        tabbedPane.setSelectedIndex(selectedIndex);
        if (oldSelectedIndex != selectedIndex) {
            tabbedPane.queueEvent(new SelectionChangeEvent(tabbedPane, oldSelectedIndex, selectedIndex));
        }
    }

    protected String getSelectionHiddenFieldSuffix() {
        return RenderingUtil.SERVER_ID_SUFFIX_SEPARATOR + TAB_SET_SUFFIX + SELECTED_INDEX_SUFFIX;
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

        List<TabbedPaneItem> allItems = tabbedPane.getTabbedPaneItems(true);

        // imlementation note for one who is going to remove outer table rendering
        // need to check, that style="padding: 10px;" (10px for example) do not breaks border under IE
        // JSFC-754 TabbedPane border is not solid if set paddings
        writer.startElement("table", tabbedPane);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.writeAttribute("id", tabbedPane.getClientId(context), "id");

        RenderingUtil.writeComponentClassAttribute(writer, tabbedPane);

        RenderingUtil.writeStandardEvents(writer, tabbedPane);

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
        String rolloverContainerClass = StyleUtil.getCSSClass(context,
                tabbedPane, tabbedPane.getRolloverContainerStyle(), StyleGroup.regularStyleGroup(), tabbedPane.getRolloverContainerClass());

        if (paneFirst) {
            encodePane(context, tabbedPane, allItems, containerClass);
        } else {
            encodeTabSet(context, tabbedPane, allItems);
        }
        writer.endElement("td");
        if (!horizontalPlacement) {
            writer.endElement("tr");
            writer.startElement("tr", tabbedPane);
        }
        writer.startElement("td", tabbedPane);
        if (paneFirst) {
            encodeTabSet(context, tabbedPane, allItems);
        } else {
            encodePane(context, tabbedPane, allItems, containerClass);
        }
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");

        encodeScriptsAndStyles(context, tabbedPane, tabPlacement, containerClass, rolloverContainerClass);

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
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
        String borderClass = StyleUtil.getCSSClass(context, tabbedPane, fullBorderStyle, defaultBorderClass);

        LoadingMode loadingMode = tabbedPane.getLoadingMode();

        String focusedClass = StyleUtil.getCSSClass(context, tabbedPane,
                tabbedPane.getFocusedStyle(), StyleGroup.selectedStyleGroup(1), tabbedPane.getFocusedClass(), null);

        ScriptBuilder sb = new ScriptBuilder();
        String onselectionchange = tabbedPane.getOnselectionchange();
        sb.initScript(context, tabbedPane, "O$.TabbedPane._init",
                RenderingUtil.getRolloverClass(context, tabbedPane),
                containerClass,
                rolloverContainerClass,
                borderClass,
                loadingMode,
                tabbedPane.isFocusable(),
                focusedClass,
                onselectionchange != null ? new AnonymousFunction(onselectionchange, "event") : null);

        RenderingUtil.renderInitScript(context, sb,
                ResourceUtil.getUtilJsURL(context),
                ResourceUtil.getInternalResourceURL(context, TabbedPaneRenderer.class, JS_SCRIPT_URL));

        StyleUtil.renderStyleClasses(context, tabbedPane);
    }

    private String getContainerClass(FacesContext context, TabbedPane tabbedPane) {
        String containerClass = StyleUtil.getCSSClass(context,
                tabbedPane, tabbedPane.getContainerStyle(), tabbedPane.getContainerClass());
        containerClass = StyleUtil.mergeClassNames(containerClass, "o_tabbedpane_container");
        return containerClass;
    }

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component, String portionName, JSONObject jsonParam) throws IOException {
        if (!portionName.startsWith(PAGE_PORTION_NAME_PREFIX))
            throw new IllegalArgumentException("Unknown portionName: " + portionName);
        String pageIndexStr = portionName.substring(PAGE_PORTION_NAME_PREFIX.length());
        int absolutePageIndex = Integer.parseInt(pageIndexStr);
        TabbedPane tabbedPane = (TabbedPane) component;
        List tabbedPaneItems = tabbedPane.getTabbedPaneItems(true);
        encodePageContent(context, tabbedPane, tabbedPaneItems, absolutePageIndex, false, null);
        tabbedPane.setItemRendered(absolutePageIndex, true);
        return null;
    }

    private void encodePane(
            FacesContext context,
            TabbedPane tabbedPane,
            List tabbedPaneItems,
            String containerClass) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.writeAttribute("width", "100%", null);
        writer.writeAttribute("style", "vertical-align: top;", null); // required for correct content alignment with left/right tab placements under strict mode

        int allItemCount = tabbedPaneItems.size();
        tabbedPane.setRenderedItemFlags(new boolean[allItemCount]);

        if (allItemCount == 0)
            return;

        int selectedTabIndex = getSelectedIndex(tabbedPane, tabbedPaneItems);
        if (selectedTabIndex == -1)
            return; // if there are no rendered items

        LoadingMode loadingMode = tabbedPane.getLoadingMode();
        if (loadingMode.equals(LoadingMode.SERVER)) {
            encodePageContent(context, tabbedPane, tabbedPaneItems, selectedTabIndex, true, containerClass);
            tabbedPane.setItemRendered(selectedTabIndex, true);
        } else if (loadingMode.equals(LoadingMode.CLIENT)) {
            for (int i = 0; i < allItemCount; i++) {
                boolean thisPageVisible = selectedTabIndex == i;
                TabbedPaneItem tabbedPaneItem = (TabbedPaneItem) tabbedPaneItems.get(i);
                if (tabbedPaneItem.isRendered())
                    encodePageContent(context, tabbedPane, tabbedPaneItems, i, thisPageVisible, containerClass);
                tabbedPane.setItemRendered(i, true);
            }
        } else if (loadingMode.equals(LoadingMode.AJAX_LAZY)) {
            encodePageContent(context, tabbedPane, tabbedPaneItems, selectedTabIndex, true, containerClass);
            tabbedPane.setItemRendered(selectedTabIndex, true);
        } else if (loadingMode.equals(LoadingMode.AJAX_ALWAYS)) {
            encodePageContent(context, tabbedPane, tabbedPaneItems, selectedTabIndex, true, containerClass);
            tabbedPane.setItemRendered(selectedTabIndex, true);
        } else
            throw new IllegalStateException("Invalid loading mode: " + loadingMode);
    }

    /**
     * @return value of TabbedPane's selectedIndex property. If it points to a non-existing or non-rendered tab then the
     *         index of the first rendered tab is returned. If there are no rendered tabs then -1 is returned.
     */
    private int getSelectedIndex(TabbedPane tabbedPane, List tabbedPaneItems) {
        int selectedTabIndex = tabbedPane.getSelectedIndex();
        int allItemCount = tabbedPaneItems.size();
        if (selectedTabIndex < 0 || selectedTabIndex >= allItemCount)
            selectedTabIndex = 0;
        TabbedPaneItem selectedItem = (TabbedPaneItem) tabbedPaneItems.get(selectedTabIndex);
        if (!selectedItem.isRendered()) {
            selectedTabIndex = -1;
            for (int i = 0; i < allItemCount; i++) {
                TabbedPaneItem tabbedPaneItem = (TabbedPaneItem) tabbedPaneItems.get(i);
                if (tabbedPaneItem.isRendered()) {
                    selectedTabIndex = i;
                    break;
                }
            }
        }
        return selectedTabIndex;
    }

    private void encodePageContent(
            FacesContext context,
            TabbedPane tabbedPane,
            List allTabbedPaneItems,
            int absolutePageIndex,
            boolean initiallyVisible,
            String containerClass
    ) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        // imlementation note
        // table tag is used as pane content wrapper
        // instead of div tag because there is Firefox bugs for height 100% in div (from time to time) JSFC-746.
        writer.startElement("table", tabbedPane);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        String clientId = tabbedPane.getClientId(context);
        writer.writeAttribute("id", clientId + PANE_SUFFIX + absolutePageIndex, null);
        if (!initiallyVisible) {
            writer.writeAttribute("style", "display: none;", null);
        }
        writer.startElement("tr", tabbedPane);
        writer.startElement("td", tabbedPane);

        writeAttribute(writer, "class", containerClass);

        TabbedPaneItem item = (TabbedPaneItem) allTabbedPaneItems.get(absolutePageIndex);
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

    private void encodeTabSet(FacesContext context, TabbedPane tabbedPane, List<TabbedPaneItem> tabbedPaneItems) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        TabPlacement tabPlacement = tabbedPane.getTabPlacement();
        boolean verticalPlacement = TabPlacement.LEFT.equals(tabPlacement) || TabPlacement.RIGHT.equals(tabPlacement);
        if (verticalPlacement) {
            writeAttribute(writer, "height", "100%");
        }
        TabSet innerTabSet = tabbedPane.getTabSet();
        initInnerTabSet(tabbedPane, innerTabSet, tabbedPaneItems);
        innerTabSet.encodeAll(context);
    }

    private void initInnerTabSet(TabbedPane tabbedPane, TabSet innerTabSet, List<TabbedPaneItem> tabbedPaneItems) {
        List<UIComponent> tabs = new ArrayList<UIComponent>();
        for (TabbedPaneItem item : tabbedPaneItems) {
            UIComponent tabContentComponent = item.getTab();
            if (tabContentComponent != null) {
                // Note that non-rendered tabs should still be added (though with the "non-rendered" flag) to the TabSet for
                // correct tab indexing.
                // Custom component attribute is used instead of the "rendered" property in order not to interfere with
                // user's specification of the "rendered" property.
                if (!item.isRendered())
                    tabContentComponent.getAttributes().put(TabSetRenderer.ATTR_CONSIDER_TAB_NON_RENDERED, Boolean.TRUE);
                else
                    tabContentComponent.getAttributes().remove(TabSetRenderer.ATTR_CONSIDER_TAB_NON_RENDERED);
            }
            tabs.add(tabContentComponent);
        }
        TabSetItems items = (TabSetItems) innerTabSet.getChildren().get(0);
        items.setValue(tabs);

        innerTabSet.setGapWidth(tabbedPane.getTabGapWidth());

        TabPlacement tabPlacement = tabbedPane.getTabPlacement();
        boolean verticalPlacement = TabPlacement.LEFT.equals(tabPlacement) || TabPlacement.RIGHT.equals(tabPlacement);
        innerTabSet.setStyle(verticalPlacement ? "height: 100%" : "width: 100%");

        innerTabSet.setSelectedIndex(getSelectedIndex(tabbedPane, tabbedPaneItems));
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
