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
package org.openfaces.component.panel;

import org.openfaces.component.CompoundComponent;
import org.openfaces.component.LoadingMode;
import org.openfaces.component.OUIPanel;
import org.openfaces.util.ValueBindings;
import org.openfaces.component.select.TabAlignment;
import org.openfaces.component.select.TabPlacement;
import org.openfaces.component.select.TabSelectionHolder;
import org.openfaces.component.select.TabSet;
import org.openfaces.component.select.TabSetItems;
import org.openfaces.event.SelectionChangeEvent;
import org.openfaces.event.SelectionChangeListener;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.RenderingUtil;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * The TabbedPane component is a container that consists of several sub-containers called
 * pages and allows the user to switch between these pages using a set of tabs. It provides
 * flexibility in configuring the tabs and the page content within them and offers several
 * ways for content loading. A variety of style options lets you customize the appearance of
 * the entire TabbedPane component and its individual elements.
 * 
 * @author Andrew Palval
 */
public class TabbedPane extends OUIPanel implements TabSelectionHolder, CompoundComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.TabbedPane";
    public static final String COMPONENT_FAMILY = "org.openfaces.TabbedPane";

    private static final String TAB_SET_SUFFIX = "tabSet";

    private String containerStyle;
    private String rolloverContainerStyle;

    private String containerClass;
    private String rolloverContainerClass;

    // TasSet properties START -------------------------
    private Integer selectedIndex;
    private TabAlignment tabAlignment;
    private TabPlacement tabPlacement;

    private Integer tabGapWidth;

    private String tabStyle;
    private String rolloverTabStyle;
    private String selectedTabStyle;
    private String focusedTabStyle;
    private String rolloverSelectedTabStyle;
    private String tabEmptySpaceStyle;

    private String frontBorderStyle;
    private String backBorderStyle;

    private String tabClass;
    private String rolloverTabClass;
    private String selectedTabClass;
    private String focusedTabClass;
    private String rolloverSelectedTabClass;
    private String tabEmptySpaceClass;
    //--------------------------------------------------
    private LoadingMode loadingMode;
    private String onselectionchange;
    private MethodExpression selectionChangeListener;
    // TabSet properties END ----------------------------

    private Boolean focusable;
    private String focusAreaClass;
    private String focusAreaStyle;
    private String focusedClass;
    private String focusedStyle;
    private boolean[] renderedItemFlags;

    public TabbedPane() {
        setRendererType("org.openfaces.TabbedPaneRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }


    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public boolean isFocusable() {
        return ValueBindings.get(this, "focusable", focusable, true);
    }

    public String getFocusAreaStyle() {
        return ValueBindings.get(this, "focusAreaStyle", focusAreaStyle);
    }

    public void setFocusAreaStyle(String focusAreaStyle) {
        this.focusAreaStyle = focusAreaStyle;
    }

    public String getFocusAreaClass() {
        return ValueBindings.get(this, "focusAreaClass", focusAreaClass);
    }

    public void setFocusAreaClass(String focusAreaClass) {
        this.focusAreaClass = focusAreaClass;
    }

    public void setFocusedClass(String focusedClass) {
        this.focusedClass = focusedClass;
    }

    public String getFocusedStyle() {
        return ValueBindings.get(this, "focusedStyle", focusedStyle);
    }

    public void setFocusedStyle(String focusedStyle) {
        this.focusedStyle = focusedStyle;
    }

    public String getFocusedClass() {
        return ValueBindings.get(this, "focusedClass", focusedClass);
    }

    public String getTabEmptySpaceStyle() {
        return ValueBindings.get(this, "tabEmptySpaceStyle", tabEmptySpaceStyle);
    }

    public void setTabEmptySpaceStyle(String tabEmptySpaceStyle) {
        this.tabEmptySpaceStyle = tabEmptySpaceStyle;
    }

    public String getTabEmptySpaceClass() {
        return ValueBindings.get(this, "tabEmptySpaceClass", tabEmptySpaceClass);
    }

    public void setTabEmptySpaceClass(String tabEmptySpaceClass) {
        this.tabEmptySpaceClass = tabEmptySpaceClass;
    }

    public int getTabGapWidth() {
        return ValueBindings.get(this, "tabGapWidth", tabGapWidth, 2);
    }

    public void setTabGapWidth(int tabGapWidth) {
        this.tabGapWidth = tabGapWidth;
    }

    public void setContainerStyle(String containerStyle) {
        this.containerStyle = containerStyle;
    }

    public String getContainerStyle() {
        return ValueBindings.get(this, "containerStyle", containerStyle);
    }

    public String getRolloverContainerStyle() {
        return ValueBindings.get(this, "rolloverContainerStyle", rolloverContainerStyle);
    }

    public void setRolloverContainerStyle(String rolloverContainerStyle) {
        this.rolloverContainerStyle = rolloverContainerStyle;
    }

    public String getContainerClass() {
        return ValueBindings.get(this, "containerClass", containerClass);
    }

    public void setContainerClass(String containerClass) {
        this.containerClass = containerClass;
    }

    public String getRolloverContainerClass() {
        return ValueBindings.get(this, "rolloverContainerClass", rolloverContainerClass);
    }

    public void setRolloverContainerClass(String rolloverContainerClass) {
        this.rolloverContainerClass = rolloverContainerClass;
    }

    public int getSelectedIndex() {
        return ValueBindings.get(this, "selectedIndex", selectedIndex, 0);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public TabAlignment getTabAlignment() {
        return ValueBindings.get(this, "tabAlignment", tabAlignment, TabAlignment.class);
    }

    public void setTabAlignment(TabAlignment tabAlignment) {
        this.tabAlignment = tabAlignment;
    }

    public TabPlacement getTabPlacement() {
        return ValueBindings.get(this, "tabPlacement", tabPlacement, TabPlacement.class);
    }

    public void setTabPlacement(TabPlacement tabPlacement) {
        this.tabPlacement = tabPlacement;
    }

    public String getTabStyle() {
        return ValueBindings.get(this, "tabStyle", tabStyle);
    }

    public void setTabStyle(String tabStyle) {
        this.tabStyle = tabStyle;
    }

    public String getRolloverTabStyle() {
        return ValueBindings.get(this, "rolloverTabStyle", rolloverTabStyle);
    }

    public void setRolloverTabStyle(String rolloverTabStyle) {
        this.rolloverTabStyle = rolloverTabStyle;
    }

    public String getSelectedTabStyle() {
        return ValueBindings.get(this, "selectedTabStyle", selectedTabStyle);
    }

    public void setSelectedTabStyle(String selectedTabStyle) {
        this.selectedTabStyle = selectedTabStyle;
    }

    public String getFocusedTabStyle() {
        return ValueBindings.get(this, "focusedTabStyle", focusedTabStyle);
    }

    public void setFocusedTabStyle(String focusedTabStyle) {
        this.focusedTabStyle = focusedTabStyle;
    }

    public String getRolloverSelectedTabStyle() {
        return ValueBindings.get(this, "rolloverSelectedTabStyle", rolloverSelectedTabStyle);
    }

    public void setRolloverSelectedTabStyle(String rolloverSelectedTabStyle) {
        this.rolloverSelectedTabStyle = rolloverSelectedTabStyle;
    }

    public String getFrontBorderStyle() {
        return ValueBindings.get(this, "frontBorderStyle", frontBorderStyle);
    }

    public void setFrontBorderStyle(String frontBorderStyle) {
        this.frontBorderStyle = frontBorderStyle;
    }

    public String getBackBorderStyle() {
        return ValueBindings.get(this, "backBorderStyle", backBorderStyle);
    }

    public void setBackBorderStyle(String backBorderStyle) {
        this.backBorderStyle = backBorderStyle;
    }

    public String getTabClass() {
        return ValueBindings.get(this, "tabClass", tabClass);
    }

    public void setTabClass(String tabClass) {
        this.tabClass = tabClass;
    }

    public String getRolloverTabClass() {
        return ValueBindings.get(this, "rolloverTabClass", rolloverTabClass);
    }

    public void setRolloverTabClass(String rolloverTabClass) {
        this.rolloverTabClass = rolloverTabClass;
    }

    public String getSelectedTabClass() {
        return ValueBindings.get(this, "selectedTabClass", selectedTabClass);
    }

    public void setSelectedTabClass(String selectedTabClass) {
        this.selectedTabClass = selectedTabClass;
    }

    public String getFocusedTabClass() {
        return ValueBindings.get(this, "focusedTabClass", focusedTabClass);
    }

    public void setFocusedTabClass(String focusedTabClass) {
        this.focusedTabClass = focusedTabClass;
    }

    public String getRolloverSelectedTabClass() {
        return ValueBindings.get(this, "rolloverSelectedTabClass", rolloverSelectedTabClass);
    }

    public void setRolloverSelectedTabClass(String rolloverSelectedTabClass) {
        this.rolloverSelectedTabClass = rolloverSelectedTabClass;
    }

    public LoadingMode getLoadingMode() {
        return ValueBindings.get(this, "loadingMode", loadingMode, LoadingMode.class);
    }

    public void setLoadingMode(LoadingMode loadingMode) {
        this.loadingMode = loadingMode;
    }

    public String getOnselectionchange() {
        return ValueBindings.get(this, "onselectionchange", onselectionchange);
    }

    public void setOnselectionchange(String onselectionchange) {
        this.onselectionchange = onselectionchange;
    }

    public String getOnchange() {
        return getOnselectionchange();
    }

    public void setOnchange(String onselectionchange) {
        setOnselectionchange(onselectionchange);
    }

    public MethodExpression getSelectionChangeListener() {
        return selectionChangeListener;
    }

    public void setSelectionChangeListener(MethodExpression selectionChangeListener) {
        this.selectionChangeListener = selectionChangeListener;
    }

    public void addSelectionListener(SelectionChangeListener changeListener) {
        addFacesListener(changeListener);
    }

    public SelectionChangeListener[] getSelectionListeners() {
        return (SelectionChangeListener[]) getFacesListeners(SelectionChangeListener.class);
    }

    public void removeSelectionListener(SelectionChangeListener changeListener) {
        removeFacesListener(changeListener);
    }

    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

                containerStyle,
                rolloverContainerStyle,
                containerClass,
                rolloverContainerClass,

                selectedIndex,
                renderedItemFlags,
                tabAlignment,
                tabPlacement,
                tabGapWidth,
                tabStyle,
                rolloverTabStyle,
                selectedTabStyle,
                focusedTabStyle,
                rolloverSelectedTabStyle,
                frontBorderStyle,
                backBorderStyle,
                tabEmptySpaceStyle,
                tabClass,
                rolloverTabClass,
                selectedTabClass,
                focusedTabClass,
                rolloverSelectedTabClass,
                tabEmptySpaceClass,

                loadingMode,
                onselectionchange,

                focusable,
                focusAreaStyle,
                focusAreaClass,

                saveAttachedState(context, selectionChangeListener),
        };
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        containerStyle = (String) values[i++];
        rolloverContainerStyle = (String) values[i++];
        containerClass = (String) values[i++];
        rolloverContainerClass = (String) values[i++];

        selectedIndex = (Integer) values[i++];
        renderedItemFlags = (boolean[]) values[i++];
        tabAlignment = (TabAlignment) values[i++];
        tabPlacement = (TabPlacement) values[i++];
        tabGapWidth = (Integer) values[i++];
        tabStyle = (String) values[i++];
        rolloverTabStyle = (String) values[i++];
        selectedTabStyle = (String) values[i++];
        focusedTabStyle = (String) values[i++];
        rolloverSelectedTabStyle = (String) values[i++];
        frontBorderStyle = (String) values[i++];
        backBorderStyle = (String) values[i++];
        tabEmptySpaceStyle = (String) values[i++];
        tabClass = (String) values[i++];
        rolloverTabClass = (String) values[i++];
        focusedTabClass = (String) values[i++];
        selectedTabClass = (String) values[i++];
        rolloverSelectedTabClass = (String) values[i++];
        tabEmptySpaceClass = (String) values[i++];

        loadingMode = (LoadingMode) values[i++];
        onselectionchange = (String) values[i++];

        focusable = (Boolean) values[i++];
        focusAreaStyle = (String) values[i++];
        focusAreaClass = (String) values[i++];

        selectionChangeListener = (MethodExpression) restoreAttachedState(context, values[i++]);
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        if (selectionChangeListener != null && event instanceof SelectionChangeEvent) {
            try {
                ELContext elContext = getFacesContext().getELContext();
                selectionChangeListener.invoke(elContext, new Object[]{event});
            } catch (FacesException e) {
                if (e.getCause() != null && e.getCause() instanceof AbortProcessingException)
                    throw (AbortProcessingException) e.getCause();
                else
                    throw e;
            }
        }
    }

    public List<TabbedPaneItem> getTabbedPaneItems(boolean returnNotRenderedItems) {
        List<TabbedPaneItem> itemsList = new ArrayList<TabbedPaneItem>();

        List<UIComponent> children = getChildren();
        for (UIComponent child : children) {
            if (child instanceof TabbedPaneItem) {
                itemsList.add((TabbedPaneItem) child);
            } else if (child instanceof TabbedPaneItems) {
                TabbedPaneItems tabbedPaneItems = (TabbedPaneItems) child;
                Object value = tabbedPaneItems.getValue();
                if (value == null)
                    continue;
                if (!(value instanceof Collection))
                    throw new FacesException("The 'value' attribute of TabbedPaneItems should return an object that implements java.util.Collection intface, but object of the following class was returned: " + value.getClass().getName());

                Collection<TabbedPaneItem> items = (Collection<TabbedPaneItem>) value;
                itemsList.addAll(items);
                List<UIComponent> tabbedPaneItemsChildren = tabbedPaneItems.getChildren();
                tabbedPaneItemsChildren.clear();
                tabbedPaneItemsChildren.addAll(items);
            }
        }

        if (returnNotRenderedItems)
            return itemsList;

        List<TabbedPaneItem> renderedItemsList = new ArrayList<TabbedPaneItem>();
        for (TabbedPaneItem paneItem : itemsList) {
            if (paneItem.isRendered()) {
                renderedItemsList.add(paneItem);
            }
        }
        return renderedItemsList;
    }

    public void processRestoreState(FacesContext context, Object state) {
        Object ajaxState = AjaxUtil.retrieveAjaxStateObject(context, this);
        super.processRestoreState(context, ajaxState != null ? ajaxState : state);
    }

    public void setRenderedItemFlags(boolean[] renderedItemFlags) {
        this.renderedItemFlags = renderedItemFlags;
    }

    public boolean[] getRenderedItemFlags() {
        return renderedItemFlags;
    }

    public void setItemRendered(int index, boolean value) {
        renderedItemFlags[index] = value;
    }

    private boolean[] calculateRenderedItemFlags(List<TabbedPaneItem> items) {
        boolean[] flags = new boolean[items.size()];
        for (int i = 0; i < items.size(); i++) {
            flags[i] = (items.get(i)).isRendered() && getRenderedItemFlags()[i];
        }
        return flags;
    }

    private void processPhaseForItems(FacesContext context, ComponentPhaseProcessor processor) {
        List<TabbedPaneItem> items = getTabbedPaneItems(true);
        boolean[] renderedItems = calculateRenderedItemFlags(items);
        for (int i = 0; i < items.size(); i++) {
            TabbedPaneItem item = items.get(i);
            if (renderedItems[i]) {
                processor.processComponentPhase(context, item);
            } else {
                UIComponent tab = item.getTab();
                if (tab != null)
                    processor.processComponentPhase(context, tab);
            }
        }
    }

    public void processDecodes(FacesContext context) {
        if (!isRendered()) return;
        processPhaseForItems(context, new DecodesComponentPhaseProcessor());
        try {
            decode(context);
        }
        catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        }
    }

    public void processValidators(FacesContext context) {
        if (!isRendered()) return;
        processPhaseForItems(context, new ValidatorComponentPhaseProcessor());
    }

    public void processUpdates(FacesContext context) {
        if (!isRendered()) return;
        processPhaseForItems(context, new UpdateComponentPhaseProcessor());

        if (selectedIndex != null && ValueBindings.set(this, "selectedIndex", selectedIndex))
            selectedIndex = null;
    }

    public void createSubComponents(FacesContext context) {
        UIComponent tabSet = RenderingUtil.createChildComponent(context, this, TabSet.COMPONENT_TYPE, TAB_SET_SUFFIX);
        TabSetItems items = new TabSetItems();
        tabSet.getChildren().add(items);
    }

    public TabSet getTabSet() {
        return (TabSet) RenderingUtil.getChildBySuffix(this, TAB_SET_SUFFIX);
    }

    private interface ComponentPhaseProcessor {
        void processComponentPhase(FacesContext context, UIComponent component);
    }

    private static class DecodesComponentPhaseProcessor implements ComponentPhaseProcessor {
        public void processComponentPhase(FacesContext context, UIComponent component) {
            component.processDecodes(context);
        }
    }

    private static class ValidatorComponentPhaseProcessor implements ComponentPhaseProcessor {
        public void processComponentPhase(FacesContext context, UIComponent component) {
            component.processValidators(context);
        }
    }

    private static class UpdateComponentPhaseProcessor implements ComponentPhaseProcessor {
        public void processComponentPhase(FacesContext context, UIComponent component) {
            component.processUpdates(context);
        }
    }
}
