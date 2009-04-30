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
import org.openfaces.component.ValueBindings;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.AjaxUtil;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ilya Musihin
 */
public class ReloadComponents extends UICommand implements OUIClientAction {
    public static final String COMPONENT_TYPE = "org.openfaces.ReloadComponents";
    public static final String COMPONENT_FAMILY = "org.openfaces.ReloadComponents";

    private String event = "onclick";
    private String _for;
    private Boolean standalone;

    private List<String> componentIds;
    private List<String> submittedComponentIds;
    private Boolean submitInvoker;
    private Integer requestDelay;
    private Boolean disableDefault;

    private String onajaxstart;
    private String onajaxend;
    private String onerror;

    private ReloadComponentsHelper helper = new ReloadComponentsHelper();

    public ReloadComponents() {
        setRendererType("org.openfaces.ReloadComponentsRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setParent(UIComponent parent) {
        super.setParent(parent);
        helper.onParentChange(this, parent);
    }

    public void setComponentIds(List<String> componentIds) {
        String s = componentIds.get(0);
        String[] strings = s.split(",");
        this.componentIds = new ArrayList<String>();
        for (String string : strings) {
            this.componentIds.add(string.trim());
        }
    }

    public List<String> getComponentIds() {
        return componentIds;
    }

    public boolean getSubmitInvoker() { // todo: remove the "submitInvoker" property and hard-code the "true" behavior if no use-case where this should be customizible arises
        return ValueBindings.get(this, "submitInvoker", submitInvoker, true);
    }

    public void setSubmitInvoker(boolean submitInvoker) {
        this.submitInvoker = submitInvoker;
    }

    public List<String> getSubmittedComponentIds() {
        return submittedComponentIds;
    }

    public void setSubmittedComponentIds(List<String> submittedComponentIds) {
        String s = submittedComponentIds.get(0);
        String[] strings = s.split(",");
        this.submittedComponentIds = new ArrayList<String>();
        for (String string : strings) {
            this.submittedComponentIds.add(string.trim());
        }
    }

    public String getEvent() {
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

    public int getRequestDelay() {
        return ValueBindings.get(this, "requestDelay", requestDelay, 0);
    }

    public void setRequestDelay(int requestDelay) {
        this.requestDelay = requestDelay;
    }

    public boolean getDisableDefault() {
        return ValueBindings.get(this, "disableDefault", disableDefault, false);
    }

    public void setDisableDefault(boolean disableDefault) {
        this.disableDefault = disableDefault;
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


    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        if (!(getParent() instanceof HtmlCommandLink)) {
            AjaxUtil.prepareComponentForAjax(context, getParent());
            StyleUtil.requestDefaultCss(FacesContext.getCurrentInstance());
        }
        return new Object[]{superState,
                saveAttachedState(context, componentIds),
                saveAttachedState(context, submittedComponentIds),
                event,
                _for,
                standalone,
                submitInvoker,
                requestDelay,
                disableDefault,
                onerror,
                onajaxstart,
                onajaxend,
        };
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        componentIds = (List<String>) restoreAttachedState(context, stateArray[i++]);
        submittedComponentIds = (List<String>) restoreAttachedState(context, stateArray[i++]);
        event = (String) stateArray[i++];
        _for = (String) stateArray[i++];
        standalone = (Boolean) stateArray[i++];
        submitInvoker = (Boolean) stateArray[i++];
        requestDelay = (Integer) stateArray[i++];
        disableDefault = (Boolean) stateArray[i++];
        onerror = (String) stateArray[i++];
        onajaxstart = (String) stateArray[i++];
        onajaxend = (String) stateArray[i++];
    }

}
