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

import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class BaseColumn extends UIColumn {
    private String headerValue;
    private String footerValue;

    private String align;
    private String valign;
    private String width;

    private Boolean resizable;
    private String minResizingWidth;
    private Boolean fixed;

    private String style;
    private String styleClass;
    private String headerStyle;
    private String headerClass;
    private String subHeaderStyle;
    private String subHeaderClass;
    private String bodyStyle;
    private String bodyClass;
    private String footerStyle;
    private String footerClass;

    private String onclick;
    private String ondblclick;
    private String onmousedown;
    private String onmouseover;
    private String onmousemove;
    private String onmouseout;
    private String onmouseup;

    private String headerOnclick;
    private String headerOndblclick;
    private String headerOnmousedown;
    private String headerOnmouseover;
    private String headerOnmousemove;
    private String headerOnmouseout;
    private String headerOnmouseup;

    private String bodyOnclick;
    private String bodyOndblclick;
    private String bodyOnmousedown;
    private String bodyOnmouseover;
    private String bodyOnmousemove;
    private String bodyOnmouseout;
    private String bodyOnmouseup;

    private String footerOnclick;
    private String footerOndblclick;
    private String footerOnmousedown;
    private String footerOnmouseover;
    private String footerOnmousemove;
    private String footerOnmouseout;
    private String footerOnmouseup;

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                headerValue, footerValue, align, valign, width, resizable, minResizingWidth, fixed,
                style, styleClass, headerStyle, headerClass,
                subHeaderStyle, subHeaderClass, bodyStyle, bodyClass, footerStyle, footerClass,
                onclick, ondblclick, onmousedown, onmouseover, onmousemove,
                onmouseout, onmouseup, headerOnclick, headerOndblclick, headerOnmousedown, headerOnmouseover,
                headerOnmousemove, headerOnmouseout, headerOnmouseup, bodyOnclick, bodyOndblclick,
                bodyOnmousedown, bodyOnmouseover, bodyOnmousemove, bodyOnmouseout, bodyOnmouseup,
                footerOnclick, footerOndblclick, footerOnmousedown, footerOnmouseover, footerOnmousemove,
                footerOnmouseout, footerOnmouseup};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        headerValue = (String) state[i++];
        footerValue = (String) state[i++];
        align = (String) state[i++];
        valign = (String) state[i++];
        width = (String) state[i++];
        resizable = (Boolean) state[i++];
        minResizingWidth = (String) state[i++];
        fixed = (Boolean) state[i++];
        style = (String) state[i++];
        styleClass = (String) state[i++];
        headerStyle = (String) state[i++];
        headerClass = (String) state[i++];
        subHeaderStyle = (String) state[i++];
        subHeaderClass = (String) state[i++];
        bodyStyle = (String) state[i++];
        bodyClass = (String) state[i++];
        footerStyle = (String) state[i++];
        footerClass = (String) state[i++];
        onclick = (String) state[i++];
        ondblclick = (String) state[i++];
        onmousedown = (String) state[i++];
        onmouseover = (String) state[i++];
        onmousemove = (String) state[i++];
        onmouseout = (String) state[i++];
        onmouseup = (String) state[i++];
        headerOnclick = (String) state[i++];
        headerOndblclick = (String) state[i++];
        headerOnmousedown = (String) state[i++];
        headerOnmouseover = (String) state[i++];
        headerOnmousemove = (String) state[i++];
        headerOnmouseout = (String) state[i++];
        headerOnmouseup = (String) state[i++];
        bodyOnclick = (String) state[i++];
        bodyOndblclick = (String) state[i++];
        bodyOnmousedown = (String) state[i++];
        bodyOnmouseover = (String) state[i++];
        bodyOnmousemove = (String) state[i++];
        bodyOnmouseout = (String) state[i++];
        bodyOnmouseup = (String) state[i++];
        footerOnclick = (String) state[i++];
        footerOndblclick = (String) state[i++];
        footerOnmousedown = (String) state[i++];
        footerOnmouseover = (String) state[i++];
        footerOnmousemove = (String) state[i++];
        footerOnmouseout = (String) state[i++];
        footerOnmouseup = (String) state[i++];
    }

    public String getHeaderValue() {
        return ValueBindings.get(this, "headerValue", headerValue);
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public String getFooterValue() {
        return ValueBindings.get(this, "footerValue", footerValue);
    }

    public void setFooterValue(String footerValue) {
        this.footerValue = footerValue;
    }

    public String getAlign() {
        return ValueBindings.get(this, "align", align);
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getValign() {
        return ValueBindings.get(this, "valign", valign);
    }

    public void setValign(String valign) {
        this.valign = valign;
    }

    public String getWidth() {
        return ValueBindings.get(this, "width", width);
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public boolean isResizable() {
        return ValueBindings.get(this, "resizable", resizable, true);
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isFixed() {
        return ValueBindings.get(this, "fixed", fixed, false);
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getMinResizingWidth() {
        return ValueBindings.get(this, "minResizingWidth", minResizingWidth);
    }

    public void setMinResizingWidth(String minResizingWidth) {
        this.minResizingWidth = minResizingWidth;
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }


    public String getSubHeaderStyle() {
        return ValueBindings.get(this, "subHeaderStyle", subHeaderStyle);
    }

    public void setSubHeaderStyle(String subHeaderStyle) {
        this.subHeaderStyle = subHeaderStyle;
    }

    public String getBodyStyle() {
        return ValueBindings.get(this, "bodyStyle", bodyStyle);
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public String getFooterStyle() {
        return ValueBindings.get(this, "footerStyle", footerStyle);
    }

    public void setFooterStyle(String footerStyle) {
        this.footerStyle = footerStyle;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getSubHeaderClass() {
        return ValueBindings.get(this, "subHeaderClass", subHeaderClass);
    }

    public void setSubHeaderClass(String subHeaderClass) {
        this.subHeaderClass = subHeaderClass;
    }

    public String getBodyClass() {
        return ValueBindings.get(this, "bodyClass", bodyClass);
    }

    public void setBodyClass(String bodyClass) {
        this.bodyClass = bodyClass;
    }

    public String getFooterClass() {
        return ValueBindings.get(this, "footerClass", footerClass);
    }

    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
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


    public String getHeaderOnclick() {
        return ValueBindings.get(this, "headerOnclick", headerOnclick);
    }

    public void setHeaderOnclick(String onclick) {
        headerOnclick = onclick;
    }

    public String getHeaderOndblclick() {
        return ValueBindings.get(this, "headerOndblclick", headerOndblclick);
    }

    public void setHeaderOndblclick(String ondblclick) {
        headerOndblclick = ondblclick;
    }

    public String getHeaderOnmousedown() {
        return ValueBindings.get(this, "headerOnmousedown", headerOnmousedown);
    }

    public void setHeaderOnmousedown(String onmousedown) {
        headerOnmousedown = onmousedown;
    }

    public String getHeaderOnmouseover() {
        return ValueBindings.get(this, "headerOnmouseover", headerOnmouseover);
    }

    public void setHeaderOnmouseover(String onmouseover) {
        headerOnmouseover = onmouseover;
    }

    public String getHeaderOnmousemove() {
        return ValueBindings.get(this, "headerOnmousemove", headerOnmousemove);
    }

    public void setHeaderOnmousemove(String onmousemove) {
        headerOnmousemove = onmousemove;
    }

    public String getHeaderOnmouseout() {
        return ValueBindings.get(this, "headerOnmouseout", headerOnmouseout);
    }

    public void setHeaderOnmouseout(String onmouseout) {
        headerOnmouseout = onmouseout;
    }

    public String getHeaderOnmouseup() {
        return ValueBindings.get(this, "headerOnmouseup", headerOnmouseup);
    }

    public void setHeaderOnmouseup(String onmouseup) {
        headerOnmouseup = onmouseup;
    }


    public String getBodyOnclick() {
        return ValueBindings.get(this, "bodyOnclick", bodyOnclick);
    }

    public void setBodyOnclick(String onclick) {
        bodyOnclick = onclick;
    }

    public String getBodyOndblclick() {
        return ValueBindings.get(this, "bodyOndblclick", bodyOndblclick);
    }

    public void setBodyOndblclick(String ondblclick) {
        bodyOndblclick = ondblclick;
    }

    public String getBodyOnmousedown() {
        return ValueBindings.get(this, "bodyOnmousedown", bodyOnmousedown);
    }

    public void setBodyOnmousedown(String onmousedown) {
        bodyOnmousedown = onmousedown;
    }

    public String getBodyOnmouseover() {
        return ValueBindings.get(this, "bodyOnmouseover", bodyOnmouseover);
    }

    public void setBodyOnmouseover(String onmouseover) {
        bodyOnmouseover = onmouseover;
    }

    public String getBodyOnmousemove() {
        return ValueBindings.get(this, "bodyOnmousemove", bodyOnmousemove);
    }

    public void setBodyOnmousemove(String onmousemove) {
        bodyOnmousemove = onmousemove;
    }

    public String getBodyOnmouseout() {
        return ValueBindings.get(this, "bodyOnmouseout", bodyOnmouseout);
    }

    public void setBodyOnmouseout(String onmouseout) {
        bodyOnmouseout = onmouseout;
    }

    public String getBodyOnmouseup() {
        return ValueBindings.get(this, "bodyOnmouseup", bodyOnmouseup);
    }

    public void setBodyOnmouseup(String onmouseup) {
        bodyOnmouseup = onmouseup;
    }


    public String getFooterOnclick() {
        return ValueBindings.get(this, "footerOnclick", footerOnclick);
    }

    public void setFooterOnclick(String onclick) {
        footerOnclick = onclick;
    }

    public String getFooterOndblclick() {
        return ValueBindings.get(this, "footerOndblclick", footerOndblclick);
    }

    public void setFooterOndblclick(String ondblclick) {
        footerOndblclick = ondblclick;
    }

    public String getFooterOnmousedown() {
        return ValueBindings.get(this, "footerOnmousedown", footerOnmousedown);
    }

    public void setFooterOnmousedown(String onmousedown) {
        footerOnmousedown = onmousedown;
    }

    public String getFooterOnmouseover() {
        return ValueBindings.get(this, "footerOnmouseover", footerOnmouseover);
    }

    public void setFooterOnmouseover(String onmouseover) {
        footerOnmouseover = onmouseover;
    }

    public String getFooterOnmousemove() {
        return ValueBindings.get(this, "footerOnmousemove", footerOnmousemove);
    }

    public void setFooterOnmousemove(String onmousemove) {
        footerOnmousemove = onmousemove;
    }

    public String getFooterOnmouseout() {
        return ValueBindings.get(this, "footerOnmouseout", footerOnmouseout);
    }

    public void setFooterOnmouseout(String onmouseout) {
        footerOnmouseout = onmouseout;
    }

    public String getFooterOnmouseup() {
        return ValueBindings.get(this, "footerOnmouseup", footerOnmouseup);
    }

    public void setFooterOnmouseup(String onmouseup) {
        footerOnmouseup = onmouseup;
    }

    public AbstractTable getTable() {
        UIComponent parent = getParent();
        while (parent instanceof ColumnGroup)
            parent = parent.getParent();
        if (parent != null && !(parent instanceof AbstractTable))
            throw new RuntimeException("Columns can only be inserted inside DataTable or TreeTable. Column id: " + getId());

        return (AbstractTable) parent;
    }

    @Override
    public void processUpdates(FacesContext context) {
        ValueExpression widthExpression = getValueExpression("width");
        if (widthExpression != null && !widthExpression.isReadOnly(context.getELContext())) {
            if (width != null) {
                widthExpression.setValue(context.getELContext(), width);
                width = null;
            }
        }
    }
}
