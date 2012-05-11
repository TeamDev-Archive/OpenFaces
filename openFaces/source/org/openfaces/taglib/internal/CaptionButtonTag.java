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

import org.openfaces.component.CaptionButton;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class CaptionButtonTag extends OUICommandTag {

    public String getComponentType() {
        return CaptionButton.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.CaptionButtonRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        setStringProperty(component, "pressedStyle");
        setStringProperty(component, "pressedClass");
        setStringProperty(component, "imageUrl");
        setStringProperty(component, "rolloverImageUrl");
        setStringProperty(component, "pressedImageUrl");
        setStringProperty(component, "hint");
    }
}
