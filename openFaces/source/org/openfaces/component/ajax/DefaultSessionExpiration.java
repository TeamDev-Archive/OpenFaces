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
package org.openfaces.component.ajax;

import org.openfaces.component.CompoundComponent;
import org.openfaces.component.window.Confirmation;
import org.openfaces.util.ComponentUtil;

import javax.faces.context.FacesContext;

/**
 * @author Eugene Goncharov
 */
public class DefaultSessionExpiration extends SessionExpiration implements CompoundComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.DefaultSessionExpiration";
    public static final String COMPONENT_FAMILY = "org.openfaces.DefaultSessionExpiration";
    public static final String DEFAULT_CONFIRAMTION_ID = "openfaces_internal_sessionexpiration_confirmation";

    private Confirmation confirmation;

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public DefaultSessionExpiration() {
        setRendererType("org.openfaces.DefaultSessionExpirationRenderer");
    }

    public void createSubComponents(FacesContext context) {
        Confirmation confirmation = (Confirmation) ComponentUtil.createChildComponent(context,
                this, Confirmation.COMPONENT_TYPE, "confirm_expiration");
        confirmation.setId(DEFAULT_CONFIRAMTION_ID);
        confirmation.setCaptionText("Session Expired");
        confirmation.setDraggable(false);
        confirmation.setMessage("Your session has expired");
        confirmation.setDetails("A new session will be created when the page is reloaded");
        confirmation.setOkButtonText("Reload page now");
        confirmation.setCancelButtonText("Reload later");
        confirmation.setModal(true);
        confirmation.setWidth("400px");
        confirmation.setHeight("160px");
        confirmation.setModalLayerStyle("background: black; filter: alpha(opacity=50); opacity: .50;");

        this.confirmation = confirmation;
    }
}
