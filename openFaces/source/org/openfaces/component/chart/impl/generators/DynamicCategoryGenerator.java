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
package org.openfaces.component.chart.impl.generators;

import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.openfaces.component.chart.ChartViewValueExpression;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.GridPointInfo;
import org.openfaces.component.chart.impl.GridPointInfoImpl;
import org.openfaces.component.chart.impl.SeriesInfoImpl;
import org.openfaces.util.Log;

import java.io.Serializable;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicCategoryGenerator implements CategoryToolTipGenerator, CategoryURLGenerator, Serializable {
    private transient final GridChartView view;
    private transient final ChartViewValueExpression dynamicProperty;

    public DynamicCategoryGenerator(GridChartView view, ChartViewValueExpression dynamicProperty) {
        this.view = view;
        this.dynamicProperty = dynamicProperty;
    }

    public String generateToolTip(CategoryDataset categoryDataset, int i, int i1) {
        return getValue(categoryDataset, i, i1);
    }

    public String generateURL(CategoryDataset categoryDataset, int i, int i1) {
        return getValue(categoryDataset, i, i1);
    }

    public boolean generateCondition(CategoryDataset categoryDataset, int i, int i1) {
        return Boolean.valueOf(getValue(categoryDataset, i, i1));
    }

    private String getValue(CategoryDataset categoryDataset, int i, int i1) {
        String currentValue = "";
        try {
            GridPointInfo point = new GridPointInfoImpl();
            point.setSeries(new SeriesInfoImpl());
            point.setKey(categoryDataset.getColumnKey(i1));
            point.setValue(categoryDataset.getValue(i, i1));

            point.getSeries().setKey(categoryDataset.getRowKey(i));
            point.getSeries().setIndex(categoryDataset.getRowIndex(categoryDataset.getRowKey(i)));

            view.setPoint(point);
            //!
            //String currentValue = DynamicPropertiesUtils.getDynamicValue("point", point, dynamicProperty);

            //!


            if (dynamicProperty != null) {
                currentValue = dynamicProperty.getHint("point", point).toString();
            }

            view.setPoint(null);
        } catch (RuntimeException e) {
            Log.log("Exception inside DynamicCategoryGenerator.getValue", e);
        }

        return currentValue;
    }
}
