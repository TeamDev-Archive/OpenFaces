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
import org.openfaces.component.input.MultipleFileUpload;
import org.openfaces.component.output.ProgressBar;
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

public final class MultipleFileUploadRenderer extends AbstractFileUploadRenderer {
    private static final  String DEFAULT_UPLOADED_URL = "output/uploadedProgressBar.png";
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
        removeButton = fileUpload.getFacet(F_REMOVE_BUTTON);
        clearButton = fileUpload.getFacet(F_CLEAR_BUTTON);
        uploadButton = fileUpload.getFacet(F_UPLOAD_BUTTON);
        removeAllButton = fileUpload.getFacet(F_REMOVE_ALL_BUTTON);
        stopAllButton = fileUpload.getFacet(F_STOP_ALL_BUTTON);

    }

    @Override
    protected void writeHelpfulButtons(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String elementId) throws IOException {
        MultipleFileUpload multipleFileUpload = (MultipleFileUpload) abstractFileUpload;
        facetRenderer.writeButtonByDefault(removeButton, elementId + REMOVE_BTN_CONTAINER, multipleFileUpload.getRemoveButtonText(), "o_file_clear_btn");
        facetRenderer.writeButtonByDefault(clearButton, elementId + CLEAR_BTN_CONTAINER, multipleFileUpload.getClearButtonText(), "o_file_clear_btn");
        facetRenderer.writeButtonByDefault(stopButton, elementId + STOP_BTN_CONTAINER, multipleFileUpload.getStopButtonText(), "o_file_clear_btn");
        writeProgressBar(context, abstractFileUpload);
    }

    @Override
    protected void writeStructure(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String clientId) throws IOException {
        String defaultStyleClass = "o_file_upload" + (abstractFileUpload.getShowInPopup() ? " o_file_upload_popup" : "");
        Rendering.writeStyleAndClassAttributes(writer, abstractFileUpload.getStyle(), abstractFileUpload.getStyleClass(), defaultStyleClass);
        MultipleFileUpload multipleFileUpload = (MultipleFileUpload) abstractFileUpload;
        if (multipleFileUpload.getExternalBrowseButton() == null || multipleFileUpload.getShowInPopup())
            writeHeader(context, multipleFileUpload, writer, clientId + DIV_HEADER_ID);

        writeMainDivForInfos(context, writer, multipleFileUpload, clientId + DIV_FOR_INFO_ID);
        writeFooter(context, multipleFileUpload, writer, clientId + FOOTER_DIV_ID);
        writeHelpfulElements(context, multipleFileUpload, writer, clientId + HELP_ELEMENTS_ID);
    }

    @Override
    protected void encodeScriptAndStyles(FacesContext context, AbstractFileUpload abstractFileUpload, String clientId, String uniqueId) throws IOException {
        MultipleFileUpload multipleFileUpload = (MultipleFileUpload) abstractFileUpload;
        String fileInfoClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getFileInfoRowStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getFileInfoRowClass(), "o_file_upload_info");
        String infoTitleClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getFileNameStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getFileNameClass(), "o_file_upload_info_title");
        String infoStatusClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getStatusStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getStatusClass(), "o_file_upload_info_status");
        String progressBarClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getProgressBarStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getProgressBarClass(), "o_file_upload_info_progress");

        String addButtonClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getBrowseButtonStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getBrowseButtonClass(), null);
        String addButtonOnMouseOverClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getBrowseButtonRolloverStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getBrowseButtonRolloverClass(), null);
        String addButtonOnMouseDownClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getBrowseButtonPressedStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getBrowseButtonPressedClass(), null);
        String addButtonOnFocusClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getBrowseButtonFocusedStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getBrowseButtonFocusedClass(), null);
        String addButtonDisabledClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getBrowseButtonDisabledStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getBrowseButtonDisabledClass(), "o_file_upload_addBtn_dis");
        String dropTargetDragoverClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getDropTargetDragoverStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getDropTargetDragoverClass(), "o_file_drop_target_dragover");
        Styles.renderStyleClasses(context, multipleFileUpload);

        int uploadedSize = 0;
        boolean duplicateAllowed = true;//fileUpload.isDuplicateAllowed();

        Position popupPosition = abstractFileUpload.getPosition();
        String browseButtonId = clientId + (multipleFileUpload.getExternalBrowseButton() == null || multipleFileUpload.getShowInPopup() ?
                DIV_HEADER_ID : FOOTER_DIV_ID) + BROWSE_BTN_ID;

        Script initScript = new ScriptBuilder().initScript(context, multipleFileUpload, "O$.FileUpload._init",
                multipleFileUpload.getMinQuantity(),
                multipleFileUpload.getMaxQuantity(),
                uploadedSize,
                fileInfoClass,
                infoTitleClass,
                progressBarClass,
                infoStatusClass,
                multipleFileUpload.getNotUploadedStatusText(),
                multipleFileUpload.getInProgressStatusText(),
                multipleFileUpload.getUploadedStatusText(),
                multipleFileUpload.getFileSizeLimitErrorText(),
                multipleFileUpload.getUnexpectedErrorText(),
                multipleFileUpload.getAcceptedFileTypes(),
                duplicateAllowed,
                browseButtonId,
                addButtonClass,
                addButtonOnMouseOverClass,
                addButtonOnMouseDownClass,
                addButtonOnFocusClass,
                addButtonDisabledClass,
                multipleFileUpload.isDisabled(),
                multipleFileUpload.isAutoUpload(),
                multipleFileUpload.getTabindex(),
                progressBar.getClientId(context),
                multipleFileUpload.getStoppedStatusText(),
                multipleFileUpload.getStoppingStatusText(),
                multipleFileUpload.isMultiple(),
                uniqueId,
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(multipleFileUpload, "change")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(multipleFileUpload, "start")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(multipleFileUpload, "end")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(multipleFileUpload, "uploadstart")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(multipleFileUpload, "uploadinprogress")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(multipleFileUpload, "uploadend")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(multipleFileUpload, "wrongfileadded")),
                Utilities.getFunctionOfEvent(Rendering.getEventHandlerScript(multipleFileUpload, "directorydropped")),
                dropTargetDragoverClass,
                multipleFileUpload.getUploadMode(),
                getRender(context, multipleFileUpload),
                getExternalDropTargetId(context, multipleFileUpload),
                multipleFileUpload.getAcceptedMimeTypes(),
                multipleFileUpload.getDirectoryDroppedText(),
                multipleFileUpload.getWrongFileTypeText(),
                getExternalButtonId(context, multipleFileUpload),
                multipleFileUpload.getShowInPopup(),
                getPositionedBy(context, multipleFileUpload),
                popupPosition.getHorizontalAlignment(),
                popupPosition.getVerticalAlignment(),
                popupPosition.getHorizontalDistance(),
                popupPosition.getVerticalDistance()
        );

        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.jsonJsURL(context),
                Resources.internalURL(context, "input/fileUpload.js"),
                Resources.internalURL(context, "input/fileUploadUtil.js")
        );
    }

    private void writeFooter(FacesContext context, MultipleFileUpload multipleFileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", multipleFileUpload);
        writer.writeAttribute("id", elementId, null);
        String dropText = multipleFileUpload.getDropTargetText();
        if (dropText == null) {
            if (multipleFileUpload.isMultiple()) {
                dropText = "Drop file(s) here";
            } else {
                dropText = "Drop file here";
            }
        }
        writeDragAndDropArea(context, multipleFileUpload, writer, elementId + DRAG_AREA,
                (multipleFileUpload.getExternalDropTarget() == null) ? "o_file_drop_target" : "o_file_ext_drop_target",
                dropText);

        if (multipleFileUpload.getExternalBrowseButton() != null && !multipleFileUpload.getShowInPopup()){
            writer.startElement("div", multipleFileUpload);
            writer.writeAttribute("style", "display:none", null);
            writeBrowseButton(context, multipleFileUpload, writer, elementId);
            writer.endElement("div");

            facetRenderer.writeButtonByDefault(uploadButton, elementId + UPLOAD_BTN_CONTAINER, multipleFileUpload.getUploadButtonText(), "o_file_upload_btn_footer");
        }
        if (multipleFileUpload.getShowInPopup()){
            // Replace "Remove All" button with "Close" button
            facetRenderer.writeButtonByDefault(closeButton, elementId + REMOVE_ALL_BTN_CONTAINER, multipleFileUpload.getCloseButtonText(), "o_close_popup_btn");
        } else {
            facetRenderer.writeButtonByDefault(removeAllButton, elementId + REMOVE_ALL_BTN_CONTAINER, multipleFileUpload.getRemoveAllButtonText(), "o_file_remove_all_btn");
        }
        facetRenderer.writeButtonByDefault(stopAllButton, elementId + STOP_ALL_BTN_CONTAINER, multipleFileUpload.getStopAllButtonText(), "o_file_stop_all_btn");
        writer.endElement("div");
    }

    private void writeMainDivForInfos(FacesContext context, ResponseWriter writer, MultipleFileUpload multipleFileUpload, String elementId) throws IOException {
        String styleClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getAllInfosStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getAllInfosClass(), "o_file_upload_infos");

        writer.startElement("table", multipleFileUpload);
        writer.writeAttribute("id", elementId, null);
        writer.writeAttribute("class", styleClass, null);
        writer.endElement("table");
    }

    private void writeBrowseButton(FacesContext context, MultipleFileUpload multipleFileUpload, ResponseWriter writer, String elementId) throws IOException {
        String browseButtonText = multipleFileUpload.getBrowseButtonText();
        if (browseButtonText == null) {
            if (multipleFileUpload.isMultiple()) {
                browseButtonText = "Add file...";
            } else {
                browseButtonText = "Upload...";
            }
        }
        writeBrowseButtonTable(context, multipleFileUpload, writer, elementId + BROWSE_BTN_ID, browseButtonText);
    }
    
    private void writeHeader(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String elementId) throws IOException {
        MultipleFileUpload multipleFileUpload = (MultipleFileUpload) abstractFileUpload;
        String styleClass = Styles.getCSSClass(context, multipleFileUpload, multipleFileUpload.getHeaderStyle(), StyleGroup.regularStyleGroup(), multipleFileUpload.getHeaderClass(), "o_file_upload_header");
        writer.startElement("table", multipleFileUpload);
        writer.writeAttribute("id", elementId, null);
        writer.writeAttribute("class", styleClass, null);
        writer.startElement("tr", multipleFileUpload);
        writer.startElement("td", multipleFileUpload);

        writeBrowseButton(context, multipleFileUpload, writer, elementId);

        facetRenderer.writeButtonByDefault(uploadButton, elementId + UPLOAD_BTN_CONTAINER, multipleFileUpload.getUploadButtonText(), "o_file_upload_btn");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    @Override
    protected void writeProgressBar(FacesContext context, AbstractFileUpload fileUpload) throws IOException {
        if (progressBar == null) {
            progressBar = new ProgressBar();
            progressBar.setDefaultProgressImgUrl(DEFAULT_UPLOADED_URL);
        }
        progressBar.encodeAll(context);
    }
}
