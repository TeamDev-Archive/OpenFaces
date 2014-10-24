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

package org.openfaces.demo.beans.foldingpanel;

import java.util.Date;

public class Photo {
    private String name;
    private String author;
    private Date date;
    private String imageFileName;

    public Photo(String name, String author, Date date, String imageFileName) {
        this.name = name;
        this.author = author;
        this.date = date;
        this.imageFileName = imageFileName;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getImageFileName() {
        return imageFileName;
    }
}
