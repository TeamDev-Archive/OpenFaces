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
package org.openfaces.renderkit.input.fileattachments;

import org.openfaces.component.OUIClientAction;
import org.openfaces.component.input.fileattachments.AbstractFileAttachmentAction;
import org.openfaces.component.input.fileattachments.FileAttachment;
import org.openfaces.component.input.fileattachments.FileAttachments;
import org.openfaces.component.input.MultipleFileUpload;
import org.openfaces.component.table.BaseColumn;
import org.openfaces.event.FileAttachedEvent;
import org.openfaces.event.FileRemovedEvent;
import org.openfaces.event.FileUploadItem;
import org.openfaces.event.UploadCompletionEvent;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.AjaxPortionRenderer;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.renderkit.table.TableFooter;
import org.openfaces.renderkit.table.TableHeader;
import org.openfaces.renderkit.table.TableStructure;
import org.openfaces.util.AjaxUtil;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.Script;
import org.openfaces.util.ScriptBuilder;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class FileAttachmentsRenderer extends RendererBase implements AjaxPortionRenderer {
    public static final String REQ_FILE_ATTACHMENT_KEY = "fileAttachment";
    public static final String REQ_FILE_ATTACHMENTS_COMP_KEY = "fileAttachmentsComponent";
    public static final String ATTACHMENTS_LIST_ID = "::attachmentsList";

    public static final String F_FILE_UPLOAD_COMP = "fileUpload";
    public static final String JS_SCRIPT_URL = "input/fileattachments.js";

    private ChildData childData;
    private MultipleFileUpload fileUploadFacet;


    //ajax params
    private static final String A_P_ID_OF_ATTACHMENT = "attachmentId";

    private static final String A_P_CALL_REMOVE_LISTENER = "callRemoveListener";


    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        AjaxUtil.prepareComponentForAjax(context, component);

        FileAttachments fileAttachments = (FileAttachments) component;
        renderStartTagOfComponent(context, fileAttachments);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        FileAttachments fileAttachments = (FileAttachments) component;
        final String clientId = fileAttachments.getClientId(context);
        ChildData childData = getChildData(fileAttachments);
        writer.startElement("table", fileAttachments);
        writer.writeAttribute("id", clientId + ATTACHMENTS_LIST_ID, null);

        TableStructure tableStructure = childData.getTableStructure();
        TableHeader tableHeader = tableStructure.getHeader();
        if (tableHeader.isContentSpecified()) {
            tableHeader.render(context, null);
        }
        writer.startElement("tbody", fileAttachments);
        renderRows(context, fileAttachments, childData, fileAttachments.getValue());
        writer.endElement("tbody");

        TableFooter tableFooter = tableStructure.getFooter();
        if (tableFooter.isContentSpecified()) {
            tableFooter.render(context, null);
        }
        writer.endElement("table");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        FileAttachments fileAttachments = (FileAttachments) component;
        final ResponseWriter writer = context.getResponseWriter();
        writeAttachButton(context, fileAttachments, writer);
        encodeScriptAndStyles(context, fileAttachments);
        writer.endElement("div");
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
    }

    public JSONObject encodeAjaxPortion(FacesContext context, UIComponent component, String portionName, JSONObject jsonParam) throws IOException, JSONException {
        FileAttachments fileAttachments = (FileAttachments) component;
        JSONObject jsonObj = new JSONObject();
        if (jsonParam.has(A_P_CALL_REMOVE_LISTENER)) {
            if (jsonParam.has(A_P_ID_OF_ATTACHMENT)) {
                String id = (String) jsonParam.get(A_P_ID_OF_ATTACHMENT);
                MethodExpression fileRemovedListener = fileAttachments.getFileRemovedListener();

                if (fileRemovedListener != null) {
                    final FileAttachment attachment = getAttachmentFromListWithId(fileAttachments.getValue(), id);
                    fileRemovedListener.invoke(
                            context.getELContext(), new Object[]{
                            new FileRemovedEvent(fileAttachments, attachment)});
                }
                Rendering.addJsonParam(jsonObj, "isCalled", true);
            }
        }
        return jsonObj;
    }

    private void writeAttachButton(FacesContext context, FileAttachments fileAttachments, ResponseWriter writer) throws IOException {
        writer.startElement("div", fileAttachments);
        fileUploadFacet = (MultipleFileUpload) fileAttachments.getFacet(F_FILE_UPLOAD_COMP);

        if (fileUploadFacet == null) {
            //fileUploadFacet = new MultipleFileUpload();
            throw new IllegalStateException("Facet '" + F_FILE_UPLOAD_COMP + "'  is not assigned. " +
                    "MultipleFileUpload component is not specified.");
        }
        setupFileUpload(context, fileAttachments, fileUploadFacet);
        fileUploadFacet.encodeAll(context);
        writer.endElement("div");
    }

    private MultipleFileUpload setupFileUpload(final FacesContext context, final FileAttachments fileAttachments, MultipleFileUpload fileUpload) {
        fileUpload.setAutoUpload(true);
        fileUpload.setBrowseButtonText("Attach file...");
        fileUpload.setOnend("O$.FileAttachments._fileUploadEndEventHandler('" + fileAttachments.getClientId(context) + "','" +
                fileUpload.getClientId(context) + "')");
        fileUpload.setCompletionListener(new MethodExpression() {
            @Override
            public MethodInfo getMethodInfo(ELContext elContext) {
                return null;
            }

            @Override
            public Object invoke(ELContext elContext, Object[] objects) {
                UploadCompletionEvent completionEvent = (UploadCompletionEvent) objects[0];
                final List<FileUploadItem> files = completionEvent.getFiles();
                for (FileUploadItem fileUploadItem : files) {
                    final FileAttachedEvent fileAttachedEvent = new FileAttachedEvent(fileAttachments,
                            new FileAttachment(fileUploadItem.getFile().getName(), fileUploadItem.getFileName(), fileUploadItem.getFile().length()));

                    fileAttachments.getFileAttachedListener().invoke(elContext, new Object[]{fileAttachedEvent});
                }
                return null;  //void
            }

            @Override
            public String getExpressionString() {
                return null;
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public boolean isLiteralText() {
                return false;
            }
        });
        return fileUpload;
    }

    private void renderStartTagOfComponent(FacesContext context, FileAttachments fileAttachments) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", fileAttachments);
        writeIdAttribute(context, fileAttachments);
        fileAttachments.setImmutableClientId(fileAttachments.getClientId(context));
        Rendering.writeStandardEvents(writer, fileAttachments);
        Rendering.writeStyleAndClassAttributes(writer, fileAttachments.getStyle(), fileAttachments.getStyleClass(), null);
    }

    private static FileAttachment getAttachmentFromListWithId(List<FileAttachment> list, String id) {
        for (FileAttachment attachment : list) {
            if (attachment.getId().equals(id)) {
                return attachment;
            }
        }
        return null;
    }


    private ChildData getChildData(FileAttachments fileAttachmentsComponent) {
        if (childData != null)
            return childData;
        List<UIComponent> fileAttachmentsChildren = fileAttachmentsComponent.getChildren();
        List<UIComponent> childComponents = new ArrayList<UIComponent>(fileAttachmentsChildren.size());
        for (UIComponent component : fileAttachmentsChildren) {
            if (!(component instanceof BaseColumn) &&
                    !(component instanceof OUIClientAction)) {
                childComponents.add(component);
            } else if (component instanceof AbstractFileAttachmentAction) {
                AbstractFileAttachmentAction fileAttachmentAction = (AbstractFileAttachmentAction) component;
                fileAttachmentAction.setFileAttachmentsComponent(fileAttachmentsComponent);
                childComponents.add(component.getParent());
            }
        }

        FileAttachmentsTableStyles tableStyles = new FileAttachmentsTableStyles(fileAttachmentsComponent, childComponents);
        TableStructure tableStructure = new TableStructure(fileAttachmentsComponent, tableStyles);

        childData = new ChildData(tableStructure, childComponents);
        return childData;
    }

    private static class ChildData {
        TableStructure tableStructure;
        private List<UIComponent> childComponents;

        public ChildData(TableStructure tableStructure, List<UIComponent> children) {
            this.tableStructure = tableStructure;
            childComponents = children;
        }

        public TableStructure getTableStructure() {
            return tableStructure;
        }

        public List<BaseColumn> getColumns() {
            return getTableStructure().getColumns();
        }

        public List<UIComponent> getChildComponents() {
            return childComponents;
        }
    }

    private void renderRows(
            FacesContext context,
            FileAttachments fileAttachments,
            ChildData childData,
            Collection<FileAttachment> items) throws IOException {
        List<BaseColumn> columns = childData.getColumns();
        ResponseWriter writer = context.getResponseWriter();

        String var = fileAttachments.getVar();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(REQ_FILE_ATTACHMENTS_COMP_KEY, fileAttachments);
        String itemIdPrefix = fileAttachments.getClientId(context) + ATTACHMENTS_LIST_ID + ":";//ITEM_PREFIX
        int index = 0;
        for (FileAttachment item : items) {
            requestMap.put(REQ_FILE_ATTACHMENT_KEY, item);
            Object oldVarValue = null;
            if (var != null)
                oldVarValue = requestMap.put(var, item);
            writer.startElement("tr", fileAttachments);
            writer.writeAttribute("id", itemIdPrefix + index, null);
            //fileAttachments.setObjectId(String.valueOf(index));
            for (BaseColumn column : columns) {
                resetChildrenIds(column);
                //setCorrectIdsForChildrenComponents(column, String.valueOf(index));
                writer.startElement("td", fileAttachments);
                Rendering.renderChildren(context, column);
                writer.endElement("td");
            }
            if (var != null)
                requestMap.put(var, oldVarValue);
            index++;
            writer.endElement("tr");
        }
        fileAttachments.setObjectId(null);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private void encodeScriptAndStyles(FacesContext context, FileAttachments fileAttachments) throws IOException {

        Script initScript = new ScriptBuilder().initScript(context, fileAttachments, "O$.FileAttachments._init");

        Rendering.renderInitScript(context, initScript,
                Resources.utilJsURL(context),
                Resources.internalURL(context, JS_SCRIPT_URL)
        );
    }

    private static void resetChildrenIds(UIComponent parent){
        for (Iterator<UIComponent> iterator = parent.getFacetsAndChildren(); iterator.hasNext();) {
            UIComponent component = iterator.next();
            component.setId(component.getId() + "0"); // schedule clientId for recalculation (see spec 3.1.6)
            resetChildrenIds(component);
        }
    }

}
