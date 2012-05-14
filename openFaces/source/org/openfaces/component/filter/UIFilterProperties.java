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

package org.openfaces.component.filter;

import org.openfaces.component.OUIComponentBase;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Natalia Zolochevska
 */
public class UIFilterProperties extends OUIComponentBase implements Serializable{

    public static final String COMPONENT_FAMILY = "org.openfaces.FilterProperty";
    public static final String COMPONENT_TYPE = "org.openfaces.FilterProperties";

    private List<FilterProperty> value;

    public UIFilterProperties() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, value};
    }

    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        value = (List<FilterProperty>) state[i++];
    }

    public List<FilterProperty> getValue() {
        if (value != null) return value;
        ValueExpression ve = getValueExpression("value");
        return ve != null ? (List<FilterProperty>) ve.getValue(getFacesContext().getELContext()) : null;
    }


    public void setValue(Object value) {
        this.value = (List<FilterProperty>) value;
    }

}