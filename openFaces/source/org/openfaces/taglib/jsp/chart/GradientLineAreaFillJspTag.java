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

import org.openfaces.taglib.internal.chart.GradientLineAreaFillTag;

import javax.el.ValueExpression;

public class GradientLineAreaFillJspTag extends org.openfaces.taglib.jsp.AbstractComponentJspTag {

    public GradientLineAreaFillJspTag() {
        super(new GradientLineAreaFillTag());
    }


    public void setMinValueTransparency(ValueExpression minValueTransparency) {
        getDelegate().setPropertyValue("minValueTransparency", minValueTransparency);
    }

    public void setMaxValueTransparency(ValueExpression maxValueTransparency) {
        getDelegate().setPropertyValue("maxValueTransparency", maxValueTransparency);
    }
}
