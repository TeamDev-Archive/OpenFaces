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

package org.openfaces.taglib.internal.input;

import org.openfaces.component.input.Spinner;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * @author Alexander Golubev
 */
public class SpinnerTag extends DropDownComponentTag {

    public String getComponentType() {
        return Spinner.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.SpinnerRenderer";
    }

    protected boolean isAutomaticValueAttributeHandling() {
        return false;
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setIntProperty(component, "value");
        setBooleanProperty(component, "cycled");
        setBooleanProperty(component, "typingAllowed");
        setIntProperty(component, "maxValue");
        setIntProperty(component, "minValue");
        setIntProperty(component, "step");
        setStringProperty(component, "disabledIncreaseButtonImageUrl");
        setStringProperty(component, "disabledDecreaseButtonImageUrl");
        setStringProperty(component, "increaseButtonImageUrl");
        setStringProperty(component, "decreaseButtonImageUrl");
        setStringProperty(component, "onchange");
    }
}
