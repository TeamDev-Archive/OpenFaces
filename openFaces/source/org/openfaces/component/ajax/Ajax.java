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
package org.openfaces.component.ajax;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUICommand;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Styles;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Ilya Musihin
 */
public class Ajax extends OUICommand implements OUIClientAction {
    public static final String COMPONENT_TYPE = "org.openfaces.Ajax";
    public static final String COMPONENT_FAMILY = "org.openfaces.Ajax";

    private String event;
    private String _for;
    private Boolean standalone;

    private Boolean submitInvoker;
    private Integer delay;

    private String onevent;
    private String onajaxstart;
    private String onajaxend;
    private String onerror;

    private Boolean disabled;

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


    public boolean getSubmitInvoker() { // todo: remove the "submitInvoker" property and hard-code the "true" behavior if no use-case where this should be customizible arises
        return ValueBindings.get(this, "submitInvoker", submitInvoker, true);
    }

    public void setSubmitInvoker(boolean submitInvoker) {
        this.submitInvoker = submitInvoker;
    }

    public String getEvent() {
        return ValueBindings.get(this, "event", this.event, "click");
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

    public boolean isDisabled() {
        return ValueBindings.get(this, "disabled", disabled, false);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        AjaxUtil.renderJSLinks(context);
        Styles.requestDefaultCss(FacesContext.getCurrentInstance());

        return new Object[]{superState,
                event,
                _for,
                standalone,
                disabled,
                submitInvoker,
                delay,
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
        disabled = (Boolean) state[i++];
        submitInvoker = (Boolean) state[i++];
        delay = (Integer) state[i++];
        onevent = (String) state[i++];
        onerror = (String) state[i++];
        onajaxstart = (String) state[i++];
        onajaxend = (String) state[i++];
    }

}
