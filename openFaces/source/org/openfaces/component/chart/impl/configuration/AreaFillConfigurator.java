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

package org.openfaces.component.chart.impl.configuration;

import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.impl.renderers.AreaFillRenderer;

/**
 * @author Eugene Goncharov
 */
public class AreaFillConfigurator extends AbstractConfigurator implements RendererConfigurator {

    public AreaFillConfigurator() {
    }

    public void configure(ChartView view, ConfigurableRenderer renderer) {
        if (renderer instanceof AreaFillRenderer) {
            LineChartView chartView = (LineChartView) view;
            AreaFillRenderer areaFillRenderer = (AreaFillRenderer) renderer;
            areaFillRenderer.setBackgroundPaint(chartView.getBackgroundPaint());
            areaFillRenderer.setLineAreaFill(chartView.getLineAreaFill());
        }
    }
}
