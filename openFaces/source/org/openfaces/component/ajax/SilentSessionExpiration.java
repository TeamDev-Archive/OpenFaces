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
package org.openfaces.component.ajax;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Eugene Goncharov
 */
public class SilentSessionExpiration extends SessionExpiration {
    public static final String COMPONENT_TYPE = "org.openfaces.SilentSessionExpiration";
    public static final String COMPONENT_FAMILY = "org.openfaces.SilentSessionExpiration";

    private String redirectLocation;

    public SilentSessionExpiration() {
        setRendererType("org.openfaces.SilentSessionExpirationRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[] {
                super.saveState(context),
                redirectLocation
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        redirectLocation = (String) state[i++];

    }

    public String getRedirectLocation() {
        return ValueBindings.get(this, "redirectLocation", redirectLocation);
    }

    public void setRedirectLocation(String redirectLocation) {
        this.redirectLocation = redirectLocation;
    }


}
