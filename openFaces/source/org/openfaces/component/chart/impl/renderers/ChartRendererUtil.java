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

import org.jfree.chart.renderer.AbstractRenderer;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;

import java.awt.*;

/**
 * @author Ekaterina Shliakhovetskaya
 */
class ChartRendererUtil {
    private ChartRendererUtil() {
    }

    public static void setupSeriesColors(ChartView view, AbstractRenderer renderer) {
        if (view == null || renderer == null)
            return;

        String viewColors = view.getColors();
        Color[] colors = PropertiesConverter.getColors(viewColors);
        if (colors != null) {
            for (int i = 0; i < colors.length; i++) {
                Color color = colors[i];
                renderer.setSeriesPaint(i, color);
            }
        }

    }

}
