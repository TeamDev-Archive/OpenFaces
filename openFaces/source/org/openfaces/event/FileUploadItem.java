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

package org.openfaces.event;

import java.io.File;

/**
 * @author: Andrey Loboda
 * @date : 28.12.11
 */
public class FileUploadItem {
    private final String fileName;
    private final File file;
    private final Status status;

    public FileUploadItem(String fileName, File file, Status status) {
        this.file = file;
        this.fileName = fileName;
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public Status getStatus() {
        return status;
    }
}
