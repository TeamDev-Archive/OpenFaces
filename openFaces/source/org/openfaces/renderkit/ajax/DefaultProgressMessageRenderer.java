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

import org.openfaces.component.ajax.DefaultProgressMessage;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.RawScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.ResourceFilter;
import org.openfaces.util.Resources;
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
     * This request-scope attribute specifies whether the DefaultProgressMessge component is explicitly specified on
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
            Resources.renderJSLinkIfNeeded(context, Resources.getUtilJsURL(context));
            Resources.renderJSLinkIfNeeded(context, Resources.getAjaxUtilJsURL(context));
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
                Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
                String uniqueRTLibraryName = isPortletRequest
                        ? (String) sessionMap.get(AjaxUtil.ATTR_PORTLET_UNIQUE_RTLIBRARY_NAME)
                        : ResourceFilter.RUNTIME_INIT_LIBRARY_PATH + AjaxUtil.generateUniqueInitLibraryName();

                String initLibraryUrl = Resources.getApplicationURL(context, uniqueRTLibraryName);

                if (isAjax4jsfRequest) {
                    Resources.renderJSLinkIfNeeded(context, initLibraryUrl);
                }

                if (sessionMap != null && uniqueRTLibraryName != null) {
                    sessionMap.put(uniqueRTLibraryName, setMessageScript.toString());
                }
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
        Boolean ajaxCleanupRequired = paramStr != null ? Boolean.valueOf(paramStr) : Boolean.FALSE;
        return ajaxCleanupRequired;
    }


}
