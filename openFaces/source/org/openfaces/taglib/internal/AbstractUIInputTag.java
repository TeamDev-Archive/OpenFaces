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
package org.openfaces.taglib.internal;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public abstract class AbstractUIInputTag extends AbstractComponentTag {
    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        if (isAutomaticValueAttributeHandling())
            setObjectProperty(component, "value");

        UIInput uiInput = (UIInput) component;

        setValidator(facesContext, uiInput);
        setValueChangeListener(facesContext, uiInput);

        setConverterProperty(facesContext, component, "converter");

        setBooleanProperty(component, "required");
        setBooleanProperty(component, "immediate");
        setStringProperty(component, "label");
    }

    protected boolean isAutomaticValueAttributeHandling() {
        return true;
    }
}
