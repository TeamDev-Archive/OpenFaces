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
package org.openfaces.renderkit.ajax;

import org.openfaces.component.ajax.DefaultProgressMessage;
import org.openfaces.component.ajax.ProgressMessageMode;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleParam;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Eugene Goncharov
 */
public class DefaultProgressMessageRenderer extends AbstractSettingsRenderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        DefaultProgressMessage dpm = (DefaultProgressMessage) component;

        AjaxUtil.renderAjaxSupport(context);

        JSONObject backgroundLayerParams = null;
        if (dpm.getFillBackground()) {
            backgroundLayerParams = new JSONObject();
            Rendering.addJsonParam(backgroundLayerParams, "className", new StyleParam(dpm, "background", "o_ajax_blockingLayer"));
            //noinspection RedundantCast
            Rendering.addJsonParam(backgroundLayerParams, "transparency", /*don't remove (double) cast -- the other function will be invoked*/(double) dpm.getBackgroundTransparency());
            //noinspection RedundantCast
            Rendering.addJsonParam(backgroundLayerParams, "transparencyTransitionPeriod", /*don't remove (int) cast -- the other function will be invoked*/(int) dpm.getBackgroundTransparencyTransitionPeriod());
        }
        Styles.renderStyleClasses(context, dpm, true, false);

        String ajaxMessageHTML = dpm.getAjaxMessageHTML();
        ScriptBuilder setMessageScript = new ScriptBuilder().functionCall("O$.Ajax.setMessageHTML",
                ajaxMessageHTML,
                dpm.getHorizontalAlignment(),
                dpm.getVerticalAlignment(),
                dpm.getTransparency(),
                dpm.getTransparencyTransitionPeriod(),
                backgroundLayerParams,
                dpm.getMode() == ProgressMessageMode.ALL).semicolon();

        if (isAjaxCleanupRequired()) {
            setMessageScript.functionCall("O$.setAjaxCleanupRequired", true).semicolon();
        }

        Rendering.renderInitScript(context, setMessageScript);

    }

    private static boolean isAjaxCleanupRequired() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String paramStr = externalContext.getInitParameter("org.openfaces.ajaxCleanupRequired");
        boolean ajaxCleanupRequired = paramStr != null ? Boolean.valueOf(paramStr) : false;
        return ajaxCleanupRequired;
    }

}
