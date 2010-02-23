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
package org.openfaces.component;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import java.util.Collections;

/**
 * @author Dmitry Pikhulya
 */
public abstract class OUICommand extends UICommand implements OUIComponent {
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

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                saveAttachedState(context, execute),
                saveAttachedState(context, render),
                style, styleClass, rolloverStyle, rolloverClass, onclick, ondblclick, onmousedown, onmouseover,
                onmousemove, onmouseout, onmouseup, onfocus, onblur, onkeydown, onkeyup, onkeypress, oncontextmenu};
    }

    public abstract String getFamily();

    @Override
    public void restoreState(FacesContext context, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(context, state[i++]);
        execute = (Iterable<String>) restoreAttachedState(context, state[i++]);
        render = (Iterable<String>) restoreAttachedState(context, state[i++]);
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

}
