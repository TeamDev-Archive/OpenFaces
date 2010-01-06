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
package org.openfaces.taglib.internal.select;

import org.openfaces.component.select.SelectBooleanCheckbox;
import org.openfaces.taglib.internal.AbstractUIInputTag;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Roman Porotnikov
 */
public class SelectBooleanCheckboxTag extends AbstractUIInputTag {
    private static final String RENDERER_TYPE = "org.openfaces.SelectBooleanCheckboxRenderer";

    public String getComponentType() {
        return SelectBooleanCheckbox.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        SelectBooleanCheckbox sbc = (SelectBooleanCheckbox) component;
        String valueDeclaration = getPropertyValue("value");
        if (!setPropertyAsBinding(component, "value", valueDeclaration)) {
            if (valueDeclaration.equals("true"))
                sbc.setValue(true);
            else if(valueDeclaration.equals("false"))
                sbc.setValue(false);
            else if (valueDeclaration.equals("undefined"))
                sbc.setValue(null);
            else
                throw new FacesException("Unknown value attribute specification for <o:selectBooleanCheckbox>: " + valueDeclaration + "; it should be either a value binding expression or one of: \"true\", \"false\", or \"undefined\"");
        }

        setStringProperty(component, "accesskey");
        setStringProperty(component, "tabindex");
        setStringProperty(component, "title");
        setStringProperty(component, "dir");
        setStringProperty(component, "lang");
        setStringProperty(component, "onselect");
        setBooleanProperty(component, "disabled");
        setStringProperty(component, "disabledStyle");
        setStringProperty(component, "disabledClass");
        setBooleanProperty(component, "triStateAllowed");
        setLiteralCollectionProperty(component, "stateList");
        setStringProperty(component, "selectedImageUrl");
        setStringProperty(component, "unselectedImageUrl");
        setStringProperty(component, "undefinedImageUrl");
        setStringProperty(component, "rolloverSelectedImageUrl");
        setStringProperty(component, "rolloverUnselectedImageUrl");
        setStringProperty(component, "rolloverUndefinedImageUrl");
        setStringProperty(component, "pressedSelectedImageUrl");
        setStringProperty(component, "pressedUnselectedImageUrl");
        setStringProperty(component, "pressedUndefinedImageUrl");
        setStringProperty(component, "disabledSelectedImageUrl");
        setStringProperty(component, "disabledUnselectedImageUrl");
        setStringProperty(component, "disabledUndefinedImageUrl");
        setStringProperty(component, "selectedStyle");
        setStringProperty(component, "selectedClass");
        setStringProperty(component, "unselectedStyle");
        setStringProperty(component, "unselectedClass");
        setStringProperty(component, "undefinedStyle");
        setStringProperty(component, "undefinedClass");
    }

    @Override
    protected boolean isAutomaticValueAttributeHandling() {
        return false;
    }
}
