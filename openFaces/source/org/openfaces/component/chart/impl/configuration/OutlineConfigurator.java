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

package org.openfaces.component.chart.impl.configuration;

import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.LineStyle;
import org.openfaces.component.chart.impl.renderers.XYBarRendererAdapter;
import org.openfaces.component.chart.impl.renderers.XYRendererAdapter;

import java.util.Iterator;

/**
 * @author Eugene Goncharov
 */
public class OutlineConfigurator extends AbstractConfigurator implements RendererConfigurator, PlotConfigurator {
    private int seriesCount;

    public OutlineConfigurator(ChartView view) {
        super(view);
    }

    public OutlineConfigurator(ChartView view, int seriesCount) {
        super(view);
        this.seriesCount = seriesCount;
    }

    public int getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(int seriesCount) {
        this.seriesCount = seriesCount;
    }

    public void configure(ConfigurablePlot plot) {
        PiePlot piePlot = (PiePlot) plot;
        ChartView chartView = getView();
        boolean outlinesSpecified = chartView.getOutlines() != null && !chartView.getOutlines().isEmpty();

        if (chartView.getDefaultOutlineStyle() != null && !outlinesSpecified) {
            piePlot.setBaseSectionOutlinePaint(chartView.getDefaultOutlineStyle().getColor());
            piePlot.setBaseSectionOutlineStroke(chartView.getDefaultOutlineStyle().getStroke());
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                piePlot.setSectionOutlinePaint(seriesIndex, chartView.getDefaultOutlineStyle().getColor());
                piePlot.setSectionOutlineStroke(seriesIndex, chartView.getDefaultOutlineStyle().getStroke());
            }
        } else if (outlinesSpecified) {
            final Iterator outlinesIterator = chartView.getOutlines().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (outlinesIterator.hasNext()) {
                    final LineStyle lineStyle = (LineStyle) outlinesIterator.next();
                    piePlot.setSectionOutlinePaint(seriesIndex, lineStyle.getColor());
                    piePlot.setSectionOutlineStroke(seriesIndex, lineStyle.getStroke());
                }
            }
        }
    }

    public void configure(ConfigurableRenderer configurableRenderer) {
        GridChartView chartView = (GridChartView) getView();
        boolean outlinesSpecified = chartView.getOutlines() != null && !chartView.getOutlines().isEmpty();
        AbstractRenderer renderer = (AbstractRenderer) configurableRenderer;


        if (chartView.getDefaultOutlineStyle() != null || outlinesSpecified) {
            if (renderer instanceof BarRenderer) {
                ((BarRenderer) renderer).setDrawBarOutline(true);
            } else if (renderer instanceof XYBarRendererAdapter) {
                ((XYBarRendererAdapter) renderer).setDrawBarOutline(true);
            } else if (renderer instanceof LineAndShapeRenderer) {
                ((LineAndShapeRenderer) renderer).setDrawOutlines(true);
                ((LineAndShapeRenderer) renderer).setUseOutlinePaint(true);
            } else if (renderer instanceof XYRendererAdapter) {
                ((XYRendererAdapter) renderer).setDrawOutlines(true);
                ((XYRendererAdapter) renderer).setUseOutlinePaint(true);
            }
        }

        if (chartView.getDefaultOutlineStyle() != null && !outlinesSpecified) {
            renderer.setBaseOutlinePaint(chartView.getDefaultOutlineStyle().getColor());
            renderer.setBaseOutlineStroke(chartView.getDefaultOutlineStyle().getStroke());
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                renderer.setSeriesOutlinePaint(seriesIndex, chartView.getDefaultOutlineStyle().getColor());
                renderer.setSeriesOutlineStroke(seriesIndex, chartView.getDefaultOutlineStyle().getStroke());
            }
        } else if (outlinesSpecified) {
            final Iterator outlinesIterator = chartView.getOutlines().iterator();
            for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
                if (outlinesIterator.hasNext()) {
                    final LineStyle lineStyle = (LineStyle) outlinesIterator.next();
                    renderer.setSeriesOutlinePaint(seriesIndex, lineStyle.getColor());
                    renderer.setSeriesOutlineStroke(seriesIndex, lineStyle.getStroke());
                }
            }
        }
    }
}
