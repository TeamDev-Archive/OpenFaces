/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.FileUploadTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

public class FileUploadJspTag extends AbstractComponentJspTag {

    public FileUploadJspTag() {
        super(new FileUploadTag());
    }


    public void setMaxQuantity(ValueExpression maxQuantity) {
        getDelegate().setPropertyValue("maxQuantity", maxQuantity);
    }

    public void setMinQuantity(ValueExpression minQuantity) {
        getDelegate().setPropertyValue("minQuantity", minQuantity);
    }

    public void setHeaderStyle(ValueExpression headerStyle) {
        getDelegate().setPropertyValue("headerStyle", headerStyle);
    }

    public void setHeaderClass(ValueExpression headerClass) {
        getDelegate().setPropertyValue("headerClass", headerClass);
    }

    public void setAddButtonStyle(ValueExpression addButtonStyle) {
        getDelegate().setPropertyValue("addButtonStyle", addButtonStyle);
    }

    public void setAddButtonClass(ValueExpression addButtonClass) {
        getDelegate().setPropertyValue("addButtonClass", addButtonClass);
    }

    public void setAddButtonLabel(ValueExpression addButtonLabel) {
        getDelegate().setPropertyValue("addButtonLabel", addButtonLabel);
    }

    public void setAddButtonOnMouseOverStyle(ValueExpression addButtonOnMouseOverStyle) {
        getDelegate().setPropertyValue("addButtonOnMouseOverStyle", addButtonOnMouseOverStyle);
    }

    public void setAddButtonOnMouseOverClass(ValueExpression addButtonOnMouseOverClass) {
        getDelegate().setPropertyValue("addButtonOnMouseOverClass", addButtonOnMouseOverClass);
    }

    public void setAddButtonOnMouseDownStyle(ValueExpression addButtonOnMouseDownStyle) {
        getDelegate().setPropertyValue("addButtonOnMouseDownStyle", addButtonOnMouseDownStyle);
    }

    public void setAddButtonOnMouseDownClass(ValueExpression addButtonOnMouseDownClass) {
        getDelegate().setPropertyValue("addButtonOnMouseDownClass", addButtonOnMouseDownClass);
    }

    public void setAddButtonOnFocusStyle(ValueExpression addButtonOnFocusStyle) {
        getDelegate().setPropertyValue("addButtonOnFocusStyle", addButtonOnFocusStyle);
    }

    public void setAddButtonOnFocusClass(ValueExpression addButtonOnFocusClass) {
        getDelegate().setPropertyValue("addButtonOnFocusClass", addButtonOnFocusClass);
    }

    public void setAddButtonDisabledStyle(ValueExpression addButtonDisabledStyle) {
        getDelegate().setPropertyValue("addButtonDisabledStyle", addButtonDisabledStyle);
    }

    public void setAddButtonDisabledClass(ValueExpression addButtonDisabledClass) {
        getDelegate().setPropertyValue("addButtonDisabledClass", addButtonDisabledClass);
    }

    public void setAllInfosStyle(ValueExpression allInfosStyle) {
        getDelegate().setPropertyValue("allInfosStyle", allInfosStyle);
    }


    public void setAllInfosClass(ValueExpression allInfosClass) {
        getDelegate().setPropertyValue("allInfosClass", allInfosClass);
    }

    public void setFileInfoStyle(ValueExpression fileInfoStyle) {
        getDelegate().setPropertyValue("fileInfoStyle", fileInfoStyle);
    }

    public void setFileInfoClass(ValueExpression fileInfoClass) {
        getDelegate().setPropertyValue("fileInfoClass", fileInfoClass);
    }

    public void setAcceptedFileTypes(ValueExpression acceptedFileTypes) {
        getDelegate().setPropertyValue("acceptedFileTypes", acceptedFileTypes);
    }

    public void setDuplicateAllowed(ValueExpression duplicateAllowed) {
        getDelegate().setPropertyValue("duplicateAllowed", duplicateAllowed);
    }

    public void setAutoUpload(ValueExpression autoUpload) {
        getDelegate().setPropertyValue("autoUpload", autoUpload);
    }

    public void setInfoTitleClass(ValueExpression infoTitleClass) {
        getDelegate().setPropertyValue("infoTitleClass", infoTitleClass);
    }

    public void setInfoTitleStyle(ValueExpression infoTitleStyle) {
        getDelegate().setPropertyValue("infoTitleStyle", infoTitleStyle);
    }

    public void setInfoStatusClass(ValueExpression infoStatusClass) {
        getDelegate().setPropertyValue("infoStatusClass", infoStatusClass);
    }

    public void setInfoStatusStyle(ValueExpression infoStatusStyle) {
        getDelegate().setPropertyValue("infoStatusStyle", infoStatusStyle);
    }

    public void setStatusNotUploadedText(ValueExpression statusNotUploadedText) {
        getDelegate().setPropertyValue("statusNotUploadedText", statusNotUploadedText);
    }

    public void setStatusUploadedText(ValueExpression statusUploadedText) {
        getDelegate().setPropertyValue("statusUploadedText", statusUploadedText);
    }

    public void setStatusInProgressText(ValueExpression statusInProgressText) {
        getDelegate().setPropertyValue("statusInProgressText", statusInProgressText);
    }

    public void setMaxFileSizeErrorText(ValueExpression maxFileSizeErrorText) {
        getDelegate().setPropertyValue("maxFileSizeErrorText", maxFileSizeErrorText);
    }

    public void setDisabled(ValueExpression disabled) {
        getDelegate().setPropertyValue("disabled", disabled);
    }

    public void setProgressBarStyle(ValueExpression progressBarStyle) {
        getDelegate().setPropertyValue("progressBarStyle", progressBarStyle);
    }

    public void setProgressBarClass(ValueExpression progressBarClass) {
        getDelegate().setPropertyValue("progressBarClass", progressBarClass);
    }

    public void setTabindex(ValueExpression tabindex) {
        getDelegate().setPropertyValue("tabindex", tabindex);
    }

    public void setFileUploadedListener(MethodExpression fileUploadedListener) {
        getDelegate().setPropertyValue("fileUploadedListener", fileUploadedListener);
    }

    public void setStatusStoppedText(MethodExpression statusStoppedText) {
        getDelegate().setPropertyValue("statusStoppedText", statusStoppedText);
    }

    public void setMultiUpload(MethodExpression multiUpload) {
        getDelegate().setPropertyValue("multiUpload", multiUpload);
    }

    public void setUploadCompletionListener(MethodExpression uploadCompletionListener) {
        getDelegate().setPropertyValue("uploadCompletionListener", uploadCompletionListener);
    }
    public void setOnuploadstart(MethodExpression onuploadstart) {
        getDelegate().setPropertyValue("onuploadstart", onuploadstart);
    }
    public void setOnuploadend(MethodExpression onuploadend) {
        getDelegate().setPropertyValue("onuploadend", onuploadend);
    }
}
