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

import org.openfaces.taglib.internal.AbstractUIInputTag;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * @author Oleg Marshalenko
 */
public abstract class AbstractUISelectManyInputTag extends AbstractUIInputTag {

    public void setComponentProperties(FacesContext facesContext, UIComponent uiComponent) {
        super.setComponentProperties(facesContext, uiComponent);
        setStringProperty(uiComponent, "accesskey");
        setStringProperty(uiComponent, "tabindex");
        setStringProperty(uiComponent, "title");
        setStringProperty(uiComponent, "dir");
        setStringProperty(uiComponent, "lang");
        setStringProperty(uiComponent, "onselect");
        setBooleanProperty(uiComponent, "disabled");

        setBooleanProperty(uiComponent, "readonly");
        setStringProperty(uiComponent, "border");
        setStringProperty(uiComponent, "layout");

        setStringProperty(uiComponent, "enabledStyle");
        setStringProperty(uiComponent, "enabledClass");
        setStringProperty(uiComponent, "disabledStyle");
        setStringProperty(uiComponent, "disabledClass");

        setStringProperty(uiComponent, "selectedImageUrl");
        setStringProperty(uiComponent, "unselectedImageUrl");
        setStringProperty(uiComponent, "rolloverSelectedImageUrl");
        setStringProperty(uiComponent, "rolloverUnselectedImageUrl");
        setStringProperty(uiComponent, "pressedSelectedImageUrl");
        setStringProperty(uiComponent, "pressedUnselectedImageUrl");
        setStringProperty(uiComponent, "disabledSelectedImageUrl");
        setStringProperty(uiComponent, "disabledUnselectedImageUrl");

        setStringProperty(uiComponent, "focusedItemStyle");
        setStringProperty(uiComponent, "focusedItemClass");
        setStringProperty(uiComponent, "selectedItemStyle");
        setStringProperty(uiComponent, "selectedItemClass");
        setStringProperty(uiComponent, "rolloverItemStyle");
        setStringProperty(uiComponent, "rolloverItemClass");
        setStringProperty(uiComponent, "pressedItemStyle");
        setStringProperty(uiComponent, "pressedItemClass");
    }

}
