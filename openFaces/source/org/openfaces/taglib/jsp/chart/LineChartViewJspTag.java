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
package org.openfaces.taglib.jsp.chart;

import org.openfaces.taglib.internal.chart.LineChartViewTag;

import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LineChartViewJspTag extends GridChartViewJspTag {

    public LineChartViewJspTag() {
        super(new LineChartViewTag());
    }

    public void setShapesVisible(ValueExpression shapesVisible) {
        getDelegate().setPropertyValue("shapesVisible", shapesVisible);
    }

    public void setDefaultFillColor(ValueExpression defaultFillColor) {
        getDelegate().setPropertyValue("defaultFillColor", defaultFillColor);
    }

    public void setDefaultLineStyle(ValueExpression defaultLineStyle) {
        getDelegate().setPropertyValue("defaultLineStyle", defaultLineStyle);
    }

    public void setLineStyles(ValueExpression strokes) {
        getDelegate().setPropertyValue("lineStyles", strokes);
    }

    public void setFillPaints(ValueExpression fillPaints) {
        getDelegate().setPropertyValue("fillPaints", fillPaints);
    }
}
