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

import org.openfaces.component.window.Confirmation;
import org.openfaces.util.Components;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

/**
 * @author Eugene Goncharov
 */
@ResourceDependencies({
        @ResourceDependency(name = "default.css", library = "openfaces")
})
public class DefaultSessionExpiration extends SessionExpiration {
    public static final String COMPONENT_TYPE = "org.openfaces.DefaultSessionExpiration";
    public static final String COMPONENT_FAMILY = "org.openfaces.DefaultSessionExpiration";

    public Confirmation getConfirmation() {
        return (Confirmation) Components.getFacet(this, "confirmation");
    }

    public void setConfirmation(Confirmation confirmation) {
        getFacets().put("confirmation", confirmation);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public DefaultSessionExpiration() {
        setRendererType("org.openfaces.DefaultSessionExpirationRenderer");
    }

}
