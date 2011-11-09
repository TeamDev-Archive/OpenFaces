/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleParam;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

/**
 * @author Eugene Goncharov
 */
public class DefaultProgressMessageRenderer extends AbstractSettingsRenderer {
    /**
     * This request-scope attribute specifies whether the DefaultProgressMessage component is explicitly specified on
     * the page and if so, it is set for UtilPhaseListener to render the specified component after  the render-response
     * phase.
     */
    public static final String PROGRESS_MESSAGE = "org.openfaces.defaultProgressMessage";
    public static final String RENDERING = "org.openfaces.defaultProgressMessageRendering";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        DefaultProgressMessage dpm = (DefaultProgressMessage) component;

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        if (!requestMap.containsKey(PROGRESS_MESSAGE) && !requestMap.containsKey(AjaxUtil.AJAX_SUPPORT_RENDERED)) {
            requestMap.put(PROGRESS_MESSAGE, dpm);
            AjaxUtil.renderAjaxSupport(context);
            return;
        }

        boolean isAjax4jsfRequest = AjaxUtil.isAjax4jsfRequest();
        boolean isPortletRequest = AjaxUtil.isPortletRequest(context);

        if (requestMap.containsKey(AjaxUtil.AJAX_SUPPORT_RENDERED) || requestMap.containsKey(RENDERING)) {
            String ajaxMessageHTML = dpm.getAjaxMessageHTML();

            JSONObject backgroundLayerParams = null;
            if (dpm.getFillBackground()) {
                backgroundLayerParams = new JSONObject();
                Rendering.addJsonParam(backgroundLayerParams, "className", new StyleParam(dpm, "background", "o_ajax_blockingLayer"));
                Rendering.addJsonParam(backgroundLayerParams, "transparency", /*don't remove (double) cast -- the other function will be invoked*/(double) dpm.getBackgroundTransparency());
                Rendering.addJsonParam(backgroundLayerParams, "transparencyTransitionPeriod", /*don't remove (int) cast -- the other function will be invoked*/(int) dpm.getBackgroundTransparencyTransitionPeriod());
            }
            Styles.renderStyleClasses(context, dpm, true, true);

            ScriptBuilder setMessageScript = new ScriptBuilder().functionCall("O$.setAjaxMessageHTML",
                    ajaxMessageHTML,
                    dpm.getHorizontalAlignment(),
                    dpm.getVerticalAlignment(),
                    dpm.getTransparency(),
                    dpm.getTransparencyTransitionPeriod(),
                    backgroundLayerParams).semicolon();

            if (isAjaxCleanupRequired()) {
                setMessageScript.functionCall("O$.setAjaxCleanupRequired", true).semicolon();
            }

            if (isAjax4jsfRequest || isPortletRequest) {
                Rendering.appendUniqueRTLibraryScripts(context, setMessageScript);
            } else {
                Rendering.appendOnLoadScript(context, setMessageScript);
                if (isAjaxCleanupRequired()) {
                    Rendering.appendOnLoadScript(context, new RawScript("O$.setAjaxCleanupRequired(true);"));
                }
            }
        }

    }

    private static boolean isAjaxCleanupRequired() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String paramStr = externalContext.getInitParameter("org.openfaces.ajaxCleanupRequired");
        boolean ajaxCleanupRequired = paramStr != null ? Boolean.valueOf(paramStr) : false;
        return ajaxCleanupRequired;
    }


}
