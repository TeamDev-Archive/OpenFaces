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
package org.openfaces.component.ajax;

import org.openfaces.component.ComponentConfigurator;
import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.component.OUIObjectIterator;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.AnonymousFunction;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 * 
 * @author Dmitry Pikhulya
 */
public class AjaxInitializer {
    private static final String EXPRESSION_PREFIX = "#{";
    private static final String EXPRESSION_SUFFIX = "}";

    public JSONArray getRenderArray(FacesContext context, OUIClientAction action, Iterable<String> render) {
        JSONArray idsArray = new JSONArray();
        if (render != null)
            for (String componentId : render) {
                if (componentId.startsWith(":")) {
                    idsArray.put(componentId.substring(1));
                    continue;
                }
                UIComponent component = findComponent((UIComponent) action, componentId);
                if (component == null) {
                    idsArray.put(componentId);
                    continue;
                }

                if (component instanceof UIData) {
                    UIData uiData = (UIData) component;
                    int savedRowIndex = uiData.getRowIndex();
                    uiData.setRowIndex(-1);
                    idsArray.put(component.getClientId(context));
                    uiData.setRowIndex(savedRowIndex);
                } else if (component instanceof OUIObjectIterator) {
                    OUIObjectIterator ouiObjectIterator = (OUIObjectIterator) component;
                    String savedObjectId = ouiObjectIterator.getObjectId();
                    ouiObjectIterator.setObjectId(null);
                    idsArray.put(component.getClientId(context));
                    ouiObjectIterator.setObjectId(savedObjectId);
                } else {
                    idsArray.put(component.getClientId(context));
                }
            }
        return idsArray;
    }

    private UIComponent findComponent(UIComponent base, String componentId) {
        if (componentId == null || componentId.equals(""))
        if (base == null)
            base = FacesContext.getCurrentInstance().getViewRoot();
        if (base == null)
            return null;
        UIComponent locationBase = base;
        if (locationBase instanceof ComponentConfigurator)
            locationBase = ((ComponentConfigurator) locationBase).getConfiguredComponent();
        if (locationBase == null)
            locationBase = base;
        UIComponent component = locationBase.findComponent(componentId);
        if (component == null) {
            UIComponent root;
            root = base;
            while (root.getParent() != null) {
                root = root.getParent();
            }
            return findComponentAnywhere(root, componentId);
        }
        return component;
    }

    private UIComponent findComponentAnywhere(UIComponent root, String id) {
        if (id.equals(root.getId()))
            return root;
        List<UIComponent> children = root.getChildren();
        for (UIComponent child : children) {
            UIComponent c = findComponentAnywhere(child, id);
            if (c != null)
                return c;
        }
        return null;
    }


    public JSONObject getAjaxParams(FacesContext context, Ajax ajax) {
        try {
            JSONObject result = new JSONObject();
            Iterable<String> execute = ajax.getExecute();
            if (execute.iterator().hasNext() || (!ajax.isStandalone() && ajax.getSubmitInvoker())) {
                result.put("execute", getExecuteParam(context, ajax, execute));
            }
            String onajaxstart = ajax.getOnajaxstart();
            if (onajaxstart != null && onajaxstart.length() != 0) {
                result.put("onajaxstart", new AnonymousFunction(onajaxstart, "event"));
            }
            String onajaxend = ajax.getOnajaxend();
            if (onajaxend != null && onajaxend.length() != 0) {
                result.put("onajaxend", new AnonymousFunction(onajaxend, "event"));
            }
            String onerror = ajax.getOnerror();
            if (onerror != null && onerror.length() != 0) {
                result.put("onerror", new AnonymousFunction(onerror, "event"));
            }

            ValueExpression action = ajax.getValueExpression("action");
            if (action != null) {
                String actionExpressionString = action.getExpressionString();
                validateExpressionString(actionExpressionString);
                result.put("action", actionExpressionString.substring(
                        EXPRESSION_PREFIX.length(), actionExpressionString.length() - EXPRESSION_SUFFIX.length()));
                result.put("actionSourceId", getActionSourceIdParam(context, ajax));
            }
            int delay = ajax.getDelay();
            if (delay > 0) {
                result.put("delay", delay);
                result.put("delayId", getAjaxComponentParam(context, ajax));
            }
            ValueExpression actionListener = ajax.getValueExpression("listener");
            if (actionListener != null) {
                String actionListenerExpressionString = actionListener.getExpressionString();
                validateExpressionString(actionListenerExpressionString);
                result.put("actionListener", actionListenerExpressionString.substring(EXPRESSION_PREFIX.length(),
                        actionListenerExpressionString.length() - EXPRESSION_SUFFIX.length()));
                result.put("actionComponent", getAjaxComponentParam(context, ajax));
            }

            result.put("immediate", ajax.isImmediate());
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validateExpressionString(String actionExpressionString) {
        if (actionExpressionString.length() < 4 ||
                !actionExpressionString.startsWith(EXPRESSION_PREFIX) || !actionExpressionString.endsWith(EXPRESSION_SUFFIX))
            throw new FacesException("Action string should be formatted as #{action}, but the following was encountered: " +
                    actionExpressionString);
    }

    protected Object getExecuteParam(FacesContext context,
                                                   Ajax ajax,
                                                   Iterable<String> execute) {
        JSONArray renderArray = getRenderArray(context, ajax, execute);
        if (!ajax.isStandalone() && ajax.getSubmitInvoker()) {
            String invokerId = OUIClientActionHelper.getClientActionInvoker(context, ajax);
            if (context.getViewRoot().findComponent(":" + invokerId) != null) {
                // if invoker is a JSF component rather than raw HTML tag
                renderArray.put(invokerId);
            }
        }
        return renderArray;
    }

    protected Object getAjaxComponentParam(FacesContext context, Ajax ajax) {
        return ajax.getClientId(context);
    }

    protected Object getActionSourceIdParam(FacesContext context, Ajax ajax) {
        String invokerId = OUIClientActionHelper.getClientActionInvoker(context, ajax);
        return invokerId;
    }
}
