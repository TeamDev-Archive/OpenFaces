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

package org.openfaces.testapp.fileattachments;

import org.openfaces.component.input.fileattachments.FileAttachment;
import org.openfaces.event.FileAttachedEvent;
import org.openfaces.event.FileDownloadEvent;
import org.openfaces.event.FileRemovedEvent;
import org.openfaces.util.Log;

import javax.faces.context.FacesContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andrii.loboda
 */
public class FileAttachmentsBean {
    private List<FileAttachment> fileAttachments;

    public FileAttachmentsBean() {
        fileAttachments = new ArrayList<FileAttachment>();
//        fileAttachments.add(new FileAttachment("number one", 35800));
//        fileAttachments.add(new FileAttachment("number two", 65450));
//        fileAttachments.add(new FileAttachment("number three", 75400));
    }

    public void downloadListener(FileDownloadEvent e) {
        if (false) {
            try {
                final String tempDir = File.createTempFile("_openfaces_upload_temp", "tmp").getParentFile().getCanonicalPath();
                File f = new File(tempDir + "\\" + e.getFileAttachment().getId());
                final FileInputStream inputStream = new FileInputStream(f);
                e.send(inputStream);
            } catch (FileNotFoundException exception) {
                Log.log(FacesContext.getCurrentInstance(), "file not found", exception);
            } catch (IOException exception) {
                Log.log(FacesContext.getCurrentInstance(), "IOException", exception);
            }
            Log.log("downloadListener(FileDownloadEvent e) has been called");
        }
    }

    public void removeListener(FileRemovedEvent e) {
        fileAttachments.remove(e.getFileAttachment());
        e.getFileAttachment();
    }

    public void attachedListener(FileAttachedEvent e) {
        fileAttachments.add(e.getFileAttachment());
    }

    public List<FileAttachment> getFileAttachments() {
        return fileAttachments;
    }

    public void setFileAttachments(List<FileAttachment> fileAttachments) {
        this.fileAttachments = fileAttachments;
    }
}
