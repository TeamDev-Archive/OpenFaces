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

import org.openfaces.taglib.internal.chart.ChartTag;

import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartJspTag extends AbstractStyledComponentJspTag {

    public ChartJspTag() {
        super(new ChartTag());
    }

    public void setTextStyle(ValueExpression textStyle) {
        getDelegate().setPropertyValue("textStyle", textStyle);
    }

    public void setTitleText(ValueExpression titleText) {
        getDelegate().setPropertyValue("titleText", titleText);
    }

    public void setView(ValueExpression view) {
        getDelegate().setPropertyValue("view", view);
    }

    public void setLegendVisible(ValueExpression legendVisible) {
        getDelegate().setPropertyValue("legendVisible", legendVisible);
    }

    public void setModel(ValueExpression model) {
        getDelegate().setPropertyValue("model", model);
    }

    public void setWidth(ValueExpression width) {
        getDelegate().setPropertyValue("width", width);
    }

    public void setHeight(ValueExpression height) {
        getDelegate().setPropertyValue("height", height);
    }

    public void setTimePeriodPrecision(ValueExpression timePeriodPrecision) {
        getDelegate().setPropertyValue("timePeriodPrecision", timePeriodPrecision);
    }
}
