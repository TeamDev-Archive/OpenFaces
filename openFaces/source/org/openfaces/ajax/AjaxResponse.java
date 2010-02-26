/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.ajax;

import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.AbstractResponseFacade;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Environment;
import org.openfaces.util.HTML;
import org.openfaces.util.Rendering;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly
 * by any application code.
 */
class AjaxResponse {
    private static final String TEXT_RESPONSE_PREFIX = "_openfaces_ajax_response_prefix_";
    private static final String TEXT_RESPONSE_SUFFIX = "_openfaces_ajax_response_suffix_";

    // TODO [sanders] (Apr 1, 2009, 6:41 AM): Consider moving to Tag enum
    private static final String TAG_AJAX_RESPONSE_ROOT = "ajaxResponse";
    private static final String TAG_AJAX_UPDATABLE = "updatable";
    private static final String TAG_AJAX_SCRIPT = "script";
    private static final String TAG_AJAX_STYLE = "style";
    private static final String TAG_AJAX_CSS = "css";
    private static final String TAG_AJAX_SESSION_EXPIRED = "session_expired";
    private static final String TAG_AJAX_SESSION_EXPIRED_LOCATION = "session_expired_location";
    private static final String TAG_AJAX_EXCEPTION = "ajax_exception";
    private static final String TAG_AJAX_EXCEPTION_MESSAGE = "ajax_exception_message";

    private static final String TYPE_UPDATE_SIMPLE = "simple";
    private static final String TYPE_UPDATE_PORTION = "portion";
    private static final String TYPE_UPDATE_INIT_SCRIPTS = "initialization";
    private static final String TYPE_UPDATE_STATE = "state";

    private List<AjaxPortionData> simpleUpdate;
    private List<AjaxPortionData> portions;
    private String initLibraryName;
    private List<String> jsLibraries;
    private List<String> cssFiles;
    private List<String> styles;
    private List<AjaxPortionData> componentStates;
    private String sessionExpired;
    private String sessionExpiredLocation;
    private Throwable exception;

    private AjaxSavedStateIdxHolder stateIdxHolder;

    private static final String CONTENT_TYPE = "text/xml;charset=UTF-8";

    public AjaxResponse() {
    }

    public AjaxSavedStateIdxHolder getStateIdxHolder() {
        return stateIdxHolder;
    }

    public void setStateIdxHolder(AjaxSavedStateIdxHolder stateIdxHolder) {
        this.stateIdxHolder = stateIdxHolder;
    }

    public void setSimpleUpdate(String componentId, String html, String scripts) {
        simpleUpdate = new ArrayList<AjaxPortionData>();
        simpleUpdate.add(new AjaxPortionData(componentId, html, scripts, null));
    }

    public void addSimpleUpdate(String componentId, String html, String scripts) {
        if (simpleUpdate == null) {
            simpleUpdate = new ArrayList<AjaxPortionData>();
        }
        simpleUpdate.add(new AjaxPortionData(componentId, html, scripts, null));
    }

    public void write(AbstractResponseFacade response) throws IOException {
        updateViewVariable();
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        try {
            if (!AjaxUtil.isPortletRequest(context)
                    && !requestMap.containsKey(AjaxViewHandler.ERROR_OCCURRED)) {
                response.setContentType(CONTENT_TYPE);
            } else if (AjaxUtil.isPortletRequest(context)) {
                response.setContentType(CONTENT_TYPE);
            }
            writeAsXML(response);
        } catch (IllegalArgumentException e) { // JBoss and JetSpeed throw IllegalArgumentException when trying to set text/xml content type
            if (!AjaxUtil.isPortletRequest(context)
                    && !requestMap.containsKey(AjaxViewHandler.ERROR_OCCURRED)
                    && !Environment.isRI()) {
                response.setContentType("text/html;charset=UTF-8");
            }
            writeAsText(response);
        }
    }

    /**
     * Sun's RI implementation uses "com.sun.faces.VIEW" hidden variable to map request and state in map,
     * if save state is server. We have to update the hidden field value.
     * see JSFC-1898 JS error if try to expand nodes in the TreeTable (JSF RI 1.2 and server state saving)
     */
    private void updateViewVariable() {
        Object viewStructureId = stateIdxHolder.getViewStructureId();
        if (viewStructureId == null) {
            return;
        }

        String script;

        if (stateIdxHolder.getViewStateIdentifier() != null) {
            script = "O$.updateViewId('" + stateIdxHolder.getViewStateIdentifier() + "');";
        } else {
            if (Environment.isRI()) {
                script = "O$.updateViewId('" + viewStructureId + "');";
            } else {
                script = "";
            }
        }

        if (simpleUpdate != null) {
            List<AjaxPortionData> tempList = new ArrayList<AjaxPortionData>();
            for (AjaxPortionData portionData : simpleUpdate) {
                tempList.add(formatJs(script, portionData));
            }
            simpleUpdate = tempList;
        } else if (portions != null && !portions.isEmpty()) {
            AjaxPortionData portionData = portions.get(0);
            portions.set(0, formatJs(script, portionData));
        }
    }

    private AjaxPortionData formatJs(String script, AjaxPortionData portionData) {
        String id = portionData.getId();
        String html = portionData.getHtml();
        String scripts = portionData.getScripts();
        JSONObject responseData = portionData.getResponseData();
        if (scripts == null || scripts.length() == 0)
            scripts = "<script></script>";
        int tagEnd = scripts.indexOf(">") + 1;
        String newScripts = scripts.substring(0, tagEnd) + script + scripts.substring(tagEnd);
        return new AjaxPortionData(id, html, newScripts, responseData);
    }


    private void writeAsXML(AbstractResponseFacade response) throws IOException {
        Element root = new Element(TAG_AJAX_RESPONSE_ROOT);
        Element head = new Element("head"); // dummy <head> tag is needed to overcome warning if MyFaces' ExtensionsFilter is used in the application (JSFC-1700)
        //noinspection RedundantCast
        root.addContent(/*don't remove the cast - needed for jdk1.5 compatibility*/(Content) head);
        head.setText(" ");

        if (simpleUpdate != null) {
            for (AjaxPortionData simpleUpdate : this.simpleUpdate) {
                simpleUpdate.addToElement(root, TYPE_UPDATE_SIMPLE);
            }
        }

        if (portions != null) {
            for (AjaxPortionData ajaxPortionData : portions) {
                ajaxPortionData.addToElement(root, TYPE_UPDATE_PORTION);
            }
        }

        if (initLibraryName != null) {
            //noinspection RedundantCast
            root.addContent(/*don't remove the cast - needed for jdk1.5 compatibility*/(Content) Elements.createUpdatable(null,
                    TYPE_UPDATE_INIT_SCRIPTS, initLibraryName, null, null));
        }

        if (jsLibraries != null) {
            for (String jsLibrary : jsLibraries) {
                //noinspection RedundantCast
                root.addContent(/*don't remove the cast - needed for jdk1.5 compatibility*/(Content) Elements.createScript(jsLibrary));
            }
        }

        if (cssFiles != null) {
            for (String cssFile : cssFiles) {
                //noinspection RedundantCast
                root.addContent(/*don't remove the cast - needed for jdk1.5 compatibility*/(Content) Elements.createCSSFile(cssFile));
            }
        }

        if (styles != null) {
            for (String style : styles) {
                //noinspection RedundantCast
                root.addContent(/*don't remove the cast - needed for jdk1.5 compatibility*/(Content) Elements.createStyle(style));
            }
        }

        if (componentStates != null) {
            for (AjaxPortionData stateData : componentStates) {
                stateData.addToElement(root, TYPE_UPDATE_STATE);
            }
        }

        Document responseDocument = new Document(root);
        OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
            new XMLOutputter().output(responseDocument, outputStream);
        } catch (IllegalStateException e) {
            // If the print writer is already in use,
            // then the exception will be thrown during the try to write to the output stream
            // If the exception was thrown - so we need to try to use the print writer
            outputStream = new ServletStreamWriter(((PrintWriter) response.getWriter()), response.getCharacterEncoding());
            new XMLOutputter().output(responseDocument, outputStream);
        }
    }

    private void writeAsText(AbstractResponseFacade response) throws IOException {
        StringBuilder buf = new StringBuilder(TEXT_RESPONSE_PREFIX + "{");


        buf.append(TAG_AJAX_UPDATABLE + " : [");
        boolean commaNeeded = false;
        if (simpleUpdate != null) {
            for (AjaxPortionData simpleUpdate : this.simpleUpdate) {
                simpleUpdate.addToBuffer(buf, TYPE_UPDATE_SIMPLE);
            }
            commaNeeded = true;
        }

        if (portions != null) {
            for (AjaxPortionData ajaxPortionData : portions) {
                appendCommaIfNeeded(buf, commaNeeded);
                ajaxPortionData.addToBuffer(buf, TYPE_UPDATE_PORTION);
                commaNeeded = true;
            }
        }

        if (initLibraryName != null) {
            appendCommaIfNeeded(buf, commaNeeded);
            new AjaxPortionData(null, initLibraryName, null, null).addToBuffer(buf, TYPE_UPDATE_INIT_SCRIPTS);
            commaNeeded = true;
        }

        if (componentStates != null) {
            for (AjaxPortionData stateData : componentStates) {
                appendCommaIfNeeded(buf, commaNeeded);
                stateData.addToBuffer(buf, TYPE_UPDATE_STATE);
            }
        }

        buf.append("]");

        if (jsLibraries != null) {
            appendCommaIfNeeded(buf, buf.length() > 1);
            addStringsToBuf(buf, TAG_AJAX_SCRIPT, jsLibraries);
        }

        if (cssFiles != null) {
            appendCommaIfNeeded(buf, buf.length() > 1);
            addStringsToBuf(buf, TAG_AJAX_CSS, cssFiles);
        }

        if (styles != null) {
            appendCommaIfNeeded(buf, buf.length() > 1);
            addStringsToBuf(buf, TAG_AJAX_STYLE, styles);
        }

        if (sessionExpired.length() > 0) {
            appendCommaIfNeeded(buf, buf.length() > 1);
            List<String> sessionExpiredParams = new ArrayList<String>();
            sessionExpiredParams.add(sessionExpired);
            addStringsToBuf(buf, TAG_AJAX_SESSION_EXPIRED, sessionExpiredParams);
        }

        if (sessionExpiredLocation.length() > 0) {
            appendCommaIfNeeded(buf, buf.length() > 1);
            List<String> sessionExpiredParams = new ArrayList<String>();
            sessionExpiredParams.add(sessionExpiredLocation);
            addStringsToBuf(buf, TAG_AJAX_SESSION_EXPIRED_LOCATION, sessionExpiredParams);
        }

        if (exception != null) {
            appendCommaIfNeeded(buf, buf.length() > 1);
            List<String> exception = new ArrayList<String>();
            exception.add(this.exception.getClass().getName());
            addStringsToBuf(buf, TAG_AJAX_EXCEPTION, exception);

            appendCommaIfNeeded(buf, buf.length() > 1);
            List<String> exceptionMessage = new ArrayList<String>();
            exceptionMessage.add(this.exception.getMessage());
            addStringsToBuf(buf, TAG_AJAX_EXCEPTION_MESSAGE, exceptionMessage);
        }

        buf.append("}" + TEXT_RESPONSE_SUFFIX);
        Writer writer = response.getWriter();
        writer.write(buf.toString());

    }

    private void addStringsToBuf(StringBuilder buf, String collectionName, List<String> strings) {
        buf.append(collectionName).append(" : [");
        for (int i = 0; i < strings.size(); i++) {
            String str = strings.get(i);
            if (i > 0)
                buf.append(',');
            String escapedStr = Rendering.wrapTextIntoJsString(str);
            buf.append("{value:").append(escapedStr).append("}");
        }
        buf.append("]");
    }

    private void appendCommaIfNeeded(StringBuilder buf, boolean commaNeeded) {
        if (commaNeeded)
            buf.append(", ");
    }


    public void addPortion(String portionId, String portionHtml, String portionScripts, JSONObject responseData) {
        if (portions == null)
            portions = new ArrayList<AjaxPortionData>();
        portions.add(new AjaxPortionData(portionId, portionHtml, portionScripts, responseData));
    }

    public void setInitLibraryName(String uniqueRTLibraryName) {
        initLibraryName = uniqueRTLibraryName;
    }

    public void addJsLibrary(String jsLibrary) {
        if (jsLibraries == null)
            jsLibraries = new ArrayList<String>();
        jsLibraries.add(jsLibrary);
    }

    public void addCssFile(String cssFile) {
        if (cssFiles == null)
            cssFiles = new ArrayList<String>();
        cssFiles.add(cssFile);
    }

    public void addStyle(String style) {
        if (styles == null)
            styles = new ArrayList<String>();
        styles.add(style);
    }

    public void addComponentState(String clientId, String stateString) {
        if (componentStates == null) componentStates = new ArrayList<AjaxPortionData>();
        componentStates.add(new AjaxPortionData(clientId, stateString, null, null));
    }

    public void setSessoinExpired(String sessionExpired) {
        this.sessionExpired = sessionExpired == null ? "" : sessionExpired;
    }

    public void setSessoinExpiredLocation(String sessionExpiredLocation) {
        this.sessionExpiredLocation = sessionExpiredLocation == null ? "" : sessionExpiredLocation;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    private static class Elements {

        private static Element createUpdatable(
                String id,
                String type,
                String value,
                String scripts,
                JSONObject data) {
            Element elem = new Element(TAG_AJAX_UPDATABLE);
            if (id != null)
                elem.setAttribute("id", id);
            elem.setAttribute("type", type);
            elem.setAttribute("value", value);
            if (data != null)
                elem.setAttribute("data", data.toString());
            if (scripts != null)
                elem.setAttribute("scripts", scripts);
            return elem;
        }

        private static Element createScript(String value) {
            Element elem = new Element(TAG_AJAX_SCRIPT);
            elem.setAttribute("value", value);
            return elem;
        }

        private static Element createStyle(String value) {
            Element elem = new Element(TAG_AJAX_STYLE);
            elem.setAttribute("value", value);
            return elem;
        }

        private static Element createCSSFile(String value) {
            Element elem = new Element(TAG_AJAX_CSS);
            elem.setAttribute("value", value);
            return elem;
        }

    }

    private static class AjaxPortionData {
        private final String id;
        private final String html;
        private String scripts;
        private final JSONObject responseData;

        public AjaxPortionData(String id, String html, String scripts, JSONObject responseData) {
            this.id = id;
            this.html = replaceNamedEntitiesIfNeeded(html);
            this.scripts = scripts;
            this.responseData = responseData;
        }

        public String getId() {
            return id;
        }

        public String getHtml() {
            return html;
        }

        public String getScripts() {
            return scripts;
        }

        public void setScripts(String scripts) {
            this.scripts = scripts;
        }

        public JSONObject getResponseData() {
            return responseData;
        }

        public void addToElement(Element root, String updateType) {
            //noinspection RedundantCast
            root.addContent(/*don't remove the cast - needed for jdk1.5 compatibility*/(Content) Elements.createUpdatable(
                    id, updateType, html, scripts, responseData));

        }

        public void addToBuffer(StringBuilder buf, String updateType) {
            JSONObject contents = new JSONObject();
            try {
                contents.put("type", updateType);
                contents.put("id", id);
                contents.put("value", html);
                contents.put("scripts", scripts);
                contents.put("data", responseData);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            buf.append(contents);
        }

        private static String replaceNamedEntitiesIfNeeded(String text) { // fix for JSFC-2976, fix for "NO_MODIFICATION_ALLOWED_ERR: Dom Exception 7" under Safari/Chrome
            FacesContext context = FacesContext.getCurrentInstance();
            if (!Environment.isMozillaXhtmlPlusXmlContentType(context) && !(Environment.isSafari() || Environment.isChrome()))
                return text;
            if (!text.contains("&"))
                return text;
            StringBuilder buf = new StringBuilder(text);
            int charsProcessed = 0;
            while (charsProcessed < buf.length()) {
                int nextEntityStart = buf.indexOf("&", charsProcessed);
                if (nextEntityStart == -1)
                    break;
                int nextEntityEnd = buf.indexOf(";", nextEntityStart);
                if (nextEntityEnd == -1)
                    break;
                String entityName = buf.substring(nextEntityStart + 1, nextEntityEnd);
                Integer entityCode = HTML.HTML_SPECIFIC_ENTITIES_TO_CODES.get(entityName);
                if (entityCode == null) {
                    charsProcessed = nextEntityEnd + 1;
                    continue;
                }
                if (entityCode.equals(HTML.HTML_SPECIFIC_ENTITIES_TO_CODES.get("amp"))) {
                    String entityStr = "#" + entityCode + ";#" + entityCode; // special case for ampersands to address Safari/Chrome issue
                    buf.replace(nextEntityStart + 1, nextEntityEnd, entityStr);
                    charsProcessed = nextEntityStart + 1 + entityStr.length() + 1;
                    continue;
                }
                String numericEntityCounterpart = "#" + entityCode;

                buf.replace(nextEntityStart + 1, nextEntityEnd, numericEntityCounterpart);
                charsProcessed = nextEntityStart + 1 + numericEntityCounterpart.length() + 1;
            }
            return buf.toString();
        }
    }

}
