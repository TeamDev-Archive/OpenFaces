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

import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Eugene Goncharov
 */
public class AjaxViewRoot extends UIViewRoot implements WrappedAjaxRoot {
    public static final String COMPONENT_TYPE = "org.openfaces.ajax.AjaxViewRoot";
    public static final String COMPONENT_FAMILY = "javax.faces.ViewRoot";

    public static final String ROOT_ID = "_OpenFaces_ViewRoot";

    private UIViewRoot delegate;
    private final CommonAjaxViewRoot commonAjaxViewRoot = new CommonAjaxViewRoot(this) {
        @Override
        protected void parentProcessDecodes(FacesContext context) {
            AjaxViewRoot.super.processDecodes(context);
        }

        @Override
        protected void parentProcessValidators(FacesContext context) {
            AjaxViewRoot.super.processValidators(context);
        }

        @Override
        protected void parentProcessUpdates(FacesContext context) {
            AjaxViewRoot.super.processUpdates(context);
        }

        @Override
        protected void parentProcessApplication(FacesContext context) {
            AjaxViewRoot.super.processApplication(context);
        }

        @Override
        protected void parentEncodeChildren(FacesContext context) throws IOException {
            AjaxViewRoot.super.encodeChildren(context);
        }

        @Override
        protected int parentGetChildCount() {
            return AjaxViewRoot.super.getChildCount();
        }

        @Override
        protected List<UIComponent> parentGetChildren() {
            return AjaxViewRoot.super.getChildren();
        }

        @Override
        protected Iterator<UIComponent> parentGetFacetsAndChildren() {
            return AjaxViewRoot.super.getFacetsAndChildren();
        }
    };

    public AjaxViewRoot() {
        setId(ROOT_ID);
    }

    public AjaxViewRoot(UIViewRoot delegate) {
        setId(ROOT_ID);
        if (delegate != null) {
            this.delegate = delegate;
        }
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        if (delegate != null)
            delegate.setId(id);
    }

    @Override
    public String getRendererType() {
        return COMPONENT_TYPE;
    }

    @Override
    public void processDecodes(FacesContext context) {
        Components.runScheduledActions();
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
    public void encodeAll(FacesContext context) throws IOException {
        Components.runScheduledActions();
        super.encodeAll(context);
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
            return false;
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
    public void queueEvent(FacesEvent event) {
        if (AjaxUtil.isAjaxRequest(getFacesContext())) {
            commonAjaxViewRoot.queueEvent(event);
            return;
        }
        super.queueEvent(event);
    }
}
