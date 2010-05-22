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

package org.openfaces.component.chart.impl.helpers;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartSelection;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.GridPointInfo;
import org.openfaces.component.chart.LineStyle;
import org.openfaces.component.chart.SeriesInfo;
import org.openfaces.component.chart.impl.renderers.CustomizedRenderer;
import org.openfaces.component.chart.impl.renderers.XYRendererAdapter;

import java.awt.*;

/**
 * @author Eugene Goncharov
 */
public class SelectionUtil {

    public static void setupSelectionHighlighting(Plot plot, Chart chart, GridChartView chartView) {
        if (chart.getChartSelection() != null && chartView.getPoint() != null) {
            GridPointInfo point = chartView.getPoint();
            final ChartSelection selection = chart.getChartSelection();

            final LineStyle lineStyle = selection.getLineStyle();
            Paint outlinePaint = lineStyle.getColor() != null
                    ? lineStyle.getColor()
                    : Color.BLUE;
            final Stroke outlineStroke = lineStyle.getStroke() != null
                    ? lineStyle.getStroke()
                    : new BasicStroke(5.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
            final Paint selectionPaint = selection.getFillPaint();

            Object itemRenderer = null;
            if (plot instanceof XYPlot) {
                itemRenderer = ((XYPlot) plot).getRenderer();
            } else if (plot instanceof CategoryPlot) {
                itemRenderer = ((CategoryPlot) plot).getRenderer();
            }

            if (itemRenderer != null && itemRenderer instanceof CustomizedRenderer) {
                if (itemRenderer instanceof BarRenderer) {
                    ((BarRenderer) itemRenderer).setDrawBarOutline(true);
                } else if (itemRenderer instanceof XYBarRenderer) {
                    ((XYBarRenderer) itemRenderer).setDrawBarOutline(true);
                } else if (itemRenderer instanceof XYRendererAdapter) {
                    ((XYRendererAdapter) itemRenderer).setDrawOutlines(true);
                } else if (itemRenderer instanceof XYLineAndShapeRenderer) {
                    ((XYLineAndShapeRenderer) itemRenderer).setDrawOutlines(true);
                }

                final SeriesInfo series = point.getSeries();
                ((CustomizedRenderer) itemRenderer).setItemOutlinePaint(series.getIndex(), point.getIndex(), outlinePaint);
                ((CustomizedRenderer) itemRenderer).setItemOutlineStroke(series.getIndex(), point.getIndex(), outlineStroke);

                ((CustomizedRenderer) itemRenderer).setItemPaint(series.getIndex(), point.getIndex(), selectionPaint);
            }
        }
    }
}
