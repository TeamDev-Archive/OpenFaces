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

package org.openfaces.event;

import org.apache.commons.io.IOUtils;
import org.openfaces.component.input.fileattachments.FileAttachment;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;


public class FileDownloadEvent extends FacesEvent {
    private final FileAttachment fileAttachment;
    private String contentType;

    public FileDownloadEvent(UIComponent component, FileAttachment fileAttachment) {
        super(component);
        if (component == null) throw new IllegalArgumentException("Component " + component + " is null");
        this.fileAttachment = fileAttachment;

    }


    public FileAttachment getFileAttachment() {
        return fileAttachment;
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof FileDownloadListener;
    }

    @Override
    public void processListener(FacesListener listener) {
        ((FileDownloadListener) listener).process(this);
    }

    public void send(InputStream inputStream) throws IOException {
        sendResponse(inputStream);
    }

    public void send(Reader reader) throws IOException {
        sendResponse(new ByteArrayInputStream(IOUtils.toByteArray(reader)));
    }

    public void send(byte[] bytesOfFile) throws IOException {
        sendResponse(new ByteArrayInputStream(bytesOfFile));
    }

    private void sendResponse(InputStream inputStream) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        writeFileContentToStream(inputStream, byteArrayOutputStream);
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType(getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + fileAttachment.getName());
        writeFileToResponse(byteArrayOutputStream, response);
        context.responseComplete();
    }

    private void writeFileToResponse(ByteArrayOutputStream byteArrayOutputStream, HttpServletResponse response) throws IOException {
        byte[] binaryFileContent = byteArrayOutputStream.toByteArray();
        OutputStream responseOutputStream;
        responseOutputStream = response.getOutputStream();
        responseOutputStream.write(binaryFileContent);
        responseOutputStream.close();
    }

    private void writeFileContentToStream(InputStream inputStream, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        int byteRead;
        while ((byteRead = bis.read()) != -1)
            byteArrayOutputStream.write(byteRead);

        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        bis.close();
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}