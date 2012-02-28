/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.taglib.jsp.input;

import org.openfaces.taglib.internal.input.AbstractFileUploadTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

public abstract class AbstractFileUploadJspTag extends AbstractComponentJspTag {

    protected AbstractFileUploadJspTag(AbstractFileUploadTag delegate) {
        super(delegate);
    }

    public void setBrowseButtonStyle(ValueExpression browseButtonStyle) {
        getDelegate().setPropertyValue("browseButtonStyle", browseButtonStyle);
    }

    public void setBrowseButtonClass(ValueExpression browseButtonClass) {
        getDelegate().setPropertyValue("browseButtonClass", browseButtonClass);
    }

    public void setBrowseButtonText(ValueExpression browseButtonText) {
        getDelegate().setPropertyValue("browseButtonText", browseButtonText);
    }

    public void setBrowseButtonRolloverStyle(ValueExpression browseButtonRolloverStyle) {
        getDelegate().setPropertyValue("browseButtonRolloverStyle", browseButtonRolloverStyle);
    }

    public void setBrowseButtonRolloverClass(ValueExpression browseButtonRolloverClass) {
        getDelegate().setPropertyValue("browseButtonRolloverClass", browseButtonRolloverClass);
    }

    public void setBrowseButtonPressedStyle(ValueExpression browseButtonPressedStyle) {
        getDelegate().setPropertyValue("browseButtonPressedStyle", browseButtonPressedStyle);
    }

    public void setBrowseButtonPressedClass(ValueExpression browseButtonPressedClass) {
        getDelegate().setPropertyValue("browseButtonPressedClass", browseButtonPressedClass);
    }

    public void setBrowseButtonFocusedStyle(ValueExpression browseButtonFocusedStyle) {
        getDelegate().setPropertyValue("browseButtonFocusedStyle", browseButtonFocusedStyle);
    }

    public void setBrowseButtonFocusedClass(ValueExpression browseButtonFocusedClass) {
        getDelegate().setPropertyValue("browseButtonFocusedClass", browseButtonFocusedClass);
    }

    public void setBrowseButtonDisabledStyle(ValueExpression browseButtonDisabledStyle) {
        getDelegate().setPropertyValue("browseButtonDisabledStyle", browseButtonDisabledStyle);
    }

    public void setBrowseButtonDisabledClass(ValueExpression browseButtonDisabledClass) {
        getDelegate().setPropertyValue("browseButtonDisabledClass", browseButtonDisabledClass);
    }


    public void setStopButtonText(ValueExpression stopButtonText) {
        getDelegate().setPropertyValue("stopButtonText", stopButtonText);
    }


    public void setDropTargetStyle(ValueExpression dropTargetStyle) {
        getDelegate().setPropertyValue("dropTargetStyle", dropTargetStyle);
    }

    public void setDropTargetClass(ValueExpression dropTargetClass) {
        getDelegate().setPropertyValue("dropTargetClass", dropTargetClass);
    }

    public void setDropTargetDragoverStyle(ValueExpression dropTargetDragoverStyle) {
        getDelegate().setPropertyValue("dropTargetDragoverStyle", dropTargetDragoverStyle);
    }

    public void setDropTargetDragoverClass(ValueExpression dropTargetDragoverClass) {
        getDelegate().setPropertyValue("dropTargetDragoverClass", dropTargetDragoverClass);
    }

    public void setDropTargetText(ValueExpression dropTargetText) {
        getDelegate().setPropertyValue("dropTargetText", dropTargetText);
    }

    public void setRowStyle(ValueExpression rowStyle) {
        getDelegate().setPropertyValue("rowStyle", rowStyle);
    }

    public void setRowClass(ValueExpression rowClass) {
        getDelegate().setPropertyValue("rowClass", rowClass);
    }

    public void setAcceptedFileTypes(ValueExpression acceptedFileTypes) {
        getDelegate().setPropertyValue("acceptedFileTypes", acceptedFileTypes);
    }


    public void setFileNameStyle(ValueExpression fileNameStyle) {
        getDelegate().setPropertyValue("fileNameStyle", fileNameStyle);
    }

    public void setFileNameClass(ValueExpression fileNameClass) {
        getDelegate().setPropertyValue("fileNameClass", fileNameClass);
    }

    public void setUploadStatusClass(ValueExpression uploadStatusClass) {
        getDelegate().setPropertyValue("uploadStatusClass", uploadStatusClass);
    }

    public void setUploadStatusStyle(ValueExpression uploadStatusStyle) {
        getDelegate().setPropertyValue("uploadStatusStyle", uploadStatusStyle);
    }

    public void setNotUploadedStatusText(ValueExpression notUploadedStatusText) {
        getDelegate().setPropertyValue("notUploadedStatusText", notUploadedStatusText);
    }

    public void setUploadedStatusText(ValueExpression uploadedStatusText) {
        getDelegate().setPropertyValue("uploadedStatusText", uploadedStatusText);
    }

    public void setInProgressStatusText(ValueExpression inProgressStatusText) {
        getDelegate().setPropertyValue("inProgressStatusText", inProgressStatusText);
    }

    public void setStoppedStatusText(MethodExpression stoppedStatusText) {
        getDelegate().setPropertyValue("stoppedStatusText", stoppedStatusText);
    }

    public void setStoppingStatusText(MethodExpression stoppingStatusText) {
        getDelegate().setPropertyValue("stoppingStatusText", stoppingStatusText);
    }

    public void setFileSizeLimitErrorText(ValueExpression fileSizeLimitErrorText) {
        getDelegate().setPropertyValue("fileSizeLimitErrorText", fileSizeLimitErrorText);
    }

    public void setUnexpectedErrorText(MethodExpression unexpectedErrorText) {
        getDelegate().setPropertyValue("unexpectedErrorText", unexpectedErrorText);
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


    public void setUploadCompletionListener(MethodExpression uploadCompletionListener) {
        getDelegate().setPropertyValue("uploadCompletionListener", uploadCompletionListener);
    }

    public void setOnstart(ValueExpression onstart) {
        getDelegate().setPropertyValue("onstart", onstart);
    }

    public void setOnend(ValueExpression onend) {
        getDelegate().setPropertyValue("onend", onend);
    }

    public void setOnuploadstart(ValueExpression onuploadstart) {
        getDelegate().setPropertyValue("onuploadstart", onuploadstart);
    }

    public void setOnuploadinprogress(ValueExpression onuploadinprogress) {
        getDelegate().setPropertyValue("onuploadinprogress", onuploadinprogress);
    }

    public void setOnuploadend(ValueExpression onuploadend) {
        getDelegate().setPropertyValue("onuploadend", onuploadend);
    }

    public void setOnwrongfileadded(ValueExpression onwrongfileadded) {
        getDelegate().setPropertyValue("onwrongfileadded", onwrongfileadded);
    }

    public void setOndirectorydropped(ValueExpression ondirectorydropped) {
        getDelegate().setPropertyValue("ondirectorydropped", ondirectorydropped);
    }

    public void setFileSizeLimit(ValueExpression fileSizeLimit) {
        getDelegate().setPropertyValue("fileSizeLimit", fileSizeLimit);
    }


    public void setRenderAfterUpload(ValueExpression renderAfterUpload) {
        getDelegate().setPropertyValue("renderAfterUpload", renderAfterUpload);
    }

    public void setExternalDropTarget(ValueExpression externalDropTarget) {
        getDelegate().setPropertyValue("externalDropTarget", externalDropTarget);
    }

    public void setAcceptDialogFormats(ValueExpression acceptDialogFormats) {
        getDelegate().setPropertyValue("acceptDialogFormats", acceptDialogFormats);
    }

    public void setDirectoryDroppedText(ValueExpression directoryDroppedText) {
        getDelegate().setPropertyValue("directoryDroppedText", directoryDroppedText);
    }

}