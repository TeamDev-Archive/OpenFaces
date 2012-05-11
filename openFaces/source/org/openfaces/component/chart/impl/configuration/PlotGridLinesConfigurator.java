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

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.Dataset;
import org.openfaces.component.chart.ChartDomain;
import org.openfaces.component.chart.ChartGridLines;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

import java.awt.*;
import java.util.List;

/**
 * @author Eugene Goncharov
 */
public class PlotGridLinesConfigurator extends AbstractConfigurator implements PlotConfigurator {
    private Dataset dataset;

    public PlotGridLinesConfigurator() {
    }

    public PlotGridLinesConfigurator(Dataset dataset) {
        this.dataset = dataset;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void configure(ConfigurablePlot plot, ChartView view) {
        setupGridLinesProperties((GridChartView) view, ((Plot) plot), getDataset());
    }

    private void setupGridLinesProperties(GridChartView view, Plot plot, Dataset dataset) {
        if (dataset == null) {
            setDomainGridlinesVisible(plot, false);
            setRangeGridlinesVisible(plot, false);
            return;
        }

        List<ChartGridLines> gridLines = view.getGridLines();
        if (gridLines == null || gridLines.size() <= 0)
            return;

        ChartGridLines viewBaseGrid = view.getBaseGrid();
        if (viewBaseGrid != null) {
            StyleObjectModel cssStyle = viewBaseGrid.getStyleObjectModel();
            if (cssStyle != null &&
                    cssStyle.getBorder() != null &&
                    cssStyle.getBorder().getColor() != null &&
                    !cssStyle.getBorder().getStyle().equalsIgnoreCase("none")) {

                setDomainGridlinesVisible(plot, true);
                setDomainGridlineStrokeAndPaint(plot, PropertiesConverter.toStroke(cssStyle.getBorder()), cssStyle.getBorder().getColor());

                setRangeGridlinesVisible(plot, true);
                setRangeGridlineStrokeAndPaint(plot, PropertiesConverter.toStroke(cssStyle.getBorder()), cssStyle.getBorder().getColor());
            }
        } else {
            setDomainGridlinesVisible(plot, false);
            setRangeGridlinesVisible(plot, false);
        }

        for (ChartGridLines lines : gridLines) {
            StyleObjectModel cssStyle = lines.getStyleObjectModel();
            if (lines.getDomain().equals(ChartDomain.KEY)) {
                if (cssStyle != null &&
                        cssStyle.getBorder() != null &&
                        cssStyle.getBorder().getColor() != null &&
                        !cssStyle.getBorder().getStyle().equalsIgnoreCase("none")) {

                    setDomainGridlinesVisible(plot, true);
                    setDomainGridlineStrokeAndPaint(plot, PropertiesConverter.toStroke(cssStyle.getBorder()), cssStyle.getBorder().getColor());
                }

            } else if (lines.getDomain().equals(ChartDomain.VALUE)) {
                if (cssStyle != null &&
                        cssStyle.getBorder() != null &&
                        cssStyle.getBorder().getColor() != null &&
                        !cssStyle.getBorder().getStyle().equalsIgnoreCase("none")) {

                    setRangeGridlinesVisible(plot, true);
                    setRangeGridlineStrokeAndPaint(plot, PropertiesConverter.toStroke(cssStyle.getBorder()), cssStyle.getBorder().getColor());
                }
            }
        }

    }

    private void setDomainGridlinesVisible(Plot plot, boolean value) {
        if (plot instanceof CategoryPlot)
            ((CategoryPlot) plot).setDomainGridlinesVisible(value);
        else if (plot instanceof XYPlot)
            ((XYPlot) plot).setDomainGridlinesVisible(value);
        else
            throw new IllegalArgumentException("Unknown plot type: " + plot.getClass().getName());
    }

    private void setRangeGridlinesVisible(Plot plot, boolean value) {
        if (plot instanceof CategoryPlot)
            ((CategoryPlot) plot).setRangeGridlinesVisible(value);
        else if (plot instanceof XYPlot)
            ((XYPlot) plot).setRangeGridlinesVisible(value);
        else
            throw new IllegalArgumentException("Unknown plot type: " + plot.getClass().getName());
    }

    private void setDomainGridlineStrokeAndPaint(Plot plot, Stroke stroke, Paint paint) {
        if (plot instanceof CategoryPlot) {
            ((CategoryPlot) plot).setDomainGridlinePaint(paint);
            ((CategoryPlot) plot).setDomainGridlineStroke(stroke);
        } else if (plot instanceof XYPlot) {
            ((XYPlot) plot).setDomainGridlinePaint(paint);
            ((XYPlot) plot).setDomainGridlineStroke(stroke);
        } else
            throw new IllegalArgumentException("Unknown plot type: " + plot.getClass().getName());
    }

    private void setRangeGridlineStrokeAndPaint(Plot plot, Stroke stroke, Paint paint) {
        if (plot instanceof CategoryPlot) {
            ((CategoryPlot) plot).setRangeGridlinePaint(paint);
            ((CategoryPlot) plot).setRangeGridlineStroke(stroke);
        } else if (plot instanceof XYPlot) {
            ((XYPlot) plot).setRangeGridlinePaint(paint);
            ((XYPlot) plot).setRangeGridlineStroke(stroke);
        } else
            throw new IllegalArgumentException("Unknown plot type: " + plot.getClass().getName());
    }
}
