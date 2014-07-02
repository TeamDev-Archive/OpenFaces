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
package org.openfaces.component;

import org.openfaces.util.ValueBindings;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UICommand;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
@ResourceDependencies({
        @ResourceDependency(name = "default.css", library = "openfaces"),
        @ResourceDependency(name = "jsf.js", library = "javax.faces")
})
public abstract class OUICommand extends UICommand implements OUIComponent, ClientBehaviorHolder {
    private static final List<String> EVENT_NAMES = Collections.unmodifiableList(Arrays.asList("action", "blur",
            "change", "click", "dblclick", "focus", "keydown", "keypress", "keyup", "mousedown", "mousemove",
            "mouseout", "mouseover", "mouseup", "select"));

    private Iterable<String> execute;
    private Iterable<String> render;

    private String style;
    private String styleClass;
    private String rolloverStyle;
    private String rolloverClass;

    private String onclick;
    private String ondblclick;
    private String onmousedown;
    private String onmouseover;
    private String onmousemove;
    private String onmouseout;
    private String onmouseup;
    private String onfocus;
    private String onblur;
    private String onkeydown;
    private String onkeyup;
    private String onkeypress;
    private String oncontextmenu;
    private String onajaxstart;
    private String onajaxend;
    private String onerror;
    private String onsuccess;
    private Integer delay;
    private Boolean disabled;
    private String disabledStyle;
    private String disabledClass;

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                saveAttachedState(context, execute),
                saveAttachedState(context, render),
                disabled,
                disabledStyle,
                disabledClass,
                style, styleClass, rolloverStyle, rolloverClass, onclick, ondblclick, onmousedown, onmouseover,
                onmousemove, onmouseout, onmouseup, onfocus, onblur, onkeydown, onkeyup, onkeypress, oncontextmenu,
                onajaxstart, onajaxend, onerror, onsuccess, delay};
    }

    public abstract String getFamily();

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        execute = (Iterable<String>) restoreAttachedState(context, state[i++]);
        render = (Iterable<String>) restoreAttachedState(context, state[i++]);
        disabled = (Boolean) state[i++];
        disabledStyle = (String) state[i++];
        disabledClass = (String) state[i++];
        style = (String) state[i++];
        styleClass = (String) state[i++];
        rolloverStyle = (String) state[i++];
        rolloverClass = (String) state[i++];
        onclick = (String) state[i++];
        ondblclick = (String) state[i++];
        onmousedown = (String) state[i++];
        onmouseover = (String) state[i++];
        onmousemove = (String) state[i++];
        onmouseout = (String) state[i++];
        onmouseup = (String) state[i++];
        onfocus = (String) state[i++];
        onblur = (String) state[i++];
        onkeydown = (String) state[i++];
        onkeyup = (String) state[i++];
        onkeypress = (String) state[i++];
        oncontextmenu = (String) state[i++];
        onajaxstart = (String) state[i++];
        onajaxend = (String) state[i++];
        onerror = (String) state[i++];
        onsuccess = (String) state[i++];
        delay = (Integer) state[i++];
    }

    @Override
    public String getDefaultEventName() {
        return "action";
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }


    public Iterable<String> getExecute() {
        return ValueBindings.get(this, "execute", execute, Collections.<String>emptySet(), Iterable.class);
    }

    public void setExecute(Iterable<String> execute) {
        this.execute = execute;
    }

    public void setRender(Iterable<String> render) {
        this.render = render;
    }

    public Iterable<String> getRender() {
        return ValueBindings.get(this, "render", render, Iterable.class);
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getOnclick() {
        return ValueBindings.get(this, "onclick", onclick);
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getOndblclick() {
        return ValueBindings.get(this, "ondblclick", ondblclick);
    }

    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }

    public String getOnmousedown() {
        return ValueBindings.get(this, "onmousedown", onmousedown);
    }

    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }

    public String getOnmouseover() {
        return ValueBindings.get(this, "onmouseover", onmouseover);
    }

    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }

    public String getOnmousemove() {
        return ValueBindings.get(this, "onmousemove", onmousemove);
    }

    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }

    public String getOnmouseout() {
        return ValueBindings.get(this, "onmouseout", onmouseout);
    }

    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }

    public String getOnmouseup() {
        return ValueBindings.get(this, "onmouseup", onmouseup);
    }

    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }

    public String getOnfocus() {
        return ValueBindings.get(this, "onfocus", onfocus);
    }

    public void setOnfocus(String onfocus) {
        this.onfocus = onfocus;
    }

    public String getOnblur() {
        return ValueBindings.get(this, "onblur", onblur);
    }

    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }

    public String getOnkeydown() {
        return ValueBindings.get(this, "onkeydown", onkeydown);
    }

    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    public String getOnkeyup() {
        return ValueBindings.get(this, "onkeyup", onkeyup);
    }

    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    public String getOnkeypress() {
        return ValueBindings.get(this, "onkeypress", onkeypress);
    }

    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    public String getOncontextmenu() {
        return ValueBindings.get(this, "oncontextmenu", oncontextmenu);
    }

    public void setOncontextmenu(String oncontextmenu) {
        this.oncontextmenu = oncontextmenu;
    }

    public String getRolloverStyle() {
        return ValueBindings.get(this, "rolloverStyle", rolloverStyle);
    }

    public void setRolloverStyle(String rolloverStyle) {
        this.rolloverStyle = rolloverStyle;
    }

    public String getRolloverClass() {
        return ValueBindings.get(this, "rolloverClass", rolloverClass);
    }

    public void setRolloverClass(String rolloverClass) {
        this.rolloverClass = rolloverClass;
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

    public String getOnsuccess() {
        return ValueBindings.get(this, "onsuccess", onsuccess);
    }

    public void setOnsuccess(String onsuccess) {
        this.onsuccess = onsuccess;
    }

    public Integer getDelay() {
        return ValueBindings.get(this, "delay", delay, 0);
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public boolean isDisabled() {
        return ValueBindings.get(this, "disabled", disabled, false);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getDisabledStyle() {
        return ValueBindings.get(this, "disabledStyle", disabledStyle);
    }

    public void setDisabledStyle(String disabledStyle) {
        this.disabledStyle = disabledStyle;
    }

    public String getDisabledClass() {
        return ValueBindings.get(this, "disabledClass", disabledClass);
    }

    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

    @Override
    public void decode(FacesContext context) {
        super.decode(context);
        decodeDisabledStateIfNeeded(context);
    }

    private void decodeDisabledStateIfNeeded(FacesContext context) {
        Map<String,String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String clientId = getClientId(context);
        Object decodeDisabledStateObj = getAttributes().get("decodeDisabledState");
        if (decodeDisabledStateObj != null) {
            boolean decodeDisabledState;
            if (decodeDisabledStateObj instanceof String)
                decodeDisabledState = Boolean.valueOf((String) decodeDisabledStateObj);
            else if (decodeDisabledStateObj instanceof Boolean)
                decodeDisabledState = (Boolean) decodeDisabledStateObj;
            else
                throw new FacesException("decodeDisabledState should be of a boolean type, " +
                        "but it is of the following type: " + decodeDisabledStateObj.getClass().getName());
            if (decodeDisabledState) {
                String disabledStr = requestParameterMap.get(clientId + "::disabled");
                if (disabledStr != null) {
                    boolean disabled = Boolean.valueOf(disabledStr);
                    setDisabled(disabled);
                }
            }
        }
    }
    public String getActionTriggerParam() {
        return getClientId(getFacesContext());
    }

}
