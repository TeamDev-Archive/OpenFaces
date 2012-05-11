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
package org.openfaces.component.chart;

import org.openfaces.util.ValueBindings;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartLabels extends UIOutput implements StyledComponent {
    private String style;
    private String text;

    /**
     * This method is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
     * by any application code.
     */
    public ChartViewValueExpression getDynamicText() {
        ValueExpression ve = getValueExpression("text");
        if (ve == null)
            return null;

        if (ve instanceof ChartViewValueExpression)
            return (ChartViewValueExpression) ve;

        return new ChartViewValueExpression(ve);
    }

    public void setDynamicText(ChartViewValueExpression dynamicTooltip) {
        setValueExpression("text", dynamicTooltip);
    }

    public ChartLabels() {
        setRendererType(null);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setTextStyle(String style) {
        this.style = style;
    }

    public void setStyle(String style) {
        setTextStyle(style);
    }

    public String getStyle() {
        return getTextStyle();
    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        UIComponent parent = this.getParent();
        if (!(parent instanceof StyledComponent))
            return new StyledComponent[]{this};

        StyledComponent[] parentChain = ((StyledComponent) this.getParent()).getComponentsChain();

        if (parentChain == null || parentChain.length == 0)
            return new StyledComponent[]{this};

        StyledComponent[] chain = new StyledComponent[parentChain.length + 1];

        //copy elements from parent chain
        System.arraycopy(parentChain, 0, chain, 0, parentChain.length);

        chain[chain.length - 1] = this;

        return chain;
    }

    public String getHint() {
        return null;
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (this.getParent() instanceof HasLabels) {
            HasLabels labeledComponent = (HasLabels) this.getParent();
            labeledComponent.setLabels(this);
        } else if (this.getParent() != null)
            throw new IllegalStateException("Incorrect place for Label tag. Parent component must implement HasLabel interface. Currently defined component: " + getParent());
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style, text};
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        style = (String) state[i++];
        text = (String) state[i++];

    }
}
