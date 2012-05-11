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
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import java.util.Arrays;

/**
 * @author Dmitry Pikhulya
 */
public class SelectAllCheckbox extends SelectBooleanCheckbox {
    public static final String COMPONENT_TYPE = "org.openfaces.SelectAllCheckbox";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectAllCheckbox";

    public SelectAllCheckbox() {
        setTriStateAllowed(true);
        setStateList(Arrays.asList(SelectBooleanCheckbox.SELECTED_STATE, SelectBooleanCheckbox.UNSELECTED_STATE));
        setRendererType("org.openfaces.SelectAllCheckboxRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        super.restoreState(context, state[0]);
    }

}
