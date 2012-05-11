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

import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.LineProperties;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.component.chart.impl.generators.DynamicCategoryGenerator;
import org.openfaces.component.chart.impl.generators.DynamicXYGenerator;
import org.openfaces.component.chart.impl.renderers.XYRendererAdapter;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

import java.awt.*;

/**
 * @author Eugene Goncharov
 */
public class LineAndShapePropertiesConfigurator extends AbstractConfigurator implements RendererConfigurator {
    private Dataset dataSet;

    public LineAndShapePropertiesConfigurator(Dataset dataSet) {
        this.dataSet = dataSet;
    }

    public Dataset getDataSet() {
        return dataSet;
    }

    public void configure(ChartView view, ConfigurableRenderer renderer) {
        LineChartView lineChartView = (LineChartView) view;

        java.util.List<LineProperties> linePropertiesList = lineChartView.getLinePropertiesList();
        if (linePropertiesList == null)
            return;

        for (LineProperties lineProperties : linePropertiesList) {
            if (renderer instanceof XYRendererAdapter && dataSet instanceof XYDataset) {
                XYRendererAdapter xyRenderer = (XYRendererAdapter) renderer;
                XYDataset xyDataset = (XYDataset) dataSet;

                applyPropertiesToXYDataSet(xyRenderer, xyDataset, lineChartView, lineProperties);
            } else if (renderer instanceof LineAndShapeRenderer && dataSet instanceof CategoryDataset) {
                LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) renderer;
                CategoryDataset categoryDataset = (CategoryDataset) dataSet;

                applyPropertiesToCategoryDataSet(lineRenderer, categoryDataset, lineChartView, lineProperties);
            } else {
                throw new IllegalStateException("");
            }
        }
    }

    private void applyPropertiesToXYDataSet(XYRendererAdapter renderer, XYDataset dataSet, LineChartView view,
                                            LineProperties lineProperties) {
        DynamicXYGenerator dcg = new DynamicXYGenerator(view, lineProperties.getDynamicCondition());

        ChartModel chartModel = view.getChart().getModel();
        if (chartModel == null)
            return;

        Series[] series = chartModel.getSeries();
        if (series == null)
            return;

        for (int j = 0; j < series.length; j++) {
            if (!dcg.generateCondition(dataSet, j, 0))
                continue;

            Boolean hideSeries = lineProperties.getHideSeries();
            if (hideSeries != null) {
                renderer.setSeriesVisible(j, !hideSeries);
            }
            Boolean shapesVisible = lineProperties.getShapesVisible();
            if (shapesVisible != null)
                renderer.setSeriesShapesVisible(j, shapesVisible);
            //set style
            Boolean showInLegend = lineProperties.getShowInLegend();
            if (showInLegend != null)
                renderer.setSeriesVisibleInLegend(j, showInLegend);

            StyleObjectModel linePropertiesStyleModel = lineProperties.getStyleObjectModel();
            if (linePropertiesStyleModel != null && lineProperties.getStyleObjectModel().getBorder() != null) {
                StyleBorderModel model = lineProperties.getStyleObjectModel().getBorder();
                Color color = model.getColor();
                if (color != null)
                    renderer.setSeriesPaint(j, color);
                renderer.setSeriesLinesVisible(j, Boolean.valueOf(!model.isNone()));
                renderer.setSeriesStroke(j, PropertiesConverter.toStroke(model));
            }

        }
    }

    private void applyPropertiesToCategoryDataSet(LineAndShapeRenderer renderer, CategoryDataset dataSet,
                                                  LineChartView view, LineProperties lineProperties) {
        DynamicCategoryGenerator dcg = new DynamicCategoryGenerator(view, lineProperties.getDynamicCondition());

        ChartModel chartModel = view.getChart().getModel();
        if (chartModel == null)
            return;

        Series[] series = chartModel.getSeries();
        if (series == null)
            return;

        for (int j = 0; j < series.length; j++) {
            if (!dcg.generateCondition(dataSet, j, 0))
                continue;

            Boolean hideSeries = lineProperties.getHideSeries();
            if (hideSeries != null) {
                boolean seriesVisible = !hideSeries;
                renderer.setSeriesVisible(j, seriesVisible);
            }
            Boolean shapesVisible = lineProperties.getShapesVisible();
            if (shapesVisible != null)
                renderer.setSeriesShapesVisible(j, shapesVisible);
            //set style
            Boolean showInLegend = lineProperties.getShowInLegend();
            if (showInLegend != null)
                renderer.setSeriesVisibleInLegend(j, showInLegend);

            StyleObjectModel linePropertiesStyleModel = lineProperties.getStyleObjectModel();
            if (linePropertiesStyleModel != null && linePropertiesStyleModel.getBorder() != null) {
                StyleBorderModel model = linePropertiesStyleModel.getBorder();
                Color color = model.getColor();
                if (color != null)
                    renderer.setSeriesPaint(j, color);
                renderer.setSeriesLinesVisible(j, Boolean.valueOf(!model.isNone()));
                renderer.setSeriesStroke(j, PropertiesConverter.toStroke(model));
            }

        }
    }
}
