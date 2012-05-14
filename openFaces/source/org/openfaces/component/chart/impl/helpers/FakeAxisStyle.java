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

import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartAxis;
import org.openfaces.component.chart.ChartNumberAxis;
import org.openfaces.component.chart.GridChartView;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;

/**
 * @author Ekaterina Shliakhovetskaya
 */
class FakeAxisStyle implements StyledComponent { // needed for cascading axis styles
    private String style;
    private StyledComponent[] chain;
    private boolean ticksVisible = true;
    private Double tickInterval;
    private int tickInsideLength;
    private int tickOutsideLength;
    private boolean minorTicksVisible;
    private int minorTickCount;
    private int minorTickInsideLength;
    private int minorTickOutsideLength;

    private boolean labelVisible = true;
    private boolean tickLabelsVisible = true;

    public FakeAxisStyle(ChartAxis axis, ChartAxis baseAxis, GridChartView view) {
        if (baseAxis != null) {
            if (baseAxis.getTextStyle() != null) {
                String baseAxisStyleString = baseAxis.getTextStyle();
                if (!baseAxisStyleString.endsWith(";")) {
                    baseAxisStyleString += ";";
                    style = baseAxisStyleString;
                }
            }
            style = baseAxis.getTextStyle();
            tickInterval = baseAxis instanceof ChartNumberAxis ? ((ChartNumberAxis) baseAxis).getTickInterval() : null;
            ticksVisible = baseAxis.isTicksVisible();
            labelVisible = baseAxis.isLabelVisible();
            tickLabelsVisible = baseAxis.isTickLabelsVisible();
            tickInsideLength = baseAxis.getTickInsideLength();
            tickOutsideLength = baseAxis.getTickOutsideLength();
            minorTicksVisible = baseAxis.getMinorTicksVisible();
            minorTickCount = baseAxis.getMinorTickCount();
            minorTickInsideLength = baseAxis.getMinorTickInsideLength();
            minorTickOutsideLength = baseAxis.getMinorTickOutsideLength();
        }

        if (axis != null) {
            if (axis.getTextStyle() != null) {
                if (style == null) {
                    style = axis.getTextStyle();
                } else {
                    StringBuilder buf = new StringBuilder();
                    if (!style.endsWith(";")) {

                        if (!axis.getTextStyle().endsWith(";")) {
                            buf.append(style).append(";").append(axis.getTextStyle()).append(";");
                        } else {
                            buf.append(style).append(";").append(axis.getTextStyle());
                        }
                        style = buf.toString();
                    } else {
                        if (!axis.getTextStyle().endsWith(";")) {
                            buf.append(style).append(axis.getTextStyle()).append(";");
                            style = buf.toString();
                        }
                        style = style + axis.getTextStyle();
                    }
                }
            }
            tickInterval= axis instanceof ChartNumberAxis ? ((ChartNumberAxis) axis).getTickInterval() : null;
            ticksVisible = axis.isTicksVisible();
            labelVisible = axis.isLabelVisible();
            tickLabelsVisible = axis.isTickLabelsVisible();
            tickInsideLength = axis.getTickInsideLength();
            tickOutsideLength = axis.getTickOutsideLength();
            minorTicksVisible = axis.getMinorTicksVisible();
            minorTickCount = axis.getMinorTickCount();
            minorTickInsideLength = axis.getMinorTickInsideLength();
            minorTickOutsideLength = axis.getMinorTickOutsideLength();
        }

        chain = new StyledComponent[]{
                Chart.DEFAULT_CHART_STYLE,
                (StyledComponent) view.getParent(),
                view,
                this
        };

    }

    public Double getTickInterval() {
        return tickInterval;
    }

    public int getTickInsideLength() {
        return tickInsideLength;
    }

    public int getTickOutsideLength() {
        return tickOutsideLength;
    }

    public boolean getMinorTicksVisible() {
        return minorTicksVisible;
    }

    public int getMinorTickCount() {
        return minorTickCount;
    }

    public int getMinorTickInsideLength() {
        return minorTickInsideLength;
    }

    public int getMinorTickOutsideLength() {
        return minorTickOutsideLength;
    }

    public boolean isTicksVisible() {
        return ticksVisible;
    }

    public boolean isLabelVisible() {
        return labelVisible;
    }

    public boolean isTickLabelsVisible() {
        return tickLabelsVisible;
    }

    public String getTextStyle() {
        return style;
    }

    public void setTextStyle(String style) {
        this.style = style;
    }

    public void setStyle(String style) {
        setTextStyle(style);
    }

    public String getStyle() {
        return getTextStyle();
    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        return chain;
    }

    public String getHint() {
        return null;
    }
}
