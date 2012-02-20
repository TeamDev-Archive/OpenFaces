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

package org.openfaces.component.input;

import org.openfaces.component.OUIInputBase;
import org.openfaces.event.FileUploadedListener;
import org.openfaces.event.UploadCompletionListener;
import org.openfaces.util.ValueBindings;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;

/**
 * @author andrii.loboda
 */
public abstract class AbstractFileUpload extends OUIInputBase {
    private String browseButtonStyle;
    private String browseButtonClass;
    protected String browseButtonText;

    private String browseButtonRolloverStyle;
    private String browseButtonRolloverClass;

    private String browseButtonPressedStyle;
    private String browseButtonPressedClass;

    private String browseButtonFocusedStyle;
    private String browseButtonFocusedClass;

    private String browseButtonDisabledStyle;
    private String browseButtonDisabledClass;


    private String stopButtonText;


    private String dropTargetStyle;
    private String dropTargetClass;
    private String dropTargetDragoverStyle;
    private String dropTargetDragoverClass;
    protected String dropTargetText;

    private String rowStyle;
    private String rowClass;

    private String fileNameClass;
    private String fileNameStyle;

    private String uploadStatusClass;
    private String uploadStatusStyle;
    private String notUploadedStatusText;
    private String uploadedStatusText;
    private String inProgressStatusText;
    private String stoppingStatusText;
    private String fileSizeLimitErrorText;
    private String unexpectedErrorText;

    private String acceptedFileTypes;


    private String progressBarStyle;
    private String progressBarClass;

    private int tabindex;

    private String stoppedStatusText;
    private MethodExpression fileUploadedListener;


    private MethodExpression uploadCompletionListener;

    private String onuploadstart;
    private String onuploadend;
    private String onfileuploadstart;
    private String onfileuploadinprogress;
    private String onfileuploadend;
    private String onwrongfileadded;

    private int fileSizeLimit;


    private String renderAfterUpload;
    
    private String externalDropTarget;
    
    private String acceptDialogFormats;

    public AbstractFileUpload() {
        setRendererType("org.openfaces.FileUploadRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                browseButtonStyle,
                browseButtonClass,
                browseButtonText,
                browseButtonRolloverStyle,
                browseButtonRolloverClass,
                browseButtonPressedStyle,
                browseButtonPressedClass,
                browseButtonFocusedStyle,
                browseButtonFocusedClass,
                browseButtonDisabledStyle,
                browseButtonDisabledClass,
                stopButtonText,
                dropTargetStyle,
                dropTargetClass,
                dropTargetDragoverStyle,
                dropTargetDragoverClass,
                dropTargetText,
                rowStyle,
                rowClass,
                acceptedFileTypes,
                fileNameClass,
                fileNameStyle,
                uploadStatusClass,
                uploadStatusStyle,
                notUploadedStatusText,
                uploadedStatusText,
                inProgressStatusText,
                fileSizeLimitErrorText,
                progressBarStyle,
                progressBarClass,
                tabindex,
                saveAttachedState(context, fileUploadedListener),
                stoppedStatusText,
                saveAttachedState(context, uploadCompletionListener),
                onuploadstart,
                onuploadend,
                onfileuploadstart,
                onfileuploadinprogress,
                onfileuploadend,
                onwrongfileadded,
                stoppingStatusText,
                unexpectedErrorText,
                fileSizeLimit,
                renderAfterUpload,
                externalDropTarget,
                acceptDialogFormats
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] values = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, values[i++]);
        browseButtonStyle = (String) values[i++];
        browseButtonClass = (String) values[i++];
        browseButtonText = (String) values[i++];
        browseButtonRolloverStyle = (String) values[i++];
        browseButtonRolloverClass = (String) values[i++];
        browseButtonPressedStyle = (String) values[i++];
        browseButtonPressedClass = (String) values[i++];
        browseButtonFocusedStyle = (String) values[i++];
        browseButtonFocusedClass = (String) values[i++];
        browseButtonDisabledStyle = (String) values[i++];
        browseButtonDisabledClass = (String) values[i++];
        stopButtonText = (String) values[i++];
        dropTargetStyle = (String) values[i++];
        dropTargetClass = (String) values[i++];
        dropTargetDragoverStyle = (String) values[i++];
        dropTargetDragoverClass = (String) values[i++];
        dropTargetText = (String) values[i++];
        rowStyle = (String) values[i++];
        rowClass = (String) values[i++];
        acceptedFileTypes = (String) values[i++];
        fileNameClass = (String) values[i++];
        fileNameStyle = (String) values[i++];
        uploadStatusClass = (String) values[i++];
        uploadStatusStyle = (String) values[i++];
        notUploadedStatusText = (String) values[i++];
        uploadedStatusText = (String) values[i++];
        inProgressStatusText = (String) values[i++];
        fileSizeLimitErrorText = (String) values[i++];
        progressBarStyle = (String) values[i++];
        progressBarClass = (String) values[i++];
        tabindex = (Integer) values[i++];
        fileUploadedListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        stoppedStatusText = (String) values[i++];
        uploadCompletionListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        onuploadstart = (String) values[i++];
        onuploadend = (String) values[i++];
        onfileuploadstart = (String) values[i++];
        onfileuploadinprogress = (String) values[i++];
        onfileuploadend = (String) values[i++];
        onwrongfileadded = (String) values[i++];
        stoppingStatusText = (String) values[i++];
        unexpectedErrorText = (String) values[i++];
        fileSizeLimit = (Integer) values[i++];
        renderAfterUpload = (String) values[i++];
        externalDropTarget = (String) values[i++];
        acceptDialogFormats = (String) values[i++];
    }


    public String getDropTargetStyle() {
        return ValueBindings.get(this, "dropTargetStyle", dropTargetStyle);
    }

    public void setDropTargetStyle(String dropTargetStyle) {
        this.dropTargetStyle = dropTargetStyle;
    }

    public String getDropTargetClass() {
        return ValueBindings.get(this, "dropTargetClass", dropTargetClass);
    }

    public void setDropTargetClass(String dropTargetClass) {
        this.dropTargetClass = dropTargetClass;
    }

    public String getDropTargetDragoverStyle() {
        return ValueBindings.get(this, "dropTargetDragoverStyle", dropTargetDragoverStyle);
    }

    public void setDropTargetDragoverStyle(String dropTargetDragoverStyle) {
        this.dropTargetDragoverStyle = dropTargetDragoverStyle;
    }

    public String getDropTargetDragoverClass() {
        return ValueBindings.get(this, "dropTargetDragoverClass", dropTargetDragoverClass);
    }

    public void setDropTargetDragoverClass(String dropTargetDragoverClass) {
        this.dropTargetDragoverClass = dropTargetDragoverClass;
    }

    public abstract String getDropTargetText();

    public void setDropTargetText(String dropTargetText) {
        this.dropTargetText = dropTargetText;
    }

    public String getRowStyle() {
        return ValueBindings.get(this, "rowStyle", rowStyle);
    }

    public void setRowStyle(String rowStyle) {
        this.rowStyle = rowStyle;
    }

    public String getRowClass() {
        return ValueBindings.get(this, "rowClass", rowClass);
    }

    public void setRowClass(String rowClass) {
        this.rowClass = rowClass;
    }

    public String getFileNameClass() {
        return ValueBindings.get(this, "fileNameClass", fileNameClass);
    }

    public void setFileNameClass(String fileNameClass) {
        this.fileNameClass = fileNameClass;
    }

    public String getFileNameStyle() {
        return ValueBindings.get(this, "fileNameStyle", fileNameStyle);
    }

    public void setFileNameStyle(String fileNameStyle) {
        this.fileNameStyle = fileNameStyle;
    }

    public String getNotUploadedStatusText() {
        return ValueBindings.get(this, "notUploadedStatusText", notUploadedStatusText);
    }

    public void setNotUploadedStatusText(String notUploadedStatusText) {
        this.notUploadedStatusText = notUploadedStatusText;
    }

    public String getUploadedStatusText() {
        return ValueBindings.get(this, "uploadedStatusText", uploadedStatusText, "{size} MB{MB}");
    }

    public void setUploadedStatusText(String uploadedStatusText) {
        this.uploadedStatusText = uploadedStatusText;
    }

    public String getInProgressStatusText() {
        return ValueBindings.get(this, "inProgressStatusText", inProgressStatusText, "{uploaded} of {size} MB{MB}");
    }

    public void setInProgressStatusText(String inProgressStatusText) {
        this.inProgressStatusText = inProgressStatusText;
    }

    public String getFileSizeLimitErrorText() {
        return ValueBindings.get(this, "fileSizeLimitErrorText", fileSizeLimitErrorText, "File size limit exceeded");
    }

    public void setFileSizeLimitErrorText(String fileSizeLimitErrorText) {
        this.fileSizeLimitErrorText = fileSizeLimitErrorText;
    }

    public String getUploadStatusClass() {
        return ValueBindings.get(this, "uploadStatusClass", uploadStatusClass);
    }

    public void setUploadStatusClass(String uploadStatusClass) {
        this.uploadStatusClass = uploadStatusClass;
    }

    public String getUploadStatusStyle() {
        return ValueBindings.get(this, "uploadStatusStyle", uploadStatusStyle);
    }

    public void setUploadStatusStyle(String uploadStatusStyle) {
        this.uploadStatusStyle = uploadStatusStyle;
    }


    public String getAcceptedFileTypes() {
        return ValueBindings.get(this, "acceptedTypesOfFile", acceptedFileTypes);
    }

    public void setAcceptedFileTypes(String acceptedFileTypes) {
        this.acceptedFileTypes = acceptedFileTypes;
    }


    public String getBrowseButtonStyle() {
        return ValueBindings.get(this, "browseButtonStyle", browseButtonStyle);
    }

    public void setBrowseButtonStyle(String browseButtonStyle) {
        this.browseButtonStyle = browseButtonStyle;
    }

    public String getBrowseButtonClass() {
        return ValueBindings.get(this, "browseButtonClass", browseButtonClass);
    }

    public void setBrowseButtonClass(String browseButtonClass) {
        this.browseButtonClass = browseButtonClass;
    }

    public abstract String getBrowseButtonText();

    public void setBrowseButtonText(String browseButtonText) {
        this.browseButtonText = browseButtonText;
    }

    public String getBrowseButtonRolloverStyle() {
        return ValueBindings.get(this, "browseButtonRolloverStyle", browseButtonRolloverStyle);
    }

    public void setBrowseButtonRolloverStyle(String browseButtonRolloverStyle) {
        this.browseButtonRolloverStyle = browseButtonRolloverStyle;
    }

    public String getBrowseButtonRolloverClass() {
        return ValueBindings.get(this, "browseButtonRolloverClass", browseButtonRolloverClass);
    }

    public void setBrowseButtonRolloverClass(String browseButtonRolloverClass) {
        this.browseButtonRolloverClass = browseButtonRolloverClass;
    }

    public String getBrowseButtonPressedStyle() {
        return ValueBindings.get(this, "browseButtonPressedStyle", browseButtonPressedStyle);
    }

    public void setBrowseButtonPressedStyle(String browseButtonPressedStyle) {
        this.browseButtonPressedStyle = browseButtonPressedStyle;
    }

    public String getBrowseButtonPressedClass() {
        return ValueBindings.get(this, "browseButtonPressedClass", browseButtonPressedClass);
    }

    public void setBrowseButtonPressedClass(String browseButtonPressedClass) {
        this.browseButtonPressedClass = browseButtonPressedClass;
    }

    public String getBrowseButtonDisabledStyle() {
        return ValueBindings.get(this, "browseButtonDisabledStyle", browseButtonDisabledStyle);
    }

    public void setBrowseButtonDisabledStyle(String browseButtonDisabledStyle) {
        this.browseButtonDisabledStyle = browseButtonDisabledStyle;
    }

    public String getBrowseButtonDisabledClass() {
        return ValueBindings.get(this, "browseButtonDisabledClass", browseButtonDisabledClass);
    }

    public void setBrowseButtonDisabledClass(String browseButtonDisabledClass) {
        this.browseButtonDisabledClass = browseButtonDisabledClass;
    }

    public String getBrowseButtonFocusedStyle() {
        return ValueBindings.get(this, "browseButtonFocusedStyle", browseButtonFocusedStyle);
    }

    public void setBrowseButtonFocusedStyle(String browseButtonFocusedStyle) {
        this.browseButtonFocusedStyle = browseButtonFocusedStyle;
    }

    public String getBrowseButtonFocusedClass() {
        return ValueBindings.get(this, "browseButtonFocusedClass", browseButtonFocusedClass);
    }

    public void setBrowseButtonFocusedClass(String browseButtonFocusedClass) {
        this.browseButtonFocusedClass = browseButtonFocusedClass;
    }


    public String getStopButtonText() {
        return ValueBindings.get(this, "stopButtonText", stopButtonText, "Stop");
    }

    public void setStopButtonText(String stopButtonText) {
        this.stopButtonText = stopButtonText;
    }


    public String getProgressBarStyle() {
        return ValueBindings.get(this, "progressBarStyle", progressBarStyle);
    }

    public void setProgressBarStyle(String progressBarStyle) {
        this.progressBarStyle = progressBarStyle;
    }

    public String getProgressBarClass() {
        return ValueBindings.get(this, "progressBarClass", progressBarClass);
    }

    public void setProgressBarClass(String progressBarClass) {
        this.progressBarClass = progressBarClass;
    }

    public int getTabindex() {
        return ValueBindings.get(this, "tabindex", tabindex, -1);
    }

    public void setTabindex(int tabindex) {
        this.tabindex = tabindex;
    }

    public MethodExpression getFileUploadedListener() {
        return fileUploadedListener;
    }

    public void setFileUploadedListener(MethodExpression fileUploadedListener) {
        this.fileUploadedListener = fileUploadedListener;
    }


    public void addFileUploadedListener(FileUploadedListener fileUploadedListener) {
        addFacesListener(fileUploadedListener);
    }

    public FileUploadedListener[] getFileUploadedListeners() {
        return (FileUploadedListener[]) getFacesListeners(FileUploadedListener.class);
    }

    public void removeFileUploadedListener(FileUploadedListener fileUploadedListener) {
        removeFacesListener(fileUploadedListener);
    }

    public String getStoppedStatusText() {
        return ValueBindings.get(this, "stoppedStatusText", stoppedStatusText, "Stopped");
    }

    public void setStoppedStatusText(String stoppedStatusText) {
        this.stoppedStatusText = stoppedStatusText;
    }


    public MethodExpression getUploadCompletionListener() {
        return uploadCompletionListener;
    }

    public void setUploadCompletionListener(MethodExpression uploadCompletionListener) {
        this.uploadCompletionListener = uploadCompletionListener;
    }

    public void addUploadCompletionListener(UploadCompletionListener uploadCompletionListener) {
        addFacesListener(uploadCompletionListener);
    }

    public UploadCompletionListener[] getUploadCompletionListeners() {
        return (UploadCompletionListener[]) getFacesListeners(FileUploadedListener.class);
    }

    public void removeUploadCompletionListener(UploadCompletionListener uploadCompletionListener) {
        removeFacesListener(uploadCompletionListener);
    }

    public String getOnuploadstart() {
        return ValueBindings.get(this, "onuploadstart", onuploadstart);
    }

    public void setOnuploadstart(String onuploadstart) {
        this.onuploadstart = onuploadstart;
    }

    public String getOnuploadend() {
        return ValueBindings.get(this, "onuploadend", onuploadend);
    }

    public void setOnuploadend(String onuploadend) {
        this.onuploadend = onuploadend;
    }

    public String getOnfileuploadstart() {
        return ValueBindings.get(this, "onfileuploadstart", onfileuploadstart);
    }

    public void setOnfileuploadstart(String onfileuploadstart) {
        this.onfileuploadstart = onfileuploadstart;
    }

    public String getOnfileuploadinprogress() {
        return ValueBindings.get(this, "onfileuploadinprogress", onfileuploadinprogress);
    }

    public void setOnfileuploadinprogress(String onfileuploadinprogress) {
        this.onfileuploadinprogress = onfileuploadinprogress;
    }

    public String getOnfileuploadend() {
        return ValueBindings.get(this, "onfileuploadend", onfileuploadend);
    }

    public void setOnfileuploadend(String onfileuploadend) {
        this.onfileuploadend = onfileuploadend;
    }

    public String getOnwrongfileadded() {
        return ValueBindings.get(this, "onwrongfileadded", onwrongfileadded);
    }

    public void setOnwrongfileadded(String onwrongfileadded) {
        this.onwrongfileadded = onwrongfileadded;
    }

    public String getStoppingStatusText() {
        return ValueBindings.get(this, "stoppingStatusText", stoppingStatusText, "Stopping...");
    }

    public void setStoppingStatusText(String stoppingStatusText) {
        this.stoppingStatusText = stoppingStatusText;
    }

    public String getUnexpectedErrorText() {
        return ValueBindings.get(this, "unexpectedErrorText", unexpectedErrorText, "Error");
    }

    public void setUnexpectedErrorText(String unexpectedErrorText) {
        this.unexpectedErrorText = unexpectedErrorText;
    }

    public int getFileSizeLimit() {
        return ValueBindings.get(this, "fileSizeLimit", fileSizeLimit, 0);
    }

    public void setFileSizeLimit(int fileSizeLimit) {
        this.fileSizeLimit = fileSizeLimit;
    }


    public String getRenderAfterUpload() {
        return ValueBindings.get(this, "renderAfterUpload", renderAfterUpload);
    }

    public void setRenderAfterUpload(String renderAfterUpload) {
        this.renderAfterUpload = renderAfterUpload;
    }

    public String getExternalDropTarget() {
        return ValueBindings.get(this, "externalDropTarget", externalDropTarget);
    }

    public void setExternalDropTarget(String externalDropTarget) {
        this.externalDropTarget = externalDropTarget;
    }

    public String getAcceptDialogFormats() {
        return ValueBindings.get(this, "acceptDialogFormats", acceptDialogFormats);
    }

    public void setAcceptDialogFormats(String acceptDialogFormats) {
        this.acceptDialogFormats = acceptDialogFormats;
    }
}
