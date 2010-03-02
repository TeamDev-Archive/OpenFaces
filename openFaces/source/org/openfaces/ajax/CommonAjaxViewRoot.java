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

import org.openfaces.ajax.plugins.AjaxPluginIncludes;
import org.openfaces.ajax.plugins.PluginsLoader;
import org.openfaces.component.OUIObjectIterator;
import org.openfaces.component.ajax.AjaxSettings;
import org.openfaces.component.ajax.DefaultSessionExpiration;
import org.openfaces.component.ajax.SilentSessionExpiration;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.component.util.AjaxLoadBundleComponent;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.util.*;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eugene Goncharov
 */
public abstract class CommonAjaxViewRoot {
    private static final String APPLICATION_SESSION_EXPIRATION_PARAM_NAME = "org.openfaces.ajax.sessionExpiration";
    private static final String SILENT_SESSION_EXPIRATION_HANDLING = "silent";
    private static final String DEFAULT_SESSION_EXPIRATION_HANDLING = "default";

    private static final String PARAM_EXECUTE = "_of_execute";
    private static final String PARAM_ACTION_COMPONENT = "_of_actionComponent";
    private static final String PARAM_ACTION_LISTENER = "_of_actionListener";
    private static final String PARAM_ACTION = "_of_action";
    private static final String PARAM_IMMEDIATE = "_of_immediate";
    // a copy of org.apache.myfaces.shared_impl.renderkit.RendererUtils.SEQUENCE_PARAM
    private static final String MYFACES_SEQUENCE_PARAM = "jsf_sequence";
    public static final long MAX_PORTLET_PARALLEL_REQUEST_TIMEOUT = 20 * 1000;
    private static final String VALUE_ATTR_STRING = "value=\"";

    private static long tempIdCounter = 0;

    private static final Pattern JS_VAR_PATTERN = Pattern.compile("\\bvar\\b");

    private UIViewRoot viewRoot;
    private List<FacesEvent> events;

    protected CommonAjaxViewRoot(UIViewRoot viewRoot) {
        this.viewRoot = viewRoot;
    }

    protected abstract void parentProcessDecodes(FacesContext context);

    protected abstract void parentProcessValidators(FacesContext context);

    protected abstract void parentProcessUpdates(FacesContext context);

    protected abstract void parentProcessApplication(FacesContext context);

    protected abstract void parentEncodeChildren(FacesContext context) throws IOException;

    protected abstract int parentGetChildCount();

    protected abstract List<UIComponent> parentGetChildren();

    protected abstract Iterator<UIComponent> parentGetFacetsAndChildren();

    public void processDecodes(FacesContext context, boolean specialSessionExpirationHandling) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        if (!AjaxUtil.isAjaxRequest(context) ||
                (specialSessionExpirationHandling && requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING))) {
            parentProcessDecodes(context);
            return;
        }

        try {
            // The try-catch block is required to handle errors and exceptions
            // during the processing of the ajax request.
            //
            // The handling of errors and exceptions is done on each phase of JSF request life-cycle
            // If the exception is caught here, the appropriate message is sent back to the client
            // in ajax response
            doProcessDecodes(context, this);
            broadcastEvents(context, PhaseId.APPLY_REQUEST_VALUES);
        } catch (RuntimeException e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        } catch (Error e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        }

    }

    public void processValidators(FacesContext context, boolean specialSessionExpirationHandling) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        if (!AjaxUtil.isAjaxRequest(context)
                || (specialSessionExpirationHandling && requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING))) {
            parentProcessValidators(context);
            return;
        }

        try {
            // The try-catch block is required to handle errors and exceptions
            // during the processing of the ajax request.
            //
            // The handling of errors and exceptions is done on each phase of JSF request life-cycle
            // If the exception is caught here, the appropriate message is sent back to the client
            // in ajax response
            doProcessValidators(context);
            broadcastEvents(context, PhaseId.PROCESS_VALIDATIONS);
        } catch (RuntimeException e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        } catch (Error e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        }

    }

    public void processUpdates(FacesContext context, boolean specialSessionExpirationHandling) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        if (!AjaxUtil.isAjaxRequest(context) ||
                (specialSessionExpirationHandling && requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING))) {
            parentProcessUpdates(context);
            return;
        }

        try {
            // The try-catch block is required to handle errors and exceptions
            // during the processing of the ajax request.
            //
            // The handling of errors and exceptions is done on each phase of JSF request life-cycle
            // If the exception is caught here, the appropriate message is sent back to the client
            // in ajax response
            doProcessUpdates(context);
            broadcastEvents(context, PhaseId.UPDATE_MODEL_VALUES);
        } catch (RuntimeException e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        } catch (Error e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        }

    }

    public void processApplication(FacesContext context, boolean specialSessionExpirationHandling) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        if (!AjaxUtil.isAjaxRequest(context)
                || (specialSessionExpirationHandling && requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING))) {
            parentProcessApplication(context);
            return;
        }

        try {
            // The try-catch block is required to handle errors and exceptions
            // during the processing of the ajax request.
            //
            // The handling of errors and exceptions is done on each phase of JSF request life-cycle
            // If the exception is caught here, the appropriate message is sent back to the client
            // in ajax response
            doProcessApplication(context);
            broadcastEvents(context, PhaseId.INVOKE_APPLICATION);
        } catch (RuntimeException e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        } catch (Error e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        }

    }

    public void encodeChildren(FacesContext context) throws IOException {
        if (!AjaxUtil.isAjaxRequest(context)) {
            parentEncodeChildren(context);
            return;
        }

        try {
            // The try-catch block is required to handle errors and exceptions
            // during the processing of the ajax request.
            //
            // The handling of errors and exceptions is done on each phase of JSF request life-cycle
            // If the exception is caught here, the appropriate message is sent back to the client
            // in ajax response
            doEncodeChildren(context);
        } catch (RuntimeException e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        } catch (Error e) {
            processExceptionDuringAjax(context, e);
            if (e.getMessage() != null) {
                Log.log(context, e.getMessage(), e);
            }
        }

    }

    private void doProcessDecodes(FacesContext context, Object objectInstanceForSynchronizeOn) {
        ExternalContext externalContext = context.getExternalContext();
        RequestFacade request = RequestFacade.getInstance(externalContext.getRequest());

        Map<String, Object> requestMap = externalContext.getRequestMap();
        if (requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING)) {
            return;
        }

        ResponseFacade response = ResponseFacade.getInstance(externalContext.getResponse());

        String componentId = request.getParameter(AjaxUtil.PARAM_RENDER);
        String[] render = extractRender(request);
        String[] execute = extractExecute(request);

        if (response instanceof ResponseFacade.ActionResponseFacade) {
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

            boolean shouldWaitForPreviousAjaxCompletion = true;
            long timeBefore = System.currentTimeMillis();
            do {

                synchronized (objectInstanceForSynchronizeOn) {
                    long timeElapsed = System.currentTimeMillis() - timeBefore;
                    if (timeElapsed > MAX_PORTLET_PARALLEL_REQUEST_TIMEOUT) {
                        Log.log(context, "CommonAjaxViewRoot.doProcessDecodes: waiting for parallel ajax request timed out");
                        sessionMap.remove(AjaxUtil.AJAX_REQUEST_MARKER);
                    }

                    if (sessionMap.get(AjaxUtil.AJAX_REQUEST_MARKER) == null) {
                        sessionMap.put(AjaxUtil.AJAX_REQUEST_MARKER, request.getParameter(AjaxUtil.AJAX_REQUEST_MARKER));
                        sessionMap.put(AjaxUtil.PARAM_RENDER, componentId);
                        sessionMap.put(AjaxUtil.UPDATE_PORTIONS_SUFFIX, request.getParameter(AjaxUtil.UPDATE_PORTIONS_SUFFIX));
                        sessionMap.put(AjaxUtil.CUSTOM_JSON_PARAM, request.getParameter(AjaxUtil.CUSTOM_JSON_PARAM));
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

        UIViewRoot viewRoot = context.getViewRoot();
        assertChildren(viewRoot);


        UIComponent[] components = locateComponents(render, viewRoot, true, false);
        ajaxApplyRequestValues(context, components, viewRoot, execute);
        if (Boolean.valueOf(request.getParameter(PARAM_IMMEDIATE))) {
            doProcessApplication(context);
        }
    }

    private void doProcessUpdates(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        RequestFacade request = RequestFacade.getInstance(externalContext.getRequest());

        Map<String, Object> requestMap = externalContext.getRequestMap();
        if (requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING)) {
            return;
        }

        String[] render = extractRender(request);
        if (render == null) {
            throw new IllegalStateException(AjaxUtil.PARAM_RENDER + " not found at request");
        }
        String[] execute = extractExecute(request);


        UIViewRoot viewRoot = context.getViewRoot();
        assertChildren(viewRoot);

        UIComponent[] components = locateComponents(render, viewRoot, false, false);
        ajaxUpdateModelValues(context, components, viewRoot, execute);
    }

    private UIComponent[] locateComponents(String[] render, UIViewRoot viewRoot,
                                           boolean preProcessDecodesOnTables,
                                           boolean preRenderResponseOnTables) {
        UIComponent[] components = new UIComponent[render.length];
        for (int i = 0; i < render.length; i++) {
            String componentId = render[i];
            components[i] = findComponentById(viewRoot, componentId, preProcessDecodesOnTables, preRenderResponseOnTables);
        }
        return components;
    }

    private void doProcessValidators(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        RequestFacade request = RequestFacade.getInstance(externalContext.getRequest());


        Map<String, Object> requestMap = externalContext.getRequestMap();
        if (requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING)) {
            return;
        }


        String[] render = extractRender(request);
        String[] execute = extractExecute(request);

        UIViewRoot viewRoot = context.getViewRoot();
        assertChildren(viewRoot);

        UIComponent[] components = locateComponents(render, viewRoot, false, false);
        ajaxProcessValidations(context, components, viewRoot, execute);
    }

    private String[] extractRender(RequestFacade request) {
        String componentId = request.getParameter(AjaxUtil.PARAM_RENDER);
        assertComponentId(componentId);
        String[] render = componentId.split(";");
        return render;
    }

    private String[] extractExecute(RequestFacade request) {
        String idsStr = request.getParameter(PARAM_EXECUTE);
        String[] execute = !Rendering.isNullOrEmpty(idsStr) ? idsStr.split(";") : null;
        return execute;
    }

    private void doProcessApplication(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        RequestFacade request = RequestFacade.getInstance(externalContext.getRequest());


        Map<String, Object> requestMap = externalContext.getRequestMap();
        if (requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING)) {
            return;
        }

        String[] render = extractRender(request);
        UIViewRoot viewRoot = context.getViewRoot();
        assertChildren(viewRoot);


        String listener = request.getParameter(PARAM_ACTION_LISTENER);
        String action = request.getParameter(PARAM_ACTION);
        String actionComponentId = request.getParameter(PARAM_ACTION_COMPONENT);
        Log.log(context, "try invoke listener");
        if (listener != null || action != null) {
            ELContext elContext = context.getELContext();
            UIComponent component = null;
            if (actionComponentId != null)
                component = findComponentById(viewRoot, actionComponentId, false, false, false);
            if (component == null)
                component = viewRoot;

            if (action != null) {
                MethodExpression methodBinding = context.getApplication().getExpressionFactory().createMethodExpression(
                        elContext, "#{" + action + "}", String.class, new Class[]{});
                methodBinding.invoke(elContext, null);
            }
            if (listener != null) {
                ActionEvent event = new ActionEvent(component);
                event.setPhaseId(Boolean.valueOf(request.getParameter(PARAM_IMMEDIATE)) ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION);
                MethodExpression methodExpression = context.getApplication().getExpressionFactory().createMethodExpression(
                        elContext, "#{" + listener + "}", void.class, new Class[]{ActionEvent.class});
                methodExpression.invoke(elContext, new Object[]{event});
            }
        }
        // invoke application should be after notification listeners
        Log.log(context, "invoke listener finished");
        UIComponent[] components = locateComponents(render, viewRoot, false, false);
        if (actionComponentId != null) {
            // todo: if component is an iterator its rowIndex should be reset so that the following id check succeed (JSFC-1974)
            // [DPikhulya Oct-15] it's possible that after moving ajax from AjaxRequestsPhaseListener there are no additional
            // actions are required for this check to succeed, because of the added findComponetByPath above.
        }
        for (int i = 0; i < components.length; i++) {
            UIComponent component = components[i];
            String thisComponentId = render[i];
            Class clazz = null;
            try {
                clazz = Class.forName("com.sun.facelets.component.UIRepeat");
            } catch (ClassNotFoundException e) {
                //do nothing - it's ok - not facelets environment
            }
            if (!component.getClientId(context).equals(thisComponentId) &&
                    !(component instanceof UIData || component instanceof OUIObjectIterator || (clazz != null && clazz.isInstance(component))))
                throw new IllegalStateException("component.getClientId [" + component.getClientId(context) + "] " +
                        "is supposed to be equal to componentId [" + thisComponentId + "]");
        }
    }

    protected void assertChildren(UIViewRoot viewRoot) {
        if (viewRoot.getChildCount() == 0) {
            throw new IllegalStateException("View should have been already restored.");
        }
    }

    private void doEncodeChildren(FacesContext context) throws IOException {
        ExternalContext externalContext = context.getExternalContext();
        RequestFacade request = RequestFacade.getInstance(externalContext.getRequest());

        Map<String, Object> requestMap = externalContext.getRequestMap();
        if (requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING)) {
            handleSessionExpirationOnEncodeChildren(context, request);
            releaseSyncObject(context);
            return;
        }

        if (AjaxUtil.isPortletRequest(context)) {
            renderPortletsAjaxResponse(context);
            releaseSyncObject(context);
            return;
        }

        String[] render = extractRender(request);

        UIViewRoot viewRoot = context.getViewRoot();

        assertChildren(viewRoot);

        loadBundles(context);

        UIComponent[] components = locateComponents(render, viewRoot, false, true);
        Object originalResponse = externalContext.getResponse();
        ResponseFacade response = ResponseFacade.getInstance(originalResponse);
        Integer sequence = getSequenceIdForMyFaces(context);
        finishProcessAjaxRequest(context, request, response, components, sequence);

        releaseSyncObject(context);

    }

    /**
     * @param componentId it to be verified
     * @throws IllegalStateException if the passed component id is {@code null}
     */
    private void assertComponentId(String componentId) {
        if (componentId == null)
            throw new IllegalStateException("processAjaxRequest: " + AjaxUtil.PARAM_RENDER + " is null");
    }

    private void handleSessionExpirationOnEncodeChildren(FacesContext context, RequestFacade request) throws IOException {
        ExternalContext externalContext = context.getExternalContext();
        Object originalResponse = externalContext.getResponse();
        if (originalResponse instanceof HttpServletResponse) {
            ResponseWrapper response = new ResponseWrapper((HttpServletResponse) originalResponse);
            response.setHeader(AjaxViewHandler.AJAX_EXPIRED_HEADER, AjaxViewHandler.AJAX_VIEW_EXPIRED);
        }

        UIViewRoot viewRoot = context.getViewRoot();
        List<UIComponent> children = viewRoot.getChildren();
        AjaxSettings ajaxSettings = null;

        String[] componentIds = extractRender(request);
        Map<String, Object> requestMap = externalContext.getRequestMap();

        assertChildren(viewRoot);

        UIComponent component = componentIds.length > 0 ? findComponentById(viewRoot, componentIds[0], false, false) : null;
        if (component != null && component.getChildCount() > 0) {
            List<UIComponent> ajaxSubmittedComponentChildren = component.getChildren();
            ajaxSettings = findAjaxSettings(ajaxSubmittedComponentChildren);
        }

        if (ajaxSettings == null) {
            ajaxSettings = findPageAjaxSettings(children);
        }

        if (ajaxSettings == null) {
            Map initParameterMap = externalContext.getInitParameterMap();
            String sessionExpirationHandling = (initParameterMap != null)
                    ? (String) initParameterMap.get(APPLICATION_SESSION_EXPIRATION_PARAM_NAME)
                    : null;

            if (sessionExpirationHandling != null && sessionExpirationHandling.length() > 0) {
                if (sessionExpirationHandling.equalsIgnoreCase(SILENT_SESSION_EXPIRATION_HANDLING)) {
                    ajaxSettings = createSilentSessionExpirationSettings();
                } else if (sessionExpirationHandling.equalsIgnoreCase(DEFAULT_SESSION_EXPIRATION_HANDLING)) {
                    ajaxSettings = createDefaultSessionExpirationSettings(context);
                }
            } else {
                ajaxSettings = createDefaultSessionExpirationSettings(context);
            }
        }

        if (ajaxSettings != null) {
            boolean isNonPortletRequest = !AjaxUtil.isPortletRequest(context);
            AbstractResponseFacade responseFacade =
                    finishSessionExpirationAjaxResponse(context, request, new UIComponent[]{ajaxSettings},
                            isNonPortletRequest);
            String sessionExpiredResponse = null;

            if (responseFacade.getOutputStream() != null) {
                ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) responseFacade.getOutputStream();
                sessionExpiredResponse = byteArrayOutputStream.toString("UTF-8");
            } else if (responseFacade.getWriter() != null) {
                sessionExpiredResponse = responseFacade.getWriter().toString();
            }

            if (sessionExpiredResponse != null) {
                requestMap.put(AjaxViewHandler.SESSION_EXPIRED_RESPONSE, sessionExpiredResponse);
            }
        }
    }

    private static void releaseSyncObject(FacesContext context) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        // TODO [sanders] (Apr 1, 2009, 5:09 AM): Can't we synchronize on something shorter?.. :)
        // TODO [sanders] (Apr 1, 2009, 5:09 AM): Won't java.util.concurrent help?
        RequestsSyncObject syncObject = (RequestsSyncObject) sessionMap.get(AjaxViewHandler.SESSION_SYNCHRONIZATION);
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (syncObject) {
            syncObject.setAjaxRequestProcessing(false);
            syncObject.notifyAll();
        }
    }

    private AjaxSettings findAjaxSettings(List<UIComponent> children) {
        AjaxSettings result = null;
        for (Object iteratedChild : children) {
            if (iteratedChild instanceof AjaxSettings) {
                result = (AjaxSettings) iteratedChild;
                return result;
            }
            UIComponent uiComponent = (UIComponent) iteratedChild;
            if (uiComponent.getChildCount() > 0) {
                result = findAjaxSettings(uiComponent.getChildren());
                if (result != null) {
                    return result;
                }
            }
        }
        return result;
    }

    private AjaxSettings findPageAjaxSettings(List<UIComponent> children) {
        AjaxSettings result = null;
        for (Object iteratedChild : children) {
            if (iteratedChild instanceof AjaxSettings && isPageSettings((AjaxSettings) iteratedChild)) {
                result = (AjaxSettings) iteratedChild;
                return result;
            }
            UIComponent uiComponent = (UIComponent) iteratedChild;
            if (uiComponent.getChildCount() > 0) {
                result = findPageAjaxSettings(uiComponent.getChildren());
                if (result != null) {
                    return result;
                }
            }
        }
        return result;
    }

    public static void processExceptionDuringAjax(FacesContext context, Throwable exception) {
        try {
            ExternalContext externalContext = context.getExternalContext();
            Map<String, Object> requestMap = externalContext.getRequestMap();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            if (AjaxUtil.isPortletRequest(context) && externalContext.getRequest() instanceof RenderRequest) {
                try {
                    if (!Environment.isRI()) {
                        finishProccessErrorUnderPortletsDuringAjax(context, exception);
                    } else if (sessionMap.containsKey(AjaxViewHandler.ERROR_OCCURRED_UNDER_PORTLETS)) {
                        finishProccessErrorUnderPortletsDuringAjax(context, exception);
                    }
                } catch (IOException e) {
                    Log.log(context, "An attempt to process exception during ajax failed.IOException was thrown during processing.");
                }
            } else if (!(sessionMap.containsKey(AjaxViewHandler.ERROR_OCCURRED_UNDER_PORTLETS))
                    && AjaxUtil.isPortletRequest(context)
                    && externalContext.getRequest() instanceof ActionRequest) {
                sessionMap.put(AjaxViewHandler.ERROR_OCCURRED_UNDER_PORTLETS, Boolean.TRUE);
                sessionMap.put(AjaxViewHandler.ERROR_OBJECT_UNDER_PORTLETS, exception);
            }
            if (!requestMap.containsKey(AjaxViewHandler.ERROR_OCCURRED)) {
                requestMap.put(AjaxViewHandler.ERROR_OCCURRED, Boolean.TRUE.toString());
                requestMap.put(AjaxViewHandler.ERROR_MESSAGE_HEADER, exception.getClass().getName() + ": " + exception.getMessage());
                requestMap.put(AjaxViewHandler.ERROR_CAUSE_MESSAGE_HEADER, exception.getCause());
            }
        } finally {
            releaseSyncObject(context);
        }

    }

    private boolean isPageSettings(AjaxSettings ajaxSettings) {
        return (ajaxSettings.getParent() instanceof UIViewRoot || ajaxSettings.getParent() instanceof UIForm);
    }

    private AjaxSettings createSilentSessionExpirationSettings() {
        AjaxSettings result = new AjaxSettings();
        result.setSessionExpiration(new SilentSessionExpiration());
        return result;
    }

    private AjaxSettings createDefaultSessionExpirationSettings(FacesContext context) {
        AjaxSettings result = new AjaxSettings();
        DefaultSessionExpiration dse = new DefaultSessionExpiration();
        result.setSessionExpiration(dse);
        return result;
    }

    public int getChildCount() {
        int childCount = parentGetChildCount();
        UIViewRoot delegate = ((WrappedAjaxRoot) viewRoot).getDelegate();
        if (childCount == 0 && delegate != null) {
            childCount = delegate.getChildCount();
        }
        return childCount;
    }

    public List<UIComponent> getChildren() {
        List<UIComponent> children = parentGetChildren();
        UIViewRoot delegate = ((WrappedAjaxRoot) viewRoot).getDelegate();

        if (children == null || children.isEmpty() && delegate != null) {
            List<UIComponent> delegateChildren = delegate.getChildren();
            if (children == null) {
                children = new ArrayList<UIComponent>();
            }
            children.addAll(delegateChildren);
        }

        return children;
    }

    public Iterator<UIComponent> getFacetsAndChildren() {
        Iterator<UIComponent> facetsAndChildren = parentGetFacetsAndChildren();
        UIViewRoot delegate = ((WrappedAjaxRoot) viewRoot).getDelegate();
        if (facetsAndChildren == null || !facetsAndChildren.hasNext() && delegate != null) {
            facetsAndChildren = delegate.getFacetsAndChildren();
        }
        return facetsAndChildren;
    }


    /**
     * Find all instances of {@link org.openfaces.component.util.LoadBundle} in view tree and load bundles
     * to request-scope map.
     */
    private void loadBundles(FacesContext context) {
        loadBundles(context, context.getViewRoot());
    }

    /**
     * Recursive helper for {@link #loadBundles(FacesContext)}
     */
    private void loadBundles(FacesContext context, UIComponent component) {
        // Iterate over children
        for (UIComponent child : component.getChildren()) {
            loadChildBundles(context, child);
        }
        // Iterate over facets
        for (UIComponent child : component.getFacets().values()) {
            loadChildBundles(context, child);
        }
    }

    private void loadChildBundles(FacesContext context, UIComponent child) {
        if (child instanceof AjaxLoadBundleComponent) {
            try {
                child.encodeBegin(context);
            } catch (IOException e) {
                Log.log(context, "Exception while invoking LoadBundle", e);
            }
        } else {
            loadBundles(context, child);
        }
    }


    private void ajaxApplyRequestValues(FacesContext context,
                                        UIComponent[] components,
                                        UIViewRoot viewRoot,
                                        String[] execute)
            throws FacesException {
        if (components != null) {
            for (UIComponent component : components) {
                Log.log(context, "start ajaxApplyRequestValues for " + component);
                component.processDecodes(context);
                Log.log(context, "finish ajaxApplyRequestValues for " + component);
            }
        }

        if (execute != null) {
            for (String submittedComponentId : execute) {
                UIComponent submittedComponent = findComponentById(viewRoot, submittedComponentId);
                Log.log(context, "start ajaxApplyRequestValues for " + submittedComponent);
                submittedComponent.processDecodes(context);
                Log.log(context, "finish ajaxApplyRequestValues for " + submittedComponent);
            }
        }
    }

    private void ajaxProcessValidations(FacesContext context,
                                        UIComponent[] components,
                                        UIViewRoot viewRoot,
                                        String[] execute) throws FacesException {
        if (components != null) {
            for (UIComponent component : components) {
                Log.log(context, "start ajaxProcessValidations for " + component);
                component.processValidators(context);
                Log.log(context, "finish ajaxProcessValidations for " + component);
            }
        }
        if (execute != null) {
            for (String submittedComponentId : execute) {
                UIComponent submittedComponent = findComponentById(viewRoot, submittedComponentId);
                Log.log(context, "start ajaxProcessValidations for " + submittedComponent);
                submittedComponent.processValidators(context);
                Log.log(context, "finish ajaxProcessValidations for " + submittedComponent);
            }
        }
    }

    private void ajaxUpdateModelValues(FacesContext context,
                                       UIComponent[] components,
                                       UIViewRoot viewRoot,
                                       String[] execute)
            throws FacesException {
        if (components != null) {
            for (UIComponent component : components) {
                Log.log(context, "start ajaxUpdateModelValues for " + component);
                component.processUpdates(context);
                Log.log(context, "finish ajaxUpdateModelValues for " + component);
            }
        }
        if (execute != null) {
            for (String submittedComponentId : execute) {
                UIComponent submittedComponent = findComponentById(viewRoot, submittedComponentId);
                Log.log(context, "start ajaxUpdateModelValues for " + submittedComponent);
                submittedComponent.processUpdates(context);
                Log.log(context, "finish ajaxUpdateModelValues for " + submittedComponent);
            }
        }
    }

    private void renderPortletsAjaxResponse(FacesContext context) {
        if (!AjaxUtil.isAjaxRequest(context)) {
            throw new IllegalStateException("This method should be only invoked for portlet Ajax requests");
        }

        if (!AjaxUtil.isPortletRequest(context)) {
            throw new IllegalStateException("This method should be only invoked for portlet Ajax requests");
        }

        Integer sequenceId = (Environment.isLiferay(context.getExternalContext().getRequestMap()))
                ? getSequenceIdForMyFaces(context)
                : null;

        Map sessionMap = context.getExternalContext().getSessionMap();
        String componentId = (String) sessionMap.get(AjaxUtil.PARAM_RENDER);
        if (componentId == null) {
            Log.log(context, "CommonAjaxViewRoot.renderPortletsAjaxResponse: " + AjaxUtil.PARAM_RENDER + " == null");
            // Can happen sometimes on simultaneous ajax requests in Portlets.
            // Seems that there's no better way to handle it in Portlets 1.0
            return;
        }
        String[] render = componentId.split(";");
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(AjaxUtil.KEY_RENDERING_PORTLETS_AJAX_RESPONSE, Boolean.TRUE);
        try {
            UIViewRoot viewRoot = context.getViewRoot();
            assertChildren(viewRoot);

            // clear the style ids set just constructed on the "render" phase in order to avoid warning of repeated style rendering
            Styles.getRenderedStyleElementsIds(context).clear();

            loadBundles(context);

            UIComponent[] components = new UIComponent[render.length];
            for (int i = 0; i < render.length; i++) {
                String component = render[i];
                UIComponent findComponent = findComponentById(viewRoot, component);
                if (findComponent == null) {
                    throw new IllegalStateException("Couldn't find component by client id: " + component);
                }
                components[i] = findComponent;
            }
            ExternalContext externalContext = context.getExternalContext();
            RequestFacade request = RequestFacade.getInstance(externalContext.getRequest());
            ResponseFacade response = ResponseFacade.getInstance(externalContext.getResponse());
            try {
                finishProcessAjaxRequest(context, request, response, components, sequenceId);
            } catch (IOException e) {
                throw new FacesException(e);
            }
        } finally {
            if (!requestMap.containsKey(AjaxViewHandler.SESSION_EXPIRATION_PROCESSING)) {
                clearPortletSessionParams(context);
            }
            requestMap.remove(AjaxUtil.KEY_RENDERING_PORTLETS_AJAX_RESPONSE);
        }
    }

    private static void clearPortletSessionParams(FacesContext context) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        synchronized (CommonAjaxViewRoot.class) {
            sessionMap.remove(AjaxUtil.PARAM_RENDER);
            sessionMap.remove(AjaxUtil.UPDATE_PORTIONS_SUFFIX);
            sessionMap.remove(AjaxUtil.CUSTOM_JSON_PARAM);
            sessionMap.remove(AjaxUtil.AJAX_REQUEST_MARKER);
        }
    }

    private void finishProcessAjaxRequest(
            FacesContext context,
            RequestFacade request,
            ResponseFacade response,
            UIComponent[] components,
            Integer sequence) throws IOException {
        AjaxResponse ajaxResponse = ajaxRenderResponse(request, context, components);

        AjaxSavedStateIdxHolder stateIdxHolder = ajaxSaveState(context, request, ajaxResponse, components, sequence);

        ajaxResponse.setStateIdxHolder(stateIdxHolder);

        ajaxResponse.write(response);
    }

    private AbstractResponseFacade finishSessionExpirationAjaxResponse(FacesContext context,
                                                                       RequestFacade request,
                                                                       UIComponent[] components,
                                                                       boolean nonPortletAjaxRequest) throws IOException {
        AjaxResponse ajaxResponse = null;
        if (!nonPortletAjaxRequest) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.put(AjaxUtil.KEY_RENDERING_PORTLETS_AJAX_RESPONSE, Boolean.TRUE);
            try {
                // clear the style ids set just constructed on the "render" phase in order to avoid warning of repeated style rendering
                Styles.getRenderedStyleElementsIds(context).clear();
                ajaxResponse = ajaxRenderResponse(request, context, components);
            } finally {
                clearPortletSessionParams(context);
                requestMap.remove(AjaxUtil.KEY_RENDERING_PORTLETS_AJAX_RESPONSE);
            }
        } else {
            ajaxResponse = ajaxRenderResponse(request, context, components);
        }

        ResponseFacade response = ResponseFacade.getInstance(context.getExternalContext().getResponse());
        AbstractResponseFacade responseFacade = new ResponseAdapter(response);
        ajaxResponse.setStateIdxHolder(new AjaxSavedStateIdxHolder());
        if (!nonPortletAjaxRequest) {
            ajaxResponse.setSessoinExpired(Boolean.TRUE.toString());
            ajaxResponse.setSessoinExpiredLocation((String) context.getExternalContext().getRequestMap().get(AjaxViewHandler.LOCATION_HEADER));
            ajaxResponse.write(response);
        } else {
            ajaxResponse.write(responseFacade);
        }
        return responseFacade;
    }

    // TODO [sanders] (Apr 1, 2009, 5:11 AM): Too long name
    private static void finishProccessErrorUnderPortletsDuringAjax(FacesContext context, Throwable e) throws IOException {
        AjaxResponse ajaxResponse = new AjaxResponse();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(AjaxUtil.KEY_RENDERING_PORTLETS_AJAX_RESPONSE, Boolean.TRUE);
        ajaxResponse.setStateIdxHolder(new AjaxSavedStateIdxHolder());
        try {
            ajaxResponse.setException(e);
            ajaxResponse.write(ResponseFacade.getInstance(context.getExternalContext().getResponse()));
        } finally {
            clearPortletSessionParams(context);
            requestMap.remove(AjaxUtil.KEY_RENDERING_PORTLETS_AJAX_RESPONSE);
        }
    }

    private void ajaxPrepareInitializationScripts(
            FacesContext context, AjaxResponse ajaxResponse, List<String> foreignHeadScripts, StringBuilder initializationScripts) {
        StringBuilder tempBuffer = new StringBuilder();
        if (foreignHeadScripts != null) {
            for (String script : foreignHeadScripts) {
                StringInspector scriptInspector = new StringInspector(script);
                boolean substituteDocumentWrite = scriptInspector.indexOfIgnoreCase("document.write") > -1;
                if (substituteDocumentWrite) {
                    tempBuffer.append("O$.substituteDocumentWrite();\n"); // tricky workaround. see ajaxUtil.js for details
                }
                tempBuffer.append(script).append("\n");
                if (substituteDocumentWrite) {
                    tempBuffer.append("O$.restoreDocumentWrite();\n"); // tricky workaround. see ajaxUtil.js for details
                }
            }
        }
        if (tempBuffer.length() > 0) {
            initializationScripts.insert(0, tempBuffer.toString());
        }

        if (initializationScripts.length() > 0) {
            String initScriptsStr = initializationScripts.toString();
            initScriptsStr = initScriptsStr.replaceAll("<!--", "").replaceAll("//-->", "");
            // create special node with runtime js library that contains all initialization scripts
            String uniqueRTLibraryName = ResourceFilter.RUNTIME_INIT_LIBRARY_PATH + AjaxUtil.generateUniqueInitLibraryName();
            String initLibraryUrl = Resources.getApplicationURL(context, uniqueRTLibraryName);
            ajaxResponse.setInitLibraryName(initLibraryUrl);

            context.getExternalContext().getSessionMap().put(uniqueRTLibraryName, initScriptsStr);
        }
    }

    private AjaxResponse ajaxRenderResponse(
            RequestFacade request,
            FacesContext context,
            UIComponent[] components
    ) throws IOException {

        AjaxResponse ajaxResponse = new AjaxResponse();
        // collect all initialization scripts to buffer to use them in runtime loaded js library
        StringBuilder initializationScripts = new StringBuilder();

        List<String> updatePortions = AjaxUtil.getAjaxPortionNames(context, request);
        for (UIComponent component : components) {
            Log.log(context, "ajaxRenderResponse start for component " + component);
            try {
                if (updatePortions.isEmpty()) {
                    renderSimpleUpdate(request, context, component, ajaxResponse, initializationScripts);
                } else {
                    renderPortionUpdate(request, context, component, ajaxResponse, initializationScripts, updatePortions);
                }
            } catch (IOException e) {
                throw new FacesException(e.getMessage(), e);
            }
            Log.log(context, "ajaxRenderResponse finish for component " + component);
        }

        AjaxRequest ajaxRequest = AjaxRequest.getInstance(context);
        Set<String> additionalRender = ajaxRequest.getReloadedComponentIds();
        UIViewRoot viewRoot = context.getViewRoot();
        for (String componentId : additionalRender) {
            UIComponent component = findComponentById(viewRoot, componentId, false, true);
            renderSimpleUpdate(request, context, component, ajaxResponse, initializationScripts);
        }

        AjaxPluginIncludes availableIncludes = PluginsLoader.getAvailableIncludes(context);
        List<String> foreignHeadScripts = availableIncludes.getScripts();
        ajaxPrepareInitializationScripts(context, ajaxResponse, foreignHeadScripts, initializationScripts);

        //todo: find component with inheader styles declaration and add corresponding functionality to AjaxPlugin(s)

        addJSLibraries(context, ajaxResponse);
        List<String> jsLibraries = availableIncludes.getJsIncludes();
        if (jsLibraries != null)
            addForeignJSLibraries(ajaxResponse, jsLibraries);

        addStyles(context, ajaxResponse, components);
        List<String> cssFiles = availableIncludes.getCssIncludes();
        if (cssFiles != null) {
            addForeignCSSFiles(ajaxResponse, cssFiles);
        }
        return ajaxResponse;
    }

    private void renderPortionUpdate(RequestFacade request, FacesContext context, UIComponent component, AjaxResponse ajaxResponse, StringBuilder initializationScripts, List<String> updatePortions) throws IOException {
        RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = factory.getRenderKit(context, context.getViewRoot().getRenderKitId());
        Renderer renderer = renderKit.getRenderer(component.getFamily(), component.getRendererType());
        JSONObject customJSONParam = AjaxUtil.getCustomJSONParam(context, request);
        AjaxPortionRenderer ajaxComponentRenderer = (AjaxPortionRenderer) renderer;
        for (String nextId : updatePortions) {
            StringBuilder portionOutput;
            JSONObject responseData;
            StringWriter stringWriter = new StringWriter();
            ResponseWriter originalWriter = substituteResponseWriter(context, request, stringWriter);
            try {
                responseData = ajaxComponentRenderer.encodeAjaxPortion(context, component, nextId, customJSONParam);
                portionOutput = new StringBuilder(stringWriter.toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            } finally {
                restoreWriter(context, originalWriter);
            }

            StringBuilder rawScriptsBuffer = new StringBuilder();
            StringBuilder rtLibraryScriptsBuffer = new StringBuilder();
            extractScripts(portionOutput, rawScriptsBuffer, rtLibraryScriptsBuffer);
            if (rtLibraryScriptsBuffer.length() > 0) {
                initializationScripts.append(rtLibraryScriptsBuffer).append("\n");
            }
            ajaxResponse.addPortion(nextId, portionOutput.toString(), rawScriptsBuffer.toString(), responseData);
        }
    }

    private void renderSimpleUpdate(RequestFacade request, FacesContext context, UIComponent component, AjaxResponse ajaxResponse, StringBuilder initializationScripts) throws IOException {
        StringWriter wrt = new StringWriter();
        ResponseWriter originalWriter = substituteResponseWriter(context, request, wrt);
        StringBuilder outputBuffer;
        try {
            component.encodeBegin(context);
            component.encodeChildren(context);
            component.encodeEnd(context);

            outputBuffer = new StringBuilder(wrt.toString());
        } finally {
            restoreWriter(context, originalWriter);
        }
        StringBuilder rtLibraryScriptsBuffer = new StringBuilder();
        StringBuilder rawScriptsBuffer = new StringBuilder();
        extractScripts(outputBuffer, rawScriptsBuffer, rtLibraryScriptsBuffer);
        if (rtLibraryScriptsBuffer.length() > 0) {
            initializationScripts.append(rtLibraryScriptsBuffer).append("\n");
        }
        String output = outputBuffer.toString();
        String clientId = component.getClientId(context);
        ajaxResponse.addSimpleUpdate(clientId, output, rawScriptsBuffer.toString());
    }

    private void extractScripts(StringBuilder buffer,
                                StringBuilder rawScriptBuffer,
                                StringBuilder rtLibraryScriptBuffer) {
        String scriptStart = "<script";
        String scriptEnd = "/script>";
        while (true) {
            StringInspector bufferInspector = new StringInspector(buffer.toString());
            int fromIndex = bufferInspector.indexOfIgnoreCase(scriptStart);
            if (fromIndex == -1)
                break;

            int toIndex = bufferInspector.indexOfIgnoreCase(scriptEnd, fromIndex);
            if (toIndex == -1)
                break;

            toIndex += scriptEnd.length();
            String rawScript = buffer.substring(fromIndex, toIndex);
            String script = purifyScripts(new StringInspector(rawScript));

            Matcher matcher = JS_VAR_PATTERN.matcher(rawScript);
            boolean varFound = matcher.find();

            buffer.delete(fromIndex, toIndex);

            if (new StringInspector(script).indexOfIgnoreCase("document.write") > -1) {
                if (tempIdCounter == Long.MAX_VALUE) tempIdCounter = 0;
                String someId = "OpenFaces_Ajax_Placeholder:ajax_placeholser_" + tempIdCounter++;
                buffer.insert(fromIndex, "<span id=\"" + someId + "\"></span>");
                script = "O$.substituteDocumentWrite();\n" + script + "\nO$.restoreDocumentWrite('" + someId + "');\n";
            }
            if (varFound) {
                rtLibraryScriptBuffer.append(script).append("\n");
            } else {
                rawScriptBuffer.append(rawScript);
            }
        }
    }

    private String purifyScripts(StringInspector script) {
        StringBuffer result = new StringBuffer();
        int startIdx = script.indexOfIgnoreCase("<script");
        int endIdx = script.indexOfIgnoreCase("</script>");
        if (startIdx == -1 || endIdx == -1) return script.toString();
        int endScriptInit = script.toString().indexOf(">", startIdx + 1);
        if (startIdx > 0) {
            result.append(script.substring(0, startIdx));
            result.append("\n");
            script = script.substring(startIdx);
            // re-read indices
            startIdx = script.indexOfIgnoreCase("<script");
            endIdx = script.indexOfIgnoreCase("</script>");
            if (startIdx != -1)
                endScriptInit = script.toString().indexOf(">", startIdx + 1);
        }
        if (endScriptInit == -1) return script.toString();
        while (startIdx > -1) {
            result.append(script.substring(endScriptInit + 1, endIdx));
            result.append("\n");
            script = script.substring(endIdx + "</script>".length());
            // re-read indices
            startIdx = script.indexOfIgnoreCase("<script");
            endIdx = script.indexOfIgnoreCase("</script>");
            if (startIdx > -1)
                endScriptInit = script.toString().indexOf(">", startIdx + 1);
        }
        if (script.toString().length() > 0) {
            result.append(script);
            result.append("\n");
        }
        return result.toString();
    }

    private void addJSLibraries(FacesContext context, AjaxResponse ajaxResponse) {
        List<String> libraries = (List<String>) context.getExternalContext().getRequestMap().get(Resources.HEADER_JS_LIBRARIES);
        if (libraries == null) return;
        for (String jsLibrary : libraries) {
            ajaxResponse.addJsLibrary(jsLibrary);
        }
    }

    /**
     * Adds all JS libraries declarations retrieved from third-party JSF components libraries that are currently supported
     * and returned by corresponding plugin.
     */
    private void addForeignJSLibraries(AjaxResponse ajaxResponse, List<String> libraries) {
        for (String library : libraries) {
            ajaxResponse.addJsLibrary(library);
        }
    }

    private void addStyles(FacesContext context, AjaxResponse ajaxResponse, UIComponent[] components) {
        for (UIComponent component : components) {
            List<String> styleClasses = Styles.getAllStyleClassesForComponent(context, component);
            addStyleClasses(ajaxResponse, styleClasses);
            Styles.markStylesRenderedForComponent(context, component);
        }
    }

    private void addStyleClasses(AjaxResponse ajaxResponse, List<String> styleClasses) {
        if (styleClasses == null) return;
        for (String style : styleClasses) {
            ajaxResponse.addStyle(style);
        }
    }

    private void addForeignCSSFiles(AjaxResponse ajaxResponse, List<String> cssFiles) {
        for (String cssFile : cssFiles) {
            ajaxResponse.addCssFile(cssFile);
        }
    }

    /**
     * Save state for processed ajaxs request
     *
     * @return If there is server side state saving, return serializid view state. Otherwise, return null.
     *         Serialized view state is used for adjusting "com.sun.faces.VIEW" for RI faces implementation
     */
    @SuppressWarnings({"deprecation"})
    private AjaxSavedStateIdxHolder ajaxSaveState(
            FacesContext context,
            RequestFacade request,
            AjaxResponse ajaxResponse,
            UIComponent[] components, Integer sequence) throws IOException {
        AjaxSavedStateIdxHolder savedStateStructure = new AjaxSavedStateIdxHolder();
        StateManager stateManager = context.getApplication().getStateManager();
        boolean savingStateInClient = stateManager.isSavingStateInClient(context);
        if (savingStateInClient) {
            StringWriter stringWriter = new StringWriter();
            ResponseWriter originalWriter = substituteResponseWriter(context, request, stringWriter);
            try {
                for (UIComponent component : components) {
                    Object state = component.processSaveState(context);
                    String clientId = component.getClientId(context);
                    writeState(context, clientId, state);
                    String stateString = stringWriter.toString();
                    ajaxResponse.addComponentState(clientId, stateString);
                }
            } finally {
                restoreWriter(context, originalWriter);
            }
        } else {
            StateManager.SerializedView view = stateManager.saveSerializedView(context);

            boolean myFaces = Environment.isMyFaces();
            boolean richFaces = Environment.isRichFacesStateManager(stateManager);
            boolean facelets = Environment.isFacelets(context);

            if (myFaces && !richFaces && !facelets) {
                obtainViewStateSequenceForMyFaces(context, request, sequence, savedStateStructure);
            }

            if (!myFaces && view != null) {
                obtainViewStateSequence(context, request, view, savedStateStructure);
            } else if (myFaces) {
                if (richFaces && view != null) {
                    obtainViewStateSequence(context, request, view, savedStateStructure);
                } else {
                    if (facelets) {
                        obtainViewStateSequenceForMyFaces(context, request, sequence, savedStateStructure);
                    }
                }
            }

            if (view != null) {
                savedStateStructure.setViewStructureId(view.getStructure());
            }
        }
        return savedStateStructure;
    }

    private void obtainViewStateSequence(FacesContext context, RequestFacade request, StateManager.SerializedView view,
                                         AjaxSavedStateIdxHolder stateIdxHolder) throws IOException {
        StringWriter stringWriter = new StringWriter();
        ResponseWriter originalWriter = substituteResponseWriter(context, request, stringWriter);

        ResponseStateManager responseStateManager = context.getRenderKit().getResponseStateManager();
        responseStateManager.writeState(context, view);

        restoreWriter(context, originalWriter);

        String stateString = stringWriter.getBuffer().toString();
        // This is necessarry to obtain valid state key for updating it on client side
        parseStateString(stateString, stateIdxHolder);
    }

    private void obtainViewStateSequenceForMyFaces(FacesContext context, RequestFacade request, Integer sequence,
                                                   AjaxSavedStateIdxHolder stateIdxHolder)
            throws IOException {
        StringWriter stringWriter = new StringWriter();
        ResponseWriter originalWriter = substituteResponseWriter(context, request, stringWriter);

        if (!Environment.isFacelets(context)) {
            context.getApplication().getViewHandler().writeState(context);
        } else {
            ResponseStateManager responseStateManager = context.getRenderKit().getResponseStateManager();

            Object[] state = new Object[2];
            state[0] = Integer.toString(sequence, Character.MAX_RADIX);

            responseStateManager.writeState(context, state);
        }
        restoreWriter(context, originalWriter);

        String stateString = stringWriter.getBuffer().toString();
        // This is necessarry to obtain valid state key for updating it on client side
        parseStateString(stateString, stateIdxHolder);
    }

    private void parseStateString(String stateString, AjaxSavedStateIdxHolder stateIdxHolder) {
        if (stateString == null) {
            return;
        }

        Pattern valuePattern = Pattern.compile("value=(\"([^\"]*\")|'[^']*')");
        final Matcher matcher = valuePattern.matcher(stateString);
        final boolean isValuePatternFound = matcher.find();

        if (isValuePatternFound) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();
            String valueString = stateString.substring(startIndex, endIndex);
            int firstIndex = VALUE_ATTR_STRING.length();

            String viewStateString = valueString.substring(firstIndex, valueString.lastIndexOf("\""));
            stateIdxHolder.setViewStateIdentifier(viewStateString);
        } else {
            throw new FacesException("Could not obtain view state identifier by state description string - : " + stateString);
        }
    }

    private Integer getSequenceIdForMyFaces(FacesContext context) { // see JSFC-1516
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(false);
        if (session == null)
            return null;

        Map<String, Object> sessionMap = externalContext.getSessionMap();
        Integer sequence = (Integer) sessionMap.get(MYFACES_SEQUENCE_PARAM);
        return sequence;
    }

    private UIComponent findComponentById(UIComponent parent, String id) {
        return findComponentById(parent, id, false, false);
    }

    private UIComponent findComponentById(UIComponent parent,
                                          String id,
                                          boolean preProcessDecodesOnTables,
                                          boolean preRenderResponseOnTables) {
        return findComponentById(parent, id, preProcessDecodesOnTables, preRenderResponseOnTables, true);
    }

    private UIComponent findComponentById(UIComponent parent,
                                          String id,
                                          boolean preProcessDecodesOnTables,
                                          boolean preRenderResponseOnTables,
                                          boolean checkComponentPresence) {
        UIComponent componentByPath = findComponentByPath(parent, id, preProcessDecodesOnTables, preRenderResponseOnTables);
        if (checkComponentPresence && componentByPath == null)
            throw new FacesException("Component by id not found: " + id);
        return componentByPath;
    }

    private UIComponent findComponentByPath(UIComponent parent,
                                            String path,
                                            boolean preProcessDecodesOnTables,
                                            boolean preRenderResponseOnTables) {
        while (true) {
            if (path == null) {
                return null;
            }

            int separator = path.indexOf(NamingContainer.SEPARATOR_CHAR, 1);
            if (separator == -1)
                return componentById(parent, path, true, preProcessDecodesOnTables, preRenderResponseOnTables);

            String id = path.substring(0, separator);
            UIComponent nextParent = componentById(parent, id, false, preProcessDecodesOnTables, preRenderResponseOnTables);
            if (nextParent == null) {
                return null;
            }
            parent = nextParent;
            path = path.substring(separator + 1);
        }
    }

    private UIComponent componentById(UIComponent parent, String id, boolean isLastComponentInPath,
                                      boolean preProcessDecodesOnTables, boolean preRenderResponseOnTables) {
        if (id.length() > 0 && (isIntegerNumber(id) || id.startsWith(":")) && parent instanceof AbstractTable) {
            AbstractTable table = ((AbstractTable) parent);
            if (!isLastComponentInPath) {
                if (preProcessDecodesOnTables)
                    table.invokeBeforeProcessDecodes(FacesContext.getCurrentInstance());
                if (preRenderResponseOnTables) {
                    table.invokeBeforeRenderResponse(FacesContext.getCurrentInstance());
                    table.setRowIndex(-1); // make the succeeding setRowIndex call provide the just-read actual row data through request-scope variables
                }

                int rowIndex = table.getRowIndexByClientSuffix(id);
                if (table.getRowIndex() == rowIndex) {
                    // ensure row index setting will be run anew to ensure proper request-scope variable values
                    table.setRowIndex(-1);
                }
                table.setRowIndex(rowIndex);
            } else {
                int rowIndex = table.getRowIndexByClientSuffix(id);
                if (table.getRowIndex() == rowIndex) {
                    // ensure row index setting will be run anew to ensure proper request-scope variable values
                    table.setRowIndex(-1);
                }
                table.setRowIndex(rowIndex);
            }
            return table;
        } else if (isIntegerNumber(id) && parent instanceof UIData) {
            UIData grid = ((UIData) parent);
            int rowIndex = Integer.parseInt(id);
            grid.setRowIndex(rowIndex);
            return grid;
        } else if (id.charAt(0) == ':' && parent instanceof OUIObjectIterator) {
            id = id.substring(1);
            OUIObjectIterator iterator = (OUIObjectIterator) parent;
            iterator.setObjectId(id);
            return (UIComponent) iterator;
        } else if (isIntegerNumber(id)) {
            try {
                Class clazz = Class.forName("com.sun.facelets.component.UIRepeat");
                if (clazz.isInstance(parent)) {
                    ReflectionUtil.invokeMethod("com.sun.facelets.component.UIRepeat", "setIndex",
                            new Class[]{Integer.TYPE}, new Object[]{Integer.parseInt(id)}, parent);
                    return parent;
                }
            } catch (ClassNotFoundException e) {
                //do nothing - it's ok - not facelets environment
            }

        }
        if (id.equals(parent.getId()))
            return parent;

        Iterator<UIComponent> iterator = parent.getFacetsAndChildren();
        while (iterator.hasNext()) {
            UIComponent child = iterator.next();
            if (child instanceof NamingContainer) {
                if (id.equals(child.getId()))
                    return child;
            } else {
                UIComponent component = componentById(child, id,
                        isLastComponentInPath, preProcessDecodesOnTables, preRenderResponseOnTables);
                if (component != null)
                    return component;
            }
        }
        return null;
    }

    private boolean isIntegerNumber(String id) {
        if (id == null || id.length() == 0)
            return false;
        for (int i = 0, length = id.length(); i < length; i++) {
            char c = id.charAt(i);
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    private void writeState(FacesContext context, String clientId, Object state) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String stateStr = AjaxUtil.objectToString(state);
        String fieldName = AjaxUtil.getComponentStateFieldName(clientId);
        Rendering.renderHiddenField(writer, fieldName, stateStr);
    }

    private ResponseWriter substituteResponseWriter(FacesContext context, RequestFacade request, Writer innerWriter) {
        ResponseWriter newWriter;
        ResponseWriter responseWriter = context.getResponseWriter();
        if (responseWriter != null) {
            newWriter = responseWriter.cloneWithWriter(innerWriter);
        } else {
            RenderKitFactory factory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = factory.getRenderKit(context, context.getViewRoot().getRenderKitId());
            newWriter = renderKit.createResponseWriter(innerWriter, null, request.getCharacterEncoding());
        }
        context.setResponseWriter(newWriter);
        return responseWriter;
    }

    private void restoreWriter(FacesContext context, ResponseWriter originalWriter) {
        if (originalWriter != null)
            context.setResponseWriter(originalWriter);
    }

    private void broadcastEvents(FacesContext context, PhaseId phaseId) {
        if (events == null) return;

        for (FacesEvent event : new ArrayList<FacesEvent>(events)) {
            PhaseId eventPhaseId = event.getPhaseId();
            if (eventPhaseId.compareTo(PhaseId.ANY_PHASE) != 0 && eventPhaseId.compareTo(phaseId) != 0)
                continue;
            events.remove(event);
            UIComponent source = event.getComponent();
            try {
                source.broadcast(event);
            } catch (AbortProcessingException e) {
                break;
            }
        }

        if (context.getRenderResponse() || context.getResponseComplete())
            events = null;
    }

    public void queueEvent(FacesEvent event) {
        if (event == null) throw new NullPointerException("event");
        if (events == null) {
            events = new ArrayList<FacesEvent>();
        }
        events.add(event);
    }
}
