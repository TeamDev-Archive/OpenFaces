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

package org.openfaces.ajax.gateinportal;

import org.ajax4jsf.event.AjaxListener;
import org.ajax4jsf.event.EventsQueue;
import org.jboss.portletbridge.component.UIPortletAjaxViewRoot;
import org.openfaces.ajax.CommonAjaxViewRoot;
import org.openfaces.ajax.WrappedAjaxRoot;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Environment;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class GateInAjaxViewRoot extends UIPortletAjaxViewRoot implements WrappedAjaxRoot {
    public static final String COMPONENT_TYPE = "org.openfaces.ajax.AjaxViewRoot";
    public static final String COMPONENT_FAMILY = "javax.faces.ViewRoot";

    public static final String ROOT_ID = "_OpenFaces_ViewRoot";

    private UIPortletAjaxViewRoot delegate;
    private final CommonAjaxViewRoot commonAjaxViewRoot = new CommonAjaxViewRoot(this) {
        @Override
        protected void parentProcessDecodes(FacesContext context) {
            GateInAjaxViewRoot.super.processDecodes(context);
        }

        @Override
        protected void parentProcessValidators(FacesContext context) {
            GateInAjaxViewRoot.super.processValidators(context);
        }

        @Override
        protected void parentProcessUpdates(FacesContext context) {
            GateInAjaxViewRoot.super.processUpdates(context);
        }

        @Override
        protected void parentProcessApplication(FacesContext context) {
            GateInAjaxViewRoot.super.processApplication(context);
        }

        @Override
        protected void parentEncodeChildren(FacesContext context) throws IOException {
            GateInAjaxViewRoot.super.encodeChildren(context);
        }

        @Override
        protected int parentGetChildCount() {
            return GateInAjaxViewRoot.super.getChildCount();
        }

        @Override
        protected List<UIComponent> parentGetChildren() {
            return GateInAjaxViewRoot.super.getChildren();
        }

        @Override
        protected Iterator<UIComponent> parentGetFacetsAndChildren() {
            return GateInAjaxViewRoot.super.getFacetsAndChildren();
        }
    };

    public GateInAjaxViewRoot() {
        setId(ROOT_ID);
    }

    public GateInAjaxViewRoot(UIViewRoot delegate) {
        setId(ROOT_ID);
        if (delegate != null) {
            this.delegate = (UIPortletAjaxViewRoot) delegate;
        }
    }


    @Override
    public String getId() {
        if (delegate != null)
            return delegate.getId();
        return super.getId();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        if (delegate != null)
            delegate.setId(id);
    }

    @Override
    public String getRendererType() {
        if (Environment.isAjax4jsfRequest()) {
            return delegate.getRendererType();
        }
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
            return delegate.getRendersChildren();
        }

        // Ajax Request. Control all output.
        return true;
    }


    public UIViewRoot getDelegate() {
        return delegate;
    }

    public void setDelegate(UIViewRoot delegate) {
        this.delegate = (UIPortletAjaxViewRoot) delegate;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] mystate = (Object[]) state;
        super.restoreState(context, mystate[0]);
        if (mystate[1] != null) {
            delegate = (UIPortletAjaxViewRoot) restoreAttachedState(context, mystate[1]);
        }

    }

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

    @Override
    public String getClientId(FacesContext context) {
        if (delegate != null)
            return delegate.getClientId(context);
        return super.getClientId(context);
    }

    @Override
    public String getViewId() {
        if (delegate != null)
            return delegate.getViewId();
        return super.getViewId();
    }

//
//    public MethodExpression getAjaxListener() {
//        if (delegate != null)
//            return delegate.getAjaxListener();
//        return super.getAjaxListener();
//    }
//
//    public boolean isImmediate() {
//        if (delegate != null)
//            return delegate.isImmediate();
//        return super.isImmediate();
//    }
//
//    public boolean isSubmitted() {
//        if (delegate != null)
//            return delegate.isSubmitted();
//        return super.isSubmitted();
//    }
//
//    @Override
//    public void addPhaseListener(PhaseListener newPhaseListener) {
//        if (delegate != null) {
//            delegate.addPhaseListener(newPhaseListener);
//            return;
//        }
//        super.addPhaseListener(newPhaseListener);
//    }
//
//    @Override
//    public void removePhaseListener(PhaseListener toRemove) {
//        if (delegate != null) {
//            delegate.removePhaseListener(toRemove);
//            return;
//        }
//        super.removePhaseListener(toRemove);
//    }
//
//    @Override
//    public void processEvents(FacesContext context, EventsQueue phaseEventsQueue, boolean havePhaseEvents) {
//        if (delegate != null) {
//            delegate.processEvents(context, phaseEventsQueue, havePhaseEvents);
//            return;
//        }
//        super.processEvents(context, phaseEventsQueue, havePhaseEvents);
//    }
//
//    @Override
//    public void broadcastAjaxEvents(FacesContext context) {
//        if (delegate != null) {
//            delegate.broadcastAjaxEvents(context);
//            return;
//        }
//        super.broadcastAjaxEvents(context);
//    }
//
//    @Override
//    public void clearEvents() {
//        if (delegate != null) {
//            delegate.clearEvents();
//            return;
//        }
//        super.clearEvents();
//    }
//
//    @Override
//    public void setAjaxListener(MethodExpression ajaxListener) {
//        if (delegate != null) {
//            delegate.setAjaxListener(ajaxListener);
//            return;
//        }
//        super.setAjaxListener(ajaxListener);
//    }
//
//    @Override
//    public void setImmediate(boolean immediate) {
//        if (delegate != null) {
//            delegate.setImmediate(immediate);
//            return;
//        }
//        super.setImmediate(immediate);
//    }
//
//    @Override
//    public void setSubmitted(boolean submitted) {
//        if (delegate != null) {
//            delegate.setSubmitted(submitted);
//            return;
//        }
//        super.setSubmitted(submitted);
//    }
//
//    @Override
//    public void addAjaxListener(AjaxListener listener) {
//        if (delegate != null) {
//            delegate.addAjaxListener(listener);
//            return;
//        }
//        super.addAjaxListener(listener);
//    }
//
//    @Override
//    public AjaxListener[] getAjaxListeners() {
//        if (delegate != null) {
//            return delegate.getAjaxListeners();
//        }
//        return super.getAjaxListeners();
//    }
//
//    @Override
//    public void removeAjaxListener(AjaxListener listener) {
//        if (delegate != null) {
//            delegate.removeAjaxListener(listener);
//            return;
//        }
//        super.removeAjaxListener(listener);
//    }
//
//    @Override
//    public boolean isSelfRendered() {
//        if (delegate != null) {
//            return delegate.isSelfRendered();
//        }
//        return super.isSelfRendered();
//    }
//
//    @Override
//    public void setSelfRendered(boolean selfRendered) {
//        if (delegate != null) {
//            delegate.setSelfRendered(selfRendered);
//            return;
//        }
//        super.setSelfRendered(selfRendered);
//    }
//
//    @Override
//    public boolean isRenderRegionOnly() {
//        if (delegate != null) {
//            return delegate.isRenderRegionOnly();
//        }
//        return super.isRenderRegionOnly();
//    }
//
//    @Override
//    public void setRenderRegionOnly(boolean reRenderPage) {
//        if (delegate != null) {
//            delegate.setRenderRegionOnly(reRenderPage);
//            return;
//        }
//        super.setRenderRegionOnly(reRenderPage);
//    }

    @Override
    public void encodeAjax(FacesContext context) throws IOException {
        if (delegate != null) {
            delegate.encodeAjax(context);
            return;
        }
        super.encodeAjax(context);
    }

//    @Override
//    public void encodeBegin(FacesContext context) throws IOException {
//        if (delegate != null) {
//            delegate.encodeBegin(context);
//            return;
//        }
//        super.encodeBegin(context);
//    }
//
//    @Override
//    public void encodeEnd(FacesContext context) throws IOException {
//        if (delegate != null) {
//            delegate.encodeEnd(context);
//            return;
//        }
//        super.encodeEnd(context);
//    }



}
