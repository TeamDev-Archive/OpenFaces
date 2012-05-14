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
package org.openfaces.renderkit.validation;

import org.openfaces.component.validation.ValidationProcessor;
import org.openfaces.util.NewInstanceScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.component.html.HtmlMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class HtmlMessagesRenderer extends BaseHtmlMessageRenderer {
    private static final String LAYOUT_LIST = "list";
    private static final String LAYOUT_TABLE = "table";

    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        super.encodeEnd(context, component);

        ValidationProcessor processor = ValidationProcessor.getInstance(context);
        HtmlMessages htmlMessages = (HtmlMessages) component;
        if (processor != null) {
            String styleClassName = Styles.getCSSClass(context, component, htmlMessages.getStyle(), htmlMessages.getStyleClass());

            Script clientScript = getClientScript(context, component, styleClassName);
            Rendering.renderInitScript(context, clientScript, getJavascriptLibraryUrls(context));
            boolean globalOnly = ((UIMessages) component).isGlobalOnly();
            boolean implicitEndOfPageMessages = component.getParent() == null;
            if (!globalOnly && !implicitEndOfPageMessages) {
                ValidatorUtil.renderPresentationExistsForAllInputComponents(context);
            }
        }

        renderClientMessage(context, (UIMessages) component);
    }

    private void renderClientMessage(FacesContext context, UIMessages messageComponent) throws IOException {
        String messageClientId = messageComponent.getClientId(context);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("span", messageComponent);
        writer.writeAttribute("id", messageClientId, null);
        renderMessages(context, messageComponent);
        writer.endElement("span");
        Styles.renderStyleClasses(context, messageComponent);
    }

    private Script getClientScript(FacesContext context, UIComponent messageComponent, String css) {
        String messageClientId = messageComponent.getClientId(context);
        return new ScriptBuilder().functionCall("O$.addMessagesRenderer", new NewInstanceScript("O$._MessagesRenderer",
                messageClientId,
                getTitle(messageComponent),
                isTooltip(messageComponent),
                isShowSummary(messageComponent),
                isShowDetail(messageComponent),
                getLayout(messageComponent),
                isGlobalOnly(messageComponent),
                css,
                getAdditionalStyles(messageComponent),
                getAdditionalParams(messageComponent))).semicolon();
    }


    private String[] getJavascriptLibraryUrls(FacesContext context) {
        return new String[]{
                Resources.utilJsURL(context),
                Resources.internalURL(context, "validation/messages.js"),
                ValidatorUtil.getValidatorUtilJsUrl(context)};
    }

    protected void renderMessages(FacesContext facesContext, UIMessages messages) throws IOException {
        MessagesIterator messagesIterator = new MessagesIterator(facesContext, isGlobalOnly(messages));

        if (messagesIterator.hasNext()) {
            String layout = getLayout(messages);
            if (layout == null || LAYOUT_LIST.equalsIgnoreCase(layout)) {
                renderList(facesContext, messages, messagesIterator);
            } else if (LAYOUT_TABLE.equalsIgnoreCase(layout)) {
                renderTable(facesContext, messages, messagesIterator);
            } else {
                // todo: is any warning needed here?
                renderList(facesContext, messages, messagesIterator);
            }
        }
    }

    private void renderList(FacesContext facesContext,
                            UIMessages messages,
                            MessagesIterator messagesIterator)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("ul", messages);
        Rendering.writeIdIfNecessary(writer, messages, facesContext);

        while (messagesIterator.hasNext()) {
            FacesMessage message = messagesIterator.next();
            if (message.isRendered() && !(messages != null && messages.isRedisplay()))
                continue;
            writer.startElement("li", messages);
            renderSingleFacesMessage(facesContext, messages, message);
            writer.endElement("li");
        }

        writer.endElement("ul");
    }

    private void renderTable(FacesContext facesContext,
                             UIMessages messages,
                             MessagesIterator messagesIterator)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement("table", messages);
        Rendering.writeIdIfNecessary(writer, messages, facesContext);

        while (messagesIterator.hasNext()) {
            FacesMessage message = messagesIterator.next();
            if (message.isRendered() && !(messages != null && messages.isRedisplay()))
                continue;

            writer.startElement("tr", messages);
            writer.startElement("td", messages);
            renderSingleFacesMessage(facesContext, messages, message);

            writer.endElement("td");
            writer.endElement("tr");
        }

        writer.endElement("table");
    }

    protected String[] getStyleAndStyleClass(UIComponent messages,
                                             FacesMessage.Severity severity) {
        String style = null;
        String styleClass = null;
        if (messages instanceof HtmlMessages) {
            if (severity == FacesMessage.SEVERITY_INFO) {
                style = ((HtmlMessages) messages).getInfoStyle();
                styleClass = ((HtmlMessages) messages).getInfoClass();
            } else if (severity == FacesMessage.SEVERITY_WARN) {
                style = ((HtmlMessages) messages).getWarnStyle();
                styleClass = ((HtmlMessages) messages).getWarnClass();
            } else if (severity == FacesMessage.SEVERITY_ERROR) {
                style = ((HtmlMessages) messages).getErrorStyle();
                styleClass = ((HtmlMessages) messages).getErrorClass();
            } else if (severity == FacesMessage.SEVERITY_FATAL) {
                style = ((HtmlMessages) messages).getFatalStyle();
                styleClass = ((HtmlMessages) messages).getFatalClass();
            }

            if (style == null) {
                style = ((HtmlMessages) messages).getStyle();
            }

            if (styleClass == null) {
                styleClass = ((HtmlMessages) messages).getStyleClass();
            }

            Styles.checkCSSStyleForSemicolon(style);
        }

        return new String[]{style, styleClass};
    }

    protected String getTitle(UIComponent component) {
        if (component instanceof HtmlMessages) {
            return ((HtmlMessages) component).getTitle();
        }
        return null;
    }

    protected boolean isTooltip(UIComponent component) {
        return !(component instanceof HtmlMessages) || ((HtmlMessages) component).isTooltip();
    }

    protected boolean isShowSummary(UIComponent component) {
        return !(component instanceof UIMessages) || ((UIMessages) component).isShowSummary();
    }

    protected boolean isShowDetail(UIComponent component) {
        return component instanceof UIMessages && ((UIMessages) component).isShowDetail();
    }

    protected boolean isGlobalOnly(UIComponent component) {
        return !(component instanceof UIMessages) || ((UIMessages) component).isGlobalOnly();
    }

    protected String getLayout(UIComponent component) {
        if (component instanceof HtmlMessages) {
            return ((HtmlMessages) component).getLayout();
        }
        return LAYOUT_LIST;
    }


    private static class MessagesIterator implements Iterator<FacesMessage> {
        private FacesContext facesContext;
        private Iterator<FacesMessage> globalMessagesIterator;
        private Iterator<String> clientIdsWithMessagesIterator;
        private Iterator<FacesMessage> componentMessagesIterator;
        private String clientId;

        public MessagesIterator(FacesContext facesContext, boolean globalOnly) {
            this.facesContext = facesContext;
            globalMessagesIterator = facesContext.getMessages(null);
            if (globalOnly) {
                clientIdsWithMessagesIterator = Collections.<String>emptyList().iterator();
            } else {
                Iterator<String> idsWithMessages = facesContext.getClientIdsWithMessages();
                Set<String> uniqueIds = new LinkedHashSet<String>();
                while (idsWithMessages.hasNext()) {
                    String id = idsWithMessages.next();
                    if (id != null)
                        uniqueIds.add(id);
                }
                clientIdsWithMessagesIterator = uniqueIds.iterator();
            }

            componentMessagesIterator = null;
            clientId = null;
        }

        public boolean hasNext() {
            return globalMessagesIterator.hasNext() ||
                    clientIdsWithMessagesIterator.hasNext() ||
                    (componentMessagesIterator != null && componentMessagesIterator.hasNext());
        }

        public FacesMessage next() {
            if (globalMessagesIterator.hasNext()) {
                return globalMessagesIterator.next();
            } else if (componentMessagesIterator != null && componentMessagesIterator.hasNext()) {
                return componentMessagesIterator.next();
            } else {
                clientId = clientIdsWithMessagesIterator.next();
                componentMessagesIterator = facesContext.getMessages(clientId);
                return componentMessagesIterator.next();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
        }

        public String getClientId() {
            return clientId;
        }
    }
}
