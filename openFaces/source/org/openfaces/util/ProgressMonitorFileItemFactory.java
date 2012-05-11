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

package org.openfaces.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.openfaces.renderkit.input.AbstractFileUploadRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.lang.ref.WeakReference;

public class ProgressMonitorFileItemFactory extends DiskFileItemFactory {

    private File temporaryDirectory;
    private WeakReference<HttpServletRequest> requestRef;
    private long requestLength;
    private long maxSizeOfFile;
    private String uniqueFileId;

    public ProgressMonitorFileItemFactory(HttpServletRequest request, long maxSizeOfFile, String uniqueFileId) {
        super();
        temporaryDirectory = (File) request.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
        requestRef = new WeakReference<HttpServletRequest>(request);
        this.maxSizeOfFile = maxSizeOfFile;
        this.uniqueFileId = uniqueFileId;

        String contentLength = request.getHeader("content-length");
        if (contentLength != null) {
            requestLength = Long.parseLong(contentLength.trim());
        }
    }

    public FileItem createItem(String fieldName, String contentType,
                               boolean isFormField, String fileName) {
        SessionUpdatingProgressObserver observer = null;
        boolean shouldProcess = true;
        if (!isFormField && !fileName.equals("")) { //This must be a file upload and has a name
            observer = new SessionUpdatingProgressObserver(uniqueFileId);
            HttpServletRequest req = requestRef.get();
            if (req != null) {
                HttpSession session = req.getSession();
                session.setAttribute(AbstractFileUploadRenderer.FILE_SIZE_ID + uniqueFileId, requestLength);
                if (requestLength > maxSizeOfFile) {
                    shouldProcess = false;
                    session.setAttribute(AbstractFileUploadRenderer.EXCEED_MAX_SIZE_ID + uniqueFileId, true);
                }
            }
            //fileName = fileName.replaceAll("[#$%^&* ]+","_"); //doesn't work unfortunately
        }

        ProgressMonitorFileItem item = new ProgressMonitorFileItem(fieldName, contentType,
                isFormField, fileName,
                DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,
                temporaryDirectory,
                observer,
                requestLength, shouldProcess);
        return item;
    }

    public class SessionUpdatingProgressObserver implements ProgressObserver {

        private final String fileId;

        public SessionUpdatingProgressObserver(String fileId) {
            this.fileId = fileId;
        }

        public void setProgress(int progress) {
            HttpServletRequest request = requestRef.get();
            if (request != null) {
                request.getSession().setAttribute(AbstractFileUploadRenderer.PROGRESS_ID + fileId, progress);
            }
        }
    }
}

