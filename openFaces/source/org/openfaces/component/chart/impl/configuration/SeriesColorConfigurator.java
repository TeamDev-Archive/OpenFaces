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

import org.jfree.chart.renderer.AbstractRenderer;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;

import java.awt.*;

/**
 * @author Eugene Goncharov
 */
public class SeriesColorConfigurator extends AbstractConfigurator implements RendererConfigurator {

    public SeriesColorConfigurator(ChartView view) {
        super(view);
    }

    public void configure(ConfigurableRenderer renderer) {
        if (getView() == null || !(renderer instanceof AbstractRenderer))
            return;

        String viewColors = getView().getColors();
        Color[] colors = PropertiesConverter.getColors(viewColors);

        if (colors != null) {
            for (int i = 0; i < colors.length; i++) {
                Color color = colors[i];
                ((AbstractRenderer) renderer).setSeriesPaint(i, color);
            }
        }
    }
}
