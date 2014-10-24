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

    @Override
    protected boolean isAutomaticValueAttributeHandling() {
        return false;
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);
        setNumberProperty(component, "value");
        setBooleanProperty(component, "cycled");
        setBooleanProperty(component, "typingAllowed");
        setNumberProperty(component, "maxValue");
        setNumberProperty(component, "minValue");
        setNumberProperty(component, "step");
        setStringProperty(component, "disabledIncreaseButtonImageUrl");
        setStringProperty(component, "disabledDecreaseButtonImageUrl");
        setStringProperty(component, "increaseButtonImageUrl");
        setStringProperty(component, "decreaseButtonImageUrl");
        setStringProperty(component, "onchange");
    }
}
