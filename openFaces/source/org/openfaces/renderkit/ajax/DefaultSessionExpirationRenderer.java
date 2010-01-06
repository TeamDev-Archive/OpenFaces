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
import org.openfaces.util.RenderingUtil;

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
        if (isAjaxSessionExpirationProcessing(context)) {
            RenderingUtil.renderChildren(context, component);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (isAjaxSessionExpirationProcessing(context)) {
            DefaultSessionExpiration defaultSessionExpiration = (DefaultSessionExpiration) component;

            String clientId = defaultSessionExpiration.getConfirmation().getClientId(context);
            String confirmationId = (clientId != null)
                    ? clientId
                    : defaultSessionExpiration.getConfirmation().getId();

            String location = getRedirectLocationOnSessionExpired(context);
            String onExpiredEventFunction = "O$('" + confirmationId + "').runConfirmedFunction(function(){O$.reloadPage('" + location + "')});";
            processEvent(context, component.getParent(), "onsessionexpired", onExpiredEventFunction);
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
