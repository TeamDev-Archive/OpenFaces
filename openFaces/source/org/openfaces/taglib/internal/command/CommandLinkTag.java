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
package org.openfaces.taglib.internal.command;

import org.openfaces.component.command.CommandLink;
import org.openfaces.taglib.internal.OUICommandTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class CommandLinkTag extends OUICommandTag {

    public String getComponentType() {
        return CommandLink.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.CommandLinkRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "value");
        setStringProperty(component, "accesskey");
        setStringProperty(component, "tabindex");
        setStringProperty(component, "lang");
        setStringProperty(component, "title");
        setStringProperty(component, "dir");
        setStringProperty(component, "charset");
        setStringProperty(component, "coords");
        setStringProperty(component, "hreflang");
        setStringProperty(component, "rel");
        setStringProperty(component, "rev");
        setStringProperty(component, "shape");
        setStringProperty(component, "target");
        setStringProperty(component, "type");
    }
}
