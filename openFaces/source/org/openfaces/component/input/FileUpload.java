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
package org.openfaces.component.input;

import org.openfaces.component.OUIInputBase;
import org.openfaces.event.FileUploadedListener;
import org.openfaces.event.UploadCompletionListener;
import org.openfaces.util.ValueBindings;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;


public class FileUpload extends OUIInputBase {
    public static final String COMPONENT_TYPE = "org.openfaces.FileUpload";
    public static final String COMPONENT_FAMILY = "org.openfaces.FileUpload";

    private int minQuantity;
    private int maxQuantity;

    private String headerStyle;
    private String headerClass;

    private String browseButtonStyle;
    private String browseButtonClass;
    private String browseButtonLabel;

    private String browseButtonOnMouseOverStyle;
    private String browseButtonOnMouseOverClass;

    private String browseButtonOnMouseDownStyle;
    private String browseButtonOnMouseDownClass;

    private String browseButtonOnFocusStyle;
    private String browseButtonOnFocusClass;

    private String browseButtonDisabledStyle;
    private String browseButtonDisabledClass;

    private String allInfosStyle;
    private String allInfosClass;

    private String fileInfoStyle;
    private String fileInfoClass;

    private String infoTitleClass;
    private String infoTitleStyle;

    private String infoStatusClass;
    private String infoStatusStyle;
    private String statusNotUploadedText;
    private String statusUploadedText;
    private String statusInProgressText;
    private String maxFileSizeErrorText;

    private String acceptedFileTypes;

    private boolean duplicateAllowed;
    private boolean autoUpload;

    private String progressBarStyle;
    private String progressBarClass;

    private int tabindex;

    private String statusStoppedText;
    private MethodExpression fileUploadedListener;
    private boolean multiUpload;

    private MethodExpression uploadCompletionListener;

    private String onuploadstart;
    private String onuploadend;

    public FileUpload() {
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
                minQuantity,
                maxQuantity,
                headerStyle,
                headerClass,
                browseButtonStyle,
                browseButtonClass,
                browseButtonLabel,
                browseButtonOnMouseOverStyle,
                browseButtonOnMouseOverClass,
                browseButtonOnMouseDownStyle,
                browseButtonOnMouseDownClass,
                browseButtonOnFocusStyle,
                browseButtonOnFocusClass,
                browseButtonDisabledStyle,
                browseButtonDisabledClass,
                allInfosStyle,
                allInfosClass,
                fileInfoStyle,
                fileInfoClass,
                acceptedFileTypes,
                duplicateAllowed,
                infoTitleClass,
                infoTitleStyle,
                infoStatusClass,
                infoStatusStyle,
                statusNotUploadedText,
                statusUploadedText,
                statusInProgressText,
                maxFileSizeErrorText,
                progressBarStyle,
                progressBarClass,
                tabindex,
                saveAttachedState(context, fileUploadedListener),
                statusStoppedText,
                multiUpload,
                saveAttachedState(context, uploadCompletionListener),
                onuploadstart,
                onuploadend
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] values = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, values[i++]);
        minQuantity = (Integer) values[i++];
        maxQuantity = (Integer) values[i++];
        headerStyle = (String) values[i++];
        headerClass = (String) values[i++];
        browseButtonStyle = (String) values[i++];
        browseButtonClass = (String) values[i++];
        browseButtonLabel = (String) values[i++];
        browseButtonOnMouseOverStyle = (String) values[i++];
        browseButtonOnMouseOverClass = (String) values[i++];
        browseButtonOnMouseDownStyle = (String) values[i++];
        browseButtonOnMouseDownClass = (String) values[i++];
        browseButtonOnFocusStyle = (String) values[i++];
        browseButtonOnFocusClass = (String) values[i++];
        browseButtonDisabledStyle = (String) values[i++];
        browseButtonDisabledClass = (String) values[i++];
        allInfosStyle = (String) values[i++];
        allInfosClass = (String) values[i++];
        fileInfoStyle = (String) values[i++];
        fileInfoClass = (String) values[i++];
        acceptedFileTypes = (String) values[i++];
        duplicateAllowed = (Boolean) values[i++];
        infoTitleClass = (String) values[i++];
        infoTitleStyle = (String) values[i++];
        infoStatusClass = (String) values[i++];
        infoStatusStyle = (String) values[i++];
        statusNotUploadedText = (String) values[i++];
        statusUploadedText = (String) values[i++];
        statusInProgressText = (String) values[i++];
        maxFileSizeErrorText = (String) values[i++];
        progressBarStyle = (String) values[i++];
        progressBarClass = (String) values[i++];
        tabindex = (Integer) values[i++];
        fileUploadedListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        statusStoppedText = (String) values[i++];
        multiUpload = (Boolean) values[i++];
        uploadCompletionListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        onuploadstart = (String) values[i++];
        onuploadstart = (String) values[i++];
    }


    public String getHeaderStyle() {
        return ValueBindings.get(this, "headerStyle", headerStyle);
    }

    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    public String getHeaderClass() {
        return ValueBindings.get(this, "headerClass", headerClass);
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getAllInfosStyle() {
        return ValueBindings.get(this, "allInfosStyle", allInfosStyle);
    }

    public void setAllInfosStyle(String allInfosStyle) {
        this.allInfosStyle = allInfosStyle;
    }

    public String getAllInfosClass() {
        return ValueBindings.get(this, "allInfosClass", allInfosClass);
    }

    public void setAllInfosClass(String allInfosClass) {
        this.allInfosClass = allInfosClass;
    }

    public String getFileInfoStyle() {
        return ValueBindings.get(this, "fileInfoStyle", fileInfoStyle);
    }

    public void setFileInfoStyle(String fileInfoStyle) {
        this.fileInfoStyle = fileInfoStyle;
    }

    public String getFileInfoClass() {
        return ValueBindings.get(this, "fileInfoClass", fileInfoClass);
    }

    public void setFileInfoClass(String fileInfoClass) {
        this.fileInfoClass = fileInfoClass;
    }

    public String getInfoTitleClass() {
        return ValueBindings.get(this, "infoTitleClass", infoTitleClass);
    }

    public void setInfoTitleClass(String infoTitleClass) {
        this.infoTitleClass = infoTitleClass;
    }

    public String getInfoTitleStyle() {
        return ValueBindings.get(this, "infoTitleStyle", infoTitleStyle);
    }

    public void setInfoTitleStyle(String infoTitleStyle) {
        this.infoTitleStyle = infoTitleStyle;
    }

    public String getStatusNotUploadedText() {
        return ValueBindings.get(this, "statusNotUploadedText", statusNotUploadedText);
    }

    public void setStatusNotUploadedText(String statusNotUploadedText) {
        this.statusNotUploadedText = statusNotUploadedText;
    }

    public String getStatusUploadedText() {
        return ValueBindings.get(this, "infoStatusLabelUploaded", statusUploadedText, "Uploaded");
    }

    public void setStatusUploadedText(String statusUploadedText) {
        this.statusUploadedText = statusUploadedText;
    }

    public String getStatusInProgressText() {
        return ValueBindings.get(this, "statusInProgressText", statusInProgressText, "Uploading...");
    }

    public void setStatusInProgressText(String statusInProgressText) {
        this.statusInProgressText = statusInProgressText;
    }

    public String getMaxFileSizeErrorText() {
        return ValueBindings.get(this, "maxFileSizeErrorText", maxFileSizeErrorText, "File size limit exceeded");
    }

    public void setMaxFileSizeErrorText(String maxFileSizeErrorText) {
        this.maxFileSizeErrorText = maxFileSizeErrorText;
    }

    public String getInfoStatusClass() {
        return infoStatusClass;
    }

    public void setInfoStatusClass(String infoStatusClass) {
        this.infoStatusClass = infoStatusClass;
    }

    public String getInfoStatusStyle() {
        return ValueBindings.get(this, "infoStatusStyle", infoStatusStyle);
    }

    public void setInfoStatusStyle(String infoStatusStyle) {
        this.infoStatusStyle = infoStatusStyle;
    }

    public int getMinQuantity() {
        return ValueBindings.get(this, "minQuantity", minQuantity, 1);
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getMaxQuantity() {
        return ValueBindings.get(this, "maxQuantity", maxQuantity, 0);
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getAcceptedFileTypes() {
        return ValueBindings.get(this, "acceptedTypesOfFile", acceptedFileTypes);
    }

    public void setAcceptedFileTypes(String acceptedFileTypes) {
        this.acceptedFileTypes = acceptedFileTypes;
    }

    public boolean isDuplicateAllowed() {
        return ValueBindings.get(this, "duplicateAllowed", duplicateAllowed, true);
    }

    public void setDuplicateAllowed(boolean duplicateAllowed) {
        this.duplicateAllowed = duplicateAllowed;
    }

    public boolean isAutoUpload() {
        return ValueBindings.get(this, "autoUpload", autoUpload, false);
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
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

    public String getBrowseButtonLabel() {
        return ValueBindings.get(this, "browseButtonLabel", browseButtonLabel);
    }

    public void setBrowseButtonLabel(String browseButtonLabel) {
        this.browseButtonLabel = browseButtonLabel;
    }

    public String getBrowseButtonOnMouseOverStyle() {
        return ValueBindings.get(this, "browseButtonOnMouseOverStyle", browseButtonOnMouseOverStyle);
    }

    public void setBrowseButtonOnMouseOverStyle(String browseButtonOnMouseOverStyle) {
        this.browseButtonOnMouseOverStyle = browseButtonOnMouseOverStyle;
    }

    public String getBrowseButtonOnMouseOverClass() {
        return ValueBindings.get(this, "browseButtonOnMouseOverClass", browseButtonOnMouseOverClass);
    }

    public void setBrowseButtonOnMouseOverClass(String browseButtonOnMouseOverClass) {
        this.browseButtonOnMouseOverClass = browseButtonOnMouseOverClass;
    }

    public String getBrowseButtonOnMouseDownStyle() {
        return ValueBindings.get(this, "browseButtonOnMouseDownStyle", browseButtonOnMouseDownStyle);
    }

    public void setBrowseButtonOnMouseDownStyle(String browseButtonOnMouseDownStyle) {
        this.browseButtonOnMouseDownStyle = browseButtonOnMouseDownStyle;
    }

    public String getBrowseButtonOnMouseDownClass() {
        return ValueBindings.get(this, "browseButtonOnMouseDownClass", browseButtonOnMouseDownClass);
    }

    public void setBrowseButtonOnMouseDownClass(String browseButtonOnMouseDownClass) {
        this.browseButtonOnMouseDownClass = browseButtonOnMouseDownClass;
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

    public String getBrowseButtonOnFocusStyle() {
        return ValueBindings.get(this, "browseButtonOnFocusStyle", browseButtonOnFocusStyle);
    }

    public void setBrowseButtonOnFocusStyle(String browseButtonOnFocusStyle) {
        this.browseButtonOnFocusStyle = browseButtonOnFocusStyle;
    }

    public String getBrowseButtonOnFocusClass() {
        return ValueBindings.get(this, "browseButtonOnFocusClass", browseButtonOnFocusClass);
    }

    public void setBrowseButtonOnFocusClass(String browseButtonOnFocusClass) {
        this.browseButtonOnFocusClass = browseButtonOnFocusClass;
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

    public String getStatusStoppedText() {
        return ValueBindings.get(this, "statusStoppedText", statusStoppedText, "Stopped");
    }

    public void setStatusStoppedText(String statusStoppedText) {
        this.statusStoppedText = statusStoppedText;
    }

    public boolean isMultiUpload() {
        return ValueBindings.get(this, "multiUpload", multiUpload, false);
    }

    public void setMultiUpload(boolean multiUpload) {
        this.multiUpload = multiUpload;
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
}
