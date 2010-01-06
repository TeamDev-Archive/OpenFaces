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

import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.openfaces.component.chart.ChartModel;
import org.openfaces.component.chart.LineChartView;
import org.openfaces.component.chart.LineProperties;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.component.chart.impl.generators.DynamicCategoryGenerator;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

import java.awt.*;
import java.util.List;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineRendererAdapter extends LineAndShapeRenderer {
    public LineRendererAdapter(LineChartView chartView, CategoryDataset dataset) {
        ChartRendererUtil.setupSeriesColors(chartView, this);
        setShapesVisible(chartView.isShapesVisible());

        processProperties(dataset, chartView);
    }

    private void processProperties(CategoryDataset dataset, LineChartView view) {
        List<LineProperties> linePropertiesList = view.getLinePropertiesList();
        if (linePropertiesList == null)
            return;

        for (LineProperties lineProperties : linePropertiesList) {
            DynamicCategoryGenerator dcg = new DynamicCategoryGenerator(view, lineProperties.getDynamicCondition());

            ChartModel chartModel = view.getChart().getModel();
            if (chartModel == null)
                continue;

            Series[] series = chartModel.getSeries();
            if (series == null)
                continue;

            for (int j = 0; j < series.length; j++) {
                if (!dcg.generateCondition(dataset, j, 0))
                    continue;

                Boolean hideSeries = lineProperties.getHideSeries();
                if (hideSeries != null) {
                    boolean seriesVisible = !hideSeries;
                    setSeriesVisible(j, seriesVisible);
                }
                Boolean shapesVisible = lineProperties.getShapesVisible();
                if (shapesVisible != null)
                    setSeriesShapesVisible(j, shapesVisible);
                //set style
                Boolean showInLegend = lineProperties.getShowInLegend();
                if (showInLegend != null)
                    setSeriesVisibleInLegend(j, showInLegend);

                StyleObjectModel linePropertiesStyleModel = lineProperties.getStyleObjectModel();
                if (linePropertiesStyleModel != null && linePropertiesStyleModel.getBorder() != null) {
                    StyleBorderModel model = linePropertiesStyleModel.getBorder();
                    Color color = model.getColor();
                    if (color != null)
                        setSeriesPaint(j, color);
                    setSeriesLinesVisible(j, Boolean.valueOf(!model.isNone()));
                    setSeriesStroke(j, PropertiesConverter.toStroke(model));
                }

            }

        }
    }

}
