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

import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineProperties extends UIOutput implements StyledComponent, Serializable {
    private String style;
    private Boolean shapesVisible;
    private Boolean showInLegend;
    private Boolean hideSeries;
    private Boolean labelsVisible;

    public LineProperties() {
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

    public void setDynamicCondition(ChartViewValueExpression dynamicCondition) {
        setValueExpression("condition", dynamicCondition);
    }

    public boolean getShapesVisible() {
        return ValueBindings.get(this, "shapesVisible", shapesVisible, false);
    }

    public boolean getShowInLegend() {
        return ValueBindings.get(this, "showInLegend", shapesVisible, false);
    }

    public boolean getHideSeries() {
        return ValueBindings.get(this, "hideSeries", hideSeries, false);
    }

    public boolean getLabelsVisible() {
        return ValueBindings.get(this, "labelsVisible", labelsVisible, false);
    }

    public void setShapesVisible(boolean shapesVisible) {
        this.shapesVisible = shapesVisible;
    }

    public void setShowInLegend(boolean showInLegend) {
        this.showInLegend = showInLegend;
    }

    public void setHideSeries(boolean hideSeries) {
        this.hideSeries = hideSeries;
    }

    public void setLabelsVisible(boolean labelsVisible) {
        this.labelsVisible = labelsVisible;
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

        if (this.getParent() instanceof LineChartView) {
            LineChartView lineChartView = (LineChartView) this.getParent();
            lineChartView.getLinePropertiesList().add(this);
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
        setValueExpression("condition", getValueExpression("condition"));
        Object superState = super.saveState(context);
        return new Object[]{superState,
                style,
                shapesVisible,
                showInLegend
        };

    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;

        super.restoreState(facesContext, state[i++]);
        style = (String) state[i++];
        shapesVisible = (Boolean) state[i++];
        showInLegend = (Boolean) state[i++];


    }
}

