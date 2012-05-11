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
package org.openfaces.taglib.internal.filter;

import org.openfaces.component.Side;
import org.openfaces.component.input.SuggestionMode;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public abstract class AutoCompleteFilterTag extends ExpressionFilterTag {
    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setBooleanProperty(component, "customValueAllowed");

        setEnumerationProperty(component, "suggestionMode", SuggestionMode.class);
        setIntProperty(component, "suggestionMinChars");
        setIntProperty(component, "suggestionDelay");
        setBooleanProperty(component, "autoComplete");

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

        setIntProperty(component, "timeout");

        setEnumerationProperty(component, "listAlignment", Side.class);

        setLineStyleProperty(component, "horizontalGridLines");
        setLineStyleProperty(component, "verticalGridLines");
        setLineStyleProperty(component, "headerHorizSeparator");
        setLineStyleProperty(component, "headerVertSeparator");
        setLineStyleProperty(component, "multiHeaderSeparator");
        setLineStyleProperty(component, "multiFooterSeparator");
        setLineStyleProperty(component, "footerHorizSeparator");
        setLineStyleProperty(component, "footerVertSeparator");

        setIntProperty(component, "maxlength");
    }
}
