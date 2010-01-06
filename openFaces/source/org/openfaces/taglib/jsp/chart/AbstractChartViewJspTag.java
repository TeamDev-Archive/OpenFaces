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

import org.openfaces.taglib.internal.chart.AbstractChartViewTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public abstract class AbstractChartViewJspTag extends AbstractStyledComponentJspTag {

    protected AbstractChartViewJspTag(AbstractChartViewTag delegate) {
        super(delegate);
    }

    public void setActionListener(MethodExpression actionListener) {
        getDelegate().setPropertyValue("actionListener", actionListener);
    }

    public void setForegroundAlpha(ValueExpression foregroundAlpha) {
        getDelegate().setPropertyValue("foregroundAlpha", foregroundAlpha);
    }

    public void setColors(ValueExpression colors) {
        getDelegate().setPropertyValue("colors", colors);
    }

    public void setUrl(ValueExpression url) {
        getDelegate().setPropertyValue("url", url);
    }

    public void setAction(MethodExpression action) {
        getDelegate().setPropertyValue("action", action);
    }

    public void setTooltip(ValueExpression tooltip) {
        getDelegate().setPropertyValue("tooltip", tooltip);
    }


}
