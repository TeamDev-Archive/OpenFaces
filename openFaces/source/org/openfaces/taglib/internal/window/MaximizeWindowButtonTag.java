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
package org.openfaces.taglib.internal.window;

import org.openfaces.component.window.MaximizeWindowButton;
import org.openfaces.taglib.internal.ToggleCaptionButtonTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class MaximizeWindowButtonTag extends ToggleCaptionButtonTag {

    @Override
    public String getComponentType() {
        return MaximizeWindowButton.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return "org.openfaces.MaximizeWindowButtonRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "restoreImageUrl");
        setStringProperty(component, "restoreRolloverImageUrl");
        setStringProperty(component, "restorePressedImageUrl");
        setStringProperty(component, "maximizeImageUrl");
        setStringProperty(component, "maximizeRolloverImageUrl");
        setStringProperty(component, "maximizePressedImageUrl");
    }
}
