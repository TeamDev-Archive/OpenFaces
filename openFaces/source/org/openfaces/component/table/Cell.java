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
package org.openfaces.component.table;

import org.openfaces.renderkit.table.TableBody;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
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
public class Cell extends UIComponentBase implements ClientBehaviorHolder {
    public static final String COMPONENT_TYPE = "org.openfaces.Cell";
    public static final String COMPONENT_FAMILY = "org.openfaces.Cell";

    private static final List<String> EVENT_NAMES = Collections.unmodifiableList(Arrays.asList("click", "dblclick",
            "keydown", "keypress", "keyup", "mousedown", "mousemove", "mouseout", "mouseover", "mouseup"));

    private Object columnIds;
    private Integer span;
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

    public Cell() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                columnIds,
                span,
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
        columnIds = state[i++];
        span = (Integer) state[i++];
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

    public ValueExpression getConditionExpression() {
        return getValueExpression("condition");
    }

    public void setConditionExpression(ValueExpression expression) {
        setValueExpression("condition", expression);
    }

    public Object getColumnIds() {
        return ValueBindings.get(this, "columnIds", columnIds, Object.class);
    }

    public void setColumnIds(Object columnIds) {
        this.columnIds = columnIds;
    }

    public int getSpan() {
        return ValueBindings.get(this, "span", span, 1);
    }

    public void setSpan(int span) {
        this.span = span;
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


    public String getStyleClassForCell(FacesContext context,
                                       AbstractTable table,
                                       int columnIndex,
                                       String columnId) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String columnIndexVar = table.getColumnIndexVar();
        String columnIdVar = table.getColumnIdVar();
        Object prevColumnIndexVarValue = null;
        Object prevColumnIdVarValue = null;
        if (columnIndexVar != null)
            prevColumnIndexVarValue = requestMap.put(columnIndexVar, columnIndex);
        if (columnIdVar != null)
            prevColumnIdVarValue = requestMap.put(columnIdVar, columnId);

        Integer customCellIndex = (Integer) getAttributes().get(TableBody.CUSTOM_CELL_INDEX_ATTRIBUTE);
        StyleGroup styleGroup = customCellIndex != null
                ? StyleGroup.regularStyleGroup(2 + customCellIndex) : StyleGroup.regularStyleGroup();
        String result = Styles.getCSSClass(context, table, getStyle(), styleGroup, getStyleClass());
        if (columnIndexVar != null)
            requestMap.put(columnIndexVar, prevColumnIndexVarValue);
        if (columnIdVar != null)
            requestMap.put(columnIdVar, prevColumnIdVarValue);
        return result;
    }

    @Override
    public String getDefaultEventName() {
        return "click";
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

}
