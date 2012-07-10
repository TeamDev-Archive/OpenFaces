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
package org.openfaces.component.util;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUICommand;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class Action extends OUICommand implements OUIClientAction {
    public static final String COMPONENT_TYPE = "org.openfaces.Action";
    public static final String COMPONENT_FAMILY = "org.openfaces.Action";

    private String event;
    private String _for;
    private Boolean standalone;

    private Boolean disabled;

    private ActionHelper helper = new ActionHelper();

    public Action() {
        setRendererType("org.openfaces.ActionRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                event,
                _for,
                standalone,
                disabled
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        event = (String) state[i++];
        _for = (String) state[i++];
        standalone = (Boolean) state[i++];
        disabled = (Boolean) state[i++];
    }

    public String getEvent() {
        return ValueBindings.get(this, "event", this.event, "click");
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFor() {
        return ValueBindings.get(this, "for", _for);
    }

    public void setFor(String _for) {
        this._for = _for;
    }


    public boolean isStandalone() {
        return ValueBindings.get(this, "standalone", standalone, false);
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (parent != null)
            helper.onParentChange(this, parent);
    }


}
