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

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.openfaces.component.chart.ChartAxis;
import org.openfaces.component.chart.ChartCategoryAxis;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.component.chart.impl.PropertiesConverter;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class CategoryAxisAdapter extends CategoryAxis {

    public CategoryAxisAdapter(String label, boolean visible, ChartCategoryAxis chartCategoryAxis, ChartAxis chartBaseAxis, GridChartView view) {
        if (!visible) {
            setVisible(false);
            return;
        }
        if (chartCategoryAxis == null) {
            setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        } else {
            setCategoryLabelPositions(PropertiesConverter.toCategroryLabelPosition(chartCategoryAxis.getPosition()));
        }

        FakeAxisStyle fakeStyle = new FakeAxisStyle(chartCategoryAxis, chartBaseAxis, view);
        AxisUtil.setupAxisPresentation(label, this, fakeStyle);
    }


}


