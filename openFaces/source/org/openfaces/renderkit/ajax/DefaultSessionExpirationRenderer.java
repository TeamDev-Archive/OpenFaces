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
package org.openfaces.renderkit.ajax;

import org.openfaces.component.ajax.DefaultSessionExpiration;
import org.openfaces.component.window.Confirmation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Eugene Goncharov
 */
public class DefaultSessionExpirationRenderer extends AbstractSettingsRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (isAjaxSessionExpirationProcessing(context)) {
            DefaultSessionExpiration defaultSessionExpiration = (DefaultSessionExpiration) component;

            Confirmation confirmation = defaultSessionExpiration.getConfirmation();
            if (confirmation == null) {
                confirmation = new Confirmation();
                defaultSessionExpiration.setConfirmation(confirmation);
                confirmation.setId("openfaces_internal_sessionexpiration_confirmation");
                confirmation.setStandalone(true);
                confirmation.setCaption("Session Expired");
                confirmation.setDraggable(false);
                confirmation.setMessage("Your session has expired");
                confirmation.setDetails("A new session will be created when the page is reloaded");
                confirmation.setOkButtonText("Reload page now");
                confirmation.setCancelButtonText("Reload later");
                confirmation.setModal(true);
                confirmation.setWidth("400px");
                confirmation.setHeight("160px");
                confirmation.setModalLayerStyle("background: black; filter: alpha(opacity=50); opacity: .50;");
            }
            confirmation.encodeAll(context);
            String clientId = confirmation.getClientId(context);
            String confirmationId = (clientId != null)
                    ? clientId
                    : confirmation.getId();

            String location = getRedirectLocationOnSessionExpired(context);
            String onExpiredEventFunction = "O$._pageReloadConfirmation('" + confirmationId + "', '" + location + "');";
            processEvent(context, component.getParent(), "onsessionexpired", onExpiredEventFunction);
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
