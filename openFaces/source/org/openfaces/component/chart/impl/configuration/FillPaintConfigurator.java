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
import org.openfaces.component.chart.impl.renderers.XYRendererAdapter;

import java.awt.*;
import java.util.Iterator;

/**
 * @author Eugene Goncharov
 */
public class FillPaintConfigurator extends AbstractConfigurator implements RendererConfigurator {
    private int seriesCount;

    public FillPaintConfigurator(int seriesCount) {
        this.seriesCount = seriesCount;
    }

    public void configure(ChartView view, ConfigurableRenderer configurableRenderer) {
        LineChartView chartView = (LineChartView) view;
        final boolean fillPaintsSpecified = chartView.getFillPaints() != null && !chartView.getFillPaints().isEmpty();
        AbstractRenderer renderer = (AbstractRenderer) configurableRenderer;

        if (chartView.getDefaultFillColor() != null || fillPaintsSpecified) {
            if (renderer instanceof XYRendererAdapter) {
                ((XYRendererAdapter) renderer).setUseFillPaint(true);
            } else if (renderer instanceof LineAndShapeRenderer) {
                ((LineAndShapeRenderer) renderer).setUseFillPaint(true);
            }
        }

        if (chartView.getDefaultFillColor() != null && !fillPaintsSpecified) {
            renderer.setBaseFillPaint(chartView.getDefaultFillColor());
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                renderer.setSeriesFillPaint(seriesIndex, chartView.getDefaultFillColor());
            }
        } else if (fillPaintsSpecified) {
            final Iterator fillPaintsIterator = chartView.getFillPaints().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (fillPaintsIterator.hasNext()) {
                    final Paint paint = (Paint) fillPaintsIterator.next();
                    renderer.setSeriesFillPaint(seriesIndex, paint);
                }
            }
        }
    }
}
