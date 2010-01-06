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
package org.openfaces.component.window;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUIClientActionHelper;
import org.openfaces.util.ValueBindings;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import static java.lang.Boolean.valueOf;

/**
 *
 * The Confirmation component allows users to confirm or reject critical actions before
 * their execution. It is displayed over other page elements like a modal dialog and can be
 * attached to a client-side event of any component or invoked from JavaScript explicitly.
 * The Confirmation component has a lot of options to customize its appearance and provides a
 * flexible invocation mechanism.
 * 
 * @author Andrew Palval
 */
public class Confirmation extends AbstractWindow implements OUIClientAction {
    public static final String COMPONENT_TYPE = "org.openfaces.Confirmation";
    public static final String COMPONENT_FAMILY = "org.openfaces.Confirmation";

    private String _for;
    private String event;
    private Boolean standalone;

    private String message;
    private String details;
    private String okButtonText;
    private String cancelButtonText;

    private ButtonType defaultButton;

    private String messageIconUrl;
    private Boolean showMessageIcon;

    private String iconAreaStyle;
    private String rolloverIconAreaStyle;
    private String rolloverContentStyle;
    private String messageStyle;
    private String rolloverMessageStyle;
    private String detailsStyle;
    private String rolloverDetailsStyle;
    private String buttonAreaStyle;
    private String rolloverButtonAreaStyle;
    private String okButtonStyle;
    private String rolloverOkButtonStyle;
    private String cancelButtonStyle;
    private String rolloverCancelButtonStyle;

    private String iconAreaClass;
    private String rolloverIconAreaClass;
    private String rolloverContentClass;
    private String messageClass;
    private String rolloverMessageClass;
    private String detailsClass;
    private String rolloverDetailsClass;
    private String buttonAreaClass;
    private String rolloverButtonAreaClass;
    private String okButtonClass;
    private String rolloverOkButtonClass;
    private String cancelButtonClass;
    private String rolloverCancelButtonClass;

    private Boolean alignToInvoker; // todo: replace with a more generic alignment mechanism (in PopupLayer component)

    public Confirmation() {
        setRendererType("org.openfaces.ConfirmationRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public boolean isModal() {
        return true; // always modal
    }

    @Override
    public String getWidth() {
        return ValueBindings.get(this, "width", super.getWidth(), "275px");
    }

    public boolean getAlignToInvoker() {
        return ValueBindings.get(this, "alignToInvoker", alignToInvoker, false);
    }

    public void setAlignToInvoker(boolean alignToInvoker) {
        this.alignToInvoker = alignToInvoker;
    }

    public boolean getShowMessageIcon() {
        return ValueBindings.get(this, "showMessageIcon", showMessageIcon, true);
    }

    public void setShowMessageIcon(boolean showMessageIcon) {
        this.showMessageIcon = valueOf(showMessageIcon);
    }

    public String getIconAreaStyle() {
        return ValueBindings.get(this, "iconAreaStyle", iconAreaStyle);
    }

    public void setIconAreaStyle(String iconAreaStyle) {
        this.iconAreaStyle = iconAreaStyle;
    }

    public String getRolloverIconAreaStyle() {
        return ValueBindings.get(this, "rolloverIconAreaStyle", rolloverIconAreaStyle);
    }

    public void setRolloverIconAreaStyle(String rolloverIconAreaStyle) {
        this.rolloverIconAreaStyle = rolloverIconAreaStyle;
    }

    public String getIconAreaClass() {
        return ValueBindings.get(this, "iconAreaClass", iconAreaClass);
    }

    public void setIconAreaClass(String iconAreaClass) {
        this.iconAreaClass = iconAreaClass;
    }

    public String getRolloverIconAreaClass() {
        return ValueBindings.get(this, "rolloverIconAreaClass", rolloverIconAreaClass);
    }

    public void setRolloverIconAreaClass(String rolloverIconAreaClass) {
        this.rolloverIconAreaClass = rolloverIconAreaClass;
    }

    public String getMessageStyle() {
        return ValueBindings.get(this, "messageStyle", messageStyle);
    }

    public void setMessageStyle(String messageStyle) {
        this.messageStyle = messageStyle;
    }

    public String getRolloverMessageStyle() {
        return ValueBindings.get(this, "rolloverMessageStyle", rolloverMessageStyle);
    }

    public void setRolloverMessageStyle(String rolloverMessageStyle) {
        this.rolloverMessageStyle = rolloverMessageStyle;
    }

    public String getMessageClass() {
        return ValueBindings.get(this, "messageClass", messageClass);
    }

    public void setMessageClass(String messageClass) {
        this.messageClass = messageClass;
    }

    public String getRolloverMessageClass() {
        return ValueBindings.get(this, "rolloverMessageClass", rolloverMessageClass);
    }

    public void setRolloverMessageClass(String rolloverMessageClass) {
        this.rolloverMessageClass = rolloverMessageClass;
    }

    public String getDetails() {
        return ValueBindings.get(this, "details", details, "Click OK to perform the action");
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getMessageIconUrl() {
        return ValueBindings.get(this, "messageIconUrl", messageIconUrl);
    }

    public void setMessageIconUrl(String messageIconUrl) {
        this.messageIconUrl = messageIconUrl;
    }

    public String getButtonAreaStyle() {
        return ValueBindings.get(this, "buttonAreaStyle", buttonAreaStyle);
    }

    public void setButtonAreaStyle(String buttonAreaStyle) {
        this.buttonAreaStyle = buttonAreaStyle;
    }

    public String getRolloverButtonAreaStyle() {
        return ValueBindings.get(this, "rolloverButtonAreaStyle", rolloverButtonAreaStyle);
    }

    public void setRolloverButtonAreaStyle(String rolloverButtonAreaStyle) {
        this.rolloverButtonAreaStyle = rolloverButtonAreaStyle;
    }

    public String getButtonAreaClass() {
        return ValueBindings.get(this, "buttonAreaClass", buttonAreaClass);
    }

    public void setButtonAreaClass(String buttonAreaClass) {
        this.buttonAreaClass = buttonAreaClass;
    }

    public String getRolloverButtonAreaClass() {
        return ValueBindings.get(this, "rolloverButtonAreaClass", rolloverButtonAreaClass);
    }

    public void setRolloverButtonAreaClass(String rolloverButtonAreaClass) {
        this.rolloverButtonAreaClass = rolloverButtonAreaClass;
    }

    public ButtonType getDefaultButton() {
        return ValueBindings.get(this, "defaultButton", defaultButton, ButtonType.class);
    }

    public void setDefaultButton(ButtonType defaultButton) {
        this.defaultButton = defaultButton;
    }

    public String getRolloverContentStyle() {
        return ValueBindings.get(this, "rolloverContentStyle", rolloverContentStyle);
    }

    public void setRolloverContentStyle(String rolloverContentStyle) {
        this.rolloverContentStyle = rolloverContentStyle;
    }

    public String getRolloverContentClass() {
        return ValueBindings.get(this, "rolloverContentClass", rolloverContentClass);
    }

    public void setRolloverContentClass(String rolloverContentClass) {
        this.rolloverContentClass = rolloverContentClass;
    }

    public String getDetailsStyle() {
        return ValueBindings.get(this, "detailsStyle", detailsStyle);
    }

    public void setDetailsStyle(String detailsStyle) {
        this.detailsStyle = detailsStyle;
    }

    public String getRolloverDetailsStyle() {
        return ValueBindings.get(this, "rolloverDetailsStyle", rolloverDetailsStyle);
    }

    public void setRolloverDetailsStyle(String rolloverDetailsStyle) {
        this.rolloverDetailsStyle = rolloverDetailsStyle;
    }

    public String getOkButtonStyle() {
        return ValueBindings.get(this, "okButtonStyle", okButtonStyle);
    }

    public void setOkButtonStyle(String okButtonStyle) {
        this.okButtonStyle = okButtonStyle;
    }

    public String getRolloverOkButtonStyle() {
        return ValueBindings.get(this, "rolloverOkButtonStyle", rolloverOkButtonStyle);
    }

    public void setRolloverOkButtonStyle(String rolloverOkButtonStyle) {
        this.rolloverOkButtonStyle = rolloverOkButtonStyle;
    }

    public String getCancelButtonStyle() {
        return ValueBindings.get(this, "cancelButtonStyle", cancelButtonStyle);
    }

    public void setCancelButtonStyle(String cancelButtonStyle) {
        this.cancelButtonStyle = cancelButtonStyle;
    }

    public String getRolloverCancelButtonStyle() {
        return ValueBindings.get(this, "rolloverCancelButtonStyle", rolloverCancelButtonStyle);
    }

    public void setRolloverCancelButtonStyle(String rolloverCancelButtonStyle) {
        this.rolloverCancelButtonStyle = rolloverCancelButtonStyle;
    }

    public String getDetailsClass() {
        return ValueBindings.get(this, "detailsClass", detailsClass);
    }

    public void setDetailsClass(String detailsClass) {
        this.detailsClass = detailsClass;
    }

    public String getRolloverDetailsClass() {
        return ValueBindings.get(this, "rolloverDetailsClass", rolloverDetailsClass);
    }

    public void setRolloverDetailsClass(String rolloverDetailsClass) {
        this.rolloverDetailsClass = rolloverDetailsClass;
    }

    public String getOkButtonClass() {
        return ValueBindings.get(this, "okButtonClass", okButtonClass);
    }

    public void setOkButtonClass(String okButtonClass) {
        this.okButtonClass = okButtonClass;
    }

    public String getRolloverOkButtonClass() {
        return ValueBindings.get(this, "rolloverOkButtonClass", rolloverOkButtonClass);
    }

    public void setRolloverOkButtonClass(String rolloverOkButtonClass) {
        this.rolloverOkButtonClass = rolloverOkButtonClass;
    }

    public String getCancelButtonClass() {
        return ValueBindings.get(this, "cancelButtonClass", cancelButtonClass);
    }

    public void setCancelButtonClass(String cancelButtonClass) {
        this.cancelButtonClass = cancelButtonClass;
    }

    public String getRolloverCancelButtonClass() {
        return ValueBindings.get(this, "rolloverCancelButtonClass", rolloverCancelButtonClass);
    }

    public void setRolloverCancelButtonClass(String rolloverCancelButtonClass) {
        this.rolloverCancelButtonClass = rolloverCancelButtonClass;
    }

    public String getMessage() {
        return ValueBindings.get(this, "message", message, "Confirm your action");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOkButtonText() {
        return ValueBindings.get(this, "okButtonText", okButtonText, "OK");
    }

    public void setOkButtonText(String okButtonText) {
        this.okButtonText = okButtonText;
    }

    public String getCancelButtonText() {
        return ValueBindings.get(this, "cancelButtonText", cancelButtonText, "Cancel");
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public String getFor() {
        return ValueBindings.get(this, "for", _for);
    }

    public void setFor(String aFor) {
        _for = aFor;
    }

    public String getEvent() {
        return ValueBindings.get(this, "event", event);
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean isStandalone() {
        return ValueBindings.get(this, "standalone", standalone, false);
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    @Override
    protected boolean isResizableByDefault() {
        return false;
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        OUIClientActionHelper.ensureComponentIdSpecified(parent);
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context),
                _for,
                event,
                standalone,

                message,
                details,
                okButtonText,
                cancelButtonText,

                defaultButton,

                messageIconUrl,
                showMessageIcon,

                iconAreaStyle,
                rolloverIconAreaStyle,
                rolloverContentStyle,
                messageStyle,
                rolloverMessageStyle,
                detailsStyle,
                rolloverDetailsStyle,
                buttonAreaStyle,
                rolloverButtonAreaStyle,
                okButtonStyle,
                rolloverOkButtonStyle,
                cancelButtonStyle,
                rolloverCancelButtonStyle,

                iconAreaClass,
                rolloverIconAreaClass,
                rolloverContentClass,
                messageClass,
                rolloverMessageClass,
                detailsClass,
                rolloverDetailsClass,
                buttonAreaClass,
                rolloverButtonAreaClass,
                okButtonClass,
                rolloverOkButtonClass,
                cancelButtonClass,
                rolloverCancelButtonClass,

                alignToInvoker};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        _for = (String) values[i++];
        event = (String) values[i++];
        standalone = (Boolean) values[i++];

        message = (String) values[i++];
        details = (String) values[i++];
        okButtonText = (String) values[i++];
        cancelButtonText = (String) values[i++];

        defaultButton = (ButtonType) values[i++];

        messageIconUrl = (String) values[i++];
        showMessageIcon = (Boolean) values[i++];

        iconAreaStyle = (String) values[i++];
        rolloverIconAreaStyle = (String) values[i++];
        rolloverContentStyle = (String) values[i++];
        messageStyle = (String) values[i++];
        rolloverMessageStyle = (String) values[i++];
        detailsStyle = (String) values[i++];
        rolloverDetailsStyle = (String) values[i++];
        buttonAreaStyle = (String) values[i++];
        rolloverButtonAreaStyle = (String) values[i++];
        okButtonStyle = (String) values[i++];
        rolloverOkButtonStyle = (String) values[i++];
        cancelButtonStyle = (String) values[i++];
        rolloverCancelButtonStyle = (String) values[i++];

        iconAreaClass = (String) values[i++];
        rolloverIconAreaClass = (String) values[i++];
        rolloverContentClass = (String) values[i++];
        messageClass = (String) values[i++];
        rolloverMessageClass = (String) values[i++];
        detailsClass = (String) values[i++];
        rolloverDetailsClass = (String) values[i++];
        buttonAreaClass = (String) values[i++];
        rolloverButtonAreaClass = (String) values[i++];
        okButtonClass = (String) values[i++];
        rolloverOkButtonClass = (String) values[i++];
        cancelButtonClass = (String) values[i++];
        rolloverCancelButtonClass = (String) values[i++];

        alignToInvoker = (Boolean) values[i++];
    }
}
