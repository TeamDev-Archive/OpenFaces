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

package org.openfaces.component.table.export;

import org.openfaces.component.table.BaseColumn;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlInputHidden;
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
        return
                component instanceof ValueHolder &&
                        !(component instanceof BaseColumn) && /* BaseColumn implements ValueHolder to support placing
                        converter tags inside of it, but column's rendering is not as simple as rendering its value
                        generally and the usual container export procedure should be used for columns instead, so we're
                        not considering columns as applicable for export by ValueHolderDataExtractor */
                        !(component instanceof HtmlInputHidden);
    }
}
