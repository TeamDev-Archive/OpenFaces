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

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

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

}
