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
package org.openfaces.renderkit.ajax;

import org.openfaces.component.ComponentConfigurator;
import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.component.ajax.Ajax;
import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.org.json.JSONArray;
import org.openfaces.renderkit.OUIClientActionRendererHelper;
import org.openfaces.util.Components;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class AjaxRendererHelper extends OUIClientActionRendererHelper {

    protected void encodeAdditionalScript(FacesContext context, ScriptBuilder javaScript, OUIClientAction clientAction) {
        Ajax ajax = (Ajax) clientAction;
        appendMissingParameters(context, ajax, javaScript);
    }

    protected String getClientActionScript(FacesContext context, OUIClientAction clientAction) {
        Ajax ajax = (Ajax) clientAction;
        AjaxInitializer initializer = new AjaxInitializer();
        ScriptBuilder script = new ScriptBuilder();
        script.functionCall("O$._ajaxReload",
                initializer.getRenderArray(context, ajax, ajax.getRender()),
                initializer.getAjaxParams(context, ajax)).semicolon();
        if (isDisableDefaultRequired(ajax))
            script.append("return false;");
        return script.toString();
    }

    private static boolean isDisableDefaultRequired(Ajax ajax) {
        String _for = ajax.getFor();
        if (_for != null) {
            UIComponent referredComponent = Components.referenceIdToComponent(ajax, _for);
            return isCommandComponent(referredComponent);
        }
        if (!ajax.isStandalone()) {
            UIComponent parent = ajax.getParent();
            if (parent instanceof ComponentConfigurator) {
                parent = ((ComponentConfigurator) parent).getConfiguredComponent();
            }
            return isCommandComponent(parent);
        }
        return false;
    }

    private static boolean isCommandComponent(UIComponent component) {
        return component instanceof HtmlCommandButton || component instanceof HtmlCommandLink;
    }

    protected void appendMissingParameters(FacesContext context, Ajax ajax, ScriptBuilder script) {
        UIComponent parent = ajax.getParent();
        if (parent instanceof ComponentConfigurator) {
            parent = ((ComponentConfigurator) parent).getConfiguredComponent();
        }
        if (!isCommandComponent(parent)) {
            String ajaxComponentId = ajax.getId();
            script.append("if (!O$._renderIds) {O$._renderIds = []};O$._renderIds['").append(ajaxComponentId).append("'] = ");
            AjaxInitializer initializer = new AjaxInitializer();
            JSONArray idsArray = initializer.getRenderArray(context, ajax, ajax.getRender());
            script.append(idsArray);
            script.append(";");

            script.append("if (!O$._executeIds) {O$._executeIds = []};O$._executeIds['").append(ajaxComponentId).append("'] = ");
            JSONArray submittedIdsArray = initializer.getRenderArray(context, ajax, ajax.getExecute());
            if (!ajax.isStandalone() && ajax.getSubmitInvoker()) {
                String invokerId = OUIClientActionHelper.getClientActionInvoker(context, ajax);
                if (context.getViewRoot().findComponent(":" + invokerId) != null) {
                    // if invoker is a JSF component rather than raw HTML tag
                    submittedIdsArray.put(invokerId);
                }
            }
            script.append(submittedIdsArray);
            script.append(";");

            script.append("if (!O$._actionIds) {O$._actionIds = []};O$._actionIds['").append(ajaxComponentId).append("'] = '");
            script.append(ajax.getClientId(context));
            script.append("';");

        }
    }

}
