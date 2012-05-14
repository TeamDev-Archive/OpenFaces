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
package org.openfaces.component.chart.impl.helpers;

import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.title.TextTitle;
import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartTitle;
import org.openfaces.component.chart.ChartViewType;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleBorderModel;
import org.openfaces.renderkit.cssparser.StyleObjectModel;

/**
 * @author Ekaterina Shliakhovetskaya
 */
class TextTitleAdapter extends TextTitle {
    public TextTitleAdapter(Chart chart) {
        ChartTitle title = chart.getTitle();
        if (title == null || title.getText() == null)
            return;

        setText(title.getText());

        if (title.getActionExpression() == null && title.getUrl() != null) {
            setURLText(title.getUrl());
        }

        setToolTipText(title.getTooltip());

        StyleObjectModel styleModel = title.getStyleObjectModel();
        if (styleModel != null) {
            setFont(CSSUtil.getFont(styleModel));

            final boolean chartTitlePaintDefined = chart.getChartView().getBackgroundPaint() != null;

            if (chartTitlePaintDefined) {
                setExpandToFitSpace(true);
                setBackgroundPaint(chart.getChartView().getTitlePaint());
            }

            if (styleModel.getBackground() != null && !chartTitlePaintDefined) {
                setBackgroundPaint(styleModel.getBackground());
                setBorder(new BlockBorder(styleModel.getBackground()));
            }

            if (styleModel.getColor() != null) {
                setPaint(styleModel.getColor());
            }

            StyleBorderModel border = styleModel.getBorder();
            if (border != null) {
                if (!border.isNone() && border.getColor() != null) {
                    BlockBorder bb = new BlockBorder(border.getColor());
                    setBorder(bb);
                }
            }

            setMargin(styleModel.getMargin(0), styleModel.getMargin(1), styleModel.getMargin(2), styleModel.getMargin(3));
        }

        setNotify(true);
    }

}
