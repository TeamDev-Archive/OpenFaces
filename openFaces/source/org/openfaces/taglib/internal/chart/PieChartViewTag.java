/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.internal.chart;

import org.openfaces.component.chart.PieChartView;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class PieChartViewTag extends AbstractChartViewTag {

    public String getComponentType() {
        return "org.openfaces.PieChartView";
    }

    public String getRendererType() {
        return null;
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        PieChartView view = (PieChartView) component;

        String labelsVisible = getPropertyValue("labelsVisible");
        if (labelsVisible != null) {
            Boolean labelVisible = Boolean.valueOf(labelsVisible);
            view.setLabelsVisible(labelVisible);
        }
    }

}
