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
package org.openfaces.taglib.internal.ajax;

import org.openfaces.component.ajax.AjaxSettings;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Eugene Goncharov
 */
public class AjaxSettingsTag extends AbstractComponentTag {

    public String getComponentType() {
        return AjaxSettings.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.AjaxSettingsRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setStringProperty(component, "onerror");
        setStringProperty(component, "onsuccess");
        setStringProperty(component, "onsessionexpired");
        setStringProperty(component, "onajaxstart");
        setStringProperty(component, "onajaxend");
    }
}
