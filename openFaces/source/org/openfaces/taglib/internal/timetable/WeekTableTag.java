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
package org.openfaces.taglib.internal.timetable;

import org.openfaces.component.timetable.WeekTable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Roman Porotnikov
 */
public class WeekTableTag extends TimeScaleTableTag {
    public String getComponentType() {
        return WeekTable.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.WeekTableRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "weekdayColumnSeparator");
        setStringProperty(component, "weekdayHeadersRowSeparator");
        setStringProperty(component, "weekdayHeadersRowStyle");
        setStringProperty(component, "weekdayHeadersRowClass");
        setStringProperty(component, "weekdayStyle");
        setStringProperty(component, "weekdayClass");
        setStringProperty(component, "weekdayPattern");
    }
}
