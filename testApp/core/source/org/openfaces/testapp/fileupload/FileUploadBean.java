/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.fileupload;

import org.openfaces.event.FileUploadItem;
import org.openfaces.event.UploadCompletionEvent;
import org.openfaces.util.Faces;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vladislav Lubenskiy
 */
public class FileUploadBean implements Serializable {
    public String fileName = "";

    public FileUploadBean() {
    }

    public void uploadComplete(UploadCompletionEvent e) {
        List<FileUploadItem> files = e.getFiles();
        for (FileUploadItem file : e.getFiles()) {
            fileName += file.getFileName();
        }
        deleteFiles(files);
    }
    public void singleFileUploaded(UploadCompletionEvent e) {
        FileUploadItem fileUploadItem = e.getFiles().get(0);
        fileName = fileUploadItem.getFileName();
        File file = fileUploadItem.getFile();
        if (file != null) {
            file.delete();
        }
    }

//    public void uploadComplete(UploadCompletionEvent e) {
//        List<FileUploadItem> files = e.getFiles();
//        uploadedFiles.addAll(files);
//        for (FileUploadItem item : files) {
//            if (item.getFile() == null) {
//                continue;
//            } else if (!fileSizes.containsKey(item.getFile().getName())) {
//                fileSizes.put(item.getFile().getName(), item.getFile().length());
//            }
//        }
//        deleteFiles(files);
//    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void deleteFiles(List<FileUploadItem> files) {
        for (FileUploadItem fileUploadItem : files) {
            File file = fileUploadItem.getFile();
            // delete the file to save disk space on the server running the demo
            if (file != null)
                file.delete();
        }
    }

}
