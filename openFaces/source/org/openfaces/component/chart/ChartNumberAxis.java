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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartNumberAxis extends ChartAxis {
    public static final double INTEGER_TICK_INTERVAL = -1;

    private Double lowerBound;
    private Double upperBound;
    private Boolean logarithmic;
    private Double tickInterval;

    public Double getLowerBound() {
        return ValueBindings.get(this, "lowerBound", lowerBound, null, Double.class);
    }

    public void setLowerBound(Double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public boolean isLogarithmic() {
        return ValueBindings.get(this, "logarithmic", logarithmic, false);
    }

    public void setLogarithmic(boolean value) {
        logarithmic = value;
    }

    public Double getUpperBound() {
        return ValueBindings.get(this, "upperBound", upperBound, null, Double.class);
    }

    public void setUpperBound(Double upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (getParent() instanceof ChartView) {
            GridChartView view = (GridChartView) getParent();
            view.getAxes().add(this);
        }
    }

    public Double getTickInterval() {
        return ValueBindings.get(this, "tickInterval", tickInterval, null, Double.class);
    }

    public void setTickInterval(Double tickInterval) {
        this.tickInterval = tickInterval;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                lowerBound,
                upperBound,
                logarithmic,
                tickInterval
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        lowerBound = (Double) state[i++];
        upperBound = (Double) state[i++];
        logarithmic = (Boolean) state[i++];
        tickInterval = (Double) state[i++];
    }

}
