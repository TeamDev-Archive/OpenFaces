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

package org.openfaces.component.chart.impl.renderers;

import org.jfree.chart.renderer.xy.XYLine3DRenderer;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.LineChartView;

/**
 * @author Dmitry Pikhulya
 */
public class XYLineRenderer3DAdapter extends XYLine3DRenderer implements XYRendererAdapter, Chart3DRendererAdapter {
    public XYLineRenderer3DAdapter(LineChartView chartView, XYDataset dataSet) {
        ChartRendering.setupSeriesColors(chartView, this);

        ChartRendering.processXYLineAndShapeRendererProperties(this, dataSet, chartView);
    }

}
