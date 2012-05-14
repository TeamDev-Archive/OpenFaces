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

import org.openfaces.component.input.fileattachments.DownloadAttachmentAction;
import org.openfaces.component.input.fileattachments.FileAttachments;
import org.openfaces.event.FileDownloadEvent;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

public final class DownloadAttachmentActionRenderer extends AbstractAttachmentActionRenderer {
    private static final String DOWNLOAD_KEY = "::download";

    @Override
    protected String getComponentName() {
        return "o:downloadAttachmentAction";
    }

    @Override
    protected String getScriptInitFunction() {
        return "O$.DownloadAttachment._init";
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        final DownloadAttachmentAction downloadAttachmentAction = (DownloadAttachmentAction) component;
        invokeFileDownloadListener(context, downloadAttachmentAction);
    }

    private void invokeFileDownloadListener(FacesContext context, DownloadAttachmentAction downloadAttachmentAction) {
        final FileAttachments fileAttachmentsComponent = downloadAttachmentAction.getFileAttachmentsComponent();
        if (fileAttachmentsComponent != null) {
            String key = fileAttachmentsComponent.getClientId(context) + DOWNLOAD_KEY;
            final ExternalContext externalContext = context.getExternalContext();
            final Map<String, String> requestParameterMap = externalContext.getRequestParameterMap();
            if (requestParameterMap.containsKey(key) && requestParameterMap.get(key).equals("true")) {
                MethodExpression fileDownloadListener = fileAttachmentsComponent.getFileDownloadListener();
                if (fileDownloadListener != null) {
                    fileDownloadListener.invoke(
                            context.getELContext(), new Object[]{
                            new FileDownloadEvent(fileAttachmentsComponent, downloadAttachmentAction.getFileAttachment())});
                }
            }
        }
    }

}
