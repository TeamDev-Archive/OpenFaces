/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.chart;

import org.jfree.chart.plot.Plot;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.configuration.charts.ChartConfigurator;

/**
 * @author Eugene Goncharov
 */
public class PlotFactory {

    public static Plot createPlot(Chart chart, ModelInfo info) {
        ChartConfigurator configurator = chart.getChartView().getConfigurator();

        return configurator.configurePlot(chart, info);
    }
}
