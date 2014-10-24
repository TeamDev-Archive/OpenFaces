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
package org.openfaces.taglib.internal.input;

import org.openfaces.component.Side;
import org.openfaces.component.input.SuggestionMode;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Pavel Kaplin
 */
public class DropDownFieldTag extends DropDownComponentTag {
    private static final String COMPONENT_TYPE = "org.openfaces.DropDownField";
    private static final String RENDERER_TYPE = "org.openfaces.DropDownFieldRenderer";

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        setStringProperty(component, "var", false, false);
        setBooleanProperty(component, "customValueAllowed");

        setEnumerationProperty(component, "suggestionMode", SuggestionMode.class);
        setIntProperty(component, "suggestionMinChars");
        setIntProperty(component, "suggestionDelay");
        setBooleanProperty(component, "autoComplete");
        setIntProperty(component, "preloadedItemCount");
        setIntProperty(component, "pageSize");
        setObjectProperty(component, "totalItemCount");
        setBooleanProperty(component, "cachingAllowed");

        setStringProperty(component, "rolloverStyle");
        setStringProperty(component, "rolloverStyle");
        setStringProperty(component, "listStyle");
        setStringProperty(component, "rolloverListStyle");
        setStringProperty(component, "listItemStyle");
        setStringProperty(component, "rolloverListItemStyle");

        setStringProperty(component, "rolloverClass");
        setStringProperty(component, "listClass");
        setStringProperty(component, "rolloverListClass");
        setStringProperty(component, "listItemClass");
        setStringProperty(component, "rolloverListItemClass");
        setStringProperty(component, "oddListItemStyle");
        setStringProperty(component, "oddListItemClass");
        setStringProperty(component, "listHeaderRowStyle");
        setStringProperty(component, "listHeaderRowClass");
        setStringProperty(component, "listFooterRowStyle");
        setStringProperty(component, "listFooterRowClass");

        setIntProperty(component, "timeout");

        setStringProperty(component, "ondropdown");
        setStringProperty(component, "oncloseup");

        setEnumerationProperty(component, "listAlignment", Side.class);

        setLineStyleProperty(component, "horizontalGridLines");
        setLineStyleProperty(component, "verticalGridLines");
        setLineStyleProperty(component, "headerHorizSeparator");
        setLineStyleProperty(component, "headerVertSeparator");
        setLineStyleProperty(component, "multiHeaderSeparator");
        setLineStyleProperty(component, "multiFooterSeparator");
        setLineStyleProperty(component, "footerHorizSeparator");
        setLineStyleProperty(component, "footerVertSeparator");

        setIntProperty(component, "size");
        setIntProperty(component, "maxlength");
        setBooleanProperty(component, "changeValueOnSelect");
    }
}
