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

package org.openfaces.component.chart.impl.plots;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.RendererUtilities;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.ChartDomain;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.component.chart.impl.configuration.ConfigurablePlot;
import org.openfaces.component.chart.impl.configuration.PlotColorsConfigurator;
import org.openfaces.component.chart.impl.configuration.PlotConfigurator;
import org.openfaces.component.chart.impl.configuration.PlotGridLinesConfigurator;
import org.openfaces.component.chart.impl.configuration.PlotSelectionConfigurator;
import org.openfaces.component.chart.impl.renderers.AreaFillRenderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

/**
 * @author Eugene Goncharov
 */
public abstract class XYPlotAdapter extends XYPlot implements ConfigurablePlot {
    private ConfigurablePlotBase configurationDelegate = new ConfigurablePlotBase();
    private boolean keyAxisVisible;
    private boolean valueAxisVisible;

    public XYPlotAdapter(XYDataset ds, AbstractXYItemRenderer renderer,
                         GridChartView view) {
        setDataset(ds);
        setRenderer(renderer);

        ChartDomain showAxes = view.getShowAxes();
        if (showAxes == null) {
            showAxes = ChartDomain.BOTH;
            view.setShowAxes(showAxes);
        }

        keyAxisVisible = showAxes.equals(ChartDomain.BOTH) || showAxes.equals(ChartDomain.KEY);
        valueAxisVisible = showAxes.equals(ChartDomain.BOTH) || showAxes.equals(ChartDomain.VALUE);

        ValueAxis domainAxisAdapter = getDomainAxisAdapter(view);
        ValueAxis rangeAxisAdapter = getRangeAxisAdapter(view);

        setDomainAxis(domainAxisAdapter);
        setRangeAxis(rangeAxisAdapter);

        if (ds == null) {
            domainAxisAdapter.setVisible(false);
            rangeAxisAdapter.setVisible(false);
        }

        setOrientation(PropertiesConverter.toPlotOrientation(view.getOrientation()));

        addConfigurator(new PlotGridLinesConfigurator(ds));
        addConfigurator(new PlotColorsConfigurator());
        addConfigurator(new PlotSelectionConfigurator());

        configure(view);
    }

    @Override
    public boolean render(Graphics2D g2, Rectangle2D dataArea, int index,
                          PlotRenderingInfo info, CrosshairState crosshairState) {
        final XYItemRenderer renderer = getRenderer();
        final boolean customRenderingModeEnabled = renderer instanceof AreaFillRenderer;

        if (!customRenderingModeEnabled) {
            return super.render(g2, dataArea, index, info, crosshairState);
        }

        XYDataset xyDataset = getDataset(index);
        final boolean isDataSetNotEmpty = !DatasetUtilities.isEmptyOrNull(xyDataset);

        if (!isDataSetNotEmpty) {
            return false;
        }
        ValueAxis xValueAxis = getDomainAxisForDataset(index);
        ValueAxis yValueAxis = getRangeAxisForDataset(index);
        XYItemRenderer xyItemRenderer = findRenderer(index);

        if (xValueAxis == null || yValueAxis == null || xyItemRenderer == null) {
            return false;
        }

        XYItemRendererState state = xyItemRenderer.initialise(g2, dataArea, this, xyDataset, info);
        boolean isReverseSeriesRenderingOrder = getSeriesRenderingOrder() == SeriesRenderingOrder.REVERSE;
        int totalRendererPasses = xyItemRenderer.getPassCount();
        int totalSeries = xyDataset.getSeriesCount();

        if (isReverseSeriesRenderingOrder) {
            for (int seriesIndex = totalSeries - 1; seriesIndex >= 0; seriesIndex--) {
                renderItems(g2, dataArea, info, crosshairState, xyDataset, xValueAxis, yValueAxis,
                        xyItemRenderer, state, totalRendererPasses,
                        seriesIndex);
            }
        } else {
            for (int seriesIndex = 0; seriesIndex < totalSeries; seriesIndex++) {
                renderItems(g2, dataArea, info, crosshairState, xyDataset, xValueAxis, yValueAxis,
                        xyItemRenderer, state, totalRendererPasses,
                        seriesIndex);
            }
        }

        return true;
    }

    private void renderItems(Graphics2D g2, Rectangle2D dataArea, PlotRenderingInfo info, CrosshairState crosshairState,
                             XYDataset xyDataset, ValueAxis xValueAxis, ValueAxis yValueAxis,
                             XYItemRenderer xyItemRenderer, XYItemRendererState state,
                             int totalRendererPasses, int seriesIndex) {
        for (int currentPassIndex = 0; currentPassIndex < totalRendererPasses; currentPassIndex++) {
            if (xyDataset.getItemCount(seriesIndex) == 0) {
                return;
            }

            int firstItemIndex = 0;
            int lastItemIndex = xyDataset.getItemCount(seriesIndex) - 1;

            if (state.getProcessVisibleItemsOnly()) {
                final double xLowerBound = xValueAxis.getLowerBound();
                final double xUpperBound = xValueAxis.getUpperBound();
                int[] itemBounds = RendererUtilities.findLiveItems(xyDataset, seriesIndex, xLowerBound, xUpperBound);
                firstItemIndex = itemBounds[0] - 1;
                if (firstItemIndex < 0) {
                    firstItemIndex = 0;
                }
                final int lastBoundaryIndex = itemBounds[1] + 1;
                if (lastBoundaryIndex < lastItemIndex) {
                    lastItemIndex = lastBoundaryIndex;
                }
            }

            state.startSeriesPass(xyDataset, seriesIndex, firstItemIndex,
                    lastItemIndex, currentPassIndex, totalRendererPasses);

            for (int item = firstItemIndex; item <= lastItemIndex; item++) {
                xyItemRenderer.drawItem(g2, state, dataArea, info,
                        this, xValueAxis, yValueAxis, xyDataset, seriesIndex, item,
                        crosshairState, currentPassIndex);
            }

            state.endSeriesPass(xyDataset, seriesIndex, firstItemIndex,
                    lastItemIndex, currentPassIndex, totalRendererPasses);
        }
    }

    private XYItemRenderer findRenderer(int index) {
        XYItemRenderer renderer = getRenderer(index);
        if (renderer == null) {
            renderer = getRenderer();
        }
        return renderer;
    }

    abstract ValueAxis getDomainAxisAdapter(ChartView chartView);

    abstract ValueAxis getRangeAxisAdapter(ChartView chartView);

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

    public void addConfigurator(PlotConfigurator configurator) {
        configurationDelegate.addConfigurator(configurator);
    }

    public Collection<PlotConfigurator> getConfigurators() {
        return configurationDelegate.getConfigurators();
    }

    public void configure(ChartView chartView) {
        configurationDelegate.configure(this, chartView);
    }
}
