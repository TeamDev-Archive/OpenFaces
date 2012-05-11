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

package org.openfaces.taglib.internal.select;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * @author Oleg Marshalenko
 */
public class SelectItemTag extends AbstractComponentTag {
    private static final String COMPONENT_TYPE = "org.openfaces.SelectItem";

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);
        setStringProperty(component, "itemDescription");
        setStringProperty(component, "itemLabel");
        setBooleanProperty(component, "itemDisabled");
        setObjectProperty(component, "itemValue");
        setValueExpressionProperty(component, "value");
    }
}
