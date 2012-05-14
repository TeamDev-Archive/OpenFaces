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

import org.openfaces.component.chart.impl.configuration.charts.BarChartConfigurator;
import org.openfaces.component.chart.impl.configuration.charts.ChartConfigurator;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.awt.*;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class BarChartView extends GridChartView {
    private Boolean showGradient;
    private Double g1WhitePosition = 0.1;
    private Double g2FullIntensityPosition = 0.2;
    private Double g3LightIntensityPosition = 0.8;

    private boolean showShadow = true;
    private Double shadowXOffset = 4.0;
    private Double shadowYOffset = 4.0;
    private Color shadowColor = Color.GRAY;

    @Override
    public String getFamily() {
        return "org.openfaces.BarChartView";
    }

    public String getHint() {
        return null;
    }

    public boolean isShowGradient() {
        return ValueBindings.get(this, "showGradient", showGradient, true);
    }

    public void setShowGradient(boolean showGradient) {
        this.showGradient = showGradient;
    }

    public double getG1WhitePosition() {
        return ValueBindings.get(this, "g1WhitePosition", g1WhitePosition, 0.1);
    }

    public void setG1WhitePosition(double g1WhitePosition) {
        this.g1WhitePosition = g1WhitePosition;
    }

    public double getG2FullIntensityPosition() {
        return ValueBindings.get(this, "g2FullIntensityPosition", g2FullIntensityPosition, 0.2);
    }

    public void setG2FullIntensityPosition(double g2FullIntensityPosition) {
        this.g2FullIntensityPosition = g2FullIntensityPosition;
    }

    public double getG3LightIntensityPosition() {
        return ValueBindings.get(this, "g3LightIntensityPosition", g3LightIntensityPosition, 0.8);
    }

    public void setG3LightIntensityPosition(double g3LightIntensityPosition) {
        this.g3LightIntensityPosition = g3LightIntensityPosition;
    }

    public Boolean isShowShadow() {
        return ValueBindings.get(this, "showShadow", showShadow, true, Boolean.class);
    }

    public void setShowShadow(Boolean showShadow) {
        this.showShadow = showShadow;
    }

    public double getShadowXOffset() {
        return ValueBindings.get(this, "shadowXOffset", shadowXOffset, 4.0);
    }

    public void setShadowXOffset(double shadowXOffset) {
        this.shadowXOffset = shadowXOffset;
    }

    public double getShadowYOffset() {
        return ValueBindings.get(this, "shadowYOffset", shadowYOffset, 4.0);
    }

    public void setShadowYOffset(double shadowYOffset) {
        this.shadowYOffset = shadowYOffset;
    }

    public Color getShadowColor() {
        return ValueBindings.get(this, "shadowColor", shadowColor, Color.class);
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);

        return new Object[]{superState,
                showGradient,
                g1WhitePosition,
                g2FullIntensityPosition,
                g3LightIntensityPosition,
                showShadow,
                shadowXOffset,
                shadowYOffset,
                saveAttachedState(context, shadowColor),
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;

        super.restoreState(facesContext, state[i++]);
        showGradient = (Boolean) state[i++];
        g1WhitePosition = (Double) state[i++];
        g2FullIntensityPosition = (Double) state[i++];
        g3LightIntensityPosition = (Double) state[i++];
        showShadow = (Boolean) state[i++];
        shadowXOffset = (Double) state[i++];
        shadowYOffset = (Double) state[i++];
        shadowColor = (Color) restoreAttachedState(facesContext, state[i++]);
    }

    @Override
    public ChartConfigurator getConfigurator() {
        final Chart chart = getChart();
        return new BarChartConfigurator(chart.getModel());
    }
}