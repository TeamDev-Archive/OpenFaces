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

    public void setBrowseButtonStyle(ValueExpression browseButtonStyle) {
        getDelegate().setPropertyValue("browseButtonStyle", browseButtonStyle);
    }

    public void setBrowseButtonClass(ValueExpression browseButtonClass) {
        getDelegate().setPropertyValue("browseButtonClass", browseButtonClass);
    }

    public void setBrowseButtonLabel(ValueExpression browseButtonLabel) {
        getDelegate().setPropertyValue("browseButtonLabel", browseButtonLabel);
    }

    public void setBrowseButtonOnMouseOverStyle(ValueExpression browseButtonOnMouseOverStyle) {
        getDelegate().setPropertyValue("browseButtonOnMouseOverStyle", browseButtonOnMouseOverStyle);
    }

    public void setBrowseButtonOnMouseOverClass(ValueExpression browseButtonOnMouseOverClass) {
        getDelegate().setPropertyValue("browseButtonOnMouseOverClass", browseButtonOnMouseOverClass);
    }

    public void setBrowseButtonOnMouseDownStyle(ValueExpression browseButtonOnMouseDownStyle) {
        getDelegate().setPropertyValue("browseButtonOnMouseDownStyle", browseButtonOnMouseDownStyle);
    }

    public void setAddButtonOnMouseDownClass(ValueExpression browseButtonOnMouseDownClass) {
        getDelegate().setPropertyValue("browseButtonOnMouseDownClass", browseButtonOnMouseDownClass);
    }

    public void setBrowseButtonOnFocusStyle(ValueExpression browseButtonOnFocusStyle) {
        getDelegate().setPropertyValue("browseButtonOnFocusStyle", browseButtonOnFocusStyle);
    }

    public void setBrowseButtonOnFocusClass(ValueExpression browseButtonOnFocusClass) {
        getDelegate().setPropertyValue("browseButtonOnFocusClass", browseButtonOnFocusClass);
    }

    public void setBrowseButtonDisabledStyle(ValueExpression browseButtonDisabledStyle) {
        getDelegate().setPropertyValue("browseButtonDisabledStyle", browseButtonDisabledStyle);
    }

    public void setBrowseButtonDisabledClass(ValueExpression browseButtonDisabledClass) {
        getDelegate().setPropertyValue("browseButtonDisabledClass", browseButtonDisabledClass);
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
