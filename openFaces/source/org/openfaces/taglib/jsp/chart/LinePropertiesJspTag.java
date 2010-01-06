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

import org.openfaces.taglib.internal.chart.LinePropertiesTag;

import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class LinePropertiesJspTag extends AbstractStyledComponentJspTag {

    public LinePropertiesJspTag() {
        super(new LinePropertiesTag());
    }

    public void setLabelsVisible(ValueExpression labelsVisible) {
        getDelegate().setPropertyValue("labelsVisible", labelsVisible);
    }

    public void setHideSeries(ValueExpression hideSeries) {
        getDelegate().setPropertyValue("hideSeries", hideSeries);
    }

    public void setShapesVisible(ValueExpression shapesVisible) {
        getDelegate().setPropertyValue("shapesVisible", shapesVisible);
    }

    public void setShowInLegend(ValueExpression showInLegend) {
        getDelegate().setPropertyValue("showInLegend", showInLegend);
    }

    public void setCondition(ValueExpression condition) {
        getDelegate().setPropertyValue("condition", condition);
    }
}

