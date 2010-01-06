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

import org.openfaces.component.chart.Chart;
import org.openfaces.component.chart.ChartAxis;
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
            ticksVisible = baseAxis.isTicksVisible();
            labelVisible = baseAxis.isLabelVisible();
            tickLabelsVisible = baseAxis.isTickLabelsVisible();
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
            ticksVisible = axis.isTicksVisible();
            labelVisible = axis.isLabelVisible();
            tickLabelsVisible = axis.isTickLabelsVisible();
        }

        chain = new StyledComponent[]{
                Chart.DEFAULT_CHART_STYLE,
                (StyledComponent) view.getParent(),
                view,
                this
        };

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
