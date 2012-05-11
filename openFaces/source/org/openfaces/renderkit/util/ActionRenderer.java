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
package org.openfaces.renderkit.util;

import org.openfaces.component.util.Action;
import org.openfaces.component.util.ActionHelper;
import org.openfaces.renderkit.OUICommandRenderer;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ActionRenderer extends OUICommandRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        Action action = (Action) component;
        if (action.isStandalone())
            encodeStandaloneInvocationMode(context, action);
    }

    private void encodeStandaloneInvocationMode(FacesContext context, Action action) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", action);
        writer.writeAttribute("style", "display: none", null);
        String clientId = action.getClientId(context);
        writer.writeAttribute("id", clientId, null);

        Rendering.renderInitScript(context,
                new ScriptBuilder().initScript(context, action, "O$._initAction",
                        action.getId(),
                        ActionHelper.extractActionStr(action, "action"),
                        ActionHelper.extractActionStr(action, "listener")),
                Resources.utilJsURL(context));

        writer.endElement("span");
    }
}
