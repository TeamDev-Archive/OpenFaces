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

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartAxis;
import org.openfaces.component.chart.ChartDomain;
import org.openfaces.component.chart.ChartNumberAxis;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.component.chart.impl.generators.DynamicXYGenerator;
import org.openfaces.component.chart.impl.helpers.NumberAxisAdapter;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class GridXYPlotAdapter extends XYPlot {
    public GridXYPlotAdapter(XYDataset ds, AbstractXYItemRenderer renderer, Chart chart, GridChartView chartView) {
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

        if (!(keyAxis instanceof ChartNumberAxis))
            keyAxis = null;

        if (!(valueAxis instanceof ChartNumberAxis))
            valueAxis = null;

        NumberAxis numberKeyAxis = new NumberAxisAdapter(chartView.getKeyAxisLabel(), keyAxisVisible, (ChartNumberAxis) keyAxis, baseAxis, chartView);
        NumberAxis numberValueAxis = new NumberAxisAdapter(chartView.getValueAxisLabel(), valueAxisVisible, (ChartNumberAxis) valueAxis, baseAxis, chartView);

        if (ds == null) {
            numberKeyAxis.setVisible(false);
            numberValueAxis.setVisible(false);
        }

        setDomainAxis(numberKeyAxis);
        setRangeAxis(numberValueAxis);

        PlotUtil.initGridLabels(chartView, renderer);

        PlotUtil.setupColorProperties(chart, this);
        setOrientation(PropertiesConverter.toPlotOrientation(chartView.getOrientation()));
        PlotUtil.setupGridLinesProperties(chartView, this, ds);
        setTooltips(chartView, renderer);
        setUrls(chartView, renderer);
    }

    private void setTooltips(final GridChartView view, AbstractXYItemRenderer renderer) {
        if (view.getTooltip() != null) {
            renderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
                public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                    return view.getTooltip();
                }
            });
        } else if (view.getDynamicTooltip() != null) {
            renderer.setBaseToolTipGenerator(new DynamicXYGenerator(view, view.getDynamicTooltip()));
        }
    }

    private void setUrls(final GridChartView view, AbstractXYItemRenderer renderer) {
        if (view.getUrl() != null) {
            renderer.setURLGenerator(new XYURLGenerator() {
                public String generateURL(XYDataset xyDataset, int i, int i1) {
                    return view.getUrl();
                }
            });
        } else if (view.getDynamicUrl() != null) {
            renderer.setURLGenerator(new DynamicXYGenerator(view, view.getDynamicUrl()));
        }
    }

}

