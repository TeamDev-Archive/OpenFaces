/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
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
public class Scrolling extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.Scrolling";
    public static final String COMPONENT_FAMILY = "org.openfaces.Scrolling";

    private Boolean vertical;
    private Boolean horizontal;

    public Scrolling() {
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean isVertical() {
        return ValueBindings.get(this, "vertical", vertical, true);
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public boolean isHorizontal() {
        return ValueBindings.get(this, "horizontal", horizontal, true);
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[] {
                super.saveState(context),
                vertical,
                horizontal
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        vertical = (Boolean) state[i++];
        horizontal = (Boolean) state[i++];
    }
}
