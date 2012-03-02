/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.component.table.export;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * @author Natalia.Zolochevska@Teamdev.com
 */
public class ValueHolderDataExtractor implements ComponentDataExtractor {

    public Object getData(UIComponent component) {
        ValueHolder valueHolder = (ValueHolder) component;
        FacesContext context = FacesContext.getCurrentInstance();
        Object value = valueHolder.getValue();
        Converter valueConverter = valueHolder.getConverter();
        if (valueConverter != null) {
            return valueConverter.getAsString(context, component, value);
        }
        return value;
    }


    public boolean isApplicableFor(UIComponent component) {
        return component instanceof ValueHolder;
    }
}
