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
package org.openfaces.ajax.richfaces;

import org.ajax4jsf.component.AjaxViewRoot;
import org.openfaces.ajax.CommonAjaxViewRoot;
import org.openfaces.ajax.WrappedAjaxRoot;
import org.openfaces.util.AjaxUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public final class A4JAjaxViewRoot extends AjaxViewRoot implements WrappedAjaxRoot {
    public static final String ROOT_ID = AjaxViewRoot.ROOT_ID;

    private UIViewRoot delegate;

    private final CommonAjaxViewRoot commonAjaxViewRoot = new CommonAjaxViewRoot(this) {
        @Override
        protected void parentProcessDecodes(FacesContext context) {
            A4JAjaxViewRoot.super.processDecodes(context);
        }

        @Override
        protected void parentProcessValidators(FacesContext context) {
            A4JAjaxViewRoot.super.processValidators(context);
        }

        @Override
        protected void parentProcessUpdates(FacesContext context) {
            A4JAjaxViewRoot.super.processUpdates(context);
        }

        @Override
        protected void parentProcessApplication(FacesContext context) {
            A4JAjaxViewRoot.super.processApplication(context);
        }

        @Override
        protected void parentEncodeChildren(FacesContext context) throws IOException {
            A4JAjaxViewRoot.super.encodeChildren(context);
        }

        @Override
        protected int parentGetChildCount() {
            return A4JAjaxViewRoot.super.getChildCount();
        }

        @Override
        protected List<UIComponent> parentGetChildren() {
            return A4JAjaxViewRoot.super.getChildren();
        }

        @Override
        protected Iterator<UIComponent> parentGetFacetsAndChildren() {
            return A4JAjaxViewRoot.super.getFacetsAndChildren();
        }
    };

    public A4JAjaxViewRoot() {
        setId(ROOT_ID);
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        if (delegate != null)
            delegate.setId(id);
    }

    public A4JAjaxViewRoot(UIViewRoot delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getRendererType() {
        return COMPONENT_TYPE;
    }

    @Override
    public void processDecodes(FacesContext context) {
        commonAjaxViewRoot.processDecodes(context, false);
    }

    @Override
    public void processUpdates(FacesContext context) {
        commonAjaxViewRoot.processUpdates(context, false);
    }

    @Override
    public void processValidators(FacesContext context) {
        commonAjaxViewRoot.processValidators(context, false);
    }

    @Override
    public void processApplication(FacesContext context) {
        commonAjaxViewRoot.processApplication(context, false);
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {
        commonAjaxViewRoot.encodeChildren(context);
    }

    @Override
    public int getChildCount() {
        return commonAjaxViewRoot.getChildCount();
    }

    @Override
    public List<UIComponent> getChildren() {
        return commonAjaxViewRoot.getChildren();
    }

    @Override
    public Iterator<UIComponent> getFacetsAndChildren() {
        return commonAjaxViewRoot.getFacetsAndChildren();
    }

    @Override
    public boolean getRendersChildren() {
        FacesContext context = FacesContext.getCurrentInstance();
        // For non Ajax request, view root not render children
        if (!AjaxUtil.isAjaxRequest(context)) {
            return super.getRendersChildren();
//      return false;
        }

        // Ajax Request. Control all output.
        return true;
    }

    public UIViewRoot getDelegate() {
        return delegate;
    }

    public void setDelegate(UIViewRoot delegate) {
        this.delegate = delegate;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] mystate = (Object[]) state;
        super.restoreState(context, mystate[0]);
        if (mystate[1] != null) {
            delegate = (UIViewRoot) restoreAttachedState(context, mystate[1]);
        }

    }

    /*
    * (non-Javadoc)
    *
    * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#saveState(javax.faces.context.FacesContext)
    */
    @Override
    public Object saveState(FacesContext context) {
        Object[] state = new Object[2];
        state[0] = super.saveState(context);
        if (delegate != null) {
            state[1] = saveAttachedState(context, delegate);
        }

        return state;
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);
        if (delegate != null) {
            delegate.broadcast(event);
        }
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if (AjaxUtil.isAjaxRequest(getFacesContext())) {
            commonAjaxViewRoot.queueEvent(event);
            return;
        }
        super.queueEvent(event);
        if (delegate != null) {
            delegate.queueEvent(event);
        }
    }
}

