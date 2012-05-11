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

import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.LineStyle;
import org.openfaces.component.chart.impl.renderers.XYRendererAdapter;

import java.util.Iterator;

/**
 * @author Eugene Goncharov
 */
public class LineStrokeConfigurator extends AbstractConfigurator implements RendererConfigurator {
    private int seriesCount;

    public LineStrokeConfigurator(int seriesCount) {
        this.seriesCount = seriesCount;
    }

    public void configure(ChartView view, ConfigurableRenderer configurableRenderer) {
        LineChartView chartView = (LineChartView) view;

        final boolean lineStylesSpecified = chartView.getLineStyles() != null && !chartView.getLineStyles().isEmpty();
        AbstractRenderer renderer = (AbstractRenderer) configurableRenderer;

        if (renderer instanceof XYRendererAdapter) {
            ((XYRendererAdapter) renderer).setBaseShapesVisible(chartView.isShapesVisible());
        } else if (renderer instanceof LineAndShapeRenderer) {
            ((LineAndShapeRenderer) renderer).setBaseShapesVisible(chartView.isShapesVisible());
        }

        if (chartView.getDefaultLineStyle() != null && !lineStylesSpecified) {
            renderer.setBaseStroke(chartView.getDefaultLineStyle().getStroke());
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                renderer.setSeriesStroke(seriesIndex, chartView.getDefaultLineStyle().getStroke());
            }
        } else if (lineStylesSpecified) {
            final Iterator strokesIterator = chartView.getLineStyles().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (strokesIterator.hasNext()) {
                    final LineStyle lineStyle = (LineStyle) strokesIterator.next();
                    renderer.setSeriesStroke(seriesIndex, lineStyle.getStroke());
                }
            }
        }
    }
}
