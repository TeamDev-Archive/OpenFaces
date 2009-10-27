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
package org.openfaces.renderkit.ajax;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.component.ajax.ReloadComponents;
import org.openfaces.component.ajax.ReloadComponentsInitializer;
import org.openfaces.org.json.JSONArray;
import org.openfaces.renderkit.OUIClientActionRendererHelper;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class ReloadComponentsRendererHelper extends OUIClientActionRendererHelper {

    protected void encodeAdditionalScript(FacesContext context, ScriptBuilder javaScript, OUIClientAction clientAction) {
        ReloadComponents reloadComponents = (ReloadComponents) clientAction;
        appendMissingParameters(context, reloadComponents, javaScript);
    }

    protected String getClientActionScript(FacesContext context, OUIClientAction clientAction) {
        ReloadComponents reloadComponents = (ReloadComponents) clientAction;
        ReloadComponentsInitializer initializer = new ReloadComponentsInitializer();
        ScriptBuilder script = new ScriptBuilder();
        script.functionCall("O$.reloadComponents",
                initializer.getComponentIdsArray(context, reloadComponents, reloadComponents.getComponentIds()),
                initializer.getReloadParams(context, reloadComponents)).semicolon();
        if (reloadComponents.getDisableDefault())
            script.append("return false;");
        return script.toString();
    }

    protected void appendMissingParameters(FacesContext context, ReloadComponents reloadComponents, ScriptBuilder script) {
        UIComponent parent = reloadComponents.getParent();
        if (!(parent instanceof HtmlCommandButton || parent instanceof HtmlCommandLink)) {
            String reloadComponentsId = reloadComponents.getId();
            script.append("if (!window._of_reloadComponents) {window._of_reloadComponents = []};window._of_reloadComponents['").append(reloadComponentsId).append("'] = ");
            ReloadComponentsInitializer initializer = new ReloadComponentsInitializer();
            JSONArray idsArray = initializer.getComponentIdsArray(context, reloadComponents, reloadComponents.getComponentIds());
            script.append(idsArray);
            script.append(";");

            script.append("if (!window._of_submitComponents) {window._of_submitComponents = []};window._of_submitComponents['").append(reloadComponentsId).append("'] = ");
            JSONArray submittedIdsArray = initializer.getComponentIdsArray(context, reloadComponents, reloadComponents.getSubmittedComponentIds());
            if (!reloadComponents.isStandalone() && reloadComponents.getSubmitInvoker()) {
                String invokerId = OUIClientActionHelper.getClientActionInvoker(context, reloadComponents);
                if (context.getViewRoot().findComponent(":" + invokerId) != null) {
                    // if invoker is a JSF component rather than raw HTML tag
                    submittedIdsArray.put(invokerId);
                }
            }
            script.append(submittedIdsArray);
            script.append(";");

            script.append("if (!window._of_actionComponent) {window._of_actionComponent = []};window._of_actionComponent['").append(reloadComponentsId).append("'] = '");
            script.append(reloadComponents.getClientId(context));
            script.append("';");
        }
    }

}
