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

package org.openfaces.component.chart.impl.plots;

import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.impl.configuration.ConfigurablePlot;
import org.openfaces.component.chart.impl.configuration.PlotConfigurator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Eugene Goncharov
 */
public class ConfigurablePlotBase implements Serializable {
    private List<PlotConfigurator> configurators = new ArrayList<PlotConfigurator>();


    public void addConfigurator(PlotConfigurator configurator) {
        configurators.add(configurator);
    }

    public Collection<PlotConfigurator> getConfigurators() {
        return configurators;
    }

    public void configure(ConfigurablePlot plot, ChartView view) {
        if (configurators != null && !configurators.isEmpty()) {
            for (PlotConfigurator configurator : configurators) {
                configurator.configure(plot, view);
            }
        }
    }
}
