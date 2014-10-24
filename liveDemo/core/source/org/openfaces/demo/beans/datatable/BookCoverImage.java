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

package org.openfaces.demo.beans.datatable;

import org.openfaces.event.FileUploadItem;

import java.io.Serializable;

/**
 * @author Vladislav Lubenskiy
 */
public class BookCoverImage implements Serializable {

    private FileUploadItem uploadedCoverImage;
    private long fileSize;

    public BookCoverImage() {
    }

    public FileUploadItem getUploadedCoverImage() {
        return uploadedCoverImage;
    }

    public void setUploadedCoverImage(FileUploadItem uploadedCoverImage) {
        this.uploadedCoverImage = uploadedCoverImage;
        if (uploadedCoverImage.getFile() == null) {
            return;
        } else {
            fileSize = uploadedCoverImage.getFile().length();
        }
    }

    public long getFileSize() {
        return fileSize;
    }
}
