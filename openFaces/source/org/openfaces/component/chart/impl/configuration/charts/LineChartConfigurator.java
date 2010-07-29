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

package org.openfaces.component.chart.impl.configuration.charts;

import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.configuration.*;
import org.openfaces.component.chart.impl.plots.GridCategoryPlotAdapter;
import org.openfaces.component.chart.impl.plots.GridDatePlotAdapter;
import org.openfaces.component.chart.impl.plots.GridXYPlotAdapter;
import org.openfaces.component.chart.impl.renderers.LineFillRenderer;
import org.openfaces.component.chart.impl.renderers.LineRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.LineRendererAdapter;
import org.openfaces.component.chart.impl.renderers.XYLineFillRenderer;
import org.openfaces.component.chart.impl.renderers.XYLineRenderer3DAdapter;
import org.openfaces.component.chart.impl.renderers.XYLineRendererAdapter;

/**
 * @author Eugene Goncharov
 */
public class LineChartConfigurator extends GridChartConfigurator {

    public LineChartConfigurator(Chart chart, ChartModel model) {
        super(chart, model);
    }

    public Plot configurePlot(ModelInfo info) {
        return createPlot(getChart(), info);
    }

    private Plot createPlot(Chart chart, ModelInfo info) {
        final LineChartView chartView = (LineChartView) getChartView();

        if (info.getModelType().equals(ModelType.Number)) {
            XYDataset ds = ModelConverter.toXYSeriesCollection(info);

            AbstractXYItemRenderer renderer = createRenderer(ds);

            final GridXYPlotAdapter xyPlot = new GridXYPlotAdapter(ds, renderer, chart, chartView);
            initMarkers(xyPlot);

            return xyPlot;
        }

        if (info.getModelType().equals(ModelType.Date)) {
            TimeSeriesCollection ds = ModelConverter.toTimeSeriesCollection(chart, info);

            AbstractXYItemRenderer renderer = createRenderer(ds);

            final GridDatePlotAdapter xyPlot = new GridDatePlotAdapter(ds, renderer, chart, chartView);
            initMarkers(xyPlot);

            return xyPlot;
        }

        CategoryDataset ds = ModelConverter.toCategoryDataset(info);
        LineAndShapeRenderer renderer;

        if (chartView.getLineAreaFill() != null) {
            renderer = new LineFillRenderer();
        } else {
            renderer = chartView.isEnable3D()
                    ? new LineRenderer3DAdapter()
                    : new LineRendererAdapter();
        }

        if (getChart().getChartSelection() != null && !chartView.isShapesVisible()) {
            throw new IllegalStateException("Chart selection is unsupported with disabled shapes.");
        }

        int rowCount = ds != null ? ds.getRowCount() : 0;
        ConfigurableRenderer configurableRenderer = (ConfigurableRenderer) renderer;

        configure(chartView, configurableRenderer, ds, rowCount);

        final GridCategoryPlotAdapter gridCategoryPlot = new GridCategoryPlotAdapter(ds, renderer, chart, chartView);
        initMarkers(gridCategoryPlot);

        return gridCategoryPlot;
    }

    private AbstractXYItemRenderer createRenderer(XYDataset ds) {
        final LineChartView chartView = (LineChartView) getChartView();
        AbstractXYItemRenderer renderer;

        if (chartView.getLineAreaFill() != null) {
            renderer = new XYLineFillRenderer();
        } else {
            renderer = chartView.isEnable3D()
                    ? new XYLineRenderer3DAdapter()
                    : new XYLineRendererAdapter();
        }

        if (getChart().getChartSelection() != null && !chartView.isShapesVisible()) {
            throw new IllegalStateException("Chart selection is unsupported with disabled shapes.");
        }

        int seriesCount = ds != null ? ds.getSeriesCount() : 0;
        ConfigurableRenderer configurableRenderer = (ConfigurableRenderer) renderer;
        configure(chartView, configurableRenderer, ds, seriesCount);

        return renderer;
    }


    private void configure(LineChartView chartView, ConfigurableRenderer configurableRenderer, Dataset ds, int rowCount) {
        configurableRenderer.addConfigurator(new SeriesColorConfigurator(chartView));
        configurableRenderer.addConfigurator(new LineAndShapePropertiesConfigurator(chartView, ds));
        configurableRenderer.addConfigurator(new OutlineConfigurator(chartView, rowCount));
        configurableRenderer.addConfigurator(new FillPaintConfigurator(chartView, rowCount));
        configurableRenderer.addConfigurator(new LineStrokeConfigurator(chartView, rowCount));
        configurableRenderer.addConfigurator(new Chart3DRendererConfigurator(chartView));
        configurableRenderer.addConfigurator(new AreaFillConfigurator(chartView));
        configurableRenderer.addConfigurator(new GridLabelsConfigurator(chartView));
        configurableRenderer.addConfigurator(new TooltipsConfigurator(chartView));
        configurableRenderer.addConfigurator(new UrlsConfigurator(chartView));

        configurableRenderer.configure();
    }

}
