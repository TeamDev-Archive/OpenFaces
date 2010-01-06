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
package org.openfaces.component.select;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * @author Andrew Palval
 */
public class TabSetItems extends UIComponentBase implements Serializable {
    public static final String COMPONENT_TYPE = "org.openfaces.TabSetItems";
    private static final String COMPONENT_FAMILY = "org.openfaces.TabSetItems";

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    private Object value;

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
                saveAttachedState(context, value)
                // value
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        value = restoreAttachedState(context, values[1]);
        // values[1]
    }
}
