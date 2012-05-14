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

import org.openfaces.component.timetable.TimePeriodSwitcher;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class TimePeriodSwitcherTag extends AbstractSwitcherTag {

    public String getComponentType() {
        return TimePeriodSwitcher.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.TimePeriodSwitcherRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setObjectProperty(component, "monthPattern");
        setObjectProperty(component, "fromWeekPattern");
        setObjectProperty(component, "toWeekPattern");
        setObjectProperty(component, "dayPattern");
        setObjectProperty(component, "dayUpperPattern");
    }
}
