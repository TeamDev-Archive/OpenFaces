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

import org.jfree.chart.axis.DateAxis;
import org.openfaces.component.chart.ChartAxis;
import org.openfaces.component.chart.ChartDateAxis;
import org.openfaces.component.chart.GridChartView;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DateAxisAdapter extends DateAxis {

    public DateAxisAdapter(String label, boolean visible, ChartDateAxis chartDateAxis, ChartAxis chartBaseAxis, GridChartView view) {
        if (!visible) {
            setVisible(false);
            return;
        }

        if (chartDateAxis != null && chartDateAxis.getDateFormat() != null) {
            setDateFormatOverride(chartDateAxis.getDateFormat());
        }
        FakeAxisStyle fakeStyle = new FakeAxisStyle(chartDateAxis, chartBaseAxis, view);
        AxisUtil.setupAxisPresentation(label, this, fakeStyle);
    }

}


