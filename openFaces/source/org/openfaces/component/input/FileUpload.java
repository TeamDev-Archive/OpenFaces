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
import org.openfaces.event.UploadCompletionListener;
import org.openfaces.util.ValueBindings;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(name = "default.css", library = "openfaces"),
        @ResourceDependency(name = "jsf.js", library = "javax.faces")
})
public class FileUpload extends OUIInputBase {
    protected static final List<String> EVENT_NAMES;
    static {
        List<String> eventNames = new ArrayList<String>(Arrays.asList("uploadstart", "uploadEnd"));
        eventNames.addAll(OUIInputBase.EVENT_NAMES);
        EVENT_NAMES = Collections.unmodifiableList(eventNames);
    }

    public static final String COMPONENT_TYPE = "org.openfaces.FileUpload";
    public static final String COMPONENT_FAMILY = "org.openfaces.FileUpload";

    private int minQuantity;
    private int maxQuantity;

    private String headerStyle;
    private String headerClass;

    private String browseButtonStyle;
    private String browseButtonClass;
    private String browseButtonText;

    private String browseButtonRolloverStyle;
    private String browseButtonRolloverClass;

    private String browseButtonPressedStyle;
    private String browseButtonPressedClass;

    private String browseButtonFocusedStyle;
    private String browseButtonFocusedClass;

    private String browseButtonDisabledStyle;
    private String browseButtonDisabledClass;

    private String allInfosStyle;
    private String allInfosClass;

    private String rowStyle;
    private String rowClass;

    private String fileNameClass;
    private String fileNameStyle;

    private String uploadStatusClass;
    private String uploadStatusStyle;
    private String notUploadedStatusText;
    private String uploadedStatusText;
    private String inProgressStatusText;
    private String fileSizeLimitErrorText;

    private String acceptedFileTypes;

    private boolean duplicateAllowed;
    private boolean autoUpload;

    private String progressBarStyle;
    private String progressBarClass;

    private int tabindex;

    private String stoppedStatusText;
    private MethodExpression fileUploadedListener;
    private boolean multiple;

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
                browseButtonText,
                browseButtonRolloverStyle,
                browseButtonRolloverClass,
                browseButtonPressedStyle,
                browseButtonPressedClass,
                browseButtonFocusedStyle,
                browseButtonFocusedClass,
                browseButtonDisabledStyle,
                browseButtonDisabledClass,
                allInfosStyle,
                allInfosClass,
                rowStyle,
                rowClass,
                acceptedFileTypes,
                duplicateAllowed,
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
                multiple,
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
        browseButtonText = (String) values[i++];
        browseButtonRolloverStyle = (String) values[i++];
        browseButtonRolloverClass = (String) values[i++];
        browseButtonPressedStyle = (String) values[i++];
        browseButtonPressedClass = (String) values[i++];
        browseButtonFocusedStyle = (String) values[i++];
        browseButtonFocusedClass = (String) values[i++];
        browseButtonDisabledStyle = (String) values[i++];
        browseButtonDisabledClass = (String) values[i++];
        allInfosStyle = (String) values[i++];
        allInfosClass = (String) values[i++];
        rowStyle = (String) values[i++];
        rowClass = (String) values[i++];
        acceptedFileTypes = (String) values[i++];
        duplicateAllowed = (Boolean) values[i++];
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
        multiple = (Boolean) values[i++];
        uploadCompletionListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        onuploadstart = (String) values[i++];
        onuploadend = (String) values[i++];
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
        return ValueBindings.get(this, "uploadedStatusText", uploadedStatusText, "Uploaded");
    }

    public void setUploadedStatusText(String uploadedStatusText) {
        this.uploadedStatusText = uploadedStatusText;
    }

    public String getInProgressStatusText() {
        return ValueBindings.get(this, "inProgressStatusText", inProgressStatusText, "Uploading...");
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

    public String getBrowseButtonText() {
        return ValueBindings.get(this, "browseButtonText", browseButtonText);
    }

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

    public boolean isMultiple() {
        return ValueBindings.get(this, "multiple", multiple, false);
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
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

    @Override
    public String getDefaultEventName() {
        return "uploadEnd";
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

}
