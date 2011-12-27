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

package org.openfaces.demo.filter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.ref.WeakReference;

public class ProgressMonitorFileItemFactory extends DiskFileItemFactory {

    private File temporaryDirectory;
    private WeakReference<HttpServletRequest> requestRef;
    private long requestLength;
    private static final String PROGRESS_ID = "progress_";

    public ProgressMonitorFileItemFactory(HttpServletRequest request) {
        super();
        temporaryDirectory = (File) request.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
        requestRef = new WeakReference<HttpServletRequest>(request);

        String contentLength = request.getHeader("content-length");

        if (contentLength != null) {
            requestLength = Long.parseLong(contentLength.trim());
        }
    }

    public FileItem createItem(String fieldName, String contentType,
                               boolean isFormField, String fileName) {
        SessionUpdatingProgressObserver observer = null;

        if (!isFormField && !fileName.equals("")) { //This must be a file upload and has a name
            observer = new SessionUpdatingProgressObserver(fileName);
            observer.setProgress(0);

        }

        ProgressMonitorFileItem item = new ProgressMonitorFileItem(fieldName, contentType,
                isFormField, fileName,
                DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,
                temporaryDirectory,
                observer,
                requestLength);
        return item;
    }

    public class SessionUpdatingProgressObserver implements ProgressObserver {

        private final String fileName;


        public SessionUpdatingProgressObserver(String fileName) {

            this.fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
        }

        public void setProgress(int progress) {
            HttpServletRequest request = requestRef.get();
            if (request != null) {
                request.getSession().setAttribute(PROGRESS_ID + fileName, progress);
            }
        }
    }
}

