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

import org.openfaces.taglib.internal.chart.BarChartViewTag;

import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class BarChartViewJspTag extends GridChartViewJspTag {

    public BarChartViewJspTag() {
        super(new BarChartViewTag());
    }

    public void setShowGradient(ValueExpression useGradient) {
        getDelegate().setPropertyValue("showGradient", useGradient);
    }

    public void setG1WhitePosition(ValueExpression g1WhitePosition) {
        getDelegate().setPropertyValue("g1WhitePosition", g1WhitePosition);
    }

    public void setG2FullIntensityPosition(ValueExpression g2FullIntensityPosition) {
        getDelegate().setPropertyValue("g2FullIntensityPosition", g2FullIntensityPosition);
    }

    public void setG3LightIntensityPosition(ValueExpression g3LightIntensityPosition) {
        getDelegate().setPropertyValue("g3LightIntensityPosition", g3LightIntensityPosition);
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

