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
package org.openfaces.component.table;

import org.openfaces.renderkit.table.TableBody;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class Row extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.Row";
    public static final String COMPONENT_FAMILY = "org.openfaces.Row";

    private Boolean condition;
    private String style;
    private String styleClass;

    private String onclick;
    private String ondblclick;
    private String onmousedown;
    private String onmouseover;
    private String onmousemove;
    private String onmouseout;
    private String onmouseup;
    private String onkeydown;
    private String onkeyup;
    private String onkeypress;

    public Row() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                condition,
                style,
                styleClass,
                onclick,
                ondblclick,
                onmousedown,
                onmouseover,
                onmousemove,
                onmouseout,
                onmouseup,
                onkeydown,
                onkeyup,
                onkeypress
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        condition = (Boolean) state[i++];
        style = (String) state[i++];
        styleClass = (String) state[i++];
        onclick = (String) state[i++];
        ondblclick = (String) state[i++];
        onmousedown = (String) state[i++];
        onmouseover = (String) state[i++];
        onmousemove = (String) state[i++];
        onmouseout = (String) state[i++];
        onmouseup = (String) state[i++];
        onkeydown = (String) state[i++];
        onkeyup = (String) state[i++];
        onkeypress = (String) state[i++];
    }

    public boolean getCondition() {
        return ValueBindings.get(this, "condition", condition, true);
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
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

    public String getStyleClassForRow(FacesContext context, AbstractTable table) {
        boolean applicable = getCondition();
        Integer customRowIndex = (Integer) getAttributes().get(TableBody.CUSTOM_ROW_INDEX_ATTRIBUTE);
        String result = applicable
                ? Styles.getCSSClass(context, table, getStyle(), StyleGroup.regularStyleGroup(1 + customRowIndex), getStyleClass())
                : null;

        return result;

    }
}
