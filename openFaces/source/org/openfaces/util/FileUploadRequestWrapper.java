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
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.openfaces.event.FileUploadItem;
import org.openfaces.event.FileUploadStatus;
import org.openfaces.renderkit.input.AbstractFileUploadRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class FileUploadRequestWrapper extends HttpServletRequestWrapper {

    private static final String FIELD_NAME = "::inputs::input";

    public FileUploadRequestWrapper(HttpServletRequest request, String tempDirPath, final long maxSizeOfFile, String uniqueFileId) {
        super(request);
        final String contentLength = request.getHeader("content-length");
        if (contentLength == null)
            return;

        try {
            ServletFileUpload upload = new ServletFileUpload();
            upload.setFileItemFactory(new ProgressMonitorFileItemFactory(request, maxSizeOfFile, uniqueFileId));
            List<FileItem> fileItems = upload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                if (!fileItem.isFormField()) {
                    if (fileItem.getSize() != 0) {
                        if (request.getSession().getAttribute(uniqueFileId + AbstractFileUploadRenderer.TERMINATED_TEXT) == null) {
                            String correctFileName = getCorrectFileName(fileItem.getName());
                            File f = writeFile(fileItem, tempDirPath, correctFileName);
                            int index = fileItem.getFieldName().indexOf(FIELD_NAME);
                            String genericNameForFile = fileItem.getFieldName().substring(0, index + FIELD_NAME.length());
                            request.setAttribute(genericNameForFile, new FileUploadItem(correctFileName, f, FileUploadStatus.SUCCESSFUL));
                            request.setAttribute("FILE_ID", uniqueFileId);
                        } else {
                            request.getSession().removeAttribute(uniqueFileId + AbstractFileUploadRenderer.TERMINATED_TEXT);
                        }
                    } else {
                        throw new RuntimeException("File size is equal 0 bytes");
                    }
                }
            }

        } catch (FileUploadException fe) {
            /*this exception can happened in case if something wrong with file or we stopped manually*/
            request.getSession().setAttribute(uniqueFileId + AbstractFileUploadRenderer.TERMINATED_TEXT, true);
        } catch (IOException ne) {
            /*this exception can happened in case if problem in writing file*/
            request.getSession().setAttribute(uniqueFileId + AbstractFileUploadRenderer.TERMINATED_TEXT, true);
        } catch (Exception e) {
            /*this exception can happened if on server some problem*/
            request.getSession().setAttribute(uniqueFileId + AbstractFileUploadRenderer.TERMINATED_TEXT, true);
        }
    }

    private File writeFile(FileItem fileItem, String tempDirPath, String correctFileName) throws IOException {
        File f = getAndChangeFileNameIfNeeded(tempDirPath, correctFileName);

        OutputStream out = new FileOutputStream(f);
        int read = 0;
        byte[] bytes = new byte[1024];
        InputStream inputStream = fileItem.getInputStream();
        while ((read = inputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        inputStream.close();
        out.flush();
        out.close();
        return f;
    }

    private File getAndChangeFileNameIfNeeded(String tempDirPath, String fileName) {
        File f = new File(tempDirPath + "\\" + fileName);
        int i = 0;
        while (f.isFile()) {
            f = new File(tempDirPath + "\\copy_" + i + "_" + fileName);
            i++;
        }
        return f;
    }
    private String getCorrectFileName(String notCorrectName) throws UnsupportedEncodingException{
        String correctFileName = new String(notCorrectName.getBytes(), "UTF-8"); // for cyrillic symbols in fileName
        int indexOfSlash = correctFileName.lastIndexOf("\\");
        return (indexOfSlash == -1) ? correctFileName : correctFileName.substring(indexOfSlash + 1);
    }

}