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
package org.openfaces.taglib.internal.input;

import org.openfaces.component.Side;
import org.openfaces.taglib.internal.OUIInputTextTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public abstract class DropDownComponentTag extends OUIInputTextTag {
    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setEnumerationProperty(component, "buttonAlignment", Side.class);
        setStringProperty(component, "fieldStyle");
        setStringProperty(component, "disabledFieldStyle");
        setStringProperty(component, "rolloverFieldStyle");
        setStringProperty(component, "buttonStyle");
        setStringProperty(component, "disabledButtonStyle");
        setStringProperty(component, "rolloverButtonStyle");
        setStringProperty(component, "disabledStyle");

        setStringProperty(component, "fieldClass");
        setStringProperty(component, "disabledFieldClass");
        setStringProperty(component, "rolloverFieldClass");
        setStringProperty(component, "disabledClass");
        setStringProperty(component, "buttonClass");
        setStringProperty(component, "disabledButtonClass");
        setStringProperty(component, "rolloverButtonClass");

        setStringProperty(component, "buttonImageUrl");
        setStringProperty(component, "disabledButtonImageUrl");

        setStringProperty(component, "pressedButtonStyle");
        setStringProperty(component, "pressedButtonClass");

        setStringProperty(component, "rolloverStyle");
        setStringProperty(component, "rolloverClass");
    }

}
