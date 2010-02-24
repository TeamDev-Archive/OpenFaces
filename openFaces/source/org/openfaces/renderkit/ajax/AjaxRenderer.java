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

import org.openfaces.component.ajax.Ajax;
import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Ilya Musihin
 */
public class AjaxRenderer extends AbstractSettingsRenderer {

    private AjaxRendererHelper helper = new AjaxRendererHelper();

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        Ajax ajax = (Ajax) component;
        if (ajax.isStandalone())
            encodeStandaloneInvocationMode(context, ajax);
        else
            helper.encodeAutomaticInvocationMode(context, ajax);
    }

    private void encodeStandaloneInvocationMode(FacesContext context, Ajax ajax) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", ajax);
        writer.writeAttribute("style", "display: none", null);
        String clientId = ajax.getClientId(context);
        writer.writeAttribute("id", clientId, null);

        AjaxInitializer ajaxInitializer = new AjaxInitializer();
        ScriptBuilder initScript = new ScriptBuilder();
        initScript.initScript(context, ajax, "O$._initAjax",
                ajaxInitializer.getRenderArray(context, ajax, ajax.getRender()),
                ajaxInitializer.getAjaxParams(context, ajax));

        helper.appendMissingParameters(context, ajax, initScript);

        Rendering.renderInitScript(context, initScript,
                Resources.getUtilJsURL(context),
                Resources.getAjaxUtilJsURL(context));

        writer.endElement("span");
    }


}