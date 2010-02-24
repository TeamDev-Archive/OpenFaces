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
package org.openfaces.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class StyleParam extends RawScript {

    public StyleParam(UIComponent component, String styleName, String defaultStyleClass) {
        this(component, styleName, defaultStyleClass, StyleGroup.regularStyleGroup());
    }

    public StyleParam(UIComponent component, String styleName, String defaultStyleClass, StyleGroup styleGroup) {
        this(component,
                (String) component.getAttributes().get(styleName + "Style"),
                defaultStyleClass,
                (String) component.getAttributes().get(styleName + "Class"),
                styleGroup);
    }

    public StyleParam(UIComponent component, String style, String defaultStyleClass, String styleClass) {
        this(component, style, defaultStyleClass, styleClass, StyleGroup.regularStyleGroup());
    }

    public StyleParam(UIComponent component, String style, String defaultStyleClass, String styleClass, StyleGroup styleGroup) {
        super(FunctionCallScript.escapeStringForJSAndQuote(
                Styles.getCSSClass(
                        FacesContext.getCurrentInstance(), component,
                        style, styleGroup, styleClass, defaultStyleClass)
        ));
    }
}
