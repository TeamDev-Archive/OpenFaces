/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.chart.impl.configuration.charts;

import org.jfree.chart.plot.Plot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.TableOrder;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.impl.ModelConverter;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.plots.MultiplePiePlotAdapter;
import org.openfaces.component.chart.impl.plots.PiePlot3DAdapter;
import org.openfaces.component.chart.impl.plots.PiePlotAdapter;

/**
 * @author Eugene Goncharov
 */
public class PieChartConfigurator extends AbstractChartConfigurator {

    public PieChartConfigurator(Chart chart, ChartModel model) {
        super(chart, model);
    }

    @Override
    public Plot configurePlot(ModelInfo info) {
        return createPlot(getChart(), getModel(), info);
    }

    protected Plot createPlot(Chart chart, ChartModel model, ModelInfo info) {
        final PieChartView chartView = (PieChartView) getChartView();

        if (info.isDataEmpty()) {
            return new PiePlotAdapter(null, chart, chartView);
        }

        if (info.getNonEmptySeriesList().length < 2) {
            PieDataset ds = ModelConverter.toPieDataset(model);
            final Plot adapter = (chartView.isEnable3D())
                    ? new PiePlot3DAdapter(ds, chart, chartView)
                    : new PiePlotAdapter(ds, chart, chartView);

            return adapter;
        }

        CategoryDataset ds = ModelConverter.toCategoryDataset(info);

        return new MultiplePiePlotAdapter(ds, TableOrder.BY_ROW, chart, chartView);
    }

}
