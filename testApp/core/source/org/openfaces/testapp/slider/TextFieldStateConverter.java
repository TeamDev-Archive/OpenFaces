/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.slider;

import org.openfaces.component.input.TextFieldState;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author : roman.nikolaienko
 */
public class TextFieldStateConverter implements Converter {
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.equals("off"))
            return TextFieldState.OFF;
        if (value.equals("readOnly"))
            return TextFieldState.READ_ONLY;
        if (value.equals("writeEnabled"))
            return TextFieldState.WRITE_ENABLED;
        return TextFieldState.valueOf(value.toUpperCase());
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
    }
}
