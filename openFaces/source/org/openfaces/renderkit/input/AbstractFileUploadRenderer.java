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

import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.component.input.AbstractFileUpload;
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
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractFileUploadRenderer extends RendererBase implements AjaxPortionRenderer {
    public static final String INIT_PARAM_MAX_FILE_SIZE = "org.openfaces.fileUpload.fileSizeLimit";
    public static final String TERMINATED_TEXT = "_TERMINATED";
    public static final String PROGRESS_ID = "progress_";
    public static final String FILE_SIZE_ID = "size_";
    public static final String EXCEED_MAX_SIZE_ID = "exceedMaxSize_";

    protected static final String BROWSE_BTN_ID = "::addButton";

    private static final String DIV_FOR_INPUTS_ID = "::inputs";
    private static final String INPUT_OF_FILE_ID = "::input";
    private static final String TITLE_ADD_BTN_DIV_ID = "::title";
    private static final String INPUT_DIV_OF_ADD_BTN_ID = "::forInput";

    /*facet names and components which is used in this component*/
    private static final String F_BROWSE_BUTTON = "browseButton";
    private static final String F_STOP_BUTTON = "stopButton";
    private static final String F_CLOSE_POPUP_BUTTON = "closeButton";
    private static final String F_PROGRESS_BAR = "progressBar";

    /*id of elements*/
    protected static final String STOP_BTN_CONTAINER = "::stopFacet";
    protected static final String CLOSE_BTN_CONTAINER = "::closeFacet";
    protected static final String HELP_ELEMENTS_ID = "::elements";
    protected static final String DRAG_AREA = "::dragArea";

    /*default text for browse button*/
    protected UIComponent stopButton;
    protected ProgressBar progressBar;
    protected FacetRenderer facetRenderer;
    private UIComponent browseButton;
    protected UIComponent closeButton;


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
    private static final String AJAX_IS_INFORM_FAILED_REQUEST = "informFailedRequest";
    private static final String AJAX_PARAM_ID_FAILED_FILE = "uniqueIdOfFile";

    protected abstract void writeStructure(FacesContext context, AbstractFileUpload fileUpload, ResponseWriter writer, String clientId) throws IOException;

    protected abstract void encodeScriptAndStyles(FacesContext context, AbstractFileUpload fileUpload, String clientId, String uniqueId) throws IOException;

    protected abstract void writeHelpfulButtons(FacesContext context, AbstractFileUpload abstractFileUpload, ResponseWriter writer, String elementId) throws IOException;

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        AjaxUtil.prepareComponentForAjax(context, component);

        AbstractFileUpload fileUpload = (AbstractFileUpload) component;

        if (((HttpServletRequest) context.getExternalContext().getRequest()).getAttribute("fileUploadRequest") != null) {
            uploadIfExistFiles(context, fileUpload);
            return;
        }

        setAllFacets(fileUpload);
        renderComponent(context, fileUpload);
    }

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component, String portionName, JSONObject jsonParam) throws IOException, JSONException {
        if (jsonParam.has(AJAX_PARAM_PROGRESS_REQUEST)) {
            JSONObject jsonObj = new JSONObject();
            String fileId = (String) jsonParam.get(AJAX_PARAM_FILE_ID);
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            if (sessionMap.containsKey(PROGRESS_ID + fileId)) {
                Integer progress = (Integer) sessionMap.get(PROGRESS_ID + fileId);
                Long size = (Long) sessionMap.get(FILE_SIZE_ID + fileId);
                Rendering.addJsonParam(jsonObj, "progressInPercent", progress);
                Rendering.addJsonParam(jsonObj, "status", "inProgress");
                Rendering.addJsonParam(jsonObj, "size", size);
                if (progress.equals(100)) {
                    sessionMap.remove(PROGRESS_ID + fileId);
                    sessionMap.remove(FILE_SIZE_ID + fileId);
                }
            } else {
                /*if FileSize Exceed*/
                if (sessionMap.containsKey(EXCEED_MAX_SIZE_ID + fileId)) {
                    boolean maxFileExceeded = (Boolean) sessionMap.get(EXCEED_MAX_SIZE_ID + fileId);
                    Long size = (Long) sessionMap.get(FILE_SIZE_ID + fileId);
                    Rendering.addJsonParam(jsonObj, "isFileSizeExceed", maxFileExceeded);
                    Rendering.addJsonParam(jsonObj, "size", size);
                    sessionMap.remove(EXCEED_MAX_SIZE_ID + fileId);
                } else {
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
                        filesItems.add(new FileUploadItem(Utilities.decodeURIComponent(file.getString(2)), null, FileUploadStatus.STOPPED));
                        sessionMap.remove(PROGRESS_ID + file.getString(1));
                        sessionMap.remove(FILE_SIZE_ID + file.getString(1));
                    } else if (file.getString(3).equals("FAILED")) {
                        filesItems.add(new FileUploadItem(Utilities.decodeURIComponent(file.getString(2)), null, FileUploadStatus.FAILED));
                        sessionMap.remove(PROGRESS_ID + file.getString(1));
                        sessionMap.remove(FILE_SIZE_ID + file.getString(1));
                    } else if (file.getString(3).equals("SIZE_LIMIT_EXCEEDED")) {
                        filesItems.add(new FileUploadItem(Utilities.decodeURIComponent(file.getString(2)), null, FileUploadStatus.SIZE_LIMIT_EXCEEDED));
                        sessionMap.remove(FILE_SIZE_ID + file.getString(1));
                    }
                }
                AbstractFileUpload fileUpload = (AbstractFileUpload) component;
                MethodExpression completionListener = fileUpload.getCompletionListener();
                if (completionListener != null) {
                    completionListener.invoke(
                            context.getELContext(), new Object[]{
                            new UploadCompletionEvent(fileUpload, filesItems)});
                }
            }
            Rendering.addJsonParam(jsonObj, "allUploaded", allUploaded);
            Rendering.addJsonParam(jsonObj, "fileSizes", fileSizes);
            return jsonObj;
        } else if (jsonParam.has(AJAX_IS_STOP_REQUEST)) {
            /*This is request can be sent by two reasons : if we want to find out if file is stopped or it is terminated by another reasons*/
            String uniqueId = (String) jsonParam.get(AJAX_PARAM_UNIQUE_ID);
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            JSONObject jsonObj = new JSONObject();
            if (sessionMap.containsKey(uniqueId + TERMINATED_TEXT)) {
                Rendering.addJsonParam(jsonObj, "isStopped", true);
                sessionMap.remove(uniqueId + TERMINATED_TEXT);
            } else {
                Rendering.addJsonParam(jsonObj, "isStopped", false);
            }
            return jsonObj;
        } else if (jsonParam.has(AJAX_IS_INFORM_FAILED_REQUEST)) {
            /*This is request can be sent to inform that request is failed because of timeout*/
            String uniqueId = (String) jsonParam.get(AJAX_PARAM_ID_FAILED_FILE);
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            JSONObject jsonObj = new JSONObject();
            sessionMap.put(uniqueId + TERMINATED_TEXT, true);
            return jsonObj;
        }
        return null;
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
    }

    protected void writeHelpfulElements(FacesContext context, AbstractFileUpload fileUpload, ResponseWriter writer, String elementId) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        writer.writeAttribute("style", "display:none", null);
        writeHelpfulButtons(context, fileUpload, writer, elementId);
        writer.endElement("div");
    }

    protected void setAllFacets(AbstractFileUpload fileUpload) {

        browseButton = fileUpload.getFacet(F_BROWSE_BUTTON);
        stopButton = fileUpload.getFacet(F_STOP_BUTTON);
        progressBar = (ProgressBar) fileUpload.getFacet(F_PROGRESS_BAR);
        if (fileUpload.getShowInPopup()){
            closeButton = fileUpload.getFacet(F_CLOSE_POPUP_BUTTON);
        }
    }

    protected void writeDragAndDropArea(FacesContext context, AbstractFileUpload fileUpload, ResponseWriter writer, String elementId, String defClass, String dropText) throws IOException {
        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId, null);
        String dragDropClass = Styles.getCSSClass(context, fileUpload, fileUpload.getDropTargetStyle(), StyleGroup.regularStyleGroup(), fileUpload.getDropTargetClass(), defClass);
        writer.writeAttribute("class", dragDropClass, null);
        writer.writeAttribute("style", "display:none", null);
        /*todo for temporary compatibility*/
        writer.write(dropText);
        //writer.write(fileUpload.getDropTargetText());
        writer.endElement("div");
    }

    protected void writeBrowseButtonTable(FacesContext context, AbstractFileUpload fileUpload, ResponseWriter writer, String elementId, String textOfButton) throws IOException {
        writer.startElement("table", fileUpload);
        writer.writeAttribute("style", "float:left;padding:0px", null); //todo temporary
        writer.writeAttribute("id", elementId, null);
        writer.startElement("tr", fileUpload);
        writer.startElement("td", fileUpload);
        writer.writeAttribute("style", "padding:0px", null);

        writer.startElement("div", fileUpload);
        writer.writeAttribute("style", "position:relative", null);

        writer.startElement("div", fileUpload);
        writer.writeAttribute("id", elementId + TITLE_ADD_BTN_DIV_ID, null);
        if (browseButton == null) {
            writer.startElement("input", fileUpload);
            writer.writeAttribute("type", "button", null);
            /*todo for temporary compatibility*/
            writer.writeAttribute("value", textOfButton, null);
            //writer.writeAttribute("value", fileUpload.getBrowseButtonText(), null);
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

    protected abstract void writeProgressBar(FacesContext context, AbstractFileUpload fileUpload) throws IOException;

    private void renderComponent(FacesContext context, AbstractFileUpload fileUpload) throws IOException {
        String clientId = fileUpload.getClientId(context);
        String uniqueID = Utilities.generateUniqueId(clientId);
        setFileSizeLimitInSession(context, fileUpload, uniqueID);
        ResponseWriter writer = context.getResponseWriter();
        this.facetRenderer = new FacetRenderer(context, fileUpload, writer);
        writer.startElement("div", fileUpload);
        Rendering.writeIdAttribute(context, fileUpload);
        Rendering.writeStandardEvents(writer, fileUpload);

        writeStructure(context, fileUpload, writer, clientId);

        encodeScriptAndStyles(context, fileUpload, clientId, uniqueID);
        writer.endElement("div");
    }

    private void setFileSizeLimitInSession(FacesContext context, AbstractFileUpload fileUpload, String uniqueId) {
        long sizeLimit = fileUpload.getFileSizeLimit();
        if (sizeLimit == 0) {
            String maxSizeString = context.getExternalContext().getInitParameter(INIT_PARAM_MAX_FILE_SIZE);
            sizeLimit = (maxSizeString != null) ? Long.parseLong(maxSizeString) * 1024 : Long.MAX_VALUE;
        } else {
            sizeLimit *= 1024;
        }
        context.getExternalContext().getSessionMap().put(uniqueId, sizeLimit);
    }

    private void uploadIfExistFiles(FacesContext context, AbstractFileUpload fileUpload) {
        ExternalContext extContext = context.getExternalContext();
        String clientId = fileUpload.getClientId(context);
        try {
            HttpServletRequest request = (HttpServletRequest) extContext.getRequest();
            FileUploadItem uploadedFile = (FileUploadItem) request.getAttribute(clientId + DIV_FOR_INPUTS_ID + INPUT_OF_FILE_ID);
            if (uploadedFile == null) {
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

    protected class FacetRenderer {
        private final FacesContext context;
        private final AbstractFileUpload fileUpload;
        private final ResponseWriter writer;

        public FacetRenderer(FacesContext context, AbstractFileUpload fileUpload, ResponseWriter writer) {
            this.context = context;
            this.fileUpload = fileUpload;
            this.writer = writer;
        }

        public void writeButtonByDefault(UIComponent facet, String elementId, String defText, String defClass) throws IOException {
            writer.startElement("div", fileUpload);
            writer.writeAttribute("id", elementId, null);
            if (facet == null) {
                writer.startElement("input", fileUpload);
                writer.writeAttribute("type", "button", null);
                writer.writeAttribute("value", defText, null);
                writer.writeAttribute("class", defClass, null);
                writer.endElement("input");
            } else {
                facet.encodeAll(context);
            }
            writer.endElement("div");
        }

        public void writeDivByDefault(UIComponent facet, String elementId, String defText, String defClass) throws IOException{
            writer.startElement("div", fileUpload);
            writer.writeAttribute("id", elementId, null);
            if (facet == null) {
                writer.startElement("div", fileUpload);
                writer.writeAttribute("class", defClass, null);
                writer.write(defText);
                writer.endElement("div");
            } else {
                facet.encodeAll(context);
            }
            writer.endElement("div");
        }
    }

    protected static class Utilities {
        private static String generateUniqueId(String clientId) {
            return clientId + System.currentTimeMillis();
        }

        protected static AnonymousFunction getFunctionOfEvent(String eventHandler) {
            AnonymousFunction eventFunction = null;

            if (eventHandler != null) {
                eventFunction = new AnonymousFunction(eventHandler, "event");
            }
            return eventFunction;
        }

        protected static UIForm getForm(UIComponent component) {
            UIComponent parent = component.getParent();
            while (!(parent instanceof UIForm)) {
                parent = parent.getParent();
            }
            return (UIForm) parent;
        }

        private static String decodeURIComponent(String encodedURI) {
            char actualChar;

            StringBuffer buffer = new StringBuffer();

            int bytePattern, sumb = 0;

            for (int i = 0, more = -1; i < encodedURI.length(); i++) {
                actualChar = encodedURI.charAt(i);

                switch (actualChar) {
                    case '%': {
                        actualChar = encodedURI.charAt(++i);
                        int hb = (Character.isDigit(actualChar) ? actualChar - '0'
                                : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
                        actualChar = encodedURI.charAt(++i);
                        int lb = (Character.isDigit(actualChar) ? actualChar - '0'
                                : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
                        bytePattern = (hb << 4) | lb;
                        break;
                    }
                    case '+': {
                        bytePattern = ' ';
                        break;
                    }
                    default: {
                        bytePattern = actualChar;
                    }
                }

                if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
                    sumb = (sumb << 6) | (bytePattern & 0x3f);
                    if (--more == 0)
                        buffer.append((char) sumb);
                } else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
                    buffer.append((char) bytePattern);
                } else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
                    sumb = bytePattern & 0x1f;
                    more = 1;
                } else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
                    sumb = bytePattern & 0x0f;
                    more = 2;
                } else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
                    sumb = bytePattern & 0x07;
                    more = 3;
                } else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
                    sumb = bytePattern & 0x03;
                    more = 4;
                } else { // 1111110x
                    sumb = bytePattern & 0x01;
                    more = 5;
                }
            }
            return buffer.toString();
        }

    }
    
    protected String getPositionedBy(FacesContext context, AbstractFileUpload abstractFileUpload){
        return Components.referenceIdToClientId(context, abstractFileUpload, abstractFileUpload.getPosition().getBy());
    }

    protected String getExternalButtonId(FacesContext context, AbstractFileUpload abstractFileUpload){
        return Components.referenceIdToClientId(context, abstractFileUpload, abstractFileUpload.getExternalBrowseButton());
    }

    protected String getExternalDropTargetId(FacesContext context, AbstractFileUpload abstractFileUpload){
        return Components.referenceIdToClientId(context, abstractFileUpload, abstractFileUpload.getExternalDropTarget());
    }

    protected JSONArray getRender(FacesContext context, AbstractFileUpload fileUpload) {
        AjaxInitializer ajaxInitializer = new AjaxInitializer();
        return ajaxInitializer.getRenderArray(context, fileUpload, fileUpload.getRender());
    }
}
