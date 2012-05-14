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

import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.openfaces.component.chart.BarChartView;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.impl.renderers.GradientXYBarPainterAdapter;
import org.openfaces.component.chart.impl.renderers.StandardXYBarPainterAdapter;

/**
 * @author Eugene Goncharov
 */
public class BarsPainterConfigurator extends AbstractConfigurator implements RendererConfigurator {

    public BarsPainterConfigurator() {
    }

    public void configure(ChartView view, ConfigurableRenderer renderer) {
        BarChartView chartView = (BarChartView) view;
        Boolean showGradient = chartView.isShowGradient();

        if (renderer instanceof XYBarRenderer) {
            XYBarRenderer xyBarRenderer = (XYBarRenderer) renderer;

            if (showGradient) {
                validateGradientParameters(chartView.getG1WhitePosition(),
                        chartView.getG2FullIntensityPosition(), chartView.getG3LightIntensityPosition());

                GradientXYBarPainterAdapter gradientPainter =
                        new GradientXYBarPainterAdapter(chartView.getG1WhitePosition(),
                                chartView.getG2FullIntensityPosition(), chartView.getG3LightIntensityPosition());

                xyBarRenderer.setBarPainter(gradientPainter);
            } else {
                xyBarRenderer.setBarPainter(new StandardXYBarPainterAdapter());
            }
        } else if (renderer instanceof BarRenderer) {
            BarRenderer barRenderer = (BarRenderer) renderer;
            if (showGradient) {
                validateGradientParameters(chartView.getG1WhitePosition(),
                        chartView.getG2FullIntensityPosition(), chartView.getG3LightIntensityPosition());

                GradientBarPainter gradientPainter = new GradientBarPainter(chartView.getG1WhitePosition(),
                        chartView.getG2FullIntensityPosition(), chartView.getG3LightIntensityPosition());
                barRenderer.setBarPainter(gradientPainter);
            } else {
                barRenderer.setBarPainter(new StandardBarPainter());
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void validateGradientParameters(Double g1WhitePosition,
                                            Double g2FullIntensityPosition,
                                            Double g3LightIntensityPosition) {

        g1WhitePosition = checkBoundaryValues(g1WhitePosition);
        g2FullIntensityPosition = checkBoundaryValues(g2FullIntensityPosition);
        g3LightIntensityPosition = checkBoundaryValues(g3LightIntensityPosition);

        if (g1WhitePosition > g2FullIntensityPosition
                || g1WhitePosition > g3LightIntensityPosition
                || g2FullIntensityPosition > g3LightIntensityPosition) {
            throw new IllegalArgumentException("Gradient parameters are incorrect.");
        }
    }

    private double checkBoundaryValues(double position) {
        double correctPosition = position;

        if (position < 0) {
            correctPosition = 0;
        } else if (position > 1) {
            correctPosition = 1;
        }

        return correctPosition;
    }
}
