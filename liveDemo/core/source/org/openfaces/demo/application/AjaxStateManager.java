/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.application;

import org.openfaces.util.ReflectionUtil;

import javax.faces.application.StateManager;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Eugene Goncharov
 * Dirty fix for html tags missed after ajax request
 */
public class AjaxStateManager extends StateManager {

    private StateManager delegate;

    public AjaxStateManager(StateManager delegate) {
        this.delegate = delegate;
    }

    public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {
        return delegate.restoreView(context, viewId, renderKitId);
    }

    @Override
    public Object saveView(FacesContext context) {
        UIViewRoot viewRoot = context.getViewRoot();
        processTransientComponents(viewRoot);

        return delegate.saveView(context);
    }

    @Override
    public SerializedView saveSerializedView(FacesContext context) {
        return delegate.saveSerializedView(context);
    }

    @Override
    protected Object getTreeStructureToSave(FacesContext context) {
        return ReflectionUtil.invokeMethod(delegate.getClass(), "getTreeStructureToSave",
                new Class[]{FacesContext.class}, new Object[]{context}, delegate);

    }

    @Override
    protected Object getComponentStateToSave(FacesContext context) {
        return ReflectionUtil.invokeMethod(delegate.getClass(), "getComponentStateToSave",
                new Class[]{FacesContext.class}, new Object[]{context}, delegate);
    }

    @Override
    public void writeState(FacesContext context, Object state) throws IOException {
        delegate.writeState(context, state);
    }

    @Override
    public void writeState(FacesContext context, SerializedView state) throws IOException {
        delegate.writeState(context, state);
    }

    @Override
    protected UIViewRoot restoreTreeStructure(FacesContext context, String viewId, String renderKitId) {
        return (UIViewRoot) ReflectionUtil.invokeMethod(delegate.getClass(), "restoreTreeStructure",
                new Class[]{FacesContext.class, String.class, String.class},
                new Object[]{context, viewId, renderKitId}, delegate);
    }

    @Override
    protected void restoreComponentState(FacesContext context, UIViewRoot viewRoot, String renderKitId) {
        ReflectionUtil.invokeMethod(delegate.getClass(), "restoreTreeStructure",
                new Class[]{FacesContext.class, UIViewRoot.class, String.class},
                new Object[]{context, viewRoot, renderKitId}, delegate);
    }

    @Override
    public boolean isSavingStateInClient(FacesContext context) {
        return delegate.isSavingStateInClient(context);
    }

    private void processTransientComponents(UIComponent component) {
        if (component.isTransient() && component instanceof HtmlOutputText) {
            component.setTransient(false);
        }

        if (component.getChildCount() > 0) {
            for (UIComponent childComponent : component.getChildren()) {
                processTransientComponents(childComponent);
            }
        }
    }
}
