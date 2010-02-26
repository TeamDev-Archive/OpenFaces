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
package org.openfaces.ajax;

import org.openfaces.component.validation.ValidationSupportResponseWriter;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Components;
import org.openfaces.util.Environment;
import org.openfaces.util.Log;
import org.openfaces.util.Resources;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * This class is provides our own mechanisms of handling of the activities
 * in the <em>Render Response</em> and <em>Restore View</em>
 * phases of the request processing life-cycle.
 *
 * @author Eugene Goncharov
 */
public class AjaxViewHandler extends ViewHandlerWrapper {
    public static final String AJAX_EXPIRED_HEADER = "Ajax-Expired";

    public static final String SESSION_EXPIRATION_PROCESSING = "Ajax-Expiration-Processing";
    public static final String AJAX_ERROR_PROCESSING = "Ajax-Error-Processing";
    public static final String AJAX_VIEW_EXPIRED = "Ajax-View-Expired";
    public static final String LOCATION_HEADER = "Location";
    public static final String ERROR_MESSAGE_HEADER = "Error-Message";
    public static final String ERROR_CAUSE_MESSAGE_HEADER = "Error-Detailed-Message";
    public static final String ERROR_OCCURRED = "Error-Occured";
    public static final String ERROR_OCCURRED_UNDER_PORTLETS = "Error-Occured-Under-Portlets";
    public static final String ERROR_OBJECT_UNDER_PORTLETS = "Error-Object-Under-Portlets";
    public static final String SESSION_EXPIRED_RESPONSE = "Session-Expired-Response";
    public static final String SESSION_SYNCHRONIZATION = "OF_REQUEST_SESSION_SYNC";

    private static final String SESSION_SCOPED_PARAMETER = "OF__SESSION";
    private static final String SKIP_FURTHER_VIEW_HANDLERS = "OF_SKIP_FURTHER_VIEW_HANDLERS";
    private static final String SESSION_EXPIRATION_TESTING_PARAM = "of__session_expiration_testing";

    public AjaxViewHandler(ViewHandler parent) {
        super(parent);
    }


    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {

        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

        if (!sessionMap.containsKey(SESSION_SYNCHRONIZATION)) {
            // RequestSyncObject is required for synchronization of parallel ajax requests.
            // If sessionMap doesn't contain RequestSyncObject, we need to put it in.
            sessionMap.put(SESSION_SYNCHRONIZATION, new RequestsSyncObject());
        }

        // Begin of synchronized block
        synchronized (sessionMap.get(SESSION_SYNCHRONIZATION)) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            // RequestMap will contain SKIP_FURTHER_VIEW_HANDLERS key, only if one of our viewHandler
            // instances in "chain" already done it's processing.
            RequestsSyncObject syncObject = (RequestsSyncObject) sessionMap.get(SESSION_SYNCHRONIZATION);
            if (requestMap == null
                    || !requestMap.containsKey(SKIP_FURTHER_VIEW_HANDLERS)) {

                while (syncObject.getAjaxRequestProcessing()) {
                    // While we have ajax request in progress,
                    // all new requests that comes from client should wait for finishing of previous one.
                    try {
                        syncObject.wait();
                    } catch (InterruptedException e) {
                        // It's OK.
                    }
                }
            }

            UIViewRoot viewRoot = null;
            try {
                viewRoot = super.restoreView(context, viewId);
            } catch (RuntimeException e) {
                // If exception was caught during our ajax request then we need to process it
                // and send ajax response with details about exception.
                if (AjaxUtil.isAjaxRequest(context)) {
                    CommonAjaxViewRoot.processExceptionDuringAjax(context, e);
                    Log.log(context, e.getMessage(), e);
                } else {
                    // If current request is not our ajax request and exception was caught then we need to rethrow exception.
                    throw new RuntimeException(e);
                }
            } catch (Error e) {
                // If exception was caught during our ajax request then we need to process it
                // and send ajax response with details about exception.
                if (AjaxUtil.isAjaxRequest(context)) {
                    CommonAjaxViewRoot.processExceptionDuringAjax(context, e);
                    Log.log(context, e.getMessage(), e);
                } else {
                    // If current request is not our ajax request and exception was caught then we need to rethrow exception.
                    throw new Error(e);
                }
            }

            if (AjaxUtil.isAjaxRequest(context)) {
                if (!requestMap.containsKey(SKIP_FURTHER_VIEW_HANDLERS)) {
                    // Ajax request processing starts from here, so we need to set boolean flag
                    // on RequestSyncObject for futher synchronization of parallel ajax requests
                    syncObject.setAjaxRequestProcessing(true);
                    // AjaxViewHandler's logic need to be invoked only once,
                    // so we indicate it by putting SKIP_FURTHER_VIEW_HANDLERS attribute
                    requestMap.put(SKIP_FURTHER_VIEW_HANDLERS, Boolean.TRUE);
                }
            }

            ExternalContext externalContext = context.getExternalContext();
            Object httpSession = externalContext.getSession(false);
            boolean isSessionExpirationTesting = context.getExternalContext().getRequestParameterMap().containsKey(SESSION_EXPIRATION_TESTING_PARAM);
            // This statement checks for session expiration. It works in both client and server state saving.
            if (isSessionExpirationTesting || null == httpSession || isNewSession(httpSession) || viewRoot == null) {
                boolean isLiferay = Environment.isLiferay(requestMap);
                if (!isLiferay) {
                    if (AjaxUtil.isAjaxRequest(context)
                            && !requestMap.containsKey(SESSION_EXPIRATION_PROCESSING)) {
                        // If session expired, we need to set request scoped parameter,
                        // this will indicate session expiration processing.
                        requestMap.put(SESSION_EXPIRATION_PROCESSING, Boolean.TRUE);

                        if (AjaxUtil.isPortletRequest(context)) {
                            // In portlet environment we need to set session scoped parameter that indicate session expiration processing
                            // because in portlet for one user request, we have ActionRequest and RenderRequest on a server.
                            // Both these requests have their own request maps.
                            sessionMap.put(SESSION_EXPIRATION_PROCESSING, Boolean.TRUE);
                        }

                        if (!AjaxUtil.isPortletRequest(context)) {
                            // After session expired, we need to send to the client redirect location url
                            // So, here we encode redirect url for current view and put it into requestMap for futher processing.
                            String actionURL = getActionURL(context, viewId);
                            actionURL = externalContext.encodeActionURL(actionURL);
                            requestMap.put(LOCATION_HEADER, actionURL);
                        }
                        return null;
                    }

                } else {
                    // If we work in LifeRay environment, we need only to set session-scoped parameter, that
                    // current session was expired and we will process it accordingly.
                    if (AjaxUtil.isAjaxRequest(context)) {
                        sessionMap.put(SESSION_EXPIRATION_PROCESSING, Boolean.TRUE);
                    }
                }
            }
            return viewRoot;
        }
    }

    /*
    * This method checks is session new or not.
    * In servlet environment we use servlet-api for this checking.
    * In portlet environment we checks by our custom session scoped parameter.
    * If session is new - it shouldn't contain our custom parameter.
    *
    * @param httpSession - current session
    */
    @SuppressWarnings({"ChainOfInstanceofChecks"})
    public static boolean isNewSession(Object httpSession) {
        if (httpSession instanceof HttpSession) {
            return ((HttpSession) httpSession).isNew();
        } else if (httpSession instanceof PortletSession) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            if (Environment.isLiferay(externalContext.getRequestMap())) {
                Map<String, Object> sessionMap = externalContext.getSessionMap();
                return sessionMap != null
                        && !sessionMap.containsKey(SESSION_SCOPED_PARAMETER);

            }

            Map<String, Object> sessionMap = externalContext.getSessionMap();
            return sessionMap != null
                    && !sessionMap.containsKey(SESSION_SCOPED_PARAMETER);
        }
        return false;
    }

    @Override
    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot root = null;

        try {
            root = super.createView(context, viewId);
        } catch (RuntimeException e) {
            if (AjaxUtil.isAjaxRequest(context)) {
                // If exception was caught during our ajax request then we need to process it
                // and send ajax response with details about exception.
                CommonAjaxViewRoot.processExceptionDuringAjax(context, e);
                Log.log(context, e.getMessage(), e);
            } else {
                // We need to rethrow exception.
                throw new RuntimeException(e);
            }
        } catch (Error e) {
            if (AjaxUtil.isAjaxRequest(context)) {
                // If exception was caught during our ajax request then we need to process it
                // and send ajax response with details about exception.
                CommonAjaxViewRoot.processExceptionDuringAjax(context, e);
                Log.log(context, e.getMessage(), e);
            } else {
                // We need to rethrow exception.
                throw new Error(e);
            }
        }

        // If created instance of UIViewRoot does not implement our interface WrappedAjaxRoot
        // then we need to wrap it with our AjaxViewRoot
        UIViewRoot riRoot;
        if (null == root || root instanceof WrappedAjaxRoot) {
            riRoot = root;
        } else {
            riRoot = getAjaxViewRoot(root, context);
        }

        ExternalContext externalContext = context.getExternalContext();
        Map<String, String> requestParameterMap = externalContext.getRequestParameterMap();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        if (requestParameterMap.containsKey("of_sessionExpiration")
                && requestParameterMap.containsKey(AjaxUtil.AJAX_REQUEST_MARKER)) {
            if (!requestMap.containsKey(SESSION_EXPIRATION_PROCESSING)) {
                requestMap.put(SESSION_EXPIRATION_PROCESSING, Boolean.TRUE);

                String actionURL = getActionURL(context, viewId);
                actionURL = externalContext.encodeActionURL(actionURL);
                requestMap.put(LOCATION_HEADER, actionURL);
            }
        }

        return riRoot;
    }

    /*
    * This method provides custom logic of wrapping viewRoot instances.
    * This is done, because different environments casts current viewRoot
    * instance to their own implementation of UIViewRoot class.
    * So, we need to comply with inheritance hierarchy.
    */
    private UIViewRoot getAjaxViewRoot(UIViewRoot root, FacesContext context) {
        UIViewRoot riRoot;
        Class ajax4jsfViewRootClass = null;
        try {
            ajax4jsfViewRootClass = Class.forName("org.ajax4jsf.framework.ajax.AjaxViewRoot");
        } catch (ClassNotFoundException e) {
            // absence of the Ajax4jsf library is a valid case
        }
        boolean isAjax4jsf = (ajax4jsfViewRootClass != null);
        if (isAjax4jsf)
            throw new IllegalArgumentException("OpenFaces warning: The old Ajax4jsf framework is not supported. Use RichFaces that now incorporates this framework instead.");

        Class richFacesAjaxViewRootClass = null;
        try {
            richFacesAjaxViewRootClass = Class.forName("org.ajax4jsf.component.AjaxViewRoot");
        } catch (ClassNotFoundException e) {
            // absence of the RichFaces library is a valid case
        }

        if (Environment.isExoPortal())
            riRoot = createExoAjaxViewRoot(root);
        else
            riRoot = richFacesAjaxViewRootClass == null ? new AjaxViewRoot() : getA4jAjaxViewRoot(root);
        // fill properties from default.
        riRoot.setViewId(root.getViewId());
        riRoot.setLocale(root.getLocale());
        String renderKitId = root.getRenderKitId();
        // Fix facelets bug - for debug requests renderKitId is null !
        if (null == renderKitId) {
            renderKitId = calculateRenderKitId(context);
        }
        riRoot.setRenderKitId(renderKitId);
        return riRoot;
    }

    private UIViewRoot createExoAjaxViewRoot(UIViewRoot root) {
        UIViewRoot result;
        try {
            Class<?> viewRootClass = Class.forName("org.openfaces.ajax.exoportal.ExoAjaxViewRoot");
            Constructor<?> constructor = viewRootClass.getConstructor(UIViewRoot.class);
            result = (UIViewRoot) constructor.newInstance(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private UIViewRoot getA4jAjaxViewRoot(UIViewRoot root) {
        UIViewRoot result;
        try {
            Class<?> viewRootClass = Class.forName("org.openfaces.ajax.richfaces.A4JAjaxViewRoot");
            Constructor<?> constructor = viewRootClass.getConstructor(UIViewRoot.class);
            // TODO [sanders] (Apr 1, 2009, 7:03 AM): avoid cast
            result = (UIViewRoot) constructor.newInstance(root);
            // TODO [sanders] (Apr 1, 2009, 7:03 AM): Log exception?
        } catch (ClassNotFoundException e) {
            result = new AjaxViewRoot();
        } catch (IllegalAccessException e) {
            result = new AjaxViewRoot();
        } catch (InstantiationException e) {
            result = new AjaxViewRoot();
        } catch (NoSuchMethodException e) {
            result = new AjaxViewRoot();
        } catch (InvocationTargetException e) {
            result = new AjaxViewRoot();
        }

        return result;
    }

    @Override
    public void renderView(FacesContext context, UIViewRoot root)
            throws IOException, FacesException {
        Components.runScheduledActions();
        if (!context.getResponseComplete()) {

            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(false);
            Map<String, Object> requestMap = externalContext.getRequestMap();

            if (externalContext.getSessionMap().containsKey(ERROR_OCCURRED_UNDER_PORTLETS)) {
                processExceptionUnderPortlets(context);
                return;
            }

            if (AjaxUtil.isPortletRequest(context) && Environment.isMyFaces()
                    && isNewSession(externalContext.getSession(false))) {
                processSessionExpirationUnderPortletsMyFaces(context, root, externalContext, session);
            }

            if (AjaxUtil.isPortletRequest(context) && Environment.isRI()
                    && isNewSession(externalContext.getSession(false))) {
                processSessionExpirationUnderPortletsRI(context, root, externalContext);
            }

            boolean ajaxRequest = AjaxUtil.isAjaxRequest(context);
            if (!ajaxRequest && !AjaxUtil.isAjax4jsfRequest())
                ValidationSupportResponseWriter.resetBubbleIndex(context);

            if (ajaxRequest) {
                updateSessionExpirationFlagUnderPortlets(context, root, externalContext, requestMap);
                try {
                    // HACK for MyFaces ( <f:view> tag not call renderers )
                    //ServletResponse response = (ServletResponse) context
                    //		.getExternalContext().getResponse();
                    Object response = externalContext.getResponse();

                    if (!AjaxUtil.isPortletRequest(context)) {
                        try {
                            response.getClass().getDeclaredMethod("resetResponse").invoke(response);
                            //response.reset();
                        } catch (Exception e) {
                            // Do nothing - we will use directly and reset
                            // wrapper
                        }
                    }

                    if (requestMap.containsKey(SESSION_EXPIRATION_PROCESSING)) {
                        super.renderView(context, root);
                        //This is done for MyFaces use-case, because MyFaces doesn't call rendering methods for ViewRoot
                        if (!Environment.isRI()) {
                            root.encodeChildren(context);
                        }
                        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
                        if (sessionMap != null && !sessionMap.containsKey(SESSION_SCOPED_PARAMETER)) {
                            sessionMap.put(SESSION_SCOPED_PARAMETER, Boolean.TRUE.toString());
                        }
                        return;
                    }

                    if (Environment.isFacelets(context)) {
                        super.renderView(context, root);
                    } else {
                        root.encodeBegin(context);
                        if (root.getRendersChildren()) {
                            root.encodeChildren(context);
                        }
                        root.encodeEnd(context);
                    }
                } catch (RuntimeException e) {
                    CommonAjaxViewRoot.processExceptionDuringAjax(context, e);
                    externalContext.log(e.getMessage(), e);
                }
                catch (Error e) {
                    CommonAjaxViewRoot.processExceptionDuringAjax(context, e);
                    externalContext.log(e.getMessage(), e);
                }

            } else {

                super.renderView(context, root);
                Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
                if (sessionMap != null && !sessionMap.containsKey(SESSION_SCOPED_PARAMETER)) {
                    sessionMap.put(SESSION_SCOPED_PARAMETER, Boolean.TRUE.toString());
                }

                if (AjaxUtil.isAjax4jsfRequest()) {
                    Resources.processHeadResources(context);
                }

            }

        }
    }

    private void updateSessionExpirationFlagUnderPortlets(FacesContext context, UIViewRoot root, ExternalContext externalContext, Map<String, Object> requestMap) {
        if (AjaxUtil.isPortletRequest(context)
                && externalContext.getSessionMap().containsKey(SESSION_EXPIRATION_PROCESSING)
                && !(requestMap.containsKey(LOCATION_HEADER))) {
            requestMap.put(SESSION_EXPIRATION_PROCESSING, Boolean.TRUE.toString());
            addSessionExpirationFlagUnderPortlets(context, root);
            externalContext.getSessionMap().remove(SESSION_EXPIRATION_PROCESSING);
        }
    }

    private void processSessionExpirationUnderPortletsRI(FacesContext context, UIViewRoot root, ExternalContext externalContext) {
        //If previous ActionRequest was our AJAX request and session expired then this flag should be set to TRUE
        Boolean isNewSession = (Boolean) externalContext.getSessionMap().get(SESSION_EXPIRATION_PROCESSING);
        if (isNewSession != null && isNewSession) {
//          We need to put AjaxRequest marker to sessionMap
            addAjaxRequestMarkerUnderPortlets(context, root);
        }
    }

    private void processSessionExpirationUnderPortletsMyFaces(FacesContext context, UIViewRoot root, ExternalContext externalContext, Object session) {
        Object requestObject = context.getExternalContext().getRequest();

        HttpServletRequest catalinaServletRequest = null;
        try {
            Class jbossRenderRequestClass = Class.forName("org.jboss.portlet.JBossRenderRequest");
            Class abstractRequestContextClass = Class.forName("org.jboss.portal.portlet.impl.spi.AbstractRequestContext");

            Class requestObjectClass = requestObject.getClass();
            if (jbossRenderRequestClass.isAssignableFrom(requestObjectClass)) {
                Field requestContextField = requestObject.getClass().getSuperclass().getSuperclass().getDeclaredField("requestContext");
                requestContextField.setAccessible(true);
                Object requestContext = requestContextField.get(requestObject);
                Class requestContextClass = requestContext.getClass();
                if (abstractRequestContextClass.isAssignableFrom(requestContextClass)) {
                    Field reqField = requestContextClass.getDeclaredField("req");
                    reqField.setAccessible(true);
                    Object req = reqField.get(requestContext);
                    if (req instanceof HttpServletRequest) {
                        catalinaServletRequest = (HttpServletRequest) req;
                    }
                }
            }
        }
        catch (ClassNotFoundException e) {
            externalContext.log(e.getMessage());
        } catch (NoSuchFieldException e) {
            externalContext.log(e.getMessage());
        } catch (IllegalAccessException e) {
            externalContext.log(e.getMessage());
        }

        if (AjaxUtil.isPortletRenderRequest(context)
                && !AjaxUtil.isAjaxRequest(context)
                && catalinaServletRequest != null && catalinaServletRequest.getParameterMap().size() > 0) {
            if (isNewSession(session)) {
                addAjaxRequestMarkerUnderPortlets(context, root);
                addSessionExpirationFlagUnderPortlets(context, root);
            }
        }

        if (Environment.isLiferay(context.getExternalContext().getRequestMap())) {
            if (context.getExternalContext().getSessionMap().containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING)) {
                addAjaxRequestMarkerUnderPortlets(context, root);
                addSessionExpirationFlagUnderPortlets(context, root);
                context.getExternalContext().getSessionMap().remove(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING);
            }
        }
    }

    private void processExceptionUnderPortlets(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        final Map<String, Object> sessionMan = externalContext.getSessionMap();
        Throwable exception = (Throwable) sessionMan.get(ERROR_OBJECT_UNDER_PORTLETS);
        CommonAjaxViewRoot.processExceptionDuringAjax(context, exception);
        sessionMan.remove(ERROR_OBJECT_UNDER_PORTLETS);
        sessionMan.remove(ERROR_OCCURRED_UNDER_PORTLETS);
    }

    private void addAjaxRequestMarkerUnderPortlets(FacesContext context, UIViewRoot root) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

        boolean shouldWaitForPreviousAjaxCompletion = true;
        long timeBefore = System.currentTimeMillis();
        do {
            // TODO [sanders] (Apr 1, 2009, 7:06 AM): Synchronization on parameter
            synchronized (root) {
                long timeElapsed = System.currentTimeMillis() - timeBefore;
                if (timeElapsed > CommonAjaxViewRoot.MAX_PORTLET_PARALLEL_REQUEST_TIMEOUT) {
                    Log.log(context, "AjaxViewHandler.addAjaxRequestMarkerUnderPortlets: waiting for parallel ajax request timed out");
                    sessionMap.remove(AjaxUtil.AJAX_REQUEST_MARKER);
                }

                if (sessionMap.get(AjaxUtil.AJAX_REQUEST_MARKER) == null) {
                    sessionMap.put(AjaxUtil.AJAX_REQUEST_MARKER, Boolean.TRUE.toString());
                    shouldWaitForPreviousAjaxCompletion = false;
                } else
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // prevoius ajax request completion should be waited for anyway...
                    }
            }
        } while (shouldWaitForPreviousAjaxCompletion);
    }

    private void addSessionExpirationFlagUnderPortlets(FacesContext context, UIViewRoot root) {
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        if (!requestMap.containsKey(SESSION_EXPIRATION_PROCESSING)) {
            requestMap.put(SESSION_EXPIRATION_PROCESSING, Boolean.TRUE);
        }
        if (!requestMap.containsKey(LOCATION_HEADER)) {
            String actionURL = getActionURL(context, root.getViewId());
            actionURL = externalContext.encodeActionURL(actionURL);
            requestMap.put(LOCATION_HEADER, actionURL);
        }
    }
}
