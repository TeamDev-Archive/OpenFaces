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
package org.openfaces.taglib.jsp.chart;

import org.openfaces.taglib.internal.chart.LevelIndicatorTag;

import javax.el.ValueExpression;

public class LevelIndicatorJspTag extends org.openfaces.taglib.jsp.AbstractComponentJspTag {

    public LevelIndicatorJspTag() {
        super(new LevelIndicatorTag());
    }

    public void setOrientation(ValueExpression orientation) {
        getDelegate().setPropertyValue("orientation", orientation);
    }

    public void setValue(ValueExpression value) {
        getDelegate().setPropertyValue("value", value);
    }

    public void setLevel(ValueExpression level) {
        getDelegate().setPropertyValue("value", level);
    }

    public void setIndicatorStyle(ValueExpression indicatorStyle) {
        getDelegate().setPropertyValue("indicatorStyle", indicatorStyle);
    }

    public void setIndicatorClass(ValueExpression indicatorClass) {
        getDelegate().setPropertyValue("indicatorClass", indicatorClass);
    }

    public void setLabelStyle(ValueExpression labelStyle) {
        getDelegate().setPropertyValue("labelStyle", labelStyle);
    }

    public void setLabelClass(ValueExpression labelClass) {
        getDelegate().setPropertyValue("labelClass", labelClass);
    }

    public void setIndicatorSegmentSize(ValueExpression indicatorSegmentSize) {
        getDelegate().setPropertyValue("indicatorSegmentSize", indicatorSegmentSize);
    }

    public void setFillDirection(ValueExpression fillDirection) {
        getDelegate().setPropertyValue("fillDirection", fillDirection);
    }

    public void setColors(ValueExpression colors) {
        getDelegate().setPropertyValue("colors", colors);
    }

    public void setTransitionLevels(ValueExpression transitionLevels) {
        getDelegate().setPropertyValue("transitionLevels", transitionLevels);
    }

    public void setColorBlendIntensity(ValueExpression colorBlendIntensity) {
        getDelegate().setPropertyValue("colorBlendIntensity", colorBlendIntensity);
    }
}
