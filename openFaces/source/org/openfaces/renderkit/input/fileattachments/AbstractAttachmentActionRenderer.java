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

import org.openfaces.component.input.fileattachments.AbstractFileAttachmentAction;
import org.openfaces.component.input.fileattachments.FileAttachment;
import org.openfaces.component.input.fileattachments.FileAttachments;
import org.openfaces.renderkit.RendererBase;
import org.openfaces.util.Components;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author andrii.loboda
 */
public abstract class AbstractAttachmentActionRenderer extends RendererBase {
    private static final String JS_SCRIPT_URL = "input/fileattachments.js";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        AbstractFileAttachmentAction abstractFileAttachmentAction = (AbstractFileAttachmentAction) component;
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        final FileAttachment fileAttachment = (FileAttachment) requestMap.get(FileAttachmentsRenderer.REQ_FILE_ATTACHMENT_KEY);
        abstractFileAttachmentAction.setFileAttachment(fileAttachment);
        final FileAttachments fileAttachmentsComponent = (FileAttachments) requestMap.get(FileAttachmentsRenderer.REQ_FILE_ATTACHMENTS_COMP_KEY);
        abstractFileAttachmentAction.setFileAttachmentsComponent(fileAttachmentsComponent);
        encodeComponent(context, abstractFileAttachmentAction);
        encodeScriptAndStyles(context, abstractFileAttachmentAction);

    }

    private void encodeComponent(FacesContext context, AbstractFileAttachmentAction component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("style", "display:none", null);
        writeIdAttribute(context, component);
        writer.endElement("div");
    }


    private void encodeScriptAndStyles(FacesContext context, AbstractFileAttachmentAction component) throws IOException {
        String invokerId;
        String aFor = component.getFor();
        if (aFor != null)
            invokerId = Components.referenceIdToClientId(context, component, aFor);
        else if (!component.isStandalone())
            invokerId = component.getParent().getClientId(context);
        else
            invokerId = null;

        ScriptBuilder sb = new ScriptBuilder();
        final FileAttachments attachmentsComponent = component.getFileAttachmentsComponent();
        sb.initScript(context, component, getScriptInitFunction(),
                invokerId,
                Rendering.getEventWithOnPrefix(context, component, getComponentName()),
                attachmentsComponent.getImmutableClientId(),
                component.getFileAttachment().getId()
        );

        Rendering.renderInitScript(context, sb, Resources.internalURL(context, AbstractAttachmentActionRenderer.JS_SCRIPT_URL));
    }

    protected abstract String getComponentName();

    protected abstract String getScriptInitFunction();


    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
    }
}
