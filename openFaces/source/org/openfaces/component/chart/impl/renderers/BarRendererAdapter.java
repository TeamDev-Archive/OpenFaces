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

import org.jfree.chart.renderer.category.BarRenderer;
import org.openfaces.component.chart.BarChartView;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class BarRendererAdapter extends BarRenderer {
    public BarRendererAdapter(BarChartView chartView) {
        ChartRendererUtil.setupSeriesColors(chartView, this);
    }

}

