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
package org.openfaces.component.chart.impl;

import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.openfaces.component.chart.CategoryAxisLabelPosition;
import org.openfaces.component.chart.LineStyle;
import org.openfaces.component.chart.Orientation;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleBorderModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PropertiesConverter {

    private PropertiesConverter() {
    }

    public static int getSize(String pxSize, int defaultSize) {
        int intSize = defaultSize;

        if (pxSize == null)
            return intSize;

        if (pxSize.endsWith("px")) {
            try {
                String number = pxSize.substring(0, pxSize.indexOf("px"));
                intSize = new Integer(number);
            } catch (RuntimeException e) {
                throw new DataConversionException("Unable to convert size value. Correct value must looks like 200px. Currently defined: " + pxSize);
            }
        }
        return intSize;
    }

    public static Collection<Paint> getColors(Object colors) {
        Collection<Paint> resultColors = new ArrayList<Paint>();
        if (colors == null)
            return resultColors;

        if (colors != null) {
            if (colors instanceof String) {
                String[] st_colors = ((String) colors).split(",");

                if (st_colors == null)
                    return resultColors;

                if (st_colors.length == 0)
                    return resultColors;

                for (String st_color : st_colors) {
                    try {
                        resultColors.add(CSSUtil.parseColor(st_color));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Can't parse color string: " + st_color);
                    }
                }
            } else if (colors.getClass().isArray()) {
                resultColors = Arrays.asList((Paint[]) colors);
            } else if (colors instanceof Collection)
                resultColors = (Collection<Paint>) colors;
            else
                throw new IllegalArgumentException("'colors' attribute of chart view tag should contain either " +
                        "an array or a collection of colors, but a value of the following type encountered: "
                        + colors.getClass().getName());
        }

        return resultColors;
    }

    public static PlotOrientation toPlotOrientation(Orientation orientation) {
        if (orientation == null)
            return PlotOrientation.VERTICAL;

        if (orientation.equals(Orientation.HORIZONTAL))
            return PlotOrientation.HORIZONTAL;
        else
            return PlotOrientation.VERTICAL;
    }

    public static Stroke toStroke(StyleBorderModel border) {
        Stroke stroke = null;

        if (border != null) {
            int width = border.getWidth();
            String style = border.getStyle();

            if (style.equalsIgnoreCase("DASHED")) {
                float[] dash = {width * 4};
                stroke = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, width * 3, dash, 1F);
            } else if (style.equalsIgnoreCase("DOTTED")) {
                float[] dash = {width};
                stroke = new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, width * 3, dash, 1F);
            } else {
                stroke = new BasicStroke(width);
            }
        }
        return stroke;
    }

    public static LineStyle toLineStyle(StyleBorderModel border) {
        return new LineStyle(border.getColor(), toStroke(border));
    }


    public static CategoryLabelPositions toCategroryLabelPosition(CategoryAxisLabelPosition position) {
        if (position == null)
            return CategoryLabelPositions.STANDARD;

        if (position.equals(CategoryAxisLabelPosition.UP_45))
            return CategoryLabelPositions.UP_45;
        if (position.equals(CategoryAxisLabelPosition.UP_90))
            return CategoryLabelPositions.UP_90;
        if (position.equals(CategoryAxisLabelPosition.DOWN_45))
            return CategoryLabelPositions.DOWN_45;
        if (position.equals(CategoryAxisLabelPosition.DOWN_90))
            return CategoryLabelPositions.DOWN_90;


        return CategoryLabelPositions.STANDARD;
    }
}