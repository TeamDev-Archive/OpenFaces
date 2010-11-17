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

import org.openfaces.ajax.AjaxRequest;
import org.openfaces.component.OUIObjectIterator;
import org.openfaces.component.ajax.DefaultProgressMessage;
import org.openfaces.component.table.AbstractTable;
import org.openfaces.event.AjaxActionEvent;
import org.openfaces.renderkit.ajax.DefaultProgressMessageRenderer;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class UtilPhaseListener extends PhaseListenerBase {
    private static final String FOCUSED_COMPONENT_ID_KEY = UtilPhaseListener.class.getName() + ".focusedComponentId";
    private static final String FOCUS_TRACKER_FIELD_ID = "o::defaultFocus";
    private static final String FORCE_UTIL_JS_CONTEXT_PARAM = "org.openfaces.forceIncludingUtilJs";
    private static final String AUTO_FOCUS_TRACKING_CONTEXT_PARAM = "org.openfaces.autoSaveFocus";
    private static final String DISABLED_CONTEXT_MENU_CONTEXT_PARAM = "org.openfaces.disabledContextMenu";

    private static final String SCROLL_POS_KEY = UtilPhaseListener.class.getName() + ".pageScrollPos";
    private static final String SCROLL_POS_TRACKER_FIELD_ID = "o::defaultScrollPosition";
    private static final String AUTO_SCROLL_POS_TRACKING_CONTEXT_PARAM = "org.openfaces.autoSaveScrollPos";
    private static final String SUBMISSION_AJAX_INACTIVITY_TIMEOUT_CONTEXT_PARAM = "org.openfaces.submissionAjaxInactivityTimeout";
    private static final long DEFAULT_SUBMISSION_AJAX_INACTIVITY_TIMEOUT = 5000;

    public static final String PARAM_ACTION_COMPONENT = "_of_actionComponent";
    public static final String PARAM_ACTION_LISTENER = "_of_actionListener";
    public static final String PARAM_ACTION = "_of_action";
    public static final String PARAM_IMMEDIATE = "_of_immediate";

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
            boolean renderFocusScript = isAutoFocusTrackingEnabled(context);
            boolean renderScrollingScript = isAutoScrollPosTrackingEnabled(context);
            boolean renderContextMenuScript = isDisabledContextMenuEnabled(context);
            if (!renderedJsLinks.contains(utilJs)) {
                if (renderFocusScript ||
                        renderScrollingScript ||
                        renderContextMenuScript ||
                        getForceIncludingUtilJs(context)) {
                    Resources.registerJavascriptLibrary(context, utilJs);
                }
            }
            if (renderFocusScript)
                Rendering.appendOnLoadScript(context, encodeFocusTracking(context));
            if (renderScrollingScript)
                Rendering.appendOnLoadScript(context, encodeScrollPosTracking(context));

            if (renderContextMenuScript)
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

    private boolean getForceIncludingUtilJs(FacesContext context) {
        return Rendering.getBooleanContextParam(context, FORCE_UTIL_JS_CONTEXT_PARAM);
    }

    private boolean isAutoFocusTrackingEnabled(FacesContext context) {
        return Rendering.getBooleanContextParam(context, AUTO_FOCUS_TRACKING_CONTEXT_PARAM);
    }

    private boolean isDisabledContextMenuEnabled(FacesContext context) {
        return Rendering.getBooleanContextParam(context, DISABLED_CONTEXT_MENU_CONTEXT_PARAM);
    }

    private boolean isAutoScrollPosTrackingEnabled(FacesContext context) {
        return Rendering.getBooleanContextParam(context, AUTO_SCROLL_POS_TRACKING_CONTEXT_PARAM);
    }

    private Script encodeFocusTracking(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map requestMap = externalContext.getRequestMap();
        String focusedComponentId = (String) requestMap.get(FOCUSED_COMPONENT_ID_KEY);
        return new ScriptBuilder().functionCall("O$.initDefaultFocus",
                FOCUS_TRACKER_FIELD_ID,
                focusedComponentId != null ? focusedComponentId : null).semicolon();
    }

    private Script encodeScrollPosTracking(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map requestMap = externalContext.getRequestMap();
        String scrollPos = (String) requestMap.get(SCROLL_POS_KEY);
        return new ScriptBuilder().functionCall("O$.initDefaultScrollPosition",
                SCROLL_POS_TRACKER_FIELD_ID,
                scrollPos != null ? scrollPos : null).semicolon();
    }

    private Script encodeDisabledContextMenu(FacesContext facesContext) {
        return new RawScript("O$.disabledContextMenuFor(document);");
    }

    public static void encodeFormSubmissionAjaxInactivityTimeout(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        String paramStr = externalContext.getInitParameter(SUBMISSION_AJAX_INACTIVITY_TIMEOUT_CONTEXT_PARAM);

        long inactivityTimeout = DEFAULT_SUBMISSION_AJAX_INACTIVITY_TIMEOUT;
        if (paramStr != null) {
            try {
                final long parameterValue = Long.parseLong(paramStr);
                inactivityTimeout = Math.abs(parameterValue);
            } catch (NumberFormatException e) {
                externalContext.log("Invalid value specified for context parameter named " + SUBMISSION_AJAX_INACTIVITY_TIMEOUT_CONTEXT_PARAM
                        + ": it must be a number");
            }
        }

        final ScriptBuilder script = new ScriptBuilder("O$.setSubmissionAjaxInactivityTimeout(" + inactivityTimeout + ");");
        boolean isAjax4jsfRequest = AjaxUtil.isAjax4jsfRequest();
        boolean isPortletRequest = AjaxUtil.isPortletRequest(context);

        if (isAjax4jsfRequest || isPortletRequest) {
            Rendering.appendUniqueRTLibraryScripts(context, script);
        } else {
            Rendering.appendOnLoadScript(context, script);
        }

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


    public static String processAjaxExecutePhase(FacesContext context, RequestFacade request, UIViewRoot viewRoot) {
        String listener = request.getParameter(PARAM_ACTION_LISTENER);
        String action = request.getParameter(PARAM_ACTION);
        String actionComponentId = request.getParameter(PARAM_ACTION_COMPONENT);
        if (listener != null || action != null) {
            ELContext elContext = context.getELContext();
            UIComponent component = null;
            List<Runnable> restoreDataPointerRunnables = new ArrayList<Runnable>();
            if (actionComponentId != null)
                component = findComponentById(viewRoot, actionComponentId, true, false, false, restoreDataPointerRunnables);
            if (component == null)
                component = viewRoot;

            Object result = null;
            if (action != null) {
                MethodExpression methodBinding = context.getApplication().getExpressionFactory().createMethodExpression(
                        elContext, "#{" + action + "}", String.class, new Class[]{});
                /*result = */methodBinding.invoke(elContext, null);
            }
            if (listener != null) {
                AjaxActionEvent event = new AjaxActionEvent(component);
                event.setPhaseId(Boolean.valueOf(request.getParameter(PARAM_IMMEDIATE)) ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION);
                MethodExpression methodExpression = context.getApplication().getExpressionFactory().createMethodExpression(
                        elContext, "#{" + listener + "}", void.class, new Class[]{ActionEvent.class});
                try {
                    methodExpression.getMethodInfo(elContext);
                } catch (MethodNotFoundException e) {
                    // both actionEvent and AjaxActionEvent parameter declarations are allowed
                    methodExpression = context.getApplication().getExpressionFactory().createMethodExpression(
                        elContext, "#{" + listener + "}", void.class, new Class[]{AjaxActionEvent.class});
                }
                methodExpression.invoke(elContext, new Object[]{event});
                Object listenerResult = event.getAjaxResult();
                if (listenerResult != null)
                    result = listenerResult;
            }
            if (result != null)
                AjaxRequest.getInstance().setAjaxResult(result);
            for (Runnable restoreDataPointerRunnable : restoreDataPointerRunnables) {
                restoreDataPointerRunnable.run();
            }
        }
        return actionComponentId;
    }

    public static UIComponent findComponentById(UIComponent parent,
                                          String id,
                                          boolean preProcessDecodesOnTables,
                                          boolean preRenderResponseOnTables,
                                          boolean checkComponentPresence) {
        return findComponentById(parent, id, preProcessDecodesOnTables, preRenderResponseOnTables, checkComponentPresence, null);
    }
    public static UIComponent findComponentById(UIComponent parent,
                                          String id,
                                          boolean preProcessDecodesOnTables,
                                          boolean preRenderResponseOnTables,
                                          boolean checkComponentPresence,
                                          List<Runnable> restoreDataPointerRunnables) {
        UIComponent componentByPath = findComponentByPath(parent, id, preProcessDecodesOnTables,
                preRenderResponseOnTables, restoreDataPointerRunnables);
        if (checkComponentPresence && componentByPath == null)
            throw new FacesException("Component by id not found: " + id);
        return componentByPath;
    }

    private static UIComponent findComponentByPath(UIComponent parent,
                                            String path,
                                            boolean preProcessDecodesOnTables,
                                            boolean preRenderResponseOnTables,
                                            List<Runnable> restoreDataPointerRunnables) {
        while (true) {
            if (path == null) {
                return null;
            }

            int separator = path.indexOf(NamingContainer.SEPARATOR_CHAR, 1);
            if (separator == -1)
                return componentById(parent, path, true, preProcessDecodesOnTables,
                        preRenderResponseOnTables, restoreDataPointerRunnables);

            String id = path.substring(0, separator);
            UIComponent nextParent = componentById(parent, id, false, preProcessDecodesOnTables,
                    preRenderResponseOnTables, restoreDataPointerRunnables);
            if (nextParent == null) {
                return null;
            }
            parent = nextParent;
            path = path.substring(separator + 1);
        }
    }

    private static UIComponent componentById(UIComponent parent, String id, boolean isLastComponentInPath,
                                             boolean preProcessDecodesOnTables, boolean preRenderResponseOnTables,
                                             List<Runnable> restoreDataPointerRunnables) {
        if (id.length() > 0 && (isNumberBasedId(id) || id.startsWith(":")) && parent instanceof AbstractTable) {
            final AbstractTable table = ((AbstractTable) parent);
            if (!isLastComponentInPath) {
                if (preProcessDecodesOnTables)
                    table.invokeBeforeProcessDecodes(FacesContext.getCurrentInstance());
                if (preRenderResponseOnTables) {
                    table.invokeBeforeRenderResponse(FacesContext.getCurrentInstance());
                    table.setRowIndex(-1); // make the succeeding setRowIndex call provide the just-read actual row data through request-scope variables
                }

                int rowIndex = table.getRowIndexByClientSuffix(id);
                final int prevRowIndex = table.getRowIndex();
                if (prevRowIndex == rowIndex) {
                    // ensure row index setting will be run anew to ensure proper request-scope variable values
                    table.setRowIndex(-1);
                }
                table.setRowIndex(rowIndex);
                if (restoreDataPointerRunnables != null)
                    restoreDataPointerRunnables.add(new Runnable() {
                        public void run() {
                            table.setRowIndex(prevRowIndex);
                        }
                    });
            } else {
                int rowIndex = table.getRowIndexByClientSuffix(id);
                final int prevRowIndex = table.getRowIndex();
                if (prevRowIndex == rowIndex) {
                    // ensure row index setting will be run anew to ensure proper request-scope variable values
                    table.setRowIndex(-1);
                }
                table.setRowIndex(rowIndex);
                if (restoreDataPointerRunnables != null)
                    restoreDataPointerRunnables.add(new Runnable() {
                        public void run() {
                            table.setRowIndex(prevRowIndex);
                        }
                    });
            }
            return table;
        } else if (isNumberBasedId(id) && parent instanceof UIData) {
            final UIData grid = ((UIData) parent);
            int rowIndex = Integer.parseInt(id);
            final int prevRowIndex = grid.getRowIndex();
            grid.setRowIndex(rowIndex);
            if (restoreDataPointerRunnables != null)
                restoreDataPointerRunnables.add(new Runnable() {
                    public void run() {
                        grid.setRowIndex(prevRowIndex);
                    }
                });
            return grid;
        } else if (id.charAt(0) == ':' && parent instanceof OUIObjectIterator) {
            id = id.substring(1);
            final OUIObjectIterator iterator = (OUIObjectIterator) parent;
            final String prevObjectId = iterator.getObjectId();
            iterator.setObjectId(id);
            if (restoreDataPointerRunnables != null)
                restoreDataPointerRunnables.add(new Runnable() {
                    public void run() {
                        iterator.setObjectId(prevObjectId);
                    }
                });
            return (UIComponent) iterator;
        } else if (isNumberBasedId(id)) {
            try {
                Class clazz = Class.forName("com.sun.facelets.component.UIRepeat");
                if (clazz.isInstance(parent)) {
                    final Object uiRepeat = parent;
                    final Object prevIndex = ReflectionUtil.invokeMethod("com.sun.facelets.component.UIRepeat", "getIndex", null, null, uiRepeat);
                    ReflectionUtil.invokeMethod("com.sun.facelets.component.UIRepeat", "setIndex",
                            new Class[]{Integer.TYPE}, new Object[]{Integer.parseInt(id)}, uiRepeat);
                    if (restoreDataPointerRunnables != null)
                        restoreDataPointerRunnables.add(new Runnable() {
                            public void run() {
                                ReflectionUtil.invokeMethod("com.sun.facelets.component.UIRepeat", "setIndex",
                                        new Class[]{Integer.TYPE}, new Object[]{prevIndex}, uiRepeat);
                            }
                        });
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
                        isLastComponentInPath, preProcessDecodesOnTables, preRenderResponseOnTables,
                        restoreDataPointerRunnables);
                if (component != null)
                    return component;
            }
        }
        return null;
    }

    private static boolean isNumberBasedId(String id) {
        if (id == null || id.length() == 0)
            return false;

        char c = id.charAt(0);
        return Character.isDigit(c);
    }
}
