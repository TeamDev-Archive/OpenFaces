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

import org.openfaces.component.input.AbstractFileUpload;
import org.openfaces.component.input.SingleFileUpload;
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
    public static final String DEFAULT_STOP_URL = "input/fileUpload-stop.png";
    public static final String STOP_ICO_STYLE_MIN = "o_s_file_clear_btn_min";
    private SingleFileUploadLayoutMode layoutMode;

    @Override
    protected void writeStructure(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String clientId) throws IOException {
        SingleFileUpload fileUpload = (SingleFileUpload) abstractFileUpload;
        layoutMode = fileUpload.getLayoutMode();
        switch (layoutMode) {
            case FULL:
                Rendering.writeStyleAndClassAttributes(writer, fileUpload.getStyle(), fileUpload.getStyleClass(), "o_s_file_upload");

                writer.startElement("table", fileUpload);
                writer.startElement("tr", fileUpload);
                writer.startElement("td", fileUpload);

                writeBrowseButton(context, writer, clientId, fileUpload);
                writer.endElement("td");
                writer.startElement("td", fileUpload);
                writer.writeAttribute("style", "width:100%", null);
                writeFileInfo(context, fileUpload, writer, clientId + DIV_FOR_FILE_INFO_ID);
                writeDragAndDrop(context, abstractFileUpload, writer, clientId, fileUpload);
                writer.endElement("td");

                writer.endElement("tr");

                writer.startElement("tr", fileUpload);
                writer.startElement("td", fileUpload);
                writeProgressArea(context, fileUpload, writer);
                writer.endElement("td");
                writer.endElement("tr");

                writer.endElement("table");

                writeHelpfulElements(context, fileUpload, writer, clientId + HELP_ELEMENTS_ID);
                break;
            case COMPACT:
                Rendering.writeStyleAndClassAttributes(writer, fileUpload.getStyle(), fileUpload.getStyleClass(), "o_s_file_upload_compact");

                writer.startElement("table", fileUpload);
                writer.startElement("tr", fileUpload);
                writer.startElement("td", fileUpload);
                writer.writeAttribute("style", "padding:0", null);

                writeBrowseButton(context, writer, clientId, fileUpload);
                writeDragAndDrop(context, abstractFileUpload, writer, clientId, fileUpload);
                writer.endElement("td");

                writer.endElement("tr");

                writer.startElement("tr", fileUpload);
                writer.startElement("td", fileUpload);
                writer.writeAttribute("style", "padding:0", null);
                writeProgressArea(context, fileUpload, writer);
                writer.endElement("td");
                writer.endElement("tr");

                writer.endElement("table");

                writeHelpfulElements(context, fileUpload, writer, clientId + HELP_ELEMENTS_ID);
                break;
            case MINIMALISTIC:
                Rendering.writeStyleAndClassAttributes(writer, fileUpload.getStyle(), fileUpload.getStyleClass(), "o_s_file_upload_compact");

                writer.startElement("table", fileUpload);
                writer.startElement("tr", fileUpload);
                writer.startElement("td", fileUpload);
                writer.writeAttribute("style", "padding:0", null);
                writeBrowseButton(context, writer, clientId, fileUpload);
                writeDragAndDrop(context, abstractFileUpload, writer, clientId, fileUpload);
                writer.endElement("td");
                writer.endElement("tr");

                writer.endElement("table");

                writeHelpfulElements(context, fileUpload, writer, clientId + HELP_ELEMENTS_ID);
                break;
        }

    }

    private void writeBrowseButton(FacesContext context, ResponseWriter writer, String clientId, SingleFileUpload fileUpload) throws IOException {
        String browseBtnText = fileUpload.getBrowseButtonText();
        if (browseBtnText == null){
            browseBtnText = "Upload..."; 
        }
        writeBrowseButtonTable(context, fileUpload, writer, clientId + BROWSE_BTN_ID, browseBtnText);
    }

    private void writeDragAndDrop(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String clientId, SingleFileUpload fileUpload) throws IOException {
        String dropTargetText = fileUpload.getDropTargetText();
        if (dropTargetText == null) {
            switch (layoutMode) {
                case FULL:
                    dropTargetText = "Drop file here";
                    break;
                case COMPACT:
                    dropTargetText = "Drop file";
                    break;
                case MINIMALISTIC:
                    dropTargetText = "Drop file";
                    break;
            }
        }
        writeDragAndDropArea(context, abstractFileUpload, writer, clientId + DRAG_AREA,
                (fileUpload.getExternalDropTarget() == null) ? "o_s_file_drop_target" : "o_s_file_ext_drop_target",
                dropTargetText);
    }

    private void writeProgressArea(FacesContext context, AbstractFileUpload fileUpload, ResponseWriter writer) throws IOException {
        String progressBarClass = Styles.getCSSClass(context, fileUpload, fileUpload.getProgressBarStyle(), StyleGroup.regularStyleGroup(), fileUpload.getProgressBarClass(), "o_s_file_upload_info_progress");
        writer.writeAttribute("class", progressBarClass, null);
        writer.writeAttribute("colspan", 2, null);
        writeProgressBar(context);
    }

    private void writeFileInfo(FacesContext context, SingleFileUpload fileUpload, ResponseWriter writer, String clientId) throws IOException {
        String fileInfoClass = Styles.getCSSClass(context, fileUpload, fileUpload.getRowStyle(), StyleGroup.regularStyleGroup(), fileUpload.getRowClass(), "o_file_upload_info");
        String infoTitleClass = Styles.getCSSClass(context, fileUpload, fileUpload.getFileNameStyle(), StyleGroup.regularStyleGroup(), fileUpload.getFileNameClass(), "o_file_upload_info_title");
        String infoStatusClass = Styles.getCSSClass(context, fileUpload, fileUpload.getUploadStatusStyle(), StyleGroup.regularStyleGroup(), fileUpload.getUploadStatusClass(), "o_s_file_upload_info_status");

        writer.startElement("table", fileUpload);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", fileInfoClass, null);
        writer.startElement("tr", fileUpload);

        writer.startElement("td", fileUpload);
        writer.writeAttribute("class", infoTitleClass, null);
        writer.endElement("td");

        writer.startElement("td", fileUpload);
        writer.writeAttribute("class", infoStatusClass, null);
        writer.endElement("td");

        writer.endElement("tr");

        writer.endElement("table");
    }

    @Override
    protected void writeProgressBar(FacesContext context) throws IOException {
        if (progressBar == null) {
            progressBar = new ProgressBar();
        }
        progressBar.setStyleClass(Styles.getCSSClass(context, progressBar, progressBar.getStyle(), StyleGroup.regularStyleGroup(), progressBar.getStyleClass(), getProgressBarStyle()));
        progressBar.setLabelClass(Styles.getCSSClass(context, progressBar, progressBar.getLabelStyle(), StyleGroup.regularStyleGroup(), progressBar.getLabelClass(), "o_s_file_upload_progress_label"));
        progressBar.encodeAll(context);
    }
    
    private String getProgressBarStyle(){
        switch (layoutMode){
            case COMPACT:
                return "o_s_fileup_pro_bar_compact";
            case MINIMALISTIC:
                return "o_s_fileup_pro_bar_min";
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

        List<String> listOfImages = new LinkedList<String>();
        String defStopUrl = null;
        if (layoutMode == SingleFileUploadLayoutMode.MINIMALISTIC && stopButton == null){
            defStopUrl = Resources.getURL(context,  null, DEFAULT_STOP_URL);
            listOfImages.add(defStopUrl);
        }
        Rendering.renderPreloadImagesScript(context, listOfImages, false);

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
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "uploadstart")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "uploadend")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "fileuploadstart")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "fileuploadinprogress")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(fileUpload, "fileuploadend")),
                dropTargetDragoverClass,
                (fileUpload.getRenderAfterUpload() == null) ? null : Utilities.getForm(fileUpload).getClientId(context) + ":" + fileUpload.getRenderAfterUpload(),
                fileUpload.getExternalDropTarget(),
                fileUpload.getAcceptDialogFormats(),
                fileUpload.getLayoutMode(),
                defStopUrl,
                STOP_ICO_STYLE_MIN
        );

        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.jsonJsURL(context),
                Resources.internalURL(context, "input/singleFileUpload.js"),
                Resources.internalURL(context, "input/fileUploadUtil.js")
        );
    }

    @Override
    protected void writeHelpfulButtons(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String elementId) throws IOException {
        if (layoutMode == SingleFileUploadLayoutMode.MINIMALISTIC){
            writeProgressArea(context, abstractFileUpload, writer);
            facetRenderer.writeDivByDefault(stopButton, elementId + STOP_BTN_CONTAINER, "", STOP_ICO_STYLE_MIN);
        }else{
            facetRenderer.writeButtonByDefault(stopButton, elementId + STOP_BTN_CONTAINER, abstractFileUpload.getStopButtonText(), "o_s_file_clear_btn");
        }

    }

}
