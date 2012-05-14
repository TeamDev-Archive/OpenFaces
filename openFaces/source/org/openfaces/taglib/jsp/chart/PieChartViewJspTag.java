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

import org.openfaces.taglib.internal.chart.PieChartViewTag;

import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class PieChartViewJspTag extends AbstractChartViewJspTag {

    public PieChartViewJspTag() {
        super(new PieChartViewTag());
    }

    public void setLabelsVisible(ValueExpression labelsVisible) {
        getDelegate().setPropertyValue("labelsVisible", labelsVisible);
    }

     public void setShowShadow(ValueExpression shadow) {
        getDelegate().setPropertyValue("showShadow", shadow);
    }

    public void setShadowXOffset(ValueExpression shadowXOffset) {
        getDelegate().setPropertyValue("shadowXOffset", shadowXOffset);
    }

    public void setShadowYOffset(ValueExpression shadowYOffset) {
        getDelegate().setPropertyValue("shadowYOffset", shadowYOffset);
    }

    public void setShadowColor(ValueExpression shadowColor) {
        getDelegate().setPropertyValue("shadowColor", shadowColor);
    }
}

