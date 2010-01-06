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
package org.openfaces.taglib.internal.output;

import org.openfaces.component.output.HintLabel;
import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class HintLabelTag extends AbstractComponentTag {

    public String getComponentType() {
        return HintLabel.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.HintLabelRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setBooleanProperty(component, "escape");
        setObjectProperty(component, "value");
        setStringProperty(component, "hint");

        setIntProperty(component, "hintTimeout");

        setConverterProperty(facesContext, component, "converter");
        setStringProperty(component, "hintStyle");
        setStringProperty(component, "hintClass");
    }
}
