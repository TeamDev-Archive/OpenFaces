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

package org.openfaces.component.chart.impl.configuration.charts;

import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.BarChartView;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.configuration.BarsPainterConfigurator;
import org.openfaces.component.chart.impl.configuration.ConfigurableRenderer;
import org.openfaces.component.chart.impl.configuration.GridLabelsConfigurator;
import org.openfaces.component.chart.impl.configuration.ItemsColorConfigurator;
import org.openfaces.component.chart.impl.configuration.OutlineConfigurator;
import org.openfaces.component.chart.impl.configuration.ShadowConfigurator;
import org.openfaces.component.chart.impl.configuration.TooltipsConfigurator;
import org.openfaces.component.chart.impl.configuration.UrlsConfigurator;
import org.openfaces.component.chart.impl.plots.GridCategoryPlotAdapter;
import org.openfaces.component.chart.impl.plots.GridDatePlotAdapter;
import org.openfaces.component.chart.impl.plots.GridXYPlotAdapter;
import org.openfaces.component.chart.impl.renderers.BarRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.BarRendererAdapter;
import org.openfaces.component.chart.impl.renderers.XYBarRendererAdapter;

/**
 * @author Eugene Goncharov
 */
public class BarChartConfigurator extends GridChartConfigurator {

    public BarChartConfigurator(ChartModel model) {
        super(model);
    }

    public Plot configurePlot(Chart chart, ModelInfo info) {
        return createPlot(chart, info);
    }

    private Plot createPlot(Chart chart, ModelInfo info) {
        final BarChartView chartView = (BarChartView) chart.getChartView();

        if (info.getModelType().equals(ModelType.Number)) {
            XYDataset ds = ModelConverter.toXYSeriesCollection(info);
            XYBarRenderer renderer = new XYBarRendererAdapter();
            ConfigurableRenderer configurableRenderer = (ConfigurableRenderer) renderer;

            int seriesCount = ds != null ? ds.getSeriesCount() : 0;
            configure(chartView, configurableRenderer, seriesCount);

            final GridXYPlotAdapter xyPlotAdapter = new GridXYPlotAdapter(ds, renderer, chartView);
            initMarkers(chart, xyPlotAdapter);

            return xyPlotAdapter;
        }

        if (info.getModelType().equals(ModelType.Date)) {
            TimeSeriesCollection ds = ModelConverter.toTimeSeriesCollection(chart, info);
            XYBarRenderer renderer = new XYBarRendererAdapter();
            ConfigurableRenderer configurableRenderer = (ConfigurableRenderer) renderer;

            int seriesCount = ds != null ? ds.getSeriesCount() : 0;
            configure(chartView, configurableRenderer, seriesCount);

            final GridDatePlotAdapter datePlotAdapter = new GridDatePlotAdapter(ds, renderer, chartView);
            initMarkers(chart, datePlotAdapter);

            return datePlotAdapter;
        }

        CategoryDataset ds = ModelConverter.toCategoryDataset(info);
        int rowCount = ds != null ? ds.getRowCount() : 0;

        BarRenderer renderer = chartView.isEnable3D()
                ? new BarRenderer3DAdapter()
                : new BarRendererAdapter();

        ConfigurableRenderer configurableRenderer = (ConfigurableRenderer) renderer;
        configure(chartView, configurableRenderer, rowCount);

        if (chartView.isEnable3D() && renderer instanceof BarRenderer3DAdapter) {
            ((BarRenderer3DAdapter) renderer).setWallPaint(chartView.getWallColor());
        }

        final GridCategoryPlotAdapter gridCategoryPlot = new GridCategoryPlotAdapter(ds, renderer, chartView);
        initMarkers(chart, gridCategoryPlot);

        return gridCategoryPlot;
    }

    private void configure(ChartView chartView, ConfigurableRenderer configurableRenderer, int seriesCount) {
        configurableRenderer.addConfigurator(new ItemsColorConfigurator());
        configurableRenderer.addConfigurator(new ShadowConfigurator());
        configurableRenderer.addConfigurator(new BarsPainterConfigurator());
        configurableRenderer.addConfigurator(new OutlineConfigurator(seriesCount));
        configurableRenderer.addConfigurator(new GridLabelsConfigurator());
        configurableRenderer.addConfigurator(new TooltipsConfigurator());
        configurableRenderer.addConfigurator(new UrlsConfigurator());

        configurableRenderer.configure(chartView);
    }
}
