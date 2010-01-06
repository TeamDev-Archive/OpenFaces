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
package org.openfaces.component.chart;

import org.openfaces.component.chart.impl.plots.DynamicPropertiesUtils;

import javax.el.ELContext;
import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartViewValueExpression extends ValueExpression {

    private ChartView chartView;
    private ValueExpression valueExpression;

    public ChartViewValueExpression() {
    }

    public ChartViewValueExpression(ValueExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    public ChartView getChartView() {
        return chartView;
    }

    public void setChartView(ChartView chartView) {
        this.chartView = chartView;
    }

    public Object getValue(ELContext elContext) {
        if (valueExpression != null)
            return valueExpression.getValue(elContext);
        return null;
    }

    public void setValue(ELContext elContext, Object value) {
        throw new UnsupportedOperationException("Property is read-only");
    }

    public boolean isReadOnly(ELContext elContext) {
        return true;
    }

    public Class getType(ELContext elContext) {
        return Object.class;
    }

    public final Object getHint(String name, Object value) {
        return DynamicPropertiesUtils.getDynamicValue(name, value, this);
    }

    public Class getExpectedType() {
        return Object.class;
    }

    public String getExpressionString() {
        return null;
    }

    public boolean equals(Object o) {
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public boolean isLiteralText() {
        return false;
    }
}
