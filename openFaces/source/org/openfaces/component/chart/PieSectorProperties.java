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
public class PieSectorProperties extends UIOutput implements StyledComponent {
    private String style;
    private Float pulled;

    public PieSectorProperties() {
        setRendererType(null);
    }

    public ChartViewValueExpression getDynamicCondition() {
        ValueExpression ve = getValueExpression("condition");
        if (ve == null)
            return null;

        if (ve instanceof ChartViewValueExpression)
            return (ChartViewValueExpression) ve;

        return new ChartViewValueExpression(ve);
    }

    public void setDynamicCondition(ChartViewValueExpression dynamicTooltip) {
        setValueExpression("condition", dynamicTooltip);
    }

    public Float getPulled() {
        return ValueBindings.get(this, "pulled", pulled, Float.class);
    }

    public void setPulled(Float pulled) {
        this.pulled = pulled;
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

    @Override
    public void setParent(UIComponent uiComponent) {
        super.setParent(uiComponent);

        if (this.getParent() instanceof PieChartView) {
            PieChartView sectorsComponent = (PieChartView) this.getParent();
            sectorsComponent.getSectors().add(this);
        }

    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        StyledComponent[] chain = new StyledComponent[1];
        chain[0] = this;
        return chain;

    }

    public String getHint() {
        return null;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style, pulled
        };

    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        //   PieChartView sectorsComponent = (PieChartView) this.getParent();
        //   sectorsComponent.getSectors().add(this);

        super.restoreState(facesContext, state[i++]);
        style = (String) state[i++];
        pulled = (Float) state[i++];


    }
}
