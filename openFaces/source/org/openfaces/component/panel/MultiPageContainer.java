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
package org.openfaces.component.panel;

import org.openfaces.component.LoadingMode;
import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIPanel;
import org.openfaces.event.SelectionChangeEvent;
import org.openfaces.event.SelectionChangeListener;
import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
@ResourceDependencies({
        @ResourceDependency(name = "jsf.js", library = "javax.faces"),
        @ResourceDependency(name = "default.css", library = "openfaces")
})
public abstract class MultiPageContainer extends OUIPanel {
    private Integer selectedIndex;
    private LoadingMode loadingMode;
    private MethodExpression selectionChangeListener;
    private boolean[] renderedItemFlags;
    private String containerStyle;
    private String containerClass;

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                saveAttachedState(context, selectionChangeListener),
                selectedIndex,
                loadingMode,
                renderedItemFlags,
                containerStyle,
                containerClass
        };
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        selectionChangeListener = (MethodExpression) restoreAttachedState(context, state[i++]);
        selectedIndex = (Integer) state[i++];
        loadingMode = (LoadingMode) state[i++];
        renderedItemFlags = (boolean[]) state[i++];
    }

    public int getSelectedIndex() {
        return ValueBindings.get(this, "selectedIndex", selectedIndex, 0);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public LoadingMode getLoadingMode() {
        return ValueBindings.get(this, "loadingMode", loadingMode, LoadingMode.AJAX_LAZY, LoadingMode.class);
    }

    public void setLoadingMode(LoadingMode loadingMode) {
        this.loadingMode = loadingMode;
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

    public List<SubPanel> getSubPanels(boolean returnNotRenderedItems) {
        List<SubPanel> itemsList = new ArrayList<SubPanel>();

        List<UIComponent> children = getChildren();
        for (UIComponent child : children) {
            if (child instanceof SubPanel) {
                itemsList.add((SubPanel) child);
            } else if (child instanceof SubPanels) {
                SubPanels subPanels = (SubPanels) child;
                Object value = subPanels.getValue();
                if (value == null)
                    continue;
                if (!(value instanceof Collection))
                    throw new FacesException("The 'value' attribute of SubPanels should return an object that " +
                            "implements java.util.Collection interface, but object of the following class was " +
                            "returned: " + value.getClass().getName());

                Collection<SubPanel> items = (Collection<SubPanel>) value;
                itemsList.addAll(items);
                List<UIComponent> subPanelsChildren = subPanels.getChildren();
                Runnable restoreIterators = Components.resetParentIterators(subPanels);
                subPanelsChildren.clear();
                subPanelsChildren.addAll(items);
                if (restoreIterators != null) restoreIterators.run();
            }
        }

        if (returnNotRenderedItems)
            return itemsList;

        List<SubPanel> renderedItemsList = new ArrayList<SubPanel>();
        for (SubPanel paneItem : itemsList) {
            if (paneItem.isRendered()) {
                renderedItemsList.add(paneItem);
            }
        }
        return renderedItemsList;
    }

    public void setRenderedItemFlags(boolean[] renderedItemFlags) {
        this.renderedItemFlags = renderedItemFlags;
    }

    public boolean[] getRenderedItemFlags() {
        if (renderedItemFlags == null) {
            List<SubPanel> subPanels = getSubPanels(true);
            if (subPanels != null) {
                renderedItemFlags = new boolean[subPanels.size()];
            }
        }
        return renderedItemFlags;
    }

    public void setItemRendered(int index, boolean value) {
        getRenderedItemFlags()[index] = value;
    }

    private boolean[] calculateRenderedItemFlags(List<SubPanel> items) {
        boolean[] flags = new boolean[items.size()];
        boolean[] itemFlags = getRenderedItemFlags();
        for (int i = 0; i < items.size(); i++) {
            flags[i] = (items.get(i)).isRendered() && itemFlags != null && itemFlags[i];
        }
        return flags;
    }

    private void processPhaseForItems(FacesContext context, ComponentPhaseProcessor processor) {
        List<SubPanel> items = getSubPanels(true);
        boolean[] renderedItems = calculateRenderedItemFlags(items);
        for (int i = 0; i < items.size(); i++) {
            SubPanel item = items.get(i);
            if (renderedItems[i]) {
                processor.processComponentPhase(context, item);
            } else {
                UIComponent tab = item.getCaptionFacet();
                if (tab != null)
                    processor.processComponentPhase(context, tab);
            }
        }
        List<UIComponent> children = getChildren();
        for (UIComponent child : children) {
            if (child instanceof OUIClientAction) {
                processor.processComponentPhase(context, child);
            }
        }
    }

    @Override
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

    @Override
    public void processValidators(FacesContext context) {
        if (!isRendered()) return;
        processPhaseForItems(context, new ValidatorComponentPhaseProcessor());
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (!isRendered()) return;
        processPhaseForItems(context, new UpdateComponentPhaseProcessor());

        if (selectedIndex != null && ValueBindings.set(this, "selectedIndex", selectedIndex))
            selectedIndex = null;
    }

    @Override
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

    public MethodExpression getSelectionChangeListener() {
        return selectionChangeListener;
    }

    public void setContainerStyle(String containerStyle) {
        this.containerStyle = containerStyle;
    }

    public String getContainerStyle() {
        return ValueBindings.get(this, "containerStyle", containerStyle);
    }

    public String getContainerClass() {
        return ValueBindings.get(this, "containerClass", containerClass);
    }

    public void setContainerClass(String containerClass) {
        this.containerClass = containerClass;
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
