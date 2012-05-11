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

package org.openfaces.taglib.internal.timetable;

import org.openfaces.taglib.internal.AbstractComponentTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Roman Gorodischer
 */
public abstract class AbstractSwitcherTag extends AbstractComponentTag {

    @Override
    public void setComponentProperties(FacesContext facesContext, UIComponent component) {
        super.setComponentProperties(facesContext, component);

        setStringProperty(component, "for", false, false);

        setStringProperty(component, "dateFormat");
        setStringProperty(component, "pattern");
        setLocaleProperty(component, "locale");
        setTimeZoneProperty(component, "timeZone");

        setBooleanProperty(component, "enabled");

        setStringProperty(component, "previousButtonStyle");
        setStringProperty(component, "previousButtonClass");
        setStringProperty(component, "previousButtonRolloverStyle");
        setStringProperty(component, "previousButtonRolloverClass");
        setStringProperty(component, "previousButtonPressedStyle");
        setStringProperty(component, "previousButtonPressedClass");
        setStringProperty(component, "previousButtonImageUrl");

        setStringProperty(component, "nextButtonStyle");
        setStringProperty(component, "nextButtonClass");
        setStringProperty(component, "nextButtonRolloverStyle");
        setStringProperty(component, "nextButtonRolloverClass");
        setStringProperty(component, "nextButtonPressedStyle");
        setStringProperty(component, "nextButtonPressedClass");
        setStringProperty(component, "nextButtonImageUrl");

        setStringProperty(component, "textStyle");     //text
        setStringProperty(component, "textClass");
        setStringProperty(component, "textRolloverStyle");
        setStringProperty(component, "textRolloverClass");

    }

}