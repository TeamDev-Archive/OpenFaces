/*
 * OpenFaces - JSF Component Library 3.0
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
import org.openfaces.util.ValueBindings;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import java.io.File;
import java.util.List;


public class FileUpload extends OUIInputBase {
    public static final String COMPONENT_TYPE = "org.openfaces.FileUpload";
    public static final String COMPONENT_FAMILY = "org.openfaces.FileUpload";
    public static final String DEF_CANCEL_LABEL = "Cancel";

    private int minQuantity;
    private int maxQuantity;

    private String headerStyle;
    private String headerClass;

    private String addButtonStyle;
    private String addButtonClass;
    private String addButtonLabel;

    private String addButtonOnMouseOverStyle;
    private String addButtonOnMouseOverClass;

    private String addButtonOnMouseDownStyle;
    private String addButtonOnMouseDownClass;

    private String addButtonOnFocusStyle;
    private String addButtonOnFocusClass;

    private String addButtonDisabledStyle;
    private String addButtonDisabledClass;

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

    //private MethodExpression uploadListener;
    private MethodExpression fileUploadedListener;

    private List<File> uploadedFiles;

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
                addButtonStyle,
                addButtonClass,
                addButtonLabel,
                addButtonOnMouseOverStyle,
                addButtonOnMouseOverClass,
                addButtonOnMouseDownStyle,
                addButtonOnMouseDownClass,
                addButtonOnFocusStyle,
                addButtonOnFocusClass,
                addButtonDisabledStyle,
                addButtonDisabledClass,
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
                //saveAttachedState(context, uploadListener),
                saveAttachedState(context, fileUploadedListener)
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
        addButtonStyle = (String) values[i++];
        addButtonClass = (String) values[i++];
        addButtonLabel = (String) values[i++];
        addButtonOnMouseOverStyle = (String) values[i++];
        addButtonOnMouseOverClass = (String) values[i++];
        addButtonOnMouseDownStyle = (String) values[i++];
        addButtonOnMouseDownClass = (String) values[i++];
        addButtonOnFocusStyle = (String) values[i++];
        addButtonOnFocusClass = (String) values[i++];
        addButtonDisabledStyle = (String) values[i++];
        addButtonDisabledClass = (String) values[i++];
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
        //uploadListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        fileUploadedListener = (MethodExpression) restoreAttachedState(context, values[i++]);
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
        return ValueBindings.get(this, "maxFileSizeErrorText", maxFileSizeErrorText, "Error: size of file is too big");
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

    public List<File> getUploadedFiles() {
        return ValueBindings.get(this, "uploadedFiles", uploadedFiles, List.class);
    }

    public void setUploadedFiles(List<File> uploadedFiles) {
        ValueBindings.set(this, "uploadedFiles", uploadedFiles);
    }

    public String getAcceptedFileTypes() {
        return ValueBindings.get(this, "acceptedTypesOfFile", acceptedFileTypes);
    }

    public void setAcceptedFileTypes(String acceptedFileTypes) {
        this.acceptedFileTypes = acceptedFileTypes;
    }

    public boolean isDuplicateAllowed() {
        return ValueBindings.get(this, "duplicateAllowed", duplicateAllowed, false);
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

    public String getAddButtonStyle() {
        return ValueBindings.get(this, "addButtonStyle", addButtonStyle);
    }

    public void setAddButtonStyle(String addButtonStyle) {
        this.addButtonStyle = addButtonStyle;
    }

    public String getAddButtonClass() {
        return ValueBindings.get(this, "addButtonClass", addButtonClass);
    }

    public void setAddButtonClass(String addButtonClass) {
        this.addButtonClass = addButtonClass;
    }

    public String getAddButtonLabel() {
        return ValueBindings.get(this, "addButtonLabel", addButtonLabel, "Add file...");
    }

    public void setAddButtonLabel(String addButtonLabel) {
        this.addButtonLabel = addButtonLabel;
    }

    public String getAddButtonOnMouseOverStyle() {
        return ValueBindings.get(this, "addButtonOnMouseOverStyle", addButtonOnMouseOverStyle);
    }

    public void setAddButtonOnMouseOverStyle(String addButtonOnMouseOverStyle) {
        this.addButtonOnMouseOverStyle = addButtonOnMouseOverStyle;
    }

    public String getAddButtonOnMouseOverClass() {
        return ValueBindings.get(this, "addButtonOnMouseOverClass", addButtonOnMouseOverClass);
    }

    public void setAddButtonOnMouseOverClass(String addButtonOnMouseOverClass) {
        this.addButtonOnMouseOverClass = addButtonOnMouseOverClass;
    }

    public String getAddButtonOnMouseDownStyle() {
        return ValueBindings.get(this, "addButtonOnMouseDownStyle", addButtonOnMouseDownStyle);
    }

    public void setAddButtonOnMouseDownStyle(String addButtonOnMouseDownStyle) {
        this.addButtonOnMouseDownStyle = addButtonOnMouseDownStyle;
    }

    public String getAddButtonOnMouseDownClass() {
        return ValueBindings.get(this, "addButtonOnMouseDownClass", addButtonOnMouseDownClass);
    }

    public void setAddButtonOnMouseDownClass(String addButtonOnMouseDownClass) {
        this.addButtonOnMouseDownClass = addButtonOnMouseDownClass;
    }

    public String getAddButtonDisabledStyle() {
        return ValueBindings.get(this, "addButtonDisabledStyle", addButtonDisabledStyle);
    }

    public void setAddButtonDisabledStyle(String addButtonDisabledStyle) {
        this.addButtonDisabledStyle = addButtonDisabledStyle;
    }

    public String getAddButtonDisabledClass() {
        return ValueBindings.get(this, "addButtonDisabledClass", addButtonDisabledClass);
    }

    public void setAddButtonDisabledClass(String addButtonDisabledClass) {
        this.addButtonDisabledClass = addButtonDisabledClass;
    }

    public String getAddButtonOnFocusStyle() {
        return ValueBindings.get(this, "addButtonOnFocusStyle", addButtonOnFocusStyle);
    }

    public void setAddButtonOnFocusStyle(String addButtonOnFocusStyle) {
        this.addButtonOnFocusStyle = addButtonOnFocusStyle;
    }

    public String getAddButtonOnFocusClass() {
        return ValueBindings.get(this, "addButtonOnFocusClass", addButtonOnFocusClass);
    }

    public void setAddButtonOnFocusClass(String addButtonOnFocusClass) {
        this.addButtonOnFocusClass = addButtonOnFocusClass;
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
}
