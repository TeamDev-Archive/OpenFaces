/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */


package org.openfaces.taglib.internal.input;

import org.openfaces.component.input.DefaultMasks;
import org.openfaces.component.input.MaskEdit;
import org.openfaces.taglib.internal.AbstractComponentTag;
import org.openfaces.taglib.internal.OUIInputTextTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Sergey Pensov
 */
public class MaskEditTag extends OUIInputTextTag {
    private static final String RENDERER_TYPE = "org.openfaces.MaskEditRenderer";

    public String getComponentType() {
        return MaskEdit.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }


    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setBooleanProperty(component, "rendered");
        setStringProperty(component, "mask");
        setStringProperty(component, "blank");
        setStringProperty(component, "maskSymbolArray");
        setStringProperty(component, "dictionary");
        setBooleanProperty(component,"blankVisible");
        setEnumerationProperty(component,"defaultMask",DefaultMasks.class);
    }
}
