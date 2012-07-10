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
package org.openfaces.component.ajax;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.component.OUICommand;
import org.openfaces.renderkit.ajax.AjaxRenderer;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.ListenersFor;
import javax.faces.event.PostAddToViewEvent;

/**
 * @author Ilya Musihin
 */

@ListenersFor({
        @ListenerFor(systemEventClass = PostAddToViewEvent.class)
})
public class Ajax extends OUICommand implements OUIClientAction {
    public static final String COMPONENT_TYPE = "org.openfaces.Ajax";
    public static final String COMPONENT_FAMILY = "org.openfaces.Ajax";

    private String event;
    private String _for;
    private Boolean standalone;

    private Boolean submitInvoker;

    private String onevent;
    private String onajaxstart;
    private String onajaxend;
    private String onerror;

    public Ajax() {
        setRendererType("org.openfaces.AjaxRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);

        if (event instanceof PostAddToViewEvent) {
            if (isStandalone()) return;
            UIComponent parent = getParent();
            ClientBehaviorHolder cbh;
            if (getFor() == null) {
                if (!(parent instanceof ClientBehaviorHolder)) {
                    throw new IllegalStateException("<o:ajax> can only be inserted into components that allow placing " +
                            "client behaviors inside (components that implement ClientBehaviorHolder interface). " +
                            "Component id is: " + parent.getClientId() + "; Component class: " + parent.getClass().getName());
                }
                cbh = (ClientBehaviorHolder) parent;
            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                String invokerId = OUIClientActionHelper.getClientActionInvoker(context, this);
                UIComponent targetComponent = context.getViewRoot().findComponent(":" + invokerId);
                if (targetComponent == null) {
                    getAttributes().put(AjaxRenderer.ATTACH_ON_CLIENT, true);
                    return;
                }
                if (!(targetComponent instanceof ClientBehaviorHolder)) {
                    throw new IllegalStateException("<o:ajax> can only be attached to components that support " +
                            "client behaviors (components that implement ClientBehaviorHolder interface). " +
                            "Component id is: " + invokerId + "; Component class: " + parent.getClass().getName());
                }
                cbh = (ClientBehaviorHolder) targetComponent;
            }

            String eventName = getEvent();
            if (eventName == null)
                eventName = cbh.getDefaultEventName();
            if (eventName == null) {
                FacesContext context = getFacesContext();
                String invokerId = ((UIComponent) cbh).getClientId(context);
                throw new IllegalStateException("The 'event' attribute of <o:ajax> is not specified and no default event " +
                        "name exists for component: " + invokerId + " (component class: " + parent.getClass().getName() + "); " +
                        "you should specify the 'event' attribute for the appropriate <o:ajax> tag");
            }
            if (!cbh.getClientBehaviors().containsKey(eventName)) {
                //OFCS-57: PostAddToViewEvent could be fired multiple time under certain conditions
                AjaxHelper ajaxHelper = new AjaxHelper(this);
                cbh.addClientBehavior(eventName, ajaxHelper);
            }
        }
    }

    public final boolean getSubmitInvoker() { // todo: clean up usages
        return false;
    }

    public String getEvent() {
        return ValueBindings.get(this, "event", this.event);
    }

    public void setEvent(String event) {

        this.event = event;
    }

    public String getFor() {
        return ValueBindings.get(this, "for", _for);
    }

    public void setFor(String _for) {
        this._for = _for;
    }


    public boolean isStandalone() {
        return ValueBindings.get(this, "standalone", standalone, false);
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    public String getOnevent() {
        return ValueBindings.get(this, "onevent", onevent);
    }

    public void setOnevent(String onevent) {
        this.onevent = onevent;
    }

    public String getOnajaxstart() {
        return ValueBindings.get(this, "onajaxstart", onajaxstart);
    }

    public void setOnajaxstart(String onajaxstart) {
        this.onajaxstart = onajaxstart;
    }

    public String getOnajaxend() {
        return ValueBindings.get(this, "onajaxend", onajaxend);
    }

    public void setOnajaxend(String onajaxend) {
        this.onajaxend = onajaxend;
    }

    public String getOnerror() {
        return ValueBindings.get(this, "onerror", onerror);
    }

    public void setOnerror(String onerror) {
        this.onerror = onerror;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        AjaxUtil.renderJSLinks(context);

        return new Object[]{superState,
                event,
                _for,
                standalone,
                submitInvoker,
                onevent,
                onerror,
                onajaxstart,
                onajaxend,
        };
    }

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        event = (String) state[i++];
        _for = (String) state[i++];
        standalone = (Boolean) state[i++];
        submitInvoker = (Boolean) state[i++];
        onevent = (String) state[i++];
        onerror = (String) state[i++];
        onajaxstart = (String) state[i++];
        onajaxend = (String) state[i++];
    }

}
