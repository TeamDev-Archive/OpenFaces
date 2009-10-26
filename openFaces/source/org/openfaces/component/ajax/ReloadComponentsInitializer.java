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
import java.util.ArrayList;
import java.util.List;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 * 
 * @author Dmitry Pikhulya
 */
public class ReloadComponentsInitializer {
    private static final String EXPRESSION_PREFIX = "#{";
    private static final String EXPRESSION_SUFFIX = "}";

    public JSONArray getComponentIdsArray(FacesContext context, OUIClientAction action, List<String> componentIds) {
        JSONArray idsArray = new JSONArray();
        if (componentIds != null)
            for (String componentId : componentIds) {
                if (componentId.startsWith(":")) {
                    idsArray.put(componentId.substring(1));
                    continue;
                }
                UIComponent component = ((UIComponent) action).findComponent(componentId);
                if (component == null) {
                    throw new FacesException("<o:reloadComponents> couldn't find component by relative id: \"" + componentId + "\"; consider using absolute component id, e.g. \":formId:componentId\"");
//                    idsArray.put(componentId);
//                    continue;
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


    public JSONObject getReloadParams(FacesContext context, ReloadComponents reloadComponents) {
        try {
            JSONObject result = new JSONObject();
            List<String> submittedComponentIds = reloadComponents.getSubmittedComponentIds();
            if (submittedComponentIds == null)
                submittedComponentIds = new ArrayList<String>();
            if (submittedComponentIds.size() > 0 || (!reloadComponents.isStandalone() && reloadComponents.getSubmitInvoker())) {
                result.put("submittedComponentIds", getSubmittedComponentIdsParam(context, reloadComponents, submittedComponentIds));
            }
            String onajaxstart = reloadComponents.getOnajaxstart();
            if (onajaxstart != null && onajaxstart.length() != 0) {
                result.put("onajaxstart", new AnonymousFunction(onajaxstart, "event"));
            }
            String onajaxend = reloadComponents.getOnajaxend();
            if (onajaxend != null && onajaxend.length() != 0) {
                result.put("onajaxend", new AnonymousFunction(onajaxend, "event"));
            }
            String onerror = reloadComponents.getOnerror();
            if (onerror != null && onerror.length() != 0) {
                result.put("onerror", new AnonymousFunction(onerror, "event"));
            }

            ValueExpression action = reloadComponents.getValueExpression("action");
            if (action != null) {
                String actionExpressionString = action.getExpressionString();
                validateExpressionString(actionExpressionString);
                result.put("action", actionExpressionString.substring(
                        EXPRESSION_PREFIX.length(), actionExpressionString.length() - EXPRESSION_SUFFIX.length()));
                String invokerId = OUIClientActionHelper.getClientActionInvoker(context, reloadComponents);
                result.put("actionSourceId", invokerId); 
            }
            int requestDelay = reloadComponents.getRequestDelay();
            if (requestDelay > 0) {
                result.put("requestDelay", requestDelay);
                result.put("requestDelayId", getReloadComponentsIdParam(context, reloadComponents));
            }
            ValueExpression actionListener = reloadComponents.getValueExpression("actionListener");
            if (actionListener != null) {
                String actionListenerExpressionString = actionListener.getExpressionString();
                validateExpressionString(actionListenerExpressionString);
                result.put("actionListener", actionListenerExpressionString.substring(EXPRESSION_PREFIX.length(),
                        actionListenerExpressionString.length() - EXPRESSION_SUFFIX.length()));
                result.put("actionComponent", getReloadComponentsIdParam(context, reloadComponents));
            }

            result.put("immediate", reloadComponents.isImmediate());
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

    protected Object getSubmittedComponentIdsParam(FacesContext context,
                                                   ReloadComponents reloadComponents,
                                                   List<String> submittedComponentIds) {
        JSONArray componentIdsArray = getComponentIdsArray(context, reloadComponents, submittedComponentIds);
        if (!reloadComponents.isStandalone() && reloadComponents.getSubmitInvoker()) {
            String invokerId = OUIClientActionHelper.getClientActionInvoker(context, reloadComponents);
            if (context.getViewRoot().findComponent(":" + invokerId) != null) {
                // if invoker is a JSF component rather than raw HTML tag
                componentIdsArray.put(invokerId);
            }
        }
        return componentIdsArray;
    }

    protected Object getReloadComponentsIdParam(FacesContext context, ReloadComponents reloadComponents) {
        return reloadComponents.getClientId(context);
    }
}
