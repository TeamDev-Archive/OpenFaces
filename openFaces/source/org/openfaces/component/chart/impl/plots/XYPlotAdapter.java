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

package org.openfaces.component.chart.impl.plots;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.RendererUtilities;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartDomain;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.component.chart.impl.generators.DynamicXYGenerator;
import org.openfaces.component.chart.impl.helpers.SelectionUtil;
import org.openfaces.component.chart.impl.renderers.AreaFillRenderer;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Eugene Goncharov
 */
public abstract class XYPlotAdapter extends XYPlot {
    private boolean keyAxisVisible;
    private boolean valueAxisVisible;
    private Chart chart;
    private GridChartView chartView;

    public XYPlotAdapter(XYDataset ds, AbstractXYItemRenderer renderer,
                         Chart chart, GridChartView view) {
        setDataset(ds);
        setRenderer(renderer);
        this.chart = chart;
        this.chartView = view;
        setOrientation(PropertiesConverter.toPlotOrientation(chartView.getOrientation()));

        ChartDomain showAxes = this.chartView.getShowAxes();
        if (showAxes == null) {
            showAxes = ChartDomain.BOTH;
            this.chartView.setShowAxes(showAxes);
        }

        keyAxisVisible = showAxes.equals(ChartDomain.BOTH) || showAxes.equals(ChartDomain.KEY);
        valueAxisVisible = showAxes.equals(ChartDomain.BOTH) || showAxes.equals(ChartDomain.VALUE);

        ValueAxis domainAxisAdapter = getDomainAxisAdapter();
        ValueAxis rangeAxisAdapter = getRangeAxisAdapter();

        setDomainAxis(domainAxisAdapter);
        setRangeAxis(rangeAxisAdapter);

        if (ds == null) {
            domainAxisAdapter.setVisible(false);
            rangeAxisAdapter.setVisible(false);
        }

        PlotUtil.initGridLabels(this.chartView, renderer);

        if (chartView.getLabels() == null || chartView.getLabels().getText() == null) {
            renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        }

        PlotUtil.setupGridLinesProperties(this.chartView, this, ds);

        setupColorProperties();
        setupTooltips();
        setupUrls();
        SelectionUtil.setupSelectionHighlighting(this, chart, chartView);
    }

    @Override
    public boolean render(Graphics2D g2, Rectangle2D dataArea, int index,
                          PlotRenderingInfo info, CrosshairState crosshairState) {
        final boolean customRenderingModeEnabled = getRenderer() != null && getRenderer() instanceof AreaFillRenderer;

        if (!customRenderingModeEnabled) {
            return super.render(g2, dataArea, index, info, crosshairState);
        } else {
            boolean foundData = false;
            XYDataset dataset = getDataset(index);
            if (!DatasetUtilities.isEmptyOrNull(dataset)) {
                foundData = true;
                ValueAxis xAxis = getDomainAxisForDataset(index);
                ValueAxis yAxis = getRangeAxisForDataset(index);
                if (xAxis == null || yAxis == null) {
                    return foundData;  // can't render anything without axes
                }
                XYItemRenderer renderer = getRenderer(index);
                if (renderer == null) {
                    renderer = getRenderer();
                    if (renderer == null) { // no default renderer available
                        return foundData;
                    }
                }

                XYItemRendererState state = renderer.initialise(g2, dataArea, this,
                        dataset, info);
                int passCount = renderer.getPassCount();

                SeriesRenderingOrder seriesOrder = getSeriesRenderingOrder();
                if (seriesOrder == SeriesRenderingOrder.REVERSE) {
                    //render series in reverse order
                    int seriesCount = dataset.getSeriesCount();
                    for (int series = seriesCount - 1; series >= 0; series--) {

                        for (int pass = 0; pass < passCount; pass++) {
                            int firstItem = 0;
                            int lastItem = dataset.getItemCount(series) - 1;
                            if (lastItem == -1) {
                                continue;
                            }
                            if (state.getProcessVisibleItemsOnly()) {
                                int[] itemBounds = RendererUtilities.findLiveItems(
                                        dataset, series, xAxis.getLowerBound(),
                                        xAxis.getUpperBound());
                                firstItem = Math.max(itemBounds[0] - 1, 0);
                                lastItem = Math.min(itemBounds[1] + 1, lastItem);
                            }
                            state.startSeriesPass(dataset, series, firstItem,
                                    lastItem, pass, passCount);
                            for (int item = firstItem; item <= lastItem; item++) {
                                renderer.drawItem(g2, state, dataArea, info,
                                        this, xAxis, yAxis, dataset, series, item,
                                        crosshairState, pass);
                            }
                            state.endSeriesPass(dataset, series, firstItem,
                                    lastItem, pass, passCount);
                        }
                    }
                } else {
                    //render series in forward order
                    int seriesCount = dataset.getSeriesCount();
                    for (int series = 0; series < seriesCount; series++) {

                        for (int pass = 0; pass < passCount; pass++) {
                            int firstItem = 0;
                            int lastItem = dataset.getItemCount(series) - 1;
                            if (state.getProcessVisibleItemsOnly()) {
                                int[] itemBounds = RendererUtilities.findLiveItems(
                                        dataset, series, xAxis.getLowerBound(),
                                        xAxis.getUpperBound());
                                firstItem = Math.max(itemBounds[0] - 1, 0);
                                lastItem = Math.min(itemBounds[1] + 1, lastItem);
                            }
                            state.startSeriesPass(dataset, series, firstItem,
                                    lastItem, pass, passCount);
                            for (int item = firstItem; item <= lastItem; item++) {
                                renderer.drawItem(g2, state, dataArea, info,
                                        this, xAxis, yAxis, dataset, series, item,
                                        crosshairState, pass);
                            }
                            state.endSeriesPass(dataset, series, firstItem,
                                    lastItem, pass, passCount);
                        }
                    }
                }
            }
            return foundData;
        }
    }

    abstract ValueAxis getDomainAxisAdapter();

    abstract ValueAxis getRangeAxisAdapter();

    public boolean isKeyAxisVisible() {
        return keyAxisVisible;
    }

    public void setKeyAxisVisible(boolean keyAxisVisible) {
        this.keyAxisVisible = keyAxisVisible;
    }

    public boolean isValueAxisVisible() {
        return valueAxisVisible;
    }

    public void setValueAxisVisible(boolean valueAxisVisible) {
        this.valueAxisVisible = valueAxisVisible;
    }

    public GridChartView getChartView() {
        return chartView;
    }

    public void setChartView(GridChartView chartView) {
        this.chartView = chartView;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    protected void setupColorProperties() {
        StyleObjectModel cssChartViewModel = chartView.getStyleObjectModel();

        if (chartView.getBackgroundPaint() != null) {
            setBackgroundPaint(null);
        } else {
            setBackgroundPaint(cssChartViewModel.getBackground());
        }

        StyleBorderModel border = cssChartViewModel.getBorder();
        setOutlinePaint(border == null || border.isNone()
                ? cssChartViewModel.getBackground()
                : border.getColor());
    }

    protected void setupTooltips() {
        if (chartView.getTooltip() != null) {
            getRenderer().setBaseToolTipGenerator(new XYToolTipGenerator() {
                public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                    return chartView.getTooltip();
                }
            });
        } else if (chartView.getDynamicTooltip() != null) {
            getRenderer().setBaseToolTipGenerator(new DynamicXYGenerator(chartView, chartView.getDynamicTooltip()));
        }
    }

    protected void setupUrls() {
        if (chartView.getUrl() != null) {
            getRenderer().setURLGenerator(new XYURLGenerator() {
                public String generateURL(XYDataset xyDataset, int i, int i1) {
                    return chartView.getUrl();
                }
            });
        } else if (chartView.getDynamicUrl() != null) {
            getRenderer().setURLGenerator(new DynamicXYGenerator(chartView, chartView.getDynamicUrl()));
        }
    }
}
