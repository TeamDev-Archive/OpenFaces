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

import org.openfaces.component.ajax.DefaultProgressMessage;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.ResourceFilter;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

/**
 * @author Eugene Goncharov
 */
public class DefaultProgressMessageRenderer extends AbstractSettingsRenderer {

    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        DefaultProgressMessage defaultProgressMessage = (DefaultProgressMessage) component;

        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        if (!requestMap.containsKey("_of_defaultProgressMessageInUse")
                && !requestMap.containsKey("_of_ajaxSupportOnPageRendered")) {
            requestMap.put("_of_defaultProgressMessageInUse", Boolean.TRUE);
            requestMap.put("_of_defaultProgressMessage", defaultProgressMessage);
            ResourceUtil.renderJSLinkIfNeeded(ResourceUtil.getUtilJsURL(context), context);
            ResourceUtil.renderJSLinkIfNeeded(ResourceUtil.getAjaxUtilJsURL(context), context);
        } else {
            boolean isAjax4jsfRequest = AjaxUtil.isAjax4jsfRequest();
            boolean isProtletRequest = AjaxUtil.isPortletRequest(context);

            if (requestMap.containsKey("_of_ajaxSupportOnPageRendered")) {
                String ajaxMessageHTML = defaultProgressMessage.getAjaxMessageHTML();
                boolean ajaxCleanupRequired = isAjaxCleanupRequired();

                String uniqueRTLibraryName = isProtletRequest
                        ? (String) context.getExternalContext().getSessionMap().get("_of_portletUniqueRTLibraryName")
                        : ResourceFilter.RUNTIME_INIT_LIBRARY_PATH + AjaxUtil.generateUniqueInitLibraryName();

                String initLibraryUrl = ResourceUtil.getApplicationResourceURL(context, uniqueRTLibraryName);
                ScriptBuilder setMessageScript = new ScriptBuilder().functionCall("O$.setAjaxMessageHTML", ajaxMessageHTML).semicolon();

                if (ajaxCleanupRequired) {
                    setMessageScript.functionCall("O$.setAjaxCleanupRequired", true).semicolon();
                }

                if (isAjax4jsfRequest || isProtletRequest) {
                    if (isAjax4jsfRequest) {
                        ResourceUtil.renderJSLinkIfNeeded(initLibraryUrl, context);
                    }

                    context.getExternalContext().getSessionMap().put(uniqueRTLibraryName, setMessageScript.toString());
                } else {
                    RenderingUtil.appendOnLoadScript(context,
                            setMessageScript.toString());
                    if (ajaxCleanupRequired) {
                        RenderingUtil.appendOnLoadScript(context, "O$.setAjaxCleanupRequired(true);");
                    }
                }
            } else if (requestMap.containsKey("_of_defaultProgressMessageRendering")) {
                String ajaxMessageHTML = defaultProgressMessage.getAjaxMessageHTML();
                boolean ajaxCleanupRequired = isAjaxCleanupRequired();

                String uniqueRTLibraryName = isProtletRequest
                        ? (String) context.getExternalContext().getSessionMap().get("_of_portletUniqueRTLibraryName")
                        : ResourceFilter.RUNTIME_INIT_LIBRARY_PATH + AjaxUtil.generateUniqueInitLibraryName();

                String initLibraryUrl = ResourceUtil.getApplicationResourceURL(context, uniqueRTLibraryName);
                String setMessageScript = new ScriptBuilder().functionCall("O$.setAjaxMessageHTML", ajaxMessageHTML).semicolon().toString();
                String initScriptsStr = setMessageScript;

                if (ajaxCleanupRequired) {
                    initScriptsStr = initScriptsStr + " O$.setAjaxCleanupRequired(true);";
                }

                if (isAjax4jsfRequest || isProtletRequest) {
                    if (isAjax4jsfRequest) {
                        ResourceUtil.renderJSLinkIfNeeded(initLibraryUrl, context);
                    }

                    if (context.getExternalContext().getSessionMap() != null && uniqueRTLibraryName != null) {
                        context.getExternalContext().getSessionMap().put(uniqueRTLibraryName, initScriptsStr);
                    }
                } else {
                    RenderingUtil.appendOnLoadScript(context, setMessageScript);
                    if (ajaxCleanupRequired) {
                        RenderingUtil.appendOnLoadScript(context, "O$.setAjaxCleanupRequired(true);");
                    }
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
