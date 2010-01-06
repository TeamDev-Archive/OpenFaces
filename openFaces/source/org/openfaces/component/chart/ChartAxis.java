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

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartAxis extends UIOutput implements StyledComponent, HasLabels {
    private String style;
    private boolean labelVisible = true;
    private boolean ticksVisible = true;
    private boolean tickLabelsVisible = true;
    private ChartAxisTicks ticks;
    private ChartLabels labels;
    private ChartDomain domain;

    public ChartAxis() {
        setRendererType(null);
    }

    public boolean isTickLabelsVisible() {
        return tickLabelsVisible;
    }

    public void setTickLabelsVisible(boolean tickLabelsVisible) {
        this.tickLabelsVisible = tickLabelsVisible;
    }

    public ChartLabels getLabels() {
        return labels;
    }

    public void setLabels(ChartLabels labels) {
        this.labels = labels;
    }

    public ChartAxisTicks getTicks() {
        return ticks;
    }

    public void setTicks(ChartAxisTicks ticks) {
        this.ticks = ticks;
    }

    public ChartDomain getDomain() {
        return ValueBindings.get(this, "domain", domain, ChartDomain.class);
    }

    public void setDomain(ChartDomain domain) {
        this.domain = domain;
    }

    public boolean isLabelVisible() {
        return labelVisible;
    }

    public void setLabelVisible(boolean labelVisible) {
        this.labelVisible = labelVisible;
    }

    public boolean isTicksVisible() {
        return ticksVisible;
    }

    public void setTicksVisible(boolean ticksVisible) {
        this.ticksVisible = ticksVisible;
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
        StyledComponent[] chain = new StyledComponent[1];
        chain[0] = this;
        return chain;
    }

    public String getHint() {
        return "border";
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (getParent() instanceof ChartView) {
            GridChartView view = (GridChartView) getParent();
            view.getAxes().add(this);

        }
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style,
                labelVisible,
                ticksVisible,
                tickLabelsVisible,
                saveAttachedState(context, domain),
                saveAttachedState(context, ticks)
        };

    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;

        super.restoreState(facesContext, state[i++]);
        style = (String) state[i++];
        labelVisible = (Boolean) state[i++];
        ticksVisible = (Boolean) state[i++];
        tickLabelsVisible = (Boolean) state[i++];
        domain = (ChartDomain) restoreAttachedState(facesContext, state[i++]);
        ticks = (ChartAxisTicks) restoreAttachedState(facesContext, state[i++]);
    }

}
