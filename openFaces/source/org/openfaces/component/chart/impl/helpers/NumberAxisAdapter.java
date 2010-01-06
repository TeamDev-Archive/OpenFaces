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
package org.openfaces.component.chart.impl.helpers;

import org.jfree.chart.axis.NumberAxis;
import org.openfaces.component.chart.ChartAxis;
import org.openfaces.component.chart.ChartNumberAxis;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.Series;
import org.openfaces.component.chart.Tuple;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class NumberAxisAdapter extends NumberAxis {

    public NumberAxisAdapter(String label, boolean visible, ChartNumberAxis chartNumberAxis, ChartAxis chartBaseAxis, GridChartView view) {
        if (!visible) {
            setVisible(false);
            return;
        }

        if (chartNumberAxis != null) {
            Series[] series = view.getChart().getModel().getSeries();
            if ((chartNumberAxis.getLowerBound() != null) ^ (chartNumberAxis.getUpperBound() != null)) {
                if (chartNumberAxis.getLowerBound() == null) {
                    chartNumberAxis.setLowerBound(getBound(series, true));
                } else {
                    chartNumberAxis.setUpperBound(getBound(series, false));
                }
            }
            if (chartNumberAxis.getLowerBound() != null)
                setLowerBound(chartNumberAxis.getLowerBound());
            if (chartNumberAxis.getUpperBound() != null)
                setUpperBound(chartNumberAxis.getUpperBound());
        }

        FakeAxisStyle fakeStyle = new FakeAxisStyle(chartNumberAxis, chartBaseAxis, view);
        AxisUtil.setupAxisPresentation(label, this, fakeStyle);
    }

    private Double getBound(Series[] series, boolean foundLowerBound) {
        double result = 0;
        for (Series sery : series) {
            if (sery == null)
                continue;

            Tuple[] tuples = sery.getTuples();
            for (Tuple tuple : tuples) {
                Number value = ((Number) tuple.getValue());
                if (foundLowerBound) {
                    if (value.doubleValue() < result)
                        result = value.doubleValue();
                } else {
                    if (value.doubleValue() > result)
                        result = value.doubleValue();
                }
            }
        }
        return result;
    }

}
