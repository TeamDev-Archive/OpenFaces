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

import org.openfaces.taglib.internal.chart.MarkerTag;

import javax.el.ValueExpression;

public class MarkerJspTag extends org.openfaces.taglib.jsp.AbstractComponentJspTag {

    public MarkerJspTag() {
        super(new MarkerTag());
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setStartValue(ValueExpression startValue) {
        getDelegate().setPropertyValue("startValue", startValue);
    }

    public void setEndValue(ValueExpression endValue) {
        getDelegate().setPropertyValue("endValue", endValue);
    }

    public void setDrawAsLine(ValueExpression drawAsLine) {
        getDelegate().setPropertyValue("drawAsLine", drawAsLine);
    }

    public void setTextStyle(ValueExpression textStyle) {
        getDelegate().setPropertyValue("textStyle", textStyle);
    }

    public void setLineStyle(ValueExpression lineStyle) {
        getDelegate().setPropertyValue("lineStyle", lineStyle);
    }

    public void setOutlineStyle(ValueExpression outlineStyle) {
        getDelegate().setPropertyValue("outlineStyle", outlineStyle);
    }

    public void setAlpha(ValueExpression alpha) {
        getDelegate().setPropertyValue("alpha", alpha);
    }

    public void setLabel(ValueExpression label) {
        getDelegate().setPropertyValue("label", label);
    }

    public void setLayer(ValueExpression layer) {
        getDelegate().setPropertyValue("layer", layer);
    }

    public void setLabelAnchor(ValueExpression labelAnchor) {
        getDelegate().setPropertyValue("labelAnchor", labelAnchor);
    }

    public void setLabelTextAnchor(ValueExpression labelTextAnchor) {
        getDelegate().setPropertyValue("labelTextAnchor", labelTextAnchor);
    }

    public void setLabelOffsetType(ValueExpression labelOffsetType) {
        getDelegate().setPropertyValue("labelOffsetType", labelOffsetType);
    }

    public void setLabelOffset(ValueExpression labelOffset) {
        getDelegate().setPropertyValue("labelOffset", labelOffset);
    }
}
