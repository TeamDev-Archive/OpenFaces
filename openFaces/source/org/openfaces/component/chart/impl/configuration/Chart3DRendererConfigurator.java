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
import org.openfaces.component.chart.impl.renderers.Chart3DRendererAdapter;

/**
 * @author Eugene Goncharov
 */
public class Chart3DRendererConfigurator extends AbstractConfigurator implements RendererConfigurator {

    public Chart3DRendererConfigurator() {
    }

    public void configure(ChartView view, ConfigurableRenderer renderer) {
        if (view.isEnable3D() && renderer instanceof Chart3DRendererAdapter) {
            ((Chart3DRendererAdapter) renderer).setWallPaint(view.getWallColor());
        }
    }
}
