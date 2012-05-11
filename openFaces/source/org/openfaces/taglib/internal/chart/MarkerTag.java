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

import org.openfaces.component.chart.Marker;
import org.openfaces.component.chart.MarkerLabelAnchor;
import org.openfaces.component.chart.MarkerLabelOffsetType;
import org.openfaces.component.chart.MarkerLabelTextAnchor;
import org.openfaces.component.chart.MarkerLayer;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class MarkerTag extends AbstractComponentTag {
    public static final String COMPONENT_TYPE = "org.openfaces.Marker";
    public static final String COMPONENT_FAMILY = "org.openfaces.Marker";


    public String getComponentType() {
        return Marker.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setObjectProperty(component, "value");
        setDoubleProperty(component, "startValue");
        setDoubleProperty(component, "endValue");

        setBooleanProperty(component, "drawAsLine");

        setStringProperty(component, "textStyle");

        setLineStyleObjectProperty(component, "lineStyle");
        setLineStyleObjectProperty(component, "outlineStyle");

        setFloatProperty(component, "alpha");
        setStringProperty(component, "label");

        setEnumerationProperty(component, "layer", MarkerLayer.class);

        setEnumerationProperty(component, "labelAnchor", MarkerLabelAnchor.class);
        setEnumerationProperty(component, "labelTextAnchor", MarkerLabelTextAnchor.class);
        setEnumerationProperty(component, "labelOffsetType", MarkerLabelOffsetType.class);

        setStringProperty(component, "labelOffset");
    }
}
