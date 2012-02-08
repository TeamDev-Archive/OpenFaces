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
package org.openfaces.renderkit.input;

import org.openfaces.component.input.AbstractFileUpload;
import org.openfaces.component.input.SingleFileUpload;
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

public class SingleFileUploadRenderer extends AbstractFileUploadRenderer {
    private static final String DIV_FOR_FILE_INFO_ID = "::fileInfo";

    @Override
    protected void writeStructure(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String clientId) throws IOException {
        Rendering.writeStyleAndClassAttributes(writer, abstractFileUpload.getStyle(), abstractFileUpload.getStyleClass(), "o_s_file_upload");
        SingleFileUpload fileUpload = (SingleFileUpload) abstractFileUpload;
        writer.startElement("table", fileUpload);
        writer.startElement("tr", fileUpload);
        writer.startElement("td", fileUpload);

        String browseBtnText = fileUpload.getBrowseButtonText();
        if (browseBtnText == null){
            browseBtnText = "Upload..."; 
        }
        writeBrowseButtonTable(context, fileUpload, writer, clientId + BROWSE_BTN_ID, browseBtnText);
        writer.endElement("td");

        writer.startElement("td", fileUpload);
        writer.writeAttribute("style", "width:100%", null);
        writeFileInfo(context, fileUpload, writer, clientId + DIV_FOR_FILE_INFO_ID);

        String dropTargetText = fileUpload.getDropTargetText();
        if (dropTargetText == null){
            dropTargetText ="Drop file here";
        }
        writeDragAndDropArea(context, abstractFileUpload, writer, clientId + DRAG_AREA, "o_s_file_drop_target", dropTargetText);
        writer.endElement("td");
        writer.endElement("tr");

        writer.startElement("tr", fileUpload);
        writer.startElement("td", fileUpload);
        writeProgressArea(context, fileUpload, writer);
        writer.endElement("td");
        writer.endElement("tr");

        writer.endElement("table");
        writeHelpfulElements(context, fileUpload, writer, clientId + HELP_ELEMENTS_ID);
    }

    private void writeProgressArea(FacesContext context, SingleFileUpload fileUpload, ResponseWriter writer) throws IOException {
        String progressBarClass = Styles.getCSSClass(context, fileUpload, fileUpload.getProgressBarStyle(), StyleGroup.regularStyleGroup(), fileUpload.getProgressBarClass(), "o_file_upload_info_progress");
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
            progressBar.setStyleClass("o_s_fileup_pro_bar");
            progressBar.setLabelStyle("display:none");
        }
        progressBar.encodeAll(context);
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
                Utilities.getFunctionOfEvent(fileUpload.getOnchange()),
                Utilities.getFunctionOfEvent(fileUpload.getOnuploadstart()),
                Utilities.getFunctionOfEvent(fileUpload.getOnuploadend()),
                Utilities.getFunctionOfEvent(fileUpload.getOnfileuploadstart()),
                Utilities.getFunctionOfEvent(fileUpload.getOnfileuploadinprogress()),
                Utilities.getFunctionOfEvent(fileUpload.getOnfileuploadend()),
                dropTargetDragoverClass,
                (fileUpload.getRenderAfterUpload() == null) ? null : Utilities.getForm(fileUpload).getClientId(context) + ":" + fileUpload.getRenderAfterUpload()
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
        simpleButton.write(stopButton, elementId + STOP_BTN_CONTAINER, abstractFileUpload.getStopButtonText(), "o_s_file_clear_btn");
    }

}
