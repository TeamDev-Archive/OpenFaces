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
package org.openfaces.component.chart.impl.helpers;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartNoDataMessage;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class JFreeChartAdapter extends JFreeChart {

    public JFreeChartAdapter(Plot plot, Chart chart) {
        super(null, JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        final boolean chartBackgroundPaintDefined = chart.getChartView().getBackgroundPaint() != null;
        if (chart.isLegendVisible()) {
            addSubtitle(new LegendAdapter(plot, chart));
        }

        if (chart.getTitle() != null) {
            setTitle(new TextTitleAdapter(chart));
        }

        if (chartBackgroundPaintDefined) {
            setBackgroundPaint(chart.getChartView().getBackgroundPaint());
        }

        //TODO: separate style properties

        StyleObjectModel cssChartModel = chart.getStyleObjectModel();
        if (cssChartModel != null) {
            if (!chartBackgroundPaintDefined) {
                setBackgroundPaint(cssChartModel.getBackground());
            }

            StyleBorderModel border = cssChartModel.getBorder();
            if (border != null && !border.isNone()) {
                setBorderPaint(border.getColor());
                setBorderVisible(true);
            } else {
                setBorderVisible(false);
            }
        }

        Float foregroundAlpha = chart.getChartView().getForegroundAlpha();
        if (foregroundAlpha != null) {
            plot.setForegroundAlpha(foregroundAlpha);
        }

        ChartNoDataMessage chartNoDataMessage = chart.getNoDataMessage();
        if (chartNoDataMessage != null && chartNoDataMessage.getText() != null) {
            plot.setNoDataMessage(chartNoDataMessage.getText());

            StyleObjectModel cssMessageModel = chartNoDataMessage.getStyleObjectModel();
            if (cssMessageModel != null) {
                plot.setNoDataMessagePaint(cssMessageModel.getColor());
                plot.setNoDataMessageFont(CSSUtil.getFont(cssMessageModel));
            }
        }
    }
}