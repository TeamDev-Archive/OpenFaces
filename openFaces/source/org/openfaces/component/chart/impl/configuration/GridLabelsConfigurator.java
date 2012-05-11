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

import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.ui.TextAnchor;
import org.openfaces.component.chart.ChartLabelPosition;
import org.openfaces.component.chart.ChartLabels;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

/**
 * @author Eugene Goncharov
 */
public class GridLabelsConfigurator extends AbstractConfigurator implements RendererConfigurator {

    public GridLabelsConfigurator() {
    }

    public void configure(ChartView view, ConfigurableRenderer configurableRenderer) {
        GridChartView chartView = (GridChartView) view;
        boolean isLabelsVisible = chartView.isLabelsVisible();

        AbstractRenderer renderer = (AbstractRenderer) configurableRenderer;

        if (isLabelsVisible) {
            renderer.setBaseItemLabelsVisible(true);

            setGenerator(renderer);
            defaultInit(chartView, renderer);
            colorInit(chartView, renderer);
        } else {
            renderer.setBaseItemLabelsVisible(false);
            if (chartView.getLabels() == null || chartView.getLabels().getText() == null) {
                setGenerator(renderer);
            }
        }
    }

    private void setGenerator(AbstractRenderer renderer) {
        if (renderer instanceof AbstractCategoryItemRenderer) {
            ((AbstractCategoryItemRenderer) renderer).setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        } else if (renderer instanceof AbstractXYItemRenderer) {
            ((AbstractXYItemRenderer) renderer).setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        }

    }

    private void colorInit(GridChartView chartView, AbstractRenderer renderer) {
        StyleObjectModel cssViewModel = chartView.getStyleObjectModel();

        ChartLabels labels = chartView.getLabels();

        if (labels != null) {
            StyleObjectModel cssLabelsModel = labels.getStyleObjectModel();
            renderer.setBaseItemLabelPaint(cssLabelsModel.getColor());
            renderer.setBaseItemLabelFont(CSSUtil.getFont(cssLabelsModel));

        } else {
            renderer.setBaseItemLabelPaint(cssViewModel.getColor());
            renderer.setBaseItemLabelFont(CSSUtil.getFont(cssViewModel));
        }

    }

    private void defaultInit(GridChartView chartView, AbstractRenderer renderer) {
        final ChartLabelPosition positiveLabelsPosition = chartView.getPositiveLabelsPosition();
        final ChartLabelPosition negativeLabelsPosition = chartView.getNegativeLabelsPosition();
        final ChartLabelPosition defaultLabelPosition = chartView.getDefaultLabelsPosition();

        if (defaultLabelPosition != null && positiveLabelsPosition == null) {
            renderer.setBasePositiveItemLabelPosition(createItemLabelPosition(defaultLabelPosition));
        } else if (positiveLabelsPosition != null) {
            renderer.setBasePositiveItemLabelPosition(createItemLabelPosition(positiveLabelsPosition));
        }

        if (defaultLabelPosition != null && negativeLabelsPosition == null) {
            renderer.setBaseNegativeItemLabelPosition(createItemLabelPosition(defaultLabelPosition));
        } else if (negativeLabelsPosition != null) {
            renderer.setBaseNegativeItemLabelPosition(createItemLabelPosition(negativeLabelsPosition));
        }

        renderer.setItemLabelAnchorOffset(chartView.getLabelsOffset());
    }

    private ItemLabelPosition createItemLabelPosition(ChartLabelPosition labelPosition) {
        return new ItemLabelPosition(labelPosition.getLabelAnchor(), TextAnchor.CENTER);
    }


}
