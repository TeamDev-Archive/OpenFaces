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

package org.openfaces.component.chart.impl.renderers;

import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.impl.configuration.ConfigurableRenderer;
import org.openfaces.component.chart.impl.configuration.RendererConfigurator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Eugene Goncharov
 */
public class ConfigurableRendererBase implements Serializable {
    private List<RendererConfigurator> configurators = new ArrayList<RendererConfigurator>();

    public void addConfigurator(RendererConfigurator configurator) {
        configurators.add(configurator);
    }

    public Collection<RendererConfigurator> getConfigurators() {
        return configurators;
    }

    public void configure(ChartView chartView, ConfigurableRenderer renderer) {
        if (configurators != null && !configurators.isEmpty()) {
            for (RendererConfigurator configurator : configurators) {
                configurator.configure(chartView, renderer);
            }
        }
    }
}
