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
package org.openfaces.component.chart.impl.generators;

import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.openfaces.component.chart.ChartViewValueExpression;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.GridPointInfo;
import org.openfaces.component.chart.impl.GridPointInfoImpl;
import org.openfaces.component.chart.impl.ModelInfo;
import org.openfaces.component.chart.impl.ModelType;
import org.openfaces.component.chart.impl.SeriesInfoImpl;
import org.openfaces.util.Log;

import java.util.Date;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicXYGenerator implements XYToolTipGenerator, XYURLGenerator {
    private final GridChartView view;
    private final ChartViewValueExpression valueExpression;

    public DynamicXYGenerator(GridChartView view, ChartViewValueExpression valueExpression) {
        this.view = view;
        this.valueExpression = valueExpression;
    }

    public String generateToolTip(XYDataset xyDataset, int i, int i1) {
        return getValue(xyDataset, i, i1);
    }

    public String generateURL(XYDataset xyDataset, int i, int i1) {
        return getValue(xyDataset, i, i1);
    }

    public boolean generateCondition(XYDataset xyDataset, int i, int i1) {
        return Boolean.valueOf(getValue(xyDataset, i, i1));
    }

    private String getValue(XYDataset xyDataset, int i, int i1) {
        String currentValue = "";
        try {
            GridPointInfo point = new GridPointInfoImpl();
            point.setSeries(new SeriesInfoImpl());

            Object key = xyDataset.getX(i, i1);

            ModelInfo info = view.getChart().getRenderHints().getModelInfo();

            if ((key instanceof Long) && info != null && info.getModelType() == ModelType.Date) {
                key = new Date((Long) key);
            }

            point.setKey(key);
            point.setValue(xyDataset.getY(i, i1));

            point.getSeries().setKey(xyDataset.getSeriesKey(i));

            view.setPoint(point);
            //!
            //Object currentValue = DynamicPropertiesUtils.getDynamicValue("point", point, myDynamicProperty);
            //!

            if (valueExpression != null) {
                valueExpression.setChartView(view);
                currentValue = valueExpression.getHint("point", point).toString();
            }

            view.setPoint(null);

        } catch (RuntimeException e) {
            Log.log("Exception inside DynamicXYGenerator.getValue", e);
        }
        return currentValue;
    }
}
