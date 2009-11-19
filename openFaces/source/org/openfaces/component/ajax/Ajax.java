/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
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
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * @author Ilya Musihin
 */
public class Ajax extends UICommand implements OUIClientAction {
    public static final String COMPONENT_TYPE = "org.openfaces.Ajax";
    public static final String COMPONENT_FAMILY = "org.openfaces.Ajax";

    private String event;
    private String _for;
    private Boolean standalone;

    private Iterable<String> render;
    private Iterable<String> execute;
    private Boolean submitInvoker;
    private Integer delay;

    private String onevent;
    private String onajaxstart;
    private String onajaxend;
    private String onerror;

    private AjaxHelper helper = new AjaxHelper();

    public Ajax() {
        setRendererType("org.openfaces.AjaxRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (parent != null)
            helper.onParentChange(this, parent);
    }

    public void setRender(Iterable<String> render) {
        this.render = render;
    }

    public Iterable<String> getRender() {
        return render;
    }

    public boolean getSubmitInvoker() { // todo: remove the "submitInvoker" property and hard-code the "true" behavior if no use-case where this should be customizible arises
        return ValueBindings.get(this, "submitInvoker", submitInvoker, true);
    }

    public void setSubmitInvoker(boolean submitInvoker) {
        this.submitInvoker = submitInvoker;
    }

    public Iterable<String> getExecute() {
        return execute;
    }

    public void setExecute(Iterable<String> execute) {
        this.execute = execute;
    }

    public String getEvent() {
        String event = ValueBindings.get(this, "event", this.event, "click");
        if (!event.startsWith("on")) {
            event = "on" + event;
        }
        return event;
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

    public int getDelay() {
        return ValueBindings.get(this, "delay", delay, 0);
    }

    public void setDelay(int delay) {
        this.delay = delay;
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
        AjaxUtil.addJSLinks(context, getParent());
        StyleUtil.requestDefaultCss(FacesContext.getCurrentInstance());
        
        return new Object[]{superState,
                saveAttachedState(context, render),
                saveAttachedState(context, execute),
                event,
                _for,
                standalone,
                submitInvoker,
                delay,
                onevent,
                onerror,
                onajaxstart,
                onajaxend,
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        render = (List<String>) restoreAttachedState(context, stateArray[i++]);
        execute = (List<String>) restoreAttachedState(context, stateArray[i++]);
        event = (String) stateArray[i++];
        _for = (String) stateArray[i++];
        standalone = (Boolean) stateArray[i++];
        submitInvoker = (Boolean) stateArray[i++];
        delay = (Integer) stateArray[i++];
        onevent= (String) stateArray[i++];
        onerror = (String) stateArray[i++];
        onajaxstart = (String) stateArray[i++];
        onajaxend = (String) stateArray[i++];
    }

}
