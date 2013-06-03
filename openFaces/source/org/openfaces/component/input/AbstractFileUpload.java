/*
 * OpenFaces - JSF Component Library 3.0
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
import org.openfaces.component.Position;
import org.openfaces.event.FileUploadedListener;
import org.openfaces.event.UploadCompletionListener;
import org.openfaces.util.Components;
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

/**
 * @author andrii.loboda
 */
@ResourceDependencies({
        @ResourceDependency(name = "default.css", library = "openfaces"),
        @ResourceDependency(name = "jsf.js", library = "javax.faces")
})
public abstract class AbstractFileUpload extends OUIInputBase {
    protected static final List<String> EVENT_NAMES;
    static {
        List<String> eventNames = new ArrayList<String>(Arrays.asList("start", "end",
                "filestart", "fileinprogress", "fileend","wrongfiletype","directorydropped"));
        eventNames.addAll(OUIInputBase.EVENT_NAMES);
        EVENT_NAMES = Collections.unmodifiableList(eventNames);
    }
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

    private String fileNameClass;
    private String fileNameStyle;

    private String statusClass;
    private String statusStyle;
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

    private MethodExpression completionListener;

    private String onstart;
    private String onend;
    private String onfilestart;
    private String onfileinprogress;
    private String onfileend;
    private String onwrongfiletype;
    private String ondirectorydropped;

    private int fileSizeLimit;

    private Iterable<String> render;
    private String externalDropTarget;
    private String acceptedMimeTypes;

    private String directoryDroppedText;
    private String wrongFileTypeText;
    private String externalBrowseButton;
    private Boolean showInPopup;
    private String closeButtonText;

    public AbstractFileUpload() {
        setRendererType("org.openfaces.MultipleFileUploadRenderer");
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
                acceptedFileTypes,
                fileNameClass,
                fileNameStyle,
                statusClass,
                statusStyle,
                notUploadedStatusText,
                uploadedStatusText,
                inProgressStatusText,
                fileSizeLimitErrorText,
                progressBarStyle,
                progressBarClass,
                tabindex,
                saveAttachedState(context, fileUploadedListener),
                stoppedStatusText,
                saveAttachedState(context, completionListener),
                onstart,
                onend,
                onfilestart,
                onfileinprogress,
                onfileend,
                onwrongfiletype,
                ondirectorydropped,
                stoppingStatusText,
                unexpectedErrorText,
                fileSizeLimit,
                render,
                externalDropTarget,
                acceptedMimeTypes,
                directoryDroppedText,
                wrongFileTypeText,
                externalBrowseButton,
                showInPopup,
                closeButtonText
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
        acceptedFileTypes = (String) values[i++];
        fileNameClass = (String) values[i++];
        fileNameStyle = (String) values[i++];
        statusClass = (String) values[i++];
        statusStyle = (String) values[i++];
        notUploadedStatusText = (String) values[i++];
        uploadedStatusText = (String) values[i++];
        inProgressStatusText = (String) values[i++];
        fileSizeLimitErrorText = (String) values[i++];
        progressBarStyle = (String) values[i++];
        progressBarClass = (String) values[i++];
        tabindex = (Integer) values[i++];
        fileUploadedListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        stoppedStatusText = (String) values[i++];
        completionListener = (MethodExpression) restoreAttachedState(context, values[i++]);
        onstart = (String) values[i++];
        onend = (String) values[i++];
        onfilestart = (String) values[i++];
        onfileinprogress = (String) values[i++];
        onfileend = (String) values[i++];
        onwrongfiletype = (String) values[i++];
        ondirectorydropped = (String) values[i++];
        stoppingStatusText = (String) values[i++];
        unexpectedErrorText = (String) values[i++];
        fileSizeLimit = (Integer) values[i++];
        render = (Iterable<String>) values[i++];
        externalDropTarget = (String) values[i++];
        acceptedMimeTypes = (String) values[i++];
        directoryDroppedText = (String) values[i++];
        wrongFileTypeText = (String)values[i++];
        externalBrowseButton = (String) values[i++];
        showInPopup = (Boolean) values[i++];
        closeButtonText = (String) values[i++];
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
        return ValueBindings.get(this, "uploadedStatusText", uploadedStatusText, "{size} KB[KB]");
    }

    public void setUploadedStatusText(String uploadedStatusText) {
        this.uploadedStatusText = uploadedStatusText;
    }

    public String getInProgressStatusText() {
        return ValueBindings.get(this, "inProgressStatusText", inProgressStatusText, "{uploaded} of {size} KB[KB]");
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

    public String getStatusClass() {
        return ValueBindings.get(this, "statusClass", statusClass);
    }

    public void setStatusClass(String statusClass) {
        this.statusClass = statusClass;
    }

    public String getStatusStyle() {
        return ValueBindings.get(this, "statusStyle", statusStyle);
    }

    public void setStatusStyle(String statusStyle) {
        this.statusStyle = statusStyle;
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


    public MethodExpression getCompletionListener() {
        return completionListener;
    }

    public void setCompletionListener(MethodExpression completionListener) {
        this.completionListener = completionListener;
    }

    public void addCompletionListener(UploadCompletionListener completionListener) {
        addFacesListener(completionListener);
    }

    public UploadCompletionListener[] getCompletionListeners() {
        return (UploadCompletionListener[]) getFacesListeners(FileUploadedListener.class);
    }

    public void removeCompletionListener(UploadCompletionListener completionListener) {
        removeFacesListener(completionListener);
    }

    public String getOnstart() {
        return ValueBindings.get(this, "onstart", onstart);
    }

    public void setOnstart(String onstart) {
        this.onstart = onstart;
    }

    public String getOnend() {
        return ValueBindings.get(this, "onend", onend);
    }

    public void setOnend(String onend) {
        this.onend = onend;
    }

    public String getOnfilestart() {
        return ValueBindings.get(this, "filestart", onfilestart);
    }

    public void setOnfilestart(String onfilestart) {
        this.onfilestart = onfilestart;
    }

    public String getOnfileinprogress() {
        return ValueBindings.get(this, "fileinprogress", onfileinprogress);
    }

    public void setOnfileinprogress(String onfileinprogress) {
        this.onfileinprogress = onfileinprogress;
    }

    public String getOnfileend() {
        return ValueBindings.get(this, "fileend", onfileend);
    }

    public void setOnfileend(String onfileend) {
        this.onfileend = onfileend;
    }

    public String getOnwrongfiletype() {
        return ValueBindings.get(this, "wrongfiletype", onwrongfiletype);
    }

    public void setOnwrongfiletype(String onwrongfiletype) {
        this.onwrongfiletype = onwrongfiletype;
    }

    public String getOndirectorydropped() {
        return ValueBindings.get(this, "ondirectorydropped", ondirectorydropped);
    }

    public void setOndirectorydropped(String ondirectorydropped) {
        this.ondirectorydropped = ondirectorydropped;
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


    public Iterable<String> getRender() {
        return ValueBindings.get(this, "render", render, Iterable.class);
    }

    public void setRender(Iterable<String> render) {
        this.render = render;
    }

    public String getExternalDropTarget() {
        return ValueBindings.get(this, "externalDropTarget", externalDropTarget);
    }

    public void setExternalDropTarget(String externalDropTarget) {
        this.externalDropTarget = externalDropTarget;
    }
    @Override
    public String getDefaultEventName() {
        return "end";
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    public String getAcceptedMimeTypes() {
        return ValueBindings.get(this, "acceptedMimeTypes", acceptedMimeTypes);
    }

    public void setAcceptedMimeTypes(String acceptedMimeTypes) {
        this.acceptedMimeTypes = acceptedMimeTypes;
    }

    public String getDirectoryDroppedText() {
        return ValueBindings.get(this, "directoryDroppedText", directoryDroppedText, "Can't upload folder");
    }

    public void setDirectoryDroppedText(String directoryDroppedText) {
        this.directoryDroppedText = directoryDroppedText;
    }

    public String getWrongFileTypeText() {
        return ValueBindings.get(this, "wrongFileTypeText", wrongFileTypeText, "Wrong type of file");
    }

    public void setWrongFileTypeText(String wrongFileTypeText) {
        this.wrongFileTypeText = wrongFileTypeText;
    }

    public String getExternalBrowseButton() {
        return ValueBindings.get(this, "externalBrowseButton", externalBrowseButton);
    }

    public void setExternalBrowseButton(String externalBrowseButton) {
        this.externalBrowseButton = externalBrowseButton;
    }

    public boolean getShowInPopup() {
        return ValueBindings.get(this, "showInPopup", showInPopup, false);
    }

    public void setShowInPopup(boolean showInPopup) {
        this.showInPopup = showInPopup;
    }

    public String getCloseButtonText() {
        return ValueBindings.get(this, "closeButtonText", closeButtonText, "Close");
    }

    public void setCloseButtonText(String closeButtonText) {
        this.closeButtonText = closeButtonText;
    }

    public Position getPosition() {
        Position position = Components.findChildWithClass(this, Position.class, "<o:position>");
        if (position == null)
            position = new Position();
        return position;
    }
}
