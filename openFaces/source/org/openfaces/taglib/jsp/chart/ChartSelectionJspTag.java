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

import org.openfaces.taglib.jsp.OUICommandJspTag;

import javax.el.ValueExpression;

public class ChartSelectionJspTag extends OUICommandJspTag {

    public ChartSelectionJspTag() {
        super(new org.openfaces.taglib.internal.chart.ChartSelectionTag());
    }

    public void setLineStyle(ValueExpression lineStyle) {
        getDelegate().setPropertyValue("lineStyle", lineStyle);
    }

    public void setFillPaint(ValueExpression fillPaint) {
        getDelegate().setPropertyValue("fillPaint", fillPaint);
    }

    public void setItem(ValueExpression selectedItem) {
        getDelegate().setPropertyValue("item", selectedItem);
    }

    public void setOnchange(ValueExpression onchange) {
        getDelegate().setPropertyValue("onchange", onchange);
    }
}
