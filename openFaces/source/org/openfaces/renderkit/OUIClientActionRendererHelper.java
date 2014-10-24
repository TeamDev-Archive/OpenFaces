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
package org.openfaces.renderkit;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public abstract class OUIClientActionRendererHelper {
    public void encodeAutomaticInvocationMode(FacesContext context, OUIClientAction clientAction) throws IOException {
        String invokerId = OUIClientActionHelper.getClientActionInvoker(context, clientAction, true);
        ScriptBuilder javaScript = new ScriptBuilder();
        if (invokerId != null) {
            javaScript.functionCall("O$.byIdOrName", invokerId).dot();
            javaScript.append(Rendering.getEventWithOnPrefix(context, clientAction, null)).append("=");
            javaScript.anonymousFunction(new RawScript(getClientActionScript(context, clientAction)), "event").semicolon();
        }
        encodeAdditionalScript(context, javaScript, clientAction);
        AjaxUtil.renderAjaxSupport(context);
        Rendering.renderInitScript(context, new ScriptBuilder().onLoadScript(javaScript).semicolon());
    }

    protected void encodeAdditionalScript(FacesContext context, ScriptBuilder javaScript, OUIClientAction clientAction) {

    }

    protected abstract String getClientActionScript(FacesContext context, OUIClientAction clientAction);
}
