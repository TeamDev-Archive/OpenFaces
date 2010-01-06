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
package org.openfaces.taglib.internal.window;

import org.openfaces.component.window.MinimizeWindowButton;
import org.openfaces.taglib.internal.CaptionButtonTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class MinimizeWindowButtonTag extends CaptionButtonTag {

    @Override
    public String getComponentType() {
        return MinimizeWindowButton.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return "org.openfaces.MinimizeWindowButtonRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "restoreImageUrl");
        setStringProperty(component, "restoreRolloverImageUrl");
        setStringProperty(component, "restorePressedImageUrl");
        setStringProperty(component, "minimizeImageUrl");
        setStringProperty(component, "minimizeRolloverImageUrl");
        setStringProperty(component, "minimizePressedImageUrl");
    }
}