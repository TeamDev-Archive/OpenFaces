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
package org.openfaces.taglib.jsp.window;

import org.openfaces.taglib.internal.window.ConfirmationTag;
import org.openfaces.taglib.jsp.AbstractWindowJspTag;

import javax.el.ValueExpression;

/**
 * @author Andrew Palval
 */
public class ConfirmationJspTag extends AbstractWindowJspTag {

    public ConfirmationJspTag() {
        super(new ConfirmationTag());
    }

    public void setAlignToInvoker(ValueExpression alignToInvoker) {
        getDelegate().setPropertyValue("alignToInvoker", alignToInvoker);
    }

    public void setShowMessageIcon(ValueExpression showMessageIcon) {
        getDelegate().setPropertyValue("showMessageIcon", showMessageIcon);
    }

    public void setIconAreaStyle(ValueExpression iconAreaStyle) {
        getDelegate().setPropertyValue("iconAreaStyle", iconAreaStyle);
    }

    public void setRolloverIconAreaStyle(ValueExpression rolloverIconAreaStyle) {
        getDelegate().setPropertyValue("rolloverIconAreaStyle", rolloverIconAreaStyle);
    }

    public void setIconAreaClass(ValueExpression iconAreaClass) {
        getDelegate().setPropertyValue("iconAreaClass", iconAreaClass);
    }

    public void setRolloverIconAreaClass(ValueExpression rolloverIconAreaClass) {
        getDelegate().setPropertyValue("rolloverIconAreaClass", rolloverIconAreaClass);
    }

    public void setMessageStyle(ValueExpression messageStyle) {
        getDelegate().setPropertyValue("messageStyle", messageStyle);
    }

    public void setRolloverMessageStyle(ValueExpression rolloverMessageStyle) {
        getDelegate().setPropertyValue("rolloverMessageStyle", rolloverMessageStyle);
    }

    public void setMessageClass(ValueExpression messageClass) {
        getDelegate().setPropertyValue("messageClass", messageClass);
    }

    public void setRolloverMessageClass(ValueExpression rolloverMessageClass) {
        getDelegate().setPropertyValue("rolloverMessageClass", rolloverMessageClass);
    }

    public void setDetails(ValueExpression details) {
        getDelegate().setPropertyValue("details", details);
    }

    public void setMessageIconUrl(ValueExpression messageIconUrl) {
        getDelegate().setPropertyValue("messageIconUrl", messageIconUrl);
    }

    public void setButtonAreaStyle(ValueExpression buttonAreaStyle) {
        getDelegate().setPropertyValue("buttonAreaStyle", buttonAreaStyle);
    }

    public void setRolloverButtonAreaStyle(ValueExpression rolloverButtonAreaStyle) {
        getDelegate().setPropertyValue("rolloverButtonAreaStyle", rolloverButtonAreaStyle);
    }

    public void setButtonAreaClass(ValueExpression buttonAreaClass) {
        getDelegate().setPropertyValue("buttonAreaClass", buttonAreaClass);
    }

    public void setRolloverButtonBacklaneClass(ValueExpression rolloverButtonBacklaneClass) {
        getDelegate().setPropertyValue("rolloverButtonBacklaneClass", rolloverButtonBacklaneClass);
    }

    public void setDefaultButton(ValueExpression defaultButton) {
        getDelegate().setPropertyValue("defaultButton", defaultButton);
    }

    public void setRolloverContentStyle(ValueExpression rolloverContentStyle) {
        getDelegate().setPropertyValue("rolloverContentStyle", rolloverContentStyle);
    }

    public void setRolloverContentClass(ValueExpression rolloverContentClass) {
        getDelegate().setPropertyValue("rolloverContentClass", rolloverContentClass);
    }

    public void setDetailsStyle(ValueExpression detailsStyle) {
        getDelegate().setPropertyValue("detailsStyle", detailsStyle);
    }

    public void setRolloverDetailsStyle(ValueExpression rolloverDetailsStyle) {
        getDelegate().setPropertyValue("rolloverDetailsStyle", rolloverDetailsStyle);
    }

    public void setOkButtonStyle(ValueExpression okButtonStyle) {
        getDelegate().setPropertyValue("okButtonStyle", okButtonStyle);
    }

    public void setRolloverOkButtonStyle(ValueExpression rolloverOkButtonStyle) {
        getDelegate().setPropertyValue("rolloverOkButtonStyle", rolloverOkButtonStyle);
    }

    public void setCancelButtonStyle(ValueExpression cancelButtonStyle) {
        getDelegate().setPropertyValue("cancelButtonStyle", cancelButtonStyle);
    }

    public void setRolloverCancelButtonStyle(ValueExpression rolloverCancelButtonStyle) {
        getDelegate().setPropertyValue("rolloverCancelButtonStyle", rolloverCancelButtonStyle);
    }

    public void setDetailsClass(ValueExpression detailsClass) {
        getDelegate().setPropertyValue("detailsClass", detailsClass);
    }

    public void setRolloverDetailsClass(ValueExpression rolloverDetailsClass) {
        getDelegate().setPropertyValue("rolloverDetailsClass", rolloverDetailsClass);
    }

    public void setOkButtonClass(ValueExpression okButtonClass) {
        getDelegate().setPropertyValue("okButtonClass", okButtonClass);
    }

    public void setRolloverOkButtonClass(ValueExpression rolloverOkButtonClass) {
        getDelegate().setPropertyValue("rolloverOkButtonClass", rolloverOkButtonClass);
    }

    public void setCancelButtonClass(ValueExpression cancelButtonClass) {
        getDelegate().setPropertyValue("cancelButtonClass", cancelButtonClass);
    }

    public void setRolloverCancelButtonClass(ValueExpression rolloverCancelButtonClass) {
        getDelegate().setPropertyValue("rolloverCancelButtonClass", rolloverCancelButtonClass);
    }

    public void setMessage(ValueExpression message) {
        getDelegate().setPropertyValue("message", message);
    }

    public void setOkButtonText(ValueExpression okButtonText) {
        getDelegate().setPropertyValue("okButtonText", okButtonText);
    }

    public void setCancelButtonText(ValueExpression cancelButtonText) {
        getDelegate().setPropertyValue("cancelButtonText", cancelButtonText);
    }

    public void setFor(ValueExpression forValue) {
        getDelegate().setPropertyValue("for", forValue);
    }

    public void setEvent(ValueExpression event) {
        getDelegate().setPropertyValue("event", event);
    }

    public void setStandalone(ValueExpression standalone) {
        getDelegate().setPropertyValue("standalone", standalone);
    }

    public void setRolloverButtonAreaClass(ValueExpression rolloverButtonAreaClass) {
        getDelegate().setPropertyValue("rolloverButtonAreaClass", rolloverButtonAreaClass);
    }

}
