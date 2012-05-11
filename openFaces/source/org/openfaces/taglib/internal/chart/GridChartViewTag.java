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
package org.openfaces.taglib.internal.chart;

import org.openfaces.component.chart.ChartDomain;
import org.openfaces.component.chart.ChartLabelPosition;
import org.openfaces.component.chart.Orientation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public abstract class GridChartViewTag extends AbstractChartViewTag {
    @Override
    public String getComponentType() {
        return "org.openfaces.GridChartView";
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setEnumerationProperty(component, "orientation", Orientation.class);

        setBooleanProperty(component, "labelsVisible");
        setEnumerationProperty(component, "showAxes", ChartDomain.class);

        setStringProperty(component, "keyAxisLabel");
        setStringProperty(component, "valueAxisLabel");

        setLineStyleObjectProperty(component, "defaultOutlineStyle");
        setObjectProperty(component, "outlines");

        setEnumerationProperty(component, "defaultLabelsPosition", ChartLabelPosition.class);
        setEnumerationProperty(component, "positiveLabelsPosition", ChartLabelPosition.class);
        setEnumerationProperty(component, "negativeLabelsPosition", ChartLabelPosition.class);

        setDoubleProperty(component, "labelsOffset");
    }
}
