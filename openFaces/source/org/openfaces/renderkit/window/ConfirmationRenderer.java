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
package org.openfaces.renderkit.window;

import org.openfaces.component.window.AbstractWindow;
import org.openfaces.component.window.Confirmation;
import org.openfaces.component.window.PopupLayer;
import org.openfaces.util.Components;
import org.openfaces.util.HTML;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Andrew Palval
 */
public class ConfirmationRenderer extends AbstractWindowRenderer {
    public static final String JS_SCRIPT_URL = "confirmation.js";

    private static final String ICON_FACET_NAME = "icon";
    private static final String MESSAGE_FACET_NAME = "message";
    private static final String DETAILS_FACET_NAME = "details";

    public static final String ICON_AREA_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + ICON_FACET_NAME;
    public static final String MESSAGE_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "headerText";
    public static final String DETAILS_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "detailsText";
    public static final String OK_BUTTON_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "yes_button";
    public static final String CANCEL_BUTTON_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "no_button";
    public static final String BUTTON_AREA_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "buttonArea";

    private static final String DEFAULT_ICON_BACKPLANE_CLASS = "o_confirmation_icon_backplane";
    private static final String DEFAULT_TEXT_BACKPLANE_CLASS = "o_confirmation_text_backplane";
    private static final String DEFAULT_HEADER_TEXT_CLASS = "o_confirmation_header_text";
    private static final String DEFAULT_DETAILS_TEXT_CLASS = "o_confirmation_details";
    private static final String DEFAULT_DETAILS_TEXT_MESSAGE_CLASS = "o_confirmation_details_message";
    private static final String DEFAULT_BUTTON_BACKPLANE_CLASS = "o_confirmation_button_backplane";
    private static final String DEFAULT_YES_BUTTON_CLASS = "o_confirmation_yes_button";
    private static final String DEFAULT_NO_BUTTON_CLASS = "o_confirmation_no_button";
    private static final String DEFAULT_ROLLOVER_BUTTON_CLASS = "";

    @Override
    protected boolean getForceRenderCaptionIfNotSpecified() {
        return false;
    }

    @Override
    protected void encodeFooterPane(ResponseWriter writer, AbstractWindow abstractWindow, String clientId) throws IOException {
        writer.startElement("tr", abstractWindow);
        writer.writeAttribute("id", clientId + "::footerRow", null);
        writer.writeAttribute("style", "height: 1px;", null);
        writer.startElement("td", abstractWindow);

        encodeOkCancelButtons(writer, (Confirmation) abstractWindow, clientId);

        writer.endElement("td");
        writer.endElement("tr");
    }

    @Override
    protected void encodeContentPane(FacesContext context, AbstractWindow abstractWindow) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = abstractWindow.getClientId(context);

        writer.startElement("table", abstractWindow);
        writer.writeAttribute("id", clientId + MIDDLE_AREA_SUFFIX, null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);

        if (abstractWindow.getWidth() != null && abstractWindow.getWidth().length() != 0) {
            writer.writeAttribute("width", "100%", null);
        }
        writer.writeAttribute("style", "height: 100%;", null);

        writer.startElement("tr", abstractWindow);

        Confirmation confirmation = (Confirmation) abstractWindow;
        encodeIcon(confirmation, context);

        boolean hasMessage = encodeMessage(confirmation, writer, clientId);

        encodeMessageDetails(confirmation, hasMessage, writer, clientId);

        writer.endElement("tr");
        writer.endElement("table");
    }

    private void encodeMessageDetails(Confirmation confirmation, boolean hasMessage,
                                      ResponseWriter writer, String clientId) throws IOException {
        if (hasMessage) {
            writer.endElement("tr");
            writer.startElement("tr", confirmation);
            writer.startElement("td", confirmation);
            writer.write(HTML.NBSP_ENTITY);
            writer.endElement("td");
        }

        writer.startElement("td", confirmation);
        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("valign", "middle", null);
        writer.writeAttribute("id", clientId + DETAILS_SUFFIX, null);

        UIComponent detailsFacet = confirmation.getFacet(DETAILS_FACET_NAME);
        if (detailsFacet != null) {
            detailsFacet.encodeAll(FacesContext.getCurrentInstance());
        } else {
            String messageDetailsText = confirmation.getDetails();
            writer.writeText(messageDetailsText, null);
        }

        writer.endElement("td");
    }

    private boolean encodeMessage(Confirmation confirmation,
                                  ResponseWriter writer, String clientId) throws IOException {
        String messageHeaderText = confirmation.getMessage();
        boolean hasMessage = messageHeaderText != null && messageHeaderText.length() > 0;
        if (hasMessage) {
            writer.startElement("td", confirmation);
            writer.writeAttribute("align", "center", null);
            writer.writeAttribute("valign", "middle", null);
            writer.writeAttribute("id", clientId + MESSAGE_SUFFIX, null);

            UIComponent messageFacet = confirmation.getFacet(MESSAGE_FACET_NAME);
            if (messageFacet != null) {
                messageFacet.encodeAll(FacesContext.getCurrentInstance());
            } else {
                writer.writeText(messageHeaderText, null);
            }

            writer.endElement("td");
        }
        return hasMessage;
    }

    private void encodeIcon(Confirmation confirmation, FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = confirmation.getClientId(context);
        writer.startElement("td", confirmation);

        if (confirmation.getShowMessageIcon()) {
            UIComponent iconFacet = confirmation.getFacet(ICON_FACET_NAME);
            if (iconFacet != null) {
                iconFacet.encodeAll(FacesContext.getCurrentInstance());
            } else {
                String iconPath = confirmation.getMessageIconUrl();
                String iconUrl = Resources.getURL(context, iconPath, ConfirmationRenderer.class, "warnIcon.gif");

                writer.writeAttribute("width", "1%", null);
                writer.writeAttribute("id", clientId + ICON_AREA_SUFFIX, null);
//      writer.writeAttribute("rowspan", "2", null);
                writer.startElement("img", confirmation);
                writer.writeAttribute("src", iconUrl, null);
                writer.endElement("img");
            }
        } else {
            writer.write(HTML.NBSP_ENTITY);
        }

        writer.endElement("td");
    }

    private void encodeOkCancelButtons(ResponseWriter writer, Confirmation confirmation, String clientId) throws IOException {
        writer.writeAttribute("id", clientId + BUTTON_AREA_SUFFIX, null);
        writer.writeAttribute("align", "center", null);

        writer.startElement("table", confirmation);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "3", null);
        writer.startElement("tr", confirmation);
        writer.startElement("td", confirmation);
        writer.writeAttribute("width", "50%", null);

        writer.startElement("input", confirmation);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("id", clientId + OK_BUTTON_SUFFIX, null);
        writer.writeAttribute("value", confirmation.getOkButtonText(), null);
        writer.endElement("input");

        writer.endElement("td");
        writer.startElement("td", confirmation);
        writer.writeAttribute("width", "50%", null);

        writer.startElement("input", confirmation);
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute("id", clientId + CANCEL_BUTTON_SUFFIX, null);
        writer.writeAttribute("value", confirmation.getCancelButtonText(), null);
        writer.endElement("input");

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    @Override
    protected String getDefaultClassName() {
        return Styles.mergeClassNames(super.getDefaultClassName(), "o_confirmation");
    }

    protected boolean isResizableByDefault() {
        return false;
    }

    @Override
    protected void encodeScriptsAndStyles(FacesContext context, PopupLayer component) throws IOException {
        super.encodeScriptsAndStyles(context, component);

        Confirmation confirmation = (Confirmation) component;

        String invokerId;
        String aFor = confirmation.getFor();
        if (aFor != null)
            invokerId = Components.referenceIdToClientId(context, confirmation, aFor);
        else if (!confirmation.isStandalone())
            invokerId = confirmation.getParent().getClientId(context);
        else
            invokerId = null;

        String iconAreaStyle = Styles.getCSSClass(context, component, confirmation.getIconAreaStyle(),
                DEFAULT_ICON_BACKPLANE_CLASS, confirmation.getIconAreaClass());
        String rolloverIconAreaStyle = Styles.getCSSClass(context, component, confirmation.getRolloverIconAreaStyle(),
                StyleGroup.rolloverStyleGroup(), confirmation.getRolloverIconAreaClass());
        rolloverIconAreaStyle = Styles.mergeClassNames(iconAreaStyle, rolloverIconAreaStyle);

        String textAreaStyle = Styles.getCSSClass(context, component, confirmation.getContentStyle(), DEFAULT_TEXT_BACKPLANE_CLASS, confirmation.getContentClass());
        String rolloverTextAreaStyle = Styles.getCSSClass(context, component, confirmation.getRolloverContentStyle(), StyleGroup.rolloverStyleGroup(), confirmation.getRolloverContentClass());
        rolloverTextAreaStyle = Styles.mergeClassNames(textAreaStyle, rolloverTextAreaStyle);

        String headerTextStyle = Styles.getCSSClass(context, component, confirmation.getMessageStyle(), DEFAULT_HEADER_TEXT_CLASS, confirmation.getMessageClass());
        String rolloverHeaderTextStyle = Styles.getCSSClass(context, component, confirmation.getRolloverMessageStyle(), StyleGroup.rolloverStyleGroup(), confirmation.getRolloverMessageClass());
        rolloverHeaderTextStyle = Styles.mergeClassNames(headerTextStyle, rolloverHeaderTextStyle);

        String messageHeaderText = confirmation.getMessage();
        boolean hasMessage = messageHeaderText != null && messageHeaderText.length() > 0;
        String defaultDetailsTextClass = hasMessage ? DEFAULT_DETAILS_TEXT_MESSAGE_CLASS : DEFAULT_DETAILS_TEXT_CLASS;
        String detailsTextStyle = Styles.getCSSClass(context, component, confirmation.getDetailsStyle(), defaultDetailsTextClass, confirmation.getDetailsClass());
        String rolloverDetailsTextStyle = Styles.getCSSClass(context, component, confirmation.getRolloverDetailsStyle(), StyleGroup.rolloverStyleGroup(), confirmation.getRolloverDetailsClass());
        rolloverDetailsTextStyle = Styles.mergeClassNames(detailsTextStyle, rolloverDetailsTextStyle);

        String buttonAreaStyle = Styles.getCSSClass(context, component, confirmation.getButtonAreaStyle(), DEFAULT_BUTTON_BACKPLANE_CLASS, confirmation.getButtonAreaClass());
        String rolloverButtonAreaStyle = Styles.getCSSClass(context, component, confirmation.getRolloverButtonAreaStyle(), StyleGroup.rolloverStyleGroup(), confirmation.getRolloverButtonAreaClass());
        rolloverButtonAreaStyle = Styles.mergeClassNames(buttonAreaStyle, rolloverButtonAreaStyle);

        String okButtonStyle = Styles.getCSSClass(context, component, confirmation.getOkButtonStyle(), DEFAULT_YES_BUTTON_CLASS, confirmation.getOkButtonClass());
        String rolloverOkButtonStyle = Styles.getCSSClass(context, component, confirmation.getRolloverOkButtonStyle(), StyleGroup.rolloverStyleGroup(), confirmation.getRolloverOkButtonClass(), DEFAULT_ROLLOVER_BUTTON_CLASS);
        rolloverOkButtonStyle = Styles.mergeClassNames(okButtonStyle, rolloverOkButtonStyle);

        String cancelButtonStyle = Styles.getCSSClass(context, component, confirmation.getCancelButtonStyle(), DEFAULT_NO_BUTTON_CLASS, confirmation.getCancelButtonClass());
        String rolloverCancelButtonStyle = Styles.getCSSClass(context, component, confirmation.getRolloverCancelButtonStyle(), StyleGroup.rolloverStyleGroup(), confirmation.getRolloverCancelButtonClass(), DEFAULT_ROLLOVER_BUTTON_CLASS);
        rolloverCancelButtonStyle = Styles.mergeClassNames(cancelButtonStyle, rolloverCancelButtonStyle);

        ScriptBuilder sb = new ScriptBuilder();
        String event = Rendering.getEventWithOnPrefix(context, confirmation, "o:confirmation");

        sb.initScript(context, confirmation, "O$.Confirmation._init",
                invokerId,
                event,
                confirmation.getDefaultButton(),
                confirmation.getAlignToInvoker(),
                new Object[]{
                        iconAreaStyle,
                        rolloverIconAreaStyle,
                        textAreaStyle,
                        rolloverTextAreaStyle,
                        headerTextStyle,
                        rolloverHeaderTextStyle,
                        detailsTextStyle,
                        rolloverDetailsTextStyle,
                        buttonAreaStyle,
                        rolloverButtonAreaStyle,
                        okButtonStyle,
                        rolloverOkButtonStyle,
                        cancelButtonStyle,
                        rolloverCancelButtonStyle}
        );

        Rendering.renderInitScript(context, sb,
                Resources.getInternalURL(context, ConfirmationRenderer.class, ConfirmationRenderer.JS_SCRIPT_URL));
    }

    @Override
    protected boolean isMinimizeAllowed() {
        return false;
    }
}