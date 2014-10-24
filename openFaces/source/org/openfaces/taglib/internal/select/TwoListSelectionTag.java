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

import org.openfaces.component.select.TwoListSelection;
import org.openfaces.taglib.internal.AbstractUIInputTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class TwoListSelectionTag extends AbstractUIInputTag {
    public String getComponentType() {
        return TwoListSelection.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.TwoListSelectionRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        setStringProperty(component, "leftListboxHeader");
        setStringProperty(component, "rightListboxHeader");
        setStringProperty(component, "headerStyle");
        setStringProperty(component, "headerClass");
        setIntProperty(component, "size");
        setStringProperty(component, "tabindex");
        setStringProperty(component, "listStyle");
        setStringProperty(component, "listClass");
        setBooleanProperty(component, "sortingAllowed");
        setBooleanProperty(component, "reorderingAllowed");
        setStringProperty(component, "onadd");
        setStringProperty(component, "onremove");
        setBooleanProperty(component, "allowAddRemoveAll");
        setStringProperty(component, "addAllHint");
        setStringProperty(component, "addHint");
        setStringProperty(component, "removeAllHint");
        setStringProperty(component, "removeHint");
        setStringProperty(component, "moveUpHint");
        setStringProperty(component, "moveDownHint");

        setStringProperty(component, "addText");
        setStringProperty(component, "removeText");
        setStringProperty(component, "addAllText");
        setStringProperty(component, "removeAllText");
        setStringProperty(component, "moveUpText");
        setStringProperty(component, "moveDownText");

        setStringProperty(component, "buttonStyle");
        setStringProperty(component, "buttonClass");

        setStringProperty(component, "disabledButtonClass");
        setStringProperty(component, "disabledButtonStyle");
        setStringProperty(component, "disabledClass");
        setStringProperty(component, "disabledHeaderClass");
        setStringProperty(component, "disabledHeaderStyle");
        setStringProperty(component, "disabledListClass");
        setStringProperty(component, "disabledListStyle");
        setStringProperty(component, "disabledStyle");

    }
}
