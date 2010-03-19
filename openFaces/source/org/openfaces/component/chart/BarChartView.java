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

import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.chart.renderer.xy.GradientXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.plots.GridCategoryPlotAdapter;
import org.openfaces.component.chart.impl.plots.GridDatePlotAdapter;
import org.openfaces.component.chart.impl.plots.GridXYPlotAdapter;
import org.openfaces.component.chart.impl.renderers.BarRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.BarRendererAdapter;
import org.openfaces.component.chart.impl.renderers.XYBarRendererAdapter;

import java.awt.*;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class BarChartView extends GridChartView {

    @Override
    public String getFamily() {
        return "org.openfaces.BarChartView";
    }

    public String getHint() {
        return null;
    }

    protected Plot createPlot(Chart chart, ChartModel model, ModelInfo info) {
        if (info.getModelType().equals(ModelType.Number)) {
            XYDataset ds = ModelConverter.toXYSeriesCollection(info);
            XYBarRenderer renderer = new XYBarRendererAdapter(this);
            configureRendererPresentation(renderer);
            return new GridXYPlotAdapter(ds, renderer, chart, this);
        }
        if (info.getModelType().equals(ModelType.Date)) {
            TimeSeriesCollection ds = ModelConverter.toTimeSeriesCollection(info);
            XYBarRenderer renderer = new XYBarRendererAdapter(this);
            configureRendererPresentation(renderer);
            return new GridDatePlotAdapter(ds, renderer, chart, this);
        }
        CategoryDataset ds = ModelConverter.toCategoryDataset(info);
        BarRenderer renderer = isEnable3D()
                ? new BarRenderer3DAdapter(this)
                : new BarRendererAdapter(this);
        configureRendererPresentation(renderer);
        return new GridCategoryPlotAdapter(ds, renderer, chart, this);
    }

    private void configureRendererPresentation(BarRenderer renderer) {
        double g1WhitePosition = 0.1;
        double g2FullIntensityPosition = 0.2;
        double g3LightIntensityPosition = 0.8;
        boolean shadowVisible = true;
        int shadowXOffset = 4;
        int shadowYOffset = 4;
        boolean outlineVisible = false;
        Color outlineColor = Color.GRAY;

        renderer.setBarPainter(new GradientBarPainter(g1WhitePosition, g2FullIntensityPosition, g3LightIntensityPosition));
        renderer.setShadowVisible(shadowVisible);
        renderer.setShadowXOffset(shadowXOffset);
        renderer.setShadowYOffset(shadowYOffset);
        renderer.setShadowPaint(Color.LIGHT_GRAY);
        renderer.setDrawBarOutline(outlineVisible);
        renderer.setBaseOutlinePaint(outlineColor);
    }

    private void configureRendererPresentation(XYBarRenderer renderer) {
        double g1WhitePosition = 0;
        double g2FullIntensityPosition = 0;
        double g3LightIntensityPosition = 0.9;
        boolean shadowVisible = true;
        int shadowXOffset = 4;
        int shadowYOffset = 4;
        boolean outlineVisible = true;
        Color outlineColor = Color.BLUE;

        renderer.setBarPainter(new GradientXYBarPainter(g1WhitePosition, g2FullIntensityPosition, g3LightIntensityPosition));
        renderer.setShadowVisible(shadowVisible);
        renderer.setShadowXOffset(shadowXOffset);
        renderer.setShadowYOffset(shadowYOffset);
        renderer.setDrawBarOutline(outlineVisible);
        renderer.setBaseOutlinePaint(outlineColor);
    }

}