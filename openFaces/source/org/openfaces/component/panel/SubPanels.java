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
package org.openfaces.component.panel;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * @author Andrew Palval
 */
public class SubPanels extends UIComponentBase implements Serializable {
    public static final String COMPONENT_TYPE = "org.openfaces.SubPanels";
    public static final String COMPONENT_FAMILY = "org.openfaces.SubPanels";

    private Object value;

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getValue() {
        return ValueBindings.get(this, "value", value, Object.class);
    }

    public void setValue(Object value) {
        this.value = value;
    }


    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                value};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        value = values[i++];
    }
}
