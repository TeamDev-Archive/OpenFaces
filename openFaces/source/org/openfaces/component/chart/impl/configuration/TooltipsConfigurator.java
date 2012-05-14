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

import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.impl.generators.DynamicCategoryGenerator;
import org.openfaces.component.chart.impl.generators.DynamicXYGenerator;

/**
 * @author Eugene Goncharov
 */
public class TooltipsConfigurator extends AbstractConfigurator implements RendererConfigurator {

    public TooltipsConfigurator() {
    }

    public void configure(ChartView view, ConfigurableRenderer renderer) {
        if (renderer instanceof AbstractCategoryItemRenderer) {
            setupTooltips((GridChartView) view, (AbstractCategoryItemRenderer) renderer);
        } else if (renderer instanceof XYItemRenderer) {
            setupTooltips((GridChartView) view, (XYItemRenderer) renderer);
        }
    }

    private void setupTooltips(final GridChartView chartView, XYItemRenderer renderer) {
        if (chartView.getTooltip() != null) {
            renderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
                public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                    return chartView.getTooltip();
                }
            });
        } else if (chartView.getDynamicTooltip() != null) {
            renderer.setBaseToolTipGenerator(new DynamicXYGenerator(chartView, chartView.getDynamicTooltip()));
        }
    }

    private void setupTooltips(final GridChartView chartView, AbstractCategoryItemRenderer renderer) {
        if (chartView.getTooltip() != null) {
            renderer.setBaseToolTipGenerator(new CategoryToolTipGenerator() {
                public String generateToolTip(CategoryDataset categoryDataset, int i, int i1) {
                    return chartView.getTooltip();
                }
            });
        } else if (chartView.getDynamicTooltip() != null) {
            renderer.setBaseToolTipGenerator(new DynamicCategoryGenerator(chartView, chartView.getDynamicTooltip()));
        }
    }
}
