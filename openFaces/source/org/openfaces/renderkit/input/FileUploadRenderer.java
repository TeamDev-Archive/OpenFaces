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

import org.openfaces.component.input.FileUpload;
import org.openfaces.component.output.ProgressBar;
import org.openfaces.event.FileUploadItem;
import org.openfaces.event.FileUploadStatus;
import org.openfaces.event.UploadCompletionEvent;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.AnonymousFunction;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FileUploadRenderer extends RendererBase implements AjaxPortionRenderer {
    public static final String INIT_PARAM_MAX_FILE_SIZE = "org.openfaces.fileUpload.fileSizeLimit";
    public static final String TERMINATED_TEXT = "_TERMINATED";
    public static final String PROGRESS_ID = "progress_";
    public  static final String EXCEED_MAX_SIZE_ID = "exceedMaxSize_";

    private static final String DIV_FOR_INPUTS_ID = "::inputs";
    private static final String INPUT_OF_FILE_ID = "::input";
    private static final String DIV_FOR_INFO_ID = "::infoDiv";
    public static final String DIV_HEADER_ID = "::header";

    private static final String BROWSE_BTN_ID = "::addButton";
    private static final String TITLE_ADD_BTN_DIV_ID = "::title";
    private static final String INPUT_DIV_OF_ADD_BTN_ID = "::forInput";

    /*facet names and components which is used in this component*/
    private static final String F_BROWSE_BUTTON = "browseButton";
    private static final String F_UPLOAD_BUTTON = "uploadButton";
    private static final String F_CLEAR_ALL_BUTTON = "clearAllButton";

    private static final String F_REMOVE_BUTTON = "removeButton";
    private static final String F_STOP_BUTTON = "stopButton";
    private static final String F_CLEAR_BUTTON = "clearButton";

    private static final String F_PROGRESS_BAR = "progressBar";
    /*id of elements*/
    private static final String REMOVE_BTN_CONTAINER = "::removeFacet";
    private static final String CLEAR_BTN_CONTAINER = "::clearFacet";
    private static final String STOP_BTN_CONTAINER = "::stopFacet";

    private static final String UPLOAD_BTN_CONTAINER = "::uploadFacet";
    private static final String CLEAR_ALL_BTN_CONTAINER = "::clearAllFacet";

    private static final String FOOTER_DIV_ID = "::footer";
    private static final String HELP_ELEMENTS_ID = "::elements";
    private static final String DRAG_AREA = "::dragArea";
    /*default text for buttons*/
    private static final String DEF_BROWSE_BTN_LABEL_SINGLE = "Upload...";
    private static final String DEF_BROWSE_LABEL_MULTIPLE = "Add file...";
    private static final String DEF_UPLOAD_BTN_TEXT = "Upload";
    private static final String DEF_REMOVE_BTN_TEXT = "Remove";
    private static final String DEF_CLEAR_BTN_TEXT = "Clear";
    private static final String DEF_STOP_BTN_TEXT = "Stop";
    private static final String DEF_CLEAR_ALL_BTN_TEXT = "Remove all";


    private UIComponent browseButton;
    private UIComponent uploadButton;
    private UIComponent clearAllButton;
    private UIComponent removeButton;
    private UIComponent stopButton;
    private UIComponent clearButton;
    private ProgressBar progressBar;

    /*progress*/
    private static final String AJAX_PARAM_PROGRESS_REQUEST = "progressRequest";
    private static final String AJAX_PARAM_FILE_ID = "fileId";

    /*listOfFiles*/
    private static final String AJAX_FILES_REQUEST = "listOfFilesRequest";
    private static final String AJAX_PARAM_FILES_ID = "idOfFiles";
    /*iStopRequest*/
    private static final String AJAX_IS_STOP_REQUEST = "stoppedRequest";
    private static final String AJAX_PARAM_UNIQUE_ID = "uniqueIdOfFile";
    /*information request that file is stopped because of timeout*/
    private static final String AJAX_IS_INFORM_FAILED_REQUEST="informFailedRequest";
    private static final String AJAX_PARAM_ID_FAILED_FILE="uniqueIdOfFile";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        AjaxUtil.prepareComponentForAjax(context, component);

        FileUpload fileUpload = (FileUpload) component;

        if (((HttpServletRequest) context.getExternalContext().getRequest()).getAttribute("fileUploadRequest") != null) {
            uploadIfExistFiles(context, fileUpload);
            return;
        }

        setAllFacets(fileUpload);
        renderComponent(context, fileUpload);
    }

    private void renderComponent(FacesContext context, FileUpload fileUpload) throws IOException {
        String clientId = fileUpload.getClientId(context);
        String uniqueID = generateUniqueId(clientId);
        setFileSizeLimitInSession(context, fileUpload, uniqueID);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", fileUpload);
        Rendering.writeIdAttribute(context, fileUpload);
        Rendering.writeStyleAndClassAttributes(writer, fileUpload.getStyle(), fileUpload.getStyleClass(), "o_file_upload");
        Rendering.writeStandardEvents(writer, fileUpload);

        writeHeader(context, fileUpload, writer, clientId + DIV_HEADER_ID);

        writeMainDivForInfos(context, writer, fileUpload, clientId + DIV_FOR_INFO_ID);
        writeFooter(context, fileUpload, writer, clientId + FOOTER_DIV_ID);
        writeHelpfulElements(context, fileUpload, writer, clientId + HELP_ELEMENTS_ID);

        encodeScriptAndStyles(context, fileUpload, clientId, uniqueID);
        writer.endElement("div");
    }

    private void writeHelpfulElements(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        writer.writeAttribute("style", "display:none", null);
        writeRemoveButton(context, fileUpload, writer, elementId + REMOVE_BTN_CONTAINER);
        writeClearButton(context, fileUpload, writer, elementId + CLEAR_BTN_CONTAINER);
        writeStopButton(context, fileUpload, writer, elementId + STOP_BTN_CONTAINER);
        writeProgressBar(context);
        writer.endElement("div");
    }

    private void writeProgressBar(FacesContext context) throws IOException {
        if (progressBar == null) {
            progressBar = new ProgressBar();
        }
        progressBar.encodeAll(context);
    }

    private void writeClearButton(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        if (clearButton == null) {
            writer.startElement("input", fileUpload);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("class", "o_file_clear_btn", null);
            writer.writeAttribute("value", DEF_CLEAR_BTN_TEXT, null);
            writer.endElement("input");
        } else {
            clearButton.encodeAll(context);
        }
        writer.endElement("div");
    }

    private void writeStopButton(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        if (stopButton == null) {
            writer.startElement("input", fileUpload);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("class", "o_file_clear_btn", null);
            writer.writeAttribute("value", DEF_STOP_BTN_TEXT, null);
            writer.endElement("input");
        } else {
            stopButton.encodeAll(context);
        }
        writer.endElement("div");
    }

    private void writeRemoveButton(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        if (removeButton == null) {
            writer.startElement("input", fileUpload);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("class", "o_file_clear_btn", null);
            writer.writeAttribute("value", DEF_REMOVE_BTN_TEXT, null);
            writer.endElement("input");
        } else {
            removeButton.encodeAll(context);
        }
        writer.endElement("div");
    }

    private void setAllFacets(FileUpload fileUpload) {

        browseButton = fileUpload.getFacet(F_BROWSE_BUTTON);
        uploadButton = fileUpload.getFacet(F_UPLOAD_BUTTON);
        clearAllButton = fileUpload.getFacet(F_CLEAR_ALL_BUTTON);

        removeButton = fileUpload.getFacet(F_REMOVE_BUTTON);
        stopButton = fileUpload.getFacet(F_STOP_BUTTON);
        clearButton = fileUpload.getFacet(F_CLEAR_BUTTON);
        progressBar = (ProgressBar) fileUpload.getFacet(F_PROGRESS_BAR);
    }

    private void writeMainDivForInfos(FacesContext context, ResponseWriter writer, FileUpload fileUpload, String elementId) throws IOException {
        String styleClass = Styles.getCSSClass(context, fileUpload, fileUpload.getAllInfosStyle(), StyleGroup.regularStyleGroup(), fileUpload.getAllInfosClass(), "o_file_upload_infos");

        writer.startElement("table", fileUpload);
        writer.writeAttribute("id", elementId, null);
        writer.writeAttribute("class", styleClass, null);
        writer.endElement("table");
    }

    private void writeHeader(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        String styleClass = Styles.getCSSClass(context, fileUpload, fileUpload.getHeaderStyle(), StyleGroup.regularStyleGroup(), fileUpload.getHeaderClass(), "o_file_upload_header");
        writer.startElement("table", fileUpload);
        writer.writeAttribute("id", elementId, null);
        writer.writeAttribute("class", styleClass, null);
        writer.startElement("tr", fileUpload);
        writer.startElement("td", fileUpload);

        writeBrowseButtonTable(context, fileUpload, writer, elementId + BROWSE_BTN_ID);
        writeUploadButton(context, fileUpload, writer, elementId + UPLOAD_BTN_CONTAINER);
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void writeUploadButton(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        if (uploadButton == null) {
            writer.startElement("input", fileUpload);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("class", "o_file_upload_btn", null);
            writer.writeAttribute("value", DEF_UPLOAD_BTN_TEXT, null);
            writer.endElement("input");
        } else {
            uploadButton.encodeAll(context);
        }
        writer.endElement("div");
    }

    private void writeDragAndDropArea(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException{
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        String dragDropClass = Styles.getCSSClass(context, fileUpload, fileUpload.getDropTargetStyle(), StyleGroup.regularStyleGroup(), fileUpload.getDropTargetClass(), "o_file_drop_target");
        writer.writeAttribute("class", dragDropClass, null);
        writer.writeAttribute("style", "display:none", null);
        String value = fileUpload.getDropTargetText();
        if (value == null) {
            if (!fileUpload.isMultiple()) {
                value = "Drop file here";
            } else {
                value = "Drop file(s) here";
            }
        }
        writer.write(value);
        writer.endElement("div");
    }

    private void writeClearAllButton(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        if (clearAllButton == null) {
            writer.startElement("input", fileUpload);
            writer.writeAttribute("type", "button", null);
            writer.writeAttribute("value", DEF_CLEAR_ALL_BTN_TEXT, null);
            writer.writeAttribute("class", "o_file_clear_all_btn", null);
            writer.endElement("input");
        } else {
            clearAllButton.encodeAll(context);
        }
        writer.endElement("div");
    }

    private void writeBrowseButtonTable(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("table", fileUpload);
        writer.writeAttribute("style", "float:left;", null); //todo temporary
        writer.writeAttribute("id", elementId, null);
        writer.startElement("tr", fileUpload);
        writer.startElement("td", fileUpload);

        writer.startElement("div", fileUpload);
        writer.writeAttribute("style", "position:relative", null);

        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId + TITLE_ADD_BTN_DIV_ID, null);
        if (browseButton == null) {
            writer.startElement("input", fileUpload);
            writer.writeAttribute("type", "button", null);
            String value = fileUpload.getBrowseButtonText();
            if (value == null) {
                if (!fileUpload.isMultiple()) {
                    value = DEF_BROWSE_BTN_LABEL_SINGLE;
                } else {
                    value = DEF_BROWSE_LABEL_MULTIPLE;
                }
            }
            writer.writeAttribute("value", value, null);
            writer.endElement("input");
        } else {
            browseButton.encodeAll(context);
        }

        writer.endElement("div");
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId + INPUT_DIV_OF_ADD_BTN_ID, null);
        writer.writeAttribute("style", "overflow:hidden;position:absolute;top:0;width:100%;height:100%", null); //todo temporary

        writer.endElement("div");

        writer.endElement("div");

        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("table");
    }

    private void writeFooter(FacesContext context, FileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        writeDragAndDropArea(context, fileUpload, writer, elementId + DRAG_AREA);
        writeClearAllButton(context, fileUpload, writer, elementId + CLEAR_ALL_BTN_CONTAINER);
        writer.endElement("div");
    }

    private AnonymousFunction getFunctionOfEvent(String eventHandler){
        AnonymousFunction eventFunction = null;

        if (eventHandler != null) {
            eventFunction = new AnonymousFunction(eventHandler, "event");
        }
        return eventFunction;
    }

    private void encodeScriptAndStyles(FacesContext context, FileUpload fileUpload, String clientId, String uniqueId) throws IOException {
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
                getFunctionOfEvent(fileUpload.getOnchange()),
                getFunctionOfEvent(fileUpload.getOnuploadstart()),
                getFunctionOfEvent(fileUpload.getOnuploadend()),
                getFunctionOfEvent(fileUpload.getOnfileuploadstart()),
                getFunctionOfEvent(fileUpload.getOnfileuploadinprogress()),
                getFunctionOfEvent(fileUpload.getOnfileuploadend()),
                dropTargetDragoverClass
        );

        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.jsonJsURL(context),
                Resources.internalURL(context, "input/fileUpload.js")
        );
    }

    private String generateUniqueId(String clientId) {
        return clientId + System.currentTimeMillis();
    }

    private void setFileSizeLimitInSession(FacesContext context, FileUpload fileUpload, String uniqueId){
        long sizeLimit = fileUpload.getFileSizeLimit();
        if (sizeLimit == 0){
            String maxSizeString = context.getExternalContext().getInitParameter(INIT_PARAM_MAX_FILE_SIZE);
            sizeLimit = (maxSizeString != null) ? Long.parseLong(maxSizeString) * 1024 : Long.MAX_VALUE;
        }else{
            sizeLimit *= 1024;
        }
        context.getExternalContext().getSessionMap().put(uniqueId, sizeLimit);
    }

    private void uploadIfExistFiles(FacesContext context, FileUpload fileUpload) {
        ExternalContext extContext = context.getExternalContext();
        String clientId = fileUpload.getClientId(context);
        try {
            HttpServletRequest request = (HttpServletRequest) extContext.getRequest();
            FileUploadItem uploadedFile = (FileUploadItem) request.getAttribute(clientId + DIV_FOR_INPUTS_ID + INPUT_OF_FILE_ID);
            if (uploadedFile == null){
                return;
            }
            String id = (String) request.getAttribute("FILE_ID");
            request.getSession().setAttribute(id, uploadedFile);

            //file uploaded.
            //fileUpload.getFileUploadedListener().invoke(context.getELContext(), new Object[]{new FileUploadedEvent(fileUpload, uploadedFile)});
        } catch (Exception e) {
            throw new RuntimeException("Could not handle file upload - please ensure that you have correctly configured the filter.", e);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
    }

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component, String portionName, JSONObject jsonParam) throws IOException, JSONException {
        if (jsonParam.has(AJAX_PARAM_PROGRESS_REQUEST)) {
            JSONObject jsonObj = new JSONObject();
            String fileId = (String) jsonParam.get(AJAX_PARAM_FILE_ID);
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            if (sessionMap.containsKey(PROGRESS_ID + fileId)) {
                Integer progress = (Integer) sessionMap.get(PROGRESS_ID + fileId);
                Rendering.addJsonParam(jsonObj, "progressInPercent", progress);
                Rendering.addJsonParam(jsonObj, "status", "inProgress");
                if (progress.equals(100)) {
                    sessionMap.remove(PROGRESS_ID + fileId);
                }
            } else {
                /*if FileSize Exceed*/
                if (sessionMap.containsKey(EXCEED_MAX_SIZE_ID + fileId)) {
                    boolean maxFileExceeded = (Boolean) sessionMap.get(EXCEED_MAX_SIZE_ID + fileId);
                    Rendering.addJsonParam(jsonObj, "isFileSizeExceed", maxFileExceeded);
                    sessionMap.remove(EXCEED_MAX_SIZE_ID + fileId);
                }else{
                /*if there is no fileUpload request*/
                    Rendering.addJsonParam(jsonObj, "progressInPercent", 0);
                    Rendering.addJsonParam(jsonObj, "status", "inProgress");
                }
            }
            return jsonObj;
        } else if (jsonParam.has(AJAX_FILES_REQUEST)) {
            JSONObject jsonObj = new JSONObject();
            JSONArray files = (JSONArray) jsonParam.get(AJAX_PARAM_FILES_ID);
            boolean allUploaded = true;
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            for (int i = 0; i < files.length(); i++) {
                JSONArray file = files.getJSONArray(i);
                if (file.getString(3).equals("SUCCESSFUL")) {
                    if (!sessionMap.containsKey(file.getString(0))) {
                        allUploaded = false;
                        break;
                    }
                }
            }
            JSONArray fileSizes = new JSONArray();
            if (allUploaded) {
                List<FileUploadItem> filesItems = new LinkedList<FileUploadItem>();
                for (int i = 0; i < files.length(); i++) {
                    JSONArray file = files.getJSONArray(i);
                    if (file.getString(3).equals("SUCCESSFUL")) {
                        FileUploadItem fileUploadItem = (FileUploadItem) sessionMap.get(file.getString(0));
                        filesItems.add(fileUploadItem);
                        sessionMap.remove(file.getString(0));

                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(file.getString(4));
                        jsonArray.put(fileUploadItem.getFile().length());
                        fileSizes.put(jsonArray);

                    } else if (file.getString(3).equals("STOPPED")) {
                        filesItems.add(new FileUploadItem(file.getString(2), null, FileUploadStatus.STOPPED));
                        sessionMap.remove(PROGRESS_ID + file.getString(1));
                    } else if (file.getString(3).equals("FAILED")) {
                        filesItems.add(new FileUploadItem(file.getString(2), null, FileUploadStatus.FAILED));
                    } else if (file.getString(3).equals("SIZE_LIMIT_EXCEEDED")) {
                        filesItems.add(new FileUploadItem(file.getString(2), null, FileUploadStatus.SIZE_LIMIT_EXCEEDED));
                    }
                }
                FileUpload fileUpload = (FileUpload) component;
                MethodExpression uploadCompletionListener = fileUpload.getUploadCompletionListener();
                if (uploadCompletionListener != null) {
                    uploadCompletionListener.invoke(
                            context.getELContext(), new Object[]{
                            new UploadCompletionEvent(fileUpload, filesItems)});
                }
            }
            Rendering.addJsonParam(jsonObj, "allUploaded", allUploaded);
            Rendering.addJsonParam(jsonObj, "fileSizes", fileSizes);
            return jsonObj;
        }else if (jsonParam.has(AJAX_IS_STOP_REQUEST)){
            /*This is request can be sent by two reasons : if we want to find out if file is stopped or it is terminated by another reasons*/
            String uniqueId = (String) jsonParam.get(AJAX_PARAM_UNIQUE_ID);
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            JSONObject jsonObj = new JSONObject();
            if (sessionMap.containsKey(uniqueId + TERMINATED_TEXT)){
                Rendering.addJsonParam(jsonObj, "isStopped", true);
                sessionMap.remove(uniqueId + TERMINATED_TEXT);
            }else{
                Rendering.addJsonParam(jsonObj, "isStopped", false);
            }
            return jsonObj;
        }else if (jsonParam.has(AJAX_IS_INFORM_FAILED_REQUEST)){
            /*This is request can be sent to inform that request is failed because of timeout*/
            String uniqueId = (String) jsonParam.get(AJAX_PARAM_ID_FAILED_FILE);
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            JSONObject jsonObj = new JSONObject();
            sessionMap.put(uniqueId + TERMINATED_TEXT, true);
            return jsonObj;
        }
        return null;
    }
}
