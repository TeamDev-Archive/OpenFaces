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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class FileUploadFilter {
    private static final String INIT_PARAM_TEMP_DIR = "org.openfaces.fileUpload.tempDir";
    private static final String COMPONENT_ID = "uniqueID";
    private static final String ID_OF_FILE = "idOfFile";

    private static String getTempDir(ServletContext servletContext) {
        String tempDirAttr = FileUploadFilter.class.getName() + "." + INIT_PARAM_TEMP_DIR;
        String tempDir = (String) servletContext.getAttribute(tempDirAttr);
        if (tempDir == null) {
            tempDir = servletContext.getInitParameter("org.openfaces.fileUpload.tempDir");
            if (tempDir == null) {
                tempDir = getStandardTempDir();
            }

            File tempDirFile = new File(tempDir);
            if (!tempDirFile.exists()) {
                if (!tempDirFile.mkdirs()) {
                    String tempDirFileCanonicalPath;
                    try {
                        tempDirFileCanonicalPath = tempDirFile.getCanonicalPath();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    throw createFilterRuntimeException("Cannot create temporary directory at the specified location: " +
                            tempDirFileCanonicalPath, null);
                }
            } else {
                if (tempDirFile.isFile()) {
                    String tempDirFileCanonicalPath;
                    try {
                        tempDirFileCanonicalPath = tempDirFile.getCanonicalPath();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    throw createFilterRuntimeException("Cannot create temporary directory at the specified location, because a" +
                            "file with the same name already exists: " + tempDirFileCanonicalPath, null);
                }
            }


            servletContext.setAttribute(tempDirAttr, tempDir);
        }
        return tempDir;
    }

    private static String getStandardTempDir() {
        String tempDir;
        File tempFile = null;
        try {
            tempFile = File.createTempFile("_openfaces_upload_temp", "tmp");

            File standardTempDir = tempFile.getParentFile();
            try {
                tempDir = standardTempDir.getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            String tempDirProperty = System.getProperty("java.io.tmpdir");
            String error = "OpenFaces FileUpload error: cannot create temporary file with " +
                    "File.createTempFile() method. System property java.io.tmpdir=" + tempDirProperty +
                    "; Consider specifying the " + INIT_PARAM_TEMP_DIR +
                    " context param in application's web.xml to specify temporary upload directory explicitly";
            throw createFilterRuntimeException(error, e);
        } finally {
            if (tempFile != null) tempFile.delete();
        }
        return tempDir;
    }

    private static RuntimeException createFilterRuntimeException(String message, IOException e) {
        System.err.println(message);
        if (e != null)
            return new RuntimeException(message, e);
        else
            return new RuntimeException(message);
    }

    public static void processMultipartRequest(ServletRequest request, ServletContext servletContext)
            throws IOException, ServletException {

        HttpServletRequest hRequest = (HttpServletRequest) request;

        //Check whether we're dealing with a multipart request
        boolean isMultipart = (hRequest.getHeader("content-type") != null &&
                hRequest.getHeader("content-type").contains("multipart/form-data"));

        if (isMultipart) {//if multipart request
            String compID = request.getParameter(COMPONENT_ID);
            if (compID != null) {//if our component is using
                String tempDirStr = getTempDir(servletContext);
                Long maxSizeOfFile = (Long) ((HttpServletRequest) request).getSession().getAttribute(compID);
                if (maxSizeOfFile != null) {
                    String idOfFile = request.getParameter(ID_OF_FILE);
                    new FileUploadRequestWrapper(hRequest, tempDirStr, maxSizeOfFile, compID + idOfFile);
                    request.setAttribute("fileUploadRequest", true);
                }
            }
        }
    }
}