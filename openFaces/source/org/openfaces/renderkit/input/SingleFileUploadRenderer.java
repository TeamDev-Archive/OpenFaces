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
package org.openfaces.renderkit.input;

import org.openfaces.component.Position;
import org.openfaces.component.input.AbstractFileUpload;
import org.openfaces.component.input.SingleFileUpload;
import org.openfaces.component.input.SingleFileUploadBtnBehavior;
import org.openfaces.component.input.SingleFileUploadLayoutMode;
import org.openfaces.component.output.ProgressBar;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class SingleFileUploadRenderer extends AbstractFileUploadRenderer {
    private static final String DIV_FOR_FILE_INFO_ID = "::fileInfo";
    private static final String ACTION_BUTTON_CONTAINER_ID = "::actionButtonContainer";
    private static final String DEFAULT_STOP_URL = "input/fileUpload-stop.png";
    private static final String STOP_ICO_STYLE_MIN = "o_s_file_clear_icon_min";
    private static final String STOP_ICO_STYLE_FULL = "o_s_file_clear_icon_full";
    private static final String DEFAULT_UPLOADED_MIN_URL = "output/uploadedProgressBarMin.png";
    private SingleFileUploadLayoutMode layoutMode;

    @Override
    protected void writeStructure(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String clientId) throws IOException {
        SingleFileUpload fileUpload = (SingleFileUpload) abstractFileUpload;
        layoutMode = fileUpload.getLayoutMode();
        setLayoutSettings(fileUpload, layoutMode);
        boolean showInPopup = fileUpload.getShowInPopup();
        boolean hasExternalBrowseButton = fileUpload.getExternalBrowseButton() != null;

        String defaultStyleClass = (layoutMode == SingleFileUploadLayoutMode.FULL ? "o_s_file_upload" : "o_s_file_upload_compact")
                + (showInPopup ? " o_file_upload_popup" : "");
        Rendering.writeStyleAndClassAttributes(writer, fileUpload.getStyle(), fileUpload.getStyleClass(),
                defaultStyleClass);

        writer.startElement("table", fileUpload);
        writer.writeAttribute("style", "width:100%", null);

        writer.startElement("tr", fileUpload);

        if (!showInPopup && !hasExternalBrowseButton) {
            writer.startElement("td", fileUpload);
            writer.writeAttribute("id", clientId + ACTION_BUTTON_CONTAINER_ID, null);
            writeBrowseButton(context, writer, clientId, fileUpload);
            writer.endElement("td");
        }
        writer.startElement("td", fileUpload);
        writer.writeAttribute("style", "width:100%", null);
        writeFileInfo(context, fileUpload, writer, clientId + DIV_FOR_FILE_INFO_ID);
        writer.endElement("td");
        if (showInPopup || hasExternalBrowseButton) {
            writer.startElement("td", fileUpload);
            writer.writeAttribute("id", clientId + ACTION_BUTTON_CONTAINER_ID, null);
            writer.endElement("td");
        }
        writer.endElement("tr");

        writer.startElement("tr", fileUpload);
        writer.startElement("td", fileUpload);
        if (layoutMode == SingleFileUploadLayoutMode.FULL) {
            writer.writeAttribute("colspan", 2, null);
        }
        writeProgressArea(context, fileUpload, writer);
        writer.endElement("td");
        writer.endElement("tr");

        writer.endElement("table");
        // Ignore for popup without external drop target
        if (!showInPopup || abstractFileUpload.getExternalDropTarget() != null)
            writeDragAndDrop(context, abstractFileUpload, writer, clientId, fileUpload);

        if (showInPopup || hasExternalBrowseButton) {
            writer.startElement("div", fileUpload);
            writer.writeAttribute("style", "display:none", null);
            writeBrowseButton(context, writer, clientId, fileUpload);
            writer.endElement("div");
        }
        writeHelpfulElements(context, fileUpload, writer, clientId + HELP_ELEMENTS_ID);

    }

    private void setLayoutSettings(SingleFileUpload fileUpload, SingleFileUploadLayoutMode mode) {
        if (fileUpload.getShowInfoAfterUpload() == null) {
            if (mode == SingleFileUploadLayoutMode.FULL) {
                fileUpload.setShowInfoAfterUpload(true);
            } else {
                fileUpload.setShowInfoAfterUpload(false);
            }
        }
        if (fileUpload.getBrowseButtonDuringUpload() == null) {
            if (mode == SingleFileUploadLayoutMode.FULL) {
                fileUpload.setBrowseButtonDuringUpload(SingleFileUploadBtnBehavior.SHOW_STOP);
            } else {
                fileUpload.setBrowseButtonDuringUpload(SingleFileUploadBtnBehavior.HIDE);
            }
        }
        if (fileUpload.getStopButtonNearProgress() == null) {
            if (mode == SingleFileUploadLayoutMode.FULL) {
                fileUpload.setStopButtonNearProgress(false);
            } else {
                fileUpload.setStopButtonNearProgress(true);
            }
        }
    }

    private void writeBrowseButton(FacesContext context, ResponseWriter writer, String clientId, SingleFileUpload fileUpload) throws IOException {
        String browseBtnText = fileUpload.getBrowseButtonText();
        if (browseBtnText == null) {
            browseBtnText = "Upload...";
        }
        writeBrowseButtonTable(context, fileUpload, writer, clientId + BROWSE_BTN_ID, browseBtnText);
    }

    private void writeDragAndDrop(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String clientId, SingleFileUpload fileUpload) throws IOException {
        String dropTargetText = fileUpload.getDropTargetText();
        if (dropTargetText == null) {
            switch (layoutMode) {
                case COMPACT:
                    dropTargetText = "Drop file";
                    break;
                case FULL:
                    dropTargetText = "Drop file here";
                    break;
            }
        }
        writeDragAndDropArea(context, abstractFileUpload, writer, clientId + DRAG_AREA,
                (fileUpload.getExternalDropTarget() == null)
                        ? ((layoutMode == SingleFileUploadLayoutMode.FULL) ? "o_s_file_drop_target_full" : "o_s_file_drop_target_min")
                        : "o_s_file_ext_drop_target",
                dropTargetText);
    }

    private void writeProgressArea(FacesContext context, AbstractFileUpload fileUpload, ResponseWriter writer) throws IOException {
        String progressBarClass = Styles.getCSSClass(context, fileUpload, fileUpload.getProgressBarStyle(), StyleGroup.regularStyleGroup(), fileUpload.getProgressBarClass(), null);
        writer.writeAttribute("class", progressBarClass, null);
        writeProgressBar(context, fileUpload);
    }

    private void writeFileInfo(FacesContext context, SingleFileUpload fileUpload, ResponseWriter writer, String clientId) throws IOException {
        String fileInfoClass = Styles.getCSSClass(context, fileUpload, fileUpload.getFileInfoAreaStyle(), StyleGroup.regularStyleGroup(), fileUpload.getFileInfoAreaClass(), "o_file_upload_info");
        String infoTitleClass = Styles.getCSSClass(context, fileUpload, fileUpload.getFileNameStyle(), StyleGroup.regularStyleGroup(), fileUpload.getFileNameClass(), "o_s_file_upload_info_title");
        String infoStatusClass = Styles.getCSSClass(context, fileUpload, fileUpload.getStatusStyle(), StyleGroup.regularStyleGroup(), fileUpload.getStatusClass(), "o_s_file_upload_info_status");

        writer.startElement("table", fileUpload);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", fileInfoClass, null);
        writer.startElement("tr", fileUpload);

        writer.startElement("td", fileUpload);
        writer.startElement("div", fileUpload);
        writer.writeAttribute("class", infoTitleClass, null);
        writer.endElement("div");
        writer.endElement("td");

        writer.startElement("td", fileUpload);
        writer.writeAttribute("class", infoStatusClass, null);
        writer.endElement("td");

        writer.endElement("tr");

        writer.endElement("table");
    }

    @Override
    protected void writeProgressBar(FacesContext context, AbstractFileUpload fileUpload) throws IOException {
        if (progressBar == null) {
            progressBar = new ProgressBar();
            progressBar.setDefaultProgressImgUrl(DEFAULT_UPLOADED_MIN_URL);
        }
        progressBar.setStyleClass(Styles.getCSSClass(context, progressBar, progressBar.getStyle(), StyleGroup.regularStyleGroup(), progressBar.getStyleClass(), getProgressBarStyle(((SingleFileUpload) fileUpload))));
        progressBar.setLabelClass(Styles.getCSSClass(context, progressBar, progressBar.getLabelStyle(), StyleGroup.regularStyleGroup(), progressBar.getLabelClass(), "o_s_file_upload_progress_label"));
        progressBar.encodeAll(context);
    }

    private String getProgressBarStyle(SingleFileUpload fileUpload) {
        switch (layoutMode) {
            case COMPACT:
                if (fileUpload.getBrowseButtonDuringUpload() == SingleFileUploadBtnBehavior.HIDE && !fileUpload.getShowInPopup()) {
                    return "o_s_fileup_pro_bar_min";
                } else {
                    return "o_s_fileup_pro_bar_compact";
                }
            case FULL:
                return "o_s_fileup_pro_bar";
        }
        return null;
    }

    @Override
    protected void encodeScriptAndStyles(FacesContext context, AbstractFileUpload abstractFileUpload, String clientId, String uniqueId) throws IOException {
        SingleFileUpload fileUpload = (SingleFileUpload) abstractFileUpload;
        String addButtonClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonClass(), null);
        String addButtonOnMouseOverClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonRolloverStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonRolloverClass(), null);
        String addButtonOnMouseDownClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonPressedStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonPressedClass(), null);
        String addButtonOnFocusClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonFocusedStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonFocusedClass(), null);
        String addButtonDisabledClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonDisabledStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonDisabledClass(), "o_file_upload_addBtn_dis");
        String dropTargetDragoverClass = Styles.getCSSClass(context, fileUpload, fileUpload.getDropTargetDragoverStyle(), StyleGroup.regularStyleGroup(), fileUpload.getDropTargetDragoverClass(), "o_file_drop_target_dragover");
        Styles.renderStyleClasses(context, fileUpload);

        int uploadedSize = 0;
        boolean duplicateAllowed = true;//fileUpload.isDuplicateAllowed();

        Position popupPosition = fileUpload.getPosition();

        List<String> listOfImages = new LinkedList<String>();
        String defStopUrl = null;
        if (fileUpload.getStopButtonNearProgress()) {
            if (stopButton == null) {
                defStopUrl = Resources.getURL(context, null, DEFAULT_STOP_URL);
                listOfImages.add(defStopUrl);
                Rendering.renderPreloadImagesScript(context, listOfImages, false);
            }
        }
        Script initScript = new ScriptBuilder().initScript(context, fileUpload, "O$.SingleFileUpload._init",
                uploadedSize,
                fileUpload.getNotUploadedStatusText(),
                fileUpload.getInProgressStatusText(),
                fileUpload.getUploadedStatusText(),
                fileUpload.getFileSizeLimitErrorText(),
                fileUpload.getUnexpectedErrorText(),
                fileUpload.getAcceptedFileTypes(),
                duplicateAllowed,
                clientId + BROWSE_BTN_ID,
                addButtonClass,
                addButtonOnMouseOverClass,
                addButtonOnMouseDownClass,
                addButtonOnFocusClass,
                addButtonDisabledClass,
                fileUpload.isDisabled(),
                fileUpload.getTabindex(),
                progressBar.getClientId(context),
                fileUpload.getStoppedStatusText(),
                fileUpload.getStoppingStatusText(),
                uniqueId,
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "change")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "start")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "end")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "uploadstart")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "uploadinprogress")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "uploadend")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "wrongfileadded")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "directorydropped")),
                dropTargetDragoverClass,
                getRender(context, fileUpload),
                getExternalDropTargetId(context, fileUpload),
                fileUpload.getAcceptedMimeTypes(),
                fileUpload.getLayoutMode(),
                defStopUrl,
                getIconStyle(fileUpload, layoutMode),
                fileUpload.getShowInfoAfterUpload(),
                fileUpload.getBrowseButtonDuringUpload(),
                fileUpload.getStopButtonNearProgress(),
                fileUpload.getDirectoryDroppedText(),
                fileUpload.getWrongFileTypeText(),
                getExternalButtonId(context, fileUpload),
                fileUpload.getShowInPopup(),
                getPositionedBy(context, fileUpload),
                popupPosition.getHorizontalAlignment(),
                popupPosition.getVerticalAlignment(),
                popupPosition.getHorizontalDistance(),
                popupPosition.getVerticalDistance()
        );

        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.jsonJsURL(context),
                Resources.internalURL(context, "input/singleFileUpload.js"),
                Resources.internalURL(context, "input/fileUploadUtil.js")
        );
    }

    @Override
    protected void writeHelpfulButtons(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter
            writer, String elementId) throws IOException {
        SingleFileUpload fileUpload = (SingleFileUpload) abstractFileUpload;
        if (fileUpload.getStopButtonNearProgress() && fileUpload.getBrowseButtonDuringUpload() != SingleFileUploadBtnBehavior.SHOW_STOP) {
            facetRenderer.writeDivByDefault(stopButton, elementId + STOP_BTN_CONTAINER, "", getIconStyle(fileUpload, layoutMode));
        } else {
            String stopButtonClass = (fileUpload.getShowInPopup() || fileUpload.getExternalBrowseButton() != null )
                    ? "o_s_close_popup_btn" : "o_s_file_clear_btn";
            facetRenderer.writeButtonByDefault(stopButton, elementId + STOP_BTN_CONTAINER, abstractFileUpload.getStopButtonText(), stopButtonClass);
        }
        if (abstractFileUpload.getShowInPopup())
            facetRenderer.writeButtonByDefault(closeButton, elementId + CLOSE_BTN_CONTAINER, fileUpload.getCloseButtonText(), "o_s_close_popup_btn");
    }

    private String getIconStyle(SingleFileUpload fileUpload, SingleFileUploadLayoutMode layoutMode) {
        return (layoutMode == SingleFileUploadLayoutMode.FULL) ? STOP_ICO_STYLE_FULL
                : (fileUpload.getBrowseButtonDuringUpload() == SingleFileUploadBtnBehavior.HIDE && !fileUpload.getShowInPopup()
                ? STOP_ICO_STYLE_MIN : STOP_ICO_STYLE_FULL);
    }
}
