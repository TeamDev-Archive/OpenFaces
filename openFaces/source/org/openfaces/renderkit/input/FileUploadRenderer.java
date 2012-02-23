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
package org.openfaces.renderkit.input;

import org.openfaces.component.input.AbstractFileUpload;
import org.openfaces.component.input.FileUpload;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public final class FileUploadRenderer extends AbstractFileUploadRenderer {
    private static final String F_REMOVE_BUTTON = "removeButton";
    private static final String F_CLEAR_BUTTON = "clearButton";

    private static final String DIV_FOR_INFO_ID = "::infoDiv";
    private static final String FOOTER_DIV_ID = "::footer";
    private static final String DIV_HEADER_ID = "::header";

    private static final String REMOVE_BTN_CONTAINER = "::removeFacet";
    private static final String CLEAR_BTN_CONTAINER = "::clearFacet";
    private static final String UPLOAD_BTN_CONTAINER = "::uploadFacet";
    private static final String REMOVE_ALL_BTN_CONTAINER = "::removeAllFacet";
    private static final String STOP_ALL_BTN_CONTAINER = "::stopAllFacet";

    private static final String F_UPLOAD_BUTTON = "uploadButton";
    private static final String F_REMOVE_ALL_BUTTON = "removeAllButton";
    private static final String F_STOP_ALL_BUTTON = "stopAllButton";

    private UIComponent removeButton;
    private UIComponent clearButton;
    private UIComponent uploadButton;
    private UIComponent removeAllButton;
    private UIComponent stopAllButton;

    @Override
    protected void setAllFacets(AbstractFileUpload fileUpload) {
        super.setAllFacets(fileUpload);
        removeButton = fileUpload.getFacet(F_CLEAR_BUTTON);
        clearButton = fileUpload.getFacet(F_REMOVE_BUTTON);
        uploadButton = fileUpload.getFacet(F_UPLOAD_BUTTON);
        removeAllButton = fileUpload.getFacet(F_REMOVE_ALL_BUTTON);
        stopAllButton = fileUpload.getFacet(F_STOP_ALL_BUTTON);

    }

    @Override
    protected void writeHelpfulButtons(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String elementId) throws IOException {
        FileUpload fileUpload = (FileUpload) abstractFileUpload;
        facetRenderer.writeButtonByDefault(removeButton, elementId + REMOVE_BTN_CONTAINER, fileUpload.getRemoveButtonText(), "o_file_clear_btn");
        facetRenderer.writeButtonByDefault(clearButton, elementId + CLEAR_BTN_CONTAINER, fileUpload.getClearButtonText(), "o_file_clear_btn");
        facetRenderer.writeButtonByDefault(stopButton, elementId + STOP_BTN_CONTAINER, fileUpload.getStopButtonText(), "o_file_clear_btn");
        writeProgressBar(context, abstractFileUpload);
    }

    @Override
    protected void writeStructure(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String clientId) throws IOException {
        Rendering.writeStyleAndClassAttributes(writer, abstractFileUpload.getStyle(), abstractFileUpload.getStyleClass(), "o_file_upload");
        FileUpload fileUpload = (FileUpload) abstractFileUpload;
        writeHeader(context, fileUpload, writer, clientId + DIV_HEADER_ID);

        writeMainDivForInfos(context, writer, fileUpload, clientId + DIV_FOR_INFO_ID);
        writeFooter(context, fileUpload, writer, clientId + FOOTER_DIV_ID);
        writeHelpfulElements(context, fileUpload, writer, clientId + HELP_ELEMENTS_ID);
    }

    @Override
    protected void encodeScriptAndStyles(FacesContext context, AbstractFileUpload abstractFileUpload, String clientId, String uniqueId) throws IOException {
        FileUpload fileUpload = (FileUpload) abstractFileUpload;
        String fileInfoClass = Styles.getCSSClass(context, fileUpload, fileUpload.getRowStyle(), StyleGroup.regularStyleGroup(), fileUpload.getRowClass(), "o_file_upload_info");
        String infoTitleClass = Styles.getCSSClass(context, fileUpload, fileUpload.getFileNameStyle(), StyleGroup.regularStyleGroup(), fileUpload.getFileNameClass(), "o_file_upload_info_title");
        String infoStatusClass = Styles.getCSSClass(context, fileUpload, fileUpload.getUploadStatusStyle(), StyleGroup.regularStyleGroup(), fileUpload.getUploadStatusClass(), "o_file_upload_info_status");
        String progressBarClass = Styles.getCSSClass(context, fileUpload, fileUpload.getProgressBarStyle(), StyleGroup.regularStyleGroup(), fileUpload.getProgressBarClass(), "o_file_upload_info_progress");

        String addButtonClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonClass(), null);
        String addButtonOnMouseOverClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonRolloverStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonRolloverClass(), null);
        String addButtonOnMouseDownClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonPressedStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonPressedClass(), null);
        String addButtonOnFocusClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonFocusedStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonFocusedClass(), null);
        String addButtonDisabledClass = Styles.getCSSClass(context, fileUpload, fileUpload.getBrowseButtonDisabledStyle(), StyleGroup.regularStyleGroup(), fileUpload.getBrowseButtonDisabledClass(), "o_file_upload_addBtn_dis");
        String dropTargetDragoverClass = Styles.getCSSClass(context, fileUpload, fileUpload.getDropTargetDragoverStyle(), StyleGroup.regularStyleGroup(), fileUpload.getDropTargetDragoverClass(), "o_file_drop_target_dragover");
        Styles.renderStyleClasses(context, fileUpload);

        int uploadedSize = 0;
        String headerId = clientId + DIV_HEADER_ID;
        boolean duplicateAllowed = true;//fileUpload.isDuplicateAllowed();

        Script initScript = new ScriptBuilder().initScript(context, fileUpload, "O$.FileUpload._init",
                fileUpload.getMinQuantity(),
                fileUpload.getMaxQuantity(),
                uploadedSize,
                fileInfoClass,
                infoTitleClass,
                progressBarClass,
                infoStatusClass,
                fileUpload.getNotUploadedStatusText(),
                fileUpload.getInProgressStatusText(),
                fileUpload.getUploadedStatusText(),
                fileUpload.getFileSizeLimitErrorText(),
                fileUpload.getUnexpectedErrorText(),
                fileUpload.getAcceptedFileTypes(),
                duplicateAllowed,
                headerId + BROWSE_BTN_ID,
                addButtonClass,
                addButtonOnMouseOverClass,
                addButtonOnMouseDownClass,
                addButtonOnFocusClass,
                addButtonDisabledClass,
                fileUpload.isDisabled(),
                fileUpload.isAutoUpload(),
                fileUpload.getTabindex(),
                progressBar.getClientId(context),
                fileUpload.getStoppedStatusText(),
                fileUpload.getStoppingStatusText(),
                fileUpload.isMultiple(),
                uniqueId,
                Utilities.getFunctionOfEvent(fileUpload.getOnchange()),
                Utilities.getFunctionOfEvent(fileUpload.getOnstart()),
                Utilities.getFunctionOfEvent(fileUpload.getOnend()),
                Utilities.getFunctionOfEvent(fileUpload.getOnuploadstart()),
                Utilities.getFunctionOfEvent(fileUpload.getOnuploadinprogress()),
                Utilities.getFunctionOfEvent(fileUpload.getOnuploadend()),
                Utilities.getFunctionOfEvent(fileUpload.getOnwrongfileadded()),
                Utilities.getFunctionOfEvent(fileUpload.getOndirectorydropped()),
                dropTargetDragoverClass,
                fileUpload.getUploadMode(),
                (fileUpload.getRenderAfterUpload() == null) ? null : Utilities.getForm(fileUpload).getClientId(context) + ":" + fileUpload.getRenderAfterUpload(),
                fileUpload.getExternalDropTarget(),
                fileUpload.getAcceptDialogFormats(),
                fileUpload.getDirectoryDroppedText(),
                fileUpload.getWrongFileTypeText()
        );

        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.jsonJsURL(context),
                Resources.internalURL(context, "input/fileUpload.js"),
                Resources.internalURL(context, "input/fileUploadUtil.js")
        );
    }

    private void writeFooter(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        String dropText = fileUpload.getDropTargetText();
        if (dropText == null){
            if (fileUpload.isMultiple()){
                dropText = "Drop file(s) here";
            }else{
                dropText = "Drop file here";
            }
        }
        writeDragAndDropArea(context, fileUpload, writer, elementId + DRAG_AREA,
                (fileUpload.getExternalDropTarget() == null) ? "o_file_drop_target" : "o_file_ext_drop_target",
                dropText);
        facetRenderer.writeButtonByDefault(removeAllButton, elementId + REMOVE_ALL_BTN_CONTAINER, fileUpload.getRemoveAllButtonText(), "o_file_remove_all_btn");
        facetRenderer.writeButtonByDefault(stopAllButton, elementId + STOP_ALL_BTN_CONTAINER, fileUpload.getStopAllButtonText(), "o_file_stop_all_btn");
        writer.endElement("div");
    }

    private void writeMainDivForInfos(FacesContext context, ResponseWriter writer, FileUpload fileUpload, String elementId) throws IOException {
        String styleClass = Styles.getCSSClass(context, fileUpload, fileUpload.getAllInfosStyle(), StyleGroup.regularStyleGroup(), fileUpload.getAllInfosClass(), "o_file_upload_infos");

        writer.startElement("table", fileUpload);
        writer.writeAttribute("id", elementId, null);
        writer.writeAttribute("class", styleClass, null);
        writer.endElement("table");
    }

    private void writeHeader(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String elementId) throws IOException {
        FileUpload fileUpload = (FileUpload) abstractFileUpload;
        String styleClass = Styles.getCSSClass(context, fileUpload, fileUpload.getHeaderStyle(), StyleGroup.regularStyleGroup(), fileUpload.getHeaderClass(), "o_file_upload_header");
        writer.startElement("table", fileUpload);
        writer.writeAttribute("id", elementId, null);
        writer.writeAttribute("class", styleClass, null);
        writer.startElement("tr", fileUpload);
        writer.startElement("td", fileUpload);

        String browseButtonText = fileUpload.getBrowseButtonText();
        if (browseButtonText == null){
            if (fileUpload.isMultiple()){
                browseButtonText = "Add file...";
            }else{
                browseButtonText = "Upload...";
            }
        }

        writeBrowseButtonTable(context, fileUpload, writer, elementId + BROWSE_BTN_ID, browseButtonText);
        facetRenderer.writeButtonByDefault(uploadButton, elementId + UPLOAD_BTN_CONTAINER, fileUpload.getUploadButtonText(), "o_file_upload_btn");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

}
