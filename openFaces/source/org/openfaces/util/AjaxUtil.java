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
package org.openfaces.util;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eugene Goncharov
 */
public class AjaxUtil {
    private static final String COMPONENT_DELIMITER = ",";
    public static final String UPDATE_PORTIONS_SUFFIX = "_of_ajax_portions";
    public static final String CUSTOM_JSON_PARAM = "_of_customJsonParam";

    public static final String KEY_RENDERING_PORTLETS_AJAX_RESPONSE = AjaxUtil.class.getName() + ".renderingPortletsAjaxResponse";
    private static final int LENGTH_UNICODE = 4;
    private static final int LENGTH_BACKSLASH_AND_UNICODE = 5;

    private static final String AJAX_SUPPORT_RENDERED = "org.openfaces.ajaxSupportOnPageRendered";

    public static boolean isAjax4jsfRequest() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Class richFacesContextClazz = null;
        Boolean result;
        try {
            Object currentInstance = null;

            try {
                richFacesContextClazz = Class.forName("org.ajax4jsf.context.AjaxContext");
            } catch (ClassNotFoundException e) {
                // absence of RichFaces is a valid case
            }

            if (richFacesContextClazz != null) {
                Method currentInstanceMethod = richFacesContextClazz.getMethod("getCurrentInstance", FacesContext.class);
                if (currentInstanceMethod != null) {
                    currentInstance = currentInstanceMethod.invoke(null, FacesContext.getCurrentInstance());
                }

                if (currentInstance != null) {
                    boolean isFacesContextInUse = true;
                    Method isAjaxRequestMethod = null;

                    try {
                        isAjaxRequestMethod = richFacesContextClazz.getMethod("isAjaxRequest", FacesContext.class);
                    } catch (NoSuchMethodException e) {
                        // We will try to search for this method without FacesContext in params
                    } catch (SecurityException e) {
                        //
                    }

                    if (isAjaxRequestMethod == null) {
                        isAjaxRequestMethod = richFacesContextClazz.getMethod("isAjaxRequest");
                        isFacesContextInUse = false;
                    }

                    if (isAjaxRequestMethod != null) {
                        result = (isFacesContextInUse)
                                ? (Boolean) isAjaxRequestMethod.invoke(currentInstance, FacesContext.getCurrentInstance())
                                : (Boolean) isAjaxRequestMethod.invoke(currentInstance);
                        if (result != null)
                            return result;
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            externalContext.log("Ajax4jsf support is disabled, because getCurrentInstance method was not " +
                    "found in org.ajax4jsf.context.AjaxContext.");
            return false;
        } catch (IllegalAccessException e) {
            externalContext.log("Ajax4jsf support is disabled because exception was thrown during " +
                    "execution of getCurrentInstance method in org.ajax4jsf.context.AjaxContext");
            return false;
        } catch (InvocationTargetException e) {
            externalContext.log("Ajax4jsf support is disabled because exception was thrown during " +
                    "execution of getCurrentInstance method in org.ajax4jsf.context.AjaxContext");
            return false;
        }
        return false;
    }

    public static boolean isAjaxRequest(FacesContext context) {
        return context.getPartialViewContext().isAjaxRequest();
    }

    private static boolean considerPortlets = true;

    public static boolean isPortletRequest(FacesContext context) {
        if (!considerPortlets)
            return false;
        ExternalContext externalContext = context.getExternalContext();
        Object requestObj = externalContext.getRequest();
        try {
            return requestObj instanceof PortletRequest;
        } catch (NoClassDefFoundError e) {
            considerPortlets = false;
            return false;
        }
    }

    public static boolean isPortletRenderRequest(FacesContext context) {
        if (!considerPortlets)
            return false;
        ExternalContext externalContext = context.getExternalContext();
        Object requestObj = externalContext.getRequest();
        try {
            return requestObj instanceof RenderRequest;
        } catch (NoClassDefFoundError e) {
            considerPortlets = false;
            return false;
        }
    }

    public static void prepareComponentForAjax(FacesContext context, UIComponent component) {
        if (!component.isRendered())
            return;
        if (isAjaxRequest(context))
            return;
        renderJSLinks(context);
        makeComponentsNonTransient(context, component);
    }

    public static void renderJSLinks(FacesContext context) {
        if (isAjaxRequest(context))
            return;
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        if (requestMap.put(AJAX_SUPPORT_RENDERED, Boolean.TRUE) == null) {
            try {
                renderAjaxSupport(context);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void renderAjaxSupport(FacesContext context) throws IOException {
        Resources.renderJSLinkIfNeeded(context, Resources.UTIL_JS_PATH);
        Resources.renderJSLinkIfNeeded(context, Resources.AJAX_UTIL_JS_PATH);
        UtilPhaseListener.encodeFormSubmissionAjaxInactivityTimeout(context);
    }

    /**
     * This method should be called for a component that is using AJAX before it is rendered. This method makes all
     * sub-components non-transient to avoid losing component state on AJAX requests.
     */
    private static void makeComponentsNonTransient(FacesContext context, UIComponent component) {
        if (component.isTransient()) {
            component.setTransient(false);
            if (component.isTransient()) {
                if (!component.getClass().getName().equalsIgnoreCase("com.sun.facelets.compiler.UIInstructions")) {
                    Rendering.logWarning(context, "Couldn't reset 'transient' flag of component to false for Ajax request to maintain component state correctly. " +
                            "Component's clientId = " + component.getClientId(context) + "; component class: " + component.getClass());
                }
            }
        }
        Iterator<UIComponent> descendants = component.getFacetsAndChildren();
        //Commented to avoid wrong partial response working for new versions of mojara
        //Request: http://stackoverflow.com/questions/14397257/openfaces-datatable-issue-throwing-exception-com-sun-faces-facelets-compile
        /*while (descendants.hasNext()) {
            UIComponent descendant = descendants.next();
            makeComponentsNonTransient(context, descendant);
        } */
    }

    public static boolean getSkipExtraRenderingOnPortletsAjax(FacesContext context) {
        if (!isAjaxRequest(context) || !isPortletRequest(context))
            return false;
        boolean skipRendering = !isRenderingPortletsAjaxResponse(context);
        return skipRendering;
    }

    public static boolean isRenderingPortletsAjaxResponse(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        boolean result = requestMap.containsKey(KEY_RENDERING_PORTLETS_AJAX_RESPONSE);
        return result;
    }


    public static boolean isAjaxPortionRequest(FacesContext context) {
        if (!isAjaxRequest(context))
            return false;
        List<String> portionNames = getAjaxPortionNames(context);
        return portionNames != null && portionNames.size() > 0;
    }

    /**
     * This method should be used by components to ensure the correct lifecycle of using Ajax under Portlets. The specifics
     * of Ajax under Portlets is that as opposed to other configurations, component's encoding functions are invoked even
     * if the ajax request is sent to render the component's portions only. The components which can potentially have a
     * problem with such double rendering should invoke this method and skip the rendering functions if the current is
     * an Ajax portion request.
     *
     * @return true if the current request is an ajax request for retrieving portions from the specified component. This method
     *         performs its primary job only during the render phase under portlets. In other conditions it always returns false.
     */
    public static boolean isAjaxPortionRequest(FacesContext context, UIComponent component) {
        if (!isAjaxRequest(context))
            return false;
        List<String> portionNames = getAjaxPortionNames(context);
        if (portionNames == null || portionNames.size() == 0)
            return false;
        String componentId = context.getExternalContext().getRequestParameterMap().get(
                PartialViewContext.PARTIAL_RENDER_PARAM_NAME);
        boolean requestForSpecifiedComponent = component.getClientId(context).equals(componentId);
        return requestForSpecifiedComponent;

    }

    public static JSONObject getCustomJSONParam(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String jsonParamStr = externalContext.getRequestParameterMap().get(CUSTOM_JSON_PARAM);
        if (jsonParamStr == null)
            return null;
        JSONObject param;
        try {
            param = new JSONObject(jsonParamStr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return param;
    }

    public static List<String> getAjaxPortionNames(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String updateIds = externalContext.getRequestParameterMap().get(UPDATE_PORTIONS_SUFFIX);
        List<String> result = new ArrayList<String>();
        if (updateIds == null || updateIds.equals("")) {
            return Collections.emptyList();
        }
        int idx = updateIds.indexOf(COMPONENT_DELIMITER);
        if (idx == -1) {
            result.add(unescapeSymbol(updateIds.trim()));
        } else {
            while (idx != -1) {
                String part = updateIds.substring(0, idx);
                result.add(unescapeSymbol(part).trim());
                updateIds = updateIds.substring(idx + 1);
                idx = updateIds.indexOf(COMPONENT_DELIMITER);
            }
            result.add(updateIds.trim());
        }
        return result;
    }

    public static String unescapeSymbol(String str) {
        StringBuilder buf = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < str.length(); ) {
            char c = chars[i];
            if (c == '\\' && i < str.length() - LENGTH_UNICODE) {
                if (chars[i + 1] == '\\') {
                    buf.append('\\');
                    i = i + 2;
                    continue;
                } else {
                    buf.append((char) Integer.parseInt(str.substring(i + 1, i + LENGTH_BACKSLASH_AND_UNICODE)));
                    i = i + LENGTH_BACKSLASH_AND_UNICODE;
                    continue;
                }
            }
            buf.append(c);
            i++;
        }
        return buf.toString();
    }
}
