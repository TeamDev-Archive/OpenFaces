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
package org.openfaces.ajax;

import org.openfaces.util.Components;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Locale;

/**
 * @author Eugene Goncharov
 */
public class ViewHandlerWrapper extends ViewHandler {
    private ViewHandler delegate;

    public ViewHandlerWrapper(ViewHandler delegate) {
        this.delegate = delegate;
    }

    public ViewHandler getDelegate() {
        return delegate;
    }

    @Override
    public Locale calculateLocale(FacesContext context) {
        return delegate.calculateLocale(context);
    }

    @Override
    public String calculateRenderKitId(FacesContext context) {
        return delegate.calculateRenderKitId(context);
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        return delegate.createView(context, viewId);
    }

    @Override
    public String getActionURL(FacesContext context, String url) {
        return delegate.getActionURL(context, url);
    }

    @Override
    public String getResourceURL(FacesContext context, String url) {
        return delegate.getResourceURL(context, url);
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot root) throws IOException, FacesException {
        delegate.renderView(context, root);
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        UIViewRoot result = delegate.restoreView(context, viewId);
        Components.runScheduledActions();
        return result;
    }

    @Override
    public void writeState(FacesContext context) throws IOException {
        delegate.writeState(context);
    }

}
