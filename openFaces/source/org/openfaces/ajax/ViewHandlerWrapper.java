/*
 * OpenFaces - JSF Component Library 3.0
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
import javax.faces.view.ViewDeclarationLanguage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Eugene Goncharov
 */
public class ViewHandlerWrapper extends javax.faces.application.ViewHandlerWrapper {
    private ViewHandler delegate;

    public ViewHandlerWrapper(ViewHandler delegate) {
        this.delegate = delegate;
    }

    public ViewHandler getDelegate() {
        return getWrapped();
    }

    @Override
    public ViewHandler getWrapped() {
        return delegate;
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        UIViewRoot result = super.restoreView(context, viewId);
        Components.runScheduledActions();
        return result;
    }

    @Override
    public void writeState(FacesContext context) throws IOException {
        delegate.writeState(context);
    }


    public String calculateCharacterEncoding(FacesContext context) {
        return delegate.calculateCharacterEncoding(context);
    }


    public String deriveViewId(FacesContext context, String rawViewId) {
        return delegate.deriveViewId(context, rawViewId);
    }

    public String getRedirectURL(FacesContext context,
                                 String viewId,
                                 Map<String, List<String>> parameters,
                                 boolean includeViewParams) {
        return delegate.getActionURL(context, viewId);
    }


    public String getBookmarkableURL(FacesContext context,
                                     String viewId,
                                     Map<String, List<String>> parameters,
                                     boolean includeViewParams) {

        return delegate.getBookmarkableURL(context, viewId, parameters, includeViewParams);
    }

    public ViewDeclarationLanguage getViewDeclarationLanguage(FacesContext context,
                                                              String viewId) {
        return delegate.getViewDeclarationLanguage(context, viewId);
    }

    public void initView(FacesContext context) throws FacesException {
        delegate.initView(context);
    }

}
