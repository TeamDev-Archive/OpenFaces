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
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Eugene Goncharov
 */
public class ItemsColorConfigurator extends AbstractConfigurator implements RendererConfigurator, PlotConfigurator {

    public ItemsColorConfigurator(ChartView view) {
        super(view);
    }

    public void configure(ConfigurablePlot plot) {
        if (getView().getColors() != null) {
            Collection<Paint> colors = PropertiesConverter.getColors(getView().getColors());

            Iterator<Paint> colorsIterator = colors.iterator();
            int colorIndex = 0;
            while (colorsIterator.hasNext()) {
                Paint color = colorsIterator.next();
                
                ((PiePlot) plot).setSectionPaint(colorIndex, color);

                colorIndex++;
            }
        }
    }

    public void configure(ConfigurableRenderer renderer) {
        if (getView() == null || !(renderer instanceof AbstractRenderer))
            return;

        Collection<Paint> colors = PropertiesConverter.getColors(getView().getColors());
        Iterator<Paint> colorsIterator = colors.iterator();
        int colorIndex = 0;
        while (colorsIterator.hasNext()) {
            Paint color = colorsIterator.next();
            ((AbstractRenderer) renderer).setSeriesPaint(colorIndex, color);
            colorIndex++;
        }
    }
}
