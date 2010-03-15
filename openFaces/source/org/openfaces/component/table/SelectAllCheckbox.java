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
package org.openfaces.component.table;

import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class SelectAllCheckbox extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.SelectAllCheckbox";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectAllCheckbox";
    private Boolean disabled;

    public SelectAllCheckbox() {
        setRendererType("org.openfaces.SelectAllCheckboxRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean isDisabled() {
        return ValueBindings.get(this, "disabled", disabled, false);
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                disabled};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        super.restoreState(context, state[0]);
        disabled = (Boolean) state[1];
    }

}
