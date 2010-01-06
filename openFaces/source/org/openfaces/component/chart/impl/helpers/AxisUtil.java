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

import org.jfree.chart.axis.Axis;
import org.openfaces.component.chart.impl.PropertiesConverter;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

/**
 * @author Ekaterina Shliakhovetskaya
 */
class AxisUtil {

    public static void setupAxisPresentation(String label, Axis axis, FakeAxisStyle axisStyle) {
        StyleObjectModel model = axisStyle.getStyleObjectModel();
        StyleBorderModel border = model.getBorder();
        if (border != null) {
            if (border.getColor() != null) {
                axis.setAxisLinePaint(border.getColor());
            }

            if (!border.isNone()) {
                axis.setAxisLineVisible(true);
                axis.setAxisLineStroke(PropertiesConverter.toStroke(border));
                axis.setTickMarkPaint(border.getColor());
                axis.setTickMarksVisible(true);
                if (!axis.isTickMarksVisible())
                    axis.setTickMarksVisible(false);
            }

            if (border.getStyle().equalsIgnoreCase("none")) {
                axis.setAxisLineVisible(false);
                axis.setTickMarksVisible(false);
                if (axis.isTickMarksVisible())
                    axis.setTickMarksVisible(true);
            }

        }

        axis.setTickLabelsVisible(axisStyle.isTickLabelsVisible());

        if (axisStyle.isLabelVisible() && label != null) {
            axis.setLabel(label);
            axis.setLabelPaint(model.getColor());
            axis.setLabelFont(CSSUtil.getFont(model));
        }

        if (axisStyle.isTickLabelsVisible()) {
            axis.setTickLabelsVisible(true);
            axis.setTickLabelPaint(model.getColor());
            axis.setTickLabelFont(CSSUtil.getFont(model));
        } else {
            axis.setTickLabelsVisible(false);
        }

    }


}
