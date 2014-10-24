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
package org.openfaces.taglib.internal;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public abstract class AbstractUIInputTag extends AbstractComponentTag {
    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        if (isAutomaticValueAttributeHandling())
            setObjectProperty(component, "value");

        UIInput uiInput = (UIInput) component;

        setValidator(context, uiInput);
        setValueChangeListener(context, uiInput);

        setConverterProperty(component, "converter");

        setBooleanProperty(component, "disabled");
        setBooleanProperty(component, "required");
        setBooleanProperty(component, "immediate");
        setStringProperty(component, "label");
    }

    protected boolean isAutomaticValueAttributeHandling() {
        return true;
    }
}
