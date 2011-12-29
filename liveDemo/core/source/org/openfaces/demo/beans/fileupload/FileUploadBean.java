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

package org.openfaces.demo.beans.fileupload;

import org.openfaces.event.FileUploadedEvent;
import org.openfaces.event.UploadCompletionEvent;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileUploadBean implements Serializable {
    private String addButtonLabel;
    private String uploadButtonLabel;
    private List<File> files;

    public FileUploadBean() {
        addButtonLabel = "Choose me!";
        uploadButtonLabel = "Upload files";
    }


    public String getAddButtonLabel() {
        return addButtonLabel;
    }

    public void setAddButtonLabel(String addButtonLabel) {
        this.addButtonLabel = addButtonLabel;
    }

    public String getUploadButtonLabel() {
        return uploadButtonLabel;
    }

    public void setUploadButtonLabel(String uploadButtonLabel) {
        this.uploadButtonLabel = uploadButtonLabel;
    }

    /*public void fileUploaded(FileUploadedEvent e) {
        if (files == null || files.isEmpty()) {
            files = new ArrayList<File>();
        }
        files.add(e.getUploadedFile());
        return;
    }*/

    public void allFilesUploaded(UploadCompletionEvent e) {
        //files.add(e.getUploadedFile());
        System.out.println("Uploaded files : " + e.getFiles().size());
        return;
    }
}
