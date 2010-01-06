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

import org.openfaces.taglib.internal.chart.ChartTitleTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartTitleJspTag extends AbstractStyledComponentJspTag {

    public ChartTitleJspTag() {
        super(new ChartTitleTag());
    }

    public void setActionListener(MethodExpression actionListener) {
        getDelegate().setPropertyValue("actionListener", actionListener);
    }


    public void setUrl(ValueExpression url) {
        getDelegate().setPropertyValue("url", url);
    }

    public void setAction(MethodExpression action) {
        getDelegate().setPropertyValue("action", action);
    }

    public void setText(ValueExpression text) {
        getDelegate().setPropertyValue("text", text);
    }

    public void setTooltip(ValueExpression tooltip) {
        getDelegate().setPropertyValue("tooltip", tooltip);
    }


}
