/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.fileUpload;

import org.openfaces.event.FileUploadItem;
import org.openfaces.event.UploadCompletionEvent;

import java.io.Serializable;

/**
 * @Author Vladislav Lubenskiy
 */
public class FileUploadBean implements Serializable {
    private String fileName;
    private String fileSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void fileUploaded(UploadCompletionEvent event) {
        FileUploadItem item = event.getFiles().get(0);

        fileName = item.getFileName();
        if (item.getFile() != null) {
            fileSize = String.valueOf(item.getFile().length());
            item.getFile().delete();
        } else
            fileSize = item.getStatus().toString();

    }
}
