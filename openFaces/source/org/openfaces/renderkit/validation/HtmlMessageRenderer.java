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
package org.openfaces.renderkit.validation;

import org.openfaces.component.validation.ClientValidationMode;
import org.openfaces.component.validation.ValidationProcessor;
import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;
import org.openfaces.renderkit.validation.ValidatorUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Vladimir Korenev
 */
public class HtmlMessageRenderer extends BaseHtmlMessageRenderer {
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        super.encodeEnd(context, component);

        if (!component.isRendered())
            return;

        renderMessage(context, component);
        String forComponentClientId = getForComponentClientId(context, component);

        UIComponent forComponent = getForComponent(component);

        ValidationProcessor validationProcessor = ValidationProcessor.getInstance(context);
        if (validationProcessor != null) {
            ClientValidationMode cv = validationProcessor.getClientValidationRule(component, forComponent);
            boolean clientValidation = !cv.equals(ClientValidationMode.OFF);
            if (clientValidation) {
                if (forComponentClientId == null) {
                    RenderingUtil.logWarning(context, "Cannot render floatingIconMessage bacause can't calculate " +
                            "target component client ID. It may be caused by 'for' attribute absence");
                    return;
                }
                UIForm form = RenderingUtil.getEnclosingForm(component);
                if (validationProcessor.isUseDefaultClientValidationPresentationForForm(form)) {
                    ValidatorUtil.renderPresentationExistsForComponent(forComponentClientId, context);
                }
                Script clientScript = getClientScript(component.getClientId(context), forComponentClientId, component);
                RenderingUtil.renderInitScript(context, clientScript, getJavascriptLibraryUrls(context, clientValidation));
                StyleUtil.renderStyleClasses(context, component);
            }
        }
    }

    private Script getClientScript(
            String messageClientId,
            String forComponentClientId,
            UIComponent messageComponent) {

        if (forComponentClientId == null) {
            throw new NullPointerException("forComponentClientId");
        }

        ScriptBuilder resultString = new ScriptBuilder();
        resultString.functionCall("O$.addClientMessageRenderer",
                new NewInstanceScript("O$._MessageRenderer",
                        messageClientId,
                        forComponentClientId,
                        getTitle(messageComponent),
                        isTooltip(messageComponent),
                        isShowSummary(messageComponent),
                        isShowDetail(messageComponent))).semicolon();
        return resultString;
    }

    private String[] getJavascriptLibraryUrls(FacesContext context, boolean clientValidation) {
        if (clientValidation)
            return new String[]{
                    ResourceUtil.getUtilJsURL(context),
                    ResourceUtil.getInternalResourceURL(context, getClass(), "MessageRenderer.js"),
                    ValidatorUtil.getValidatorUtilJsUrl(context)};
        else
            return new String[]{
                    ResourceUtil.getUtilJsURL(context),
                    ValidatorUtil.getValidatorUtilJsUrl(context)};
    }

}
