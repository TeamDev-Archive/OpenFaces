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
package org.openfaces.component.table;

import org.openfaces.component.select.SelectBooleanCheckbox;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.util.Arrays;

public class SelectRowCheckbox extends SelectBooleanCheckbox {
    public static final String COMPONENT_TYPE = "org.openfaces.SelectRowCheckbox";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectRowCheckbox";

    public SelectRowCheckbox() {
        setTriStateAllowed(true);
        setStateList(Arrays.asList(SelectBooleanCheckbox.SELECTED_STATE, SelectBooleanCheckbox.UNSELECTED_STATE));
        setRendererType("org.openfaces.SelectRowCheckboxRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
            super.saveState(context),

        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);

    }
}
