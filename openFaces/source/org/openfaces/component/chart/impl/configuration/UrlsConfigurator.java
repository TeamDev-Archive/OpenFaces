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

import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.ChartView;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.impl.generators.DynamicCategoryGenerator;
import org.openfaces.component.chart.impl.generators.DynamicXYGenerator;

/**
 * @author Eugene Goncharov
 */
public class UrlsConfigurator extends AbstractConfigurator implements RendererConfigurator {

    public UrlsConfigurator() {
    }

    public void configure(ChartView view, ConfigurableRenderer renderer) {
        if (renderer instanceof AbstractCategoryItemRenderer) {
            setupUrls((GridChartView) view, (AbstractCategoryItemRenderer) renderer);
        } else if (renderer instanceof XYItemRenderer) {
            setupUrls((GridChartView) view, (XYItemRenderer) renderer);
        }
    }

    private void setupUrls(final GridChartView chartView, XYItemRenderer renderer) {
        if (chartView.getUrl() != null) {
            renderer.setURLGenerator(new XYURLGenerator() {
                public String generateURL(XYDataset xyDataset, int i, int i1) {
                    return chartView.getUrl();
                }
            });
        } else if (chartView.getDynamicUrl() != null) {
            renderer.setURLGenerator(new DynamicXYGenerator(chartView, chartView.getDynamicUrl()));
        }
    }

    private void setupUrls(final GridChartView chartView, AbstractCategoryItemRenderer renderer) {
        if (chartView.getUrl() != null) {
            renderer.setItemURLGenerator(new CategoryURLGenerator() {
                public String generateURL(CategoryDataset categoryDataset, int i, int i1) {
                    return chartView.getUrl();
                }
            });
        } else if (chartView.getDynamicUrl() != null) {
            renderer.setItemURLGenerator(new DynamicCategoryGenerator(chartView, chartView.getDynamicUrl()));
        }
    }
}
