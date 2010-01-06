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
package org.openfaces.taglib.internal.util;

import org.openfaces.component.util.Debug;
import org.openfaces.taglib.internal.window.WindowTag;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * @author Dmitry Pikhulya
 */
public class DebugTag extends WindowTag {

    @Override
    public String getComponentType() {
        return Debug.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return "org.openfaces.DebugRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
    }

}
