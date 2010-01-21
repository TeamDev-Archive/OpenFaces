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

import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.VerticalAlignment;
import org.openfaces.component.ajax.DefaultProgressMessage;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceFilter;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;

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
        DefaultProgressMessage defaultProgressMessage = (DefaultProgressMessage) component;

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        if (!requestMap.containsKey(PROGRESS_MESSAGE) && !requestMap.containsKey(AjaxUtil.AJAX_SUPPORT_RENDERED)) {
            requestMap.put(PROGRESS_MESSAGE, defaultProgressMessage);
            ResourceUtil.renderJSLinkIfNeeded(ResourceUtil.getUtilJsURL(context), context);
            ResourceUtil.renderJSLinkIfNeeded(ResourceUtil.getAjaxUtilJsURL(context), context);
            return;
        }

        boolean isAjax4jsfRequest = AjaxUtil.isAjax4jsfRequest();
        boolean isProtletRequest = AjaxUtil.isPortletRequest(context);

        if (requestMap.containsKey(AjaxUtil.AJAX_SUPPORT_RENDERED) || requestMap.containsKey(RENDERING)) {
            String ajaxMessageHTML = defaultProgressMessage.getAjaxMessageHTML();

            ScriptBuilder setMessageScript = new ScriptBuilder().functionCall("O$.setAjaxMessageHTML",
                    ajaxMessageHTML,
                    HorizontalAlignment.RIGHT,
                    VerticalAlignment.TOP,
                    0,
                    0,
                    null).semicolon();

            if (isAjaxCleanupRequired()) {
                setMessageScript.functionCall("O$.setAjaxCleanupRequired", true).semicolon();
            }

            if (isAjax4jsfRequest || isProtletRequest) {
                Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
                String uniqueRTLibraryName = isProtletRequest
                        ? (String) sessionMap.get(AjaxUtil.ATTR_PORTLET_UNIQUE_RTLIBRARY_NAME)
                        : ResourceFilter.RUNTIME_INIT_LIBRARY_PATH + AjaxUtil.generateUniqueInitLibraryName();

                String initLibraryUrl = ResourceUtil.getApplicationResourceURL(context, uniqueRTLibraryName);

                if (isAjax4jsfRequest) {
                    ResourceUtil.renderJSLinkIfNeeded(initLibraryUrl, context);
                }

                if (sessionMap != null && uniqueRTLibraryName != null) {
                    sessionMap.put(uniqueRTLibraryName, setMessageScript.toString());
                }
            } else {
                RenderingUtil.appendOnLoadScript(context, setMessageScript.toString());
                if (isAjaxCleanupRequired()) {
                    RenderingUtil.appendOnLoadScript(context, "O$.setAjaxCleanupRequired(true);");
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
