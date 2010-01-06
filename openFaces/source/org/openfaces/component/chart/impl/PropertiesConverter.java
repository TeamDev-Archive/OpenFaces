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
package org.openfaces.component.chart.impl;

import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.openfaces.component.chart.CategoryAxisLabelPosition;
import org.openfaces.component.chart.Orientation;
import org.openfaces.renderkit.cssparser.StyleBorderModel;

import java.awt.*;

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

    public static Color[] getColors(String colorList) {
        if (colorList == null)
            return null;

        String[] st_colors = colorList.split(",");

        if (st_colors == null)
            return null;

        if (st_colors.length == 0)
            return null;

        Color[] colors = new Color[st_colors.length];
        for (int i = 0; i < st_colors.length; i++) {
            String st_color = st_colors[i];
            try {
                colors[i] = Color.decode(st_color.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Can't parse color string: " + st_color);
            }
        }

        return colors;
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
                float[] dush = {15.0F};
                stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0F, dush, 1F);
            } else if (style.equalsIgnoreCase("DOTTED")) {
                float[] dush = {4.0F};
                stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0F, dush, 1F);
            } else {
                stroke = new BasicStroke(width);
            }
        }
        return stroke;
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