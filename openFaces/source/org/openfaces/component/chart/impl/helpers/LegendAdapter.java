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

import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartLabels;
import org.openfaces.component.chart.ChartLegend;
import org.openfaces.component.chart.LegendPosition;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

/**
 * @author Ekaterina Shliakhovetskaya
 */
class LegendAdapter extends LegendTitle {
    public LegendAdapter(Plot plot, Chart chart) {
        super(plot);
        ChartLegend legend = chart.getLegend();

        setPosition(RectangleEdge.BOTTOM);

        StyleObjectModel cssLegendModel = (legend != null)
                ? legend.getStyleObjectModel()
                : chart.getStyleObjectModel();

        setBackgroundPaint(cssLegendModel.getBackground());

        StyleBorderModel border = cssLegendModel.getBorder();

        if (border != null && !border.isNone()) {
            setBorder(new BlockBorder(border.getColor()));
        } else {
            setBorder(new BlockBorder(cssLegendModel.getBackground()));
        }

        setMargin(cssLegendModel.getMargin(0), cssLegendModel.getMargin(1), cssLegendModel.getMargin(2), cssLegendModel.getMargin(3));

        if (legend != null) {
            LegendPosition position = legend.getPosition();
            setPosition(position != null ? position.toRectangleEdge() : RectangleEdge.BOTTOM);
        }

        //from legend labels
        ChartLabels labels = (legend != null) ? legend.getLabels() : null;
        if (labels != null) {
            StyleObjectModel cssLabelsModel = labels.getStyleObjectModel();
            setItemPaint(cssLabelsModel.getColor());
            setItemFont(CSSUtil.getFont(cssLabelsModel));
        } else {
            setItemPaint(cssLegendModel.getColor());
            setItemFont(CSSUtil.getFont(cssLegendModel));
        }

    }
}
