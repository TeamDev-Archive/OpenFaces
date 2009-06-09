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
package org.openfaces.renderkit.ajax;

import org.openfaces.component.ajax.ReloadComponents;
import org.openfaces.component.ajax.ReloadComponentsInitializer;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Ilya Musihin
 */
public class ReloadComponentsRenderer extends AbstractSettingsRenderer {

    private ReloadComponentsRendererHelper helper = new ReloadComponentsRendererHelper();

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ReloadComponents reloadComponents = (ReloadComponents) component;
        if (reloadComponents.isStandalone())
            encodeStandaloneInvocationMode(context, reloadComponents);
        else
            helper.encodeAutomaticInvocationMode(context, reloadComponents);
    }

    private void encodeStandaloneInvocationMode(FacesContext context, ReloadComponents reloadComponents) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", reloadComponents);
        writer.writeAttribute("style", "display: none", null);
        String clientId = reloadComponents.getClientId(context);
        writer.writeAttribute("id", clientId, null);

        ReloadComponentsInitializer reloadComponentsInitializer = new ReloadComponentsInitializer();
        ScriptBuilder initScript = new ScriptBuilder();
        initScript.initScript(context, reloadComponents, "O$._initReloadComponents",
                reloadComponentsInitializer.getComponentIdsArray(context, reloadComponents, reloadComponents.getComponentIds()),
                reloadComponentsInitializer.getReloadParams(context, reloadComponents));

        helper.appendMissingParameters(context, reloadComponents, initScript);

        RenderingUtil.renderInitScript(context, initScript, new String[]{
                ResourceUtil.getUtilJsURL(context),
                ResourceUtil.getAjaxUtilJsURL(context)
        });

        writer.endElement("span");
    }


}