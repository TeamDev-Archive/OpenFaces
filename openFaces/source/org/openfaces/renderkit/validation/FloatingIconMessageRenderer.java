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
package org.openfaces.renderkit.validation;

import org.openfaces.component.validation.ClientValidationMode;
import org.openfaces.component.validation.FloatingIconMessage;
import org.openfaces.component.validation.ValidationProcessor;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.Components;
import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;
import org.openfaces.validator.ClientValidatorUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Vladimir Korenev
 */
public class FloatingIconMessageRenderer extends BaseMessageRenderer {
    private static final String DEFAULT_CLASS = "o_floatingIconMessage";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        if (!component.isRendered())
            return;

        FloatingIconMessage fim = (FloatingIconMessage) component;

        UIComponent forComponent = getForComponent(fim);
        boolean pageDefinedMessage = !fim.isRuntimeDefined();
        ValidationProcessor validationProcessor = ValidationProcessor.getInstance(context);
        if (validationProcessor == null)
            return;


        ClientValidationMode cv = validationProcessor.getClientValidationRule(fim, forComponent);
        UIForm form = Components.getEnclosingForm(fim);
        boolean clientValidation = !cv.equals(ClientValidationMode.OFF);
        boolean useDCVP = validationProcessor.isUseDefaultClientValidationPresentationForForm(form);
        boolean useDSVP = validationProcessor.isUseDefaultServerValidationPresentationForForm(form);

        String forComponentClientId = getForComponentClientId(context, fim);
        if (forComponentClientId == null) {
            Rendering.logWarning(context, "Cannot render floatingIconMessage bacause can't calculate " +
                    "target component client ID. It may be caused by 'for' attribute absence");
            return;
        }
        Script clientScript = getClientScript(context, fim, forComponentClientId,
                clientValidation, pageDefinedMessage, useDCVP, useDSVP);
        Rendering.renderInitScript(context, clientScript, getJavascriptLibraryUrls(context));
        if (clientScript.toString().length() > 0) {
            if (!isDefaultPresentation(fim))
                ValidatorUtil.renderPresentationExistsForComponent(forComponentClientId, context);
            Styles.renderStyleClasses(context, fim, true, false);
        }
    }

    private Script getClientScript(FacesContext context,
                                   FloatingIconMessage fim,
                                   String forComponentClientId,
                                   boolean clientValidation,
                                   boolean pageDefinedMessage,
                                   boolean useDefaultClientPresentation,
                                   boolean useDefaultServerPresentation) {
        if (forComponentClientId == null) {
            throw new NullPointerException("forComponentClientId");
        }
        ScriptBuilder resultScript = new ScriptBuilder();
        Iterator messages = context.getMessages(forComponentClientId);
        FacesMessage message = null;

        if (messages.hasNext()) {
            message = (FacesMessage) messages.next();
        }

        String css = Styles.getCSSClass(context, fim,
                !fim.isNoStyle() ? fim.getStyle() : null, DEFAULT_CLASS,
                !fim.isNoStyle() ? fim.getStyleClass() : null);
        JSONObject events = Rendering.getEventsParam(fim, "onclick", "ondblclick", "onmousedown", "onmouseover", "onmousemove", "onmouseout", "onmouseup");

        ScriptBuilder clientValidationScript = new ScriptBuilder().functionCall("O$.addClientMessageRenderer",
                new NewInstanceScript("O$._FloatingIconMessageRenderer",
                        fim,
                        forComponentClientId,
                        getImageUrl(fim),
                        getOffsetTop(fim),
                        getOffsetLeft(fim),
                        css,
                        events,
                        fim.isNoImage(),
                        fim.isShowSummary(),
                        fim.isShowDetail(),
                        isDefaultPresentation(fim))).semicolon();

        ScriptBuilder serverValidationScript = new ScriptBuilder();
        if (message != null) {
            serverValidationScript.onLoadScript(new ScriptBuilder().append(
                    ClientValidatorUtil.getScriptAddMessageById(message, forComponentClientId)).

                    newInstance("O$._FloatingIconMessageRenderer",
                            fim,
                            forComponentClientId,
                            getImageUrl(fim),
                            getOffsetTop(fim),
                            getOffsetLeft(fim),
                            css,
                            events,
                            fim.isNoImage(),
                            fim.isShowSummary(),
                            fim.isShowDetail(),
                            isDefaultPresentation(fim)).append(".update();\n"));
        }

        if (pageDefinedMessage) {
            if (clientValidation) {
                resultScript = new ScriptBuilder(clientValidationScript);
            }
            if (message != null) {
                resultScript.append(serverValidationScript);
            }
        } else {
            if (clientValidation && useDefaultClientPresentation) {
                resultScript = new ScriptBuilder(clientValidationScript);
            }
            if (useDefaultServerPresentation) {
                if (message != null) {
                    resultScript.append(serverValidationScript);
                } else {
                    if (clientValidation) {
                        resultScript = new ScriptBuilder(clientValidationScript);
                    }
                }
            }
        }
        return resultScript;
    }

    private String[] getJavascriptLibraryUrls(FacesContext context) {
        return new String[]{
                Resources.getUtilJsURL(context),
                Resources.getInternalURL(context, this.getClass(), "floatingIconMessage.js"),
                ValidatorUtil.getValidatorUtilJsUrl(context)
        };
    }

    protected String getImageUrl(UIComponent component) {
        if (component instanceof FloatingIconMessage) {
            return ((FloatingIconMessage) component).getImageUrl();
        } else {
            return (String) component.getAttributes().get("imageUrl");
        }
    }


    protected int getOffsetTop(UIComponent component) {
        if (component instanceof FloatingIconMessage) {
            return ((FloatingIconMessage) component).getOffsetTop();
        } else {
            Number n = (Number) component.getAttributes().get("offsetTop");
            return n.intValue();
        }
    }

    protected int getOffsetLeft(UIComponent component) {
        if (component instanceof FloatingIconMessage) {
            return ((FloatingIconMessage) component).getOffsetLeft();
        } else {
            Number n = (Number) component.getAttributes().get("offsetLeft");
            return n.intValue();
        }
    }
}
