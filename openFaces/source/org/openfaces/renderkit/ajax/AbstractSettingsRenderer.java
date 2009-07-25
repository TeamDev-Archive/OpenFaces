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

import org.openfaces.ajax.AjaxViewHandler;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.AjaxUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

/**
 * @author Eugene Goncharov
 */
public abstract class AbstractSettingsRenderer extends RendererBase {

    protected void processEvent(FacesContext context, UIComponent component, String eventName, String eventHandlerJavascript) throws IOException {
        String eventFunction = getEventFunction(eventHandlerJavascript);

        String eventHandlingJavascript = getEventScript(eventName, eventFunction, component);
        renderInitScript(context, eventHandlingJavascript);
    }

    protected void renderInitScript(FacesContext context, String javaScript) throws IOException {
        String initScript = "O$.addLoadEvent(function(){" + javaScript + "});";

        RenderingUtil.renderInitScript(context, initScript, new String[]{
                ResourceUtil.getUtilJsURL(context),
                ResourceUtil.getAjaxUtilJsURL(context)
        });
    }

    protected String getEventScript(String eventName, String eventFunction, UIComponent component) {
        String javaScript;

        UIComponent parentComponent = component.getParent();

        boolean isEventForComponent = isParentComponentValid(parentComponent) && (getComponentId(parentComponent) != null);
        javaScript = (isEventForComponent)
                ? "O$.setComponentAjaxEventHandler('" + eventName + "'," + eventFunction + ",'" + getComponentId(parentComponent) + "');"
                : "O$.setCommonAjaxEventHandler('" + eventName + "'," + eventFunction + ");";
        return javaScript;
    }

    private String getComponentId(UIComponent component) {
        String resultId = null;

        while (component != null) {
            if (resultId == null) {
                resultId = component.getId();
            } else {
                if (component.getId() != null) {
                    resultId = component.getId() + ":" + resultId;
                }
            }
            component = component.getParent();
        }
        return resultId;
    }

    protected String getEventFunction(String eventHandlerJavascript) {
        if (!eventHandlerJavascript.startsWith("function")) {
            return "function(event){" + eventHandlerJavascript + "}";
        }
        return eventHandlerJavascript;
    }

    /**
     * This method checks that parent component of AjaxSettings is valid for adding event processing to all ajax request processing from it.
     *
     * @param parentComponent
     * @return
     */
    private boolean isParentComponentValid(UIComponent parentComponent) {
        return !(parentComponent instanceof UIViewRoot) && !(parentComponent instanceof UIForm);
    }

    protected boolean isAjaxSessionExpirationProcessing(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestMap();
        return (AjaxUtil.isAjaxRequest(context) &&
                requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING));
    }

    protected boolean isAjaxErrorProcessing(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestMap();
        return AjaxUtil.isAjaxRequest(context) &&
                requestMap.containsKey(AjaxViewHandler.AJAX_ERROR_PROCESSING);
    }

    protected String getRedirectLocationOnSessionExpired(FacesContext context) {
        return (String) context.getExternalContext().getRequestMap().get(AjaxViewHandler.LOCATION_HEADER);
    }
}
