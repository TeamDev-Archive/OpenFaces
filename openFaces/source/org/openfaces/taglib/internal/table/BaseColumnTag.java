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
package org.openfaces.taglib.internal.table;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public abstract class BaseColumnTag extends AbstractComponentTag {
    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "headerValue", getPropertyValue("header"), "header");
        setStringProperty(component, "footerValue", getPropertyValue("footer"), "footer");

        setStringProperty(component, "width");
        setStringProperty(component, "align");
        setStringProperty(component, "valign");

        setBooleanProperty(component, "resizable");
        setStringProperty(component, "minResizingWidth");
        setBooleanProperty(component, "fixed");

        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");
        setStringProperty(component, "subHeaderStyle");
        setStringProperty(component, "subHeaderClass");
        setStringProperty(component, "footerStyle");
        setStringProperty(component, "footerClass");
        setStringProperty(component, "bodyStyle");
        setStringProperty(component, "bodyClass");

        setStringProperty(component, "headerOnclick");
        setStringProperty(component, "headerOndblclick");
        setStringProperty(component, "headerOnmousedown");
        setStringProperty(component, "headerOnmouseover");
        setStringProperty(component, "headerOnmousemove");
        setStringProperty(component, "headerOnmouseout");
        setStringProperty(component, "headerOnmouseup");

        setStringProperty(component, "bodyOnclick");
        setStringProperty(component, "bodyOndblclick");
        setStringProperty(component, "bodyOnmousedown");
        setStringProperty(component, "bodyOnmouseover");
        setStringProperty(component, "bodyOnmousemove");
        setStringProperty(component, "bodyOnmouseout");
        setStringProperty(component, "bodyOnmouseup");

        setStringProperty(component, "footerOnclick");
        setStringProperty(component, "footerOndblclick");
        setStringProperty(component, "footerOnmousedown");
        setStringProperty(component, "footerOnmouseover");
        setStringProperty(component, "footerOnmousemove");
        setStringProperty(component, "footerOnmouseout");
        setStringProperty(component, "footerOnmouseup");
    }
}
