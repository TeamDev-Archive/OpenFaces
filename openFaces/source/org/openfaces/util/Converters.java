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
package org.openfaces.util;

import org.openfaces.validator.EMailValidator;
import org.openfaces.validator.URLValidator;

import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;

/**
 * @author Vladimir Korenev
 */
public class Converters {
    public static Converter getConverter(UIComponent component, FacesContext context) {
        try {
            if (component instanceof EditableValueHolder) {
                Converter converter = ((EditableValueHolder) component).getConverter();
                if (converter != null) return converter;
                else {
                    EditableValueHolder editableValueHolder = ((EditableValueHolder) component);
                    Validator[] validators = editableValueHolder.getValidators();
                    for (Validator validator : validators) {
                        if (validator instanceof EMailValidator
                                || validator instanceof URLValidator) {
                            converter = new StringConverter();
                            editableValueHolder.setConverter(converter);
                            break;
                        }
                    }
                    if (converter != null) return converter;
                }
            }

            ValueExpression ve = component.getValueExpression("value");
            if (ve == null) return null;

            Class valueType = ve.getType(context.getELContext());
            if (valueType == null) return null;

            if (String.class.equals(valueType)) return null;
            if (Object.class.equals(valueType)) return null;

            return context.getApplication().createConverter(valueType);
        } catch (RuntimeException e) {
            Log.log("error in getConverter. component:" + component, e);
        }
        return null;
    }
}
