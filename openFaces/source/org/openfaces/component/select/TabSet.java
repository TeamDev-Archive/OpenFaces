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
package org.openfaces.component.select;

import org.openfaces.component.OUIInputBase;
import org.openfaces.event.SelectionChangeEvent;
import org.openfaces.event.SelectionChangeListener;
import org.openfaces.util.ValueBindings;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import java.util.Arrays;
import java.util.List;

/**
 * The TabSet is a component that displays a set of tabs that look like the ones used in
 * the TabbedPane component. As opposed to TabbedPane, the TabSet component is not a
 * container and doesn't display any data when the user switches the tab. Instead it
 * just serves as a selector that can be used to introduce a content switching on pages
 * where TabbedPane can't be used because of special layout or some other reason.
 *
 * @author Andrew Palval
 */
public class TabSet extends OUIInputBase implements TabSelectionHolder {
    public static final String COMPONENT_TYPE = "org.openfaces.TabSet";
    public static final String COMPONENT_FAMILY = "org.openfaces.TabSet";

    //todo: add support for "disabled" attribute inherited from OUIInput
    private Integer selectedIndex;
    private TabAlignment alignment;
    private TabPlacement placement;

    private Integer gapWidth;

    private String tabStyle;
    private String rolloverTabStyle;
    private String selectedTabStyle;
    private String focusedTabStyle;
    private String rolloverSelectedTabStyle;
    private String emptySpaceStyle;

    private String frontBorderStyle;
    private String backBorderStyle;

    private String tabClass;
    private String rolloverTabClass;
    private String selectedTabClass;
    private String focusedTabClass;
    private String rolloverSelectedTabClass;
    private String emptySpaceClass;
    private Boolean focusable;

    private String focusAreaStyle;
    private String focusAreaClass;



    private MethodExpression selectionChangeListener;

    public TabSet() {
        setRendererType("org.openfaces.TabSetRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public int getGapWidth() {
        return ValueBindings.get(this, "gapWidth", gapWidth, 2);
    }

    public void setGapWidth(int gapWidth) {
        this.gapWidth = gapWidth;
    }

    public boolean isFocusable() {
        return ValueBindings.get(this, "focusable", focusable, true);
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }


    public MethodExpression getSelectionChangeListener() {
        return selectionChangeListener;
    }

    public void setSelectionChangeListener(MethodExpression selectionChangeListener) {
        this.selectionChangeListener = selectionChangeListener;
    }

    public int getSelectedIndex() {
        return ValueBindings.get(this, "selectedIndex", selectedIndex, 0);
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public String getRolloverSelectedTabStyle() {
        return ValueBindings.get(this, "rolloverSelectedTabStyle", rolloverSelectedTabStyle);
    }

    public void setRolloverSelectedTabStyle(String rolloverSelectedTabStyle) {
        this.rolloverSelectedTabStyle = rolloverSelectedTabStyle;
    }

    public String getEmptySpaceStyle() {
        return ValueBindings.get(this, "emptySpaceStyle", emptySpaceStyle);
    }

    public void setEmptySpaceStyle(String emptySpaceStyle) {
        this.emptySpaceStyle = emptySpaceStyle;
    }

    public String getTabClass() {
        return ValueBindings.get(this, "tabClass", tabClass);
    }

    public void setTabClass(String styleClass) {
        tabClass = styleClass;
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

    public String getRolloverTabClass() {
        return ValueBindings.get(this, "rolloverTabClass", rolloverTabClass);
    }

    public void setRolloverTabClass(String rolloverClass) {
        rolloverTabClass = rolloverClass;
    }

    public String getSelectedTabClass() {
        return ValueBindings.get(this, "selectedTabClass", selectedTabClass);
    }

    public void setSelectedTabClass(String selectedClass) {
        selectedTabClass = selectedClass;
    }

    public String getFocusedTabClass() {
        return ValueBindings.get(this, "focusedTabClass", focusedTabClass);
    }

    public void setFocusedTabClass(String focusedClass) {
        focusedTabClass = focusedClass;
    }

    public String getRolloverSelectedTabClass() {
        return ValueBindings.get(this, "rolloverSelectedTabClass", rolloverSelectedTabClass);
    }

    public void setRolloverSelectedTabClass(String rolloverSelectedClass) {
        rolloverSelectedTabClass = rolloverSelectedClass;
    }

    public String getEmptySpaceClass() {
        return ValueBindings.get(this, "emptySpaceClass", emptySpaceClass);
    }

    public void setEmptySpaceClass(String emptySpaceClass) {
        this.emptySpaceClass = emptySpaceClass;
    }

    public String getTabStyle() {
        return ValueBindings.get(this, "tabStyle", tabStyle);
    }

    public void setTabStyle(String style) {
        tabStyle = style;
    }

    public String getRolloverTabStyle() {
        return ValueBindings.get(this, "rolloverTabStyle", rolloverTabStyle);
    }

    public void setRolloverTabStyle(String rolloverStyle) {
        rolloverTabStyle = rolloverStyle;
    }

    public String getSelectedTabStyle() {
        return ValueBindings.get(this, "selectedTabStyle", selectedTabStyle);
    }

    public String getFocusedTabStyle() {
        return ValueBindings.get(this, "focusedTabStyle", focusedTabStyle);
    }


    public void setFocusAreaStyle(String focusAreaStyle) {
        this.focusAreaStyle = focusAreaStyle;
    }

    public String getFocusAreaStyle() {
        return ValueBindings.get(this, "focusAreaStyle", focusAreaStyle);
    }

    public void setFocusAreaClass(String focusAreaClass) {
        this.focusAreaClass = focusAreaClass;
    }

    public String getFocusAreaClass() {
        return ValueBindings.get(this, "focusAreaClass", focusAreaClass);
    }

    public void setSelectedTabStyle(String selectedStyle) {
        selectedTabStyle = selectedStyle;
    }

    public void setFocusedTabStyle(String focusedStyle) {
        focusedTabStyle = focusedStyle;
    }

    public TabAlignment getAlignment() {
        return ValueBindings.get(this, "alignment", alignment, TabAlignment.class);
    }

    public void setAlignment(TabAlignment alignment) {
        this.alignment = alignment;
    }

    public TabPlacement getPlacement() {
        return ValueBindings.get(this, "placement", placement, TabPlacement.class);
    }

    public void setPlacement(TabPlacement placement) {
        this.placement = placement;
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

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                selectedIndex,

                gapWidth,

                tabStyle,
                rolloverTabStyle,
                selectedTabStyle,
                focusedTabStyle,
                rolloverSelectedTabStyle,
                emptySpaceStyle,

                frontBorderStyle,
                backBorderStyle,

                tabClass,
                rolloverTabClass,
                selectedTabClass,
                focusedTabClass,
                rolloverSelectedTabClass,
                emptySpaceClass,

                alignment,
                placement,

                focusable,
                focusAreaStyle,
                focusAreaClass,
                saveAttachedState(context, selectionChangeListener),
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        selectedIndex = (Integer) values[i++];

        gapWidth = (Integer) values[i++];

        tabStyle = (String) values[i++];
        rolloverTabStyle = (String) values[i++];
        selectedTabStyle = (String) values[i++];
        focusedTabStyle = (String) values[i++];
        rolloverSelectedTabStyle = (String) values[i++];
        emptySpaceStyle = (String) values[i++];

        frontBorderStyle = (String) values[i++];
        backBorderStyle = (String) values[i++];

        tabClass = (String) values[i++];
        rolloverTabClass = (String) values[i++];
        selectedTabClass = (String) values[i++];
        focusedTabClass = (String) values[i++];
        rolloverSelectedTabClass = (String) values[i++];
        emptySpaceClass = (String) values[i++];

        alignment = (TabAlignment) values[i++];
        placement = (TabPlacement) values[i++];

        focusable = (Boolean) values[i++];
        focusAreaStyle = (String) values[i++];
        focusAreaClass = (String) values[i++];

        selectionChangeListener = (MethodExpression) restoreAttachedState(context, values[i++]);
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        uiInput_broadcast(event);
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

    private void uiInput_broadcast(FacesEvent event) {
        uiComponent_broadcast(event);
        if (!(event instanceof ValueChangeEvent))
            return;
        ValueChangeEvent valueChangeEvent = (ValueChangeEvent) event;

        ValueChangeListener[] valueChangeListeners = getValueChangeListeners();
        for (ValueChangeListener listener : valueChangeListeners) {

            listener.processValueChange(valueChangeEvent);
        }
    }

    private void uiComponent_broadcast(FacesEvent event) {
        if (event == null) throw new NullPointerException("event");
        List<FacesListener> listeners = Arrays.asList(getFacesListeners(FacesListener.class));
        if (listeners == null) return;
        for (FacesListener facesListener : listeners) {
            if (event.isAppropriateListener(facesListener)) {
                event.processListener(facesListener);
            }
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        ValueExpression ve = getValueExpression("value");
        boolean itemStorableInValueExpression = ve != null && !ve.isReadOnly(context.getELContext());
        super.processUpdates(context);

        if (selectedIndex != null && ValueBindings.set(this, "selectedIndex", selectedIndex))
            selectedIndex = null;
        if (itemStorableInValueExpression)
            selectedIndex = null;
    }

    public Integer getLocalSelectedIndex() {
        return selectedIndex;
    }

}
