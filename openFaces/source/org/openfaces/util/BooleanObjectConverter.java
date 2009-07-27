/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
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
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * @author Roman Porotnikov
 */
public class BooleanObjectConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1 || "null".equalsIgnoreCase(value)) {
            return null;
        } else if ("true".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }

        return String.valueOf(value);
    }

}
