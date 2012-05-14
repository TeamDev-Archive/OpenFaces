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
package org.openfaces.component.table;

import org.openfaces.component.ComponentConfigurator;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnResizing extends UIComponentBase implements ComponentConfigurator {
    public static final String COMPONENT_TYPE = "org.openfaces.ColumnResizing";
    public static final String COMPONENT_FAMILY = "org.openfaces.ColumnResizing";

    private Boolean enabled;
    private Boolean retainTableWidth;
    private String minColWidth;
    private String resizeHandleWidth;
    private ColumnResizingState resizingState;

    private Boolean autoSaveState;

    public ColumnResizing() {
        setRendererType("org.openfaces.ColumnResizingRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                enabled,
                retainTableWidth,
                minColWidth,
                resizeHandleWidth,
                resizingState,
                autoSaveState
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        enabled = (Boolean) state[i++];
        retainTableWidth = (Boolean) state[i++];
        minColWidth = (String) state[i++];
        resizeHandleWidth = (String) state[i++];
        resizingState = (ColumnResizingState) state[i++];
        autoSaveState = (Boolean) state[i++];
    }

    public UIComponent getConfiguredComponent() {
        return getTable();
    }

    private AbstractTable getTable() {
        return (AbstractTable) getParent();
    }


    public ColumnResizingState getResizingState() {
        return ValueBindings.get(
                this, "resizingState", resizingState, ColumnResizingState.class);
    }

    public void setResizingState(ColumnResizingState resizingState) {
        this.resizingState = resizingState;
    }

    public String getMinColWidth() {
        return ValueBindings.get(this, "minColWidth", minColWidth);
    }

    public void setMinColWidth(String minColWidth) {
        this.minColWidth = minColWidth;
    }

    public String getResizeHandleWidth() {
        return ValueBindings.get(this, "resizeHandleWidth", resizeHandleWidth);
    }

    public void setResizeHandleWidth(String resizeHandleWidth) {
        this.resizeHandleWidth = resizeHandleWidth;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public boolean isEnabled() {
        return ValueBindings.get(this, "enabled", enabled, true);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public boolean getRetainTableWidth() {
        return ValueBindings.get(this, "retainTableWidth", retainTableWidth, true);
    }

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public void setRetainTableWidth(boolean retainTableWidth) {
        this.retainTableWidth = retainTableWidth;
    }

    @Override
    public void processUpdates(FacesContext context) {
        super.processUpdates(context);

        if (resizingState != null && ValueBindings.set(this, "resizingState", resizingState))
            resizingState = null;
    }

/*
*/

    public void encodeOnBodyReload(FacesContext context, ScriptBuilder sb) {
        
    }

    public boolean getAutoSaveState() {
        return ValueBindings.get(this, "autoSaveState", autoSaveState, true);
    }

    public void setAutoSaveState(boolean autoSaveState) {
        this.autoSaveState = autoSaveState;
    }
}
