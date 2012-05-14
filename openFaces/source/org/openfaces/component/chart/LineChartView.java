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

import org.openfaces.component.chart.impl.configuration.charts.ChartConfigurator;
import org.openfaces.component.chart.impl.configuration.charts.LineChartConfigurator;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineChartView extends GridChartView {
    private Boolean shapesVisible;
    private List<LineProperties> linePropertiesList = new ArrayList<LineProperties>();

    private Paint defaultFillColor;
    private LineStyle defaultLineStyle;
    private Collection lineStyles;
    private Collection fillPaints;

    public boolean isShapesVisible() {
        return ValueBindings.get(this, "shapesVisible", shapesVisible, true);
    }

    public void setShapesVisible(boolean shapesVisible) {
        this.shapesVisible = shapesVisible;
    }

    public List<LineProperties> getLinePropertiesList() {
        return linePropertiesList;
    }

    public void addLinePropertiesList(LineProperties lineProperties) {
        if (lineProperties == null)
            return;

        if (linePropertiesList == null)
            linePropertiesList = new ArrayList<LineProperties>();

        linePropertiesList.add(lineProperties);
    }

    @Override
    public String getFamily() {
        return "org.openfaces.LineChartView";
    }

    public String getHint() {
        return null;
    }

    public Paint getDefaultFillColor() {
        return ValueBindings.get(this, "defaultFillColor", defaultFillColor, Paint.class);
    }

    public void setDefaultFillColor(Paint defaultFillColor) {
        this.defaultFillColor = defaultFillColor;
    }

    public LineStyle getDefaultLineStyle() {
        return ValueBindings.get(this, "defaultLineStyle", defaultLineStyle, new LineStyle(Color.RED, new BasicStroke(1.25f)), LineStyle.class);
    }

    public void setDefaultLineStyle(LineStyle defaultLineStyle) {
        this.defaultLineStyle = defaultLineStyle;
    }


    public Collection getLineStyles() {
        return ValueBindings.get(this, "lineStyles", lineStyles, Collection.class);
    }

    public void setLineStyles(Collection lineStyles) {
        this.lineStyles = lineStyles;
    }

    public Collection getFillPaints() {
        return ValueBindings.get(this, "fillPaints", fillPaints, Collection.class);
    }

    public void setFillPaints(Collection fillPaints) {
        this.fillPaints = fillPaints;
    }

    public LineAreaFill getLineAreaFill() {
        List<UIComponent> children = getChildren();
        LineAreaFill lineAreaFill = null;
        for (UIComponent child : children) {
            if (child instanceof LineAreaFill) {
                if (lineAreaFill != null)
                    throw new RuntimeException("There should be only one LineAreaFill child under this component: " + getId());
                lineAreaFill = (LineAreaFill) child;
            }
        }

        return lineAreaFill;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                shapesVisible,
                saveAttachedState(context, linePropertiesList),
                saveAttachedState(context, defaultFillColor),
                saveAttachedState(context, fillPaints),
                saveAttachedState(context, defaultLineStyle),
                saveAttachedState(context, lineStyles)
        };

    }

    @SuppressWarnings("unchecked")
    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        shapesVisible = (Boolean) state[i++];
        linePropertiesList = (List<LineProperties>) restoreAttachedState(facesContext, state[i++]);
        defaultFillColor = (Paint) restoreAttachedState(facesContext, state[i++]);
        fillPaints = (Collection<Paint>) restoreAttachedState(facesContext, state[i++]);
        defaultLineStyle = (LineStyle) restoreAttachedState(facesContext, state[i++]);
        lineStyles = (Collection<LineStyle>) restoreAttachedState(facesContext, state[i++]);
    }

    @Override
    public ChartConfigurator getConfigurator() {
        final Chart chart = getChart();
        return new LineChartConfigurator(chart.getModel());
    }
}
