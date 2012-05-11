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

package org.openfaces.component.chart.impl.configuration;

import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.openfaces.component.chart.BarChartView;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.impl.renderers.XYBarRendererAdapter;

/**
 * @author Eugene Goncharov
 */
public class ShadowConfigurator extends AbstractConfigurator implements RendererConfigurator, PlotConfigurator {

    public ShadowConfigurator() {
    }

    public void configure(ConfigurablePlot plot, ChartView view) {
        PieChartView pieChartView = (PieChartView) view;
        PiePlot piePlot = (PiePlot) plot;

        piePlot.setShadowPaint(pieChartView.getShadowColor());
        piePlot.setShadowXOffset(pieChartView.getShadowXOffset());
        piePlot.setShadowYOffset(pieChartView.getShadowYOffset());
    }

    public void configure(ChartView view, ConfigurableRenderer renderer) {
        BarChartView chartView = (BarChartView) view;
        if (chartView.isShowShadow()) {
            validateShadowOffsetParameters(chartView);
        }

        if (renderer instanceof BarRenderer || renderer instanceof XYBarRendererAdapter) {
            if (renderer instanceof BarRenderer) {
                configureShadowProperties(chartView, (BarRenderer) renderer);
            } else {
                configureShadowProperties(chartView, (XYBarRendererAdapter) renderer);
            }
        }
    }

    private void configureShadowProperties(BarChartView chartView, BarRenderer renderer) {
        renderer.setShadowVisible(chartView.isShowShadow());
        renderer.setShadowXOffset(chartView.getShadowXOffset());
        renderer.setShadowYOffset(chartView.getShadowYOffset());

        renderer.setShadowPaint(chartView.getShadowColor());
    }

    private void configureShadowProperties(BarChartView chartView, XYBarRendererAdapter renderer) {
        renderer.setShadowVisible(chartView.isShowShadow());
        renderer.setShadowXOffset(chartView.getShadowXOffset());
        renderer.setShadowYOffset(chartView.getShadowYOffset());

        renderer.setShadowPaint(chartView.getShadowColor());
    }

    private void validateShadowOffsetParameters(BarChartView chartView) {
        if (chartView.getShadowXOffset() < 0) {
            chartView.setShadowXOffset(0.0);
        }

        if (chartView.getShadowYOffset() < 0) {
            chartView.setShadowYOffset(0.0);
        }
    }
}
