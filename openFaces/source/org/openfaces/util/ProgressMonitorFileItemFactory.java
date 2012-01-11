/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.ref.WeakReference;

public class ProgressMonitorFileItemFactory extends DiskFileItemFactory {

    private File temporaryDirectory;
    private WeakReference<HttpServletRequest> requestRef;
    private long requestLength;
    private static final String PROGRESS_ID = "progress_";
    private static final String EXCEED_MAX_SIZE_ID = "exceedMaxSize_";
    private long maxSizeOfFile;

    public ProgressMonitorFileItemFactory(HttpServletRequest request, long maxSizeOfFile) {
        super();
        temporaryDirectory = (File) request.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
        requestRef = new WeakReference<HttpServletRequest>(request);
        this.maxSizeOfFile = maxSizeOfFile;

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
            observer = new SessionUpdatingProgressObserver(fileName);
            if (requestLength > maxSizeOfFile) {
                shouldProcess = false;
                HttpServletRequest request = requestRef.get();
                request.getSession().setAttribute(EXCEED_MAX_SIZE_ID + fileName, true);
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

        private final String fileName;


        public SessionUpdatingProgressObserver(String fileName) {

            this.fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
        }

        public void setProgress(int progress) {
            HttpServletRequest request = requestRef.get();
            if (request != null) {
                request.getSession().setAttribute(PROGRESS_ID + fileName, progress);
            }
        }
    }
}

