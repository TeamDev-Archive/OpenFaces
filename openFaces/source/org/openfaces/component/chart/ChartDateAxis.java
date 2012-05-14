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
package org.openfaces.component.chart;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartDateAxis extends ChartAxis {
    private Date lowerBound;
    private Date upperBound;
    private DateFormat dateFormat;

    public DateFormat getDateFormat() {
        return ValueBindings.get(this, "dateFormat", dateFormat, DateFormat.class);
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Date getLowerBound() {
        return ValueBindings.get(this, "lowerBound", lowerBound, Date.class);
    }

    public void setLowerBound(Date lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Date getUpperBound() {
        return ValueBindings.get(this, "upperBound", upperBound, Date.class);
    }

    public void setUpperBound(Date upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (getParent() instanceof ChartView) {
            GridChartView view = (GridChartView) getParent();
            view.getAxes().add(this);
        }

    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                lowerBound,
                upperBound,
                dateFormat
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        lowerBound = (Date) state[i++];
        upperBound = (Date) state[i++];
        dateFormat = (DateFormat) state[i++];
    }
}
