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
package org.openfaces.component.select;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * @author Andrew Palval
 */
public class TabSetItem extends UIComponentBase implements Serializable {
    public static final String COMPONENT_TYPE = "org.openfaces.TabSetItem";
    public static final String COMPONENT_FAMILY = "org.openfaces.TabSetItem";

    private Object itemValue = null;

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setItemValue(Object itemValue) {
        this.itemValue = itemValue;
    }

    public Object getItemValue() {
        if (itemValue != null) return itemValue;
        ValueExpression ve = getValueExpression("itemValue");
        return ve != null ? ve.getValue(getFacesContext().getELContext()) : null;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context), itemValue};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        itemValue = stateArray[1];
    }

}
