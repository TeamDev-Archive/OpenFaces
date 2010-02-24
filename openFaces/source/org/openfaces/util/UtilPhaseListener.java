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
package org.openfaces.util;

import org.openfaces.component.ajax.DefaultProgressMessage;
import org.openfaces.renderkit.ajax.DefaultProgressMessageRenderer;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.Map;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class UtilPhaseListener extends PhaseListenerBase {
    private static final String FOCUSED_COMPONENT_ID_KEY = UtilPhaseListener.class.getName() + ".focusedComponentId";
    private static final String FOCUS_TRACKER_FIELD_ID = "o::defaultFocus";
    private static final String AUTO_FOCUS_TRACKING_CONTEXT_PARAM = "org.openfaces.autoSaveFocus";
    private static final String DISABLED_CONTEXT_MENU_CONTEXT_PARAM = "org.openfaces.disabledContextMenu";

    private static final String SCROLL_POS_KEY = UtilPhaseListener.class.getName() + ".pageScrollPos";
    private static final String SCROLL_POS_TRACKER_FIELD_ID = "o::defaultScrollPosition";
    private static final String AUTO_SCROLL_POS_TRACKING_CONTEXT_PARAM = "org.openfaces.autoSaveScrollPos";


    public void beforePhase(PhaseEvent event) {
    }

    public void afterPhase(PhaseEvent event) {
        if (checkPortletMultipleNotifications(event, false))
            return;

        FacesContext context = event.getFacesContext();
        PhaseId phaseId = event.getPhaseId();
        if (phaseId.equals(PhaseId.RENDER_RESPONSE)) {
            List<String> renderedJsLinks = Resources.getRenderedJsLinks(context);
            String utilJs = Resources.getUtilJsURL(context);
            if (!renderedJsLinks.contains(utilJs))
                Resources.registerJavascriptLibrary(context, utilJs);
            Rendering.appendOnLoadScript(context, encodeFocusTracking(context));
            Rendering.appendOnLoadScript(context, encodeScrollPosTracking(context));
            Rendering.appendOnLoadScript(context, encodeDisabledContextMenu(context));
            encodeAjaxProgressMessage(context);
        } else if (phaseId.equals(PhaseId.APPLY_REQUEST_VALUES)) {
            decodeFocusTracking(context);
            decodeScrollPosTracking(context);
        }
    }

    private void encodeAjaxProgressMessage(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        if (requestMap.containsKey(DefaultProgressMessageRenderer.PROGRESS_MESSAGE)) {
            requestMap.put(DefaultProgressMessageRenderer.RENDERING, Boolean.TRUE);
            DefaultProgressMessage defaultProgressMessage = (DefaultProgressMessage) requestMap.get(DefaultProgressMessageRenderer.PROGRESS_MESSAGE);
            renderProgressMessage(context, defaultProgressMessage);
        } else if (requestMap.containsKey(AjaxUtil.AJAX_SUPPORT_RENDERED)) {
            DefaultProgressMessage defaultProgressMessage = new DefaultProgressMessage();
            renderProgressMessage(context, defaultProgressMessage);
        }
    }

    private void renderProgressMessage(FacesContext context, DefaultProgressMessage defaultProgressMessage) {
        try {
            defaultProgressMessage.encodeAll(context);
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }

    private boolean isAutoFocusTrackingEnabled(FacesContext context) {
        return getBooleanContextParam(context, AUTO_FOCUS_TRACKING_CONTEXT_PARAM);
    }

    private boolean isDisabledContextMenuEnabled(FacesContext context) {
        return getBooleanContextParam(context, DISABLED_CONTEXT_MENU_CONTEXT_PARAM);
    }

    private boolean isAutoScrollPosTrackingEnabled(FacesContext context) {
        return getBooleanContextParam(context, AUTO_SCROLL_POS_TRACKING_CONTEXT_PARAM);
    }

    private boolean getBooleanContextParam(FacesContext context, String webXmlContextParam) {
        String applicationMapKey = "_openFaces_contextParam:" + webXmlContextParam;
        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();

        Boolean result = (Boolean) applicationMap.get(applicationMapKey);
        if (result == null) {
            ExternalContext externalContext = context.getExternalContext();
            String paramStr = externalContext.getInitParameter(webXmlContextParam);
            if (paramStr == null)
                result = Boolean.FALSE;
            else {
                paramStr = paramStr.trim();
                if (paramStr.equalsIgnoreCase("true"))
                    result = Boolean.TRUE;
                else if (paramStr.equalsIgnoreCase("false"))
                    result = Boolean.FALSE;
                else {
                    externalContext.log("Unrecognized value specified for context parameter named " + webXmlContextParam + ": it must be either true or false");
                    result = Boolean.FALSE;
                }
            }
            applicationMap.put(applicationMapKey, result);
        }
        return result;
    }

    private Script encodeFocusTracking(FacesContext facesContext) {
        if (!isAutoFocusTrackingEnabled(facesContext))
            return null;
        ExternalContext externalContext = facesContext.getExternalContext();
        Map requestMap = externalContext.getRequestMap();
        String focusedComponentId = (String) requestMap.get(FOCUSED_COMPONENT_ID_KEY);
        return new ScriptBuilder().functionCall("O$.initDefaultFocus",
                FOCUS_TRACKER_FIELD_ID,
                focusedComponentId != null ? focusedComponentId: null).semicolon();
    }

    private Script encodeScrollPosTracking(FacesContext facesContext) {
        if (!isAutoScrollPosTrackingEnabled(facesContext))
            return null;
        ExternalContext externalContext = facesContext.getExternalContext();
        Map requestMap = externalContext.getRequestMap();
        String scrollPos = (String) requestMap.get(SCROLL_POS_KEY);
        return new ScriptBuilder().functionCall("O$.initDefaultScrollPosition",
                SCROLL_POS_TRACKER_FIELD_ID,
                scrollPos != null ? scrollPos : null).semicolon();
    }

    private Script encodeDisabledContextMenu(FacesContext facesContext) {
        if (!isDisabledContextMenuEnabled(facesContext))
            return null;
        return new RawScript("O$.disabledContextMenuFor(document);");
    }

    private void decodeFocusTracking(FacesContext facesContext) {
        if (!isAutoFocusTrackingEnabled(facesContext))
            return;
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, String> requestParameterMap = externalContext.getRequestParameterMap();
        String focusedComponentId = requestParameterMap.get(FOCUS_TRACKER_FIELD_ID);
        Map<String, Object> requestMap = externalContext.getRequestMap();
        requestMap.put(FOCUSED_COMPONENT_ID_KEY, focusedComponentId);
    }

    private void decodeScrollPosTracking(FacesContext facesContext) {
        if (!isAutoScrollPosTrackingEnabled(facesContext))
            return;
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, String> requestParameterMap = externalContext.getRequestParameterMap();
        String focusedComponentId = requestParameterMap.get(SCROLL_POS_TRACKER_FIELD_ID);
        Map<String, Object> requestMap = externalContext.getRequestMap();
        requestMap.put(SCROLL_POS_KEY, focusedComponentId);
    }


    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }


}
