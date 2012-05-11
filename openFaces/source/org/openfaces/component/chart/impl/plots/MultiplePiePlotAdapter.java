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

import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.CategoryToPieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.TableOrder;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.PieChartView;
import org.openfaces.component.chart.impl.configuration.ConfigurablePlot;
import org.openfaces.component.chart.impl.configuration.PlotColorsConfigurator;
import org.openfaces.component.chart.impl.configuration.PlotConfigurator;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

import java.awt.*;
import java.util.Collection;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class MultiplePiePlotAdapter extends MultiplePiePlot implements ConfigurablePlot {
    private ConfigurablePlotBase configurationDelegate = new ConfigurablePlotBase();

    public MultiplePiePlotAdapter(CategoryDataset ds, TableOrder order, Chart chart, PieChartView view) {
        super(ds);

        TextTitle seriesTitle = getSeriesTitle(chart);
        getPieChart().setTitle(seriesTitle);

        PiePlot piePlot = (PiePlot) getPieChart().getPlot();
        piePlot.setDataset(new CategoryToPieDataset(ds, order, 0));
        setDataExtractOrder(order);

        if (view.isEnable3D()) {
            new PiePlot3DAdapter(piePlot, ds, order, view, chart);
        } else {
            new PiePlotAdapter(piePlot, ds, order, view, chart);
        }

        addConfigurator(new PlotColorsConfigurator());

        configure(view);
    }

    private TextTitle getSeriesTitle(Chart chart) {
        StyleObjectModel cssChartViewModel = chart.getChartView().getStyleObjectModel();
        TextTitle seriesTitle;
        Font f = CSSUtil.getFont(cssChartViewModel);
        if (f != null) {
            seriesTitle = new TextTitle("Series Title", f);
        } else {
            seriesTitle = new TextTitle("Series Title", new Font("SansSerif", Font.BOLD, 12));
        }

        if (cssChartViewModel.getColor() != null) {
            seriesTitle.setPaint(cssChartViewModel.getColor());
        }
        seriesTitle.setPosition(RectangleEdge.BOTTOM);
        return seriesTitle;
    }

    public void addConfigurator(PlotConfigurator configurator) {
        configurationDelegate.addConfigurator(configurator);
    }

    public Collection<PlotConfigurator> getConfigurators() {
        return configurationDelegate.getConfigurators();
    }

    public void configure(ChartView chartView) {
        configurationDelegate.configure(this, chartView);
    }
}
