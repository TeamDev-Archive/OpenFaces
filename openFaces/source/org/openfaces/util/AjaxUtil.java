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

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Eugene Goncharov
 */
public class AjaxUtil {
    public static final String AJAX_REQUEST_MARKER = "_openFaces_ajax";
    public static final String PARAM_RENDER = "_of_render";
    private static final String COMPONENT_DELIMITER = ",";
    public static final String UPDATE_PORTIONS_SUFFIX = "_of_ajax_portions";
    public static final String CUSTOM_JSON_PARAM = "_of_customJsonParam";

    public static final String KEY_RENDERING_PORTLETS_AJAX_RESPONSE = AjaxUtil.class.getName() + ".renderingPortletsAjaxResponse";
    private static final String STATE_CHARSET = "ISO-8859-1";
    private static final int LENGTH_UNICODE = 4;
    private static final int LENGTH_BACKSLASH_AND_UNICODE = 5;

    private static long iInitLibraryNameCounter = 0;
    private static final Random random = new Random();
    public static final String ATTR_PORTLET_UNIQUE_RTLIBRARY_NAME = "org.openfaces.portletUniqueRTLibraryName";
    public static final String AJAX_SUPPORT_RENDERED = "org.openfaces.ajaxSupportOnPageRendered";

    /**
     * Runtime javascript library unique name generator. Runtime library is required for third-party JSF components
     * support in OpenFaces Ajaxed components in case when third-party components uses own javascripts for initialization
     * and these scripts contains global variables declarations. In such situation simple javascript eval() function does
     * not work correct under IE (menas that global variables declared in script that is executes by eval() under IE would
     * not be declared).
     *
     * @return string with unique name for runtime library
     */
    public static String generateUniqueInitLibraryName() {
        long partOne = System.currentTimeMillis();
        int partTwo = random.nextInt(1000);
        long partThree = iInitLibraryNameCounter++;
        if (iInitLibraryNameCounter == Long.MAX_VALUE) iInitLibraryNameCounter = 0;

        StringBuilder buffer = new StringBuilder().append(partOne).append(partTwo).append(partThree).append(".js");
        return buffer.toString();
    }

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

    /**
     * Checks whether the current request is an Ajax request
     *
     * @param request incoming request.
     * @return <code>true</code> - if the current request is an Ajax request. <code>false</code> otherwise.
     */
    public static boolean isAjaxRequest(RequestFacade request) {
        // for portlets: String browser = request.getProperty(...);
        return request.getParameter(AJAX_REQUEST_MARKER) != null;
    }

    public static boolean isAjaxRequest(FacesContext context) {
        if (isPortletRenderRequest(context)) {
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            boolean ajaxKeyExists = sessionMap.containsKey(AJAX_REQUEST_MARKER);
            if (ajaxKeyExists)
                return true;
            // isPortletRenderRequest also returns true for action requests under Liferay, so it's safe to perform the default check to address this
        }

        ExternalContext externalContext = context.getExternalContext();
        if (isPortletRequest(FacesContext.getCurrentInstance())) {
            RequestFacade request = RequestFacade.getInstance(externalContext.getRequest());
            // for portlets: String browser = request.getProperty(...);
            return request.getParameter(AJAX_REQUEST_MARKER) != null;
        } else {
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            return request.getParameter(AJAX_REQUEST_MARKER) != null;
        }
    }

    private static boolean considerPortlets = true;

    public static boolean isPortletRequest(FacesContext context) {
        if (!considerPortlets)
            return false;
        ExternalContext externalContext = context.getExternalContext();
        Object requestObj = externalContext.getRequest();
        try {
            return requestObj instanceof RenderRequest || requestObj instanceof ActionRequest;
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

    public static String getComponentStateFieldName(String clientId) {
        return clientId + "::_state";
    }

    public static String objectToString(Object object) throws IOException {
        byte[] bytes;
        {
            ByteArrayOutputStream compressedBytesStream = new ByteArrayOutputStream();
            OutputStream compressingStream = new GZIPOutputStream(compressedBytesStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(compressingStream);
            try {
                objectOutputStream.writeObject(object);
            } finally {
                objectOutputStream.close();
                compressingStream.close();
                compressedBytesStream.close();
            }
            bytes = compressedBytesStream.toByteArray();
        }
        Base64 base64 = new Base64();
        byte[] encodedBytes = base64.encode(bytes);
        return new String(encodedBytes, STATE_CHARSET);
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
                Resources.renderJSLinkIfNeeded(context, Resources.getUtilJsURL(context));
                Resources.renderJSLinkIfNeeded(context, Resources.getAjaxUtilJsURL(context));
                if (isPortletRequest(context)) {
                    String uniqueRTLibraryName = ResourceFilter.RUNTIME_INIT_LIBRARY_PATH + generateUniqueInitLibraryName();
                    context.getExternalContext().getSessionMap().put(ATTR_PORTLET_UNIQUE_RTLIBRARY_NAME, uniqueRTLibraryName);
                    String initLibraryUrl = Resources.getApplicationURL(context, uniqueRTLibraryName);
                    Resources.renderJSLinkIfNeeded(context, initLibraryUrl);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Object retrieveAjaxStateObject(FacesContext context, UIComponent component) {
        Object stateObj;
        try {
            String componentId = getComponentId(component);
            stateObj = readState(context, componentId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stateObj;
    }

    private static String getComponentId(UIComponent component) {
        UIComponent namingContainer = component.getParent();
        for (; namingContainer != null;
             namingContainer = namingContainer.getParent())
            if (namingContainer instanceof NamingContainer)
                break;
        StringBuilder buf = new StringBuilder();
        if (namingContainer != null)
            buf.append(getComponentId(namingContainer)).append(NamingContainer.SEPARATOR_CHAR);
        buf.append(component.getId());
        return buf.toString();
    }

    private static Object readState(FacesContext context, String clientId) throws IOException {
        Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String stateFieldName = getComponentStateFieldName(clientId);
        String stateStr = requestParameterMap.get(stateFieldName);
        if (stateStr == null)
            return null;
        return stringToObject(stateStr);
    }

    private static Object stringToObject(String str) throws IOException {
        Base64 base64 = new Base64();
        byte[] encodedBytes = str.getBytes(STATE_CHARSET);
        byte[] decodedBytes = base64.decode(encodedBytes);
        ByteArrayInputStream compressedBytesStream = new ByteArrayInputStream(decodedBytes);
        InputStream decompressingStream = new GZIPInputStream(compressedBytesStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(decompressingStream);
        try {
            return objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            objectInputStream.close();
            decompressingStream.close();
            compressedBytesStream.close();
        }
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
        while (descendants.hasNext()) {
            UIComponent descendant = descendants.next();
            makeComponentsNonTransient(context, descendant);
        }
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

    public static List<String> getRequestedAjaxPortionNames(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        RequestFacade request = RequestFacade.getInstance(externalContext.getRequest());
        return getAjaxPortionNames(context, request);
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
        RequestFacade request = RequestFacade.getInstance(context.getExternalContext().getRequest());
        List<String> portionNames = getAjaxPortionNames(context, request);
        if (portionNames == null || portionNames.size() == 0)
            return false;
        Map sessionMap = context.getExternalContext().getSessionMap();
        String componentId = (String) sessionMap.get(PARAM_RENDER);
        if (componentId == null) // not portlets
            componentId = request.getParameter(PARAM_RENDER);
        boolean requestForSpecifiedComponent = component.getClientId(context).equals(componentId);
        return requestForSpecifiedComponent;

    }

    public static JSONObject getCustomJSONParam(FacesContext context, RequestFacade request) {
        String jsonParamStr = isPortletRequest(context)
                ? (String) context.getExternalContext().getSessionMap().get(CUSTOM_JSON_PARAM)
                : request.getParameter(CUSTOM_JSON_PARAM);
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

    public static List<String> getAjaxPortionNames(FacesContext context, RequestFacade request) {
        String updateIds = isPortletRequest(context)
                ? (String) context.getExternalContext().getSessionMap().get(UPDATE_PORTIONS_SUFFIX)
                : request.getParameter(UPDATE_PORTIONS_SUFFIX);
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

    private static String unescapeSymbol(String str) {
        StringBuilder buf = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < str.length();) {
            char temp = chars[i];
            if (temp == '\\' && i < str.length() - LENGTH_UNICODE) {
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
            buf.append(temp);
            i++;
        }
        return buf.toString();
    }
}
