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

package org.openfaces.component.util;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;
import javax.faces.event.BehaviorEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class ActionHelper extends OUIClientActionHelper implements ClientBehavior {
    private static final String EXPRESSION_PREFIX = "#{";
    private static final String EXPRESSION_SUFFIX = "}";

    private Action action;

    public ActionHelper(Action action) {
        this.action = action;
    }

    public String getScript(ClientBehaviorContext behaviorContext) {
        return getClientActionScript(behaviorContext.getFacesContext(), action);
    }

    public void decode(FacesContext context, UIComponent component) {
    }

    public Set<ClientBehaviorHint> getHints() {
        return Collections.singleton(ClientBehaviorHint.SUBMITTING);
    }

    public void broadcast(BehaviorEvent event) {
    }


    protected String getClientActionScript(FacesContext context, OUIClientAction ouiClientAction) {
        Action action = (Action) ouiClientAction;

        if (action.isDisabled()) {
            return null;
        }
        ScriptBuilder buf = new ScriptBuilder();

        buf.functionCall("O$._submitAction", action.getId(), extractActionStr(action, "action"), extractActionStr(action, "listener"));
        return buf.toString();
    }

    public static String extractActionStr(Action action, String attributeName) {
        String expressionString = getExpressionString(action, attributeName);
        if (expressionString == null) return null;
        validateExpressionString(expressionString);
        String extractedStr = expressionString.substring(
                EXPRESSION_PREFIX.length(), expressionString.length() - EXPRESSION_SUFFIX.length());
        return extractedStr;
    }

    private static String getExpressionString(Action action, String attributeName) {
        if ("action".equals(attributeName)) {
            MethodExpression actionExpression = action.getActionExpression();
            if (actionExpression == null) return null;
            return actionExpression.getExpressionString();
        }
        ValueExpression valueExpression = action.getValueExpression(attributeName);
        if (valueExpression == null) return null;
        String expressionString = valueExpression.getExpressionString();
        return expressionString;

    }

    private static void validateExpressionString(String actionExpressionString) {
        if (actionExpressionString.length() < 4 ||
                !actionExpressionString.startsWith(EXPRESSION_PREFIX) || !actionExpressionString.endsWith(EXPRESSION_SUFFIX))
            throw new FacesException("Action string should be formatted as #{...}, but the following was encountered: " +
                    actionExpressionString);
    }


    @Override
    protected void renderResources(FacesContext context) {
        try {
            Resources.renderJSLinkIfNeeded(context, Resources.utilJsURL(context));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
