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
import org.openfaces.component.validation.FloatingIconMessage;
import org.openfaces.component.validation.ValidationProcessor;
import org.openfaces.util.ComponentUtil;
import org.openfaces.util.FunctionCallScript;
import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.RenderingUtil;
import org.openfaces.util.ResourceUtil;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleUtil;

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

        FloatingIconMessage flMessage = (FloatingIconMessage) component;

        UIComponent forComponent = getForComponent(component);
        boolean pageDefinedMessage = !flMessage.isRuntimeDefined();
        ValidationProcessor validationProcessor = ValidationProcessor.getInstance(context);
        if (validationProcessor != null) {
            String styleClassName = StyleUtil.getCSSClass(context, component, flMessage.getStyle(), DEFAULT_CLASS, flMessage.getStyleClass());

            ClientValidationMode cv = validationProcessor.getClientValidationRule(component, forComponent);
            UIForm form = ComponentUtil.getEnclosingForm(component);
            boolean clientValidation = !cv.equals(ClientValidationMode.OFF);
            boolean useDCVP = validationProcessor.isUseDefaultClientValidationPresentationForForm(form);
            boolean useDSVP = validationProcessor.isUseDefaultServerValidationPresentationForForm(form);

            String forComponentClientId = getForComponentClientId(context, component);
            if (forComponentClientId == null) {
                RenderingUtil.logWarning(context, "Cannot render floatingIconMessage bacause can't calculate " +
                        "target component client ID. It may be caused by 'for' attribute absence");
                return;
            }
            String clientScript = getClientScript(context, flMessage.getClientId(context), forComponentClientId, component,
                    styleClassName, clientValidation, pageDefinedMessage, useDCVP, useDSVP);
            RenderingUtil.renderInitScript(context, clientScript, getJavascriptLibraryUrls(context));
            if (!isDefaultPresentation(component) && clientScript.length() > 0) {
                ValidatorUtil.renderPresentationExistsForComponent(forComponentClientId, context);
            }
            if (clientScript.length() > 0) {
                StyleUtil.renderStyleClasses(context, flMessage, true);
            }
        }
    }

    private String getClientScript(FacesContext context,
                                   String messageClientId,
                                   String forComponentClientId,
                                   UIComponent messageComponent,
                                   String css,
                                   boolean clientValidation,
                                   boolean pageDefinedMessage,
                                   boolean useDefaultClientPresentation,
                                   boolean useDefaultServerPresentation) {
        if (forComponentClientId == null) {
            throw new NullPointerException("forComponentClientId");
        }
        FloatingIconMessage flMessage = (FloatingIconMessage) messageComponent;
        String resultScript = "";
        Iterator messages = context.getMessages(forComponentClientId);
        FacesMessage message = null;

        if (messages.hasNext()) {
            message = (FacesMessage) messages.next();
        }

        ScriptBuilder clientValidationScript = new ScriptBuilder();
        clientValidationScript.functionCall("O$.addClientMessageRenderer", new NewInstanceScript("O$._FloatingIconMessageRenderer",
                messageClientId,
                forComponentClientId,
                getImageUrl(messageComponent),
                getOffsetTop(messageComponent),
                getOffsetLeft(messageComponent),
                css,
                flMessage.isNoImage(),
                flMessage.isShowSummary(),
                flMessage.isShowDetail(),
                isDefaultPresentation(flMessage))).semicolon();

        ScriptBuilder serverValidationScript = new ScriptBuilder();
        if (message != null) {
            serverValidationScript.onLoadScript(new ScriptBuilder().functionCall("O$.addMessage",
                    new FunctionCallScript("O$.byIdOrName", forComponentClientId),
                    message.getSummary(),
                    message.getDetail()).semicolon().

                    newInstance("O$._FloatingIconMessageRenderer",
                            messageClientId,
                            forComponentClientId,
                            getImageUrl(messageComponent),
                            getOffsetTop(messageComponent),
                            getOffsetLeft(messageComponent),
                            css,
                            flMessage.isNoImage(),
                            flMessage.isShowSummary(),
                            flMessage.isShowDetail(),
                            isDefaultPresentation(flMessage)).append(".update();\n"));
        }

        if (pageDefinedMessage) {
            if (clientValidation) {
                resultScript = clientValidationScript.toString();
            }
            if (message != null) {
                resultScript += serverValidationScript;
            }
        } else {
            if (clientValidation && useDefaultClientPresentation) {
                resultScript = clientValidationScript.toString();
            }
            if (useDefaultServerPresentation) {
                if (message != null) {
                    resultScript += serverValidationScript;
                } else {
                    if (clientValidation) {
                        resultScript = clientValidationScript.toString();
                    }
                }
            }
        }
        return resultScript;
    }

    private String[] getJavascriptLibraryUrls(FacesContext context) {
        return new String[]{
                ResourceUtil.getUtilJsURL(context),
                ResourceUtil.getInternalResourceURL(context, this.getClass(), "FloatingIconMessageRenderer.js"),
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
