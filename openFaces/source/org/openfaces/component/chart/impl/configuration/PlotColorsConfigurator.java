/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.chart.impl.configuration;

import org.jfree.chart.plot.Plot;
import org.openfaces.component.chart.ChartView;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

/**
 * @author Eugene Goncharov
 */
public class PlotColorsConfigurator extends AbstractConfigurator implements PlotConfigurator {

    public PlotColorsConfigurator(ChartView view) {
        super(view);
    }

    public void configure(ConfigurablePlot configurablePlot) {
        StyleObjectModel cssChartViewModel = getView().getStyleObjectModel();

        Plot plot = (Plot) configurablePlot;

        if (getView().getBackgroundPaint() != null) {
            plot.setBackgroundPaint(null);
        } else {
            plot.setBackgroundPaint(cssChartViewModel.getBackground());
        }

        StyleBorderModel border = cssChartViewModel.getBorder();
        plot.setOutlinePaint(border == null || border.isNone()
                ? cssChartViewModel.getBackground()
                : border.getColor());
    }
}
