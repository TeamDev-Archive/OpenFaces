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

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryCrosshairState;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.util.SortOrder;
import org.openfaces.component.chart.ChartAxis;
import org.openfaces.component.chart.ChartCategoryAxis;
import org.openfaces.component.chart.ChartDomain;
import org.openfaces.component.chart.ChartNumberAxis;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.component.chart.impl.configuration.ConfigurablePlot;
import org.openfaces.component.chart.impl.configuration.PlotColorsConfigurator;
import org.openfaces.component.chart.impl.configuration.PlotConfigurator;
import org.openfaces.component.chart.impl.configuration.PlotGridLinesConfigurator;
import org.openfaces.component.chart.impl.configuration.PlotSelectionConfigurator;
import org.openfaces.component.chart.impl.helpers.CategoryAxis3DAdapter;
import org.openfaces.component.chart.impl.helpers.CategoryAxisAdapter;
import org.openfaces.component.chart.impl.helpers.NumberAxis3DAdapter;
import org.openfaces.component.chart.impl.helpers.NumberAxisAdapter;
import org.openfaces.component.chart.impl.renderers.AreaFillRenderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class GridCategoryPlotAdapter extends CategoryPlot implements ConfigurablePlot {
    private ConfigurablePlotBase configurationDelegate = new ConfigurablePlotBase();

    public GridCategoryPlotAdapter(CategoryDataset ds, AbstractCategoryItemRenderer renderer,
                                   GridChartView chartView) {
        setDataset(ds);
        setRenderer(renderer);

        ChartAxis baseAxis = chartView.getBaseAxis();
        ChartAxis keyAxis = chartView.getKeyAxis();
        ChartAxis valueAxis = chartView.getValueAxis();
        ChartDomain showAxes = chartView.getShowAxes();
        if (showAxes == null) {
            showAxes = ChartDomain.BOTH;
            chartView.setShowAxes(showAxes);
        }
        boolean keyAxisVisible = showAxes.equals(ChartDomain.BOTH) || showAxes.equals(ChartDomain.KEY);
        boolean valueAxisVisible = showAxes.equals(ChartDomain.BOTH) || showAxes.equals(ChartDomain.VALUE);

        if (!(keyAxis instanceof ChartCategoryAxis))
            keyAxis = null;

        if (!(valueAxis instanceof ChartNumberAxis))
            valueAxis = null;

        CategoryAxis categoryAxis = chartView.isEnable3D()
                ? new CategoryAxis3DAdapter(chartView.getKeyAxisLabel(), keyAxisVisible, (ChartCategoryAxis) keyAxis, baseAxis, chartView)
                : new CategoryAxisAdapter(chartView.getKeyAxisLabel(), keyAxisVisible, (ChartCategoryAxis) keyAxis, baseAxis, chartView);
        NumberAxis numberAxis = chartView.isEnable3D()
                ? new NumberAxis3DAdapter(chartView.getValueAxisLabel(), valueAxisVisible, (ChartNumberAxis) valueAxis, baseAxis, chartView)
                : new NumberAxisAdapter(chartView.getValueAxisLabel(), valueAxisVisible, (ChartNumberAxis) valueAxis, baseAxis, chartView);

        if (ds == null) {
            categoryAxis.setVisible(false);
            numberAxis.setVisible(false);
        }

        setDomainAxis(categoryAxis);
        setRangeAxis(numberAxis);

        setOrientation(PropertiesConverter.toPlotOrientation(chartView.getOrientation()));

        addConfigurator(new PlotColorsConfigurator());
        addConfigurator(new PlotGridLinesConfigurator(ds));
        addConfigurator(new PlotSelectionConfigurator());

        configure(chartView);
    }

    @Override
    public boolean render(Graphics2D g2, Rectangle2D dataArea, int index,
                          PlotRenderingInfo info, CategoryCrosshairState crossHairState) {
        final boolean customRenderingModeEnabled = getRenderer() != null && getRenderer() instanceof AreaFillRenderer;

        if (!customRenderingModeEnabled) {
            return super.render(g2, dataArea, index, info, crossHairState);
        }

        CategoryItemRenderer categoryItemRenderer = getRenderer(index);
        CategoryDataset categoryDataset = getDataset(index);

        boolean isDataSetNotEmpty = !DatasetUtilities.isEmptyOrNull(categoryDataset);
        boolean isAscendingRowOrdering = getRowRenderingOrder() == SortOrder.ASCENDING;

        if (!isDataSetNotEmpty || categoryItemRenderer == null) {
            return false;
        }
        CategoryItemRendererState rendererState = categoryItemRenderer.initialise(g2, dataArea, this, index, info);
        rendererState.setCrosshairState(crossHairState);
        int totalRows = categoryDataset.getRowCount();

        if (isAscendingRowOrdering) {
            for (int currentRowIndex = 0; currentRowIndex < totalRows; currentRowIndex++) {
                renderColumns(g2, rendererState, dataArea, categoryItemRenderer, categoryDataset,
                        index, currentRowIndex);
            }
        } else {
            for (int currentRowIndex = totalRows - 1; currentRowIndex >= 0; currentRowIndex--) {
                renderColumns(g2, rendererState, dataArea, categoryItemRenderer, categoryDataset,
                        index, currentRowIndex);
            }
        }

        return true;
    }

    private void renderColumns(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea,
                               CategoryItemRenderer renderer, CategoryDataset currentDataSet, int index, int row) {
        boolean isAscendingColumnOrder = getColumnRenderingOrder() == SortOrder.ASCENDING;
        CategoryAxis categoryAxis = getDomainAxisForDataset(index);
        ValueAxis valueAxis = getRangeAxisForDataset(index);
        int totalRendererPasses = renderer.getPassCount();
        int totalColumns = currentDataSet.getColumnCount();

        for (int currentPassIndex = 0; currentPassIndex < totalRendererPasses; currentPassIndex++) {
            if (isAscendingColumnOrder) {
                for (int columnIndex = 0; columnIndex < totalColumns; columnIndex++) {
                    final boolean isLastColumn = columnIndex == totalColumns - 1;
                    renderColumn(g2, state, dataArea, renderer, currentDataSet, categoryAxis, valueAxis,
                            row, currentPassIndex, columnIndex, isLastColumn);
                }
            } else {
                for (int columnIndex = totalColumns - 1; columnIndex >= 0; columnIndex--) {
                    final boolean isLastColumn = columnIndex == 0;
                    renderColumn(g2, state, dataArea, renderer, currentDataSet, categoryAxis, valueAxis,
                            row, currentPassIndex, columnIndex, isLastColumn);
                }
            }
        }
    }

    private void renderColumn(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea,
                              CategoryItemRenderer renderer, CategoryDataset currentDataSet,
                              CategoryAxis categoryAxis, ValueAxis valueAxis,
                              int row, int currentPassIndex, int columnIndex, boolean lastColumn) {
        renderer.drawItem(g2, state, dataArea, this, categoryAxis, valueAxis, currentDataSet,
                row, columnIndex, currentPassIndex);

        if (lastColumn) {
            ((AreaFillRenderer) renderer).completePass(g2, state, dataArea, this,
                    categoryAxis, valueAxis, currentDataSet, row, currentPassIndex);
        }
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
